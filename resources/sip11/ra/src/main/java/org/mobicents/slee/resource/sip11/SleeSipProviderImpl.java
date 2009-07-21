package org.mobicents.slee.resource.sip11;

import gov.nist.javax.sip.address.SipUri;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;
import java.util.TooManyListenersException;

import javax.sip.ClientTransaction;
import javax.sip.Dialog;
import javax.sip.InvalidArgumentException;
import javax.sip.ListeningPoint;
import javax.sip.ObjectInUseException;
import javax.sip.ServerTransaction;
import javax.sip.SipException;
import javax.sip.SipListener;
import javax.sip.SipProvider;
import javax.sip.SipStack;
import javax.sip.Transaction;
import javax.sip.TransactionAlreadyExistsException;
import javax.sip.TransactionUnavailableException;
import javax.sip.TransportAlreadySupportedException;
import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.address.SipURI;
import javax.sip.header.CallIdHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.header.ViaHeader;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;

import net.java.slee.resource.sip.CancelRequestEvent;
import net.java.slee.resource.sip.DialogActivity;
import net.java.slee.resource.sip.SleeSipProvider;

import org.apache.log4j.Logger;
import org.mobicents.slee.resource.sip11.wrappers.ClientTransactionWrapper;
import org.mobicents.slee.resource.sip11.wrappers.DialogWrapper;
import org.mobicents.slee.resource.sip11.wrappers.ServerTransactionWrapper;
import org.mobicents.slee.resource.sip11.wrappers.SuperTransactionWrapper;

public class SleeSipProviderImpl implements SleeSipProvider {

	protected AddressFactory addressFactory = null;
	protected HeaderFactory headerFactory = null;
	protected MessageFactory messageFactory = null;
	protected SipStack stack = null;
	protected SipResourceAdaptor ra = null;
	protected SipProvider provider = null;
	protected static final Logger logger = Logger.getLogger(SleeSipProviderImpl.class);

	public SleeSipProviderImpl(AddressFactory addressFactory,
			HeaderFactory headerFactory, MessageFactory messageFactory,
			SipStack stack, SipResourceAdaptor ra, SipProvider provider) {
		super();
		this.addressFactory = addressFactory;
		this.headerFactory = headerFactory;
		this.messageFactory = messageFactory;
		this.stack = stack;
		this.ra = ra;
		this.provider = provider;
	}

	public AddressFactory getAddressFactory() {

		return this.addressFactory;
	}

	public HeaderFactory getHeaderFactory() {

		return this.headerFactory;
	}

	public SipURI getLocalSipURI(String transport) {

		ListeningPoint lp = this.provider.getListeningPoint(transport);
		SipURI uri = null;
		if (lp != null) {
			try {
				uri = new SipUri();
				uri.setHost(lp.getIPAddress());
				uri.setPort(lp.getPort());
				uri.setTransportParam(transport);
			} catch (ParseException e) {
				logger.error("Failed parsing LP info. Failed to parse listening point for transport ["
								+ transport + "] [" + lp + "]",e);
			}
			return uri;

		} else {
			
			if (logger.isDebugEnabled()) {
				logger
						.debug("Failed parsing LP info. No listening point for transport ["
								+ transport + "] [" + lp + "]");
			}
			
			return null;
		}
	}

