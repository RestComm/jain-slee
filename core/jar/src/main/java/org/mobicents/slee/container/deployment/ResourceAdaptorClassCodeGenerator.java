package org.mobicents.slee.container.deployment;

import javax.slee.management.DeploymentException;

import org.mobicents.slee.container.component.ResourceAdaptorComponent;

/**
 * Class to control generation of concrete classes from provided ra.
 * @author martins
 */
public class ResourceAdaptorClassCodeGenerator {

	/**
	 * generates all class code for the specified ra component
	 * @param component
	 * @throws DeploymentException
	 */
	public void process(ResourceAdaptorComponent component) throws DeploymentException {
		// resource adaptors only define usage param to be generated
		new SleeComponentWithUsageParametersClassCodeGenerator().process(component);
	}

}