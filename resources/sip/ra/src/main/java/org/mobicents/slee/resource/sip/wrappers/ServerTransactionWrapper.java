/*
 * Mobicents: The Open Source SLEE Platform      
 *
 * Copyright 2003-2005, CocoonHive, LLC., 
 * and individual contributors as indicated
 * by the @authors tag. See the copyright.txt 
 * in the distribution for a full listing of   
 * individual contributors.
 *
 * This is free software; you can redistribute it
 * and/or modify it under the terms of the 
 * GNU Lesser General Public License as
 * published by the Free Software Foundation; 
 * either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that 
 * it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR 
 * PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the 
 * GNU Lesser General Public
 * License along with this software; 
 * if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, 
 * Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site:
 * http://www.fsf.org.
 */

package org.mobicents.slee.resource.sip.wrappers;

import java.util.Timer;

import javax.sip.Dialog;
import javax.sip.InvalidArgumentException;
import javax.sip.ObjectInUseException;
import javax.sip.ServerTransaction;
import javax.sip.SipException;
import javax.sip.Transaction;
import javax.sip.TransactionState;
import javax.sip.message.Request;
import javax.sip.message.Response;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.resource.sip.SipActivityHandle;
import org.mobicents.slee.resource.sip.SipResourceAdaptor;

/**
 * 
 * Wraps SIP ServerTransaction.
 * 
 * @author M. Ranganathan
 * @author B. Baranowski
 */
