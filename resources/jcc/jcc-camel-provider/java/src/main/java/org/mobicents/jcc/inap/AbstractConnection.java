/*
 * File Name     : AbstractConnection.java
 *
 * The Java Call Control API for CAMEL 2
 *
 * The source code contained in this file is in in the public domain.
 * It can be used in any project or product without prior permission,
 * license or royalty payments. There is  NO WARRANTY OF ANY KIND,
 * EXPRESS, IMPLIED OR STATUTORY, INCLUDING, WITHOUT LIMITATION,
 * THE IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE,
 * AND DATA ACCURACY.  We do not warrant or make any representations
 * regarding the use of the software or the  results thereof, including
 * but not limited to the correctness, accuracy, reliability or
 * usefulness of the software.
 */
package org.mobicents.jcc.inap;

import java.util.ArrayList;
import java.util.HashMap;

import javax.csapi.cc.jcc.EventFilter;
import javax.csapi.cc.jcc.InvalidArgumentException;
import javax.csapi.cc.jcc.InvalidStateException;
import javax.csapi.cc.jcc.JccAddress;
import javax.csapi.cc.jcc.JccCall;
import javax.csapi.cc.jcc.JccConnection;
import javax.csapi.cc.jcc.JccConnectionEvent;
import javax.csapi.cc.jcc.JccConnectionListener;
import javax.csapi.cc.jcc.JccEvent;
import javax.csapi.cc.jcc.PrivilegeViolationException;
import javax.csapi.cc.jcc.ResourceUnavailableException;

import org.apache.log4j.Logger;
import org.mobicents.jcc.inap.address.JccCalledPartyNumber;
import org.mobicents.jcc.inap.protocol.Connect;
import org.mobicents.protocols.ss7.tcap.api.TCAPException;
import org.mobicents.protocols.ss7.tcap.api.TCAPProvider;
import org.mobicents.protocols.ss7.tcap.api.TCAPSendException;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCContinueRequest;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCEndRequest;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TerminationType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.util.LocalTimer;

import EDU.oswego.cs.dl.util.concurrent.QueuedExecutor;
import EDU.oswego.cs.dl.util.concurrent.Semaphore;

/**
 *
 * @author Oleg Kulikov
 */
public abstract class AbstractConnection implements JccConnection {

    public final static int IDLE_TIMEOUT = 5;
    public final static int AUTH_TIMEOUT = 5;
    public final static int ADDRESS_ANALYZE_TIMEOUT = 5;
    public final static int CALL_DELIVERY_TIMEOUT = 65;
    public final static int ALERTING_TIMEOUT = 65;
    public final static int CONNECTED_TIMEOUT = 1805;
    protected ConnectionID connectionID;
    protected volatile boolean isBlocked = false;
    protected JccAddress address;
    protected JccCallImpl call;
    protected volatile int state;
    protected volatile int cause;
    protected LocalTimer timer = new LocalTimer();
    protected Semaphore semaphore = new Semaphore(0);
    private QueuedExecutor applicationEventQueue = new QueuedExecutor();
    private QueuedExecutor signalingEventQueue = new QueuedExecutor();
    private final static HashMap states = new HashMap();
    private final static HashMap causes = new HashMap();
    private volatile boolean released = false;
    private String callID;
    
    protected TCAPProvider tcapProvider;
    protected Dialog tcapDialog;
    
    
    private final static Logger logger = Logger.getLogger(AbstractConnection.class);

    static {
        states.put(new Integer(JccConnection.IDLE), "IDLE");
        states.put(new Integer(JccConnection.ADDRESS_ANALYZE), "ADDRESS_ANALYZE");
        states.put(new Integer(JccConnection.ADDRESS_COLLECT), "ADDRESS_COLLECT");
        states.put(new Integer(JccConnection.ALERTING), "ALERTING");
        states.put(new Integer(JccConnection.AUTHORIZE_CALL_ATTEMPT), "AUTHORIZE_CALL_ATTEMPT");
        states.put(new Integer(JccConnection.CALL_DELIVERY), "CALL_DELIVERY");
        states.put(new Integer(JccConnection.CONNECTED), "CONNECTED");
        states.put(new Integer(JccConnection.DISCONNECTED), "DISCONNECTED");
        states.put(new Integer(JccConnection.FAILED), "FAILED");
    }

