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

package org.mobicents.slee.resource.cluster;

import org.restcomm.timers.PeriodicScheduleStrategy;
import org.restcomm.timers.TimerTaskData;

/**
 * Wrapps the FT timer task data, into the task data used by Restcomm Cluster
 * framework.
 * 
 * @author martins
 * 
 */
public class FaultTolerantTimerTaskDataWrapper extends TimerTaskData {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final FaultTolerantTimerTaskData wrappedData;

	/**
	 * 
	 * @param wrappedData
	 * @param startTime
	 * @param period
	 * @param periodicScheduleStrategy
	 */
	public FaultTolerantTimerTaskDataWrapper(
			FaultTolerantTimerTaskData wrappedData, long startTime,
			long period, PeriodicScheduleStrategy periodicScheduleStrategy) {
		super(wrappedData.getTaskID(), startTime, period,
				periodicScheduleStrategy);
		this.wrappedData = wrappedData;
	}

	/**
	 * Retrieves the wrapped timer task data.
	 * 
	 * @return
	 */
	public FaultTolerantTimerTaskData getWrappedData() {
		return wrappedData;
	}

}
