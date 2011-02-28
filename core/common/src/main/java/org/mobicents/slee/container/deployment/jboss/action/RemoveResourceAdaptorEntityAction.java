package org.mobicents.slee.container.deployment.jboss.action;

import org.mobicents.slee.container.management.ResourceManagement;

/**
 * 
 * @author martins
 *
 */
public class RemoveResourceAdaptorEntityAction extends ResourceManagementAction {

	private final String raEntity;

	public RemoveResourceAdaptorEntityAction(String raEntity,
			ResourceManagement resourceManagement) {
		super(resourceManagement);
		this.raEntity = raEntity;
	}

	@Override
	public void invoke() throws Exception {
		getResourceManagement().removeResourceAdaptorEntity(raEntity);
	}

	@Override
	public String toString() {
		return "RemoveResourceAdaptorEntityAction[" + raEntity + "]";
	}

}
