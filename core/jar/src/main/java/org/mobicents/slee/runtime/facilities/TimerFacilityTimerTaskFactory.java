package org.mobicents.slee.runtime.facilities;

import org.mobicents.slee.core.timers.TimerTask;
import org.mobicents.slee.core.timers.TimerTaskData;
import org.mobicents.slee.core.timers.TimerTaskFactory;

public class TimerFacilityTimerTaskFactory implements TimerTaskFactory {

	public TimerTask newTimerTask(TimerTaskData data) {
		return new TimerFacilityTimerTask((TimerFacilityTimerTaskData) data);
	}
	
}
