/**
 * 
 */
package org.mobicents.slee.container.event;

import javax.slee.Address;
import javax.slee.EventTypeID;
import javax.slee.ServiceID;

import org.mobicents.slee.container.SleeContainerModule;
import org.mobicents.slee.container.activity.ActivityContext;

/**
 * @author martins
 * 
 */
public interface EventContextFactory extends SleeContainerModule {

	/**
	 * 
	 * @param ac
	 * @param unreferencedCallback
	 * @return
	 */
	public EventContext createActivityEndEventContext(ActivityContext ac,
			EventUnreferencedCallback unreferencedCallback);

	/**
	 * 
	 * @param eventTypeId
	 * @param eventObject
	 * @param ac
	 * @param address
	 * @param serviceID
	 * @param succeedCallback
	 * @param failedCallback
	 * @param unreferencedCallback
	 * @return
	 */
	public EventContext createEventContext(EventTypeID eventTypeId,
			Object eventObject, ActivityContext ac, Address address,
			ServiceID serviceID,
			EventProcessingSucceedCallback succeedCallback,
			EventProcessingFailedCallback failedCallback,
			EventUnreferencedCallback unreferencedCallback);

	/**
	 * 
	 * @param eventTypeId
	 * @param eventObject
	 * @param ac
	 * @param address
	 * @param serviceID
	 * @param referencesHandler
	 * @return
	 */
	public EventContext createEventContext(EventTypeID eventTypeId,
			Object eventObject, ActivityContext ac, Address address,
			ServiceID serviceID,ReferencesHandler referencesHandler);
	
	/**
	 * 
	 * @param handle
	 * @return
	 */
	public EventContext getEventContext(EventContextHandle handle);

}
