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
package org.mobicents.slee.container.eventrouter.stats;

import javax.slee.EventTypeID;

/**
 * Performance and load statistics for an {@link EventRouterExecutor}, in the
 * SLEE {@link EventRouter}.
 * 
 * @author martins
 * 
 */
public interface EventRouterExecutorStatistics {

	/**
	 * Retrieves the number of activities currently mapped to the executor.
	 * @return
	 */
	public int getActivitiesMapped();
	
	/**
	 * Retrieves the number of tasks executed, which is the sum of the events
	 * routed for each {@link EventTypeID}, and also the misc tasks executed.
	 * 
	 * @return
	 */
	public long getExecutedTasks();

	/**
	 * Retrieves the time spent on executing tasks, which is the sum of the time
	 * spent routing events for each {@link EventTypeID}, and also the time
	 * spent executing the misc tasks.
	 * 
	 * @return
	 */
	public long getExecutingTime();

	/**
	 * Retrieves the total time the executor was idle, i.e., not executing
	 * tasks.
	 * 
	 * @return
	 */
	public long getIdleTime();

	/**
	 * Retrieves the number of events routed with a specific {@link EventTypeID}
	 * .
	 * 
	 * @return
	 */
	public long getEventsRouted(EventTypeID eventTypeID);

	/**
	 * Retrieves the time spent routing events with a specific
	 * {@link EventTypeID}.
	 * 
	 * @return
	 */
	public long getRoutingTime(EventTypeID eventTypeID);

	/**
	 * Collect the statistics for the routing of events with the specific
	 * {@link EventTypeID}.
	 * 
	 * @param eventTypeID
	 */
	public void addEventTypeRoutingStatistics(
			EventTypeID eventTypeID);
	
	/**
	 * Retrieves the statistics for the routing of events with a specific
	 * {@link EventTypeID}.
	 * 
	 * @param eventTypeID
	 * @return
	 */
	public EventTypeRoutingStatistics getEventTypeRoutingStatistics(
			EventTypeID eventTypeID);

	/**
	 * Remove the statistics for the routing of events with the specific
	 * {@link EventTypeID}.
	 * 
	 * @param eventTypeID
	 */
	public void removeEventTypeRoutingStatistics(
			EventTypeID eventTypeID);
	
	/**
	 * Retrieves the number of misc tasks executed.
	 * 
	 * @return
	 */
	public long getMiscTasksExecuted();

	/**
	 * Retrieves the time spent executing misc tasks.
	 * 
	 * @return
	 */
	public long getMiscTasksExecutingTime();

	/**
	 * Retrieves the average time spent to route one event.
	 * @return
	 */
	public long getAverageEventRoutingTime();
	
	/**
	 * Retrieves the average time spent to route one event of the specified type.
	 * @return
	 */
	public long getAverageEventRoutingTime(EventTypeID eventTypeID);
	
	/**
	 * Retrieves the size of the executor's working queue. 
	 * @return
	 */
	public int getWorkingQueueSize();
	
}
