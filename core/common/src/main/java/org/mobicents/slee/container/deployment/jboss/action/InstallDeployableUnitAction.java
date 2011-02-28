package org.mobicents.slee.container.deployment.jboss.action;

import javax.slee.management.DeploymentMBean;

/**
 * 
 * @author martins
 *
 */
public class InstallDeployableUnitAction extends DeployManagementAction {

	public InstallDeployableUnitAction(String duURL,
			DeploymentMBean deploymentMBean) {
		super(duURL, deploymentMBean);
	}

	@Override
	public void invoke() throws Exception {
		getDeploymentMBean().install(getDuURL());
	}

}
