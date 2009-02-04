/**
 * Start time:17:54:46 2009-01-30<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component;

import javax.slee.EventTypeID;
import javax.slee.SbbID;
import javax.slee.management.LibraryID;
import javax.slee.profile.ProfileSpecificationID;
import javax.slee.resource.ResourceAdaptorID;
import javax.slee.resource.ResourceAdaptorTypeID;

/**
 * Start time:17:54:46 2009-01-30<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public interface ComponentRepository {

	
	//FIXME: possibly rename
	
	
	public SbbComponent getComponentByID(SbbID id);
	public EventTypeComponent getComponentByID(EventTypeID id);
	public ProfileSpecificationComponent getComponentByID(ProfileSpecificationID id);
	public LibraryComponent getComponentByID(LibraryID id);
	public ResourceAdaptorComponent getComponentByID(ResourceAdaptorID id);
	public ResourceAdaptorTypeComponent getComponentByID(ResourceAdaptorTypeID id);
	
}
