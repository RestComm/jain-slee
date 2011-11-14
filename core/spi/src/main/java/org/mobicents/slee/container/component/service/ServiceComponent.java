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

package org.mobicents.slee.container.component.service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.slee.SbbID;
import javax.slee.ServiceID;
import javax.slee.management.ServiceState;

import org.mobicents.slee.container.component.ComponentRepository;
import org.mobicents.slee.container.component.SleeComponent;
import org.mobicents.slee.container.component.sbb.SbbComponent;
import org.mobicents.slee.container.management.jmx.ServiceUsageMBean;

/**
 * 
 * @author martins
 *
 */
public interface ServiceComponent extends SleeComponent {

	/**
	 * Retrieves the component's descriptor.
	 * @return
	 */
	public ServiceDescriptor getDescriptor();
	
	/**
	 * Retrieves the service alarm notification sources, mapped by sbb id.
	 * @return
	 */
	public ConcurrentHashMap<SbbID, Object> getAlarmNotificationSources();

	/**
	 * Retrieves the id of the service
	 * 
	 * @return
	 */
	public ServiceID getServiceID();

	/**
	 * Retrieves the JAIN SLEE specs descriptor
	 * 
	 * @return
	 */
	public javax.slee.management.ServiceDescriptor getSpecsDescriptor();

	/**
	 * Retrieves the time in milliseconds this component was created
	 * @return
	 */
	public long getCreationTime();
	
	/**
	 * Retrieves the set of sbbs used by this service
	 * 
	 * @param componentRepository
	 * @return
	 */
	public Set<SbbID> getSbbIDs(ComponentRepository componentRepository);

	/**
	 * Retrieves the set of RA entity links referenced by the sbbs related with the service.
	 * @param componentRepository
	 * @return
	 */
	public Set<String> getResourceAdaptorEntityLinks(ComponentRepository componentRepository);
	
	/**
	 * Retrieves the {@link SbbComponent} the service defines as root
	 * 
	 * @return
	 */
	public SbbComponent getRootSbbComponent();

	/**
	 * Sets the {@link SbbComponent} the service defines as root
	 * 
	 * @param rootSbbComponent
	 */
	public void setRootSbbComponent(SbbComponent rootSbbComponent);
	
	/**
	 * Retrieves the usage mbean for this service
	 * 
	 * @return
	 */
	public ServiceUsageMBean getServiceUsageMBean();

	/**
	 * Sets the usage mbean for this service
	 * 
	 * @param serviceUsageMBean
	 */
	public void setServiceUsageMBean(ServiceUsageMBean serviceUsageMBean);
	
	/**
	 * 
	 * @param state
	 */
	public void setServiceState(ServiceState state);
	
	/**
	 * 
	 * @return
	 */
	public ServiceState getServiceState();
	
	/**
	 * 
	 * @return
	 */
	public ServiceID getOldVersion();

	/**
	 * 
	 * @param serviceID
	 */
	public void setOldVersion(ServiceID serviceID);
}
