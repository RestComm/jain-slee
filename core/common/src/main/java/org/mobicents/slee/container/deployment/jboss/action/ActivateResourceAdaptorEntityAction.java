package org.mobicents.slee.container.deployment.jboss.action;

import org.mobicents.slee.container.management.ResourceManagement;

/**
 * 
 * @author martins
 *
 */
public class ActivateResourceAdaptorEntityAction extends
		ResourceManagementAction {

	private final String raEntity;

	public ActivateResourceAdaptorEntityAction(String raEntity,
			ResourceManagement resourceManagement) {
		super(resourceManagement);
		this.raEntity = raEntity;
	}

	@Override
	public void invoke() throws Exception {
		getResourceManagement().activateResourceAdaptorEntity(raEntity);
	}

	@Override
	public String toString() {
		return "ActivateResourceAdaptorEntityAction[" + raEntity + "]";
	}

}
