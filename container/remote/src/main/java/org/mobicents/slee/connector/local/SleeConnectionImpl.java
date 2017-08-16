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
package org.mobicents.slee.connector.local;

import javax.resource.ResourceException;
import javax.slee.Address;
import javax.slee.EventTypeID;
import javax.slee.UnrecognizedActivityException;
import javax.slee.UnrecognizedEventException;
import javax.slee.connection.ExternalActivityHandle;
import javax.slee.connection.SleeConnection;

import org.apache.log4j.Logger;

/**
 * Connection object which proxies calls.
 * @author baranowb
 *
 */
public class SleeConnectionImpl implements SleeConnection {

	
	private static final Logger logger = Logger.getLogger(MobicentsSleeConnectionFactoryImpl.class);

	private MobicentsSleeConnectionFactoryImpl sleeConnectionFactory;
	private SleeConnectionService service;
	
	SleeConnectionImpl(SleeConnectionService service,MobicentsSleeConnectionFactoryImpl sleeConnectionFactoryImpl) {
		super();

		this.sleeConnectionFactory = sleeConnectionFactoryImpl;
		this.service = service;

	}

	/* (non-Javadoc)
	 * @see javax.slee.connection.SleeConnection#close()
	 */
	public void close() throws ResourceException {
		if(service == null)
		{
			throw new ResourceException("Connection is in closed state");
		}else
		{
			this.service = null;
			this.sleeConnectionFactory.connectionClosed(this);
		}

	}

	/* (non-Javadoc)
	 * @see javax.slee.connection.SleeConnection#createActivityHandle()
	 */
	public ExternalActivityHandle createActivityHandle() throws ResourceException {
		if(this.service == null)
		{
			throw new ResourceException("Connection is in closed state");
		}
		
		return this.service.createActivityHandle();
	}

	/* (non-Javadoc)
	 * @see javax.slee.connection.SleeConnection#fireEvent(java.lang.Object, javax.slee.EventTypeID, javax.slee.connection.ExternalActivityHandle, javax.slee.Address)
	 */
	public void fireEvent(Object event, EventTypeID eventType,
			ExternalActivityHandle activityHandle, Address address) throws NullPointerException,
			UnrecognizedActivityException, UnrecognizedEventException, ResourceException {
		if(service == null)
		{
			throw new ResourceException("Connection is in closed state");
		}

		if (logger.isDebugEnabled()) {
			logger.debug("fireEvent(event=" + event + ",eventType=" + eventType
					+ ",activityHandle=" + activityHandle + ",address="
					+ address + ")");
		}

		this.service.fireEvent(event, eventType, activityHandle, address);
	}

	/* (non-Javadoc)
	 * @see javax.slee.connection.SleeConnection#getEventTypeID(java.lang.String, java.lang.String, java.lang.String)
	 */
	public EventTypeID getEventTypeID(String name, String vendor, String version) throws UnrecognizedEventException, ResourceException {
		if(service == null)
		{
			throw new ResourceException("Connection is in closed state");
		}
		return service.getEventTypeID(name, vendor, version);
	}

	
	void start(SleeConnectionService service) {
		this.service = service;
	}

}
