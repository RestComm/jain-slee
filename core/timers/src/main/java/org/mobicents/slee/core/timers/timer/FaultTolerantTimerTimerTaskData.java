package org.mobicents.slee.core.timers.timer;

import java.io.Serializable;
import java.util.TimerTask;

import org.mobicents.slee.core.timers.PeriodicScheduleStrategy;
import org.mobicents.slee.core.timers.TimerTaskData;

/**
 * 
 * @author martins
 *
 */
public class FaultTolerantTimerTimerTaskData extends TimerTaskData {

	/**
	 * 
	 */
	private final TimerTask javaUtilTimerTask;
	
	/**
	 * 
	 * @param javaUtilTimerTask
	 * @param id
	 * @param startTime
	 * @param period
	 * @param periodicScheduleStrategy
	 */
	public FaultTolerantTimerTimerTaskData(TimerTask javaUtilTimerTask, Serializable id, long startTime, long period, PeriodicScheduleStrategy periodicScheduleStrategy) {
		super(id,startTime,period, periodicScheduleStrategy);
		this.javaUtilTimerTask = javaUtilTimerTask;		
	}

	/**
	 * 
	 * @return
	 */
	public TimerTask getJavaUtilTimerTask() {
		return javaUtilTimerTask;
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
