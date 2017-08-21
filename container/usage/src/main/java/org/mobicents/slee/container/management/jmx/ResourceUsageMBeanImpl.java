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

import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.slee.SLEEException;
import javax.slee.management.ManagementException;
import javax.slee.management.ResourceAdaptorEntityNotification;
import javax.slee.usage.UsageMBean;
import javax.slee.usage.UsageNotificationManagerMBean;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.ra.ResourceAdaptorComponent;

/**
 * Implementation of the {@link ResourceUsageMBean} from SLEE 1.1 specs.
 * 
 * @author martins
 * 
 */
public class ResourceUsageMBeanImpl extends AbstractUsageMBeanImplParent
		implements ResourceUsageMBean {

	private static final long serialVersionUID = 2670146310843436229L;

	private static final transient Logger logger = Logger
			.getLogger(ResourceUsageMBeanImpl.class);

	/**
	 * the ra entity name for this mbean
	 */
	private String entityName;

	public ResourceUsageMBeanImpl(String entityName,
			ResourceAdaptorComponent component, SleeContainer sleeContainer)
			throws NotCompliantMBeanException, MalformedObjectNameException,
			NullPointerException {
		super(ResourceUsageMBean.class, component,
				new ResourceAdaptorEntityNotification(entityName),
				sleeContainer);
		this.entityName = entityName;
		super.setObjectName(new ObjectName(ResourceUsageMBean.BASE_OBJECT_NAME+','+ResourceUsageMBean.RESOURCE_ADAPTOR_ENTITY_NAME_KEY+'='+ObjectName.quote(entityName)));
		try {
			sleeContainer.getMBeanServer().registerMBean(this, getObjectName());
			createUsageParameterSet();
		} catch (Throwable e) {
			throw new SLEEException(e.getMessage(),e);
		}
	}

	@Override
	protected ObjectName generateUsageNotificationManagerMBeanObjectName()
			throws MalformedObjectNameException, NullPointerException {

		String objectNameAsString = UsageNotificationManagerMBean.BASE_OBJECT_NAME
				+ ','
				+ UsageMBean.NOTIFICATION_SOURCE_KEY
				+ '='
				+ ResourceAdaptorEntityNotification.USAGE_NOTIFICATION_TYPE
				+ ','
				+ ResourceAdaptorEntityNotification.RESOURCE_ADAPTOR_ENTITY_NAME_KEY
				+ '=' + ObjectName.quote(entityName);
		return new ObjectName(objectNameAsString);
	}

	@Override
	protected ObjectName generateUsageParametersMBeanObjectName(String name)
			throws MalformedObjectNameException, NullPointerException {

		String objectNameAsString = UsageMBean.BASE_OBJECT_NAME
				+ (name != null ? "," + UsageMBean.USAGE_PARAMETER_SET_NAME_KEY
						+ '=' + ObjectName.quote(name) : "")
				+ ','
				+ UsageMBean.NOTIFICATION_SOURCE_KEY
				+ '='
				+ ResourceAdaptorEntityNotification.USAGE_NOTIFICATION_TYPE
				+ ','
				+ ResourceAdaptorEntityNotification.RESOURCE_ADAPTOR_ENTITY_NAME_KEY
				+ '=' + ObjectName.quote(entityName);
		return new ObjectName(objectNameAsString);
	}

	@Override
	protected Logger getLogger() {
		return logger;
	}

	@Override
	public String toString() {
		return "Resource Entity " + entityName + " Usage MBean : " 
			+ "\n+-- Usage Parameter Sets: "	+ getUsageParameterNamesSet();
	}
	
	public String getEntityName() throws ManagementException {
		
		ensureMBeanIsNotClosed();
		
		return entityName;
	}
}
