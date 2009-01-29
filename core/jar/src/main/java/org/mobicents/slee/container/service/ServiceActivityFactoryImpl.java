/*
 * ServiceActivityFactoryImpl.java
 * 
 * Created on May 26, 2005
 * 
 * Created by: M. Ranganathan
 *
 * The Mobicents Open SLEE project
 * 
 * A SLEE for the people!
 *
 * The source code contained in this file is in in the public domain.          
 * It can be used in any project or product without prior permission, 	      
 * license or royalty payments. There is  NO WARRANTY OF ANY KIND,
 * EXPRESS, IMPLIED OR STATUTORY, INCLUDING, WITHOUT LIMITATION,
 * THE IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, 
 * AND DATA ACCURACY.  We do not warrant or make any representations 
 * regarding the use of the software or the  results thereof, including 
 * but not limited to the correctness, accuracy, reliability or 
 * usefulness of the software.
 */

package org.mobicents.slee.container.service;

import java.io.Serializable;

import javax.slee.FactoryException;
import javax.slee.ServiceID;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.serviceactivity.ServiceActivity;
import javax.slee.serviceactivity.ServiceActivityFactory;
import javax.transaction.SystemException;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;

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
	public static final String TXLOCALDATA_SERVICEID_KEY = "serviceactivityfactory.serviceid";

	/* (non-Javadoc)
	 * @see javax.slee.serviceactivity.ServiceActivityFactory#getActivity()
	 */
	public ServiceActivity getActivity()
			throws TransactionRequiredLocalException, FactoryException {

		SleeTransactionManager stm = SleeContainer.lookupFromJndi().getTransactionManager();
		stm.mandateTransaction();
		ServiceID serviceID = null;
		try {
			serviceID = (ServiceID) stm.getTransactionContext().getData().get(TXLOCALDATA_SERVICEID_KEY);
		} catch (SystemException e) {
			throw new FactoryException(e.getMessage(),e);
		}
		return getActivity(serviceID);
	}

	public static ServiceActivity getActivity(ServiceID serviceID)
			throws FactoryException {

		try {
			return SleeContainer.lookupFromJndi().getServiceManagement()
					.getService(serviceID).getServiceActivity();
		} catch (Exception ex) {
			throw new FactoryException("cannot get service activity for "
					+ serviceID);
		}
	}
}
