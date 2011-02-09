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
