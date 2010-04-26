package org.mobicents.slee.runtime.facilities;

import org.mobicents.timers.TimerTask;
import org.mobicents.timers.TimerTaskData;
import org.mobicents.timers.TimerTaskFactory;

/**
 * 
 * @author martins
 *
 */
public class TimerFacilityTimerTaskFactory implements TimerTaskFactory {

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.timers.TimerTaskFactory#newTimerTask(org.mobicents.timers.TimerTaskData)
	 */
	public TimerTask newTimerTask(TimerTaskData data) {
		return new TimerFacilityTimerTask((TimerFacilityTimerTaskData) data);
	}
	
}
