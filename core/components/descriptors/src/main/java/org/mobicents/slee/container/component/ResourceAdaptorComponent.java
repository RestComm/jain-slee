/**
 * Start time:00:45:25 2009-02-04<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component;

import javax.slee.management.DeployableUnitID;
import javax.slee.profile.ProfileSpecificationID;
import javax.slee.resource.ResourceAdaptorID;

import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ProfileSpecificationDescriptorImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ResourceAdaptorDescriptorImpl;

/**
 * Start time:00:45:25 2009-02-04<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ResourceAdaptorComponent {

	protected DeployableUnitID deployableUnitID = null;
	protected ResourceAdaptorDescriptorImpl descriptor = null;
	protected ResourceAdaptorID resourceAdaptorID = null;
	protected Class resourceAdaptorClass = null;
	protected Class resourceAdaptorUsageParametersInterfaceClass = null;

	public DeployableUnitID getDeployableUnitID() {
		return deployableUnitID;
	}

	public void setDeployableUnitID(DeployableUnitID deployableUnitID) {
		this.deployableUnitID = deployableUnitID;
	}

	public ResourceAdaptorDescriptorImpl getDescriptor() {
		return descriptor;
	}

	public void setDescriptor(ResourceAdaptorDescriptorImpl descriptor) {
		this.descriptor = descriptor;
	}

	public ResourceAdaptorID getResourceAdaptorID() {
		return resourceAdaptorID;
	}

	public void setResourceAdaptorID(ResourceAdaptorID resourceAdaptorID) {
		this.resourceAdaptorID = resourceAdaptorID;
	}

	public Class getResourceAdaptorClass() {
		return resourceAdaptorClass;
	}

	public void setResourceAdaptorClass(Class resourceAdaptorClass) {
		this.resourceAdaptorClass = resourceAdaptorClass;
	}

	public Class getResourceAdaptorUsageParametersInterfaceClass() {
		return resourceAdaptorUsageParametersInterfaceClass;
	}

	public void setResourceAdaptorUsageParametersInterfaceClass(
			Class resourceAdaptorUsageParametersInterfaceClass) {
		this.resourceAdaptorUsageParametersInterfaceClass = resourceAdaptorUsageParametersInterfaceClass;
	}

}
