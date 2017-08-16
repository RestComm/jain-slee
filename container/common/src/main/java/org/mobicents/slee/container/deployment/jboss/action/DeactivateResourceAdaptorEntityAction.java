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

import javax.slee.management.ResourceAdaptorEntityState;

import org.mobicents.slee.container.management.ResourceManagement;
import org.mobicents.slee.container.resource.ResourceAdaptorEntity;

/**
 * 
 * @author martins
 *
 */
public class DeactivateResourceAdaptorEntityAction extends
		ResourceManagementAction {

	private final String raEntityName;

	public DeactivateResourceAdaptorEntityAction(String raEntityName,
			ResourceManagement resourceManagement) {
		super(resourceManagement);
		this.raEntityName = raEntityName;
	}

	public String getRaEntity() {
		return raEntityName;
	}
	
	@Override
	public void invoke() throws Exception {
		final ResourceManagement resourceManagement = getResourceManagement();
		final ResourceAdaptorEntity resourceAdaptorEntity = resourceManagement.getResourceAdaptorEntity(raEntityName);
		if (resourceAdaptorEntity != null) {
			// deactivate if needed
			if (resourceAdaptorEntity.getState() == ResourceAdaptorEntityState.ACTIVE) {
				resourceManagement.deactivateResourceAdaptorEntity(raEntityName);
			}
			// continue once entity is inactive
			while(resourceAdaptorEntity.getState() != ResourceAdaptorEntityState.INACTIVE) {
				try {
					Thread.sleep(1000);					
				}
				catch (Exception e) {
					// ignore
				}				
			}						
		}
	}

	@Override
	public String toString() {
		return "DeactivateResourceAdaptorEntityAction[" + raEntityName + "]";
	}

}
