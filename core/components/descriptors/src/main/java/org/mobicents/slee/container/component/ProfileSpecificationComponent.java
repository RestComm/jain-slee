/**
 * Start time:15:32:06 2009-02-02<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component;

import java.util.HashSet;
import java.util.Set;

import javax.slee.ComponentID;
import javax.slee.management.ComponentDescriptor;
import javax.slee.management.DependencyException;
import javax.slee.management.DeploymentException;
import javax.slee.management.LibraryID;
import javax.slee.profile.ProfileSpecificationDescriptor;
import javax.slee.profile.ProfileSpecificationID;

import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ProfileSpecificationDescriptorImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.references.MLibraryRef;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.references.MProfileSpecRef;
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
	 * the JAIN SLEE specs descriptor
	 */
	private ProfileSpecificationDescriptor specsDescriptor = null;
	
	/**
	 * 
	 * @param descriptor
	 */
	public ProfileSpecificationComponent(
			ProfileSpecificationDescriptorImpl descriptor) {
		this.descriptor = descriptor;
	}

	/**
	 * Retrieves the profile specification descriptor
	 * @return
	 */
	public ProfileSpecificationDescriptorImpl getDescriptor() {
		return descriptor;
	}

	/**
	 * Retrieves the profile specification id
	 * @return
	 */
	public ProfileSpecificationID getProfileSpecificationID() {
		return descriptor.getProfileSpecificationID();
	}

	/**
	 * Retrieves the profile cmp interface
	 * @return
	 */
	public Class getProfileCmpInterfaceClass() {
		return profileCmpInterfaceClass;
	}

	/**
	 * Sets the profile cmp interface
	 * @param profileCmpInterfaceClass
	 */
	public void setProfileCmpInterfaceClass(Class profileCmpInterfaceClass) {
		this.profileCmpInterfaceClass = profileCmpInterfaceClass;
	}

	/**
	 * Retrieves the profile local interface
	 * @return
	 */
	public Class getProfileLocalInterfaceClass() {
		return profileLocalInterfaceClass;
	}

	/**
	 * Sets the profile local interface
	 * @param profileLocalInterfaceClass
	 */
	public void setProfileLocalInterfaceClass(Class profileLocalInterfaceClass) {
		this.profileLocalInterfaceClass = profileLocalInterfaceClass;
	}

	/**
	 * Retrieves the profile management interface
	 * @return
	 */
	public Class getProfileManagementInterfaceClass() {
		return profileManagementInterfaceClass;
	}

	/**
	 * Sets the profile management interface
	 * @param profileManagementInterfaceClass
	 */
	public void setProfileManagementInterfaceClass(
			Class profileManagementInterfaceClass) {
		this.profileManagementInterfaceClass = profileManagementInterfaceClass;
	}

	/**
	 * Retrieves the profile abstract class
	 * @return
	 */
	public Class getProfileAbstractClass() {
		return profileAbstractClass;
	}

	/**
	 * Sets the profile abstract class
	 * @param profileAbstractClass
	 */
	public void setProfileAbstractClass(Class profileAbstractClass) {
		this.profileAbstractClass = profileAbstractClass;
	}

	/**
	 * Retrieves the profile table interface
	 * @return
	 */
	public Class getProfileTableInterfaceClass() {
		return profileTableInterfaceClass;
	}

	/**
	 * Sets the profile table interface
	 * @param profileTableInterfaceClass
	 */
	public void setProfileTableInterfaceClass(Class profileTableInterfaceClass) {
		this.profileTableInterfaceClass = profileTableInterfaceClass;
	}

	@Override
	void addToDeployableUnit() {
		getDeployableUnit().getProfileSpecificationComponents().put(getProfileSpecificationID(), this);
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
	 *  Retrieves the JAIN SLEE specs descriptor
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
			specsDescriptor = new ProfileSpecificationDescriptor(getProfileSpecificationID(),getDeployableUnit().getDeployableUnitID(),getDeploymentUnitSource(),libraryIDs,profileSpecs,getDescriptor().getProfileCMPInterface().getProfileCmpInterfaceName());
		}
		return specsDescriptor;
	}
	
	@Override
	public ComponentDescriptor getComponentDescriptor() {
		return getSpecsDescriptor();
	}
}
