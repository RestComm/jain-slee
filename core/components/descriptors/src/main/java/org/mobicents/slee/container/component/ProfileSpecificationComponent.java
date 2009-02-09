/**
 * Start time:15:32:06 2009-02-02<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component;

import java.util.logging.Logger;

import javax.slee.management.DeployableUnitID;
import javax.slee.profile.ProfileSpecificationID;

import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ProfileSpecificationDescriptorImpl;

/**
 * Start time:15:32:06 2009-02-02<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ProfileSpecificationComponent {
	
	protected static final transient Logger logger = Logger
			.getLogger(ProfileSpecificationComponent.class.getName());
	
	protected DeployableUnitID deployableUnitID=null;
	protected ProfileSpecificationDescriptorImpl descriptor=null;
	protected ProfileSpecificationID profileSpecificationID=null;
	
	
	protected transient Class profileCmpInterfaceClass=null;
	protected transient Class profileLocalInterfaceClass=null;
	protected transient Class profileManagementInterfaceClass=null;
	protected transient Class profileAbstractClass=null;
	protected transient Class profileTableInterfaceClass=null;
	public DeployableUnitID getDeployableUnitID() {
		return deployableUnitID;
	}
	public void setDeployableUnitID(DeployableUnitID deployableUnitID) {
		this.deployableUnitID = deployableUnitID;
	}
	public ProfileSpecificationDescriptorImpl getDescriptor() {
		return descriptor;
	}
	public void setDescriptor(ProfileSpecificationDescriptorImpl descriptor) {
		this.descriptor = descriptor;
	}
	public ProfileSpecificationID getProfileSpecificationID() {
		return profileSpecificationID;
	}
	public void setProfileSpecificationID(
			ProfileSpecificationID profileSpecificationID) {
		this.profileSpecificationID = profileSpecificationID;
	}
	public Class getProfileCmpInterfaceClass() {
		return profileCmpInterfaceClass;
	}
	public void setProfileCmpInterfaceClass(Class profileCmpInterfaceClass) {
		this.profileCmpInterfaceClass = profileCmpInterfaceClass;
	}
	public Class getProfileLocalInterfaceClass() {
		return profileLocalInterfaceClass;
	}
	public void setProfileLocalInterfaceClass(Class profileLocalInterfaceClass) {
		this.profileLocalInterfaceClass = profileLocalInterfaceClass;
	}
	public Class getProfileManagementInterfaceClass() {
		return profileManagementInterfaceClass;
	}
	public void setProfileManagementInterfaceClass(
			Class profileManagementInterfaceClass) {
		this.profileManagementInterfaceClass = profileManagementInterfaceClass;
	}
	public Class getProfileAbstractClass() {
		return profileAbstractClass;
	}
	public void setProfileAbstractClass(Class profileAbstractClass) {
		this.profileAbstractClass = profileAbstractClass;
	}
	public Class getProfileTableInterfaceClass() {
		return profileTableInterfaceClass;
	}
	public void setProfileTableInterfaceClass(Class profileTableInterfaceClass) {
		this.profileTableInterfaceClass = profileTableInterfaceClass;
	}
	
	public boolean isSlee11() {
		return this.descriptor.isSlee11();
	}

	
}
