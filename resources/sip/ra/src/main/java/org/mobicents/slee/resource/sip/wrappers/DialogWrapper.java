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

import java.util.Iterator;
import java.util.Timer;

import javax.sip.ClientTransaction;
import javax.sip.Dialog;
import javax.sip.DialogDoesNotExistException;
import javax.sip.DialogState;
import javax.sip.InvalidArgumentException;
import javax.sip.SipException;
import javax.sip.Transaction;
import javax.sip.TransactionDoesNotExistException;
import javax.sip.address.Address;
import javax.sip.header.CSeqHeader;
import javax.sip.header.CallIdHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.ToHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;
import javax.slee.AddressPlan;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.component.ComponentKey;
import org.mobicents.slee.resource.sip.SipActivityHandle;
import org.mobicents.slee.resource.sip.SipResourceAdaptor;
import org.mobicents.slee.resource.sip.SipToSLEEUtility;

/**
 * 
 * Wraps SIP Dialog.
 * 
 * @author M. Ranganathan
 * @author B. Baranowski
 * @author eduardomartins
 */
public class DialogWrapper implements javax.sip.Dialog {

	private static Logger logger = Logger.getLogger(DialogWrapper.class);
	// private static Logger logger=Logger.getLogger(SipResourceAdaptor.class);
	// DIALOGS apData IS USED TO STORE THIS OBJECT, WE HAVE TO PROVIDE SOMETHING
	// IN EXCHANGE
	private Object applicationData = null;
	// LOCAL ID WHICH WILL BE INTRODUCED TO USER << IT BYPASSES SIPimpl
	// WHICH RETURNS NULL IF DIALOG IS BEFOR EOR IN EARLY STATE (WHEN IT DIDNT
	// RECEIVE totag)
	private String localID = null;
	// REAL DIALOG WHICH IS WRAPED BY THIS OBJECT
	protected Dialog realDialog = null;
	// LAST STATE OF realDialog, THIS VAR HELPS US TO DETERMINE WHEN STATE
	// EVENTS SHOULD BE FIRED
	private DialogState lastState = null;
	// LETS DOUBLE FLAG FOR TerminateOnBye - default value is false
	private boolean termianteOnBye = false;

	private SipResourceAdaptor sipResourceAdaptor;

	private static long dialogTimeout = 360000;

	private boolean hasTimedOut = false;

	// Timer for dialog idle timeouts
	private static Timer dialogTimer = new Timer();

	private DialogTimeoutTimerTask timerTask;

	private SipActivityHandle activityHandle;

	private boolean bothTagsPresent = false;

	private int pendingStateEvents = 0;

	private boolean hasBeenCanceled = false;

