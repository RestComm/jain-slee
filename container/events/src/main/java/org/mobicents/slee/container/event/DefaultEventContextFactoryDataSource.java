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

import java.util.concurrent.ConcurrentHashMap;

import javax.slee.Address;
import javax.slee.EventTypeID;
import javax.slee.ServiceID;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.activity.ActivityContext;

/**
 * @author martins
 *
 */
public class DefaultEventContextFactoryDataSource implements EventContextFactoryDataSource {

	private final static Logger logger = Logger.getLogger(DefaultEventContextFactoryDataSource.class);
	
	private final ConcurrentHashMap<EventContextHandle, EventContext> dataSource = new ConcurrentHashMap<EventContextHandle, EventContext>();
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.container.event.EventContextFactoryDataSource#addEventContext(org.mobicents.slee.container.event.EventContextHandle, org.mobicents.slee.container.event.EventContext)
	 */
	public void addEventContext(EventContextHandle handle,
			EventContext eventContext) {
		if(logger.isDebugEnabled()) {
			logger.debug("Adding event context "+eventContext+" to datasource. Event context handle is "+handle);
		}
		dataSource.put(handle, eventContext);
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.container.event.EventContextFactoryDataSource#getEventContext(org.mobicents.slee.container.event.EventContextHandle)
	 */
	public EventContext getEventContext(EventContextHandle handle) {
		return dataSource.get(handle);
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.container.event.EventContextFactoryDataSource#newEventContextData(javax.slee.EventTypeID, java.lang.Object, org.mobicents.slee.container.activity.ActivityContext, javax.slee.Address, javax.slee.ServiceID, org.mobicents.slee.container.event.EventProcessingSucceedCallback, org.mobicents.slee.container.event.EventProcessingFailedCallback, org.mobicents.slee.container.event.EventUnreferencedCallback, org.mobicents.slee.container.event.EventReferencesHandler)
	 */
	public EventContextData newEventContextData(EventTypeID eventTypeId,
			Object event, ActivityContext ac, Address address,
			ServiceID serviceID,
			EventProcessingSucceedCallback succeedCallback,
			EventProcessingFailedCallback failedCallback,
			EventUnreferencedCallback unreferencedCallback,
			ReferencesHandler referencesHandler) {
		return new DefaultEventContextData(eventTypeId, event, ac, address, serviceID, succeedCallback, failedCallback, unreferencedCallback, referencesHandler);
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.container.event.EventContextFactoryDataSource#removeEventContext(org.mobicents.slee.container.event.EventContextHandle)
	 */
	public void removeEventContext(EventContextHandle handle) {
		if(logger.isDebugEnabled()) {
			logger.debug("Removing event context from datasource. Event context handle is "+handle);
		}
		dataSource.remove(handle);
	}
	
	@Override
	public String toString() {
		return "DefaultEventContextFactoryDataSource[ "+(dataSource.size() > 20 ? dataSource.size() : dataSource.values())+" ]";
	}
	
}
