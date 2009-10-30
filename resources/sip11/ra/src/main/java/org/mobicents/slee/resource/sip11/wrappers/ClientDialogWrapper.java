package org.mobicents.slee.resource.sip11.wrappers;

import gov.nist.javax.sip.address.GenericURI;
import gov.nist.javax.sip.header.CSeq;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.concurrent.atomic.AtomicLong;

import javax.sip.ClientTransaction;
import javax.sip.Dialog;
import javax.sip.DialogDoesNotExistException;
import javax.sip.DialogState;
import javax.sip.InvalidArgumentException;
import javax.sip.ListeningPoint;
import javax.sip.ResponseEvent;
import javax.sip.ServerTransaction;
import javax.sip.SipException;
import javax.sip.Transaction;
import javax.sip.TransactionDoesNotExistException;
import javax.sip.TransactionUnavailableException;
import javax.sip.address.Address;
import javax.sip.address.SipURI;
import javax.sip.address.URI;
import javax.sip.header.CSeqHeader;
import javax.sip.header.CallIdHeader;
import javax.sip.header.ContactHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.header.MaxForwardsHeader;
import javax.sip.header.RouteHeader;
import javax.sip.header.ToHeader;
import javax.sip.header.ViaHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;
import javax.slee.facilities.Tracer;
import javax.slee.resource.FireableEventType;

import net.java.slee.resource.sip.DialogForkedEvent;

import org.mobicents.slee.resource.sip11.DialogWithoutIdActivityHandle;
import org.mobicents.slee.resource.sip11.LateResponseHandler;
import org.mobicents.slee.resource.sip11.SipActivityHandle;
import org.mobicents.slee.resource.sip11.SipResourceAdaptor;
import org.mobicents.slee.resource.sip11.Utils;

public class ClientDialogWrapper extends DialogWrapper {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Tracer tracer;

	// FIXME currently transient due to not support for early dialog failover
	private transient ClientDialogWrapperData data;
	
	private ClientDialogWrapper(SipActivityHandle activityHandle,
			String localTag, SipResourceAdaptor ra) {
		super(activityHandle, localTag, ra);
		data = new ClientDialogWrapperData(this);
		setResourceAdaptor(ra);
	}

	@Override
	public void setResourceAdaptor(SipResourceAdaptor ra) {
		super.setResourceAdaptor(ra);
		if (tracer == null) {
			tracer = ra.getTracer(ClientDialogWrapper.class.getSimpleName());
		}
	}

	/**
	 * Constructs an instance of a UAC dialog.
	 * 
	 * @param from
	 * @param localTag
	 * @param to
	 * @param callIdHeader
	 * @param provider
	 * @param ra
	 */
	public ClientDialogWrapper(Address from, String localTag, Address to,
			CallIdHeader callIdHeader, SipResourceAdaptor ra) {
		this(new DialogWithoutIdActivityHandle(callIdHeader.getCallId(),
				localTag, null), localTag, ra);
		data.setToAddress(to);
		data.setFromAddress(from);
		data.setCustomCallId(callIdHeader);
	}

	/**
	 * for forks
	 * 
	 * @param wrappedDialog
	 * @param forkInitialActivityHandle
	 * @param provider
	 * @param ra
	 */
	private ClientDialogWrapper(Dialog wrappedDialog,
			DialogWithoutIdActivityHandle forkInitialActivityHandle,
			SipResourceAdaptor ra) {
		this(new DialogWithoutIdActivityHandle(wrappedDialog.getCallId()
				.getCallId(), wrappedDialog.getLocalTag(), wrappedDialog
				.getRemoteTag()), wrappedDialog.getLocalTag(), ra);
		data.setForkInitialActivityHandle(forkInitialActivityHandle);
		this.wrappedDialog = wrappedDialog;
	}

	@Override
	public javax.slee.Address getEventFiringAddress() {
		if (eventFiringAddress == null) {
			if (wrappedDialog != null) {
				eventFiringAddress = super.getEventFiringAddress();
			} else {
				// outgoing dialog where the wrapped dialog does not exists yet
				eventFiringAddress = ClientTransactionWrapper.getEventFiringAddress(data.getFromAddress());
			}
		}
		return eventFiringAddress;
	}
	
	@Override
	public Request createAck(long arg0) throws InvalidArgumentException,
			SipException {
		verifyDialogExistency();
		return super.createAck(arg0);
	}

	@Override
	public Request createPrack(Response arg0)
			throws DialogDoesNotExistException, SipException {
		verifyDialogExistency();
		return super.createPrack(arg0);
	}

	@Override
	public Response createReliableProvisionalResponse(int arg0)
			throws InvalidArgumentException, SipException {
		verifyDialogExistency();
		return super.createReliableProvisionalResponse(arg0);
	}

