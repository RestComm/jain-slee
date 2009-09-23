package org.mobicents.slee.runtime.facilities;

import java.io.Serializable;

import javax.slee.Address;
import javax.slee.facilities.TimerID;
import javax.slee.facilities.TimerOptions;

import org.mobicents.timers.PeriodicScheduleStrategy;
import org.mobicents.timers.TimerTaskData;
import org.mobicents.slee.runtime.activity.ActivityContextHandle;

/**
 * TODO
 * @author martins
 * 
 */
public class TimerFacilityTimerTaskData extends TimerTaskData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8896519632871403402L;

	/**
	 * 
	 */
	private final ActivityContextHandle ach;

	/**
     * 
     */
	private final Address address;

	/**
     * 
     */
	private final TimerOptions timerOptions;

	/**
     * 
     */
	private final int numRepetitions;

	/**
     * 
     */
	private int executions = 0;

	/**
     * 
     */
	private int missedRepetitions = 0;

	/**
     * 
     */
	private long lastTick;
	
	/**
	 * 
	 * @param timerID
	 * @param acID
	 * @param address
	 * @param startTime
	 * @param period
	 * @param numRepetitions
	 * @param timerOptions
	 */
	public TimerFacilityTimerTaskData(TimerID timerID, ActivityContextHandle ach,
			Address address, long startTime, long period, int numRepetitions,
			TimerOptions timerOptions) {
		super(timerID, startTime, period,PeriodicScheduleStrategy.atFixedRate);
		this.ach = ach;
		this.address = address;
		this.numRepetitions = numRepetitions;
		this.timerOptions = timerOptions;
	}
 
	public void incrementExecutions() {
		executions++;
	}
	
	/**
	 * 
	 * @return
	 */
	public ActivityContextHandle getActivityContextHandle() {
		return ach;
	}

	/**
	 * 
	 * @return
	 */
	public Address getAddress() {
		return address;
	}

	/**
	 * 
	 * @return
	 */
	public long getLastTick() {
		return lastTick;
	}

	/**
	 * 
	 * @return
	 */
	public int getMissedRepetitions() {
		return missedRepetitions;
	}

	/**
	 * 
	 * @return
	 */
	public int getNumRepetitions() {
		return numRepetitions;
	}

	/**
	 * 
	 * @return
	 */
	public int getRemainingRepetitions() {
		return numRepetitions > 0 ? (numRepetitions-executions) : Integer.MAX_VALUE;
	}

	/**
	 * 
	 * @return
	 */
	public TimerID getTimerID() {
		return (TimerID) super.getTaskID();
	}

	/**
	 * 
	 * @return
	 */
	public TimerOptions getTimerOptions() {
		return timerOptions;
	}

	/**
	 * 
	 */
	public void incrementMissedRepetitions() {
		missedRepetitions++;
	}
	
	/**
	 * 
	 * @param lastTick
	 */
	public void setLastTick(long lastTick) {
		this.lastTick = lastTick;
	}

	/**
	 * 
	 * @param missedRepetitions
	 */
	public void setMissedRepetitions(int missedRepetitions) {
		this.missedRepetitions = missedRepetitions;
	}

	@Override
	public String toString() {
		return "TimerFacilityTimerTaskData[ timerID = " + getTimerID()
				+ " , ach = " + ach + " , address = " + address
				+ " , timerOptions = " + timerOptions + " , startTime = "
				+ getStartTime() + " , numRepetitions = " + numRepetitions
				+ " , executions = " + executions
				+ " , missedRepetitions = " + missedRepetitions
				+ " , period = " + getPeriod() + " , lastTick = " + lastTick + " ]";
	}
	
	public long getScheduledTime() {
		final long period = getPeriod();
		long scheduledTime = getStartTime();
		if (period > 0) {
			scheduledTime += executions*period;
		}
		return scheduledTime;
	}
}
