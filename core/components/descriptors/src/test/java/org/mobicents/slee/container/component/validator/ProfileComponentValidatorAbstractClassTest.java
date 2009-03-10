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

import org.mobicents.slee.container.component.ProfileSpecificationComponent;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ProfileSpecificationDescriptorFactory;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ProfileSpecificationDescriptorImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.TCUtilityClass;
import org.mobicents.slee.container.component.validator.profile.abstrakt.ProfileAbstractClass10LackLifeCycle;
import org.mobicents.slee.container.component.validator.profile.abstrakt.ProfileAbstractClassConcreteUsageParametersAccess;
import org.mobicents.slee.container.component.validator.profile.abstrakt.ProfileAbstractClassDeclareConcreteCMP;
import org.mobicents.slee.container.component.validator.profile.abstrakt.ProfileAbstractClassDefaultConstructorThrows;
import org.mobicents.slee.container.component.validator.profile.abstrakt.ProfileAbstractClassImplementingProfileLocalObject;
import org.mobicents.slee.container.component.validator.profile.abstrakt.ProfileAbstractClassLackLifeCycle;
import org.mobicents.slee.container.component.validator.profile.abstrakt.ProfileAbstractClassNoDefaultConstructor;
import org.mobicents.slee.container.component.validator.profile.abstrakt.ProfileAbstractClassNoUsageParametersAccess;
import org.mobicents.slee.container.component.validator.profile.abstrakt.ProfileAbstractClassNotImplementingCMP;
import org.mobicents.slee.container.component.validator.profile.abstrakt.ProfileAbstractClassNotImplementingManagementInterface;
import org.mobicents.slee.container.component.validator.profile.abstrakt.ProfileAbstractClassNotImplementingProfileLocalMethod;

