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

/*
 * CallSbb.java
 *
 * The source code contained in this file is in in the public domain.
 * It can be used in any project or product without prior permission,
 * license or royalty payments. There is  NO WARRANTY OF ANY KIND,
 * EXPRESS, IMPLIED OR STATUTORY, INCLUDING, WITHOUT LIMITATION,
 * THE IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE,
 * AND DATA ACCURACY.  We do not warrant or make any representations
 * regarding the use of the software or the  results thereof, including
 * but not limited to the correctness, accuracy, reliability or
 * usefulness of the software.
 */
package org.mobicents.mgcp.demo;

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
import jain.protocol.ip.mgcp.message.parms.ConnectionIdentifier;
import jain.protocol.ip.mgcp.message.parms.ConnectionMode;
import jain.protocol.ip.mgcp.message.parms.EndpointIdentifier;
import jain.protocol.ip.mgcp.message.parms.EventName;
import jain.protocol.ip.mgcp.message.parms.NotifiedEntity;
import jain.protocol.ip.mgcp.message.parms.RequestedAction;
import jain.protocol.ip.mgcp.message.parms.RequestedEvent;
import jain.protocol.ip.mgcp.message.parms.ReturnCode;
import jain.protocol.ip.mgcp.pkg.MgcpEvent;
import jain.protocol.ip.mgcp.pkg.PackageName;

import java.text.ParseException;
import java.util.regex.Pattern;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sip.Dialog;
import javax.sip.InvalidArgumentException;
import javax.sip.RequestEvent;
import javax.sip.ServerTransaction;
import javax.sip.SipException;
import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.header.ContactHeader;
import javax.sip.header.ContentTypeHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.header.ToHeader;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;
import javax.slee.ActivityContextInterface;
import javax.slee.CreateException;
import javax.slee.FactoryException;
import javax.slee.RolledBackContext;
import javax.slee.Sbb;
import javax.slee.SbbContext;
import javax.slee.UnrecognizedActivityException;
import javax.slee.facilities.Tracer;

import org.mobicents.protocols.mgcp.jain.pkg.AUPackage;

import net.java.slee.resource.mgcp.JainMgcpProvider;
import net.java.slee.resource.mgcp.MgcpActivityContextInterfaceFactory;
import net.java.slee.resource.mgcp.MgcpConnectionActivity;
import net.java.slee.resource.mgcp.MgcpEndpointActivity;
import net.java.slee.resource.sip.DialogActivity;
import net.java.slee.resource.sip.SipActivityContextInterfaceFactory;
import net.java.slee.resource.sip.SleeSipProvider;

/**
 * 
 * @author amit bhayani
 */
public abstract class IVRSbb implements Sbb {

	public final static String ENDPOINT_NAME = "mobicents/ivr/$";

	public final static String JBOSS_BIND_ADDRESS = System.getProperty("jboss.bind.address", "127.0.0.1");

	public final static String WELCOME = "http://" + JBOSS_BIND_ADDRESS + ":8080/mgcpdemo/audio/RQNT-ULAW.wav";

	private final static String DTMF_0 = "http://" + JBOSS_BIND_ADDRESS + ":8080/mgcpdemo/audio/ulaw-dtmf0.wav";
	private final static String DTMF_1 = "http://" + JBOSS_BIND_ADDRESS + ":8080/mgcpdemo/audio/ulaw-dtmf1.wav";
	private final static String DTMF_2 = "http://" + JBOSS_BIND_ADDRESS + ":8080/mgcpdemo/audio/ulaw-dtmf2.wav";
	private final static String DTMF_3 = "http://" + JBOSS_BIND_ADDRESS + ":8080/mgcpdemo/audio/ulaw-dtmf3.wav";
	private final static String DTMF_4 = "http://" + JBOSS_BIND_ADDRESS + ":8080/mgcpdemo/audio/ulaw-dtmf4.wav";
	private final static String DTMF_5 = "http://" + JBOSS_BIND_ADDRESS + ":8080/mgcpdemo/audio/ulaw-dtmf5.wav";
	private final static String DTMF_6 = "http://" + JBOSS_BIND_ADDRESS + ":8080/mgcpdemo/audio/ulaw-dtmf6.wav";
	private final static String DTMF_7 = "http://" + JBOSS_BIND_ADDRESS + ":8080/mgcpdemo/audio/ulaw-dtmf7.wav";
	private final static String DTMF_8 = "http://" + JBOSS_BIND_ADDRESS + ":8080/mgcpdemo/audio/ulaw-dtmf8.wav";
	private final static String DTMF_9 = "http://" + JBOSS_BIND_ADDRESS + ":8080/mgcpdemo/audio/ulaw-dtmf9.wav";
	private final static String STAR = "http://" + JBOSS_BIND_ADDRESS + ":8080/mgcpdemo/audio/ulaw-star.wav";
	private final static String POUND = "http://" + JBOSS_BIND_ADDRESS + ":8080/mgcpdemo/audio/ulaw-pound.wav";
	private final static String A = "http://" + JBOSS_BIND_ADDRESS + ":8080/mgcpdemo/audio/ulaw-A.wav";
	private final static String B = "http://" + JBOSS_BIND_ADDRESS + ":8080/mgcpdemo/audio/ulaw-B.wav";
	private final static String C = "http://" + JBOSS_BIND_ADDRESS + ":8080/mgcpdemo/audio/ulaw-C.wav";
	private final static String D = "http://" + JBOSS_BIND_ADDRESS + ":8080/mgcpdemo/audio/ulaw-D.wav";

