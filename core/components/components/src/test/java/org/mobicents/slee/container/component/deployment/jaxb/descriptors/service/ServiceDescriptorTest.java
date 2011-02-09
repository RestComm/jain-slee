/**
 * Start time:14:23:34 2009-01-23<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.deployment.jaxb.descriptors.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import javax.slee.management.DeploymentException;

import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ServiceDescriptorFactoryImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ServiceDescriptorImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.TCUtilityClass;
import org.xml.sax.SAXException;

/**
 * Start time:14:23:34 2009-01-23<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ServiceDescriptorTest extends TCUtilityClass {

	
	
	

	private static final String _TWO_DESCRIPTOR_FILE="xml/service-xml-two_1_1.xml";
	private static final String _TWO_DESCRIPTOR_FILE10="xml/service-xml-two_1_0.xml";
	
	private static final String _SERVICE_NAME="service-name";
	private static final String _SERVICE_VENDOR="service-vendor";
	private static final String _SERVICE_VERSION="service-version";
	
	private static final String _SBB_NAME="sbb-name";
	private static final String _SBB_VENDOR="sbb-vendor";
	private static final String _SBB_VERSION="sbb-version";
	
	private static final String _ADDRESS_PROFILE_TABLE="address-profile-table";
	
	//only 1.0
	private static final String _RESOURCE_INFO_PROFILE_TABLE="resource-info-profile-table";
	
	public void testParseTwo10() throws DeploymentException, SAXException, IOException, URISyntaxException
	{
		
		List<ServiceDescriptorImpl> specs=new ServiceDescriptorFactoryImpl().parse(super.getFileStream(_TWO_DESCRIPTOR_FILE10));

		assertNotNull("Service return value is null", specs);
		assertTrue("Service  size is wrong!!!", specs.size()==2);
		assertNotNull("Service return value cell is null", specs.get(0));
		assertFalse("Service should indicate v1.0 not v1.1",specs.get(0).isSlee11());
		//Test values
		doTestOnValues(specs.get(0),1);
		
		
		assertNotNull("Service return value cell is null", specs.get(1));
		assertFalse("Service should indicate v1.0 not v1.1",specs.get(1).isSlee11());
		//Test values
		doTestOnValues(specs.get(1),2);
	}

	
	public void testParseTwo() throws DeploymentException, SAXException, IOException, URISyntaxException
	{
		
		List<ServiceDescriptorImpl> specs=new ServiceDescriptorFactoryImpl().parse(super.getFileStream(_TWO_DESCRIPTOR_FILE));
		assertNotNull("Service return value is null", specs);
		assertTrue("Service  size is wrong!!!", specs.size()==2);
		assertNotNull("Service return value cell is null", specs.get(0));
		assertTrue("Service should indicate v1.1 not v1.0",specs.get(0).isSlee11());
		//Test values
		doTestOnValues(specs.get(0),1);
		
		
		assertNotNull("Service return value cell is null", specs.get(1));
		assertTrue("Service should indicate v1.1 not v1.0",specs.get(1).isSlee11());
		//Test values
		doTestOnValues(specs.get(1),2);
	}
	
	
	protected void doTestOnValues(ServiceDescriptorImpl service,int index) {
		

		validateKey(service.getRootSbbID(), "Root Sbb key", new String[]{_SBB_NAME+index,_SBB_VENDOR+index,_SBB_VERSION+index});
		validateKey(service.getServiceID(), "Service key", new String[]{_SERVICE_NAME+index,_SERVICE_VENDOR+index,_SERVICE_VERSION+index});
		validateValue(service.getAddressProfileTable(), "Address profile table", _ADDRESS_PROFILE_TABLE+index);
	
		
		validateValue(service.getAddressProfileTable(), "Address profile table", _ADDRESS_PROFILE_TABLE+index);
		if(!service.isSlee11())
			validateValue(service.getResourceInfoProfileTable(), "Resource info profile table", _RESOURCE_INFO_PROFILE_TABLE+index);
	}
	
	
}
