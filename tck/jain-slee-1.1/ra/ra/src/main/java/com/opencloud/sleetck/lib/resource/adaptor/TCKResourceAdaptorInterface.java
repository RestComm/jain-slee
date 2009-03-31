/*
* "Downloading, compilation and use of this TCK
* is governed by the terms of the JAIN SLEE (JSLEE) V1.1 TECHNOLOGY
* COMPATIBILITY KIT LICENCE AGREEMENT, the text of which is included in
* the download package as LICENSE and can be viewed at
* https://jsleetck11.dev.java.net/jslee-1.1-tck-license.txt"
*/

package com.opencloud.sleetck.lib.resource.adaptor;

import com.opencloud.sleetck.lib.resource.sbbapi.TCKResourceSbbInterface;
import com.opencloud.sleetck.lib.resource.sbbapi.TCKActivity;
import com.opencloud.sleetck.lib.resource.TCKActivityID;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * JAIN SLEE TCK 1.0 Resource Adaptor implementations use this interface to interact with
 * the JAIN SLEE TCK 1.0 Resource.
 */
public interface TCKResourceAdaptorInterface extends Remote {

    /**
     * The rmi name which the tck resource's resource adaptor interface is bound to on the
     * resource's host machine.
     */
    public static final String RMI_NAME = "TCKResourceAdaptorInterface";

    /**
     * The resource adaptor entity should add a TCKResourceEventHandler to the
     * TCKResource during resource adaptor entity activation.
     * Note that if the event handler and the resource are in different JVMs,
     * the event handler must be exported.
     */
    public void addEventHandler(TCKResourceEventHandler eventHandler) throws RemoteException;

    /**
     * Resource Adaptor implementations should call this method 
     * to indicate that the given event handler is deactivating.
     */
    public void eventHandlerDeactivating(TCKResourceEventHandler eventHandler) throws RemoteException;

    /**
     * The resource adaptor entity should remove its TCKResourceEventHandler from the
     * TCKResource upon resource adaptor entity deactivation.
     */
    public void removeEventHandler(TCKResourceEventHandler eventHandler) throws RemoteException;

    /**
     * This method returns a reference to the SBB interface of the resource.
     * The resource adaptor must provide a reference to the interface in the SBB's
     * JNDI name space.
     */
    public TCKResourceSbbInterface getSbbInterface() throws RemoteException;

    /**
     * Returns the activity represented by the given activity ID, or null
     * if no such activity is registered.<br>
     * The returned TCKActivity object is serializable, and holds a serializable
     * reference to the resource instance. The resource adaptor
     * does not need to provide a proxy for it.
     */
    public TCKActivity getActivity(TCKActivityID activityID) throws RemoteException;

// Maintenance note: this method should be activated if and when the relevant callback
//  method is introduced in a standard JAIN SLEE Resource Adaptor API
//    /**
//     * The resource adaptor calls this method when the activity context of
//     * the given activity is no longer attached to any SBBs or referenced
//     * by any SLEE facilities.
//     */
//    public void onActivityContextNotAttached(TCKActivityID activityID) throws RemoteException;

    /**
     * The resource adaptor calls this method when the activity context of
     * the given activity has moved from the Ending state to the Invalid state.
     */
    public void onActivityContextInvalid(TCKActivityID activityID) throws RemoteException;

    /**
     * The resource adaptor calls this method to forward any unexpected exception or error condition
     * to the test. In many cases this call will cause the currently executing test to 
     * exit with error status.
     */
    public void onException(Exception exception) throws RemoteException;

    /**
     * The resource adaptor calls this method to indicate that the SLEE successfully processed the given event
     * as per the requirements of the JAIN SLEE 1.0 specification.
     * 
     * This method should only be called for TCKResourceEvents fired via the JAIN SLEE TCK 1.0 Resource Adaptor, 
     * and should be called after the event's transactions have committed or rolled back.
     *
     * Exactly one call to either onEventProcessingSuccessful() or onEventProcessingFailed() must 
     * be made for each event fired via the JAIN SLEE TCK 1.0 Resource Adaptor, regardless of the 
     * number of SBB's that received the event.
     *
     * @param eventObjectID the unique object ID of the event, as returned by TCKResourceEvent.getEventObjectID()
     */
    public void onEventProcessingSuccessful(long eventObjectID) throws RemoteException;

    /**
     * The resource adaptor calls this method to indicate that the SLEE failed to process a given event.
     * This method should be called if the SLEE considered itself to have failed to process the event
     * as per the requirements of the JAIN SLEE 1.0 specification.
     * 
     * This method should only be called for failures related to TCKResourceEvents fired via the 
     * JAIN SLEE TCK 1.0 Resource Adaptor. 
     * 
     * Exactly one call to either onEventProcessingSuccessful() or onEventProcessingFailed() must 
     * be made for each event fired via the JAIN SLEE TCK 1.0 Resource Adaptor, regardless of the 
     * number of attempts to deliver the event, or the number of error conditions detected.
     *
     * @param eventObjectID the unique object ID of the event, as returned by TCKResourceEvent.getEventObjectID()
     * @param message an optional message related to the failure
     * @param exception an optional Exception related to the failure
     */
    public void onEventProcessingFailed(long eventObjectID, String message, Exception exception) throws RemoteException;

    /**
     * Causes the TCK Resource to log a message. This will either log to stderr, or a Tracer associated with the TCK
     * Resource Adaptor (depending on when it is called).
     */
    public void log(int level, String message) throws RemoteException;

    /**
     * Causes the TCK Resource to log a message. This will either log to stderr, or a Tracer associated with the TCK
     * Resource Adaptor (depending on when it is called).
     */
    public void log(int level, Throwable t) throws RemoteException;

}
