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
package org.mobicents.slee.container.event;

import javax.slee.Address;
import javax.slee.EventTypeID;
import javax.slee.ServiceID;

import org.mobicents.slee.container.SleeContainerModule;
import org.mobicents.slee.container.activity.ActivityContext;

/**
 * @author martins
 * 
 */
public interface EventContextFactory extends SleeContainerModule {

	/**
	 * 
	 * @param ac
	 * @param unreferencedCallback
	 * @return
	 */
	public EventContext createActivityEndEventContext(ActivityContext ac,
			EventUnreferencedCallback unreferencedCallback);

	/**
	 * 
	 * @param eventTypeId
	 * @param eventObject
	 * @param ac
	 * @param address
	 * @param serviceID
	 * @param succeedCallback
	 * @param failedCallback
	 * @param unreferencedCallback
	 * @return
	 */
	public EventContext createEventContext(EventTypeID eventTypeId,
			Object eventObject, ActivityContext ac, Address address,
			ServiceID serviceID,
			EventProcessingSucceedCallback succeedCallback,
			EventProcessingFailedCallback failedCallback,
			EventUnreferencedCallback unreferencedCallback);

	/**
	 * 
	 * @param eventTypeId
	 * @param eventObject
	 * @param ac
	 * @param address
	 * @param serviceID
	 * @param reference
	 * @return
	 */
	public EventContext createEventContext(EventTypeID eventTypeId,
			Object eventObject, ActivityContext ac, Address address,
			ServiceID serviceID, EventContext reference);
	
	/**
	 * 
	 * @param handle
	 * @return
	 */
	public EventContext getEventContext(EventContextHandle handle);

}
