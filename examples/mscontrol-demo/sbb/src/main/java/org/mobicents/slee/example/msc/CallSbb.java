/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mobicents.slee.example.msc;

import java.net.URI;
import java.text.ParseException;

import javax.media.mscontrol.MediaSession;
import javax.media.mscontrol.MsControlException;
import javax.media.mscontrol.MsControlFactory;
import javax.media.mscontrol.join.JoinEvent;
import javax.media.mscontrol.join.Joinable.Direction;
import javax.media.mscontrol.mediagroup.MediaGroup;
import javax.media.mscontrol.mediagroup.PlayerEvent;
import javax.media.mscontrol.networkconnection.NetworkConnection;
import javax.media.mscontrol.networkconnection.SdpPortManager;
import javax.media.mscontrol.networkconnection.SdpPortManagerEvent;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sip.Dialog;
import javax.sip.InvalidArgumentException;
import javax.sip.RequestEvent;
import javax.sip.ServerTransaction;
import javax.sip.SipException;
import javax.sip.address.Address;
import javax.sip.header.ContactHeader;
import javax.sip.header.ContentTypeHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;
import javax.slee.ActivityContextInterface;
import javax.slee.CreateException;
import javax.slee.RolledBackContext;
import javax.slee.Sbb;
import javax.slee.SbbContext;
import javax.slee.SbbLocalObject;
import javax.slee.facilities.Tracer;

import net.java.slee.resource.sip.DialogActivity;
import net.java.slee.resource.sip.SipActivityContextInterfaceFactory;
import net.java.slee.resource.sip.SleeSipProvider;

import org.mobicents.slee.resource.mediacontrol.MsActivityContextInterfaceFactory;

/**
 * 
 * @author kulikov
 * @author martins
 */
public abstract class CallSbb implements Sbb {

	public final static String JBOSS_BIND_ADDRESS = System.getProperty(
			"jboss.bind.address", "127.0.0.1");
	public final static String WELCOME = "http://" + JBOSS_BIND_ADDRESS
			+ ":8080/mscontrol/audio/RQNT-ULAW.wav";
	public final static String ENDPOINT_NAME = "/mobicents/media/IVR/$";

	private SbbContext sbbContext;
	private Tracer tracer;
	private SleeSipProvider sipRaSbbInterface;
	private SipActivityContextInterfaceFactory sipRaAciFactory;
	private MsControlFactory msRaSbbInterface;
	private MsActivityContextInterfaceFactory mscRaAciFactory;

	// cmp fields

	public abstract ActivityContextInterface getServerTransactionACI();

	public abstract void setServerTransactionACI(ActivityContextInterface aci);

	public abstract ActivityContextInterface getDialogACI();

	public abstract void setDialogACI(ActivityContextInterface aci);

	public abstract ActivityContextInterface getMediaSessionACI();

	public abstract void setMediaSessionACI(ActivityContextInterface aci);

	public abstract ActivityContextInterface getNetworkConnectionACI();

	public abstract void setNetworkConnectionACI(ActivityContextInterface aci);

	public abstract ActivityContextInterface getMediaGroupACI();

	public abstract void setMediaGroupACI(ActivityContextInterface aci);

	// service logic

	/**
	 * Handles the event notifying new SIP session invitation.
	 * 
	 * @param event
	 * @param aci
	 */
	public void onInvite(RequestEvent event, ActivityContextInterface aci) {
		tracer.info("Received new SIP session invitation.");
		try {
			initialSipSessionSetup(event, aci);
		} catch (Exception e) {
			tracer.severe("Failed to do initial sip session setup.", e);
			abortSipSessionSetup();
			return;
		}
		try {
			initialMediaSessionSetup(event.getServerTransaction());
		} catch (Exception e) {
			tracer.severe("Failed to process sip invite", e);
			abortSipSessionSetup();
			abortMediaSessionSetup();
		}
	}