    static {
        causes.put(new Integer(JccConnectionEvent.CAUSE_BUSY), "CAUSE_BUSY");
        causes.put(new Integer(JccConnectionEvent.CAUSE_CALL_CANCELLED), "CAUSE_CALL_CANCELED");
        causes.put(new Integer(JccConnectionEvent.CAUSE_CALL_RESTRICTED), "CAUSE_CALL_RESTRICTED");
        causes.put(new Integer(JccConnectionEvent.CAUSE_DEST_NOT_OBTAINABLE), "CAUSE_DEST_NOT_OBTAINABLE");
        causes.put(new Integer(JccConnectionEvent.CAUSE_GENERAL_FAILURE), "CAUSE_GENERAL_FAILURE");
        causes.put(new Integer(JccConnectionEvent.CAUSE_INCOMPATIBLE_DESTINATION), "CAUSE_INCOMPATIBLE_DESTINATION");
        causes.put(new Integer(JccConnectionEvent.CAUSE_MORE_DIGITS_NEEDED), "CAUSE_MORE_DIGITS_NEEDED");
        causes.put(new Integer(JccConnectionEvent.CAUSE_NETWORK_CONGESTION), "CAUSE_NETWORK_CONGESTION");
        causes.put(new Integer(JccConnectionEvent.CAUSE_NETWORK_NOT_OBTAINABLE), "CAUSE_NETWORK_NOT_OBTAINABLE");
        causes.put(new Integer(JccConnectionEvent.CAUSE_NEW_CALL), "CAUSE_NEW_CALL");
        causes.put(new Integer(JccConnectionEvent.CAUSE_NORMAL), "CAUSE_NORMAL");
        causes.put(new Integer(JccConnectionEvent.CAUSE_NO_ANSWER), "CAUSE_NO_ANSWER");
        causes.put(new Integer(JccConnectionEvent.CAUSE_REDIRECTED), "CAUSE_REDIRECTED");
        causes.put(new Integer(JccConnectionEvent.CAUSE_RESOURCES_NOT_AVAILABLE), "CAUSE_RESOURCES_NOT_AVAILABLE");
        causes.put(new Integer(JccConnectionEvent.CAUSE_SNAPSHOT), "CAUSE_SNAPSHOT");
        causes.put(new Integer(JccConnectionEvent.CAUSE_TIMER_EXPIRY), "CAUSE_TIMER_EXPIRY");
        causes.put(new Integer(JccConnectionEvent.CAUSE_UNKNOWN), "CAUSE_UNKNOWN");
        causes.put(new Integer(JccConnectionEvent.CAUSE_USER_NOT_AVAILABLE), "CAUSE_USER_NOT_AVAILABLE");
    }

    /** Creates a new instance of AbstractConnection */
    public AbstractConnection(ConnectionID connectionID, JccCallImpl call, JccAddress address, TCAPProvider provider,Dialog tcapDialog) {
        this.connectionID = connectionID;
        this.call = call;
        this.address = address;
        this.state = JccConnection.IDLE;
        this.callID = call.callID;
        this.tcapDialog = tcapDialog;
        this.tcapProvider = provider;
    }

    public ConnectionID getID() {
        return connectionID;
    }

    /**
     * (Non Java-doc).
     * @see javax.csapi.cc.jcc.JccConnection#getAddress().
     */
    public JccAddress getAddress() {
        return address;
    }

    /**
     * (Non Java-doc).
     * @see javax.csapi.cc.jcc.JccConnection#getCall().
     */
    public JccCall getCall() {
        return call;
    }

    /**
     * (Non Java-doc).
     * @see javax.csapi.cc.jcc.JccConnection#getState().
     */
    public int getState() {
        return state;
    }

    /**
     * (Non Java-doc).
     * @see javax.csapi.cc.jcc.JccConnection#isBlocked().
     */
    public boolean isBlocked() {
        return isBlocked;
    }

    /**
     * (Non Java-doc).
     * @see javax.csapi.cc.jcc.JccConnection#release().
     */
    public void release(int causeCode) throws PrivilegeViolationException,
            ResourceUnavailableException, InvalidStateException, InvalidArgumentException {
        released = true;
        //send connect on to appropriate answer machine number
        JccEvent evt = new JccConnectionEventImpl(
                JccConnectionEvent.CONNECTION_FAILED,
                this,
                JccEvent.CAUSE_CALL_RESTRICTED);
        queueEvent(evt);
        if (isBlocked()) {
            resume();
        }

        JccCalledPartyNumber cpn = null;
        switch (causeCode) {
            case JccEvent.CAUSE_CALL_RESTRICTED:
                cpn = new JccCalledPartyNumber(call.provider, "9999");
            default:
                cpn = new JccCalledPartyNumber(call.provider, "9999");
        }

        if (logger.isDebugEnabled()) {
            logger.debug(this + "release(): connecting to " + cpn.getRouteAddress());
        }
        Connect connect = new Connect(cpn.getRouteAddress());

        Invoke component = this.tcapProvider.getComponentPrimitiveFactory().createTCInvokeRequest();
        try {
			component.setInvokeId(this.tcapDialog.getNewInvokeId());
		} catch (TCAPException e) {
			throw new RuntimeException("Cant create invoke id", e);
		}

        TCContinueRequest message = this.tcapProvider.getDialogPrimitiveFactory().createContinue(this.tcapDialog);
        message.setApplicationContextName(this.tcapDialog.getApplicationContextName());
        message.setUserInformation(this.tcapDialog.getUserInformation());
        
        try {
			this.tcapDialog.sendComponent(component);
		} catch (TCAPSendException e) {
			throw new RuntimeException("Cant send", e);
		}
        try {
			this.tcapDialog.send(message);
		} catch (TCAPSendException e) {
			throw new RuntimeException("Cant send", e);
		}

        //switch called and calling party addresses
//        SccpAddress calledPartyAddress = connectionID.getCallingPartyAddress();
//        SccpAddress callingPartyAddress = connectionID.getCalledPartyAddress();
//
//        try {
//            call.provider.send(calledPartyAddress, callingPartyAddress, message);
//        } catch (IOException e) {
//        }
    }

