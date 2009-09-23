package org.mobicents.slee.container.service;

import org.mobicents.slee.container.component.ServiceComponent;

public class ServiceFactory {
	
	// TODO eliminate the factory
	public static Service createService(ServiceComponent serviceComponent) {
		return new Service(serviceComponent);		
	}

	public static Service getService(ServiceComponent serviceComponent) {
		return new Service(serviceComponent);
	}
	
}
