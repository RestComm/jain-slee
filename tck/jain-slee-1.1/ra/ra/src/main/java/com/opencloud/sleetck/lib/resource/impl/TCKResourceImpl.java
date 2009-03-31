/*
* "Downloading, compilation and use of this TCK
* is governed by the terms of the JAIN SLEE (JSLEE) V1.1 TECHNOLOGY
* COMPATIBILITY KIT LICENCE AGREEMENT, the text of which is included in
* the download package as LICENSE and can be viewed at
* https://jsleetck11.dev.java.net/jslee-1.1-tck-license.txt"
*/

package com.opencloud.sleetck.lib.resource.impl;

import com.opencloud.logging.Logable;
import com.opencloud.logging.StdErrLog;
import com.opencloud.sleetck.lib.TCKTestErrorException;
import com.opencloud.sleetck.lib.resource.TCKActivityID;
import com.opencloud.sleetck.lib.resource.TCKSbbMessage;
import com.opencloud.sleetck.lib.resource.TCKTestCallException;
import com.opencloud.sleetck.lib.resource.adaptor.TCKResourceAdaptorInterface;
import com.opencloud.sleetck.lib.resource.adaptor.TCKResourceEventHandler;
import com.opencloud.sleetck.lib.resource.adaptor.TCKResourceSetupInterface;
import com.opencloud.sleetck.lib.resource.sbbapi.TCKActivity;
import com.opencloud.sleetck.lib.resource.sbbapi.TCKResourceSbbInterface;
import com.opencloud.sleetck.lib.resource.testapi.TCKResourceListener;
import com.opencloud.sleetck.lib.resource.testapi.TCKResourceTestInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.HashMap;

/**
 * The main implementation class for the TCK resource interfaces.<br>
 * This class implements the resource adaptor's and SBB's interfaces to
 * the resource, and delegates to TCKResourceTestInterfaceImpl to implement
 * the test's interface. This class is closely coupled with TCKResourceTestInterfaceImpl.
 */
