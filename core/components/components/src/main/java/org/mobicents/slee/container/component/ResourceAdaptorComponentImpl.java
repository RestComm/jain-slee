/**
 * Start time:00:45:25 2009-02-04<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.slee.ComponentID;
import javax.slee.management.ComponentDescriptor;
import javax.slee.management.DependencyException;
import javax.slee.management.DeploymentException;
import javax.slee.management.LibraryID;
import javax.slee.profile.ProfileSpecificationID;
import javax.slee.resource.ConfigProperties;
import javax.slee.resource.ResourceAdaptorID;
import javax.slee.resource.ResourceAdaptorTypeID;

import org.mobicents.slee.container.component.UsageParameterDescriptor;
import org.mobicents.slee.container.component.common.ProfileSpecRefDescriptor;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ResourceAdaptorDescriptorImpl;
import org.mobicents.slee.container.component.ra.ConfigPropertyDescriptor;
import org.mobicents.slee.container.component.ra.ResourceAdaptorComponent;
import org.mobicents.slee.container.component.security.PermissionHolderImpl;

/**
 * Start time:00:45:25 2009-02-04<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ResourceAdaptorComponentImpl extends AbstractSleeComponentWithUsageParametersInterface implements ResourceAdaptorComponent {

	/**
	 * the ra descriptor
	 */
	private final ResourceAdaptorDescriptorImpl descriptor;

	/**
	 * the ra class
	 */
	private Class<?> resourceAdaptorClass = null;

	/**
	 * the JAIN SLEE specs descriptor
	 */
	private javax.slee.resource.ResourceAdaptorDescriptor specsDescriptor = null;

	/**
	 * 
	 * @param descriptor
	 */
	public ResourceAdaptorComponentImpl(ResourceAdaptorDescriptorImpl descriptor) {
		this.descriptor = descriptor;
	}

	/**
	 * Retrieves the ra descriptor
	 * 
	 * @return
	 */
	public ResourceAdaptorDescriptorImpl getDescriptor() {
		return descriptor;
	}

	/**
	 * Retrieves the ra id
	 * 
	 * @return
	 */
	public ResourceAdaptorID getResourceAdaptorID() {
		return descriptor.getResourceAdaptorID();
	}

	/**
	 * Retrieves the ra class
	 * 
	 * @return
	 */
	public Class<?> getResourceAdaptorClass() {
		return resourceAdaptorClass;
	}

	/**
	 * Sets the ra class
	 * 
	 * @param resourceAdaptorClass
	 */
	public void setResourceAdaptorClass(Class<?> resourceAdaptorClass) {
		this.resourceAdaptorClass = resourceAdaptorClass;
	}

	@Override
	public boolean addToDeployableUnit() {
		return getDeployableUnit().getResourceAdaptorComponents().put(getResourceAdaptorID(), this) == null;
	}

	@Override
	public Set<ComponentID> getDependenciesSet() {
		return descriptor.getDependenciesSet();
	}

	@Override
	public ComponentID getComponentID() {
		return getResourceAdaptorID();
	}

	@Override
	public boolean isSlee11() {
		return descriptor.isSlee11();
	}

	@Override
	public boolean validate() throws DependencyException, DeploymentException {
		// FIXME use validator when available
		return true;
	}

	/**
	 * Retrieves the JAIN SLEE specs descriptor
	 * 
	 * @return
	 */
	public javax.slee.resource.ResourceAdaptorDescriptor getSpecsDescriptor() {
		if (specsDescriptor == null) {
			final LibraryID[] libraryIDs = descriptor.getLibraryRefs().toArray(new LibraryID[descriptor.getLibraryRefs().size()]);
			final ResourceAdaptorTypeID[] raTypeIDs = descriptor.getResourceAdaptorTypeRefs().toArray(new ResourceAdaptorTypeID[descriptor.getResourceAdaptorTypeRefs().size()]);
			Set<ProfileSpecificationID> profileSpecSet = new HashSet<ProfileSpecificationID>();
			for (ProfileSpecRefDescriptor mProfileSpecRef : descriptor.getProfileSpecRefs()) {
				profileSpecSet.add(mProfileSpecRef.getComponentID());
			}
			ProfileSpecificationID[] profileSpecs = profileSpecSet.toArray(new ProfileSpecificationID[profileSpecSet.size()]);
			specsDescriptor = new javax.slee.resource.ResourceAdaptorDescriptor(getResourceAdaptorID(), getDeployableUnit().getDeployableUnitID(), getDeploymentUnitSource(), libraryIDs, raTypeIDs, profileSpecs,
					getDescriptor().getSupportsActiveReconfiguration());
		}
		return specsDescriptor;
	}

	@Override
	public ComponentDescriptor getComponentDescriptor() {
		return getSpecsDescriptor();
	}

	/**
	 * Creates an instance of the {@link ConfigProperties} for this component
	 * 
	 * @return
	 */
	public ConfigProperties getDefaultConfigPropertiesInstance() {
		ConfigProperties defaultProperties = new ConfigProperties();
		for (ConfigPropertyDescriptor mConfigProperty : getDescriptor().getConfigProperties()) {
			Object configPropertyValue = mConfigProperty.getConfigPropertyValue() == null ? null : ConfigProperties.Property.toObject(mConfigProperty.getConfigPropertyType(), mConfigProperty
					.getConfigPropertyValue());
			defaultProperties.addProperty(new ConfigProperties.Property(mConfigProperty.getConfigPropertyName(), mConfigProperty.getConfigPropertyType(), configPropertyValue));
		}
		return defaultProperties;
	}

	@Override
	public void processSecurityPermissions() throws DeploymentException {
		try {
			if (this.descriptor.getSecurityPermissions() != null) {
				super.permissions.add(new PermissionHolderImpl(super.getDeploymentDir().toURI(), this.descriptor.getSecurityPermissions()));
			}
		} catch (Exception e) {
			throw new DeploymentException("Failed to make permissions usable.", e);
		}
	}
	
	@Override
	public void undeployed() {
		super.undeployed();
		specsDescriptor = null;
		resourceAdaptorClass = null;
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.container.component.AbstractSleeComponentWithUsageParametersInterface#getUsageParametersList()
	 */
	@Override
	public List<UsageParameterDescriptor> getUsageParametersList() {
		return descriptor.getResourceAdaptorUsageParametersInterface().getUsageParameter();
	}
}
