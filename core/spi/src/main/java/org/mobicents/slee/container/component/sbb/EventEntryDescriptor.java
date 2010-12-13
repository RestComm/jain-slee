/**
 * 
 */
package org.mobicents.slee.container.component.sbb;

import javax.slee.EventTypeID;

/**
 * @author martins
 *
 */
public interface EventEntryDescriptor {

	/**
	 * 
	 * @return
	 */
	public InitialEventSelectorVariables getInitialEventSelectVariables();

	/**
	 * 
	 * @return
	 */
	public String getInitialEventSelectorMethod();

	/**
	 * 
	 * @return
	 */
	public String getResourceOption();

	/**
	 * 
	 * @return true if the event direction is Receive or FireAndReceive
	 */
	public boolean isReceived();

	/**
	 * 
	 * @return true if the event direction is Fire or FireAndReceive
	 */
	public boolean isFired();

	/**
	 * @return
	 */
	public boolean isInitialEvent();

	/**
	 * @return
	 */
	public boolean isMaskOnAttach();

	/**
	 * @return
	 */
	public EventTypeID getEventReference();

	/**
	 * @return
	 */
	public EventDirection getEventDirection();

	/**
	 * @return
	 */
	public String getEventName();

}
