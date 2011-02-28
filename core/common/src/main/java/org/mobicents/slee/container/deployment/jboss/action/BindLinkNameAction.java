package org.mobicents.slee.container.deployment.jboss.action;

import org.mobicents.slee.container.management.ResourceManagement;

/**
 * 
 * @author martins
 * 
 */
public class BindLinkNameAction extends ResourceManagementAction {

	private final String linkName;
	private final String raEntity;

	public BindLinkNameAction(String linkName, String raEntity,
			ResourceManagement resourceManagement) {
		super(resourceManagement);
		this.linkName = linkName;
		this.raEntity = raEntity;
	}

	@Override
	public void invoke() throws Exception {
		getResourceManagement().bindLinkName(linkName, raEntity);
	}

	@Override
	public String toString() {
		return "BindLinkNameAction[" + linkName + "," + raEntity + "]";
	}

}
