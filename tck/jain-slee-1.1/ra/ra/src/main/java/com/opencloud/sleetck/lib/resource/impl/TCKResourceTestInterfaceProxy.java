/*
* "Downloading, compilation and use of this TCK
* is governed by the terms of the JAIN SLEE (JSLEE) V1.1 TECHNOLOGY
* COMPATIBILITY KIT LICENCE AGREEMENT, the text of which is included in
* the download package as LICENSE and can be viewed at
* https://jsleetck11.dev.java.net/jslee-1.1-tck-license.txt"
*/

package com.opencloud.sleetck.lib.resource.impl;

import java.rmi.RemoteException;
import com.opencloud.sleetck.lib.resource.TCKActivityID;
import com.opencloud.sleetck.lib.resource.testapi.TCKResourceTestInterface;
import com.opencloud.sleetck.lib.resource.testapi.TCKResourceListener;
import com.opencloud.sleetck.lib.TCKTestErrorException;
import com.opencloud.logging.Logable;

import javax.slee.Address;

public class TCKResourceTestInterfaceProxy implements TCKResourceTestInterface {

    public TCKResourceTestInterfaceProxy(TCKResourceTestInterface remoteHandle, Logable log) throws RemoteException, TCKTestErrorException {
        this.remoteHandle=remoteHandle;
        listenerProxy = new TCKResourceListenerProxy(log);
        remoteHandle.setResourceListener(listenerProxy);
    }

    // -- Implementation of TCKResourceTestInterface -- //

    public TCKActivityID createActivity(String name) throws TCKTestErrorException, RemoteException {
        return remoteHandle.createActivity(name);
    }

    public void endActivity(TCKActivityID id) throws TCKTestErrorException, RemoteException {
        remoteHandle.endActivity(id);
    }

    public boolean isLive(TCKActivityID id) throws TCKTestErrorException, RemoteException {
        return remoteHandle.isLive(id);
    }

    public void endAllActivities() throws TCKTestErrorException, RemoteException {
        remoteHandle.endAllActivities();
    }

    public void purgeActivites() throws TCKTestErrorException, RemoteException {
        remoteHandle.purgeActivites();
    }

    public void clearActivities() throws TCKTestErrorException, RemoteException {
        remoteHandle.clearActivities();
    }

    public long fireEvent(String eventTypeName, Object message, TCKActivityID activityID, Address address) throws TCKTestErrorException, RemoteException {
        return remoteHandle.fireEvent(eventTypeName, message, activityID, address);
    }

    public void fireEvent(long objectId, String eventTypeName, Object message, TCKActivityID activityID, Address address) throws TCKTestErrorException, RemoteException {
        remoteHandle.fireEvent(objectId, eventTypeName, message, activityID, address);
    }

    /**
     * Registers the given resource listener with the resource listener proxy.
     */
    public void setResourceListener(TCKResourceListener listener) throws TCKTestErrorException, RemoteException {
        listenerProxy.setResourceListener(listener);
    }

    /**
     * Deregisters the current resource listener (if any) from the resource listener proxy.
     */
    public void removeResourceListener() throws TCKTestErrorException, RemoteException {
        listenerProxy.removeResourceListener();
    }

    public void log(int level, String message) throws TCKTestErrorException, RemoteException {
       remoteHandle.log(level, message);
    }

    public void log(int level, Throwable t) throws TCKTestErrorException, RemoteException {
        remoteHandle.log(level, t);
    }
    
    // -- Introduced methods -- //

    /**
     * Releases resources associated with this proxy class and the resource listener proxy class:
     * - deregisters the resource listener from the remote resource
     * - stops the message delivery thread
     * - releases object references
     */
    public void releaseResources() throws RemoteException, TCKTestErrorException {
        remoteHandle.removeResourceListener();
        listenerProxy.releaseResources();
        remoteHandle = null;
        listenerProxy = null;
        // the proxy is now unreferenced and eligible for garbage collection
    }

    private TCKResourceTestInterface remoteHandle;
    private TCKResourceListenerProxy listenerProxy;

}