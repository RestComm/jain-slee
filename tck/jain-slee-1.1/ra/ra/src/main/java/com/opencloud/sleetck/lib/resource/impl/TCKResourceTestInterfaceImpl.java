/*
* "Downloading, compilation and use of this TCK
* is governed by the terms of the JAIN SLEE (JSLEE) V1.1 TECHNOLOGY
* COMPATIBILITY KIT LICENCE AGREEMENT, the text of which is included in
* the download package as LICENSE and can be viewed at
* https://jsleetck11.dev.java.net/jslee-1.1-tck-license.txt"
*/

package com.opencloud.sleetck.lib.resource.impl;

import java.util.Iterator;
import java.util.Map;
import java.util.HashSet;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import javax.slee.Address;
import javax.slee.facilities.TraceLevel;

import com.opencloud.sleetck.lib.TCKTestErrorException;
import com.opencloud.sleetck.lib.resource.testapi.TCKResourceTestInterface;
import com.opencloud.sleetck.lib.resource.testapi.TCKResourceListener;
import com.opencloud.sleetck.lib.resource.TCKActivityID;
import com.opencloud.sleetck.lib.resource.events.TCKResourceEvent;

/**
 * TCKResourceImpl uses this class as the implementation of TCKResourceTestInterface.<br>
 * This class is closely coupled with TCKResourceImpl.
 */
public class TCKResourceTestInterfaceImpl extends UnicastRemoteObject implements TCKResourceTestInterface {

    public TCKResourceTestInterfaceImpl(TCKResourceImpl resource, Map activityMap) throws RemoteException {
        this.resource=resource;
        this.activityMap=activityMap;
    }

    // -- Implementation of TCKResourceTestInterface -- //

    public TCKActivityID createActivity(String name) throws TCKTestErrorException, RemoteException {
        return resource.createAndRegisterActivity(name);
    }

    public void endActivity(TCKActivityID id) throws TCKTestErrorException, RemoteException {
        resource.markActivityEnded(id);
        resource.getEventDelegator().handleActivityEnd(id,false);
    }

    public boolean isLive(TCKActivityID id) throws TCKTestErrorException, RemoteException {
        return resource.isLive(id);
    }

    /**
     * Note: activities added during the call to this method will not be ended
     * by this method, and attempts to end activities concurrently during this call
     * will cause a TCKTestErrorException to be thrown from either this method or the
     * concurrent call.
     */
    public void endAllActivities() throws TCKTestErrorException, RemoteException {
        // take a copy of the map and release the lock, as endActivity() may cause
        // concurrent access to the map (e.g. via RMI calls)
        Iterator activityIDsIter;
        synchronized (activityMap) {
            activityIDsIter = new HashSet(activityMap.keySet()).iterator();
        }
        while(activityIDsIter.hasNext()) {
            TCKActivityID activityID = (TCKActivityID)activityIDsIter.next();
            if(isLive(activityID)) {
                endActivity(activityID);
            }
        }
    }

    public void purgeActivites() throws TCKTestErrorException, RemoteException {
        synchronized (activityMap) {
            // iterate over a copy of the key set, as we will be modifying the map as we iterate
            Iterator activityIDsIter = new HashSet(activityMap.keySet()).iterator();
            while(activityIDsIter.hasNext()) {
                TCKActivityID activityID = (TCKActivityID)activityIDsIter.next();
                if(!isLive(activityID)) {
                    activityMap.remove(activityID);
                }
            }
        }
    }

    public void clearActivities() throws TCKTestErrorException, RemoteException {
        endAllActivities();
        purgeActivites();
        resource.getEventDelegator().onActivitiesCleared();
    }

    public void fireEvent(long objectId, String eventTypeName, Object message, TCKActivityID activityID, Address address)
                                                throws TCKTestErrorException, RemoteException {
        // This implementation of fireEvent was added to simulate firing of duplicate events
        ActivityState state = null;
        synchronized (activityMap) {
            state = (ActivityState)activityMap.get(activityID);
        }
        if(state == null) throw new TCKTestErrorException("Can't fire event on an unknown activity: "+activityID);
        synchronized (state) { // synchronize on the state object to avoid a race condition with markActivityEnded()
            if(!state.isLive()) throw new TCKTestErrorException("Can't fire event on an ended activity: "+activityID);
            TCKResourceEvent event = new TCKResourceEventImpl(objectId, eventTypeName, message, state.getActivityInterface());
            resource.getEventDelegator().handleEvent(event,eventTypeName,activityID,address);
        }
    }

    public long fireEvent(String eventTypeName, Object message, TCKActivityID activityID, Address address)
                                                throws TCKTestErrorException, RemoteException {
        ActivityState state = null;
        synchronized (activityMap) {
            state = (ActivityState)activityMap.get(activityID);
        }
        if(state == null) throw new TCKTestErrorException("Can't fire event on an unknown activity: "+activityID);
        synchronized (state) { // synchronize on the state object to avoid a race condition with markActivityEnded()
            if(!state.isLive()) throw new TCKTestErrorException("Can't fire event on an ended activity: "+activityID);
            long oid = resource.nextEventOID();
            TCKResourceEvent event = new TCKResourceEventImpl(oid, eventTypeName, message, state.getActivityInterface());
            resource.getEventDelegator().handleEvent(event,eventTypeName,activityID,address);
            return oid;
        }
    }

    public void setResourceListener(TCKResourceListener resourceListener) throws TCKTestErrorException {
        this.resourceListener = resourceListener;
    }

    public void removeResourceListener() throws TCKTestErrorException {
        resourceListener = null;
    }
    
    public void log(int level, String message) throws TCKTestErrorException, RemoteException {
        resource.log(level, message);
    }

    public void log(int level, Throwable t) throws TCKTestErrorException, RemoteException {
        resource.log(level, t);
    }
     
    // -- Package access methods used by TCKResourceImpl -- //

    TCKResourceListener getResourceListener() {
        return resourceListener;
    }

    // -- Private state -- //

    private TCKResourceImpl resource;
    private Map activityMap;
    private TCKResourceListener resourceListener;

}

