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
import org.mobicents.slee.container.component.validator.profile.localinterface.LocalInterfaceNoProfileLocalObject;
import org.mobicents.slee.container.component.validator.profile.localinterface.LocalInterfaceOkDeclareValidCMPMethod;
import org.mobicents.slee.container.component.validator.profile.localinterface.LocalInterfaceOkDeclareValidCMPMethodWithThrows;
import org.mobicents.slee.container.component.validator.profile.localinterface.LocalInterfaceOkExtendCMPInterface;
import org.mobicents.slee.container.component.validator.profile.localinterface.LocalInterfaceWrongMethod_DynamicMBean;
import org.mobicents.slee.container.component.validator.profile.localinterface.LocalInterfaceWrongMethod_MBeanRegistration;
import org.mobicents.slee.container.component.validator.profile.localinterface.LocalInterfaceWrongMethod_Profile;
import org.mobicents.slee.container.component.validator.profile.localinterface.LocalInterfaceWrongMethod_ProfileMBean;
import org.mobicents.slee.container.component.validator.profile.localinterface.LocalInterfaceWrongMethod_ProfileManagement;
import org.mobicents.slee.container.component.validator.profile.localinterface.LocalInterfaceWrongPrefix;
import org.mobicents.slee.container.component.validator.profile.localinterface.LocalInterfaceWrongType;


