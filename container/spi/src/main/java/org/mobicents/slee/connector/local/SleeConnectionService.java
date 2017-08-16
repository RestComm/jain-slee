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

package org.mobicents.slee.connector.local;

import java.rmi.RemoteException;

import javax.resource.ResourceException;
import javax.slee.Address;
import javax.slee.EventTypeID;
import javax.slee.UnrecognizedActivityException;
import javax.slee.UnrecognizedEventException;
import javax.slee.connection.ExternalActivityHandle;
import javax.slee.connection.SleeConnection;

import org.mobicents.slee.container.SleeContainerModule;

/**
 * This interface duplicates methods from {@link javax.slee.connection.SleeConnection}. Its extends SleeContainer module.
 * @author baranowb
 * 
 */
public interface SleeConnectionService extends SleeContainerModule {

	/**
	 * @see SleeConnection#createActivityHandle()
	 * @return
	 * @throws RemoteException
	 */
	public ExternalActivityHandle createActivityHandle() throws ResourceException;

	/**
	 * @see SleeConnection#fireEvent(Object, EventTypeID,
	 *      ExternalActivityHandle, Address)
	 * @param event
	 * @param eventType
	 * @param activityHandle
	 * @param address
	 * @throws NullPointerException
	 * @throws UnrecognizedEventException
	 * @throws RemoteException
	 */
	public void fireEvent(Object event, EventTypeID eventType,
			ExternalActivityHandle activityHandle, Address address)
	throws NullPointerException,
	UnrecognizedActivityException, UnrecognizedEventException, ResourceException;

	/**
	 * @see SleeConnection#getEventTypeID(String, String, String)
	 * @param name
	 * @param vendor
	 * @param version
	 * @return
	 * @throws UnrecognizedEventException
	 * @throws RemoteException
	 */
	public EventTypeID getEventTypeID(String name, String vendor, String version)
	throws UnrecognizedEventException, ResourceException;

}