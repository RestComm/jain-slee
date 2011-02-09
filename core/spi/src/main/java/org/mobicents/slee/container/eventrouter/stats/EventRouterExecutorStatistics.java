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
