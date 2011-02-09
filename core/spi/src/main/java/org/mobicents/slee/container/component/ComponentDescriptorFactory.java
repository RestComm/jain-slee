/**
 * 
 */
package org.mobicents.slee.container.component;

import org.mobicents.slee.container.component.du.DeployableUnitDescriptorFactory;
import org.mobicents.slee.container.component.event.EventTypeDescriptorFactory;
import org.mobicents.slee.container.component.library.LibraryDescriptorFactory;
import org.mobicents.slee.container.component.profile.ProfileSpecificationDescriptorFactory;
import org.mobicents.slee.container.component.ra.ResourceAdaptorDescriptorFactory;
import org.mobicents.slee.container.component.ratype.ResourceAdaptorTypeDescriptorFactory;
import org.mobicents.slee.container.component.sbb.SbbDescriptorFactory;
import org.mobicents.slee.container.component.service.ServiceDescriptorFactory;

/**
 * @author martins
 *
 */
public interface ComponentDescriptorFactory {

	/**
	 * 
	 * @return
	 */
	public DeployableUnitDescriptorFactory getDeployableUnitDescriptorFactory();
	
	/**
	 * 
	 * @return
	 */
	public EventTypeDescriptorFactory getEventTypeDescriptorFactory();
	
	/**
	 * 
	 * @return
	 */
	public LibraryDescriptorFactory getLibraryDescriptorFactory();
	
	/**
	 * 
	 * @return
	 */
	public ProfileSpecificationDescriptorFactory getProfileSpecificationDescriptorFactory();
	
	/**
	 * 
	 * @return
	 */
	public ResourceAdaptorDescriptorFactory getResourceAdaptorDescriptorFactory();
	
	/**
	 * 
	 * @return
	 */
	public ResourceAdaptorTypeDescriptorFactory getResourceAdaptorTypeDescriptorFactory();
	
	/**
	 * 
	 * @return
	 */
	public SbbDescriptorFactory getSbbDescriptorFactory();
	
	/**
	 * 
	 * @return
	 */
	public ServiceDescriptorFactory getServiceDescriptorFactory();

}
