package org.mobicents.slee.container.component.deployment.jaxb.descriptors;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.slee.SLEEException;
import javax.slee.management.DeploymentException;

import org.mobicents.slee.container.component.deployment.jaxb.descriptors.service.MService;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.service.MServiceXML;

/**
 * Factory to build {@link ServiceDescriptorImpl} objects.
 * @author martins
 *
 */
public class ServiceDescriptorFactory extends AbstractDescriptorFactory {
	
	/**
	 * Builds a list of {@link ServiceDescriptorImpl} objects, from an {@link InputStream} containing the event jar xml.
	 * @param inputStream
	 * @return
	 * @throws DeploymentException
	 */
	public List<ServiceDescriptorImpl> parse(InputStream inputStream) throws DeploymentException {
		
		Object jaxbPojo = buildJAXBPojo(inputStream);
		
		List<ServiceDescriptorImpl> result = new ArrayList<ServiceDescriptorImpl>();
		
		MServiceXML serviceXML = null;
		if (jaxbPojo instanceof org.mobicents.slee.container.component.deployment.jaxb.slee.service.ServiceXml) {
			serviceXML = new MServiceXML((org.mobicents.slee.container.component.deployment.jaxb.slee.service.ServiceXml)jaxbPojo);
		}
		else if (jaxbPojo instanceof org.mobicents.slee.container.component.deployment.jaxb.slee11.service.ServiceXml) {
			serviceXML = new MServiceXML((org.mobicents.slee.container.component.deployment.jaxb.slee11.service.ServiceXml)jaxbPojo);
		} 
		else {
			throw new SLEEException("unexpected class of jaxb pojo built: "+(jaxbPojo != null ? jaxbPojo.getClass() : null));
		}
		
		for (MService mService : serviceXML.getMServices()) {
			result.add(new ServiceDescriptorImpl(mService));
		}
		return result;
	}
}
