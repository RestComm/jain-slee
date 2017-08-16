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

import org.mobicents.slee.container.activity.ActivityContext;

/**
 * @author martins
 *
 */
public interface EventContextFactoryDataSource {

	/**
	 * 
	 * @param handle
	 * @param eventContext
	 */
	public void addEventContext(EventContextHandle handle, EventContext eventContext);
	
	/**
	 * 
	 * @param handle
	 * @return
	 */
	public EventContext getEventContext(EventContextHandle handle);
	
	/**
	 * 
	 * @param handle
	 */
	public void removeEventContext(EventContextHandle handle);

	/**
	 * 
	 * @param eventTypeId
	 * @param event
	 * @param ac
	 * @param address
	 * @param serviceID
	 * @param succeedCallback
	 * @param failedCallback
	 * @param unreferencedCallback
	 * @param referencesHandler
	 * @return
	 */
	public EventContextData newEventContextData(EventTypeID eventTypeId, Object event,
			ActivityContext ac, Address address, ServiceID serviceID,
			EventProcessingSucceedCallback succeedCallback,
			EventProcessingFailedCallback failedCallback,
			EventUnreferencedCallback unreferencedCallback,
			ReferencesHandler referencesHandler);
}
