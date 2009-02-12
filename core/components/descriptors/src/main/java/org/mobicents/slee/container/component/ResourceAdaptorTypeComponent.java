/**
 * Start time:00:45:05 2009-02-04<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component;

import java.util.Set;

import javax.slee.ComponentID;
import javax.slee.resource.ResourceAdaptorTypeID;

import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ResourceAdaptorTypeDescriptorImpl;

/**
 * Start time:00:45:05 2009-02-04<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ResourceAdaptorTypeComponent extends SleeComponent {

	/**
	 * the ratype descriptor
	 */
	private final ResourceAdaptorTypeDescriptorImpl descriptor;
	
	/**
	 * 
	 * @param descriptor
	 */
	public ResourceAdaptorTypeComponent(
			ResourceAdaptorTypeDescriptorImpl descriptor) {
		this.descriptor = descriptor;
	}

	/**
	 * Retrieves the ratype descriptor
	 * @return
	 */
	public ResourceAdaptorTypeDescriptorImpl getDescriptor() {
		return descriptor;
	}

	/**
	 * Retrieves the ratype id
	 * @return
	 */
	public ResourceAdaptorTypeID getResourceAdaptorTypeID() {
		return descriptor.getResourceAdaptorTypeID();
	}

	@Override
	public Set<ComponentID> getDependenciesSet() {
		return descriptor.getDependenciesSet();
	}
	
	@Override
	public boolean isSlee11() {
		return descriptor.isSlee11();
	}
	
	@Override
	public ComponentID getComponentID() {
		return getResourceAdaptorTypeID();
	}
}
