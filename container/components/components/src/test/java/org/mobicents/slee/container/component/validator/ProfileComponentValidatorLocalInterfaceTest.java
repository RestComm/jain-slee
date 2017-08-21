/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

/**
 * Start time:17:07:31 2009-01-31<br>
 * Project: restcomm-jainslee-server-core<br>
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


/**
 * Start time:17:07:31 2009-01-31<br>
 * Project: restcomm-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ProfileComponentValidatorLocalInterfaceTest extends
TCUtilityClass {

	public static final String _PROFILE_SPEC_JAR_ONE_OK_LOCAL_INTERFACE_CONSTRAINTS = "xml/validator/profile/local/profile-spec-jar-one-LocalInterface.xml";
	
	
	public void testProfileLocalInterfaceConstraintsOk() throws Exception {

		
		List<ProfileSpecificationDescriptorImpl> specs = new ProfileSpecificationDescriptorFactoryImpl()
		.parse(super.getFileStream(_PROFILE_SPEC_JAR_ONE_OK_LOCAL_INTERFACE_CONSTRAINTS));

		ProfileSpecificationDescriptorImpl descriptor = specs.get(0);
		ProfileSpecificationComponentImpl component=new ProfileSpecificationComponentImpl(descriptor);
		component.setProfileCmpInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileCMPInterface()
								.getProfileCmpInterfaceName()));

		component.setProfileLocalInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileLocalInterface()
								.getProfileLocalInterfaceName()));
		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();		
		validator.setComponent(component);

		boolean b = validator.validateProfileLocalInterface();

		assertTrue("Local Interface class has not been validated", b);

	}
	
	
	public void testProfileLocalInterfaceConstraintsOkExtendCMPInterface() throws Exception {

		
		List<ProfileSpecificationDescriptorImpl> specs = new ProfileSpecificationDescriptorFactoryImpl()
		.parse(super.getFileStream(_PROFILE_SPEC_JAR_ONE_OK_LOCAL_INTERFACE_CONSTRAINTS));

		ProfileSpecificationDescriptorImpl descriptor = specs.get(0);
		ProfileSpecificationComponentImpl component=new ProfileSpecificationComponentImpl(descriptor);
		component.setProfileCmpInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileCMPInterface()
								.getProfileCmpInterfaceName()));

		component.setProfileLocalInterfaceClass(LocalInterfaceOkExtendCMPInterface.class);
		
		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();		
		validator.setComponent(component);

		boolean b = validator.validateCMPInterface();

		assertTrue("CMP Interface class has not been validated", b);

	}
	
	public void testProfileLocalInterfaceConstraintsOkDeclareValidCMPMethod() throws Exception {

		
		List<ProfileSpecificationDescriptorImpl> specs = new ProfileSpecificationDescriptorFactoryImpl()
		.parse(super.getFileStream(_PROFILE_SPEC_JAR_ONE_OK_LOCAL_INTERFACE_CONSTRAINTS));

		ProfileSpecificationDescriptorImpl descriptor = specs.get(0);
		ProfileSpecificationComponentImpl component=new ProfileSpecificationComponentImpl(descriptor);
		component.setProfileCmpInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileCMPInterface()
								.getProfileCmpInterfaceName()));

		component.setProfileLocalInterfaceClass(LocalInterfaceOkDeclareValidCMPMethod.class);
		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();		
		validator.setComponent(component);

		boolean b = validator.validateProfileLocalInterface();

		assertTrue("Local Interface class has not been validated - it does not extend CMP interface, however it is valid to declare CMP method.", b);

	}
	
	public void testProfileLocalInterfaceConstraintsDeclareValidCMPMethodWithThrows() throws Exception {

		
		List<ProfileSpecificationDescriptorImpl> specs = new ProfileSpecificationDescriptorFactoryImpl()
		.parse(super.getFileStream(_PROFILE_SPEC_JAR_ONE_OK_LOCAL_INTERFACE_CONSTRAINTS));

		ProfileSpecificationDescriptorImpl descriptor = specs.get(0);
		ProfileSpecificationComponentImpl component=new ProfileSpecificationComponentImpl(descriptor);
		component.setProfileCmpInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileCMPInterface()
								.getProfileCmpInterfaceName()));

		component.setProfileLocalInterfaceClass(LocalInterfaceOkDeclareValidCMPMethodWithThrows.class);
		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();		
		validator.setComponent(component);

		boolean b = validator.validateProfileLocalInterface();

		assertFalse("Local Interface class has been validated, it should not - it declares CMP methods with throws clause.", b);

	}
	
	public void testProfileLocalInterfaceConstraintsWrongPrefix() throws Exception {

		
		List<ProfileSpecificationDescriptorImpl> specs = new ProfileSpecificationDescriptorFactoryImpl()
		.parse(super.getFileStream(_PROFILE_SPEC_JAR_ONE_OK_LOCAL_INTERFACE_CONSTRAINTS));

		ProfileSpecificationDescriptorImpl descriptor = specs.get(0);
		ProfileSpecificationComponentImpl component=new ProfileSpecificationComponentImpl(descriptor);
		component.setProfileCmpInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileCMPInterface()
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
//		ProfileSpecificationComponentImpl component=new ProfileSpecificationComponentImpl(descriptor);
//		component.setProfileCmpInterfaceClass(Thread.currentThread()
//				.getContextClassLoader().loadClass(
//						descriptor.getProfileCMPInterface()
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

		
		List<ProfileSpecificationDescriptorImpl> specs = new ProfileSpecificationDescriptorFactoryImpl()
		.parse(super.getFileStream(_PROFILE_SPEC_JAR_ONE_OK_LOCAL_INTERFACE_CONSTRAINTS));

		ProfileSpecificationDescriptorImpl descriptor = specs.get(0);
		ProfileSpecificationComponentImpl component=new ProfileSpecificationComponentImpl(descriptor);
		component.setProfileCmpInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileCMPInterface()
								.getProfileCmpInterfaceName()));

		component.setProfileLocalInterfaceClass(LocalInterfaceWrongMethod_ProfileMBean.class);
		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();		
		validator.setComponent(component);

		boolean b = validator.validateProfileLocalInterface();

		assertFalse("Local Interface class has been validated, it should not as it declared method from ProfileMBean", b);

	}
	
	public void testProfileLocalInterfaceConstraintsWrongMethodProfileLocal() throws Exception {

		
		List<ProfileSpecificationDescriptorImpl> specs = new ProfileSpecificationDescriptorFactoryImpl()
		.parse(super.getFileStream(_PROFILE_SPEC_JAR_ONE_OK_LOCAL_INTERFACE_CONSTRAINTS));

		ProfileSpecificationDescriptorImpl descriptor = specs.get(0);
		ProfileSpecificationComponentImpl component=new ProfileSpecificationComponentImpl(descriptor);
		component.setProfileCmpInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileCMPInterface()
								.getProfileCmpInterfaceName()));

		component.setProfileLocalInterfaceClass(LocalInterfaceWrongMethod_ProfileManagement.class);
		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();		
		validator.setComponent(component);

		boolean b = validator.validateProfileLocalInterface();

		assertFalse("Local Interface class has been validated, it should not as it declared method from ProfileLocal", b);

	}
	
	public void testProfileLocalInterfaceConstraintsWrongMethodProfile() throws Exception {

		
		List<ProfileSpecificationDescriptorImpl> specs = new ProfileSpecificationDescriptorFactoryImpl()
		.parse(super.getFileStream(_PROFILE_SPEC_JAR_ONE_OK_LOCAL_INTERFACE_CONSTRAINTS));

		ProfileSpecificationDescriptorImpl descriptor = specs.get(0);
		ProfileSpecificationComponentImpl component=new ProfileSpecificationComponentImpl(descriptor);
		component.setProfileCmpInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileCMPInterface()
								.getProfileCmpInterfaceName()));

		component.setProfileLocalInterfaceClass(LocalInterfaceWrongMethod_Profile.class);
		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();		
		validator.setComponent(component);

		boolean b = validator.validateProfileLocalInterface();

		assertFalse("Local Interface class has been validated, it should not as it declared method from Profile", b);

	}
	
	public void testProfileLocalInterfaceConstraintsWrongMethodDynamicMBean() throws Exception {

		
		List<ProfileSpecificationDescriptorImpl> specs = new ProfileSpecificationDescriptorFactoryImpl()
		.parse(super.getFileStream(_PROFILE_SPEC_JAR_ONE_OK_LOCAL_INTERFACE_CONSTRAINTS));

		ProfileSpecificationDescriptorImpl descriptor = specs.get(0);
		ProfileSpecificationComponentImpl component=new ProfileSpecificationComponentImpl(descriptor);
		component.setProfileCmpInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileCMPInterface()
								.getProfileCmpInterfaceName()));

		component.setProfileLocalInterfaceClass(LocalInterfaceWrongMethod_DynamicMBean.class);
		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();		
		validator.setComponent(component);

		boolean b = validator.validateProfileLocalInterface();

		assertFalse("Local Interface class has been validated, it should not as it declared method from DynamicMBean", b);

	}
	
	public void testProfileLocalInterfaceConstraintsWrongMethodMBeanRegistration() throws Exception {

		
		List<ProfileSpecificationDescriptorImpl> specs = new ProfileSpecificationDescriptorFactoryImpl()
		.parse(super.getFileStream(_PROFILE_SPEC_JAR_ONE_OK_LOCAL_INTERFACE_CONSTRAINTS));

		ProfileSpecificationDescriptorImpl descriptor = specs.get(0);
		ProfileSpecificationComponentImpl component=new ProfileSpecificationComponentImpl(descriptor);
		component.setProfileCmpInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileCMPInterface()
								.getProfileCmpInterfaceName()));

		component.setProfileLocalInterfaceClass(LocalInterfaceWrongMethod_MBeanRegistration.class);
		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();		
		validator.setComponent(component);

		boolean b = validator.validateProfileLocalInterface();

		assertFalse("Local Interface class has been validated, it should not as it declared method from MBeanRegistration", b);

	}
	
	public void testProfileLocalInterfaceConstraintsNoSuperInterface() throws Exception {

		
		List<ProfileSpecificationDescriptorImpl> specs = new ProfileSpecificationDescriptorFactoryImpl()
		.parse(super.getFileStream(_PROFILE_SPEC_JAR_ONE_OK_LOCAL_INTERFACE_CONSTRAINTS));

		ProfileSpecificationDescriptorImpl descriptor = specs.get(0);
		ProfileSpecificationComponentImpl component=new ProfileSpecificationComponentImpl(descriptor);
		component.setProfileCmpInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getProfileCMPInterface()
								.getProfileCmpInterfaceName()));

		component.setProfileLocalInterfaceClass(LocalInterfaceNoProfileLocalObject.class);
		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();		
		validator.setComponent(component);

		boolean b = validator.validateProfileLocalInterface();

		assertFalse("Local Interface class has been validated, it should not as it does not extends ProfileLocalObject", b);

	}
}
