/**
 * Start time:17:07:31 2009-01-31<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.validator;

import org.mobicents.slee.container.component.ProfileSpecificationComponent;
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
import org.mobicents.slee.container.component.validator.profile.managementinterface.ManagementInterfaceOkDeclareValidCMPMethod;
import org.mobicents.slee.container.component.validator.profile.managementinterface.ManagementInterfaceOkDeclareValidCMPMethodWithThrows;
import org.mobicents.slee.container.component.validator.profile.managementinterface.ManagementInterfaceOkExtendCMPInterface;
import org.mobicents.slee.container.component.validator.profile.managementinterface.ManagementInterfaceWrongMethod_DynamicMBean;
import org.mobicents.slee.container.component.validator.profile.managementinterface.ManagementInterfaceWrongMethod_MBeanRegistration;
import org.mobicents.slee.container.component.validator.profile.managementinterface.ManagementInterfaceWrongMethod_Profile;
import org.mobicents.slee.container.component.validator.profile.managementinterface.ManagementInterfaceWrongMethod_ProfileMBean;
import org.mobicents.slee.container.component.validator.profile.managementinterface.ManagementInterfaceWrongMethod_ProfileManagement;
import org.mobicents.slee.container.component.validator.profile.managementinterface.ManagementInterfaceWrongPrefix;
import org.mobicents.slee.container.component.validator.profile.managementinterface.ManagementInterfaceWrongType;

/**
 * Start time:17:07:31 2009-01-31<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ProfileComponentValidatorManagementInterfaceTest extends
TCUtilityClass {

	public static final String _PROFILE_SPEC_JAR_ONE_OK_MANAGENEBT_INTERFACE_CONSTRAINTS = "xml/validator/profile/mgmt/profile-spec-jar-one-ManagementInterface.xml";
	
	
	public void testProfilemanagmentInterfaceConstraintsOk() throws Exception {
		final ProfileSpecificationDescriptorImpl descriptor = ProfileSpecificationDescriptorImpl.parseDocument(
				super.parseDocument(_PROFILE_SPEC_JAR_ONE_OK_MANAGENEBT_INTERFACE_CONSTRAINTS), null)[0];
		ProfileSpecificationComponent component=new ProfileSpecificationComponent(descriptor);
		component.setProfileCmpInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileCMPInterface()
								.getProfileCmpInterfaceName()));

		component.setProfileManagementInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileManagementInterface()
								.getProfileManagementInterfaceName()));
		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();		
		validator.setComponent(component);

		boolean b = validator.validateProfileManagementInterface();

		assertTrue("Management Interface class has not been validated", b);

	}
	
	
	public void testProfilemanagmentInterfaceConstraintsOkExtendCMPInterface() throws Exception {
		final ProfileSpecificationDescriptorImpl descriptor = ProfileSpecificationDescriptorImpl.parseDocument(
				super.parseDocument(_PROFILE_SPEC_JAR_ONE_OK_MANAGENEBT_INTERFACE_CONSTRAINTS), null)[0];
		ProfileSpecificationComponent component=new ProfileSpecificationComponent(descriptor);
		component.setProfileCmpInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileCMPInterface()
								.getProfileCmpInterfaceName()));

		component.setProfileManagementInterfaceClass(ManagementInterfaceOkExtendCMPInterface.class);
		
		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();		
		validator.setComponent(component);

		boolean b = validator.validateCMPInterface();

		assertTrue("CMP Interface class has not been validated", b);

	}
	
	public void testProfilemanagmentInterfaceConstraintsOkDeclareValidCMPMethod() throws Exception {
		final ProfileSpecificationDescriptorImpl descriptor = ProfileSpecificationDescriptorImpl.parseDocument(
				super.parseDocument(_PROFILE_SPEC_JAR_ONE_OK_MANAGENEBT_INTERFACE_CONSTRAINTS), null)[0];
		ProfileSpecificationComponent component=new ProfileSpecificationComponent(descriptor);
		component.setProfileCmpInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileCMPInterface()
								.getProfileCmpInterfaceName()));

		component.setProfileManagementInterfaceClass(ManagementInterfaceOkDeclareValidCMPMethod.class);
		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();		
		validator.setComponent(component);

		boolean b = validator.validateProfileManagementInterface();

		assertTrue("Management Interface class has not been validated", b);

	}
	
	public void testProfilemanagmentInterfaceConstraintsDeclareValidCMPMethodWithThrows() throws Exception {
		final ProfileSpecificationDescriptorImpl descriptor = ProfileSpecificationDescriptorImpl.parseDocument(
				super.parseDocument(_PROFILE_SPEC_JAR_ONE_OK_MANAGENEBT_INTERFACE_CONSTRAINTS), null)[0];
		ProfileSpecificationComponent component=new ProfileSpecificationComponent(descriptor);
		component.setProfileCmpInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileCMPInterface()
								.getProfileCmpInterfaceName()));

		component.setProfileManagementInterfaceClass(ManagementInterfaceOkDeclareValidCMPMethodWithThrows.class);
		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();		
		validator.setComponent(component);

		boolean b = validator.validateProfileManagementInterface();

		assertFalse("Management Interface class has been validated", b);

	}
	
	public void testProfilemanagmentInterfaceConstraintsWrongPrefix() throws Exception {
		final ProfileSpecificationDescriptorImpl descriptor = ProfileSpecificationDescriptorImpl.parseDocument(
				super.parseDocument(_PROFILE_SPEC_JAR_ONE_OK_MANAGENEBT_INTERFACE_CONSTRAINTS), null)[0];
		ProfileSpecificationComponent component=new ProfileSpecificationComponent(descriptor);
		component.setProfileCmpInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileCMPInterface()
								.getProfileCmpInterfaceName()));

		component.setProfileManagementInterfaceClass(ManagementInterfaceWrongPrefix.class);
		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();		
		validator.setComponent(component);

		boolean b = validator.validateProfileManagementInterface();

		assertFalse("Management Interface class has been validated", b);

	}
	public void testProfilemanagmentInterfaceConstraintsWrongParameterType() throws Exception {
		final ProfileSpecificationDescriptorImpl descriptor = ProfileSpecificationDescriptorImpl.parseDocument(
				super.parseDocument(_PROFILE_SPEC_JAR_ONE_OK_MANAGENEBT_INTERFACE_CONSTRAINTS), null)[0];
		ProfileSpecificationComponent component=new ProfileSpecificationComponent(descriptor);
		component.setProfileCmpInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileCMPInterface()
								.getProfileCmpInterfaceName()));

		component.setProfileManagementInterfaceClass(ManagementInterfaceWrongType.class);
		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();		
		validator.setComponent(component);

		boolean b = validator.validateProfileManagementInterface();

		assertFalse("Management Interface class has been validated", b);

	}
	
	
	public void testProfilemanagmentInterfaceConstraintsWrongMethodProfileMBean() throws Exception {
		final ProfileSpecificationDescriptorImpl descriptor = ProfileSpecificationDescriptorImpl.parseDocument(
				super.parseDocument(_PROFILE_SPEC_JAR_ONE_OK_MANAGENEBT_INTERFACE_CONSTRAINTS), null)[0];
		ProfileSpecificationComponent component=new ProfileSpecificationComponent(descriptor);
		component.setProfileCmpInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileCMPInterface()
								.getProfileCmpInterfaceName()));

		component.setProfileManagementInterfaceClass(ManagementInterfaceWrongMethod_ProfileMBean.class);
		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();		
		validator.setComponent(component);

		boolean b = validator.validateProfileManagementInterface();

		assertFalse("Management Interface class has been validated", b);

	}
	
	public void testProfilemanagmentInterfaceConstraintsWrongMethodProfileManagement() throws Exception {
		final ProfileSpecificationDescriptorImpl descriptor = ProfileSpecificationDescriptorImpl.parseDocument(
				super.parseDocument(_PROFILE_SPEC_JAR_ONE_OK_MANAGENEBT_INTERFACE_CONSTRAINTS), null)[0];
		ProfileSpecificationComponent component=new ProfileSpecificationComponent(descriptor);
		component.setProfileCmpInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileCMPInterface()
								.getProfileCmpInterfaceName()));

		component.setProfileManagementInterfaceClass(ManagementInterfaceWrongMethod_ProfileManagement.class);
		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();		
		validator.setComponent(component);

		boolean b = validator.validateProfileManagementInterface();

		assertFalse("Management Interface class has been validated", b);

	}
	
	public void testProfilemanagmentInterfaceConstraintsWrongMethodProfile() throws Exception {
		final ProfileSpecificationDescriptorImpl descriptor = ProfileSpecificationDescriptorImpl.parseDocument(
				super.parseDocument(_PROFILE_SPEC_JAR_ONE_OK_MANAGENEBT_INTERFACE_CONSTRAINTS), null)[0];
		ProfileSpecificationComponent component=new ProfileSpecificationComponent(descriptor);
		component.setProfileCmpInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileCMPInterface()
								.getProfileCmpInterfaceName()));

		component.setProfileManagementInterfaceClass(ManagementInterfaceWrongMethod_Profile.class);
		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();		
		validator.setComponent(component);

		boolean b = validator.validateProfileManagementInterface();

		assertFalse("Management Interface class has been validated", b);

	}
	
	public void testProfilemanagmentInterfaceConstraintsWrongMethodDynamicMBean() throws Exception {
		final ProfileSpecificationDescriptorImpl descriptor = ProfileSpecificationDescriptorImpl.parseDocument(
				super.parseDocument(_PROFILE_SPEC_JAR_ONE_OK_MANAGENEBT_INTERFACE_CONSTRAINTS), null)[0];
		ProfileSpecificationComponent component=new ProfileSpecificationComponent(descriptor);
		component.setProfileCmpInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileCMPInterface()
								.getProfileCmpInterfaceName()));

		component.setProfileManagementInterfaceClass(ManagementInterfaceWrongMethod_DynamicMBean.class);
		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();		
		validator.setComponent(component);

		boolean b = validator.validateProfileManagementInterface();

		assertFalse("Management Interface class has been validated", b);

	}
	
	public void testProfilemanagmentInterfaceConstraintsWrongMethodMBeanRegistration() throws Exception {
		final ProfileSpecificationDescriptorImpl descriptor = ProfileSpecificationDescriptorImpl.parseDocument(
				super.parseDocument(_PROFILE_SPEC_JAR_ONE_OK_MANAGENEBT_INTERFACE_CONSTRAINTS), null)[0];
		ProfileSpecificationComponent component=new ProfileSpecificationComponent(descriptor);
		component.setProfileCmpInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileCMPInterface()
								.getProfileCmpInterfaceName()));

		component.setProfileManagementInterfaceClass(ManagementInterfaceWrongMethod_MBeanRegistration.class);
		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();		
		validator.setComponent(component);

		boolean b = validator.validateProfileManagementInterface();

		assertFalse("Management Interface class has been validated", b);

	}
	
	
}
