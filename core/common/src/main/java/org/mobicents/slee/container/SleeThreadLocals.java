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

package org.mobicents.slee.container;

import java.util.concurrent.ExecutorService;

import javax.slee.ServiceID;

/**
 * Storage of data in event router {@link ExecutorService}'s threads
 * 
 * @author martins
 *
 */
public class SleeThreadLocals {

	/**
	 * the id of the service, being invoked in the current thread.
	 */
	private static ThreadLocal<ServiceID> invokingService = new ThreadLocal<ServiceID>();
	
	/**
	 * Sets the id of the service, being invoked in the current thread.
	 * @param serviceID
	 */
	public static void setInvokingService(ServiceID serviceID) {
		invokingService.set(serviceID);
	}

	/**
	 * Retrieves the id of the service, being invoked in the current thread.
	 * @return null if the thread is not invoking any server
	 */
	public static ServiceID getInvokingService() {
		return invokingService.get();
	}
	
}
