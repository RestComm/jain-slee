package org.mobicents.slee.container.deployment.jboss.action;

import javax.slee.ServiceID;

import org.mobicents.slee.container.management.ServiceManagement;

/**
 * 
 * @author martins
 * 
 */
public class ActivateServiceAction extends ServiceManagementAction {

	public ActivateServiceAction(ServiceID serviceID,
			ServiceManagement serviceManagement) {
		super(serviceID, serviceManagement);
	}

	@Override
	public void invoke() throws Exception {
		getServiceManagement().activate(getServiceID());
	}

}
