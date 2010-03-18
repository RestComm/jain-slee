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

import org.mobicents.slee.container.component.SbbComponent;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.SbbDescriptorFactory;
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
public class SbbComponentValidatorSbbConstraintsAllTest extends TCUtilityClass {

	public static final String _SBB_JAR_ONE_11_OK_CONSTRAINTS = "xml/validator/sbb/test/sbb-jar-one-SbbTest_1_1.xml";

	public void testSbbOne11ConstraintsOk() throws Exception {

		List<SbbDescriptorImpl> specs = new SbbDescriptorFactory().parse(super.getFileStream(_SBB_JAR_ONE_11_OK_CONSTRAINTS));
		final SbbDescriptorImpl descriptor = specs.get(0);
		SbbComponent component = new SbbComponent(descriptor);
		component.setAbstractSbbClass(Thread.currentThread().getContextClassLoader().loadClass(
				descriptor.getSbbAbstractClass().getSbbAbstractClassName()));
		component.setSbbLocalInterfaceClass(Thread.currentThread().getContextClassLoader().loadClass(
				descriptor.getSbbLocalInterface().getSbbLocalInterfaceName()));
		component.setActivityContextInterface(Thread.currentThread().getContextClassLoader().loadClass(
				descriptor.getSbbActivityContextInterface().getInterfaceName()));
		component.setUsageParametersInterface(Thread.currentThread().getContextClassLoader().loadClass(
				descriptor.getSbbClasses().getSbbUsageParametersInterface().getUsageParametersInterfaceName()));
		SbbComponentValidator validator = new SbbComponentValidator();
		validator.setComponent(component);

		boolean b = validator.validate();

		assertTrue("Sbb class has not been validated", b);

	}

}
