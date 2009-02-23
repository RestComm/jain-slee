/**
 * Start time:17:54:46 2009-01-30<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component;

import javax.slee.ComponentID;
import javax.slee.EventTypeID;
import javax.slee.SbbID;
import javax.slee.ServiceID;
import javax.slee.management.LibraryID;
import javax.slee.profile.ProfileSpecificationID;
import javax.slee.resource.ResourceAdaptorID;
import javax.slee.resource.ResourceAdaptorTypeID;

/**
 * Start time:17:54:46 2009-01-30<br>
 * Project: mobicents-jainslee-server-core<br>
 * It provides elements which atleast passed referential constraints
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public interface ComponentRepository {

	/**
	 * Retrieves the {@link EventTypeComponent} associated with the specified id;
	 * @param id
	 * @return null if no such component exists
	 */
	public EventTypeComponent getComponentByID(EventTypeID id);
	
	/**
	 * Retrieves the {@link ProfileSpecificationComponent} associated with the specified id;
	 * @param id
	 * @return null if no such component exists
	 */
	public ProfileSpecificationComponent getComponentByID(ProfileSpecificationID id);
	
	/**
	 * Retrieves the {@link LibraryComponent} associated with the specified id;
	 * @param id
	 * @return null if no such component exists
	 */
	public LibraryComponent getComponentByID(LibraryID id);
	
	/**
	 * Retrieves the {@link ResourceAdaptorComponent} associated with the specified id;
	 * @param id
	 * @return null if no such component exists
	 */
	public ResourceAdaptorComponent getComponentByID(ResourceAdaptorID id);
	
	/**
	 * Retrieves the {@link ResourceAdaptorTypeComponent} associated with the specified id;
	 * @param id
	 * @return null if no such component exists
	 */
	public ResourceAdaptorTypeComponent getComponentByID(ResourceAdaptorTypeID id);
	
	/**
	 * Retrieves the {@link SbbComponent} associated with the specified id;
	 * @param id
	 * @return null if no such component exists
	 */
	public SbbComponent getComponentByID(SbbID id);

	/**
	 * Retrieves the {@link ServiceComponent} associated with the specified id
	 * @param id
	 * @return null if no such component exists
	 */
	public ServiceComponent getComponentByID(ServiceID id);

	/**
	 * 
	 * @param componentID
	 * @return true if a component with the specified id is in the repository, false otherwise
	 */
	public boolean isInstalled(ComponentID componentID);
}
