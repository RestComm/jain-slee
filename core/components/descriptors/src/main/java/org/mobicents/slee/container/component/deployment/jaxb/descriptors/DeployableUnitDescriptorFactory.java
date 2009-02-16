package org.mobicents.slee.container.component.deployment.jaxb.descriptors;

import java.io.InputStream;

import javax.slee.SLEEException;
import javax.slee.management.DeploymentException;

/**
 * Factory to build {@link DeployableUnitDescriptorImpl} objects.
 * @author martins
 *
 */
public class DeployableUnitDescriptorFactory extends AbstractDescriptorFactory {
	
	/**
	 * Builds a {@link DeployableUnitDescriptorImpl} object, from an {@link InputStream} containing the deployable-unit jar xml.
	 * @param inputStream
	 * @return
	 * @throws DeploymentException
	 */
	public DeployableUnitDescriptorImpl parse(InputStream inputStream) throws DeploymentException {
		
		Object jaxbPojo = buildJAXBPojo(inputStream);
		
		if (jaxbPojo instanceof org.mobicents.slee.container.component.deployment.jaxb.slee11.du.DeployableUnit) {
			return new DeployableUnitDescriptorImpl((org.mobicents.slee.container.component.deployment.jaxb.slee11.du.DeployableUnit)jaxbPojo);
		}
		else if (jaxbPojo instanceof org.mobicents.slee.container.component.deployment.jaxb.slee.du.DeployableUnit) {
			return  new DeployableUnitDescriptorImpl((org.mobicents.slee.container.component.deployment.jaxb.slee.du.DeployableUnit)jaxbPojo);
		} 
		else {
			throw new SLEEException("unexpected class of jaxb pojo built: "+(jaxbPojo != null ? jaxbPojo.getClass() : null));
		}
	}
}
