/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2017, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

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
