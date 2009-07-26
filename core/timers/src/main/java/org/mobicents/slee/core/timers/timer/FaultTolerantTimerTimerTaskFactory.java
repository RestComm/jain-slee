package org.mobicents.slee.core.timers.timer;

import java.util.FaultTolerantTimerTimerTask;

import org.mobicents.slee.core.timers.TimerTask;
import org.mobicents.slee.core.timers.TimerTaskData;

/**
 * 
 * @author martins
 *
 */
public class FaultTolerantTimerTimerTaskFactory implements org.mobicents.slee.core.timers.TimerTaskFactory {
	
	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.core.timers.TimerTaskFactory#newTimerTask(org.mobicents.slee.core.timers.TimerTaskData)
	 */
	public TimerTask newTimerTask(TimerTaskData data) {
		return new FaultTolerantTimerTimerTask((org.mobicents.slee.core.timers.timer.FaultTolerantTimerTimerTaskData) data);
	}

}
