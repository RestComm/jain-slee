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
import javax.sip.SipProvider;
import javax.sip.TransactionUnavailableException;
import javax.sip.header.CallIdHeader;
import javax.sip.header.ContactHeader;
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

import org.apache.log4j.Logger;
import org.mobicents.media.msc.common.MsLinkMode;
import org.mobicents.mscontrol.MsConnection;
import org.mobicents.mscontrol.MsConnectionEvent;
import org.mobicents.mscontrol.MsLink;
import org.mobicents.mscontrol.MsProvider;
import org.mobicents.mscontrol.MsSession;
import org.mobicents.slee.resource.media.ratype.MediaRaActivityContextInterfaceFactory;
import org.mobicents.slee.resource.sip.SipActivityContextInterfaceFactory;
import org.mobicents.slee.resource.sip.SipFactoryProvider;
import org.mobicents.slee.service.common.SimpleCallFlowRequestState;
import org.mobicents.slee.service.common.SimpleCallFlowResponseState;
import org.mobicents.slee.service.common.SimpleCallFlowState;
import org.mobicents.slee.service.events.CustomEvent;
import org.mobicents.slee.util.CacheException;
import org.mobicents.slee.util.CacheFactory;
import org.mobicents.slee.util.CacheUtility;
import org.mobicents.slee.util.Session;
import org.mobicents.slee.util.SessionAssociation;
import org.mobicents.slee.util.SipUtils;
import org.mobicents.slee.util.SipUtilsFactorySingleton;
import org.mobicents.slee.util.StateCallback;

public abstract class CallControlSbb implements javax.slee.Sbb {
	private static Logger log = Logger.getLogger(CallControlSbb.class);

	public final static String ENDPOINT_NAME = "media/trunk/PacketRelay/$";

	public final static String ANNOUNCEMENT_ENDPOINT = "media/trunk/Announcement/$";

	private SbbContext sbbContext; // This SBB's SbbContext

	private SipProvider sipProvider;

	private SipActivityContextInterfaceFactory activityContextInterfaceFactory;

	private MsProvider msProvider;

	private MediaRaActivityContextInterfaceFactory mediaAcif;

	private SipUtils sipUtils;

