/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.slee.resource.mgcp.ra;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.slee.EventTypeID;
import javax.slee.ServiceID;
import javax.slee.resource.FireableEventType;
import javax.slee.resource.ReceivableService;
import javax.slee.resource.ReceivableService.ReceivableEvent;

public class EventIDFilter {

	private final ConcurrentHashMap<EventTypeID, Set<ServiceID>> initialEvents = new ConcurrentHashMap<EventTypeID, Set<ServiceID>>(
			31);

	/**
	 * Holds mappings eventTypeID --> Set(ServiceID) which are interested in receiving event
	 */
	private final ConcurrentHashMap<EventTypeID, Set<ServiceID>> eventID2serviceIDs = new ConcurrentHashMap<EventTypeID, Set<ServiceID>>(
			31);

	/**
	 * checks if event should be filtered or not
	 * 
	 * @param eventType
	 * @return true is event is to be filtered, false otherwise
	 */
	public boolean filterEvent(FireableEventType eventType) {
		return !eventID2serviceIDs.containsKey(eventType.getEventType());
	}

	public boolean isInitialEvent(FireableEventType eventType) {
		return initialEvents.containsKey(eventType.getEventType());
	}

	/**
	 * Informs the filter that a receivable service is now active. For the events related with the service, and if there
	 * are no other services bound, then events of such event type should now not be filtered.
	 * 
	 * @param receivableService
	 */
	public void serviceActive(ReceivableService receivableService) {
		for (ReceivableEvent receivableEvent : receivableService.getReceivableEvents()) {
			Set<ServiceID> servicesReceivingEvent = eventID2serviceIDs.get(receivableEvent.getEventType());

			if (servicesReceivingEvent == null) {
				servicesReceivingEvent = new HashSet<ServiceID>();
				Set<ServiceID> anotherSet = eventID2serviceIDs.putIfAbsent(receivableEvent.getEventType(),
						servicesReceivingEvent);
				if (anotherSet != null) {
					servicesReceivingEvent = anotherSet;
				}
			}
			synchronized (servicesReceivingEvent) {
				servicesReceivingEvent.add(receivableService.getService());
			}

		}// end of for loop

		populateInitialEvent(receivableService);
	}

	private void populateInitialEvent(ReceivableService receivableService) {
		for (ReceivableEvent receivableEvent : receivableService.getReceivableEvents()) {
			if (receivableEvent.isInitialEvent()) {

				Set<ServiceID> servicesReceivingEvent = initialEvents.get(receivableEvent.getEventType());

				if (servicesReceivingEvent == null) {
					servicesReceivingEvent = new HashSet<ServiceID>();
					Set<ServiceID> anotherSet = initialEvents.putIfAbsent(receivableEvent.getEventType(),
							servicesReceivingEvent);
					if (anotherSet != null) {
						servicesReceivingEvent = anotherSet;
					}
				}
				synchronized (servicesReceivingEvent) {
					servicesReceivingEvent.add(receivableService.getService());
				}
			}

		}
	}

	/**
	 * Informs the filter that a receivable service is now inactive. For the events related with the service, if there
	 * are no other services bound, then events of such event type should be filtered
	 * 
	 * @param receivableService
	 */
	public void serviceInactive(ReceivableService receivableService) {
		for (ReceivableEvent receivableEvent : receivableService.getReceivableEvents()) {
			Set<ServiceID> servicesReceivingEvent = eventID2serviceIDs.get(receivableEvent.getEventType());
			if (servicesReceivingEvent != null) {
				synchronized (servicesReceivingEvent) {
					servicesReceivingEvent.remove(receivableService.getService());
					if (servicesReceivingEvent.size() == 0) {
						eventID2serviceIDs.remove(receivableEvent.getEventType());
					}
				}
			}
		}

		depopulateInitialEvent(receivableService);
	}

	private void depopulateInitialEvent(ReceivableService receivableService) {
		for (ReceivableEvent receivableEvent : receivableService.getReceivableEvents()) {

			if (receivableEvent.isInitialEvent()) {
				Set<ServiceID> servicesReceivingEvent = initialEvents.get(receivableEvent.getEventType());
				if (servicesReceivingEvent != null) {
					synchronized (servicesReceivingEvent) {
						servicesReceivingEvent.remove(receivableService.getService());
						if (servicesReceivingEvent.size() == 0) {
							initialEvents.remove(receivableEvent.getEventType());
						}
					}
				}
			}
		}
	}

	/**
	 * Informs the filter that a receivable service is now stopping.
	 * 
	 * @param receivableService
	 */
	public void serviceStopping(ReceivableService receivableService) {
		// do nothing
	}

}