	@Override
	public void delete() {
		// This ensures that dialog will be removed
		if (wrappedDialog == null || data.getForkInitialActivityHandle() != null) {
			ra.processDialogTerminated(this);
		} else {
			// We are master, if we die, everything else does
			try {
				this.terminateFork(this.getActivityHandle());
			} finally {
				super.delete();
			}
		}
	}

	@Override
	public CallIdHeader getCallId() {
		if (this.wrappedDialog == null) {
			return data.getCustomCallId();
		}
		return super.getCallId();
	}

	@Override
	public String getDialogId() {
		if (this.wrappedDialog == null) {
			return null;
		}
		return super.getDialogId();
	}

	@Override
	public Transaction getFirstTransaction() {
		verifyDialogExistency();
		return super.getFirstTransaction();
	}

	@Override
	public Address getLocalParty() {
		if (wrappedDialog != null && !data.isInForkedActions()) {
			return super.getLocalParty();
		} else {
			return data.getFromAddress();
		}
	}

	@Override
	public Address getRemoteParty() {
		if (wrappedDialog != null && !data.isInForkedActions()) {
			return super.getRemoteParty();
		} else {
			return data.getToAddress();
		}
	}

	@Override
	public Address getRemoteTarget() {
		if (wrappedDialog != null && !data.isInForkedActions()) {
			return super.getRemoteTarget();
		} else {
			return data.getToAddress();
		}
	}

	@Override
	public long getLocalSeqNumber() {
		if (data.isInForkedActions() || wrappedDialog == null) {
			return data.getLocalSequenceNumber().get();
		} else {
			return super.getLocalSeqNumber();
		}
	}

	@Override
	public int getLocalSequenceNumber() {
		return (int) getLocalSeqNumber();
	}

	@Override
	public long getRemoteSeqNumber() {
		if (wrappedDialog != null)
			return super.getRemoteSeqNumber();
		else
			return 1L;
	}

	@Override
	public int getRemoteSequenceNumber() {
		return (int) getRemoteSeqNumber();
	}

	@Override
	public String getRemoteTag() {
		if (wrappedDialog != null && !data.isInForkedActions()) {
			return super.getRemoteTag();
		} else {
			return data.getLocalRemoteTag();
		}
	}

	@Override
	public Iterator<RouteHeader> getRouteSet() {
		if (wrappedDialog != null) {
			return super.getRouteSet();
		} else {
			return data.getRouteSet().iterator();
		}
	}

	@Override
	public DialogState getState() {
		if (wrappedDialog == null) {
			return null;
		}
		return super.getState();
	}

	@Override
	public void incrementLocalSequenceNumber() {
		if (data.isInForkedActions() || wrappedDialog == null) {
			data.getLocalSequenceNumber().incrementAndGet();
			// not needed till we have some sort of early dialog replication
			// updateReplicatedState();
		} else {
			super.incrementLocalSequenceNumber();
		}
	}

	@Override
	public boolean isSecure() {
		if (wrappedDialog == null)
			return false;
		return super.isSecure();
	}

	@Override
	public boolean isServer() {
		return false;
	}

	@Override
	public void sendAck(Request arg0) throws SipException {
		verifyDialogExistency();
		super.sendAck(arg0);
	}

	@Override
	public void sendReliableProvisionalResponse(Response arg0)
			throws SipException {
		verifyDialogExistency();
		super.sendReliableProvisionalResponse(arg0);
	}

	@Override
	public void terminateOnBye(boolean arg0) throws SipException {
		verifyDialogExistency();
		super.terminateOnBye(arg0);
	}

	@Override
	public boolean addOngoingTransaction(ServerTransactionWrapper stw) {
		if (data.isInForkedActions()) {
			// Yup, could be reinveite? we have to update local DW cseq :)
			final long cSeqNewValue = ((CSeq) stw.getRequest().getHeader(
					CSeqHeader.NAME)).getSeqNumber();
			final AtomicLong cSeq = data.getLocalSequenceNumber();
			synchronized (cSeq) {
				if (cSeqNewValue <= cSeq.get())
					throw new IllegalArgumentException("Sequence number should not decrease !");
				cSeq.set(cSeqNewValue);
			}
		}
		// super updates replicated state
		return super.addOngoingTransaction(stw);
	}

	// =========================== XXX: Helper methods =====================

	@Override
	public String toString() {
		return new StringBuilder("ClientDialogWrapper Id[").append(
				this.getDialogId()).append("] Handle[").append(
				this.getActivityHandle()).append("] State[").append(
				this.getState()).append("] OngoingCTX[").append(
				this.ongoingClientTransactions.size()).append("] OngoingSTX[")
				.append(this.ongoingServerTransactions.size()).append("]")
				.toString();
	}

