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

import javax.slee.Address;
import javax.slee.EventTypeID;
import javax.slee.ServiceID;

import org.mobicents.slee.container.AbstractSleeContainerModule;
import org.mobicents.slee.container.activity.ActivityContext;

/**
 * @author martins
 * 
 */
public class EventContextFactoryImpl extends AbstractSleeContainerModule
		implements EventContextFactory {

	private final EventContextFactoryDataSource dataSource;

	/**
	 * @param dataSource
	 */
	public EventContextFactoryImpl(EventContextFactoryDataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.slee.container.event.EventContextFactory#
	 * createActivityEndEventContext
	 * (org.mobicents.slee.container.activity.ActivityContext,
	 * org.mobicents.slee.container.event.EventUnreferencedCallback)
	 */
	public EventContext createActivityEndEventContext(ActivityContext ac,
			EventUnreferencedCallback unreferencedCallback) {
		final EventReferencesHandlerImpl referencesHandler = new EventReferencesHandlerImpl();
		final EventContextData data = dataSource.newEventContextData(
				ActivityEndEventImpl.EVENT_TYPE_ID,
				ActivityEndEventImpl.SINGLETON, ac, null, null, null, null,
				unreferencedCallback,referencesHandler);
		final EventContextImpl eventContext = new ActivityEndEventContextImpl(data, this);
		referencesHandler.setEventContext(eventContext);
		return eventContext; 
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.container.event.EventContextFactory#createEventContext(javax.slee.EventTypeID, java.lang.Object, org.mobicents.slee.container.activity.ActivityContext, javax.slee.Address, javax.slee.ServiceID, org.mobicents.slee.container.event.EventProcessingSucceedCallback, org.mobicents.slee.container.event.EventProcessingFailedCallback, org.mobicents.slee.container.event.EventUnreferencedCallback)
	 */
	public EventContext createEventContext(EventTypeID eventTypeId,
			Object eventObject, ActivityContext ac, Address address,
			ServiceID serviceID,
			EventProcessingSucceedCallback succeedCallback,
			EventProcessingFailedCallback failedCallback,
			EventUnreferencedCallback unreferencedCallback) {
		final EventReferencesHandlerImpl referencesHandler = new EventReferencesHandlerImpl();
		final EventContextData data = dataSource.newEventContextData(
				eventTypeId, eventObject, ac, address, serviceID,
				succeedCallback, failedCallback, unreferencedCallback,
				referencesHandler);
		final EventContextImpl eventContext = new EventContextImpl(data, this);
		referencesHandler.setEventContext(eventContext);
		return eventContext;
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.container.event.EventContextFactory#createEventContext(javax.slee.EventTypeID, java.lang.Object, org.mobicents.slee.container.activity.ActivityContext, javax.slee.Address, javax.slee.ServiceID, org.mobicents.slee.container.event.ReferencesHandler)
	 */
	public EventContext createEventContext(EventTypeID eventTypeId,
			Object eventObject, ActivityContext ac, Address address,
			ServiceID serviceID, ReferencesHandler referencesHandler) {
		final EventContextData data = dataSource.newEventContextData(
				eventTypeId, eventObject, ac, address, serviceID,
				null, null, null,referencesHandler);
		return new EventContextImpl(data, this);
	}
	
	/**
	 * @return the dataSource
	 */
	public EventContextFactoryDataSource getDataSource() {
		return dataSource;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.container.event.EventContextFactory#getEventContext
	 * (org.mobicents.slee.container.event.EventContextHandle)
	 */
	public EventContext getEventContext(EventContextHandle handle) {
		return dataSource.getEventContext(handle);
	}

	@Override
	public String toString() {
		return "EventContextFactoryImpl[ datasource = "+dataSource+" ]";
	}
}
