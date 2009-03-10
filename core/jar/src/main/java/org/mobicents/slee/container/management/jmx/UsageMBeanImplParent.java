package org.mobicents.slee.container.management.jmx;

import javax.slee.management.NotificationSource;

/**
 * Interface for callbacks from the child usage mbean to the parent mbean.
 * 
 * @author martins
 * 
 */
public interface UsageMBeanImplParent {

	/**
	 * call back to ask the parent mbean the removal of a child
	 * 
	 * @param usageMBeanImpl
	 */
	public void removeChild(UsageMBeanImpl usageMBeanImpl);

	/**
	 * Retrieves the shared notification manager mbean. It may return null if
	 * the management client used the close() operation directly in the mbean.
	 * 
	 * @return
	 */
	public UsageNotificationManagerMBeanImpl getUsageNotificationManagerMBean(NotificationSource notificationSource);
}
