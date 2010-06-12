/**
 * 
 */
package org.mobicents.slee.runtime.facilities;

import javax.slee.facilities.TimerID;

import org.mobicents.slee.container.event.EventUnreferencedCallback;
import org.mobicents.slee.container.facilities.TimerFacility;

/**
 * Cancels the timer after the event is routed.
 * @author martins
 *
 */
public class CancelTimerEventProcessingCallbacks implements EventUnreferencedCallback {

	final TimerFacility  timerFacility;
	final TimerID timerID;
		
	/**
	 * @param timerFacilityImpl
	 * @param timerID
	 */
	public CancelTimerEventProcessingCallbacks(TimerFacility timerFacility,
			TimerID timerID) {
		this.timerFacility = timerFacility;
		this.timerID = timerID;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.container.event.EventUnreferencedCallback#acceptsTransaction()
	 */
	public boolean requiresTransaction() {
		return true;
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.container.event.EventUnreferencedCallback#eventUnreferenced()
	 */
	public void eventUnreferenced() {
		timerFacility.cancelTimerWithoutValidation(timerID);			
	}
}