	// ###########################################
	// # Strictly DialogActivity defined methods #
	// ###########################################

	@Override
	public ClientTransaction sendCancel() throws SipException {
		verifyDialogExistency();
		return super.sendCancel();
	}

	@Override
	public void associateServerTransaction(ClientTransaction ct,
			ServerTransaction st) {
		// ct MUST be in ongoing transaction, its local, st - comes from another
		// dialog
		verifyDialogExistency();
		super.associateServerTransaction(ct, st);
	}

	@Override
	public Request createRequest(String methodName) throws SipException {
		final HeaderFactory headerFactory = provider.getHeaderFactory();
		Request request = null;
		if (this.wrappedDialog == null) {
			// the real dialog doesn't exist yet so we act like we will build
			// such a dialog when sending this request
			try {
				// create headers
				final FromHeader fromHeader = headerFactory.createFromHeader(
						data.getFromAddress(), super.getLocalTag());
				final ToHeader toHeader = headerFactory.createToHeader(
						data.getToAddress(), null);
				final javax.sip.address.URI requestURI = (URI) data.getToAddress()
						.getURI().clone();
				final ArrayList<ViaHeader> viaHeadersList = new ArrayList<ViaHeader>(
						1);
				viaHeadersList.add(provider.getLocalVia());
				final MaxForwardsHeader maxForwardsHeader = headerFactory
						.createMaxForwardsHeader(70);
				final CSeqHeader cSeqHeader = headerFactory.createCSeqHeader(
						data.getLocalSequenceNumber().get() + 1, methodName);
				request = provider.getMessageFactory()
						.createRequest(requestURI, methodName, data.getCustomCallId(),
								cSeqHeader, fromHeader, toHeader,
								viaHeadersList, maxForwardsHeader);
				for (RouteHeader routeHeader : data.getRouteSet()) {
					request.addLast(routeHeader);
				}
			} catch (Exception e) {
				throw new SipException(e.getMessage(), e);
			}
		} else if (data.isInForkedActions()) {
			try {
				// create request using dialog
				request = this.wrappedDialog.createRequest(methodName);
				request.removeHeader(RouteHeader.NAME);
				for (RouteHeader routeHeader : data.getRouteSet()) {
					request.addLast(routeHeader);
				}
				request.addFirst(provider.getLocalVia());
				// We have wrappedDialog, but we can't rely on some of its info,
				// only CallId, from address, to address, from header are
				// static
				final SipURI requestURI = (SipURI) data.getRequestURI().clone();
				request.setRequestURI(requestURI);
				final CSeqHeader cseq = (CSeqHeader) request
						.getHeader(CSeq.NAME);
				try {
					if (!methodName.equals(Request.CANCEL)
							&& !methodName.equals(Request.ACK)) {
						cseq.setSeqNumber(data.getLocalSequenceNumber().get() + 1);
					}
					requestURI.setMethodParam(cseq.getMethod());
					if (data.getLocalRemoteTag() != null) {
						((ToHeader) request.getHeader(ToHeader.NAME))
								.setTag(data.getLocalRemoteTag());
					}
				} catch (ParseException e) {
					throw new SipException(e.getMessage(), e);
				} catch (InvalidArgumentException e) {
					throw new SipException(e.getMessage(), e);
				}
			} catch (Exception e) {
				throw new SipException(e.getMessage(), e);
			}
		} else {
			request = super.createRequest(methodName);
		}
		return request;
	}

