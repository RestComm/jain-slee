package org.mobicents.slee.container.congestion;

import javax.slee.management.NotificationSource;

import org.jgroups.Address;

public class CongestionControlNotification implements NotificationSource {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String ALARM_NOTIFICATION_TYPE = "org.mobicents.slee.management.alarm.congestion";

    public static final String TRACE_NOTIFICATION_TYPE = "org.mobicents.slee.management.trace.congestion";

    public static final String USAGE_NOTIFICATION_TYPE = "org.mobicents.slee.management.usage.congestion";

    private final String localAddress;
    
    public CongestionControlNotification(Address localAddress) {
		this.localAddress = localAddress == null ? "localhost" : localAddress.toString();
	}
    
    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null) return false;
        if (obj.getClass() == this.getClass()) {
        	
        	return ((CongestionControlNotification)obj).localAddress.equals(localAddress);
        }
        else {
        	return false;
        }
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return localAddress.hashCode();
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return new StringBuilder("CongestionControlNotification[localAddress=").append(localAddress).append(']').toString();
    }

    /*
     * (non-Javadoc)
     * @see javax.slee.management.NotificationSource#compareTo(java.lang.Object)
     */
	public int compareTo(Object obj) {
    	if (obj == this) return 0;
        if (obj == null) throw new NullPointerException();
        if (obj.getClass() == this.getClass()) {
            return ((CongestionControlNotification) obj).localAddress.compareTo(this.localAddress);
        }
        else {
            return -1;
        }
    }
    
    /*
     * (non-Javadoc)
     * @see javax.slee.management.NotificationSource#getAlarmNotificationType()
     */
	public String getAlarmNotificationType() {
		return ALARM_NOTIFICATION_TYPE;
	}

	/*
	 * (non-Javadoc)
	 * @see javax.slee.management.NotificationSource#getTraceNotificationType()
	 */
	public String getTraceNotificationType() {
		return TRACE_NOTIFICATION_TYPE;
	}

	/*
	 * (non-Javadoc)
	 * @see javax.slee.management.NotificationSource#getUsageNotificationType()
	 */
	public String getUsageNotificationType() {
		return USAGE_NOTIFICATION_TYPE;
	}

	/**
	 * 
	 * @return
	 */
	public String getLocalAddress() {
		return localAddress;
	}
}
