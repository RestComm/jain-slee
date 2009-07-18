package org.mobicents.slee.resource.sip11.wrappers;

import gov.nist.javax.sip.address.GenericURI;
import gov.nist.javax.sip.header.CSeq;
import gov.nist.javax.sip.message.SIPRequest;

import java.rmi.server.UID;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import javax.sip.ClientTransaction;
import javax.sip.Dialog;
import javax.sip.DialogDoesNotExistException;
import javax.sip.DialogState;
import javax.sip.DialogTerminatedEvent;
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
import javax.sip.header.CSeqHeader;
import javax.sip.header.CallIdHeader;
import javax.sip.header.ContactHeader;
import javax.sip.header.ContentTypeHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.Header;
import javax.sip.header.HeaderFactory;
import javax.sip.header.MaxForwardsHeader;
import javax.sip.header.Parameters;
import javax.sip.header.RecordRouteHeader;
import javax.sip.header.RouteHeader;
import javax.sip.header.ToHeader;
import javax.sip.header.ViaHeader;
import javax.sip.message.Message;
import javax.sip.message.Request;
import javax.sip.message.Response;
import javax.slee.AddressPlan;
import javax.slee.resource.FireableEventType;

import net.java.slee.resource.sip.DialogActivity;
import net.java.slee.resource.sip.DialogForkedEvent;

import org.apache.log4j.Logger;
import org.mobicents.slee.resource.sip11.SipActivityHandle;
import org.mobicents.slee.resource.sip11.SipResourceAdaptor;
import org.mobicents.slee.resource.sip11.SleeSipProviderImpl;

public class DialogWrapper implements DialogActivity, WrapperSuperInterface {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public final static Set<String> dialogCreatingMethods;
	static {
		Set<String> set = new HashSet<String>();
		set.add(Request.INVITE);
		set.add(Request.REFER);
		set.add(Request.SUBSCRIBE);
		dialogCreatingMethods = Collections.unmodifiableSet(set);
	}
	
	protected javax.sip.Dialog wrappedDialog = null;
	protected SipActivityHandle sipActivityHandle;
	protected String initiatingTransctionId = null;
	protected SipResourceAdaptor ra;
	
	protected ConcurrentHashMap<String, ClientTransaction> ongoingClientTransactions = new ConcurrentHashMap<String, ClientTransaction>();
	protected ConcurrentHashMap<String, ServerTransaction> ongoingServerTransactions = new ConcurrentHashMap<String, ServerTransaction>();

	protected String lastCancelableTransactionId = null;

	// Forging kit?
	protected SleeSipProviderImpl provider = null;

	protected static final Logger logger = Logger.getLogger(DialogWrapper.class);

	// ######################
	// # STRICTLY FORK AREA #
	// ######################

	private enum DialogForkState {
		AWAIT_FIRST_TAG, AWAIT_FINAL, END
	}

	// Comment: JSIP set ToTag and route list on first provisional response
	// with ToTag and 2xx, so we have to keep track of those for each activity,
	// in order to allow it to send responses to proper peer.
	// This is due to having one SIPDialog for all forks - this is standard for
	// no auto dialog support.
	/**
	 * Indicates sip activity handle for initial dialog activity created for
	 * this dialog - created as first. Child dialogs are represented by dialog
	 * activities with the same wrappedDialog object.
	 * wrappedDialog.getApplicationData() returns always initial dialog activity
	 * or one that won race to get 2xx response
	 */
	SipActivityHandle forkInitialActivityHandle = null;

	protected DialogForkState forkState = DialogForkState.AWAIT_FIRST_TAG;
	/**
	 * Used when: {@link #forkInitialActivityHandle} !=null or Dialog is null.
	 */
	protected ArrayList<RouteHeader> localRouteSet = new ArrayList<RouteHeader>();
	/**
	 * Used to detect how route set has been created - in case of responses we want to override
	 *  local route set, since in case of forks, route set on request will be propably bad :)
	 */
	protected boolean routeSetOnRequest = false;
	/**
	 * Represents local ToTag - see comment above
	 */
	protected String localToTag = null;
	protected SipURI reqeustURI = null;
	protected AtomicLong localSequenceNumber= new AtomicLong(0);
	// private Address localRemoteParty;
	/**
	 * Cotnains activity handles of fork children.
	 */
	protected ConcurrentHashMap<String, SipActivityHandle> toTag2DialogHandle = new ConcurrentHashMap<String, SipActivityHandle>();
	
	public DialogWrapper(SleeSipProviderImpl provider, SipResourceAdaptor ra) {
		this.provider = provider;
		this.ra = ra;
		// TODO: Come up with something way better, we cant hash on
		// dialogId, since it changes
		// We cant make ID inside change, since this would make Container to
		// leak attached SBBE - we would loose them, as in container view,
		// hash contract on handle would be broken
		// and on each change dialog would be considered as different activity
		sipActivityHandle = new SipActivityHandle(new UID().toString());

		//long id = (long) (System.currentTimeMillis() * ((double) Math.random()) * 100000);
		//long high32 = (id & 0xffffffff00000000L) >> 32;
		//long low32 = (id & 0xffffffffL);
		//autoGeneratedFromTag = high32 + "_" + low32;
		autoGeneratedFromTag = gov.nist.javax.sip.Utils.getInstance().generateTag();

	}

