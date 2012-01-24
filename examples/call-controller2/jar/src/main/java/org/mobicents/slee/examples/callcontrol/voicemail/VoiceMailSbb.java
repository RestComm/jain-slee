/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.slee.examples.callcontrol.voicemail;

import jain.protocol.ip.mgcp.JainMgcpEvent;
import jain.protocol.ip.mgcp.message.CreateConnection;
import jain.protocol.ip.mgcp.message.CreateConnectionResponse;
import jain.protocol.ip.mgcp.message.DeleteConnection;
import jain.protocol.ip.mgcp.message.NotificationRequest;
import jain.protocol.ip.mgcp.message.NotificationRequestResponse;
import jain.protocol.ip.mgcp.message.Notify;
import jain.protocol.ip.mgcp.message.NotifyResponse;
import jain.protocol.ip.mgcp.message.parms.CallIdentifier;
import jain.protocol.ip.mgcp.message.parms.ConflictingParameterException;
import jain.protocol.ip.mgcp.message.parms.ConnectionDescriptor;
import jain.protocol.ip.mgcp.message.parms.ConnectionMode;
import jain.protocol.ip.mgcp.message.parms.EndpointIdentifier;
import jain.protocol.ip.mgcp.message.parms.EventName;
import jain.protocol.ip.mgcp.message.parms.NotifiedEntity;
import jain.protocol.ip.mgcp.message.parms.RequestedAction;
import jain.protocol.ip.mgcp.message.parms.RequestedEvent;
import jain.protocol.ip.mgcp.message.parms.ReturnCode;
import jain.protocol.ip.mgcp.pkg.MgcpEvent;
import jain.protocol.ip.mgcp.pkg.PackageName;

import java.io.File;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.regex.Pattern;

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
import javax.slee.ActivityEndEvent;
import javax.slee.Address;
import javax.slee.AddressPlan;
import javax.slee.FactoryException;
import javax.slee.SLEEException;
import javax.slee.SbbContext;
import javax.slee.SbbLocalObject;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.UnrecognizedActivityException;
import javax.slee.facilities.TimerEvent;
import javax.slee.facilities.Tracer;

import net.java.slee.resource.mgcp.JainMgcpProvider;
import net.java.slee.resource.mgcp.MgcpActivityContextInterfaceFactory;
import net.java.slee.resource.mgcp.MgcpConnectionActivity;
import net.java.slee.resource.mgcp.MgcpEndpointActivity;
import net.java.slee.resource.sip.DialogActivity;
import net.java.slee.resource.sip.SipActivityContextInterfaceFactory;