    /**
     * (Non Java-doc).
     * @see javax.csapi.cc.jcc.JccConnection#continueProcessing().
     */
    public synchronized void continueProcessing()
            throws PrivilegeViolationException, ResourceUnavailableException, InvalidStateException {
        resume();
    }

    public synchronized void queueEvent(JccEvent event) {
        if (event.getID() == JccConnectionEvent.CONNECTION_FAILED) {
            logger.debug(this + "restarting signaling queue");
            if (signalingEventQueue != null) {
                signalingEventQueue.shutdownNow();
                signalingEventQueue = new QueuedExecutor();
            }
        }
        try {
            if (logger.isDebugEnabled()) {
                logger.debug(this + "queue event " + event);
            }
            if (signalingEventQueue != null) {
                signalingEventQueue.execute((Runnable) event);
            } else {
                logger.error(this + ", Unexpected event=" + event);
            }
        } catch (InterruptedException e) {
        }
    }

    protected void block() {
        this.isBlocked = true;
        try {
            if (logger.isDebugEnabled()) {
                logger.debug(this + "blocking processing in state=" + getStateName(state));
            }

            semaphore.acquire();
            this.isBlocked = false;

            if (logger.isDebugEnabled()) {
                logger.debug(this + "resuming processing in state=" + getStateName(state));
            }
        } catch (InterruptedException e) {
            this.isBlocked = false;
            if (logger.isDebugEnabled()) {
                logger.debug(this + "interrupted block in state=" + getStateName(state));
            }
        }
    }

    protected void resume() {
        if (isBlocked) {
            semaphore.release();
        }
    }

    /**
     * Handle InitialDP detection point.
     */
    public void onConnectionCreated() {
        logger.info(this + "CONNECTION_CREATED, " + getCauseName(cause));
        call.append(this);
        timer.schedule(new CancelTimeoutTask(this), IDLE_TIMEOUT);
    }

    public abstract void onAuthorizeCallAttempt();

    public abstract void onAddressCollect();

    public abstract void onAddressAnalyze(JccConnectionEventImpl evt);

    public abstract void onCallDelivery();

    public abstract void onAlerting();

    public abstract void onConnected();

    /**
     * Handle tc_abort event
     */
    public void onFailed() {
        logger.info(this + "FAILED, " + getCauseName(cause));

        if (logger.isDebugEnabled()) {
            logger.debug(this + "onFailed(): disable timer");
        }
        timer.stop();

        JccEvent event = new JccConnectionEventImpl(JccConnectionEvent.CONNECTION_DISCONNECTED, this, cause);
        queueEvent(event);

        if (logger.isDebugEnabled()) {
            logger.debug(this + "onFailed(): release all blocks");
        }
        resume();
    }

    public void onDisconnected() {
        logger.info(this + "DISCONNECTING, " + getCauseName(cause));
        if (logger.isDebugEnabled()) {
            logger.debug(this + "onDisconnected(): disable timer");
        }
        timer.stop();
        timer = null;

        if (logger.isDebugEnabled()) {
            logger.debug(this + "onDisconnected(): release all blocks");
        }
        if (isBlocked()) {
            resume();
        }
        semaphore = null;

        if (released) {
            return;
        }

        if (logger.isDebugEnabled()) {
            logger.debug(this + "onDisconnected(): ending TCAP dialogue");
        }
        TCEndRequest message = this.tcapProvider.getDialogPrimitiveFactory().createEnd(this.tcapDialog);
        message.setTermination(TerminationType.Basic);
        try {
			this.tcapDialog.send(message);
		} catch (TCAPSendException e) {
			throw new RuntimeException(e);
		}
        //switch called and calling party addresses
//        SccpAddress calledPartyAddress = connectionID.getCallingPartyAddress();
//        SccpAddress callingPartyAddress = connectionID.getCalledPartyAddress();
//
//        try {
//            call.provider.send(calledPartyAddress, callingPartyAddress, message);
//        } catch (IOException e) {
//            logger.error("Network error", e);
//        }
        //call = null;
    }

