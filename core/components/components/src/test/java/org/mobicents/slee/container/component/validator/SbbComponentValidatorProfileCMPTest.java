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

import javax.slee.ComponentID;
import javax.slee.EventTypeID;
import javax.slee.SbbID;
import javax.slee.ServiceID;
import javax.slee.management.DeploymentException;
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
import org.mobicents.slee.container.component.validator.ClassUtils;
import org.mobicents.slee.container.component.validator.SbbComponentValidator;
import org.mobicents.slee.container.component.validator.sbb.abstracts.profilecmp.ProfileCmpInterface;
import org.mobicents.slee.container.component.validator.sbb.abstracts.profilecmp.SbbConstraintsProfileCMPWrongParameterSbb;
import org.mobicents.slee.container.component.validator.sbb.abstracts.profilecmp.SbbConstraintsProfileCMPWrongPrefixSbb;
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
		
		List<SbbDescriptorImpl> specs = new SbbDescriptorFactory().parse(super.getFileStream(_SBB_JAR_ONE_11_PROFILE_CMP_OK));
		final SbbDescriptorImpl descriptor = specs.get(0);
		
		
		SbbComponent component = new FakeComponent(descriptor);
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

		
		List<SbbDescriptorImpl> specs = new SbbDescriptorFactory().parse(super.getFileStream(_SBB_JAR_ONE_11_PROFILE_CMP_OK));
		final SbbDescriptorImpl descriptor = specs.get(0);

		SbbComponent component = new FakeComponent(descriptor);
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

		List<SbbDescriptorImpl> specs = new SbbDescriptorFactory().parse(super.getFileStream(_SBB_JAR_ONE_11_PROFILE_CMP_OK));
		final SbbDescriptorImpl descriptor = specs.get(0);

		SbbComponent component = new FakeComponent(descriptor);
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

		List<SbbDescriptorImpl> specs = new SbbDescriptorFactory().parse(super.getFileStream(_SBB_JAR_ONE_11_PROFILE_CMP_OK));
		final SbbDescriptorImpl descriptor = specs.get(0);

	
		SbbComponent component = new FakeComponent(descriptor);
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

		List<SbbDescriptorImpl> specs = new SbbDescriptorFactory().parse(super.getFileStream(_SBB_JAR_ONE_11_PROFILE_CMP_WRONG_PREFIX));
		final SbbDescriptorImpl descriptor = specs.get(0);

		SbbComponent component = new FakeComponent(descriptor);

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

		ProfileSpecificationComponent cmp = new ProfileSpecificationComponent(null);
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
}

class FakeComponent extends SbbComponent
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
