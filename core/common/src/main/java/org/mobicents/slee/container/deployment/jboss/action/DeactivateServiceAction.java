package org.mobicents.slee.container.deployment.jboss.action;

import javax.slee.ServiceID;
import javax.slee.management.ServiceState;

import org.mobicents.slee.container.component.service.ServiceComponent;
import org.mobicents.slee.container.management.ServiceManagement;

/**
 * 
 * @author martins
 * 
 */
public class DeactivateServiceAction extends ServiceManagementAction {

	public DeactivateServiceAction(ServiceID serviceID,
			ServiceManagement serviceManagement) {
		super(serviceID, serviceManagement);
	}

	@Override
	public void invoke() throws Exception {
		final ServiceManagement serviceManagement = getServiceManagement();
		final ServiceComponent serviceComponent = serviceManagement.getSleeContainer().getComponentRepository().getComponentByID(getServiceID());
		if (serviceComponent != null) {
			// deactivate if needed
			if (serviceComponent.getServiceState() == ServiceState.ACTIVE) {
				serviceManagement.deactivate(getServiceID());
			}
			// continue once service is inactive
			while(serviceComponent.getServiceState() != ServiceState.INACTIVE) {
				try {
					Thread.sleep(1000);					
				}
				catch (Exception e) {
					// ignore
				}				
			}
		}
	}

}
