package org.mobicents.slee.container.service;

import java.util.Map;

import javax.transaction.SystemException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.ServiceComponent;

public class ServiceFactory {

	private static final Logger logger = Logger.getLogger(ServiceFactory.class);
	
	private static final SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
	
	public static Service createService(ServiceComponent serviceComponent) {
		// create service
		Service service = new Service(serviceComponent,true);
		// store in tx context data
		try {
			sleeContainer.getTransactionManager().getTransactionContext().getData().put(serviceComponent.getServiceID(),service);
		} catch (SystemException e) {
			if (logger.isInfoEnabled()) {
				logger.warn("unable to obtain transaction context", e);
			}
		}
		return service;
	}

	public static Service getService(ServiceComponent serviceComponent) {
		
		Map transactionContextData = null;
		try {
			transactionContextData = sleeContainer.getTransactionManager().getTransactionContext().getData();
		} catch (SystemException e) {
			if (logger.isInfoEnabled()) {
				logger.warn("unable to obtain transaction context", e);
			}
		}
		
		Service service = (Service) transactionContextData.get(serviceComponent.getServiceID());
		if (service == null) {
			// not in tx local data so recreate the service and store in tx 
			service = new Service(serviceComponent,false);
			if (transactionContextData != null) {
				transactionContextData.put(serviceComponent.getServiceID(), service);
			}
		}

		return service;
	}
	
}
