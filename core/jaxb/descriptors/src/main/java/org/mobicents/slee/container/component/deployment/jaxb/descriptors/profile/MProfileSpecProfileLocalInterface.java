/**
 * Start time:16:56:24 2009-01-18<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile;

import javax.slee.management.DeploymentException;

import org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.ProfileLocalInterface;

/**
 * Start time:16:56:24 2009-01-18<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MProfileSpecProfileLocalInterface {

	// Again its 1.1 only
	private ProfileLocalInterface profileLocalInterface = null;
	// FIXME: Maybe this should be boolean?
	private String isolateSecurityPermissions = null;
	private String description, profileLocalInterfaceName = null;

	public MProfileSpecProfileLocalInterface(
			ProfileLocalInterface profileLocalInterface){

		this.profileLocalInterface = profileLocalInterface;
		// init
		this.description = this.profileLocalInterface.getDescription() == null ? null
				: this.profileLocalInterface.getDescription().getvalue();

//		if (this.profileLocalInterface.getProfileLocalInterfaceName() == null
//				|| this.profileLocalInterface.getProfileLocalInterfaceName()
//						.getvalue() == null
//				|| this.profileLocalInterface.getProfileLocalInterfaceName()
//						.getvalue().compareTo("") == 0) {
//
//			throw new DeploymentException("Profile local interface class name can not be null or empty");
//		}
		this.profileLocalInterfaceName=this.profileLocalInterface.getProfileLocalInterfaceName().getvalue();
		this.isolateSecurityPermissions=this.profileLocalInterface.getIsolateSecurityPermissions();

	}

	public String getIsolateSecurityPermissions() {
		return isolateSecurityPermissions;
	}

	public String getDescription() {
		return description;
	}

	public String getProfileLocalInterfaceName() {
		return profileLocalInterfaceName;
	}

	
	
}
