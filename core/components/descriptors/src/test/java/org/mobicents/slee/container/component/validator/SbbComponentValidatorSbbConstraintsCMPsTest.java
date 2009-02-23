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

import javax.slee.ComponentID;
import javax.slee.EventTypeID;
import javax.slee.SbbID;
import javax.slee.ServiceID;
import javax.slee.management.LibraryID;
import javax.slee.profile.ProfileSpecificationID;
import javax.slee.resource.ResourceAdaptorID;
import javax.slee.resource.ResourceAdaptorTypeID;

import org.mobicents.slee.container.component.ComponentRepository;
import org.mobicents.slee.container.component.EventTypeComponent;
import org.mobicents.slee.container.component.LibraryComponent;
import org.mobicents.slee.container.component.ProfileSpecificationComponent;
import org.mobicents.slee.container.component.ResourceAdaptorComponent;
import org.mobicents.slee.container.component.ResourceAdaptorTypeComponent;
import org.mobicents.slee.container.component.SbbComponent;
import org.mobicents.slee.container.component.ServiceComponent;
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
public class SbbComponentValidatorSbbConstraintsCMPsTest extends TCUtilityClass {

	public static final String _SBB_JAR_ONE_11_OK_CMPS = "xml/validator/sbb/cmp/sbb-jar-one-SbbConstraintsSbbCMPsOk_1_1.xml";
	public static final String _SBB_JAR_ONE_11_CMPS_NO_REF_ON_CUSTOM_SBBLO = "xml/validator/sbb/cmp/sbb-jar-one-SbbConstraintsSbbCMPsNoRefOnCustomSbbLO_1_1.xml";
	public static final String _SBB_JAR_ONE_11_CMPS_REF_ON_NOT_SBBLO = "xml/validator/sbb/cmp/sbb-jar-one-SbbConstraintsSbbCMPsRefOnNotSbbLO_1_1.xml";
	public static final String _SBB_JAR_ONE_11_CMPS_DIFFERENT_TYPES = "xml/validator/sbb/cmp/sbb-jar-one-SbbConstraintsSbbCMPsDifferentTypes_1_1.xml";
	public static final String _SBB_JAR_ONE_11_CMPS_THROWS = "xml/validator/sbb/cmp/sbb-jar-one-SbbConstraintsSbbCMPsThrows_1_1.xml";
	public static final String _SBB_JAR_ONE_11_CMPS_CONCRETE = "xml/validator/sbb/cmp/sbb-jar-one-SbbConstraintsSbbCMPsConcrete_1_1.xml";
	public static final String _SBB_JAR_ONE_11_CMPS_SCOPE = "xml/validator/sbb/cmp/sbb-jar-one-SbbConstraintsSbbCMPsScope_1_1.xml";

	public void testSbbOne11ConstraintsCMPsOk() throws Exception {

		List<SbbDescriptorImpl> specs = new SbbDescriptorFactory().parse(super.getFileStream(_SBB_JAR_ONE_11_OK_CMPS));
		final SbbDescriptorImpl descriptor = specs.get(0);
		final SbbComponent component = new SbbComponent(descriptor);
		component.setAbstractSbbClass(Thread.currentThread().getContextClassLoader().loadClass(
				descriptor.getSbbAbstractClass().getSbbAbstractClassName()));
		component.setSbbLocalInterfaceClass(Thread.currentThread().getContextClassLoader().loadClass(
				descriptor.getSbbLocalInterface().getSbbLocalInterfaceName()));
		component.setActivityContextInterface(Thread.currentThread().getContextClassLoader().loadClass(
				descriptor.getSbbActivityContextInterface().getInterfaceName()));
		SbbComponentValidator validator = new SbbComponentValidator();
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

			public ProfileSpecificationComponent getComponentByID(ProfileSpecificationID id) {
				// TODO Auto-generated method stub
				return null;
			}

			public LibraryComponent getComponentByID(LibraryID id) {
				// TODO Auto-generated method stub
				return null;
			}

			public ResourceAdaptorComponent getComponentByID(ResourceAdaptorID id) {
				// TODO Auto-generated method stub
				return null;
			}

			public ResourceAdaptorTypeComponent getComponentByID(ResourceAdaptorTypeID id) {
				// TODO Auto-generated method stub
				return null;
			}

			public ServiceComponent getComponentByID(ServiceID id) {
				// TODO Auto-generated method stub
				return null;
			}

			public boolean isInstalled(ComponentID componentID) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		boolean b = validator.validateCmpFileds(ClassUtils.getAbstractMethodsFromClass(component.getAbstractSbbClass()), ClassUtils
				.getAbstractMethodsFromSuperClasses(component.getAbstractSbbClass()));

		assertTrue("Sbb class has not been validated", b);

	}