	private CacheUtility cache;

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
		if (log.isDebugEnabled()) {
			log.debug("Setting convergence name to: " + callId);
		}
		ies.setCustomName(callId);
		return ies;
	}

	private void executeRequestState(RequestEvent event) {
		String callId = ((CallIdHeader) event.getRequest().getHeader(
				CallIdHeader.NAME)).getCallId();
		SessionAssociation sa = (SessionAssociation) cache.get(callId);
		SimpleCallFlowState simpleCallFlowState = getState(sa.getState());
		simpleCallFlowState.execute(event);
	}

	private void executeResponseState(ResponseEvent event) {
		String callId = ((CallIdHeader) event.getResponse().getHeader(
				CallIdHeader.NAME)).getCallId();
		SessionAssociation sa = (SessionAssociation) cache.get(callId);
		SimpleCallFlowState simpleCallFlowState = getState(sa.getState());
		simpleCallFlowState.execute(event);
	}

	// public void onAckEvent(RequestEvent event, ActivityContextInterface aci)
	// {
	// if (log.isDebugEnabled()) {
	// log.debug("Received: ACK");
	// }
	// executeRequestState(event);
	// }

	public void onByeEvent(RequestEvent event, ActivityContextInterface aci) {

		log.info("************Received BYEEEE**************");

		if (log.isDebugEnabled()) {
			log.debug("Received BYE");
		}

		releaseMediaConnectionAndDialog();

		try {
			sipUtils.sendStatefulOk(event);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (SipException e) {
			e.printStackTrace();
		} catch (InvalidArgumentException invalidArgEx) {
			invalidArgEx.printStackTrace();
		}

		// executeRequestState(event);
	}

	// public void onCancelEvent(RequestEvent event, ActivityContextInterface
	// aci) {
	// if (log.isDebugEnabled()) {
	// log.debug("Received CANCEL");
	// }
	// executeRequestState(event);
	// }

	// public void onServerErrorRespEvent(ResponseEvent event,
	// ActivityContextInterface aci) {
	// if (log.isDebugEnabled()) {
	// log.debug("Received server error response : "
	// + event.getResponse().getStatusCode());
	// }
	// executeResponseState(event);
	// }
	//
	public void onClientErrorRespEvent(ResponseEvent event,
			ActivityContextInterface aci) {
		if (log.isDebugEnabled()) {
			log.debug("Received client error event : "
					+ event.getResponse().getStatusCode());
		}
		executeResponseState(event);
	}

	//
	// public void onGlobalFailureRespEvent(ResponseEvent event,
	// ActivityContextInterface aci) {
	// if (log.isDebugEnabled()) {
	// log.debug("Received global failure event : "
	// + event.getResponse().getStatusCode());
	// }
	// executeResponseState(event);
	// }

	public void onSuccessRespEvent(ResponseEvent event,
			ActivityContextInterface aci) {
		if (log.isDebugEnabled()) {
			log.debug("Received success response event "
					+ event.getResponse().getStatusCode());
		}
		executeResponseState(event);
	}

	// public void onInfoRespEvent(ResponseEvent event,
	// ActivityContextInterface aci) {
	// if (log.isDebugEnabled()) {
	// log.debug("Received informational response event : "
	// + event.getResponse().getStatusCode());
	// }
	// executeResponseState(event);
	// }

	/*
	 * Timeouts
	 */
	// public void onTransactionTimeoutEvent(TimeoutEvent event,
	// ActivityContextInterface ac) {
	// if (log.isDebugEnabled()) {
	// log.debug("Received timeout event");
	// }
	// }
	//
	// public void onRetransmitTimeoutEvent(TimeoutEvent event,
	// ActivityContextInterface ac) {
	// if (log.isDebugEnabled()) {
	// log.debug("Received retransmit timeout event");
	// }
	// }
	public void onConnectionCreated(MsConnectionEvent evt,
			ActivityContextInterface aci) {
		log.info("--------------onConnectionCreated--------------");
		MsConnection connection = evt.getConnection();

		log.info("Created RTP connection [" + connection.getEndpoint() + "]");

		try {
			Dialog dialog = sipUtils.getDialog(getResponseEventCmp());
			Request ackRequest = sipUtils.buildAck(dialog, connection
					.getLocalDescriptor());
			dialog.sendAck(ackRequest);
		} catch (SipException e) {
			e.printStackTrace();
		}
		MsSession session = connection.getSession();
		MsLink link = session.createLink(MsLinkMode.FULL_DUPLEX);

		ActivityContextInterface linkActivity = null;
		try {
			linkActivity = mediaAcif.getActivityContextInterface(link);
		} catch (UnrecognizedActivityException ex) {
			ex.printStackTrace();
		}

		linkActivity.attach(getParentCmp());

		link.join(connection.getEndpoint(), ANNOUNCEMENT_ENDPOINT);
	}

	private void releaseMediaConnectionAndDialog() {
		ActivityContextInterface[] activities = sbbContext.getActivities();
		SbbLocalObject sbbLocalObject = getSbbContext().getSbbLocalObject();
		MsConnection msConnection = null;
		for (ActivityContextInterface attachedAci : activities) {
			if (attachedAci.getActivity() instanceof Dialog) {
				attachedAci.detach(sbbLocalObject);
				attachedAci.detach(this.getParentCmp());
			}
			if (attachedAci.getActivity() instanceof MsConnection) {
				attachedAci.detach(sbbLocalObject);
				msConnection = (MsConnection) attachedAci.getActivity();
				attachedAci.detach(this.getParentCmp());
			}
		}
		if (msConnection != null) {
			msConnection.release();
		}
	}

	public void sendBye() {

		releaseMediaConnectionAndDialog();

		try {
			Dialog dialog = sipUtils.getDialog(getResponseEventCmp());
			sendRequest(dialog, Request.BYE);
		} catch (SipException e) {
			log.error("Error sending BYE", e);
		}
	}

	// TODO: Perform further operations if required in these methods.
	public void setSbbContext(SbbContext context) {
		this.sbbContext = context;

		try {
			// Create the cache used for the session association
			cache = CacheFactory.getInstance().getCache();

			Context myEnv = (Context) new InitialContext()
					.lookup("java:comp/env");
			// Getting JAIN SIP Resource Adaptor interfaces
			SipFactoryProvider factoryProvider = (SipFactoryProvider) myEnv
					.lookup("slee/resources/jainsip/1.2/provider");

			sipProvider = factoryProvider.getSipProvider();

			activityContextInterfaceFactory = (SipActivityContextInterfaceFactory) myEnv
					.lookup("slee/resources/jainsip/1.2/acifactory");

			// Check that callControlSipAddress and password are present
			callControlSipAddress = (String) myEnv
					.lookup("callControlSipAddress");
			password = (String) myEnv.lookup("password");
			String passwordDisplay = password == null ? "null" : "*******";
			if (log.isDebugEnabled()) {
				log
						.debug("Checking that CallControlSbb callControlSipAddress and password are present : callControlSipAddress = "
								+ callControlSipAddress
								+ " password = "
								+ passwordDisplay);
			}

			sipUtils = SipUtilsFactorySingleton.getInstance().getSipUtils();

			// initilize Media API
			msProvider = (MsProvider) myEnv
					.lookup("slee/resources/media/1.0/provider");
			mediaAcif = (MediaRaActivityContextInterfaceFactory) myEnv
					.lookup("slee/resources/media/1.0/acifactory");

		} catch (NamingException ne) {
			ne.printStackTrace();
		} catch (CacheException e) {
			e.printStackTrace();
		}
	}

	public void unsetSbbContext() {
		this.sbbContext = null;
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
		return sbbContext;
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
		SessionAssociation sa = (SessionAssociation) cache.get(callId);

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

			log.debug("BeforeCalleeConfirmedState.handleOK");

			setResponseEventCmp(event);
			Dialog currentDialog = null;

			// Check that the dialog of the event is the same as in the session
			// If not this is the result of a fork, we don't handle this
			try {
				Dialog eventDialog = sipUtils.getDialog(event);
				currentDialog = getDialog(eventDialog.getCallId().getCallId());
				if (!eventDialog.equals(currentDialog)) {
					log.warn("Received 200 response from forked dialog");
					return; // We don't currently handle this. Should send ACK
					// and BYE
				}
			} catch (SipException e) {
				log.error("SipException while trying to retreive Dialog", e);
			}

			// Let us attach sbbLocalObject to Dialog to receive Bye latter
			try {

				ActivityContextInterface daci = activityContextInterfaceFactory
						.getActivityContextInterface(currentDialog);
				daci.attach(sbbContext.getSbbLocalObject());
			} catch (Exception e) {
				e.printStackTrace();
			}

			String sdp = new String(event.getResponse().getRawContent());

			log.debug("BeforeCalleeConfirmedState.handleOK sdpOffer = " + sdp);

			MsSession session = msProvider.createSession();
			MsConnection msConnection = session
					.createNetworkConnection(ENDPOINT_NAME);

			try {
				ActivityContextInterface aci = mediaAcif
						.getActivityContextInterface(msConnection);
				aci.attach(sbbContext.getSbbLocalObject());
				aci.attach(getParentCmp());
			} catch (Exception e) {
				e.printStackTrace();
			}

			log.info("Creating RTP connection [" + ENDPOINT_NAME + "]");

			msConnection.modify("$", sdp);

			setState(new SessionEstablishedState(), calleeCallId);
		}

		public void handleDecline(String calleeCallId, ResponseEvent event) {

			// Not sending ACK. Is it ok?
			// sendCallerAck(event);

			// Dialog dialog = null;
			// // TODO Handle exception
			// try {
			// dialog = sipUtils.getDialog(event);
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
						.debug("org.mobicents.slee.service.callcontrol.CallControlSbb$InitialState - execute - status == Response.TRYING - calleeCallId = "
								+ calleeCallId);
				// status == 100
				handleTrying(calleeCallId, event);
			} else if (status >= Response.RINGING && status < Response.OK) {
				// 180 <= status < 200
				log
						.debug("org.mobicents.slee.service.callcontrol.CallControlSbb$InitialState - execute - status >= Response.RINGING && status < Response.OK - calleeCallId = "
								+ calleeCallId);
				handleRinging(calleeCallId, event);
			} else if (status >= Response.OK && status <= Response.ACCEPTED) {
				// 200 <= status <= 202
				log
						.debug("org.mobicents.slee.service.callcontrol.CallControlSbb$InitialState - execute - status >= Response.OK && status <= Response.ACCEPTED - calleeCallId = "
								+ calleeCallId);
				handleOK(calleeCallId, event);
			} else if (status == Response.DECLINE) {
				log
						.debug("org.mobicents.slee.service.callcontrol.CallControlSbb$InitialState - execute - status == Response.DECLINE - calleeCallId = "
								+ calleeCallId);
				handleDecline(calleeCallId, event);
			} else if (status == Response.UNAUTHORIZED
					|| status == Response.PROXY_AUTHENTICATION_REQUIRED) {
				log
						.debug("org.mobicents.slee.service.callcontrol.CallControlSbb$InitialState - execute - status == Response.UNAUTHORIZED || status == Response.PROXY_AUTHENTICATION_REQUIRED - calleeCallId = "
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
						.debug("CallControlSbb$CalleeTryingState - execute - status >= Response.RINGING && status < Response.OK - calleeCallId = "
								+ calleeCallId);
				// 180 <= status < 200
				handleRinging(calleeCallId, event);
			} else if (status >= Response.OK && status <= Response.ACCEPTED) {
				// 200 <= status <= 202
				log
						.debug("CallControlSbb$CalleeTryingState - execute - status >= Response.OK && status <= Response.ACCEPTED - calleeCallId = "
								+ calleeCallId);
				handleOK(calleeCallId, event);
			} else if (status == Response.DECLINE) {
				log
						.debug("CallControlSbb$CalleeTryingState - execute - status == Response.DECLINE - calleeCallId = "
								+ calleeCallId);
				handleDecline(calleeCallId, event);
			} else if (status == Response.UNAUTHORIZED
					|| status == Response.PROXY_AUTHENTICATION_REQUIRED) {
				log
						.debug("CallControlSbb$CalleeTryingState - execute - status == Response.UNAUTHORIZED || status == Response.PROXY_AUTHENTICATION_REQUIRED - calleeCallId = "
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
				Dialog eventDialog = sipUtils.getDialog(event);
				Dialog currentDialog = getDialog(eventDialog.getCallId()
						.getCallId());
				if (!eventDialog.equals(currentDialog)) {
					log.warn("Received 200 response from forked dialog");
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
				log.error("Error sending BYE", e);
			}
			setState(new UATerminationState(), callerCallId);
		}

		public void handleDecline(String callerCallId) {
			Dialog dialog = getPeerDialog(callerCallId);
			try {
				sendRequest(dialog, Request.BYE);
			} catch (SipException e) {
				log.error("Error sending BYE", e);
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
				sipUtils.sendOk(request);
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

		public void execute(RequestEvent event) {
			Request request = event.getRequest();
			final String method = request.getMethod();

			if (method.equals(Request.BYE)) {
				final String callId = ((CallIdHeader) request
						.getHeader(CallIdHeader.NAME)).getCallId();

				Dialog dialog = getPeerDialog(callId);
				try {
					sipUtils.sendOk(request);
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
						.error("Exception while sending BYE in execute for callId : "
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
		if (log.isDebugEnabled()) {
			log.debug("Setting state to " + state + " for callId " + callId);
			;
		}
		String stateName = state.getClass().getName();
		SessionAssociation sa = (SessionAssociation) cache.get(callId);
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
		SessionAssociation sa = (SessionAssociation) cache.get(callId);
		Session session = sa.getSession(callId);
		if (log.isDebugEnabled()) {
			log.debug("Setting dialog in session for callId : " + callId);
		}
		session.setDialog(dialog);
	}

	public void sendCallerAck(ResponseEvent event) {
		try {
			Dialog dialog = sipUtils.getDialog(event);
			Request ackRequest = sipUtils.buildAck(dialog, null);
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

		log.debug("Sending Calee ACK event ResposneEvent = " + event);
		try {
			ClientTransaction ct = event.getClientTransaction();
			final String callerCallId = ((CallIdHeader) ct.getRequest()
					.getHeader(CallIdHeader.NAME)).getCallId();

			Dialog calleeDialog = getPeerDialog(callerCallId);
			Object content = event.getResponse().getContent();

			log.debug("Building ACK content = " + content + " Dialog = "
					+ calleeDialog);
			Request ackRequest = sipUtils.buildAck(calleeDialog, content);
			calleeDialog.sendAck(ackRequest);

		} catch (SipException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Dialog getPeerDialog(String callId) {
		SessionAssociation sa = (SessionAssociation) cache.get(callId);
		Session peerSession = sa.getPeerSession(callId);
		return peerSession.getDialog();
	}

	private Dialog getDialog(String callId) {
		SessionAssociation sa = (SessionAssociation) cache.get(callId);
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
			sipUtils.sendCancel(ct);
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

			try {
				request.addHeader(sipUtils.createLocalViaHeader());
				request.addHeader(sipUtils.createLocalContactHeader());
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
			ClientTransaction ct = sipProvider.getNewClientTransaction(request);
			ActivityContextInterface acIntf = activityContextInterfaceFactory
					.getActivityContextInterface(ct);
			SbbLocalObject mySelf = sbbContext.getSbbLocalObject();
			acIntf.attach(mySelf);
			dialog.sendRequest(ct);
		} catch (Exception e) { // This catches no less than 10 distinct
			// exception types...
			log.error("Exception in sendrequest", e);
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
			Request request = sipUtils.buildRequestWithAuthorizationHeader(
					event, getPassword());

			log
					.debug("sendRequestWithAuthorizationHeader. request with Auth = "
							+ request);

			ct = sipProvider.getNewClientTransaction(request);
		} catch (TransactionUnavailableException e) {
			e.printStackTrace();
		}

		Dialog dialog = ct.getDialog();
		SbbLocalObject sbbLocalObject = this.getSbbContext().getSbbLocalObject();
		// TODO Handle exception
		if (dialog == null) {
			// Automatic dialog support is off
			try {
				dialog = sipProvider.getNewDialog(ct);
				if (log.isDebugEnabled()) {
					log
							.debug("Obtained dialog with getNewDialog in sendRequestWithAuthorizationHeader");
				}
			} catch (SipException e) {
				log
						.error(
								"Error getting dialog in sendRequestWithAuthorizationHeader",
								e);
			}

			// Let us remove mapping between SessionAsscoiation and old Call-ID
			ClientTransaction ctOld = event.getClientTransaction();
			Header h = ctOld.getRequest().getHeader(CallIdHeader.NAME);
			String oldCallId = ((CallIdHeader) h).getCallId();

			sa = (SessionAssociation) cache.get(oldCallId);

			Header hNew = ct.getRequest().getHeader(CallIdHeader.NAME);
			String calleeCallIdNew = ((CallIdHeader) hNew).getCallId();

			Session oldCalleeSession = sa.getSession(oldCallId);
			oldCalleeSession.setCallId(calleeCallIdNew);

			try {
				ActivityContextInterface sipACI = activityContextInterfaceFactory
						.getActivityContextInterface(dialog);
				sipACI.attach(sbbLocalObject);
				sipACI.attach(this.getParentCmp());
			} catch (UnrecognizedActivityException aci) {
				aci.printStackTrace();
			}

			cache.put(calleeCallIdNew, sa);

		}

		final String callId = dialog.getCallId().getCallId();

		if (log.isDebugEnabled()) {
			log
					.debug("Obtained dialog from ClientTransaction in sendRequestWithAuthorizationHeader "
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
			ac = activityContextInterfaceFactory
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
		SessionAssociation sa = (SessionAssociation) cache.get(callId);
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