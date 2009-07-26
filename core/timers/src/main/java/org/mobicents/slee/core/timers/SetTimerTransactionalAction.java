package org.mobicents.slee.core.timers;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.mobicents.slee.runtime.transaction.TransactionalAction;

/**
 * {@link TransactionalAction} to set a timer task after the tx commits.
 * 
 * @author martins
 *
 */
public class SetTimerTransactionalAction implements TransactionalAction {

	private static final Logger logger = Logger
			.getLogger(SetTimerTransactionalAction.class);

	private final TimerTask task;

	private final FaultTolerantScheduler scheduler;

	private boolean canceled = false;

	SetTimerTransactionalAction(TimerTask task,
			FaultTolerantScheduler scheduler) {
		this.task = task;
		this.scheduler = scheduler;
	}

	public void execute() {
		
		task.setSetTimerTransactionalAction(null);
		
		if (!canceled) {
				
			final TimerTaskData taskData = task.getData();
			// calculate delay
			long delay = taskData.getStartTime() - System.currentTimeMillis();
			if (delay < 0) {
				delay = 0;
			}
			
			try {
				// schedule runnable
				if (taskData.getPeriod() < 0) {
					if (logger.isDebugEnabled()) {
						logger.debug("Scheduling one-shot timer with id "
								+ task.getData().getTaskID());
					}
					task.setScheduledFuture(scheduler.getExecutor().schedule(task, delay, TimeUnit.MILLISECONDS));
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("Scheduling periodic timer with id "
								+ task.getData().getTaskID());
					}
					if (taskData.getPeriodicScheduleStrategy() == PeriodicScheduleStrategy.withFixedDelay) {
						task.setScheduledFuture(scheduler.getExecutor().scheduleWithFixedDelay(task, delay, taskData.getPeriod(),TimeUnit.MILLISECONDS));
					}
					else {
						// default
						task.setScheduledFuture(scheduler.getExecutor().scheduleAtFixedRate(task, delay, taskData.getPeriod(),TimeUnit.MILLISECONDS));
					}					
				}		
			} catch (Throwable e) {
				logger.error(e.getMessage(), e);
				scheduler.remove(taskData.getTaskID());
			}
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("Canceled scheduling periodic timer with id "
						+ task.getData().getTaskID());
			}
		}
	}

	public void cancel() {
		if (logger.isDebugEnabled()) {
			logger.debug("Canceling set timer action for task with timer id "+task.getData().getTaskID());
		}
		canceled = true;
		scheduler.remove(task.getData().getTaskID());
	}

}