/**
 * Start time:17:07:31 2009-01-31<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ProfileComponentValidatorAbstractClassTest extends TCUtilityClass {

	public static final String _PROFILE_SPEC_JAR_ONE_ABSTRACTCLASS_OK_CONSTRAINTS = "xml/validator/profile/abstrakt/profile-spec-jar-one.xml";

	public static final String _PROFILE_SPEC_JAR_ONE_ABSTRACTCLASS_OK_CONSTRAINTS_10 = "xml/validator/profile/abstrakt/profile-spec-jar-one10.xml";
	
	public void testAbstractClassConstraintsOk() throws Exception {
		
		List<ProfileSpecificationDescriptorImpl> specs = new ProfileSpecificationDescriptorFactory()
		.parse(super.getFileStream(_PROFILE_SPEC_JAR_ONE_ABSTRACTCLASS_OK_CONSTRAINTS));

		ProfileSpecificationDescriptorImpl descriptor = specs.get(0);
		
		ProfileSpecificationComponent component = new ProfileSpecificationComponent(
				descriptor);
		component.setProfileCmpInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileClasses().getProfileCMPInterface()
								.getProfileCmpInterfaceName()));

		component.setProfileAbstractClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileClasses().getProfileAbstractClass()
								.getProfileAbstractClassName()));

		component.setProfileManagementInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileClasses().getProfileManagementInterface()
								.getProfileManagementInterfaceName()));

		component.setProfileLocalInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileClasses().getProfileLocalInterface()
								.getProfileLocalInterfaceName()));
		component.setUsageParametersInterface(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileClasses().getProfileUsageParameterInterface()
								.getUsageParametersInterfaceName()));

		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();
		validator.setComponent(component);

		boolean b = validator.validateAbstractClass();

		assertTrue("Abstract class class has not been validated", b);

	}
	
	
	
	public void testAbstractClassConstraintsDeclareConcreteCMPMethod() throws Exception {
	
		
		List<ProfileSpecificationDescriptorImpl> specs = new ProfileSpecificationDescriptorFactory()
		.parse(super.getFileStream(_PROFILE_SPEC_JAR_ONE_ABSTRACTCLASS_OK_CONSTRAINTS));

		ProfileSpecificationDescriptorImpl descriptor = specs.get(0);
		
		
		ProfileSpecificationComponent component = new ProfileSpecificationComponent(
				descriptor);
		component.setProfileCmpInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileClasses().getProfileCMPInterface()
								.getProfileCmpInterfaceName()));

		component.setProfileAbstractClass(ProfileAbstractClassDeclareConcreteCMP.class);

		component.setProfileManagementInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileClasses().getProfileManagementInterface()
								.getProfileManagementInterfaceName()));

		component.setProfileLocalInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileClasses().getProfileLocalInterface()
								.getProfileLocalInterfaceName()));
		component.setUsageParametersInterface(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileClasses().getProfileUsageParameterInterface()
								.getUsageParametersInterfaceName()));

		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();
		validator.setComponent(component);

		boolean b = validator.validateAbstractClass();

		assertFalse("Abstract class class has been validated, it should not - since it declared concrete CMP method", b);

	}
	
	public void testAbstractClassConstraintsNotImplementingCMP() throws Exception {

		
		List<ProfileSpecificationDescriptorImpl> specs = new ProfileSpecificationDescriptorFactory()
		.parse(super.getFileStream(_PROFILE_SPEC_JAR_ONE_ABSTRACTCLASS_OK_CONSTRAINTS));

		ProfileSpecificationDescriptorImpl descriptor = specs.get(0);
		ProfileSpecificationComponent component = new ProfileSpecificationComponent(
				descriptor);
		component.setProfileCmpInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileClasses().getProfileCMPInterface()
								.getProfileCmpInterfaceName()));

		component.setProfileAbstractClass(ProfileAbstractClassNotImplementingCMP.class);

		component.setProfileManagementInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileClasses().getProfileManagementInterface()
								.getProfileManagementInterfaceName()));

		component.setProfileLocalInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileClasses().getProfileLocalInterface()
								.getProfileLocalInterfaceName()));
		component.setUsageParametersInterface(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileClasses().getProfileUsageParameterInterface()
								.getUsageParametersInterfaceName()));

		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();
		validator.setComponent(component);

		boolean b = validator.validateAbstractClass();

		assertFalse("Abstract class class has been validated, it should not - since it  does not implement CMP interface", b);

	}

	public void testAbstractClassConstraintsNotImplementingManagementInterface() throws Exception {

		
		List<ProfileSpecificationDescriptorImpl> specs = new ProfileSpecificationDescriptorFactory()
		.parse(super.getFileStream(_PROFILE_SPEC_JAR_ONE_ABSTRACTCLASS_OK_CONSTRAINTS));

		ProfileSpecificationDescriptorImpl descriptor = specs.get(0);
		ProfileSpecificationComponent component = new ProfileSpecificationComponent(
				descriptor);
		component.setProfileCmpInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileClasses().getProfileCMPInterface()
								.getProfileCmpInterfaceName()));

		component.setProfileAbstractClass(ProfileAbstractClassNotImplementingManagementInterface.class);

		component.setProfileManagementInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileClasses().getProfileManagementInterface()
								.getProfileManagementInterfaceName()));

		component.setProfileLocalInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileClasses().getProfileLocalInterface()
								.getProfileLocalInterfaceName()));
		component.setUsageParametersInterface(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileClasses().getProfileUsageParameterInterface()
								.getUsageParametersInterfaceName()));

		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();
		validator.setComponent(component);

		boolean b = validator.validateAbstractClass();

		assertFalse("Abstract class class has been validated, it should not - since it does nto implement management interface", b);

	}
	

	public void testAbstractClassConstraintsImplementingProfileLocalObject() throws Exception {

		List<ProfileSpecificationDescriptorImpl> specs = new ProfileSpecificationDescriptorFactory()
		.parse(super.getFileStream(_PROFILE_SPEC_JAR_ONE_ABSTRACTCLASS_OK_CONSTRAINTS));

		ProfileSpecificationDescriptorImpl descriptor = specs.get(0);
		ProfileSpecificationComponent component = new ProfileSpecificationComponent(
				descriptor);
		component.setProfileCmpInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileClasses().getProfileCMPInterface()
								.getProfileCmpInterfaceName()));

		component.setProfileAbstractClass(ProfileAbstractClassImplementingProfileLocalObject.class);

		component.setProfileManagementInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileClasses().getProfileManagementInterface()
								.getProfileManagementInterfaceName()));

		component.setProfileLocalInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileClasses().getProfileLocalInterface()
								.getProfileLocalInterfaceName()));
		component.setUsageParametersInterface(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileClasses().getProfileUsageParameterInterface()
								.getUsageParametersInterfaceName()));

		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();
		validator.setComponent(component);

		boolean b = validator.validateAbstractClass();

		assertFalse("Abstract class class has been validated, it should not - since it implements profile local object", b);

	}
	
	public void testAbstractClassConstraintsNotImplementingProfileLocalObjectMethod() throws Exception {

		
		List<ProfileSpecificationDescriptorImpl> specs = new ProfileSpecificationDescriptorFactory()
		.parse(super.getFileStream(_PROFILE_SPEC_JAR_ONE_ABSTRACTCLASS_OK_CONSTRAINTS));

		ProfileSpecificationDescriptorImpl descriptor = specs.get(0);
		ProfileSpecificationComponent component = new ProfileSpecificationComponent(
				descriptor);
		component.setProfileCmpInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileClasses().getProfileCMPInterface()
								.getProfileCmpInterfaceName()));

		component.setProfileAbstractClass(ProfileAbstractClassNotImplementingProfileLocalMethod.class);

		component.setProfileManagementInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileClasses().getProfileManagementInterface()
								.getProfileManagementInterfaceName()));

		component.setProfileLocalInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileClasses().getProfileLocalInterface()
								.getProfileLocalInterfaceName()));
		component.setUsageParametersInterface(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileClasses().getProfileUsageParameterInterface()
								.getUsageParametersInterfaceName()));

		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();
		validator.setComponent(component);

		boolean b = validator.validateAbstractClass();

		assertFalse("Abstract class class has been validated, it should not - since it does not implement profile local object method", b);

	}
	
	public void testAbstractClassConstraintsNoDefaultConstructor() throws Exception {

		List<ProfileSpecificationDescriptorImpl> specs = new ProfileSpecificationDescriptorFactory()
		.parse(super.getFileStream(_PROFILE_SPEC_JAR_ONE_ABSTRACTCLASS_OK_CONSTRAINTS));

		ProfileSpecificationDescriptorImpl descriptor = specs.get(0);
		ProfileSpecificationComponent component = new ProfileSpecificationComponent(
				descriptor);
		component.setProfileCmpInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileClasses().getProfileCMPInterface()
								.getProfileCmpInterfaceName()));

		component.setProfileAbstractClass(ProfileAbstractClassNoDefaultConstructor.class);

		component.setProfileManagementInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileClasses().getProfileManagementInterface()
								.getProfileManagementInterfaceName()));

		component.setProfileLocalInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileClasses().getProfileLocalInterface()
								.getProfileLocalInterfaceName()));
		component.setUsageParametersInterface(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileClasses().getProfileUsageParameterInterface()
								.getUsageParametersInterfaceName()));

		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();
		validator.setComponent(component);

		boolean b = validator.validateAbstractClass();

		assertFalse("Abstract class class has been validated, it should not - does not have default no arg consutrctor", b);

	}
	
	public void testAbstractClassConstraintsDefaultConstructorThrows() throws Exception {

		
		List<ProfileSpecificationDescriptorImpl> specs = new ProfileSpecificationDescriptorFactory()
		.parse(super.getFileStream(_PROFILE_SPEC_JAR_ONE_ABSTRACTCLASS_OK_CONSTRAINTS));

		ProfileSpecificationDescriptorImpl descriptor = specs.get(0);
		ProfileSpecificationComponent component = new ProfileSpecificationComponent(
				descriptor);
		component.setProfileCmpInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileClasses().getProfileCMPInterface()
								.getProfileCmpInterfaceName()));

		component.setProfileAbstractClass(ProfileAbstractClassDefaultConstructorThrows.class);

		component.setProfileManagementInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileClasses().getProfileManagementInterface()
								.getProfileManagementInterfaceName()));

		component.setProfileLocalInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileClasses().getProfileLocalInterface()
								.getProfileLocalInterfaceName()));
		component.setUsageParametersInterface(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileClasses().getProfileUsageParameterInterface()
								.getUsageParametersInterfaceName()));

		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();
		validator.setComponent(component);

		boolean b = validator.validateAbstractClass();

		assertFalse("Abstract class class has been validated, it should not -  default constructor decalre throws clause.", b);

	}
	
	public void testAbstractClassConstraintsNoUsageParameterInterfaceAccessMethod() throws Exception {

		
		List<ProfileSpecificationDescriptorImpl> specs = new ProfileSpecificationDescriptorFactory()
		.parse(super.getFileStream(_PROFILE_SPEC_JAR_ONE_ABSTRACTCLASS_OK_CONSTRAINTS));

		ProfileSpecificationDescriptorImpl descriptor = specs.get(0);
		ProfileSpecificationComponent component = new ProfileSpecificationComponent(
				descriptor);
		component.setProfileCmpInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileClasses().getProfileCMPInterface()
								.getProfileCmpInterfaceName()));

		component.setProfileAbstractClass(ProfileAbstractClassNoUsageParametersAccess.class);

		component.setProfileManagementInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileClasses().getProfileManagementInterface()
								.getProfileManagementInterfaceName()));

		component.setProfileLocalInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileClasses().getProfileLocalInterface()
								.getProfileLocalInterfaceName()));
		component.setUsageParametersInterface(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileClasses().getProfileUsageParameterInterface()
								.getUsageParametersInterfaceName()));

		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();
		validator.setComponent(component);

		boolean b = validator.validateAbstractClass();

		assertFalse("Abstract class class has been validated, it should not - does not declare methods to access usage.", b);

	}
	
	public void testAbstractClassConstraintsConcreteUsageParameterInterfaceAccessMethod() throws Exception {

		
		List<ProfileSpecificationDescriptorImpl> specs = new ProfileSpecificationDescriptorFactory()
		.parse(super.getFileStream(_PROFILE_SPEC_JAR_ONE_ABSTRACTCLASS_OK_CONSTRAINTS));

		ProfileSpecificationDescriptorImpl descriptor = specs.get(0);
		ProfileSpecificationComponent component = new ProfileSpecificationComponent(
				descriptor);
		component.setProfileCmpInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileClasses().getProfileCMPInterface()
								.getProfileCmpInterfaceName()));

		component.setProfileAbstractClass(ProfileAbstractClassConcreteUsageParametersAccess.class);

		component.setProfileManagementInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileClasses().getProfileManagementInterface()
								.getProfileManagementInterfaceName()));

		component.setProfileLocalInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileClasses().getProfileLocalInterface()
								.getProfileLocalInterfaceName()));
		component.setUsageParametersInterface(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileClasses().getProfileUsageParameterInterface()
								.getUsageParametersInterfaceName()));

		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();
		validator.setComponent(component);

		boolean b = validator.validateAbstractClass();

		assertFalse("Abstract class class has been validated, it should not - it does declare methods to access usage.", b);

	}
	
	
	public void testAbstractClassConstraintsLackLifeCycle() throws Exception {

		
		List<ProfileSpecificationDescriptorImpl> specs = new ProfileSpecificationDescriptorFactory()
		.parse(super.getFileStream(_PROFILE_SPEC_JAR_ONE_ABSTRACTCLASS_OK_CONSTRAINTS));

		ProfileSpecificationDescriptorImpl descriptor = specs.get(0);
		ProfileSpecificationComponent component = new ProfileSpecificationComponent(
				descriptor);
		component.setProfileCmpInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileClasses().getProfileCMPInterface()
								.getProfileCmpInterfaceName()));

		component.setProfileAbstractClass(ProfileAbstractClassLackLifeCycle.class);

		component.setProfileManagementInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileClasses().getProfileManagementInterface()
								.getProfileManagementInterfaceName()));

		component.setProfileLocalInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileClasses().getProfileLocalInterface()
								.getProfileLocalInterfaceName()));
		component.setUsageParametersInterface(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileClasses().getProfileUsageParameterInterface()
								.getUsageParametersInterfaceName()));

		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();
		validator.setComponent(component);

		boolean b = validator.validateAbstractClass();

		assertFalse("Abstract class class has been validated, it should not - it does not implement all life cycle methods.", b);

	}
	
	
	public void testAbstractClassConstraintsOk10() throws Exception {

		
		List<ProfileSpecificationDescriptorImpl> specs = new ProfileSpecificationDescriptorFactory()
		.parse(super.getFileStream(_PROFILE_SPEC_JAR_ONE_ABSTRACTCLASS_OK_CONSTRAINTS_10));

		ProfileSpecificationDescriptorImpl descriptor = specs.get(0);
		ProfileSpecificationComponent component = new ProfileSpecificationComponent(
				descriptor);
		component.setProfileCmpInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileClasses().getProfileCMPInterface()
								.getProfileCmpInterfaceName()));

		component.setProfileAbstractClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileClasses().getProfileAbstractClass()
								.getProfileAbstractClassName()));

		component.setProfileManagementInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileClasses().getProfileManagementInterface()
								.getProfileManagementInterfaceName()));

		
		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();
		validator.setComponent(component);

		boolean b = validator.validateAbstractClass();

		assertTrue("Abstract class class has not been validated", b);

	}
	public void testAbstractClassConstraintsLackLifeCycle10() throws Exception {

		
		List<ProfileSpecificationDescriptorImpl> specs = new ProfileSpecificationDescriptorFactory()
		.parse(super.getFileStream(_PROFILE_SPEC_JAR_ONE_ABSTRACTCLASS_OK_CONSTRAINTS_10));

		ProfileSpecificationDescriptorImpl descriptor = specs.get(0);
		ProfileSpecificationComponent component = new ProfileSpecificationComponent(
				descriptor);
		component.setProfileCmpInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileClasses().getProfileCMPInterface()
								.getProfileCmpInterfaceName()));

		component.setProfileAbstractClass(ProfileAbstractClass10LackLifeCycle.class);

		component.setProfileManagementInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileClasses().getProfileManagementInterface()
								.getProfileManagementInterfaceName()));

		
		
		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();
		validator.setComponent(component);

		boolean b = validator.validateAbstractClass();

		assertFalse("Abstract class class has been validated, it should not - it does not implement all life cycle methods.", b);

	}
	


	//FIXME: all other tests are the same? for 1.0 and 1.1 
	
}
