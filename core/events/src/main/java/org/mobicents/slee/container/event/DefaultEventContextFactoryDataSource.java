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
package org.mobicents.slee.container.event;

import java.util.concurrent.ConcurrentHashMap;

import javax.slee.Address;
import javax.slee.EventTypeID;
import javax.slee.ServiceID;

import org.mobicents.slee.container.activity.ActivityContext;

/**
 * @author martins
 *
 */
public class DefaultEventContextFactoryDataSource implements EventContextFactoryDataSource {

	private final ConcurrentHashMap<EventContextHandle, EventContext> dataSource = new ConcurrentHashMap<EventContextHandle, EventContext>();
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.container.event.EventContextFactoryDataSource#addEventContext(org.mobicents.slee.container.event.EventContextHandle, org.mobicents.slee.container.event.EventContext)
	 */
	public void addEventContext(EventContextHandle handle,
			EventContext eventContext) {
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
		dataSource.remove(handle);
	}
	
	@Override
	public String toString() {
		return "DefaultEventContextFactoryDataSource[ "+dataSource.keySet()+" ]";
	}
	
}
