package org.mobicents.slee.core.timers;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import javax.slee.SLEEException;

import org.apache.log4j.Logger;
import org.mobicents.slee.runtime.cache.TimerTasksCacheData;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;
import org.mobicents.slee.runtime.transaction.TransactionalAction;

/**
 * 
 * @author martins
 *
 */
public class FaultTolerantScheduler {

	private static final Logger logger = Logger.getLogger(FaultTolerantScheduler.class);
	
	/**
	 * 
	 */
	private final ScheduledThreadPoolExecutor executor;
	
	/**
	 * 
	 */
	private final TimerTasksCacheData cacheData;
	
	/**
	 * 
	 */
	private final SleeTransactionManager txManager;
	
	/**
	 * 
	 */
	private ConcurrentHashMap<Serializable, TimerTask> localRunningTasks = new ConcurrentHashMap<Serializable, TimerTask>();
	
	/**
	 * 
	 */
	private final TimerTaskFactory timerTaskFactory;
	
	/**
	 * 
	 * @param corePoolSize
	 * @param cacheData
	 * @param txManager
	 */
	public FaultTolerantScheduler(int corePoolSize, TimerTasksCacheData cacheData, SleeTransactionManager txManager, TimerTaskFactory timerTaskFactory) {
		this.executor = new ScheduledThreadPoolExecutor(corePoolSize);
		this.cacheData = cacheData;
		if (!cacheData.exists()) {
			cacheData.create();
		}
		this.txManager = txManager;
		this.timerTaskFactory = timerTaskFactory;
	}

	/**
	 * Retrieves
	 * @return
	 */
	public TimerTasksCacheData getCacheData() {
		return cacheData;
	}
	
	/**
	 * 
	 * @return
	 */
	public ScheduledThreadPoolExecutor getExecutor() {
		return executor;
	}
	
	/**
	 * 
	 * @return
	 */
	public ConcurrentHashMap<Serializable, TimerTask> getLocalRunningTasks() {
		return localRunningTasks;
	}
	
	/**
	 * Schedules the specified task.
	 * 
	 * @param task
	 */
	public void schedule(TimerTask task) {
		
		final TimerTaskData taskData = task.getData(); 
		final Serializable taskID = taskData.getTaskID();
		
		if (logger.isDebugEnabled()) {
			logger.debug("Scheduling task with id "+taskID);
		}
		
		// store the task and data
		cacheData.addTaskData(taskID, taskData);
		localRunningTasks.put(taskID, task);
		
		// schedule task
		SetTimerTransactionalAction setTimerAction = new SetTimerTransactionalAction(task, this);
		if (txManager != null) {
			TransactionalAction rollbackAction = new TransactionalAction() {				
				public void execute() {
					localRunningTasks.remove(taskID);					
				}
			};
			try {
				txManager.addAfterRollbackAction(rollbackAction);
				txManager.addAfterCommitAction(setTimerAction);
				task.setSetTimerTransactionalAction(setTimerAction);
			} catch (Throwable e) {
				remove(taskID);
				throw new SLEEException(e.getMessage(),e);
			}
		}
		else {
			setTimerAction.execute();
		}		
	}

	/**
	 * Cancels a local running task with the specified ID.
	 * 
	 * @param taskID
	 * @return the task canceled
	 */
	public TimerTask cancel(Serializable taskID) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("Canceling task with timer id "+taskID);
		}
		
		TimerTask task = localRunningTasks.get(taskID);
		if (task != null) {
			// remove task data
			cacheData.removeTaskData(taskID);

			final SetTimerTransactionalAction setAction = task.getSetTimerTransactionalAction();
			if (setAction != null) {
				// we have a tx action scheduled to run when tx commits, to set the timer, lets simply cancel it
				setAction.cancel();
			}
			else {
				// do cancellation
				TransactionalAction cancelAction = new CancelTimerTransactionalAction(task,this);
				if (txManager != null) {
					try {
						txManager.addAfterCommitAction(cancelAction);
					} catch (Throwable e) {
						throw new SLEEException(e.getMessage(),e);
					}
				}
				else {
					cancelAction.execute();
				}			
			}		
		}
		
		return task;
	}
	
	void remove(Serializable taskID) {
		localRunningTasks.remove(taskID);
		cacheData.removeTaskData(taskID);
	}
	
	/**
	 * Recovers a timer task that was running in another node.
	 * 
	 * @param taskData
	 */
	void recover(TimerTaskData taskData) {
		TimerTask task = timerTaskFactory.newTimerTask(taskData);
		if (logger.isDebugEnabled()) {
			logger.debug("Recovering task with id "+taskData.getTaskID());
		}
		task.beforeRecover();
		schedule(task);
	}

	public void shutdownNow() {
		if (logger.isDebugEnabled()) {
			logger.debug("Shutdown now.");
		}
		executor.shutdownNow();
		localRunningTasks.clear();
	}
	
	@Override
	public String toString() {
		return "FaultTolerantScheduler [ local tasks = "+localRunningTasks.size()+" , all tasks "+cacheData.getTaskDatas().size()+" ]";
	}
}
