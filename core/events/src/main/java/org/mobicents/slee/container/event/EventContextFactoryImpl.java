/**
 * 
 */
package org.mobicents.slee.container.event;

import javax.slee.Address;
import javax.slee.EventTypeID;
import javax.slee.ServiceID;

import org.mobicents.slee.container.AbstractSleeContainerModule;
import org.mobicents.slee.container.activity.ActivityContext;

/**
 * @author martins
 * 
 */
public class EventContextFactoryImpl extends AbstractSleeContainerModule
		implements EventContextFactory {

	private final EventContextFactoryDataSource dataSource;

	/**
	 * @param dataSource
	 */
	public EventContextFactoryImpl(EventContextFactoryDataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.slee.container.event.EventContextFactory#
	 * createActivityEndEventContext
	 * (org.mobicents.slee.container.activity.ActivityContext,
	 * org.mobicents.slee.container.event.EventUnreferencedCallback)
	 */
	public EventContext createActivityEndEventContext(ActivityContext ac,
			EventUnreferencedCallback unreferencedCallback) {
		final EventReferencesHandlerImpl referencesHandler = new EventReferencesHandlerImpl();
		final EventContextData data = dataSource.newEventContextData(
				ActivityEndEventImpl.EVENT_TYPE_ID,
				ActivityEndEventImpl.SINGLETON, ac, null, null, null, null,
				unreferencedCallback,referencesHandler);
		final EventContextImpl eventContext = new ActivityEndEventContextImpl(data, this);
		referencesHandler.setEventContext(eventContext);
		return eventContext; 
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.container.event.EventContextFactory#createEventContext(javax.slee.EventTypeID, java.lang.Object, org.mobicents.slee.container.activity.ActivityContext, javax.slee.Address, javax.slee.ServiceID, org.mobicents.slee.container.event.EventProcessingSucceedCallback, org.mobicents.slee.container.event.EventProcessingFailedCallback, org.mobicents.slee.container.event.EventUnreferencedCallback)
	 */
	public EventContext createEventContext(EventTypeID eventTypeId,
			Object eventObject, ActivityContext ac, Address address,
			ServiceID serviceID,
			EventProcessingSucceedCallback succeedCallback,
			EventProcessingFailedCallback failedCallback,
			EventUnreferencedCallback unreferencedCallback) {
		final EventReferencesHandlerImpl referencesHandler = new EventReferencesHandlerImpl();
		final EventContextData data = dataSource.newEventContextData(
				eventTypeId, eventObject, ac, address, serviceID,
				succeedCallback, failedCallback, unreferencedCallback,
				referencesHandler);
		final EventContextImpl eventContext = new EventContextImpl(data, this);
		referencesHandler.setEventContext(eventContext);
		return eventContext;
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.container.event.EventContextFactory#createEventContext(javax.slee.EventTypeID, java.lang.Object, org.mobicents.slee.container.activity.ActivityContext, javax.slee.Address, javax.slee.ServiceID, org.mobicents.slee.container.event.ReferencesHandler)
	 */
	public EventContext createEventContext(EventTypeID eventTypeId,
			Object eventObject, ActivityContext ac, Address address,
			ServiceID serviceID, ReferencesHandler referencesHandler) {
		final EventContextData data = dataSource.newEventContextData(
				eventTypeId, eventObject, ac, address, serviceID,
				null, null, null,referencesHandler);
		return new EventContextImpl(data, this);
	}
	
	/**
	 * @return the dataSource
	 */
	public EventContextFactoryDataSource getDataSource() {
		return dataSource;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.container.event.EventContextFactory#getEventContext
	 * (org.mobicents.slee.container.event.EventContextHandle)
	 */
	public EventContext getEventContext(EventContextHandle handle) {
		return dataSource.getEventContext(handle);
	}

}
