package org.mobicents.slee.core.timers;

import java.util.concurrent.ScheduledFuture;

/**
 * The base class to implement a task to be scheduled and executed by an {@link FaultTolerantScheduler}.
 * 
 * @author martins
 *
 */
public abstract class TimerTask implements Runnable {
	
	/**
	 * the data associated with the task
	 */
	private final TimerTaskData data;
	
	/**
	 * the schedule future object that returns from the task scheduling
	 */
	private ScheduledFuture<?> scheduledFuture;
	
	/**
	 * the tx action to set the timer when the tx commits, not used in a non tx environment 
	 */
	private SetTimerTransactionalAction action;
	
	/**
	 * 
	 * @param data
	 */
	public TimerTask(TimerTaskData data) {
		this.data = data;
	}
	
	/**
	 * Retrieves the data associated with the task.
	 * @return
	 */
	public TimerTaskData getData() {
		return data;
	}
	
	/**
	 * Retrieves the tx action to set the timer when the tx commits, not used in a non tx environment.
	 * @return
	 */
	SetTimerTransactionalAction getSetTimerTransactionalAction() {
		return action;
	}

	/**
	 * Sets the tx action to set the timer when the tx commits, not used in a non tx environment.
	 * @param action
	 */
	void setSetTimerTransactionalAction(
			SetTimerTransactionalAction action) {
		this.action = action;
	}

	/**
	 * Retrieves the schedule future object that returns from the task scheduling.
	 * @return
	 */
	ScheduledFuture<?> getScheduledFuture() {
		return scheduledFuture;
	}
	
	/**
	 * Sets the schedule future object that returns from the task scheduling.
	 * @param scheduledFuture
	 */
	void setScheduledFuture(ScheduledFuture<?> scheduledFuture) {
		this.scheduledFuture = scheduledFuture;
	}
	
	/**
	 * The method executed by the scheduler
	 */
	public abstract void run();
	
	/**
	 * Invoked before a task is recovered, after fail over, by default simply adjust start time.
	 */
	public void beforeRecover() {
		final long now = System.currentTimeMillis();
		if (data.getStartTime() < now) {
			data.setStartTime(now);
		}
	}
	
}
