package org.mobicents.slee.example.sip11.b2b;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sip.ClientTransaction;
import javax.sip.InvalidArgumentException;
import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.ServerTransaction;
import javax.sip.SipException;
import javax.sip.header.CSeqHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;
import javax.slee.ActivityContextInterface;
import javax.slee.RolledBackContext;
import javax.slee.SbbContext;
import javax.slee.SbbLocalObject;
import javax.slee.facilities.Tracer;

import net.java.slee.resource.sip.CancelRequestEvent;
import net.java.slee.resource.sip.DialogActivity;
import net.java.slee.resource.sip.SipActivityContextInterfaceFactory;
import net.java.slee.resource.sip.SleeSipProvider;

public abstract class SimpleSip11B2BTestSbb implements javax.slee.Sbb {

	private SbbContext sbbContext; 
	private static Tracer tracer;
	
	private SipActivityContextInterfaceFactory sipActivityContextInterfaceFactory;
	private SleeSipProvider sipProvider;
	
	// Initial request
	public void onInviteEvent(RequestEvent event, ActivityContextInterface aci) {
		// ACI is the server transaction activity
		final ServerTransaction st = event.getServerTransaction();
		try {
			// Create the dialogs representing the incoming and outgoing call
			// legs.
			final DialogActivity incomingDialog = (DialogActivity) sipProvider.getNewDialog(st);
			final DialogActivity outgoingDialog = sipProvider.getNewDialog(incomingDialog, false);
			// Obtain the dialog activity contexts and attach to them
			final ActivityContextInterface outgoingDialogACI = sipActivityContextInterfaceFactory.getActivityContextInterface(outgoingDialog);
			final ActivityContextInterface incomingDialogACI = sipActivityContextInterfaceFactory.getActivityContextInterface(incomingDialog);
			final SbbLocalObject sbbLocalObject = sbbContext.getSbbLocalObject();
			incomingDialogACI.attach(sbbLocalObject);
			outgoingDialogACI.attach(sbbLocalObject);
			// Record which dialog is which, so we can find the peer dialog
			// when forwarding messages between dialogs.
			setIncomingDialog(incomingDialogACI);
			setOutgoingDialog(outgoingDialogACI);
			forwardRequest(st, outgoingDialog);
		} catch (Throwable e) {
			tracer.severe("Failed to process incoming INVITE.",e);
			sendErrorResponse(st, Response.SERVICE_UNAVAILABLE);
		}
	}

	// Responses
	public void on1xxResponse(ResponseEvent event, ActivityContextInterface aci) {
		processResponse(event, aci);
	}

	public void on2xxResponse(ResponseEvent event, ActivityContextInterface aci) {
		if(((CSeqHeader)event.getResponse().getHeader(CSeqHeader.NAME)).getMethod().equals(Request.CANCEL)) {
			//TODO: add ack on 487
			return;
		}
		processResponse(event, aci);
	}

	// other responses handled the same way as above
	// Mid-dialog requests
	public void onAck(RequestEvent event, ActivityContextInterface aci) {
		processMidDialogRequest(event, aci);
	}

	public void onBye(RequestEvent event, ActivityContextInterface aci) {
		processMidDialogRequest(event, aci);
	}

	public void onCancel(CancelRequestEvent event, ActivityContextInterface aci) {
		this.sipProvider.acceptCancel(event, false);
		processMidDialogRequest(event, aci);
	}
	
	// Other mid-dialog requests handled the same way as above
	// Helpers
	private void processMidDialogRequest(RequestEvent event, ActivityContextInterface dialogACI) {
		try {
			// Find the dialog to forward the request on
			ActivityContextInterface peerACI = getPeerDialog(dialogACI);
			forwardRequest(event.getServerTransaction(), (DialogActivity) peerACI.getActivity());
		} catch (SipException e) {
			tracer.severe(e.getMessage(),e);
			sendErrorResponse(event.getServerTransaction(), Response.SERVICE_UNAVAILABLE);
		}
	}

	private void processResponse(ResponseEvent event, ActivityContextInterface aci) {
		try {
			// Find the dialog to forward the response on
			ActivityContextInterface peerACI = getPeerDialog(aci);
			forwardResponse((DialogActivity) aci.getActivity(), (DialogActivity) peerACI.getActivity(), event.getClientTransaction(), event.getResponse());
		} catch (SipException e) {
			tracer.severe(e.getMessage(),e);
		}
	}

