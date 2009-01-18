/**
 * Start time:16:56:24 2009-01-18<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile;

import org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.ProfileLocalInterface;

/**
 * Start time:16:56:24 2009-01-18<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ProfileSpecProfileLocalInterface {

	//Again its 1.1 only
	private ProfileLocalInterface profileLocalInterface=null;
	//FIXME: Maybe this should be boolean?
	private String isolateSecurityPermissions=null;
	private String description,profileLocalInterfaceName=null;
	public ProfileSpecProfileLocalInterface(ProfileLocalInterface profileLocalInterface) {
		
		this.profileLocalInterface=profileLocalInterface;
		//init
	}

}
