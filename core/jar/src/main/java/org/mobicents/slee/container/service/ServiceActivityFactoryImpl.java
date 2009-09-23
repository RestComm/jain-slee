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

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.runtime.eventrouter.EventRouterThreadLocals;
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

	private final SleeContainer sleeContainer;	
	
	/**
	 * @param sleeContainer
	 */
	public ServiceActivityFactoryImpl(SleeContainer sleeContainer) {
		this.sleeContainer = sleeContainer;
	}

	/* (non-Javadoc)
	 * @see javax.slee.serviceactivity.ServiceActivityFactory#getActivity()
	 */
	public ServiceActivity getActivity()
			throws TransactionRequiredLocalException, FactoryException {

		SleeTransactionManager stm = sleeContainer.getTransactionManager();
		stm.mandateTransaction();
		
		ServiceID serviceID = EventRouterThreadLocals.getInvokingService();
		
		if (serviceID == null) {
			throw new FactoryException("unable to find out the invoking service id");
		}
		return getActivity(serviceID);
	}

	public ServiceActivity getActivity(ServiceID serviceID)
			throws FactoryException {

		try {
			return sleeContainer.getServiceManagement()
					.getService(serviceID).getServiceActivity();
		} catch (Exception ex) {
			throw new FactoryException("cannot get service activity for "
					+ serviceID);
		}
	}
}
