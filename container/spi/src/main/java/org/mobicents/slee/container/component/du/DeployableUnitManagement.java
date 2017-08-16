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