	public void testSbbOne11ConstraintsCMPsRefNotSbbLO() throws Exception {
		
		List<SbbDescriptorImpl> specs = new SbbDescriptorFactory().parse(super.getFileStream(_SBB_JAR_ONE_11_CMPS_REF_ON_NOT_SBBLO));
		final SbbDescriptorImpl descriptor = specs.get(0);
		final SbbComponent component = new SbbComponent(descriptor);
		component.setAbstractSbbClass(Thread.currentThread().getContextClassLoader().loadClass(
				descriptor.getSbbAbstractClass().getSbbAbstractClassName()));
		component.setSbbLocalInterfaceClass(Thread.currentThread().getContextClassLoader().loadClass(
				descriptor.getSbbLocalInterface().getSbbLocalInterfaceName()));
		component.setActivityContextInterface(Thread.currentThread().getContextClassLoader().loadClass(
				descriptor.getSbbActivityContextInterface().getInterfaceName()));
		SbbComponentValidator validator = new SbbComponentValidator();
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

			public ProfileSpecificationComponent getComponentByID(ProfileSpecificationID id) {
				// TODO Auto-generated method stub
				return null;
			}

			public LibraryComponent getComponentByID(LibraryID id) {
				// TODO Auto-generated method stub
				return null;
			}

			public ResourceAdaptorComponent getComponentByID(ResourceAdaptorID id) {
				// TODO Auto-generated method stub
				return null;
			}

			public ResourceAdaptorTypeComponent getComponentByID(ResourceAdaptorTypeID id) {
				// TODO Auto-generated method stub
				return null;
			}

			public ServiceComponent getComponentByID(ServiceID id) {
				// TODO Auto-generated method stub
				return null;
			}

			public boolean isInstalled(ComponentID componentID) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		boolean b = validator.validateCmpFileds(ClassUtils.getAbstractMethodsFromClass(component.getAbstractSbbClass()), ClassUtils
				.getAbstractMethodsFromSuperClasses(component.getAbstractSbbClass()));

		assertFalse("Sbb class has been validated", b);

	}

	public void testSbbOne11ConstraintsCMPsDifferentTypes() throws Exception {
	
		List<SbbDescriptorImpl> specs = new SbbDescriptorFactory().parse(super.getFileStream(_SBB_JAR_ONE_11_CMPS_DIFFERENT_TYPES));
		final SbbDescriptorImpl descriptor = specs.get(0);
		final SbbComponent component = new SbbComponent(descriptor);
		component.setAbstractSbbClass(Thread.currentThread().getContextClassLoader().loadClass(
				descriptor.getSbbAbstractClass().getSbbAbstractClassName()));
		component.setSbbLocalInterfaceClass(Thread.currentThread().getContextClassLoader().loadClass(
				descriptor.getSbbLocalInterface().getSbbLocalInterfaceName()));
		component.setActivityContextInterface(Thread.currentThread().getContextClassLoader().loadClass(
				descriptor.getSbbActivityContextInterface().getInterfaceName()));
		SbbComponentValidator validator = new SbbComponentValidator();
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

			public ProfileSpecificationComponent getComponentByID(ProfileSpecificationID id) {
				// TODO Auto-generated method stub
				return null;
			}

			public LibraryComponent getComponentByID(LibraryID id) {
				// TODO Auto-generated method stub
				return null;
			}

			public ResourceAdaptorComponent getComponentByID(ResourceAdaptorID id) {
				// TODO Auto-generated method stub
				return null;
			}

			public ResourceAdaptorTypeComponent getComponentByID(ResourceAdaptorTypeID id) {
				// TODO Auto-generated method stub
				return null;
			}

			public ServiceComponent getComponentByID(ServiceID id) {
				// TODO Auto-generated method stub
				return null;
			}

			public boolean isInstalled(ComponentID componentID) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		boolean b = validator.validateCmpFileds(ClassUtils.getAbstractMethodsFromClass(component.getAbstractSbbClass()), ClassUtils
				.getAbstractMethodsFromSuperClasses(component.getAbstractSbbClass()));

		assertFalse("Sbb class has been validated - it should not - accessors ahve different type", b);

	}

