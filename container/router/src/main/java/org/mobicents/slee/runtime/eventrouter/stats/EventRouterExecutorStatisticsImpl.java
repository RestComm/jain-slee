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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.slee.EventTypeID;

import org.mobicents.slee.container.activity.ActivityContextHandle;
import org.mobicents.slee.container.eventrouter.stats.EventRouterExecutorStatistics;
import org.mobicents.slee.container.eventrouter.stats.EventTypeRoutingStatistics;

/**
 * Impl of {@link EventRouterExecutorStatistics}. This class is not thread safe
 * due to performance overhead introduced when collecting stats, i.e., errors in
 * values due to multiple threads accessing the statistics state are not
 * important.
 * 
 * @author martins
 * 
 */
public class EventRouterExecutorStatisticsImpl implements
		EventRouterExecutorStatistics {

	private AtomicInteger activitiesMapped = new AtomicInteger(0);
	
	private final Map<EventTypeID, EventTypeRoutingStatisticsImpl> eventTypeRoutingStatisticsMap = new HashMap<EventTypeID, EventTypeRoutingStatisticsImpl>();

	private long miscTasksExecuted = 0L;
	private long miscTaskExecutingTime = 0L;

	private long tasksExecuted = 0L;
	private long taskExecutingTime = 0L;

	private final long startTime = System.nanoTime();

	private void taskExecuted(long executionTime) {
		tasksExecuted++;
		taskExecutingTime += executionTime;
	}

	/**
	 * Indicates that the activity with the specified handle was mapped to the executor.
	 * @param ach
	 */
	public void activityMapped(ActivityContextHandle ach) {
		activitiesMapped.incrementAndGet();
	}
	
	/**
	 * Indicates that the activity with the specified handle was unmapped to the executor.
	 * @param ach
	 */
	public void activityUnmapped(ActivityContextHandle ach) {
		activitiesMapped.decrementAndGet();
	}
	
	/**
	 * Adds the time for an event routing with a specific {@link EventTypeID}.
	 * 
	 * @param eventTypeID
	 *            the id of the event type
	 * @param routingTime
	 *            the time spent to route the event, in milliseconds
	 */
	public void eventRouted(EventTypeID eventTypeID, long routingTime) {
		EventTypeRoutingStatisticsImpl eventTypeRoutingStatistics = eventTypeRoutingStatisticsMap.get(eventTypeID);
		if (eventTypeRoutingStatistics == null) {
			synchronized (eventTypeRoutingStatisticsMap) {
				eventTypeRoutingStatistics = new EventTypeRoutingStatisticsImpl(eventTypeID);
				eventTypeRoutingStatisticsMap.put(eventTypeID, eventTypeRoutingStatistics); 
			}
		}
		eventTypeRoutingStatistics.eventRouted(routingTime);
		taskExecuted(routingTime);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.runtime.eventrouter.stats.EventRouterExecutorStatistics
	 * #addEventTypeRoutingStatistics(javax.slee.EventTypeID)
	 */
	public void addEventTypeRoutingStatistics(EventTypeID eventTypeID) {
		eventTypeRoutingStatisticsMap.put(eventTypeID,
				new EventTypeRoutingStatisticsImpl(eventTypeID));
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.runtime.eventrouter.stats.EventRouterExecutorStatistics#getActivitiesMapped()
	 */
	public int getActivitiesMapped() {
		return activitiesMapped.get();
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.eventrouter.stats.EventRouterExecutorStatistics#getAverageEventRoutingTime()
	 */
	public long getAverageEventRoutingTime() {
		long time = 0L;
		long events = 0L;
		for(EventTypeRoutingStatistics eventTypeRoutingStatistics : eventTypeRoutingStatisticsMap.values()) {
			time += eventTypeRoutingStatistics.getRoutingTime();
			events += eventTypeRoutingStatistics.getEventsRouted();
		}
		return time == 0L ? 0L : time / events;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.eventrouter.stats.EventRouterExecutorStatistics#getAverageEventRoutingTime(javax.slee.EventTypeID)
	 */
	public long getAverageEventRoutingTime(EventTypeID eventTypeID) {
		final EventTypeRoutingStatistics eventTypeRoutingStatistics = getEventTypeRoutingStatistics(eventTypeID);
		return eventTypeRoutingStatistics == null ? 0L
				: eventTypeRoutingStatistics.getAverageEventRoutingTime();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.runtime.eventrouter.stats.EventRouterExecutorStatistics
	 * #getEventTypeRoutingStatistics(javax.slee.EventTypeID)
	 */
	public EventTypeRoutingStatisticsImpl getEventTypeRoutingStatistics(
			EventTypeID eventTypeID) {
		return eventTypeRoutingStatisticsMap.get(eventTypeID);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.runtime.eventrouter.stats.EventRouterExecutorStatistics
	 * #removeEventTypeRoutingStatistics(javax.slee.EventTypeID)
	 */
	public void removeEventTypeRoutingStatistics(EventTypeID eventTypeID) {
		eventTypeRoutingStatisticsMap.remove(eventTypeID);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.runtime.eventrouter.stats.EventRouterExecutorStatistics
	 * #getEventsRouted(javax.slee.EventTypeID)
	 */
	public long getEventsRouted(EventTypeID eventTypeID) {
		final EventTypeRoutingStatistics eventTypeRoutingStatistics = getEventTypeRoutingStatistics(eventTypeID);
		return eventTypeRoutingStatistics == null ? 0L
				: eventTypeRoutingStatistics.getEventsRouted();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.runtime.eventrouter.stats.EventRouterExecutorStatistics
	 * #getExecutedTasks()
	 */
	public long getExecutedTasks() {
		return tasksExecuted;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.runtime.eventrouter.stats.EventRouterExecutorStatistics
	 * #getExecutingTime()
	 */
	public long getExecutingTime() {
		return taskExecutingTime;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.runtime.eventrouter.stats.EventRouterExecutorStatistics
	 * #getIdleTime()
	 */
	public long getIdleTime() {
		return (System.nanoTime() - startTime) - getExecutingTime();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.runtime.eventrouter.stats.EventRouterExecutorStatistics
	 * #getMiscTasksExecuted()
	 */
	public long getMiscTasksExecuted() {
		return miscTasksExecuted;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.runtime.eventrouter.stats.EventRouterExecutorStatistics
	 * #getMiscTasksExecutingTime()
	 */
	public long getMiscTasksExecutingTime() {
		return miscTaskExecutingTime;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.runtime.eventrouter.stats.EventRouterExecutorStatistics
	 * #getRoutingTime(javax.slee.EventTypeID)
	 */
	public long getRoutingTime(EventTypeID eventTypeID) {
		final EventTypeRoutingStatistics eventTypeRoutingStatistics = getEventTypeRoutingStatistics(eventTypeID);
		return eventTypeRoutingStatistics == null ? 0L
				: eventTypeRoutingStatistics.getRoutingTime();
	}

	/**
	 * Adds the time for a misc task execution.
	 * 
	 * @param executionTime
	 *            the time spent to execute the misc task
	 */
	public void miscTaskExecuted(long executionTime) {
		miscTasksExecuted++;
		miscTaskExecutingTime += executionTime;
		taskExecuted(executionTime);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Activities mapped: ").append(getActivitiesMapped()).append('\n');
		for (EventTypeRoutingStatistics eventTypeRoutingStatistics : eventTypeRoutingStatisticsMap.values()) {
			sb.append(eventTypeRoutingStatistics).append('\n');
		}
		sb.append("Average event routing time: ").append(getAverageEventRoutingTime()).append('\n');
		sb.append("Executed Tasks: ").append(getExecutedTasks()).append('\n');
		sb.append("Executing Time: ").append(getExecutingTime()).append('\n');
		sb.append("Idle Time: ").append(getIdleTime()).append('\n');
		sb.append("Misc Tasks Executed: ").append(getMiscTasksExecuted()).append('\n');
		sb.append("Misc Tasks Executing Time: ").append(getMiscTasksExecutingTime()).append('\n');
		return sb.toString();
	}
}
