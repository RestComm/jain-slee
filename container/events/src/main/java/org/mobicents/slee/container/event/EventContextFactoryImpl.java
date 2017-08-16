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

import org.mobicents.slee.container.AbstractSleeContainerModule;
import org.mobicents.slee.container.activity.ActivityContext;
import org.mobicents.slee.container.management.jmx.EventContextFactoryConfiguration;

/**
 * @author martins
 * 
 */
public class EventContextFactoryImpl extends AbstractSleeContainerModule
		implements EventContextFactory {

	private final EventContextFactoryDataSource dataSource;

	private final EventContextFactoryConfiguration configuration;

	/**
	 * @param dataSource
	 */
	public EventContextFactoryImpl(EventContextFactoryDataSource dataSource,
			EventContextFactoryConfiguration configuration) {
		super();
		this.dataSource = dataSource;
		this.configuration = configuration;
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
				unreferencedCallback, referencesHandler);
		final EventContextImpl eventContext = new ActivityEndEventContextImpl(
				data, this);
		referencesHandler.setEventContext(eventContext);
		return eventContext;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.container.event.EventContextFactory#createEventContext
	 * (javax.slee.EventTypeID, java.lang.Object,
	 * org.mobicents.slee.container.activity.ActivityContext,
	 * javax.slee.Address, javax.slee.ServiceID,
	 * org.mobicents.slee.container.event.EventProcessingSucceedCallback,
	 * org.mobicents.slee.container.event.EventProcessingFailedCallback,
	 * org.mobicents.slee.container.event.EventUnreferencedCallback)
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

	@Override
	public EventContext createEventContext(EventTypeID eventTypeId,
			Object eventObject, ActivityContext ac, Address address,
			ServiceID serviceID, EventContext reference) {
		final EventContextData data = dataSource.newEventContextData(
				eventTypeId, eventObject, ac, address, serviceID, null, null,
				null, ((EventContextImpl)reference).getReferencesHandler());
		return new EventContextImpl(data, this);
	}

	/**
	 * @return the dataSource
	 */
	public EventContextFactoryDataSource getDataSource() {
		return dataSource;
	}

	/**
	 * Retrieves the factory's configuration.
	 * 
	 * @return
	 */
	public EventContextFactoryConfiguration getConfiguration() {
		return configuration;
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
		return "EventContextFactoryImpl[ datasource = " + dataSource + " ]";
	}
}
