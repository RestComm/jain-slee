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
package org.mobicents.slee.container.component.du;

import javax.slee.management.DeployableUnitID;

/**
 * @author martins
 *
 */
public interface DeployableUnitManagement {

	/**
	 * Adds a deployable unit.
	 * 
	 * @param descriptor -
	 *            descriptor to register
	 * 
	 */
	public void addDeployableUnit(DeployableUnit deployableUnit);
	
	/**
	 * Get the deployable unit with the specified id.
	 * 
	 * @param deployableUnitID --
	 *            the deployable unit id
	 * 
	 * @return
	 */
	public DeployableUnit getDeployableUnit(
			DeployableUnitID deployableUnitID);

	/**
	 * Retrieves the {@link DeployableUnit} builder.
	 * @return
	 */
	public DeployableUnitBuilder getDeployableUnitBuilder();

	/**
	 * Retrieves the deployable unit descriptor factory.
	 * @return
	 */
	public DeployableUnitDescriptorFactory getDeployableUnitDescriptorFactory();
	
	/**
	 * Get an array containing the deployable unit ids known to the container.
	 * 
	 * @return
	 */
	public DeployableUnitID[] getDeployableUnits();
	
	/**
	 * Removes the deployable unit with the specified id
	 * @param deployableUnitID
	 */
	public void removeDeployableUnit(DeployableUnitID deployableUnitID);
}
