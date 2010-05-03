/*
 * File Name     : JccConnectionEventImpl.java
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

import javax.csapi.cc.jcc.JccConnectionEvent;
import javax.csapi.cc.jcc.JccConnection;
import javax.csapi.cc.jcc.JccCall;
import org.mobicents.jcc.inap.address.JccCalledPartyBCDNumber;

/**
 * Implements JccConnectionEvent interface.
 *
 * @author Oleg Kulikov
 */
public class JccConnectionEventImpl implements JccConnectionEvent, Runnable {
    
    int id;
    private AbstractConnection connection;
    int cause;
    
    protected JccCalledPartyBCDNumber destAddress;
    
    /** 
     * Creates a new instance of JccConnectionEventImpl.
     *
     * @param id the id of this event.
     * @param connection the JccConnection associated with this event 
     * @param cause  the cause associated with this event.
     */
    public JccConnectionEventImpl(int id, JccConnection connection, int cause) {
        this.id = id;
        this.connection = (AbstractConnection)connection;
        this.cause = cause;
    }

    /**
     * (Non Java-doc).
     * @see javax.csapi.cc.jcc.JccConnectionEvent#getCall().
     */
    public JccCall getCall() {
        return connection.getCall();
    }

    /**
     * (Non Java-doc).
     * @see javax.csapi.cc.jcc.JccConnectionEvent#getCause().
     */
    public int getCause() {
        return cause;
    }

    /**
     * (Non Java-doc).
     * @see javax.csapi.cc.jcc.JccConnectionEvent#getConnection().
     */
    public JccConnection getConnection() {
        return connection;
    }

    /**
     * (Non Java-doc).
     * @see javax.csapi.cc.jcc.JccConnectionEvent#getID().
     */
    public int getID() {
        return id;
    }

    /**
     * (Non Java-doc).
     * @see javax.csapi.cc.jcc.JccConnectionEvent#getSource().
     */
    public Object getSource() {
        return connection;
    }
    
    public String toString() {
        switch (id) {
            case JccConnectionEvent.CONNECTION_ADDRESS_ANALYZE :
                return "javax.csapi.cc.jcc.CONNECTION_ADDRESS_ANALYZE";
            case JccConnectionEvent.CONNECTION_ADDRESS_COLLECT :
                return "javax.csapi.cc.jcc.CONNECTION_ADDRESS_COLLECT";
            case JccConnectionEvent.CONNECTION_ALERTING :
                return "javax.csapi.cc.jcc.CONNECTION_ALERTING";
            case JccConnectionEvent.CONNECTION_AUTHORIZE_CALL_ATTEMPT :
                return "javax.csapi.cc.jcc.CONNECTION_AUTHORIZE_CALL_ATTEMPT";
            case JccConnectionEvent.CONNECTION_CALL_DELIVERY :
                return "javax.csapi.cc.jcc.CONNECTION_CALL_DELIVERY";
            case JccConnectionEvent.CONNECTION_CONNECTED :
                return "javax.csapi.cc.jcc.CONNECTION_CONNECTED";
            case JccConnectionEvent.CONNECTION_CREATED :
                return "javax.csapi.cc.jcc.CONNECTION_CREATED";
            case JccConnectionEvent.CONNECTION_DISCONNECTED :
                return "javax.csapi.cc.jcc.CONNECTION_DISCONNECTED";
            case JccConnectionEvent.CONNECTION_FAILED :
                return "javax.csapi.cc.jcc.CONNECTION_FAILED";
            case JccConnectionEvent.CONNECTION_MID_CALL :
                return "javax.csapi.cc.jcc.CONNECTION_MID_CALL";
            default : 
                return "UNKNOWN";
        }
    }

    private void perform() {
        connection.cause = cause;
        switch (id) {
            case JccConnectionEvent.CONNECTION_CREATED :
                connection.state = JccConnection.IDLE;
                connection.onConnectionCreated();
                break;
            case JccConnectionEvent.CONNECTION_AUTHORIZE_CALL_ATTEMPT :
                connection.state = JccConnection.AUTHORIZE_CALL_ATTEMPT;
                connection.onAuthorizeCallAttempt();
                break;
            case JccConnectionEvent.CONNECTION_ADDRESS_COLLECT :
                connection.state = JccConnection.ADDRESS_COLLECT;
                connection.onAddressCollect();
                break;
            case JccConnectionEvent.CONNECTION_ADDRESS_ANALYZE :
                connection.state = JccConnection.ADDRESS_ANALYZE;
                connection.onAddressAnalyze((JccConnectionEventImpl) this);
                break;
            case JccConnectionEvent.CONNECTION_CALL_DELIVERY :
                connection.state = JccConnection.CALL_DELIVERY;
                connection.onCallDelivery();
                break;
            case JccConnectionEvent.CONNECTION_ALERTING :
                connection.state = JccConnection.ALERTING;
                connection.onAlerting();
                break;
            case JccConnectionEvent.CONNECTION_CONNECTED :
                connection.state = JccConnection.CONNECTED;
                connection.onConnected();
                break;
            case JccConnectionEvent.CONNECTION_FAILED :
                connection.state = JccConnection.FAILED;
                connection.onFailed();
                break;
            case JccConnectionEvent.CONNECTION_DISCONNECTED :
                connection.state = JccConnection.DISCONNECTED;
                connection.onDisconnected();
                break;
        }
        connection.fireConnectionEvent(this);
        
        if (connection.getState() == JccConnection.DISCONNECTED) {
            try {
                Thread.currentThread().sleep(3000);
            } catch (InterruptedException e) {
            } finally {
                connection.close();
            }
        }
    }
    
    public void run() {
        try {
            perform();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