public class TCKResourceImpl extends UnicastRemoteObject
            implements TCKResourceSetupInterface, TCKResourceAdaptorInterface,
                        TCKResourceSbbInterface, TCKResourceActivityInterface {

    public TCKResourceImpl() throws RemoteException {
        log = new StdErrLog();
        activityMap = new HashMap();
        testInterfaceImpl = new TCKResourceTestInterfaceImpl(this,activityMap);
        eventDelegator = new EventDelegatorImpl(log);
    }

    // -- Implementation of TCKResourceSetupInterface -- //

    public void setLog(Logable log) {
        this.log=log;
        eventDelegator.setLog(log);
    }

    public TCKResourceTestInterface getTestInterface() throws TCKTestErrorException {
        return testInterfaceImpl;
    }

    public TCKResourceAdaptorInterface getResourceAdaptorInterface() throws TCKTestErrorException {
        return this;
    }

    // -- Implementation of TCKResourceAdaptorInterface -- //

    public void addEventHandler(TCKResourceEventHandler eventHandler) {
        eventDelegator.addEventHandler(eventHandler);
    }

    public void eventHandlerDeactivating(TCKResourceEventHandler eventHandler) {
        eventDelegator.eventHandlerDeactivating(eventHandler);
    }

    public void removeEventHandler(TCKResourceEventHandler eventHandler) {
        eventDelegator.removeEventHandler(eventHandler);
    }

    public TCKResourceSbbInterface getSbbInterface() {
        return this;
    }

// Maintenance note: this method should be activated if and when the relevant callback
//  method is introduced in a standard SLEE Resource Adaptor API
//    public void onActivityContextNotAttached(TCKActivityID activityID) {
//        try {
//            boolean isRegistered = false;
//            synchronized (activityMap) {
//                isRegistered = (activityMap.get(activityID) != null);
//            }
//            if(isRegistered) {
//                TCKResourceListener listener = getResourceListener();
//                if(listener != null) listener.onActivityContextNotAttached(activityID);
//                else log.warning("No listener set during onActivityContextNotAttached() call");
//            } else log.finer("Ignoring onActivityContextNotAttached call for non-registered activity");
//        } catch(RemoteException re) { handleFailedNotification(re); }
//    }

    public void onActivityContextInvalid(TCKActivityID activityID) {
        try {
            boolean isRegistered = false;
            synchronized (activityMap) {
                isRegistered = (activityMap.get(activityID) != null);
            }
            if(isRegistered) {
                TCKResourceListener listener = getResourceListener();
                if(listener != null) listener.onActivityContextInvalid(activityID);
                else log.warning("No listener set during onActivityContextInvalid() call");
            } else log.finer("Ignoring onActivityContextInvalid call for non-registered activity");
        } catch(RemoteException re) { handleFailedNotification(re); }
    }

    public void onException(final Exception exception) {
        try {
            AccessController.doPrivileged(new PrivilegedExceptionAction() {
                public Object run() throws RemoteException {
                    TCKResourceListener listener = getResourceListener();
                    if(listener != null) listener.onException(exception);
                    else {
                        log.warning(exception);
                        log.warning("No listener set during onException() call");
                    }
                    return null;
                }
            });
        } catch(PrivilegedActionException e) {
            handleFailedNotification((RemoteException)e.getException());
        }
    }

    public void onEventProcessingSuccessful(final long eventObjectID) throws RemoteException {
        try {
            AccessController.doPrivileged(new PrivilegedExceptionAction() {
                public Object run() throws RemoteException {
                    TCKResourceListener listener = getResourceListener();
                    if(listener != null) listener.onEventProcessingSuccessful(eventObjectID);
                    else {
                        log.warning("No listener set during onEventProcessingSuccessful() call. Event object ID="+eventObjectID);
                    }
                    return null;
                }
            });
        } catch(PrivilegedActionException e) {
            handleFailedNotification((RemoteException)e.getException());
        }
    }

    public void onEventProcessingFailed(final long eventObjectID, final String message, final Exception exception) {
        try {
            AccessController.doPrivileged(new PrivilegedExceptionAction() {
                public Object run() throws RemoteException {
                    TCKResourceListener listener = getResourceListener();
                    if(listener != null) listener.onEventProcessingFailed(eventObjectID,message,exception);
                    else {
                        log.warning("No listener set during onEventProcessingFailed() call. Failed event object ID="+eventObjectID);
                        if(exception != null) log.warning(exception);
                        if(message != null) log.warning(message);
                    }
                    return null;
                }
            });
        } catch(PrivilegedActionException e) {
            handleFailedNotification((RemoteException)e.getException());
        }
    }

    public TCKActivity getActivity(TCKActivityID activityID) {
        synchronized (activityMap) {
            ActivityState state = (ActivityState)activityMap.get(activityID);
            return state != null ? state.getActivityInterface() : null;
        }
    }

    // -- Implementation of TCKResourceSbbInterface -- //

    public void sendSbbMessage(Object payload) throws TCKTestErrorException {
        sendSbbMessage(payload,null);
    }

    public Object callTest(final Object argument) throws TCKTestCallException, TCKTestErrorException, RemoteException {
        try {
            return AccessController.doPrivileged(new PrivilegedExceptionAction() {
                public Object run() throws TCKTestCallException, TCKTestErrorException, RemoteException {
                    TCKResourceListener listener = getResourceListener();
                    if(listener != null) {
                        try {
                            return listener.onSbbCall(argument);
                        } catch(RemoteException re) {
                            throw re;
                        } catch(Exception e) {
                            throw new TCKTestCallException(e);
                        }
                    } else throw new TCKTestErrorException("No resource listener set during callTest() call");
                }
            });
        } catch(PrivilegedActionException pae) {
            Exception caught = pae.getException();
            if(caught instanceof TCKTestCallException) throw (TCKTestCallException)caught;
            if(caught instanceof RemoteException) throw (RemoteException)caught;
            else throw (TCKTestErrorException)caught;
        }
    }

    public TCKActivityID createActivity(String name) {
        final TCKActivityID id = createAndRegisterActivity(name);
        try {
            AccessController.doPrivileged(new PrivilegedExceptionAction() {
                public Object run() throws TCKTestErrorException, RemoteException {
                    eventDelegator.handleActivityCreatedBySbb(id);
                    return null;
                }
            });
        } catch(PrivilegedActionException e) {
            onException(e.getException());
        }
        try {
            AccessController.doPrivileged(new PrivilegedExceptionAction() {
                public Object run() throws RemoteException {
                    TCKResourceListener listener = getResourceListener();
                    if(listener != null) listener.onActivityCreatedBySbb(id);
                    else log.warning("No listener set during createActivity() call");
                    return null;
                }
            });
        } catch(PrivilegedActionException e) {
            handleFailedNotification((RemoteException)e.getException());
        }
        return id;
    }

    // note: getActivity(TCKActivityID activityID) already implemented

    public void endActivity(final TCKActivityID activityID)  {
        try {
            markActivityEnded(activityID);
            try {
                AccessController.doPrivileged(new PrivilegedExceptionAction() {
                    public Object run() throws TCKTestErrorException, RemoteException {
                        eventDelegator.handleActivityEnd(activityID,true);
                        return null;
                    }
                });
            } catch(PrivilegedActionException e) {
                onException(e.getException());
            }
            try {
                AccessController.doPrivileged(new PrivilegedExceptionAction() {
                    public Object run() throws RemoteException {
                        TCKResourceListener listener = getResourceListener();
                        if(listener != null) listener.onActivityEndedBySbb(activityID);
                        else log.warning("No listener set during endActivity() call");
                        return null;
                    }
                });
            } catch(PrivilegedActionException e) {
                handleFailedNotification((RemoteException)e.getException());
            }
        } catch(TCKTestErrorException e) { onException(e); }
    }

    public void sendException(Exception exception) throws TCKTestErrorException {
        onException(exception);
    }
    
    public void log(int level, String message) throws RemoteException {
        log.writeToLog(level, message);
    }

    public void log(int level, Throwable t) throws RemoteException {
        log.writeToLog(level, t);
    }
    
    // -- Implementation of TCKResourceActivityInterface -- //

    // note: endActivity(TCKActivityID activityID) already implemented

    public boolean isLive(TCKActivityID activityID) throws TCKTestErrorException {
        synchronized (activityMap) {
            ActivityState state = (ActivityState)activityMap.get(activityID);
            return state != null && state.isLive();
        }
    }

    public void sendSbbMessage(final Object payload, final TCKActivityID activityID) {
        try {
            AccessController.doPrivileged(new PrivilegedExceptionAction() {
                public Object run() throws RemoteException {
                    TCKSbbMessage message = new TCKSbbMessageImpl(payload);
                    TCKResourceListener listener = getResourceListener();
                    if(listener != null) listener.onSbbMessage(message,activityID);
                    else log.warning("No listener set during sendSbbMessage() call");
                    return null;
                }
            });
        } catch(PrivilegedActionException e) {
            handleFailedNotification((RemoteException)e.getException());
        }
    }

    // -- Package access methods used by this class and TCKResourceTestInterfaceImpl -- //

    TCKActivityID createAndRegisterActivity(String name) {
        long oid = nextActivityOID();
        TCKActivityID id = new TCKActivityIDImpl(oid,name);
        TCKActivity activity = new TCKActivityImpl(id,this);
        synchronized (activityMap) {
            activityMap.put(id,new ActivityState(activity));
        }
        return id;
    }

    /**
     * Marks the given activity as ended, or throws a TCKTestErrorException if
     * the activity is not live.
     */
    void markActivityEnded(TCKActivityID activityID) throws TCKTestErrorException {
        synchronized (activityMap) {
            ActivityState state = (ActivityState)activityMap.get(activityID);
            if(state == null) throw new TCKTestErrorException("Attempt to end an unknown activity: "+activityID);
            synchronized (state) { // synchronize on the state object to avoid a race condition with fireEvent()
                if(!state.isLive()) throw new TCKTestErrorException("Can't end an activity which is not live: "+activityID);
                state.end();
            }
        }
    }

    long nextEventOID() {
        synchronized (TCKResourceImpl.class) { return eventOIDCounter++; }

    }

    EventDelegator getEventDelegator() {
        return eventDelegator;
    }

    // -- Private methods -- //

    private long nextActivityOID() {
        synchronized (TCKResourceImpl.class) { return activityOIDCounter++; }
    }

    /**
     * Handles a RemoteException thrown during a call to the remote resource listener.
     */
    private void handleFailedNotification(final RemoteException re) {
        AccessController.doPrivileged(new PrivilegedAction() {
            public Object run() {
                log.warning("Caught RemoteException while calling the resource listener:");
                log.warning(re);
                return null;
            }
        });
    }

    private TCKResourceListener getResourceListener() {
        return testInterfaceImpl.getResourceListener();
    }

    // -- Private state -- //

    private EventDelegatorImpl eventDelegator;
    private TCKResourceTestInterfaceImpl testInterfaceImpl;
    private HashMap activityMap;
    private Logable log;

    private static long eventOIDCounter     = 0;
    private static long activityOIDCounter  = 0;

}