	@Override
	public Request createRequest(Request origRequest) throws SipException {

		// FIXME clarify
		generateRouteList(origRequest);

		Request forgedRequest = null;

		if (this.wrappedDialog != null) {
			forgedRequest = super.createRequest(origRequest);
			if (data.isInForkedActions() && !wrappedDialog.isServer()) {
				// We have wrappedDialog, but we can't rely on some of its info,
				// only CallId, from address, to address, from header are
				// static
				forgedRequest.removeHeader(RouteHeader.NAME);
				final SipURI requestURI = (SipURI) data.getRequestURI().clone();
				forgedRequest.setRequestURI(requestURI);
				final CSeqHeader cseq = (CSeqHeader) forgedRequest
						.getHeader(CSeq.NAME);
				final String method = forgedRequest.getMethod();
				try {
					if (!method.equals(Request.CANCEL)
							&& !method.equals(Request.ACK)) {
						cseq.setSeqNumber(data.getLocalSequenceNumber().get() + 1);
					}
					requestURI.setMethodParam(cseq.getMethod());
					if (data.getLocalRemoteTag() != null) {
						((ToHeader) forgedRequest.getHeader(ToHeader.NAME))
								.setTag(data.getLocalRemoteTag());
					}
				} catch (ParseException e) {
					throw new SipException(e.getMessage(), e);
				} catch (InvalidArgumentException e) {
					throw new SipException(e.getMessage(), e);
				}
			}

		} else {
			try {
				final HeaderFactory headerFactory = provider.getHeaderFactory();
				final MaxForwardsHeader maxForwardsHeader = headerFactory
						.createMaxForwardsHeader(70);
				final ToHeader toHeader = this.provider.getHeaderFactory()
						.createToHeader(data.getToAddress(), null);
				final FromHeader fromHeader = headerFactory.createFromHeader(
						data.getFromAddress(), super.getLocalTag());
				final ArrayList<ViaHeader> viaHeadersList = new ArrayList<ViaHeader>(
						1);
				viaHeadersList.add(provider.getLocalVia());
				// It should be legal to create msg with null params....
				forgedRequest = provider.getMessageFactory()
						.createRequest(
								origRequest.getRequestURI(),
								origRequest.getMethod(),
								data.getCustomCallId(),
								(CSeqHeader) ((CSeqHeader) origRequest
										.getHeader(CSeqHeader.NAME)).clone(),
								fromHeader, toHeader, viaHeadersList,
								maxForwardsHeader);
			} catch (InvalidArgumentException e) {
				throw new SipException(e.getMessage(), e);
			} catch (ParseException e) {
				throw new SipException(e.getMessage(), e);
			}
			for (RouteHeader routeHeader : data.getRouteSet()) {
				forgedRequest.addLast(routeHeader);
			}
			forgeMessage(origRequest, forgedRequest, Utils
					.getHeadersToOmmitOnRequestCopy());
		}

		return forgedRequest;
	}

	@Override
	public ClientTransaction sendRequest(Request request) throws SipException,
			TransactionUnavailableException {

		if (wrappedDialog == null
				&& !Utils.getDialogCreatingMethods().contains(
						request.getMethod())) {
			throw new IllegalStateException(
					"Dialog activity present, but no dialog creating reqeust has been sent yet! This method: "
							+ request.getMethod()
							+ " is not dialog creating one");
		}

		ensureCorrectDialogLocalTag(request);

		final ClientTransactionWrapper ctw = this.provider
				.getNewDialogActivityClientTransaction(this, request);

		final String method = request.getMethod();
		if (method.equals(Request.INVITE))
			lastCancelableTransactionId = ctw.getBranchId();

		final boolean createDialog = wrappedDialog == null;
		if (createDialog) {
			this.wrappedDialog = this.provider.getRealProvider().getNewDialog(
					ctw.getWrappedTransaction());
		} else {
			// only when wrapped dialog exist we need to care about right remote
			// tag
			ensureCorrectDialogRemoteTag(request);
		}

		if (data.isInForkedActions()) {
			// cause dialog spoils - changes cseq, we dont want that
			ctw.sendRequest();
			if (!method.equals(Request.ACK) && !method.equals(Request.CANCEL))
				data.getLocalSequenceNumber().incrementAndGet();
		} else {
			if (createDialog) {
				// dialog in null state does not allows to send request
				ctw.sendRequest();
			} else {
				if (tracer.isInfoEnabled()) {
					tracer.info(String.valueOf(ctw) + " sending request:\n"
							+ request);
				}
				this.wrappedDialog.sendRequest(ctw
						.getWrappedClientTransaction());
			}
		}
		if (createDialog) {
			// assuming this will trigger replicated state update too
			this.wrappedDialog.setApplicationData(this);
		}
		else {
			// not needed till we have some sort of tx replication
			// updateReplicatedState();
		}
		return ctw;
	}

	private void ensureCorrectDialogRemoteTag(Request request)
			throws SipException {
		final String remoteTag = (data.isInForkedActions() && data.getLocalRemoteTag() != null ? data.getLocalRemoteTag()
				: wrappedDialog.getRemoteTag());
		if (remoteTag != null) {
			// ensure we are using the right remote tag
			try {
				((ToHeader) request.getHeader(ToHeader.NAME)).setTag(remoteTag);
			} catch (ParseException e) {
				throw new SipException(e.getMessage(), e);
			}
		}
	}

