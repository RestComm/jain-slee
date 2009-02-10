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
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.TCUtilityClass;
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
public class SbbComponentValidatorSbbConstraintsCMPsTest extends TCUtilityClass {

	public static final String _SBB_JAR_ONE_11_OK_CMPS = "xml/validator/sbb/cmp/sbb-jar-one-SbbConstraintsSbbCMPsOk_1_1.xml";
	public static final String _SBB_JAR_ONE_11_CMPS_NO_REF_ON_CUSTOM_SBBLO = "xml/validator/sbb/cmp/sbb-jar-one-SbbConstraintsSbbCMPsNoRefOnCustomSbbLO_1_1.xml";
	public static final String _SBB_JAR_ONE_11_CMPS_REF_ON_NOT_SBBLO = "xml/validator/sbb/cmp/sbb-jar-one-SbbConstraintsSbbCMPsRefOnNotSbbLO_1_1.xml";
	public static final String _SBB_JAR_ONE_11_CMPS_DIFFERENT_TYPES = "xml/validator/sbb/cmp/sbb-jar-one-SbbConstraintsSbbCMPsDifferentTypes_1_1.xml";
	public static final String _SBB_JAR_ONE_11_CMPS_THROWS = "xml/validator/sbb/cmp/sbb-jar-one-SbbConstraintsSbbCMPsThrows_1_1.xml";
	public static final String _SBB_JAR_ONE_11_CMPS_CONCRETE = "xml/validator/sbb/cmp/sbb-jar-one-SbbConstraintsSbbCMPsConcrete_1_1.xml";
	public static final String _SBB_JAR_ONE_11_CMPS_SCOPE = "xml/validator/sbb/cmp/sbb-jar-one-SbbConstraintsSbbCMPsScope_1_1.xml";

