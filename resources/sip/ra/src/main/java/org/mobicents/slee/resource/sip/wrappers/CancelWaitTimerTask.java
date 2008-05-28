package org.mobicents.slee.resource.sip.wrappers;

import java.text.ParseException;
import java.util.TimerTask;

import javax.sip.InvalidArgumentException;
import javax.sip.SipException;
import javax.sip.TransactionState;
import javax.sip.header.FromHeader;
import javax.sip.header.ToHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;
import javax.slee.Address;
import javax.slee.AddressPlan;
import javax.slee.UnrecognizedActivityException;
import javax.slee.UnrecognizedEventException;
import javax.slee.facilities.EventLookupFacility;
import javax.slee.facilities.FacilityException;
import javax.slee.resource.ActivityIsEndingException;
import javax.slee.resource.SleeEndpoint;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.component.ComponentKey;
import org.mobicents.slee.resource.sip.SipActivityHandle;
import org.mobicents.slee.resource.sip.SipFactoryProvider;
import org.mobicents.slee.resource.sip.SipResourceAdaptor;
import org.mobicents.slee.resource.sip.SipToSLEEUtility;

public class CancelWaitTimerTask extends TimerTask {

	private ServerTransactionWrapper inviteTX, cancelTX;

	private static ComponentKey key = new ComponentKey(
			SipResourceAdaptor.EVENT_REQUEST_CANCEL_1_1,
			SipResourceAdaptor.VENDOR_1_1, SipResourceAdaptor.VERSION_1_1);
	
	private Logger log = null;

	private SipFactoryProvider sipFactoryProvider;

	private SleeEndpoint sleeEndpoint = null;

	private EventLookupFacility eventLookup = null;

	private RequestEventWrapper cancel = null;
	private boolean run=false;
	
	public CancelWaitTimerTask(RequestEventWrapper cancel,
			ServerTransactionWrapper inviteTransaction,
			SipFactoryProvider provider, SleeEndpoint endpoint,
			EventLookupFacility eventL, Logger logger) {
		this.inviteTX = inviteTransaction;
		this.cancelTX = (ServerTransactionWrapper) cancel
				.getServerTransaction();
		this.log = logger;
		this.sipFactoryProvider = provider;
		this.cancel = cancel;
		this.eventLookup = eventL;
		this.sleeEndpoint = endpoint;
	}
	public synchronized boolean hasRun()
	{
		return run;
	}
	public void run() {
		synchronized(this)
		{
			run=true;
		}
		// TODO Auto-generated method stub
		int txState = inviteTX.getState().getValue();
		if ((txState == TransactionState._TERMINATED)
				|| (txState == TransactionState._COMPLETED)
				|| (txState == TransactionState._CONFIRMED)) {
			
			if (log.isDebugEnabled()) {
			log
					.debug("================================================== \nFINAL RESPONSE HAS BEEN SENT FOR TX, DROPPING CANCEL(CANCEL TIMER TASK): \n"
							+ inviteTX
							+ "\n==================================================");
			}
			// FINAL
			// FINAL RESPONSE
			// HAS
			// BEEN
			// SENT?
			return;
		}
		Response requestTerminatedResponse;
		DialogWrapper dialog = (DialogWrapper) inviteTX.getDialog();
		if (dialog != null) {
			// WE HAVE TO FIRE 487

			// AND CANCEL TIMEOUT
			dialog.cancel();
			try {
				requestTerminatedResponse = sipFactoryProvider
						.getMessageFactory().createResponse(
								Response.REQUEST_TERMINATED,
								inviteTX.getRequest());
				inviteTX.sendResponse(requestTerminatedResponse);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SipException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} //else {
			//String idForSipHandle = inviteTX.getBranchId() + "_"
			//		+ Request.INVITE;
			SipActivityHandle SAH=inviteTX.getActivityHandle();
			Request request = cancelTX.getRequest();
			javax.slee.Address address;
			FromHeader fromHeader = (FromHeader) request
					.getHeader(FromHeader.NAME);
			ToHeader toHeader = (ToHeader) request.getHeader(ToHeader.NAME);
			address = new Address(AddressPlan.SIP, toHeader.getAddress()
					.toString());

			SipToSLEEUtility.displayMessage(this.getClass().getName(), "looking up event", key);
			
			
			int eventID;
			try {
				eventID = eventLookup.getEventID(key.getName(),
						key.getVendor(), key.getVersion());
			} catch (FacilityException e2) {
				e2.printStackTrace();
				throw new RuntimeException("Failed to lookup		  event!", e2);
			} catch (UnrecognizedEventException e2) {
				e2.printStackTrace();
				throw new RuntimeException("Failed to lookup  event!", e2);
			}

			if (eventID == -1) { // Silently drop the message because this is
				// not
				// a registered event
				// type.
				SipToSLEEUtility.displayMessage(this.getClass().getName(), "Event is not a a registared event type:", key);
				
				return;
			}
			
			SipToSLEEUtility.displayDeliveryMessage(this.getClass().getName(), eventID, key, SAH);
			
			Object eventObj = cancel;

			try {

				sleeEndpoint.fireEvent(SAH,
						eventObj, eventID, address);
				//WHY IT IS NOT TERMINATED BY ITSELF ??
				//inviteTX.terminate();
			} catch (IllegalStateException e) {
				log.error("Unexpected exception ", e);
				e.printStackTrace();
			} catch (ActivityIsEndingException e) {
				e.printStackTrace();
			} catch (UnrecognizedActivityException e) {
				e.printStackTrace();
			//} catch (ObjectInUseException e) {
				// TODO Auto-generated catch block
			//	e.printStackTrace();
			}
		//}

	}

}