	private SbbContext sbbContext;

	// SIP
	private SleeSipProvider provider;

	private AddressFactory addressFactory;
	private HeaderFactory headerFactory;
	private MessageFactory messageFactory;
	private SipActivityContextInterfaceFactory acif;

	// MGCP
	private JainMgcpProvider mgcpProvider;
	private MgcpActivityContextInterfaceFactory mgcpAcif;

	public static final int MGCP_PEER_PORT = 2427;
	public static final int MGCP_PORT = 2727;

	private Tracer logger;

	/** Creates a new instance of CallSbb */
	public IVRSbb() {
	}

	public void onCallCreated(RequestEvent evt, ActivityContextInterface aci) {
		Request request = evt.getRequest();

		FromHeader from = (FromHeader) request.getHeader(FromHeader.NAME);
		ToHeader to = (ToHeader) request.getHeader(ToHeader.NAME);

		logger.info("Incoming call " + from + " " + to);

		// create Dialog and attach SBB to the Dialog Activity
		ActivityContextInterface daci = null;
		try {
			Dialog dialog = provider.getNewDialog(evt.getServerTransaction());
			dialog.terminateOnBye(true);
			daci = acif.getActivityContextInterface((DialogActivity) dialog);
			daci.attach(sbbContext.getSbbLocalObject());
		} catch (Exception e) {
			logger.severe("Error during dialog creation", e);
			respond(evt, Response.SERVER_INTERNAL_ERROR);
			return;
		}

		// respond(evt, Response.RINGING);

		CallIdentifier callID = mgcpProvider.getUniqueCallIdentifier();
		this.setCallIdentifier(callID.toString());
		EndpointIdentifier endpointID = new EndpointIdentifier(ENDPOINT_NAME, JBOSS_BIND_ADDRESS + ":" + MGCP_PEER_PORT);

		CreateConnection createConnection = new CreateConnection(this, callID, endpointID, ConnectionMode.SendRecv);

		try {
			String sdp = new String(evt.getRequest().getRawContent());
			createConnection.setRemoteConnectionDescriptor(new ConnectionDescriptor(sdp));
		} catch (ConflictingParameterException e) {
			// should never happen
		}

		int txID = mgcpProvider.getUniqueTransactionHandler();
		createConnection.setTransactionHandle(txID);

		MgcpConnectionActivity connectionActivity = null;
		try {
			connectionActivity = mgcpProvider.getConnectionActivity(txID, endpointID);
			ActivityContextInterface epnAci = mgcpAcif.getActivityContextInterface(connectionActivity);
			epnAci.attach(sbbContext.getSbbLocalObject());
		} catch (FactoryException ex) {
			ex.printStackTrace();
		} catch (NullPointerException ex) {
			ex.printStackTrace();
		} catch (UnrecognizedActivityException ex) {
			ex.printStackTrace();
		}

		mgcpProvider.sendMgcpEvents(new JainMgcpEvent[] { createConnection });
	}

