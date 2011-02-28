package org.mobicents.slee.container.deployment.jboss.action;

import javax.slee.ServiceID;

import org.mobicents.slee.container.management.ServiceManagement;

/**
 * 
 * @author martins
 * 
 */
public abstract class ServiceManagementAction implements ManagementAction {

	private final ServiceID serviceID;
	private final ServiceManagement serviceManagement;

	/**
	 * 
	 * @param sleeContainer
	 */
	public ServiceManagementAction(ServiceID serviceID,
			ServiceManagement serviceManagement) {
		this.serviceID = serviceID;
		this.serviceManagement = serviceManagement;
	}

	/**
	 * 
	 */
	@Override
	public Type getType() {
		return Type.SERVICE_MANAGEMENT;
	}

	/**
	 * 
	 * @return
	 */
	public ServiceID getServiceID() {
		return serviceID;
	}

	/**
	 * 
	 * @return
	 */
	public ServiceManagement getServiceManagement() {
		return serviceManagement;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "[" + serviceID + "]";
	}
}
