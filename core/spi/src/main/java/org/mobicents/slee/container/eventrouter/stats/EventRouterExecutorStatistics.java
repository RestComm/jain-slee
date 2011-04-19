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
}
