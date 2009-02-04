/**
 * Start time:17:07:31 2009-01-31<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.validator;


import javax.slee.EventTypeID;
import javax.slee.SbbID;
import javax.slee.management.LibraryID;
import javax.slee.profile.ProfileSpecificationID;
import javax.slee.resource.ResourceAdaptorID;
import javax.slee.resource.ResourceAdaptorTypeID;

import javassist.CtClass;
import javassist.NotFoundException;

import org.mobicents.slee.container.component.ComponentRepository;
import org.mobicents.slee.container.component.EventTypeComponent;
import org.mobicents.slee.container.component.LibraryComponent;
import org.mobicents.slee.container.component.ProfileSpecificationComponent;
import org.mobicents.slee.container.component.ResourceAdaptorComponent;
import org.mobicents.slee.container.component.ResourceAdaptorTypeComponent;
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
public class SbbComponentValidatorSbbConstraintsCMPsTest extends
		ValidatorSuperTestCase {

	public static final String _SBB_JAR_ONE_11_OK_CMPS = "xml/validator/sbb/sbb-jar-one-SbbConstraintsSbbCMPsOk_1_1.xml";

	
	
	
	public void testSbbOne11ConstraintsSbbLocalInterfaceOk() throws Exception {
		final SbbDescriptorImpl descriptor = SbbDescriptorImpl.parseDocument(
				super.parseDocument(_SBB_JAR_ONE_11_OK_CMPS), null)[0];
		final SbbComponent component = new SbbComponent();
		component.setAbstractSbbClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getSbbAbstractClass()
								.getSbbAbstractClassName()));
		component.setSbbLocalInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getSbbLocalInterface().getSbbLocalInterfaceName()));
		component.setActivityContextInterface(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getSbbActivityContextInterface().getInterfaceName()));
		SbbComponentValidator validator = new SbbComponentValidator();
		component.setDescriptor(descriptor);
		validator.setComponent(component);
		validator.setComponentRepository(new ComponentRepository(){

			public SbbComponent getComponentByID(SbbID id) {
				//trick, we return ourselves :)
				return component;
			}

			public EventTypeComponent getComponentByID(EventTypeID id) {
				// TODO Auto-generated method stub
				return null;
			}

			public ProfileSpecificationComponent getComponentByID(
					ProfileSpecificationID id) {
				// TODO Auto-generated method stub
				return null;
			}

			public LibraryComponent getComponentByID(LibraryID id) {
				// TODO Auto-generated method stub
				return null;
			}

			public ResourceAdaptorComponent getComponentByID(
					ResourceAdaptorID id) {
				// TODO Auto-generated method stub
				return null;
			}

			public ResourceAdaptorTypeComponent getComponentByID(
					ResourceAdaptorTypeID id) {
				// TODO Auto-generated method stub
				return null;
			}});
		boolean b = validator.validateCmpFileds(
				ClassUtils.getConcreteMethodsFromClass(component
						.getAbstractSbbClass()), ClassUtils
						.getSuperClassesConcreteMethodsFromClass(component
								.getAbstractSbbClass()));

		assertTrue("Sbb class has not been validated", b);

	}

	
	

	
	
}
