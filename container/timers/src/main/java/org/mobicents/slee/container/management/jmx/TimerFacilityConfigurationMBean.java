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
