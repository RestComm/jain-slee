package org.mobicents.slee.container.deployment.jboss.action;

import javax.slee.management.ResourceAdaptorEntityState;

import org.mobicents.slee.container.management.ResourceManagement;
import org.mobicents.slee.container.resource.ResourceAdaptorEntity;

/**
 * 
 * @author martins
 *
 */
public class DeactivateResourceAdaptorEntityAction extends
		ResourceManagementAction {

	private final String raEntityName;

	public DeactivateResourceAdaptorEntityAction(String raEntityName,
			ResourceManagement resourceManagement) {
		super(resourceManagement);
		this.raEntityName = raEntityName;
	}

	public String getRaEntity() {
		return raEntityName;
	}
	
	@Override
	public void invoke() throws Exception {
		final ResourceManagement resourceManagement = getResourceManagement();
		final ResourceAdaptorEntity resourceAdaptorEntity = resourceManagement.getResourceAdaptorEntity(raEntityName);
		if (resourceAdaptorEntity != null) {
			// deactivate if needed
			if (resourceAdaptorEntity.getState() == ResourceAdaptorEntityState.ACTIVE) {
				resourceManagement.deactivateResourceAdaptorEntity(raEntityName);
			}
			// continue once entity is inactive
			while(resourceAdaptorEntity.getState() != ResourceAdaptorEntityState.INACTIVE) {
				try {
					Thread.sleep(1000);					
				}
				catch (Exception e) {
					// ignore
				}				
			}						
		}
	}

	@Override
	public String toString() {
		return "DeactivateResourceAdaptorEntityAction[" + raEntityName + "]";
	}

}
