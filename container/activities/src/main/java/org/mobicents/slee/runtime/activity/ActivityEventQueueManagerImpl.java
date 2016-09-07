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
 * Manages the queuing of events for a specific activity. Note that this impl of {@link ActivityEventQueueManager} is
 * only thread safe if the local AC executive service is single thread.
 *
 * @author Eduardo Martins
 */
public class ActivityEventQueueManagerImpl implements ActivityEventQueueManager {

    private static final Logger LOGGER = Logger.getLogger(ActivityEventQueueManagerImpl.class);

    private final boolean isTraceEnabled = LOGGER.isTraceEnabled();

    /**
     * stores the activity end event when set
     */
    private EventContext activityEndEvent;
    private boolean activityEndEventRouted;

    /**
     * the set of pending events, i.e., events not committed yet, for this activity
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
    private final LocalActivityContextImpl localActivityContext;

    /**
     *
     * @param localActivityContext
     */
    public ActivityEventQueueManagerImpl(LocalActivityContextImpl localActivityContext) {
        this.localActivityContext = localActivityContext;
    }

    @Override
    public void pending(final EventContext event) {
        if (isTraceEnabled) {
            LOGGER.trace("Pending event of type "
                    + event.getEventTypeId() + " in AC with handle "
                    + event.getActivityContextHandle());
        }
        // let the event know it was fired
        event.fired();
        // execute pend
        executePend(event);
    }

    // commit pending
    @Override
    public void commit(final EventContext event) {
        if (isTraceEnabled) {
            LOGGER.trace("Committing event with pending true of type "
                    + event.getEventTypeId() + " in AC with handle "
                    + event.getActivityContextHandle());
        }
        // execute commit
        executeCommit(event, true);
    }

    // commit not pending
    @Override
    public void fireNotTransacted(final EventContext event) {
        if (isTraceEnabled) {
            LOGGER.trace("Committing event with pending false of type "
                    + event.getEventTypeId() + " in AC with handle "
                    + event.getActivityContextHandle());
        }
        // let the event know it was fired
        event.fired();
        // execute commit
        executeCommit(event, false);
    }

    // private methods
    private void commit(EventContext event, boolean isPendingEvent) {
        if (isPendingEvent) {
            if (pendingEvents == null || !pendingEvents.remove(event)) {
                // processing of the event failed
                if (isTraceEnabled) {
                    LOGGER.trace("Unable to commit event of type "
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
            if (isTraceEnabled) {
                LOGGER.trace("Event barriers are not set, proceeding with commit");
            }
            commitAndNotSuspended(event);
        } else {
            if (isTraceEnabled) {
                LOGGER.trace("Event barriers are set, adding event to the frozen queue");
            }
            if (eventsBarriered == null) {
                eventsBarriered = new LinkedList<EventContext>();
            }
            eventsBarriered.add(event);
        }
    }

    private void commitAndNotSuspended(EventContext event) {
        if (isTraceEnabled) {
            LOGGER.trace("Committing event of type " + event.getEventTypeId()
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
            localActivityContext.setActivityReferencesCheck(null);
            // route the event
            localActivityContext.getExecutorService().routeEvent(event);
            // perhaps we need to route a frozen activity end event too
            routeActivityEndEventIfNeeded();
        }
    }

    private void routeActivityEndEventIfNeeded() {
        if (pendingEvents == null || pendingEvents.isEmpty()) {
            // no pending events
            // now check if we have a frozen activity end event
            if (activityEndEvent == null || activityEndEventRouted) {
                return;
            }
            activityEndEventRouted = true;
            // route the activity end event on hold
            localActivityContext.getExecutorService().routeEvent(activityEndEvent);
        }
    }

    @Override
    public void rollback(final EventContext event) {
        // let the event know it was canceled
        event.canceled();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (isTraceEnabled) {
                    LOGGER.trace("Rolled back event of type "
                            + event.getEventTypeId() + " in AC with handle "
                            + event.getActivityContextHandle());
                }
                if (pendingEvents != null && pendingEvents.remove(event)) {
                    // confirmed the event was pending
                    routeActivityEndEventIfNeeded();
                }
            }
        };
        localActivityContext.getExecutorService().execute(runnable);
    }

    @Override
    public void createBarrier(final Transaction transaction) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // raise barrier
                if (eventBarriers == null) {
                    eventBarriers = new HashSet<Transaction>(2);
                }
                eventBarriers.add(transaction);
            }
        };
        localActivityContext.getExecutorService().execute(runnable);
    }

    @Override
    public void removeBarrier(final Transaction transaction) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (eventBarriers != null && eventBarriers.remove(transaction)) {
                    if (eventBarriers.isEmpty()) {
                        // no barriers, proceed with commit of all events stored
                        if (eventsBarriered != null) {
                            EventContext eventContext = null;
                            while (true) {
                                eventContext = eventsBarriered.pollFirst();
                                if (eventContext == null) {
                                    break;
                                } else if (!eventContext.isActivityEndEvent()) {
                                    commitAndNotSuspended(eventContext);
                                } else {
                                    activityEndEvent = eventContext;
                                }
                            }
                        }
                        routeActivityEndEventIfNeeded();
                    }
                }
            }
        };
        localActivityContext.getExecutorService().execute(runnable);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj.getClass() == this.getClass()) {
            return ((ActivityEventQueueManagerImpl) obj).localActivityContext.equals(this.localActivityContext);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return localActivityContext.hashCode();
    }

    // private methods
    private void executeCommit(final EventContext event, final boolean isEventPending) {
        if (isTraceEnabled) {
            LOGGER.trace("Executing commit runnable");
        }
        runnableExecute(runnableCommit(event, isEventPending));
    }
    
    private void executePend(final EventContext event) {
        if (isTraceEnabled) {
            LOGGER.trace("Executing pend runnable");
        }
        runnableExecute(runnablePend(event));
    }

    private Runnable runnableCommit(final EventContext event, final boolean isEventPending) {
        return new Runnable() {
            @Override
            public void run() {
                if (activityEndEvent == null) {
                    commit(event, isEventPending);
                } else {
                    // processing of the event failed
                    if (isTraceEnabled) {
                        LOGGER.trace("Unable to commit event of type "
                                + event.getEventTypeId()
                                + " in AC with handle "
                                + event.getActivityContextHandle()
                                + ", the activity end event is already committed");
                    }
                    event.eventProcessingFailed(FailureReason.OTHER_REASON);
                }
            }
        };
    }

    private void runnableExecute(final Runnable runnable) {
        long time = System.nanoTime();
        if (isTraceEnabled) {
            LOGGER.trace("Runnable execution start");
        }
        localActivityContext.getExecutorService().execute(runnable);
        if (isTraceEnabled) {
            LOGGER.trace("Runnable execution stop, duration " + (System.nanoTime() - time) + " ns");
        }
    }
    
    private Runnable runnablePend(final EventContext event) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (pendingEvents == null) {
                    pendingEvents = new HashSet<EventContext>(4);
                }
                // add event to pending events set
                pendingEvents.add(event);
            }
        };
        return runnable;
    }
}
