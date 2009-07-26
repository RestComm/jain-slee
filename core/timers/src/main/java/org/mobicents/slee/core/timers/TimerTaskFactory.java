package org.mobicents.slee.core.timers;

/**
 * Interface for a factory of concrete {@link TimerTask} instances, to be provided to an {@link FaultTolerantScheduler}.
 * 
 * @author martins
 *
 */
public interface TimerTaskFactory {
	
	/**
	 * Creates a new instance of a concrete {@link TimerTask}, using the specified {@link TimerTaskData}.
	 * 
	 * @param data
	 * @return
	 */
	public TimerTask newTimerTask(TimerTaskData data);
}
