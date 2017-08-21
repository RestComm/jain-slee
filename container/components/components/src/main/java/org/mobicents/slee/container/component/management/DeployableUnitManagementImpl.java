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
