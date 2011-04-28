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

package org.mobicents.slee.services.sip.location.jmx;

import java.util.Set;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.services.sip.location.LocationService;
import org.mobicents.slee.services.sip.location.LocationServiceException;

/**
 * Implementation of the Location Service Management JMX interface.
 * @author martins
 *
 */
public class LocationServiceManagement implements LocationServiceManagementMBean {

	private final LocationService locationService;
	
	private LocationServiceManagement(LocationService locationService) {
		this.locationService=locationService;
	}
	
	public Set<String> getContacts(String sipAddress)
			throws LocationServiceException {
		
		return locationService.getContacts(sipAddress);
	}

	public long getExpirationTime(String sipAddress, String contactAddress)
			throws LocationServiceException {
		
		return locationService.getExpirationTime(sipAddress, contactAddress);
	}

	public int getRegisteredUserCount() throws LocationServiceException {
		
		return locationService.getRegisteredUserCount();
	}

	public Set<String> getRegisteredUsers() throws LocationServiceException {
		
		return locationService.getRegisteredUsers();
	}

	/**
	 * Starts the MBean
	 * @return
	 */
	public static boolean create(LocationService locationService) {

		MBeanServer mbs = SleeContainer.lookupFromJndi().getMBeanServer();
		ObjectName on = null;
		try {
			on = new ObjectName(MBEAN_NAME);
		} catch (Exception e) {
			e.printStackTrace();
		} 

		try {
			if (mbs.getObjectInstance(on) != null) {
				mbs.unregisterMBean(on);
			}
		} catch (InstanceNotFoundException e) {
			// ignore
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			mbs.registerMBean(new LocationServiceManagement(locationService), on);
		} catch (InstanceAlreadyExistsException e) {

			e.printStackTrace();
			return false;
		} catch (MBeanRegistrationException e) {

			e.printStackTrace();
			return false;
		} catch (NotCompliantMBeanException e) {

			e.printStackTrace();
			return false;
		}

		return true;

	}
	
	/**
	 * Stops the MBean
	 */
	public static void destroy() {
		try {
			SleeContainer.lookupFromJndi().getMBeanServer().unregisterMBean(new ObjectName(MBEAN_NAME));
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

}
