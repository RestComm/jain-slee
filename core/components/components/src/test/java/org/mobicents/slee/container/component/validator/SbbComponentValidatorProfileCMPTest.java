/**
 * Start time:17:07:31 2009-01-31<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.validator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.slee.ComponentID;
import javax.slee.EventTypeID;
import javax.slee.SbbID;
import javax.slee.ServiceID;
import javax.slee.UnrecognizedComponentException;
import javax.slee.management.DeploymentException;
import javax.slee.management.LibraryID;
import javax.slee.profile.ProfileSpecificationID;
import javax.slee.resource.ResourceAdaptorID;
import javax.slee.resource.ResourceAdaptorTypeID;

import org.mobicents.slee.container.component.ComponentRepository;
import org.mobicents.slee.container.component.ProfileSpecificationComponentImpl;
import org.mobicents.slee.container.component.SbbComponentImpl;
import org.mobicents.slee.container.component.SleeComponent;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.SbbDescriptorFactoryImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.SbbDescriptorImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.TCUtilityClass;
import org.mobicents.slee.container.component.event.EventTypeComponent;
import org.mobicents.slee.container.component.library.LibraryComponent;
import org.mobicents.slee.container.component.profile.ProfileSpecificationComponent;
import org.mobicents.slee.container.component.ra.ResourceAdaptorComponent;
import org.mobicents.slee.container.component.ratype.ResourceAdaptorTypeComponent;
import org.mobicents.slee.container.component.sbb.SbbComponent;
import org.mobicents.slee.container.component.service.ServiceComponent;
import org.mobicents.slee.container.component.validator.ClassUtils;
import org.mobicents.slee.container.component.validator.SbbComponentValidator;
import org.mobicents.slee.container.component.validator.sbb.abstracts.profilecmp.ProfileCmpInterface;
import org.mobicents.slee.container.component.validator.sbb.abstracts.profilecmp.SbbConstraintsProfileCMPWrongParameterSbb;
import org.mobicents.slee.container.component.validator.sbb.abstracts.profilecmp.SbbConstraintsProfileCMPWrongThrowsSbb;
import org.mobicents.slee.container.component.validator.sbb.abstracts.profilecmp.SbbConstraintsProfileCMPWrongVisibilitySbb;

/**
 * Start time:17:07:31 2009-01-31<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class SbbComponentValidatorProfileCMPTest extends TCUtilityClass {

	public static final String _SBB_JAR_ONE_11_PROFILE_CMP_OK = "xml/validator/sbb/profilecmp/sbb-jar-one-SbbProfileCmpOk_1_1.xml";
	public static final String _SBB_JAR_ONE_11_PROFILE_CMP_WRONG_PREFIX = "xml/validator/sbb/profilecmp/sbb-jar-one-SbbProfileCmpWrongPrefix_1_1.xml";
	
	public void testSbbOne11ConstraintsOk() throws Exception {
		
		List<SbbDescriptorImpl> specs = new SbbDescriptorFactoryImpl().parse(super.getFileStream(_SBB_JAR_ONE_11_PROFILE_CMP_OK));
		final SbbDescriptorImpl descriptor = specs.get(0);
		
		
		SbbComponentImpl component = new FakeComponent(descriptor);
		component.setAbstractSbbClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getSbbAbstractClass()
								.getSbbAbstractClassName()));
		SbbComponentValidator validator = new SbbComponentValidator();

		validator.setComponent(component);
		validator.setComponentRepository(new FakeRepository());
		boolean b = validator.validateGetProfileCmpInterfaceMethods(ClassUtils
				.getAbstractMethodsFromClass(component.getAbstractSbbClass()),
				ClassUtils.getAbstractMethodsFromSuperClasses(component
						.getAbstractSbbClass()));

		assertTrue("Sbb class has not been validated", b);

	}
	
	public void testSbbOne11ConstraintsWrongThrows() throws Exception {

		
		List<SbbDescriptorImpl> specs = new SbbDescriptorFactoryImpl().parse(super.getFileStream(_SBB_JAR_ONE_11_PROFILE_CMP_OK));
		final SbbDescriptorImpl descriptor = specs.get(0);

		SbbComponentImpl component = new FakeComponent(descriptor);
		component.setAbstractSbbClass(SbbConstraintsProfileCMPWrongThrowsSbb.class);
		SbbComponentValidator validator = new SbbComponentValidator();

		validator.setComponent(component);
		validator.setComponentRepository(new FakeRepository());
		boolean b = validator.validateGetProfileCmpInterfaceMethods(ClassUtils
				.getAbstractMethodsFromClass(component.getAbstractSbbClass()),
				ClassUtils.getAbstractMethodsFromSuperClasses(component
						.getAbstractSbbClass()));

		assertFalse("Sbb class has been validated", b);

	}
	public void testSbbOne11ConstraintsWrongParameter() throws Exception {

		List<SbbDescriptorImpl> specs = new SbbDescriptorFactoryImpl().parse(super.getFileStream(_SBB_JAR_ONE_11_PROFILE_CMP_OK));
		final SbbDescriptorImpl descriptor = specs.get(0);

		SbbComponentImpl component = new FakeComponent(descriptor);
		component.setAbstractSbbClass(SbbConstraintsProfileCMPWrongParameterSbb.class);
		SbbComponentValidator validator = new SbbComponentValidator();

		validator.setComponent(component);
		validator.setComponentRepository(new FakeRepository());
		boolean b = validator.validateGetProfileCmpInterfaceMethods(ClassUtils
				.getAbstractMethodsFromClass(component.getAbstractSbbClass()),
				ClassUtils.getAbstractMethodsFromSuperClasses(component
						.getAbstractSbbClass()));

		assertFalse("Sbb class has been validated", b);

	}
	public void testSbbOne11ConstraintsWrongVisibility() throws Exception {

		List<SbbDescriptorImpl> specs = new SbbDescriptorFactoryImpl().parse(super.getFileStream(_SBB_JAR_ONE_11_PROFILE_CMP_OK));
		final SbbDescriptorImpl descriptor = specs.get(0);

	
		SbbComponentImpl component = new FakeComponent(descriptor);
		component.setAbstractSbbClass(SbbConstraintsProfileCMPWrongVisibilitySbb.class);
		SbbComponentValidator validator = new SbbComponentValidator();

		validator.setComponent(component);
		validator.setComponentRepository(new FakeRepository());
		boolean b = validator.validateGetProfileCmpInterfaceMethods(ClassUtils
				.getAbstractMethodsFromClass(component.getAbstractSbbClass()),
				ClassUtils.getAbstractMethodsFromSuperClasses(component
						.getAbstractSbbClass()));

		assertFalse("Sbb class has been validated", b);

	}
	
	public void testSbbOne11ConstraintsWrongPrefix() throws Exception {

		List<SbbDescriptorImpl> specs = new SbbDescriptorFactoryImpl().parse(super.getFileStream(_SBB_JAR_ONE_11_PROFILE_CMP_WRONG_PREFIX));
		final SbbDescriptorImpl descriptor = specs.get(0);

		SbbComponentImpl component = new FakeComponent(descriptor);

		SbbComponentValidator validator = new SbbComponentValidator();
		component.setAbstractSbbClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getSbbAbstractClass()
								.getSbbAbstractClassName()));
		
		validator.setComponent(component);
		validator.setComponentRepository(new FakeRepository());
		boolean b = validator.validateGetProfileCmpInterfaceMethods(ClassUtils
				.getAbstractMethodsFromClass(component.getAbstractSbbClass()),
				ClassUtils.getAbstractMethodsFromSuperClasses(component
						.getAbstractSbbClass()));

		assertFalse("Sbb class has been validated", b);

	}

}



class FakeRepository implements ComponentRepository {

	public SbbComponent getComponentByID(SbbID id) {
		// TODO Auto-generated method stub
		return null;
	}

	public EventTypeComponent getComponentByID(EventTypeID id) {
		// TODO Auto-generated method stub
		return null;
	}

	public ProfileSpecificationComponent getComponentByID(
			ProfileSpecificationID id) {

		ProfileSpecificationComponentImpl cmp = new ProfileSpecificationComponentImpl(null);
		try {
			cmp.setProfileCmpInterfaceClass(ProfileCmpInterface.class);
		} catch (DeploymentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return cmp;
	}

	public LibraryComponent getComponentByID(LibraryID id) {
		// TODO Auto-generated method stub
		return null;
	}

	public ResourceAdaptorComponent getComponentByID(ResourceAdaptorID id) {
		// TODO Auto-generated method stub
		return null;
	}

	public ResourceAdaptorTypeComponent getComponentByID(
			ResourceAdaptorTypeID id) {
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

	/* (non-Javadoc)
	 * @see org.mobicents.slee.core.component.ComponentRepository#getEventComponentIDs()
	 */
	public Set<EventTypeID> getEventComponentIDs() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.core.component.ComponentRepository#getLibraryIDs()
	 */
	public Set<LibraryID> getLibraryIDs() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.core.component.ComponentRepository#getProfileSpecificationIDs()
	 */
	public Set<ProfileSpecificationID> getProfileSpecificationIDs() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.core.component.ComponentRepository#getReferringComponents(org.mobicents.slee.core.component.SleeComponent)
	 */
	public Set<SleeComponent> getReferringComponents(
			SleeComponent component) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.core.component.ComponentRepository#getResourceAdaptorIDs()
	 */
	public Set<ResourceAdaptorID> getResourceAdaptorIDs() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.core.component.ComponentRepository#getResourceAdaptorTypeIDs()
	 */
	public Set<ResourceAdaptorTypeID> getResourceAdaptorTypeIDs() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.core.component.ComponentRepository#getSbbIDs()
	 */
	public Set<SbbID> getSbbIDs() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.core.component.ComponentRepository#getServiceIDs()
	 */
	public Set<ServiceID> getServiceIDs() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.core.component.ComponentRepository#getReferringComponents(javax.slee.ComponentID)
	 */
	public ComponentID[] getReferringComponents(ComponentID componentID)
			throws NullPointerException, UnrecognizedComponentException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.core.component.ComponentRepository#putComponent(org.mobicents.slee.core.component.event.EventTypeComponent)
	 */
	public boolean putComponent(EventTypeComponent component) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.core.component.ComponentRepository#putComponent(org.mobicents.slee.core.component.library.LibraryComponent)
	 */
	public boolean putComponent(LibraryComponent component) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.core.component.ComponentRepository#putComponent(org.mobicents.slee.core.component.profile.ProfileSpecificationComponent)
	 */
	public boolean putComponent(ProfileSpecificationComponent component) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.core.component.ComponentRepository#putComponent(org.mobicents.slee.core.component.ra.ResourceAdaptorComponent)
	 */
	public boolean putComponent(ResourceAdaptorComponent component) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.core.component.ComponentRepository#putComponent(org.mobicents.slee.core.component.ratype.ResourceAdaptorTypeComponent)
	 */
	public boolean putComponent(ResourceAdaptorTypeComponent component) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.core.component.ComponentRepository#putComponent(org.mobicents.slee.core.component.sbb.SbbComponent)
	 */
	public boolean putComponent(SbbComponent component) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.core.component.ComponentRepository#putComponent(org.mobicents.slee.core.component.service.ServiceComponent)
	 */
	public boolean putComponent(ServiceComponent component) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.core.component.ComponentRepository#removeComponent(javax.slee.EventTypeID)
	 */
	public void removeComponent(EventTypeID componentID) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.core.component.ComponentRepository#removeComponent(javax.slee.management.LibraryID)
	 */
	public void removeComponent(LibraryID componentID) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.core.component.ComponentRepository#removeComponent(javax.slee.profile.ProfileSpecificationID)
	 */
	public void removeComponent(ProfileSpecificationID componentID) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.core.component.ComponentRepository#removeComponent(javax.slee.resource.ResourceAdaptorID)
	 */
	public void removeComponent(ResourceAdaptorID componentID) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.core.component.ComponentRepository#removeComponent(javax.slee.resource.ResourceAdaptorTypeID)
	 */
	public void removeComponent(ResourceAdaptorTypeID componentID) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.core.component.ComponentRepository#removeComponent(javax.slee.SbbID)
	 */
	public void removeComponent(SbbID componentID) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.core.component.ComponentRepository#removeComponent(javax.slee.ServiceID)
	 */
	public void removeComponent(ServiceID componentID) {
		// TODO Auto-generated method stub
		
	}
}

class FakeComponent extends SbbComponentImpl
{

	public FakeComponent(SbbDescriptorImpl descriptor) {
		super(descriptor);
	}

	/**
	 * FIXME emmartins: is this needed? it is not used, I removed it from component class so I had to remove the override annot here too
	 */
	public Map<String, ProfileSpecificationID> getProfileReferences() {
		HashMap<String, ProfileSpecificationID> tmps=new HashMap<String, ProfileSpecificationID>();
		tmps.put("xxx",new ProfileSpecificationID("x","y","z"));
		return tmps;
	}
	

}
