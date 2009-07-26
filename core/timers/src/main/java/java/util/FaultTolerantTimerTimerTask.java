package java.util;

import org.mobicents.slee.core.timers.FaultTolerantScheduler;
import org.mobicents.slee.core.timers.TimerTask;
import org.mobicents.slee.core.timers.timer.FaultTolerantTimerTimerTaskData;

/**
 * A concrete {@link FaultTolerantScheduler} {@link TimerTask} to wrap a {@link java.util.TimerTask}.
 * 
 * @author martins
 *
 */
public class FaultTolerantTimerTimerTask extends TimerTask {

	/**
	 * 
	 */
	private FaultTolerantScheduler scheduler;
	
	/**
	 * 
	 */
	private final FaultTolerantTimerTimerTaskData taskData;
	
	/**
	 * 
	 * @param taskData
	 */
	public FaultTolerantTimerTimerTask(FaultTolerantTimerTimerTaskData taskData) {
		super(taskData);
		this.taskData = taskData;
		taskData.getJavaUtilTimerTask().period = taskData.getPeriod();
	}
	
	/**
	 * 
	 * @param scheduler
	 */
	public void setScheduler(FaultTolerantScheduler scheduler) {
		this.scheduler = scheduler;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.core.timers.TimerTask#run()
	 */
	public void run() {
		if (isCanceled()) {
			if (scheduler != null) {
				scheduler.cancel(this.getData().getTaskID());
			}
		}
		else {
			taskData.getJavaUtilTimerTask().run();
		}				
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isCanceled() {
		return taskData.getJavaUtilTimerTask().state == java.util.TimerTask.CANCELLED;
	}
}