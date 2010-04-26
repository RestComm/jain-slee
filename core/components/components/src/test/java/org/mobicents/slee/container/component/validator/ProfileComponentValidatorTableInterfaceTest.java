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
import org.mobicents.slee.container.component.validator.profile.tableinterface.ProfileTableInterfaceLackQuery;
import org.mobicents.slee.container.component.validator.profile.tableinterface.ProfileTableInterfaceNotEnoughParameters;
import org.mobicents.slee.container.component.validator.profile.tableinterface.ProfileTableInterfaceNotMatchedQuery;
import org.mobicents.slee.container.component.validator.profile.tableinterface.ProfileTableInterfaceWrongParameterType;

/**
 * Start time:17:07:31 2009-01-31<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ProfileComponentValidatorTableInterfaceTest extends TCUtilityClass {

	public static final String _PROFILE_SPEC_JAR_ONE_PROFILE_TABLE_OK_CONSTRAINTS = "xml/validator/profile/table/profile-spec-jar-one.xml";
	public static final String _PROFILE_SPEC_JAR_ONE_PROFILE_TABLE_WRONG_OP_CONSTRAINTS = "xml/validator/profile/table/profile-spec-jar-WrongOperator.xml";
	public static final String _PROFILE_SPEC_JAR_ONE_PROFILE_TABLE_COLLATOR_ON_NONSTRING_CONSTRAINTS = "xml/validator/profile/table/profile-spec-jar-one-CollatorOnNonString.xml";
	public static final String _PROFILE_SPEC_JAR_ONE_PROFILE_ADDRESS_CONSTRAINTS = "xml/validator/profile/table/profile-spec-jar-one-Address.xml";
	public static final String _PROFILE_SPEC_JAR_ONE_LACK_OF_PARAMANDVALUE_CONSTRAINTS = "xml/validator/profile/table/profile-spec-jar-one-LackOfParamAndValue.xml";
	
	
	
	public void testProfileTableInterfaceConstraintsOk() throws Exception {
	
		List<ProfileSpecificationDescriptorImpl> specs = new ProfileSpecificationDescriptorFactoryImpl()
		.parse(super.getFileStream(_PROFILE_SPEC_JAR_ONE_PROFILE_TABLE_OK_CONSTRAINTS));

		
		
		ProfileSpecificationDescriptorImpl descriptor = specs.get(0);
		ProfileSpecificationComponentImpl component = new ProfileSpecificationComponentImpl(
				descriptor);
		component.setProfileCmpInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileCMPInterface()
								.getProfileCmpInterfaceName()));

		

		component.setProfileTableInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileTableInterface()));

		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();
		validator.setComponent(component);

		boolean b = validator.validateProfileTableInterface();

		assertTrue("Table interface class class has not been validated", b);

	}
	
	public void testProfileTableInterfaceConstraintsWrongParameterType() throws Exception {

		List<ProfileSpecificationDescriptorImpl> specs = new ProfileSpecificationDescriptorFactoryImpl()
		.parse(super.getFileStream(_PROFILE_SPEC_JAR_ONE_PROFILE_TABLE_OK_CONSTRAINTS));
		ProfileSpecificationDescriptorImpl descriptor = specs.get(0);
		ProfileSpecificationComponentImpl component = new ProfileSpecificationComponentImpl(
				descriptor);
		component.setProfileCmpInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileCMPInterface()
								.getProfileCmpInterfaceName()));

		

		component.setProfileTableInterfaceClass(ProfileTableInterfaceWrongParameterType.class);

		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();
		validator.setComponent(component);

		boolean b = validator.validateProfileTableInterface();

		assertFalse("Table interface class has been validated - it should not since table interface method does not match declared parameter type", b);

	}
	
	public void testProfileTableInterfaceConstraintsNotEnoughParameters() throws Exception {

		List<ProfileSpecificationDescriptorImpl> specs = new ProfileSpecificationDescriptorFactoryImpl()
		.parse(super.getFileStream(_PROFILE_SPEC_JAR_ONE_PROFILE_TABLE_OK_CONSTRAINTS));

		ProfileSpecificationDescriptorImpl descriptor = specs.get(0);
		ProfileSpecificationComponentImpl component = new ProfileSpecificationComponentImpl(
				descriptor);
		component.setProfileCmpInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileCMPInterface()
								.getProfileCmpInterfaceName()));

		

		component.setProfileTableInterfaceClass(ProfileTableInterfaceNotEnoughParameters.class);

		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();
		validator.setComponent(component);

		boolean b = validator.validateProfileTableInterface();

		assertFalse("Table interface class has been validated - it should not since table interface method does not match declared parameter type", b);

	}



	public void testProfileTableInterfaceConstraintsLackQuery() throws Exception {

		List<ProfileSpecificationDescriptorImpl> specs = new ProfileSpecificationDescriptorFactoryImpl()
		.parse(super.getFileStream(_PROFILE_SPEC_JAR_ONE_PROFILE_TABLE_OK_CONSTRAINTS));

		ProfileSpecificationDescriptorImpl descriptor = specs.get(0);
		ProfileSpecificationComponentImpl component = new ProfileSpecificationComponentImpl(
				descriptor);
		component.setProfileCmpInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileCMPInterface()
								.getProfileCmpInterfaceName()));

		

		component.setProfileTableInterfaceClass(ProfileTableInterfaceLackQuery.class);

		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();
		validator.setComponent(component);

		boolean b = validator.validateProfileTableInterface();

		assertFalse("Table interface class has been validated - it should not since table interface does not decalre methods for all queries", b);

	}
	
	
	
	
	public void testProfileTableInterfaceConstraintsNotMatchedQuery() throws Exception {

		List<ProfileSpecificationDescriptorImpl> specs = new ProfileSpecificationDescriptorFactoryImpl()
		.parse(super.getFileStream(_PROFILE_SPEC_JAR_ONE_PROFILE_TABLE_OK_CONSTRAINTS));

		ProfileSpecificationDescriptorImpl descriptor = specs.get(0);
		ProfileSpecificationComponentImpl component = new ProfileSpecificationComponentImpl(
				descriptor);
		component.setProfileCmpInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileCMPInterface()
								.getProfileCmpInterfaceName()));

		

		component.setProfileTableInterfaceClass(ProfileTableInterfaceNotMatchedQuery.class);

		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();
		validator.setComponent(component);

		boolean b = validator.validateProfileTableInterface();

		assertFalse("Table interface class has been validated - it should not since table interface declares method which does not match any declared query", b);

	}
	
	public void testProfileTableInterfaceConstraintsWrongOP() throws Exception {
	
		List<ProfileSpecificationDescriptorImpl> specs = new ProfileSpecificationDescriptorFactoryImpl()
		.parse(super.getFileStream(_PROFILE_SPEC_JAR_ONE_PROFILE_TABLE_WRONG_OP_CONSTRAINTS));

		ProfileSpecificationDescriptorImpl descriptor = specs.get(0);
		ProfileSpecificationComponentImpl component = new ProfileSpecificationComponentImpl(
				descriptor);
		component.setProfileCmpInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileCMPInterface()
								.getProfileCmpInterfaceName()));

		

		component.setProfileTableInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileTableInterface()));

		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();
		validator.setComponent(component);

		boolean b = validator.validateProfileTableInterface();

		assertFalse("Table interface class has been validated - it should not since it decalres wrong operator for parameter of type boolean", b);

	}
	
	public void testProfileTableInterfaceConstraintsCollatorOnNonString() throws Exception {

		List<ProfileSpecificationDescriptorImpl> specs = new ProfileSpecificationDescriptorFactoryImpl()
		.parse(super.getFileStream(_PROFILE_SPEC_JAR_ONE_PROFILE_TABLE_COLLATOR_ON_NONSTRING_CONSTRAINTS));

		ProfileSpecificationDescriptorImpl descriptor = specs.get(0);
		ProfileSpecificationComponentImpl component = new ProfileSpecificationComponentImpl(
				descriptor);
		component.setProfileCmpInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileCMPInterface()
								.getProfileCmpInterfaceName()));

		

		component.setProfileTableInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileTableInterface()));

		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();
		validator.setComponent(component);

		boolean b = validator.validateProfileTableInterface();

		assertFalse("Table interface class has been validated - it should not since it declares collator for non string parameter", b);

	}
	
	

	
	public void testProfileTableInterfaceConstraintsExceptionalType() throws Exception {

		List<ProfileSpecificationDescriptorImpl> specs = new ProfileSpecificationDescriptorFactoryImpl()
		.parse(super.getFileStream(_PROFILE_SPEC_JAR_ONE_PROFILE_ADDRESS_CONSTRAINTS));

		ProfileSpecificationDescriptorImpl descriptor = specs.get(0);
		ProfileSpecificationComponentImpl component = new ProfileSpecificationComponentImpl(
				descriptor);
		component.setProfileCmpInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileCMPInterface()
								.getProfileCmpInterfaceName()));

		

		component.setProfileTableInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileTableInterface()));

		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();
		validator.setComponent(component);

		assertTrue("Failed to validate descriptor for allowed type(conditionaly):javax.slee.Address",validator.validateDescriptor());
		assertTrue("failed to validate cmp interface with address cmp",validator.validateCMPInterface());
		
		// FIXME: Alexandre: This is not allowed in Query Parameter, to have javax.slee.Address type
		//assertTrue("Failed to validate profile table interface for allowed type(conditionaly): javax.slee.Address",validator.validateProfileTableInterface());
		
		//assertFalse("Table interface class has been validated - it should not since it declares collator for non string parameter", b);

	}
	
	public void testProfileTableInterfaceConstraintsLackOfParamAndValue() throws Exception {

	
		List<ProfileSpecificationDescriptorImpl> specs = new ProfileSpecificationDescriptorFactoryImpl()
		.parse(super.getFileStream(_PROFILE_SPEC_JAR_ONE_LACK_OF_PARAMANDVALUE_CONSTRAINTS));
	
		ProfileSpecificationDescriptorImpl descriptor = specs.get(0);
		ProfileSpecificationComponentImpl component = new ProfileSpecificationComponentImpl(
				descriptor);
		component.setProfileCmpInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileCMPInterface()
								.getProfileCmpInterfaceName()));

		

		component.setProfileTableInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileTableInterface()));

		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();
		validator.setComponent(component);

		assertFalse("Descrip[tor has been validate, even though expression does not provide either value or parameter",validator.validateDescriptor());
		

	}
	
	
	
}
