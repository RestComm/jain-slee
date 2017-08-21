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
