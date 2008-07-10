package org.mobicents.slee.resource.sip;

import gov.nist.javax.sip.address.SipUri;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;
import java.util.TooManyListenersException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.OperationNotSupportedException;
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
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;

import org.mobicents.slee.resource.sip.wrappers.ClientTransactionWrapper;
import org.mobicents.slee.resource.sip.wrappers.DialogWrapper;
import org.mobicents.slee.resource.sip.wrappers.ServerTransactionWrapper;
import org.mobicents.slee.resource.sip.wrappers.SuperTransactionWrapper;

import net.java.slee.resource.sip.DialogActivity;
import net.java.slee.resource.sip.SleeSipProvider;

public class SleeSipProviderImpl implements SleeSipProvider {

	protected AddressFactory addressFactory = null;
	protected HeaderFactory headerFactory = null;
	protected MessageFactory messageFactory = null;
	protected SipStack stack = null;
	protected SipResourceAdaptor ra = null;
	protected TransactionManager txMgr = null;
	protected SipProvider provider = null;
	protected static final Logger logger = Logger
			.getLogger(SleeSipProviderImpl.class.getCanonicalName());

	public SleeSipProviderImpl(AddressFactory addressFactory,
			HeaderFactory headerFactory, MessageFactory messageFactory,
			SipStack stack, SipResourceAdaptor ra, SipProvider provider,
			TransactionManager txMgr) {
		super();
		this.addressFactory = addressFactory;
		this.headerFactory = headerFactory;
		this.messageFactory = messageFactory;
		this.stack = stack;
		this.ra = ra;
		this.provider = provider;
		this.txMgr = txMgr;
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
			} catch (ParseException e) {
				SipToSLEEUtility.displayMessage(this.logger,
						"Fialed parsing LP info",
						"SleeSipProvider.getLocalSipURI",
						"Failed to parse listening point for transport ["
								+ transport + "] [" + lp + "]", Level.SEVERE);
				e.printStackTrace();
			}
			return uri;

		} else {
			SipToSLEEUtility.displayMessage(this.logger,
					"Fialed parsing LP info", "SleeSipProvider.getLocalSipURI",
					"No listening point for transport [" + transport + "] ["
							+ lp + "]", Level.FINER);
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

	public DialogActivity getNewDialog(Address from, Address to)
			throws SipException {
		throw new UnsupportedOperationException("Not yet supported!!!");
	}

	public DialogActivity getNewDialog(DialogActivity incomingDialog,
			boolean useSameCallId) throws SipException {
		throw new UnsupportedOperationException("Not yet supported!!!");

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

		//XXX: InetAddress api is crude.....!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		ListeningPoint lp=this.provider.getListeningPoint(uri.getTransportParam());
		if(lp!=null && lp.getIPAddress().equals(uri.getHost()) && lp.getPort()==uri.getPort())
		{
			return true;
		}else
		{
			SipToSLEEUtility.displayMessage(this.logger, "Passed uri not local?", "SleeSipProvider.isLocalSipURI", "Passed URI["+uri+"] doesnt match lp["+lp+"]", Level.FINE);
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

	public ClientTransaction getNewClientTransaction(Request arg0)
			throws TransactionUnavailableException {
		return this.getNewClientTransaction(arg0, false, true);
	}

	public Dialog getNewDialog(Transaction arg0) throws SipException {
		return this.getNewDialog(arg0, false);
	}

	public ServerTransaction getNewServerTransaction(Request arg0)
			throws TransactionAlreadyExistsException,
			TransactionUnavailableException {
		return this.getNewServerTransaction(arg0,null, false);
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
			boolean raCreates, boolean makeActivity)
			throws TransactionUnavailableException {
		ClientTransaction t = provider.getNewClientTransaction(request);

		// TODO: add checks for wrapper
		ClientTransactionWrapper CTW = new ClientTransactionWrapper(t);
		if (t.getDialog() != null
				&& t.getDialog().getApplicationData() instanceof DialogWrapper) {
			CTW
					.updateDialog((DialogWrapper) t.getDialog()
							.getApplicationData());
		}

		if (!makeActivity) {
			return CTW;
		}

		ra.addActivity(CTW.getActivityHandle(), CTW);
		boolean begin = false;
		if (!raCreates)
			try {

				if (txMgr.getTransaction() == null) {
					txMgr.begin();
					begin = true;
				}
				ra.getSleeEndpoint().activityStartedSuspended(
						CTW.getActivityHandle());

			} catch (Exception e) {
				logger.severe("getActivityContextForActivity(" + CTW + ");\n"
						+ SipToSLEEUtility.doMessage(e));
				ra.removeActivity(CTW.getActivityHandle());
			} finally {
				if (begin)
					try {
						txMgr.commit();
					} catch (SecurityException e) {

						e.printStackTrace();
					} catch (IllegalStateException e) {

						e.printStackTrace();
					} catch (RollbackException e) {

						e.printStackTrace();
					} catch (HeuristicMixedException e) {

						e.printStackTrace();
					} catch (HeuristicRollbackException e) {

						e.printStackTrace();
					} catch (SystemException e) {

						e.printStackTrace();
					}
			}

		return CTW;
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
	public ServerTransaction getNewServerTransaction(Request request, ServerTransaction st,
			boolean raCreates) throws TransactionAlreadyExistsException,
			TransactionUnavailableException {
		// TODO: add checks for wrapper

		ServerTransaction t = null;
		if(st==null)
		{
		t = provider.getNewServerTransaction(request);
		}else
		{
			t=st;
		}
		ServerTransactionWrapper STW = new ServerTransactionWrapper(t);

		if (t.getDialog() != null
				&& t.getDialog().getApplicationData() instanceof DialogWrapper) {
			STW
					.updateDialog((DialogWrapper) t.getDialog()
							.getApplicationData());
		}
		
		
		boolean begin = false;
		ra.addActivity(STW.getActivityHandle(), STW);
		if (!raCreates)
			try {

				if (txMgr.getTransaction() == null) {
					txMgr.begin();
					begin = true;
				}
				ra.getSleeEndpoint().activityStartedSuspended(
						STW.getActivityHandle());

			} catch (Exception e) {
				logger.severe("getActivityContextForActivity(" + STW + ");\n"
						+ SipToSLEEUtility.doMessage(e));
				ra.removeActivity(STW.getActivityHandle());
			} finally {
				if (begin)
					try {
						txMgr.commit();
					} catch (SecurityException e) {

						e.printStackTrace();
					} catch (IllegalStateException e) {

						e.printStackTrace();
					} catch (RollbackException e) {

						e.printStackTrace();
					} catch (HeuristicMixedException e) {

						e.printStackTrace();
					} catch (HeuristicRollbackException e) {

						e.printStackTrace();
					} catch (SystemException e) {

						e.printStackTrace();
					}
			}

		return STW;
	}

	/**
	 * @param transaction -
	 *            object implementing <b>javax.sip.Transaction</b> interface
	 *            for which dialog should be obtained
	 * 
	 * @return Newly created dialog for transaction object.
	 * @throws TransactionAlreadyExistsException
	 * @throws TransactionUnavailableException
	 */
	public Dialog getNewDialog(Transaction transaction, boolean raCreates)
			throws SipException {

		// TODO: add checks for wrapper

		SuperTransactionWrapper stw = (SuperTransactionWrapper) transaction;
		Dialog d = null;
		d = provider.getNewDialog((Transaction) stw.getWrappedObject());

		DialogWrapper da = new DialogWrapper(d, ((Transaction) stw
				.getWrappedObject()).getBranchId(), this,ra);

		stw.updateDialog(da);
		//logger.info("TEST SET DIALOG["+stw.getDialog()+"]");
		//da.addOngoingTransaction(stw);
		boolean begin = false;
		ra.addActivity(da.getActivityHandle(), da);
		if (!raCreates)
			try {

				if (txMgr.getTransaction() == null) {
					txMgr.begin();
					begin = true;
				}
				ra.getSleeEndpoint().activityStartedSuspended(
						da.getActivityHandle());

			} catch (Exception e) {
				logger.severe("getActivityContextForActivity(" + da + ");\n"
						+ SipToSLEEUtility.doMessage(e));
				ra.removeActivity(da.getActivityHandle());
			} finally {
				if (begin)
					try {
						txMgr.commit();
					} catch (SecurityException e) {

						e.printStackTrace();
					} catch (IllegalStateException e) {

						e.printStackTrace();
					} catch (RollbackException e) {

						e.printStackTrace();
					} catch (HeuristicMixedException e) {

						e.printStackTrace();
					} catch (HeuristicRollbackException e) {

						e.printStackTrace();
					} catch (SystemException e) {

						e.printStackTrace();
					}
			}
		return da;
	}

}
