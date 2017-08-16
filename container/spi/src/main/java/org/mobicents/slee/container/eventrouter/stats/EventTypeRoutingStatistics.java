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
