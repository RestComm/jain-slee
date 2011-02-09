/**
 * 
 */
package org.mobicents.slee.container.component;

import org.mobicents.slee.container.component.ComponentDescriptorFactory;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.DeployableUnitDescriptorFactoryImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.EventTypeDescriptorFactoryImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.LibraryDescriptorFactoryImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ProfileSpecificationDescriptorFactoryImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ResourceAdaptorDescriptorFactoryImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ResourceAdaptorTypeDescriptorFactoryImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.SbbDescriptorFactoryImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ServiceDescriptorFactoryImpl;

/**
 * @author martins
 * 
 */
public class ComponentDescriptorFactoryImpl implements
		ComponentDescriptorFactory {
	
	private final DeployableUnitDescriptorFactoryImpl deployableUnitDescriptorFactory;
	private final EventTypeDescriptorFactoryImpl eventTypeDescriptorFactory;
	private final LibraryDescriptorFactoryImpl libraryDescriptorFactory;
	private final ProfileSpecificationDescriptorFactoryImpl profileSpecificationDescriptorFactory;
	private final ResourceAdaptorDescriptorFactoryImpl resourceAdaptorDescriptorFactory;
	private final ResourceAdaptorTypeDescriptorFactoryImpl resourceAdaptorTypeDescriptorFactory;
	private final SbbDescriptorFactoryImpl sbbDescriptorFactory;
	private final ServiceDescriptorFactoryImpl serviceDescriptorFactory;

	/**
	 * 
	 */
	public ComponentDescriptorFactoryImpl() {
		this.deployableUnitDescriptorFactory = new DeployableUnitDescriptorFactoryImpl();
		this.eventTypeDescriptorFactory = new EventTypeDescriptorFactoryImpl();
		this.libraryDescriptorFactory = new LibraryDescriptorFactoryImpl();
		this.profileSpecificationDescriptorFactory = new ProfileSpecificationDescriptorFactoryImpl();
		this.resourceAdaptorDescriptorFactory = new ResourceAdaptorDescriptorFactoryImpl();
		this.resourceAdaptorTypeDescriptorFactory = new ResourceAdaptorTypeDescriptorFactoryImpl();
		this.sbbDescriptorFactory = new SbbDescriptorFactoryImpl();
		this.serviceDescriptorFactory = new ServiceDescriptorFactoryImpl();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.slee.core.component.ComponentDescriptorFactory#
	 * getDeployableUnitDescriptorFactory()
	 */
	public DeployableUnitDescriptorFactoryImpl getDeployableUnitDescriptorFactory() {
		return deployableUnitDescriptorFactory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.slee.core.component.ComponentDescriptorFactory#
	 * getEventTypeDescriptorFactory()
	 */
	public EventTypeDescriptorFactoryImpl getEventTypeDescriptorFactory() {
		return eventTypeDescriptorFactory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.slee.core.component.ComponentDescriptorFactory#
	 * getLibraryDescriptorFactory()
	 */
	public LibraryDescriptorFactoryImpl getLibraryDescriptorFactory() {
		return libraryDescriptorFactory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.slee.core.component.ComponentDescriptorFactory#
	 * getProfileSpecificationDescriptorFactory()
	 */
	public ProfileSpecificationDescriptorFactoryImpl getProfileSpecificationDescriptorFactory() {
		return profileSpecificationDescriptorFactory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.slee.core.component.ComponentDescriptorFactory#
	 * getResourceAdaptorDescriptorFactory()
	 */
	public ResourceAdaptorDescriptorFactoryImpl getResourceAdaptorDescriptorFactory() {
		return resourceAdaptorDescriptorFactory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.slee.core.component.ComponentDescriptorFactory#
	 * getResourceAdaptorTypeDescriptorFactory()
	 */
	public ResourceAdaptorTypeDescriptorFactoryImpl getResourceAdaptorTypeDescriptorFactory() {
		return resourceAdaptorTypeDescriptorFactory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.slee.core.component.ComponentDescriptorFactory#
	 * getSbbDescriptorFactory()
	 */
	public SbbDescriptorFactoryImpl getSbbDescriptorFactory() {
		return sbbDescriptorFactory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.slee.core.component.ComponentDescriptorFactory#
	 * getServiceDescriptorFactory()
	 */
	public ServiceDescriptorFactoryImpl getServiceDescriptorFactory() {
		return serviceDescriptorFactory;
	}

}
