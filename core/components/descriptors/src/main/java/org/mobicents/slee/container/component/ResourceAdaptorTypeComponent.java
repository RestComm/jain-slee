/**
 * Start time:00:45:05 2009-02-04<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component;

import javax.slee.management.DeployableUnitID;
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
public class ResourceAdaptorTypeComponent {

	protected DeployableUnitID deployableUnitID = null;
	protected ResourceAdaptorTypeDescriptorImpl descriptor = null;
	protected ResourceAdaptorTypeID resourceAdaptorTypeID = null;

	public DeployableUnitID getDeployableUnitID() {
		return deployableUnitID;
	}

	public void setDeployableUnitID(DeployableUnitID deployableUnitID) {
		this.deployableUnitID = deployableUnitID;
	}

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

}
