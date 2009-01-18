/**
 * Start time:17:19:06 2009-01-18<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile;


import org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.ProfileUsageParametersInterface;

/**
 * Start time:17:19:06 2009-01-18<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ProfileSpecProfileUsageParameterInterface {

	private String description,profileUsagePamaterersInterfaceName=null;
	private ProfileUsageParametersInterface profileUsageParametersInterface=null;
	public String getDescription() {
		return description;
	}
	public String getProfileUsagePamaterersInterfaceName() {
		return profileUsagePamaterersInterfaceName;
	}
	public ProfileSpecProfileUsageParameterInterface(
			ProfileUsageParametersInterface profileUsageParametersInterface) {
		super();
		this.profileUsageParametersInterface = profileUsageParametersInterface;
	}
	


}
