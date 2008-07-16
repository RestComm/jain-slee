package org.mobicents.slee.resource.sip11.wrappers;

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
import javax.sip.DialogTerminatedEvent;
import javax.sip.InvalidArgumentException;
import javax.sip.ServerTransaction;
import javax.sip.SipException;
import javax.sip.Transaction;
import javax.sip.TransactionDoesNotExistException;
import javax.sip.TransactionUnavailableException;
import javax.sip.address.Address;
import javax.sip.header.CSeqHeader;
import javax.sip.header.CallIdHeader;
import javax.sip.header.ContactHeader;
import javax.sip.header.ContentTypeHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.Header;
import javax.sip.header.Parameters;
import javax.sip.header.RecordRouteHeader;
import javax.sip.header.RouteHeader;
import javax.sip.header.ToHeader;
import javax.sip.header.ViaHeader;
import javax.sip.message.Message;
import javax.sip.message.Request;
import javax.sip.message.Response;

import net.java.slee.resource.sip.DialogActivity;

import org.apache.log4j.Logger;
import org.mobicents.slee.resource.sip11.SipActivityHandle;
import org.mobicents.slee.resource.sip11.SipResourceAdaptor;
import org.mobicents.slee.resource.sip11.SleeSipProviderImpl;

public class DialogWrapper implements DialogActivity, WrapperSuperInterface {

	protected javax.sip.Dialog wrappedDialog = null;
	protected SipActivityHandle sipActivityHandle;
	protected String initiatingTransctionId = null;
	protected SipResourceAdaptor ra;
	// TODO: Add sync maps?
	// SPECS 1.1 stuff, wonder who came up with that idea.
	protected ConcurrentHashMap<String, ClientTransaction> ongoingClientTransactions = new ConcurrentHashMap<String, ClientTransaction>();
	protected ConcurrentHashMap<String, ServerTransaction> ongoingServerTransactions = new ConcurrentHashMap<String, ServerTransaction>();


	// Forging kit?
	protected SleeSipProviderImpl provider = null;

	protected static final Logger logger = Logger.getLogger(DialogWrapper.class);

	public DialogWrapper(Dialog wrappedDialog, String initTxID,
			SleeSipProviderImpl provider, SipResourceAdaptor ra) {
		if (wrappedDialog.getApplicationData() != null) {
			if (wrappedDialog.getApplicationData() instanceof DialogWrapper) {
				throw new IllegalArgumentException(
						"Dialog to wrap has alredy a wrapper!!!");
			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("Overwriting application data present - "
							+ wrappedDialog.getApplicationData());
				}
			}

		}

