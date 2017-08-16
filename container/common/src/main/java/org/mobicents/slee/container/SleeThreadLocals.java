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
