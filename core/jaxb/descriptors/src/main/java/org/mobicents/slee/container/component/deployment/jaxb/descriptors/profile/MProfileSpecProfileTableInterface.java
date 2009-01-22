/**
 * Start time:17:16:27 2009-01-18<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile;

import javax.slee.management.DeploymentException;

import org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.ProfileTableInterface;

/**
 * Start time:17:16:27 2009-01-18<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MProfileSpecProfileTableInterface {

	// This should be String, but lets be conistent

	private String description, profileTableInterfaceName;
	private ProfileTableInterface profileTableInterface = null;

	public MProfileSpecProfileTableInterface(
			ProfileTableInterface profileTableInterface){
		super();
		this.profileTableInterface = profileTableInterface;

//		if (this.profileTableInterface.getProfileTableInterfaceName() == null
//				|| this.profileTableInterface.getProfileTableInterfaceName()
//						.getvalue() == null
//				|| this.profileTableInterface.getProfileTableInterfaceName()
//						.getvalue().compareTo("") == 0) {
//			throw new DeploymentException(
//					"Profile table  interface can not be null or empty when specified");
//		}

		this.description = this.profileTableInterface.getDescription() == null ? null
				: this.profileTableInterface.getDescription().getvalue();
		this.profileTableInterfaceName = this.profileTableInterface
				.getProfileTableInterfaceName().getvalue();
	}

	public String getDescription() {
		return description;
	}

	public String getProfileTableInterfaceName() {
		return profileTableInterfaceName;
	}

}
