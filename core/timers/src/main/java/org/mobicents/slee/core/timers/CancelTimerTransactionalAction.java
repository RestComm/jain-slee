package org.mobicents.slee.core.timers;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.mobicents.slee.runtime.transaction.TransactionalAction;

/**
 * {@link TransactionalAction} to cancel a timer task after the tx commits.
 * @author martins
 *
 */
public class CancelTimerTransactionalAction implements TransactionalAction {

	private static final Logger logger = Logger.getLogger(CancelTimerTransactionalAction.class);
	
	private final FaultTolerantScheduler executor;	
	private TimerTask task;

	CancelTimerTransactionalAction(TimerTask task,FaultTolerantScheduler executor) {
		this.task = task;
		this.executor = executor;
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.runtime.transaction.TransactionalAction#execute()
	 */
	public void execute() {
		
		final TimerTaskData taskData = task.getData();
		final Serializable taskID = taskData.getTaskID();
		
		if (logger.isDebugEnabled()) {
			logger.debug("Cancelling timer task for timer ID "+taskID);
		}
		
		executor.getLocalRunningTasks().remove(taskID);
		
		try {
			task.getScheduledFuture().cancel(false);		
		}
		catch (Throwable e) {
			logger.error(e.getMessage(),e);
		}
	}
	
}
