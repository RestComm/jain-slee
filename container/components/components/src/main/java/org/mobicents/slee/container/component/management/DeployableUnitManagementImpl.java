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

package org.mobicents.slee.container.component.management;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.slee.management.DeployableUnitID;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.component.ComponentManagementImpl;
import org.mobicents.slee.container.component.deployment.DeployableUnitBuilderImpl;
import org.mobicents.slee.container.component.deployment.DeployableUnitImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.DeployableUnitDescriptorFactoryImpl;
import org.mobicents.slee.container.component.du.DeployableUnit;
import org.mobicents.slee.container.component.du.DeployableUnitManagement;

public class DeployableUnitManagementImpl implements DeployableUnitManagement {

	private final static Logger logger = Logger
			.getLogger(DeployableUnitManagementImpl.class);

	private final ConcurrentHashMap<DeployableUnitID, DeployableUnitImpl> deployableUnits = new ConcurrentHashMap<DeployableUnitID, DeployableUnitImpl>();

	private final DeployableUnitBuilderImpl deployableUnitBuilder;
	private final DeployableUnitDescriptorFactoryImpl deployableUnitDescriptorFactory;

	/**
	 * 
	 */
	public DeployableUnitManagementImpl(
			ComponentManagementImpl componentManagement) {
		this.deployableUnitBuilder = new DeployableUnitBuilderImpl(
				componentManagement);
		this.deployableUnitDescriptorFactory = new DeployableUnitDescriptorFactoryImpl();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.slee.core.component.du.DeployableUnitManagement#
	 * addDeployableUnit(org.mobicents.slee.core.component.du.DeployableUnit)
	 */
	public void addDeployableUnit(final DeployableUnit deployableUnit) {

		if (deployableUnit == null)
			throw new NullPointerException("null deployableUnit");

		if (logger.isTraceEnabled()) {
			logger.trace("Adding DU :  "
							+ deployableUnit.getDeployableUnitID());
		}

		deployableUnits.put(deployableUnit.getDeployableUnitID(),
				(DeployableUnitImpl) deployableUnit);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.slee.core.component.du.DeployableUnitManagement#
	 * getDeployableUnitBuilder()
	 */
	public DeployableUnitBuilderImpl getDeployableUnitBuilder() {
		return deployableUnitBuilder;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.slee.core.component.du.DeployableUnitManagement#
	 * getDeployableUnitDescriptorFactory()
	 */
	public DeployableUnitDescriptorFactoryImpl getDeployableUnitDescriptorFactory() {
		return deployableUnitDescriptorFactory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.slee.core.component.du.DeployableUnitManagement#
	 * getDeployableUnits()
	 */
	public DeployableUnitID[] getDeployableUnits() {
		Set<DeployableUnitID> deployableUnitIDs = deployableUnits.keySet();
		return deployableUnitIDs.toArray(new DeployableUnitID[deployableUnitIDs
				.size()]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.slee.core.component.du.DeployableUnitManagement#
	 * getDeployableUnit(javax.slee.management.DeployableUnitID)
	 */
	public DeployableUnitImpl getDeployableUnit(
			DeployableUnitID deployableUnitID) {
		return deployableUnits.get(deployableUnitID);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.slee.core.component.du.DeployableUnitManagement#
	 * removeDeployableUnit(javax.slee.management.DeployableUnitID)
	 */
	public void removeDeployableUnit(DeployableUnitID deployableUnitID) {

		if (logger.isTraceEnabled()) {
			logger.trace("Removing DU with id:  " + deployableUnitID);
		}

		if (deployableUnitID == null)
			throw new NullPointerException("null id");

		deployableUnits.remove(deployableUnitID);

	}

	@Override
	public String toString() {
		return "Deployable Unit Management: " + "\n+-- Deployable Unit IDs: "
				+ deployableUnits.keySet();
	}
}
