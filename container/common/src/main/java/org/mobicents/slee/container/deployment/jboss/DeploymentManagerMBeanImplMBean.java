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

package org.mobicents.slee.container.deployment.jboss;

import java.net.URL;

public interface DeploymentManagerMBeanImplMBean {

	public static final String OBJECT_NAME = "org.mobicents.slee:name=DeployerMBean";
	
  /**
   * Displays information regarding the JBoss JAIN SLEE DU Deployer, providing
   * information regarding deployed DU's and others with missing dependencies.
   * 
   * @return a HTML string with the current status information
   */
  public String showStatus();

  /**
   * Deploys a DU from a remote or local location in a persistent way, ie,
   * copying it to JBOSS_HOME/server/<node>/deploy folder.
   * 
   * @param deployableUnitURL the URL to the DU to be deployed
   * @throws DeploymentException if it's not possible to deploy this DU
   */
  void persistentInstall(URL deployableUnitURL) throws DeploymentException;

  /**
   * Undeploys a persistently deployed DU by deleting it from the server deploy folder.
   * 
   * @param deployableUnitURL the URL to the DU to be undeployed
   * @throws DeploymentException if it's not possible to undeploy this DU
   */
  void persistentUninstall(URL deployableUnitURL) throws DeploymentException;

  /**
   * Deploys a DU from a remote or local location in a persistent way to the 
   * whole cluster by copying it to JBOSS_HOME/server/<node>/farm folder.
   * 
   * @param deployableUnitURL the URL to the DU to be deployed
   * @throws DeploymentException if it's not possible to deploy this DU
   */
  void clusterInstall(URL deployableUnitURL) throws DeploymentException;

  /**
   * Undeploys a cluster-wide deployed DU by deleting it from the server farm folder.
   * 
   * @param deployableUnitURL the URL to the DU to be undeployed
   * @throws DeploymentException if it's not possible to undeploy this DU
   */
  void clusterUninstall(URL deployableUnitURL) throws DeploymentException;

}
