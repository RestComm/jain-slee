/**
 * Start time:17:07:31 2009-01-31<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.validator;

import javax.slee.Sbb;
import javax.slee.usage.SampleStatistics;

import javassist.CtClass;
import javassist.NotFoundException;

import org.mobicents.slee.container.component.SbbComponent;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.SbbDescriptorImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.TCUtilityClass;
import org.mobicents.slee.container.component.validator.ClassUtils;
import org.mobicents.slee.container.component.validator.SbbComponentValidator;
import org.mobicents.slee.container.component.validator.sbb.abstracts.usage.UsageInterfaceToFewMethods;
import org.mobicents.slee.container.component.validator.sbb.abstracts.usage.UsageInterfaceToManyMethods;
import org.mobicents.slee.container.component.validator.sbb.abstracts.usage.UsageOkInterface;
import org.mobicents.slee.container.component.validator.sbb.abstracts.usage.UsageSetterWrongLevelInterface;
import org.mobicents.slee.container.component.validator.sbb.abstracts.usage.UsageThrowsOnGetterInterface;
import org.mobicents.slee.container.component.validator.sbb.abstracts.usage.UsageThrowsOnSetterInterface;
import org.mobicents.slee.container.component.validator.sbb.abstracts.usage.UsageWrongAccesorLevelInterface;
import org.mobicents.slee.container.component.validator.sbb.abstracts.usage.UsageWrongSampleReturnTypeInterface;
import org.mobicents.slee.container.component.validator.sbb.abstracts.usage.UsageWrongSetterPrameterInterface;

/**
 * Start time:17:07:31 2009-01-31<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class SbbComponentValidatorSbbUsageConstraintsTest extends
		TCUtilityClass {

	public static final String _SBB_JAR_ONE_11_USAGE_CONSTRAINTS_OK = "xml/validator/sbb/usage/sbb-jar-one-SbbConstraintsUsageOk_1_1.xml";

	public void testSbbOne11UsageConstraintsOk() throws Exception {
		final SbbDescriptorImpl descriptor = SbbDescriptorImpl
				.parseDocument(super
						.parseDocument(_SBB_JAR_ONE_11_USAGE_CONSTRAINTS_OK),
						null)[0];
		SbbComponent component = new SbbComponent();
		component.setAbstractSbbClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getSbbAbstractClass()
								.getSbbAbstractClassName()));
		component.setUsageParametersInterface(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getSbbUsageParametersInterface()
								.getUsageParametersInterfaceName()));
		SbbComponentValidator validator = new SbbComponentValidator();
		component.setDescriptor(descriptor);
		validator.setComponent(component);

		boolean b = validator.validateSbbUsageParameterInterface(ClassUtils
				.getAbstractMethodsFromClass(component.getAbstractSbbClass()),
				ClassUtils.getAbstractSuperClassesMethodsFromClass(component
						.getAbstractSbbClass()));

		assertTrue("Sbb class has not been validated", b);

	}


	
	public void testSbbOne11ToManyUsageMethodDefined() throws Exception {
		final SbbDescriptorImpl descriptor = SbbDescriptorImpl
				.parseDocument(super
						.parseDocument(_SBB_JAR_ONE_11_USAGE_CONSTRAINTS_OK),
						null)[0];
		SbbComponent component = new SbbComponent();
		component.setAbstractSbbClass(SbbUsageInterfaceToManyMethods.class);
		component
				.setUsageParametersInterface(UsageInterfaceToManyMethods.class);
		SbbComponentValidator validator = new SbbComponentValidator();
		component.setDescriptor(descriptor);
		validator.setComponent(component);

		boolean b = validator.validateSbbUsageParameterInterface(ClassUtils
				.getAbstractMethodsFromClass(component.getAbstractSbbClass()),
				ClassUtils.getAbstractSuperClassesMethodsFromClass(component
						.getAbstractSbbClass()));

		assertFalse("Sbb class has been validated", b);

	}
	
	public void testSbbOne11WrongSetterPrameter() throws Exception {
		final SbbDescriptorImpl descriptor = SbbDescriptorImpl
				.parseDocument(super
						.parseDocument(_SBB_JAR_ONE_11_USAGE_CONSTRAINTS_OK),
						null)[0];
		SbbComponent component = new SbbComponent();
		component.setAbstractSbbClass(SbbUsageWrongSetterPrameterInterface.class);
		component.setUsageParametersInterface(UsageWrongSetterPrameterInterface.class);
		SbbComponentValidator validator = new SbbComponentValidator();
		component.setDescriptor(descriptor);
		validator.setComponent(component);

		boolean b = validator.validateSbbUsageParameterInterface(ClassUtils
				.getAbstractMethodsFromClass(component.getAbstractSbbClass()),
				ClassUtils.getAbstractSuperClassesMethodsFromClass(component
						.getAbstractSbbClass()));

		assertFalse("Sbb class has been validated", b);

	}
	public void testSbbOne11ToFewUsageMethodDefined() throws Exception {
		final SbbDescriptorImpl descriptor = SbbDescriptorImpl
				.parseDocument(super
						.parseDocument(_SBB_JAR_ONE_11_USAGE_CONSTRAINTS_OK),
						null)[0];
		SbbComponent component = new SbbComponent();
		component.setAbstractSbbClass(SbbUsageInterfaceToFewMethods.class);
		component.setUsageParametersInterface(UsageInterfaceToFewMethods.class);
		SbbComponentValidator validator = new SbbComponentValidator();
		component.setDescriptor(descriptor);
		validator.setComponent(component);

		boolean b = validator.validateSbbUsageParameterInterface(ClassUtils
				.getAbstractMethodsFromClass(component.getAbstractSbbClass()),
				ClassUtils.getAbstractSuperClassesMethodsFromClass(component
						.getAbstractSbbClass()));

		assertFalse("Sbb class has been validated", b);

	}
	
	public void testSbbOne11ThrowsOnGetter() throws Exception {
		//setter is increment or sample method
		final SbbDescriptorImpl descriptor = SbbDescriptorImpl
				.parseDocument(super
						.parseDocument(_SBB_JAR_ONE_11_USAGE_CONSTRAINTS_OK),
						null)[0];
		SbbComponent component = new SbbComponent();
		component.setAbstractSbbClass(SbbUsageThrowsOnGetterInterface.class);
		component.setUsageParametersInterface(UsageThrowsOnGetterInterface.class);
		SbbComponentValidator validator = new SbbComponentValidator();
		component.setDescriptor(descriptor);
		validator.setComponent(component);

		boolean b = validator.validateSbbUsageParameterInterface(ClassUtils
				.getAbstractMethodsFromClass(component.getAbstractSbbClass()),
				ClassUtils.getAbstractSuperClassesMethodsFromClass(component
						.getAbstractSbbClass()));

		assertFalse("Sbb class has been validated", b);

	}
	
	public void testSbbOne11ThrowsOnSetter() throws Exception {
		//setter is increment or sample method
		final SbbDescriptorImpl descriptor = SbbDescriptorImpl
				.parseDocument(super
						.parseDocument(_SBB_JAR_ONE_11_USAGE_CONSTRAINTS_OK),
						null)[0];
		SbbComponent component = new SbbComponent();
		component.setAbstractSbbClass(SbbUsageThrowsOnSetterInterface.class);
		component.setUsageParametersInterface(UsageThrowsOnSetterInterface.class);
		SbbComponentValidator validator = new SbbComponentValidator();
		component.setDescriptor(descriptor);
		validator.setComponent(component);

		boolean b = validator.validateSbbUsageParameterInterface(ClassUtils
				.getAbstractMethodsFromClass(component.getAbstractSbbClass()),
				ClassUtils.getAbstractSuperClassesMethodsFromClass(component
						.getAbstractSbbClass()));

		assertFalse("Sbb class has been validated", b);

	}public void testSbbOne11WrongSampleReturnType() throws Exception {
		//setter is increment or sample method
		final SbbDescriptorImpl descriptor = SbbDescriptorImpl
				.parseDocument(super
						.parseDocument(_SBB_JAR_ONE_11_USAGE_CONSTRAINTS_OK),
						null)[0];
		SbbComponent component = new SbbComponent();
		component.setAbstractSbbClass(SbbUsageWrongSampleReturnTypeInterface.class);
		component.setUsageParametersInterface(UsageWrongSampleReturnTypeInterface.class);
		SbbComponentValidator validator = new SbbComponentValidator();
		component.setDescriptor(descriptor);
		validator.setComponent(component);

		boolean b = validator.validateSbbUsageParameterInterface(ClassUtils
				.getAbstractMethodsFromClass(component.getAbstractSbbClass()),
				ClassUtils.getAbstractSuperClassesMethodsFromClass(component
						.getAbstractSbbClass()));

		assertFalse("Sbb class has been validated", b);

	}
	public void testSbbOne11UsageSetterWrongLevelInterface() throws Exception {
		//setter is increment or sample method
		final SbbDescriptorImpl descriptor = SbbDescriptorImpl
				.parseDocument(super
						.parseDocument(_SBB_JAR_ONE_11_USAGE_CONSTRAINTS_OK),
						null)[0];
		SbbComponent component = new SbbComponent();
		component.setAbstractSbbClass(SbbUsageSetterWrongLevelInterface.class);
		component.setUsageParametersInterface(UsageSetterWrongLevelInterface.class);
		SbbComponentValidator validator = new SbbComponentValidator();
		component.setDescriptor(descriptor);
		validator.setComponent(component);

		boolean b = validator.validateSbbUsageParameterInterface(ClassUtils
				.getAbstractMethodsFromClass(component.getAbstractSbbClass()),
				ClassUtils.getAbstractSuperClassesMethodsFromClass(component
						.getAbstractSbbClass()));

		assertFalse("Sbb class has been validated", b);

	}
	public void testSbbOne11UsageWrongAccesorLevelInterface() throws Exception {
		//setter is increment or sample method
		final SbbDescriptorImpl descriptor = SbbDescriptorImpl
				.parseDocument(super
						.parseDocument(_SBB_JAR_ONE_11_USAGE_CONSTRAINTS_OK),
						null)[0];
		SbbComponent component = new SbbComponent();
		component.setAbstractSbbClass(SbbUsageWrongAccesorLevelInterface.class);
		component.setUsageParametersInterface(UsageWrongAccesorLevelInterface.class);
		SbbComponentValidator validator = new SbbComponentValidator();
		component.setDescriptor(descriptor);
		validator.setComponent(component);

		boolean b = validator.validateSbbUsageParameterInterface(ClassUtils
				.getAbstractMethodsFromClass(component.getAbstractSbbClass()),
				ClassUtils.getAbstractSuperClassesMethodsFromClass(component
						.getAbstractSbbClass()));

		assertFalse("Sbb class has been validated", b);

	}
	
}


abstract class SbbUsageInterfaceToManyMethods implements Sbb
{
	public abstract UsageInterfaceToManyMethods getDefaultSbbUsageParameterSet();
}

abstract class SbbUsageWrongSetterPrameterInterface implements Sbb
{
	public abstract UsageWrongSetterPrameterInterface getDefaultSbbUsageParameterSet();
}
abstract class SbbUsageThrowsOnGetterInterface implements Sbb
{
	public abstract UsageThrowsOnGetterInterface getDefaultSbbUsageParameterSet();
}
abstract class SbbUsageThrowsOnSetterInterface implements Sbb
{
	public abstract UsageThrowsOnSetterInterface getDefaultSbbUsageParameterSet();
}
abstract class SbbUsageInterfaceToFewMethods implements Sbb
{
	public abstract UsageInterfaceToFewMethods getDefaultSbbUsageParameterSet();
}
abstract class SbbUsageWrongSampleReturnTypeInterface implements Sbb
{
	public abstract UsageWrongSampleReturnTypeInterface getDefaultSbbUsageParameterSet();
}
abstract class SbbUsageSetterWrongLevelInterface implements Sbb
{
	public abstract UsageSetterWrongLevelInterface getDefaultSbbUsageParameterSet();
}
abstract class SbbUsageWrongAccesorLevelInterface implements Sbb
{
	public abstract UsageWrongAccesorLevelInterface getDefaultSbbUsageParameterSet();
}

