package org.mobicents.slee.runtime.eventrouter;

import java.util.concurrent.ExecutorService;

import javax.slee.ServiceID;

/**
 * Storage of data in event router {@link ExecutorService}'s threads
 * 
 * @author martins
 *
 */
public class EventRouterThreadLocals {

	/**
	 * the id of the service, being invoked in the current thread.
	 */
	private static ThreadLocal<ServiceID> invokingService = new ThreadLocal<ServiceID>();
	
	/**
	 * Sets the id of the service, being invoked in the current thread.
	 * @param serviceID
	 */
	public static void setInvokingService(ServiceID serviceID) {
		invokingService.set(serviceID);
	}

	/**
	 * Retrieves the id of the service, being invoked in the current thread.
	 * @return null if the thread is not invoking any server
	 */
	public static ServiceID getInvokingService() {
		return invokingService.get();
	}
	
}