	public DialogWrapper(Dialog wrappedDialog, SipActivityHandle forkInitialActivityHandle, SleeSipProviderImpl provider, SipResourceAdaptor ra) {
		this(provider, ra);

		this.wrappedDialog = wrappedDialog;
		this.forkInitialActivityHandle = forkInitialActivityHandle;
		if (forkInitialActivityHandle == null) {
			if (wrappedDialog.getApplicationData() != null) {
				if (wrappedDialog.getApplicationData() instanceof DialogWrapper) {
					throw new IllegalArgumentException("Dialog to wrap has alredy a wrapper!!!");
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("Overwriting application data present - " + wrappedDialog.getApplicationData());
					}
				}

			}

			this.wrappedDialog.setApplicationData(this);
		} else {
			// do nothing
		}

	}

	public void cleanup() {

		if (wrappedDialog != null && this.forkInitialActivityHandle == null) {

			this.wrappedDialog.setApplicationData(null);
			if (!wrappedDialog.isServer()) {

				String key = this.wrappedDialog.getLocalTag() + "_" + this.wrappedDialog.getCallId().getCallId();
				this.ra.removeClientDialogMapping(key);

			}
				
		}

		this.wrappedDialog = null;
		this.sipActivityHandle = null;

		this.ra = null;
		// FIXE: Clean this ?
		this.ongoingClientTransactions = null;
		this.ongoingServerTransactions = null;
	}

	
	private void setLocalSequenceNumber(long lCseq) {
		
		if (lCseq <= this.localSequenceNumber.get())
			throw new RuntimeException("Sequence number should not decrease !");
		this.localSequenceNumber.set(lCseq);
	}
	public ClientTransaction getClientTransaction(String id) {
		return this.ongoingClientTransactions.get(id);
	}

	public String getInitiatingTransactionId() {
		return initiatingTransctionId;
	}

	public ServerTransaction getServerTransaction(String id) {
		return this.ongoingServerTransactions.get(id);
	}

	

	public Request createAck(long arg0) throws InvalidArgumentException, SipException {
		if(wrappedDialog!=null)
		{
			return wrappedDialog.createAck(arg0);
		}else
		{
			throw new SipException("Cant create ACK before initial request has not been sent!!!");
		}
	}

	public Request createPrack(Response arg0) throws DialogDoesNotExistException, SipException {
		if(wrappedDialog!=null)
		{
		return this.wrappedDialog.createPrack(arg0);
		}else
		{
			throw new SipException("Cant create PRACK before initial request has not been sent!!!");
		}
	}

	public Response createReliableProvisionalResponse(int arg0) throws InvalidArgumentException, SipException {
		if(wrappedDialog!=null)
		{
			return this.wrappedDialog.createReliableProvisionalResponse(arg0);
		}else
		{
			throw new SipException("Cant create response before initial request has not been sent!!!");
		}
	}

	
	

	public void delete() {
		// This ensures that dialog will be removed
		if (wrappedDialog == null) {
			ra.processDialogTerminated(new DialogTerminatedEvent(this.provider, this));

		} else if (this.forkInitialActivityHandle == null) {

			// We are master, if we die, everything else does
			// FIXME: is above statement correct?
			try {
				this.terminateFork(this.getActivityHandle());
			} finally {

				if (wrappedDialog.getState() == null) {

					ra.processDialogTerminated(new DialogTerminatedEvent(this.provider, wrappedDialog));
				}
			}
			wrappedDialog.delete();
			
		} else {

			ra.processDialogTerminated(new DialogTerminatedEvent(this.provider, this));
		}


	}

	public Object getApplicationData() {
		throw new SecurityException();
	}

	public CallIdHeader getCallId() {
		if(this.wrappedDialog==null)
		{
			return callIdToReUse;
		}
		return this.wrappedDialog.getCallId();
	}

	public String getDialogId() {
		if(this.wrappedDialog==null)
		{
			return null;
		}
		return this.wrappedDialog.getDialogId();
	}

	public Transaction getFirstTransaction() {
		if(wrappedDialog!=null)
		{
			return this.wrappedDialog.getFirstTransaction();
		}else
		{
			return null;
		}
	}

	// FIXME: for now its ok, only in server mode it would fial, but we dont have fork behaviour for server, nor we can have null wrappedDialog in server dialog :}
	public Address getLocalParty() {
		if(wrappedDialog!=null && !isInForkedActions())
		{
			
			return this.wrappedDialog.getLocalParty();
		}else if(wrappedDialog!=null && isInForkedActions())
		{
			return this.fromAddress;
		}else
		{
			return null;
		}
	}
	public Address getRemoteParty() {
		if(wrappedDialog!=null && !isInForkedActions())
		{
			
			return this.wrappedDialog.getRemoteParty();
		}else if(wrappedDialog!=null && isInForkedActions())
		{
			return this.toAddress;
		}else
		{
			return null;
		}
	}
	public Address getRemoteTarget() {
		if(wrappedDialog!=null && !isInForkedActions())
		{
			
			return this.wrappedDialog.getRemoteTarget();
		}else if(wrappedDialog!=null && isInForkedActions())
		{
			return this.toAddress!=null?this.toAddress:null;
		}else
		{
			return null;
		}
	}
	public long getLocalSeqNumber() {
		if(isInForkedActions() || wrappedDialog==null)
		{
			return localSequenceNumber.get();
		
		}else
		{
			return this.wrappedDialog.getLocalSeqNumber();
		}
	}

	public int getLocalSequenceNumber() {
		if(isInForkedActions() || wrappedDialog==null)
		{
			return (int) localSequenceNumber.get();
		
		}else
		{
			return this.wrappedDialog.getLocalSequenceNumber();
		}
	}

	public String getLocalTag() {
		if(wrappedDialog!=null )
		{
			return this.wrappedDialog.getLocalTag();
		}else
		{
			return autoGeneratedFromTag;
		}
	}


	public long getRemoteSeqNumber() {
		if(wrappedDialog!=null)
			return this.wrappedDialog.getRemoteSeqNumber();
		else
			return -1;
	}

	public int getRemoteSequenceNumber() {
		if(wrappedDialog!=null)
			return this.wrappedDialog.getRemoteSequenceNumber();
		else
			return -1;
			
	}

	public String getRemoteTag() {
		if(wrappedDialog!=null && !isInForkedActions())
		{
			return this.wrappedDialog.getRemoteTag();
		}else if(wrappedDialog!=null && isInForkedActions())
		{
			return this.localToTag;
		}else
		{
			return null;
		}
	}

	

	public Iterator getRouteSet() {
		if(wrappedDialog!=null)
		{
			return this.wrappedDialog.getRouteSet();
		}else
		{
			return new ArrayList(this.localRouteSet).iterator();
		}
	}

	public DialogState getState() {
		if(this.wrappedDialog==null)
		{
			return null;
		}
		return this.wrappedDialog.getState();
	}

	public void incrementLocalSequenceNumber() {
		if(isInForkedActions() || wrappedDialog==null)
		{
			 localSequenceNumber.incrementAndGet();
		
		}else
		{	
			this.wrappedDialog.incrementLocalSequenceNumber();
		}
	}

	public boolean isSecure() {
		if(wrappedDialog==null)
			return false;
		return wrappedDialog.isSecure();
	}

	public boolean isServer() {
		if(wrappedDialog==null)
			return false;
		return wrappedDialog.isServer();
	}

	public void sendAck(Request arg0) throws SipException {
		if(wrappedDialog==null)
		{
			throw new SipException("Wrong state, cant send ack before dialog createing request!!!");
		}
		wrappedDialog.sendAck(arg0);
	}

	public void sendReliableProvisionalResponse(Response arg0) throws SipException {
		if(wrappedDialog==null)
		{
			throw new SipException("Wrong state, cant send provisional response before dialog createing request!!!");
		}
			wrappedDialog.sendReliableProvisionalResponse(arg0);
		
	}

	

	public void setApplicationData(Object arg0) {
		throw new SecurityException();
	}

	public void terminateOnBye(boolean arg0) throws SipException {
		wrappedDialog.terminateOnBye(arg0);
	}

	public SipActivityHandle getActivityHandle() {
		return this.sipActivityHandle;
	}

	public Object getWrappedObject() {
		return this.wrappedDialog;
	}

	public boolean hasOngoingServerTransaction(String branchID) {
		return this.ongoingServerTransactions.containsKey(branchID);
	}

	public boolean hasOngoingClientTransaction(String branchID) {
		return this.ongoingClientTransactions.containsKey(branchID);
	}

	public void addOngoingTransaction(ServerTransactionWrapper stw) {
		this.ongoingServerTransactions.putIfAbsent(stw.getBranchId(), stw);
		if(isInForkedActions())
		{
			//Yup, could be reinveite? we have to update local DW cseq :)
			setLocalSequenceNumber( ((CSeq)stw.getRequest().getHeader(CSeqHeader.NAME)).getSeqNumber());
		}
	}

	public void addOngoingTransaction(ClientTransactionWrapper ctw) {
		this.ongoingClientTransactions.putIfAbsent(ctw.getBranchId(), ctw);
	}

	public void removeOngoingTransaction(ClientTransactionWrapper ctw) {
		// synchronized (this.wrappedDialog) {
		// if (this.getState() != DialogState.TERMINATED)
		this.ongoingClientTransactions.remove(ctw.getBranchId());

		// }

	}

	public void removeOngoingTransaction(ServerTransactionWrapper stw) {
		// synchronized (this.ongoingServerTransactions) {
		// if (this.getState() != DialogState.TERMINATED)
		this.ongoingServerTransactions.remove(stw.getBranchId());
		// }
	}

	public void removeOngoingTransaction(SuperTransactionWrapper stw) {

	}

	public void clearOngoingTransaction() {
		this.ongoingClientTransactions.clear();
		this.ongoingServerTransactions.clear();
	}

	public void clearAssociations() {

	}

	
	public String getLastCancelableTransactionId() {
		return lastCancelableTransactionId;
	}

	// =========================== XXX: Helper methods =====================
	private void forgeMessage(Message originalMessage, Message forgedMessage, Set<String> headerstoOmmit)
			throws SipException {
		// We leave to and from with tags from this dialog, but copy all other
		// parameters
		// Route headers are from this dialog
		ListIterator lit = originalMessage.getHeaderNames();
		while (lit.hasNext()) {

			String headerName = (String) lit.next();

			// FIXME: Could be wrong - but, if there is value in forgedRequest,
			// we leave original value
			// FIXME: What about address URI parameters?
			// FIXME: What about MaxForwards?
			if (headerName.equals(ToHeader.NAME) || headerName.equals(FromHeader.NAME)) {
				Parameters origHeader, forgedHeader;
				origHeader = (Parameters) originalMessage.getHeader(headerName);
				forgedHeader = (Parameters) forgedMessage.getHeader(headerName);
				Iterator it = origHeader.getParameterNames();
				Set<String> toOmmit = new HashSet<String>();
				toOmmit.add("tag");
				copyParameters(headerName, origHeader, forgedHeader, toOmmit);
			} else if (headerstoOmmit.contains(headerName)) {
				continue;
			} else {
				// FIXME: For now we simply copy everything, overwrte existing
				// headers

				if (forgedMessage.getHeaders(headerName).hasNext())
					forgedMessage.removeHeader(headerName);

				ListIterator headersIterator = originalMessage.getHeaders(headerName);

				while (headersIterator.hasNext()) {
					Header origHeader, forgedHeader;

					// origHeader = (Header)
					// originalMessage.getHeader(headerName);
					origHeader = (Header) headersIterator.next();

					forgedHeader = null;

					try {
						forgedHeader = (Header) this.provider.getHeaderFactory().createHeader(headerName,
								origHeader.toString().substring(origHeader.toString().indexOf(":") + 1));

						forgedMessage.addLast((javax.sip.header.Header) forgedHeader);
					} catch (ParseException e) {
						logger.error("Failed to generate header on [" + headerName + "]. To copy value [" + origHeader
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
				logger.error("Failed to set content on forged message. To copy value [" + new String(copy) + "] Type ["
						+ forgedMessage.getHeader(ContentTypeHeader.NAME) + "]\n", e);
			}
		}

	}

	private void copyParameters(String name, Parameters origHeader, Parameters forgedHeader, Set<String> toOmmit) {

		// FIXME: This will fail for parameters such as lr ??
		Iterator it = origHeader.getParameterNames();

		while (it.hasNext()) {
			String p_name = (String) it.next();
			if (toOmmit.contains(p_name) || forgedHeader.getParameter(p_name) != null) {
				if (logger.isDebugEnabled()) {
					logger.debug("Ommiting parameter on [" + name + "]. To copy value ["
							+ origHeader.getParameter(p_name) + "]\nValue in forged ["
							+ forgedHeader.getParameter(p_name) + "]");
				}
			} else {
				try {
					forgedHeader.setParameter(p_name, origHeader.getParameter(p_name));
				} catch (ParseException e) {
					logger.error("Failed to pass parameter on [" + name + "]. To copy value ["
							+ origHeader.getParameter(p_name) + "]\nValue in forged ["
							+ forgedHeader.getParameter(p_name) + "]", e);
				}
			}
		}

	}

	public String toString() {
		return "Dialog Id[" + this.getDialogId() + "] State[" + this.getState() + "] OngoingCTX["
				+ this.ongoingClientTransactions.size() + "] OngoingSTX[" + this.ongoingServerTransactions.size() + "]";
	}

	
	// ###########################################
	// # Strictly DialogActivity defined methods #
	// ###########################################

	public ClientTransaction sendCancel() throws SipException {

		verifyDialogExistency();
		try {
			ClientTransaction inviteCTX = this.getClientTransaction(lastCancelableTransactionId);
			Request cancelRequest = inviteCTX.createCancel();
			ClientTransaction cancelTransaction = this.provider.getNewClientTransaction(cancelRequest, false);
			cancelTransaction.sendRequest();
			return cancelTransaction;
		} catch (NullPointerException npe) {
			if (logger.isDebugEnabled()) {
				logger.debug(npe);
			}
			throw new SipException("Possibly fialed to obtain client transaction or no INVITE transaction present");
		} catch (Exception e) {

			throw new SipException("Failed to send CANCEL due to:", e);
		}
	}

	public void associateServerTransaction(ClientTransaction ct, ServerTransaction st) {
		// ct MUST be in ongoing transaction, its local, st - comes from another
		// dialog
		verifyDialogExistency();
		if (!this.hasOngoingClientTransaction(ct.getBranchId())) {
			throw new IllegalArgumentException("Client transaction is not in ongoing transaction list!!!");
		}

		if (st != null) {
			if (!((DialogWrapper) st.getDialog()).hasOngoingServerTransaction(st.getBranchId())) {
				throw new IllegalArgumentException("Server transaction is not in ongoing transaction list!!!");
			}
			// XXX: ctx can be associated to only one stx, however stx can have
			// multiple ctx

			((ClientTransactionWrapper) ct).associateServerTransaction(st.getBranchId(), ((DialogWrapper) st.getDialog()).getActivityHandle());
		} else {
			((ClientTransactionWrapper) ct).associateServerTransaction(null, null);
		}

	}

	public Request createRequest(String arg0) throws SipException {
		final HeaderFactory headerFactory = provider.getHeaderFactory();
		
		if (this.wrappedDialog == null) {

			// the real dialog doesn't exist yet so we act like we will build such a dialog when sending this request
			
			try {
					// From Header:
					FromHeader fromHeader = headerFactory.createFromHeader(
					fromAddress, autoGeneratedFromTag);

					// To header and request URI:
					ToHeader toHeader = headerFactory.createToHeader(toAddress,	null);
					javax.sip.address.URI requestURI = toAddress.getURI();

					// Create the Via header and add to an array list
					ListeningPoint listeningPoint = provider.getListeningPoints()[0];
					ViaHeader viaHeader = headerFactory.createViaHeader(
					listeningPoint.getIPAddress(),
					listeningPoint.getPort(),
					listeningPoint.getTransport(), null);
					ArrayList<ViaHeader> viaHeadersList = new ArrayList<ViaHeader>();
					viaHeadersList.add(viaHeader);

					MaxForwardsHeader maxForwardsHeader = headerFactory.createMaxForwardsHeader(70);

					// Cseq header:
					
					CSeqHeader cSeqHeader = headerFactory.createCSeqHeader(localSequenceNumber.get()+1, arg0);
					//cSeqHeader.setMethod(arg0);

					// create the request
					Request request= provider.getMessageFactory().createRequest((javax.sip.address.URI) requestURI.clone(),arg0, callIdToReUse, cSeqHeader, fromHeader, toHeader,	viaHeadersList, maxForwardsHeader);
					
					//baranowb: this propably wont be triggered ever.
					
					fillRequestHeaders(request);
					return request;
				}catch (Exception e) {
						throw new SipException(e.getMessage(),e);
				}

			}
			else if(isInForkedActions() && !wrappedDialog.isServer()) {
				try{
					Request req=this.wrappedDialog.createRequest(arg0);
				
					req.removeHeader(RouteHeader.NAME);
					ListeningPoint listeningPoint = provider.getListeningPoints()[0];
					ViaHeader viaHeader = headerFactory.createViaHeader(
						listeningPoint.getIPAddress(),
						listeningPoint.getPort(),
						listeningPoint.getTransport(), null);
					req.addFirst(viaHeader);
					
					CSeqHeader cSeqHeader = headerFactory.createCSeqHeader(localSequenceNumber.get()+1, null);
					cSeqHeader.setMethod(arg0);
					fillRequestHeaders(req);
					//this.localSequenceNumber++;
					//CSeqHeader cSeqHeader = headerFactory.createCSeqHeader(localSequenceNumber, arg0);
					//req.addHeader(cSeqHeader);
					return req;
				}catch (Exception e) {
					throw new SipException(e.getMessage(),e);
				}
			}else
			{
				try{
					//XXX: here we dont have to set anything, we are not forking so its ok.
					Request req=this.wrappedDialog.createRequest(arg0);
					ListeningPoint listeningPoint = provider.getListeningPoints()[0];
					ViaHeader viaHeader = headerFactory.createViaHeader(
					listeningPoint.getIPAddress(),
					listeningPoint.getPort(),
					listeningPoint.getTransport(), null);
					req.addFirst(viaHeader);
					return req;
				}catch (Exception e) {
					throw new SipException(e.getMessage(),e);
				}
			}
	}
	
	
	public Request createRequest(Request origRequest) throws SipException {
		
		generateRouteList(origRequest);
		Set<String> headersToOmmit = new HashSet<String>();
		Request forgedRequest = null;
		if (this.wrappedDialog != null) {
			if(!origRequest.getMethod().equals(Request.ACK))
			{
				forgedRequest = this.wrappedDialog.createRequest(origRequest.getMethod());
			}else
			{
				try {
					forgedRequest = this.wrappedDialog.createAck(this.wrappedDialog.getLocalSeqNumber());
				} catch (InvalidArgumentException e) {
					throw new SipException("",e);
				}
			}
			//Here we have to check if this is in foking condition, in this case, we have to change:
			// - route set
			// - callid
			// - from,to

			
		} else {
			try {
				MaxForwardsHeader mf = this.provider.getHeaderFactory().createMaxForwardsHeader(70);

				forgedRequest = this.provider.getMessageFactory().createRequest(null);
				forgedRequest.setRequestURI(origRequest.getRequestURI());
				forgedRequest.addHeader(mf);
				forgedRequest.addHeader(callIdToReUse);
				CSeqHeader cseqHeader=(CSeqHeader) origRequest.getHeader(CSeqHeader.NAME);
				cseqHeader=(CSeqHeader) cseqHeader.clone();
				forgedRequest.addHeader(cseqHeader);
				
				if(forgedRequest.getMethod()==null)
					((SIPRequest) forgedRequest).setMethod(cseqHeader.getMethod());
				
				
			
			} catch (Exception e) {

				throw new SipException("", e);
			}

		}

		fillRequestHeaders(forgedRequest);
		headersToOmmit.add(RouteHeader.NAME);
		headersToOmmit.add(RecordRouteHeader.NAME);
		// headersToOmmit.add(ViaHeader.NAME);
		headersToOmmit.add(CallIdHeader.NAME);
		headersToOmmit.add(CSeqHeader.NAME);

		forgeMessage(origRequest, forgedRequest, headersToOmmit);
		//FIXME: ???
		forgedRequest.addFirst(provider.getLocalVia(this.provider.getListeningPoints()[0].getTransport(), null));
		
		return forgedRequest;
	}

	public ClientTransaction sendRequest(Request request) throws SipException, TransactionUnavailableException {

		if (wrappedDialog == null && !dialogCreatingMethods.contains(request.getMethod())) {
			throw new IllegalStateException("Dialog activity present, but no dialog creating reqeust has been sent yet! This method: " + request.getMethod()
					+ " is not dialog creating one");
		}

		
		ClientTransactionWrapper CTW = (ClientTransactionWrapper) this.provider.getNewClientTransaction(request, false);
		
		boolean created=false;
		if (request.getMethod().compareTo(Request.INVITE)==0)
			lastCancelableTransactionId = CTW.getBranchId();
		if (wrappedDialog == null) {
			this.wrappedDialog = this.provider.getNewDialog(CTW,null, false);
			this.wrappedDialog.setApplicationData(this);
			created=true;

		}

		this.addOngoingTransaction(CTW);
		String method=request.getMethod();
		
		if(isInForkedActions() )
		{
			//cause dialog spoils - changes cseq, we dont want that
			CTW.sendRequest();
			if( method.compareTo(Request.ACK)!=0 && method.compareTo(Request.CANCEL)!=0)
				localSequenceNumber.incrementAndGet();
		}else
		{
			//JSIP does not allow to send via dialog on null state, yet :)
			if(!created)
			{
				this.wrappedDialog.sendRequest((ClientTransaction) CTW.getWrappedTransaction());
			}else
			{
				CTW.sendRequest();
			}
		}

		return CTW;
	}

	public void sendRequest(ClientTransaction ctw) throws TransactionDoesNotExistException, SipException {
		if(wrappedDialog==null )
		{
			Request request=ctw.getRequest();

			if (wrappedDialog == null && !dialogCreatingMethods.contains(request.getMethod())) {
				throw new IllegalStateException("Dialog activity present, but no dialog creating reqeust has been sent yet! This method: " + request.getMethod()
						+ " is not dialog creating one");
			}

			
			ClientTransactionWrapper CTW = (ClientTransactionWrapper) ctw;
			boolean created=false;
			if (request.getMethod().equals(Request.INVITE))
				lastCancelableTransactionId = CTW.getBranchId();
			if (wrappedDialog == null) {
				this.wrappedDialog = this.provider.getNewDialog(CTW,null, false);
				this.wrappedDialog.setApplicationData(this);

			}

			this.addOngoingTransaction(CTW);
			//JSIP does not allow to send via dialog on null state, yet :)
			if(!created)
			{
				this.wrappedDialog.sendRequest((ClientTransaction) CTW.getWrappedTransaction());
			}else
			{
				CTW.sendRequest();
			}

		}else if(isInForkedActions())
		{
			Request r=ctw.getRequest();
			//CSeqHeader cseq=(CSeqHeader) ctw.getRequest().getHeader(CSeqHeader.NAME);
			//if(cseq==null)
			//	throw new SipException("Cseq cant be null at this point!");
			if(isInForkedActions() && r.getMethod().compareTo(Request.ACK)!=0 && r.getMethod().compareTo(Request.CANCEL)!=0 )
				localSequenceNumber.incrementAndGet();
				//try {
				//	cseq.setSeqNumber(++localSequenceNumber);
				//} catch (InvalidArgumentException e) {
					// TODO Auto-generated catch block
				//	e.printStackTrace();
				//}
				
			ctw.sendRequest();
		}else 
		{	
			
			wrappedDialog.sendRequest((ClientTransaction) ((ClientTransactionWrapper) ctw).getWrappedTransaction());
		}
	}
	
	
	public Response createResponse(ServerTransaction origServerTransaction, Response receivedResponse) throws SipException {

		
		if (!this.hasOngoingServerTransaction(origServerTransaction.getBranchId()))
			throw new IllegalArgumentException("Passed server transaction is not in ongoing STX list for this dialog!!!!");

		Response forgedResponse;
		try {
			// FIXME check this works as expected
			forgedResponse = this.provider.getMessageFactory().createResponse(receivedResponse.getStatusCode(), origServerTransaction.getRequest());
		} catch (ParseException e) {

			e.printStackTrace();
			throw new SipException("Failed to forge message", e);
		}
		Set<String> headersToOmmit = new HashSet<String>();
		headersToOmmit.add(RouteHeader.NAME);
		headersToOmmit.add(RecordRouteHeader.NAME);
		headersToOmmit.add(ViaHeader.NAME);
		headersToOmmit.add(CallIdHeader.NAME);
		headersToOmmit.add(CSeqHeader.NAME);
		headersToOmmit.add(ContactHeader.NAME);
		forgeMessage(receivedResponse, forgedResponse, headersToOmmit);
		
		//if(this.wrappedDialog==null)
		//{
		//	forgedResponse.addHeader(this.provider.getHeaderFactory().createContactHeader(this.getLocalParty()));
		//}else
		{
			//This is ok, we receveid this dialog on one LP, we need to put Contact there.
			Request origRequest = origServerTransaction.getRequest();
			ViaHeader topVia = (ViaHeader) origRequest.getHeader(ViaHeader.NAME);
			if(topVia!=null)
			{
				Address address = getLocalAddressForTransport(topVia.getTransport());
				if(address !=null)
				{
					ContactHeader contactHeader = this.provider.getHeaderFactory().createContactHeader(address);
					forgedResponse.addLast(contactHeader);
				}else
				{
					if(logger.isDebugEnabled())
					{
						logger.debug("Failed to obtain contact address for AS, can not compute AS contact.");
					}
				}
				
			}else
			{
				if(logger.isDebugEnabled())
				{
					logger.debug("There is no via header, can not compute AS contact.");
				}
			}
		}
		
		
		//If we are client Dialog, we can receive response with ToTag, when local is not present, we shoudl copy
		ToHeader localToHeader=(ToHeader) forgedResponse.getHeader(ToHeader.NAME);
		//System.out.println("{"+(localToHeader.getTag()==null)+"}{"+(!this.isServer())+"}");
		if(localToHeader.getTag()==null && this.isServer())
		{
			ToHeader otherToHeader=(ToHeader) receivedResponse.getHeader(ToHeader.NAME);
			try {

				localToHeader.setTag(otherToHeader.getTag());
			} catch (ParseException e) {
				e.printStackTrace();
				throw new SipException("Failed to set ToTag", e);
			}
		}
	
		return forgedResponse;
	}

	public ServerTransaction getAssociatedServerTransaction(ClientTransaction ct) {

		if (!this.hasOngoingClientTransaction(ct.getBranchId())) {
			throw new IllegalArgumentException("Passed client transaction is not running for this dialog");
		}

		ClientTransactionWrapper ctw = (ClientTransactionWrapper) ct;
		if (ctw.getAssociatedTransactionBranchId() == null || ra.getActivity(ctw.getAssociationHandle()) == null) {
			return null;
		} else {
			DialogWrapper da = (DialogWrapper) ra.getActivity(ctw.getAssociationHandle());
			return da.getServerTransaction(ctw.getAssociatedTransactionBranchId());
		}
	}

	
	
	// ##############################################################
	// # Strictly dialog forge - used when no dialog is present yet #
	// ##############################################################
	protected Address fromAddress, toAddress;
	protected CallIdHeader callIdToReUse = null;
	protected String autoGeneratedFromTag = null;

	public void setFromAddress(Address fromAddress) {
		this.fromAddress = fromAddress;
	}

	public void setToAddress(Address toAddress) {
		this.toAddress = toAddress;
	}

	public void setCallIdToReUse(CallIdHeader callIdToReUse) {
		this.callIdToReUse = callIdToReUse;
	}

	private void verifyDialogExistency() {
		if (wrappedDialog == null) {
			throw new IllegalStateException("Dialog activity present, but no dialog creating reqeust has been sent yet!");
		}
	}

	/**
	 * This should be used on reqeusts when wrapped dialog is null - this DW was
	 * created by getNewDialog(from,to) or
	 * getNewDialog(incomingDialog,reuseCallId).
	 * 
	 * @param request
	 * @throws SipException
	 */
	private void fillRequestHeaders(Request request) throws SipException {
		String method = request.getMethod();

	
			try {

				if ((isInForkedActions()) && wrappedDialog!=null && !wrappedDialog.isServer()) {
					// We have wrappedDialog, but we cant relly on its info,
					// only CallId, from address, to addres, fromheader are
					// static
					request.removeHeader(CallIdHeader.NAME);
					request.addLast(wrappedDialog.getCallId());

					ToHeader toHeader = (ToHeader) request.getHeader(ToHeader.NAME);
					FromHeader fromHeader = (FromHeader) request.getHeader(FromHeader.NAME);

					if (toHeader == null || !toHeader.getAddress().equals(wrappedDialog.getRemoteParty()))
						toHeader = this.provider.getHeaderFactory().createToHeader(toAddress, null);

					if (localToTag != null && ! (localToTag.compareTo(toHeader.getTag())==0)) {
						toHeader.setTag(localToTag);
					}
					request.removeHeader(toHeader.NAME);
					request.addHeader(toHeader);

					if (fromHeader == null || !fromHeader.getAddress().equals(wrappedDialog.getLocalParty()))
						fromHeader = this.provider.getHeaderFactory().createFromHeader(wrappedDialog.getLocalParty(), wrappedDialog.getLocalTag());

					request.removeHeader(fromHeader.NAME);
					request.addHeader(fromHeader);
	
					if (localRouteSet.size() > 0 ) {
						if((request.getHeaders(RouteHeader.NAME)!=null && !request.getHeaders(RouteHeader.NAME).hasNext()))
							request.removeHeader(RouteHeader.NAME);

						for (int i = 0; i < localRouteSet.size(); i++) {
							request.addLast(localRouteSet.get(i));
							//request.addHeader(forkRouteSet.get(i));
						}
					}

					SipURI cloned=(SipURI) this.reqeustURI.clone();
					request.setRequestURI(cloned);
					CSeqHeader cseq=(CSeqHeader) request.getHeader(CSeq.NAME);
					if(cseq==null)
					{
						throw new SipException("CSeq cant be null at this point!!");
					}
					
					//we leave this as it was
					if (!method.equals(Request.CANCEL) && !method.equals(Request.ACK))
						cseq.setSeqNumber(this.localSequenceNumber.get()+1);
					
					cloned.setMethodParam(cseq.getMethod());
					
				} else if (!isInForkedActions() && wrappedDialog!=null && wrappedDialog.isServer()) {

					
				} else if(wrappedDialog==null){
					//FIXME: this can be wrong
					request.removeHeader(CallIdHeader.NAME);
					request.addLast(callIdToReUse);

					ToHeader toHeader = (ToHeader) request.getHeader(ToHeader.NAME);
					FromHeader fromHeader = (FromHeader) request.getHeader(FromHeader.NAME);

					if (toHeader == null) {
						toHeader = this.provider.getHeaderFactory().createToHeader(toAddress, null);

					} else {
						toHeader.setAddress(toAddress);
						if (wrappedDialog != null) {
							toHeader.setTag(wrappedDialog.getRemoteTag());
						}
					}
					request.removeHeader(toHeader.NAME);
					request.addLast(toHeader);
					if (fromHeader == null || !fromHeader.getAddress().equals(fromAddress)) {
						fromHeader = this.provider.getHeaderFactory().createFromHeader(fromAddress, autoGeneratedFromTag);

					} else {
						fromHeader.setAddress(fromAddress);
						fromHeader.setTag(autoGeneratedFromTag);

					}
					request.removeHeader(fromHeader.NAME);
					request.addLast(fromHeader);

					// route header set process
					if (localRouteSet.size() > 0 ) {
						if((request.getHeaders(RouteHeader.NAME)!=null && !request.getHeaders(RouteHeader.NAME).hasNext()))
							request.removeHeader(RouteHeader.NAME);
						
	
						for (int i = 0; i < localRouteSet.size(); i++) {
							request.addLast(localRouteSet.get(i));
							//request.addHeader(forkRouteSet.get(i));
						}
					}
					

				}else
				{
					if(logger.isDebugEnabled())
					{
						//THis is triggered anyway....
						logger.debug("No need to fill request headers, route set and other state should be maintaned by stack: "+this);
					}
				}

			} catch (Exception e) {

				throw new SipException("Couldnt add specs headers due to:", e);
			}
	}

	/**
	 * Does response processing in dialog
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
		// public synchronized boolean processIncomingResponse(ResponseEvent
		// respEvent) {
		// FIXME: not very good idea to cross message sending logic between RA
		// and DW
		// possibly it would be better to move indialog sending logic entirely
		// to DW ?
		// But we need it only in cases DW ends,has to send message to child or
		// indicate forking
		Response response = respEvent.getResponse();
		
		boolean firedByDialogWrapper = false;
		int statusCode = response.getStatusCode();
		String toTag = ((ToHeader) response.getHeader(ToHeader.NAME)).getTag();
		DialogForkState oldForkState = this.forkState;
		SipActivityHandle masterActivityHandle = this.getActivityHandle();

		if (wrappedDialog.isServer()) {
			// FIXME: do nothing
			// action.setAction(DialogClientForkAction.DO_NOTHING);
			return false;
		}

		switch (this.forkState) {
		case AWAIT_FIRST_TAG:

			if (100 <= statusCode && statusCode < 200) {
				if (toTag != null) {

					this.localToTag = toTag;
					this.forkState = DialogForkState.AWAIT_FINAL;
					this.toTag2DialogHandle.put(this.localToTag, this.sipActivityHandle);
					this.fetchData(response);
					this.ra.addClientDialogMaping(this.wrappedDialog.getLocalTag() + "_" + this.wrappedDialog.getCallId().getCallId(), getActivityHandle());
				}
				if (logger.isDebugEnabled()) {
					logger.debug("Received 1xx message: " + statusCode + ". ToTag:" + toTag + ". Fork state old:" + oldForkState + " - new" + this.forkState + ". On dialog: "
							+ this.toString());
				}
				// FIXME: fire on this, original message
				firedByDialogWrapper = false;

			} else if (statusCode < 300) {
				this.forkState = DialogForkState.END;
				// This one is the master, here we can have only one dialog,
				// this one, so we dont care about simblings, we just dont have
				// those
				// baranowb: what should we do if no toTag?
				if(toTag!=null)
				{
					//THis is for cancel OK, which might not have tag.
					this.localToTag = toTag;
					this.toTag2DialogHandle.put(toTag, this.sipActivityHandle);
				}
				this.fetchData(response);

				if (logger.isDebugEnabled()) {
					logger.debug("Received 2xx message: " + statusCode + ". ToTag:" + toTag + ". Fork state old:" + oldForkState + " - new" + this.forkState + ". On dialog: "
							+ this.toString());
				}
				// FIXME: fire on this, original message
				firedByDialogWrapper = false;

				// just in case
				terminateFork(masterActivityHandle);

			} else if (statusCode < 700) {

				this.forkState = DialogForkState.END;
				if (logger.isDebugEnabled()) {
					logger.debug("Received failure message: " + statusCode + ". ToTag:" + toTag + ". Fork state old:" + oldForkState + " - new" + this.forkState + ". On dialog: "
							+ this.toString());
				}
				// FIXME: fire on this, original message

				FireableEventType eventID = this.ra.eventIdCache.getEventId(this.ra.getRaContext().getEventLookupFacility(), response);
				ResponseEventWrapper REW = new ResponseEventWrapper(this.provider, (ClientTransaction) respEvent.getClientTransaction().getApplicationData(), this, response);
				this.ra.fireEvent(REW, this.getActivityHandle(), eventID, new javax.slee.Address(AddressPlan.SIP, ((FromHeader) response.getHeader(FromHeader.NAME)).getAddress()
						.toString()),false);
				firedByDialogWrapper = true;

				terminateFork(null);

			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("Received strange message: " + statusCode + ". ToTag:" + toTag + ". Fork state old:" + oldForkState + " - new" + this.forkState + ". On dialog: "
							+ this.toString());
				}
			}
			break;
		case AWAIT_FINAL:

			if (100 <= statusCode && statusCode < 200) {

				if (logger.isDebugEnabled()) {
					logger.debug("Received 1xx message: " + statusCode + ". ToTag:" + toTag + ". Fork state old:" + oldForkState + " - new" + this.forkState + ". On dialog: "
							+ this.toString());
				}
				if (toTag == null) {
					// FIXME: fire on this
					firedByDialogWrapper = false;
				} else if (toTag.compareTo(localToTag)==0) {

					this.fetchData(response);
					firedByDialogWrapper = false;
					// FIXME: fire on this
				} else if (this.toTag2DialogHandle.containsKey(toTag)) {

					// other dialog wins
					DialogWrapper child = (DialogWrapper) this.ra.getActivity(this.toTag2DialogHandle.get(toTag));
					if (child == null) {
						this.toTag2DialogHandle.remove(toTag);

						return true;
					}
					child.fetchData(response);
					// FIXME: fire on child, original message
					FireableEventType eventID = this.ra.eventIdCache.getEventId(this.ra.getRaContext().getEventLookupFacility(), response);
					logger.info("Received 1xx message: " + statusCode + ". ToTag:" + toTag + "EventId:" + eventID);
					ResponseEventWrapper REW = new ResponseEventWrapper(this.provider, (ClientTransaction) respEvent.getClientTransaction().getApplicationData(), this, response);
					this.ra.fireEvent(REW, child.getActivityHandle(), eventID, new javax.slee.Address(AddressPlan.SIP, ((FromHeader) response.getHeader(FromHeader.NAME))
							.getAddress().toString()),false);
					firedByDialogWrapper = true;
				} else {
					// We create fake dialog that - as a child of master
					DialogWrapper child = this.provider.getNewDialogActivity(wrappedDialog, this.getActivityHandle(), null);
					child.forkState = this.forkState;

					child.localToTag = toTag;
					child.fetchData(response);
					this.toTag2DialogHandle.put(toTag, child.getActivityHandle());

					// FIXME: fire on original, dialog forked
					FireableEventType eventID = this.ra.eventIdCache.getDialogForkEventId(this.ra.getRaContext().getEventLookupFacility());

					DialogForkedEvent REW = new DialogForkedEvent(this.provider, (ClientTransaction) respEvent.getClientTransaction().getApplicationData(), this, child, response);
					this.ra.fireEvent(REW, this.getActivityHandle(), eventID, new javax.slee.Address(AddressPlan.SIP, ((FromHeader) response.getHeader(FromHeader.NAME))
							.getAddress().toString()),false);
					firedByDialogWrapper = true;
				}

			} else if (statusCode < 300) {

				this.forkState = DialogForkState.END;
				if (logger.isDebugEnabled()) {
					logger.debug("Received 2xx message: " + statusCode + ". ToTag:" + toTag + ". Fork state old:" + oldForkState + " - new" + this.forkState + ". On dialog: "
							+ this.toString());
				}
				//toTag must not be null, but just in case.
				if (this.localToTag != null && toTag!=null && this.localToTag.compareTo(toTag)==0) {
					// we win
					this.fetchData(response);
					// FIXME: fire on original, original message
					firedByDialogWrapper = false;
				} else if (toTag!=null && this.toTag2DialogHandle.containsKey(toTag)) {

					// other dialog wins

					DialogWrapper child = (DialogWrapper) this.ra.getActivity(this.toTag2DialogHandle.get(toTag));
					child.fetchData(response);
					masterActivityHandle = child.getActivityHandle();
					child.makeMaster();
					if (child != this)
						this.forkInitialActivityHandle = child.getActivityHandle();
					else {
						logger.error("Local: " + localToTag + " : " + toTag + " MSG:\n" + response);
					}

					// FIXME: fire on child, original message
					FireableEventType eventID = this.ra.eventIdCache.getEventId(this.ra.getRaContext().getEventLookupFacility(), response);
					ResponseEventWrapper REW = new ResponseEventWrapper(this.provider, (ClientTransaction) respEvent.getClientTransaction().getApplicationData(), this, response);
					this.ra.fireEvent(REW, child.getActivityHandle(), eventID, new javax.slee.Address(AddressPlan.SIP, ((FromHeader) response.getHeader(FromHeader.NAME))
							.getAddress().toString()),false);
					firedByDialogWrapper = true;

				} else if(toTag !=null){
					// we have completly new master dialog
					// We create fake dialog as a child of master, but this one
					// becomes master
					DialogWrapper child = this.provider.getNewDialogActivity(wrappedDialog, this.getActivityHandle(), null);
					child.forkState = this.forkState;

					child.localToTag = toTag;
					this.toTag2DialogHandle.put(toTag, child.getActivityHandle());
					masterActivityHandle = child.getActivityHandle();
					child.makeMaster();
					this.forkInitialActivityHandle = child.getActivityHandle();
					child.fetchData(response);
					// FIXME: fire on child, original message
					FireableEventType eventID = this.ra.eventIdCache.getDialogForkEventId(this.ra.getRaContext().getEventLookupFacility());
					DialogForkedEvent REW = new DialogForkedEvent(this.provider, (ClientTransaction) respEvent.getClientTransaction().getApplicationData(), this, child, response);
					this.ra.fireEvent(REW, this.getActivityHandle(), eventID, new javax.slee.Address(AddressPlan.SIP, ((FromHeader) response.getHeader(FromHeader.NAME))
							.getAddress().toString()),false);
					firedByDialogWrapper = true;
				}else
				{
					logger.error("Received 2xx reponse without toTag, this is error.");
				}

				// just in case
				terminateFork(masterActivityHandle);
			} else if (statusCode < 700) {

				this.forkState = DialogForkState.END;
				if (logger.isDebugEnabled()) {
					logger.debug("Received failure message: " + statusCode + ". ToTag:" + toTag + ". Fork state old:" + oldForkState + " - new" + this.forkState + ". On dialog: "
							+ this.toString());
				}
				// FIXME: fire on this, original message
				FireableEventType eventID = this.ra.eventIdCache.getEventId(this.ra.getRaContext().getEventLookupFacility(), response);
				ResponseEventWrapper REW = new ResponseEventWrapper(this.provider, (ClientTransaction) respEvent.getClientTransaction().getApplicationData(), this, response);
				this.ra.fireEvent(REW, getActivityHandle(), eventID, new javax.slee.Address(AddressPlan.SIP, ((FromHeader) response.getHeader(FromHeader.NAME)).getAddress()
						.toString()),false);
				firedByDialogWrapper = true;
				terminateFork(null);

			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("Received strange message: " + statusCode + ". ToTag:" + toTag + ". Fork state old:" + oldForkState + " - new" + this.forkState + ". On dialog: "
							+ this.toString());
				}
			}

			break;
		case END:
			// Strictly for ending other dialogs - response may come late
			// FIXME: add terminate dialog

			if (toTag!=null && !toTag.equals(wrappedDialog.getRemoteTag())) {

				// if 1xx+, 3xx+ we ignore it  - as it indicates error response to another dialog, we dont care for.
				if (statusCode < 200 || statusCode > 300) {
					if (logger.isDebugEnabled()) {
						logger.debug("Received late message, action IGNORE: " + statusCode + ". ToTag:" + toTag + ". Fork state old:" + oldForkState + " - new" + this.forkState
								+ ". On dialog: " + this.toString());
					}

				} else {

					// we are in 2xx zone, we have to ack and send bye
					// FIXME: Add proper termiantion for SUBSCRIBE/REFER ???

					if (dialogCreatingMethods.contains(((CSeqHeader) response.getHeader(CSeqHeader.NAME)).getMethod()))
						doTerminateOnLate2xx(respEvent);
				}

				firedByDialogWrapper = true;

			}

			// else we leave it to be fired by normal means ?
			break;

		}

		return firedByDialogWrapper;
	}

	/**
	 * Generare 200 and sends BYE if method == 200, possibly this shoudl also terminate subscriptions?
	 * @param respEvent
	 */
	public void doTerminateOnLate2xx(ResponseEvent respEvent) {

		try {
			Response response = respEvent.getResponse();
			CSeqHeader cseq = (CSeqHeader) response.getHeader(CSeqHeader.NAME);
			List<Header> routeSet = getRouteList(response);
			SipURI requestURI = getReqeustUri(response);

			long cseqNumber = cseq.getSeqNumber();
			long statusCode = response.getStatusCode();

			// logger.info("DOING FORGE FOR: \n"+response);
			if (cseq.getMethod().equals(Request.INVITE) && (statusCode < 300 && statusCode >= 200)) {
				if (requestURI == null) {
					logger.error("Cannot ack on reqeust that has empty contact!!!!");
					return;
				}

				MaxForwardsHeader mf = this.provider.getHeaderFactory().createMaxForwardsHeader(70);

				Request forgedRequest = this.provider.getMessageFactory().createRequest(null);
				forgedRequest.setRequestURI(requestURI);
				forgedRequest.addHeader(mf);
				forgedRequest.addHeader(response.getHeader(CallIdHeader.NAME));
				forgedRequest.addHeader(this.provider.getHeaderFactory().createCSeqHeader(cseqNumber, Request.ACK));
				forgedRequest.addHeader(response.getHeader(FromHeader.NAME));
				forgedRequest.addHeader(response.getHeader(ToHeader.NAME));
				for (Header h : routeSet) {
					forgedRequest.addLast(h);
				}

				forgedRequest.addHeader(this.provider.getLocalVia(this.provider.getListeningPoints()[0].getTransport(), null));
				// ITS BUG....
				((SIPRequest) forgedRequest).setMethod(Request.ACK);
				this.provider.sendRequest(forgedRequest);

				forgedRequest = this.provider.getMessageFactory().createRequest(null);
				forgedRequest.setRequestURI(requestURI);
				forgedRequest.addHeader(mf);
				forgedRequest.addHeader(response.getHeader(CallIdHeader.NAME));
				forgedRequest.addHeader(this.provider.getHeaderFactory().createCSeqHeader(cseqNumber + 1, Request.BYE));
				forgedRequest.addHeader(response.getHeader(FromHeader.NAME));
				forgedRequest.addHeader(response.getHeader(ToHeader.NAME));
				for (Header h : routeSet) {
					forgedRequest.addLast(h);
				}

				forgedRequest.addHeader(this.provider.getLocalVia(this.provider.getListeningPoints()[0].getTransport(), null));
				// ITS BUG....
				((SIPRequest) forgedRequest).setMethod(Request.BYE);

				this.provider.sendRequest(forgedRequest);
			} else {
				// FIXME: add sometihng here?
			}
			// response.get
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Forges reqeust URI using contact and To name par tof address URI, this is
	 * requries since on fork this is how target is determined
	 * 
	 * @param response
	 * @return
	 */
	private SipURI getReqeustUri(Response response) {
		ContactHeader contact = ((ContactHeader) response.getHeader(ContactHeader.NAME));
		if (contact != null) {

			SipURI contactURI = (SipURI) contact.getAddress().getURI();

			SipURI requestURI;
			try {
				requestURI = this.provider.getAddressFactory().createSipURI(contactURI.getUser(), contactURI.getHost());
				requestURI.setPort(contactURI.getPort());
				return requestURI;
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {

		}
		return null;
	}

	/**
	 * Generates route list the same way dialog does.
	 * @param response
	 * @return
	 */
	private List getRouteList(Response response) {
		// we have record route set, as we are client, this is reversed
		ArrayList<RouteHeader> routeList = new ArrayList<RouteHeader>();
		ListIterator rrLit = response.getHeaders(RecordRouteHeader.NAME);

		while (rrLit.hasNext()) {

			RecordRouteHeader rrh = (RecordRouteHeader) rrLit.next();

			RouteHeader rh = this.provider.getHeaderFactory().createRouteHeader(rrh.getAddress());
			Iterator pIt = rrh.getParameterNames();
			while (pIt.hasNext()) {
				String pName = (String) pIt.next();
				try {
					rh.setParameter(pName, rrh.getParameter(pName));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			routeList.add(0, rh);
		}

		return routeList;
	}

	private void fetchData(Response response) {
		// fetch routeSet, requestURI and ToTag, callId
		// this is done only once?

		if (localToTag == null) {
			// all of those will become solid after tag is set , but will be
			// present without it.
			localToTag = ((ToHeader) response.getHeader(ToHeader.NAME)).getTag();
			reqeustURI = null;
			this.localRouteSet.clear();
		}
		ContactHeader contact = ((ContactHeader) response.getHeader(ContactHeader.NAME));
		//issue 623
		if(contact!=null)
			if (reqeustURI == null || (contact != null && !reqeustURI.equals(contact.getAddress().getURI()))) {
				reqeustURI = (SipURI) contact.getAddress().getURI();
			}

		//FIXME: is this correct?
		if (localRouteSet.size() == 0 || this.routeSetOnRequest) {
			this.routeSetOnRequest = false;
			this.localRouteSet.clear();
			this.localRouteSet.addAll(getRouteList(response));
		}

		this.setCallIdToReUse((CallIdHeader) response.getHeader(CallIdHeader.NAME));

	}
	
	public void generateRouteList(Request origRequest)
	{
		//FIXME: is this the only case ?
		
		if(this.wrappedDialog == null)
		{
			
			
			ViaHeader topVia = (ViaHeader) origRequest.getHeader(ViaHeader.NAME);
			Address address =null;
			if(topVia!=null)
			{
				address = getLocalAddressForTransport(topVia.getTransport());
				
				
			}else
			{
				if(logger.isDebugEnabled())
				{
					logger.debug("There is no via header, can not compute AS contact. Skipping computation of request route.");
				}
				return;
			}
			ListIterator lit=origRequest.getHeaders(RouteHeader.NAME);
			if(lit!=null )
			{
				GenericURI addressURI = (GenericURI) (address == null? null: address.getURI());
				//we atleast have one header
				//FIXME: possibly we have to do here some proxy work: RFC 3261 16.4 Route Information Preprocessing
				//FIXME: add check to remove first if it points to us.
				while(lit.hasNext())
				{
					RouteHeader rh=(RouteHeader) lit.next();
					GenericURI rhURI = (GenericURI) rh.getAddress().getURI();
					if(addressURI!=null && (rhURI.equals(addressURI)))
					{}
					else
					{
						this.localRouteSet.add(rh);
					}
				}
			}
		}
	}
	
	private Address getLocalAddressForTransport(String transport)
	{
		ListeningPoint lp = this.provider.getListeningPoint(transport);
		Address address = null;;
		if(lp!=null)
		{
			
			
			try {
				address = this.provider.getAddressFactory().createAddress("Mobicents SIP AS <sip:" + lp.getIPAddress() + ">");
				((SipURI) address.getURI()).setPort(lp.getPort());
				
				
			} catch (ParseException e) {
				e.printStackTrace();
				//throw new SipException("Failed to create contact header.",e);
			}
			
		}else
		{
			if(logger.isDebugEnabled())
			{
				logger.debug("There is no listening point, for transport: "+transport+", can not compute AS contact.");
			}
		}
		
		return address;
	}

	private void terminateFork(SipActivityHandle masterToRetain) {

		// it ConcurrentMod shouldnt happen but...
		HashSet<String> tagSet = new HashSet<String>(toTag2DialogHandle.keySet());
		Iterator<String> tagIterator = tagSet.iterator();

		while (tagIterator.hasNext()) {
			String tag = tagIterator.next();
			if (masterToRetain != null && toTag2DialogHandle.get(tag) == masterToRetain) {
				// tagSet.remove(tag);
				tagIterator.remove();
				continue;
			}
			DialogWrapper dw = (DialogWrapper) this.ra.getActivity(toTag2DialogHandle.get(tag));
			if (dw == null) {
				toTag2DialogHandle.remove(tag);
				tagIterator.remove();
			} else {

				dw.delete();
			}

		}
		// toTag2DialogHandle.keySet().removeAll(tagSet);
	}

	private void makeMaster() {
		this.forkState = DialogForkState.END;
		this.forkInitialActivityHandle = null;
		this.wrappedDialog.setApplicationData(this);
		this.ra.addClientDialogMaping(this.wrappedDialog.getLocalTag() + "_" + this.wrappedDialog.getCallId().getCallId(), getActivityHandle());
	}

	protected boolean isInForkedActions()
	{
		return this.forkInitialActivityHandle!=null||this.forkState==DialogForkState.AWAIT_FINAL;
	}

	
}
