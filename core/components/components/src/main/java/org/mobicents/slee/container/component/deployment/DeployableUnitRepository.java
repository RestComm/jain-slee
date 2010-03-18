package org.mobicents.slee.container.component.deployment;

import javax.slee.ComponentID;
import javax.slee.EventTypeID;
import javax.slee.SbbID;
import javax.slee.ServiceID;
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


/**
 * Extends the component repository, providing access also to the components in a DeployableUnit, those components are not installed yet
 * @author martins
 *
 */
public class DeployableUnitRepository implements ComponentRepository {

	private final DeployableUnit deployableUnit;
	private final ComponentRepository componentRepository;
	
	public DeployableUnitRepository(DeployableUnit deployableUnit,ComponentRepository componentRepository) {
		if (deployableUnit == null) {
			throw new NullPointerException("null deployableUnit");
		}
		if (componentRepository == null) {
			throw new NullPointerException("null componentRepository");
		}
		this.deployableUnit = deployableUnit;
		this.componentRepository = componentRepository;
	}

	public EventTypeComponent getComponentByID(EventTypeID id) {
		// get from repository
		EventTypeComponent component = componentRepository.getComponentByID(id);
		if (component == null) {
			// not found in repository, get it from deployable unit
			component = deployableUnit.getEventTypeComponents().get(id);
		}
		return component;
	}

	public ProfileSpecificationComponent getComponentByID(
			ProfileSpecificationID id) {
		// get from repository
		ProfileSpecificationComponent component = componentRepository.getComponentByID(id);
		if (component == null) {
			// not found in repository, get it from deployable unit
			component = deployableUnit.getProfileSpecificationComponents().get(id);
		}
		return component;
	}

	public LibraryComponent getComponentByID(LibraryID id) {
		// get from repository
		LibraryComponent component = componentRepository.getComponentByID(id);
		if (component == null) {
			// not found in repository, get it from deployable unit
			component = deployableUnit.getLibraryComponents().get(id);
		}
		return component;
	}

	public ResourceAdaptorComponent getComponentByID(ResourceAdaptorID id) {
		// get from repository
		ResourceAdaptorComponent component = componentRepository.getComponentByID(id);
		if (component == null) {
			// not found in repository, get it from deployable unit
			component = deployableUnit.getResourceAdaptorComponents().get(id);
		}
		return component;
	}

	public ResourceAdaptorTypeComponent getComponentByID(
			ResourceAdaptorTypeID id) {
		// get from repository
		ResourceAdaptorTypeComponent component = componentRepository.getComponentByID(id);
		if (component == null) {
			// not found in repository, get it from deployable unit
			component = deployableUnit.getResourceAdaptorTypeComponents().get(id);
		}
		return component;
	}

	public SbbComponent getComponentByID(SbbID id) {
		// get from repository
		SbbComponent component = componentRepository.getComponentByID(id);
		if (component == null) {
			// not found in repository, get it from deployable unit
			component = deployableUnit.getSbbComponents().get(id);
		}
		return component;
	}
	
	public ServiceComponent getComponentByID(ServiceID id) {
		// get from repository
		ServiceComponent component = componentRepository.getComponentByID(id);
		if (component == null) {
			// not found in repository, get it from deployable unit
			component = deployableUnit.getServiceComponents().get(id);
		}
		return component;
	}
	
	public boolean isInstalled(ComponentID componentID) {
		return componentRepository.isInstalled(componentID);
	}
}
