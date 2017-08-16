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
public class SbbComponentValidatorSbbConstraintsChildRelationsTest extends TCUtilityClass {

	public static final String _SBB_JAR_ONE_11_CONSTRAINTS_NO_CHILD_RELATION = "xml/validator/sbb/child/sbb-jar-one-SbbConstraintsOkNoChildRelation_1_1.xml";
	public static final String _SBB_JAR_ONE_11_CONSTRAINTS_2_CHILD_RELATION = "xml/validator/sbb/child/sbb-jar-one-SbbConstraintsOk2ChildRelation_1_1.xml";
	public static final String _SBB_JAR_ONE_11_CONSTRAINTS_2_CHILD_RELATION_THROW_EXCEPTION = "xml/validator/sbb/child/sbb-jar-one-SbbConstraintsOkChildRelationThrowException_1_1.xml";
	public static final String _SBB_JAR_ONE_11_CONSTRAINTS_2_CHILD_RELATION_WRONG_PREFIX = "xml/validator/sbb/child/sbb-jar-one-SbbConstraintsOk2ChildRelationWrongPrefix_1_1.xml";

	public void testSbbOne11Constraints2ChildRelation() throws Exception {

		List<SbbDescriptorImpl> specs = new SbbDescriptorFactoryImpl().parse(super.getFileStream(_SBB_JAR_ONE_11_CONSTRAINTS_2_CHILD_RELATION));
		final SbbDescriptorImpl descriptor = specs.get(0);
		SbbComponentImpl component = new SbbComponentImpl(descriptor);
		component.setAbstractSbbClass(Thread.currentThread().getContextClassLoader().loadClass(
				descriptor.getSbbAbstractClass().getSbbAbstractClassName()));

		SbbComponentValidator validator = new SbbComponentValidator();
		validator.setComponent(component);

		boolean b = validator.validateGetChildRelationMethods(ClassUtils.getAbstractMethodsFromClass(component.getAbstractSbbClass()), ClassUtils
				.getAbstractMethodsFromSuperClasses(component.getAbstractSbbClass()));

		assertTrue("Sbb class has not been validated, it should be", b);

	}

	public void testSbbOne11ConstraintsNoChildRelation() throws Exception {

		List<SbbDescriptorImpl> specs = new SbbDescriptorFactoryImpl().parse(super.getFileStream(_SBB_JAR_ONE_11_CONSTRAINTS_NO_CHILD_RELATION));
		final SbbDescriptorImpl descriptor = specs.get(0);
		SbbComponentImpl component = new SbbComponentImpl(descriptor);
		component.setAbstractSbbClass(Thread.currentThread().getContextClassLoader().loadClass(
				descriptor.getSbbAbstractClass().getSbbAbstractClassName()));

		SbbComponentValidator validator = new SbbComponentValidator();
		validator.setComponent(component);

		boolean b = validator.validateGetChildRelationMethods(ClassUtils.getAbstractMethodsFromClass(component.getAbstractSbbClass()), ClassUtils
				.getAbstractMethodsFromSuperClasses(component.getAbstractSbbClass()));

		assertFalse("Sbb class has been validated, it should not be, it does not have child relateion method defined in descriptor", b);

	}

	public void testSbbOne11Constraints2ChildRelationWrongPrefix() throws Exception {

		List<SbbDescriptorImpl> specs = new SbbDescriptorFactoryImpl().parse(super.getFileStream(_SBB_JAR_ONE_11_CONSTRAINTS_2_CHILD_RELATION_WRONG_PREFIX));
		final SbbDescriptorImpl descriptor = specs.get(0);
		SbbComponentImpl component = new SbbComponentImpl(descriptor);
		component.setAbstractSbbClass(Thread.currentThread().getContextClassLoader().loadClass(
				descriptor.getSbbAbstractClass().getSbbAbstractClassName()));

		SbbComponentValidator validator = new SbbComponentValidator();
		validator.setComponent(component);

		boolean b = validator.validateGetChildRelationMethods(ClassUtils.getAbstractMethodsFromClass(component.getAbstractSbbClass()), ClassUtils
				.getAbstractMethodsFromSuperClasses(component.getAbstractSbbClass()));

		assertFalse("Sbb class has been validated, it should not be, it does declare child relation method with wrong prefix", b);

	}

	public void testSbbOne11Constraints2ChildRelationThrowsException() throws Exception {

		List<SbbDescriptorImpl> specs = new SbbDescriptorFactoryImpl().parse(super.getFileStream(_SBB_JAR_ONE_11_CONSTRAINTS_2_CHILD_RELATION_THROW_EXCEPTION));
		final SbbDescriptorImpl descriptor = specs.get(0);
		SbbComponentImpl component = new SbbComponentImpl(descriptor);
		component.setAbstractSbbClass(Thread.currentThread().getContextClassLoader().loadClass(
				descriptor.getSbbAbstractClass().getSbbAbstractClassName()));

		SbbComponentValidator validator = new SbbComponentValidator();
		validator.setComponent(component);

		boolean b = validator.validateGetChildRelationMethods(ClassUtils.getAbstractMethodsFromClass(component.getAbstractSbbClass()), ClassUtils
				.getAbstractMethodsFromSuperClasses(component.getAbstractSbbClass()));

		assertFalse("Sbb class has been validated, it should not be, it does declare child relation method with throws clause", b);

	}

}