	/*
	 * Initial setup of the sip session: sends back a 100 response and creates
	 * the sip dialog.
	 */
	private void initialSipSessionSetup(RequestEvent event,
			ActivityContextInterface aci) throws ParseException, SipException,
			InvalidArgumentException {
		// store the server tx aci in a cmp shortcut
		setServerTransactionACI(aci);
		// send trying response
		ServerTransaction serverTransaction = event.getServerTransaction();
		Response response = sipRaSbbInterface
				.getMessageFactory()
				.createResponse(Response.TRYING, serverTransaction.getRequest());
		serverTransaction.sendResponse(response);
		// create sip dialog and attach to its activity
		Dialog dialog = sipRaSbbInterface.getNewDialog(serverTransaction);
		ActivityContextInterface dialogAci = sipRaAciFactory
				.getActivityContextInterface((DialogActivity) dialog);
		dialogAci.attach(sbbContext.getSbbLocalObject());
		// store the dialog aci in a cmp shortcut
		setDialogACI(dialogAci);
	}

	/*
	 * Setup of the media session: creates the media session, creates a network
	 * connection on it and process the client sdp received on SIP
	 */
	private void initialMediaSessionSetup(ServerTransaction serverTransaction)
			throws MsControlException {
		// create media session
		MediaSession session = msRaSbbInterface.createMediaSession();
		ActivityContextInterface mediaSessionACI = mscRaAciFactory
				.getActivityContextInterface(session);
		SbbLocalObject sbbLocalObject = sbbContext.getSbbLocalObject();
		mediaSessionACI.attach(sbbLocalObject);
		// store the media session aci in a cmp shortcut
		setMediaSessionACI(mediaSessionACI);
		tracer.info("Created media session: " + session);
		// create network connection
		NetworkConnection connection = session
				.createNetworkConnection(NetworkConnection.BASIC);
		ActivityContextInterface connectionACI = mscRaAciFactory
				.getActivityContextInterface(connection);
		connectionACI.attach(sbbLocalObject);
		// store the network connection aci in a cmp shortcut
		setNetworkConnectionACI(connectionACI);
		tracer.info("Created network connection: " + connection);
		// process the received sdp
		SdpPortManager sdpManager = connection.getSdpPortManager();
		tracer.info("Created SDP Manager, sending client sdp...");
		sdpManager.processSdpOffer((byte[]) serverTransaction.getRequest()
				.getContent());
	}

	/**
	 * Event notifying failure on processing the client SDP, abort the session.
	 * 
	 * @param event
	 * @param aci
	 */
	public void onStreamFailure(SdpPortManagerEvent event,
			ActivityContextInterface aci) {
		tracer.info("Failed to process SDP.");
		abortSipSessionSetup();
		abortMediaSessionSetup();
	}

	/**
	 * Event with the media server generated sdp, send it back to the sip
	 * client.
	 * 
	 * @param event
	 * @param aci
	 */
	public void onAnswerGenerated(SdpPortManagerEvent event,
			ActivityContextInterface aci) {
		tracer.info("Received SDP answer.");
		try {
			finishSipSessionSetup(event.getMediaServerSdp());
		} catch (Exception e) {
			tracer.severe("Unable to send OK response with generated SDP", e);
			abortSipSessionSetup();
			abortMediaSessionSetup();
			return;
		}
		try {
			finishMediaSessionSetup(aci);
		} catch (Exception e) {
			tracer.severe("Unable to initiate join.", e);
			terminateSipSession();
			abortMediaSessionSetup();
		}
	}

