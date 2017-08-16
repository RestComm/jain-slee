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
