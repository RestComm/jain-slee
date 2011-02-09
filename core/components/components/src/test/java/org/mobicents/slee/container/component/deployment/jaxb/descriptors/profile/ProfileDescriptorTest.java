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
import java.util.List;
import java.util.Set;

import javax.slee.management.DeploymentException;
import javax.slee.management.LibraryID;

import org.mobicents.slee.container.component.common.EnvEntryDescriptor;
import org.mobicents.slee.container.component.common.ProfileSpecRefDescriptor;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ProfileSpecificationDescriptorFactoryImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ProfileSpecificationDescriptorImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.TCUtilityClass;
import org.mobicents.slee.container.component.profile.ProfileIndexDescriptor;
import org.mobicents.slee.container.component.profile.cmp.IndexHintDescriptor;
import org.mobicents.slee.container.component.profile.cmp.ProfileCMPFieldDescriptor;
import org.mobicents.slee.container.component.profile.query.CollatorDescriptor;
import org.xml.sax.SAXException;

/**
 * Start time:13:22:40 2009-01-19<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ProfileDescriptorTest extends TCUtilityClass {

	
	private static final String _DEFAULT_VALUE="DVAL";
	private static final String _ONE_DESCRIPTOR_FILE="xml/profile-spec-jar-one.xml";
	private static final String _TWO_DESCRIPTOR_FILE="xml/profile-spec-jar-two.xml";
	
	private static final String _ONE_DESCRIPTOR_FILE10="xml/profile-spec-jar-one10.xml";
	//private static final String _TWO_DESCRIPTOR_FILE="xml/profile-spec-jar-two.xml";
	
	
	
	public ProfileDescriptorTest() {
		// TODO Auto-generated constructor stub
	}

	public void testParseOne() throws DeploymentException, SAXException, IOException, URISyntaxException
	{
		
		
		
		
		List<ProfileSpecificationDescriptorImpl> specs=new ProfileSpecificationDescriptorFactoryImpl().parse(super.getFileStream((_ONE_DESCRIPTOR_FILE)));
		assertNotNull("Specs return value is null", specs);
		assertTrue("Profile specs size is wrong!!!", specs.size()==1);
		assertNotNull("Specs return value cell is null", specs.get(0));
		assertTrue("Profiel specs should indicate v1.1 not v1.0",specs.get(0).isSlee11());
		//Test values
		doTestOnValues(specs.get(0));
		
	}
	
	
	public void testParseTwo() throws DeploymentException, SAXException, IOException, URISyntaxException
	{
		
		
		List<ProfileSpecificationDescriptorImpl> specs=new ProfileSpecificationDescriptorFactoryImpl().parse(super.getFileStream((_TWO_DESCRIPTOR_FILE)));
		assertNotNull("Specs return value is null", specs);
		assertTrue("Profile specs size is wrong: "+specs.size()+"!!!", specs.size()==2);
		assertNotNull("Specs return value cell is null", specs.get(0));
		assertNotNull("Specs return value cell is null", specs.get(1));
		assertTrue("Profiel specs should indicate v1.1 not v1.0",specs.get(0).isSlee11());
		doTestOnValues(specs.get(0));
		assertTrue("Profiel specs should indicate v1.1 not v1.0",specs.get(1).isSlee11());
		doTestOnValues(specs.get(1));
		
		
		
	}
	
	public void testParseOne10() throws Exception
	{
		
		List<ProfileSpecificationDescriptorImpl> specs=new ProfileSpecificationDescriptorFactoryImpl().parse(super.getFileStream((_ONE_DESCRIPTOR_FILE10)));
		assertNotNull("Specs return value is null", specs);
		assertTrue("Profile specs size is wrong!!!", specs.size()==1);
		assertNotNull("Specs return value cell is null", specs.get(0));
		assertFalse("Profiel specs should indicate v1.0 not v1.1",specs.get(0).isSlee11());
		//Test values
		doTestOnValues(specs.get(0));
	}
	
	
	
	protected void doTestOnValues(ProfileSpecificationDescriptorImpl specs)
	{
		
		//FIXME, add more flexible checking - add flags to make variable check on profile for boolean flags
		//assertNotNull("Profile specs ID is null", specs.getID());
		assertNotNull("Profile specs key is null", specs.getProfileSpecificationID());
		assertNotNull("Profile specs key.name is null", specs.getProfileSpecificationID().getName());
		assertTrue("Profile specs key.name is not equal to "+_DEFAULT_VALUE, specs.getProfileSpecificationID().getName().compareTo(_DEFAULT_VALUE)==0);
		
		assertNotNull("Profile specs key.vendor is null", specs.getProfileSpecificationID().getVendor());
		assertTrue("Profile specs key.vendor is not equal to "+_DEFAULT_VALUE, specs.getProfileSpecificationID().getVendor().compareTo(_DEFAULT_VALUE+"2")==0);
		
		assertNotNull("Profile specs key.version is null", specs.getProfileSpecificationID().getVersion());
		assertTrue("Profile specs key.version is not equal to "+_DEFAULT_VALUE, specs.getProfileSpecificationID().getVersion().compareTo(_DEFAULT_VALUE+"3")==0);
		
	
		
		if(specs.isSlee11())
		{
			assertNotNull("Collators list should not be null",specs.getCollators());
			assertTrue("Collators list should not be empty",specs.getCollators().size()==1);
			CollatorDescriptor psc=specs.getCollators().get(0);
			assertNotNull("Collator aliass can not be null", psc.getCollatorAlias());
			assertNotNull("Collator decomposition can not be null", psc.getDecomposition());
			assertNotNull("Collator strength can not be null", psc.getStrength());
			assertNotNull("Collator locale language can not be null", psc.getLocaleLanguage());
			assertNotNull("Collator locale variant can not be null", psc.getLocaleVariant());
			assertNotNull("Collator locale country can not be null", psc.getLocaleCountry());
		
			assertTrue("Collator aliass not equal: "+_DEFAULT_VALUE, psc.getCollatorAlias().compareTo(_DEFAULT_VALUE)==0);
			assertTrue("Collator decomposition nto equal: None", psc.getDecomposition().compareTo("None")==0);
			assertTrue("Collator strength not equal: Primary", psc.getStrength().compareTo("Primary")==0);
		
			assertNotNull("Collator locale language is null ", psc.getLocaleLanguage());
			assertNotNull("Collator locale variant is null ", psc.getLocaleVariant());
			assertNotNull("Collator locale country is null ", psc.getLocaleCountry());
			assertTrue("Collator locale language not equal: "+_DEFAULT_VALUE, psc.getLocaleLanguage().compareTo(_DEFAULT_VALUE+"2")==0);
			assertTrue("Collator locale variant not equal: "+_DEFAULT_VALUE, psc.getLocaleVariant().compareTo(_DEFAULT_VALUE+"4")==0);
			assertTrue("Collator locale country not equal: "+_DEFAULT_VALUE, psc.getLocaleCountry().compareTo(_DEFAULT_VALUE+"3")==0);
		}
		
		assertNotNull("Profile specs CMP interface is null", specs.getProfileCMPInterface());
		assertNotNull("Profile specs CMP interface is null2", specs.getProfileCMPInterface().getProfileCmpInterfaceName());
		assertTrue("Profile specs CMP interface is null not equal to "+_DEFAULT_VALUE, specs.getProfileCMPInterface().getProfileCmpInterfaceName().compareTo(_DEFAULT_VALUE)==0);

		
		List<? extends ProfileCMPFieldDescriptor> cmps=specs.getProfileCMPInterface().getCmpFields();
		
		if(specs.isSlee11()){
			assertNotNull("Profile specs CMP Interface cmp fields are null!!",cmps);
			assertTrue("Profile specs CMP Interface cmp fields size is not 1", cmps.size()==1);
			assertTrue("Profile specs CMP Interface cmp field association name is not equal "+_DEFAULT_VALUE,cmps.get(0).getCmpFieldName().compareTo(_DEFAULT_VALUE)==0);
			ProfileCMPFieldDescriptor cmp=cmps.get(0);
			assertNotNull("Profile specs CMP Interface cmp field is null",cmp);
			assertTrue("Profile specs CMP Interface cmp field name is not equal to "+_DEFAULT_VALUE,cmp.getCmpFieldName().compareTo(_DEFAULT_VALUE)==0);
			assertTrue("Profile specs CMP Interface cmp field should not be unique (default value set by JXB is false)",!cmp.isUnique());

		
			List<? extends IndexHintDescriptor> hints=cmp.getIndexHints();

			assertNotNull("Profile specs CMP Interface cmp field index hints are null",hints);
			assertTrue("Profile specs CMP Interface cmp field index hints size is not equal to 1",hints.size()==1);
			assertNotNull("Profile specs CMP Interface cmp field index hint",hints.get(0));
			IndexHintDescriptor hint=hints.get(0);
			assertNotNull("Profile specs CMP Interface cmp field index hint collator ref is null",hint.getCollatorRef());
			assertTrue("Profile specs CMP Interface cmp field index hint collator ref is not equal to "+_DEFAULT_VALUE,hint.getCollatorRef().compareTo(_DEFAULT_VALUE)==0);
			assertNotNull("Profile specs CMP Interface cmp field index hint query operator is null",hint.getQueryOperator());
			assertTrue("Profile specs CMP Interface cmp field index hint query operator is not equal to \"equals\"",hint.getQueryOperator().compareTo("equals")==0);
		}
		
		
		if(specs.isSlee11()){
			assertNotNull("Profile specs Local interface is null", specs.getProfileLocalInterface());
			assertNotNull("Profile specs Local interface value is null", specs.getProfileLocalInterface().getProfileLocalInterfaceName());
			assertTrue("Profile specs Local interface is not equal to "+_DEFAULT_VALUE, specs.getProfileLocalInterface().getProfileLocalInterfaceName().compareTo(_DEFAULT_VALUE)==0);
			assertTrue("Profile specs Local interface  should not isolate security permissions", !specs.getProfileLocalInterface().isIsolateSecurityPermissions());
		}
		
		if(specs.isSlee11())
		{
			Set<LibraryID> libraryRefs=specs.getLibraryRefs();
						
			assertNotNull("Profile specs library refs list is null",libraryRefs);
			assertTrue("Profile specs library refs size is not equal to 1",libraryRefs.size()==1);
			validateKey(libraryRefs.iterator().next(), "Profile specs library ref key", new String[]{_DEFAULT_VALUE,_DEFAULT_VALUE+"2",_DEFAULT_VALUE+"3"});
			
			
			List<? extends ProfileSpecRefDescriptor> profileSpecsRefs=specs.getProfileSpecRefs();
			
			
			assertNotNull("Profile specs refs list is null",profileSpecsRefs);
			assertTrue("Profile specs refs size is not equal to 1",profileSpecsRefs.size()==1);
			validateKey(profileSpecsRefs.get(0).getComponentID(), "Profile specs  ref key", new String[]{_DEFAULT_VALUE,_DEFAULT_VALUE+"2",_DEFAULT_VALUE+"3"});
		}
		
		assertNotNull("Profile specs management interface is null", specs.getProfileManagementInterface());
		assertNotNull("Profile specs management interface value is null", specs.getProfileManagementInterface());
		assertTrue("Profile specs management interface is not equal to "+_DEFAULT_VALUE, specs.getProfileManagementInterface().compareTo(_DEFAULT_VALUE)==0);
		
		
		assertNotNull("Profile specs abstract class is null", specs.getProfileAbstractClass());
		assertNotNull("Profile specs abstract class value is null", specs.getProfileAbstractClass().getProfileAbstractClassName());
		assertTrue("Profile specs abstract class is not equal to "+_DEFAULT_VALUE, specs.getProfileAbstractClass().getProfileAbstractClassName().compareTo(_DEFAULT_VALUE)==0);
		if(specs.isSlee11()){
			//Defaults to false
			assertTrue("Profile abstract class should be reentrant", specs.getProfileAbstractClass().getReentrant());
		}
		
		
		if(specs.isSlee11()){
			assertNotNull("Profile specs table interface is null", specs.getProfileTableInterface());
			assertNotNull("Profile specs table interface value is null", specs.getProfileTableInterface());
			assertTrue("Profile specs table interface not equal to "+_DEFAULT_VALUE, specs.getProfileTableInterface().compareTo(_DEFAULT_VALUE)==0);
		}
		
		if(specs.isSlee11()){
			assertNotNull("Profile specs usage parameters interface is null", specs.getProfileUsageParameterInterface());
			assertNotNull("Profile specs usage parameters interface value is null", specs.getProfileUsageParameterInterface().getUsageParametersInterfaceName());
			assertTrue("Profile specs usage parameters interface not equal to "+_DEFAULT_VALUE, specs.getProfileUsageParameterInterface().getUsageParametersInterfaceName().compareTo(_DEFAULT_VALUE)==0);
		}
		if(specs.isSlee11()){
			List<? extends EnvEntryDescriptor> entries=specs.getEnvEntries();
			assertNotNull("Profile specs env entries are null",entries);
			assertTrue("Profile specs env entries size is not equal to 1",entries.size()==1);
			assertNotNull("Profile specs env entry is null",entries.get(0));
			EnvEntryDescriptor entry=entries.get(0);
			assertNotNull("Profile specs env entry is null",entry.getEnvEntryName());
			assertNotNull("Profile specs env entry name is null ", entry.getEnvEntryName());
			assertNotNull("Profile specs env entry type is null ", entry.getEnvEntryType());
			assertNotNull("Profile specs env entry value is null ", entry.getEnvEntryValue());
			assertTrue("Profile specs env entry name not equal: "+_DEFAULT_VALUE, entry.getEnvEntryName().compareTo(_DEFAULT_VALUE+"")==0);
			assertTrue("Profile specs env entry type not equal: "+_DEFAULT_VALUE, entry.getEnvEntryType().compareTo(_DEFAULT_VALUE+"2")==0);
			assertTrue("Profile specs env entry value not equal: "+_DEFAULT_VALUE, entry.getEnvEntryValue().compareTo(_DEFAULT_VALUE+"3")==0);
		}
		
		assertFalse("Profile specs single profile hint should be false",specs.isSingleProfile());
		
		if(specs.isSlee11()){
			assertNotNull("Profile specs security permissions are null",specs.getSecurityPermissions());
			assertNotNull("Profile specs security permissions value is null", specs.getSecurityPermissions());
			assertTrue("Profile specs security permissions not equal to "+_DEFAULT_VALUE, specs.getSecurityPermissions().compareTo(_DEFAULT_VALUE)==0);
		}
		
		
		if(!specs.isSlee11()){
			//This is slee 1.0 stuf only
			List<? extends ProfileIndexDescriptor> indexedAttribute=specs.getIndexedAttributes();
			

			assertNotNull("Profile indexed attributes set is null",indexedAttribute);
			assertTrue("Profile indexed attributes set is size is not 1",indexedAttribute.size()==1);
			ProfileIndexDescriptor ia=indexedAttribute.get(0);
			assertNotNull("Profile indexed attribute is null",ia);
			assertNotNull("Profile indexed attribute is name is null",ia.getName());
			assertTrue("Profile indexed attribute is name is not equal "+_DEFAULT_VALUE,ia.getName().compareTo(_DEFAULT_VALUE)==0);
			assertFalse("Profile indexed attribute should not be unique.",ia.getUnique());
		}
		
	}
	
	
	
	
	
}
