/*
* "Downloading, compilation and use of this TCK
* is governed by the terms of the JAIN SLEE (JSLEE) V1.1 TECHNOLOGY
* COMPATIBILITY KIT LICENCE AGREEMENT, the text of which is included in
* the download package as LICENSE and can be viewed at
* https://jsleetck11.dev.java.net/jslee-1.1-tck-license.txt"
*/

package com.opencloud.sleetck.lib.resource.sbbapi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import com.opencloud.sleetck.lib.resource.TCKActivityID;
import com.opencloud.sleetck.lib.resource.TCKTestCallException;
import com.opencloud.sleetck.lib.TCKTestErrorException;

/**
 * Defines the SBB's interface to the TCK resource.
 */
public interface TCKResourceSbbInterface extends Remote {

    /**
     * Sends an asynchronous message to the test. The test will receive the message
     * via the onSbbMessage() method.
     * @param payload the payload of the message,
     *  as returned by TCKSbbMessage.getMessage()
     */
    public void sendSbbMessage(Object payload) throws TCKTestErrorException, RemoteException;

    /**
     * Makes a synchronous call to the test's implementation of the
     * onSbbCall() method (defined in TCKResourceListener),
     * then returns the result of the call.
     * @param argument The argument to pass to onSbbCall(). Note that this Object may be
     *  serialized and deserialized in the resource adaptor, and should therefore
     *  be composed of types known to the resource adaptor.
     * @return The return value of onSbbCall()
     * @throws TCKTestCallException if onSbbCall() throws an Exception other than
     *  a RemoteException
     * @throws TCKTestErrorException if no resource listener is registered
     * @throws RemoteException if a communication error occurs
     */
    public Object callTest(Object argument) throws TCKTestCallException, TCKTestErrorException, RemoteException;

    /**
     * Creates a new activity with the given name, and returns its ID.
     */
    public TCKActivityID createActivity(String name) throws TCKTestErrorException, RemoteException;

    /**
     * Returns an interface to the activity represented by
     * the given activity ID, or null if no such activity is registered.<br>
     */
    public TCKActivity getActivity(TCKActivityID activityID) throws TCKTestErrorException, RemoteException;

    /**
     * Ends the activity with the given ID. Both the SLEE and the test
     * will be informed of the activity's end.
     */
    public void endActivity(TCKActivityID activityID) throws TCKTestErrorException, RemoteException;

    /**
     * The SBB calls this method to send an unexpected exception to the test.
     */
    public void sendException(Exception exception) throws TCKTestErrorException, RemoteException;

}
