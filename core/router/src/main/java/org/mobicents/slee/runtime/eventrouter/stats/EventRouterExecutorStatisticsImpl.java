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

	private final long startTime = System.currentTimeMillis();

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
		final EventTypeRoutingStatisticsImpl eventTypeRoutingStatistics = getEventTypeRoutingStatistics(eventTypeID);
		if (eventTypeRoutingStatistics != null) {
			eventTypeRoutingStatistics.eventRouted(routingTime);
			taskExecuted(routingTime);
		}
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
		return (System.currentTimeMillis() - startTime) - getExecutingTime();
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

}