	@Override
	public void sendRequest(ClientTransaction ct)
			throws TransactionDoesNotExistException, SipException {

		final Request request = ct.getRequest();
		final ClientTransactionWrapper ctw = (ClientTransactionWrapper) ct;

		ensureCorrectDialogLocalTag(request);

		final boolean createDialog = wrappedDialog == null;

		if (createDialog) {
			if (!Utils.getDialogCreatingMethods().contains(request.getMethod())) {
				throw new IllegalStateException(
						"Dialog activity present, but no dialog creating reqeust has been sent yet! This method: "
								+ request.getMethod()
								+ " is not dialog creating one");
			}
			if (request.getMethod().equals(Request.INVITE))
				lastCancelableTransactionId = ctw.getBranchId();
			this.wrappedDialog = this.provider.getRealProvider().getNewDialog(
					ctw.getWrappedTransaction());
			this.wrappedDialog.setApplicationData(this);
			this.addOngoingTransaction(ctw);

			if (createDialog) {
				// dialog in null state does not allows to send request
				ctw.sendRequest();
			} else {
				if (tracer.isInfoEnabled()) {
					tracer.info(String.valueOf(ctw) + " sending request:\n"
							+ request);
				}
				this.wrappedDialog.sendRequest(ctw
						.getWrappedClientTransaction());
			}
		} else {
			ensureCorrectDialogRemoteTag(request);
			if (data.isInForkedActions()) {
				if (!request.getMethod().equals(Request.ACK)
						&& !request.getMethod().equals(Request.CANCEL))
					data.getLocalSequenceNumber().incrementAndGet();
				ctw.sendRequest();
			} else {
				if (tracer.isInfoEnabled()) {
					tracer.info(String.valueOf(ctw) + " sending request:\n"
							+ request);
				}
				wrappedDialog.sendRequest(ctw.getWrappedClientTransaction());
			}
		}
	}

	// ##############################################################
	// # Strictly dialog forge - used when no dialog is present yet #
	// ##############################################################

	/**
	 * 
	 */
	private void verifyDialogExistency() {
		if (wrappedDialog == null) {
			throw new IllegalStateException(
					"Dialog activity present, but no dialog creating reqeust has been sent yet!");
		}
	}

