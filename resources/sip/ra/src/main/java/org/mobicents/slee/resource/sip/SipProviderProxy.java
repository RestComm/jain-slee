package org.mobicents.slee.resource.sip;

import gov.nist.javax.sip.header.CallID;

import java.util.TooManyListenersException;

import javax.sip.ClientTransaction;
import javax.sip.Dialog;
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
import javax.sip.header.CallIdHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.resource.sip.wrappers.ClientTransactionWrapper;
import org.mobicents.slee.resource.sip.wrappers.DialogWrapper;
import org.mobicents.slee.resource.sip.wrappers.SecretWrapperInterface;
import org.mobicents.slee.resource.sip.wrappers.ServerTransactionWrapper;

/**
 * <p>
 * Title: SIP_RA
 * </p>
 * <p>
 * Description: JAIN SIP Resource Adaptor
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * <p>
 * Company: Lucent Technologies
 * </p>
 * 
 * @author mvera@lucent.com
 * @version 1.0
 */
public class SipProviderProxy implements SipProvider {
	private Logger logger = Logger.getLogger(SipProviderProxy.class);

	SipProvider provider = null;

	SipStack stack = null;

	SipResourceAdaptor sipResourceAdaptor;

	TransactionManager txMgr = null;

	public void release() {
		((SipStackProxy) stack).release();
		provider = null;
		stack = null;
	}

	public SipProviderProxy(SipProvider provider,
			SipResourceAdaptor sipResourceAdaptor) {
		this.provider = provider;
		this.sipResourceAdaptor = sipResourceAdaptor;
		this.stack = new SipStackProxy(provider.getSipStack(), this);
		this.txMgr = SleeContainer.getTransactionManager();
	}

	/**
	 * addSipListener
	 * 
	 * @param sipListener
	 *            SipListener
	 * @throws TooManyListenersException
	 */
	public void addSipListener(SipListener sipListener)
			throws TooManyListenersException {
		throw new java.lang.SecurityException();
	}

	/**
	 * getListeningPoint
	 * 
	 * @return ListeningPoint
	 */
	public ListeningPoint getListeningPoint() {
		return provider.getListeningPoints()[0];
	}

	/**
	 * getNewCallId
	 * 
	 * @return CallIdHeader
	 */
	public CallIdHeader getNewCallId() {
		return provider.getNewCallId();
	}

	public ListeningPoint getListeningPoint(String transport) {
		return provider.getListeningPoint(transport);
	}

