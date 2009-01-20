/**
 * Start time:12:38:40 2009-01-20<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb;

import org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.UsageParameter;

/**
 * Start time:12:38:40 2009-01-20<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MSbbUsageParameter {

	private UsageParameter usageParameter=null;
	
	private String name=null;
	private boolean notificationEnable=false;
	public MSbbUsageParameter(UsageParameter usageParameter) {
		super();
		this.usageParameter = usageParameter;
		this.name=this.usageParameter.getName();
		this.notificationEnable=Boolean.parseBoolean(this.usageParameter.getNotificationsEnabled());
	}
	public String getName() {
		return name;
	}
	public boolean isNotificationEnable() {
		return notificationEnable;
	}
	
	
	
	
}
