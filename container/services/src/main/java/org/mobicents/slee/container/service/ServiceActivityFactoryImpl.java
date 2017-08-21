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

package org.mobicents.slee.container.service;

import java.io.Serializable;

import javax.slee.FactoryException;
import javax.slee.ServiceID;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.serviceactivity.ServiceActivity;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.SleeThreadLocals;
import org.mobicents.slee.container.management.ServiceManagementImpl;
import org.mobicents.slee.container.transaction.SleeTransactionManager;

/**
 * Implementation of the service activity factory.
 * 
 *@author M. Ranganathan
 */
public class ServiceActivityFactoryImpl implements ServiceActivityFactory,
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String JNDI_NAME = "factory";

	private final ServiceManagementImpl serviceManagement;
	
	/**
	 * @param sleeContainer
	 */
	public ServiceActivityFactoryImpl(ServiceManagementImpl serviceManagement) {
		this.serviceManagement = serviceManagement;
	}

	/* (non-Javadoc)
	 * @see javax.slee.serviceactivity.ServiceActivityFactory#getActivity()
	 */
	public ServiceActivity getActivity()
			throws TransactionRequiredLocalException, FactoryException {

		final SleeContainer sleeContainer = serviceManagement.getSleeContainer();
		final SleeTransactionManager stm = sleeContainer.getTransactionManager();
		stm.mandateTransaction();
		
		ServiceID serviceID = SleeThreadLocals.getInvokingService();
		
		if (serviceID == null) {
			throw new FactoryException("unable to find out the invoking service id");
		}
		return new ServiceActivityImpl(serviceID);
	}

}
