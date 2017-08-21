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

import java.util.Set;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.ObjectName;
import javax.slee.InvalidArgumentException;
import javax.slee.InvalidStateException;
import javax.slee.ServiceID;
import javax.slee.UnrecognizedServiceException;
import javax.slee.management.InvalidLinkNameBindingStateException;
import javax.slee.management.ManagementException;
import javax.slee.management.ServiceState;
import javax.slee.management.UnrecognizedResourceAdaptorEntityException;
import javax.slee.serviceactivity.ServiceActivityContextInterfaceFactory;
import javax.transaction.SystemException;

import org.mobicents.slee.container.SleeContainerModule;
import org.mobicents.slee.container.component.service.ServiceComponent;
import org.mobicents.slee.container.service.ServiceActivityFactory;
import org.mobicents.slee.container.service.ServiceActivityHandle;

/**
 * @author martins
 *
 */
public interface ServiceManagement extends SleeContainerModule {

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.management.ServiceManagementMBean#getState(javax.slee.ServiceID)
	 */
	public ServiceState getState(ServiceID serviceID)
			throws NullPointerException, UnrecognizedServiceException,
			ManagementException; 

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.management.ServiceManagementMBean#getServices(javax.slee.management.ServiceState)
	 */
	public ServiceID[] getServices(ServiceState serviceState)
			throws NullPointerException, ManagementException;

	/**
	 * Retrieves the set of ra entity link names referenced by the service componen, which do not exist
	 * @param serviceComponent
	 */
	public Set<String> getReferencedRAEntityLinksWhichNotExists(ServiceComponent serviceComponent);
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.management.ServiceManagementMBean#activate(javax.slee.ServiceID)
	 */
	public void activate(final ServiceID serviceID) throws NullPointerException,
	UnrecognizedServiceException, InvalidStateException,
	InvalidLinkNameBindingStateException;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.management.ServiceManagementMBean#activate(javax.slee.ServiceID[])
	 */
	public void activate(ServiceID[] serviceIDs) throws NullPointerException,
			InvalidArgumentException, UnrecognizedServiceException,
			InvalidStateException, ManagementException;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.management.ServiceManagementMBean#deactivate(javax.slee.ServiceID)
	 */
	public void deactivate(final ServiceID serviceID) throws NullPointerException,
			UnrecognizedServiceException, InvalidStateException,
			ManagementException;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.management.ServiceManagementMBean#deactivate(javax.slee.ServiceID[])
	 */
	public void deactivate(ServiceID[] arg0) throws NullPointerException,
			InvalidArgumentException, UnrecognizedServiceException,
			InvalidStateException, ManagementException;
	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.management.ServiceManagementMBean#deactivateAndActivate(javax.slee.ServiceID,
	 *      javax.slee.ServiceID)
	 */
	public void deactivateAndActivate(ServiceID arg0, ServiceID arg1)
			throws NullPointerException, InvalidArgumentException,
			UnrecognizedServiceException, InvalidStateException,
			ManagementException;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.management.ServiceManagementMBean#deactivateAndActivate(javax.slee.ServiceID[],
	 *      javax.slee.ServiceID[])
	 */
	public void deactivateAndActivate(ServiceID[] arg0, ServiceID[] arg1)
			throws NullPointerException, InvalidArgumentException,
			UnrecognizedServiceException, InvalidStateException,
			ManagementException;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.management.ServiceManagementMBean#getServiceUsageMBean(javax.slee.ServiceID)
	 */
	public ObjectName getServiceUsageMBean(ServiceID serviceID)
			throws NullPointerException, UnrecognizedServiceException,
			ManagementException;

	// --- non JMX

	/**
	 * 
	 * @return
	 */
	public ServiceActivityFactory getServiceActivityFactory();
	
	/**
	 * 
	 * @return
	 */
	public ServiceActivityContextInterfaceFactory getServiceActivityContextInterfaceFactory();
	
	/**
	 * Install a service into SLEE
	 * 
	 * @param serviceComponent
	 * @throws Exception
	 */
	public void installService(final ServiceComponent serviceComponent)
			throws Exception;

	/**
	 * uninstall a service.
	 * 
	 * @throws SystemException
	 * @throws UnrecognizedServiceException
	 * @throws MBeanRegistrationException
	 * @throws InstanceNotFoundException
	 * @throws UnrecognizedResourceAdaptorEntityException
	 * @throws NullPointerException
	 * @throws InvalidStateException 
	 * 
	 */
	public void uninstallService(final ServiceComponent serviceComponent)
			throws SystemException, UnrecognizedServiceException,
			InstanceNotFoundException, MBeanRegistrationException,
			NullPointerException, UnrecognizedResourceAdaptorEntityException,ManagementException, InvalidStateException;
		
	/**
	 * Verifies if the specified ra entity link name is referenced by a non inactive service.
	 * 
	 * @param raLinkName
	 * @return
	 */
	public boolean isRAEntityLinkNameReferenced(String raLinkName) ;

	/**
	 * @param activityHandle
	 */
	public void activityEnded(ServiceActivityHandle activityHandle);
}