    protected void notifyDisconnectImmediately(ArrayList listeners, JccConnectionEvent event) {
        int count = listeners.size();
        for (int i = 0; i < count; i++) {
            Object[] ls = (Object[]) listeners.get(i);
            JccConnectionListener listener = (JccConnectionListener) ls[0];
            listener.connectionDisconnected(event);
        }
    }

    protected void forceDisconnect() {
        this.released = true;
        JccConnectionEventImpl event = new JccConnectionEventImpl(
                JccConnectionEvent.CONNECTION_DISCONNECTED, this,
                JccConnectionEvent.CAUSE_TIMER_EXPIRY);
        try {
            onDisconnected();
            this.notifyDisconnectImmediately(call.connectionListeners, event);
            this.notifyDisconnectImmediately(call.provider.connectionListeners, event);
        } finally {
            close();
        }
    }

    protected void close() {
        if (logger.isDebugEnabled()) {
            logger.debug(this + "onDisconnected(): shutdown application event queue");
        }

        try {
            applicationEventQueue.shutdownNow();
            applicationEventQueue = null;
        } catch (Exception e) {
        }

        if (logger.isDebugEnabled()) {
            logger.debug(this + "onDisconnected(): shutdown signaling event queue");
        }

        try {
            signalingEventQueue.shutdownNow();
            signalingEventQueue = null;
        } catch (Exception e) {
        }

        if (logger.isDebugEnabled()) {
            logger.debug(this + "onDisconnected(): removing connection reference");
        }
        call.remove(this);
        call = null;
    }

    protected void fireConnectionEvent(JccConnectionEvent event) {
        fireConnectionEvent(call.connectionListeners, event);
        fireConnectionEvent(call.provider.connectionListeners, event);
    }

    private void fireConnectionEvent(ArrayList listeners, JccConnectionEvent event) {
        AbstractConnection connection = (AbstractConnection) event.getConnection();

        int count = listeners.size();
        for (int i = 0; i < count; i++) {
            Object[] ls = (Object[]) listeners.get(i);
            JccConnectionListener listener = (JccConnectionListener) ls[0];
            EventFilter filter = (EventFilter) ls[1];
            int disposition = filter.getEventDisposition(event);
            if (disposition == EventFilter.EVENT_BLOCK
                    && event.getID() == JccConnectionEvent.CONNECTION_DISCONNECTED) {
                disposition = EventFilter.EVENT_NOTIFY;
            }
            switch (disposition) {
                case EventFilter.EVENT_DISCARD:
                    if (logger.isDebugEnabled()) {
                        logger.debug(this + "fire event " + event + ", disposition=event_discard");
                    }
                    break;
                case EventFilter.EVENT_BLOCK:
                    if (logger.isDebugEnabled()) {
                        logger.debug(this + "fire event " + event + ", disposition=event_block");
                    }

                    try {
                        applicationEventQueue.execute(new EventProducer(listener, event));
                    } catch (InterruptedException e) {
                    }

                    block();
                    break;
                case EventFilter.EVENT_NOTIFY:
                    if (logger.isDebugEnabled()) {
                        logger.debug(this + "fire event " + event + ", disposition=event_notify");
                    }
                    try {
                        applicationEventQueue.execute(new EventProducer(listener, event));
                    } catch (InterruptedException e) {
                    }
                    break;
            }
        }
    }

    public static synchronized String getStateName(int state) {
        return (String) states.get(new Integer(state));
    }

    public static synchronized String getCauseName(int cause) {
        return (String) causes.get(new Integer(cause));
    }

    @Override
    public String toString() {
        return "(call_id=" + callID + ", address=" + address.toString() + ") ";
    }

    private class CancelTimeoutTask implements Runnable {

        private JccConnection connection;

        public CancelTimeoutTask(JccConnection connection) {
            this.connection = connection;
        }

        public void run() {
            logger.debug("Timer expired. state=" + getStateName(state) + ", Cancel call");

            if (isBlocked()) {
                resume();
            }

            JccConnectionEvent evt = new JccConnectionEventImpl(
                    JccConnectionEvent.CONNECTION_FAILED,
                    connection,
                    JccConnectionEvent.CAUSE_TIMER_EXPIRY);
            queueEvent(evt);
        }
    };
}
