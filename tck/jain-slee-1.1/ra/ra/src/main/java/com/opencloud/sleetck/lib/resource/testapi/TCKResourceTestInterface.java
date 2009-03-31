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

import javax.slee.Address;
import javax.slee.facilities.TraceLevel;

import com.opencloud.sleetck.lib.TCKTestErrorException;
import com.opencloud.sleetck.lib.resource.TCKActivityID;

/**
 * The test's interface to the TCK resource.
 * <br>
 * <b>Note:</b> SleeTCKTestUtils.getResourceInterface() returns a proxy interface
 * rather than remote handle to the resource - see
 *  {@link #setResourceListener(TCKResourceListener) setResourceListener}.
 */
public interface TCKResourceTestInterface extends Remote {

    /**
     * The rmi name which the tck resource's test interface is bound to on the
     * resource's host machine.
     */
    public static final String RMI_NAME = "TCKResourceTestInterface";

    /**
     * Creates an activity with the given name and returns its ID.
     */
    public TCKActivityID createActivity(String name) throws TCKTestErrorException, RemoteException;

    /**
     * Ends the given activity. The SLEE will be informed of the activity's ending
     * via the resource adaptor.
     */
    public void endActivity(TCKActivityID id) throws TCKTestErrorException, RemoteException;

    /**
     * Returns true if the given activity has been created and has not been ended,
     * false otherwise.
     */
    public boolean isLive(TCKActivityID id) throws TCKTestErrorException, RemoteException;

    /**
     * Ends all the current activities. The SLEE will be informed of the
     * activities' endings via the resource adaptor.
     */
    public void endAllActivities() throws TCKTestErrorException, RemoteException;

    /**
     * Removes all the ended activities from the resource, making them
     * eligible for garbage collection.
     * The test should only call this method when it requires no more information
     * to be sent or received regarding the activity.
     */
    public void purgeActivites() throws TCKTestErrorException, RemoteException;

    /**
     * Cleans up all activity related state: calls endAllActivities() then
     * purgeActivites().
     */
    public void clearActivities() throws TCKTestErrorException, RemoteException;

    /**
     * Fires the given event into the SLEE.
     * @param eventTypeName The name of the event type.
     *  This must be an event type recognized by the TCK resource - i.e. an event type
     *  defined in the com.opencloud.sleetck.lib.resource.events package.
     * @param message the message to store in the event (may be null)
     * @param activityID the ID of the activity on which to fire the event
     * @param address the address associated with the event (optional)
     * @return the event's object id
     */
    public long fireEvent(String eventTypeName, Object message, TCKActivityID activityID, Address address) throws TCKTestErrorException, RemoteException;

    /**
     * Fires the given event into the SLEE.
     * @param objectId The object id to use when creating the event
     *  This value must have been returned by a previous call to <code>fireEvent</code>
     * @param eventTypeName The name of the event type.
     *  This must be an event type recognized by the TCK resource - i.e. an event type
     *  defined in the com.opencloud.sleetck.lib.resource.events package.
     * @param message the message to store in the event (may be null)
     * @param activityID the ID of the activity on which to fire the event
     * @param address the address associated with the event (optional)
     */
    public void fireEvent(long objectId, String eventTypeName, Object message, TCKActivityID activityID, Address address) throws TCKTestErrorException, RemoteException;

    /**
     * Registers the given listener with the resource.
     * <br>
     * <b>Note:</b> SleeTCKTestUtils.getResourceInterface() returns a proxy interface
     * to the resource which prevents the need to export listeners as remote objects
     * (the proxy is the exported object). Tests can simply register any object which
     * implements the listener interface.
     */
    public void setResourceListener(TCKResourceListener listener) throws TCKTestErrorException, RemoteException;

    /**
     * Removes the current listener.
     */
    public void removeResourceListener() throws TCKTestErrorException, RemoteException;
    
    /**
     * Causes the TCK RA to log a message to its tracer. This can be used to place diagnostic information in the SLEE logs from a test.
     */
    public void log(int level, String logMessage) throws TCKTestErrorException, RemoteException;
    
    /**
     * Causes the TCK RA to log a message to its tracer. This can be used to place diagnostic information in the SLEE logs from a test.
     */
    public void log(int level, Throwable t) throws TCKTestErrorException, RemoteException;

}