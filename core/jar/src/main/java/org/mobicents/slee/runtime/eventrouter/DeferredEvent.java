package org.mobicents.slee.runtime.eventrouter;

import javax.slee.Address;
import javax.slee.EventTypeID;
import javax.slee.ServiceID;
import javax.slee.resource.EventFlags;
import javax.slee.resource.FailureReason;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.runtime.activity.ActivityContext;
import org.mobicents.slee.runtime.activity.ActivityContextHandle;
import org.mobicents.slee.runtime.activity.ActivityType;

/**
 * A differed event. When an SBB posts an event, it winds up as one of these.
 * When the tx commits, it actually makes it into the event queue.
 * 
 * @author M. Ranganathan
 * @author Ivelin Ivanov (refactoring)
 * @author eduardomartins
 * 
 */
public class DeferredEvent {

	private final SleeContainer sleeContainer;
	private final EventRouterActivity era;
	private final EventTypeID eventTypeId;
	private final ActivityContextHandle ach;
	private final Object event;
	private final Address address;
	private final ServiceID serviceID;
	private final int eventFlags;

	public DeferredEvent(EventTypeID eventTypeId, Object event,
			ActivityContext ac, Address address, ServiceID serviceID,
			int eventFlags, EventRouterActivity era, SleeContainer sleeContainer) {
		this.sleeContainer = sleeContainer;
		this.era = era;
		this.eventTypeId = eventTypeId;
		this.event = event;
		this.ach = ac.getActivityContextHandle();
		this.address = address;
		this.serviceID = serviceID;
		this.eventFlags = eventFlags;
	}

	public ActivityContextHandle getActivityContextHandle() {
		return ach;
	}

	/**
	 * @return Returns the address.
	 */
	public Address getAddress() {
		return address;
	}

	/**
	 * @return Returns the event.
	 */
	public Object getEvent() {
		return event;
	}

	/**
	 * @return Returns the eventTypeId.
	 */
	public EventTypeID getEventTypeId() {
		return eventTypeId;
	}

	public int getEventFlags() {
		return eventFlags;
	}

	public ServiceID getService() {
		return serviceID;
	}

	public EventRouterActivity getEventRouterActivity() {
		return era;
	}

	// call backs

	public void eventProcessingSucceed() {
		if (EventFlags.hasRequestProcessingSuccessfulCallback(eventFlags)
				&& ach.getActivityType() == ActivityType.RA) {
			sleeContainer.getResourceManagement().getResourceAdaptorEntity(
					ach.getActivitySource()).eventProcessingSucceed(this);
		}
	}

	public void eventProcessingFailed(FailureReason failureReason) {
		if (EventFlags.hasRequestProcessingFailedCallback(eventFlags)
				&& ach.getActivityType() == ActivityType.RA) {
			sleeContainer.getResourceManagement().getResourceAdaptorEntity(
					ach.getActivitySource()).eventProcessingFailed(this,
					failureReason);
		}
	}

	public void eventUnreferenced() {
		sleeContainer.getResourceManagement().getResourceAdaptorEntity(
				ach.getActivitySource()).eventUnreferenced(this);
		
	}
	
	@Override
	public String toString() {		
		return "DeferredEvent[ eventTypeId = "+eventTypeId+" , event = "+event+" , ach = "+ach+" , address = "+address+" , serviceID = "+serviceID+" , eventFlags = "+EventFlags.toString(eventFlags)+" ]";
	}
	
}
