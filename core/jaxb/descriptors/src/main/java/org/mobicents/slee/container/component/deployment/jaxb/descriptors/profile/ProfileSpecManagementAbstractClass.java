/**
 * Start time:17:09:19 2009-01-18<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile;

import javax.slee.management.DeploymentException;

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

	private ProfileManagementAbstractClassName profileManagementAbstractClass = null;
	private ProfileAbstractClass llProfileManagementAbstractClass = null;

	// FIXME: reentrant is boolean
	private String description, profileAbstractClassName, reentrant;

	public ProfileSpecManagementAbstractClass(
			ProfileManagementAbstractClassName profileManagementAbstractClass){
		this.profileManagementAbstractClass = profileManagementAbstractClass;
		this.profileAbstractClassName = this.profileManagementAbstractClass
				.getvalue();
	}

	public ProfileSpecManagementAbstractClass(
			ProfileAbstractClass llProfileManagementAbstractClass){
		this.llProfileManagementAbstractClass = llProfileManagementAbstractClass;
		this.description = this.llProfileManagementAbstractClass
				.getDescription() == null ? null
				: this.llProfileManagementAbstractClass.getDescription()
						.getvalue();
		this.reentrant = this.llProfileManagementAbstractClass.getReentrant();
//		if (this.llProfileManagementAbstractClass.getProfileAbstractClassName() == null
//				|| this.llProfileManagementAbstractClass
//						.getProfileAbstractClassName().getvalue() == null
//				|| this.llProfileManagementAbstractClass
//						.getProfileAbstractClassName().getvalue().compareTo("") == 0)
//			throw new DeploymentException(
//					"Abstract Profile Management Class cant be of null or empty value");
		this.profileAbstractClassName = this.llProfileManagementAbstractClass
				.getProfileAbstractClassName().getvalue();

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