	public void testSbbOne11ConstraintsCMPsThrows() throws Exception {

		List<SbbDescriptorImpl> specs = new SbbDescriptorFactory().parse(super.getFileStream(_SBB_JAR_ONE_11_CMPS_THROWS));
		final SbbDescriptorImpl descriptor = specs.get(0);
		final SbbComponent component = new SbbComponent(descriptor);
		component.setAbstractSbbClass(Thread.currentThread().getContextClassLoader().loadClass(
				descriptor.getSbbAbstractClass().getSbbAbstractClassName()));
		component.setSbbLocalInterfaceClass(Thread.currentThread().getContextClassLoader().loadClass(
				descriptor.getSbbLocalInterface().getSbbLocalInterfaceName()));
		component.setActivityContextInterface(Thread.currentThread().getContextClassLoader().loadClass(
				descriptor.getSbbActivityContextInterface().getInterfaceName()));
		SbbComponentValidator validator = new SbbComponentValidator();
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

			public ProfileSpecificationComponent getComponentByID(ProfileSpecificationID id) {
				// TODO Auto-generated method stub
				return null;
			}

			public LibraryComponent getComponentByID(LibraryID id) {
				// TODO Auto-generated method stub
				return null;
			}

			public ResourceAdaptorComponent getComponentByID(ResourceAdaptorID id) {
				// TODO Auto-generated method stub
				return null;
			}

			public ResourceAdaptorTypeComponent getComponentByID(ResourceAdaptorTypeID id) {
				// TODO Auto-generated method stub
				return null;
			}

			public ServiceComponent getComponentByID(ServiceID id) {
				// TODO Auto-generated method stub
				return null;
			}

			public boolean isInstalled(ComponentID componentID) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		boolean b = validator.validateCmpFileds(ClassUtils.getAbstractMethodsFromClass(component.getAbstractSbbClass()), ClassUtils
				.getAbstractMethodsFromSuperClasses(component.getAbstractSbbClass()));

		assertFalse("Sbb class has been validated - it should not - accessor has throws", b);

	}

	public void testSbbOne11ConstraintsCMPsConcrete() throws Exception {

		List<SbbDescriptorImpl> specs = new SbbDescriptorFactory().parse(super.getFileStream(_SBB_JAR_ONE_11_CMPS_CONCRETE));
		final SbbDescriptorImpl descriptor = specs.get(0);
		final SbbComponent component = new SbbComponent(descriptor);
		component.setAbstractSbbClass(Thread.currentThread().getContextClassLoader().loadClass(
				descriptor.getSbbAbstractClass().getSbbAbstractClassName()));
		component.setSbbLocalInterfaceClass(Thread.currentThread().getContextClassLoader().loadClass(
				descriptor.getSbbLocalInterface().getSbbLocalInterfaceName()));
		component.setActivityContextInterface(Thread.currentThread().getContextClassLoader().loadClass(
				descriptor.getSbbActivityContextInterface().getInterfaceName()));
		SbbComponentValidator validator = new SbbComponentValidator();
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

			public ProfileSpecificationComponent getComponentByID(ProfileSpecificationID id) {
				// TODO Auto-generated method stub
				return null;
			}

			public LibraryComponent getComponentByID(LibraryID id) {
				// TODO Auto-generated method stub
				return null;
			}

			public ResourceAdaptorComponent getComponentByID(ResourceAdaptorID id) {
				// TODO Auto-generated method stub
				return null;
			}

			public ResourceAdaptorTypeComponent getComponentByID(ResourceAdaptorTypeID id) {
				// TODO Auto-generated method stub
				return null;
			}

			public ServiceComponent getComponentByID(ServiceID id) {
				// TODO Auto-generated method stub
				return null;
			}

			public boolean isInstalled(ComponentID componentID) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		boolean b = validator.validateCmpFileds(ClassUtils.getAbstractMethodsFromClass(component.getAbstractSbbClass()), ClassUtils
				.getAbstractMethodsFromSuperClasses(component.getAbstractSbbClass()));

		assertFalse("Sbb class has been validated", b);

	}

