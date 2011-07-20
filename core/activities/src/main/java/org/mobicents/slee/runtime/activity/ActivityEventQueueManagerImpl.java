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

import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import javax.slee.resource.FailureReason;
import javax.transaction.Transaction;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.activity.ActivityEventQueueManager;
import org.mobicents.slee.container.event.EventContext;

/**
 * 
 * Manages the queuing of events for a specific activity. Note that this impl of
 * {@link ActivityEventQueueManager} is only thread safe if the local AC
 * executive service is single thread.
 * 
 * @author Eduardo Martins
 * 
 */
public class ActivityEventQueueManagerImpl implements ActivityEventQueueManager {

	private static final Logger logger = Logger
			.getLogger(ActivityEventQueueManagerImpl.class);

	private boolean doTraceLogs = logger.isTraceEnabled();

	/**
	 * stores the activity end event when set
	 */
	private EventContext activityEndEvent;

	/**
	 * the set of pending events, i.e., events not committed yet, for this
	 * activity
	 */
	private Set<EventContext> pendingEvents;

	/**
	 * the events hold due to barriers set
	 */
	private Deque<EventContext> eventsBarriered;

	/**
	 * the transactions that hold barriers to the activity event queue
	 */
	private Set<Transaction> eventBarriers;

	/**
	 * the local view of the related activity context
	 */
	private final LocalActivityContextImpl localAC;

	/**
	 * 
	 * @param localAC
	 */
	public ActivityEventQueueManagerImpl(LocalActivityContextImpl localAC) {
		this.localAC = localAC;
	}

	@Override
	public void pending(final EventContext event) {
		Runnable r = new Runnable() {
			@Override
			public void run() {
				if (doTraceLogs) {
					logger.trace("Pending event of type "
							+ event.getEventTypeId() + " in AC with handle "
							+ event.getActivityContextHandle());
				}

				// manage event references
				event.getReferencesHandler().add(
						localAC.getActivityContextHandle());
				// add event to pending set
				if (pendingEvents == null) {
					pendingEvents = new HashSet<EventContext>(4);
				}
				pendingEvents.add(event);
			}
		};
		localAC.getExecutorService().execute(r);
	}

	@Override
	public void commit(final EventContext event) {
		Runnable r = new Runnable() {
			@Override
			public void run() {
				if (activityEndEvent == null) {
					commit(event, true);
				} else {
					// processing of the event failed
					if (doTraceLogs) {
						logger.trace("Unable to commit event of type "
								+ event.getEventTypeId()
								+ " in AC with handle "
								+ event.getActivityContextHandle()
								+ ", the activity end event is already committed");
					}
					event.eventProcessingFailed(FailureReason.OTHER_REASON);
				}
			}
		};
		localAC.getExecutorService().execute(r);
	}

	@Override
	public void fireNotTransacted(final EventContext event) {
		// manage event references
		event.getReferencesHandler().add(localAC.getActivityContextHandle());
		// commit event
		Runnable r = new Runnable() {
			@Override
			public void run() {
				if (activityEndEvent == null) {
					// activity end event not set
					// commit event
					commit(event, false);
				} else {
					// processing of the event failed
					if (doTraceLogs) {
						logger.trace("Unable to commit event of type "
								+ event.getEventTypeId()
								+ " in AC with handle "
								+ event.getActivityContextHandle()
								+ ", the activity end event is already committed");
					}
					event.eventProcessingFailed(FailureReason.OTHER_REASON);
				}
			}
		};
		localAC.getExecutorService().execute(r);
	}

	private void commit(EventContext event, boolean isPendingEvent) {
		if (isPendingEvent) {
			if (pendingEvents == null || !pendingEvents.remove(event)) {
				// processing of the event failed
				if (doTraceLogs) {
					logger.trace("Unable to commit event of type "
							+ event.getEventTypeId()
							+ " in AC with handle "
							+ event.getActivityContextHandle()
							+ ", the event was not found in the pending events set.");
				}
				event.eventProcessingFailed(FailureReason.OTHER_REASON);
				return;
			}
		}
		if (eventBarriers == null || eventBarriers.isEmpty()) {
			// barriers are not set, proceed with commit of event
			commitAndNotSuspended(event);
		} else {
			// barriers are set add the event to the frozen queue
			if (eventsBarriered == null) {
				eventsBarriered = new LinkedList<EventContext>();
			}
			eventsBarriered.add(event);
		}
	}

	private void commitAndNotSuspended(EventContext event) {

		if (doTraceLogs) {
			logger.trace("Commiting event of type " + event.getEventTypeId()
					+ " in AC with handle " + event.getActivityContextHandle());
		}

		if (event.isActivityEndEvent()) {
			// store it
			activityEndEvent = event;
			// check we can route it
			routeActivityEndEventIfNeeded();
		} else {
			// cancel any check for references possibly queued after commiting
			// the event
			localAC.setActivityReferencesCheck(null);
			// route the event
			localAC.getExecutorService().routeEvent(event);
			// perhaps we need to route a frozen activity end event too
			routeActivityEndEventIfNeeded();
		}
	}

	private void routeActivityEndEventIfNeeded() {
		if (pendingEvents == null || pendingEvents.isEmpty()) {
			// no pending events
			// now check if we have a frozen activity end event
			if (activityEndEvent != null) {
				// route the frozen activity end event too
				localAC.getExecutorService().routeEvent(activityEndEvent);
			}
		}
	}

	@Override
	public void rollback(final EventContext event) {
		Runnable r = new Runnable() {
			@Override
			public void run() {
				if (doTraceLogs) {
					logger.trace("Rolled back event of type "
							+ event.getEventTypeId() + " in AC with handle "
							+ event.getActivityContextHandle());
				}

				if (pendingEvents != null && pendingEvents.remove(event)) {
					// confirmed the event was pending
					routeActivityEndEventIfNeeded();
				}
			}
		};
		localAC.getExecutorService().execute(r);
	}

	@Override
	public void createBarrier(final Transaction transaction) {
		Runnable r = new Runnable() {
			@Override
			public void run() {
				// raise barrier
				if (eventBarriers == null) {
					eventBarriers = new HashSet<Transaction>(2);
				}
				eventBarriers.add(transaction);
			}
		};
		localAC.getExecutorService().execute(r);
	}

	@Override
	public void removeBarrier(final Transaction transaction) {
		Runnable r = new Runnable() {
			@Override
			public void run() {
				if (eventBarriers != null && eventBarriers.remove(transaction)) {
					if (eventBarriers.isEmpty()) {
						// no barriers, proceed with commit of all events stored
						if (eventsBarriered != null) {
							EventContext e = null;
							while (true) {
								e = eventsBarriered.pollFirst();
								if (e == null) {
									break;
								} else {
									if (!e.isActivityEndEvent()) {
										commitAndNotSuspended(e);
									} else {
										activityEndEvent = e;
									}
								}
							}
						}
						routeActivityEndEventIfNeeded();
					}
				}
			}
		};
		localAC.getExecutorService().execute(r);
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
