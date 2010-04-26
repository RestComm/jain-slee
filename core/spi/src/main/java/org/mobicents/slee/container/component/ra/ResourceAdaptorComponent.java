/**
 * 
 */
package org.mobicents.slee.container.component.ra;

import javax.slee.resource.ConfigProperties;
import javax.slee.resource.ResourceAdaptorID;

import org.mobicents.slee.container.component.SleeComponentWithUsageParametersInterface;

/**
 * 
 * @author martins
 * 
 */
public interface ResourceAdaptorComponent extends
		SleeComponentWithUsageParametersInterface {

	/**
	 * Retrieves the component's descriptor.
	 * @return
	 */
	public ResourceAdaptorDescriptor getDescriptor();
	
	/**
	 * Creates an instance of the {@link ConfigProperties} for this component
	 * 
	 * @return
	 */
	public ConfigProperties getDefaultConfigPropertiesInstance();

	/**
	 * Retrieves the ra class
	 * 
	 * @return
	 */
	public Class<?> getResourceAdaptorClass();

	/**
	 * Retrieves the ra id
	 * 
	 * @return
	 */
	public ResourceAdaptorID getResourceAdaptorID();

	/**
	 * Retrieves the JAIN SLEE specs descriptor
	 * 
	 * @return
	 */
	public javax.slee.resource.ResourceAdaptorDescriptor getSpecsDescriptor();

	/**
	 * Sets the ra class
	 * 
	 * @param c
	 */
	public void setResourceAdaptorClass(Class<?> c);

}
