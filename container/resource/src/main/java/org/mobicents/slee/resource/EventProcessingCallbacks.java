/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2017, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

/**
 * 
 */
package org.mobicents.slee.resource;

import javax.slee.Address;
import javax.slee.resource.ActivityHandle;
import javax.slee.resource.EventFlags;
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
	public void eventProcessingSucceed(boolean sbbProcessedEvent) {
		int flags = sbbProcessedEvent ? EventFlags.setSbbProcessedEvent(eventFlags) : eventFlags;
		raEntity.getResourceAdaptorObject().eventProcessingSuccessful(activityHandle, fireableEventType,
				event, address, receivableService, flags);
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
