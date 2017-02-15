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

/***************************************************
 *                                                 *
 *  Restcomm: The Open Source VoIP Platform       *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/

package org.mobicents.slee.container.management.jmx;

import javax.management.ObjectName;
import javax.slee.InvalidArgumentException;
import javax.slee.InvalidStateException;
import javax.slee.ServiceID;
import javax.slee.UnrecognizedServiceException;
import javax.slee.management.InvalidLinkNameBindingStateException;
import javax.slee.management.ManagementException;
import javax.slee.management.ServiceState;

import org.mobicents.slee.container.management.ServiceManagementImpl;

/**
 * Implementation of the ServiceManagementMBean
 * 
 * @author M. Ranganathan
 * @author Eduardo Martins
 */
public class ServiceManagementMBeanImpl extends MobicentsServiceMBeanSupport implements
		ServiceManagementMBeanImplMBean {

	private final ServiceManagementImpl serviceManagement;
	
	public ServiceManagementMBeanImpl(ServiceManagementImpl serviceManagement) {
		super(serviceManagement.getSleeContainer());
		this.serviceManagement = serviceManagement;
	}
	
	/* (non-Javadoc)
	 * @see javax.slee.management.ServiceManagementMBean#activate(javax.slee.ServiceID)
	 */
	public void activate(ServiceID serviceID) throws NullPointerException,
			UnrecognizedServiceException, InvalidStateException,
			InvalidLinkNameBindingStateException, ManagementException {	
		try {
			serviceManagement.activate(serviceID);
		}
		catch (NullPointerException e) {
			throw e;
		}
		catch (UnrecognizedServiceException e) {
			throw e;
		}
		catch (InvalidLinkNameBindingStateException e) {
			throw e;
		}
		catch (InvalidStateException e) {
			throw e;
		}
		catch (Throwable e) {
			throw new ManagementException(e.getMessage(),e);
		}
	}

	
	/* (non-Javadoc)
	 * @see javax.slee.management.ServiceManagementMBean#activate(javax.slee.ServiceID[])
	 */
	public void activate(ServiceID[] serviceIDs) throws NullPointerException,
	InvalidArgumentException, UnrecognizedServiceException,
	InvalidStateException, InvalidLinkNameBindingStateException,
	ManagementException {
		try {
			serviceManagement.activate(serviceIDs);
		}
		catch (NullPointerException e) {
			throw e;
		}
		catch (InvalidArgumentException e) {
			throw e;
		}
		catch (UnrecognizedServiceException e) {
			throw e;
		}
		catch (InvalidLinkNameBindingStateException e) {
			throw e;
		}
		catch (InvalidStateException e) {
			throw e;
		}
		catch (Throwable e) {
			throw new ManagementException(e.getMessage(),e);
		}		
	}

	public void deactivate(ServiceID serviceID) throws NullPointerException,
			UnrecognizedServiceException, InvalidStateException,
			ManagementException {
		serviceManagement.deactivate(serviceID);
	}

	public void deactivate(ServiceID[] serviceIDs) throws NullPointerException,
			InvalidArgumentException, UnrecognizedServiceException,
			InvalidStateException, ManagementException {
		serviceManagement.deactivate(serviceIDs);
	}

	public void deactivateAndActivate(ServiceID arg0, ServiceID arg1)
			throws NullPointerException, InvalidArgumentException,
			UnrecognizedServiceException, InvalidStateException,
			ManagementException {
		serviceManagement.deactivateAndActivate(arg0, arg1);
	}

	public void deactivateAndActivate(ServiceID[] arg0, ServiceID[] arg1)
			throws NullPointerException, InvalidArgumentException,
			UnrecognizedServiceException, InvalidStateException,
			ManagementException {
		serviceManagement.deactivateAndActivate(arg0, arg1);
	}

	public ObjectName getServiceUsageMBean(ServiceID serviceID)
			throws NullPointerException, UnrecognizedServiceException,
			ManagementException {
		return serviceManagement.getServiceUsageMBean(serviceID);
	}

	public ServiceID[] getServices(ServiceState serviceState)
			throws NullPointerException, ManagementException {

		return serviceManagement.getServices(serviceState);
	}

	public ServiceState getState(ServiceID serviceID)
			throws NullPointerException, UnrecognizedServiceException,
			ManagementException {

		return serviceManagement.getState(serviceID);
	}

}
