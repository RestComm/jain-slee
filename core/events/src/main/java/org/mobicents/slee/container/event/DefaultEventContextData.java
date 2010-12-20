/**
 * 
 */
package org.mobicents.slee.container.event;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import javax.slee.Address;
import javax.slee.EventTypeID;
import javax.slee.ServiceID;

import org.mobicents.slee.container.activity.ActivityContext;
import org.mobicents.slee.container.activity.LocalActivityContext;
import org.mobicents.slee.container.component.service.ServiceComponent;
import org.mobicents.slee.container.sbbentity.SbbEntityID;

/**
 * @author martins
 * 
 */
public class DefaultEventContextData implements EventContextData {

	private final EventTypeID eventTypeId;
	private final ServiceID serviceID;
	private final Object eventObject;
	private final Address address;
	
	private LocalActivityContext localActivityContext;
	private EventUnreferencedCallback unreferencedCallback;
	private EventProcessingSucceedCallback succeedCallback;
	private EventProcessingFailedCallback failedCallback;
	private ReferencesHandler referencesHandler;
	
	/**
	 * a queue of {@link EventContext}s barried due to this context become
	 * suspended
	 */
	private LinkedList<EventContext> barriedEvents;

	/**
	 * the set containing all sbb entities that handled the event so far
	 */
	private Set<SbbEntityID> sbbEntitiesThatHandledEvent;

	/**
	 * the ordered list containing all active services that will process this
	 * event as initial
	 */
	private LinkedList<ServiceComponent> activeServicesToProcessEventAsInitial;

	public DefaultEventContextData(EventTypeID eventTypeId, Object event,
			ActivityContext ac, Address address, ServiceID serviceID,
			EventProcessingSucceedCallback succeedCallback,
			EventProcessingFailedCallback failedCallback,
			EventUnreferencedCallback unreferencedCallback,
			ReferencesHandler referencesHandler) {
		this.eventTypeId = eventTypeId;
		this.eventObject = event;
		this.localActivityContext = ac.getLocalActivityContext();
		this.address = address;
		this.serviceID = serviceID;
		this.succeedCallback = succeedCallback;
		this.failedCallback = failedCallback;
		this.unreferencedCallback = unreferencedCallback;
		this.referencesHandler = referencesHandler;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.container.event.EventContextData#barrierEvent(org.
	 * mobicents.slee.container.event.EventContext)
	 */
	public void barrierEvent(EventContext eventContext) {
		if (barriedEvents == null) {
			barriedEvents = new LinkedList<EventContext>();
		}
		barriedEvents.add(eventContext);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.slee.container.event.EventContextData#
	 * getActiveServicesToProcessEventAsInitial()
	 */
	public LinkedList<ServiceComponent> getActiveServicesToProcessEventAsInitial() {
		if (activeServicesToProcessEventAsInitial == null) {
			activeServicesToProcessEventAsInitial = new LinkedList<ServiceComponent>();
		}
		return activeServicesToProcessEventAsInitial;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.slee.container.event.EventContextData#getAddress()
	 */
	public Address getAddress() {
		return address;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.slee.container.event.EventContextData#getEventObject()
	 */
	public Object getEventObject() {
		return eventObject;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.slee.container.event.EventContextData#getEventTypeId()
	 */
	public EventTypeID getEventTypeId() {
		return eventTypeId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.container.event.EventContextData#getFailedCallback()
	 */
	public EventProcessingFailedCallback getFailedCallback() {
		return failedCallback;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.container.event.EventContextData#getLocalActivityContext
	 * ()
	 */
	public LocalActivityContext getLocalActivityContext() {
		return localActivityContext;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.container.event.EventContextData#getReferencesHandler()
	 */
	public ReferencesHandler getReferencesHandler() {
		return referencesHandler;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.slee.container.event.EventContextData#
	 * getSbbEntitiesThatHandledEvent()
	 */
	public Set<SbbEntityID> getSbbEntitiesThatHandledEvent() {
		if (sbbEntitiesThatHandledEvent == null) {
			sbbEntitiesThatHandledEvent = new HashSet<SbbEntityID>();
		}
		return sbbEntitiesThatHandledEvent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.slee.container.event.EventContextData#getService()
	 */
	public ServiceID getService() {
		return serviceID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.container.event.EventContextData#getSucceedCallback()
	 */
	public EventProcessingSucceedCallback getSucceedCallback() {
		return succeedCallback;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.container.event.EventContextData#getUnreferencedCallback
	 * ()
	 */
	public EventUnreferencedCallback getUnreferencedCallback() {
		return unreferencedCallback;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.container.event.EventContextData#removeEventsBarried()
	 */
	public EventContext[] removeEventsBarried() {
		// this is safe because adding events and calling this is ever done by
		// same executor/thread
		final EventContext[] result = barriedEvents
				.toArray(new EventContext[barriedEvents.size()]);
		barriedEvents = null;
		return result;
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.container.event.EventContextData#unsetFailedCallback()
	 */
	public void unsetFailedCallback() {
		this.failedCallback = null;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return new StringBuilder("event type id = ").append(
				eventTypeId).append(" , event = ").append(eventObject).append(
				" , local ac = ").append(localActivityContext).append(" , address = ").append(
				address).append(" , serviceID = ").append(serviceID).toString();
	}
}
