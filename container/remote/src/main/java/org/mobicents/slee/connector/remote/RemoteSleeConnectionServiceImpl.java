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

package org.mobicents.slee.connector.remote;

import org.apache.log4j.Logger;
import org.mobicents.slee.connector.local.SleeConnectionService;
import org.mobicents.slee.container.component.ComponentRepository;
import org.mobicents.slee.container.component.SleeComponent;

import javax.resource.ResourceException;
import javax.slee.Address;
import javax.slee.EventTypeID;
import javax.slee.connection.ExternalActivityHandle;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * 
 * Implementation of the RemoteSleeService.
 * 
 * An instance of this class receives invocations via HA-RMI from the outside
 * world.
 * 
 * @author Tim
 * @author eduardomartins
 * @author baranowb
 */

public class RemoteSleeConnectionServiceImpl extends UnicastRemoteObject implements RemoteSleeConnectionService {

	private static final Logger log = Logger.getLogger(RemoteSleeConnectionServiceImpl.class);
	private SleeConnectionService service;
	private ComponentRepository repository;
	
	public RemoteSleeConnectionServiceImpl(SleeConnectionService service,ComponentRepository repository) throws RemoteException {
		if (log.isDebugEnabled())
			log.debug("Creating RemoteSleeConnectionServiceImpl");
		this.service = service;
		this.repository = repository;
	}

	/**
	 * @see RemoteSleeConnectionServiceImpl#createActivityHandle()
	 */
	public ExternalActivityHandle createActivityHandle() {
		if (log.isDebugEnabled()) {
			log.debug("Creating external activity handle");
		}
		ExternalActivityHandle externalActivityHandle = null;
		try {
			externalActivityHandle = this.service.createActivityHandle();
		} catch (ResourceException e) {
			//be good citized
			//throw new RemoteException("Failed to create activity due to:", e);
			log.error("Failed to create activity due to:", e);
		}
		return externalActivityHandle;
	}

	/**
	 * @see RemoteSleeConnectionServiceImpl#fireEvent(Object, EventTypeID,
	 *      ExternalActivityHandle, Address)
	 */
	public void fireEvent(Object event, EventTypeID eventType,
			ExternalActivityHandle activityHandle, Address address) {
		final boolean deserialize =  event instanceof RemoteEventWrapper;
		if (deserialize)
		{
			final ClassLoader origCL = Thread.currentThread().getContextClassLoader();    	
			try {

				RemoteEventWrapper rew = (RemoteEventWrapper) event;
				// we have to switch CL, lookup component CL, switch CTX, load
				// event and fire...
				SleeComponent sleeComponent = this.repository.getComponentByID(eventType);
				Thread.currentThread().setContextClassLoader(sleeComponent.getClassLoader());
				event = rew.getEvent();

			} catch (Exception e) {
				// be good citized
				//throw new RemoteException("Failed to fire event due to:", e);
				log.error("Failed to fire event due to:", e);
			} finally {
				Thread.currentThread().setContextClassLoader(origCL);
			}
		} else
		{
			//for some reason someone called RMI localy, not VIA
		}

		try {
			this.service.fireEvent(event, eventType, activityHandle, address);
		} catch (Exception e) {
			//be good citized
			//throw new RemoteException("Failed to fire event due to:", e);
			log.error("Failed to fire event due to:", e);
		}
	}

	/**
	 * @see RemoteSleeConnectionServiceImpl#getEventTypeID(String, String, String)
	 */
	public EventTypeID getEventTypeID(String name, String vendor, String version) {
		EventTypeID eventTypeID = null;
		try {
			eventTypeID = this.service.getEventTypeID(name, vendor, version);
		} catch (Exception e) {
			//be good citized
			//throw new RemoteException("Failed to retrieve event type due to:", e);
			log.error("Failed to retrieve event type due to:", e);
		}
		return eventTypeID;
	}
}