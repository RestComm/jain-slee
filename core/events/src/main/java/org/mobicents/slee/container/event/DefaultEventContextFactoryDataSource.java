/**
 * 
 */
package org.mobicents.slee.container.event;

import java.util.concurrent.ConcurrentHashMap;

import javax.slee.Address;
import javax.slee.EventTypeID;
import javax.slee.ServiceID;

import org.mobicents.slee.container.activity.ActivityContext;

/**
 * @author martins
 *
 */
public class DefaultEventContextFactoryDataSource implements EventContextFactoryDataSource {

	private final ConcurrentHashMap<EventContextHandle, EventContext> dataSource = new ConcurrentHashMap<EventContextHandle, EventContext>();
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.container.event.EventContextFactoryDataSource#addEventContext(org.mobicents.slee.container.event.EventContextHandle, org.mobicents.slee.container.event.EventContext)
	 */
	public void addEventContext(EventContextHandle handle,
			EventContext eventContext) {
		dataSource.put(handle, eventContext);
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.container.event.EventContextFactoryDataSource#getEventContext(org.mobicents.slee.container.event.EventContextHandle)
	 */
	public EventContext getEventContext(EventContextHandle handle) {
		return dataSource.get(handle);
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.container.event.EventContextFactoryDataSource#newEventContextData(javax.slee.EventTypeID, java.lang.Object, org.mobicents.slee.container.activity.ActivityContext, javax.slee.Address, javax.slee.ServiceID, org.mobicents.slee.container.event.EventProcessingSucceedCallback, org.mobicents.slee.container.event.EventProcessingFailedCallback, org.mobicents.slee.container.event.EventUnreferencedCallback, org.mobicents.slee.container.event.EventReferencesHandler)
	 */
	public EventContextData newEventContextData(EventTypeID eventTypeId,
			Object event, ActivityContext ac, Address address,
			ServiceID serviceID,
			EventProcessingSucceedCallback succeedCallback,
			EventProcessingFailedCallback failedCallback,
			EventUnreferencedCallback unreferencedCallback,
			ReferencesHandler referencesHandler) {
		return new DefaultEventContextData(eventTypeId, event, ac, address, serviceID, succeedCallback, failedCallback, unreferencedCallback, referencesHandler);
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.container.event.EventContextFactoryDataSource#removeEventContext(org.mobicents.slee.container.event.EventContextHandle)
	 */
	public void removeEventContext(EventContextHandle handle) {
		dataSource.remove(handle);
	}
	
	@Override
	public String toString() {
		return "DefaultEventContextFactoryDataSource[ "+dataSource.keySet()+" ]";
	}
	
}
