/**
 * 
 */
package org.mobicents.slee.container.eventrouter.stats;

import javax.slee.EventTypeID;

import org.mobicents.slee.container.eventrouter.EventRouter;

/**
 * The JMX interface for the SLEE {@link EventRouter}'s performance and load
 * statistics.
 * 
 * @author martins
 * 
 */
public interface EventRouterStatisticsMBean {

	/**
	 * Retrieves the total activities mapped for all the event router executors.
	 * 
	 * @return
	 */
	public int getActivitiesMapped();

	/**
	 * Retrieves the activities mapped in the specified event router executor.
	 * 
	 * @param executor
	 * @return
	 */
	public int getActivitiesMapped(int executor);

	/**
	 * Retrieves the number of events routed with a specific {@link EventTypeID}
	 * .
	 * 
	 * @return
	 */
	public long getEventsRouted(EventTypeID eventTypeID);

	/**
	 * Retrieves the number of events routed with a specific {@link EventTypeID}
	 * , for the specified event router executor.
	 * 
	 * @return
	 */
	public long getEventsRouted(int executor, EventTypeID eventTypeID);

	/**
	 * Retrieves the number of tasks executed, which is the sum of the events
	 * routed for each {@link EventTypeID}, and also the misc tasks executed.
	 * 
	 * @return
	 */
	public long getExecutedTasks();

	/**
	 * Retrieves the number of tasks executed, which is the sum of the events
	 * routed for each {@link EventTypeID}, and also the misc tasks executed,
	 * for the specified event router executor.
	 * 
	 * @return
	 */
	public long getExecutedTasks(int executor);

	/**
	 * Retrieves the time spent on executing tasks, which is the sum of the time
	 * spent routing events for each {@link EventTypeID}, and also the time
	 * spent executing the misc tasks, for the specified event router executor.
	 * 
	 * @return
	 */
	public long getExecutingTime(int executor);

	/**
	 * Retrieves the total time the executor was idle, i.e., not executing
	 * tasks, for the specified event router executor.
	 * 
	 * @return
	 */
	public long getIdleTime(int executor);

	/**
	 * Retrieves the number of misc tasks executed.
	 * 
	 * @return
	 */
	public long getMiscTasksExecuted();

	/**
	 * Retrieves the number of misc tasks executed, for the specified event
	 * router executor.
	 * 
	 * @return
	 */
	public long getMiscTasksExecuted(int executor);

	/**
	 * Retrieves the time spent executing misc tasks, for the specified event
	 * router executor.
	 * 
	 * @return
	 */
	public long getMiscTasksExecutingTime(int executor);

	/**
	 * Retrieves the time spent routing events with a specific
	 * {@link EventTypeID}, for the specified event router executor.
	 * 
	 * @return
	 */
	public long getRoutingTime(int executor, EventTypeID eventTypeID);
}
