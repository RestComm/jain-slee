package org.mobicents.slee.core.timers;

import java.io.Serializable;

/**
 * 
 * The {@link TimerTask} data, which may be replicated in a cluster environment to support fail over.
 * 
 * @author martins
 *
 */
public class TimerTaskData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8905073681622353718L;

	/**
	 * the starting time of the associated timer task execution
	 */
	private long startTime;
	
	/**
	 * the period of the associated timer task execution, -1 means it is not a periodic task
	 */
	private final long period;
	
	/**
	 * the id of the associated timer task
	 */
	private final Serializable taskID;
	
	/**
	 * the strategy used in a periodic timer task, can be null if it is not a periodic timer task
	 */
	private final PeriodicScheduleStrategy periodicScheduleStrategy;
	
	/**
	 * 
	 * @param id
	 * @param startTime
	 * @param period
	 */
	public TimerTaskData(Serializable id, long startTime, long period, PeriodicScheduleStrategy periodicScheduleStrategy) {
		this.taskID = id;
		this.startTime = startTime;
		this.period = period;
		this.periodicScheduleStrategy = periodicScheduleStrategy;
	}
	
	/**
	 * Retrieves the period of the associated timer task execution, -1 means it is not a periodic task.
	 * @return
	 */
	public long getPeriod() {
		return period;
	}
	
	/**
	 * Retrieves the starting time of the associated timer task execution.
	 * @return
	 */
	public long getStartTime() {
		return startTime;
	}
	
	/**
	 * Sets the starting time of the associated timer task execution.
	 * @param startTime
	 */
	public void setStartTime(long startTime) {
		this.startTime = startTime;		
	}
	
	/**
	 * Retrieves the id of the associated timer task.
	 * @return
	 */
	public Serializable getTaskID() {
		return taskID;
	}
	
	/**
	 * Retrieves the strategy used in a periodic timer task, can be null if it is not a periodic timer task.
	 * @return
	 */
	public PeriodicScheduleStrategy getPeriodicScheduleStrategy() {
		return periodicScheduleStrategy;
	}
	
	@Override
	public int hashCode() {		
		return taskID.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj.getClass() == this.getClass()) {
			return ((TimerTaskData)obj).taskID.equals(this.taskID);
		}
		else {
			return false;
		}
	}
}
