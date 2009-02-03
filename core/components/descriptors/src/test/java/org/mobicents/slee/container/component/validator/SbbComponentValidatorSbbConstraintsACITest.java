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
public class SbbComponentValidatorSbbConstraintsACITest extends
		ValidatorSuperTestCase {

	public static final String _SBB_JAR_ONE_11_OK_CONSTRAINTS = "xml/validator/sbb/sbb-jar-one-SbbConstraintsACIOk_1_1.xml";
	public static final String _SBB_JAR_ONE_11_OK_CONSTRAINTS_RETURN_BASE_TYPE = "xml/validator/sbb/sbb-jar-one-SbbConstraintsACIReturnBaseOk_1_1.xml";
	public static final String _SBB_JAR_ONE_11_OK_CONSTRAINTS_WRONG_PARAMETER_TYPE = "xml/validator/sbb/sbb-jar-one-SbbConstraintsACIWrongParameterType_1_1.xml";
	public static final String _SBB_JAR_ONE_11_OK_CONSTRAINTS_WRONG_METHOD = "xml/validator/sbb/sbb-jar-one-SbbConstraintsACIWrongMethod_1_1.xml";
	
	public void testSbbOne11ConstraintsACIOk() throws Exception {
		final SbbDescriptorImpl descriptor = SbbDescriptorImpl.parseDocument(
				super.parseDocument(_SBB_JAR_ONE_11_OK_CONSTRAINTS), null)[0];
		SbbComponent component = new SbbComponent();
		component.setAbstractSbbClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getSbbAbstractClass()
								.getSbbAbstractClassName()));
		component.setActivityContextInterface(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getSbbActivityContextInterface().getInterfaceName()));

		SbbComponentValidator validator = new SbbComponentValidator();
		component.setDescriptor(descriptor);
		validator.setComponent(component);

		boolean b = validator.validateSbbActivityContextInterface(
				ClassUtils.getAbstractMethodsFromClass(component
						.getAbstractSbbClass()), ClassUtils
						.getAbstractSuperClassesMethodsFromClass(component
								.getAbstractSbbClass()));

		assertTrue("Sbb class has not been validated", b);

	}

	public void testSbbOne11ConstraintsACIReturnBaseOk() throws Exception {
		final SbbDescriptorImpl descriptor = SbbDescriptorImpl.parseDocument(
				super.parseDocument(_SBB_JAR_ONE_11_OK_CONSTRAINTS_RETURN_BASE_TYPE), null)[0];
		SbbComponent component = new SbbComponent();
		component.setAbstractSbbClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getSbbAbstractClass()
								.getSbbAbstractClassName()));
		component.setActivityContextInterface(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getSbbActivityContextInterface().getInterfaceName()));

		SbbComponentValidator validator = new SbbComponentValidator();
		component.setDescriptor(descriptor);
		validator.setComponent(component);

		boolean b = validator.validateSbbActivityContextInterface(
				ClassUtils.getAbstractMethodsFromClass(component
						.getAbstractSbbClass()), ClassUtils
						.getAbstractSuperClassesMethodsFromClass(component
								.getAbstractSbbClass()));

		assertTrue("Sbb class has not been validated", b);

	}
	
	
	
	
	public void testSbbOne11ConstraintsACIWrongMethod() throws Exception {
		final SbbDescriptorImpl descriptor = SbbDescriptorImpl.parseDocument(
				super.parseDocument(_SBB_JAR_ONE_11_OK_CONSTRAINTS_WRONG_METHOD), null)[0];
		SbbComponent component = new SbbComponent();
		component.setAbstractSbbClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getSbbAbstractClass()
								.getSbbAbstractClassName()));
		component.setActivityContextInterface(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getSbbActivityContextInterface().getInterfaceName()));

		SbbComponentValidator validator = new SbbComponentValidator();
		component.setDescriptor(descriptor);
		validator.setComponent(component);

		boolean b = validator.validateSbbActivityContextInterface(
				ClassUtils.getAbstractMethodsFromClass(component
						.getAbstractSbbClass()), ClassUtils
						.getAbstractSuperClassesMethodsFromClass(component
								.getAbstractSbbClass()));

		assertFalse("Sbb class has been validated", b);

	}
	public void testSbbOne11ConstraintsACIWrongParameterType() throws Exception {
		final SbbDescriptorImpl descriptor = SbbDescriptorImpl.parseDocument(
				super.parseDocument(_SBB_JAR_ONE_11_OK_CONSTRAINTS_WRONG_PARAMETER_TYPE), null)[0];
		SbbComponent component = new SbbComponent();
		component.setAbstractSbbClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getSbbAbstractClass()
								.getSbbAbstractClassName()));
		component.setActivityContextInterface(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getSbbActivityContextInterface().getInterfaceName()));

		SbbComponentValidator validator = new SbbComponentValidator();
		component.setDescriptor(descriptor);
		validator.setComponent(component);

		boolean b = validator.validateSbbActivityContextInterface(
				ClassUtils.getAbstractMethodsFromClass(component
						.getAbstractSbbClass()), ClassUtils
						.getAbstractSuperClassesMethodsFromClass(component
								.getAbstractSbbClass()));

		assertFalse("Sbb class has been validated", b);

	}


}
