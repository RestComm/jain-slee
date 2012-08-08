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

package org.mobicents.slee.container.management.jmx;

import javax.slee.EventTypeID;
import javax.slee.management.ManagementException;

import org.mobicents.slee.container.eventrouter.EventRouter;

/**
 * 
 * @author martins
 * 
 */
public class EventRouterStatistics implements
		EventRouterStatisticsMBean {

	private final EventRouter eventRouter;

	public EventRouterStatistics(EventRouter eventRouter) {
		this.eventRouter = eventRouter;
	}

	private org.mobicents.slee.container.eventrouter.stats.EventRouterStatistics getEventRouterStatistics()
			throws ManagementException {
		if (eventRouter == null) {
			throw new ManagementException("router not set");
		}
		if (eventRouter.getEventRouterStatistics() == null) {
			throw new ManagementException("router stats not available");
		}
		return eventRouter.getEventRouterStatistics();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.container.management.jmx.EventRouterStatisticsMBean
	 * #getActivitiesMapped()
	 */
	public int getActivitiesMapped() throws ManagementException {
		return getEventRouterStatistics().getActivitiesMapped();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.container.management.jmx.EventRouterStatisticsMBean
	 * #getActivitiesMapped(int)
	 */
	public int getActivitiesMapped(int executor) throws ManagementException {
		return getEventRouterStatistics().getActivitiesMapped(executor);
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.management.jmx.EventRouterStatisticsMBean#getAverageEventRoutingTime()
	 */
	public long getAverageEventRoutingTime() throws ManagementException {
		return getEventRouterStatistics().getAverageEventRoutingTime();
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.management.jmx.EventRouterStatisticsMBean#getAverageEventRoutingTime(javax.slee.EventTypeID)
	 */
	public long getAverageEventRoutingTime(EventTypeID eventTypeID)
			throws ManagementException {
		return getEventRouterStatistics().getAverageEventRoutingTime(eventTypeID);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.management.jmx.EventRouterStatisticsMBean#getAverageEventRoutingTime(int)
	 */
	public long getAverageEventRoutingTime(int executor)
			throws ManagementException {
		return getEventRouterStatistics().getAverageEventRoutingTime(executor);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.management.jmx.EventRouterStatisticsMBean#getAverageEventRoutingTime(int, javax.slee.EventTypeID)
	 */
	public long getAverageEventRoutingTime(int executor, EventTypeID eventTypeID)
			throws ManagementException {
		return getEventRouterStatistics().getAverageEventRoutingTime(executor,eventTypeID);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.container.management.jmx.EventRouterStatisticsMBean
	 * #getEventsRouted(javax.slee.EventTypeID)
	 */
	public long getEventsRouted(EventTypeID eventTypeID)
			throws ManagementException {
		return getEventRouterStatistics().getEventsRouted(eventTypeID);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.container.management.jmx.EventRouterStatisticsMBean
	 * #getEventsRouted(int, javax.slee.EventTypeID)
	 */
	public long getEventsRouted(int executor, EventTypeID eventTypeID)
			throws ManagementException {
		return getEventRouterStatistics()
				.getEventsRouted(executor, eventTypeID);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.container.management.jmx.EventRouterStatisticsMBean
	 * #getExecutedTasks()
	 */
	public long getExecutedTasks() throws ManagementException {
		return getEventRouterStatistics().getExecutedTasks();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.container.management.jmx.EventRouterStatisticsMBean
	 * #getExecutedTasks(int)
	 */
	public long getExecutedTasks(int executor) throws ManagementException {
		return getEventRouterStatistics().getExecutedTasks(executor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.container.management.jmx.EventRouterStatisticsMBean
	 * #getExecutingTime(int)
	 */
	public long getExecutingTime(int executor) throws ManagementException {
		return getEventRouterStatistics().getExecutedTasks(executor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.container.management.jmx.EventRouterStatisticsMBean
	 * #getIdleTime(int)
	 */
	public long getIdleTime(int executor) throws ManagementException {
		return getEventRouterStatistics().getIdleTime(executor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.container.management.jmx.EventRouterStatisticsMBean
	 * #getMiscTasksExecuted()
	 */
	public long getMiscTasksExecuted() throws ManagementException {
		return getEventRouterStatistics().getMiscTasksExecuted();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.container.management.jmx.EventRouterStatisticsMBean
	 * #getMiscTasksExecuted(int)
	 */
	public long getMiscTasksExecuted(int executor) throws ManagementException {
		return getEventRouterStatistics().getMiscTasksExecuted(executor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.container.management.jmx.EventRouterStatisticsMBean
	 * #getMiscTasksExecutingTime(int)
	 */
	public long getMiscTasksExecutingTime(int executor)
			throws ManagementException {
		return getEventRouterStatistics().getMiscTasksExecutingTime(executor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.container.management.jmx.EventRouterStatisticsMBean
	 * #getRoutingTime(int, javax.slee.EventTypeID)
	 */
	public long getRoutingTime(int executor, EventTypeID eventTypeID)
			throws ManagementException {
		return getEventRouterStatistics().getRoutingTime(executor, eventTypeID);
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.management.jmx.EventRouterStatisticsMBean#printAllStats()
	 */
	public String printAllStats() throws ManagementException {
		return getEventRouterStatistics().toString();
	}
}
