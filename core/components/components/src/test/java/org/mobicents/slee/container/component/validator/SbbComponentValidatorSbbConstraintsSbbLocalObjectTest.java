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

import org.mobicents.slee.container.component.SbbComponentImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.SbbDescriptorFactoryImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.SbbDescriptorImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.TCUtilityClass;

/**
 * Start time:17:07:31 2009-01-31<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class SbbComponentValidatorSbbConstraintsSbbLocalObjectTest extends TCUtilityClass {

	public static final String _SBB_JAR_ONE_11_OK_CONSTRAINTS_SBB_LOCAL_INTERFACE = "xml/validator/sbb/sbblo/sbb-jar-one-SbbConstraintsSbbLocalInterfaceOk_1_1.xml";
	public static final String _SBB_JAR_ONE_11_WRONG_THROWS_SBB_LOCAL_INTERFACE = "xml/validator/sbb/sbblo/sbb-jar-one-SbbConstraintsSbbLocalInterfaceWrongThrows_1_1.xml";
	public static final String _SBB_JAR_ONE_11_WRONG_RETURN_TYPE_SBB_LOCAL_INTERFACE = "xml/validator/sbb/sbblo/sbb-jar-one-SbbConstraintsSbbLocalInterfaceWrongReturnType_1_1.xml";
	public static final String _SBB_JAR_ONE_11_MISSING_METHOD_SBB_LOCAL_INTERFACE = "xml/validator/sbb/sbblo/sbb-jar-one-SbbConstraintsSbbLocalInterfaceMissingMethod_1_1.xml";

	public void testSbbOne11ConstraintsSbbLocalInterfaceOk() throws Exception {
	
		List<SbbDescriptorImpl> specs = new SbbDescriptorFactoryImpl().parse(super.getFileStream(_SBB_JAR_ONE_11_OK_CONSTRAINTS_SBB_LOCAL_INTERFACE));
		final SbbDescriptorImpl descriptor = specs.get(0);
		SbbComponentImpl component = new SbbComponentImpl(descriptor);
		component.setAbstractSbbClass(Thread.currentThread().getContextClassLoader().loadClass(
				descriptor.getSbbAbstractClass().getSbbAbstractClassName()));
		component.setSbbLocalInterfaceClass(Thread.currentThread().getContextClassLoader().loadClass(
				descriptor.getSbbLocalInterface().getSbbLocalInterfaceName()));

		SbbComponentValidator validator = new SbbComponentValidator();
		validator.setComponent(component);

		boolean b = validator.validateSbbLocalInterface(ClassUtils.getConcreteMethodsFromClass(component.getAbstractSbbClass()), ClassUtils
				.getConcreteMethodsFromSuperClasses(component.getAbstractSbbClass()));

		assertTrue("Sbb class has not been validated", b);

	}

	public void testSbbOne11ConstraintsSbbLocalInterfaceWrongReturnType() throws Exception {
	
		List<SbbDescriptorImpl> specs = new SbbDescriptorFactoryImpl().parse(super.getFileStream(_SBB_JAR_ONE_11_WRONG_RETURN_TYPE_SBB_LOCAL_INTERFACE));
		final SbbDescriptorImpl descriptor = specs.get(0);
		SbbComponentImpl component = new SbbComponentImpl(descriptor);
		component.setAbstractSbbClass(Thread.currentThread().getContextClassLoader().loadClass(
				descriptor.getSbbAbstractClass().getSbbAbstractClassName()));
		component.setSbbLocalInterfaceClass(Thread.currentThread().getContextClassLoader().loadClass(
				descriptor.getSbbLocalInterface().getSbbLocalInterfaceName()));

		SbbComponentValidator validator = new SbbComponentValidator();
		validator.setComponent(component);

		boolean b = validator.validateSbbLocalInterface(ClassUtils.getConcreteMethodsFromClass(component.getAbstractSbbClass()), ClassUtils
				.getConcreteMethodsFromSuperClasses(component.getAbstractSbbClass()));

		assertFalse("Sbb class has been validated - it should not - it declares SBB LO method with different return type", b);

	}

	public void testSbbOne11ConstraintsSbbLocalInterfaceWrongThrows() throws Exception {
	
		List<SbbDescriptorImpl> specs = new SbbDescriptorFactoryImpl().parse(super.getFileStream(_SBB_JAR_ONE_11_WRONG_THROWS_SBB_LOCAL_INTERFACE));
		final SbbDescriptorImpl descriptor = specs.get(0);
		SbbComponentImpl component = new SbbComponentImpl(descriptor);
		component.setAbstractSbbClass(Thread.currentThread().getContextClassLoader().loadClass(
				descriptor.getSbbAbstractClass().getSbbAbstractClassName()));
		component.setSbbLocalInterfaceClass(Thread.currentThread().getContextClassLoader().loadClass(
				descriptor.getSbbLocalInterface().getSbbLocalInterfaceName()));

		SbbComponentValidator validator = new SbbComponentValidator();
		validator.setComponent(component);

		boolean b = validator.validateSbbLocalInterface(ClassUtils.getConcreteMethodsFromClass(component.getAbstractSbbClass()), ClassUtils
				.getConcreteMethodsFromSuperClasses(component.getAbstractSbbClass()));

		assertFalse("Sbb class has been validated -it should not - it declares SBB LO method with different throws.", b);

	}

	public void testSbbOne11ConstraintsSbbLocalInterfaceMissingMethod() throws Exception {

		List<SbbDescriptorImpl> specs = new SbbDescriptorFactoryImpl().parse(super.getFileStream(_SBB_JAR_ONE_11_MISSING_METHOD_SBB_LOCAL_INTERFACE));
		final SbbDescriptorImpl descriptor = specs.get(0);
		SbbComponentImpl component = new SbbComponentImpl(descriptor);
		component.setAbstractSbbClass(Thread.currentThread().getContextClassLoader().loadClass(
				descriptor.getSbbAbstractClass().getSbbAbstractClassName()));
		component.setSbbLocalInterfaceClass(Thread.currentThread().getContextClassLoader().loadClass(
				descriptor.getSbbLocalInterface().getSbbLocalInterfaceName()));
		SbbComponentValidator validator = new SbbComponentValidator();
		validator.setComponent(component);

		boolean b = validator.validateSbbLocalInterface(ClassUtils.getConcreteMethodsFromClass(component.getAbstractSbbClass()), ClassUtils
				.getConcreteMethodsFromSuperClasses(component.getAbstractSbbClass()));

		assertFalse("Sbb class has been validated - it should not - it does not implement method from SBBLO", b);

	}

}
