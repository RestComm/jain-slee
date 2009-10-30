package org.mobicents.slee.resource.sip11;

import gov.nist.javax.sip.Utils;
import gov.nist.javax.sip.address.SipUri;
import gov.nist.javax.sip.message.SIPRequest;

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
import javax.sip.header.FromHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.header.ToHeader;
import javax.sip.header.ViaHeader;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;
import javax.slee.facilities.Tracer;

import net.java.slee.resource.sip.CancelRequestEvent;
import net.java.slee.resource.sip.DialogActivity;
import net.java.slee.resource.sip.SleeSipProvider;

import org.mobicents.slee.resource.sip11.wrappers.ClientTransactionWrapper;
import org.mobicents.slee.resource.sip11.wrappers.DialogWrapper;
import org.mobicents.slee.resource.sip11.wrappers.ClientDialogWrapper;
import org.mobicents.slee.resource.sip11.wrappers.ServerTransactionWrapper;

/**
 * 
 * @author B.Baranowski
 * @author martins
 * 
 */
public class SleeSipProviderImpl implements SleeSipProvider {

	protected AddressFactory addressFactory = null;
	protected HeaderFactory headerFactory = null;
	protected MessageFactory messageFactory = null;
	protected SipStack stack = null;
	protected SipResourceAdaptor ra = null;
	protected SipProvider provider = null;
	protected final Tracer tracer;