	/*
	 * End of sip session setup: sends back the server generated sdp.
	 */
	private void finishSipSessionSetup(byte[] sdp) throws ParseException,
			SipException, InvalidArgumentException {
		ServerTransaction serverTransaction = (ServerTransaction) getServerTransactionACI()
				.getActivity();
		ContentTypeHeader contentType = sipRaSbbInterface.getHeaderFactory()
				.createContentTypeHeader("application", "sdp");
		String localAddress = sipRaSbbInterface.getListeningPoints()[0]
				.getIPAddress();
		int localPort = sipRaSbbInterface.getListeningPoints()[0].getPort();
		Address contactAddress = sipRaSbbInterface.getAddressFactory()
				.createAddress("sip:" + localAddress + ":" + localPort);
		ContactHeader contact = sipRaSbbInterface.getHeaderFactory()
				.createContactHeader(contactAddress);
		Response ok = sipRaSbbInterface.getMessageFactory().createResponse(
				Response.OK, serverTransaction.getRequest(), contentType, sdp);
		ok.setHeader(contact);
		serverTransaction.sendResponse(ok);
	}

	/*
	 * End of the media session setup: creates ivr and make it join the session
	 * with the sip client.
	 */
	private void finishMediaSessionSetup(
			ActivityContextInterface networkConnectionAci)
			throws MsControlException {
		NetworkConnection connection = (NetworkConnection) networkConnectionAci
				.getActivity();
		MediaSession session = connection.getMediaSession();
		MediaGroup mediaGroup = session
				.createMediaGroup(MediaGroup.PLAYER_RECORDER_SIGNALDETECTOR);
		connection.joinInitiate(Direction.DUPLEX, mediaGroup, "context");
		ActivityContextInterface mediaGroupACI = mscRaAciFactory
				.getActivityContextInterface(mediaGroup);
		mediaGroupACI.attach(sbbContext.getSbbLocalObject());
		setMediaGroupACI(mediaGroupACI);
	}

	/**
	 * Event notifying the connection between the sip client and the media
	 * server.
	 * 
	 * @param event
	 * @param aci
	 */
	public void onJoined(JoinEvent event, ActivityContextInterface aci) {
		tracer.info("SIP client and media server connected, requesting play of announcement...");
		try {
			ActivityContextInterface mediaGroupACI = getMediaGroupACI();
			MediaGroup mediaGroup = (MediaGroup) mediaGroupACI.getActivity();
			mediaGroup.getPlayer().play(new URI(WELCOME), null, null);
		} catch (Exception e) {
			tracer.severe(
					"Unexpected error playing annoucenment, terminating sip and media sessions.",
					e);
			terminateSipSession();
			terminateMediaSession();
		}
	}

	public void onAnnouncementCompleted(PlayerEvent event,
			ActivityContextInterface aci) {
		tracer.info("Announcement play completed, terminating sip and media sessions.");
		terminateSipSession();
		terminateMediaSession();
	}

	public void onDisconnect(RequestEvent event, ActivityContextInterface aci) {
		tracer.info("SIP client and media server disconnected.");
		terminateSipSession();
	}

	/*
	 * Aborts the sip session: sends an error back to the client and deletes the
	 * sip dialog (if exists).
	 */
	private void abortSipSessionSetup() {
		SbbLocalObject sbbLocalObject = sbbContext.getSbbLocalObject();
		ActivityContextInterface dialogACI = getDialogACI();
		if (dialogACI != null) {
			dialogACI.detach(sbbLocalObject);
			try {
				Dialog dialog = (Dialog) dialogACI.getActivity();
				dialog.delete();
			} catch (Exception e) {
				tracer.severe("failed to abort sip dialog.", e);
			}
		}
		ActivityContextInterface serverTransactionACI = getServerTransactionACI();
		if (serverTransactionACI != null) {
			serverTransactionACI.detach(sbbLocalObject);
			ServerTransaction serverTransaction = (ServerTransaction) serverTransactionACI
					.getActivity();
			try {
				Response response = sipRaSbbInterface.getMessageFactory()
						.createResponse(Response.SERVER_INTERNAL_ERROR,
								serverTransaction.getRequest());
				serverTransaction.sendResponse(response);
			} catch (Exception e) {
				tracer.severe("failed to abort sip server tx.", e);
			}
		}
	}

