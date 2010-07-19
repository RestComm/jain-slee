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
