/**
 * Start time:17:16:27 2009-01-18<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile;

import org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.ProfileTableInterface;

/**
 * Start time:17:16:27 2009-01-18<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ProfileSpecProfileTableInterface {

	//This should be String, but lets be conistent
	
	private String description, profileTableInterfaceName;
	private ProfileTableInterface profileTableInterface=null;
	public ProfileSpecProfileTableInterface(
			ProfileTableInterface profileTableInterface) {
		super();
		this.profileTableInterface = profileTableInterface;
	}
	public String getDescription() {
		return description;
	}
	public String getProfileTableInterfaceName() {
		return profileTableInterfaceName;
	}
	


}
