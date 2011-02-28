package org.mobicents.slee.container.deployment.jboss.action;

import javax.slee.management.DeployableUnitID;
import javax.slee.management.DeploymentMBean;

/**
 * 
 * @author martins
 *
 */
public class UninstallDeployableUnitAction extends DeployManagementAction {

	public UninstallDeployableUnitAction(String duURL,
			DeploymentMBean deploymentMBean) {
		super(duURL, deploymentMBean);
	}

	@Override
	public void invoke() throws Exception {
		getDeploymentMBean().uninstall(new DeployableUnitID(getDuURL()));
	}

}
