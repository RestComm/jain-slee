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
import java.util.Map;

import javax.slee.management.DeploymentException;


import org.mobicents.slee.container.component.ComponentKey;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.SbbDescriptorImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ServiceDescriptorImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.SuperTestCase;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.ProfileSpecsReference;
import org.mobicents.slee.container.component.deployment.jaxb.slee.sbb.ProfileSpecRef;
import org.xml.sax.SAXException;

/**
 * Start time:14:23:34 2009-01-23<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ServiceDescriptorTest extends SuperTestCase {

	
	
	

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
		
		ServiceDescriptorImpl[] specs=ServiceDescriptorImpl.parseDocument(super.parseDocument(_TWO_DESCRIPTOR_FILE10), null);
		assertNotNull("Service return value is null", specs);
		assertTrue("Service  size is wrong!!!", specs.length==2);
		assertNotNull("Service return value cell is null", specs[0]);
		assertFalse("Service should indicate v1.0 not v1.1",specs[0].isSlee11());
		//Test values
		doTestOnValues(specs[0],1);
		
		
		assertNotNull("Service return value cell is null", specs[1]);
		assertFalse("Service should indicate v1.0 not v1.1",specs[1].isSlee11());
		//Test values
		doTestOnValues(specs[1],2);
	}

	
	public void testParseTwo() throws DeploymentException, SAXException, IOException, URISyntaxException
	{
		
		ServiceDescriptorImpl[] specs=ServiceDescriptorImpl.parseDocument(super.parseDocument(_TWO_DESCRIPTOR_FILE), null);
		assertNotNull("Service return value is null", specs);
		assertTrue("Service  size is wrong!!!", specs.length==2);
		assertNotNull("Service return value cell is null", specs[0]);
		assertTrue("Service should indicate v1.1 not v1.0",specs[0].isSlee11());
		//Test values
		doTestOnValues(specs[0],1);
		
		
		assertNotNull("Service return value cell is null", specs[1]);
		assertTrue("Service should indicate v1.1 not v1.0",specs[1].isSlee11());
		//Test values
		doTestOnValues(specs[1],2);
	}
	
	
	protected void doTestOnValues(ServiceDescriptorImpl service,int index) {
		

		validateKey(service.getRootSbb(), "Root Sbb key", new String[]{_SBB_NAME+index,_SBB_VENDOR+index,_SBB_VERSION+index});
		validateKey(service.getServiceKey(), "Service key", new String[]{_SERVICE_NAME+index,_SERVICE_VENDOR+index,_SERVICE_VERSION+index});
		validateValue(service.getAddressProfileTable(), "Address profile table", _ADDRESS_PROFILE_TABLE+index);
		
		if(!service.isSlee11())
			validateValue(service.getResourceInfoProfileTable(), "Resource info profile table", _RESOURCE_INFO_PROFILE_TABLE+index);
	}
	
	
}
