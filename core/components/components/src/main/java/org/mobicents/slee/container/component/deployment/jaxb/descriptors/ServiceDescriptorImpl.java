package org.mobicents.slee.container.component.deployment.jaxb.descriptors;

import java.util.HashSet;
import java.util.Set;

import javax.slee.ComponentID;
import javax.slee.EventTypeID;
import javax.slee.SbbID;
import javax.slee.ServiceID;
import javax.slee.management.DeploymentException;

import org.mobicents.slee.container.component.deployment.jaxb.descriptors.service.MService;

/**
 * Start time:17:15:13 2009-01-20<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ServiceDescriptorImpl {

	private final MService mService;
	private final ServiceID serviceID;
	private final SbbID rootSbbID;
	private final Set<ComponentID> dependenciesSet = new HashSet<ComponentID>();

	public ServiceDescriptorImpl(MService mService) throws DeploymentException {
		try {
			this.mService = mService;			
			this.serviceID = new ServiceID(mService.getServiceName(),mService.getServiceVendor(),mService.getServiceVersion());
			this.rootSbbID = new SbbID(mService.getRootSbb().getSbbName(),mService.getRootSbb().getSbbVendor(),mService.getRootSbb().getSbbVersion());
			dependenciesSet.add(rootSbbID);
			// even if the root sbb does not declares it, all services depend on standard service started event(s)
			dependenciesSet.add(new EventTypeID("javax.slee.serviceactivity.ServiceStartedEvent", "javax.slee",
			"1.0"));
			if (isSlee11()) {
				dependenciesSet.add(new EventTypeID("javax.slee.serviceactivity.ServiceStartedEvent", "javax.slee",
				"1.1"));
			}
		} catch (Exception e) {
			throw new DeploymentException("failed to build service descriptor",e);
		}
	}

	public Set<ComponentID> getDependenciesSet() {
		return dependenciesSet;
	}
	
	public MService getMService() {
		return mService;
	}
	
	public SbbID getRootSbbID() {
		return rootSbbID;
	}
	
	public ServiceID getServiceID() {
		return serviceID;
	}
	
	public boolean isSlee11() {
		return mService.isSlee11();
	}
}
