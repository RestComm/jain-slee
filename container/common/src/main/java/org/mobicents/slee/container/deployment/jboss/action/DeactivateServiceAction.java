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

import org.mobicents.slee.container.component.service.ServiceComponent;
import org.mobicents.slee.container.management.ServiceManagement;

/**
 * 
 * @author martins
 * 
 */
public class DeactivateServiceAction extends ServiceManagementAction {

	public DeactivateServiceAction(ServiceID serviceID,
			ServiceManagement serviceManagement) {
		super(serviceID, serviceManagement);
	}

	@Override
	public void invoke() throws Exception {
		final ServiceManagement serviceManagement = getServiceManagement();
		final ServiceComponent serviceComponent = serviceManagement.getSleeContainer().getComponentRepository().getComponentByID(getServiceID());
		if (serviceComponent != null) {
			// deactivate if needed
			if (serviceComponent.getServiceState() == ServiceState.ACTIVE) {
				serviceManagement.deactivate(getServiceID());
			}
			// continue once service is inactive
			while(serviceComponent.getServiceState() != ServiceState.INACTIVE) {
				try {
					Thread.sleep(1000);					
				}
				catch (Exception e) {
					// ignore
				}				
			}
		}
	}

}
