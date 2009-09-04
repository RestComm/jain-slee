package org.mobicents.slee.container.management.jmx;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.management.ListenerNotFoundException;
import javax.management.MBeanNotificationInfo;
import javax.management.NotCompliantMBeanException;
import javax.management.NotificationBroadcaster;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.StandardMBean;
import javax.slee.InvalidStateException;
import javax.slee.management.ManagementException;
import javax.slee.management.NotificationSource;
import javax.slee.management.ProfileTableNotification;
import javax.slee.management.ResourceAdaptorEntityNotification;
import javax.slee.management.SbbNotification;
import javax.slee.management.SubsystemNotification;
import javax.slee.usage.UsageMBean;
import javax.slee.usage.UsageNotification;

public class UsageMBeanImpl extends StandardMBean implements UsageMBean,
		NotificationBroadcaster {

	private static final MBeanNotificationInfo[] notificationInfo = initNotificationInfo();
	
	/**
	 * Initiates the notification info for usage mbeans
	 * @return
	 */
	private static MBeanNotificationInfo[] initNotificationInfo() {
		String[] notificationTypes = new String[] { 
				ProfileTableNotification.USAGE_NOTIFICATION_TYPE,
				ResourceAdaptorEntityNotification.USAGE_NOTIFICATION_TYPE,
				SbbNotification.USAGE_NOTIFICATION_TYPE,
				SubsystemNotification.USAGE_NOTIFICATION_TYPE
				};
		MBeanNotificationInfo[] mbeanNotificationInfo = new MBeanNotificationInfo[] { new MBeanNotificationInfo(
				notificationTypes, UsageNotification.class.getName(),
				"JAIN SLEE 1.1 Usage MBean Notification") };

		return mbeanNotificationInfo;
	}
		
	private InstalledUsageParameterSet usageParameterSet;
	private UsageMBeanImplParent parent;
	private final NotificationSource notificationSource;
	private ObjectName objectName;
	
	public UsageMBeanImpl(Class<?> mbeanInterface, NotificationSource notificationSource) throws NotCompliantMBeanException,
			ClassNotFoundException {
		super(mbeanInterface);
		this.notificationSource = notificationSource;
	}

	public void setParent(UsageMBeanImplParent parent) {
		this.parent = parent;
	}
	
	public ObjectName getObjectName() {
		return objectName;
	}
	
	public void setObjectName(ObjectName objectName) {
		this.objectName = objectName;
	}
	
	public void setUsageParameter(
			InstalledUsageParameterSet usageParameterSet) {
		this.usageParameterSet = usageParameterSet;
	}
	
	public InstalledUsageParameterSet getUsageParameter() {
		return usageParameterSet;
	}
	
	public void close() throws InvalidStateException, ManagementException {
		if (listeners.size() != 0) {
			throw new InvalidStateException(
					"Could not close Usage MBean listeners still attached!");
		}		
	}

	public NotificationSource getNotificationSource()
			throws ManagementException {
		return notificationSource;
	}

	public ObjectName getUsageNotificationManagerMBean()
			throws ManagementException {
		UsageNotificationManagerMBeanImpl notificationManager = parent.getUsageNotificationManagerMBean(notificationSource);
		return notificationManager != null ? notificationManager.getObjectName() : null;
	}

	public String getUsageParameterSet() throws ManagementException {
		return usageParameterSet.getName();
	}

	public void resetAllUsageParameters() throws ManagementException {
		usageParameterSet.reset();
	}

	// --- NOTIFICATION BROADCASTER IMPL

	private final Map<NotificationListener, ListenerFilterHandbackTriplet> listeners = new ConcurrentHashMap<NotificationListener, ListenerFilterHandbackTriplet>();

	public void addNotificationListener(NotificationListener listener,
			NotificationFilter filter, Object handback)
			throws IllegalArgumentException {
		if (listener == null) {
			throw new IllegalArgumentException("null listener");
		} else {
			this.listeners.put(listener, new ListenerFilterHandbackTriplet(
					listener, filter, handback));
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.management.NotificationBroadcaster#getNotificationInfo()
	 */
	public MBeanNotificationInfo[] getNotificationInfo() {
		return notificationInfo;
	}

	public void removeNotificationListener(NotificationListener listener)
			throws ListenerNotFoundException {
		ListenerFilterHandbackTriplet triplet = listeners.remove(listener);
		if (triplet == null) {
			throw new ListenerNotFoundException();
		}
	}

	/**
	 * Send the notification.
	 * 
	 * @param value
	 * @param seqno
	 * @param usageParameterSetName
	 * @param usageParameterName
	 * @param isCounter
	 */
	public void sendUsageNotification(long value, long seqno,
			String usageParameterSetName, String usageParameterName,
			boolean isCounter) {
		UsageNotificationManagerMBeanImpl notificationManager = parent
				.getUsageNotificationManagerMBean(notificationSource);
		if (notificationManager == null
				|| notificationManager
						.getNotificationsEnabled(usageParameterName)) {
			// if the notification manager is null we consider the notification
			// can be sent
			UsageNotification notification = createUsageNotification(value,
					seqno, usageParameterSetName, usageParameterName, isCounter);
			for (ListenerFilterHandbackTriplet triplet : listeners.values()) {
				if (triplet.notificationFilter == null
						|| triplet.notificationFilter
								.isNotificationEnabled(notification)) {
					triplet.notificationListener.handleNotification(
							notification, triplet.handbackObject);
				}
			}
		}
	}

	/**
	 * Creates an instance of an {@link UsageNotification} for the specified
	 * args. This operation is exposed to allow it be overriden by
	 * {@link SbbUsageMBean} impl.
	 * 
	 * @param value
	 * @param seqno
	 * @param usageParameterSetName
	 * @param usageParameterName
	 * @param isCounter
	 * @return
	 */
	protected UsageNotification createUsageNotification(long value, long seqno,
			String usageParameterSetName, String usageParameterName,
			boolean isCounter) {
		return new UsageNotification(notificationSource
				.getUsageNotificationType(), this, notificationSource,
				usageParameterSetName, usageParameterName, isCounter, value,
				seqno, System.currentTimeMillis());
	}

	class ListenerFilterHandbackTriplet {

		NotificationListener notificationListener;

		NotificationFilter notificationFilter;

		Object handbackObject;

		public ListenerFilterHandbackTriplet(NotificationListener listener,
				NotificationFilter notificationFilter, Object handbackObject) {
			this.notificationListener = listener;
			this.notificationFilter = notificationFilter;
			this.handbackObject = handbackObject;
		}

		@Override
		public int hashCode() {
			return notificationListener.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			if (obj != null && obj.getClass() == this.getClass()) {
				return ((ListenerFilterHandbackTriplet) obj).notificationListener == this.notificationListener;
			} else {
				return false;
			}
		}
	}

}
