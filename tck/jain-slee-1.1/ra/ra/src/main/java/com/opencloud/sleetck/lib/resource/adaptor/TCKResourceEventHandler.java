/*
* "Downloading, compilation and use of this TCK
* is governed by the terms of the JAIN SLEE (JSLEE) V1.1 TECHNOLOGY
* COMPATIBILITY KIT LICENCE AGREEMENT, the text of which is included in
* the download package as LICENSE and can be viewed at
* https://jsleetck11.dev.java.net/jslee-1.1-tck-license.txt"
*/

package com.opencloud.sleetck.lib.resource.adaptor;

import com.opencloud.sleetck.lib.resource.TCKActivityID;
import com.opencloud.sleetck.lib.resource.events.TCKResourceEvent;
import com.opencloud.sleetck.lib.TCKTestErrorException;
import javax.slee.Address;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * JAIN SLEE TCK 1.0 resource adaptor implementations register an implementation
 * of this interface with the JAIN SLEE TCK 1.0 resource to listen for events and
 * other notifications.
 * Note that if the event handler and the resource are in different JVMs,
 * the event handler must be exported.
 */
public interface TCKResourceEventHandler extends Remote {

    /**
     * Called when the test fires an event to the SLEE via the resource.<br>
     * The implementation of this method should deliver the event to the SLEE.<br>
     * Event delivery must be asynchronous, in that event processing and subsequent callbacks
     * must not be made from the calling thread. If the JAIN SLEE implementation processes the 
     * events using the thread which fired the event, then the JAIN SLEE TCK Resoure Adaptor implementation
     * must provide an asynchronous layer for event handling.<br>
     * The TCKResourceEvent object is serializable; the
     * resource adaptor does not need to provide a proxy for it.
     */
    public void handleEvent(TCKResourceEvent event, String eventType, TCKActivityID activityID, Address address)
                                                        throws TCKTestErrorException, RemoteException;

    /**
     * Called when the given activity is ended by a test or by an SBB.<br>
     * The implementation of this method should inform the SLEE that the activity has ended.<br>
     * @param endedBySbb If true, the activity was ended by an SBB,
     *  either via the TCKActivity interface or the TCKResourceSbbInterface.
     *  If false, the activity was ended by the test.
     */
    public void handleActivityEnd(TCKActivityID activityID, boolean endedBySbb) throws TCKTestErrorException, RemoteException;

    /**
     * Called when an activity is created by an SBB.<br>
     * The implementation of this method should inform the SLEE that the activity has been created by an SBB.<br>
     * @param activityID the ID of the newly created activity
     */
    public void handleActivityCreatedBySbb(TCKActivityID activityID) throws TCKTestErrorException, RemoteException;

}
