/**
 * Start time:17:07:31 2009-01-31<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.validator;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import org.mobicents.slee.container.component.ProfileSpecificationComponent;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ProfileSpecificationDescriptorFactory;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ProfileSpecificationDescriptorImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.TCUtilityClass;
import org.mobicents.slee.container.component.validator.profile.ProfileBaseCMPInterfaceCollatorOnNonString;
import org.mobicents.slee.container.component.validator.profile.ProfileBaseCMPInterfaceLackBoolean;
import org.mobicents.slee.container.component.validator.profile.ProfileBaseCMPInterfaceToManyCMPs;
import org.mobicents.slee.container.component.validator.profile.ProfileCMPInterfaceForbbidenMethods;
import org.mobicents.slee.container.component.validator.profile.ProfileCMPInterfaceGetterThrows;
import org.mobicents.slee.container.component.validator.profile.ProfileCMPInterfaceSetterThrows;
import org.mobicents.slee.container.component.validator.profile.ProfileCMPInterfaceTypeMissMatch;
import org.mobicents.slee.container.component.validator.profile.ProfileCMPInterfaceWrongFieldType;
import org.mobicents.slee.container.component.validator.profile.ProfileSuperCMPInterface;

/**
 * Start time:17:07:31 2009-01-31<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ProfileComponentValidatorCMPInterfaceTest extends
TCUtilityClass {

	public static final String _PROFILE_SPEC_JAR_ONE_OK_CONSTRAINTS = "xml/validator/profile/cmp/profile-spec-jar-one.xml";
	public static final String _PROFILE_SPEC_JAR_ONE_COLLATOR_CONSTRAINTS = "xml/validator/profile/cmp/profile-spec-jar-one-Collator.xml";
	public static final String _PROFILE_SPEC_JAR_ONE_COLLATOR_NOTREFERENCED_CONSTRAINTS = "xml/validator/profile/cmp/profile-spec-jar-one-CollatorNotreferencedCollator.xml";
	public static final String _PROFILE_SPEC_JAR_ONE_COLLATOR_NOT_EXISTING_CONSTRAINTS = "xml/validator/profile/cmp/profile-spec-jar-one-CollatorNotExisitngCollator.xml";
	
	public void testProfileCMPInterfaceConstraintsOk() throws Exception {

		
		List<ProfileSpecificationDescriptorImpl> specs = new ProfileSpecificationDescriptorFactory()
		.parse(super.getFileStream(_PROFILE_SPEC_JAR_ONE_OK_CONSTRAINTS));

		ProfileSpecificationDescriptorImpl descriptor = specs.get(0);
		ProfileSpecificationComponent component=new ProfileSpecificationComponent(descriptor);
		component.setProfileCmpInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileClasses().getProfileCMPInterface()
								.getProfileCmpInterfaceName()));

		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();		
		validator.setComponent(component);

		boolean b = validator.validateCMPInterface();

		assertTrue("CMP Interface class has not been validated", b);

	}
	
	
	public void testProfileSuperCMPInterface() throws Exception {

		
		List<ProfileSpecificationDescriptorImpl> specs = new ProfileSpecificationDescriptorFactory()
		.parse(super.getFileStream(_PROFILE_SPEC_JAR_ONE_OK_CONSTRAINTS));

		ProfileSpecificationDescriptorImpl descriptor = specs.get(0);
		ProfileSpecificationComponent component=new ProfileSpecificationComponent(descriptor);
		component.setProfileCmpInterfaceClass(ProfileSuperCMPInterface.class);

		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();
		validator.setComponent(component);

		boolean b = validator.validateCMPInterface();

		//FIXME: should this pass??
		assertFalse("CMP Interface class has been validated", b);

	}
	
	
	
	public void testProfileCMPInterfaceWrongFieldType() throws Exception {

		
		List<ProfileSpecificationDescriptorImpl> specs = new ProfileSpecificationDescriptorFactory()
		.parse(super.getFileStream(_PROFILE_SPEC_JAR_ONE_OK_CONSTRAINTS));

		ProfileSpecificationDescriptorImpl descriptor = specs.get(0);
		ProfileSpecificationComponent component=new ProfileSpecificationComponent(descriptor);
		component.setProfileCmpInterfaceClass(ProfileCMPInterfaceWrongFieldType.class);

		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();		
		validator.setComponent(component);

		boolean b = validator.validateCMPInterface();

		assertFalse("CMP Interface class has been validated - it should not - CMP type is not allowed", b);

	}
	
	public void testProfileCMPInterfaceFieldTypeMissmatch() throws Exception {

		
		List<ProfileSpecificationDescriptorImpl> specs = new ProfileSpecificationDescriptorFactory()
		.parse(super.getFileStream(_PROFILE_SPEC_JAR_ONE_OK_CONSTRAINTS));

		ProfileSpecificationDescriptorImpl descriptor = specs.get(0);
		ProfileSpecificationComponent component=new ProfileSpecificationComponent(descriptor);
		component.setProfileCmpInterfaceClass(ProfileCMPInterfaceTypeMissMatch.class);

		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();
		validator.setComponent(component);

		boolean b = validator.validateCMPInterface();

		assertFalse("CMP Interface class has been validated - it should not - cmp get/set parameter types missmatch", b);

	}
	
	public void testProfileCMPInterfaceSetterThrows() throws Exception {

		
		List<ProfileSpecificationDescriptorImpl> specs = new ProfileSpecificationDescriptorFactory()
		.parse(super.getFileStream(_PROFILE_SPEC_JAR_ONE_OK_CONSTRAINTS));

		ProfileSpecificationDescriptorImpl descriptor = specs.get(0);
		ProfileSpecificationComponent component=new ProfileSpecificationComponent(descriptor);
		component.setProfileCmpInterfaceClass(ProfileCMPInterfaceSetterThrows.class);

		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();
		validator.setComponent(component);

		boolean b = validator.validateCMPInterface();

		assertFalse("CMP Interface class has been validated - accessors can not declare throws clause", b);

	}
	public void testProfileCMPInterfaceGetterThrows() throws Exception {

		
		List<ProfileSpecificationDescriptorImpl> specs = new ProfileSpecificationDescriptorFactory()
		.parse(super.getFileStream(_PROFILE_SPEC_JAR_ONE_OK_CONSTRAINTS));

		ProfileSpecificationDescriptorImpl descriptor = specs.get(0);
		ProfileSpecificationComponent component=new ProfileSpecificationComponent(descriptor);
		component.setProfileCmpInterfaceClass(ProfileCMPInterfaceGetterThrows.class);

		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();
		validator.setComponent(component);

		boolean b = validator.validateCMPInterface();

		assertFalse("CMP Interface class has been validated - accessors can not declare throws clause", b);

	}
	public void testProfileCMPInterfaceForbbidenMethod() throws Exception {

		
		List<ProfileSpecificationDescriptorImpl> specs = new ProfileSpecificationDescriptorFactory()
		.parse(super.getFileStream(_PROFILE_SPEC_JAR_ONE_OK_CONSTRAINTS));

		ProfileSpecificationDescriptorImpl descriptor = specs.get(0);
		ProfileSpecificationComponent component=new ProfileSpecificationComponent(descriptor);
		component.setProfileCmpInterfaceClass(ProfileCMPInterfaceForbbidenMethods.class);

		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();
		validator.setComponent(component);

		boolean b = validator.validateCMPInterface();

		assertFalse("CMP Interface class has been validated - decalres forbiden method", b);

	}
	
	public void testProfileCMPInterfaceLackDeclaredCMPInInterface() throws Exception {

		
		List<ProfileSpecificationDescriptorImpl> specs = new ProfileSpecificationDescriptorFactory()
		.parse(super.getFileStream(_PROFILE_SPEC_JAR_ONE_COLLATOR_CONSTRAINTS));

		ProfileSpecificationDescriptorImpl descriptor = specs.get(0);
		ProfileSpecificationComponent component=new ProfileSpecificationComponent(descriptor);
		component.setProfileCmpInterfaceClass(ProfileBaseCMPInterfaceLackBoolean.class);

		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();
		validator.setComponent(component);

		boolean b = validator.validateCMPInterface();

		assertFalse("CMP Interface class has been validated - it shoudl not since it lack accessors for decalred cmp", b);

	}
	
	public void testProfileCMPInterfaceCollatorOnNonString() throws Exception {

		List<ProfileSpecificationDescriptorImpl> specs = new ProfileSpecificationDescriptorFactory()
		.parse(super.getFileStream(_PROFILE_SPEC_JAR_ONE_COLLATOR_CONSTRAINTS));

		ProfileSpecificationDescriptorImpl descriptor = specs.get(0);
		ProfileSpecificationComponent component=new ProfileSpecificationComponent(descriptor);
		component.setProfileCmpInterfaceClass(ProfileBaseCMPInterfaceCollatorOnNonString.class);

		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();
		validator.setComponent(component);

		boolean b = validator.validateCMPInterface();

		assertFalse("CMP Interface class has been validated - it should not - cmp field declares index hint with collator ref, but field type is not java.lang.String", b);

	}
	
	public void testProfileCMPInterfaceToManyCMPInInterface() throws Exception {

		
		List<ProfileSpecificationDescriptorImpl> specs = new ProfileSpecificationDescriptorFactory()
		.parse(super.getFileStream(_PROFILE_SPEC_JAR_ONE_COLLATOR_CONSTRAINTS));

		ProfileSpecificationDescriptorImpl descriptor = specs.get(0);
		ProfileSpecificationComponent component=new ProfileSpecificationComponent(descriptor);
		component.setProfileCmpInterfaceClass(ProfileBaseCMPInterfaceToManyCMPs.class);

		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();
		validator.setComponent(component);

		boolean b = validator.validateCMPInterface();

		// FIXME: This should be reworked. CMP do not have to be declared, only when they are not "default".
		assertTrue("CMP Interface class has been validated - CMP interface has more accessors than cmp fields defined in xml", b);

	}
	
	public void testProfileCMPInterfaceNotReferencedCollator() throws Exception {

		
		List<ProfileSpecificationDescriptorImpl> specs = new ProfileSpecificationDescriptorFactory()
		.parse(super.getFileStream(_PROFILE_SPEC_JAR_ONE_COLLATOR_NOTREFERENCED_CONSTRAINTS));

		ProfileSpecificationDescriptorImpl descriptor = specs.get(0);
		ProfileSpecificationComponent component=new ProfileSpecificationComponent(descriptor);
		component.setProfileCmpInterfaceClass(ProfileBaseCMPInterfaceToManyCMPs.class);

		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();
		validator.setComponent(component);

		boolean b = validator.validateDescriptor();

		//assertFalse("CMP Interface class has been validated", b);
		//There is no clause saying collators ahve to be used, they can be decalred

	}
	public void testProfileCMPInterfaceReferencedNotExistingCollator() throws Exception {

		
		List<ProfileSpecificationDescriptorImpl> specs = new ProfileSpecificationDescriptorFactory()
		.parse(super.getFileStream(_PROFILE_SPEC_JAR_ONE_COLLATOR_NOT_EXISTING_CONSTRAINTS));

		ProfileSpecificationDescriptorImpl descriptor = specs.get(0);
		ProfileSpecificationComponent component=new ProfileSpecificationComponent(descriptor);
		component.setProfileCmpInterfaceClass(ProfileBaseCMPInterfaceToManyCMPs.class);

		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();
		validator.setComponent(component);

		boolean b = validator.validateDescriptor();

		assertFalse("CMP Interface class has been validated - it should not - declares cmp field: withCollator, with index hint declaring wrong collator reference: null", b);
		

	}

}
