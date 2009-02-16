package org.mobicents.slee.container.component.deployment.jaxb.descriptors;

import java.util.HashSet;
import java.util.Set;

import javax.slee.ComponentID;
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

	private final MService descriptor;
	private final ServiceID serviceID;
	private final SbbID rootSbbID;
	private final Set<ComponentID> dependenciesSet = new HashSet<ComponentID>();

	public ServiceDescriptorImpl(MService descriptor) throws DeploymentException {
		try {
			this.descriptor = descriptor;			
			this.serviceID = new ServiceID(descriptor.getServiceName(),descriptor.getServiceVendor(),descriptor.getServiceVersion());
			this.rootSbbID = new SbbID(descriptor.getRootSbb().getSbbName(),descriptor.getRootSbb().getSbbVendor(),descriptor.getRootSbb().getSbbVersion());	
		} catch (Exception e) {
			throw new DeploymentException("failed to build service descriptor",e);
		}
	}

	public Set<ComponentID> getDependenciesSet() {
		return dependenciesSet;
	}
	
	public MService getDescriptor() {
		return descriptor;
	}
	
	public SbbID getRootSbbID() {
		return rootSbbID;
	}
	
	public ServiceID getServiceID() {
		return serviceID;
	}
	
	public boolean isSlee11() {
		return descriptor.isSlee11();
	}
}
