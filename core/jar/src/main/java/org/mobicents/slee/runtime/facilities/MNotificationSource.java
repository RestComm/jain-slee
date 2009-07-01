package org.mobicents.slee.runtime.facilities;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

import javax.slee.management.NotificationSource;

/**
 * TODO javadocs
 * @author baranowb
 *
 */
public class MNotificationSource implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private NotificationSource notificationSource = null;
	
	/**
	 * 
	 */
	private final AtomicLong sequence = new AtomicLong(0);
	
	/**
	 * 
	 * @param notificationSource
	 */
	public MNotificationSource(NotificationSource notificationSource) {
		this.notificationSource = notificationSource;
	}
	
	@Override
	public int hashCode() {
		return ((notificationSource == null) ? 0 : notificationSource.hashCode());		
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MNotificationSource other = (MNotificationSource) obj;
		if (notificationSource == null) {
			if (other.notificationSource != null)
				return false;
		} else if (!notificationSource.equals(other.notificationSource))
			return false;
		return true;
	}
	
	/**
	 * @return
	 */
	public NotificationSource getNotificationSource() {
		return notificationSource;
	}
	
	/**
	 * @return
	 */
	public long getNextSequence() {
		return sequence.getAndIncrement();
	}
	
	@Override
	public String toString() {
		return notificationSource+"@"+super.toString();
	}
	
}