/*
* "Downloading, compilation and use of this TCK
* is governed by the terms of the JAIN SLEE (JSLEE) V1.1 TECHNOLOGY
* COMPATIBILITY KIT LICENCE AGREEMENT, the text of which is included in
* the download package as LICENSE and can be viewed at
* https://jsleetck11.dev.java.net/jslee-1.1-tck-license.txt"
*/

package com.opencloud.sleetck.lib.resource.testapi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import com.opencloud.sleetck.lib.resource.TCKSbbMessage;
import com.opencloud.sleetck.lib.resource.TCKActivityID;

/**
 * Tests can register an implementation of this interface to listen to a TCK resource.
 * <br>
 * <b>Note:</b> SleeTCKTestUtils.getResourceInterface() returns a proxy interface
 * to the resource which prevents the need to export listener implementations as
 * remote objects. Any object which implements TCKResourceListener can be registered
 * to listen to the resource.
 */
public interface TCKResourceListener extends Remote {

    /**
     * Called when an SBB sends a message to the test via a TCKActivity object
     * or the TCKResourceSbbInterface. When the message is sent via the
     * TCKResourceSbbInterface, the activity id will be null.
     * @param message the message the SBB sent to the test
     * @param calledActivity set to the ID of the activity if TCKActivity.sendSbbMessage()
     *  was called, or null if the call was made via TCKResourceSbbInterface.sendSbbMessage().
     */
    public void onSbbMessage(TCKSbbMessage message, TCKActivityID calledActivity) throws RemoteException;

    /**
     * Called when the SBB invokes the callTest() method of the TCKResourceSbbInterface
     * to make a synchronous call to the test.
     * The meaning of the argument and return value is left to the implmentation of this
     * method and the calling Sbb.
     * @param argument the object passed to callTest()
     * @return The value to return to the Sbb. Note that this Object may be
     *  serialized and deserialized in the resource adaptor, and should therefore
     *  be composed of types known to the resource adaptor.
     * @throws Exception any Exception other than RemoteException thrown
     *  by this method will be rethrown to the caller of callTest() wrapped
     *  in a TCKTestCallException (RemoteExceptions will not be wrapped)
     */
    public Object onSbbCall(Object argument) throws Exception;

    /**
     * Called when an activity is created by an SBB.
     */
    public void onActivityCreatedBySbb(TCKActivityID activityID) throws RemoteException;

    /**
     * Called when an activity is ended by an SBB, either by the TCKActivity.endActivity()
     * method, or by the TCKResourceSbbInterface.endActivity() method
     */
    public void onActivityEndedBySbb(TCKActivityID activityID) throws RemoteException;

// Maintenance note: this method should be activated if and when the relevant callback
//  method is introduced in a standard SLEE Resource Adaptor API
//    /**
//     * Called when the activity context of the given activity is no longer attached
//     * to any SBBs or referenced by any SLEE facilities.
//     * Note: this call is not guaranteed to be made for purged activities.
//     */
//    public void onActivityContextNotAttached(TCKActivityID activityID) throws RemoteException;

    /**
     * Called when the activity context of the given activity has moved
     * from the Ending state to the Invalid state.
     * Note: this call is not guaranteed to be made for purged activities.
     */
    public void onActivityContextInvalid(TCKActivityID activityID) throws RemoteException;

    /**
     * Called when an unexpected exception was caught by the TCK resource
     * or the TCK resource adaptor.
     */
    public void onException(Exception exception) throws RemoteException;

    /**
     * Called to indicate that the SLEE successfully processed a given event.
     * This method will only be called for TCKResourceEvents.
     * @param eventObjectID the unique object ID of the event, as returned by TCKResourceTestInterface.fireEvent()
     */
    public void onEventProcessingSuccessful(long eventObjectID) throws RemoteException;

    /**
     * Called to indicate that the SLEE failed to process a given event.
     * This method will only be called for failures related to TCKResourceEvents.
     * @param eventObjectID the unique object ID of the event, as returned by TCKResourceTestInterface.fireEvent()
     * @param message an optional message related to the failure
     * @param exception an optional Exception related to the failure
     */
    public void onEventProcessingFailed(long eventObjectID, String message, Exception exception) throws RemoteException;

}
