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
import java.util.Map;

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
		

		final SbbDescriptorImpl descriptor = SbbDescriptorImpl.parseDocument(
				super.parseDocument(_SBB_JAR_ONE_11_PROFILE_CMP_OK), null)[0];
		SbbComponent component = new FakeComponent();
		component.setAbstractSbbClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getSbbAbstractClass()
								.getSbbAbstractClassName()));
		SbbComponentValidator validator = new SbbComponentValidator();

		component.setDescriptor(descriptor);
		validator.setComponent(component);
		validator.setComponentRepository(new FakeRepository());
		boolean b = validator.validateGetProfileCmpInterfaceMethods(ClassUtils
				.getAbstractMethodsFromClass(component.getAbstractSbbClass()),
				ClassUtils.getAbstractMethodsFromSuperClasses(component
						.getAbstractSbbClass()));

		assertTrue("Sbb class has not been validated", b);

	}
	
	public void testSbbOne11ConstraintsWrongThrows() throws Exception {

		final SbbDescriptorImpl descriptor = SbbDescriptorImpl.parseDocument(
				super.parseDocument(_SBB_JAR_ONE_11_PROFILE_CMP_OK), null)[0];
		SbbComponent component = new FakeComponent();
		component.setAbstractSbbClass(SbbConstraintsProfileCMPWrongThrowsSbb.class);
		SbbComponentValidator validator = new SbbComponentValidator();

		component.setDescriptor(descriptor);
		validator.setComponent(component);
		validator.setComponentRepository(new FakeRepository());
		boolean b = validator.validateGetProfileCmpInterfaceMethods(ClassUtils
				.getAbstractMethodsFromClass(component.getAbstractSbbClass()),
				ClassUtils.getAbstractMethodsFromSuperClasses(component
						.getAbstractSbbClass()));

		assertFalse("Sbb class has been validated", b);

	}
	public void testSbbOne11ConstraintsWrongParameter() throws Exception {

		final SbbDescriptorImpl descriptor = SbbDescriptorImpl.parseDocument(
				super.parseDocument(_SBB_JAR_ONE_11_PROFILE_CMP_OK), null)[0];
		SbbComponent component = new FakeComponent();
		component.setAbstractSbbClass(SbbConstraintsProfileCMPWrongParameterSbb.class);
		SbbComponentValidator validator = new SbbComponentValidator();

		component.setDescriptor(descriptor);
		validator.setComponent(component);
		validator.setComponentRepository(new FakeRepository());
		boolean b = validator.validateGetProfileCmpInterfaceMethods(ClassUtils
				.getAbstractMethodsFromClass(component.getAbstractSbbClass()),
				ClassUtils.getAbstractMethodsFromSuperClasses(component
						.getAbstractSbbClass()));

		assertFalse("Sbb class has been validated", b);

	}
	public void testSbbOne11ConstraintsWrongVisibility() throws Exception {

		final SbbDescriptorImpl descriptor = SbbDescriptorImpl.parseDocument(
				super.parseDocument(_SBB_JAR_ONE_11_PROFILE_CMP_OK), null)[0];
		SbbComponent component = new FakeComponent();
		component.setAbstractSbbClass(SbbConstraintsProfileCMPWrongVisibilitySbb.class);
		SbbComponentValidator validator = new SbbComponentValidator();

		component.setDescriptor(descriptor);
		validator.setComponent(component);
		validator.setComponentRepository(new FakeRepository());
		boolean b = validator.validateGetProfileCmpInterfaceMethods(ClassUtils
				.getAbstractMethodsFromClass(component.getAbstractSbbClass()),
				ClassUtils.getAbstractMethodsFromSuperClasses(component
						.getAbstractSbbClass()));

		assertFalse("Sbb class has been validated", b);

	}
	
	public void testSbbOne11ConstraintsWrongPrefix() throws Exception {

		final SbbDescriptorImpl descriptor = SbbDescriptorImpl.parseDocument(
				super.parseDocument(_SBB_JAR_ONE_11_PROFILE_CMP_WRONG_PREFIX), null)[0];
		SbbComponent component = new FakeComponent();

		SbbComponentValidator validator = new SbbComponentValidator();
		component.setAbstractSbbClass(Thread.currentThread()
				.getContextClassLoader().loadClass(
						descriptor.getSbbAbstractClass()
								.getSbbAbstractClassName()));
		component.setDescriptor(descriptor);
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

		ProfileSpecificationComponent cmp = new ProfileSpecificationComponent();
		cmp.setProfileCmpInterfaceClass(ProfileCmpInterface.class);

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
}

class FakeComponent extends SbbComponent
{

	@Override
	public Map<String, ProfileSpecificationID> getProfileReferences() {
		HashMap<String, ProfileSpecificationID> tmps=new HashMap<String, ProfileSpecificationID>();
		tmps.put("xxx",new ProfileSpecificationID("x","y","z"));
		return tmps;
	}
	

}
