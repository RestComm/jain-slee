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

package org.mobicents.slee.container.management;

import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;

import org.mobicents.slee.container.AbstractSleeContainerModule;
import org.mobicents.slee.container.component.profile.ProfileSpecificationComponent;
import org.mobicents.slee.container.component.ra.ResourceAdaptorComponent;
import org.mobicents.slee.container.component.service.ServiceComponent;
import org.mobicents.slee.container.management.jmx.ProfileTableUsageMBean;
import org.mobicents.slee.container.management.jmx.ProfileTableUsageMBeanImpl;
import org.mobicents.slee.container.management.jmx.ResourceUsageMBean;
import org.mobicents.slee.container.management.jmx.ResourceUsageMBeanImpl;
import org.mobicents.slee.container.management.jmx.ServiceUsageMBean;
import org.mobicents.slee.container.management.jmx.ServiceUsageMBeanImpl;

/**
 * @author martins
 * 
 */
public class UsageParametersManagementImpl extends AbstractSleeContainerModule
		implements UsageParametersManagement {

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.slee.container.management.UsageParametersManagement#
	 * newProfileTableUsageMBean(java.lang.String,
	 * org.mobicents.slee.core.component.profile.ProfileSpecificationComponent)
	 */
	public ProfileTableUsageMBean newProfileTableUsageMBean(
			String profileTableName, ProfileSpecificationComponent component)
			throws NotCompliantMBeanException, MalformedObjectNameException,
			NullPointerException {
		return new ProfileTableUsageMBeanImpl(profileTableName, component,
				sleeContainer);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.slee.container.management.UsageParametersManagement#
	 * newResourceUsageMBean(java.lang.String,
	 * org.mobicents.slee.core.component.ra.ResourceAdaptorComponent)
	 */
	public ResourceUsageMBean newResourceUsageMBean(String entityName,
			ResourceAdaptorComponent component)
			throws NotCompliantMBeanException, MalformedObjectNameException,
			NullPointerException {
		return new ResourceUsageMBeanImpl(entityName, component, sleeContainer);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.slee.container.management.UsageParametersManagement#
	 * newServiceUsageMBean
	 * (org.mobicents.slee.core.component.service.ServiceComponent)
	 */
	public ServiceUsageMBean newServiceUsageMBean(ServiceComponent component)
			throws NotCompliantMBeanException, MalformedObjectNameException,
			NullPointerException {
		return new ServiceUsageMBeanImpl(component, sleeContainer);
	}

}