	public ViaHeader getLocalVia(String transport, String branch) {
		try {
			SipURI uri = this.getLocalSipURI(transport);
			if (uri == null) {
				return null;
			}

			return this.headerFactory.createViaHeader(uri.getHost(), uri
					.getPort(), transport, branch);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public MessageFactory getMessageFactory() {
		return this.messageFactory;
	}

	public boolean isLocalHostname(String host) {

		try {
			InetAddress[] addresses = InetAddress.getAllByName(host);

			Set<InetAddress> stackAddresses = new HashSet<InetAddress>();

			for (ListeningPoint lp : this.provider.getListeningPoints()) {
				InetAddress tmp = InetAddress.getByName(lp.getIPAddress());
				if (tmp != null)
					stackAddresses.add(tmp);
			}
			
			for(InetAddress ia:addresses)
			{
				if(stackAddresses.contains(ia))
					return true;
			}
			
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	public boolean isLocalSipURI(SipURI uri) {

		// XXX: InetAddress api is
		// crude.....!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		ListeningPoint lp = this.provider.getListeningPoint(uri
				.getTransportParam());
		if (lp != null && lp.getIPAddress().equals(uri.getHost())
				&& lp.getPort() == uri.getPort()) {
			return true;
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("Passed uri not local? Passed URI[" + uri
						+ "] doesnt match lp[" + lp + "]");
			}
			
			return false;
		}
	}

	public void addListeningPoint(ListeningPoint arg0)
			throws ObjectInUseException, TransportAlreadySupportedException {

		throw new UnsupportedOperationException("No dynamic change to LP");

	}

	public void addSipListener(SipListener arg0)
			throws TooManyListenersException {

		throw new TooManyListenersException(
				"RA can be the only Listener for this stack!!");

	}

	@SuppressWarnings("deprecation")
	public ListeningPoint getListeningPoint() {

		return this.provider.getListeningPoint();
	}

	public ListeningPoint getListeningPoint(String arg0) {

		
		return this.provider.getListeningPoint(arg0);
	}

	public ListeningPoint[] getListeningPoints() {

		return this.provider.getListeningPoints();
	}

	public CallIdHeader getNewCallId() {
		return this.provider.getNewCallId();
	}

	public ServerTransaction getNewServerTransaction(Request arg0)
			throws TransactionAlreadyExistsException,
			TransactionUnavailableException {
		return this.getNewServerTransaction(arg0,null,true);
	}

	public SipStack getSipStack() {

		throw new UnsupportedOperationException(
		"This operation is not supported yet");
	}

	public void removeListeningPoint(ListeningPoint arg0)
			throws ObjectInUseException {

		throw new UnsupportedOperationException(
				"This operation is not supported yet");

	}

	public void removeSipListener(SipListener arg0) {
		throw new UnsupportedOperationException(
				"This operation is not supported yet");

	}

	public void sendRequest(Request arg0) throws SipException {

		this.provider.sendRequest(arg0);

	}

	public void sendResponse(Response arg0) throws SipException {
		this.provider.sendResponse(arg0);

	}

	public void setAutomaticDialogSupportEnabled(boolean arg0) {
		this.provider.setAutomaticDialogSupportEnabled(arg0);

	}

	public void setListeningPoint(ListeningPoint arg0)
			throws ObjectInUseException {

		throw new UnsupportedOperationException(
				"This operation is not supported yet");
	}
	
	public ClientTransaction getNewClientTransaction(Request request,
			boolean createActivity) throws 
			TransactionUnavailableException {
		
		ClientTransaction ct = provider.getNewClientTransaction(request);
		ClientTransactionWrapper ctw = new ClientTransactionWrapper(ct);
		DialogWrapper dw = null;
		if (ct.getDialog() != null
				&& ct.getDialog().getApplicationData() instanceof DialogWrapper) {
			dw = (DialogWrapper) ct.getDialog().getApplicationData();
			dw.addOngoingTransaction(ctw);
		}
			
		// add transaction to activities if it's dialog not exists and is an activity
		// considering that if dw exists then it's an activity 
		if (dw == null) {
			if(createActivity) {
				ra.addActivity(ctw.getActivityHandle(), ctw);				
			}
		}
				
		return ctw;
	}
	public ClientTransaction getNewClientTransaction(Request request)
			throws TransactionUnavailableException {

		return this.getNewClientTransaction(request,true);
		
	}

	/**
	 * getNewServerTransaction
	 * 
	 * @param request
	 *            Request
	 * @return ServerTransaction
	 * @throws TransactionAlreadyExistsException
	 * @throws TransactionUnavailableException
	 */
	public ServerTransaction getNewServerTransaction(Request request, ServerTransaction serverTransaction,
			boolean createActivityInSlee) throws TransactionAlreadyExistsException,
			TransactionUnavailableException {
		// TODO: add checks for wrapper

		if(serverTransaction == null) {
			serverTransaction = provider.getNewServerTransaction(request);
		}
		ServerTransactionWrapper stw = new ServerTransactionWrapper(serverTransaction);
		
		DialogWrapper dw = null;
		if (serverTransaction.getDialog() != null
				&& serverTransaction.getDialog().getApplicationData() instanceof DialogWrapper) {
			dw = (DialogWrapper) serverTransaction.getDialog().getApplicationData();
			dw.addOngoingTransaction(stw);
		}
		
		// add transaction to activities if its dialog does not exists or is not an activity
		// considering that if dw exists then it's an activity 
		
		if (dw == null) {
			if (createActivityInSlee) {
				ra.addActivity(stw.getActivityHandle(), stw);
			}
		}
		
		return stw;
	}

	/**
	 * @param transaction
	 *            - object implementing <b>javax.sip.Transaction</b> interface
	 *            for which dialog should be obtained
	 * 
	 * @return Newly created dialog for transaction object.
	 * @throws TransactionAlreadyExistsException
	 * @throws TransactionUnavailableException
	 */
	public Dialog getNewDialog(Transaction transaction) throws SipException {

		// TODO: add checks for wrapper

		return this.getNewDialog(transaction, null, true);
	}

	public Dialog getNewDialog(Transaction transaction, SipActivityHandle forkMaster, boolean makeWrapper) throws SipException {

		// TODO: add checks for wrapper

		SuperTransactionWrapper stw = (SuperTransactionWrapper) transaction;
		Transaction t = (Transaction) stw.getWrappedTransaction();
		Dialog d = provider.getNewDialog(t);
		if (makeWrapper) {
			DialogWrapper dw = getNewDialogActivity(d, forkMaster, stw);

			//if (!d.isServer()) {
			//	ra.addClientDialogMaping(d.getLocalTag() + "_" + d.getCallId().getCallId(), dw.getActivityHandle());
			//}
			return dw;
		} else {
			return d;
		}
	}

	public DialogWrapper getNewDialogActivity(Dialog d, SipActivityHandle forkMaster, SuperTransactionWrapper stw) {
		//FIXME: this is a bit dangerous, we should hide this method ^
		
		// Here dialog exists, we just need another wrapper for this activity
		DialogWrapper dw = new DialogWrapper(d, forkMaster, this, ra);
		if (stw != null)
			if (stw instanceof ServerTransactionWrapper) {
				dw.addOngoingTransaction((ServerTransactionWrapper) stw);
			} else if (stw instanceof ClientTransactionWrapper) {
				dw.addOngoingTransaction((ClientTransactionWrapper) stw);
			} else {
				logger.error("Unknown type " + stw.getClass() + " of SIP Transaction, can't add to dialog wrapper");
			}
		ra.addActivity(dw.getActivityHandle(), dw);

		return dw;
	}

	public DialogActivity getNewDialog(Address from, Address to) throws SipException {
		if (from == null) {
		throw new IllegalArgumentException("From address cant be null");
		}

		if (to == null) {
		throw new IllegalArgumentException("To address cant be null");
		}

			return getNewDialog(from, to, null);
		}

	public DialogActivity getNewDialog(DialogActivity incomingDialog,
			boolean useSameCallId) throws SipException {

			if (incomingDialog == null || !incomingDialog.isServer()) {
				throw new IllegalArgumentException("Incoming dialog is either null or is UAC dialog!!");
			}
			CallIdHeader callIdHeader = null;
			if (useSameCallId) {
				callIdHeader = incomingDialog.getCallId();
			}
			
			 
			DialogWrapper dw = (DialogWrapper) getNewDialog(incomingDialog.getRemoteParty(),incomingDialog.getLocalParty(), callIdHeader);


			return dw;
	}

	private DialogActivity getNewDialog(Address from, Address to, CallIdHeader callIdHeader) throws SipException {

			DialogWrapper dw = new DialogWrapper(this, ra);
			dw.setFromAddress(from);
			dw.setToAddress(to);
			ra.addActivity(dw.getActivityHandle(), dw);
			
			if(callIdHeader == null) {
				callIdHeader = provider.getNewCallId();
			}
				dw.setCallIdToReUse(callIdHeader);

			return dw;
	}

	public boolean acceptCancel(CancelRequestEvent cancelEvent, boolean isProxy) {

		if (cancelEvent.getMatchingTransaction() != null) {
			try {

				Response response = this.getMessageFactory().createResponse(
						Response.OK, cancelEvent.getRequest());
				cancelEvent.getServerTransaction().sendResponse(response);

			} catch (Exception e) {
				// specs doesn't provide any throws clause
				throw new RuntimeException(e.getMessage(),e);
			}

			if (!isProxy)
				try {
					Response response = this.getMessageFactory()
							.createResponse(
									Response.REQUEST_TERMINATED,
									cancelEvent.getMatchingTransaction()
											.getRequest());
					cancelEvent.getMatchingTransaction().sendResponse(response);

				} catch (Exception e) {
					// specs doesn't provide any throws clause
					throw new RuntimeException(e.getMessage(),e);
				}
			return true;
		} else {
			if (!isProxy) {
				try {
					Response txDoesNotExistsResponse = this
							.getMessageFactory()
							.createResponse(
									Response.CALL_OR_TRANSACTION_DOES_NOT_EXIST,
									cancelEvent.getRequest());

					// provider.sendResponse(txDoesNotExistsResponse);
					cancelEvent.getServerTransaction().sendResponse(
							txDoesNotExistsResponse);
				} catch (Exception e) {
					// specs doesn't provide any throws clause
					throw new RuntimeException(e.getMessage(),e);
				}
			}
			return false;
		}
	}

	public DialogActivity forwardForkedResponse(
			ServerTransaction origServerTransaction, Response response)
			throws SipException {
		// TODO Auto-generated method stub
		return null;
	}

}
