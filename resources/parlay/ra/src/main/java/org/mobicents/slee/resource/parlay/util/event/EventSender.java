package org.mobicents.slee.resource.parlay.util.event;

import javax.slee.resource.ActivityHandle;

import org.mobicents.csapi.jr.slee.ResourceEvent;

/**
 * Defines interface to event publisher exposed to the corba callbacks in
 * each of the services.
 */
public interface EventSender {

    /**
     * Sends the specified event.
     * 
     * @param event
     * @param activityHandle
     */
    void sendEvent(ResourceEvent event, ActivityHandle activityHandle);
}
