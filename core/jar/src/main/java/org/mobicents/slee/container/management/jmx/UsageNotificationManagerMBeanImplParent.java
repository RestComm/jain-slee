package org.mobicents.slee.container.management.jmx;

/**
 * Interface for callbacks from the child usage notification manager mbean to the parent mbean.
 * 
 * @author martins
 *
 */
public interface UsageNotificationManagerMBeanImplParent {

	/**
	 * call back to ask the parent mbean the removal of a child
	 * @param child
	 */
	public void removeChild(UsageNotificationManagerMBeanImpl child);
	
}
