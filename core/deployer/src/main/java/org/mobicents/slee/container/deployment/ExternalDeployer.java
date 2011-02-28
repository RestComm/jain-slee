package org.mobicents.slee.container.deployment;

/**
 * Interface for the deployer, out of the SLEE Container.
 * 
 * @author martins
 * 
 */
public interface ExternalDeployer {

	/**
	 * Sets the deployer inside the SLEE container. Deployments may be started
	 * by the deployer.
	 * 
	 * @param internalDeployer
	 */
	public void setInternalDeployer(InternalDeployer internalDeployer);

}
