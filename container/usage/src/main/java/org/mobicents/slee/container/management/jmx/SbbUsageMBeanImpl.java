/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
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
		return new MBeanNotificationInfo[] { new MBeanNotificationInfo(
				notificationTypes, UsageNotification.class.getName(),
				"JAIN SLEE 1.0 Usage MBean notification") };
	}
	
	public SbbID getSbb() throws ManagementException {
		return sbb;
	}

	public ServiceID getService() throws ManagementException {
		return service;
	}
	
}
