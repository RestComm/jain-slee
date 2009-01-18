/**
 * Start time:23:53:54 2009-01-18<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile;

import org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.SecurityPermissions;

/**
 * Start time:23:53:54 2009-01-18<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class SecurityPermision {

	private SecurityPermissions secPerm=null;
	private String description, secirityPermissionSpec;
	public SecurityPermision(String secirityPermissionSpec) {
		super();
		this.secirityPermissionSpec = secirityPermissionSpec;
	}
	
	public SecurityPermissions getSecPerm() {
		return secPerm;
	}
	public String getDescription() {
		return description;
	}
	

}
