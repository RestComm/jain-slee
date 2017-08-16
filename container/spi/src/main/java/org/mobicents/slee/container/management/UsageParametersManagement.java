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
