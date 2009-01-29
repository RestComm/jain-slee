package org.mobicents.slee.container.service;

import java.util.Map;

import javax.slee.ComponentID;
import javax.transaction.SystemException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.ServiceDescriptorImpl;

public class ServiceFactory {

	private static final Logger logger = Logger.getLogger(ServiceFactory.class);
	
	private static final SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
	
	public static Service createService(ServiceDescriptorImpl serviceDescriptorImpl) {
		// create service
		Service service = new Service(serviceDescriptorImpl,true);
		// store in tx context data
		try {
			sleeContainer.getTransactionManager().getTransactionContext().getData().put(serviceDescriptorImpl.getID(),service);
		} catch (SystemException e) {
			if (logger.isInfoEnabled()) {
				logger.warn("unable to obtain transaction context", e);
			}
		}
		return service;
	}

	public static Service getService(ServiceDescriptorImpl serviceDescriptorImpl) {
		
		Map transactionContextData = null;
		try {
			transactionContextData = sleeContainer.getTransactionManager().getTransactionContext().getData();
		} catch (SystemException e) {
			if (logger.isInfoEnabled()) {
				logger.warn("unable to obtain transaction context", e);
			}
		}
		
		ComponentID serviceID = serviceDescriptorImpl.getID();
		
		Service service = (Service) transactionContextData.get(serviceID);
		if (service == null) {
			// not in tx local data so recreate the service and store in tx 
			service = new Service(serviceDescriptorImpl,false);
			if (transactionContextData != null) {
				transactionContextData.put(serviceID, service);
			}
		}

		return service;
	}
	
}
