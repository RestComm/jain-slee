/*
* "Downloading, compilation and use of this TCK
* is governed by the terms of the JAIN SLEE (JSLEE) V1.1 TECHNOLOGY
* COMPATIBILITY KIT LICENCE AGREEMENT, the text of which is included in
* the download package as LICENSE and can be viewed at
* https://jsleetck11.dev.java.net/jslee-1.1-tck-license.txt"
*/

package com.opencloud.sleetck.lib.resource.impl;

import com.opencloud.logging.Logable;
import com.opencloud.sleetck.lib.TCKTestErrorException;
import com.opencloud.sleetck.lib.resource.TCKActivityID;
import com.opencloud.sleetck.lib.resource.adaptor.TCKResourceEventHandler;
import com.opencloud.sleetck.lib.resource.events.TCKResourceEvent;

import javax.slee.Address;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

/**
 * Implementation of EventDelegator for the TCK resource.
 *
 * It does not perform load balancing, but ensures that all events related
 * to a given activity are delivered to the same event handler until
 * the event handler is removed via removeEventHandler(), and warns if
 * events for new activities are delivered when there are no 'active' event handlers.
 *
 * Caveat: this implementation requires that all registered event handlers eventually
 * be deregistered via removeEventHandler() - all event handler references are maintained
 * until this call is made. If a distributed SLEE terminates a large number of event handlers
 * without deregistering the handlers with the resource, this implementation will
 * present a memory leak. In this case, the TCK resource should be restarted.
 *
 * This approach should allow for addition and removal of event handlers during tests.
 */
public class EventDelegatorImpl implements EventDelegator {

    public EventDelegatorImpl(Logable log) {
        this.log = log;
        activeEventHandlers = new LinkedList();
        deactivatingHandlers = new LinkedList();
        activityToHandlerMap = new HashMap();
    }

    // -- Implementation of EventDelegator -- //

    /**
     * The TCK resource calls this method to add an event handler.
     */
    public void addEventHandler(TCKResourceEventHandler eventHandler) {
        synchronized (activeEventHandlers) {
            if(!activeEventHandlers.contains(eventHandler)) {
                activeEventHandlers.add(eventHandler);
                deactivatingHandlers.remove(eventHandler);
            }
        }
    }

    /**
     * Called to indicate that the given event handler is deactivating
     */
    public void eventHandlerDeactivating(TCKResourceEventHandler eventHandler) {
        synchronized (activeEventHandlers) {
            if(activeEventHandlers.contains(eventHandler)) {
                activeEventHandlers.remove(eventHandler);
                deactivatingHandlers.add(eventHandler);
            }
        }
    }

    /**
     * The TCK resource calls this method to remove an event handler.
     */
    public void removeEventHandler(TCKResourceEventHandler eventHandler) {
        // synchronize in activityToHandlerMap first to avoid a deadlock with getHandlerFor()
        synchronized (activityToHandlerMap) {
            synchronized (activeEventHandlers) {
                activeEventHandlers.remove(eventHandler);
                deactivatingHandlers.remove(eventHandler);
                if(activityToHandlerMap.containsValue(eventHandler)) {
                    Iterator activityIDsIter = activityToHandlerMap.keySet().iterator();
                    while(activityIDsIter.hasNext()) {
                        if(activityToHandlerMap.get(activityIDsIter.next()).equals(eventHandler)) {
                            // the activity is allocated to the removing event handler: remove it from
                            // the allocations map, to be reallocated to another handler upon its next event
                            activityIDsIter.remove();
                        }
                    }
                }
            }
        }
    }

    /**
     * This method must be called when the test calls clearActivities(), to
     * allow the EventDelegator to clear its activity table.
     */
    public void onActivitiesCleared() {
        synchronized (activityToHandlerMap) {
            activityToHandlerMap.clear();
        }
    }

    // -- Implementation of TCKResourceEventHandler methods -- //

    /**
     * Called when the test fires an event via the resource.
     */
    public void handleEvent(TCKResourceEvent event, String eventType, TCKActivityID activityID, Address address)
                                                        throws TCKTestErrorException, RemoteException {
        getHandlerFor(activityID).handleEvent(event,eventType,activityID,address);
    }

    /**
     * Called when the given activity is ended by a test or by an SBB.
     */
    public void handleActivityEnd(TCKActivityID activityID, boolean endedBySbb) throws TCKTestErrorException, RemoteException  {
        getHandlerFor(activityID).handleActivityEnd(activityID,endedBySbb);
    }

    /**
     * Called when an activity is created by an SBB
     */
    public void handleActivityCreatedBySbb(TCKActivityID activityID) throws TCKTestErrorException, RemoteException  {
        getHandlerFor(activityID).handleActivityCreatedBySbb(activityID);
    }

    // -- Package access methods -- //

    void setLog(Logable log) {
        this.log=log;
    }

    // -- Private methods -- //

    /**
     * Returns an event handler for the activity. If the activity is already allocated
     * to a handler, that handler is returned.
     * Otherwise, the activity is a new activity, and this method allocates the
     * activity to the most recently added active event handler (if there are active event handlers),
     * or the most recently deactivated event handler (if there are only deactivating event handlers),
     * or throws a TCKTestErrorException (if there are no event handlers registered).
     */
    private TCKResourceEventHandler getHandlerFor(TCKActivityID activityID) throws TCKTestErrorException {
        synchronized (activityToHandlerMap) {
            TCKResourceEventHandler handlerForActivity = (TCKResourceEventHandler)activityToHandlerMap.get(activityID);
            if(handlerForActivity == null) {
                synchronized (activeEventHandlers) {
                    if(!activeEventHandlers.isEmpty()) {
                        handlerForActivity = (TCKResourceEventHandler)activeEventHandlers.getLast();
                    } else if(!deactivatingHandlers.isEmpty()) {
                        log.warning("Assigning an deactivating event handler for a new activity, as "+
                                "there are no active event handlers registered with the TCK resource.");
                        handlerForActivity = (TCKResourceEventHandler)deactivatingHandlers.getLast();
                    } else {
                        throw new TCKTestErrorException("No event handlers registered with the TCK resource");
                    }
                    activityToHandlerMap.put(activityID,handlerForActivity);
                }
            }
            return handlerForActivity;
        }
    }

    // -- Private state -- //

    private LinkedList activeEventHandlers;
    private LinkedList deactivatingHandlers;
    private Map activityToHandlerMap;
    private Logable log;

}