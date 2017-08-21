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
