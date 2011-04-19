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

package org.mobicents.slee.runtime.activity;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.slee.resource.FailureReason;
import javax.transaction.Transaction;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.activity.ActivityEventQueueManager;
import org.mobicents.slee.container.activity.LocalActivityContext;
import org.mobicents.slee.container.event.EventContext;
import org.mobicents.slee.container.util.concurrent.ConcurrentHashSet;

/**
 * 
 * Manages the queuing of events for a specific activity.
 * 
 * @author Eduardo Martins
 * 
 */

public class ActivityEventQueueManagerImpl implements ActivityEventQueueManager {

	private static final Logger logger = Logger
			.getLogger(ActivityEventQueueManagerImpl.class);

	/**
	 * stores the activity end event when set
	 */
	private EventContext activityEndEvent;

	/**
	 * atomic flag to define if the activity end event was queued
	 */
	private AtomicBoolean activityEndEventQueued = new AtomicBoolean(false);

	/**
	 * the set of pending events, i.e., events not committed yet, for this
	 * activity
	 */
	private ConcurrentHashSet<EventContext> pendingEvents = new ConcurrentHashSet<EventContext>();

	/**
	 * the events hold due to barriers set
	 */
	private ConcurrentLinkedQueue<EventContext> eventsBarriered = new ConcurrentLinkedQueue<EventContext>();
	
	/**
	 * the transactions that hold barriers to the activity event queue
	 */
	private ConcurrentHashSet<Transaction> eventBarriers = new ConcurrentHashSet<Transaction>();
	
	/**
	 * the local view of the related activity context
	 */
	private final LocalActivityContext localAC;

	private boolean doTraceLogs = logger.isTraceEnabled();
	
	public ActivityEventQueueManagerImpl(
			LocalActivityContext localAC) {
		this.localAC = localAC;
	}

	/**
	 * Indicates if there are pending events to be routed for the related
	 * activity.
	 * 
	 * @return
	 */
	public boolean noPendingEvents() {
		return pendingEvents.isEmpty();
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.runtime.activity.ActivityEventQueueManager#pending(org.mobicents.slee.core.event.SleeEvent)
	 */
	public void pending(EventContext event) {

		if (doTraceLogs) {
			logger.trace("Pending event of type " + event.getEventTypeId()
					+ " in AC with handle "
					+ event.getActivityContextHandle());
		}

		if (activityEndEvent == null) {
			// activity end event not set, we accept pending events
			pendingEvents.add(event);
			// manage event references
			event.getReferencesHandler().add(localAC.getActivityContextHandle());			
		} else {
			// processing of the event failed
			event.eventProcessingFailed(FailureReason.OTHER_REASON);			
		}
	}

	/**
	 * Signals the manager that the event was committed, and thus can be routed.
	 * 
	 * @param event
	 */
	public void commit(EventContext event) {
		commit(event,true);
	}
	
	/**
	 * Similar as doing pending() and commit() of an event in a single step.
	 * @param event
	 */
	public void fireNotTransacted(EventContext event) {
		// manage event references
		event.getReferencesHandler().add(localAC.getActivityContextHandle());
		commit(event,false);
	}
	
	private void commit(EventContext event, boolean isPendingEvent) {
		if (isPendingEvent) {
			if (!pendingEvents.remove(event)) {
				// processing of the event failed
				event.eventProcessingFailed(FailureReason.OTHER_REASON);			
				return;
			}
		}	
		if (eventBarriers.isEmpty()) {
			// barriers are not set, proceed with commit of event
			commitAndNotSuspended(event);
		}
		else {
			// barriers are set
			synchronized (eventBarriers) {
				if (!eventBarriers.isEmpty()) {
					// barriers are still set, add the event to the frozen queue
					eventsBarriered.add(event);
				}
				else {
					// barriers were removed, proceed with commit of event
					commitAndNotSuspended(event);
				}
			}
		}
	}

	private void commitAndNotSuspended(EventContext event) {

		if (doTraceLogs) {
			logger.trace("Commiting event of type " + event.getEventTypeId()
					+ " in AC with handle "
					+ event.getActivityContextHandle());
		}

		if (event.isActivityEndEvent()) {
			// store it
			activityEndEvent = event;
			// check we can route it
			if (pendingEvents.isEmpty()) {
				// no pending events
				if (activityEndEventQueued.compareAndSet(false, true)) {
					// between element removal and check if it's empty there
					// is no sync so we need to ensure only one thread
					// routes the activity end event
					localAC.getExecutorService().routeEvent(event);
				}
			}
		} else {
			// route the event
			localAC.getExecutorService().routeEvent(event);
			// perhaps we need to route a frozen activity end event too
			routeActivityEndEventIfNeeded();
		}
	}
	
	private void routeActivityEndEventIfNeeded() {
		if (pendingEvents.isEmpty()) {
			// no pending events
			// now check if we have a frozen activity end event
			if (activityEndEvent != null) {
				// route the frozen activity end event too
				if (activityEndEventQueued.compareAndSet(false, true)) {
					// between pending events map element removal and check if
					// it's empty there is no sync so we need to ensure only one
					// thread routes the activity end event
					localAC.getExecutorService().routeEvent(activityEndEvent);
				}
			}
		}
	}

	/**
	 * Signals that the java transaction who fired the specified event did not
	 * commit, and thus the event should be not routed.
	 * 
	 * @param event
	 */
	public void rollback(EventContext event) {

		if (doTraceLogs) {
			logger.trace("Rolled back event of type " + event.getEventTypeId()
					+ " in AC with handle "
					+ event.getActivityContextHandle());
		}

		if (pendingEvents.remove(event)) {
			// confirmed the event was pending
			routeActivityEndEventIfNeeded();
			// manage event references
			event.getReferencesHandler().remove(localAC.getActivityContextHandle());			
		}
	}

	/**
	 * create a barrier for the specified transaction for this activity event
	 * queue, events committed are frozen till all barriers are removed
	 */
	public void createBarrier(Transaction transaction) {
		// raise barrier
		eventBarriers.add(transaction);
	}
	
	/**
	 * remove a barrier for the specified transaction for this activity event
	 * queue, if there are no more barriers then delivering of events frozen proceed
	 */
	public void removeBarrier(Transaction transaction) {
		synchronized (eventBarriers) {
			if(eventBarriers.remove(transaction)) {
				if (eventBarriers.isEmpty()) {
					// no barriers, proceed with commit of all events stored
					EventContext activityEndEvent = null;
					for (Iterator<EventContext> it = eventsBarriered.iterator();it.hasNext();) {
						EventContext e = it.next();
						it.remove();
						if (!e.isActivityEndEvent()) {
							commitAndNotSuspended(e);
						}
						else {
							activityEndEvent = e;
						}
					}
					if (activityEndEvent != null) {
						commitAndNotSuspended(activityEndEvent);
					}
				}
			}
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj.getClass() == this.getClass()) {
			return ((ActivityEventQueueManagerImpl) obj).localAC
					.equals(this.localAC);
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return localAC.hashCode();
	}
}
