/**
 * Start time:14:31:58 2009-01-19<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile;

import javax.slee.management.DeploymentException;

import org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.UsageParameter;

/**
 * Start time:14:31:58 2009-01-19<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ProfileUsageParameter {

	private UsageParameter usageParameter = null;
	private String notificationEnabled, name;

	public ProfileUsageParameter(UsageParameter usageParameter) {
		this.usageParameter = usageParameter;
//		if (this.usageParameter.getName() == null
//				|| this.usageParameter.getName().compareTo("") == 0) {
//			throw new DeploymentException(
//					"Usage parameter name can not be null or empty");
//		}
		this.name = this.usageParameter.getName();
		this.notificationEnabled = this.usageParameter
				.getNotificationsEnabled();
	}

	public String getNotificationEnabled() {
		return notificationEnabled;
	}

	public String getName() {
		return name;
	}

}
