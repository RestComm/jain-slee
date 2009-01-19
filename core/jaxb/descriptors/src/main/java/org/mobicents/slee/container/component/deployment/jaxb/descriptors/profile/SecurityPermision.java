/**
 * Start time:23:53:54 2009-01-18<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile;

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
public class SecurityPermision {

	private SecurityPermissions secPerm = null;
	private String description, securityPermissionSpec;

	public SecurityPermision(SecurityPermissions secPerm)
			throws DeploymentException {
		super();
		this.secPerm = secPerm;

		this.description = secPerm.getDescription() == null ? null
				: this.secPerm.getDescription().getvalue();
		if (this.secPerm.getSecurityPermissionSpec() == null
				|| this.secPerm.getSecurityPermissionSpec().getvalue() == null
				|| this.secPerm.getSecurityPermissionSpec().getvalue()
						.compareTo("") == 0) {
			throw new DeploymentException(
					"Security permision specification can not be null or empty when specified");
		}

		this.securityPermissionSpec = this.secPerm.getSecurityPermissionSpec()
				.getvalue();

	}

	public String getSecurityPermissionSpec() {
		return securityPermissionSpec;
	}

	public String getDescription() {
		return description;
	}

}