	/*
	 * Aborts the media session: releases the network connection and the media
	 * session.
	 */
	private void abortMediaSessionSetup() {
		releaseMediaSession();
	}

	/*
	 * 
	 */
	private void releaseMediaSession() {
		// get sbb entity local object
		SbbLocalObject sbbLocalObject = sbbContext.getSbbLocalObject();
		// release media group (the ivr) if exists
		ActivityContextInterface mediaGroupACI = getMediaGroupACI();
		if (mediaGroupACI != null) {
			mediaGroupACI.detach(sbbLocalObject);
			try {
				MediaGroup mediaGroup = (MediaGroup) mediaGroupACI
						.getActivity();
				if (mediaGroup != null) {
					mediaGroup.release();
				}
			} catch (Exception e) {
				tracer.severe("failed to abort media network connection.", e);
			}
		}
		// release network connection if exists
		ActivityContextInterface networkConnectionACI = getNetworkConnectionACI();
		if (networkConnectionACI != null) {
			networkConnectionACI.detach(sbbLocalObject);
			try {
				NetworkConnection networkConnection = (NetworkConnection) networkConnectionACI
						.getActivity();
				if (networkConnection != null) {
					networkConnection.release();
				}
			} catch (Exception e) {
				tracer.severe("failed to abort media network connection.", e);
			}
		}
		// release media session if exists
		ActivityContextInterface mediaSessionACI = getMediaSessionACI();
		if (mediaSessionACI != null) {
			mediaSessionACI.detach(sbbLocalObject);
			try {
				MediaSession mediaSession = (MediaSession) mediaSessionACI
						.getActivity();
				if (mediaSession != null) {
					mediaSession.release();
				}
			} catch (Exception e) {
				tracer.severe("failed to abort media session.", e);
			}
		}
	}

	/*
	 * Terminates the media session: releases the network connection and the
	 * media session.
	 */
	private void terminateMediaSession() {
		releaseMediaSession();
	}

	/**
	 * 
	 */
	private void terminateSipSession() {
		SbbLocalObject sbbLocalObject = sbbContext.getSbbLocalObject();
		ActivityContextInterface dialogACI = getDialogACI();
		if (dialogACI != null) {
			dialogACI.detach(sbbLocalObject);
			try {
				DialogActivity dialog = (DialogActivity) dialogACI
						.getActivity();
				Request bye = dialog.createRequest(Request.BYE);
				dialog.sendRequest(bye);
			} catch (Exception e) {
				tracer.severe("failed to abort sip dialog.", e);
			}
		}
	}

	public void setSbbContext(SbbContext sbbContext) {
		this.sbbContext = sbbContext;
		this.tracer = sbbContext.getTracer("MS-Control-DEMO");
		try {
			Context ctx = (Context) new InitialContext()
					.lookup("java:comp/env");
			sipRaSbbInterface = (SleeSipProvider) ctx
					.lookup("slee/resources/jainsip/1.2/provider");
			sipRaAciFactory = (SipActivityContextInterfaceFactory) ctx
					.lookup("slee/resources/jainsip/1.2/acifactory");
			msRaSbbInterface = (MsControlFactory) ctx
					.lookup("slee/resources/media/1.0/provider");
			mscRaAciFactory = (MsActivityContextInterfaceFactory) ctx
					.lookup("slee/resources/media/1.0/acifactory");
		} catch (Exception ne) {
			tracer.severe("Could not set SBB context:", ne);
		}
	}

	public void unsetSbbContext() {
		this.sbbContext = null;
	}

	public void sbbCreate() throws CreateException {
	}

	public void sbbPostCreate() throws CreateException {
	}

	public void sbbActivate() {
	}

	public void sbbPassivate() {
	}

	public void sbbLoad() {
	}

	public void sbbStore() {
	}

	public void sbbRemove() {
	}

	public void sbbExceptionThrown(Exception arg0, Object arg1,
			ActivityContextInterface arg2) {
	}

	public void sbbRolledBack(RolledBackContext arg0) {
	}
}