	@Override
	public boolean processIncomingResponse(ResponseEvent respEvent) {

		final Response response = respEvent.getResponse();

		boolean firedByDialogWrapper = false;
		final int statusCode = response.getStatusCode();
		final String toTag = ((ToHeader) response.getHeader(ToHeader.NAME))
				.getTag();
		final DialogForkState oldForkState = data.getForkState();
		DialogWithoutIdActivityHandle masterActivityHandle = (DialogWithoutIdActivityHandle) this.getActivityHandle();

		boolean fineTrace = tracer.isFineEnabled();

		switch (oldForkState) {
		case AWAIT_FIRST_TAG:

			if (100 <= statusCode && statusCode < 200) {
				if (toTag != null) {

					data.setLocalRemoteTag(toTag);
					data.setForkState(DialogForkState.AWAIT_FINAL);
					data.mapTagToDialog(toTag,masterActivityHandle);
					this.fetchData(response);
				}
				if (fineTrace) {
					tracer.fine("Received 1xx message: " + statusCode
							+ ". ToTag:" + toTag + ". Fork state old:"
							+ oldForkState + " - new" + data.getForkState()
							+ ". On dialog: " + this.toString());
				}
				// FIXME: fire on this, original message
				firedByDialogWrapper = false;

			} else if (statusCode < 300) {
				data.setForkState(DialogForkState.END);
				// This one is the master, here we can have only one dialog,
				// this one, so we dont care about simblings, we just dont have
				// those
				// baranowb: what should we do if no toTag?
				if (toTag != null) {
					// THis is for cancel OK, which might not have tag.
					data.setLocalRemoteTag(toTag);
					data.mapTagToDialog(toTag,masterActivityHandle);
				}
				this.fetchData(response);

				if (fineTrace) {
					tracer.fine("Received 2xx message: " + statusCode
							+ ". ToTag:" + toTag + ". Fork state old:"
							+ oldForkState + " - new" + data.getForkState()
							+ ". On dialog: " + this.toString());
				}
				// FIXME: fire on this, original message
				firedByDialogWrapper = false;

				// just in case
				terminateFork(masterActivityHandle);

			} else if (statusCode < 700) {

				data.setForkState(DialogForkState.END);
				if (fineTrace) {
					tracer.fine("Received failure message: " + statusCode
							+ ". ToTag:" + toTag + ". Fork state old:"
							+ oldForkState + " - new" + data.getForkState()
							+ ". On dialog: " + this.toString());
				}
				FireableEventType eventID = this.ra.eventIdCache.getEventId(
						this.ra.getEventLookupFacility(), response);
				ResponseEventWrapper REW = new ResponseEventWrapper(
						this.provider, (ClientTransaction) respEvent
								.getClientTransaction().getApplicationData(),
						this, response);
				this.ra.fireEvent(fineTrace, REW, this.activityHandle, this.getEventFiringAddress(), eventID,
						SipResourceAdaptor.DEFAULT_EVENT_FLAGS, false);
				firedByDialogWrapper = true;
				terminateFork(null);

			} else {
				if (fineTrace) {
					tracer.fine("Received strange message: " + statusCode
							+ ". ToTag:" + toTag + ". Fork state old:"
							+ oldForkState + " - new" + data.getForkState()
							+ ". On dialog: " + this.toString());
				}
			}
			break;
		case AWAIT_FINAL:

			if (100 <= statusCode && statusCode < 200) {

				if (fineTrace) {
					tracer.fine("Received 1xx message: " + statusCode
							+ ". ToTag:" + toTag + ". Fork state old:"
							+ oldForkState + " - new" + data.getForkState()
							+ ". On dialog: " + this.toString());
				}
				if (toTag == null) {
					// FIXME: fire on this
					firedByDialogWrapper = false;
				} else if (toTag.equals(data.getLocalRemoteTag())) {

					this.fetchData(response);
					firedByDialogWrapper = false;
					// FIXME: fire on this
				} else if (data.tagIsMapped(toTag)) {

					// other dialog wins
					ClientDialogWrapper child = (ClientDialogWrapper) this.ra
							.getActivity(data.getDialogMappedToTag(toTag));
					if (child == null) {
						data.unmapDialogMappedToTag(toTag);

						return true;
					}
					child.fetchData(response);
					// FIXME: fire on child, original message
					FireableEventType eventID = this.ra.eventIdCache
							.getEventId(this.ra.getEventLookupFacility(),
									response);
					if (fineTrace) {
						tracer.fine("Received 1xx message: " + statusCode
								+ ". ToTag:" + toTag + "EventId:" + eventID);
					}
					ResponseEventWrapper REW = new ResponseEventWrapper(
							this.provider, (ClientTransaction) respEvent
									.getClientTransaction()
									.getApplicationData(), this, response);
					this.ra.fireEvent(fineTrace, REW, child.activityHandle,child.getEventFiringAddress(), eventID,
							SipResourceAdaptor.DEFAULT_EVENT_FLAGS, false);
					firedByDialogWrapper = true;
				} else {
					// We create fake dialog that - as a child of master
					ClientDialogWrapper child = getNewChildDialogActivity(
							wrappedDialog, (DialogWithoutIdActivityHandle) this
									.getActivityHandle());
					child.data.setForkState(oldForkState);

					child.data.setLocalRemoteTag(toTag);
					child.fetchData(response);
					data.mapTagToDialog(toTag, (DialogWithoutIdActivityHandle) child
							.getActivityHandle());

					// FIXME: fire on original, dialog forked
					FireableEventType eventID = this.ra.eventIdCache
							.getDialogForkEventId(this.ra
									.getEventLookupFacility());

					DialogForkedEvent REW = new DialogForkedEvent(
							this.provider, (ClientTransaction) respEvent
									.getClientTransaction()
									.getApplicationData(), this, child,
							response);
					this.ra.fireEvent(fineTrace, REW, this.activityHandle,this.getEventFiringAddress(), eventID,
							SipResourceAdaptor.DEFAULT_EVENT_FLAGS, false);
					firedByDialogWrapper = true;
				}

			} else if (statusCode < 300) {
				data.setForkState(DialogForkState.END);
				if (tracer.isFineEnabled()) {
					tracer.fine("Received 2xx message: " + statusCode
							+ ". ToTag:" + toTag + ". Fork state old:"
							+ oldForkState + " - new" + data.getForkState()
							+ ". On dialog: " + this.toString());
				}
				// toTag must not be null, but just in case.
				if (data.getLocalRemoteTag() != null && toTag != null
						&& data.getLocalRemoteTag().equals(toTag)) {
					// we win
					this.fetchData(response);
					// FIXME: fire on original, original message
					firedByDialogWrapper = false;
				} else if (toTag != null
						&& data.tagIsMapped(toTag)) {

					// other dialog wins

					ClientDialogWrapper child = (ClientDialogWrapper) this.ra
							.getActivity(data.getDialogMappedToTag(toTag));
					child.fetchData(response);
					masterActivityHandle = (DialogWithoutIdActivityHandle) child.getActivityHandle();
					child.makeMaster();
					if (child != this)
						data.setForkInitialActivityHandle((DialogWithoutIdActivityHandle) child
								.getActivityHandle());
					else {
						tracer.severe("Local: " + data.getLocalRemoteTag() + " : "
								+ toTag + " MSG:\n" + response);
					}

					// FIXME: fire on child, original message
					FireableEventType eventID = this.ra.eventIdCache
							.getEventId(this.ra.getEventLookupFacility(),
									response);
					ResponseEventWrapper REW = new ResponseEventWrapper(
							this.provider, (ClientTransaction) respEvent
									.getClientTransaction()
									.getApplicationData(), this, response);
					this.ra.fireEvent(fineTrace, REW, child.activityHandle, child.getEventFiringAddress(), eventID,
							SipResourceAdaptor.DEFAULT_EVENT_FLAGS, false);
					firedByDialogWrapper = true;

				} else if (toTag != null) {
					// we have completly new master dialog
					// We create fake dialog as a child of master, but this one
					// becomes master
					ClientDialogWrapper child = getNewChildDialogActivity(
							wrappedDialog, null);
					child.data.setForkState(DialogForkState.END);

					child.data.setLocalRemoteTag(toTag);
					masterActivityHandle = (DialogWithoutIdActivityHandle) child.getActivityHandle();
					data.mapTagToDialog(toTag, masterActivityHandle);
					child.makeMaster();
					data.setForkInitialActivityHandle((DialogWithoutIdActivityHandle) child
							.getActivityHandle());
					child.fetchData(response);
					// FIXME: fire on child, original message
					FireableEventType eventID = this.ra.eventIdCache
							.getDialogForkEventId(this.ra
									.getEventLookupFacility());
					DialogForkedEvent REW = new DialogForkedEvent(
							this.provider, (ClientTransaction) respEvent
									.getClientTransaction()
									.getApplicationData(), this, child,
							response);
					this.ra.fireEvent(fineTrace, REW, this.activityHandle,this.getEventFiringAddress(), eventID,
							SipResourceAdaptor.DEFAULT_EVENT_FLAGS, false);
					firedByDialogWrapper = true;
				} else {
					tracer
							.severe("Received 2xx reponse without toTag, this is error.");
				}

				// just in case
				terminateFork(masterActivityHandle);
			} else if (statusCode < 700) {

				data.setForkState(DialogForkState.END);
				if (fineTrace) {
					tracer.fine("Received failure message: " + statusCode
							+ ". ToTag:" + toTag + ". Fork state old:"
							+ oldForkState + " - new" + data.getForkState()
							+ ". On dialog: " + this.toString());
				}
				// FIXME: fire on this, original message
				FireableEventType eventID = this.ra.eventIdCache.getEventId(
						this.ra.getEventLookupFacility(), response);
				ResponseEventWrapper REW = new ResponseEventWrapper(
						this.provider, (ClientTransaction) respEvent
								.getClientTransaction().getApplicationData(),
						this, response);
				this.ra.fireEvent(fineTrace, REW, this.activityHandle,this.getEventFiringAddress(), eventID,
						SipResourceAdaptor.DEFAULT_EVENT_FLAGS, false);
				firedByDialogWrapper = true;
				terminateFork(null);

			} else {
				if (fineTrace) {
					tracer.fine("Received strange message: " + statusCode
							+ ". ToTag:" + toTag + ". Fork state old:"
							+ oldForkState + " - new" + data.getForkState()
							+ ". On dialog: " + this.toString());
				}
			}

			break;
		case END:
			// Strictly for ending other dialogs - response may come late
			// FIXME: add terminate dialog

			if (toTag != null && !toTag.equals(wrappedDialog.getRemoteTag())) {

				// if 1xx+, 3xx+ we ignore it - as it indicates error response
				// to another dialog, we dont care for.
				if (statusCode < 200 || statusCode > 300) {
					if (fineTrace) {
						tracer.fine("Received late message, action IGNORE: "
								+ statusCode + ". ToTag:" + toTag
								+ ". Fork state old:" + oldForkState + " - new"
								+ data.getForkState() + ". On dialog: "
								+ this.toString());
					}

				} else {

					// we are in 2xx zone, we have to ack and send bye
					// FIXME: Add proper termiantion for SUBSCRIBE/REFER ???
					if (Utils.getDialogCreatingMethods().contains(
							((CSeqHeader) response.getHeader(CSeqHeader.NAME))
									.getMethod()))
						LateResponseHandler.doTerminateOnLate2xx(respEvent, ra);
				}

				firedByDialogWrapper = true;

			}

			// else we leave it to be fired by normal means ?
			break;

		}

		// not needed till we have some sort of early dialog replication
		// updateReplicatedState();
		return firedByDialogWrapper;
	}

