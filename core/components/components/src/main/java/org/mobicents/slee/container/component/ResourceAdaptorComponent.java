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
import java.util.Set;

import javax.slee.ComponentID;
import javax.slee.management.ComponentDescriptor;
import javax.slee.management.DependencyException;
import javax.slee.management.DeploymentException;
import javax.slee.management.LibraryID;
import javax.slee.profile.ProfileSpecificationID;
import javax.slee.resource.ConfigProperties;
import javax.slee.resource.ResourceAdaptorDescriptor;
import javax.slee.resource.ResourceAdaptorID;
import javax.slee.resource.ResourceAdaptorTypeID;

import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ResourceAdaptorDescriptorImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.references.MLibraryRef;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.references.MProfileSpecRef;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.references.MResourceAdaptorTypeRef;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ra.MConfigProperty;
import org.mobicents.slee.container.component.security.PermissionHolder;

/**
 * Start time:00:45:25 2009-02-04<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ResourceAdaptorComponent extends SleeComponentWithUsageParametersInterface {

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
	private ResourceAdaptorDescriptor specsDescriptor = null;

	/**
	 * 
	 * @param descriptor
	 */
	public ResourceAdaptorComponent(ResourceAdaptorDescriptorImpl descriptor) {
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
	boolean addToDeployableUnit() {
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
	public ResourceAdaptorDescriptor getSpecsDescriptor() {
		if (specsDescriptor == null) {
			Set<LibraryID> libraryIDSet = new HashSet<LibraryID>();
			for (MLibraryRef mLibraryRef : getDescriptor().getLibraryRefs()) {
				libraryIDSet.add(mLibraryRef.getComponentID());
			}
			LibraryID[] libraryIDs = libraryIDSet.toArray(new LibraryID[libraryIDSet.size()]);

			Set<ResourceAdaptorTypeID> raTypeIDSet = new HashSet<ResourceAdaptorTypeID>();
			for (MResourceAdaptorTypeRef mResourceAdaptorTypeRef : getDescriptor().getResourceAdaptorTypeRefs()) {
				raTypeIDSet.add(mResourceAdaptorTypeRef.getComponentID());
			}
			ResourceAdaptorTypeID[] raTypeIDs = raTypeIDSet.toArray(new ResourceAdaptorTypeID[raTypeIDSet.size()]);

			Set<ProfileSpecificationID> profileSpecSet = new HashSet<ProfileSpecificationID>();
			for (MProfileSpecRef mProfileSpecRef : getDescriptor().getProfileSpecRefs()) {
				profileSpecSet.add(mProfileSpecRef.getComponentID());
			}
			ProfileSpecificationID[] profileSpecs = profileSpecSet.toArray(new ProfileSpecificationID[profileSpecSet.size()]);

			specsDescriptor = new ResourceAdaptorDescriptor(getResourceAdaptorID(), getDeployableUnit().getDeployableUnitID(), getDeploymentUnitSource(), libraryIDs, raTypeIDs, profileSpecs,
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
		for (MConfigProperty mConfigProperty : getDescriptor().getConfigProperties()) {
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
				super.permissions.add(new PermissionHolder(super.getDeploymentDir().toURI(), this.descriptor.getSecurityPermissions().getSecurityPermissionSpec()));
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
}
