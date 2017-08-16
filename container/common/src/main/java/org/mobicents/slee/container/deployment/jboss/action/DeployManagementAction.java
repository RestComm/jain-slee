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

import javax.slee.management.DeploymentMBean;

/**
 * 
 * @author martins
 * 
 */
public abstract class DeployManagementAction implements ManagementAction {

	private final DeploymentMBean deploymentMBean;
	private final String duURL;

	public DeployManagementAction(String duURL, DeploymentMBean deploymentMBean) {
		this.deploymentMBean = deploymentMBean;
		this.duURL = duURL;
	}

	public String getDuURL() {
		return duURL;
	}

	public DeploymentMBean getDeploymentMBean() {
		return deploymentMBean;
	}

	/**
	 * 
	 */
	@Override
	public Type getType() {
		return Type.DEPLOY_MANAGEMENT;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "[" + duURL + "]";
	}
}