	/**
	 * 
	 * @param dialog
	 * @return
	 */
	private ClientDialogWrapper getNewChildDialogActivity(Dialog child,
			DialogWithoutIdActivityHandle master) {
		final ClientDialogWrapper dw = new ClientDialogWrapper(child, master,
				ra);
		ra.addActivity(dw, false, tracer.isFineEnabled());
		return dw;
	}

	private void fetchData(Response response) {
		// fetch routeSet, requestURI and ToTag, callId
		// this is done only once?

		if (data.getLocalRemoteTag() == null) {
			// all of those will become solid after tag is set , but will be
			// present without it.
			data.setLocalRemoteTag(((ToHeader) response.getHeader(ToHeader.NAME))
					.getTag());
			data.setRequestURI(null);
			data.clearRouteSet();
		}
		ContactHeader contact = ((ContactHeader) response
				.getHeader(ContactHeader.NAME));
		// issue 623
		if (contact != null)
			if (data.getRequestURI() == null
					|| (contact != null && !data.getRequestURI().equals(contact
							.getAddress().getURI()))) {
				data.setRequestURI((SipURI) contact.getAddress().getURI());
			}

		// FIXME: is this correct?
		if (data.getRouteSet().size() == 0 || data.isRouteSetOnRequest()) {
			data.setRouteSetOnRequest(false);
			data.clearRouteSet();
			try {
				data.addAlltoRouteSet(Utils.getRouteList(response, provider
						.getHeaderFactory()));
			} catch (ParseException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		}

		data.setCustomCallId((CallIdHeader) response.getHeader(CallIdHeader.NAME));

	}

	@SuppressWarnings("unchecked")
	public void generateRouteList(Request origRequest) {
		// FIXME: is this the only case ?

		if (this.wrappedDialog == null) {

			ViaHeader topVia = (ViaHeader) origRequest
					.getHeader(ViaHeader.NAME);
			Address address = null;
			if (topVia != null) {
				address = getLocalAddressForTransport(topVia.getTransport());
			} else {
				if (tracer.isFineEnabled()) {
					tracer
							.fine("There is no via header, can not compute AS contact. Skipping computation of request route.");
				}
				return;
			}
			ListIterator<RouteHeader> lit = origRequest
					.getHeaders(RouteHeader.NAME);
			if (lit != null) {
				GenericURI addressURI = (GenericURI) (address == null ? null
						: address.getURI());
				// we atleast have one header
				// FIXME: possibly we have to do here some proxy work: RFC 3261
				// 16.4 Route Information Preprocessing
				// FIXME: add check to remove first if it points to us.
				while (lit.hasNext()) {
					RouteHeader rh = lit.next();
					GenericURI rhURI = (GenericURI) rh.getAddress().getURI();
					if (addressURI != null && (rhURI.equals(addressURI))) {
					} else {
						data.addToRouteSet(rh);
					}
				}
			}
			// not needed till we have some sort of early dialog replication
			// updateReplicatedState();
		}
	}

	private Address getLocalAddressForTransport(String transport) {
		ListeningPoint lp = this.provider.getListeningPoint(transport);
		Address address = null;
		if (lp != null) {
			try {
				address = this.provider.getAddressFactory().createAddress(
						"Mobicents SIP AS <sip:" + lp.getIPAddress() + ">");
				((SipURI) address.getURI()).setPort(lp.getPort());
			} catch (ParseException e) {
				e.printStackTrace();
				// throw new SipException("Failed to create contact header.",e);
			}
		} else {
			if (tracer.isFineEnabled()) {
				tracer.fine("There is no listening point, for transport: "
						+ transport + ", can not compute AS contact.");
			}
		}
		return address;
	}

	private void terminateFork(SipActivityHandle dialogToRetain) {

		ClientDialogWrapper master = null;
		for (String tag : new HashSet<String>(data.getTagsMappedToDialogs())) {
			
			final DialogWithoutIdActivityHandle child = data.unmapDialogMappedToTag(tag);
			
			if (dialogToRetain == null || !dialogToRetain.equals(child)) {
				// not the one to keep
				final ClientDialogWrapper dw = (ClientDialogWrapper) this.ra.getActivity(child);
				if (dw != null && dw.data.getForkInitialActivityHandle() == null) {
					// this is the master and it seems it is not the one to keep
					// anyway we need to remove it as last
					master = dw;
				}
				else {
					dw.delete();
				}
			}
		}
		if (master != null) {
			master.delete();
		}
	}

	private void makeMaster() {
		data.setForkState(DialogForkState.END);
		data.setForkInitialActivityHandle(null);
		this.wrappedDialog.setApplicationData(this);
	}

	// serialization
	
	private void writeObject(ObjectOutputStream stream) throws IOException {
		stream.defaultWriteObject();
	}

	private void readObject(ObjectInputStream stream) throws IOException,
			ClassNotFoundException {
		stream.defaultReadObject();
		// FIXME currently data is rebuilt due to no support for early dialog failover
		//data.setOwner(this);
		data = new ClientDialogWrapperData(this);
		activityHandle.setActivity(this);
	}
}
