/**
 * Start time:00:45:25 2009-02-04<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component;

import java.util.Set;

import javax.slee.ComponentID;
import javax.slee.resource.ResourceAdaptorID;

import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ResourceAdaptorDescriptorImpl;

/**
 * Start time:00:45:25 2009-02-04<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ResourceAdaptorComponent extends SleeComponent {

	/**
	 * the ra descriptor
	 */
	private final ResourceAdaptorDescriptorImpl descriptor;
	
	/**
	 * the ra class
	 */
	private Class resourceAdaptorClass = null;
	
	/**
	 * the ra usage parameters interface
	 */
	private Class resourceAdaptorUsageParametersInterfaceClass = null;

	/**
	 * 
	 * @param descriptor
	 */
	public ResourceAdaptorComponent(ResourceAdaptorDescriptorImpl descriptor) {
		this.descriptor = descriptor;
	}

	/**
	 * Retrieves the ra descriptor
	 * @return
	 */
	public ResourceAdaptorDescriptorImpl getDescriptor() {
		return descriptor;
	}

	/**
	 * Retrieves the ra id
	 * @return
	 */
	public ResourceAdaptorID getResourceAdaptorID() {
		return descriptor.getResourceAdaptorID();
	}

	/**
	 * Retrieves the ra class
	 * @return
	 */
	public Class getResourceAdaptorClass() {
		return resourceAdaptorClass;
	}

	/**
	 * Sets the ra class
	 * @param resourceAdaptorClass
	 */
	public void setResourceAdaptorClass(Class resourceAdaptorClass) {
		this.resourceAdaptorClass = resourceAdaptorClass;
	}

	/**
	 * Retrieves the ra usage parameters interface
	 * @return
	 */
	public Class getResourceAdaptorUsageParametersInterfaceClass() {
		return resourceAdaptorUsageParametersInterfaceClass;
	}

	/**
	 * Sets the ra usage parameters interface
	 * @param resourceAdaptorUsageParametersInterfaceClass
	 */
	public void setResourceAdaptorUsageParametersInterfaceClass(
			Class resourceAdaptorUsageParametersInterfaceClass) {
		this.resourceAdaptorUsageParametersInterfaceClass = resourceAdaptorUsageParametersInterfaceClass;
	}

	@Override
	public Set<ComponentID> getDependenciesSet() {
		return descriptor.getDependenciesSet();
	}
	
	@Override
	public ComponentID getComponentID() {
		return getResourceAdaptorID();
	}
	
	@Override
	public boolean isSlee11() {
		return descriptor.isSlee11();
	}
}
