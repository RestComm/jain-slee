package org.mobicents.slee.container.management.jmx;

import java.util.concurrent.ConcurrentHashMap;

import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.management.StandardMBean;
import javax.slee.management.ManagementException;
import javax.slee.management.NotificationSource;
import javax.slee.usage.UsageNotificationManagerMBean;

/**
 * Base implementation of the {@link UsageNotificationManagerMBean} interface
 * from SLEE 1.1 specs. Concrete classes for the user specified interfaces must
 * extend this class.
 * 
 * @author martins
 * 
 */
public class UsageNotificationManagerMBeanImpl extends StandardMBean implements
		UsageNotificationManagerMBean {

	private final NotificationSource notificationSource;
	private UsageNotificationManagerMBeanImplParent parent;
	private ObjectName objectName;
	private final ConcurrentHashMap<String, Boolean> paramNames = new ConcurrentHashMap<String, Boolean>();

	public UsageNotificationManagerMBeanImpl(Class interfaceName,
			NotificationSource notificationSource)
			throws NotCompliantMBeanException, ClassNotFoundException {
		super(interfaceName);
		this.notificationSource = notificationSource;
	}

	public void close() throws ManagementException {
		try {
			parent.removeChild(this);
		} catch (Throwable e) {
			throw new ManagementException(e.getMessage(), e);
		}
	}

	public NotificationSource getNotificationSource()
			throws ManagementException {
		return notificationSource;
	}

	/**
	 * Indicates if notifications are enabled for the specified parameter name
	 * 
	 * @param paramName
	 * @return
	 */
	public boolean getNotificationsEnabled(String paramName) {
		
		Boolean areNotificationsEnabled = paramNames.get(paramName);
		if (areNotificationsEnabled != null
				&& areNotificationsEnabled.booleanValue()) {
			// considering that notifications are enabled, by default, for each
			// param
			return true;
		} else {
			return false;
		}
	}

	public void setNotificationsEnabled(String paramName, boolean enabled) {
		
		paramNames.put(paramName, Boolean.valueOf(enabled));
	}

	public ObjectName getObjectName() {
		return objectName;
	}
	
	public void setObjectName(ObjectName objectName) {
		this.objectName = objectName;
	}
	
	public void setParent(UsageNotificationManagerMBeanImplParent parent) {
		this.parent = parent;
	}
}
