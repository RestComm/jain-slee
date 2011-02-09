/**
 * 
 */
package org.mobicents.slee.container.eventrouter.stats;

import javax.slee.EventTypeID;

/**
 * Statistics for the routing of events with a specific {@link EventTypeID}.
 * 
 * @author martins
 * 
 */
public interface EventTypeRoutingStatistics {

	/**
	 * Retrieves the average time spent to route one event.
	 * @return
	 */
	public long getAverageEventRoutingTime();

	/**
	 * Retrieves the number of events routed
	 * 
	 * @return
	 */
	public long getEventsRouted();

	/**
	 * Retrieves the event type associated with the statistics
	 * 
	 * @return
	 */
	public EventTypeID getEventType();
	
	/**
	 * Retrieves the time spent routing events.
	 * 
	 * @return
	 */
	public long getRoutingTime();

}
