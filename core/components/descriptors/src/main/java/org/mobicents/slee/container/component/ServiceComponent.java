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

	/**
	 * the service descriptor
	 */
	private final ServiceDescriptorImpl descriptor;
	
	/**
	 * 
	 * @param descriptor
	 */
	public ServiceComponent(ServiceDescriptorImpl descriptor) {
		this.descriptor = descriptor;
	}
	
	/**
	 * Retrieves the service descriptor
	 * @return
	 */
	public ServiceDescriptorImpl getDescriptor() {
		return descriptor;
	}

	/**
	 * Retrieves the id of the service
	 * @return
	 */
	public ServiceID getServiceID() {
		return descriptor.getServiceID();
	}
	
	@Override
	public boolean isSlee11() {
		return this.descriptor.isSlee11();
	}	
	
	@Override
	public ComponentID getComponentID() {
		return getServiceID();
	}
}
