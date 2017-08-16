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
