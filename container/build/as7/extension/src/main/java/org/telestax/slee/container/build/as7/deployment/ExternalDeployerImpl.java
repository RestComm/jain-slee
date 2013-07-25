package org.telestax.slee.container.build.as7.deployment;

import org.mobicents.slee.container.deployment.ExternalDeployer;
import org.mobicents.slee.container.deployment.InternalDeployer;

public class ExternalDeployerImpl implements ExternalDeployer {

	private InternalDeployer internalDeployer;
	
	@Override
	public void setInternalDeployer(InternalDeployer internalDeployer) {
		this.internalDeployer = internalDeployer;		
	}

}
