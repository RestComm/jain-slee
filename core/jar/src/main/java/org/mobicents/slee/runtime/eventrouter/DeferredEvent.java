package org.mobicents.slee.runtime.eventrouter;

import javax.slee.ActivityContextInterface;
import javax.slee.Address;
import javax.slee.EventTypeID;
import javax.slee.resource.EventFlags;
import javax.slee.resource.FailureReason;
import javax.slee.resource.ReceivableService;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.runtime.activity.ActivityContext;
import org.mobicents.slee.runtime.activity.ActivityContextHandle;
import org.mobicents.slee.runtime.activity.ActivityType;

/**
 * A differed event. When an SBB posts an event, it winds up as one of these. When the tx commits, it actually makes it
 * into the event queue.
 * 
 * @author M. Ranganathan
 * @author Ivelin Ivanov (refactoring)
 * @author eduardomartins
 *
 */
public class DeferredEvent {
	
	private final SleeContainer sleeContainer;
	private final EventTypeID eventTypeId;
	private final String acId;
	private final ActivityContextHandle ach;
	private final Object event;
	private final Address address;
	private final ReceivableService receivableService;
	private final int eventFlags;
		
	/**
	 * the aci loaded for event routing, to be used in event handling rollbacks
	 */
	private ActivityContextInterface loadedAci;

	public DeferredEvent(EventTypeID eventTypeId, Object event,
			ActivityContext ac, Address address, SleeContainer sleeContainer) {
		this(eventTypeId,event,ac,address,null,EventFlags.NO_FLAGS,sleeContainer);		
	}
	
	public DeferredEvent(EventTypeID eventTypeId, Object event,
			ActivityContext ac, Address address, ReceivableService receivableService, int eventFlags,SleeContainer sleeContainer) {
		this.sleeContainer = sleeContainer;
		this.eventTypeId = eventTypeId;
		this.event = event;
		this.acId = ac.getActivityContextId();
		this.ach = ac.getActivityContextHandle();
		this.address = address;
		this.receivableService = receivableService;
		this.eventFlags = eventFlags;		
	}

	/**
	 * @return Returns the activity id.
	 */
	public String getActivityContextId() {
		return acId;
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
	
	public ReceivableService getReceivableService() {
		return receivableService;
	}
	
	public ActivityContextInterface getLoadedAci() {
		return loadedAci;
	}
	
	public void setLoadedAci(ActivityContextInterface aci) {
		this.loadedAci = aci;
	}
	
	// call backs
	
	public void eventProcessingSucceed() {
		if (EventFlags.hasRequestProcessingSuccessfulCallback(eventFlags) && ach.getActivityType() == ActivityType.externalActivity) {
			sleeContainer.getResourceManagement().getResourceAdaptorEntity(ach.getActivitySource()).eventProcessingSucceed(this);
		}
	}
	
	public void eventProcessingFailed(FailureReason failureReason) {
		if (EventFlags.hasRequestProcessingFailedCallback(eventFlags) && ach.getActivityType() == ActivityType.externalActivity) {
			sleeContainer.getResourceManagement().getResourceAdaptorEntity(ach.getActivitySource()).eventProcessingFailed(this, failureReason);
		}
	}
}
