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

/*
 * ***************************************************
 *                                                 *
 *  Restcomm: The Open Source JSLEE Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************
 *
 * Created on Dec 6, 2004 RemoteSleeEndpoint.java
 */
package org.mobicents.slee.connector.remote;

import java.rmi.RemoteException;

import javax.slee.Address;
import javax.slee.EventTypeID;
import javax.slee.UnrecognizedEventException;
import javax.slee.connection.ExternalActivityHandle;
import javax.slee.connection.SleeConnection;

/**
 * This interface duplicates methods from
 * {@link javax.slee.connection.SleeConnection}. However it is meant as RMI
 * interface, as such only RemoteException is declared in throws clause.
 * 
 * @author baranowb
 */
public interface RemoteSleeConnectionService {

	/**
	 * @see SleeConnection#createActivityHandle()
	 * @return
	 * @throws RemoteException
	 */
	public ExternalActivityHandle createActivityHandle() throws RemoteException;

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
	public void fireEvent(Object event, EventTypeID eventType, ExternalActivityHandle activityHandle, Address address)
	throws RemoteException;

	/**
	 * @see SleeConnection#getEventTypeID(String, String, String)
	 * @param name
	 * @param vendor
	 * @param version
	 * @return
	 * @throws UnrecognizedEventException
	 * @throws RemoteException
	 */
	public EventTypeID getEventTypeID(String name, String vendor, String version) throws RemoteException;

}