package org.mobicents.slee.core.timers.timer;

import java.util.Date;
import java.util.FaultTolerantTimerTimerTask;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import org.mobicents.slee.core.timers.FaultTolerantScheduler;
import org.mobicents.slee.core.timers.PeriodicScheduleStrategy;
import org.mobicents.slee.runtime.cache.TimerTasksCacheData;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;

/**
 * A fault tolerant implementation of a {@link Timer}, using the
 * {@link FaultTolerantScheduler}.
 * 
 * There is no guarantee that fail over recover works correctly with not serializable {@link TimerTask}s
 * 
 * @author martins
 * 
 */
public class FaulTolerantTimer extends java.util.Timer {

	/**
	 * 
	 */
	private final FaultTolerantScheduler scheduler;
	
	/**
	 * 
	 */
	private static final FaultTolerantTimerTimerTaskFactory timerTaskFactory = new FaultTolerantTimerTimerTaskFactory();
	
	/**
	 * 
	 * @param cacheData
	 * @param txManager
	 */
	public FaulTolerantTimer(TimerTasksCacheData cacheData, SleeTransactionManager txManager) {
		 scheduler = new FaultTolerantScheduler(16, cacheData, txManager, timerTaskFactory);
	}
	
	@Override
	public void cancel() {
		scheduler.shutdownNow();
	}
	
	@Override
	public int purge() {
		int count = 0;
		for (org.mobicents.slee.core.timers.TimerTask timerTask : scheduler.getLocalRunningTasks().values()) {
			FaultTolerantTimerTimerTask ftTimerTask = (FaultTolerantTimerTimerTask) timerTask;
			if (ftTimerTask.isCanceled()) {
				scheduler.cancel(ftTimerTask.getData().getTaskID());
				count++;
			}
		}
		return count;
	}
	
	@Override
	public void schedule(TimerTask task, Date firstTime, long period) {
		final org.mobicents.slee.core.timers.TimerTask taskWrapper = timerTaskFactory.newTimerTask(new FaultTolerantTimerTimerTaskData(task, UUID.randomUUID(),firstTime.getTime() - System.currentTimeMillis(),period,PeriodicScheduleStrategy.withFixedDelay));
		scheduler.schedule(taskWrapper);
	}
	
	@Override
	public void schedule(TimerTask task, Date time) {
		final org.mobicents.slee.core.timers.TimerTask taskWrapper = timerTaskFactory.newTimerTask(new FaultTolerantTimerTimerTaskData(task, UUID.randomUUID(),time.getTime() - System.currentTimeMillis(),-1,null));
		scheduler.schedule(taskWrapper);		
	}
	
	@Override
	public void schedule(TimerTask task, long delay) {
		final org.mobicents.slee.core.timers.TimerTask taskWrapper = timerTaskFactory.newTimerTask(new FaultTolerantTimerTimerTaskData(task, UUID.randomUUID(),System.currentTimeMillis()+delay,-1,null));
		scheduler.schedule(taskWrapper);	
	}
	
	@Override
	public void schedule(TimerTask task, long delay, long period) {
		final org.mobicents.slee.core.timers.TimerTask taskWrapper = timerTaskFactory.newTimerTask(new FaultTolerantTimerTimerTaskData(task, UUID.randomUUID(),System.currentTimeMillis()+delay,period,PeriodicScheduleStrategy.withFixedDelay));
		scheduler.schedule(taskWrapper);		
	}
	
	@Override
	public void scheduleAtFixedRate(TimerTask task, Date firstTime, long period) {
		final org.mobicents.slee.core.timers.TimerTask taskWrapper = timerTaskFactory.newTimerTask(new FaultTolerantTimerTimerTaskData(task, UUID.randomUUID(),firstTime.getTime() - System.currentTimeMillis(),period,PeriodicScheduleStrategy.atFixedRate));
		scheduler.schedule(taskWrapper);
	}
	
	@Override
	public void scheduleAtFixedRate(TimerTask task, long delay, long period) {
		final org.mobicents.slee.core.timers.TimerTask taskWrapper = timerTaskFactory.newTimerTask(new FaultTolerantTimerTimerTaskData(task, UUID.randomUUID(),System.currentTimeMillis()+delay,period,PeriodicScheduleStrategy.atFixedRate));
		scheduler.schedule(taskWrapper);	
	}
}
