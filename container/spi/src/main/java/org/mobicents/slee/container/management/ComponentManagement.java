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
package org.mobicents.slee.container.management;

import org.mobicents.slee.container.SleeContainerModule;
import org.mobicents.slee.container.component.ComponentDescriptorFactory;
import org.mobicents.slee.container.component.ComponentRepository;
import org.mobicents.slee.container.component.classloading.ClassLoaderFactory;
import org.mobicents.slee.container.component.du.DeployableUnitManagement;

/**
 * @author martins
 * 
 */
public interface ComponentManagement extends SleeContainerModule {

	/**
	 * Retrieves the class loader factory.
	 * 
	 * @return
	 */
	public ClassLoaderFactory getClassLoaderFactory();

	/**
	 * Retrieves the component descriptor factory
	 * 
	 * @return
	 */
	public ComponentDescriptorFactory getComponentDescriptorFactory();

	/**
	 * Retrieves the component repository.
	 * 
	 * @return
	 */
	public ComponentRepository getComponentRepository();

	/**
	 * Retrieves the deployable unit management.
	 * 
	 * @return
	 */
	public DeployableUnitManagement getDeployableUnitManagement();

}
