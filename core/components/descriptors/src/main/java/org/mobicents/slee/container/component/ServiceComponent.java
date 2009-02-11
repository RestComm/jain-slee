/**
 * Start time:16:00:31 2009-01-25<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component;

import javax.slee.ComponentID;
import javax.slee.ServiceID;

import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ServiceDescriptorImpl;

/**
 * Start time:16:00:31 2009-01-25<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ServiceComponent extends SleeComponent {

	private ServiceDescriptorImpl descriptor;

	private ServiceID serviceID;
	
	public ServiceDescriptorImpl getDescriptor() {
		return descriptor;
	}

	public void setDescriptor(ServiceDescriptorImpl descriptor) {
		this.descriptor = descriptor;
	}
	
	public ServiceID getServiceID() {
		return serviceID;
	}
	
	public void setServiceID(ServiceID serviceID) {
		this.serviceID = serviceID;
	}
	
	public boolean isSlee11() {
		return this.descriptor.isSlee11();
	}	
	
	@Override
	public ComponentID getComponentID() {
		return getServiceID();
	}
}
