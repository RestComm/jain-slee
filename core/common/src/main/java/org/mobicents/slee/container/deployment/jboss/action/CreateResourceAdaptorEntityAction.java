package org.mobicents.slee.container.deployment.jboss.action;

import javax.slee.resource.ConfigProperties;
import javax.slee.resource.ResourceAdaptorID;

import org.mobicents.slee.container.management.ResourceManagement;

/**
 * 
 * @author martins
 * 
 */
public class CreateResourceAdaptorEntityAction extends ResourceManagementAction {

	private final ResourceAdaptorID id;
	private final String raEntity;
	private final ConfigProperties properties;

	public CreateResourceAdaptorEntityAction(ResourceAdaptorID id,
			String raEntity, ConfigProperties properties,
			ResourceManagement resourceManagement) {
		super(resourceManagement);
		this.id = id;
		this.raEntity = raEntity;
		this.properties = properties;
	}

	@Override
	public void invoke() throws Exception {
		getResourceManagement().createResourceAdaptorEntity(id, raEntity,
				properties);
	}

	@Override
	public String toString() {
		return "CreateResourceAdaptorEntityAction[" + raEntity + "]";
	}
}
