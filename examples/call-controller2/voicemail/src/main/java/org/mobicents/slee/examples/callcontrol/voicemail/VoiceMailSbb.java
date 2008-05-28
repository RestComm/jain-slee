/***************************************************
 *                                                 *
 *  Mobicents: The Open Source VoIP Platform       *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/
package org.mobicents.slee.examples.callcontrol.voicemail;

import java.net.URL;
import java.text.ParseException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sip.ClientTransaction;
import javax.sip.Dialog;
import javax.sip.InvalidArgumentException;
import javax.sip.RequestEvent;
import javax.sip.ServerTransaction;
import javax.sip.SipException;
import javax.sip.SipProvider;
import javax.sip.Transaction;
import javax.sip.TransactionUnavailableException;
import javax.sip.address.SipURI;
import javax.sip.address.URI;
import javax.sip.header.ContactHeader;
import javax.sip.header.ContentTypeHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.header.ToHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;
import javax.slee.ActivityContextInterface;
import javax.slee.Address;
import javax.slee.AddressPlan;
import javax.slee.FactoryException;
import javax.slee.SLEEException;
import javax.slee.SbbContext;
import javax.slee.SbbLocalObject;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.UnrecognizedActivityException;
import javax.slee.facilities.TimerEvent;
import javax.slee.facilities.TimerFacility;
import javax.slee.facilities.TimerID;

import org.mobicents.media.server.impl.common.events.EventCause;
import org.mobicents.media.server.impl.common.events.EventID;
import org.mobicents.mscontrol.MsConnection;
import org.mobicents.mscontrol.MsConnectionEvent;
import org.mobicents.mscontrol.MsNotifyEvent;
import org.mobicents.mscontrol.MsProvider;
import org.mobicents.mscontrol.MsSession;
import org.mobicents.mscontrol.MsSignalDetector;
import org.mobicents.mscontrol.MsSignalGenerator;
import org.mobicents.slee.examples.callcontrol.common.SubscriptionProfileSbb;
import org.mobicents.slee.examples.callcontrol.profile.CallControlProfileCMP;
import org.mobicents.slee.resource.media.ratype.MediaRaActivityContextInterfaceFactory;
import org.mobicents.slee.resource.sip.SipActivityContextInterfaceFactory;

/**
 * Voice Mail service logic using SIP RA with dialog support and Media RA.
 * 
 * @author torosvi
 * @author baranowb
 * @author iivanov
 * 
 */
