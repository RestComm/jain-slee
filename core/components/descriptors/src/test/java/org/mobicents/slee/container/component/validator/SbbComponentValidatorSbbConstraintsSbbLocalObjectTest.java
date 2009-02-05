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

import org.mobicents.slee.container.component.SbbComponent;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.SbbDescriptorImpl;
import org.mobicents.slee.container.component.validator.ClassUtils;
import org.mobicents.slee.container.component.validator.SbbComponentValidator;

/**
 * Start time:17:07:31 2009-01-31<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class SbbComponentValidatorSbbConstraintsSbbLocalObjectTest extends
		ValidatorSuperTestCase {

	public static final String _SBB_JAR_ONE_11_OK_CONSTRAINTS_SBB_LOCAL_INTERFACE = "xml/validator/sbb/sbblo/sbb-jar-one-SbbConstraintsSbbLocalInterfaceOk_1_1.xml";
	public static final String _SBB_JAR_ONE_11_WRONG_THROWS_SBB_LOCAL_INTERFACE = "xml/validator/sbb/sbblo/sbb-jar-one-SbbConstraintsSbbLocalInterfaceWrongThrows_1_1.xml";
	public static final String _SBB_JAR_ONE_11_WRONG_RETURN_TYPE_SBB_LOCAL_INTERFACE = "xml/validator/sbb/sbblo/sbb-jar-one-SbbConstraintsSbbLocalInterfaceWrongReturnType_1_1.xml";
	public static final String _SBB_JAR_ONE_11_MISSING_METHOD_SBB_LOCAL_INTERFACE = "xml/validator/sbb/sbblo/sbb-jar-one-SbbConstraintsSbbLocalInterfaceMissingMethod_1_1.xml";
	
	
	
	public void testSbbOne11ConstraintsSbbLocalInterfaceOk() throws Exception {
		final SbbDescriptorImpl descriptor = SbbDescriptorImpl.parseDocument(
				super.parseDocument(_SBB_JAR_ONE_11_OK_CONSTRAINTS_SBB_LOCAL_INTERFACE), null)[0];
		SbbComponent component = new SbbComponent();
		component.setAbstractSbbClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getSbbAbstractClass()
								.getSbbAbstractClassName()));
		component.setSbbLocalInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getSbbLocalInterface().getSbbLocalInterfaceName()));

		SbbComponentValidator validator = new SbbComponentValidator();
		component.setDescriptor(descriptor);
		validator.setComponent(component);

		boolean b = validator.validateSbbLocalInterface(
				ClassUtils.getConcreteMethodsFromClass(component
						.getAbstractSbbClass()), ClassUtils
						.getSuperClassesConcreteMethodsFromClass(component
								.getAbstractSbbClass()));

		assertTrue("Sbb class has not been validated", b);

	}

	
	

	public void testSbbOne11ConstraintsSbbLocalInterfaceWrongReturnType() throws Exception {
		final SbbDescriptorImpl descriptor = SbbDescriptorImpl.parseDocument(
				super.parseDocument(_SBB_JAR_ONE_11_WRONG_RETURN_TYPE_SBB_LOCAL_INTERFACE), null)[0];
		SbbComponent component = new SbbComponent();
		component.setAbstractSbbClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getSbbAbstractClass()
								.getSbbAbstractClassName()));
		component.setSbbLocalInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getSbbLocalInterface().getSbbLocalInterfaceName()));

		SbbComponentValidator validator = new SbbComponentValidator();
		component.setDescriptor(descriptor);
		validator.setComponent(component);

		boolean b = validator.validateSbbLocalInterface(
				ClassUtils.getConcreteMethodsFromClass(component
						.getAbstractSbbClass()), ClassUtils
						.getSuperClassesConcreteMethodsFromClass(component
								.getAbstractSbbClass()));

		assertFalse("Sbb class has been validated", b);

	}
	
	public void testSbbOne11ConstraintsSbbLocalInterfaceWrongThrows() throws Exception {
		final SbbDescriptorImpl descriptor = SbbDescriptorImpl.parseDocument(
				super.parseDocument(_SBB_JAR_ONE_11_WRONG_THROWS_SBB_LOCAL_INTERFACE), null)[0];
		SbbComponent component = new SbbComponent();
		component.setAbstractSbbClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getSbbAbstractClass()
								.getSbbAbstractClassName()));
		component.setSbbLocalInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getSbbLocalInterface().getSbbLocalInterfaceName()));

		SbbComponentValidator validator = new SbbComponentValidator();
		component.setDescriptor(descriptor);
		validator.setComponent(component);

		boolean b = validator.validateSbbLocalInterface(
				ClassUtils.getConcreteMethodsFromClass(component
						.getAbstractSbbClass()), ClassUtils
						.getSuperClassesConcreteMethodsFromClass(component
								.getAbstractSbbClass()));

		assertFalse("Sbb class has been validated", b);

	}
	
	
	
	
	public void testSbbOne11ConstraintsSbbLocalInterfaceMissingMethod() throws Exception {
		final SbbDescriptorImpl descriptor = SbbDescriptorImpl.parseDocument(
				super.parseDocument(_SBB_JAR_ONE_11_MISSING_METHOD_SBB_LOCAL_INTERFACE), null)[0];
		SbbComponent component = new SbbComponent();
		component.setAbstractSbbClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getSbbAbstractClass()
								.getSbbAbstractClassName()));
		component.setSbbLocalInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getSbbLocalInterface().getSbbLocalInterfaceName()));
		SbbComponentValidator validator = new SbbComponentValidator();
		component.setDescriptor(descriptor);
		validator.setComponent(component);

		boolean b = validator.validateSbbLocalInterface(
				ClassUtils.getConcreteMethodsFromClass(component
						.getAbstractSbbClass()), ClassUtils
						.getSuperClassesConcreteMethodsFromClass(component
								.getAbstractSbbClass()));

		assertFalse("Sbb class has been validated", b);

	}
	
}
