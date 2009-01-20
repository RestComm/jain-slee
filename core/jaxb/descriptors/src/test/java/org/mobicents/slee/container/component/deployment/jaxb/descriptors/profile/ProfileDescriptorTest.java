/**
 * Start time:13:22:40 2009-01-19<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.slee.management.DeployableUnitID;
import javax.slee.management.DeploymentException;

import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ProfileSpecificationDescriptorImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.SuperTestCase;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Start time:13:22:40 2009-01-19<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ProfileDescriptorTest extends SuperTestCase {

	
	private static final String _DEFAULT_VALUE="DVAL";
	private static final String _ONE_DESCRIPTOR_FILE="xml/profile-spec-jar-one.xml";
	private static final String _TWO_DESCRIPTOR_FILE="xml/profile-spec-jar-two.xml";
	
	
	public ProfileDescriptorTest() {
		// TODO Auto-generated constructor stub
	}

	public void testParseOne() throws DeploymentException, SAXException, IOException, URISyntaxException
	{
		
		ProfileSpecificationDescriptorImpl[] specs=ProfileSpecificationDescriptorImpl.parseDocument(super.parseDocument(_ONE_DESCRIPTOR_FILE), null);
		assertNotNull("Specs return value is null", specs);
		assertTrue("Profile specs size is wrong!!!", specs.length==1);
		assertNotNull("Specs return value cell is null", specs[0]);
		
		//Test values
		doTestOnValues(specs[0]);
		
	}
	
	
	public void testParseTwo() throws DeploymentException, SAXException, IOException, URISyntaxException
	{
		
		Document d=super.parseDocument(_TWO_DESCRIPTOR_FILE);
		ProfileSpecificationDescriptorImpl[] specs=ProfileSpecificationDescriptorImpl.parseDocument(d, null);
		assertNotNull("Specs return value is null", specs);
		assertTrue("Profile specs size is wrong: "+specs.length+"!!!", specs.length==2);
		assertNotNull("Specs return value cell is null", specs[0]);
		assertNotNull("Specs return value cell is null", specs[1]);
		doTestOnValues(specs[0]);
		doTestOnValues(specs[1]);
		
		
	}
	
	protected void doTestOnValues(ProfileSpecificationDescriptorImpl specs)
	{
		
		//FIXME, add more flexible checking
		//assertNotNull("Profile specs ID is null", specs.getID());
		assertNotNull("Profile specs key is null", specs.getProfileSpecKey());
		assertNotNull("Profile specs key.name is null", specs.getProfileSpecKey().getName());
		assertTrue("Profile specs key.name is not equal to "+_DEFAULT_VALUE, specs.getProfileSpecKey().getName().compareTo(_DEFAULT_VALUE)==0);
		
		assertNotNull("Profile specs key.vendor is null", specs.getProfileSpecKey().getVendor());
		assertTrue("Profile specs key.vendor is not equal to "+_DEFAULT_VALUE, specs.getProfileSpecKey().getVendor().compareTo(_DEFAULT_VALUE+"2")==0);
		
		assertNotNull("Profile specs key.version is null", specs.getProfileSpecKey().getVersion());
		assertTrue("Profile specs key.version is not equal to "+_DEFAULT_VALUE, specs.getProfileSpecKey().getVersion().compareTo(_DEFAULT_VALUE+"3")==0);
		
		//FIXME: check  library and profile refs here once they are done
		
		assertNotNull("Collators list should not be null",specs.getCollators());
		assertTrue("Collators list should not be empty",specs.getCollators().size()==1);
		ProfileSpecCollator psc=specs.getCollators().get(0);
		assertNotNull("Collator aliass can not be null", psc.getAlias());
		assertNotNull("Collator decomposition can not be null", psc.getDecomposition());
		assertNotNull("Collator strength can not be null", psc.getStrength());
		assertNotNull("Collator locale language can not be null", psc.getLocaleLanguage());
		assertNotNull("Collator locale variant can not be null", psc.getLocaleVariant());
		assertNotNull("Collator locale country can not be null", psc.getLocaleCountry());
		
		assertTrue("Collator aliass not equal: "+_DEFAULT_VALUE, psc.getAlias().compareTo(_DEFAULT_VALUE)==0);
		assertTrue("Collator decomposition nto equal: None", psc.getDecomposition().compareTo("None")==0);
		assertTrue("Collator strength not equal: Primary", psc.getStrength().compareTo("Primary")==0);
		assertTrue("Collator locale language not equal: "+_DEFAULT_VALUE, psc.getLocaleLanguage().compareTo(_DEFAULT_VALUE+"2")==0);
		assertTrue("Collator locale variant not equal: "+_DEFAULT_VALUE, psc.getLocaleVariant().compareTo(_DEFAULT_VALUE+"4")==0);
		assertTrue("Collator locale country not equal: "+_DEFAULT_VALUE, psc.getLocaleCountry().compareTo(_DEFAULT_VALUE+"3")==0);
		
		
		assertNotNull("Profile specs CMP interface is null", specs.getProfileCMPInterface());
		assertNotNull("Profile specs CMP interface is null2", specs.getProfileCMPInterface().getCmpInterfaceClassName());
		assertTrue("Profile specs CMP interface is null not equal to "+_DEFAULT_VALUE, specs.getProfileCMPInterface().getCmpInterfaceClassName().compareTo(_DEFAULT_VALUE)==0);
		//FIXME: Cmp fields check
		
		
		
		
		assertNotNull("Profile specs Local interface is null", specs.getProfileLocalInterface());
		assertNotNull("Profile specs Local interface is null2", specs.getProfileLocalInterface().getProfileLocalInterfaceName());
		assertTrue("Profile specs Local interface is null not equal to "+_DEFAULT_VALUE, specs.getProfileLocalInterface().getProfileLocalInterfaceName().compareTo(_DEFAULT_VALUE)==0);
		//FIXME: security perm check
		
		
		assertNotNull("Profile specs table interface is null", specs.getProfileTableInterface());
		assertNotNull("Profile specs table interface is null2", specs.getProfileTableInterface().getProfileTableInterfaceName());
		assertTrue("Profile specs table interface is null not equal to "+_DEFAULT_VALUE, specs.getProfileTableInterface().getProfileTableInterfaceName().compareTo(_DEFAULT_VALUE)==0);
		
		assertNotNull("Profile specs usage parameters interface is null", specs.getProfileUsageParameterInterface());
		assertNotNull("Profile specs usage parameters interface is null2", specs.getProfileUsageParameterInterface().getProfileUsagePamaterersInterfaceName());
		assertTrue("Profile specs usage parameters interface is null not equal to "+_DEFAULT_VALUE, specs.getProfileUsageParameterInterface().getProfileUsagePamaterersInterfaceName().compareTo(_DEFAULT_VALUE)==0);
		
		
	}
	
//	public void testAbsenceOfObligatoryElements()
//	{
//		//FIXME: add test for absent elements - like vendor/version etc
//	}
}
