/**
 * 
 */
package org.mobicents.slee.container.component.du;

import java.io.File;
import java.util.Map;
import java.util.Set;

import javax.slee.EventTypeID;
import javax.slee.SbbID;
import javax.slee.ServiceID;
import javax.slee.management.DeployableUnitID;
import javax.slee.management.LibraryID;
import javax.slee.profile.ProfileSpecificationID;
import javax.slee.resource.ResourceAdaptorID;
import javax.slee.resource.ResourceAdaptorTypeID;

import org.mobicents.slee.container.component.SleeComponent;
import org.mobicents.slee.container.component.event.EventTypeComponent;
import org.mobicents.slee.container.component.library.LibraryComponent;
import org.mobicents.slee.container.component.profile.ProfileSpecificationComponent;
import org.mobicents.slee.container.component.ra.ResourceAdaptorComponent;
import org.mobicents.slee.container.component.ratype.ResourceAdaptorTypeComponent;
import org.mobicents.slee.container.component.sbb.SbbComponent;
import org.mobicents.slee.container.component.service.ServiceComponent;

/**
 * 
 * @author martins
 * 
 */
public interface DeployableUnit {

	/**
	 * Retrieves the DU descriptor
	 * 
	 * @return
	 */
	public DeployableUnitDescriptor getDeployableUnitDescriptor();

	/**
	 * Retrieves the DU id
	 * 
	 * @return
	 */
	public DeployableUnitID getDeployableUnitID();

	/**
	 * Retrieves the DU component repository
	 * 
	 * @return
	 */
	public DeployableUnitRepository getDeployableUnitRepository();

	/**
	 * Retrieves the temp dir where the DU is installed
	 * 
	 * @return
	 */
	public File getDeploymentDir();

	/**
	 * Retrieves the DU event type components
	 * 
	 * @return
	 */
	public Map<EventTypeID, EventTypeComponent> getEventTypeComponents();

	/**
	 * Retrieves the DU library components
	 * 
	 * @return
	 */
	public Map<LibraryID, LibraryComponent> getLibraryComponents();

	/**
	 * Retrieves the DU profile spec components
	 * 
	 * @return
	 */
	public Map<ProfileSpecificationID, ProfileSpecificationComponent> getProfileSpecificationComponents();

	/**
	 * Retrieves the DU ra components
	 * 
	 * @return
	 */
	public Map<ResourceAdaptorID, ResourceAdaptorComponent> getResourceAdaptorComponents();

	/**
	 * Retrieves the DU ratype components
	 * 
	 * @return
	 */
	public Map<ResourceAdaptorTypeID, ResourceAdaptorTypeComponent> getResourceAdaptorTypeComponents();

	/**
	 * Retrieves the DU sbb components
	 * 
	 * @return
	 */
	public Map<SbbID, SbbComponent> getSbbComponents();

	/**
	 * Retrieves the DU service components
	 * 
	 * @return
	 */
	public Map<ServiceID, ServiceComponent> getServiceComponents();

	/**
	 * Returns an unmodifiable set with all {@link SleeComponent}s of the
	 * deployable unit.
	 * 
	 * @return
	 */
	public Set<SleeComponent> getDeployableUnitComponents();

	/**
	 * Undeploys this unit
	 */
	public void undeploy();

	/**
	 * Returns the {@link DeployableUnitDescriptor} for this deployable unit.
	 * 
	 * @return
	 */
	public javax.slee.management.DeployableUnitDescriptor getSpecsDeployableUnitDescriptor();

}
