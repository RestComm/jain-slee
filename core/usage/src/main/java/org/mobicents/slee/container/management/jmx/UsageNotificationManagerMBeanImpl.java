package org.mobicents.slee.container.management.jmx;

import java.util.concurrent.ConcurrentHashMap;

import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.management.StandardMBean;
import javax.slee.management.ManagementException;
import javax.slee.management.NotificationSource;
import javax.slee.usage.UsageNotificationManagerMBean;

import org.mobicents.slee.container.component.SleeComponentWithUsageParametersInterface;
import org.mobicents.slee.container.component.UsageParameterDescriptor;

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
	private ObjectName objectName;
	private final ConcurrentHashMap<String, Boolean> paramNames = new ConcurrentHashMap<String, Boolean>();
	
	private boolean isSlee11 = false;
	
	public UsageNotificationManagerMBeanImpl(Class<?> interfaceName,
			NotificationSource notificationSource, SleeComponentWithUsageParametersInterface component)
			throws NotCompliantMBeanException, ClassNotFoundException {
		super(interfaceName);
		this.notificationSource = notificationSource;
		if(!component.isSlee11())
		{
			this.isSlee11=false;
			return;
		}else
		{
			for(UsageParameterDescriptor param:component.getUsageParametersList())
			{
				this.setNotificationsEnabled(param.getName(), param.getNotificationsEnabled());
			}	
		}
		
	}
	
	public void close() throws ManagementException {
		// ignore
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
		
		if(!isSlee11)
		{
			if (areNotificationsEnabled == null
				|| areNotificationsEnabled.booleanValue()) {
			// considering that notifications are enabled, by default, for each
			// param
				return true;
			} else {
				return false;
			}
		}else
		{
			if (areNotificationsEnabled != null
					&& areNotificationsEnabled.booleanValue()) {
				// considering that notifications are enabled, by default, for each
				// param
					return true;
				} else {
					return false;
				}
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
	
}
