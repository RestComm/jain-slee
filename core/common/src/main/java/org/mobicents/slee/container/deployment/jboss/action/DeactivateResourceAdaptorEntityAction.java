package org.mobicents.slee.container.deployment.jboss.action;

import org.mobicents.slee.container.management.ResourceManagement;

/**
 * 
 * @author martins
 *
 */
public class DeactivateResourceAdaptorEntityAction extends
		ResourceManagementAction {

	private final String raEntity;

	public DeactivateResourceAdaptorEntityAction(String raEntity,
			ResourceManagement resourceManagement) {
		super(resourceManagement);
		this.raEntity = raEntity;
	}

	public String getRaEntity() {
		return raEntity;
	}
	
	@Override
	public void invoke() throws Exception {
		getResourceManagement().deactivateResourceAdaptorEntity(raEntity);
	}

	@Override
	public String toString() {
		return "DeactivateResourceAdaptorEntityAction[" + raEntity + "]";
	}

}
