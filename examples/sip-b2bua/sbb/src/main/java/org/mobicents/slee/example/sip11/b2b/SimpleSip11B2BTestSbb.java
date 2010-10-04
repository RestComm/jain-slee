package org.mobicents.slee.example.sip11.b2b;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sip.ClientTransaction;
import javax.sip.DialogState;
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
		try {
			// Create the dialogs representing the incoming and outgoing call
			// legs.
			final DialogActivity incomingDialog = (DialogActivity) sipProvider
					.getNewDialog(event.getServerTransaction());
			final DialogActivity outgoingDialog = sipProvider.getNewDialog(
					incomingDialog, true);
			// Obtain the dialog activity contexts and attach to them
			final ActivityContextInterface outgoingDialogACI = sipActivityContextInterfaceFactory
					.getActivityContextInterface(outgoingDialog);
			final ActivityContextInterface incomingDialogACI = sipActivityContextInterfaceFactory
					.getActivityContextInterface(incomingDialog);
			final SbbLocalObject sbbLocalObject = sbbContext
					.getSbbLocalObject();
			incomingDialogACI.attach(sbbLocalObject);
			outgoingDialogACI.attach(sbbLocalObject);
			// Record which dialog is which, so we can find the peer dialog
			// when forwarding messages between dialogs.
			setIncomingDialog(incomingDialogACI);
			setOutgoingDialog(outgoingDialogACI);
			forwardRequest(event, outgoingDialog);
		} catch (Throwable e) {
			tracer.severe("Failed to process incoming INVITE.", e);
			replyToRequestEvent(event, Response.SERVICE_UNAVAILABLE);
		}
	}

	// Responses
	public void on1xxResponse(ResponseEvent event, ActivityContextInterface aci) {
		if (event.getResponse().getStatusCode() == Response.TRYING) {
			// those are not forwarded to the other dialog
			return;
		}
		processResponse(event, aci);
	}

	public void on2xxResponse(ResponseEvent event, ActivityContextInterface aci) {
		final CSeqHeader cseq = (CSeqHeader) event.getResponse().getHeader(
				CSeqHeader.NAME);
		if (cseq.getMethod().equals(Request.INVITE)) {
			// lets ack it ourselves to avoid UAS retransmissions due to
			// forwarding of this response and further UAC Ack
			// note that the app does not handles UAC ACKs
			try {
				final Request ack = event.getDialog().createAck(
						cseq.getSeqNumber());
				event.getDialog().sendAck(ack);
			} catch (Exception e) {
				tracer.severe("Unable to ack INVITE's 200 ok from UAS", e);
			}
		} else if (cseq.getMethod().equals(Request.BYE)
				|| cseq.getMethod().equals(Request.CANCEL)) {
			// not forwarded to the other dialog
			return;
		}
		processResponse(event, aci);
	}

	public void onBye(RequestEvent event, ActivityContextInterface aci) {
		// send back 200 ok for this dialog right away, to avoid retransmissions
		replyToRequestEvent(event, Response.OK);
		// forward to the other dialog
		processMidDialogRequest(event, aci);
	}

	public void onCancel(CancelRequestEvent event, ActivityContextInterface aci) {
		if (tracer.isInfoEnabled()) {
			tracer.info("Got a CANCEL request.");
		}
		
		try {
			this.sipProvider.acceptCancel(event, false);
			final ActivityContextInterface peerDialogACI = getPeerDialog(aci);
			final DialogActivity peerDialog = (DialogActivity) peerDialogACI
					.getActivity();
			final DialogState peerDialogState = peerDialog.getState();
			if (peerDialogState == null || peerDialogState == DialogState.EARLY) {
				peerDialog.sendCancel();
			} else {
				peerDialog.sendRequest(peerDialog.createRequest(Request.BYE));
			}
		} catch (Exception e) {
			tracer.severe("Failed to process cancel request", e);
		}
	}

	// Other mid-dialog requests handled the same way as above
	// Helpers

	private void replyToRequestEvent(RequestEvent event, int status) {
		try {
			event.getServerTransaction().sendResponse(
					sipProvider.getMessageFactory().createResponse(status,
							event.getRequest()));
		} catch (Throwable e) {
			tracer.severe("Failed to reply to request event:\n" + event, e);
		}
	}

	private void processMidDialogRequest(RequestEvent event,
			ActivityContextInterface dialogACI) {
		try {
			// Find the dialog to forward the request on
			ActivityContextInterface peerACI = getPeerDialog(dialogACI);
			forwardRequest(event,
					(DialogActivity) peerACI.getActivity());
		} catch (SipException e) {
			tracer.severe(e.getMessage(), e);
			replyToRequestEvent(event, Response.SERVICE_UNAVAILABLE);
		}
	}

	private void processResponse(ResponseEvent event,
			ActivityContextInterface aci) {
		try {
			// Find the dialog to forward the response on
			ActivityContextInterface peerACI = getPeerDialog(aci);
			forwardResponse((DialogActivity) aci.getActivity(),
					(DialogActivity) peerACI.getActivity(), event
							.getClientTransaction(), event.getResponse());
		} catch (SipException e) {
			tracer.severe(e.getMessage(), e);
		}
	}

	private ActivityContextInterface getPeerDialog(ActivityContextInterface aci)
			throws SipException {
		final ActivityContextInterface incomingDialogAci = getIncomingDialog();
		if (aci.equals(incomingDialogAci)) {
			return getOutgoingDialog();
		}
		if (aci.equals(getOutgoingDialog())) {
			return incomingDialogAci;
		}
		throw new SipException("could not find peer dialog");

	}

	private void forwardRequest(RequestEvent event, DialogActivity out)
			throws SipException {
		final Request incomingRequest = event.getRequest();
		if (tracer.isInfoEnabled()) {
			tracer.info("Forwarding request " + incomingRequest.getMethod()
					+ " to dialog " + out);
		}
		// Copies the request, setting the appropriate headers for the dialog.
		Request outgoingRequest = out.createRequest(incomingRequest);
		// Send the request on the dialog activity
		final ClientTransaction ct = out.sendRequest(outgoingRequest);
		// Record an association with the original server transaction,
		// so we can retrieve it when forwarding the response.
		out.associateServerTransaction(ct, event.getServerTransaction());
	}

	private void forwardResponse(DialogActivity in, DialogActivity out,
			ClientTransaction ct, Response receivedResponse)
			throws SipException {
		// Find the original server transaction that this response
		// should be forwarded on.
		final ServerTransaction st = in.getAssociatedServerTransaction(ct);
		// could be null
		if (st == null)
			throw new SipException(
					"could not find associated server transaction");
		if (tracer.isInfoEnabled()) {
			tracer.info("Forwarding response "
					+ receivedResponse.getStatusCode() + " to dialog " + out);
		}
		// Copy the response across, setting the appropriate headers for the
		// dialog
		final Response outgoingResponse = out.createResponse(st,
				receivedResponse);
		// Forward response upstream.
		try {
			st.sendResponse(outgoingResponse);
		} catch (InvalidArgumentException e) {
			tracer.severe("Failed to send response:\n" + outgoingResponse, e);
			throw new SipException("invalid response", e);
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
		if (tracer == null) {
			tracer = sbbContext.getTracer(SimpleSip11B2BTestSbb.class
					.getSimpleName());
		}
		try {
			final Context ctx = (Context) new InitialContext()
					.lookup("java:comp/env");
			sipActivityContextInterfaceFactory = (SipActivityContextInterfaceFactory) ctx
					.lookup("slee/resources/jainsip/1.2/acifactory");
			sipProvider = (SleeSipProvider) ctx
					.lookup("slee/resources/jainsip/1.2/provider");
		} catch (NamingException e) {
			tracer.severe(e.getMessage(), e);
		}
	}

	public void unsetSbbContext() {
		this.sbbContext = null;
		this.sipActivityContextInterfaceFactory = null;
		this.sipProvider = null;
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

	public void sbbExceptionThrown(Exception exception, Object event,
			ActivityContextInterface activity) {
	}

	public void sbbRolledBack(RolledBackContext context) {
	}

}