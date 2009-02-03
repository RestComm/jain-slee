/**
 * Start time:23:53:54 2009-01-18<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.deployment.jaxb.descriptors.common;

import javax.slee.management.DeploymentException;

import org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.SecurityPermissions;

/**
 * Start time:23:53:54 2009-01-18<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MSecurityPermision {

	private String description, securityPermissionSpec;

	
	public MSecurityPermision(String description, String securityPermissionSpec) {
		super();
		this.description = description;
		this.securityPermissionSpec = securityPermissionSpec;
	}

	public String getSecurityPermissionSpec() {
		return securityPermissionSpec;
	}

	public String getDescription() {
		return description;
	}

}
