package org.mobicents.slee.container.deployment.jboss.action;

import org.mobicents.slee.container.management.ResourceManagement;

/**
 * 
 * @author martins
 * 
 */
public abstract class ResourceManagementAction implements ManagementAction {

	private final ResourceManagement resourceManagement;

	/**
	 * 
	 * @param sleeContainer
	 */
	public ResourceManagementAction(ResourceManagement resourceManagement) {
		this.resourceManagement = resourceManagement;
	}

	/**
	 * 
	 * @return
	 */
	public ResourceManagement getResourceManagement() {
		return resourceManagement;
	}

	/**
	 * 
	 */
	@Override
	public Type getType() {
		return Type.RESOURCE_MANAGEMENT;
	}

}