public abstract class VoiceMailSbb extends SubscriptionProfileSbb implements
		javax.slee.Sbb {

	public void onInvite(javax.sip.RequestEvent event,
			VoiceMailSbbActivityContextInterface localAci) {
		Response response;
		log.info("########## VOICE MAIL SBB: INVITE ##########");
		// Request
		Request request = event.getRequest();
		// Setting Request
		this.setInviteRequest(request);

		// Server Transaction
		ServerTransaction st = event.getServerTransaction();
		// Setting Server Transaction
		this.setServerTransaction(st);

		try {
			localAci.detach(this.getSbbLocalObject());

			if (localAci.getFilteredByAncestor()) {
				log
						.info("########## VOICE MAIL SBB: FILTERED BY ANCESTOR ##########");
				return;
			}

			// if we are calling to vmail this means we want to check our mail
			// box
			// sameUser = true
			boolean sameUser = sameUser(event);
			URI uri;

			if (sameUser) {
				// The user is the caller
				FromHeader fromHeader = (FromHeader) request
						.getHeader(FromHeader.NAME);
				uri = fromHeader.getAddress().getURI();
			} else {
				// The user is the callee - we are calling someone else
				ToHeader toHeader = (ToHeader) request.getHeader(ToHeader.NAME);
				uri = toHeader.getAddress().getURI();
			}
			// In the Profile Table the port is not used
			((SipURI) uri).removePort();

			// Responding to the user
			// To know whether the user has the Voice mail service enabled
			boolean isSubscriber = isSubscriber(uri.toString());

			if (isSubscriber) {
				// Voice Mail service enabled
				String fileRoute = null;

				// Looking for the audio file to transmit
				Context initCtx = new InitialContext();
				Context myEnv = (Context) initCtx.lookup("java:comp/env");

				// check if the user is calling their own number to check voice
				// mail
				if (sameUser) {

				}
				// or someone else is calling the user to leave a voice message
				else {

					ToHeader toHeader = (ToHeader) request
							.getHeader(ToHeader.NAME);
					String fileName = ((SipURI) toHeader.getAddress().getURI())
							.getUser()
							+ WAV_EXT;

					// Setting File Route where recording the voice message
					String route = (String) myEnv.lookup("filesRoute");
					fileRoute = /*route + */fileName;
				}

				// SDP Description from the request
				String sdp = new String(request.getRawContent());

				// Creating Media Session
				MsSession mediaSession = msProvider.createSession();
				// Setting Media Session
				this.setMediaSession(mediaSession);
				MsConnection msConnection = mediaSession
						.createNetworkConnection(ENDPOINT_NAME);

				// Attaching session AC
				ActivityContextInterface msAci = null;
				try {
					msAci = msActivityFactory
							.getActivityContextInterface(msConnection);
					msAci.attach(this.getSbbLocalObject());
				} catch (Exception ex) {
					log.error("Internal server error", ex);
					getMessageFactory().createResponse(
							Response.SERVER_INTERNAL_ERROR, request);
					return;
				}

				// Attaching to SIP Dialog activity
				Dialog dial = getSipFactoryProvider().getSipProvider()
						.getNewDialog((Transaction) st);
				ActivityContextInterface dialogAci = sipACIF
						.getActivityContextInterface(dial);

				// attach this SBB object to the Dialog activity to receive
				// subsequent events on this Dialog
				dialogAci.attach(this.getSbbLocalObject());

				// Notify caller that we're TRYING to reach voice mail. Just a
				// formality, we know we can go further than TRYING at this
				// point
				response = getMessageFactory().createResponse(Response.TRYING,
						request);
				st.sendResponse(response);

				// RINGING. Another formality of the SIP protocol.
				response = getMessageFactory().createResponse(Response.RINGING,
						request);
				st.sendResponse(response);

				log.info("Creating RTP connection [" + ENDPOINT_NAME + "]");
				msConnection.modify("$", sdp);

			}
			// Voice Mail service disabled
			else {
				response = getMessageFactory().createResponse(
						Response.TEMPORARILY_UNAVAILABLE, request);
				log.info("########## NO VOICE MAIL AVAILABLE FOR USER: "
						+ uri.toString());
				st.sendResponse(response);
			}

		} catch (TransactionRequiredLocalException e) {
			log.error(e.getMessage(), e);
		} catch (SLEEException e) {
			log.error(e.getMessage(), e);
		} catch (ParseException e) {
			log.error(e.getMessage(), e);
		} catch (SipException e) {
			log.error(e.getMessage(), e);
		} catch (InvalidArgumentException e) {
			log.error(e.getMessage(), e);
		} catch (NullPointerException e) {
			log.error(e.getMessage(), e);
		} catch (NamingException e) {
			log.error(e.getMessage(), e);
		} catch (UnrecognizedActivityException e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * At any time a SIP Client can send a BYE Request. If the Voice Mail is
	 * being used it will be the VoicemailSbb the one that will send OK
	 * Response.
	 * 
	 * @param event
	 * @param aci
	 */
	public void onByeEvent(RequestEvent event, ActivityContextInterface aci) {
		log.info("########## VOICE MAIL SBB: BYE ##########");
		try {
			TimerID timerID = this.getTimerID();

			// If there is a Timer set we have to cancel it.
			if (timerID != null) {
				timerFacility.cancelTimer(timerID);
			}

			releaseMediaConnectionAndDialog();

			// Sending the OK Response to the BYE Request received.
			byeRequestOkResponse(event);

		} catch (FactoryException e) {
			log.error(e.getMessage(), e);
		} catch (NullPointerException e) {
			log.error(e.getMessage(), e);
		}
	}

	public void onTimerEvent(TimerEvent event, ActivityContextInterface aci) {
		// Timer Event is fired in 2 cases after waiting a number of secs:
		// * After being waiting to receive DTMF digit and do not receive any.
		// * After being recording audio from an UA without receiving a BYE
		// Request.
		// mediaSession = this.getMediaSession();
		//
		// if (this.getSameUser()) {
		// // In this case we were waiting DTMF digit.
		// mediaSession.stopSession();
		// // We have not received any DTMF digit, so we try it again.
		// URL audioFileURL = getClass().getResource(tryAgain);
		// mediaSession.createTransmitterReceiver(mediaSession
		// .getSdpDescription(), audioFileURL, null, true);
		// } else {
		// // In this case we were waiting a BYE Request.
		// mediaSession.stopSession();
		// // We have not received the BYE, so we send the Request to the UA.
		// sendByeRequest();
		// aci.detach(this.getSbbLocalObject());
		// }
	}

	public void onConnectionCreated(MsConnectionEvent evt,
			ActivityContextInterface aci) {
		MsConnection connection = evt.getConnection();
		log.info("Created RTP connection [" + connection.getEndpoint() + "]");

		MsConnection msConnection = evt.getConnection();
		String sdp = msConnection.getLocalDescriptor();

		ServerTransaction txn = getServerTransaction();
		if (txn == null) {
			log.error("SIP activity lost, close RTP connection");
			msConnection.release();
			return;
		}

		Request request = txn.getRequest();

		ContentTypeHeader contentType = null;
		try {
			contentType = getHeaderFactory().createContentTypeHeader(
					"application", "sdp");
		} catch (ParseException ex) {
		}

		String localAddress = getSipFactoryProvider().getSipProvider()
				.getListeningPoints()[0].getIPAddress();
		int localPort = getSipFactoryProvider().getSipProvider()
				.getListeningPoints()[0].getPort();

		javax.sip.address.Address contactAddress = null;
		try {
			contactAddress = getAddressFactory().createAddress(
					"sip:" + localAddress + ":" + localPort);
		} catch (ParseException ex) {
			log.error(ex.getMessage(), ex);
		}
		ContactHeader contact = getHeaderFactory().createContactHeader(
				contactAddress);

		Response response = null;
		try {
			response = getMessageFactory().createResponse(Response.OK, request,
					contentType, sdp.getBytes());
		} catch (ParseException ex) {
		}

		response.setHeader(contact);
		try {
			txn.sendResponse(response);
		} catch (InvalidArgumentException ex) {
			log.error(ex.getMessage(), ex);
		} catch (SipException ex) {
			log.error(ex.getMessage(), ex);
		}

		String endpointName = evt.getConnection().getEndpoint();
		this.setUserEndpoint(endpointName);

		if (getSameUser()) {

			log.debug("same user, lets play the voice mail");
			System.out.println("same user, lets play the voice mail");

			MsSignalGenerator generator = msProvider
					.getSignalGenerator(endpointName);

			try {
				ActivityContextInterface generatorActivity = msActivityFactory
						.getActivityContextInterface(generator);
				generatorActivity.attach(this.getSbbLocalObject());

				URL audioFileURL = getClass().getResource(waitingDTMF);

				generator.apply(EventID.PLAY, new String[] { audioFileURL
						.toString() });

				this.initDtmfDetector(evt.getConnection(), endpointName);

			} catch (UnrecognizedActivityException e) {
				e.printStackTrace();
			}

		} else {
			log.debug("not the same user, start recording after announcement");
			System.out
					.println("not the same user, start recording after announcement");

			URL audioFileURL = getClass().getResource(recordAfterTone);

			ToHeader toHeader = (ToHeader) request.getHeader(ToHeader.NAME);
			String fileName = ((SipURI) toHeader.getAddress().getURI())
					.getUser()
					+ WAV_EXT;
			String route = null;
			String recordFilePath = null;

			try {
				Context initCtx = new InitialContext();
				Context myEnv = (Context) initCtx.lookup("java:comp/env");

				route = (String) myEnv.lookup("filesRoute");
			} catch (NamingException nEx) {
				log.warn("Lookup of filesRoute env Variable failed", nEx);
			}

//			if (route != null) {
//				recordFilePath = route + fileName;
//			} else {
				recordFilePath = fileName;
//			}

			String[] params = new String[2];
			params[0] = audioFileURL.toString();
			params[1] = recordFilePath;

			MsSignalGenerator signalGenerator = msProvider
					.getSignalGenerator(endpointName);

			try {
				ActivityContextInterface dtmfAci = msActivityFactory
						.getActivityContextInterface(signalGenerator);
				dtmfAci.attach(this.getSbbLocalObject());
				signalGenerator.apply(EventID.PLAY_RECORD, params);
			} catch (UnrecognizedActivityException e) {
				log.error(e.getMessage(), e);
			}
		}

	}

	private void initDtmfDetector(MsConnection connection, String userEndPoint) {
		MsSignalDetector dtmfDetector = msProvider
				.getSignalDetector(userEndPoint);
		try {
			ActivityContextInterface dtmfAci = msActivityFactory
					.getActivityContextInterface(dtmfDetector);
			dtmfAci.attach(this.getSbbLocalObject());
			dtmfDetector.receive(EventID.DTMF, connection, new String[] {});
		} catch (UnrecognizedActivityException e) {
			log.error(e.getMessage(), e);
		}
	}

	private void releaseMediaConnectionAndDialog() {
		ActivityContextInterface[] activities = getSbbContext().getActivities();
		SbbLocalObject sbbLocalObject = getSbbContext().getSbbLocalObject();
		MsConnection msConnection = null;
		for (ActivityContextInterface attachedAci : activities) {
			if (attachedAci.getActivity() instanceof Dialog) {
				attachedAci.detach(sbbLocalObject);
			}
			if (attachedAci.getActivity() instanceof MsConnection) {
				attachedAci.detach(sbbLocalObject);
				msConnection = (MsConnection) attachedAci.getActivity();
			}
		}
		if (msConnection != null) {
			msConnection.release();
		}
	}

	private void sendServerInternalError() {
		try {
			Response response = getMessageFactory().createResponse(
					Response.SERVER_INTERNAL_ERROR, this.getInviteRequest());
			this.getServerTransaction().sendResponse(response);

		} catch (ParseException e) {
			log.error(e.getMessage(), e);
		} catch (SipException e) {
			log.error(e.getMessage(), e);
		} catch (InvalidArgumentException e) {
			log.error(e.getMessage(), e);
		}
	}

	public void onDtmf(MsNotifyEvent evt, ActivityContextInterface aci) {
		log.info("########## VOICE MAIL SBB: onDTMFEvent ##########");
		EventCause cause = evt.getCause();
		checkDtmfDigit(cause);
		this.initDtmfDetector(this.getConnection(), this.getUserEndpoint());

	}

	private MsConnection getConnection() {
		ActivityContextInterface[] activities = this.getSbbContext()
				.getActivities();
		for (int i = 0; i < activities.length; i++) {
			if (activities[i].getActivity() instanceof MsConnection) {
				return (MsConnection) activities[i].getActivity();
			}
		}
		return null;
	}

	/**
	 * Voice Mail will hang up on caller sending a BYE Request.
	 * 
	 */
	private void sendByeRequest() {
		log.info("########## VOICE MAIL SBB: sendByRequest ##########");
		try {
			SipProvider sipProvider = getSipFactoryProvider().getSipProvider();
			Dialog dialog = this.getServerTransaction().getDialog();
			Request request = dialog.createRequest(Request.BYE);
			ClientTransaction ct = sipProvider.getNewClientTransaction(request);

			dialog.sendRequest(ct);

		} catch (TransactionUnavailableException e) {
			log.error(e.getMessage(), e);
		} catch (SipException e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * After receiving a BYE Request, an OK Respose has to be sent.
	 * 
	 * @param byeEvent
	 */
	private void byeRequestOkResponse(RequestEvent byeEvent) {
		log.info("########## VOICE MAIL SBB: byeRequestOkResponse ##########");
		Request request = byeEvent.getRequest();
		ServerTransaction tx = byeEvent.getServerTransaction();
		try {
			Response response = getMessageFactory().createResponse(Response.OK,
					request);
			tx.sendResponse(response);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	private void checkDtmfDigit(EventCause dtmf) {
		URL audioFileURL;

		/**
		 * TODO: The Feature of listening the next message and deleting the last
		 * message is not implemented yet. After pressing 1 or 7 you will only
		 * listen a message saying which number you have pressed
		 */

		// Press 1 if you want to listen the next message
		if (dtmf.equals(EventCause.DTMF_DIGIT_1) ) {
			audioFileURL = getClass().getResource(dtmf1);
		}
		// Press 7 if you want to delete the last message
		else if (dtmf.equals(EventCause.DTMF_DIGIT_7) ) {
			audioFileURL = getClass().getResource(dtmf7);
		}
		// Press 9 if you want to hang up
		else if (dtmf.equals(EventCause.DTMF_DIGIT_9) ) {
			audioFileURL = getClass().getResource(dtmf9);

		} else {
			audioFileURL = getClass().getResource(tryAgain);
		}

		MsSignalGenerator generator = msProvider.getSignalGenerator(this
				.getUserEndpoint());

		try {
			ActivityContextInterface generatorActivity = msActivityFactory
					.getActivityContextInterface(generator);
			generatorActivity.attach(getSbbContext().getSbbLocalObject());

			generator.apply(EventID.PLAY, new String[] { audioFileURL
					.toString() });

			// this.initDtmfDetector(getConnection(), this.getEndpointName());
		} catch (UnrecognizedActivityException e) {
			e.printStackTrace();
		}

	}

	/**
	 * To know whether or not the called user has the Voice Mail service
	 * enabled.
	 * 
	 * @param sipAddress:
	 *            Called user address.
	 * @return boolean: TRUE -> Voice Mail enabled. FALSE -> Voice Mail disabled
	 *         for the given user identified by sip address.
	 */
	private boolean isSubscriber(String sipAddress) {
		boolean state = false;
		CallControlProfileCMP profile = lookup(new Address(AddressPlan.SIP,
				sipAddress));

		if (profile != null) {
			state = profile.getVoicemailState();
		}

		return state;
	}

	/**
	 * This method is used to know if the it is going to be used the voice mail
	 * of the same user or the voice mail of a different user.
	 * 
	 * @param event
	 * @return TRUE: If the called user is sip:vmail@nist.gov
	 */
	private boolean sameUser(javax.sip.RequestEvent event) {
		boolean sameUser = false;
		Request inviteRequest = event.getRequest();

		// Checking if the called user and the caller are the same
		ToHeader toHeader = (ToHeader) inviteRequest.getHeader(ToHeader.NAME);
		SipURI toURI = (SipURI) toHeader.getAddress().getURI();

		if ((toURI.getUser().equals(USER) && toURI.getHost().equals(HOST))) {
			sameUser = true;
		}

		// Setting Same User value
		this.setSameUser(sameUser);

		return sameUser;
	}

	// TODO: Perform further operations if required in these methods.
	public void setSbbContext(SbbContext context) {
		super.setSbbContext(context);

		// To create Header objects from a particular implementation of JAIN SIP
		headerFactory = getSipFactoryProvider().getHeaderFactory();

		try {
			Context myEnv = (Context) new InitialContext()
					.lookup("java:comp/env");
			// Getting Media Resource Adaptor interfaces
			msProvider = (MsProvider) myEnv
					.lookup("slee/resources/media/1.0/provider");
			msActivityFactory = (MediaRaActivityContextInterfaceFactory) myEnv
					.lookup("slee/resources/media/1.0/acifactory");
			// Getting Sip Resource Adaptor interface
			sipACIF = (SipActivityContextInterfaceFactory) myEnv
					.lookup("slee/resources/jainsip/1.2/acifactory");
			// Getting Timer Facility interface
			timerFacility = (TimerFacility) myEnv
					.lookup("slee/facilities/timer");

		} catch (NamingException e) {
			log.error(e.getMessage(), e);
		}
	}

	public void sbbPostCreate() throws javax.slee.CreateException {
		// Setting DTMF
		this.setDtmf(NON_DIGIT);
	}

	public abstract org.mobicents.slee.examples.callcontrol.profile.CallControlProfileCMP getCallControlProfileCMP(
			javax.slee.profile.ProfileID profileID)
			throws javax.slee.profile.UnrecognizedProfileNameException,
			javax.slee.profile.UnrecognizedProfileTableNameException;

	public abstract org.mobicents.slee.examples.callcontrol.voicemail.VoiceMailSbbActivityContextInterface asSbbActivityContextInterface(
			ActivityContextInterface aci);

	private final HeaderFactory getHeaderFactory() {
		return headerFactory;
	}

	// Interfaces
	private HeaderFactory headerFactory;

	private SipActivityContextInterfaceFactory sipACIF;
	private TimerFacility timerFacility;

	private final String recordAfterTone = "audiofiles/RecordAfterTone.wav";
	private final String waitingDTMF = "audiofiles/WaitingDTMF.wav";
	private final String dtmf1 = "audiofiles/DTMF1.wav";
	private final String dtmf7 = "audiofiles/DTMF7.wav";
	private final String dtmf9 = "audiofiles/DTMF9.wav";
	private final String tryAgain = "audiofiles/TryAgain.wav";

	private final String USER = "vmail";
	private final String HOST = "nist.gov";
	private final String NON_DIGIT = "NULL";
	private final String WAV_EXT = ".wav";

	private MsProvider msProvider;
	private MediaRaActivityContextInterfaceFactory msActivityFactory;

	public final static String ENDPOINT_NAME = "media/trunk/IVR/$";

	/**
	 * ***************************************** ************** CMP Fields
	 * *************** *****************************************
	 */

	// 'mediaSession' CMP field setter
	public abstract void setMediaSession(MsSession value);

	// 'mediaSession' CMP field getter
	public abstract MsSession getMediaSession();

	// 'inviteRequest' CMP field setter
	public abstract void setInviteRequest(Request value);

	// 'inviteRequest' CMP field getter
	public abstract Request getInviteRequest();

	// 'serverTransaction' CMP field setter
	public abstract void setServerTransaction(ServerTransaction value);

	// 'serverTransaction' CMP field getter
	public abstract ServerTransaction getServerTransaction();

	// 'ok' CMP field setter
	public abstract void setOk(boolean value);

	// 'ok' CMP field getter
	public abstract boolean getOk();

	// 'sameUser' CMP field setter
	public abstract void setSameUser(boolean value);

	// 'sameUser' CMP field getter
	public abstract boolean getSameUser();

	// 'timerID' CMP field setter
	public abstract void setTimerID(TimerID value);

	// 'timerID' CMP field getter
	public abstract TimerID getTimerID();

	// 'dtmf' CMP field setter
	public abstract void setDtmf(String value);

	// 'dtmf' CMP field getter
	public abstract String getDtmf();

	public abstract String getUserEndpoint();

	public abstract void setUserEndpoint(String endpointName);

}