/**
 * Start time:17:44:15 2009-01-25<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.slee.EventTypeID;
import javax.slee.SbbID;
import javax.slee.management.DeployableUnitID;
import javax.slee.management.LibraryID;
import javax.slee.profile.ProfileSpecificationID;
import javax.slee.resource.ResourceAdaptorID;
import javax.slee.resource.ResourceAdaptorTypeID;

import org.mobicents.slee.container.component.deployment.jaxb.descriptors.DeployableUnitDescriptorImpl;

/**
 * Start time:17:44:15 2009-01-25<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class DeployableUnit {

	/**
	 * the DU id
	 */
	private final DeployableUnitID id;
	
	/**
	 * the DU descriptor
	 */
	private final DeployableUnitDescriptorImpl descriptor;
	
	/**
	 * the DU repository, provides an extended view of the container's component repository
	 */
	private final DeployableUnitRepository repository;

	/**
	 * the temp dir where the DU is installed
	 */
	private final File deploymentDir;
	
	/**
	 * the DU event type components
	 */
	private final Map<EventTypeID,EventTypeComponent> eventTypeComponents = new HashMap<EventTypeID,EventTypeComponent>();
	
	/**
	 * the DU library components
	 */
	private final Map<LibraryID,LibraryComponent> libraryComponents = new HashMap<LibraryID,LibraryComponent>();
	
	/**
	 * the DU profile spec components
	 */
	private final Map<ProfileSpecificationID,ProfileSpecificationComponent> profileSpecificationComponents = new HashMap<ProfileSpecificationID,ProfileSpecificationComponent>();
	
	/**
	 * the DU ra components
	 */
	private final Map<ResourceAdaptorID,ResourceAdaptorComponent> resourceAdaptorComponents = new HashMap<ResourceAdaptorID,ResourceAdaptorComponent>();
	
	/**
	 * the DU ratype components
	 */
	private final Map<ResourceAdaptorTypeID,ResourceAdaptorTypeComponent> resourceAdaptorTypeComponents = new HashMap<ResourceAdaptorTypeID,ResourceAdaptorTypeComponent>();
	
	/**
	 * the DU sbb components
	 */
	private final Map<SbbID, SbbComponent> sbbComponents = new HashMap<SbbID,SbbComponent>();

	public DeployableUnit(DeployableUnitID deployableUnitID,
			DeployableUnitDescriptorImpl duDescriptor,
			ComponentRepository componentRepository,File deploymentDir) {
		if (deployableUnitID == null) {
			throw new NullPointerException("null deployableUnitID");
		}
		if (duDescriptor == null) {
			throw new NullPointerException("null duDescriptor");
		}
		if (componentRepository == null) {
			throw new NullPointerException("null componentRepository");
		}
		if (deploymentDir == null) {
			throw new NullPointerException("null deploymentDir");
		}
		this.id = deployableUnitID;
		this.descriptor = duDescriptor;
		this.repository = new DeployableUnitRepository(this,componentRepository);
		this.deploymentDir = deploymentDir;
	}

	/**
	 * Retrieves the DU descriptor
	 * @return
	 */
	public DeployableUnitDescriptorImpl getDeployableUnitDescriptor() {
		return descriptor;
	}

	/**
	 * Retrieves the DU id
	 * @return
	 */
	public DeployableUnitID getDeployableUnitID() {
		return id;
	}

	/**
	 * Retrieves the DU component repository
	 * @return
	 */
	public DeployableUnitRepository getDeployableUnitRepository() {
		return repository;
	}

	/**
	 * Retrieves the temp dir where the DU is installed
	 * @return
	 */
	public File getDeploymentDir() {
		return deploymentDir;
	}
	
	/**
	 * Retrieves the DU event type components
	 * @return
	 */
	public Map<EventTypeID, EventTypeComponent> getEventTypeComponents() {
		return eventTypeComponents;
	}

	/**
	 * Retrieves the DU library components
	 * @return
	 */
	public Map<LibraryID, LibraryComponent> getLibraryComponents() {
		return libraryComponents;
	}

	/**
	 * Retrieves the DU profile spec components
	 * @return
	 */
	public Map<ProfileSpecificationID, ProfileSpecificationComponent> getProfileSpecificationComponents() {
		return profileSpecificationComponents;
	}

	/**
	 * Retrieves the DU ra components
	 * @return
	 */
	public Map<ResourceAdaptorID, ResourceAdaptorComponent> getResourceAdaptorComponents() {
		return resourceAdaptorComponents;
	}

	/**
	 * Retrieves the DU ratype components
	 * @return
	 */
	public Map<ResourceAdaptorTypeID, ResourceAdaptorTypeComponent> getResourceAdaptorTypeComponents() {
		return resourceAdaptorTypeComponents;
	}

	/**
	 * Retrieves the DU sbb components
	 * @return
	 */
	public Map<SbbID, SbbComponent> getSbbComponents() {
		return sbbComponents;
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj.getClass() == this.getClass()) {
			return ((DeployableUnit) obj).id
					.equals(this.id);
		} else {
			return false;
		}
	}
}
