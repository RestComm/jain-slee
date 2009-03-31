/*
* "Downloading, compilation and use of this TCK
* is governed by the terms of the JAIN SLEE (JSLEE) V1.1 TECHNOLOGY
* COMPATIBILITY KIT LICENCE AGREEMENT, the text of which is included in
* the download package as LICENSE and can be viewed at
* https://jsleetck11.dev.java.net/jslee-1.1-tck-license.txt"
*/

package com.opencloud.sleetck.lib.resource.impl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;

import com.opencloud.sleetck.lib.resource.TCKSbbMessage;
import com.opencloud.sleetck.lib.resource.TCKActivityID;
import com.opencloud.sleetck.lib.resource.testapi.TCKResourceListener;
import com.opencloud.logging.Logable;

/**
 * The TCKResourceListenerProxy is registered with the TCKResource, and acts as a proxy
 * to the test-specific resource listeners.
 * This class handles registration of local test-implemented listeners, and manages
 * a message queue to provide asynchronous delivery of TCKSbbMessages.
 * There are no flow control mechanisms for the message queue, as the number of messages
 * per test is expected to be small.
 */
public class TCKResourceListenerProxy extends UnicastRemoteObject implements TCKResourceListener, Runnable {

    public TCKResourceListenerProxy(Logable log) throws RemoteException {
        this.log=log;
        messageQueue = new LinkedList();
        // start the delivery thread
        new Thread(this).start();
    }

    // -- Implementation of TCKResourceListener -- //

    public void onSbbMessage(TCKSbbMessage message, TCKActivityID calledActivity) throws RemoteException {
        synchronized(messageQueue) {
            if(!isStopping) {
                messageQueue.addLast(new TCKSbbMessageWrapper(message, calledActivity));
                messageQueue.notifyAll();
            } else {
                log.warning("Could not add TCKSbbMessage to queue, as the resource listener proxy is stopping. message="+
                        message+";calledActivity="+calledActivity);
            }
        }
    }

    public Object onSbbCall(Object argument) throws Exception {
        TCKResourceListener _listener = getListener();
        if(_listener != null) return _listener.onSbbCall(argument);
        else {
            log.warning("TCKResourceListenerProxy.onSbbCall() called, with no listener set");
            throw new IllegalStateException("TCKResourceListenerProxy.onSbbCall() called, with no listener set");
        }
    }

    public void onActivityCreatedBySbb(TCKActivityID activityID) throws RemoteException {
        TCKResourceListener _listener = getListener();
        if(_listener != null) _listener.onActivityCreatedBySbb(activityID);
        else log.warning("TCKResourceListenerProxy.onActivityCreatedBySbb() called, with no listener set");
    }

    public void onActivityEndedBySbb(TCKActivityID activityID) throws RemoteException {
        TCKResourceListener _listener = getListener();
        if(_listener != null) _listener.onActivityEndedBySbb(activityID);
        else log.warning("TCKResourceListenerProxy.onActivityEndedBySbb() called, with no listener set");
    }

// Maintenance note: this method should be activated if and when the relevant callback
//  method is introduced in a standard SLEE Resource Adaptor API
//    public void onActivityContextNotAttached(TCKActivityID activityID) throws RemoteException {
//        TCKResourceListener _listener = getListener();
//        if(_listener != null) _listener.onActivityContextNotAttached(activityID);
//        else log.warning("TCKResourceListenerProxy.onActivityContextNotAttached() called, with no listener set");
//    }

    public void onActivityContextInvalid(TCKActivityID activityID) throws RemoteException {
        TCKResourceListener _listener = getListener();
        if(_listener != null) _listener.onActivityContextInvalid(activityID);
        else log.warning("TCKResourceListenerProxy.onActivityContextInvalid() called, with no listener set");
    }

    public void onException(Exception exception) throws RemoteException {
        TCKResourceListener _listener = getListener();
        if(_listener != null) _listener.onException(exception);
        else log.warning("TCKResourceListenerProxy.onException() called, with no listener set");
    }

    public void onEventProcessingSuccessful(long eventObjectID) throws RemoteException {
        TCKResourceListener _listener = getListener();
        if(_listener != null) _listener.onEventProcessingSuccessful(eventObjectID);
        else log.warning("TCKResourceListenerProxy.onEventProcessingSuccessful() called, with no listener set");
    }

    public void onEventProcessingFailed(long eventObjectID, String message, Exception exception) throws RemoteException {
        TCKResourceListener _listener = getListener();
        if(_listener != null) _listener.onEventProcessingFailed(eventObjectID,message,exception);
        else log.warning("TCKResourceListenerProxy.onEventProcessingFailed() called, with no listener set");
    }

    // -- Implementation of Runnable -- //

    /**
     * Called by the delivery thread
     */
    public void run() {
        while(!isStopping) {
            TCKSbbMessageWrapper messageWrapper = null;
            synchronized(messageQueue) {
                while(messageQueue.isEmpty() && !isStopping) {
                    try {
                        messageQueue.wait();
                    } catch (InterruptedException e) { /* no-op*/ }
                }
                if(isStopping) return;
                messageWrapper = (TCKSbbMessageWrapper)messageQueue.removeFirst();
            }
            TCKSbbMessage message = messageWrapper.getMessage();
            TCKActivityID calledActivity = messageWrapper.getCalledActivity();
            synchronized(listenerLock) {
                if(listener != null) {
                    try {
                        listener.onSbbMessage(message, calledActivity);
                    } catch (RemoteException remoteException) {
                        log.warning("Caught RemoteException when delivering TCKSbbMessage:");
                        log.warning(remoteException);
                    } catch (RuntimeException runtimeException) {
                        log.warning("Caught RuntimeException when delivering TCKSbbMessage:");
                        log.warning(runtimeException);
                    }
                } else {
                    log.warning("No listener set to handle message from TCK resource. message="+
                            message+";calledActivity="+calledActivity);
                }
            }
        }
    }

    // -- Introduced methods -- //

    public void setResourceListener(TCKResourceListener listener) {
        synchronized(listenerLock) {
            this.listener = listener;
        }
    }

    public void removeResourceListener() {
        synchronized(listenerLock) {
            listener = null;
        }
    }

    public void releaseResources() {
        synchronized(messageQueue) {
            isStopping = true;
            messageQueue.clear();
            messageQueue.notifyAll();
        }
        synchronized(listenerLock) {
            listener = null;
        }
    }

    // -- Private methods -- //

    private TCKResourceListener getListener() {
        synchronized(listenerLock) {
            return listener;
        }
    }

    // -- Private inner classes -- //

    private class TCKSbbMessageWrapper {

        public TCKSbbMessageWrapper(TCKSbbMessage message, TCKActivityID calledActivity) {
            this.message = message;
            this.calledActivity = calledActivity;
        }

        public TCKSbbMessage getMessage() {
            return message;
        }

        public TCKActivityID getCalledActivity() {
            return calledActivity;
        }

        private TCKActivityID calledActivity;
        private TCKSbbMessage message;

    }

    // -- Private state -- //

    private TCKResourceListener listener;
    private LinkedList messageQueue;
    private volatile boolean isStopping = false;
    private Object listenerLock = new Object();
    private Logable log;

}
