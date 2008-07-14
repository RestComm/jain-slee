package net.java.slee.resource.sip;

import javax.sip.ClientTransaction;
import javax.sip.Dialog;
import javax.sip.ServerTransaction;
import javax.sip.SipException;
import javax.sip.TransactionUnavailableException;
import javax.sip.message.Request;
import javax.sip.message.Response;

public interface DialogActivity extends Dialog {
	public ClientTransaction sendRequest(Request request) throws SipException,
			TransactionUnavailableException;

	public Request createRequest(Request origRequest) throws SipException;

	public Response createResponse(ServerTransaction origServerTransaction,
			Response receivedResponse) throws SipException;

	/**
	 * This method creates an association between a ServerTransaction from
	 * another Dialog with a ClientTransaction for use with this Dialog. This
	 * association can be accessed later via the getAssociatedServerTransaction
	 * method. The association is cleared when the ServerTransaction terminates.
	 * It is used primarily by B2BUA applications that wish to forward responses
	 * from a UAC Dialog to a UAS Dialog. It is intended to be used in
	 * conjunction with the getAssociatedServerTransaction and
	 * createResponse(ServerTransaction, Response) methods.
	 * 
	 * @param ct
	 *            This argument represents a Client Transaction for this Dialog
	 * @param st
	 *            This argument represents a Server Transaction for another
	 *            Dialog.
	 */
	public void associateServerTransaction(ClientTransaction ct,
			ServerTransaction st);

	public ServerTransaction getAssociatedServerTransaction(ClientTransaction ct);

	public ClientTransaction getClientTransaction(String id);

	public ServerTransaction getServerTransaction(String id);

	public String getInitiatingTransactionId();
}