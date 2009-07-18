package org.mobicents.slee.resource.sip11;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.slee.EventTypeID;
import javax.slee.ServiceID;
import javax.slee.resource.FireableEventType;
import javax.slee.resource.ReceivableService;
import javax.slee.resource.ReceivableService.ReceivableEvent;

public class EventIDFilter {

	/**
	 * Holds mappings eventTypeID --> Set(ServiceID) which are interested in receiving event
	 */
	private final ConcurrentHashMap<EventTypeID, Set<ServiceID>> eventID2serviceIDs = new ConcurrentHashMap<EventTypeID, Set<ServiceID>>(
			31);
	
	/**
	 * checks if event should be filtered or not
	 * @param eventType
	 * @return true is event is to be filtered, false otherwise
	 */
	public boolean filterEvent(FireableEventType eventType) {		
		return !eventID2serviceIDs.containsKey(eventType.getEventType());
	}

	/**
	 * Informs the filter that a receivable service is now active.
	 * For the events related with the service, and if there are no other
	 * services bound, then events of such event type should now not be filtered.
	 * 
	 * @param receivableService
	 */
	public void serviceActive(ReceivableService receivableService) {
		for (ReceivableEvent receivableEvent : receivableService.getReceivableEvents()) {
			Set<ServiceID> servicesReceivingEvent = eventID2serviceIDs.get(receivableEvent.getEventType());
			if (servicesReceivingEvent == null) {
				servicesReceivingEvent = new HashSet<ServiceID>();
				Set<ServiceID> anotherSet = eventID2serviceIDs.putIfAbsent(receivableEvent.getEventType(), servicesReceivingEvent);
				if (anotherSet != null) {
					servicesReceivingEvent = anotherSet;
				}
			}
			synchronized (servicesReceivingEvent) {
				servicesReceivingEvent.add(receivableService.getService());
			}
		}		
	}

	/**
	 * Informs the filter that a receivable service is now inactive.
	 * For the events related with the service, if there are no other
	 * services bound, then events of such event type should be filtered 
	 * 
	 * @param receivableService
	 */
	public void serviceInactive(ReceivableService receivableService) {
		for (ReceivableEvent receivableEvent : receivableService.getReceivableEvents()) {
			Set<ServiceID> servicesReceivingEvent = eventID2serviceIDs.get(receivableEvent.getEventType());
			if (servicesReceivingEvent != null) {
				synchronized (servicesReceivingEvent) {
					servicesReceivingEvent.remove(receivableService.getService());
				}
			}
		}		
	}

	/**
	 * Informs the filter that a receivable service is now stopping.
	 * @param receivableService
	 */
	public void serviceStopping(ReceivableService receivableService) {
		// do nothing		
	}

}