	private ActivityContextInterface getPeerDialog(ActivityContextInterface aci) throws SipException {
		final ActivityContextInterface incomingDialogAci = getIncomingDialog();
		final DialogActivity incomingDialog = (DialogActivity) incomingDialogAci.getActivity();
		if ( aci.getActivity().equals(incomingDialog)) {	
			if (tracer.isInfoEnabled()) {
				tracer.info("Peer is outgoing dialog.");
			}
			return getOutgoingDialog();
		}
		final DialogActivity outgoingDialog = (DialogActivity) getOutgoingDialog().getActivity();
		if (aci.getActivity().equals(outgoingDialog)) {	
			if (tracer.isInfoEnabled()) {
				tracer.info("Peer is incoming dialog.");
			}
			return incomingDialogAci;
		}
		throw new SipException("could not find peer dialog");
	}

	private void forwardRequest(ServerTransaction st, DialogActivity out) throws SipException {
		final Request incomingRequest = st.getRequest();
		if (tracer.isInfoEnabled()) {
			tracer.info("Forwarding request "+incomingRequest.getMethod()+" to dialog "+out);
		}
		// Copies the request, setting the appropriate headers for the dialog.
		if (incomingRequest.getMethod().equals(Request.ACK)) {
			// Just forward the ACK statelessly - don't need to remember
			// transaction state
			final Request outgoingRequest = out.createRequest(incomingRequest);
			out.sendAck(outgoingRequest);
		} 
		else if(incomingRequest.getMethod().equals(Request.CANCEL)) {
			out.sendCancel();
		}
		else {
			final Request outgoingRequest = out.createRequest(incomingRequest);
			// Send the request on the dialog activity
			final ClientTransaction ct = out.sendRequest(outgoingRequest);
			// Record an association with the original server transaction,
			// so we can retrieve it when forwarding the response.
			out.associateServerTransaction(ct, st);
		}
	}

	private void forwardResponse(DialogActivity in, DialogActivity out, ClientTransaction ct, Response receivedResponse) throws SipException {
		// Find the original server transaction that this response
		// should be forwarded on.
		final ServerTransaction st = in.getAssociatedServerTransaction(ct);
		// could be null
		if (st == null)
			throw new SipException("could not find associated server transaction");
		if (tracer.isInfoEnabled()) {
			tracer.info("Forwarding response "+receivedResponse.getStatusCode()+" to dialog "+out);
		}
		// Copy the response across, setting the appropriate headers for the
		// dialog
		final Response outgoingResponse = out.createResponse(st, receivedResponse);
		// Forward response upstream.
		try {
			st.sendResponse(outgoingResponse);
		} catch (InvalidArgumentException e) {
			tracer.severe(e.getMessage(),e);
			throw new SipException("invalid response", e);
		}
	}

	private void sendErrorResponse(ServerTransaction st, int statusCode) {
		try {
			final Response response = sipProvider.getMessageFactory().createResponse(statusCode, st.getRequest());
			st.sendResponse(response);
		} catch (Exception e) {
			// exception handling code
			tracer.severe(e.getMessage(),e);
		}
	}

	// other request handling methods
	// lifecycle methods
	// CMP field accessors for each Dialogs ACI
	public abstract void setIncomingDialog(ActivityContextInterface aci);

	public abstract ActivityContextInterface getIncomingDialog();

	public abstract void setOutgoingDialog(ActivityContextInterface aci);

	public abstract ActivityContextInterface getOutgoingDialog();

	public void setSbbContext(SbbContext context) {
		this.sbbContext = context;
		if (tracer == null){
			tracer = sbbContext.getTracer(SimpleSip11B2BTestSbb.class.getSimpleName());
		}
		try {
			Context ctx = (Context) new InitialContext().lookup("java:comp/env");
			sipActivityContextInterfaceFactory = (SipActivityContextInterfaceFactory) ctx.lookup("slee/resources/jainsip/1.2/acifactory");
			sipProvider = (SleeSipProvider) ctx.lookup("slee/resources/jainsip/1.2/provider");
		} catch (NamingException e) {
			tracer.severe(e.getMessage(),e);
		}
	}

	public void unsetSbbContext() {
		this.sbbContext = null;
	}

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

	public void sbbExceptionThrown(Exception exception, Object event, ActivityContextInterface activity) {
	}

	public void sbbRolledBack(RolledBackContext context) {
	}

}