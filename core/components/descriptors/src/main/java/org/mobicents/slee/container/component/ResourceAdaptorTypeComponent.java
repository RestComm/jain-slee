/**
 * Start time:00:45:05 2009-02-04<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component;

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

	protected ResourceAdaptorTypeDescriptorImpl descriptor = null;
	protected ResourceAdaptorTypeID resourceAdaptorTypeID = null;

	public ResourceAdaptorTypeDescriptorImpl getDescriptor() {
		return descriptor;
	}

	public void setDescriptor(ResourceAdaptorTypeDescriptorImpl descriptor) {
		this.descriptor = descriptor;
	}

	public ResourceAdaptorTypeID getResourceAdaptorTypeID() {
		return resourceAdaptorTypeID;
	}

	public void setResourceAdaptorTypeID(
			ResourceAdaptorTypeID resourceAdaptorTypeID) {
		this.resourceAdaptorTypeID = resourceAdaptorTypeID;
	}

	@Override
	public ComponentID getComponentID() {
		return getResourceAdaptorTypeID();
	}
}
