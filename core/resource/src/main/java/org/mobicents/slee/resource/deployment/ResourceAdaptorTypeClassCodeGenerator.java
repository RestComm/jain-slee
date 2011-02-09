package org.mobicents.slee.resource.deployment;

import javax.slee.management.DeploymentException;

import org.mobicents.slee.container.component.ratype.ResourceAdaptorTypeComponent;

/**
 * Class to control generation of concrete classes from provided ra type.
 * @author martins
 */
public class ResourceAdaptorTypeClassCodeGenerator {

	public void process(ResourceAdaptorTypeComponent component) throws DeploymentException {
		try {
			new ConcreteActivityContextInterfaceFactoryGenerator(component).generateClass();
		} catch (DeploymentException ex) {
			throw ex;
		} catch (Throwable ex) {
			throw new DeploymentException(
					"Failed to generate ra type class(es)", ex);
		}
	}

}