	/**
	 * 
	 * @param addressFactory
	 * @param headerFactory
	 * @param messageFactory
	 * @param stack
	 * @param ra
	 * @param provider
	 */
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
		this.tracer = ra.getTracer(SleeSipProviderImpl.class.getSimpleName());
	}

	/**
	 * 
	 * @return
	 */
	public SipProvider getRealProvider() {
		return provider;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.java.slee.resource.sip.SleeSipProvider#getAddressFactory()
	 */
	public AddressFactory getAddressFactory() {
		return this.addressFactory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.java.slee.resource.sip.SleeSipProvider#getHeaderFactory()
	 */
	public HeaderFactory getHeaderFactory() {
		return this.headerFactory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.java.slee.resource.sip.SleeSipProvider#getLocalSipURI(java.lang.String
	 * )
	 */
	public SipURI getLocalSipURI(String transport) {

		final ListeningPoint lp = this.provider.getListeningPoint(transport);
		if (lp != null) {
			try {
				final SipURI uri = new SipUri();
				uri.setHost(lp.getIPAddress());
				uri.setPort(lp.getPort());
				uri.setTransportParam(transport);
				return uri;
			} catch (ParseException e) {
				tracer.severe(
						"Failed parsing LP info. Failed to parse listening point for transport ["
								+ transport + "] [" + lp + "]", e);
				return null;
			}
		} else {
			if (tracer.isFineEnabled()) {
				tracer
						.fine("Failed parsing LP info. No listening point for transport ["
								+ transport + "] [" + lp + "]");
			}
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.java.slee.resource.sip.SleeSipProvider#getLocalVia(java.lang.String,
	 * java.lang.String)
	 */
	public ViaHeader getLocalVia(String transport, String branch) {
		final ListeningPoint lp = provider.getListeningPoint(transport);
		if (lp != null) {
			try {
				return headerFactory.createViaHeader(lp.getIPAddress(), lp
						.getPort(), lp.getTransport(), branch);
			} catch (ParseException e) {
				tracer.severe(e.getMessage(), e);
			} catch (InvalidArgumentException e) {
				tracer.severe(e.getMessage(), e);
			}
		}
		return null;
	}

	/**
	 * 
	 * @return
	 * @throws InvalidArgumentException
	 * @throws ParseException
	 */
	public ViaHeader getLocalVia() throws ParseException,
			InvalidArgumentException {
		final ListeningPoint lp = provider.getListeningPoints()[0];
		return headerFactory.createViaHeader(lp.getIPAddress(), lp.getPort(),
				lp.getTransport(), null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.java.slee.resource.sip.SleeSipProvider#getMessageFactory()
	 */
	public MessageFactory getMessageFactory() {
		return this.messageFactory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.java.slee.resource.sip.SleeSipProvider#isLocalHostname(java.lang.
	 * String)
	 */
	public boolean isLocalHostname(String host) {

		try {
			InetAddress[] addresses = InetAddress.getAllByName(host);

			Set<InetAddress> stackAddresses = new HashSet<InetAddress>();

			for (ListeningPoint lp : this.provider.getListeningPoints()) {
				InetAddress tmp = InetAddress.getByName(lp.getIPAddress());
				if (tmp != null)
					stackAddresses.add(tmp);
			}

			for (InetAddress ia : addresses) {
				if (stackAddresses.contains(ia))
					return true;
			}

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.java.slee.resource.sip.SleeSipProvider#isLocalSipURI(javax.sip.address
	 * .SipURI)
	 */
	public boolean isLocalSipURI(SipURI uri) {

		// XXX: InetAddress api is
		// crude.....!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		ListeningPoint lp = this.provider.getListeningPoint(uri
				.getTransportParam());
		if (lp != null && lp.getIPAddress().equals(uri.getHost())
				&& lp.getPort() == uri.getPort()) {
			return true;
		} else {
			if (tracer.isFineEnabled()) {
				tracer.fine("Passed uri not local? Passed URI[" + uri
						+ "] doesnt match lp[" + lp + "]");
			}

			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.sip.SipProvider#addListeningPoint(javax.sip.ListeningPoint)
	 */
	public void addListeningPoint(ListeningPoint arg0)
			throws ObjectInUseException, TransportAlreadySupportedException {
		throw new UnsupportedOperationException("No dynamic change to LP");
	}

	public void addSipListener(SipListener arg0)
			throws TooManyListenersException {
		throw new TooManyListenersException(
				"RA can be the only Listener for this stack!!");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.sip.SipProvider#getListeningPoint()
	 */
	@SuppressWarnings("deprecation")
	public ListeningPoint getListeningPoint() {
		return this.provider.getListeningPoint();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.sip.SipProvider#getListeningPoint(java.lang.String)
	 */
	public ListeningPoint getListeningPoint(String arg0) {
		return this.provider.getListeningPoint(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.sip.SipProvider#getListeningPoints()
	 */
	public ListeningPoint[] getListeningPoints() {
		return this.provider.getListeningPoints();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.sip.SipProvider#getNewCallId()
	 */
	public CallIdHeader getNewCallId() {
		return this.provider.getNewCallId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.sip.SipProvider#getSipStack()
	 */
	public SipStack getSipStack() {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.sip.SipProvider#removeListeningPoint(javax.sip.ListeningPoint)
	 */
	public void removeListeningPoint(ListeningPoint arg0)
			throws ObjectInUseException {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.sip.SipProvider#removeSipListener(javax.sip.SipListener)
	 */
	public void removeSipListener(SipListener arg0) {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.sip.SipProvider#sendRequest(javax.sip.message.Request)
	 */
	public void sendRequest(Request arg0) throws SipException {
		this.provider.sendRequest(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.sip.SipProvider#sendResponse(javax.sip.message.Response)
	 */
	public void sendResponse(Response arg0) throws SipException {
		this.provider.sendResponse(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.sip.SipProvider#setAutomaticDialogSupportEnabled(boolean)
	 */
	public void setAutomaticDialogSupportEnabled(boolean arg0) {
		this.provider.setAutomaticDialogSupportEnabled(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.sip.SipProvider#setListeningPoint(javax.sip.ListeningPoint)
	 */
	public void setListeningPoint(ListeningPoint arg0)
			throws ObjectInUseException {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.sip.SipProvider#getNewClientTransaction(javax.sip.message.Request)
	 */
	public ClientTransaction getNewClientTransaction(Request request)
			throws TransactionUnavailableException {

		final ClientTransaction ct = provider.getNewClientTransaction(request);
		final ClientTransactionWrapper ctw = new ClientTransactionWrapper(ct,
				ra);
		ctw.setActivity(true);

		final Dialog d = ct.getDialog();
		if (d != null) {
			final DialogWrapper dw = (DialogWrapper) d.getApplicationData();
			if (dw != null) {
				dw.addOngoingTransaction(ctw);
			}
		}

		if (!ra.addSuspendedActivity(ctw, tracer.isFineEnabled())) {
			throw new TransactionUnavailableException(
					"Failed to create activity");
		}

		return ctw;
	}

	/**
	 * Creates a new {@link ClientTransactionWrapper} bound to a
	 * {@link DialogWrapper}, which is not an activity in SLEE.
	 * 
	 * @param dialogWrapper
	 * @param request
	 * @return
	 * @throws TransactionUnavailableException
	 */
	public ClientTransactionWrapper getNewDialogActivityClientTransaction(
			DialogWrapper dialogWrapper, Request request)
			throws TransactionUnavailableException {
		final ClientTransaction ct = provider.getNewClientTransaction(request);
		final ClientTransactionWrapper ctw = new ClientTransactionWrapper(ct,
				ra);
		dialogWrapper.addOngoingTransaction(ctw);
		return ctw;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.sip.SipProvider#getNewServerTransaction(javax.sip.message.Request)
	 */
	public ServerTransaction getNewServerTransaction(Request request)
			throws TransactionAlreadyExistsException,
			TransactionUnavailableException {

		// TODO: add checks for wrapper

		final ServerTransaction st = provider.getNewServerTransaction(request);
		ServerTransactionWrapper stw = new ServerTransactionWrapper(st, ra);

		final Dialog d = st.getDialog();
		final DialogWrapper dw = (d == null ? null : (DialogWrapper) d
				.getApplicationData());
		if (dw != null) {
			dw.addOngoingTransaction(stw);
		} else {
			// SLEE 1.1 specs: D.4
			// ServerTransaction Activity objects are created automatically when
			// the resource adaptor receives an
			// incoming SIP request. The activity ends when a final response is
			// sent on the ServerTransaction.
			stw.setActivity(true);
			if (!ra.addSuspendedActivity(stw, tracer.isFineEnabled())) {
				throw new TransactionUnavailableException(
						"Failed to create activity.");
			}
		}

		return stw;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.sip.SipProvider#getNewDialog(javax.sip.Transaction)
	 */
	public Dialog getNewDialog(Transaction transaction) throws SipException {
		if (transaction.getClass() == ServerTransactionWrapper.class) {
			final ServerTransactionWrapper stw = (ServerTransactionWrapper) transaction;
			final ServerTransaction st = stw.getWrappedServerTransaction();
			final Dialog d = provider.getNewDialog(st);
			// some hacking in jsip, we need a dialog id now and the real dialog
			// does not have a local tag
			final String localTag = Utils.getInstance().generateTag();
			final String dialogId = ((SIPRequest) st.getRequest()).getDialogId(
					true, localTag);
			final DialogWrapper dw = new DialogWrapper(d, dialogId, localTag,ra);
			dw.addOngoingTransaction(stw);
			ra.addSuspendedActivity(dw, tracer.isFineEnabled());
			return dw;
		} else if (transaction.getClass() == ClientTransactionWrapper.class) {
			// this is not efficient, but should not be used anyway and saves
			// more code scenarios
			final Request r = transaction.getRequest();
			final DialogWrapper dw = _getNewDialog(
					((FromHeader) r.getHeader(FromHeader.NAME)).getAddress(),
					Utils.getInstance().generateTag(),
					((ToHeader) r.getHeader(ToHeader.NAME)).getAddress(), null);
			dw.addOngoingTransaction((ClientTransactionWrapper) transaction);
			return dw;
		} else {
			throw new IllegalArgumentException("unknown transaction class");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.java.slee.resource.sip.SleeSipProvider#getNewDialog(javax.sip.address
	 * .Address, javax.sip.address.Address)
	 */
	public DialogActivity getNewDialog(Address from, Address to)
			throws SipException {
		if (from == null) {
			throw new IllegalArgumentException("From address cant be null");
		}
		if (to == null) {
			throw new IllegalArgumentException("To address cant be null");
		}
		return _getNewDialog(from, gov.nist.javax.sip.Utils.getInstance()
				.generateTag(), to, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.java.slee.resource.sip.SleeSipProvider#getNewDialog(net.java.slee
	 * .resource.sip.DialogActivity, boolean)
	 */
	public DialogActivity getNewDialog(DialogActivity incomingDialog,
			boolean useSameCallId) throws SipException {

		if (incomingDialog == null || !incomingDialog.isServer()) {
			throw new IllegalArgumentException(
					"Incoming dialog is either null or is UAC dialog!!");
		}
		CallIdHeader callIdHeader = null;
		if (useSameCallId) {
			callIdHeader = incomingDialog.getCallId();
		}

		return _getNewDialog(incomingDialog.getRemoteParty(), Utils.getInstance().generateTag(), incomingDialog.getLocalParty(), callIdHeader);
	}

	/**
	 * 
	 * @param from
	 * @param to
	 * @param callIdHeader
	 * @return
	 * @throws SipException
	 */
	private DialogWrapper _getNewDialog(Address from, String localTag,
			Address to, CallIdHeader callIdHeader) throws SipException {

		final DialogWrapper dw = new ClientDialogWrapper(from, localTag,
				to, (callIdHeader == null ? provider.getNewCallId()
						: callIdHeader), ra);

		if (!ra.addSuspendedActivity(dw, tracer.isFineEnabled())) {
			throw new SipException("Failed to create activity.");
		}

		return dw;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.java.slee.resource.sip.SleeSipProvider#acceptCancel(net.java.slee
	 * .resource.sip.CancelRequestEvent, boolean)
	 */
	public boolean acceptCancel(CancelRequestEvent cancelEvent, boolean isProxy) {

		if (cancelEvent.getMatchingTransaction() != null) {
			// FIXME: no delay may cause unexpected behaviour?
			try {

				Response response = this.getMessageFactory().createResponse(
						Response.OK, cancelEvent.getRequest());
				cancelEvent.getServerTransaction().sendResponse(response);
				Thread.sleep(50);
			} catch (Exception e) {
				// specs doesn't provide any throws clause
				throw new RuntimeException(e.getMessage(), e);
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
					throw new RuntimeException(e.getMessage(), e);
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
					Thread.sleep(50);
					// provider.sendResponse(txDoesNotExistsResponse);
					cancelEvent.getServerTransaction().sendResponse(
							txDoesNotExistsResponse);
					Thread.sleep(50);
				} catch (Exception e) {
					// specs doesn't provide any throws clause
					throw new RuntimeException(e.getMessage(), e);
				}
			}
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.java.slee.resource.sip.SleeSipProvider#forwardForkedResponse(javax
	 * .sip.ServerTransaction, javax.sip.message.Response)
	 */
	public DialogActivity forwardForkedResponse(
			ServerTransaction origServerTransaction, Response response)
			throws SipException {
		// TODO
		throw new UnsupportedOperationException();
	}

}
