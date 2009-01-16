package org.mobicents.slee.runtime.eventrouter.routingtask;

import org.apache.log4j.Logger;
import org.mobicents.slee.runtime.eventrouter.DeferredEvent;
import org.mobicents.slee.runtime.facilities.TimerEventImpl;
import org.mobicents.slee.runtime.facilities.TimerFacilityImpl;

/**
 * 
 * 
 * @author martins
 *
 */
public class TimerEventPostProcessor {

	private static final Logger logger = Logger.getLogger(TimerEventPostProcessor.class);
	
	/**
	 * 
	 * Procedures after a timer event has been routed.
	 * 
	 * @param deferredEvent
	 * @param timerFacilityImpl
	 */
	public void process(DeferredEvent deferredEvent, TimerFacilityImpl timerFacilityImpl) {

		// get timer task from event, and check if timer should be cancelled
		TimerEventImpl timerEventImpl = (TimerEventImpl) deferredEvent.getEvent();
		if (timerEventImpl.isLastTimerEvent()) {
			if (logger.isDebugEnabled()) {
				logger.debug("Last timer event routed, canceling timer. Event: "+deferredEvent);
			}
			timerFacilityImpl.cancelTimer(
					timerEventImpl.getTimerID());
		}	
	}
}
