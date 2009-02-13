/**
 * Start time:15:32:06 2009-02-02<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.slee.ComponentID;
import javax.slee.profile.ProfileSpecificationID;

import org.mobicents.slee.container.component.validator.ProfileSpecificationComponentValidator;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ProfileSpecificationDescriptorImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.query.MQuery;

/**
 * Start time:15:32:06 2009-02-02<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ProfileSpecificationComponent extends SleeComponent {

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
	 * the profile usage interface
	 */
	private Class profileUsageInterfaceClass = null;
	
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

	/**
	 * Sets the profile usage interface
	 * @param profileUsageInterfaceClass
	 */
	public void setProfileUsageInterfaceClass(Class profileUsageInterfaceClass) {
		this.profileUsageInterfaceClass = profileUsageInterfaceClass;
	}

	/**
	 * Retrieves the profile usage interface
	 * @return
	 */
	public Class getProfileUsageInterfaceClass() {
		return profileUsageInterfaceClass;
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
}
