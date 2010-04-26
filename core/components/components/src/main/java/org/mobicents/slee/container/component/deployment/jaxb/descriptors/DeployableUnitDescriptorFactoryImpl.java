package org.mobicents.slee.container.component.deployment.jaxb.descriptors;

import java.io.InputStream;

import javax.slee.SLEEException;
import javax.slee.management.DeploymentException;

import org.mobicents.slee.container.component.du.DeployableUnitDescriptorFactory;

/**
 * 
 * @author martins
 *
 */
public class DeployableUnitDescriptorFactoryImpl extends AbstractDescriptorFactory implements DeployableUnitDescriptorFactory {
	
	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.core.component.du.DeployableUnitDescriptorFactory#parse(java.io.InputStream)
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