public class ServerTransactionWrapper implements ServerTransaction,
		SecretWrapperInterface {

	private static Logger logger = Logger
			.getLogger(ServerTransactionWrapper.class);
	// REAL TX WHICH IS WRAPPED IN THIS CLASS
	private ServerTransaction realTransaction = null;

	// TXs APPLICATION DATA IS USED TO STORE THIS OBJECT IN TX, SO WE HAVE TO
	// PROVIDE SOMETHING IN RETURN
	private Object applicationData = null;

	// private boolean proxied=false;
	private SleeContainer serviceContainer = null;

	// THIS IS FOR INVITES CANCEL
	private RequestEventWrapper inviteCANCEL = null;
	private CancelWaitTimerTask cancelTimerTask = null;
	private DialogWrapper dialogWrapper;

	private SipActivityHandle activityHandle = null;

	private static long cancelWait = 5000;

	// Timer which postpones cancel firing into slee
	private static Timer cancelTimer = new Timer();

	public static void setCancelWait(long timeout) {
		cancelWait = timeout;
	}

	private SipResourceAdaptor sipResourceAdaptor;

	public ServerTransactionWrapper(ServerTransaction ST,
			DialogWrapper dialogWrapper, SipResourceAdaptor sipResourceAdaptor) {
		realTransaction = ST;
		this.dialogWrapper = dialogWrapper;
		// STORE THIS OBJECT IN TX
		ST.setApplicationData(this);
		this.sipResourceAdaptor = sipResourceAdaptor;
		serviceContainer = SleeContainer.lookupFromJndi();
		realTransaction.setApplicationData(this);
		activityHandle = new SipActivityHandle(ST.getBranchId() + "_"
				+ ST.getRequest().getMethod());
	}

	public Transaction getRealTransaction() {
		return realTransaction;
	}

	public void setDialogWrapper(DialogWrapper dialogWrapper) {
		this.dialogWrapper = dialogWrapper;
	}

	public void sendResponse(Response arg0) throws SipException,
			InvalidArgumentException {

		boolean gotToFire = false;
		try {
			if (this.dialogWrapper != null)
				this.dialogWrapper.startStateEventFireSequence();
			this.realTransaction.sendResponse(arg0);
			gotToFire = true;
			// HERE WE HAVE TO CHECK STATE CHANGE OF A DIALOG
			if (this.dialogWrapper == null)
				return;

			if (logger.isDebugEnabled()) {
				logger
						.debug("\n---------------------------------------\nSENDING RESPONSE:\n"
								+ arg0
								+ "\n---------------------------------------");
			}

			if (logger.isDebugEnabled()) {
				logger
						.debug("\n-----------------------------------------\nOld State: "
								+ ((DialogWrapper) dialogWrapper)
										.getLastState()
								+ "\nNew State: "
								+ dialogWrapper.getState()
								+ "\n--------------------------------------");
			}

			this.dialogWrapper.fireDialogStateEvent(arg0);
			
		} finally {
			if (!gotToFire)
				this.dialogWrapper.endStateEventFireSequence();
		}

	}

	public void enableRetransmissionAlerts() throws SipException {
		realTransaction.enableRetransmissionAlerts();

	}

	public Dialog getDialog() {

		return dialogWrapper;
	}

	public TransactionState getState() {

		return realTransaction.getState();
	}

	public int getRetransmitTimer() throws UnsupportedOperationException {

		return realTransaction.getRetransmitTimer();
	}

	public void setRetransmitTimer(int arg0)
			throws UnsupportedOperationException {
		realTransaction.setRetransmitTimer(arg0);

	}

	public String getBranchId() {
		// LEAK BUG PATCH
		return realTransaction.getBranchId();
		// return
		// realTransaction.getBranchId()+"_"+realTransaction.getRequest().getMethod();
	}

	public Request getRequest() {

		return realTransaction.getRequest();
	}

	public void setApplicationData(Object arg0) {
		applicationData = arg0;

	}

	public Object getApplicationData() {

		return applicationData;
	}

	public void terminate() throws ObjectInUseException {
		// FIXME: DO WE NEED SOMETHING HERE?
		realTransaction.terminate();

	}

	public String toString() {
		return "[TransactionW WRAPPED[" + realTransaction + "] BRANCHID["
				+ realTransaction.getBranchId() + "] STATE["
				+ realTransaction.getState() + "] DIALOG{ " + dialogWrapper
				+ " } ]";
	}

	/**
	 * Set local cancel request. Also activats timer, after it expires cancel
	 * will be fired into slee.
	 * 
	 * @param cancel
	 */
	public void setCancel(RequestEventWrapper cancel) {
		if (logger.isDebugEnabled()) {
			logger.debug("\n XxX Setting cancel to:" + cancel + " for tx:"
					+ realTransaction.getBranchId() + "_"
					+ realTransaction.getRequest().getMethod());
		}

		this.inviteCANCEL = cancel;
		if (this.inviteCANCEL == null) {
			this.cancelTimerTask.cancel();
			this.cancelTimerTask = null;
		} else {
			this.cancelTimerTask = new CancelWaitTimerTask(this.inviteCANCEL,
					this, sipResourceAdaptor.getSipFactoryProvider(),
					sipResourceAdaptor.getSleeEndpoint(), sipResourceAdaptor
							.getBootstrapContext().getEventLookupFacility(),
					logger);
			this.cancelTimer.schedule(this.cancelTimerTask, this.cancelWait);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("\n XxX CANCEL SET");
			logger.debug("======= STAGE: \n" + inviteCANCEL + "\n"
					+ cancelTimerTask + "\n==========");
		}

	}

	/**
	 * Removes cancel timer, etc
	 * 
	 */
	public void processCancelOnEventProcessingFailed() {

		if (logger.isDebugEnabled()) {
			logger
					.debug("\n XxX processCancelOnEventProcessingFailed called for tx:"
							+ realTransaction.getBranchId()
							+ "_"
							+ realTransaction.getRequest().getMethod());
			logger.debug("======= STAGE: \n" + inviteCANCEL + "\n"
					+ cancelTimerTask + "\n==========");
		}

		if (this.inviteCANCEL == null)
			return;
		this.cancelTimerTask.cancel();
		if (!this.cancelTimerTask.hasRun())
			this.cancelTimerTask.run();
		this.cancelTimerTask = null;
		this.inviteCANCEL = null;
	}

	/**
	 * Removes cancel timer, etc
	 * 
	 */
	public void processCancelOnEventProcessingSucess() {

		if (logger.isDebugEnabled()) {
			logger
					.debug("\n XxX processCancelOnEventProcessingSucess() called for tx:"
							+ realTransaction.getBranchId()
							+ "_"
							+ realTransaction.getRequest().getMethod());
			logger.debug("======= STAGE: \n" + inviteCANCEL + "\n"
					+ cancelTimerTask + "\n==========");
		}

		if (this.inviteCANCEL == null)
			return;
		this.cancelTimerTask.cancel();
		if (!this.cancelTimerTask.hasRun())
			this.cancelTimerTask.run();
		this.cancelTimerTask = null;
		this.inviteCANCEL = null;
	}

	/**
	 * Removes cancel timer, etc
	 * 
	 */
	public void processCancelOnActivityEnd() {

		if (logger.isDebugEnabled()) {
			logger.debug("\n XxX processCancelOnActivityEnd() called for tx:"
					+ realTransaction.getBranchId() + "_"
					+ realTransaction.getRequest().getMethod());
			logger.debug("======= STAGE: \n" + inviteCANCEL + "\n"
					+ cancelTimerTask + "\n==========");
		}

		if (this.inviteCANCEL == null)
			return;
		this.cancelTimerTask.cancel();
		// this.cancelTimerTask.run();
		this.cancelTimerTask = null;
		this.inviteCANCEL = null;
	}

	public SipActivityHandle getActivityHandle() {

		return this.activityHandle;
	}
}