	public void onCreateConnectionResponse(CreateConnectionResponse event, ActivityContextInterface aci) {
		logger.info("Receive CRCX response: " + event.getTransactionHandle());

		ServerTransaction txn = getServerTransaction();
		Request request = txn.getRequest();

		ReturnCode status = event.getReturnCode();

		switch (status.getValue()) {
		case ReturnCode.TRANSACTION_EXECUTED_NORMALLY:

			this.setEndpointName(event.getSpecificEndpointIdentifier().getLocalEndpointName());
			logger.info("***&& " + this.getEndpointName());

			ConnectionIdentifier connectionIdentifier = event.getConnectionIdentifier();

			this.setConnectionIdentifier(connectionIdentifier.toString());
			String sdp = event.getLocalConnectionDescriptor().toString();

			ContentTypeHeader contentType = null;
			try {
				contentType = headerFactory.createContentTypeHeader("application", "sdp");
			} catch (ParseException ex) {
			}

			String localAddress = provider.getListeningPoints()[0].getIPAddress();
			int localPort = provider.getListeningPoints()[0].getPort();

			Address contactAddress = null;
			try {
				contactAddress = addressFactory.createAddress("sip:" + localAddress + ":" + localPort);
			} catch (ParseException ex) {
			}
			ContactHeader contact = headerFactory.createContactHeader(contactAddress);

			sendRQNT(WELCOME, true);

			Response response = null;
			try {
				response = messageFactory.createResponse(Response.OK, request, contentType, sdp.getBytes());
			} catch (ParseException ex) {
				logger.severe("ParseException while trying to create OK Response", ex);
			}

			response.setHeader(contact);
			try {
				txn.sendResponse(response);
			} catch (InvalidArgumentException ex) {
				logger.severe("InvalidArgumentException while trying to send OK Response", ex);
			} catch (SipException ex) {
				logger.severe("SipException while trying to send OK Response", ex);
			}
			break;
		default:
			try {
				response = messageFactory.createResponse(Response.SERVER_INTERNAL_ERROR, request);
				txn.sendResponse(response);
			} catch (Exception ex) {
				logger.severe("Exception while trying to send SERVER_INTERNAL_ERROR Response", ex);
			}
		}
	}

