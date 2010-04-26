/**
 * 
 */
package org.mobicents.slee.container.event;

import java.util.LinkedList;
import java.util.Set;

import javax.slee.Address;
import javax.slee.EventTypeID;
import javax.slee.ServiceID;

import org.mobicents.slee.container.activity.LocalActivityContext;
import org.mobicents.slee.container.component.service.ServiceComponent;

/**
 * @author martins
 * 
 */
public interface EventContextData {

	/**
	 * 
	 * @param eventContext
	 */
	public void barrierEvent(EventContext eventContext);

	/**
	 * 
	 * @return
	 */
	public LinkedList<ServiceComponent> getActiveServicesToProcessEventAsInitial();

	/**
	 * @return Returns the address.
	 */
	public Address getAddress();

	/**
	 * @return Returns the event.
	 */
	public Object getEventObject();

	/**
	 * @return Returns the eventTypeId.
	 */
	public EventTypeID getEventTypeId();

	/**
	 * 
	 * @return
	 */
	public EventProcessingFailedCallback getFailedCallback();

	/**
	 * 
	 * @return
	 */
	public LocalActivityContext getLocalActivityContext();

	/**
	 * 
	 * @return
	 */
	public Set<String> getSbbEntitiesThatHandledEvent();

	/**
	 * 
	 * @return
	 */
	public ServiceID getService();

	/**
	 * 
	 * @return
	 */
	public EventProcessingSucceedCallback getSucceedCallback();

	/**
	 * 
	 * @return
	 */
	public EventUnreferencedCallback getUnreferencedCallback();

	/**
	 * 
	 * @return
	 */
	public ReferencesHandler getReferencesHandler();
	
	/**
	 * 
	 * @return
	 */
	public EventContext[] removeEventsBarried();

	/**
	 * @param object
	 */
	public void unsetFailedCallback();

}
