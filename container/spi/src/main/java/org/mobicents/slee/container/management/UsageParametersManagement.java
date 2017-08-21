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

/**
 * 
 */
package org.mobicents.slee.container.management;

import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;

import org.mobicents.slee.container.SleeContainerModule;
import org.mobicents.slee.container.component.profile.ProfileSpecificationComponent;
import org.mobicents.slee.container.component.ra.ResourceAdaptorComponent;
import org.mobicents.slee.container.component.service.ServiceComponent;
import org.mobicents.slee.container.management.jmx.ProfileTableUsageMBean;
import org.mobicents.slee.container.management.jmx.ResourceUsageMBean;
import org.mobicents.slee.container.management.jmx.ServiceUsageMBean;

/**
 * @author martins
 * 
 */
public interface UsageParametersManagement extends SleeContainerModule {

	/**
	 * 
	 * @param profileTableName
	 * @param component
	 * @return
	 * @throws NotCompliantMBeanException
	 * @throws MalformedObjectNameException
	 * @throws NullPointerException
	 */
	public ProfileTableUsageMBean newProfileTableUsageMBean(
			String profileTableName, ProfileSpecificationComponent component)
			throws NotCompliantMBeanException, MalformedObjectNameException,
			NullPointerException;

	/**
	 * 
	 * @param entityName
	 * @param component
	 * @return
	 * @throws NotCompliantMBeanException
	 * @throws MalformedObjectNameException
	 * @throws NullPointerException
	 */
	public ResourceUsageMBean newResourceUsageMBean(String entityName,
			ResourceAdaptorComponent component)
			throws NotCompliantMBeanException, MalformedObjectNameException,
			NullPointerException;

	/**
	 * 
	 * @param component
	 * @return
	 * @throws NotCompliantMBeanException
	 * @throws MalformedObjectNameException
	 * @throws NullPointerException
	 */
	public ServiceUsageMBean newServiceUsageMBean(ServiceComponent component)
			throws NotCompliantMBeanException, MalformedObjectNameException,
			NullPointerException;

}
