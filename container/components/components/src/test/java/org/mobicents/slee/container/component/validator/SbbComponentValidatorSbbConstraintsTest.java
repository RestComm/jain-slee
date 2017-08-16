/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2017, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
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

import org.mobicents.slee.container.component.SbbComponentImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.SbbDescriptorFactoryImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.SbbDescriptorImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.TCUtilityClass;

/**
 * Start time:17:07:31 2009-01-31<br>
 * Project: restcomm-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class SbbComponentValidatorSbbConstraintsTest extends TCUtilityClass {

	public static final String _SBB_JAR_ONE_11_OK_CONSTRAINTS = "xml/validator/sbb/sbb-jar-one-SbbConstraintsOk_1_1.xml";
	public static final String _SBB_JAR_ONE_11_CONSTRAINTS_LIFECYCLE_ABSTRACT = "xml/validator/sbb/sbb-jar-one-SbbConstraintsLifeCycleAbstract_1_1.xml";
	public static final String _SBB_JAR_ONE_11_CONSTRAINTS_EJB_METHOD = "xml/validator/sbb/sbb-jar-one-SbbConstraintsEJBMethod_1_1.xml";
	public static final String _SBB_JAR_ONE_11_CONSTRAINTS_SBB_METHOD = "xml/validator/sbb/sbb-jar-one-SbbConstraintsSBBMethod_1_1.xml";

	public void testSbbOne11ConstraintsOk() throws Exception {

		List<SbbDescriptorImpl> specs = new SbbDescriptorFactoryImpl().parse(super.getFileStream(_SBB_JAR_ONE_11_OK_CONSTRAINTS));
		final SbbDescriptorImpl descriptor = specs.get(0);
		SbbComponentImpl component = new SbbComponentImpl(descriptor);
		component.setAbstractSbbClass(Thread.currentThread().getContextClassLoader().loadClass(
				descriptor.getSbbAbstractClass().getSbbAbstractClassName()));
		SbbComponentValidator validator = new SbbComponentValidator();
		validator.setComponent(component);

		boolean b = validator.validateAbstractClassConstraints(ClassUtils.getConcreteMethodsFromClass(component.getAbstractSbbClass()), ClassUtils
				.getConcreteMethodsFromSuperClasses(component.getAbstractSbbClass()));

		assertTrue("Sbb class has not been validated", b);

	}

	public void testSbbOne11UsageConstraintsOk() throws Exception {

		List<SbbDescriptorImpl> specs = new SbbDescriptorFactoryImpl().parse(super.getFileStream(_SBB_JAR_ONE_11_CONSTRAINTS_LIFECYCLE_ABSTRACT));
		final SbbDescriptorImpl descriptor = specs.get(0);
		SbbComponentImpl component = new SbbComponentImpl(descriptor);
		component.setAbstractSbbClass(Thread.currentThread().getContextClassLoader().loadClass(
				descriptor.getSbbAbstractClass().getSbbAbstractClassName()));
		SbbComponentValidator validator = new SbbComponentValidator();
		validator.setComponent(component);

		boolean b = validator.validateAbstractClassConstraints(ClassUtils.getConcreteMethodsFromClass(component.getAbstractSbbClass()), ClassUtils
				.getConcreteMethodsFromSuperClasses(component.getAbstractSbbClass()));

		assertFalse("Sbb class has been validated and it should not be", b);

	}

	public void testSbbOne11ConstraintsEjbMethod() throws Exception {

		List<SbbDescriptorImpl> specs = new SbbDescriptorFactoryImpl().parse(super.getFileStream(_SBB_JAR_ONE_11_CONSTRAINTS_EJB_METHOD));
		final SbbDescriptorImpl descriptor = specs.get(0);
		SbbComponentImpl component = new SbbComponentImpl(descriptor);
		component.setAbstractSbbClass(Thread.currentThread().getContextClassLoader().loadClass(
				descriptor.getSbbAbstractClass().getSbbAbstractClassName()));
		SbbComponentValidator validator = new SbbComponentValidator();
		validator.setComponent(component);

		boolean b = validator.validateAbstractClassConstraints(ClassUtils.getConcreteMethodsFromClass(component.getAbstractSbbClass()), ClassUtils
				.getConcreteMethodsFromSuperClasses(component.getAbstractSbbClass()));

		assertFalse("Sbb class has been validated and it shoudl not be, it has ejbX method", b);

	}

	public void testSbbOne11ConstraintsSbbMethod() throws Exception {

		List<SbbDescriptorImpl> specs = new SbbDescriptorFactoryImpl().parse(super.getFileStream(_SBB_JAR_ONE_11_CONSTRAINTS_SBB_METHOD));
		final SbbDescriptorImpl descriptor = specs.get(0);
		SbbComponentImpl component = new SbbComponentImpl(descriptor);
		component.setAbstractSbbClass(Thread.currentThread().getContextClassLoader().loadClass(
				descriptor.getSbbAbstractClass().getSbbAbstractClassName()));
		SbbComponentValidator validator = new SbbComponentValidator();
		validator.setComponent(component);

		boolean b = validator.validateAbstractClassConstraints(ClassUtils.getConcreteMethodsFromClass(component.getAbstractSbbClass()), ClassUtils
				.getConcreteMethodsFromSuperClasses(component.getAbstractSbbClass()));

		assertFalse("Sbb class has been validated and it shoudl not be, it has sbX method", b);

	}

}