/**
 * Start time:17:07:31 2009-01-31<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ProfileComponentValidatorLocalInterfaceTest extends
TCUtilityClass {

	public static final String _PROFILE_SPEC_JAR_ONE_OK_LOCAL_INTERFACE_CONSTRAINTS = "xml/validator/profile/local/profile-spec-jar-one-LocalInterface.xml";
	
	
	public void testProfileLocalInterfaceConstraintsOk() throws Exception {

		
		List<ProfileSpecificationDescriptorImpl> specs = new ProfileSpecificationDescriptorFactory()
		.parse(super.getFileStream(_PROFILE_SPEC_JAR_ONE_OK_LOCAL_INTERFACE_CONSTRAINTS));

		ProfileSpecificationDescriptorImpl descriptor = specs.get(0);
		ProfileSpecificationComponent component=new ProfileSpecificationComponent(descriptor);
		component.setProfileCmpInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileClasses().getProfileCMPInterface()
								.getProfileCmpInterfaceName()));

		component.setProfileLocalInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileClasses().getProfileLocalInterface()
								.getProfileLocalInterfaceName()));
		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();		
		validator.setComponent(component);

		boolean b = validator.validateProfileLocalInterface();

		assertTrue("Local Interface class has not been validated", b);

	}
	
	
	public void testProfileLocalInterfaceConstraintsOkExtendCMPInterface() throws Exception {

		
		List<ProfileSpecificationDescriptorImpl> specs = new ProfileSpecificationDescriptorFactory()
		.parse(super.getFileStream(_PROFILE_SPEC_JAR_ONE_OK_LOCAL_INTERFACE_CONSTRAINTS));

		ProfileSpecificationDescriptorImpl descriptor = specs.get(0);
		ProfileSpecificationComponent component=new ProfileSpecificationComponent(descriptor);
		component.setProfileCmpInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileClasses().getProfileCMPInterface()
								.getProfileCmpInterfaceName()));

		component.setProfileLocalInterfaceClass(LocalInterfaceOkExtendCMPInterface.class);
		
		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();		
		validator.setComponent(component);

		boolean b = validator.validateCMPInterface();

		assertTrue("CMP Interface class has not been validated", b);

	}
	
	public void testProfileLocalInterfaceConstraintsOkDeclareValidCMPMethod() throws Exception {

		
		List<ProfileSpecificationDescriptorImpl> specs = new ProfileSpecificationDescriptorFactory()
		.parse(super.getFileStream(_PROFILE_SPEC_JAR_ONE_OK_LOCAL_INTERFACE_CONSTRAINTS));

		ProfileSpecificationDescriptorImpl descriptor = specs.get(0);
		ProfileSpecificationComponent component=new ProfileSpecificationComponent(descriptor);
		component.setProfileCmpInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileClasses().getProfileCMPInterface()
								.getProfileCmpInterfaceName()));

		component.setProfileLocalInterfaceClass(LocalInterfaceOkDeclareValidCMPMethod.class);
		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();		
		validator.setComponent(component);

		boolean b = validator.validateProfileLocalInterface();

		assertTrue("Local Interface class has not been validated - it does not extend CMP interface, however it is valid to declare CMP method.", b);

	}
	
	public void testProfileLocalInterfaceConstraintsDeclareValidCMPMethodWithThrows() throws Exception {

		
		List<ProfileSpecificationDescriptorImpl> specs = new ProfileSpecificationDescriptorFactory()
		.parse(super.getFileStream(_PROFILE_SPEC_JAR_ONE_OK_LOCAL_INTERFACE_CONSTRAINTS));

		ProfileSpecificationDescriptorImpl descriptor = specs.get(0);
		ProfileSpecificationComponent component=new ProfileSpecificationComponent(descriptor);
		component.setProfileCmpInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileClasses().getProfileCMPInterface()
								.getProfileCmpInterfaceName()));

		component.setProfileLocalInterfaceClass(LocalInterfaceOkDeclareValidCMPMethodWithThrows.class);
		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();		
		validator.setComponent(component);

		boolean b = validator.validateProfileLocalInterface();

		assertFalse("Local Interface class has been validated, it should not - it declares CMP methods with throws clause.", b);

	}
	
	public void testProfileLocalInterfaceConstraintsWrongPrefix() throws Exception {

		
		List<ProfileSpecificationDescriptorImpl> specs = new ProfileSpecificationDescriptorFactory()
		.parse(super.getFileStream(_PROFILE_SPEC_JAR_ONE_OK_LOCAL_INTERFACE_CONSTRAINTS));

		ProfileSpecificationDescriptorImpl descriptor = specs.get(0);
		ProfileSpecificationComponent component=new ProfileSpecificationComponent(descriptor);
		component.setProfileCmpInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileClasses().getProfileCMPInterface()
								.getProfileCmpInterfaceName()));

		component.setProfileLocalInterfaceClass(LocalInterfaceWrongPrefix.class);
		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();		
		validator.setComponent(component);

		boolean b = validator.validateProfileLocalInterface();

		assertFalse("Local Interface class has been validated, it should not as interface declares method with wrong prefix.", b);

	}
	
// FIXME: Alexandre: This test is not correct, I guess.
//	public void testProfileLocalInterfaceConstraintsWrongParameterType() throws Exception {
//
//		List<ProfileSpecificationDescriptorImpl> specs = new ProfileSpecificationDescriptorFactory()
//		.parse(super.getFileStream(_PROFILE_SPEC_JAR_ONE_OK_LOCAL_INTERFACE_CONSTRAINTS));
//
//		ProfileSpecificationDescriptorImpl descriptor = specs.get(0);
//		ProfileSpecificationComponent component=new ProfileSpecificationComponent(descriptor);
//		component.setProfileCmpInterfaceClass(Thread.currentThread()
//				.getContextClassLoader().loadClass(
//						descriptor.getProfileClasses().getProfileCMPInterface()
//								.getProfileCmpInterfaceName()));
//
//		component.setProfileLocalInterfaceClass(LocalInterfaceWrongType.class);
//		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();		
//		validator.setComponent(component);
//
//		boolean b = validator.validateProfileLocalInterface();
//
//		assertFalse("Local Interface class has been validated, it should not, as it decalres method with wrong parameter.", b);
//
//	}
	
	
	public void testProfileLocalInterfaceConstraintsWrongMethodProfileMBean() throws Exception {

		
		List<ProfileSpecificationDescriptorImpl> specs = new ProfileSpecificationDescriptorFactory()
		.parse(super.getFileStream(_PROFILE_SPEC_JAR_ONE_OK_LOCAL_INTERFACE_CONSTRAINTS));

		ProfileSpecificationDescriptorImpl descriptor = specs.get(0);
		ProfileSpecificationComponent component=new ProfileSpecificationComponent(descriptor);
		component.setProfileCmpInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileClasses().getProfileCMPInterface()
								.getProfileCmpInterfaceName()));

		component.setProfileLocalInterfaceClass(LocalInterfaceWrongMethod_ProfileMBean.class);
		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();		
		validator.setComponent(component);

		boolean b = validator.validateProfileLocalInterface();

		assertFalse("Local Interface class has been validated, it should not as it declared method from ProfileMBean", b);

	}
	
	public void testProfileLocalInterfaceConstraintsWrongMethodProfileLocal() throws Exception {

		
		List<ProfileSpecificationDescriptorImpl> specs = new ProfileSpecificationDescriptorFactory()
		.parse(super.getFileStream(_PROFILE_SPEC_JAR_ONE_OK_LOCAL_INTERFACE_CONSTRAINTS));

		ProfileSpecificationDescriptorImpl descriptor = specs.get(0);
		ProfileSpecificationComponent component=new ProfileSpecificationComponent(descriptor);
		component.setProfileCmpInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileClasses().getProfileCMPInterface()
								.getProfileCmpInterfaceName()));

		component.setProfileLocalInterfaceClass(LocalInterfaceWrongMethod_ProfileManagement.class);
		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();		
		validator.setComponent(component);

		boolean b = validator.validateProfileLocalInterface();

		assertFalse("Local Interface class has been validated, it should not as it declared method from ProfileLocal", b);

	}
	
	public void testProfileLocalInterfaceConstraintsWrongMethodProfile() throws Exception {

		
		List<ProfileSpecificationDescriptorImpl> specs = new ProfileSpecificationDescriptorFactory()
		.parse(super.getFileStream(_PROFILE_SPEC_JAR_ONE_OK_LOCAL_INTERFACE_CONSTRAINTS));

		ProfileSpecificationDescriptorImpl descriptor = specs.get(0);
		ProfileSpecificationComponent component=new ProfileSpecificationComponent(descriptor);
		component.setProfileCmpInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileClasses().getProfileCMPInterface()
								.getProfileCmpInterfaceName()));

		component.setProfileLocalInterfaceClass(LocalInterfaceWrongMethod_Profile.class);
		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();		
		validator.setComponent(component);

		boolean b = validator.validateProfileLocalInterface();

		assertFalse("Local Interface class has been validated, it should not as it declared method from Profile", b);

	}
	
	public void testProfileLocalInterfaceConstraintsWrongMethodDynamicMBean() throws Exception {

		
		List<ProfileSpecificationDescriptorImpl> specs = new ProfileSpecificationDescriptorFactory()
		.parse(super.getFileStream(_PROFILE_SPEC_JAR_ONE_OK_LOCAL_INTERFACE_CONSTRAINTS));

		ProfileSpecificationDescriptorImpl descriptor = specs.get(0);
		ProfileSpecificationComponent component=new ProfileSpecificationComponent(descriptor);
		component.setProfileCmpInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileClasses().getProfileCMPInterface()
								.getProfileCmpInterfaceName()));

		component.setProfileLocalInterfaceClass(LocalInterfaceWrongMethod_DynamicMBean.class);
		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();		
		validator.setComponent(component);

		boolean b = validator.validateProfileLocalInterface();

		assertFalse("Local Interface class has been validated, it should not as it declared method from DynamicMBean", b);

	}
	
	public void testProfileLocalInterfaceConstraintsWrongMethodMBeanRegistration() throws Exception {

		
		List<ProfileSpecificationDescriptorImpl> specs = new ProfileSpecificationDescriptorFactory()
		.parse(super.getFileStream(_PROFILE_SPEC_JAR_ONE_OK_LOCAL_INTERFACE_CONSTRAINTS));

		ProfileSpecificationDescriptorImpl descriptor = specs.get(0);
		ProfileSpecificationComponent component=new ProfileSpecificationComponent(descriptor);
		component.setProfileCmpInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileClasses().getProfileCMPInterface()
								.getProfileCmpInterfaceName()));

		component.setProfileLocalInterfaceClass(LocalInterfaceWrongMethod_MBeanRegistration.class);
		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();		
		validator.setComponent(component);

		boolean b = validator.validateProfileLocalInterface();

		assertFalse("Local Interface class has been validated, it should not as it declared method from MBeanRegistration", b);

	}
	
	public void testProfileLocalInterfaceConstraintsNoSuperInterface() throws Exception {

		
		List<ProfileSpecificationDescriptorImpl> specs = new ProfileSpecificationDescriptorFactory()
		.parse(super.getFileStream(_PROFILE_SPEC_JAR_ONE_OK_LOCAL_INTERFACE_CONSTRAINTS));

		ProfileSpecificationDescriptorImpl descriptor = specs.get(0);
		ProfileSpecificationComponent component=new ProfileSpecificationComponent(descriptor);
		component.setProfileCmpInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileClasses().getProfileCMPInterface()
								.getProfileCmpInterfaceName()));

		component.setProfileLocalInterfaceClass(LocalInterfaceNoProfileLocalObject.class);
		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();		
		validator.setComponent(component);

		boolean b = validator.validateProfileLocalInterface();

		assertFalse("Local Interface class has been validated, it should not as it does not extends ProfileLocalObject", b);

	}
}
