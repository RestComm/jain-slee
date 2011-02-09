/**
 * 
 */
package org.mobicents.slee.container.component.event;

import javax.slee.EventTypeID;

import org.mobicents.slee.container.component.ComponentWithLibraryRefsDescriptor;

/**
 * @author martins
 *
 */
public interface EventTypeDescriptor extends ComponentWithLibraryRefsDescriptor {

	/**
	 * 
	 * @return
	 */
	public EventTypeID getEventTypeID();

	/**
	 * 
	 * @return
	 */
	public String getEventClassName();

}
