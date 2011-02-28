package org.mobicents.slee.container.deployment.jboss.action;

import org.mobicents.slee.container.management.ResourceManagement;

/**
 * 
 * @author martins
 *
 */
public class UnbindLinkNameAction extends ResourceManagementAction {

	private final String linkName;

	public UnbindLinkNameAction(String linkName,
			ResourceManagement resourceManagement) {
		super(resourceManagement);
		this.linkName = linkName;
	}

	@Override
	public String toString() {
		return "UnbindLinkNameAction[" + linkName + "]";
	}

	@Override
	public void invoke() throws Exception {
		getResourceManagement().unbindLinkName(linkName);
	}

}
