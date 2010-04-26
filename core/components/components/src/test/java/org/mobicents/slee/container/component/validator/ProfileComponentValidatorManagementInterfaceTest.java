/**
 * Start time:17:07:31 2009-01-31<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.validator;

import java.util.List;

import org.mobicents.slee.container.component.ProfileSpecificationComponentImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ProfileSpecificationDescriptorFactoryImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ProfileSpecificationDescriptorImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.TCUtilityClass;
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

		
		List<ProfileSpecificationDescriptorImpl> specs = new ProfileSpecificationDescriptorFactoryImpl()
		.parse(super.getFileStream(_PROFILE_SPEC_JAR_ONE_OK_MANAGENEBT_INTERFACE_CONSTRAINTS));

		ProfileSpecificationDescriptorImpl descriptor = specs.get(0);
		ProfileSpecificationComponentImpl component=new ProfileSpecificationComponentImpl(descriptor);
		component.setProfileCmpInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileCMPInterface()
								.getProfileCmpInterfaceName()));

		component.setProfileManagementInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileManagementInterface()));
		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();		
		validator.setComponent(component);

		boolean b = validator.validateProfileManagementInterface();

		assertTrue("Management Interface class has not been validated", b);

	}
	
	
	public void testProfilemanagmentInterfaceConstraintsOkExtendCMPInterface() throws Exception {

		
		List<ProfileSpecificationDescriptorImpl> specs = new ProfileSpecificationDescriptorFactoryImpl()
		.parse(super.getFileStream(_PROFILE_SPEC_JAR_ONE_OK_MANAGENEBT_INTERFACE_CONSTRAINTS));

		ProfileSpecificationDescriptorImpl descriptor = specs.get(0);
		ProfileSpecificationComponentImpl component=new ProfileSpecificationComponentImpl(descriptor);
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

		
		List<ProfileSpecificationDescriptorImpl> specs = new ProfileSpecificationDescriptorFactoryImpl()
		.parse(super.getFileStream(_PROFILE_SPEC_JAR_ONE_OK_MANAGENEBT_INTERFACE_CONSTRAINTS));

		ProfileSpecificationDescriptorImpl descriptor = specs.get(0);
		ProfileSpecificationComponentImpl component=new ProfileSpecificationComponentImpl(descriptor);
		component.setProfileCmpInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileCMPInterface()
								.getProfileCmpInterfaceName()));

		component.setProfileManagementInterfaceClass(ManagementInterfaceOkDeclareValidCMPMethod.class);
		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();		
		validator.setComponent(component);

		boolean b = validator.validateProfileManagementInterface();

		assertTrue("Management Interface class has not been validated - it does not extend CMP interface, however it is valid to declare CMP method.", b);

	}
	
	public void testProfilemanagmentInterfaceConstraintsDeclareValidCMPMethodWithThrows() throws Exception {

		
		List<ProfileSpecificationDescriptorImpl> specs = new ProfileSpecificationDescriptorFactoryImpl()
		.parse(super.getFileStream(_PROFILE_SPEC_JAR_ONE_OK_MANAGENEBT_INTERFACE_CONSTRAINTS));

		ProfileSpecificationDescriptorImpl descriptor = specs.get(0);
		ProfileSpecificationComponentImpl component=new ProfileSpecificationComponentImpl(descriptor);
		component.setProfileCmpInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileCMPInterface()
								.getProfileCmpInterfaceName()));

		component.setProfileManagementInterfaceClass(ManagementInterfaceOkDeclareValidCMPMethodWithThrows.class);
		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();		
		validator.setComponent(component);

		boolean b = validator.validateProfileManagementInterface();

		assertFalse("Management Interface class has been validated, it should not - it declares CMP methods with throws clause.", b);

	}
	
	public void testProfilemanagmentInterfaceConstraintsWrongPrefix() throws Exception {

		
		List<ProfileSpecificationDescriptorImpl> specs = new ProfileSpecificationDescriptorFactoryImpl()
		.parse(super.getFileStream(_PROFILE_SPEC_JAR_ONE_OK_MANAGENEBT_INTERFACE_CONSTRAINTS));

		ProfileSpecificationDescriptorImpl descriptor = specs.get(0);
		ProfileSpecificationComponentImpl component=new ProfileSpecificationComponentImpl(descriptor);
		component.setProfileCmpInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileCMPInterface()
								.getProfileCmpInterfaceName()));

		component.setProfileManagementInterfaceClass(ManagementInterfaceWrongPrefix.class);
		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();		
		validator.setComponent(component);

		boolean b = validator.validateProfileManagementInterface();

		assertFalse("Management Interface class has been validated, it should not as interface declares method with wrong prefix.", b);

	}
	public void testProfilemanagmentInterfaceConstraintsWrongParameterType() throws Exception {

		
		List<ProfileSpecificationDescriptorImpl> specs = new ProfileSpecificationDescriptorFactoryImpl()
		.parse(super.getFileStream(_PROFILE_SPEC_JAR_ONE_OK_MANAGENEBT_INTERFACE_CONSTRAINTS));

		ProfileSpecificationDescriptorImpl descriptor = specs.get(0);
		ProfileSpecificationComponentImpl component=new ProfileSpecificationComponentImpl(descriptor);
		component.setProfileCmpInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileCMPInterface()
								.getProfileCmpInterfaceName()));

		component.setProfileManagementInterfaceClass(ManagementInterfaceWrongType.class);
		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();		
		validator.setComponent(component);

		boolean b = validator.validateProfileManagementInterface();

		assertFalse("Management Interface class has been validated, it should not, as it decalres method with wrong parameter.", b);

	}
	
	
	public void testProfilemanagmentInterfaceConstraintsWrongMethodProfileMBean() throws Exception {

		
		List<ProfileSpecificationDescriptorImpl> specs = new ProfileSpecificationDescriptorFactoryImpl()
		.parse(super.getFileStream(_PROFILE_SPEC_JAR_ONE_OK_MANAGENEBT_INTERFACE_CONSTRAINTS));

		ProfileSpecificationDescriptorImpl descriptor = specs.get(0);
		ProfileSpecificationComponentImpl component=new ProfileSpecificationComponentImpl(descriptor);
		component.setProfileCmpInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileCMPInterface()
								.getProfileCmpInterfaceName()));

		component.setProfileManagementInterfaceClass(ManagementInterfaceWrongMethod_ProfileMBean.class);
		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();		
		validator.setComponent(component);

		boolean b = validator.validateProfileManagementInterface();

		assertFalse("Management Interface class has been validated, it should not as it declared method from ProfileMBean", b);

	}
	
	public void testProfilemanagmentInterfaceConstraintsWrongMethodProfileManagement() throws Exception {

		
		List<ProfileSpecificationDescriptorImpl> specs = new ProfileSpecificationDescriptorFactoryImpl()
		.parse(super.getFileStream(_PROFILE_SPEC_JAR_ONE_OK_MANAGENEBT_INTERFACE_CONSTRAINTS));

		ProfileSpecificationDescriptorImpl descriptor = specs.get(0);
		ProfileSpecificationComponentImpl component=new ProfileSpecificationComponentImpl(descriptor);
		component.setProfileCmpInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileCMPInterface()
								.getProfileCmpInterfaceName()));

		component.setProfileManagementInterfaceClass(ManagementInterfaceWrongMethod_ProfileManagement.class);
		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();		
		validator.setComponent(component);

		boolean b = validator.validateProfileManagementInterface();

		assertFalse("Management Interface class has been validated, it should not as it declared method from ProfileManagement", b);

	}
	
	public void testProfilemanagmentInterfaceConstraintsWrongMethodProfile() throws Exception {

		
		List<ProfileSpecificationDescriptorImpl> specs = new ProfileSpecificationDescriptorFactoryImpl()
		.parse(super.getFileStream(_PROFILE_SPEC_JAR_ONE_OK_MANAGENEBT_INTERFACE_CONSTRAINTS));

		ProfileSpecificationDescriptorImpl descriptor = specs.get(0);
		ProfileSpecificationComponentImpl component=new ProfileSpecificationComponentImpl(descriptor);
		component.setProfileCmpInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileCMPInterface()
								.getProfileCmpInterfaceName()));

		component.setProfileManagementInterfaceClass(ManagementInterfaceWrongMethod_Profile.class);
		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();		
		validator.setComponent(component);

		boolean b = validator.validateProfileManagementInterface();

		assertFalse("Management Interface class has been validated, it should not as it declared method from Profile", b);

	}
	
	public void testProfilemanagmentInterfaceConstraintsWrongMethodDynamicMBean() throws Exception {
	
		
		List<ProfileSpecificationDescriptorImpl> specs = new ProfileSpecificationDescriptorFactoryImpl()
		.parse(super.getFileStream(_PROFILE_SPEC_JAR_ONE_OK_MANAGENEBT_INTERFACE_CONSTRAINTS));

		ProfileSpecificationDescriptorImpl descriptor = specs.get(0);
		ProfileSpecificationComponentImpl component=new ProfileSpecificationComponentImpl(descriptor);
		component.setProfileCmpInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileCMPInterface()
								.getProfileCmpInterfaceName()));

		component.setProfileManagementInterfaceClass(ManagementInterfaceWrongMethod_DynamicMBean.class);
		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();		
		validator.setComponent(component);

		boolean b = validator.validateProfileManagementInterface();

		assertFalse("Management Interface class has been validated, it should not as it declared method from DynamicMBean", b);

	}
	
	public void testProfilemanagmentInterfaceConstraintsWrongMethodMBeanRegistration() throws Exception {

		
		List<ProfileSpecificationDescriptorImpl> specs = new ProfileSpecificationDescriptorFactoryImpl()
		.parse(super.getFileStream(_PROFILE_SPEC_JAR_ONE_OK_MANAGENEBT_INTERFACE_CONSTRAINTS));

		ProfileSpecificationDescriptorImpl descriptor = specs.get(0);
		ProfileSpecificationComponentImpl component=new ProfileSpecificationComponentImpl(descriptor);
		component.setProfileCmpInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileCMPInterface()
								.getProfileCmpInterfaceName()));

		component.setProfileManagementInterfaceClass(ManagementInterfaceWrongMethod_MBeanRegistration.class);
		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();		
		validator.setComponent(component);

		boolean b = validator.validateProfileManagementInterface();

		assertFalse("Management Interface class has been validated, it should not as it declared method from MBeanRegistration", b);

	}
	
	
}
