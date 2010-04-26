/**
 * 
 */
package org.mobicents.slee.container.event;

import javax.slee.Address;
import javax.slee.EventTypeID;
import javax.slee.ServiceID;

import org.mobicents.slee.container.activity.ActivityContext;

/**
 * @author martins
 *
 */
public interface EventContextFactoryDataSource {

	/**
	 * 
	 * @param handle
	 * @param eventContext
	 */
	public void addEventContext(EventContextHandle handle, EventContext eventContext);
	
	/**
	 * 
	 * @param handle
	 * @return
	 */
	public EventContext getEventContext(EventContextHandle handle);
	
	/**
	 * 
	 * @param handle
	 */
	public void removeEventContext(EventContextHandle handle);

	/**
	 * 
	 * @param eventTypeId
	 * @param event
	 * @param ac
	 * @param address
	 * @param serviceID
	 * @param succeedCallback
	 * @param failedCallback
	 * @param unreferencedCallback
	 * @param referencesHandler
	 * @return
	 */
	public EventContextData newEventContextData(EventTypeID eventTypeId, Object event,
			ActivityContext ac, Address address, ServiceID serviceID,
			EventProcessingSucceedCallback succeedCallback,
			EventProcessingFailedCallback failedCallback,
			EventUnreferencedCallback unreferencedCallback,
			ReferencesHandler referencesHandler);
}