import org.mobicents.protocols.mgcp.jain.pkg.AUPackage;
import org.mobicents.slee.examples.callcontrol.common.SubscriptionProfileSbb;
import org.mobicents.slee.examples.callcontrol.profile.CallControlProfileCMP;

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

		try {

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

				// Formalities of sip, so we dont get retrans
				// Attaching to SIP Dialog activity
				Dialog dial = getSipFactoryProvider().getNewDialog(
						(Transaction) st);
				ActivityContextInterface dialogAci = sipACIF
						.getActivityContextInterface((DialogActivity) dial);

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

				String sdp = new String(event.getRequest().getRawContent());

				CallIdentifier callID = this.mgcpProvider
						.getUniqueCallIdentifier();
				// this is not requiered, but to be good MGCP citizen we will
				// obey mgcp call id rule.
				setCallIdentifier(callID);

				EndpointIdentifier endpointID = new EndpointIdentifier(
						PRE_ENDPOINT_NAME, mmsBindAddress + ":"
								+ MGCP_PEER_PORT);
				CreateConnection createConnection = new CreateConnection(this,
						callID, endpointID, ConnectionMode.Confrnce);
				try {
					createConnection
							.setRemoteConnectionDescriptor(new ConnectionDescriptor(
									sdp));
				} catch (ConflictingParameterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				int txID = mgcpProvider.getUniqueTransactionHandler();
				createConnection.setTransactionHandle(txID);

				MgcpConnectionActivity connectionActivity = null;
				try {
					connectionActivity = mgcpProvider.getConnectionActivity(
							txID, endpointID);
					ActivityContextInterface epnAci = mgcpActivityContestInterfaceFactory
							.getActivityContextInterface(connectionActivity);
					epnAci.attach(getSbbContext().getSbbLocalObject());

				} catch (FactoryException ex) {
					ex.printStackTrace();
				} catch (NullPointerException ex) {
					ex.printStackTrace();
				} catch (UnrecognizedActivityException ex) {
					ex.printStackTrace();
				}

				mgcpProvider
						.sendMgcpEvents(new JainMgcpEvent[] { createConnection });
				log
						.info("########## VOICE MAIL AVAILABLE FOR USER: sent PR CRCX request ##########");
			} else {
				// Voice Mail service disabled
				response = getMessageFactory().createResponse(
						Response.TEMPORARILY_UNAVAILABLE, request);
				log.info("########## NO VOICE MAIL AVAILABLE FOR USER: "
						+ uri.toString());
				st.sendResponse(response);
			}

		} catch (TransactionRequiredLocalException e) {
			log.severe(e.getMessage(), e);
		} catch (SLEEException e) {
			log.severe(e.getMessage(), e);
		} catch (ParseException e) {
			log.severe(e.getMessage(), e);
		} catch (SipException e) {
			log.severe(e.getMessage(), e);
		} catch (InvalidArgumentException e) {
			log.severe(e.getMessage(), e);
		} catch (NullPointerException e) {
			log.severe(e.getMessage(), e);
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

			releaseState();

			// Sending the OK Response to the BYE Request received.
			byeRequestOkResponse(event);

		} catch (FactoryException e) {
			log.severe(e.getMessage(), e);
		} catch (NullPointerException e) {
			log.severe(e.getMessage(), e);
		}
	}
	private void sendServerError(String message, int errorCode) {
		try {
			Response response = getMessageFactory()
					.createResponse(
							Response.SERVER_INTERNAL_ERROR,
							this.getInviteRequest(),
							getHeaderFactory().createContentTypeHeader("text",
									"plain"), message.getBytes());

			this.getServerTransaction().sendResponse(response);

		} catch (ParseException e) {
			log.severe(e.getMessage(), e);
		} catch (SipException e) {
			log.severe(e.getMessage(), e);
		} catch (InvalidArgumentException e) {
			log.severe(e.getMessage(), e);
		}

		releaseState();
	}
	
	public void onCreateConnectionResponse(CreateConnectionResponse event,
			ActivityContextInterface aci) {
		log.info("Receive CRCX response: " + event);

		ReturnCode status = event.getReturnCode();

		switch (status.getValue()) {
		case ReturnCode.TRANSACTION_EXECUTED_NORMALLY:
			log.info("Connection created properly.");
			break;
		default:
			ReturnCode rc = event.getReturnCode();
			log.severe("CRCX failed. Value = " + rc.getValue() + " Comment = "
					+ rc.getComment());

			sendServerError("Failed to create connection, code: "
					+ event.getReturnCode(), Response.SERVER_INTERNAL_ERROR);

			return;
		}
		boolean startMailMedia = false;
		if (event.getSecondEndpointIdentifier() == null) {
			// this is response for PR creation
			// we have one connection activity, lets send another crcx

			// send OK with sdp
			DialogActivity da = getDialogActivity();
			ServerTransaction txn = getServerTransaction();
			if (txn == null) {
				log.severe("SIP activity lost, close RTP connection");
				releaseState();
				return;
			}

			Request request = txn.getRequest();

			ContentTypeHeader contentType = null;
			try {
				contentType = getHeaderFactory().createContentTypeHeader("application", "sdp");
			} catch (ParseException ex) {
			}

			String localAddress = getSipFactoryProvider().getListeningPoints()[0].getIPAddress();
			int localPort = getSipFactoryProvider().getListeningPoints()[0].getPort();

			javax.sip.address.Address contactAddress = null;
			try {
				contactAddress = getAddressFactory().createAddress("sip:" + localAddress + ":" + localPort);
			} catch (ParseException ex) {
				log.severe(ex.getMessage(), ex);
			}
			ContactHeader contact = getHeaderFactory().createContactHeader(contactAddress);

			Response response = null;
			try {
				response = getMessageFactory().createResponse(Response.OK, request, contentType, event.getLocalConnectionDescriptor().toString().getBytes());
			} catch (ParseException ex) {
			}

			response.setHeader(contact);
			try {
				txn.sendResponse(response);
			} catch (InvalidArgumentException ex) {
				log.severe(ex.getMessage(), ex);
			} catch (SipException ex) {
				log.severe(ex.getMessage(), ex);
			}
			EndpointIdentifier endpointID = new EndpointIdentifier(
					IVR_ENDPOINT_NAME, mmsBindAddress + ":" + MGCP_PEER_PORT);
			CreateConnection createConnection = new CreateConnection(this,
					getCallIdentifier(), event					.getSpecificEndpointIdentifier(), ConnectionMode.Confrnce);

			int txID = mgcpProvider.getUniqueTransactionHandler();
			createConnection.setTransactionHandle(txID);

			// now set other end
			try {
				createConnection.setSecondEndpointIdentifier(endpointID);
			} catch (ConflictingParameterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			MgcpConnectionActivity connectionActivity = null;
			try {
				connectionActivity = mgcpProvider.getConnectionActivity(txID,
						endpointID);
				ActivityContextInterface epnAci = mgcpActivityContestInterfaceFactory
						.getActivityContextInterface(connectionActivity);
				epnAci.attach(getSbbContext().getSbbLocalObject());
				// epnAci.attach(getParentCmp());
			} catch (FactoryException ex) {
				ex.printStackTrace();
			} catch (NullPointerException ex) {
				ex.printStackTrace();
			} catch (UnrecognizedActivityException ex) {
				ex.printStackTrace();
			}

			mgcpProvider
					.sendMgcpEvents(new JainMgcpEvent[] { createConnection });

		} else {
			// this is last
			startMailMedia = true;

		}
		
		EndpointIdentifier eid = null;
		if(startMailMedia)
		{
			eid = event.getSecondEndpointIdentifier();
		}else
		{
			eid = event.getSpecificEndpointIdentifier();	
		}
		 
		log.info("Creating endpoint activity on: " + eid);
		MgcpEndpointActivity eActivity = mgcpProvider.getEndpointActivity(eid);
		ActivityContextInterface eAci = mgcpActivityContestInterfaceFactory
				.getActivityContextInterface(eActivity);
		eAci.attach(this.getSbbContext().getSbbLocalObject());

		if (startMailMedia) {
			startMailMedia();

		}

	}

	public void onTimerEvent(TimerEvent event, ActivityContextInterface aci) {

	}

	private void releaseState() {

		ActivityContextInterface[] activities = getSbbContext().getActivities();
		SbbLocalObject sbbLocalObject = getSbbContext().getSbbLocalObject();

		MgcpEndpointActivity mea = getEndpointActivity("ivr");
		if(mea!=null)
		{
			//empty RQNT, this is requiered to flush data.
			sendRQNT(null,false,false);
		}
	
		for (ActivityContextInterface attachedAci : activities) {
			if (attachedAci.getActivity() instanceof Dialog) {
				attachedAci.detach(sbbLocalObject);
				
			}
			if (attachedAci.getActivity() instanceof MgcpConnectionActivity) {
				attachedAci.detach(sbbLocalObject);
	

			}
			
			if ( (attachedAci.getActivity() instanceof MgcpEndpointActivity)) {
				attachedAci.detach(sbbLocalObject);
	
				MgcpEndpointActivity mgcpEndpoint = (MgcpEndpointActivity) attachedAci
						.getActivity();
				DeleteConnection deleteConnection = new DeleteConnection(this,
						mgcpEndpoint.getEndpointIdentifier());
				deleteConnection.setCallIdentifier(this.getCallIdentifier());

				deleteConnection.setTransactionHandle(mgcpProvider.getUniqueTransactionHandler());
				mgcpProvider.sendMgcpEvents(
						new JainMgcpEvent[] { deleteConnection });
				log.info("Delete connections: \n"+deleteConnection);
		
			}

		}
		this.setCallIdentifier(null);
	}
	
	public void onNotificationRequestResponse(NotificationRequestResponse event, ActivityContextInterface aci) {


		ReturnCode status = event.getReturnCode();

		switch (status.getValue()) {
		case ReturnCode.TRANSACTION_EXECUTED_NORMALLY:
			log.info("########## VOICE MAIL SBB: RQNT executed properly. TXID: "+event.getTransactionHandle()+" ##########");
			break;
		default:
			ReturnCode rc = event.getReturnCode();
			log.info("########## VOICE MAIL SBB: RQNT failed, terminating call. TXID: "+event.getTransactionHandle()+" ##########");
			sendByeRequest();
			
			break;
		}

	}
	
	public void onNotifyRequest(Notify event, ActivityContextInterface aci) {


		NotifyResponse response = new NotifyResponse(event.getSource(),
				ReturnCode.Transaction_Executed_Normally);
		response.setTransactionHandle(event.getTransactionHandle());
		log.info("########## VOICE MAIL SBB: Sending Notify response["+response+"] to ["+event+"]["+event.getTransactionHandle()+"] ["+response.getTransactionHandle()+"]##########");
		
		mgcpProvider.sendMgcpEvents(new JainMgcpEvent[] { response });

		
		EventName[] observedEvents = event.getObservedEvents();

		for (EventName observedEvent : observedEvents) {
			switch (observedEvent.getEventIdentifier().intValue()) {
			case MgcpEvent.REPORT_ON_COMPLETION:
				log.info("########## VOICE MAIL SBB: Signal completed, event identifier["+observedEvent.getEventIdentifier()+"] ##########");
				
				if(observedEvent.getEventIdentifier().getName().toString().equals("oc"))
				{
					onAnnouncementComplete();
				}
				
				break;
			case MgcpEvent.REPORT_FAILURE:
				log.info("########## VOICE MAIL SBB: Signal failed, event identifier["+observedEvent.getEventIdentifier()+"] ##########");
				//releaseState();
				sendByeRequest();
				break;
			default:
				//MGCP RI expects D/dtmfX, but correct is D/X ... hence it fails to match on 
				//MgcpEvent.DTMF_X .... Thus event ID is wrong....
				if(observedEvent.getPackageName().toString().equals("D"))
				{
					int decodedId = decodeDTMF(observedEvent);
					processDTMF(decodedId);
				}else
				{
					log.info("########## VOICE MAIL SBB: Notify on unknown event, event identifier["+observedEvent.getEventIdentifier()+"] identifier["+observedEvent.getEventIdentifier().intValue()+"] ##########");
				}
			}
			
		}

	}
	
	private int decodeDTMF(EventName observed)
	{
		
		String eventName = observed.getEventIdentifier().getName();
		if(Pattern.matches("\\d", eventName))
		{
			//digit
			int i = Integer.parseInt(eventName);
			return MgcpEvent.DTMF_0+i;
		} else if(Pattern.matches("[A-D]#*", eventName))
		{
			switch(eventName.charAt(0))
			{
			case 'A':
				return MgcpEvent.DTMF_A;
				
			case 'B':
				return MgcpEvent.DTMF_B;
				
			case 'C':
				return MgcpEvent.DTMF_C;
				
			case 'D':
				return MgcpEvent.DTMF_D;
					
			case '#':
				return MgcpEvent.DTMF_HASH;
				
			case '*':
				return MgcpEvent.DTMF_STAR;
			
			default:
					return -1;
					
			}
		} else 
		{
			return -1;
		}

	}
	

		
	
	
	private void onAnnouncementComplete() {
		log.info("########## VOICE MAIL SBB: onAnnouncementComplete ##########");

		boolean record = false;
		boolean detectDtmf = true;
		if (this.getSameUser()) {

			
			sendRQNT(null, record, detectDtmf);
		} else {
			ServerTransaction txn = getServerTransaction();
			Request request = txn.getRequest();

			ToHeader toHeader = (ToHeader) request.getHeader(ToHeader.NAME);
			String userName = ((SipURI) toHeader.getAddress().getURI())
					.getUser();

			String recordFilePath = getAudioFileURL(getAudioFileString(userName));

//			if (route != null) {
//				recordFilePath = route+File.separator+fileName;
//			} else {
//				recordFilePath = _DEFAULT_FILE_ROUTE_+File.separator+fileName;
//			}
			
			record = true;
			detectDtmf = false;
			sendRQNT(recordFilePath, record, detectDtmf);
		}
	}

	public void onActivityEndEvent(ActivityEndEvent end,ActivityContextInterface aci)
	{
		log.info("########## VOICE MAIL SBB: onActivityEndEvent["+aci.getActivity()+"] ##########");
	}

	private ServerTransaction getServerTransaction() {
		ActivityContextInterface[] activities = this.getSbbContext()
				.getActivities();
		for (int i = 0; i < activities.length; i++) {
			if (activities[i].getActivity() instanceof ServerTransaction) {
				return (ServerTransaction) activities[i].getActivity();
			}
		}
		return null;
	}

	private Dialog getDialog() {
		ActivityContextInterface[] activities = this.getSbbContext()
				.getActivities();
		for (int i = 0; i < activities.length; i++) {
			if (activities[i].getActivity() instanceof Dialog) {
				return (Dialog) activities[i].getActivity();
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
			SipProvider sipProvider = getSipFactoryProvider();
			Dialog dialog = this.getDialog();
			if(dialog == null)
			{
				return;
			}
			Request request = dialog.createRequest(Request.BYE);
			ClientTransaction ct = sipProvider.getNewClientTransaction(request);

			dialog.sendRequest(ct);

			releaseState();

		} catch (TransactionUnavailableException e) {
			log.severe(e.getMessage(), e);
		} catch (SipException e) {
			log.severe(e.getMessage(), e);
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
			log.severe(e.getMessage(), e);
		}
	}

	/*
	 * Methods below are required for two reasons. Java seems to be loose with URL and File objects:
	 * 
	 * File f1= new File("file:///e:/tmp");
	 * File f2= new File("file://e:/tmp");
	 * File f3= new File("file:/e:/tmp");
	 * File f4= new File("file:///e:/tmp");
	 * 
	 * ONLY f.exists() will return true.
	 * To make it worst: Class.getResource(String) returns URLs... which have look as follows:
	 * 'file:/e:/tmp' which is seems to be a violation of 'file:///'.=.
	 * 
	 * Its better to specify proper one, record signals dont work with 'file:/', announcement 
	 * depends on javax, which seems to be less restrictive
	 * 
	 */
	
	/**
	 * Fectch audio file string for passed user name. Used to get file string when called user is not available.
	 * @param userName
	 * @return
	 */
	private String getAudioFileString(String userName)
	{
		String fileName = userName + WAV_EXT;

		String recordFilePath = System.getenv(_DEFAULT_RECORDINGS_HOME_)
				+ File.separator;
		
		if (route != null) {
			recordFilePath = recordFilePath+route +File.separator+ fileName;
			
		} else {
			//recordFilePath = recordFilePath + fileName;
			recordFilePath = recordFilePath+_DEFAULT_FILE_ROUTE_ +File.separator+ fileName;
		}
		//recordFilePath = new File(recordFilePath).toURI().toString();
		log.info("The File to be played = " + recordFilePath);

		return recordFilePath;
	}
	/**
	 * Fetch audio file string for callee, used to check for recorded message.
	 * @return
	 */
	private String getAudioFileString() {

		FromHeader fromHeader = (FromHeader) this.getInviteRequest().getHeader(
				FromHeader.NAME);

		return getAudioFileString(((SipURI) fromHeader.getAddress().getURI()).getUser());
	}
	
	private String getAudioFileURL(String audioFileString)
	{
		
		if(audioFileString.startsWith("file:///"))
			return audioFileString;
		
		if(audioFileString.startsWith("file://"))
		{
			return audioFileString.replace("file://", "file:///");
		}
		
		if(audioFileString.startsWith("file:/"))
		{
			return audioFileString.replace("file:/", "file:///");
		}
		
		if(audioFileString.startsWith("/"))
		{
			return "file://"+audioFileString;
		}else
		{
			return "file:///"+audioFileString;
		}
	
	}

	private boolean processDTMF(int id) {
		String audioFileURL = null;

		boolean bye = false;
		switch(id)
		{
		case MgcpEvent.DTMF_1:
			String filePath = getAudioFileString();
			File f = new File(filePath);
			boolean exists = f.exists();
			//String audioFileString = "file:/" + filePath;
			//linux stuff?
			
			try {
				// Just to check if file exist
				//File file = new File(filePath);
				if (exists) {
					audioFileURL = getAudioFileURL(filePath);
				} else {
					audioFileURL = getAudioFileURL(getClass().getResource(novoicemessage).toString());
				}
			} catch (NullPointerException npe) {
				log.severe(
						"Ignore. NullPointerException. The file does not exist "
								+ filePath, npe);
				audioFileURL = getAudioFileURL(getClass().getResource(dtmf1).toString());

			} 
			break;
		case MgcpEvent.DTMF_7:
			audioFileURL = getAudioFileURL(getClass().getResource(dtmf7).toString());


			filePath = getAudioFileString();
			File fileToBeDeleted = new File(filePath);
			boolean deleted = fileToBeDeleted.delete();
			log.info("Deletion of file " + filePath + " is successful = "
					+ deleted);
			break;
		case MgcpEvent.DTMF_9:
			// audioFileURL = getClass().getResource(dtmf9);
						this.sendByeRequest();
						bye = true;
			break;
			default:
				log.info("########## VOICE MAIL SBB: Notify on not handled DTMF! ##########");
				audioFileURL = getAudioFileURL(getClass().getResource(tryAgain).toString());
		}

		if (!bye) {

			boolean record = false;
			boolean detectDtmf = false;
			sendRQNT(audioFileURL.toString(), record, detectDtmf);

		}

		return bye;

	}

	/**
	 * To know whether or not the called user has the Voice Mail service
	 * enabled.
	 * 
	 * @param sipAddress
	 *            : Called user address.
	 * @return boolean: TRUE -> Voice Mail enabled. FALSE -> Voice Mail disabled
	 *         for the given user identified by sip address.
	 */
	private boolean isSubscriber(String sipAddress) {
		boolean state = false;
		CallControlProfileCMP profile = lookup(new Address(AddressPlan.SIP,
				sipAddress));
		log.info("Retrieved CallControllProfile["+(profile!=null)+"] for user: "+sipAddress);
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

	public void setSbbContext(SbbContext context) {
		super.setSbbContext(context);
		this.log = getSbbContext().getTracer("VoiceMailSbb");
		// To create Header objects from a particular implementation of JAIN SIP
		headerFactory = getSipFactoryProvider().getHeaderFactory();

		try {
			Context myEnv = (Context) new InitialContext()
					.lookup("java:comp/env");
			// Getting Media Resource Adaptor interfaces
			mgcpProvider = (JainMgcpProvider) myEnv
					.lookup("slee/resources/jainmgcp/2.0/provider");
			mgcpActivityContestInterfaceFactory = (MgcpActivityContextInterfaceFactory) myEnv
					.lookup("slee/resources/jainmgcp/2.0/acifactory");
			// Getting Sip Resource Adaptor interface
			sipACIF = (SipActivityContextInterfaceFactory) myEnv
					.lookup("slee/resources/jainsip/1.2/acifactory");
			// Getting Timer Facility interface

			route = (String) myEnv.lookup("filesRoute");

			log.info("=== Files Route: "+route+" ===");
			mmsBindAddress = (String) myEnv.lookup("server.address");
		} catch (NamingException e) {
			log.severe(e.getMessage(), e);
		}
	}

	public void sbbPostCreate() throws javax.slee.CreateException {

	}
	
	/*
	public abstract org.mobicents.slee.examples.callcontrol.profile.CallControlProfileCMP getCallControlProfileCMP(
			javax.slee.profile.ProfileID profileID)
			throws javax.slee.profile.UnrecognizedProfileNameException,
			javax.slee.profile.UnrecognizedProfileTableNameException;
	*/
	public abstract org.mobicents.slee.examples.callcontrol.voicemail.VoiceMailSbbActivityContextInterface asSbbActivityContextInterface(
			ActivityContextInterface aci);

	private final HeaderFactory getHeaderFactory() {
		return headerFactory;
	}

	private Tracer log;
	
	// Interfaces
	private HeaderFactory headerFactory;

	private SipActivityContextInterfaceFactory sipACIF;
	private MgcpActivityContextInterfaceFactory mgcpActivityContestInterfaceFactory;
	private JainMgcpProvider mgcpProvider;
	//private TimerFacility timerFacility;

	private final String recordAfterTone = "audiofiles/RecordAfterTone.wav";
	private final String waitingDTMF = "audiofiles/WaitingDTMF.wav";
	private final String dtmf1 = "audiofiles/DTMF1.wav";
	private final String dtmf7 = "audiofiles/DTMF7.wav";
	private final String dtmf9 = "audiofiles/DTMF9.wav";
	private final String tryAgain = "audiofiles/TryAgain.wav";
	private final String novoicemessage = "audiofiles/NoVoiceMessage.wav";

	private final String USER = "vmail";
	private final String HOST = System.getProperty("jboss.bind.address",
			"127.0.0.1");

	private final String WAV_EXT = ".wav";

	public final static String IVR_ENDPOINT_NAME = "mobicents/ivr/$";
	// Pre is required since it has capability to transcode
	public final static String PRE_ENDPOINT_NAME = "mobicents/relay/$";
	public final static String _DEFAULT_FILE_ROUTE_ = "call-controll2";
	public final static String _DEFAULT_RECORDINGS_HOME_="MOBICENTS_SLEE_EXAMPLE_CC2_RECORDINGS_HOME";
	
	private String route = null;
	
	protected String mmsBindAddress;
	public final static String JBOSS_BIND_ADDRESS = System.getProperty(
			"jboss.bind.address", "127.0.0.1");
	public static final int MGCP_PEER_PORT = 2427;
	public static final int MGCP_PORT = 2727;


	/**
	 * ***************************************** ************** CMP Fields
	 * *************** *****************************************
	 */

	// 'inviteRequest' CMP field setter
	public abstract void setInviteRequest(Request value);

	// 'inviteRequest' CMP field getter
	public abstract Request getInviteRequest();

	// 'sameUser' CMP field setter
	public abstract void setSameUser(boolean value);

	// 'sameUser' CMP field getter
	public abstract boolean getSameUser();

	public abstract void setCallIdentifier(CallIdentifier cid);

	public abstract CallIdentifier getCallIdentifier();

	// //////////////////
	// Helper Methods //
	// //////////////////
	private MgcpEndpointActivity getEndpointActivity(String ePartialID) {
		for (ActivityContextInterface aci : getSbbContext().getActivities()) {
			if (aci.getActivity() instanceof MgcpEndpointActivity) {

				MgcpEndpointActivity activity = (MgcpEndpointActivity) aci
						.getActivity();
				if (activity.getEndpointIdentifier().toString().toLowerCase().contains(ePartialID.toLowerCase())) {
					return activity;
				}
			}
		}
		return null;
	}

	private DialogActivity getDialogActivity() {
		for (ActivityContextInterface aci : getSbbContext().getActivities()) {
			if (aci.getActivity() instanceof DialogActivity) {

				return (DialogActivity) aci.getActivity();
			}
		}
		return null;
	}

	private void startMailMedia() {

		URL audioFileURL = null;
		boolean waitDtmf = false;
		boolean record = false;
		if (this.getSameUser()) {

			log.info("same user, lets play the voice mail");
			String audioFile = getAudioFileString();
			File file = null;
			boolean fileExist = false;

			try {
				file = new File(audioFile);
				fileExist = file.exists();
			} catch (NullPointerException npe) {
				// Ignore
			}

			if (fileExist) {
				audioFileURL = getClass().getResource(waitingDTMF);
			} else {
				log.info("Mail media file does not exist: "+file);
				audioFileURL = getClass().getResource(novoicemessage);
			}

		} else {
			log.info("not the same user, start recording after announcement");
			audioFileURL = getClass().getResource(recordAfterTone);
		}

		MgcpEndpointActivity mea = getEndpointActivity("ivr");

		log.info("########## VOICE MAIL SBB: Execute on ["
				+ mea
				+ "] ##########");
		String stringURL = getAudioFileURL(audioFileURL.toString());
		sendRQNT(stringURL, record, waitDtmf);
	}

	private MgcpConnectionActivity getConnectionActivity(EndpointIdentifier eid) {
		for (ActivityContextInterface aci : getSbbContext().getActivities()) {
			if (aci.getActivity() instanceof MgcpConnectionActivity) {

				MgcpConnectionActivity activity = (MgcpConnectionActivity) aci
						.getActivity();
				if (activity.getEndpointIdentifier().equals(eid)) {
					return activity;
				}
			}
		}
		return null;
	}

	public void sendRQNT(String audioFileUrl, boolean record, boolean detectDtmf) {
		MgcpEndpointActivity endpointActivity = getEndpointActivity("ivr");

		if (endpointActivity == null) {
			// bad practice
			throw new RuntimeException("There is no IVR endpoint activity");
		}
//		MgcpConnectionActivity connectionActivity = getConnectionActivity(endpointActivity
//				.getEndpointIdentifier());
//		if (connectionActivity == null) {
//			// bad practice
//			throw new RuntimeException(
//					"There is no IVR connection activity");
//		}
		EndpointIdentifier endpointID = endpointActivity
				.getEndpointIdentifier();
//		ConnectionIdentifier connectionID = new ConnectionIdentifier(
//				connectionActivity.getConnectionIdentifier());
		NotificationRequest notificationRequest = new NotificationRequest(this,
				endpointID, mgcpProvider.getUniqueRequestIdentifier());
		RequestedAction[] actions = new RequestedAction[] { RequestedAction.NotifyImmediately };

		
		
		
		if (audioFileUrl != null) {
			EventName[] signalRequests = null;
			if (!record) {

				signalRequests = new EventName[] { new EventName(
						AUPackage.AU, MgcpEvent.factory("pa").withParm("an="+audioFileUrl)/*,connectionID*/) };
				
				RequestedEvent[] requestedEvents = {
						new RequestedEvent(new EventName(AUPackage.AU, MgcpEvent.oc/*,connectionID*/), actions),
						new RequestedEvent(new EventName(AUPackage.AU, MgcpEvent.of/*,connectionID*/), actions), };
				notificationRequest.setRequestedEvents(requestedEvents);
								
			} else {
				signalRequests = new EventName[] { new EventName(AUPackage.AU, MgcpEvent.factory("pr")
						.withParm("ri="+audioFileUrl+" oa=true")/*,connectionID*/) };
				
				RequestedEvent[] requestedEvents = { 
						new RequestedEvent(new EventName(AUPackage.AU, MgcpEvent.oc/*,connectionID*/), actions),
						new RequestedEvent(new EventName(AUPackage.AU, MgcpEvent.of/*,connectionID*/), actions), };
				notificationRequest.setRequestedEvents(requestedEvents);
			}

			notificationRequest.setSignalRequests(signalRequests);
			
			//add notification, in case dtmf part is not included
			

		}
		
		if (detectDtmf) {
			
			RequestedEvent[] requestedEvents = null;

			// This has to be present, since MGCP states that new RQNT erases
			// previous set.
			RequestedEvent[] requestedDtmfEvents = {
					
					new RequestedEvent(new EventName(AUPackage.AU, MgcpEvent.oc/* , connectionID */), actions),
					new RequestedEvent(new EventName(AUPackage.AU, MgcpEvent.of/* , connectionID */), actions),
					new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.factory("0")/* , connectionID */), actions),
					new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.factory("1")/* , connectionID */), actions),
					new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.factory("2")/* , connectionID */), actions),
					new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.factory("3")/* , connectionID */), actions),
					new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.factory("4")/* , connectionID */), actions),
					new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.factory("5")/* , connectionID */), actions),
					new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.factory("6")/* , connectionID */), actions),
					new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.factory("7")/* , connectionID */), actions),
					new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.factory("8")/* , connectionID */), actions),

					new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.factory("9")/* , connectionID */), actions),
					new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.factory("A")/* , connectionID */), actions),
					new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.factory("B")/* , connectionID */), actions),
					new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.factory("C")/* , connectionID */), actions),
					new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.factory("D")/* , connectionID */), actions),
					new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.factory("*")/* , connectionID */), actions),
					new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.factory("#")/* , connectionID */), actions) };

			if(notificationRequest.getRequestedEvents()!=null)
			{
				//we have something
				ArrayList<RequestedEvent> listOfEvents = new ArrayList<RequestedEvent>();
				//add old ones - tahts A or AU
				for(RequestedEvent re:notificationRequest.getRequestedEvents())
				{
					listOfEvents.add(re);
				}
				
				//now dtmfs;
				
				for(RequestedEvent re:requestedDtmfEvents)
				{
					listOfEvents.add(re);
				}
				
				requestedEvents = new RequestedEvent[listOfEvents.size()];
				requestedEvents = listOfEvents.toArray(requestedEvents);
			}else
			{
				requestedEvents = requestedDtmfEvents;
			}
			notificationRequest.setRequestedEvents(requestedEvents);
		}
		
		notificationRequest.setTransactionHandle(mgcpProvider
				.getUniqueTransactionHandler());

		NotifiedEntity notifiedEntity = new NotifiedEntity(JBOSS_BIND_ADDRESS,
				JBOSS_BIND_ADDRESS, MGCP_PORT);
		notificationRequest.setNotifiedEntity(notifiedEntity);

		// we can send empty RQNT, that is clean all req.
		mgcpProvider
				.sendMgcpEvents(new JainMgcpEvent[] { notificationRequest });

		log.info(" NotificationRequest sent: \n" + notificationRequest);
	}

}
