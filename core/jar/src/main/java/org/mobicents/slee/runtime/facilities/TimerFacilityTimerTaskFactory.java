package org.mobicents.slee.runtime.facilities;

import org.mobicents.timers.TimerTask;
import org.mobicents.timers.TimerTaskData;
import org.mobicents.timers.TimerTaskFactory;

public class TimerFacilityTimerTaskFactory implements TimerTaskFactory {

	public TimerTask newTimerTask(TimerTaskData data) {
		return new TimerFacilityTimerTask((TimerFacilityTimerTaskData) data);
	}
	
}