	public void testSbbOne11ConstraintsCMPsOk() throws Exception {
		final SbbDescriptorImpl descriptor = SbbDescriptorImpl.parseDocument(
				super.parseDocument(_SBB_JAR_ONE_11_OK_CMPS), null)[0];
		final SbbComponent component = new SbbComponent();
		component.setAbstractSbbClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getSbbAbstractClass()
								.getSbbAbstractClassName()));
		component.setSbbLocalInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getSbbLocalInterface()
								.getSbbLocalInterfaceName()));
		component.setActivityContextInterface(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getSbbActivityContextInterface()
								.getInterfaceName()));
		SbbComponentValidator validator = new SbbComponentValidator();
		component.setDescriptor(descriptor);
		validator.setComponent(component);
		validator.setComponentRepository(new ComponentRepository() {

			public SbbComponent getComponentByID(SbbID id) {
				// trick, we return ourselves :)
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
			}
		});
		boolean b = validator.validateCmpFileds(ClassUtils
				.getAbstractMethodsFromClass(component.getAbstractSbbClass()),
				ClassUtils.getAbstractMethodsFromSuperClasses(component
						.getAbstractSbbClass()));

		assertTrue("Sbb class has not been validated", b);

	}

	public void testSbbOne11ConstraintsCMPsRefNotSbbLO() throws Exception {
		final SbbDescriptorImpl descriptor = SbbDescriptorImpl.parseDocument(
				super.parseDocument(_SBB_JAR_ONE_11_CMPS_REF_ON_NOT_SBBLO),
				null)[0];
		final SbbComponent component = new SbbComponent();
		component.setAbstractSbbClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getSbbAbstractClass()
								.getSbbAbstractClassName()));
		component.setSbbLocalInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getSbbLocalInterface()
								.getSbbLocalInterfaceName()));
		component.setActivityContextInterface(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getSbbActivityContextInterface()
								.getInterfaceName()));
		SbbComponentValidator validator = new SbbComponentValidator();
		component.setDescriptor(descriptor);
		validator.setComponent(component);
		validator.setComponentRepository(new ComponentRepository() {

			public SbbComponent getComponentByID(SbbID id) {
				// trick, we return ourselves :)
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
			}
		});
		boolean b = validator.validateCmpFileds(ClassUtils
				.getAbstractMethodsFromClass(component.getAbstractSbbClass()),
				ClassUtils.getAbstractMethodsFromSuperClasses(component
						.getAbstractSbbClass()));

		assertFalse("Sbb class has been validated", b);

	}

	public void testSbbOne11ConstraintsCMPsDifferentTypes() throws Exception {
		final SbbDescriptorImpl descriptor = SbbDescriptorImpl
				.parseDocument(super
						.parseDocument(_SBB_JAR_ONE_11_CMPS_DIFFERENT_TYPES),
						null)[0];
		final SbbComponent component = new SbbComponent();
		component.setAbstractSbbClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getSbbAbstractClass()
								.getSbbAbstractClassName()));
		component.setSbbLocalInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getSbbLocalInterface()
								.getSbbLocalInterfaceName()));
		component.setActivityContextInterface(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getSbbActivityContextInterface()
								.getInterfaceName()));
		SbbComponentValidator validator = new SbbComponentValidator();
		component.setDescriptor(descriptor);
		validator.setComponent(component);
		validator.setComponentRepository(new ComponentRepository() {

			public SbbComponent getComponentByID(SbbID id) {
				// trick, we return ourselves :)
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
			}
		});
		boolean b = validator.validateCmpFileds(ClassUtils
				.getAbstractMethodsFromClass(component.getAbstractSbbClass()),
				ClassUtils.getAbstractMethodsFromSuperClasses(component
						.getAbstractSbbClass()));

		assertFalse("Sbb class has been validated", b);

	}

	public void testSbbOne11ConstraintsCMPsThrows() throws Exception {
		final SbbDescriptorImpl descriptor = SbbDescriptorImpl.parseDocument(
				super.parseDocument(_SBB_JAR_ONE_11_CMPS_THROWS), null)[0];
		final SbbComponent component = new SbbComponent();
		component.setAbstractSbbClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getSbbAbstractClass()
								.getSbbAbstractClassName()));
		component.setSbbLocalInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getSbbLocalInterface()
								.getSbbLocalInterfaceName()));
		component.setActivityContextInterface(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getSbbActivityContextInterface()
								.getInterfaceName()));
		SbbComponentValidator validator = new SbbComponentValidator();
		component.setDescriptor(descriptor);
		validator.setComponent(component);
		validator.setComponentRepository(new ComponentRepository() {

			public SbbComponent getComponentByID(SbbID id) {
				// trick, we return ourselves :)
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
			}
		});
		boolean b = validator.validateCmpFileds(ClassUtils
				.getAbstractMethodsFromClass(component.getAbstractSbbClass()),
				ClassUtils.getAbstractMethodsFromSuperClasses(component
						.getAbstractSbbClass()));

		assertFalse("Sbb class has been validated", b);

	}

	public void testSbbOne11ConstraintsCMPsConcrete() throws Exception {
		final SbbDescriptorImpl descriptor = SbbDescriptorImpl.parseDocument(
				super.parseDocument(_SBB_JAR_ONE_11_CMPS_CONCRETE), null)[0];
		final SbbComponent component = new SbbComponent();
		component.setAbstractSbbClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getSbbAbstractClass()
								.getSbbAbstractClassName()));
		component.setSbbLocalInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getSbbLocalInterface()
								.getSbbLocalInterfaceName()));
		component.setActivityContextInterface(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getSbbActivityContextInterface()
								.getInterfaceName()));
		SbbComponentValidator validator = new SbbComponentValidator();
		component.setDescriptor(descriptor);
		validator.setComponent(component);
		validator.setComponentRepository(new ComponentRepository() {

			public SbbComponent getComponentByID(SbbID id) {
				// trick, we return ourselves :)
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
			}
		});
		boolean b = validator.validateCmpFileds(ClassUtils
				.getAbstractMethodsFromClass(component.getAbstractSbbClass()),
				ClassUtils.getAbstractMethodsFromSuperClasses(component
						.getAbstractSbbClass()));

		assertFalse("Sbb class has been validated", b);

	}

	public void testSbbOne11ConstraintsCMPsScope() throws Exception {
		final SbbDescriptorImpl descriptor = SbbDescriptorImpl.parseDocument(
				super.parseDocument(_SBB_JAR_ONE_11_CMPS_SCOPE), null)[0];
		final SbbComponent component = new SbbComponent();
		component.setAbstractSbbClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getSbbAbstractClass()
								.getSbbAbstractClassName()));
		component.setSbbLocalInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getSbbLocalInterface()
								.getSbbLocalInterfaceName()));
		component.setActivityContextInterface(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getSbbActivityContextInterface()
								.getInterfaceName()));
		SbbComponentValidator validator = new SbbComponentValidator();
		component.setDescriptor(descriptor);
		validator.setComponent(component);
		validator.setComponentRepository(new ComponentRepository() {

			public SbbComponent getComponentByID(SbbID id) {
				// trick, we return ourselves :)
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
			}
		});
		boolean b = validator.validateCmpFileds(ClassUtils
				.getAbstractMethodsFromClass(component.getAbstractSbbClass()),
				ClassUtils.getAbstractMethodsFromSuperClasses(component
						.getAbstractSbbClass()));

		assertFalse("Sbb class has been validated", b);

	}

	public void testSbbOne11ConstraintsCMPsNoRefCustomSbbLO() throws Exception {
		final SbbDescriptorImpl descriptor = SbbDescriptorImpl
				.parseDocument(
						super
								.parseDocument(_SBB_JAR_ONE_11_CMPS_NO_REF_ON_CUSTOM_SBBLO),
						null)[0];
		final SbbComponent component = new SbbComponent();
		component.setAbstractSbbClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getSbbAbstractClass()
								.getSbbAbstractClassName()));
		component.setSbbLocalInterfaceClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getSbbLocalInterface()
								.getSbbLocalInterfaceName()));
		component.setActivityContextInterface(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getSbbActivityContextInterface()
								.getInterfaceName()));
		SbbComponentValidator validator = new SbbComponentValidator();
		component.setDescriptor(descriptor);
		validator.setComponent(component);
		validator.setComponentRepository(new ComponentRepository() {

			public SbbComponent getComponentByID(SbbID id) {
				// trick, we return ourselves :)
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
			}
		});
		boolean b = validator.validateCmpFileds(ClassUtils
				.getAbstractMethodsFromClass(component.getAbstractSbbClass()),
				ClassUtils.getAbstractMethodsFromSuperClasses(component
						.getAbstractSbbClass()));

		assertFalse("Sbb class has been validated", b);

	}

}
