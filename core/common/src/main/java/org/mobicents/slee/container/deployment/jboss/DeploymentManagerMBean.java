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

package org.mobicents.slee.container.deployment.jboss;

import java.net.URL;

import org.jboss.deployers.spi.DeploymentException;

public interface DeploymentManagerMBean {

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