	private void sendRQNT(String mediaPath, boolean createActivity) {
		EndpointIdentifier endpointID = new EndpointIdentifier(this.getEndpointName(), JBOSS_BIND_ADDRESS + ":"
				+ MGCP_PEER_PORT);

		NotificationRequest notificationRequest = new NotificationRequest(this, endpointID, mgcpProvider
				.getUniqueRequestIdentifier());
		
		//ConnectionIdentifier connectionIdentifier = new ConnectionIdentifier(this.getConnectionIdentifier());
		
		EventName[] signalRequests = { new EventName(AUPackage.AU, MgcpEvent.factory("pa").withParm("an="+mediaPath/*+" mn=1"*/)/* , connectionIdentifier */) };
		notificationRequest.setSignalRequests(signalRequests);

		RequestedAction[] actions = new RequestedAction[] { RequestedAction.NotifyImmediately };

		RequestedEvent[] requestedEvents = {
				new RequestedEvent(new EventName(AUPackage.AU, MgcpEvent.oc/* , connectionIdentifier */), actions),
				new RequestedEvent(new EventName(AUPackage.AU, MgcpEvent.of/* , connectionIdentifier */), actions),
				new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.factory("0")/* , connectionIdentifier */), actions),
				new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.factory("1")/* , connectionIdentifier */), actions),
				new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.factory("2")/* , connectionIdentifier */), actions),
				new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.factory("3")/* , connectionIdentifier */), actions),
				new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.factory("4")/* , connectionIdentifier */), actions),
				new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.factory("5")/* , connectionIdentifier */), actions),
				new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.factory("6")/* , connectionIdentifier */), actions),
				new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.factory("7")/* , connectionIdentifier */), actions),
				new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.factory("8")/* , connectionIdentifier */), actions),

				new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.factory("9")/* , connectionIdentifier */), actions),
				new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.factory("A")/* , connectionIdentifier */), actions),
				new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.factory("B")/* , connectionIdentifier */), actions),
				new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.factory("C")/* , connectionIdentifier */), actions),
				new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.factory("D")/* , connectionIdentifier */), actions),
				new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.factory("*")/* , connectionIdentifier */), actions),
				new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.factory("#")/* , connectionIdentifier */), actions) };

		notificationRequest.setRequestedEvents(requestedEvents);
		notificationRequest.setTransactionHandle(mgcpProvider.getUniqueTransactionHandler());

		NotifiedEntity notifiedEntity = new NotifiedEntity(JBOSS_BIND_ADDRESS, JBOSS_BIND_ADDRESS, MGCP_PORT);
		notificationRequest.setNotifiedEntity(notifiedEntity);

		if (createActivity) {
			MgcpEndpointActivity endpointActivity = null;
			try {
				endpointActivity = mgcpProvider.getEndpointActivity(endpointID);
				ActivityContextInterface epnAci = mgcpAcif.getActivityContextInterface(endpointActivity);
				epnAci.attach(sbbContext.getSbbLocalObject());
			} catch (FactoryException ex) {
				ex.printStackTrace();
			} catch (NullPointerException ex) {
				ex.printStackTrace();
			} catch (UnrecognizedActivityException ex) {
				ex.printStackTrace();
			}
		} // if (createActivity)

		mgcpProvider.sendMgcpEvents(new JainMgcpEvent[] { notificationRequest });

		logger.info("============================= \nNotificationRequest sent:\n"+notificationRequest+"\n=============================");
	}

	public void onNotificationRequestResponse(NotificationRequestResponse event, ActivityContextInterface aci) {
		logger.info("onNotificationRequestResponse");

		ReturnCode status = event.getReturnCode();

		switch (status.getValue()) {
		case ReturnCode.TRANSACTION_EXECUTED_NORMALLY:
			logger.info("The Announcement should have been started");
			break;
		default:
			ReturnCode rc = event.getReturnCode();
			logger.severe("RQNT failed. Value = " + rc.getValue() + " Comment = " + rc.getComment());

			// TODO : Send DLCX to MMS. Send BYE to UA
			break;
		}

	}

	public void onNotifyRequest(Notify event, ActivityContextInterface aci) {
		logger.info("onNotifyRequest:\n"+event);

		 NotifyResponse response = new  NotifyResponse(event.getSource(),
				ReturnCode.Transaction_Executed_Normally);
		response.setTransactionHandle(event.getTransactionHandle());

		mgcpProvider.sendMgcpEvents(new JainMgcpEvent[] { response });

		EventName[] observedEvents = event.getObservedEvents();

		for (EventName observedEvent : observedEvents) {
			
			switch (observedEvent.getEventIdentifier().intValue()) {
			case MgcpEvent.REPORT_ON_COMPLETION:
				logger.info("Announcemnet Completed NTFY received");
				break;
			case MgcpEvent.REPORT_FAILURE:
				logger.info("Announcemnet Failed received");
				// TODO : Send DLCX and Send BYE to UA
				break;
			default:
				//MGCP RI expects D/dtmfX, but correct is D/X ... hence it fails to match on 
				//MgcpEvent.DTMF_X .... Thus event ID is wrong....
				if(observedEvent.getPackageName().toString().equals("D"))
				{
					int decodedId = decodeDTMF(observedEvent);
					processDTMF(decodedId);
				}
				
			
			}
		}
	}

	public void onCallTerminated(RequestEvent evt, ActivityContextInterface aci) {
		EndpointIdentifier endpointID = new EndpointIdentifier(this.getEndpointName(), JBOSS_BIND_ADDRESS + ":"
				+ MGCP_PEER_PORT);
		DeleteConnection deleteConnection = new DeleteConnection(this, endpointID);

		deleteConnection.setTransactionHandle(mgcpProvider.getUniqueTransactionHandler());
		mgcpProvider.sendMgcpEvents(new JainMgcpEvent[] { deleteConnection });

		ServerTransaction tx = evt.getServerTransaction();
		Request request = evt.getRequest();

		try {
			Response response = messageFactory.createResponse(Response.OK, request);
			tx.sendResponse(response);
		} catch (Exception e) {
			logger.severe("Error while sending DLCX ", e);
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
	
	private void processDTMF(int id)
	{
		switch(id)
		{
			case MgcpEvent.DTMF_0:
				logger.info("You have pressed 0");
				sendRQNT(DTMF_0, false);
				break;
			case MgcpEvent.DTMF_1:
				logger.info("You have pressed 1");
				sendRQNT(DTMF_1, false);
				break;
			case MgcpEvent.DTMF_2:
				logger.info("You have pressed 2");
				sendRQNT(DTMF_2, false);
				break;
			case MgcpEvent.DTMF_3:
				logger.info("You have pressed 3");
				sendRQNT(DTMF_3, false);
				break;
			case MgcpEvent.DTMF_4:
				logger.info("You have pressed 4");
				sendRQNT(DTMF_4, false);
				break;
			case MgcpEvent.DTMF_5:
				logger.info("You have pressed 5");
				sendRQNT(DTMF_5, false);
				break;
			case MgcpEvent.DTMF_6:
				logger.info("You have pressed 6");
				sendRQNT(DTMF_6, false);
				break;
			case MgcpEvent.DTMF_7:
				logger.info("You have pressed 7");
				sendRQNT(DTMF_7, false);
				break;
			case MgcpEvent.DTMF_8:
				logger.info("You have pressed 8");
				sendRQNT(DTMF_8, false);
				break;
			case MgcpEvent.DTMF_9:
				logger.info("You have pressed 9");
				sendRQNT(DTMF_9, false);
				break;
			case MgcpEvent.DTMF_A:
				logger.info("You have pressed A");
				sendRQNT(A, false);
				break;
			case MgcpEvent.DTMF_B:
				logger.info("You have pressed B");
				sendRQNT(B, false);
				break;
			case MgcpEvent.DTMF_C:
				logger.info("You have pressed C");
				sendRQNT(C, false);
				break;
			case MgcpEvent.DTMF_D:
				logger.info("You have pressed D");
				sendRQNT(D, false);
	
				break;
			case MgcpEvent.DTMF_STAR:
				logger.info("You have pressed *");
				sendRQNT(STAR, false);
	
				break;
			case MgcpEvent.DTMF_HASH:
				logger.info("You have pressed C");
				sendRQNT(POUND, false);
	
				break;
		}
	}

	private void respond(RequestEvent evt, int cause) {
		Request request = evt.getRequest();
		ServerTransaction tx = evt.getServerTransaction();
		try {
			Response response = messageFactory.createResponse(cause, request);
			tx.sendResponse(response);
		} catch (Exception e) {
			logger.warning("Unexpected error: ", e);
		}
	}

	private ServerTransaction getServerTransaction() {
		ActivityContextInterface[] activities = sbbContext.getActivities();
		for (ActivityContextInterface activity : activities) {
			if (activity.getActivity() instanceof ServerTransaction) {
				return (ServerTransaction) activity.getActivity();
			}
		}
		return null;
	}

	public void setSbbContext(SbbContext sbbContext) {
		this.sbbContext = sbbContext;
		this.logger = sbbContext.getTracer(IVRSbb.class.getSimpleName());

		try {
			Context ctx = (Context) new InitialContext().lookup("java:comp/env");

			// initialize SIP API
			provider = (SleeSipProvider) ctx.lookup("slee/resources/jainsip/1.2/provider");

			addressFactory = provider.getAddressFactory();
			headerFactory = provider.getHeaderFactory();
			messageFactory = provider.getMessageFactory();
			acif = (SipActivityContextInterfaceFactory) ctx.lookup("slee/resources/jainsip/1.2/acifactory");

			// initialize media api

			mgcpProvider = (JainMgcpProvider) ctx.lookup("slee/resources/jainmgcp/2.0/provider/demo");
			mgcpAcif = (MgcpActivityContextInterfaceFactory) ctx.lookup("slee/resources/jainmgcp/2.0/acifactory/demo");

		} catch (Exception ne) {
			logger.severe("Could not set SBB context:", ne);
		}
	}

	public abstract String getConnectionIdentifier();

	public abstract void setConnectionIdentifier(String connectionIdentifier);

	public abstract String getCallIdentifier();

	public abstract void setCallIdentifier(String CallIdentifier);

	public abstract String getRemoteSdp();

	public abstract void setRemoteSdp(String remoteSdp);

	public abstract String getEndpointName();

	public abstract void setEndpointName(String endpointName);

	public void unsetSbbContext() {
		this.sbbContext = null;
		this.logger = null;
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

	public void sbbExceptionThrown(Exception exception, Object object, ActivityContextInterface activityContextInterface) {
	}

	public void sbbRolledBack(RolledBackContext rolledBackContext) {
	}
}
