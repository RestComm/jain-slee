/**
 * 
 */
package org.mobicents.slee.container.facilities;

import javax.slee.management.NotificationSource;

/**
 * @author baranowb
 *
 */
public interface NotificationSourceWrapper {

	/**
	 * @return
	 */
	public NotificationSource getNotificationSource();
	
	/**
	 * @return
	 */
	public long getNextSequence();
}
