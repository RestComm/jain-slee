/**
 * 
 */
package org.mobicents.slee.container.component.sbb;

import java.util.List;

import javax.slee.resource.ResourceAdaptorTypeID;

/**
 * @author martins
 * 
 */
public interface ResourceAdaptorTypeBindingDescriptor {

	/**
	 * 
	 * @return
	 */
	public String getActivityContextInterfaceFactoryName();

	/**
	 * 
	 * @return
	 */
	public List<ResourceAdaptorEntityBindingDescriptor> getResourceAdaptorEntityBinding();

	/**
	 * 
	 * @return
	 */
	public ResourceAdaptorTypeID getResourceAdaptorTypeRef();

}
