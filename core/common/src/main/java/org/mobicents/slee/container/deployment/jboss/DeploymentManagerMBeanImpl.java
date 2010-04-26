package org.mobicents.slee.container.deployment.jboss;

import javax.management.NotCompliantMBeanException;
import javax.management.StandardMBean;

public class DeploymentManagerMBeanImpl extends StandardMBean implements DeploymentManagerMBean {

	public DeploymentManagerMBeanImpl()
			throws NotCompliantMBeanException {
		super(DeploymentManagerMBean.class);
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.deployment.jboss.DeploymentManagerMBean#showStatus()
	 */
	public String showStatus() {
		return DeploymentManager.INSTANCE.showStatus();
	}

}
