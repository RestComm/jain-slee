package org.mobicents.slee.runtime.facilities;

import java.util.concurrent.atomic.AtomicLong;

import javax.slee.management.NotificationSource;

public class MNotificationSource {

	private NotificationSource notificationSource = null;
	private AtomicLong sequence = new AtomicLong(0);
	
	public MNotificationSource(NotificationSource notificationSource) {
		super();
		this.notificationSource = notificationSource;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((notificationSource == null) ? 0 : notificationSource.hashCode());
		return result;
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
	public NotificationSource getNotificationSource() {
		return notificationSource;
	}
	public long getNextSequence() {
		return sequence.getAndIncrement();
	}

	
	
	
	
}
