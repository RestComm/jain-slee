/*
 * Mobicents: The Open Source SLEE Platform      
 *
 * Copyright 2003-2005, CocoonHive, LLC., 
 * and individual contributors as indicated
 * by the @authors tag. See the copyright.txt 
 * in the distribution for a full listing of   
 * individual contributors.
 *
 * This is free software; you can redistribute it
 * and/or modify it under the terms of the 
 * GNU Lesser General Public License as
 * published by the Free Software Foundation; 
 * either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that 
 * it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR 
 * PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the 
 * GNU Lesser General Public
 * License along with this software; 
 * if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, 
 * Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site:
 * http://www.fsf.org.
 */

package org.mobicents.slee.service.callcontrol;

import gov.nist.javax.sip.header.CSeq;
import jain.protocol.ip.mgcp.JainMgcpEvent;
import jain.protocol.ip.mgcp.JainMgcpResponseEvent;
import jain.protocol.ip.mgcp.message.CreateConnection;
import jain.protocol.ip.mgcp.message.CreateConnectionResponse;
import jain.protocol.ip.mgcp.message.DeleteConnection;
import jain.protocol.ip.mgcp.message.NotificationRequest;
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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sip.ClientTransaction;
import javax.sip.Dialog;
import javax.sip.InvalidArgumentException;
import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.SipException;
import javax.sip.TransactionUnavailableException;
import javax.sip.address.Address;
import javax.sip.address.SipURI;
import javax.sip.header.CallIdHeader;
import javax.sip.header.ContactHeader;
import javax.sip.header.ContentTypeHeader;
import javax.sip.header.Header;
import javax.sip.header.ViaHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;
import javax.slee.ActivityContextInterface;
import javax.slee.FactoryException;
import javax.slee.InitialEventSelector;
import javax.slee.RolledBackContext;
import javax.slee.SbbContext;
import javax.slee.SbbLocalObject;
import javax.slee.UnrecognizedActivityException;
import javax.slee.facilities.Tracer;

import net.java.slee.resource.mgcp.JainMgcpProvider;
import net.java.slee.resource.mgcp.MgcpActivityContextInterfaceFactory;
import net.java.slee.resource.mgcp.MgcpConnectionActivity;
import net.java.slee.resource.mgcp.MgcpEndpointActivity;
import net.java.slee.resource.sip.DialogActivity;
import net.java.slee.resource.sip.SipActivityContextInterfaceFactory;
import net.java.slee.resource.sip.SleeSipProvider;

import org.mobicents.slee.service.common.CommonSbb;
import org.mobicents.slee.service.common.SimpleCallFlowRequestState;
import org.mobicents.slee.service.common.SimpleCallFlowResponseState;
import org.mobicents.slee.service.common.SimpleCallFlowState;
import org.mobicents.slee.service.events.CustomEvent;
import org.mobicents.slee.util.Session;
import org.mobicents.slee.util.SessionAssociation;
import org.mobicents.slee.util.StateCallback;

public abstract class CallControlSbb extends CommonSbb {
	private Tracer log = null;

	public final static String ENDPOINT_NAME = "/mobicents/media/packetrelay/$";

	public final static String IVR_ENDPOINT = "/mobicents/media/IVR/$";

	protected String mmsBindAddress;

	private String callControlSipAddress;

	private String password;

	public abstract ResponseEvent getResponseEventCmp();

	public abstract void setResponseEventCmp(ResponseEvent evt);

	public abstract void setParentCmp(SbbLocalObject sbbLocalObject);

	public abstract SbbLocalObject getParentCmp();

	public abstract void setCustomEventCmp(CustomEvent sbbLocalObject);

	public abstract CustomEvent getCustomEventCmp();

	public void setCustomEvent(CustomEvent event) {
		setCustomEventCmp(event);
	}

	public void setParent(SbbLocalObject sbbLocalObject) {
		setParentCmp(sbbLocalObject);
	}

	public ResponseEvent getResponseEvent() {
		return getResponseEventCmp();
	}

	public abstract void setCallIdentifierCmp(CallIdentifier cid);

	public abstract CallIdentifier getCallIdentifierCmp();

	public void setCallIdentifier(CallIdentifier cid) {
		this.setCallIdentifierCmp(cid);
	}

	public CallIdentifier getCallIdentifier() {
		return this.getCallIdentifierCmp();
	}

	public abstract void setSendByeCmp(boolean isBye);

	public abstract boolean getSendByeCmp();

	public void setSendBye(boolean isBye) {
		this.setSendByeCmp(isBye);
	}

	public boolean getSendBye() {
		return this.getSendByeCmp();
	}

	/**
	 * Generate a custom convergence name so that events with the same call
	 * identifier will go to the same root SBB entity. For other methods, use
	 * ActivityContext.
	 */
	public InitialEventSelector callIdSelect(InitialEventSelector ies) {
		log.info("***************     callIdSelect     ***************");
		Object event = ies.getEvent();
		String callId = null;
		if (event instanceof ResponseEvent) {
			// If response event, the convergence name to callId
			Response response = ((ResponseEvent) event).getResponse();
			callId = ((CallIdHeader) response.getHeader(CallIdHeader.NAME))
					.getCallId();
		} else if (event instanceof RequestEvent) {
			// If request event, the convergence name to callId
			Request request = ((RequestEvent) event).getRequest();
			callId = ((CallIdHeader) request.getHeader(CallIdHeader.NAME))
					.getCallId();
		} else {
			// If something else, use activity context.
			ies.setActivityContextSelected(true);
			return ies;
		}
		// Set the convergence name
		if (log.isFineEnabled()) {
			log.fine("Setting convergence name to: " + callId);
		}
		ies.setCustomName(callId);
		return ies;
	}

	private void executeRequestState(RequestEvent event) {
		String callId = ((CallIdHeader) event.getRequest().getHeader(
				CallIdHeader.NAME)).getCallId();
		SessionAssociation sa = (SessionAssociation) getCacheUtility().get(
				callId);
		SimpleCallFlowState simpleCallFlowState = getState(sa.getState());
		simpleCallFlowState.execute(event);
	}

