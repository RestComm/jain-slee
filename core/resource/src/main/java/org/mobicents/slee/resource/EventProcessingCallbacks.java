/**
 * 
 */
package org.mobicents.slee.resource;

import javax.slee.Address;
import javax.slee.resource.ActivityHandle;
import javax.slee.resource.FailureReason;
import javax.slee.resource.FireableEventType;
import javax.slee.resource.ReceivableService;

import org.mobicents.slee.container.event.EventProcessingFailedCallback;
import org.mobicents.slee.container.event.EventProcessingSucceedCallback;
import org.mobicents.slee.container.event.EventUnreferencedCallback;
import org.mobicents.slee.container.resource.ResourceAdaptorEntity;

/**
 * @author martins
 * 
 */
public class EventProcessingCallbacks implements EventProcessingFailedCallback,
		EventProcessingSucceedCallback, EventUnreferencedCallback {

	private final int eventFlags;
	private final FireableEventType fireableEventType;
	private final ReceivableService receivableService;
	private final ResourceAdaptorEntity raEntity;
	private final Object event;
	private final Address address;
	private final ActivityHandle activityHandle;


	/**
	 * 
	 * @param activityHandle
	 * @param fireableEventType
	 * @param event
	 * @param address
	 * @param receivableService
	 * @param eventFlags
	 * @param raEntity
	 */
	public EventProcessingCallbacks(ActivityHandle activityHandle,
			FireableEventType fireableEventType, Object event, Address address,
			ReceivableService receivableService, int eventFlags,
			ResourceAdaptorEntity raEntity) {
		this.eventFlags = eventFlags;
		this.fireableEventType = fireableEventType;
		this.receivableService = receivableService;
		this.raEntity = raEntity;
		this.event = event;
		this.address = address;
		this.activityHandle = activityHandle;
	}

	
	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.slee.core.event.SleeEventProcessingFailedCallback#
	 * eventProcessingFailed(javax.slee.resource.FailureReason)
	 */
	public void eventProcessingFailed(FailureReason failureReason) {
		raEntity.getResourceAdaptorObject().eventProcessingFailed(activityHandle, fireableEventType,
				failureReason, address, receivableService, eventFlags,
				failureReason);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.slee.core.event.SleeEventProcessingSucceedCallback#
	 * eventProcessingSucceed()
	 */
	public void eventProcessingSucceed() {
		raEntity.getResourceAdaptorObject().eventProcessingSuccessful(activityHandle, fireableEventType,
				event, address, receivableService, eventFlags);
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.container.event.EventUnreferencedCallback#eventUnreferenced()
	 */
	public void eventUnreferenced() {
		raEntity.getResourceAdaptorObject().eventUnreferenced(activityHandle, fireableEventType,
				event, address, receivableService, eventFlags);
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.container.event.EventUnreferencedCallback#acceptsTransaction()
	 */
	public boolean requiresTransaction() {
		return false;
	}
}
