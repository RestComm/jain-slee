package org.mobicents.slee.resource.sip11.wrappers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.sip.ClientTransaction;
import javax.sip.Dialog;
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
import javax.sip.header.CallIdHeader;
import javax.sip.header.ContactHeader;
import javax.sip.header.ContentTypeHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.Header;
import javax.sip.header.Parameters;
import javax.sip.header.RouteHeader;
import javax.sip.header.ToHeader;
import javax.sip.header.ViaHeader;
import javax.sip.message.Message;
import javax.sip.message.Request;
import javax.sip.message.Response;
import javax.slee.AddressPlan;
import javax.slee.facilities.Tracer;

import net.java.slee.resource.sip.DialogActivity;

import org.mobicents.slee.resource.sip11.DialogWithIdActivityHandle;
import org.mobicents.slee.resource.sip11.SipActivityHandle;
import org.mobicents.slee.resource.sip11.SipResourceAdaptor;
import org.mobicents.slee.resource.sip11.SleeSipProviderImpl;

/**
 * Abstract dialog wrapper code.
 * 
 * @author martins
 *
 */
public class DialogWrapper extends Wrapper implements DialogActivity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	protected transient SipResourceAdaptor ra;
	
	/**
	 * 
	 */
	protected transient SleeSipProviderImpl provider;
	
	/**
	 * 
	 */
	protected transient ConcurrentHashMap<String, ClientTransaction> ongoingClientTransactions = new ConcurrentHashMap<String, ClientTransaction>(1);
	
	/**
	 * 
	 */
	protected transient ConcurrentHashMap<String, ServerTransaction> ongoingServerTransactions = new ConcurrentHashMap<String, ServerTransaction>(1);
	
	/**
	 * the wrapped dialog
	 */
	protected Dialog wrappedDialog;
	
	/**
	 * the local tag of the dialog, for certain cases it is out of sync with wrapped dialog, because the wrapped dialog may be created without one assigned 
	 */
	private String localTag;
	
	/*
	 * the slee {@link Address} where events on this dialog are fired.
	 */
	protected transient javax.slee.Address eventFiringAddress;
	
	/**
	 * tracer for this class
	 */
	private static Tracer tracer;

	/**
	 * 
	 */
	protected String lastCancelableTransactionId;

	// Constructors 

	/**
	 * 
	 * @param wrappedDialog
	 * @param dialogId
	 * @param localTag
	 * @param ra
	 * @throws IllegalArgumentException
	 */
	public DialogWrapper(Dialog wrappedDialog, String dialogId, String localTag, SipResourceAdaptor ra) throws IllegalArgumentException {
		this(new DialogWithIdActivityHandle(dialogId),localTag,ra);
		this.wrappedDialog = wrappedDialog;
		this.wrappedDialog.setApplicationData(this);
	}
	
	/**
	 * Extensible constructor.
	 * @param sipActivityHandle
	 * @param localTag
	 * @param ra
	 */
	protected DialogWrapper(SipActivityHandle sipActivityHandle, String localTag,SipResourceAdaptor ra) {
		super(sipActivityHandle);
		setResourceAdaptor(ra);
		this.localTag = localTag;
	}		
	
	@Override
	public void setResourceAdaptor(SipResourceAdaptor ra) {
		super.setResourceAdaptor(ra);
		this.ra = ra;
		this.provider = ra.getProviderWrapper();
		if (tracer == null) {
			tracer = ra.getTracer(DialogWrapper.class.getSimpleName());
		}
	}
	
	// Wrapper Methods

	@Override
	public javax.slee.Address getEventFiringAddress() {
		if (eventFiringAddress == null) {
			eventFiringAddress = new javax.slee.Address(AddressPlan.SIP, wrappedDialog.getLocalParty().toString());			
		}
		return eventFiringAddress;
	}	

	@Override
	public boolean isAckTransaction() {
		return false;
	}
	
	@Override
	public boolean isActivity() {
		return true;
	}
	
	@Override
	public boolean isDialog() {
		return true;
	}
	
	// Dialog Activity Methods
	
	/*
	 * (non-Javadoc)
	 * @see net.java.slee.resource.sip.DialogActivity#associateServerTransaction(javax.sip.ClientTransaction, javax.sip.ServerTransaction)
	 */
	public void associateServerTransaction(ClientTransaction ct, ServerTransaction st) {
		if (ct == null) {
			throw new NullPointerException("null client transaction");
		}		
		if (st == null) {
			throw new NullPointerException("null server transaction");
		}		
		if (!hasOngoingClientTransaction(ct.getBranchId())) {
			throw new IllegalArgumentException("client transaction is not in ongoing transaction list.");
		}
		final DialogWrapper stDialog = (DialogWrapper) st.getDialog();
		if (stDialog == null) {
			throw new IllegalArgumentException("the specified server transaction has no dialog.");
		}					
		((ClientTransactionWrapper) ct).associateServerTransaction(st.getBranchId(), stDialog.getActivityHandle());
	}

	/*
	 * (non-Javadoc)
	 * @see net.java.slee.resource.sip.DialogActivity#getAssociatedServerTransaction(javax.sip.ClientTransaction)
	 */
	public ServerTransaction getAssociatedServerTransaction(ClientTransaction ct) {
		if (!hasOngoingClientTransaction(ct.getBranchId())) {
			return null;
		}
		final ClientTransactionWrapper ctw = (ClientTransactionWrapper) ct;
		final ClientTransactionAssociation cta = ctw.getClientTransactionAssociation();
		if (cta != null) {
			final DialogWrapper associatedDialog = (DialogWrapper) ra.getActivity(cta.getDialogActivityHandle());
			if (associatedDialog != null) {
				return associatedDialog.getServerTransaction(cta.getAssociatedServerTransactionBranchId());
			}
			else {
				return null;
			}
		}
		else {
			return null;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.sip.Dialog#createRequest(java.lang.String)
	 */
	public Request createRequest(String methodName) throws SipException {
		try{
			final Request request = this.wrappedDialog.createRequest(methodName);
			//request.addFirst(getLocalViaHeader());
			return request;
		}catch (Exception e) {
			throw new SipException(e.getMessage(),e);
		}
	}
	
	/* FIXME untested
	 * (non-Javadoc)
	 * @see net.java.slee.resource.sip.DialogActivity#createRequest(javax.sip.message.Request)
	 */
	public Request createRequest(Request origRequest) throws SipException {
		
		Request forgedRequest = null;
		try {
			if (!origRequest.getMethod().equals(Request.ACK)) {
				forgedRequest = this.wrappedDialog.createRequest(origRequest.getMethod());
			}
			else {
				forgedRequest = this.wrappedDialog.createAck(this.wrappedDialog.getLocalSeqNumber());
			}		
			forgedRequest.addFirst(provider.getLocalVia());
		} catch (ParseException e) {
			throw new SipException(e.getMessage(),e);
		} catch (InvalidArgumentException e) {
			throw new SipException(e.getMessage(),e);
		}

		forgeMessage(origRequest, forgedRequest, org.mobicents.slee.resource.sip11.Utils.getHeadersToOmmitOnRequestCopy());
		
		return forgedRequest;
	}

	/**
	 * FIXME untested
	 * @param originalMessage
	 * @param forgedMessage
	 * @param headerstoOmmit
	 * @throws SipException
	 */
	@SuppressWarnings("unchecked")
	protected void forgeMessage(Message originalMessage, Message forgedMessage, Set<String> headerstoOmmit)
	throws SipException {
		// We leave to and from with tags from this dialog, but copy all other
		// parameters
		// Route headers are from this dialog
		ListIterator<String> lit = originalMessage.getHeaderNames();
		while (lit.hasNext()) {

			String headerName = lit.next();

			// FIXME: Could be wrong - but, if there is value in forgedRequest,
			// we leave original value
			// FIXME: What about address URI parameters?
			// FIXME: What about MaxForwards?
			if (headerName.equals(ToHeader.NAME) || headerName.equals(FromHeader.NAME)) {
				final Parameters origHeader = (Parameters) originalMessage.getHeader(headerName);
				final Parameters forgedHeader = (Parameters) forgedMessage.getHeader(headerName);
				final Set<String> toOmmit = new HashSet<String>(1);
				toOmmit.add("tag");
				copyParameters(headerName, origHeader, forgedHeader, toOmmit);
			} else if (headerstoOmmit.contains(headerName)) {
				continue;
			} else {
				// FIXME: For now we simply copy everything, overwrte existing
				// headers

				if (forgedMessage.getHeaders(headerName).hasNext())
					forgedMessage.removeHeader(headerName);

				ListIterator<Header> headersIterator = originalMessage.getHeaders(headerName);
				while (headersIterator.hasNext()) {
					final Header origHeader = headersIterator.next();

					try {
						final Header forgedHeader = (Header) this.provider.getHeaderFactory().createHeader(headerName,
								origHeader.toString().substring(origHeader.toString().indexOf(":") + 1));

						forgedMessage.addLast((javax.sip.header.Header) forgedHeader);
					} catch (ParseException e) {
						tracer.severe("Failed to generate header on [" + headerName + "]. To copy value [" + origHeader
								+ "]\n", e);
						throw new SipException("Major failure", e);
					}

				}
			}
		}

		// Copy content
		byte[] rawOriginal = originalMessage.getRawContent();
		if (rawOriginal != null && rawOriginal.length != 0) {
			byte[] copy = new byte[rawOriginal.length];
			System.arraycopy(rawOriginal, 0, copy, 0, copy.length);
			try {
				forgedMessage.setContent(new String(copy), (ContentTypeHeader) forgedMessage
						.getHeader(ContentTypeHeader.NAME));
			} catch (ParseException e) {
				tracer.severe("Failed to set content on forged message. To copy value [" + new String(copy) + "] Type ["
						+ forgedMessage.getHeader(ContentTypeHeader.NAME) + "]\n", e);
			}
		}
	}

	/**
	 * FIXME untested
	 * @param name
	 * @param origHeader
	 * @param forgedHeader
	 * @param toOmmit
	 */
	@SuppressWarnings("unchecked")
	private void copyParameters(String name, Parameters origHeader, Parameters forgedHeader, Set<String> toOmmit) {
		// FIXME: This will fail for parameters such as lr ??
		Iterator<String> it = origHeader.getParameterNames();
		while (it.hasNext()) {
			String p_name = it.next();
			if (toOmmit.contains(p_name) || forgedHeader.getParameter(p_name) != null) {
				if (tracer.isFineEnabled()) {
					tracer.fine("Ommiting parameter on [" + name + "]. To copy value ["
							+ origHeader.getParameter(p_name) + "]\nValue in forged ["
							+ forgedHeader.getParameter(p_name) + "]");
				}
			} else {
				try {
					forgedHeader.setParameter(p_name, origHeader.getParameter(p_name));
				} catch (ParseException e) {
					tracer.severe("Failed to pass parameter on [" + name + "]. To copy value ["
							+ origHeader.getParameter(p_name) + "]\nValue in forged ["
							+ forgedHeader.getParameter(p_name) + "]", e);
				}
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.java.slee.resource.sip.DialogActivity#sendRequest(javax.sip.message.Request)
	 */
	public ClientTransaction sendRequest(Request request) throws SipException, TransactionUnavailableException {
		ensureCorrectDialogLocalTag(request);
		final ClientTransactionWrapper ctw = this.provider.getNewDialogActivityClientTransaction(this,request);
		if (request.getMethod().equals(Request.INVITE))
			lastCancelableTransactionId = ctw.getBranchId();
		if (tracer.isInfoEnabled()) {
			tracer.info(String.valueOf(ctw)+" sending request:\n"+request);
		}
		wrappedDialog.sendRequest(ctw.getWrappedClientTransaction());				
		return ctw;
	}
	
	/**
	 * 
	 * @param request
	 * @throws SipException
	 */
	protected void ensureCorrectDialogLocalTag(Request request) throws SipException {
		if (localTag != null) {
			// ensure we are using the right tag
			try {
				((FromHeader)request.getHeader(FromHeader.NAME)).setTag(localTag);
			} catch (ParseException e) {
				throw new SipException(e.getMessage(),e);
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.sip.Dialog#sendRequest(javax.sip.ClientTransaction)
	 */
	public void sendRequest(ClientTransaction ct) throws TransactionDoesNotExistException, SipException {
		final Request request = ct.getRequest();
		ensureCorrectDialogLocalTag(request);
		if (tracer.isInfoEnabled()) {
			tracer.info(String.valueOf(ct)+" sending request:\n"+request);
		}
		wrappedDialog.sendRequest(((ClientTransactionWrapper)ct).getWrappedClientTransaction());		
	}
	
	/*
	 * FIXME untested
	 * (non-Javadoc)
	 * @see net.java.slee.resource.sip.DialogActivity#createResponse(javax.sip.ServerTransaction, javax.sip.message.Response)
	 */
	public Response createResponse(ServerTransaction origServerTransaction, Response receivedResponse) throws SipException {

		if (!this.hasOngoingServerTransaction(origServerTransaction.getBranchId()))
			throw new IllegalArgumentException("Passed server transaction is not in ongoing server transaction list for the dialog.");

		Response forgedResponse;
		try {
			// FIXME check this works as expected
			forgedResponse = this.provider.getMessageFactory().createResponse(receivedResponse.getStatusCode(), origServerTransaction.getRequest());
		} catch (ParseException e) {
			throw new SipException("Failed to forge message", e);
		}
		
		forgeMessage(receivedResponse, forgedResponse, org.mobicents.slee.resource.sip11.Utils.getHeadersToOmmitOnResponseCopy());
		
		//This is ok, we receveid this dialog on one LP, we need to put Contact there.
		final Request origRequest = origServerTransaction.getRequest();
		final ViaHeader topVia = (ViaHeader) origRequest.getHeader(ViaHeader.NAME);
		if (topVia!=null) {
			final Address address = provider.getAddressFactory().createAddress(provider.getLocalSipURI(topVia.getTransport()));
			if (address !=null) {
				final ContactHeader contactHeader = this.provider.getHeaderFactory().createContactHeader(address);
				forgedResponse.addLast(contactHeader);
			} 
			else {
				if (tracer.isFineEnabled()) {
					tracer.fine("Failed to obtain contact address for AS, can not compute AS contact.");
				}
			}
		} 
		else {
			if (tracer.isFineEnabled())	{
				tracer.fine("There is no via header, can not compute AS contact.");
			}
		}
	
		return forgedResponse;
	}

	/*
	 * (non-Javadoc)
	 * @see net.java.slee.resource.sip.DialogActivity#sendCancel()
	 */
	public ClientTransaction sendCancel() throws SipException {
		try {
			final ClientTransaction inviteCTX = this.getClientTransaction(lastCancelableTransactionId);
			final ClientTransaction cancelTransaction = this.provider.getNewDialogActivityClientTransaction(this,inviteCTX.createCancel());
			cancelTransaction.sendRequest();
			return cancelTransaction;
		} catch (NullPointerException npe) {
			if (tracer.isFineEnabled()) {
				tracer.fine(npe.getMessage(),npe);
			}
			throw new SipException("Possibly fialed to obtain client transaction or no INVITE transaction present");
		} catch (Exception e) {
			throw new SipException("Failed to send CANCEL due to:", e);
		}
	}
	
	// Dialog Methods
	
	/*
	 * (non-Javadoc)
	 * @see javax.sip.Dialog#createAck(long)
	 */
	public Request createAck(long arg0) throws InvalidArgumentException, SipException {
		return wrappedDialog.createAck(arg0);
	}

	/*
	 * (non-Javadoc)
	 * @see javax.sip.Dialog#createPrack(javax.sip.message.Response)
	 */
	public Request createPrack(Response arg0) throws DialogDoesNotExistException, SipException {
		return this.wrappedDialog.createPrack(arg0);
	}

	/*
	 * (non-Javadoc)
	 * @see javax.sip.Dialog#createReliableProvisionalResponse(int)
	 */
	public Response createReliableProvisionalResponse(int arg0) throws InvalidArgumentException, SipException {
		return wrappedDialog.createReliableProvisionalResponse(arg0);		
	}

	/*
	 * (non-Javadoc)
	 * @see javax.sip.Dialog#delete()
	 */
	public void delete() {
		final DialogState currentState = wrappedDialog.getState();
		boolean needToFireDTE = !isEnding() && !wrappedDialog.isServer() && (currentState == null || currentState == DialogState.TERMINATED); 
		wrappedDialog.delete();
		if (needToFireDTE) {
			ra.processDialogTerminated(this);
		}		
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.sip.Dialog#getCallId()
	 */
	public CallIdHeader getCallId() {
		return this.wrappedDialog.getCallId();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.sip.Dialog#getDialogId()
	 */
	public String getDialogId() {
		// FIXME if localtag is set it is not good
		return this.wrappedDialog.getDialogId();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.sip.Dialog#getFirstTransaction()
	 */
	@Deprecated
	public Transaction getFirstTransaction() {
		return this.wrappedDialog.getFirstTransaction();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.sip.Dialog#getLocalParty()
	 */
	public Address getLocalParty() {
		return this.wrappedDialog.getLocalParty();
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.sip.Dialog#getRemoteParty()
	 */
	public Address getRemoteParty() {
		return this.wrappedDialog.getRemoteParty();
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.sip.Dialog#getRemoteTarget()
	 */
	public Address getRemoteTarget() {
		return this.wrappedDialog.getRemoteTarget();
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.sip.Dialog#getLocalSeqNumber()
	 */
	public long getLocalSeqNumber() {
		return this.wrappedDialog.getLocalSeqNumber();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.sip.Dialog#getLocalSequenceNumber()
	 */
	@Deprecated
	public int getLocalSequenceNumber() {
		return this.wrappedDialog.getLocalSequenceNumber();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.sip.Dialog#getLocalTag()
	 */
	public String getLocalTag() {
		if (localTag == null) {
			if (wrappedDialog != null) {
				localTag = wrappedDialog.getLocalTag();
			}
		}
		return localTag;
	}

	/*
	 * (non-Javadoc)
	 * @see javax.sip.Dialog#getRemoteSeqNumber()
	 */
	public long getRemoteSeqNumber() {
		return this.wrappedDialog.getRemoteSeqNumber();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.sip.Dialog#getRemoteSequenceNumber()
	 */
	@Deprecated
	public int getRemoteSequenceNumber() {
		return this.wrappedDialog.getRemoteSequenceNumber();		
	}

	/*
	 * (non-Javadoc)
	 * @see javax.sip.Dialog#getRemoteTag()
	 */
	public String getRemoteTag() {
		return this.wrappedDialog.getRemoteTag();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.sip.Dialog#getRouteSet()
	 */
	@SuppressWarnings("unchecked")
	public Iterator<RouteHeader> getRouteSet() {
		return this.wrappedDialog.getRouteSet();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.sip.Dialog#getState()
	 */
	public DialogState getState() {
		return this.wrappedDialog.getState();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.sip.Dialog#incrementLocalSequenceNumber()
	 */
	public void incrementLocalSequenceNumber() {
		this.wrappedDialog.incrementLocalSequenceNumber();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.sip.Dialog#isSecure()
	 */
	public boolean isSecure() {
		return wrappedDialog.isSecure();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.sip.Dialog#isServer()
	 */
	public boolean isServer() {
		return wrappedDialog.isServer();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.sip.Dialog#sendAck(javax.sip.message.Request)
	 */
	public void sendAck(Request arg0) throws SipException {
		wrappedDialog.sendAck(arg0);
	}

	/*
	 * (non-Javadoc)
	 * @see javax.sip.Dialog#sendReliableProvisionalResponse(javax.sip.message.Response)
	 */
	public void sendReliableProvisionalResponse(Response arg0) throws SipException {
		wrappedDialog.sendReliableProvisionalResponse(arg0);		
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.sip.Dialog#terminateOnBye(boolean)
	 */
	public void terminateOnBye(boolean arg0) throws SipException {
		wrappedDialog.terminateOnBye(arg0);
	}
	
	// ...  

	@Override
	public String toString() {
		return new StringBuilder("DialogWrapper Id[").append(this.getDialogId())
			.append("] Handle[").append(this.getActivityHandle())
			.append("] State[").append(this.getState())
			.append("] OngoingCTX[").append(this.ongoingClientTransactions.size())
			.append("] OngoingSTX[").append(this.ongoingServerTransactions.size()).append("]")
			.toString();
	}

	// Own Methods

	/**
	 * 
	 * @param respEvent
	 * @return <ul>
	 *         <li><b>true</b> - if DialogWrapper finished processing and RA
	 *         doesnt have to take any action</li>
	 *         <li><b>false</b> - DialogWrapper did nothing, ra should triger
	 *         regular logic</li>
	 *         </ul>
	 */
	public boolean processIncomingResponse(ResponseEvent respEvent) {
		return false;
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public ClientTransaction getClientTransaction(String id) {
		return this.ongoingClientTransactions.get(id);
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public ServerTransaction getServerTransaction(String id) {
		return this.ongoingServerTransactions.get(id);
	}
	
	/**
	 * 
	 * @param branchID
	 * @return
	 */
	public boolean hasOngoingServerTransaction(String branchID) {
		return ongoingServerTransactions.containsKey(branchID);
	}

	/**
	 * 
	 * @param branchID
	 * @return
	 */
	public boolean hasOngoingClientTransaction(String branchID) {
		return ongoingClientTransactions.containsKey(branchID);
	}

	/**
	 * 
	 * @param stw
	 */
	public boolean addOngoingTransaction(ServerTransactionWrapper stw) {
		final boolean alreadyMapped = ongoingServerTransactions.put(stw.getBranchId(), stw) == null;
		// not needed till we have some sort of tx replication
		// updateReplicatedState();
		return alreadyMapped;
	}

	/**
	 * For now we will do like this to force replication in jsip ha, but it is probably better to use the ra replicated data
	 */
	protected void updateReplicatedState() {
		if (!ra.inLocalMode()) {
			wrappedDialog.setApplicationData(this);
		}
	}
	
	/**
	 * 
	 * @param ctw
	 */
	public boolean addOngoingTransaction(ClientTransactionWrapper ctw) {
		final boolean alreadyMapped = ongoingClientTransactions.put(ctw.getBranchId(), ctw) == null;
		// not needed till we have some sort of tx replication
		// updateReplicatedState();
		return alreadyMapped;
	}

	/**
	 * 
	 * @param ctw
	 */
	public void removeOngoingTransaction(ClientTransactionWrapper ctw) {
		if (ongoingClientTransactions != null) {
			if (ongoingClientTransactions.remove(ctw.getBranchId()) != null) {
				// not needed till we have some sort of tx replication
				// updateReplicatedState();
			}
		}
	}

	/**
	 * 
	 * @param stw
	 */
	public void removeOngoingTransaction(ServerTransactionWrapper stw) {
		if (ongoingServerTransactions != null) {
			if (ongoingServerTransactions.remove(stw.getBranchId()) != null) {
				// not needed till we have some sort of tx replication
				// updateReplicatedState();
			}
		}
	}
	
	static final String[] EMPTY_STRING_ARRAY = {};
	
	private void writeObject(ObjectOutputStream stream) throws IOException {
		
		// write everything not static or transient
		stream.defaultWriteObject();
		
		// TODO work this out once we have some sort of tx replication
		// now write the ongoing client and server txs
		//final String[] ctIds = ongoingClientTransactions.keySet().toArray(EMPTY_STRING_ARRAY);
		//stream.writeObject(ctIds);
		//final String[] stIds = ongoingServerTransactions.keySet().toArray(EMPTY_STRING_ARRAY);
		//stream.writeObject(stIds);
	}
	
	private void readObject(ObjectInputStream stream)  throws IOException, ClassNotFoundException {
				
		stream.defaultReadObject();

		// TODO work this out once we have some sort of tx replication
		//final String[] ctIds = (String[]) stream.readObject();
		//final String[] stIds = (String[]) stream.readObject();
		// re-populate the ongoing server and client tx maps fetching txs from stack
		// TODO
		ongoingClientTransactions = new ConcurrentHashMap<String, ClientTransaction>(1);
		ongoingServerTransactions = new ConcurrentHashMap<String, ServerTransaction>(1);		
		activityHandle.setActivity(this);
	}
}
