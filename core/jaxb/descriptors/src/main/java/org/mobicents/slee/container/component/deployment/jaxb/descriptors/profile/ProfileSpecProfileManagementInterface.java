/**
 * Start time:17:00:48 2009-01-18<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile;


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
public class ProfileSpecProfileManagementInterface {


	private ProfileManagementInterface llProfileManagementInterface=null;
	private ProfileManagementInterfaceName profileManagementInterface=null;
	
	private String description,profileManagementName=null;
	public ProfileSpecProfileManagementInterface(ProfileManagementInterface llProfileManagementInterface) {
		this.llProfileManagementInterface=llProfileManagementInterface;
	}

	public ProfileSpecProfileManagementInterface(ProfileManagementInterfaceName profileManagementInterface) {
		this.profileManagementInterface=profileManagementInterface;
	}

	public String getDescription() {
		return description;
	}

	public String getProfileManagementName() {
		return profileManagementName;
	}
	
	
	
}
