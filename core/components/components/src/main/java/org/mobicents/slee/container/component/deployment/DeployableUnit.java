/**
 * Start time:17:44:15 2009-01-25<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author martins
 */
package org.mobicents.slee.container.component.deployment;

import java.io.File;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.slee.ComponentID;
import javax.slee.EventTypeID;
import javax.slee.SbbID;
import javax.slee.ServiceID;
import javax.slee.management.DeployableUnitDescriptor;
import javax.slee.management.DeployableUnitID;
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
import org.mobicents.slee.container.component.SleeComponent;
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
	 * the DU repository, provides an extended view of the container's component
	 * repository
	 */
	private final DeployableUnitRepository repository;

	/**
	 * the temp dir where the DU is installed
	 */
	private final File deploymentDir;

	/**
	 * the DU event type components
	 */
	private final Map<EventTypeID, EventTypeComponent> eventTypeComponents = new HashMap<EventTypeID, EventTypeComponent>();

	/**
	 * the DU library components
	 */
	private final Map<LibraryID, LibraryComponent> libraryComponents = new HashMap<LibraryID, LibraryComponent>();

	/**
	 * the DU profile spec components
	 */
	private final Map<ProfileSpecificationID, ProfileSpecificationComponent> profileSpecificationComponents = new HashMap<ProfileSpecificationID, ProfileSpecificationComponent>();

	/**
	 * the DU ra components
	 */
	private final Map<ResourceAdaptorID, ResourceAdaptorComponent> resourceAdaptorComponents = new HashMap<ResourceAdaptorID, ResourceAdaptorComponent>();

	/**
	 * the DU ratype components
	 */
	private final Map<ResourceAdaptorTypeID, ResourceAdaptorTypeComponent> resourceAdaptorTypeComponents = new HashMap<ResourceAdaptorTypeID, ResourceAdaptorTypeComponent>();

	/**
	 * the DU sbb components
	 */
	private final Map<SbbID, SbbComponent> sbbComponents = new HashMap<SbbID, SbbComponent>();

	/**
	 * the DU service components
	 */
	private final Map<ServiceID, ServiceComponent> serviceComponents = new HashMap<ServiceID, ServiceComponent>();

	/**
	 * the date this deployable unit was built
	 */
	private final Date date = new Date();

	public DeployableUnit(DeployableUnitID deployableUnitID,
			DeployableUnitDescriptorImpl duDescriptor,
			ComponentRepository componentRepository, File deploymentDir) {
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
		this.repository = new DeployableUnitRepository(this,
				componentRepository);
		this.deploymentDir = deploymentDir;
	}

	/**
	 * Retrieves the DU descriptor
	 * 
	 * @return
	 */
	public DeployableUnitDescriptorImpl getDeployableUnitDescriptor() {
		return descriptor;
	}

	/**
	 * Retrieves the DU id
	 * 
	 * @return
	 */
	public DeployableUnitID getDeployableUnitID() {
		return id;
	}

	/**
	 * Retrieves the DU component repository
	 * 
	 * @return
	 */
	public DeployableUnitRepository getDeployableUnitRepository() {
		return repository;
	}

	/**
	 * Retrieves the temp dir where the DU is installed
	 * 
	 * @return
	 */
	public File getDeploymentDir() {
		return deploymentDir;
	}

	/**
	 * Retrieves the DU event type components
	 * 
	 * @return
	 */
	public Map<EventTypeID, EventTypeComponent> getEventTypeComponents() {
		return eventTypeComponents;
	}

	/**
	 * Retrieves the DU library components
	 * 
	 * @return
	 */
	public Map<LibraryID, LibraryComponent> getLibraryComponents() {
		return libraryComponents;
	}

	/**
	 * Retrieves the DU profile spec components
	 * 
	 * @return
	 */
	public Map<ProfileSpecificationID, ProfileSpecificationComponent> getProfileSpecificationComponents() {
		return profileSpecificationComponents;
	}

	/**
	 * Retrieves the DU ra components
	 * 
	 * @return
	 */
	public Map<ResourceAdaptorID, ResourceAdaptorComponent> getResourceAdaptorComponents() {
		return resourceAdaptorComponents;
	}

	/**
	 * Retrieves the DU ratype components
	 * 
	 * @return
	 */
	public Map<ResourceAdaptorTypeID, ResourceAdaptorTypeComponent> getResourceAdaptorTypeComponents() {
		return resourceAdaptorTypeComponents;
	}

	/**
	 * Retrieves the DU sbb components
	 * 
	 * @return
	 */
	public Map<SbbID, SbbComponent> getSbbComponents() {
		return sbbComponents;
	}

	/**
	 * Retrieves the DU service components
	 * 
	 * @return
	 */
	public Map<ServiceID, ServiceComponent> getServiceComponents() {
		return serviceComponents;
	}

	/**
	 * Returns an unmodifiable set with all {@link SleeComponent}s of the
	 * deployable unit.
	 * 
	 * @return
	 */
	public Set<SleeComponent> getDeployableUnitComponents() {
		Set<SleeComponent> result = new HashSet<SleeComponent>();
		result.addAll(getEventTypeComponents().values());
		result.addAll(getLibraryComponents().values());
		result.addAll(getProfileSpecificationComponents().values());
		result.addAll(getResourceAdaptorComponents().values());
		result.addAll(getResourceAdaptorTypeComponents().values());
		result.addAll(getSbbComponents().values());
		result.addAll(getServiceComponents().values());
		return Collections.unmodifiableSet(result);
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj.getClass() == this.getClass()) {
			return ((DeployableUnit) obj).id.equals(this.id);
		} else {
			return false;
		}
	}

	/**
	 * Undeploys this unit
	 */
	public void undeploy() {		
		// remove components
		for (SleeComponent component : getDeployableUnitComponents()) {
			component.undeployed();
		}
		// now delete the deployment dir
		deletePath(getDeploymentDir());
	}

	/**
	 * deletes the whole path, going through directories
	 * 
	 * @param path
	 */
	private void deletePath(File path) {
		if (path.isDirectory()) {
			for (File file : path.listFiles()) {
				deletePath(file);
			}
		}
		path.delete();
	}

	/**
	 * Returns the {@link DeployableUnitDescriptor} for this deployable unit.
	 * 
	 * @return
	 */
	public javax.slee.management.DeployableUnitDescriptor getSpecsDeployableUnitDescriptor() {
		Set<ComponentID> componentIDs = new HashSet<ComponentID>();
		for (SleeComponent component : getDeployableUnitComponents()) {
			componentIDs.add(component.getComponentID());
		}
		return new DeployableUnitDescriptor(getDeployableUnitID(), date,
				componentIDs.toArray(new ComponentID[0]));
	}
	
}
