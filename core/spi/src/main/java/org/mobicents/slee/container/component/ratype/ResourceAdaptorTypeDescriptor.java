/**
 * 
 */
package org.mobicents.slee.container.component.ratype;

import java.util.List;

import javax.slee.EventTypeID;
import javax.slee.resource.ResourceAdaptorTypeID;

import org.mobicents.slee.container.component.ComponentWithLibraryRefsDescriptor;

/**
 * @author martins
 * 
 */
public interface ResourceAdaptorTypeDescriptor extends ComponentWithLibraryRefsDescriptor {

	/**
	 * 
	 * @return
	 */
	public List<EventTypeID> getEventTypeRefs();

	/**
	 * 
	 * @return
	 */
	public List<String> getActivityTypes();

	/**
	 * 
	 * @return
	 */
	public String getActivityContextInterfaceFactoryInterface();

	/**
	 * 
	 * @return
	 */
	public String getResourceAdaptorInterface();

	/**
	 * 
	 * @return
	 */
	public ResourceAdaptorTypeID getResourceAdaptorTypeID();

}