	/**
	 * getNewClientTransaction
	 * 
	 * @param request
	 *            Request
	 * @return ClientTransaction
	 * @throws TransactionUnavailableException
	 */
	public ClientTransaction getNewClientTransaction(Request request)
			throws TransactionUnavailableException {
		
		return getNewClientTransaction(request, false);
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
	public ServerTransaction getNewServerTransaction(Request request)
			throws TransactionAlreadyExistsException,
			TransactionUnavailableException {
		return getNewServerTransaction(request, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.sip.SipProvider#getNewDialog(javax.sip.Transaction)
	 * 
	 * public Dialog getNewDialog(Transaction transaction) throws SipException {
	 * 
	 * return this.provider.getNewDialog(transaction); }
	 */
	/**
	 * @param transaction -
	 *            object implementing <b>javax.sip.Transaction</b> interface
	 *            for which dialog should be obtained
	 * 
	 * @return Newly created dialog for transaction object.
	 * @throws TransactionAlreadyExistsException
	 * @throws TransactionUnavailableException
	 */
	public Dialog getNewDialog(Transaction transaction) throws SipException {

		return getNewDialog(transaction, false);
	}

	/**
	 * getNewClientTransaction
	 * 
	 * @param request
	 *            Request
	 * @return ClientTransaction
	 * @throws TransactionUnavailableException
	 */
	public ClientTransaction getNewClientTransaction(Request request,
			boolean raCreates) throws TransactionUnavailableException {
		ClientTransaction t = provider.getNewClientTransaction(request);

		Dialog dialog = t.getDialog();
		DialogWrapper dialogWrapper = null;
		if (dialog != null) {
			dialogWrapper = (DialogWrapper) dialog.getApplicationData();
			if (dialogWrapper == null) {
				dialogWrapper = new DialogWrapper(dialog, sipResourceAdaptor);
			}
		}

		ClientTransactionWrapper CTW = new ClientTransactionWrapper(t,
				dialogWrapper);
		SipActivityHandle SAH = CTW.getActivityHandle();

		if (logger.isDebugEnabled()) {
			logger.debug("\n === CREATED ACTIVITY FOR:\"" + t.getBranchId()
					+ "\"");
			logger.debug("\n=== SAH:\"" + SAH + "\"");
		}
		sipResourceAdaptor.getActivities().put(SAH, CTW);
		t.setApplicationData(CTW);
		boolean begin = false;
		if (!raCreates)
			try {

				if (txMgr.getTransaction() == null) {
					txMgr.begin();
					begin = true;
				}
				sipResourceAdaptor.getSleeEndpoint().activityStartedSuspended(
						SAH);
			} catch (Exception e) {
				logger.error("getNewClientTransaction(" + request + ")", e);
			} finally {
				if (begin)
					try {
						txMgr.commit();
					} catch (SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (RollbackException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (HeuristicMixedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (HeuristicRollbackException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SystemException e) {
						// TODO Auto-generated catch block
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
	public ServerTransaction getNewServerTransaction(Request request,
			boolean raCreates) throws TransactionAlreadyExistsException,
			TransactionUnavailableException {
		ServerTransaction t = provider.getNewServerTransaction(request);
		Dialog dialog = t.getDialog();
		DialogWrapper dialogWrapper = null;
		if (dialog != null) {
			dialogWrapper = (DialogWrapper) dialog.getApplicationData();
			if (dialogWrapper == null) {
				dialogWrapper = new DialogWrapper(dialog, sipResourceAdaptor);
			}
		}
		// ServerTransactionWrapper STW=new ServerTransactionWrapper(t,dialog,
		// sleeEndpoint,this);
		ServerTransactionWrapper STW = new ServerTransactionWrapper(t,
				dialogWrapper, sipResourceAdaptor);
		// LEAK BUG
		// sipResourceAdaptor.getActivities().put(t.getBranchId(), STW);

		SipActivityHandle SAH = STW.getActivityHandle();
		sipResourceAdaptor.getActivities().put(SAH, STW);
		t.setApplicationData(STW);
		if (logger.isDebugEnabled()) {
			logger.debug("\n === CREATED ACTIVITY FOR:\"" + "\"");
			logger.debug("\n=== SAH:\"" + SAH + "\"");
		}

		boolean begin = false;
		if (!raCreates)
			try {

				if (txMgr.getTransaction() == null) {
					txMgr.begin();
					begin = true;
				}
				sipResourceAdaptor.getSleeEndpoint().activityStartedSuspended(
						SAH);
			} catch (Exception e) {
				logger.error("getNewServerTransaction(" + request + ");", e);
			} finally {
				if (begin)
					try {
						txMgr.commit();
					} catch (SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (RollbackException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (HeuristicMixedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (HeuristicRollbackException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SystemException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}

		return STW;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.sip.SipProvider#getNewDialog(javax.sip.Transaction)
	 * 
	 * public Dialog getNewDialog(Transaction transaction) throws SipException {
	 * 
	 * return this.provider.getNewDialog(transaction); }
	 */
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

		SecretWrapperInterface transactionWrapper = (SecretWrapperInterface) transaction;

		Transaction realTx = transactionWrapper.getRealTransaction();

		Dialog dial = realTx.getDialog();

		DialogWrapper DW = null;

		if (dial != null && dial.getApplicationData() != null
				&& dial.getApplicationData() instanceof DialogWrapper) {
			DW = (DialogWrapper) dial.getApplicationData();
			DW.renew();
			return DW;
		} else {
			dial = this.provider.getNewDialog(realTx);
			DW = new DialogWrapper(dial, sipResourceAdaptor);
			String dialID = DW.getDialogId();
			transactionWrapper.setDialogWrapper(DW);
			if (logger.isDebugEnabled()) {
				logger.debug("==================== DIALOG: " + dial + " : "
						+ dialID + " ===============");
			}
			SipActivityHandle SAH = DW.getActivityHandle();
			sipResourceAdaptor.getActivities().put(SAH, DW);

			if (logger.isDebugEnabled()) {
				logger.debug("\n === CREATED ACTIVITY FOR:\"" + dialID + "\"");
				logger.debug("\n=== SAH:\"" + SAH + "\"");
			}

			boolean begin = false;
			if (!raCreates)
				try {

					if (txMgr.getTransaction() == null) {
						txMgr.begin();
						begin = true;
					}
					sipResourceAdaptor.getSleeEndpoint()
							.activityStartedSuspended(SAH);
				} catch (Exception e) {
					logger.error("getNewDialog(" + transaction + ")", e);
				} finally {
					if (begin)
						try {
							txMgr.commit();
						} catch (SecurityException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalStateException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (RollbackException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (HeuristicMixedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (HeuristicRollbackException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (SystemException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				}

			return DW;
		}
	}

	/**
	 * getSipStack
	 * 
	 * @return SipStack
	 */
	public SipStack getSipStack() {
		return stack;
	}

	/**
	 * removeSipListener
	 * 
	 * @param sipListener
	 *            SipListener
	 */
	public void removeSipListener(SipListener sipListener) {
		throw new java.lang.SecurityException();
	}

	/**
	 * sendRequest
	 * 
	 * @param request
	 *            Request
	 * @throws SipException
	 */
	public void sendRequest(Request request) throws SipException {
		provider.sendRequest(request);
		/*
		 * Iterator it=request.getHeaders(ViaHeader.NAME); ViaHeader
		 * via=(ViaHeader) it.next(); String branch=via.getBranch(); //IF
		 * ACTIITY EXISTS if(branch!=null) { String
		 * activityID=branch+"_"+request.getMethod(); Transaction
		 * t=(Transaction)sipResourceAdaptor.getActivities().get(activityID);
		 * //AND IT IS ServerTransaction if(t instanceof
		 * ServerTransactionWrapper) { //WE CAN ASSUME THAT REQUEST IS BEEING
		 * PROXIED ServerTransactionWrapper STW=(ServerTransactionWrapper)t;
		 * STW.proxied(); } }
		 */
	}

	/**
	 * sendResponse
	 * 
	 * @param response
	 *            Response
	 * @throws SipException
	 */
	public void sendResponse(Response response) throws SipException {
		provider.sendResponse(response);
	}

	/**
	 * setListeningPoint
	 * 
	 * @param listeningPoint
	 *            ListeningPoint
	 * @throws ObjectInUseException
	 */
	public void setListeningPoint(ListeningPoint listeningPoint)
			throws ObjectInUseException {
		throw new ObjectInUseException();
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
	 * @see javax.sip.SipProvider#addListeningPoint(javax.sip.ListeningPoint)
	 */
	public void addListeningPoint(ListeningPoint listeningPoint)
			throws ObjectInUseException, TransportAlreadySupportedException {

		throw new ObjectInUseException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.sip.SipProvider#removeListeningPoint(javax.sip.ListeningPoint)
	 */
	public void removeListeningPoint(ListeningPoint listeningPoint)
			throws ObjectInUseException {
		throw new ObjectInUseException();

	}

	public void setAutomaticDialogSupportEnabled(boolean booean) {
		this.provider.setAutomaticDialogSupportEnabled(true);

	}
}
