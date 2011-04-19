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
package org.mobicents.slee.runtime.eventrouter.stats;

import javax.slee.EventTypeID;

import org.mobicents.slee.container.eventrouter.EventRouterExecutor;
import org.mobicents.slee.container.eventrouter.stats.EventRouterExecutorStatistics;
import org.mobicents.slee.container.eventrouter.stats.EventRouterStatistics;
import org.mobicents.slee.container.eventrouter.stats.EventTypeRoutingStatistics;
import org.mobicents.slee.runtime.eventrouter.EventRouterImpl;

/**
 * @author martins
 *
 */
public class EventRouterStatisticsImpl implements EventRouterStatistics {

	private final EventRouterImpl eventRouter;
	
	public EventRouterStatisticsImpl(EventRouterImpl eventRouter) {
		this.eventRouter = eventRouter;
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.core.eventrouter.stats.EventRouterStatisticsMBean#getActivitiesMapped()
	 */
	public int getActivitiesMapped() {
		int result = 0;
		for (int i = 0; i < getExecutors().length; i++) {
			result += getActivitiesMapped(i);			
		}
		return result;
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.core.eventrouter.stats.EventRouterStatisticsMBean#getActivitiesMapped(int)
	 */
	public int getActivitiesMapped(int executor) {
		final EventRouterExecutorStatistics executorStats = getExecutors()[executor].getStatistics();
		return executorStats == null ? 0 : executorStats.getActivitiesMapped();		
	}
	
	private EventRouterExecutor[] getExecutors() throws IllegalStateException {
		return eventRouter.getExecutors();
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.runtime.eventrouter.stats.EventRouterStatistics#getEventRouterExecutorStatistics(int)
	 */
	public EventRouterExecutorStatistics getEventRouterExecutorStatistics(
			int executor) {
		return getExecutors()[executor].getStatistics();
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.eventrouter.stats.EventRouterExecutorStatistics#getAverageEventRoutingTime()
	 */
	public long getAverageEventRoutingTime() {
		long time = 0L;
		long events = 0L;
		for (EventTypeID eventTypeID : eventRouter.getSleeContainer().getComponentManagement().getComponentRepository().getEventComponentIDs()) {
			for (int i = 0; i < getExecutors().length; i++) {
				EventTypeRoutingStatistics eventTypeRoutingStatistics = getEventRouterExecutorStatistics(i).getEventTypeRoutingStatistics(eventTypeID);
				if (eventTypeRoutingStatistics != null) {
					time += eventTypeRoutingStatistics.getRoutingTime();
					events += eventTypeRoutingStatistics.getEventsRouted();
				}
			}
		}
		return time == 0L ? 0L : time / events;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.eventrouter.stats.EventRouterExecutorStatistics#getAverageEventRoutingTime(javax.slee.EventTypeID)
	 */
	public long getAverageEventRoutingTime(EventTypeID eventTypeID) {
		long time = 0L;
		long events = 0L;
		for (int i = 0; i < getExecutors().length; i++) {
			EventTypeRoutingStatistics eventTypeRoutingStatistics = getEventRouterExecutorStatistics(i).getEventTypeRoutingStatistics(eventTypeID);
			if (eventTypeRoutingStatistics != null) {
				time += eventTypeRoutingStatistics.getRoutingTime();
				events += eventTypeRoutingStatistics.getEventsRouted();
			}
		}		
		return time == 0L ? 0L : time / events;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.eventrouter.stats.EventRouterStatistics#getAverageEventRoutingTime(int)
	 */
	public long getAverageEventRoutingTime(int executor) {
		final EventRouterExecutorStatistics executorStats = getExecutors()[executor].getStatistics();
		return executorStats == null ? 0 : executorStats.getAverageEventRoutingTime();
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.eventrouter.stats.EventRouterStatistics#getAverageEventRoutingTime(int, javax.slee.EventTypeID)
	 */
	public long getAverageEventRoutingTime(int executor, EventTypeID eventTypeID) {
		final EventRouterExecutorStatistics executorStats = getExecutors()[executor].getStatistics();
		return executorStats == null ? 0 : executorStats.getAverageEventRoutingTime(eventTypeID);
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.runtime.eventrouter.stats.EventRouterStatisticsMBean#getEventsRouted(javax.slee.EventTypeID)
	 */
	public long getEventsRouted(EventTypeID eventTypeID) {
		long result = 0L;
		for (int i = 0; i < getExecutors().length; i++) {
			result += getEventsRouted(i,eventTypeID);			
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.runtime.eventrouter.stats.EventRouterStatisticsMBean#getEventsRouted(int, javax.slee.EventTypeID)
	 */
	public long getEventsRouted(int executor, EventTypeID eventTypeID) {
		final EventRouterExecutorStatistics executorStats = getExecutors()[executor].getStatistics();
		return executorStats == null ? 0 : executorStats.getEventsRouted(eventTypeID);
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.runtime.eventrouter.stats.EventRouterStatisticsMBean#getExecutedTasks()
	 */
	public long getExecutedTasks() {
		long result = 0L;
		for (int i = 0; i < getExecutors().length; i++) {
			result += getExecutedTasks(i);			
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.runtime.eventrouter.stats.EventRouterStatisticsMBean#getExecutedTasks(int)
	 */
	public long getExecutedTasks(int executor) {
		final EventRouterExecutorStatistics executorStats = getExecutors()[executor].getStatistics();
		return executorStats == null ? 0 : executorStats.getExecutedTasks();
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.runtime.eventrouter.stats.EventRouterStatisticsMBean#getExecutingTime(int)
	 */
	public long getExecutingTime(int executor) {
		final EventRouterExecutorStatistics executorStats = getExecutors()[executor].getStatistics();
		return executorStats == null ? 0 : executorStats.getExecutingTime();
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.runtime.eventrouter.stats.EventRouterStatisticsMBean#getIdleTime(int)
	 */
	public long getIdleTime(int executor) {
		final EventRouterExecutorStatistics executorStats = getExecutors()[executor].getStatistics();
		return executorStats == null ? 0 : executorStats.getIdleTime();
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.runtime.eventrouter.stats.EventRouterStatisticsMBean#getMiscTasksExecuted()
	 */
	public long getMiscTasksExecuted() {
		long result = 0L;
		for (int i = 0; i < getExecutors().length; i++) {
			result += getMiscTasksExecuted(i);			
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.runtime.eventrouter.stats.EventRouterStatisticsMBean#getMiscTasksExecuted(int)
	 */
	public long getMiscTasksExecuted(int executor) {
		final EventRouterExecutorStatistics executorStats = getExecutors()[executor].getStatistics();
		return executorStats == null ? 0 : executorStats.getMiscTasksExecuted();
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.runtime.eventrouter.stats.EventRouterStatisticsMBean#getMiscTasksExecutingTime(int)
	 */
	public long getMiscTasksExecutingTime(int executor) {
		final EventRouterExecutorStatistics executorStats = getExecutors()[executor].getStatistics();
		return executorStats == null ? 0 : executorStats.getMiscTasksExecutingTime();
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.runtime.eventrouter.stats.EventRouterStatisticsMBean#getRoutingTime(javax.slee.EventTypeID)
	 */
	public long getRoutingTime(EventTypeID eventTypeID) {
		long result = 0L;
		for (int i = 0; i < getExecutors().length; i++) {
			result += getRoutingTime(i,eventTypeID);			
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.runtime.eventrouter.stats.EventRouterStatisticsMBean#getRoutingTime(int, javax.slee.EventTypeID)
	 */
	public long getRoutingTime(int executor, EventTypeID eventTypeID) {
		final EventRouterExecutorStatistics executorStats = getExecutors()[executor].getStatistics();
		return executorStats == null ? 0 : executorStats.getRoutingTime(eventTypeID);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Global Statistics\n");
		sb.append("\tActivities mapped: ").append(getActivitiesMapped()).append('\n');
		sb.append("\tAverage event routing time: ").append(getAverageEventRoutingTime()).append('\n');
		sb.append("\tExecuted Tasks: ").append(getExecutedTasks()).append('\n');
		sb.append("\tMisc Tasks Executed: ").append(getMiscTasksExecuted()).append('\n');
		for (EventTypeID eventTypeID : eventRouter.getSleeContainer().getComponentManagement().getComponentRepository().getEventComponentIDs()) {
			sb.append('\n');
			sb.append(eventTypeID).append(" statistics:\n");
			sb.append("\tAverage event routing time: ").append(getAverageEventRoutingTime(eventTypeID)).append('\n');
			sb.append("\tEvent routing time: ").append(getRoutingTime(eventTypeID)).append('\n');
			sb.append("\tEvents routed: ").append(getEventsRouted(eventTypeID)).append('\n');
		}
		for (int i = 0; i < getExecutors().length; i++) {
			sb.append("\nExecutor ").append(i).append(" statistics:\n");
			sb.append(getEventRouterExecutorStatistics(i));
		}
		return sb.toString();
	}
}
