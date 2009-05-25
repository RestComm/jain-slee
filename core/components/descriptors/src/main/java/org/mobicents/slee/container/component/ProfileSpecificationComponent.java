/**
 * Start time:15:32:06 2009-02-02<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.slee.ComponentID;
import javax.slee.management.ComponentDescriptor;
import javax.slee.management.DependencyException;
import javax.slee.management.DeploymentException;
import javax.slee.management.LibraryID;
import javax.slee.profile.ProfileSpecificationDescriptor;
import javax.slee.profile.ProfileSpecificationID;

import org.apache.tools.ant.util.StringUtils;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ProfileSpecificationDescriptorImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.references.MLibraryRef;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.references.MProfileSpecRef;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.MCMPField;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.MProfileIndex;
import org.mobicents.slee.container.component.security.PermissionHolder;
import org.mobicents.slee.container.component.validator.ProfileSpecificationComponentValidator;

/**
 * Start time:15:32:06 2009-02-02<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ProfileSpecificationComponent extends SleeComponentWithUsageParametersInterface {

	/**
	 * the profile specification descriptor
	 */
	private final ProfileSpecificationDescriptorImpl descriptor;

	/**
	 * the profile cmp interface
	 */
	private Class profileCmpInterfaceClass = null;

	/**
	 * the profile local interface
	 */
	private Class profileLocalInterfaceClass = null;

	/**
	 * the profile management interface
	 */
	private Class profileManagementInterfaceClass = null;

	/**
	 * the profile abstract class
	 */
	private Class profileAbstractClass = null;

	/**
	 * the profile table interface
	 */
	private Class profileTableInterfaceClass = null;

	/**
	 * holds concrete class that implementeds all required interface and methods
	 */
	private Class profileConcreteClass = null;

	/**
	 * holder of cmp data for a profile
	 */
	private Class profileEntityClass = null;
	
	/**
	 * holds reference to generate MBean interface class for this specification
	 */
	private Class profileMBeanConcreteInterfaceClass = null;

	/**
	 * Holds refence to concrete MBean Impl for mgmt mbean for this profiel spec
	 */
	private Class profileMBeanConcreteImplClass = null;

	/**
	 * the JAIN SLEE specs descriptor
	 */
	private ProfileSpecificationDescriptor specsDescriptor = null;

	/**
	 * Dont know yet what it is? ;[
	 */
	private Class profilePersistanceTransientStateConcreteClass;

	/**
	 * Class object for profiel table.
	 */
	private Class profileTableConcreteClass;

	/**
	 * 
	 */
	private Class profileLocalObjectConcreteClass;

	/**
	 * a map containing all attributes of the profile specification
	 */
	private Map<String, ProfileAttribute> profileAttributeMap;
	
	/**
	 * 
	 * @param descriptor
	 */
	public ProfileSpecificationComponent(ProfileSpecificationDescriptorImpl descriptor) {
		this.descriptor = descriptor;
	}

	/**
	 * Retrieves the profile specification descriptor
	 * 
	 * @return
	 */
	public ProfileSpecificationDescriptorImpl getDescriptor() {
		return descriptor;
	}

	/**
	 * Retrieves the profile specification id
	 * 
	 * @return
	 */
	public ProfileSpecificationID getProfileSpecificationID() {
		return descriptor.getProfileSpecificationID();
	}

	/**
	 * Retrieves the profile cmp interface
	 * 
	 * @return
	 */
	public Class getProfileCmpInterfaceClass() {
		return profileCmpInterfaceClass;
	}

	/**
	 * Sets the profile cmp interface and builds the profile attribute map
	 * 
	 * @param profileCmpInterfaceClass
	 * @throws DeploymentException 
	 */
	public void setProfileCmpInterfaceClass(Class profileCmpInterfaceClass) throws DeploymentException {
		this.profileCmpInterfaceClass = profileCmpInterfaceClass;
		buildProfileAttributeMap();
	}

	/**
	 * Builds the profile attribute map using the cmp interface class
	 * @throws DeploymentException 
	 */
	private void buildProfileAttributeMap() throws DeploymentException {
		 HashMap<String, ProfileAttribute> map = new HashMap<String, ProfileAttribute>();
		 Class<?> cmpInterface = getProfileCmpInterfaceClass();
		 String attributeGetterMethodPrefix = "get";
		 for (Method method : cmpInterface.getMethods()) {
			 if (!method.getDeclaringClass().equals(Object.class) && method.getName().startsWith(attributeGetterMethodPrefix)) {
				 String attributeName = method.getName().substring(attributeGetterMethodPrefix.length());
				 switch (attributeName.length()) {
				 case 0:
					throw new DeploymentException("the profile cmp interface class has an invalid attribute getter method name > "+method.getName());					
				 case 1:
					attributeName = attributeName.toLowerCase();
					break;					
				 default:
					attributeName = attributeName.substring(0, 1).toLowerCase() + attributeName.substring(1);
				 	break;
				 }			
				 ProfileAttribute profileAttribute = null;
				 try {
					profileAttribute = new ProfileAttribute(attributeName,method.getReturnType());
				 } catch (Throwable e) {
					throw new DeploymentException("Invalid profile cmp interface attribute getter method definition ( name = "+attributeName+" , type = "+method.getReturnType()+" )",e);
				 }
				 if (isSlee11()) {
					 for (MCMPField cmpField : getDescriptor().getProfileCMPInterface().getCmpFields()) {
						 if (cmpField.getCmpFieldName().equals(attributeName)) {
							 // TODO add index hints ?
							 profileAttribute.setUnique(cmpField.getUnique());
						 }
					 }
				 }
				 else {
					 for (MProfileIndex profileIndex : getDescriptor().getIndexedAttributes()) {
						 if (profileIndex.getName().equals(attributeName)) {
							 profileAttribute.setIndex(true);
							 profileAttribute.setUnique(profileIndex.getUnique());
						 }
					 }
				 }
				 map.put(attributeName, profileAttribute);
			 }
		 }
		 profileAttributeMap = Collections.unmodifiableMap(map);		
	}

	/**
	 * Retrieves the profile local interface
	 * 
	 * @return
	 */
	public Class getProfileLocalInterfaceClass() {
		return profileLocalInterfaceClass;
	}

	/**
	 * Sets the profile local interface
	 * 
	 * @param profileLocalInterfaceClass
	 */
	public void setProfileLocalInterfaceClass(Class profileLocalInterfaceClass) {
		this.profileLocalInterfaceClass = profileLocalInterfaceClass;
	}

	/**
	 * Retrieves the profile management interface
	 * 
	 * @return
	 */
	public Class getProfileManagementInterfaceClass() {
		return profileManagementInterfaceClass;
	}

	/**
	 * Sets the profile management interface
	 * 
	 * @param profileManagementInterfaceClass
	 */
	public void setProfileManagementInterfaceClass(Class profileManagementInterfaceClass) {
		this.profileManagementInterfaceClass = profileManagementInterfaceClass;
	}

	/**
	 * Retrieves the profile abstract class
	 * 
	 * @return
	 */
	public Class getProfileAbstractClass() {
		return profileAbstractClass;
	}

	/**
	 * Sets the profile abstract class
	 * 
	 * @param profileAbstractClass
	 */
	public void setProfileAbstractClass(Class profileAbstractClass) {
		this.profileAbstractClass = profileAbstractClass;
	}

	/**
	 * Retrieves the profile table interface
	 * 
	 * @return
	 */
	public Class getProfileTableInterfaceClass() {
		return profileTableInterfaceClass;
	}

	/**
	 * Sets the profile table interface
	 * 
	 * @param profileTableInterfaceClass
	 */
	public void setProfileTableInterfaceClass(Class profileTableInterfaceClass) {
		this.profileTableInterfaceClass = profileTableInterfaceClass;
	}

	/**
	 * Returns class that holds cmp data for a profile
	 * implements all required
	 * 
	 * @return
	 */
	public Class getProfileEntityClass() {
		return profileEntityClass;
	}

	/**
	 * Sets class that holds cmp data for a profile
	 * implements all required
	 * 
	 * @param c
	 */
	public void setProfileEntityClass(Class c) {
		this.profileEntityClass = c;
	}

	/**
	 * Returns class object representing concrete impl of cmp interface - it
	 * implements all required
	 * 
	 * @return
	 */
	public Class getProfileCmpConcreteClass() {
		return profileConcreteClass;
	}

	/**
	 * Set class object representing concrete impl of cmp interface - it
	 * implements all required
	 * 
	 * @param profileCmpConcreteClass
	 */
	public void setProfileCmpConcreteClass(Class profileCmpConcreteClass) {
		this.profileConcreteClass = profileCmpConcreteClass;
	}

	
	/**
	 * Returns concrete/generated mbean interface for this profile specs
	 * 
	 * @return
	 */
	public Class getProfileMBeanConcreteInterfaceClass() {
		return profileMBeanConcreteInterfaceClass;
	}

	/**
	 * Sets concrete/generated mbean interface for this profile specs
	 * 
	 * @param profileMBeanConcreteInterfaceClass
	 */
	public void setProfileMBeanConcreteInterfaceClass(Class profileMBeanConcreteInterfaceClass) {
		this.profileMBeanConcreteInterfaceClass = profileMBeanConcreteInterfaceClass;
	}

	/**
	 * returns concrete MBean impl that manages this profile spec
	 * 
	 * @return
	 */
	public Class getProfileMBeanConcreteImplClass() {
		return profileMBeanConcreteImplClass;
	}

	/**
	 * sets concrete MBean impl that manages this profile spec
	 * 
	 * @param profileMBeanConcreteImplClass
	 */
	public void setProfileMBeanConcreteImplClass(Class profileMBeanConcreteImplClass) {
		this.profileMBeanConcreteImplClass = profileMBeanConcreteImplClass;
	}

	public void setProfilePersistanceTransientStateConcreteClass(Class profilePersistanceTransientStateConcreteClass) {
		this.profilePersistanceTransientStateConcreteClass = profilePersistanceTransientStateConcreteClass;

	}

	public Class getProfilePersistanceTransientStateConcreteClass() {
		return profilePersistanceTransientStateConcreteClass;
	}

	/**
	 * set profile table concrete class - its impl of profile table interface -
	 * either default or provided by dev - this is instrumented class that
	 * handles runtime calls
	 * 
	 * @param clazz
	 */
	public void setProfileTableConcreteClass(Class clazz) {

		this.profileTableConcreteClass = clazz;
	}

	/**
	 * get profile table concrete class - its impl of profile table interface -
	 * either default or provided by dev - this is instrumented class that
	 * handles runtime calls
	 * 
	 * @return
	 */
	public Class getProfileTableConcreteClass() {
		return this.profileTableConcreteClass;

	}

	/**
	 * set profile local object concrete class - this is instrumented class that
	 * handles runtime calls
	 * 
	 * @param clazz
	 */
	public void setProfileLocalObjectConcreteClass(Class clazz) {
		this.profileLocalObjectConcreteClass = clazz;

	}

	/**
	 * sget profile local object concrete class - this is instrumented class
	 * that handles runtime calls
	 * 
	 * @return
	 */
	public Class getProfileLocalObjectConcreteClass() {
		return this.profileLocalObjectConcreteClass;

	}

	@Override
	boolean addToDeployableUnit() {
		return getDeployableUnit().getProfileSpecificationComponents().put(getProfileSpecificationID(), this) == null;
	}

	@Override
	public Set<ComponentID> getDependenciesSet() {
		return descriptor.getDependenciesSet();
	}

	@Override
	public boolean isSlee11() {
		return this.descriptor.isSlee11();
	}

	@Override
	public ComponentID getComponentID() {
		return getProfileSpecificationID();
	}

	@Override
	public boolean validate() throws DependencyException, DeploymentException {
		ProfileSpecificationComponentValidator validator = new ProfileSpecificationComponentValidator();
		validator.setComponent(this);
		validator.setComponentRepository(getDeployableUnit().getDeployableUnitRepository());
		return validator.validate();
	}

	/**
	 * Retrieves the JAIN SLEE specs descriptor
	 * 
	 * @return
	 */
	public ProfileSpecificationDescriptor getSpecsDescriptor() {
		if (specsDescriptor == null) {
			Set<LibraryID> libraryIDSet = new HashSet<LibraryID>();
			for (MLibraryRef mLibraryRef : getDescriptor().getLibraryRefs()) {
				libraryIDSet.add(mLibraryRef.getComponentID());
			}
			LibraryID[] libraryIDs = libraryIDSet.toArray(new LibraryID[libraryIDSet.size()]);
			Set<ProfileSpecificationID> profileSpecSet = new HashSet<ProfileSpecificationID>();
			for (MProfileSpecRef mProfileSpecRef : getDescriptor().getProfileSpecRefs()) {
				profileSpecSet.add(mProfileSpecRef.getComponentID());
			}
			ProfileSpecificationID[] profileSpecs = profileSpecSet.toArray(new ProfileSpecificationID[profileSpecSet.size()]);
			specsDescriptor = new ProfileSpecificationDescriptor(getProfileSpecificationID(), getDeployableUnit().getDeployableUnitID(), getDeploymentUnitSource(), libraryIDs, profileSpecs,
					getDescriptor().getProfileCMPInterface().getProfileCmpInterfaceName());
		}
		return specsDescriptor;
	}

	@Override
	public ComponentDescriptor getComponentDescriptor() {
		return getSpecsDescriptor();
	}

	@Override
	public void processSecurityPermissions() throws DeploymentException {
		try {
			if (this.descriptor.getSecurityPermissions() != null) {
				super.permissions.add(new PermissionHolder(super.getDeploymentDir().toURI(), this.descriptor.getSecurityPermissions().getSecurityPermissionSpec()));
			}
		} catch (Exception e) {
			throw new DeploymentException("Failed to make permissions usable.", e);
		}
	}

	/**
	 * the Profile CMP Slee 1.0 Wrapper, an object that implements the Profile CMP
	 * Interface, wrapping the SLEE 1.1 real profile concrete object in a SLEE 1.0
	 * compatible interface
	 */
	private Class<?> profileCmpSlee10WrapperClass;

	/**
	 * Retreives the Profile CMP Slee 1.0 Wrapper class, an object that implements the Profile CMP
	 * Interface, wrapping the SLEE 1.1 real profile concrete object in a SLEE 1.0
	 * compatible interface
	 * @return
	 */
	public Class<?> getProfileCmpSlee10WrapperClass() {
		return profileCmpSlee10WrapperClass;
	}

	/**
	 * Sets the Profile CMP Slee 1.0 Wrapper class, an object that implements the Profile CMP
	 * Interface, wrapping the SLEE 1.1 real profile concrete object in a SLEE 1.0
	 * compatible interface
	 * @param clazz
	 */
	public void setProfileCmpSlee10WrapperClass(Class<?> clazz) {
		this.profileCmpSlee10WrapperClass = clazz;		
	}

	/**
	 * Retrieves a unmodifiable map of {@link ProfileAttribute}, the key of this map is the attribute name 
	 * @return
	 */
	public Map<String, ProfileAttribute> getProfileAttributes() {
		return profileAttributeMap;
	}
}
