/**
 * 
 */
package org.mobicents.slee.runtime.facilities;

import org.mobicents.slee.container.event.EventUnreferencedCallback;

/**
 * Cancels the timer after the event is routed.
 * @author martins
 *
 */
public class CancelTimerEventProcessingCallbacks implements EventUnreferencedCallback {

	final TimerFacilityTimerTask  task;
		
	/**
	 * @param timerFacilityImpl
	 * @param timerID
	 */
	public CancelTimerEventProcessingCallbacks(TimerFacilityTimerTask  task) {
		this.task = task;
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
		task.remove();			
	}
}
