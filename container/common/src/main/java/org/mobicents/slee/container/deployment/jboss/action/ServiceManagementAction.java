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

package org.mobicents.slee.container.deployment.jboss.action;

import javax.slee.ServiceID;

import org.mobicents.slee.container.management.ServiceManagement;

/**
 * 
 * @author martins
 * 
 */
public abstract class ServiceManagementAction implements ManagementAction {

	private final ServiceID serviceID;
	private final ServiceManagement serviceManagement;

	/**
	 * 
	 * @param sleeContainer
	 */
	public ServiceManagementAction(ServiceID serviceID,
			ServiceManagement serviceManagement) {
		this.serviceID = serviceID;
		this.serviceManagement = serviceManagement;
	}

	/**
	 * 
	 */
	@Override
	public Type getType() {
		return Type.SERVICE_MANAGEMENT;
	}

	/**
	 * 
	 * @return
	 */
	public ServiceID getServiceID() {
		return serviceID;
	}

	/**
	 * 
	 * @return
	 */
	public ServiceManagement getServiceManagement() {
		return serviceManagement;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "[" + serviceID + "]";
	}
}
