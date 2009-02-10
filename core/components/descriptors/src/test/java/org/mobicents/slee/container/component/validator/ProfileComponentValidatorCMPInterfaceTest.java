/**
 * Start time:17:07:31 2009-01-31<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.validator;

import javassist.CtClass;
import javassist.NotFoundException;

import org.mobicents.slee.container.component.ProfileSpecificationComponent;
import org.mobicents.slee.container.component.SbbComponent;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ProfileSpecificationDescriptorImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.SbbDescriptorImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.TCUtilityClass;
import org.mobicents.slee.container.component.validator.ClassUtils;
import org.mobicents.slee.container.component.validator.SbbComponentValidator;
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
	

	public void testProfileCMPInterfaceConstraintsOk() throws Exception {
		final ProfileSpecificationDescriptorImpl descriptor = ProfileSpecificationDescriptorImpl.parseDocument(
				super.parseDocument(_PROFILE_SPEC_JAR_ONE_OK_CONSTRAINTS), null)[0];
		ProfileSpecificationComponent component=new ProfileSpecificationComponent();
		component.setProfileCmpInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileCMPInterface()
								.getProfileCmpInterfaceName()));

		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();
		component.setDescriptor(descriptor);
		validator.setComponent(component);

		boolean b = validator.validateCMPInterface();

		assertTrue("CMP Interface class has not been validated", b);

	}
	
	
	public void testProfileSuperCMPInterface() throws Exception {
		final ProfileSpecificationDescriptorImpl descriptor = ProfileSpecificationDescriptorImpl.parseDocument(
				super.parseDocument(_PROFILE_SPEC_JAR_ONE_OK_CONSTRAINTS), null)[0];
		ProfileSpecificationComponent component=new ProfileSpecificationComponent();
		component.setProfileCmpInterfaceClass(ProfileSuperCMPInterface.class);

		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();
		component.setDescriptor(descriptor);
		validator.setComponent(component);

		boolean b = validator.validateCMPInterface();

		assertTrue("CMP Interface class has not been validated", b);

	}
	
	
	
	public void testProfileCMPInterfaceWrongFieldType() throws Exception {
		final ProfileSpecificationDescriptorImpl descriptor = ProfileSpecificationDescriptorImpl.parseDocument(
				super.parseDocument(_PROFILE_SPEC_JAR_ONE_OK_CONSTRAINTS), null)[0];
		ProfileSpecificationComponent component=new ProfileSpecificationComponent();
		component.setProfileCmpInterfaceClass(ProfileCMPInterfaceWrongFieldType.class);

		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();
		component.setDescriptor(descriptor);
		validator.setComponent(component);

		boolean b = validator.validateCMPInterface();

		assertFalse("CMP Interface class has been validated", b);

	}
	
	public void testProfileCMPInterfaceFieldTypeMissmatch() throws Exception {
		final ProfileSpecificationDescriptorImpl descriptor = ProfileSpecificationDescriptorImpl.parseDocument(
				super.parseDocument(_PROFILE_SPEC_JAR_ONE_OK_CONSTRAINTS), null)[0];
		ProfileSpecificationComponent component=new ProfileSpecificationComponent();
		component.setProfileCmpInterfaceClass(ProfileCMPInterfaceTypeMissMatch.class);

		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();
		component.setDescriptor(descriptor);
		validator.setComponent(component);

		boolean b = validator.validateCMPInterface();

		assertFalse("CMP Interface class has been validated", b);

	}
	
	public void testProfileCMPInterfaceSetterThrows() throws Exception {
		final ProfileSpecificationDescriptorImpl descriptor = ProfileSpecificationDescriptorImpl.parseDocument(
				super.parseDocument(_PROFILE_SPEC_JAR_ONE_OK_CONSTRAINTS), null)[0];
		ProfileSpecificationComponent component=new ProfileSpecificationComponent();
		component.setProfileCmpInterfaceClass(ProfileCMPInterfaceSetterThrows.class);

		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();
		component.setDescriptor(descriptor);
		validator.setComponent(component);

		boolean b = validator.validateCMPInterface();

		assertFalse("CMP Interface class has been validated", b);

	}
	public void testProfileCMPInterfaceGetterThrows() throws Exception {
		final ProfileSpecificationDescriptorImpl descriptor = ProfileSpecificationDescriptorImpl.parseDocument(
				super.parseDocument(_PROFILE_SPEC_JAR_ONE_OK_CONSTRAINTS), null)[0];
		ProfileSpecificationComponent component=new ProfileSpecificationComponent();
		component.setProfileCmpInterfaceClass(ProfileCMPInterfaceGetterThrows.class);

		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();
		component.setDescriptor(descriptor);
		validator.setComponent(component);

		boolean b = validator.validateCMPInterface();

		assertFalse("CMP Interface class has been validated", b);

	}
	public void testProfileCMPInterfaceForbbidenMethod() throws Exception {
		final ProfileSpecificationDescriptorImpl descriptor = ProfileSpecificationDescriptorImpl.parseDocument(
				super.parseDocument(_PROFILE_SPEC_JAR_ONE_OK_CONSTRAINTS), null)[0];
		ProfileSpecificationComponent component=new ProfileSpecificationComponent();
		component.setProfileCmpInterfaceClass(ProfileCMPInterfaceForbbidenMethods.class);

		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();
		component.setDescriptor(descriptor);
		validator.setComponent(component);

		boolean b = validator.validateCMPInterface();

		assertFalse("CMP Interface class has been validated", b);

	}
	

}
