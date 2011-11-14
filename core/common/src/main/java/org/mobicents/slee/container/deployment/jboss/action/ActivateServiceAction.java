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

package org.mobicents.slee.container.deployment.jboss.action;

import javax.slee.ServiceID;
import javax.slee.management.ServiceState;

import org.mobicents.slee.container.management.ServiceManagement;

/**
 * Deployment action to activate a service. Note: If a active service with same
 * name and vendor is found, then the action will instead use the management
 * operation that deactivates and activates a service in one step, providing
 * smooth service upgrade.
 * 
 * @author martins
 * 
 */
public class ActivateServiceAction extends ServiceManagementAction {

	public ActivateServiceAction(ServiceID serviceID,
			ServiceManagement serviceManagement) {
		super(serviceID, serviceManagement);
	}

	@Override
	public void invoke() throws Exception {
		ServiceID oldVersion = null;
		for (ServiceID activeService : getServiceManagement().getServices(
				ServiceState.ACTIVE)) {
			if (activeService.getName().equals(getServiceID().getName())
					&& activeService.getVendor().equals(
							getServiceID().getVendor())) {
				oldVersion = activeService;
				break;
			}
		}
		if (oldVersion == null) {
			getServiceManagement().activate(getServiceID());
		} else {
			getServiceManagement().deactivateAndActivate(oldVersion,
					getServiceID());
		}
	}

}
