/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.slee.runtime.facilities;

import java.io.IOException;
import java.io.Serializable;

import javax.slee.Address;
import javax.slee.facilities.TimerID;
import javax.slee.facilities.TimerOptions;
import javax.slee.facilities.TimerPreserveMissed;

import org.mobicents.slee.container.activity.ActivityContextHandle;
import org.mobicents.timers.PeriodicScheduleStrategy;
import org.mobicents.timers.TimerTaskData;

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
	private transient ActivityContextHandle ach;

	/**
     * 
     */
	private transient Address address;

	/**
     * 
     */
	private transient TimerOptions timerOptions;

	/**
     * 
     */
	private transient int numRepetitions;

	/**
     * 
     */
	private transient int executions = 0;

	/**
     * 
     */
	private transient int missedRepetitions = 0;

	/**
     * 
     */
	private transient long lastTick;
	
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

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
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
	
	/**
	 * 
	 * @return
	 */
	public long getScheduledTime() {
		final long period = getPeriod();
		long scheduledTime = getStartTime();
		if (period > 0) {
			scheduledTime += executions*period;
		}
		return scheduledTime;
	}
	
	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.writeObject(ach);
		out.writeObject(address);
		out.writeInt(executions);
		out.writeLong(lastTick);
		out.writeInt(missedRepetitions);
		out.writeInt(numRepetitions);
		out.writeBoolean(timerOptions.isPersistent());
		out.writeLong(timerOptions.getTimeout());
		out.writeInt(timerOptions.getPreserveMissed().toInt());
	}
    
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
    	ach = (ActivityContextHandle) in.readObject();
		address = (Address) in.readObject();
		executions = in.readInt();
		lastTick = in.readLong();
		missedRepetitions = in.readInt();
		numRepetitions = in.readInt();
		final boolean persistent = in.readBoolean();
		final long timeout = in.readLong();
		final TimerPreserveMissed preserveMissed = TimerPreserveMissed.fromInt(in.readInt());
		timerOptions = new TimerOptions(persistent, timeout, preserveMissed);
    }
	
}
