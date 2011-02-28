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
