/**
 * Start time:17:00:48 2009-01-18<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile;

import javax.slee.management.DeploymentException;

import org.mobicents.slee.container.component.deployment.jaxb.slee.profile.ProfileManagementInterfaceName;
import org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.ProfileManagementInterface;

/**
 * Start time:17:00:48 2009-01-18<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MProfileSpecProfileManagementInterface {

	private ProfileManagementInterface llProfileManagementInterface = null;
	private ProfileManagementInterfaceName profileManagementInterface = null;

	private String description, profileManagementName = null;

	public MProfileSpecProfileManagementInterface(
			ProfileManagementInterface llProfileManagementInterface)
			 {
		this.llProfileManagementInterface = llProfileManagementInterface;

//		if (this.llProfileManagementInterface
//				.getProfileManagementInterfaceName() == null
//				|| this.llProfileManagementInterface
//						.getProfileManagementInterfaceName().getvalue() == null
//				|| this.llProfileManagementInterface
//						.getProfileManagementInterfaceName().getvalue()
//						.compareTo("") == 0) {
//			throw new DeploymentException(
//					"Profile management interface can not be null or empty when specified");
//		}

		this.description = this.llProfileManagementInterface.getDescription() == null ? null
				: this.llProfileManagementInterface.getDescription().getvalue();
		
		this.profileManagementName=this.llProfileManagementInterface.getProfileManagementInterfaceName().getvalue();
		

	}

	public MProfileSpecProfileManagementInterface(
			ProfileManagementInterfaceName profileManagementInterface)
{
		this.profileManagementInterface = profileManagementInterface;
//		if (this.profileManagementInterface.getvalue() == null
//				|| this.profileManagementInterface.getvalue().compareTo("") == 0) {
//			throw new DeploymentException(
//					"If sppecified profile management interface name can not be null or empty");
//		}

		this.profileManagementName = this.profileManagementInterface.getvalue();
	}

	public String getDescription() {
		return description;
	}

	public String getProfileManagementName() {
		return profileManagementName;
	}

}
