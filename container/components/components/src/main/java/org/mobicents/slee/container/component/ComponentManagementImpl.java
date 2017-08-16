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

/**
 * 
 */
package org.mobicents.slee.container.component;

import org.mobicents.slee.container.AbstractSleeContainerModule;
import org.mobicents.slee.container.component.deployment.classloading.ClassLoaderFactoryImpl;
import org.mobicents.slee.container.component.management.DeployableUnitManagementImpl;
import org.mobicents.slee.container.management.ComponentManagement;

/**
 * @author martins
 * 
 */
public class ComponentManagementImpl extends AbstractSleeContainerModule implements ComponentManagement {

	private final ComponentDescriptorFactoryImpl componentDescriptorFactory;
	private final ComponentRepositoryImpl componentRepository;
	private final DeployableUnitManagementImpl deployableUnitManagement;
	private final ClassLoaderFactoryImpl classLoaderFactory;

	/**
	 * 
	 */
	public ComponentManagementImpl() {
		classLoaderFactory = new ClassLoaderFactoryImpl();
		componentDescriptorFactory = new ComponentDescriptorFactoryImpl();
		componentRepository = new ComponentRepositoryImpl();
		deployableUnitManagement = new DeployableUnitManagementImpl(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.slee.container.management.ComponentManagement#
	 * getClassLoaderFactory()
	 */
	public ClassLoaderFactoryImpl getClassLoaderFactory() {
		return classLoaderFactory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.slee.core.component.ComponentManagement#
	 * getComponentDescriptorFactory()
	 */
	public ComponentDescriptorFactoryImpl getComponentDescriptorFactory() {
		return componentDescriptorFactory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.core.component.ComponentManagement#getComponentRepository
	 * ()
	 */
	public ComponentRepositoryImpl getComponentRepository() {
		return componentRepository;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.slee.core.component.ComponentManagement#
	 * getDeployableUnitManagement()
	 */
	public DeployableUnitManagementImpl getDeployableUnitManagement() {
		return deployableUnitManagement;
	}

}