	private void executeResponseState(ResponseEvent event) {
		String callId = ((CallIdHeader) event.getResponse().getHeader(
				CallIdHeader.NAME)).getCallId();
		SessionAssociation sa = (SessionAssociation) getCacheUtility().get(
				callId);
		SimpleCallFlowState simpleCallFlowState = getState(sa.getState());
		simpleCallFlowState.execute(event);
	}

	public void onByeEvent(RequestEvent event, ActivityContextInterface aci) {

		log.info("************Received BYEEEE**************");

		if (log.isFineEnabled()) {
			log.fine("Received BYE");
		}

		try {
			getSipUtils().sendStatefulOk(event);
			setSendBye(false);
			releaseMediaConnectionAndDialog();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (SipException e) {
			e.printStackTrace();
		} catch (InvalidArgumentException invalidArgEx) {
			invalidArgEx.printStackTrace();
		}
	}

	public void onClientErrorRespEvent(ResponseEvent event,
			ActivityContextInterface aci) {
		if (log.isInfoEnabled()) {
			log.info("Received client error event : "
					+ event.getResponse().getStatusCode());
		}
		executeResponseState(event);
	}

	public void onSuccessRespEvent(ResponseEvent event,
			ActivityContextInterface aci) {
		if (log.isInfoEnabled()) {
			log.info("Received success response event "
					+ event.getResponse().getStatusCode());
		}
		executeResponseState(event);
		if (((CSeq) event.getResponse().getHeader(CSeq.NAME)).getMethod()
				.compareTo(Request.BYE) == 0)
			releaseMediaConnectionAndDialog();
		if (((CSeq) event.getResponse().getHeader(CSeq.NAME)).getMethod()
				.compareTo(Request.INVITE) == 0)
			this.setSendByeCmp(true);

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

			if (this.getSendByeCmp()) {
				this.sendBye();
			}

			return;
		}
		boolean attachParent = false;
		if (event.getSecondEndpointIdentifier() == null) {
			// this is response for PR creation
			// we have one connection activity, lets send another crcx
			
			//send ACK with sdp
			DialogActivity da=getDialogActivity();
			try {
				Request ackRequest=da.createAck(da.getLocalSeqNumber());
				ContentTypeHeader cth = ((SleeSipProvider)getSipProvider()).getHeaderFactory().createContentTypeHeader("application", "sdp");
				ackRequest.setContent(event.getLocalConnectionDescriptor().toString(), cth);
				da.sendAck(ackRequest);
			} catch (InvalidArgumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SipException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			EndpointIdentifier endpointID = new EndpointIdentifier(
					IVR_ENDPOINT, mmsBindAddress + ":" + MGCP_PEER_PORT);
			CreateConnection createConnection = new CreateConnection(this,
					getCallIdentifier(), endpointID, ConnectionMode.SendRecv);

			int txID = ((JainMgcpProvider) getMgcpProvider())
					.getUniqueTransactionHandler();
			createConnection.setTransactionHandle(txID);

			// now set other end
			try {
				createConnection.setSecondEndpointIdentifier(event
						.getSpecificEndpointIdentifier());
			} catch (ConflictingParameterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			MgcpConnectionActivity connectionActivity = null;
			try {
				connectionActivity = ((JainMgcpProvider) getMgcpProvider())
						.getConnectionActivity(txID, endpointID);
				ActivityContextInterface epnAci = ((MgcpActivityContextInterfaceFactory) getMgcpActivityContestInterfaceFactory())
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

			((JainMgcpProvider) getMgcpProvider())
					.sendMgcpEvents(new JainMgcpEvent[] { createConnection });

		} else {
			// this is last
			attachParent = true;
		}
		EndpointIdentifier eid = event.getSpecificEndpointIdentifier();
		log.info("Creating endpoint activity on: " + eid);
		MgcpEndpointActivity eActivity = ((JainMgcpProvider) getMgcpProvider())
				.getEndpointActivity(eid);
		ActivityContextInterface eAci = ((MgcpActivityContextInterfaceFactory) getMgcpActivityContestInterfaceFactory())
				.getActivityContextInterface(eActivity);
		eAci.attach(this.getSbbContext().getSbbLocalObject());
		// eAci.attach(this.getParentCmp());

		if (attachParent) {
			// we must attach parent only if we know media path is there, it
			// will get this last event and trigger.
			// this is cause parent always gets event FIRST.
			ActivityContextInterface[] acis = this.getSbbContext()
					.getActivities();
			for (ActivityContextInterface _aci : acis) {
				if (_aci.getActivity() instanceof MgcpEndpointActivity
						|| _aci.getActivity() instanceof MgcpConnectionActivity) {
					_aci.attach(getParentCmp());
				}
			}

		}

	}

	public void sendRQNT(String textToPlay, String audioFileUrl,
			boolean detectDtmf) {
		MgcpEndpointActivity endpointActivity = getEndpointActivity("IVR");
		
		if (endpointActivity == null) {
			// bad practice
			throw new RuntimeException("There is no IVR endpoint activity");
		}
		
		EndpointIdentifier endpointID = endpointActivity
				.getEndpointIdentifier();
		MgcpConnectionActivity connectionActivity = getConnectionActivity(endpointActivity.getEndpointIdentifier());
		if (connectionActivity == null) {
			// bad practice
			throw new RuntimeException("There is no IVR connection endpoint activity");
		}
		ConnectionIdentifier connectionID = new ConnectionIdentifier(
				connectionActivity.getConnectionIdentifier());
		NotificationRequest notificationRequest = new NotificationRequest(this,
				endpointID, ((JainMgcpProvider) getMgcpProvider())
						.getUniqueRequestIdentifier());
		RequestedAction[] actions = new RequestedAction[] { RequestedAction.NotifyImmediately };
		PackageName auPackageName=PackageName.factory("AU");
		if (textToPlay != null) {

			// this will give something like: "AU/pa(ts("+textToPlay+"))", AU and pa are configured in mgcp controller.
			
			MgcpEvent e = MgcpEvent.factory("ann");
			EventName[] signalRequests = { new EventName(auPackageName, e.withParm("ts(" + textToPlay
					+ ")"),connectionID) };
			notificationRequest.setSignalRequests(signalRequests);
			
			
			RequestedEvent[] requestedEvents = {
					new RequestedEvent(new EventName(auPackageName, MgcpEvent.oc,connectionID), actions),
					new RequestedEvent(new EventName(auPackageName, MgcpEvent.of,connectionID), actions),
					 };
			notificationRequest.setRequestedEvents(requestedEvents);
		} else if (audioFileUrl != null) {
			MgcpEvent e = MgcpEvent.factory("ann");
			EventName[] signalRequests = { new EventName(auPackageName, e.withParm(audioFileUrl),connectionID) };
			
			notificationRequest.setSignalRequests(signalRequests);
			RequestedEvent[] requestedEvents = {
					new RequestedEvent(new EventName(auPackageName, MgcpEvent.oc,connectionID), actions),
					new RequestedEvent(new EventName(auPackageName, MgcpEvent.of,connectionID), actions),
					 };
			notificationRequest.setRequestedEvents(requestedEvents);
		}

		if (detectDtmf) {
			
			
			

			// This has to be present, since MGCP states that new RQNT erases
			// previous set.
			RequestedEvent[] requestedEvents = {
					new RequestedEvent(new EventName(auPackageName, MgcpEvent.oc,connectionID), actions),
					new RequestedEvent(new EventName(auPackageName, MgcpEvent.of,connectionID), actions),
					new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.dtmf0,connectionID), actions),
					new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.dtmf1,connectionID), actions),
					new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.dtmf2,connectionID), actions),
					new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.dtmf3,connectionID), actions),
					new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.dtmf4,connectionID), actions),
					new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.dtmf5,connectionID), actions),
					new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.dtmf6,connectionID), actions),
					new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.dtmf7,connectionID), actions),
					new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.dtmf8,connectionID), actions),
					new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.dtmf9,connectionID), actions),
					new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.dtmfA,connectionID), actions),
					new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.dtmfB,connectionID), actions),
					new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.dtmfC,connectionID), actions),
					new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.dtmfD,connectionID), actions),
					new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.dtmfStar,connectionID), actions),
					new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.dtmfHash,connectionID), actions) };
			
			notificationRequest.setRequestedEvents(requestedEvents);
		}
		notificationRequest
				.setTransactionHandle(((JainMgcpProvider) getMgcpProvider())
						.getUniqueTransactionHandler());

		NotifiedEntity notifiedEntity = new NotifiedEntity(JBOSS_BIND_ADDRESS,
				JBOSS_BIND_ADDRESS, MGCP_PORT);
		notificationRequest.setNotifiedEntity(notifiedEntity);

		// we can send empty RQNT, that is clean all req.
		((JainMgcpProvider) getMgcpProvider())
				.sendMgcpEvents(new JainMgcpEvent[] { notificationRequest });

		log.info(" NotificationRequest sent: \n"+notificationRequest);
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
	private MgcpEndpointActivity getEndpointActivity(String ePartialID) {
		for (ActivityContextInterface aci : getSbbContext().getActivities()) {
			if (aci.getActivity() instanceof MgcpEndpointActivity) {
				
				MgcpEndpointActivity activity = (MgcpEndpointActivity) aci
						.getActivity();
				if (activity.getEndpointIdentifier().toString().toLowerCase()
						.contains(ePartialID.toLowerCase())) {
					return activity;
				}
			} 
		}
		return null;
	}
	private DialogActivity getDialogActivity() {
		for (ActivityContextInterface aci : getSbbContext().getActivities()) {
			if (aci.getActivity() instanceof DialogActivity) {
				
				return (DialogActivity)aci.getActivity();
			} 
		}
		return null;
	}
	private void releaseMediaConnectionAndDialog() {
		ActivityContextInterface[] activities = getSbbContext().getActivities();
		SbbLocalObject sbbLocalObject = getSbbContext().getSbbLocalObject();

		for (ActivityContextInterface attachedAci : activities) {
			if (attachedAci.getActivity() instanceof Dialog) {
				attachedAci.detach(sbbLocalObject);
				attachedAci.detach(this.getParentCmp());
			}
			if (attachedAci.getActivity() instanceof MgcpConnectionActivity) {
				attachedAci.detach(sbbLocalObject);
				attachedAci.detach(this.getParentCmp());

			}
			if (attachedAci.getActivity() instanceof MgcpEndpointActivity) {
				attachedAci.detach(sbbLocalObject);
				attachedAci.detach(this.getParentCmp());
				MgcpEndpointActivity mgcpEndpoint = (MgcpEndpointActivity) attachedAci
						.getActivity();
				DeleteConnection deleteConnection = new DeleteConnection(this,
						mgcpEndpoint.getEndpointIdentifier());
				deleteConnection.setCallIdentifier(this.getCallIdentifier());

				deleteConnection.setTransactionHandle(getMgcpProvider()
						.getUniqueTransactionHandler());
				getMgcpProvider().sendMgcpEvents(
						new JainMgcpEvent[] { deleteConnection });

			}

		}
		this.setCallIdentifier(null);
	}

	public void sendBye() {

		// releaseMediaConnectionAndDialog();

		try {
			Dialog dialog = getSipUtils().getDialog(getResponseEventCmp());
			sendRequest(dialog, Request.BYE);
			this.setSendByeCmp(false);
		} catch (SipException e) {
			log.severe("Error sending BYE", e);
		}
	}

	// TODO: Perform further operations if required in these methods.
	public void setSbbContext(SbbContext context) {
		this.log = context.getTracer("CallControl");
		super.setSbbContext(context);

		try {

			Context myEnv = (Context) new InitialContext()
					.lookup("java:comp/env");

			// Check that callControlSipAddress and password are present
			callControlSipAddress = (String) myEnv
					.lookup("callControlSipAddress");
			password = (String) myEnv.lookup("password");
			String passwordDisplay = password == null ? "null" : "*******";
			mmsBindAddress = (String) myEnv.lookup("server.address");
			if (log.isInfoEnabled()) {
				log
						.info("Checking that CallControlSbb callControlSipAddress and password are present : callControlSipAddress = "
								+ callControlSipAddress
								+ " password = "
								+ passwordDisplay);
			}

		} catch (NamingException ne) {
			ne.printStackTrace();
		}
	}

	public void unsetSbbContext() {
		super.unsetSbbContext();
	}

	// TODO: Implement the lifecycle methods if required
	public void sbbCreate() throws javax.slee.CreateException {
	}

	public void sbbPostCreate() throws javax.slee.CreateException {
	}

	public void sbbActivate() {
	}

	public void sbbPassivate() {
	}

	public void sbbRemove() {
	}

	public void sbbLoad() {
	}

	public void sbbStore() {
	}

	public void sbbExceptionThrown(Exception exception, Object event,
			ActivityContextInterface activity) {
	}

	public void sbbRolledBack(RolledBackContext context) {
	}

	/**
	 * Convenience method to retrieve the SbbContext object stored in
	 * setSbbContext. TODO: If your SBB doesn't require the SbbContext object
	 * you may remove this method, the sbbContext variable and the variable
	 * assignment in setSbbContext().
	 * 
	 * @return this SBB's SbbContext object
	 */

	protected SbbContext getSbbContext() {
		return super.getSbbContext();
	}

	/**
	 * Store the client transaction in the cache since we may need to send a
	 * cancel request associated with this ClientTransaction later.
	 * 
	 * @param The
	 *            client transaction to store as "to be cancelled".
	 */
	private void setToBeCancelledClientTransaction(ClientTransaction ct) {
		String callId = ((CallIdHeader) ct.getRequest().getHeader(
				CallIdHeader.NAME)).getCallId();
		SessionAssociation sa = (SessionAssociation) getCacheUtility().get(
				callId);

		if (sa != null) {
			Session session = sa.getSession(callId);
			session.setToBeCancelledClientTransaction(ct);
		}
	}

	private SimpleCallFlowState getState(String classNameForState) {
		SimpleCallFlowState simpleCallFlowState = null;
		try {
			Class innerCls = Class.forName(classNameForState);
			Constructor c = innerCls.getDeclaredConstructors()[0];
			simpleCallFlowState = (SimpleCallFlowState) c
					.newInstance(new Object[] { this });
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return simpleCallFlowState;
	}

	public abstract class StateSupport implements SimpleCallFlowState {
		private String statusMessage;

		public StateSupport(String statusMessage) {
			this.statusMessage = statusMessage;
		}

		public String getStatusMessage() {
			return statusMessage;
		}
	}

	public abstract class BeforeCalleeConfirmedState extends
			SimpleCallFlowResponseState {
		// TODO Handle exception
		public void handleTrying(String calleeCallId, ResponseEvent event) {

			setState(new CalleeTryingState(), calleeCallId);
		}

		// TODO Handle exception
		public void handleRinging(String calleeCallId, ResponseEvent event) {

			setState(new CalleeRingingState(), calleeCallId);
		}

		public void handleOK(String calleeCallId, ResponseEvent event) {

			log.info("BeforeCalleeConfirmedState.handleOK");

			setResponseEventCmp(event);
			Dialog currentDialog = null;

			// Check that the dialog of the event is the same as in the session
			// If not this is the result of a fork, we don't handle this
			try {
				Dialog eventDialog = getSipUtils().getDialog(event);
				currentDialog = getDialog(eventDialog.getCallId().getCallId());
				if (!eventDialog.equals(currentDialog)) {
					log.warning("Received 200 response from forked dialog");
					return; // We don't currently handle this. Should send ACK
					// and BYE
				}
			} catch (SipException e) {
				log.severe("SipException while trying to retreive Dialog", e);
			}

			// Let us attach sbbLocalObject to Dialog to receive Bye latter
			try {

				ActivityContextInterface daci = ((SipActivityContextInterfaceFactory) getSipActivityContextInterfaceFactory())
						.getActivityContextInterface((DialogActivity) currentDialog);
				daci.attach(getSbbContext().getSbbLocalObject());
			} catch (Exception e) {
				e.printStackTrace();
			}

			String sdp = new String(event.getResponse().getRawContent());

			log.info("BeforeCalleeConfirmedState.handleOK sdpOffer = " + sdp);
			// XXX
			CallIdentifier callID = ((JainMgcpProvider) getMgcpProvider())
					.getUniqueCallIdentifier();
			setCallIdentifier(callID);
			EndpointIdentifier endpointID = new EndpointIdentifier(
					ENDPOINT_NAME, mmsBindAddress + ":" + MGCP_PEER_PORT);
			CreateConnection createConnection = new CreateConnection(this,
					callID, endpointID, ConnectionMode.SendRecv);
			try {
				createConnection
						.setRemoteConnectionDescriptor(new ConnectionDescriptor(
								sdp));
			} catch (ConflictingParameterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int txID = ((JainMgcpProvider) getMgcpProvider())
					.getUniqueTransactionHandler();
			createConnection.setTransactionHandle(txID);

			MgcpConnectionActivity connectionActivity = null;
			try {
				connectionActivity = ((JainMgcpProvider) getMgcpProvider())
						.getConnectionActivity(txID, endpointID);
				ActivityContextInterface epnAci = ((MgcpActivityContextInterfaceFactory) getMgcpActivityContestInterfaceFactory())
						.getActivityContextInterface(connectionActivity);
				epnAci.attach(getSbbContext().getSbbLocalObject());
				epnAci.attach(getParentCmp());

			} catch (FactoryException ex) {
				ex.printStackTrace();
			} catch (NullPointerException ex) {
				ex.printStackTrace();
			} catch (UnrecognizedActivityException ex) {
				ex.printStackTrace();
			}

			((JainMgcpProvider) getMgcpProvider())
					.sendMgcpEvents(new JainMgcpEvent[] { createConnection });

			log.info("Creating RTP connection [" + ENDPOINT_NAME + "]\n"
					+ createConnection);
			// XXX
			// msConnection.modify("$", sdp);

			// setState(new SessionEstablishedState(), calleeCallId);
			setState(new SessionEstablishedState(), calleeCallId);
		}

		public void handleDecline(String calleeCallId, ResponseEvent event) {

			// Not sending ACK. Is it ok?
			// sendCallerAck(event);

			// Dialog dialog = null;
			// // TODO Handle exception
			// try {
			// dialog = getSipUtils().getDialog(event);
			// } catch (SipException e) {
			// log.error("Error getting dialog in handleDecline", e);
			// }
			// Request ackRequest;
			// try {
			// ackRequest = dialog.createRequest(Request.ACK);
			// dialog.sendAck(ackRequest);
			// } catch (SipException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			setState(new TerminationState(), calleeCallId);

		}

		public void handleAuthentication(String calleeCallId,
				ResponseEvent event) {
			sendRequestWithAuthorizationHeader(event);
			setState(new InitialState(), calleeCallId);
		}

		public abstract void execute(ResponseEvent event);

	}

	public class InitialState extends BeforeCalleeConfirmedState {

		public void execute(ResponseEvent event) {
			Response response = event.getResponse();
			int status = response.getStatusCode();
			final String calleeCallId = ((CallIdHeader) response
					.getHeader(CallIdHeader.NAME)).getCallId();
			if (status == Response.TRYING) {
				log
						.info("org.mobicents.slee.service.callcontrol.CallControlSbb$InitialState - execute - status == Response.TRYING - calleeCallId = "
								+ calleeCallId);
				// status == 100
				handleTrying(calleeCallId, event);
			} else if (status >= Response.RINGING && status < Response.OK) {
				// 180 <= status < 200
				log
						.info("org.mobicents.slee.service.callcontrol.CallControlSbb$InitialState - execute - status >= Response.RINGING && status < Response.OK - calleeCallId = "
								+ calleeCallId);
				handleRinging(calleeCallId, event);
			} else if (status >= Response.OK && status <= Response.ACCEPTED) {
				// 200 <= status <= 202
				log
						.info("org.mobicents.slee.service.callcontrol.CallControlSbb$InitialState - execute - status >= Response.OK && status <= Response.ACCEPTED - calleeCallId = "
								+ calleeCallId);
				handleOK(calleeCallId, event);
			} else if (status == Response.DECLINE) {
				log
						.info("org.mobicents.slee.service.callcontrol.CallControlSbb$InitialState - execute - status == Response.DECLINE - calleeCallId = "
								+ calleeCallId);
				handleDecline(calleeCallId, event);
			} else if (status == Response.UNAUTHORIZED
					|| status == Response.PROXY_AUTHENTICATION_REQUIRED) {
				log
						.info("org.mobicents.slee.service.callcontrol.CallControlSbb$InitialState - execute - status == Response.UNAUTHORIZED || status == Response.PROXY_AUTHENTICATION_REQUIRED - calleeCallId = "
								+ calleeCallId);
				handleAuthentication(calleeCallId, event);
			}
		}

	}

	public class CalleeTryingState extends BeforeCalleeConfirmedState {

		public void execute(ResponseEvent event) {
			Response response = event.getResponse();
			int status = response.getStatusCode();
			final String calleeCallId = ((CallIdHeader) response
					.getHeader(CallIdHeader.NAME)).getCallId();

			if (status >= Response.RINGING && status < Response.OK) {
				log
						.info("CallControlSbb$CalleeTryingState - execute - status >= Response.RINGING && status < Response.OK - calleeCallId = "
								+ calleeCallId);
				// 180 <= status < 200
				handleRinging(calleeCallId, event);
			} else if (status >= Response.OK && status <= Response.ACCEPTED) {
				// 200 <= status <= 202
				log
						.info("CallControlSbb$CalleeTryingState - execute - status >= Response.OK && status <= Response.ACCEPTED - calleeCallId = "
								+ calleeCallId);
				handleOK(calleeCallId, event);
			} else if (status == Response.DECLINE) {
				log
						.info("CallControlSbb$CalleeTryingState - execute - status == Response.DECLINE - calleeCallId = "
								+ calleeCallId);
				handleDecline(calleeCallId, event);
			} else if (status == Response.UNAUTHORIZED
					|| status == Response.PROXY_AUTHENTICATION_REQUIRED) {
				log
						.info("CallControlSbb$CalleeTryingState - execute - status == Response.UNAUTHORIZED || status == Response.PROXY_AUTHENTICATION_REQUIRED - calleeCallId = "
								+ calleeCallId);
				handleAuthentication(calleeCallId, event);
			}
		}

	}

	public class CalleeRingingState extends BeforeCalleeConfirmedState {

		public void execute(ResponseEvent event) {
			Response response = event.getResponse();
			int status = response.getStatusCode();
			final String calleeCallId = ((CallIdHeader) response
					.getHeader(CallIdHeader.NAME)).getCallId();

			if (status >= Response.OK && status <= Response.ACCEPTED) {
				handleOK(calleeCallId, event);
			} else if (status == Response.DECLINE) {
				handleDecline(calleeCallId, event);
			} else if (status == Response.UNAUTHORIZED
					|| status == Response.PROXY_AUTHENTICATION_REQUIRED) {
				handleAuthentication(calleeCallId, event);
			}
		}

	}

	public abstract class AfterCalleeConfirmedAndBeforeCallerConfirmedState
			implements SimpleCallFlowState {

		public void handleTrying(String callerCallId, ResponseEvent event) {
			// TODO Handle exception

			setState(new CallerTryingState(), callerCallId);
		}

		// TODO Handle exception
		public void handleRinging(String callerCallId, ResponseEvent event) {
			setState(new CallerRingingState(), callerCallId);
		}

		// TODO Handle exception
		public void handleOK(String callerCallId, ResponseEvent event) {

			// Check that the dialog of the event is the same as in the session
			// If not this is the result of a fork, we don't handle this
			try {
				Dialog eventDialog = getSipUtils().getDialog(event);
				Dialog currentDialog = getDialog(eventDialog.getCallId()
						.getCallId());
				if (!eventDialog.equals(currentDialog)) {
					log.warning("Received 200 response from forked dialog");
					return; // We don't currently handle this. Should send ACK
					// and BYE
				}
			} catch (SipException e) {
				// TODO Handle this
			}

			sendCalleeAck(event);
			sendCallerAck(event);
			setState(new SessionEstablishedState(), callerCallId);

		}

		/**
		 * Used to handle responses in the 400 series.
		 * 
		 * @param callerCallId
		 */
		public void handleError(String callerCallId) {
			Dialog dialog = getPeerDialog(callerCallId);
			try {
				sendRequest(dialog, Request.BYE);
			} catch (SipException e) {
				log.severe("Error sending BYE", e);
			}
			setState(new UATerminationState(), callerCallId);
		}

		public void handleDecline(String callerCallId) {
			Dialog dialog = getPeerDialog(callerCallId);
			try {
				sendRequest(dialog, Request.BYE);
			} catch (SipException e) {
				log.severe("Error sending BYE", e);
			}
			setState(new UATerminationState(), callerCallId);
		}

		public void handleAuthentication(String callerCallId,
				ResponseEvent event) {
			sendRequestWithAuthorizationHeader(event);
			setState(new CallerInvitedState(), callerCallId);
		}

		/**
		 * Handles a BYE
		 * 
		 * @param calleeCallId
		 * @param request
		 */
		public void handleBye(String calleeCallId, Request request) {
			// Send OK to callee
			try {
				getSipUtils().sendOk(request);
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (SipException e) {
				e.printStackTrace();
			}
			// Send Cancel to caller
			sendRequestCancel(getPeerDialog(calleeCallId));
			setState(new UATerminationState(), calleeCallId);
		}

		public abstract void execute(RequestEvent event);

		public abstract void execute(ResponseEvent event);

	}

	public class CallerInvitedState extends
			AfterCalleeConfirmedAndBeforeCallerConfirmedState {

		public CallerInvitedState() {
			super();
			// TODO Auto-generated constructor stub
		}

		public void execute(ResponseEvent event) {

			final Response response = event.getResponse();
			final int status = response.getStatusCode();
			final String callerCallId = ((CallIdHeader) response
					.getHeader(CallIdHeader.NAME)).getCallId();

			if (status == Response.TRYING) { // status == 100
				handleTrying(callerCallId, event);
			} else if (status >= Response.RINGING && status < Response.OK) {
				// 180 <= status < 200
				handleRinging(callerCallId, event);
			} else if (status >= Response.OK && status <= Response.ACCEPTED) {
				// 200 <= status <= 202
				handleOK(callerCallId, event);
			} else if (status == Response.UNAUTHORIZED
					|| status == Response.PROXY_AUTHENTICATION_REQUIRED) {
				handleAuthentication(callerCallId, event);
			} else if (status >= Response.BAD_REQUEST
					&& status < Response.SERVER_INTERNAL_ERROR) {
				// 400 <= status < 500
				handleError(callerCallId);
			} else if (status == Response.DECLINE) {
				// Status = 603
				handleDecline(callerCallId);
			}
		}

		public void execute(RequestEvent event) {
			Request request = event.getRequest();
			final String calleeCallId = ((CallIdHeader) request
					.getHeader(CallIdHeader.NAME)).getCallId();
			String method = request.getMethod();

			if (Request.BYE.equals(method)) {
				handleBye(calleeCallId, request);
			}
		}

	}

	public class CallerTryingState extends
			AfterCalleeConfirmedAndBeforeCallerConfirmedState {

		public void execute(ResponseEvent event) {
			final Response response = event.getResponse();
			final int status = response.getStatusCode();
			final String callerCallId = ((CallIdHeader) response
					.getHeader(CallIdHeader.NAME)).getCallId();

			if (status >= Response.RINGING && status < Response.OK) {
				// 180 <= status < 200
				handleRinging(callerCallId, event);
			} else if (status >= Response.OK && status <= Response.ACCEPTED) {
				// 200 <= status <= 202
				handleOK(callerCallId, event);
			} else if (status == Response.UNAUTHORIZED
					|| status == Response.PROXY_AUTHENTICATION_REQUIRED) {
				handleAuthentication(callerCallId, event);
			} else if (status >= Response.BAD_REQUEST
					&& status < Response.SERVER_INTERNAL_ERROR) {
				// 400 <= status < 500
				handleError(callerCallId);
			} else if (status == Response.DECLINE) {
				// Status = 603
				handleDecline(callerCallId);
			}
		}

		public void execute(RequestEvent event) {
			Request request = event.getRequest();
			final String calleeCallId = ((CallIdHeader) request
					.getHeader(CallIdHeader.NAME)).getCallId();
			String method = request.getMethod();

			if (Request.BYE.equals(method)) {
				handleBye(calleeCallId, request);
			}
		}

	}

	public class CallerRingingState extends
			AfterCalleeConfirmedAndBeforeCallerConfirmedState {

		public void execute(ResponseEvent event) {
			final Response response = event.getResponse();
			final int status = response.getStatusCode();
			final String callerCallId = ((CallIdHeader) response
					.getHeader(CallIdHeader.NAME)).getCallId();

			if (status >= Response.OK && status <= Response.ACCEPTED) {
				// 200 <= status <= 202
				handleOK(callerCallId, event);
			} else if (status == Response.UNAUTHORIZED
					|| status == Response.PROXY_AUTHENTICATION_REQUIRED) {
				handleAuthentication(callerCallId, event);
			} else if (status >= Response.BAD_REQUEST
					&& status < Response.SERVER_INTERNAL_ERROR) {
				// 400 <= status < 500
				handleError(callerCallId);
			} else if (status == Response.DECLINE) {
				// Status = 603
				handleDecline(callerCallId);
			}
		}

		public void execute(RequestEvent event) {
			Request request = event.getRequest();
			final String calleeCallId = ((CallIdHeader) request
					.getHeader(CallIdHeader.NAME)).getCallId();
			String method = request.getMethod();

			if (Request.BYE.equals(method)) {
				handleBye(calleeCallId, request);
			}
		}

	}

	public class SessionEstablishedState extends SimpleCallFlowRequestState {

		public SessionEstablishedState() {
			super();
			// TODO Auto-generated constructor stub
		}

		public void execute(RequestEvent event) {
			Request request = event.getRequest();
			final String method = request.getMethod();

			if (method.equals(Request.BYE)) {
				final String callId = ((CallIdHeader) request
						.getHeader(CallIdHeader.NAME)).getCallId();

				Dialog dialog = getPeerDialog(callId);
				try {
					getSipUtils().sendOk(request);
					sendRequest(dialog, Request.BYE);
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SipException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				setState(new UATerminationState(), callId);
			}
		}

		public void execute(JainMgcpResponseEvent event) {
			// here we receive CRCX answer for PR, one side is attached to UA
			// or for second
			log
					.info("SessionEstablishedStateNoMediaPath.execute(JainMgcpResponseEvent");
			if (event instanceof CreateConnectionResponse) {
				CreateConnectionResponse crcxResponse = (CreateConnectionResponse) event;
				if (crcxResponse.getSecondEndpointIdentifier() == null) {
					// this is answer for initial
				} else {

				}
			} else {
				sendBye();

			}
		}
	}

	public class UATerminationState extends SimpleCallFlowResponseState {
		public void execute(ResponseEvent event) {
			Response response = event.getResponse();
			int status = response.getStatusCode();
			final String callId = ((CallIdHeader) response
					.getHeader(CallIdHeader.NAME)).getCallId();

			if (status == Response.OK) { // status == 200
				setState(new TerminationState(), callId);

			}
		}

	}

	/**
	 * We enter this state when an external cancellation has been triggered
	 * (e.g. by invocation of cancel(String guid) method ) and CANCEL has been
	 * sent to calleee.
	 * 
	 */
	public class ExternalCancellationState extends SimpleCallFlowResponseState {
		public void execute(ResponseEvent event) {
			Response response = event.getResponse();
			// We expect a 200 OK
			// However, we should send switch state to TerminationState whatever
			// the response is
			// so we simply ignore the response for now.
			final String callId = ((CallIdHeader) response
					.getHeader(CallIdHeader.NAME)).getCallId();
			setState(new TerminationState(), callId);
		}

	}

	/**
	 * We enter this state when the termination of both sessions has been
	 * triggered (e.g. by invocation of terminate(String guid) method ) and BYE
	 * has been sent to callee.
	 * 
	 * @author niklas
	 * 
	 */
	public class ExternalTerminationCalleeState extends
			SimpleCallFlowResponseState {
		public void execute(ResponseEvent event) {
			Response response = event.getResponse();
			// We expect a 200 OK and send a bye to Caller
			// However, we should send BYE to the Caller whatever the response
			// is
			// so we simply ignore the response for now.
			final String callId = ((CallIdHeader) response
					.getHeader(CallIdHeader.NAME)).getCallId();

			Dialog dialog = getPeerDialog(callId);
			// TODO Handle exception better (exception indicated in termination
			// state)
			try {
				sendRequest(dialog, Request.BYE);
				setState(new ExternalTerminationCallerState(), callId);
			} catch (SipException e) {
				log
						.severe("Exception while sending BYE in execute for callId : "
								+ dialog.getCallId().getCallId());
				setState(new TerminationState(), callId);
			}

		}

	}

	/**
	 * We enter this state when the termination of both sessions has been
	 * triggered (e.g. by invocation of terminateDialogs(String guid) ), BYE has
	 * been sent to both parties and we wait for a response for the Caller. <br>
	 * When this arrives the session has been successfully teared down.
	 * 
	 * @author niklas
	 * 
	 */
	public class ExternalTerminationCallerState extends
			SimpleCallFlowResponseState {
		public void execute(ResponseEvent event) {
			Response response = event.getResponse();
			// We expect a 200 OK but move to TerminationState no matter
			// what the response status is.

			// TODO Introduce a termination cause property on the
			// TerminationEvent to communicate this information
			final String callId = ((CallIdHeader) response
					.getHeader(CallIdHeader.NAME)).getCallId();
			setState(new TerminationState(), callId);

		}

	}

	public class TerminationState extends SimpleCallFlowResponseState {

		public void execute(ResponseEvent event) {
			// FINAL state, do nothing!
		}

	}

	/**
	 * Associates a state object to the association of the two sip dialogs
	 * defining the unit of call control.
	 * 
	 * @param state
	 * @param callId
	 */
	private void setState(SimpleCallFlowState state, String callId) {
		if (log.isInfoEnabled()) {
			log.info("Setting state to " + state + " for callId " + callId);
			;
		}
		String stateName = state.getClass().getName();
		SessionAssociation sa = (SessionAssociation) getCacheUtility().get(
				callId);
		// This state is used to manage the state machine transitions in the Sbb
		// implementation
		sa.setState(stateName);
		// Also communicate the state to external observers
		StateCallback callback = sa.getStateCallback();
		if (callback != null) { // The callback property is optional and can be
			// null
			callback.setSessionState(stateName);
		}

	}

	/**
	 * Utility method that associates a Dialog to the Session it belongs to via
	 * its callID. <br>
	 * <br>
	 * Note: This is a hack that needs to be fixed. There can be multiple
	 * dialogs that arise as a consequence of an invite being sent. This must be
	 * dealt with.
	 * 
	 * 
	 * @param dialog
	 * @param callId
	 */
	// TODO Fix the hack referred to above
	private void setDialog(Dialog dialog, String callId) {
		SessionAssociation sa = (SessionAssociation) getCacheUtility().get(
				callId);
		Session session = sa.getSession(callId);
		if (log.isInfoEnabled()) {
			log.info("Setting dialog in session for callId : " + callId);
		}
		session.setDialog(dialog);
	}

	public void sendCallerAck(ResponseEvent event) {
		try {
			Dialog dialog = getSipUtils().getDialog(event);
			Request ackRequest = getSipUtils().buildAck(dialog, null);
			dialog.sendAck(ackRequest);
		} catch (SipException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Accepts a response event and sends an ACK (containing the sdp from this
	 * event) to the callee.
	 * 
	 * @param event
	 */
	private void sendCalleeAck(ResponseEvent event) {

		log.info("Sending Calee ACK event ResposneEvent = " + event);
		try {
			ClientTransaction ct = event.getClientTransaction();
			final String callerCallId = ((CallIdHeader) ct.getRequest()
					.getHeader(CallIdHeader.NAME)).getCallId();

			Dialog calleeDialog = getPeerDialog(callerCallId);
			Object content = event.getResponse().getContent();

			log.info("Building ACK content = " + content + " Dialog = "
					+ calleeDialog);
			Request ackRequest = getSipUtils().buildAck(calleeDialog, content);
			calleeDialog.sendAck(ackRequest);

		} catch (SipException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Dialog getPeerDialog(String callId) {
		SessionAssociation sa = (SessionAssociation) getCacheUtility().get(
				callId);
		Session peerSession = sa.getPeerSession(callId);
		return peerSession.getDialog();
	}

	private Dialog getDialog(String callId) {
		SessionAssociation sa = (SessionAssociation) getCacheUtility().get(
				callId);
		Session session = sa.getSession(callId);
		return session.getDialog();
	}

	/**
	 * The cancel request uses the same sequence number as the invite it is ment
	 * to cancel and thus needs to be treated specially. I.e. retrive the
	 * sequence number from this invite and use this in the new cancel request.
	 * 
	 * @param dialog
	 *            The dialog of the UA where the cancel is to be sent.
	 */
	private void sendRequestCancel(Dialog dialog) {
		// Send to callee
		try {
			// Retrieve the client transacation to cancel
			Session session = getSession(dialog);
			ClientTransaction ct = session.getToBeCancelledClientTransaction();
			getSipUtils().sendCancel(ct);
		} catch (SipException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Send a request to a UA.
	 * 
	 * @param dialog
	 *            The dialog of the UA where the request is to be sent.
	 * @param requestType
	 *            The request type to send to the UA.
	 */
	private void sendRequest(Dialog dialog, String requestType)
			throws SipException {
		Request request;
		request = dialog.createRequest(requestType);

		/*
		 * If the request is of type BYE, modify the via and contact header to
		 * match the expected behavior. I.e. due to a bug in jain sip, the
		 * contact header is incorrectly set to the address of the receiver.
		 * This header should instead point to the address running this
		 * application. The bug has been reported!
		 */
		if (Request.BYE.equals(requestType)) {
			// TODO Do we have to change the VIA header?
			request.removeHeader(ViaHeader.NAME);
			request.removeHeader(ContactHeader.NAME);

			Address address = dialog.getLocalParty();

			try {
				SipURI sipUri = getSipUtils().convertAddressToSipURI(address);
				request.addHeader(getSipUtils().createLocalViaHeader());
				request.addHeader(getSipUtils().createLocalContactHeader(
						sipUri.getUser()));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		// Get a client transaction and corresponding activity context interface
		// Attach ourselves to receive responses and finally send the request
		try {
			ClientTransaction ct = getSipProvider().getNewClientTransaction(
					request);
			// ActivityContextInterface acIntf = activityContextInterfaceFactory
			// .getActivityContextInterface(ct);
			// SbbLocalObject mySelf = sbbContext.getSbbLocalObject();
			// acIntf.attach(mySelf);
			dialog.sendRequest(ct);
		} catch (Exception e) { // This catches no less than 10 distinct
			// exception types...
			log.severe("Exception in sendrequest", e);
			throw new SipException(
					"Exception rethrown as SipException in sendRequest", e);
		}
	}

	/**
	 * Send the request associated with the event again but this time include an
	 * authorization header based on the Digest Scheme presented in RFC 2069.
	 * 
	 * @param event
	 * @param password
	 */

	private void sendRequestWithAuthorizationHeader(ResponseEvent event) {

		ClientTransaction ct = null;
		SessionAssociation sa = null;
		try {
			Request request = getSipUtils()
					.buildRequestWithAuthorizationHeader(event, getPassword());

			log.info("sendRequestWithAuthorizationHeader. request with Auth = "
					+ request);

			ct = getSipProvider().getNewClientTransaction(request);
		} catch (TransactionUnavailableException e) {
			e.printStackTrace();
		}

		Dialog dialog = ct.getDialog();
		SbbLocalObject sbbLocalObject = this.getSbbContext()
				.getSbbLocalObject();
		// TODO Handle exception
		if (dialog == null) {
			// Automatic dialog support is off
			try {
				dialog = getSipProvider().getNewDialog(ct);
				if (log.isInfoEnabled()) {
					log
							.info("Obtained dialog with getNewDialog in sendRequestWithAuthorizationHeader");
				}
			} catch (SipException e) {
				log
						.severe(
								"Error getting dialog in sendRequestWithAuthorizationHeader",
								e);
			}

			// Let us remove mapping between SessionAsscoiation and old Call-ID
			ClientTransaction ctOld = event.getClientTransaction();
			Header h = ctOld.getRequest().getHeader(CallIdHeader.NAME);
			String oldCallId = ((CallIdHeader) h).getCallId();

			sa = (SessionAssociation) getCacheUtility().get(oldCallId);

			Header hNew = ct.getRequest().getHeader(CallIdHeader.NAME);
			String calleeCallIdNew = ((CallIdHeader) hNew).getCallId();

			Session oldCalleeSession = sa.getSession(oldCallId);
			oldCalleeSession.setCallId(calleeCallIdNew);

			try {
				ActivityContextInterface sipACI = ((SipActivityContextInterfaceFactory) getSipActivityContextInterfaceFactory())
						.getActivityContextInterface((DialogActivity) dialog);
				sipACI.attach(sbbLocalObject);
				sipACI.attach(this.getParentCmp());
			} catch (UnrecognizedActivityException aci) {
				aci.printStackTrace();
			}

			getCacheUtility().put(calleeCallIdNew, sa);

		}

		final String callId = dialog.getCallId().getCallId();

		if (log.isInfoEnabled()) {
			log
					.info("Obtained dialog from ClientTransaction in sendRequestWithAuthorizationHeader "
							+ " : dialog callId = " + callId);
		}
		// Store the client transaction in the cache
		// since we may need to send a cancel request associated with this
		// ClientTransaction later.
		setToBeCancelledClientTransaction(ct);

		// Store the dialog in the cache with the associated call id of the new
		// transaction.
		setDialog(dialog, callId);

		// Since we are about the send the request in a new client transaction,
		// we need to
		// attach it to the new activity context in order to receive the
		// following events.
		// We start this by getting the activity context
		ActivityContextInterface ac = null;
		try {
			ac = ((SipActivityContextInterfaceFactory) getSipActivityContextInterfaceFactory())
					.getActivityContextInterface(ct);
		} catch (FactoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnrecognizedActivityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Attach our local interface to the new ActivityContextInterface
		// This makes this Sbb receive responses to the request
		ac.attach(sbbLocalObject);

		// Finally, send the request!
		try {
			ct.sendRequest();
		} catch (SipException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param callId
	 * @return
	 */
	private Session getSession(Dialog dialog) {
		final String callId = dialog.getCallId().getCallId();
		SessionAssociation sa = (SessionAssociation) getCacheUtility().get(
				callId);
		Session session = sa.getSession(callId);
		return session;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCallControlSipAddress() {
		return callControlSipAddress;
	}

	public void setCallControlSipAddress(String username) {
		this.callControlSipAddress = username;
	}

}
 