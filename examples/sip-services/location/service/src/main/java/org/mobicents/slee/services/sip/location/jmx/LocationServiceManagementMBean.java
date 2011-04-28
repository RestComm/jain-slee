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

import org.mobicents.slee.services.sip.location.LocationServiceException;


public interface LocationServiceManagementMBean {

	public static final String MBEAN_NAME="slee:sipservice=Location";
	
	/**
	 * 
	 * @return Set with registered users. It contains entries like "sip:316471@kocia.domena.au" or "sip:mobicents@gmail.com"
	 */
	public Set<String> getRegisteredUsers() throws LocationServiceException;
	/**
	 * 
	 * @param sipAddress - address of record, value like "sip:mobicents@gmail.com" this is passed in from and to header of REGISTER reqeust
	 * @return
	 * @throws LocationServiceException 
	 */
	public Set<String> getContacts(String sipAddress) throws LocationServiceException;
	
	/**
	 * Returns time in miliseconds left till certain contact expires, if there is some error it return Long.MIN_VALUE.
	 * 
	 * @param sipAddress
	 * @param contactAddress - must be exact value put into register. See return values of getContacts
	 * @return
	 * @throws LocationServiceException 
	 */
	public long getExpirationTime(String sipAddress, String contactAddress) throws LocationServiceException;
	
	/**
	 * Number of registered users.
	 * @return
	 */
	public int getRegisteredUserCount() throws LocationServiceException;
	
}
