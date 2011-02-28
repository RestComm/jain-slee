package org.mobicents.slee.container.deployment;

import java.net.URL;

import org.jboss.deployment.DeploymentException;

/**
 * The interface for the deployer inside the SLEE Container.
 * 
 * @author martins
 * 
 */
@SuppressWarnings("deprecation")
public interface InternalDeployer {

	/**
	 * Sets the external deployer.
	 * 
	 * @param deployer
	 */
	public void setExternalDeployer(ExternalDeployer deployer);

	/**
	 * 
	 * @param deployableUnitURL
	 * @return
	 */
	public boolean accepts(URL deployableUnitURL);

	/**
	 * 
	 * @param componentURL
	 * @throws DeploymentException
	 */
	public void init(URL componentURL) throws DeploymentException;

	/**
	 * 
	 * @param componentURL
	 * @throws DeploymentException
	 */
	public void start(URL componentURL) throws DeploymentException;

	/**
	 * 
	 * @param deployableUnitURL
	 * @throws DeploymentException
	 */
	public void stop(URL deployableUnitURL) throws DeploymentException;

}
