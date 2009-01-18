/**
 * Start time:17:09:19 2009-01-18<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile;

import org.mobicents.slee.container.component.deployment.jaxb.slee.profile.ProfileManagementAbstractClassName;
import org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.ProfileAbstractClass;

/**
 * Start time:17:09:19 2009-01-18<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ProfileSpecManagementAbstractClass {

	private ProfileManagementAbstractClassName profileManagementAbstractClass=null;
	private ProfileAbstractClass llProfileManagementAbstractClass=null;
	
	//FIXME: reentrant is boolean
	private String description, profileAbstractClassName,reentrant;
	
	public ProfileSpecManagementAbstractClass(ProfileManagementAbstractClassName profileManagementAbstractClass) {
		this.profileManagementAbstractClass=profileManagementAbstractClass;
	}
	public ProfileSpecManagementAbstractClass(ProfileAbstractClass llProfileManagementAbstractClass) {
		this.llProfileManagementAbstractClass=llProfileManagementAbstractClass;
	}
	public String getProfileAbstractClassName() {
		return profileAbstractClassName;
	}
	public String getDescription() {
		return description;
	}
	public String getReentrant() {
		return reentrant;
	}

	
	
}