		this.wrappedDialog = wrappedDialog;
		this.wrappedDialog.setApplicationData(this);
		this.initiatingTransctionId = initTxID;
		this.provider = provider;
		this.ra = ra;
		// TODO: Come up with something way better, we cant hash on
		// dialogId, since it changes
		// We cant make ID inside change, since this would make Container to
		// leak attached SBBE - we would loose them, as in container view,
		// hash contract on handle would be broken
		// and on each change dialog would be considered as different activity.
		sipActivityHandle = new SipActivityHandle(String.valueOf(this.wrappedDialog.hashCode()));
	}

	public void cleanup() {
		this.wrappedDialog.setApplicationData(null);
		this.wrappedDialog = null;
		this.sipActivityHandle = null;
		this.initiatingTransctionId = null;
		this.ra = null;
		this.ongoingClientTransactions = null;
		this.ongoingServerTransactions = null;
	}
	
	public void associateServerTransaction(ClientTransaction ct,
			ServerTransaction st) {
		// ct MUST be in ongoing transaction, its local, st - comes from another
		// dialog

		if (!this.hasOngoingClientTransaction(ct.getBranchId())) {
			throw new IllegalArgumentException(
					"Client transaction is not in ongoing transaction list!!!");
		}

		if (st != null) {
			if (!((DialogWrapper) st.getDialog())
					.hasOngoingServerTransaction(st.getBranchId())) {
				throw new IllegalArgumentException(
						"Server transaction is not in ongoing transaction list!!!");
			}
			// XXX: ctx can be associated to only one stx, however stx can have
			// multiple ctx

			((ClientTransactionWrapper) ct).associateServerTransaction(st
					.getBranchId(), ((DialogWrapper) st.getDialog())
					.getActivityHandle());
		} else {
			((ClientTransactionWrapper) ct).associateServerTransaction(null,
					null);
		}

	}

	public Request createRequest(Request origRequest) throws SipException {
		Request forgedRequest = this.wrappedDialog.createRequest(origRequest
				.getMethod());

		Set<String> headersToOmmit = new HashSet<String>();
		headersToOmmit.add(RouteHeader.NAME);
		headersToOmmit.add(RecordRouteHeader.NAME);
		headersToOmmit.add(ViaHeader.NAME);
		headersToOmmit.add(CallIdHeader.NAME);
		headersToOmmit.add(CSeqHeader.NAME);
		forgeMessage(origRequest, forgedRequest, headersToOmmit);
		return forgedRequest;
	}

	public Response createResponse(ServerTransaction origServerTransaction,
			Response receivedResponse) throws SipException {
		if (!this.hasOngoingServerTransaction(origServerTransaction
				.getBranchId()))
			throw new IllegalArgumentException(
					"Passed server transaction is not in ongoing STX list for this dialog!!!!");

		Response forgedResponse;
		try {
			// I thinks this will fail?
			forgedResponse = this.provider.getMessageFactory().createResponse(
					receivedResponse.getStatusCode(),
					origServerTransaction.getRequest());
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
		forgedResponse.addHeader(this.provider.getHeaderFactory()
				.createContactHeader(this.getLocalParty()));
		return forgedResponse;
	}

	public ServerTransaction getAssociatedServerTransaction(ClientTransaction ct) {

		if (!this.hasOngoingClientTransaction(ct.getBranchId())) {
			throw new IllegalArgumentException(
					"Passed client transaction is not running for this dialog");
		}
			
		ClientTransactionWrapper ctw = (ClientTransactionWrapper) ct;
		if (ctw.getAssociatedTransactionBranchId() == null || ra.getActivity(ctw.getAssociationHandle()) == null) {
			return null;
		} else {
			DialogActivity da = (DialogActivity) ra.getActivity(ctw
					.getAssociationHandle());
			return da.getServerTransaction(ctw
					.getAssociatedTransactionBranchId());
		}
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

	public ClientTransaction sendRequest(Request request) throws SipException,
			TransactionUnavailableException {
		
		ClientTransactionWrapper CTW = (ClientTransactionWrapper) this.provider
				.getNewClientTransaction(request);
		this.wrappedDialog.sendRequest((ClientTransaction) CTW
				.getWrappedTransaction());
		return CTW;
	}

	public Request createAck(long arg0) throws InvalidArgumentException,
			SipException {
		return wrappedDialog.createAck(arg0);
	}

	public Request createPrack(Response arg0)
			throws DialogDoesNotExistException, SipException {
		return this.wrappedDialog.createPrack(arg0);
	}

	public Response createReliableProvisionalResponse(int arg0)
			throws InvalidArgumentException, SipException {
		return this.wrappedDialog.createReliableProvisionalResponse(arg0);
	}

	public Request createRequest(String arg0) throws SipException {
		return this.wrappedDialog.createRequest(arg0);
	}

	public void delete() {
		// This ensures that dialog will be removed
		if (wrappedDialog.getState() == null) {
			ra.processDialogTerminated(new DialogTerminatedEvent(this.provider,
					wrappedDialog));
		}
		wrappedDialog.delete();

	}

	public Object getApplicationData() {
		throw new SecurityException();
	}

	public CallIdHeader getCallId() {
		return this.wrappedDialog.getCallId();
	}

	public String getDialogId() {
		return this.wrappedDialog.getDialogId();
	}

	public Transaction getFirstTransaction() {
		return this.wrappedDialog.getFirstTransaction();
	}

	public Address getLocalParty() {
		return this.wrappedDialog.getLocalParty();
	}

	public long getLocalSeqNumber() {
		return this.wrappedDialog.getLocalSeqNumber();
	}

	public int getLocalSequenceNumber() {
		return this.wrappedDialog.getLocalSequenceNumber();
	}

	public String getLocalTag() {
		return this.wrappedDialog.getLocalTag();
	}

	public Address getRemoteParty() {
		return this.wrappedDialog.getRemoteParty();
	}

	public long getRemoteSeqNumber() {
		return this.wrappedDialog.getRemoteSeqNumber();
	}

	public int getRemoteSequenceNumber() {
		return this.wrappedDialog.getRemoteSequenceNumber();
	}

	public String getRemoteTag() {
		return this.wrappedDialog.getRemoteTag();
	}

	public Address getRemoteTarget() {
		return this.wrappedDialog.getRemoteTarget();
	}

	public Iterator getRouteSet() {
		return this.wrappedDialog.getRouteSet();
	}

	public DialogState getState() {
		return this.wrappedDialog.getState();
	}

	public void incrementLocalSequenceNumber() {

		this.wrappedDialog.incrementLocalSequenceNumber();

	}

	public boolean isSecure() {
		return wrappedDialog.isSecure();
	}

	public boolean isServer() {
		return wrappedDialog.isServer();
	}

	public void sendAck(Request arg0) throws SipException {
		wrappedDialog.sendAck(arg0);
	}

	public void sendReliableProvisionalResponse(Response arg0)
			throws SipException {
		wrappedDialog.sendReliableProvisionalResponse(arg0);
	}

	public void sendRequest(ClientTransaction arg0)
			throws TransactionDoesNotExistException, SipException {
		// TODO: add check for wrapper
		wrappedDialog
				.sendRequest((ClientTransaction) ((ClientTransactionWrapper) arg0)
						.getWrappedTransaction());
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

	// =========================== XXX: Helper methods =====================
	private void forgeMessage(Message originalMessage, Message forgedMessage,
			Set<String> headerstoOmmit) throws SipException {
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
			if (headerName.equals(ToHeader.NAME)
					|| headerName.equals(FromHeader.NAME)) {
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

				ListIterator headersIterator = originalMessage
						.getHeaders(headerName);

				while (headersIterator.hasNext()) {
					Header origHeader, forgedHeader;

					// origHeader = (Header)
					// originalMessage.getHeader(headerName);
					origHeader = (Header) headersIterator.next();

					forgedHeader = null;

					try {
						forgedHeader = (Header) this.provider
								.getHeaderFactory().createHeader(
										headerName,
										origHeader.toString().substring(
												origHeader.toString().indexOf(
														":") + 1));

						forgedMessage
								.addLast((javax.sip.header.Header) forgedHeader);
					} catch (ParseException e) {
						logger.error("Failed to generate header on [" + headerName
										+ "]. To copy value [" + origHeader + "]\n",e);
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
				forgedMessage.setContent(new String(copy),
						(ContentTypeHeader) forgedMessage
								.getHeader(ContentTypeHeader.NAME));
			} catch (ParseException e) {
				logger.error("Failed to set content on forged message. To copy value ["
								+ new String(copy)
								+ "] Type ["
								+ forgedMessage
										.getHeader(ContentTypeHeader.NAME)
								+ "]\n",e);
			}
		}

	}

	private void copyParameters(String name, Parameters origHeader,
			Parameters forgedHeader, Set<String> toOmmit) {

		// FIXME: This will fail for parameters such as lr ??
		Iterator it = origHeader.getParameterNames();

		while (it.hasNext()) {
			String p_name = (String) it.next();
			if (toOmmit.contains(p_name)
					|| forgedHeader.getParameter(p_name) != null) {
				if (logger.isDebugEnabled()) {
					logger.debug("Ommiting parameter on [" + name
							+ "]. To copy value ["
							+ origHeader.getParameter(p_name)
							+ "]\nValue in forged ["
							+ forgedHeader.getParameter(p_name) + "]");
				}
			} else {
				try {
					forgedHeader.setParameter(p_name, origHeader
							.getParameter(p_name));
				} catch (ParseException e) {
					logger.error("Failed to pass parameter on [" + name + "]. To copy value ["
									+ origHeader.getParameter(p_name)
									+ "]\nValue in forged ["
									+ forgedHeader.getParameter(p_name) + "]",e);
				}
			}
		}

	}

	public String toString() {
		return "Dialog Id[" + this.getDialogId() + "] State[" + this.getState()
				+ "] OngoingCTX[" + this.ongoingClientTransactions.size()
				+ "] OngoingSTX[" + this.ongoingServerTransactions.size() + "]";
	}

}
