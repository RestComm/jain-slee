package org.mobicents.slee.container.management.jmx;

import javax.management.MBeanNotificationInfo;
import javax.management.NotCompliantMBeanException;
import javax.slee.SbbID;
import javax.slee.ServiceID;
import javax.slee.management.ManagementException;
import javax.slee.management.SbbNotification;
import javax.slee.usage.SbbUsageMBean;
import javax.slee.usage.UsageNotification;

/**
 * Implementation of the SbbUsageMBean, extending {@link UsageMBeanImpl}
 * 
 * @author Eduardo Martins
 * 
 */
public class SbbUsageMBeanImpl extends UsageMBeanImpl implements SbbUsageMBean {

	private final ServiceID service;
	private final SbbID sbb;
	
	public SbbUsageMBeanImpl(Class mbeanInterface, SbbNotification notificationSource) throws NotCompliantMBeanException,
			ClassNotFoundException {
		super(mbeanInterface,notificationSource);
		this.service = notificationSource.getService();
		this.sbb = notificationSource.getSbb();		
	}

	@Override
	protected UsageNotification createUsageNotification(long value, long seqno,
			String usageParameterSetName, String usageParameterName,
			boolean isCounter) {
		return new UsageNotification(this,service,sbb,usageParameterSetName,
				usageParameterName, isCounter, value, seqno, System
						.currentTimeMillis());
	}

	@Override
	public MBeanNotificationInfo[] getNotificationInfo() {
		String[] notificationTypes = new String[] { SbbUsageMBean.USAGE_NOTIFICATION_TYPE };
		MBeanNotificationInfo[] mbeanNotificationInfo = new MBeanNotificationInfo[] { new MBeanNotificationInfo(
				notificationTypes, UsageNotification.class.getName(),
				"JAIN SLEE 1.0 Usage MBean notification") };

		return mbeanNotificationInfo;
	}
	
	public SbbID getSbb() throws ManagementException {
		return sbb;
	}

	public ServiceID getService() throws ManagementException {
		return service;
	}
	
}