	public void testSbbOne11ConstraintsCMPsScope() throws Exception {

		List<SbbDescriptorImpl> specs = new SbbDescriptorFactory().parse(super.getFileStream(_SBB_JAR_ONE_11_CMPS_SCOPE));
		final SbbDescriptorImpl descriptor = specs.get(0);
		final SbbComponent component = new SbbComponent(descriptor);
		component.setAbstractSbbClass(Thread.currentThread().getContextClassLoader().loadClass(
				descriptor.getSbbAbstractClass().getSbbAbstractClassName()));
		component.setSbbLocalInterfaceClass(Thread.currentThread().getContextClassLoader().loadClass(
				descriptor.getSbbLocalInterface().getSbbLocalInterfaceName()));
		component.setActivityContextInterface(Thread.currentThread().getContextClassLoader().loadClass(
				descriptor.getSbbActivityContextInterface().getInterfaceName()));
		SbbComponentValidator validator = new SbbComponentValidator();
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

			public ProfileSpecificationComponent getComponentByID(ProfileSpecificationID id) {
				// TODO Auto-generated method stub
				return null;
			}

			public LibraryComponent getComponentByID(LibraryID id) {
				// TODO Auto-generated method stub
				return null;
			}

			public ResourceAdaptorComponent getComponentByID(ResourceAdaptorID id) {
				// TODO Auto-generated method stub
				return null;
			}

			public ResourceAdaptorTypeComponent getComponentByID(ResourceAdaptorTypeID id) {
				// TODO Auto-generated method stub
				return null;
			}

			public ServiceComponent getComponentByID(ServiceID id) {
				// TODO Auto-generated method stub
				return null;
			}

			public boolean isInstalled(ComponentID componentID) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		boolean b = validator.validateCmpFileds(ClassUtils.getAbstractMethodsFromClass(component.getAbstractSbbClass()), ClassUtils
				.getAbstractMethodsFromSuperClasses(component.getAbstractSbbClass()));

		assertFalse("Sbb class has been validated it shouyld not - it declares not public cmp acccessor", b);

	}

	public void testSbbOne11ConstraintsCMPsNoRefCustomSbbLO() throws Exception {

		List<SbbDescriptorImpl> specs = new SbbDescriptorFactory().parse(super.getFileStream(_SBB_JAR_ONE_11_CMPS_NO_REF_ON_CUSTOM_SBBLO));
		final SbbDescriptorImpl descriptor = specs.get(0);
		final SbbComponent component = new SbbComponent(descriptor);
		component.setAbstractSbbClass(Thread.currentThread().getContextClassLoader().loadClass(
				descriptor.getSbbAbstractClass().getSbbAbstractClassName()));
		component.setSbbLocalInterfaceClass(Thread.currentThread().getContextClassLoader().loadClass(
				descriptor.getSbbLocalInterface().getSbbLocalInterfaceName()));
		component.setActivityContextInterface(Thread.currentThread().getContextClassLoader().loadClass(
				descriptor.getSbbActivityContextInterface().getInterfaceName()));
		SbbComponentValidator validator = new SbbComponentValidator();
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

			public ProfileSpecificationComponent getComponentByID(ProfileSpecificationID id) {
				// TODO Auto-generated method stub
				return null;
			}

			public LibraryComponent getComponentByID(LibraryID id) {
				// TODO Auto-generated method stub
				return null;
			}

			public ResourceAdaptorComponent getComponentByID(ResourceAdaptorID id) {
				// TODO Auto-generated method stub
				return null;
			}

			public ResourceAdaptorTypeComponent getComponentByID(ResourceAdaptorTypeID id) {
				// TODO Auto-generated method stub
				return null;
			}

			public ServiceComponent getComponentByID(ServiceID id) {
				// TODO Auto-generated method stub
				return null;
			}

			public boolean isInstalled(ComponentID componentID) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		boolean b = validator.validateCmpFileds(ClassUtils.getAbstractMethodsFromClass(component.getAbstractSbbClass()), ClassUtils
				.getAbstractMethodsFromSuperClasses(component.getAbstractSbbClass()));

		assertFalse("Sbb class has been validated it should not - since cmp field has no sbb reference - it must store generic SBB LO", b);

	}

}
