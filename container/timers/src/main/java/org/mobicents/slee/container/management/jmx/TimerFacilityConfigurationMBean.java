/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2017, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

/**
 * 
 */
package org.mobicents.slee.container.management.jmx;

/**
 * JMX interface for the SLEE timer facility configuration.
 * @author martins
 *
 */
public interface TimerFacilityConfigurationMBean {

	public static final String OBJECT_NAME = "org.mobicents.slee:name=TimerFacilityConfiguration";
	
	/**
	 * Retrieves the number of threads used by timers.
	 * @return
	 */
	public int getTimerThreads();
	
	/**
	 * Sets the number of threads used by timers.
	 * 
	 * Note that setting a different value for this method will only be effective on server (re)start.
	 * 
	 * @param value
	 */
	public void setTimerThreads(int value);
	
	/**
	 * Retrieves the period (in minutes) of purging canceled tasks from the Timer Facility.
	 * @return
	 */
	public int getPurgePeriod();
	
	/**
	 * Sets the period (in minutes) of purging canceled tasks from the Timer Facility. Use 0 for no purge at all.
	 * @param value
	 */
	public void setPurgePeriod(int value);
	
	/**
	 * Indicates if the timer task should wait for confirmation that the tx,
	 * which set the timer, finished commit. This confirmation is the only
	 * guarantee that short timers (such as the ones with 0 delay), set on new
	 * tx aware activities, will not execute before all state (such as the actual AC), is committed.
	 * 
	 * @return
	 */
	public boolean getTaskExecutionWaitsForTxCommitConfirmation();
	
	/**
	 * Defines if the timer task should wait for confirmation that the tx,
	 * which set the timer, finished commit. This confirmation is the only
	 * guarantee that short timers (such as the ones with 0 delay), set on new
	 * tx aware activities, will not execute before all state (such as the actual AC), is committed.
	 * @param value
	 */
	public void setTaskExecutionWaitsForTxCommitConfirmation(boolean value);
	
}
