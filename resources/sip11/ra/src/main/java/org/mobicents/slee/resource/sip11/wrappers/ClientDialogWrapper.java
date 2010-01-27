package org.mobicents.slee.resource.sip11.wrappers;

import gov.nist.javax.sip.ListeningPointImpl;
import gov.nist.javax.sip.address.SipUri;
import gov.nist.javax.sip.header.CSeq;
import gov.nist.javax.sip.header.RouteList;
import gov.nist.javax.sip.message.SIPResponse;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import javax.sip.ClientTransaction;
import javax.sip.DialogDoesNotExistException;
import javax.sip.DialogState;
import javax.sip.InvalidArgumentException;
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

import org.mobicents.slee.resource.sip11.ClusteredSipActivityManagement;
import org.mobicents.slee.resource.sip11.DialogWithoutIdActivityHandle;
import org.mobicents.slee.resource.sip11.SipActivityHandle;
import org.mobicents.slee.resource.sip11.SipResourceAdaptor;
import org.mobicents.slee.resource.sip11.SleeSipProviderImpl;
import org.mobicents.slee.resource.sip11.Utils;

public class ClientDialogWrapper extends DialogWrapper {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Tracer tracer;

	protected ClientDialogWrapperData data;
		
	private ClientDialogWrapper(SipActivityHandle activityHandle,
			String localTag, SipResourceAdaptor ra, ClientDialogForkHandler forkData) {
		super(activityHandle, localTag, ra);
		data = new ClientDialogWrapperData(forkData);
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
			CallIdHeader callIdHeader, SipResourceAdaptor ra, ClientDialogForkHandler forkData) {
		this(new DialogWithoutIdActivityHandle(callIdHeader.getCallId(),
				localTag, null), localTag, ra, forkData);
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
	private ClientDialogWrapper(DialogWithoutIdActivityHandle forkHandle, ClientDialogWrapper master) {
		this(forkHandle,master.getLocalTag(), master.ra, new ClientDialogForkHandler((DialogWithoutIdActivityHandle) master.activityHandle));
		this.wrappedDialog = master.wrappedDialog;
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
		
		if (wrappedDialog == null) {
			if (tracer.isFineEnabled()) {
				tracer.fine("Deleting "+getActivityHandle()+" dialog activity, there is no wrapped dialog.");
			}
			// no real dialog
			ra.processDialogTerminated(this);
		} else {
			final ClientDialogForkHandler forkHandler = data.getForkHandler();
			if (forkHandler.getMaster() == null) {
				// we are master, if it is forking lets terminate all forks
				forkHandler.terminate(ra);
				if (forkHandler.getForkWinner() == null) {
					// this is the confirmed dialog, it's safe to delete the wrapped dialog in super
					if (tracer.isFineEnabled()) {
						tracer.fine("Fully deleting "+getActivityHandle()+" dialog, there is no confirmed fork.");
					}
					super.delete();
				}
				else {
					// a fork confirmed, just terminate the activity for this one
					if (tracer.isFineEnabled()) {
						tracer.fine("Deleting "+getActivityHandle()+" dialog activity, the wrapped dialog is still used by a confirmed fork.");
					}
					ra.processDialogTerminated(this);
				}
			}
			else {
				// a fork dialog
				DialogWithoutIdActivityHandle forkWinner = forkHandler.getForkWinner();
				if (forkHandler.isForking() || forkWinner == null || !forkWinner.equals(getActivityHandle())) {
					if (tracer.isFineEnabled()) {
						tracer.fine("Deleting "+getActivityHandle()+" dialog activity, a fork which was not confirmed.");
					}
					ra.processDialogTerminated(this);
				}
				else {
					if (tracer.isFineEnabled()) {
						tracer.fine("Fully deleting "+getActivityHandle()+" dialog, a fork which was confirmed.");
					}
					super.delete();					
				}
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

	@SuppressWarnings("deprecation")
	@Override
	public Transaction getFirstTransaction() {
		verifyDialogExistency();
		return super.getFirstTransaction();
	}

	@Override
	public Address getLocalParty() {
		if (wrappedDialog != null && !data.getForkHandler().isForking()) {
			return super.getLocalParty();
		} else {
			return data.getFromAddress();
		}
	}

	@Override
	public Address getRemoteParty() {
		if (wrappedDialog != null && !data.getForkHandler().isForking()) {
			return super.getRemoteParty();
		} else {
			return data.getToAddress();
		}
	}

	@Override
	public Address getRemoteTarget() {
		if (wrappedDialog != null && !data.getForkHandler().isForking()) {
			return super.getRemoteTarget();
		} else {
			return data.getToAddress();
		}
	}

	@Override
	public long getLocalSeqNumber() {
		if (data.getForkHandler().isForking() || wrappedDialog == null) {
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
		if (wrappedDialog != null && !data.getForkHandler().isForking()) {
			return super.getRemoteTag();
		} else {
			return data.getLocalRemoteTag();
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
		if (data.getForkHandler().isForking() || wrappedDialog == null) {
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
		if (data.getForkHandler().isForking()) {
			// Yup, could be reinveite? we have to update local DW cseq :)
			final long cSeqNewValue = ((CSeqHeader) stw.getRequest().getHeader(
					CSeqHeader.NAME)).getSeqNumber();
			final AtomicLong cSeq = data.getLocalSequenceNumber();
			if (cSeqNewValue > cSeq.get())
				cSeq.set(cSeqNewValue);			
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
				this.ongoingClientTransactions.keySet()).append("] OngoingSTX[")
				.append(this.ongoingServerTransactions.keySet()).append("]")
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
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.resource.sip11.wrappers.DialogWrapper#createRequest(javax.sip.message.Request)
	 */
	@Override
	public Request createRequest(Request origRequest) throws SipException {
		Request request = super.createRequest(origRequest);
		if (wrappedDialog != null && data.getForkHandler().isForking()) {
			updateRequestWithForkData(request);
		}
		return request;
	}
	
	@Override
	public Request createRequest(String methodName) throws SipException {
		
		if (methodName.equals(Request.ACK) || methodName.equals(Request.PRACK)) {
            throw new SipException("Invalid method specified for createRequest:" + methodName);
        }
		
		final SleeSipProviderImpl provider = ra.getProviderWrapper();
		final HeaderFactory headerFactory = provider.getHeaderFactory();
		Request request = null;
		if (this.wrappedDialog == null) {
			// the real dialog doesn't exist yet so we act like we will build
			// such a dialog when sending this request
			
			try {
				// create headers
				SipUri requestURI = null;
				if (this.getRemoteTarget() != null)
					requestURI = (SipUri) getRemoteTarget().getURI().clone();
				else {
					requestURI = (SipUri) data.getToAddress().getURI().clone();
					requestURI.clearUriParms();
				}
				final FromHeader fromHeader = headerFactory.createFromHeader(
						data.getFromAddress(), getLocalTag());
				final ToHeader toHeader = headerFactory.createToHeader(
						data.getToAddress(), null);
				final List<Object> viaHeadersList = new ArrayList<Object>(1);
				viaHeadersList.add(provider.getLocalVia());
				final MaxForwardsHeader maxForwardsHeader = headerFactory
				.createMaxForwardsHeader(70);
				final CSeqHeader cSeqHeader = headerFactory.createCSeqHeader(
						data.getLocalSequenceNumber().get() + 1, methodName);
				request = provider.getMessageFactory()
				.createRequest(requestURI, methodName, data.getCustomCallId(),
						cSeqHeader, fromHeader, toHeader,
						viaHeadersList, maxForwardsHeader);
				
				final RouteList routeList = data.getLocalRouteList();
				if (routeList != null) {
					request.setHeader(routeList);
				}
				
			} catch (Exception e) {
				throw new SipException(e.getMessage(), e);
			}
		} else if (data.getForkHandler().isForking()) {
			// create request using dialog
			request = this.wrappedDialog.createRequest(methodName);
			updateRequestWithForkData(request);
		} else {
			request = super.createRequest(methodName);
		}
		if (getState() == null) {
			// adds load balancer to route if exists
			ra.getProviderWrapper().addLoadBalancerToRoute(request);
		}
		return request;
	}

	private void updateRequestWithForkData(Request request) throws SipException {
		final SipURI requestURI = (SipURI) data.getRequestURI().clone();
		request.setRequestURI(requestURI);
		
		final RouteList routeList = data.getLocalRouteList();
		if (routeList != null) {
			request.setHeader(routeList);
		}
		
		final CSeqHeader cseq = (CSeqHeader) request
				.getHeader(CSeq.NAME);
		try {
			
			if (!request.getMethod().equals(Request.CANCEL)
					&& !request.getMethod().equals(Request.ACK)) {
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

		final SleeSipProviderImpl provider = ra.getProviderWrapper();
		final ClientTransactionWrapper ctw = provider
				.getNewDialogActivityClientTransaction(this, request);

		final String method = request.getMethod();
		if (method.equals(Request.INVITE))
			lastCancelableTransactionId = ctw.activityHandle;

		final boolean createDialog = wrappedDialog == null;
		if (createDialog) {
			this.wrappedDialog = provider.getRealProvider().getNewDialog(
					ctw.getWrappedTransaction());
			this.wrappedDialog.setApplicationData(this);
		} else {
			// only when wrapped dialog exist we need to care about right remote
			// tag
			ensureCorrectDialogRemoteTag(request);			
		}

		if (tracer.isInfoEnabled()) {
			tracer.info(String.valueOf(ctw) + " sending request:\n"
					+ request);
		}
		
		if (data.getForkHandler().isForking()) {
			// cause dialog spoils - changes cseq, we dont want that
			if (!method.equals(Request.ACK) && !method.equals(Request.CANCEL))
				data.getLocalSequenceNumber().incrementAndGet();
			ctw.getWrappedClientTransaction().sendRequest();			
		} else {
			if (createDialog) {
				// dialog in null state does not allows to send request
				ctw.getWrappedClientTransaction().sendRequest();
			} else {
				this.wrappedDialog.sendRequest(ctw
						.getWrappedClientTransaction());
			}
		}
		
		return ctw;
	}

	private void ensureCorrectDialogRemoteTag(Request request)
			throws SipException {
		final String remoteTag = (data.getForkHandler().isForking() && data.getLocalRemoteTag() != null ? data.getLocalRemoteTag()
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

		final SleeSipProviderImpl provider = ra.getProviderWrapper();
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
				lastCancelableTransactionId = ctw.getActivityHandle();
			this.wrappedDialog = provider.getRealProvider().getNewDialog(
					ctw.getWrappedTransaction());
			this.wrappedDialog.setApplicationData(this);
			this.addOngoingTransaction(ctw);

			if (tracer.isInfoEnabled()) {
				tracer.info(String.valueOf(ctw) + " sending request:\n"
						+ request);
			}
			// dialog in null state does not allows to send request
			ctw.getWrappedClientTransaction().sendRequest();			
		} else {
			ensureCorrectDialogRemoteTag(request);
			if (tracer.isInfoEnabled()) {
				tracer.info(String.valueOf(ctw) + " sending request:\n"
						+ request);
			}
			if (data.getForkHandler().isForking()) {
				if (!request.getMethod().equals(Request.ACK)
						&& !request.getMethod().equals(Request.CANCEL))
					data.getLocalSequenceNumber().incrementAndGet();
				ctw.getWrappedClientTransaction().sendRequest();
			} else {
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
					"Dialog activity present, but no dialog creating request has been sent yet!");
		}
	}

	@Override
	public boolean processIncomingResponse(ResponseEvent respEvent) {

		final ClientDialogForkHandler forkHandler = data.getForkHandler();
		if (!forkHandler.isForking()) {
			// nothing to do, let the ra fire the event
			return false;
		}
		
		final Response response = respEvent.getResponse();

		boolean eventFired = false;
		final int statusCode = response.getStatusCode();
		final String toTag = ((ToHeader) response.getHeader(ToHeader.NAME))
				.getTag();

		boolean fineTrace = tracer.isFineEnabled();

		if (statusCode < 200) {		
			
			if (toTag != null) {
				if (data.getLocalRemoteTag() == null) {
					if (fineTrace) {
						tracer.fine("Client dialog "+getActivityHandle()+" received "+statusCode+" response with first remote tag "+toTag);
					}
					data.setLocalRemoteTag(toTag);
					this.fetchData(response);
				}
				else if (data.getLocalRemoteTag().equals(toTag)) {
					if (fineTrace) {
						tracer.fine("Client dialog "+getActivityHandle()+" received "+statusCode+" response with first remote tag "+toTag+" again");
					}
					this.fetchData(response);
				}
				else {
					// fork
					DialogWithoutIdActivityHandle forkHandle = forkHandler.getFork(toTag);
					if (forkHandle == null) {
						if (fineTrace) {
							tracer.fine("Client dialog "+getActivityHandle()+" received "+statusCode+" response with new fork remote tag "+toTag);
						}
						ClientDialogWrapper forkDialog = getNewDialogFork(toTag,fineTrace);
						forkDialog.fetchData(response);
						forkHandler.addFork(ra, (DialogWithoutIdActivityHandle) forkDialog.getActivityHandle());
						// fire dialog forked event on original activity
						fireDialogForkEvent(respEvent, forkDialog, fineTrace);
						eventFired = true;
					}
					else {
						if (fineTrace) {
							tracer.fine("Client dialog "+getActivityHandle()+" received "+statusCode+" response with existent fork remote tag "+toTag);
						}
						ClientDialogWrapper forkDialog = (ClientDialogWrapper) this.ra.getActivity(forkHandle);
						if (forkDialog == null) {
							// dude, where is my dialog?
							if (tracer.isSevereEnabled()) {
								tracer.severe("Can't find dialog "+activityHandle+" fork with remote tag "+toTag+" in RA's activity management");
							}
							return true;
						}
						forkDialog.fetchData(response);
						// fire event on fork activity
						fireReceivedEvent(respEvent,forkDialog,fineTrace);
						eventFired = true;
					}					
				}				
			}
		}
		else if (statusCode < 300) {
			
			if (toTag != null) {
				if (data.getLocalRemoteTag() == null) {
					if (fineTrace) {
						tracer.fine("Client dialog "+getActivityHandle()+" confirmed with remote tag "+toTag);
					}
					data.setLocalRemoteTag(toTag);
					if (!ra.inLocalMode()) {
						((ClusteredSipActivityManagement)ra.getActivityManagement()).replicateRemoteTag((DialogWithoutIdActivityHandle) getActivityHandle(),toTag);
					}
					this.fetchData(response);
					forkHandler.terminate(ra);
					
				}
				else if (data.getLocalRemoteTag().equals(toTag)) {
					if (fineTrace) {
						tracer.fine("Client dialog "+getActivityHandle()+" confirmed with remote tag "+toTag);
					}
					if (!ra.inLocalMode()) {
						((ClusteredSipActivityManagement)ra.getActivityManagement()).replicateRemoteTag((DialogWithoutIdActivityHandle) getActivityHandle(),toTag);
					}
					this.fetchData(response);
					forkHandler.terminate(ra);
				}
				else {
					// fork
					DialogWithoutIdActivityHandle forkHandle = forkHandler.getFork(toTag);
					if (forkHandle == null) {
						if (fineTrace) {
							tracer.fine("Client dialog "+getActivityHandle()+" confirmed with new fork remote tag "+toTag);
						}
						final ClientDialogWrapper forkDialog = getNewDialogFork(toTag,fineTrace);
						forkDialog.fetchData(response);
						// end forking
						forkHandler.forkConfirmed(ra, this, forkDialog);
						// fire dialog forked event on original activity
						fireDialogForkEvent(respEvent, forkDialog, fineTrace);
						eventFired = true;						
					}
					else {
						if (fineTrace) {
							tracer.fine("Client dialog "+getActivityHandle()+" confirmed with existent fork remote tag "+toTag);
						}
						final ClientDialogWrapper forkDialog = (ClientDialogWrapper) ra
						.getActivityManagement().get(forkHandle);
						if (forkDialog == null) {
							// dude, where is my dialog?
							if (tracer.isSevereEnabled()) {
								tracer.severe("Can't find dialog "+activityHandle+" fork with remote tag "+toTag+" in RA's activity management");
							}
							return true;
						}
						forkHandler.forkConfirmed(ra, this, forkDialog);
						forkDialog.fetchData(response);
						// fire event on fork activity
						fireReceivedEvent(respEvent,forkDialog,fineTrace);
						eventFired = true;						
					}
				}
				// if in cluster mode we now need to redo replication since the wrapper state chnaged
				wrappedDialog.setApplicationData(this);
			}
			
		}
		else {
			if (fineTrace) {
				tracer.fine("Client dialog "+getActivityHandle()+" received "+statusCode+" error response, terminating fork");
			}
			// error while forking, fire event and terminate forking
			fireReceivedEvent(respEvent, this, fineTrace);
			eventFired = true;
			this.data.getForkHandler().terminate(ra);
		}
		
		// not needed till we have some sort of early dialog replication
		// updateReplicatedState();
		return eventFired;
	}

	/**
	 * @param respEvent
	 * @param dw
	 * @param fineTrace
	 */
	private void fireReceivedEvent(ResponseEvent respEvent,
			ClientDialogWrapper dw, boolean fineTrace) {
		final FireableEventType eventType = ra.getEventIdCache()
		.getEventId(ra.getEventLookupFacility(),
				respEvent.getResponse());
		final ResponseEventWrapper eventObject = new ResponseEventWrapper(
		ra.getProviderWrapper(), (ClientTransaction) respEvent
				.getClientTransaction()
				.getApplicationData(), this, respEvent.getResponse());
		fireEvent(fineTrace, eventObject, dw.activityHandle,dw.getEventFiringAddress(), eventType);
		
	}

	/**
	 * 
	 * @param respEvent
	 * @param fork
	 * @param fineTrace
	 */
	private void fireDialogForkEvent(ResponseEvent respEvent, ClientDialogWrapper fork, boolean fineTrace) {
		// fire dialog forked event on original activity
		final FireableEventType eventID = ra.getEventIdCache()
				.getDialogForkEventId(ra
						.getEventLookupFacility());
		final DialogForkedEvent eventObject = new DialogForkedEvent(
				ra.getProviderWrapper(), respEvent
						.getClientTransaction(), this, fork,
				respEvent.getResponse());
		fireEvent(fineTrace, eventObject, this.activityHandle,this.getEventFiringAddress(), eventID);
	}
	
	/**
	 * @param fineTrace
	 * @param event
	 * @param activityHandle
	 * @param eventFiringAddress
	 * @param eventID
	 */
	private void fireEvent(boolean fineTrace, Object event,
			SipActivityHandle activityHandle,
			javax.slee.Address eventFiringAddress, FireableEventType eventType) {
		if (ra.getEventIDFilter().filterEvent(eventType)) {
			if(fineTrace) {
				tracer.fine("Event "+(eventType==null?"null":eventType.getEventType())+" filtered.");
			}
			return;
		}
		try {
			ra.getSleeEndpoint().fireEvent(activityHandle, eventType, event, eventFiringAddress, null,SipResourceAdaptor.DEFAULT_EVENT_FLAGS);
		} catch (Throwable e) {
			tracer.severe("Failed to fire event",e);
		}
	}

	/**
	 * 
	 * @param dialog
	 * @return
	 */
	private ClientDialogWrapper getNewDialogFork(String forkRemoteTag, boolean fineTrace) {
		final DialogWithoutIdActivityHandle originalHandle = (DialogWithoutIdActivityHandle) this.activityHandle;
		final DialogWithoutIdActivityHandle forkHandle = new DialogWithoutIdActivityHandle(originalHandle.getCallId(), originalHandle.getLocalTag(), forkRemoteTag);
		final ClientDialogWrapper dw = new ClientDialogWrapper(forkHandle, this);
		dw.data.setLocalRemoteTag(forkRemoteTag);
		data.getForkHandler().addFork(ra, forkHandle);
		ra.addActivity(dw, false, fineTrace);
		return dw;
	}

	private void fetchData(Response response) {
		// fetch routeSet, requestURI and ToTag, callId
		// this is done only once?

		data.setLocalRemoteTag(((ToHeader) response.getHeader(ToHeader.NAME))
					.getTag());
		ContactHeader contact = ((ContactHeader) response
				.getHeader(ContactHeader.NAME));
		// issue 623
		if (contact != null)
			if (data.getRequestURI() == null
					|| (contact != null && !data.getRequestURI().equals(contact
							.getAddress().getURI()))) {
				//FIXME: it can be gov.nist.javax.sip.address.GenericURI or TelURI!!, we must check, its an error condition and report.
				data.setRequestURI((SipURI) contact.getAddress().getURI());
			}

		// save the route, but ensure we don't save the top route pointing to us
		final SIPResponse sipResponse = (SIPResponse) response;
		RouteList routeList = sipResponse.getRouteHeaders();
		if (routeList != null) {
			final RouteHeader topRoute = routeList.get(0);
			final URI topRouteURI = topRoute.getAddress().getURI();
			if (topRouteURI.isSipURI()) {
				final SipURI topRouteSipURI = (SipURI) topRouteURI;
				final String transport = ((ViaHeader)sipResponse.getHeader(ViaHeader.NAME)).getTransport();
				final ListeningPointImpl lp = (ListeningPointImpl) ra.getProviderWrapper().getListeningPoint(transport);
				if (topRouteSipURI.getHost().equals(
						lp.getIPAddress())
						&& topRouteSipURI.getPort() == lp.getPort()) {
					if (routeList.size() > 1) {
						routeList = (RouteList) routeList.clone();
						routeList.remove(0);
						data.setLocalRouteList(routeList);
					}					
				}
				else {
					data.setLocalRouteList((RouteList) routeList.clone());
				}
			}
		}
		
		data.setCustomCallId((CallIdHeader) response.getHeader(CallIdHeader.NAME));

	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.resource.sip11.wrappers.Wrapper#clear()
	 */
	@Override
	public void clear() {
		final ClientDialogForkHandler forkHandler = data.getForkHandler();
		if (forkHandler.getMaster() != null) {
			if (wrappedDialog != null) {			
				// a confirmed fork, remove the master now
				if (tracer.isFineEnabled()) {
					tracer.fine("Confirmed fork dialog "+getActivityHandle()+" ended, removing its master dialog");
				}
				ra.getActivityManagement().remove(forkHandler.getMaster());
			}							
		}		
		else {
			if (forkHandler.getForkWinner() != null) {
				// a fork confirmed the dialog, lets add it again to the activities map, to be able to handle late fork confirmations
				if (tracer.isFineEnabled()) {
					tracer.fine("Restoring dialog "+getActivityHandle()+" to the RA's activity management o handle possible late 2xx responses, a fork was confirmed.");
				}
				ra.getActivityManagement().put(getActivityHandle(), this);
			}
		}
		
		super.clear();
		data = null;
	}
	
	// serialization
	
	private void writeObject(ObjectOutputStream stream) throws IOException {
		stream.defaultWriteObject();
	}

	private void readObject(ObjectInputStream stream) throws IOException,
			ClassNotFoundException {
		stream.defaultReadObject();		
		activityHandle.setActivity(this);
	}
}