	public DialogWrapper(Dialog realDialog,
			SipResourceAdaptor sipResourceAdaptor) {

		this.realDialog = realDialog;

		// WE NEED SOME PLACE WHERE WE CAN STORE WRAPPER AND RETRIEVE IT IN
		// CONVENIANT WAY
		try {
			realDialog.setApplicationData(this);

			localID = (realDialog.getLocalParty().toString() + ":"
					+ realDialog.getLocalTag() + ":"
					+ realDialog.getRemoteParty().toString() + ":"
					+ realDialog.getRemoteTag() + ":" + realDialog.getCallId())
					.trim();
			bothTagsPresent = (realDialog.getLocalTag() != null && realDialog
					.getRemoteTag() != null);
			lastState = realDialog.getState();

			timerTask = new DialogTimeoutTimerTask(this, sipResourceAdaptor);
			this.dialogTimer.schedule(timerTask, dialogTimeout);

			this.sipResourceAdaptor = sipResourceAdaptor;
			this.activityHandle = new SipActivityHandle(this.getDialogId(),true);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public Dialog getRealDialog() {
		return realDialog;
	}

	public Address getLocalParty() {

		return realDialog.getLocalParty();
	}

	public Address getRemoteParty() {
		return realDialog.getRemoteParty();
	}

	public Address getRemoteTarget() {

		return realDialog.getRemoteTarget();
	}

	public String getDialogId() {

		return localID;
	}

	public CallIdHeader getCallId() {
		return realDialog.getCallId();
	}

	public int getLocalSequenceNumber() {
		return realDialog.getLocalSequenceNumber();
	}

	public int getRemoteSequenceNumber() {
		return realDialog.getRemoteSequenceNumber();
	}

	public long getLocalSeqNumber() {

		return realDialog.getLocalSeqNumber();
	}

	public long getRemoteSeqNumber() {
		// TODO Auto-generated method stub
		return realDialog.getRemoteSeqNumber();
	}

	public Iterator getRouteSet() {
		return realDialog.getRouteSet();
	}

	public boolean isSecure() {
		return realDialog.isSecure();
	}

	public boolean isServer() {
		return realDialog.isServer();
	}

	public void incrementLocalSequenceNumber() {
		realDialog.incrementLocalSequenceNumber();

	}

	public Request createRequest(String arg0) throws SipException {
		return realDialog.createRequest(arg0);
	}

	public Response createReliableProvisionalResponse(int arg0)
			throws InvalidArgumentException, SipException {
		return realDialog.createReliableProvisionalResponse(arg0);
	}

	public void sendRequest(ClientTransaction clientTransaction)
			throws TransactionDoesNotExistException, SipException {
		// unwrap and send request on the real client transaction
		realDialog
				.sendRequest((ClientTransaction) ((ClientTransactionWrapper) clientTransaction)
						.getRealTransaction());
		this.renew();
	}

	public void sendReliableProvisionalResponse(Response arg0)
			throws SipException {

		if (logger.isDebugEnabled()) {
			logger.debug("------- SENDING ReliableProvisionalResponse ------\n"
					+ arg0 + "-------------------------------");
		}
		realDialog.sendReliableProvisionalResponse(arg0);
		this.renew();
		// HERE WE HAVE TO CHECK FOR DIALOG STATE CHANGE

		if (logger.isDebugEnabled()) {
			logger
					.debug("\n-----------------------------------------\nOld State: "
							+ lastState
							+ "\nNew State: "
							+ this.getState()
							+ "\n--------------------------------------");
		}

		this.fireDialogStateEvent(arg0);

	}

	public Request createPrack(Response arg0)
			throws DialogDoesNotExistException, SipException {

		return realDialog.createPrack(arg0);
	}

	public Request createAck(long arg0) throws InvalidArgumentException,
			SipException {

		return realDialog.createAck(arg0);
	}

	public void sendAck(Request arg0) throws SipException {

		realDialog.sendAck(arg0);
		this.renew();

	}

	public DialogState getState() {
		return realDialog.getState();
	}

	public void delete() {
		realDialog.delete();
		this.cancel();
	}

	public Transaction getFirstTransaction() {

		return realDialog.getFirstTransaction();
	}

	public String getLocalTag() {

		return realDialog.getLocalTag();
	}

	public String getRemoteTag() {

		return realDialog.getRemoteTag();
	}

	public void setApplicationData(Object applicaionData) {
		this.applicationData = applicaionData;

	}

	public Object getApplicationData() {

		return this.applicationData;
	}

	public void terminateOnBye(boolean terminate) throws SipException {
		this.termianteOnBye = terminate;
		realDialog.terminateOnBye(terminate);

	}

	public boolean getTerminateOnBye() {
		return this.termianteOnBye;
	}

	public DialogState getLastState() {
		return lastState;
	}

	public void setLastState(DialogState lastState) {
		this.lastState = lastState;
	}

	public String toString() {
		return "[DialogW   Handler[" + this.activityHandle + "] LOCALID["
				+ localID + "] DESC[" + super.toString() + "] WRAPPED["
				+ realDialog + "] STATE[" + realDialog.getState() + "] REALID["
				+ realDialog.getDialogId() + "]]";
	}

	/**
	 * Renews Session timer - resets ETA of dialog timeout event.
	 * 
	 */
	public void renew() {
		if (logger.isDebugEnabled()) {
			logger.debug("Renewing timeout task for dialog "
					+ this.getDialogId());
		}
		if (timerTask != null) {
			timerTask.cancel();

			DialogTimeoutTimerTask newTimerTask = new DialogTimeoutTimerTask(
					this, sipResourceAdaptor);
			dialogTimer.schedule(newTimerTask, dialogTimeout);
			timerTask = newTimerTask;
		}
	}

	/**
	 * Cancels session timer. Timeout evet wont be fired.
	 * 
	 */
	public boolean cancel() {
		if (logger.isDebugEnabled()) {
			logger.debug("[CANCEL TIMER]Cancelling timeout task for dialog "
					+ this.getDialogId());
		}

		boolean returnValue = false;

		if (timerTask != null) {
			returnValue = timerTask.cancel();

			if (returnValue == true && hasBeenCanceled == false) {
				hasBeenCanceled = true;
			}

			return returnValue;
		} else {

			return false;
		}
	}

	public boolean hasBeenCanceled() {
		return hasBeenCanceled;
	}

	public void setHasTimedOut() {
		this.hasTimedOut = true;

	}

	public boolean getHasTimedOut() {
		return this.hasTimedOut;
	}

	/**
	 * Checks if state event for dialog should be fired.
	 * 
	 * @see org.mobicents.slee.resource.sip.wrappers.DialogWrapper#getKeyFor1_2DialogState(String
	 *      method, int statusCode)
	 * @param response
	 * @return
	 *            <ul>
	 *            <li><b>true</b> if state event has been fired
	 *            <li><b>false</b>if state event has not been fired
	 *            </ul>
	 */
	public boolean fireDialogStateEvent(javax.sip.message.Response response) {

		try {
			if (logger.isDebugEnabled()) {
				logger
						.debug("\n---------------------------------------------\nSTATE:"
								+ getState()
								+ "\nOLD:"
								+ getLastState()
								+ "\n---------------------------------------------");
			}

			final String sipMethod = ((CSeqHeader) response
					.getHeader(CSeqHeader.NAME)).getMethod();

			ComponentKey key = this
					.getKeyFor1_2DialogState(sipMethod, response);

			if (key == null) {
				if (logger.isDebugEnabled()) {
					logger
							.debug("------------------ NO STATE EVENT HAS TO BE FIRED ----------------");
				}

				return false;// WE DONT HAVE TO FIRE ANYTHING, STATE EVENTS
				// ARE
				// HANDLED
				// ELSWHERE OR NONE HAS TO BE FIRED
			} else {
				boolean result = false;
				if (!sipResourceAdaptor.isEventGoingToBereceived(key)) {
					 if (logger.isDebugEnabled()) {
					logger.debug("------------------ STATE EVENT[" + key
							+ "] FOR DIALOG[" + getDialogId()
							+ "] WONT BE RECEIVED, DROPING ----------------");
					 }
				} else {
					SipToSLEEUtility.displayMessage("Wrapper["
							+ this.getClass() + "]", "Looking up event", key);

					int eventID = -1;
					try {
						eventID = sipResourceAdaptor.getBootstrapContext()
								.getEventLookupFacility().getEventID(
										key.getName(), key.getVendor(),
										key.getVersion());
					} catch (Exception e2) {
						e2.printStackTrace();
					}
					if (eventID == -1) {
						SipToSLEEUtility.displayMessage("Wrapper["
								+ this.getClass() + "]",
								"Event is not a a registared event type", key);
					} else {
						SipToSLEEUtility.displayDeliveryMessage("Wrapper["
								+ this.getClass() + "]", eventID, key,
								getDialogId());

						// NOW SOME LITTLE CHEAT, THIS WILL BE FIRED INSTEAD
						// ORIGINAL
						// RequestEvent, it should provide
						// sbb with conveniant way of
						// getting what it needs, no more magic, each wrapper
						// extends class it
						// wrapps.
						// We just take care of proper initialization
						
						if (!bothTagsPresent
								&& (key.getName().endsWith("SetupEarly") || key
										.getName().endsWith("SetupConfirmed"))) {
							bothTagsPresent = (realDialog.getLocalTag() != null && realDialog
									.getRemoteTag() != null);

							if (bothTagsPresent) {
								localID = (realDialog.getLocalParty()
										.toString()
										+ ":"
										+ realDialog.getLocalTag()
										+ ":"
										+ realDialog.getRemoteParty()
												.toString()
										+ ":"
										+ realDialog.getRemoteTag() + ":" + realDialog
										.getCallId()).trim();
								//SipActivityHandle oldSAH = new SipActivityHandle(
								//		this.getActivityHandle().getID());
								this.activityHandle.update(localID);
								//this.sipResourceAdaptor
								//		.updateActivityReference(oldSAH,
								//				this.activityHandle, this);

							}

						}

						// CTX IS NULL SINCE RESPONSE CAN BE OUR RESPONSE - IN
						// THIS CASE WE HAVE
						// ONLY STX, NOT CTX
						try {
							sipResourceAdaptor
									.getBootstrapContext()
									.getSleeEndpoint()
									.fireEvent(
											this.getActivityHandle(),
											new ResponseEventWrapper(
													this.sipResourceAdaptor
															.getSipFactoryProvider(),
													null, this, response),
											eventID,
											new javax.slee.Address(
													AddressPlan.SIP,
													((ToHeader) response
															.getHeader(ToHeader.NAME))
															.getAddress()
															.toString()));

							result = true;
						} catch (Exception e) {
							e.printStackTrace();
						}

					}

				}

				try {
					// setup failed and setup timeout end the activity
					// if (key.getName().equals("javax.sip.dialog.SetupFailed")
					// || key.getName().equals(
					// "javax.sip.dialog.SetupTimeout")) {
					// sipResourceAdaptor.sendActivityEndEvent(this);
					// }
				} catch (Exception e) {
					e.printStackTrace();
				}
				return result;
			}
		} finally {
			// if(isInStateEventFireSequence() &&
			// DialogState.TERMINATED.equals(realDialog.getState()))
			// sipResourceAdaptor.sendActivityEndEvent(this);
			endStateEventFireSequence();
		}

	}

	public static void setDialogTimeout(long timeout) {

		dialogTimeout = timeout;
	}

	/**
	 * Checks if DTE should be fired - in cases when dialog state is null
	 * (SetupEarly state) stack doesnt fire DTE!!!
	 */
	private boolean fireDTE(String method, int statusCode) {

		/*
		 * if (logger.isDebugEnabled()) { logger.debug(" == fireDTE> METHOD:" +
		 * method + " StatusCODE:" + statusCode + " =="); logger
		 * .debug("COND:STATE[" + getState() + "] Last state[" + lastState + "] (" +
		 * (lastState == null || lastState.getValue() == DialogState.EARLY
		 * .getValue()) + ")(" + (getState() == null || getState() .getValue() ==
		 * DialogState.EARLY.getValue()) + ") Method[" + method + "] Status[" +
		 * statusCode + "]"); }
		 * 
		 * if ((lastState == null || lastState.getValue() == DialogState.EARLY
		 * .getValue()) && (getState() == null || getState().getValue() ==
		 * DialogState.EARLY .getValue()) && method.equals(Request.CANCEL) &&
		 * statusCode == Response.OK) {
		 * 
		 * return true; } else {
		 * 
		 * return false; }
		 */
		return false;

	}

	/**
	 * 
	 * @param DW
	 *            Dialog wrapper of dialgo for which this method will check if
	 *            state event should be fired, <b>SetupTimedOut</b> and
	 *            <b>DialogTerminated</b> are events that wont be returned here
	 *            as they are bound to specific conditions - TxTimeout and
	 *            DialogTermination.
	 * @param method -
	 *            method for which we will compute state event; As for now it
	 *            can be null
	 * @param statusCode -
	 *            status code of event; As for now it is ignored.
	 * @return
	 *            <ul>
	 *            <li><b>null</b> - if no status message has to be fired
	 *            <li><b>key</b>- ComponentKey of status message that has to
	 *            be fired:
	 *            <ul>
	 *            <li>SetupEarly
	 *            <li>SetupConfirmed
	 *            <li>SetupFailed
	 *            </ul>
	 */
	public ComponentKey getKeyFor1_2DialogState(String method,
			javax.sip.message.Response response) {
		int statusCode = response.getStatusCode();
		ComponentKey key = null;

		if (logger.isDebugEnabled()) {
			logger.debug(" == METHOD:" + method + "  StatusCODE:" + statusCode
					+ " ==");
		}

		DialogState localLastState = getLastState(); // aka this.lastState
		if (getLastState() != getState()) {
			int stateToSwitch = -1;

			// Status code: 99<statusCode<200
			if (statusCode < 200 && statusCode > 99) {
				if (getState().getValue() == DialogState._CONFIRMED
						|| getState().getValue() == DialogState._TERMINATED) {
					// This means we got provisional response and ok/bye without
					// time to process first one
					if (((ToHeader) response.getHeader(ToHeader.NAME)).getTag() != null
							&& ((FromHeader) response
									.getHeader(FromHeader.NAME)).getTag() != null) {

						stateToSwitch = DialogState._EARLY;
					} else {

						setLastState(getState());
						stateToSwitch = getState().getValue();
					}
				} else {

					setLastState(getState());
					stateToSwitch = getState().getValue();
				}
			} else {
		
				setLastState(getState());
				stateToSwitch = getState().getValue();
			}
			// WE NEED SOME MAGIC TO HAPPEN
			switch (stateToSwitch) {
			case DialogState._EARLY:
				key = new ComponentKey(
						SipResourceAdaptor.EVENT_DIALOG_STATE_SetupEarly,
						SipResourceAdaptor.VENDOR_1_2,
						SipResourceAdaptor.VERSION_1_2);
			
				break;
			case DialogState._CONFIRMED:
				key = new ComponentKey(
						SipResourceAdaptor.EVENT_DIALOG_STATE_SetupConfirmed,
						SipResourceAdaptor.VENDOR_1_2,
						SipResourceAdaptor.VERSION_1_2);
		
				break;
			case DialogState._TERMINATED:
				// logger.info("-----> 11");
				// LAST STATE SHOULDNT BE NULL, BUT JUST IN CASE
				// method==INVITE is here because if CANCEL is fired as soon as
				// INVITE -states will
				// change in Dialog since different thread does that but method
				// will be invite
				// IS THIS A BUG ?
				if ((((localLastState == null) || (lastState == null)) || ((localLastState
						.getValue() == DialogState._EARLY) || (lastState
						.getValue() == DialogState._EARLY)))
						&& (method.equals(Request.CANCEL) || method
								.equals(Request.INVITE))) {
		
					key = new ComponentKey(
							SipResourceAdaptor.EVENT_DIALOG_STATE_SetupFailed,
							SipResourceAdaptor.VENDOR_1_2,
							SipResourceAdaptor.VERSION_1_2);
				}
				break;
			}
		}

		 if (logger.isDebugEnabled()) {
		logger.debug("\n===================\nSTATE KEY:" + key
				+ "\n===================");
		// THIS IS THE CASE WHERE DIALGO STATE WAS NULL AND INVITE WAS
		// CANCELED
		// THIS CAN HAPPEN WHEN NO PROVISIONAL RESPONSE WITH TAG HAS BEEN
		// SENT
		// OR RECEIVED
		/*
		 * -------------------------------- HACK
		 * --------------------------------
		 */
		logger
				.debug("COND:STATE["
						+ getState()
						+ "] Last state["
						+ localLastState
						+ "] ("
						+ (localLastState == null || localLastState.getValue() == DialogState._EARLY)
						+ ")("
						+ (getState() == null || getState().getValue() == DialogState._EARLY)
						+ ") Method[" + method + "] Status[" + statusCode + "]");

		 }

		if (key == null
				&& (localLastState == null || localLastState.getValue() == DialogState._EARLY)
				&& (getState() == null || getState().getValue() == DialogState._EARLY)
				&& method.equals(Request.CANCEL) && statusCode == Response.OK) {
			key = new ComponentKey(
					SipResourceAdaptor.EVENT_DIALOG_STATE_SetupFailed,
					SipResourceAdaptor.VENDOR_1_2,
					SipResourceAdaptor.VERSION_1_2);

		}
	
		 if (logger.isDebugEnabled()) {
		logger.debug("\n===================\nRETURNING STATE KEY:" + key
				+ "\nFOR:" + method + "\nSTATUS:" + statusCode
				+ "\n===================");
		 }

		return key;

	}

	@Override
	protected void finalize() throws Throwable {

		super.finalize();

		try {
			realDialog.setApplicationData(null);
			realDialog = null;
			timerTask.cancel();
		} catch (Exception e) {

		}
	}

	public SipActivityHandle getActivityHandle() {
		return this.activityHandle;
	}

	public synchronized void startStateEventFireSequence() {

		this.pendingStateEvents++;

	}

	public synchronized void endStateEventFireSequence() {
		this.pendingStateEvents--;
		if (this.pendingStateEvents < 0)
			this.pendingStateEvents = 0;

	}

	public synchronized boolean isInStateEventFireSequence() {
;
		return this.pendingStateEvents != 0;
	}

}
