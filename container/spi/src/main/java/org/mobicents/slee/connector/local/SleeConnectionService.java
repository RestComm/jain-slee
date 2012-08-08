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