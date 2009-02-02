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
public class SbbComponentValidatorSbbConstraintsTest extends
		ValidatorSuperTestCase {

	public static final String _SBB_JAR_ONE_11_OK_CONSTRAINTS = "xml/validator/sbb/sbb-jar-one-SbbConstraintsOk_1_1.xml";
	public static final String _SBB_JAR_ONE_11_CONSTRAINTS_LIFECYCLE_ABSTRACT = "xml/validator/sbb/sbb-jar-one-SbbConstraintsLifeCycleAbstract_1_1.xml";
	public static final String _SBB_JAR_ONE_11_CONSTRAINTS_EJB_METHOD = "xml/validator/sbb/sbb-jar-one-SbbConstraintsEJBMethod_1_1.xml";
	public static final String _SBB_JAR_ONE_11_CONSTRAINTS_SBB_METHOD = "xml/validator/sbb/sbb-jar-one-SbbConstraintsSBBMethod_1_1.xml";

	public void testSbbOne11ConstraintsOk() throws Exception {
		final SbbDescriptorImpl descriptor = SbbDescriptorImpl.parseDocument(
				super.parseDocument(_SBB_JAR_ONE_11_OK_CONSTRAINTS), null)[0];
		SbbComponent component = new SbbComponent() {

			public CtClass getCtAbstractSbbClass() {

				try {
					return classPool.get(descriptor.getSbbAbstractClass()
							.getSbbAbstractClassName());
				} catch (NotFoundException e) {
					System.err.println(e);
					e.printStackTrace();
				}
				return null;
			}
		};

		SbbComponentValidator validator = new SbbComponentValidator();
		component.setDescriptor(descriptor);
		validator.setComponent(component);

		boolean b = validator.validateAbstractClassConstraints(
				ClassUtils.getConcreteMethodsFromClass(component
						.getCtAbstractSbbClass()), ClassUtils
						.getSuperClassesConcreteMethodsFromClass(component
								.getCtAbstractSbbClass()));

		assertTrue("Sbb class has not been validated", b);

	}

	public void testSbbOne11ConstraintsLifeCycleMethodAbstract()
			throws Exception {
		final SbbDescriptorImpl descriptor = SbbDescriptorImpl
				.parseDocument(
						super
								.parseDocument(_SBB_JAR_ONE_11_CONSTRAINTS_LIFECYCLE_ABSTRACT),
						null)[0];
		SbbComponent component = new SbbComponent() {

			public CtClass getCtAbstractSbbClass() {

				try {
					return classPool.get(descriptor.getSbbAbstractClass()
							.getSbbAbstractClassName());
				} catch (NotFoundException e) {
					System.err.println(e);
					e.printStackTrace();
				}
				return null;
			}
		};

		SbbComponentValidator validator = new SbbComponentValidator();
		component.setDescriptor(descriptor);
		validator.setComponent(component);

		boolean b = validator.validateAbstractClassConstraints(
				ClassUtils.getConcreteMethodsFromClass(component
						.getCtAbstractSbbClass()), ClassUtils
						.getSuperClassesConcreteMethodsFromClass(component
								.getCtAbstractSbbClass()));

		assertFalse("Sbb class has been validated and it shoudl not be", b);

	}

	public void testSbbOne11ConstraintsEjbMethod() throws Exception {
		final SbbDescriptorImpl descriptor = SbbDescriptorImpl.parseDocument(
				super.parseDocument(_SBB_JAR_ONE_11_CONSTRAINTS_EJB_METHOD),
				null)[0];
		SbbComponent component = new SbbComponent() {

			public CtClass getCtAbstractSbbClass() {

				try {
					return classPool.get(descriptor.getSbbAbstractClass()
							.getSbbAbstractClassName());
				} catch (NotFoundException e) {
					System.err.println(e);
					e.printStackTrace();
				}
				return null;
			}
		};

		SbbComponentValidator validator = new SbbComponentValidator();
		component.setDescriptor(descriptor);
		validator.setComponent(component);

		boolean b = validator.validateAbstractClassConstraints(
				ClassUtils.getConcreteMethodsFromClass(component
						.getCtAbstractSbbClass()), ClassUtils
						.getSuperClassesConcreteMethodsFromClass(component
								.getCtAbstractSbbClass()));

		assertFalse(
				"Sbb class has been validated and it shoudl not be, it has ejbX method",
				b);

	}
	public void testSbbOne11ConstraintsSbbMethod() throws Exception {
		final SbbDescriptorImpl descriptor = SbbDescriptorImpl.parseDocument(
				super.parseDocument(_SBB_JAR_ONE_11_CONSTRAINTS_SBB_METHOD),
				null)[0];
		SbbComponent component = new SbbComponent() {

			public CtClass getCtAbstractSbbClass() {

				try {
					return classPool.get(descriptor.getSbbAbstractClass()
							.getSbbAbstractClassName());
				} catch (NotFoundException e) {
					System.err.println(e);
					e.printStackTrace();
				}
				return null;
			}
		};

		SbbComponentValidator validator = new SbbComponentValidator();
		component.setDescriptor(descriptor);
		validator.setComponent(component);

		boolean b = validator.validateAbstractClassConstraints(
				ClassUtils.getConcreteMethodsFromClass(component
						.getCtAbstractSbbClass()), ClassUtils
						.getSuperClassesConcreteMethodsFromClass(component
								.getCtAbstractSbbClass()));

		assertFalse(
				"Sbb class has been validated and it shoudl not be, it has sbX method",
				b);

	}
	
}
