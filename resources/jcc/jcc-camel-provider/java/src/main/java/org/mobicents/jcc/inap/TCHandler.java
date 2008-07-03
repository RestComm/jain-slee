/*
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

import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayInputStream;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import javax.csapi.cc.jcc.JccAddress;
import javax.csapi.cc.jcc.JccCall;
import javax.csapi.cc.jcc.JccConnectionEvent;

import javax.csapi.cc.jcc.JccEvent;
import javax.csapi.cc.jcc.JccConnection;
import org.mobicents.jcc.inap.address.JccCalledPartyBCDNumber;
import org.mobicents.jcc.inap.address.JccCalledPartyNumber;
import org.mobicents.jcc.inap.address.JccCallingPartyNumber;
import org.mobicents.jcc.inap.protocol.EventReportBCSM;
import org.mobicents.jcc.inap.protocol.InitialDP;
import org.mobicents.jcc.inap.protocol.Operation;
import org.mobicents.jcc.inap.protocol.parms.BCSMEvent;
import org.mobicents.jcc.inap.protocol.parms.CalledPartyBcdNumber;
import org.mobicents.jcc.inap.protocol.parms.CalledPartyNumber;
import org.mobicents.jcc.inap.protocol.parms.CallingPartyNumber;
import org.mobicents.jcc.inap.protocol.tcap.Components;
import org.mobicents.jcc.inap.protocol.tcap.Invoke;
import org.mobicents.jcc.inap.protocol.tcap.TCBegin;
import org.mobicents.jcc.inap.protocol.tcap.TCContinue;
import org.mobicents.jcc.inap.protocol.tcap.TCMessage;

import org.mobicents.ss7.sccp.SccpAddress;

import org.apache.log4j.Logger;

/**
 *
 * @author Oleg Kulikov
 */
public class TCHandler implements Runnable {
    
    private JccInapProviderImpl provider = null;
    
    private SccpAddress calledPartyAddress;
    private SccpAddress callingPartyAddress;
    
    private byte[] data;
    private TCMessage message;
    
    private static Logger logger = Logger.getLogger(TCHandler.class);
    
    /** Creates a new instance of Handler */
    public TCHandler(JccInapProviderImpl provider, SccpAddress calledPartyAddress,
            SccpAddress callingPartyAddress, TCMessage message) {
        this.provider = provider;
        this.calledPartyAddress = calledPartyAddress;
        this.callingPartyAddress = callingPartyAddress;
        this.message = message;
    }
    
    
    private boolean isOutgoingCall(InitialDP initialDP) {
        return initialDP.getCalledPartyBcdNumber() != null;
    }
    
    private boolean isOutAllowed(InitialDP initialDP) {
        if (initialDP.getCallingPartyNumber().getAddress().endsWith("9023629581")
        || initialDP.getCallingPartyNumber().getAddress().endsWith("9023802866")) {
            return true;
        } else return false;
    }
    
    private boolean isIncAllowed(InitialDP initialDP) {
        if ((initialDP.getCalledPartyNumber() != null && initialDP.getCalledPartyNumber().getAddress().endsWith("9023629581"))
        || (initialDP.getCalledPartyNumber() != null && initialDP.getCalledPartyNumber().getAddress().endsWith("9023629581"))) {
            return true;
        } else return false;
    }
    
    public void run() {
        long txID = message.getTxID();
        switch (message.getType()) {
            case TCMessage.BEGIN :
                TCBegin begin = (TCBegin) message;
                
                Invoke invoke = (Invoke) begin.getComponents().get(0);
                InitialDP initialDP = (InitialDP) invoke.getOperation();
                
                if (initialDP.getCallingPartyNumber() == null) {
                    logger.warn("txID = " + txID + ", [InitialDP] CallingPartyNumber missing");
                    return;
                }
                
//                if (!(isIncAllowed(initialDP) || isOutAllowed(initialDP))) {
//                    return;
//                }
                
                //creating calling party address
                CallingPartyNumber callingPartyNumber = initialDP.getCallingPartyNumber();
                JccAddress callingNumber = provider.createAddress(callingPartyNumber);
                
                //lookup or creating new call
                JccCallImpl call = provider.calls.containsKey(callingNumber.getName()) ?
                    provider.getCall(callingNumber) :
                    provider.createCall(callingNumber);
                
                ConnectionID connectionID = new ConnectionID(txID, calledPartyAddress, callingPartyAddress);
                JccAddress calledNumber = null;
                
                if (initialDP.getCalledPartyBcdNumber() != null) {
                    OriginatingConnection connection = new OriginatingConnection(
                            connectionID, call, callingNumber);
                    
                    //handle o_connection created event
                    JccEvent evt = new JccConnectionEventImpl(
                            JccConnectionEvent.CONNECTION_CREATED,
                            connection,
                            JccEvent.CAUSE_NEW_CALL);
                    connection.queueEvent(evt);
                    
                    
                    CalledPartyBcdNumber bcdNumber = initialDP.getCalledPartyBcdNumber();
                    calledNumber = provider.createAddress(bcdNumber);
                    
                    evt = new JccConnectionEventImpl(
                            JccConnectionEvent.CONNECTION_ADDRESS_ANALYZE,
                            connection,
                            JccEvent.CAUSE_NEW_CALL);
                    ((JccConnectionEventImpl) evt).destAddress = (JccCalledPartyBCDNumber) calledNumber;
                    connection.queueEvent(evt);
                } else if (initialDP.getCalledPartyNumber() != null) {
                    CalledPartyNumber calledPartyNumber = initialDP.getCalledPartyNumber();
                    calledNumber = provider.createAddress(calledPartyNumber);
                    
                    TerminatingConnection connection = new TerminatingConnection(
                            connectionID, call, calledNumber, callingNumber);
                    
                    //handle connection created event
                    JccEvent evt = new JccConnectionEventImpl(
                            JccConnectionEvent.CONNECTION_CREATED,
                            connection,
                            JccEvent.CAUSE_NEW_CALL);
                    connection.queueEvent(evt);
                    
                    //handle authorize_call_attempt event
                    evt = new JccConnectionEventImpl(
                            JccConnectionEvent.CONNECTION_AUTHORIZE_CALL_ATTEMPT,
                            connection,
                            JccEvent.CAUSE_NEW_CALL);
                    connection.queueEvent(evt);
                } else {
                    logger.warn("txID = " + txID + ", [InitialDP] either " +
                            "CalledPartyNumber or CalledPartyBCDNumber missing");
                }
                
                break;
            case TCMessage.CONTINUE :
                AbstractConnection connection = provider.getConnection(txID);
                if (connection == null) {
                    logger.warn("Unknown connection " + txID);
                    return;
                    //TODO send abort
                }
                
                TCContinue continueInd = (TCContinue) message;
                Components components = continueInd.getComponents();
                
                if (components == null) {
                    logger.warn("One or more components are missing");
                    return;
                }
                
                Enumeration elements = components.elements();
                while (elements.hasMoreElements()) {
                    invoke = (Invoke) elements.nextElement();
                    Operation operation = (Operation) invoke.getOperation();
                    switch (operation.getCode()) {
                        case Operation.EVENT_REPORT_BCSM :
                            EventReportBCSM bcsmEvent = (EventReportBCSM) operation;
                            switch (bcsmEvent.getEventType()) {
                                case BCSMEvent.O_ANSWER :
                                case BCSMEvent.T_ANSWER :
                                    JccConnectionEvent evt = new JccConnectionEventImpl(
                                            JccConnectionEvent.CONNECTION_CONNECTED,
                                            connection,
                                            JccConnectionEvent.CAUSE_NORMAL);
                                    connection.queueEvent(evt);
                                    break;
                                case BCSMEvent.O_DISCONNECT :
                                case BCSMEvent.T_DISCONNECT :
                                    evt = new JccConnectionEventImpl(
                                            JccConnectionEvent.CONNECTION_DISCONNECTED,
                                            connection,
                                            JccConnectionEvent.CAUSE_NORMAL);
                                    connection.queueEvent(evt);
                                    break;
                                case BCSMEvent.O_CALLED_PARTY_BUSY :
                                case BCSMEvent.T_BUSY :
                                    evt = new JccConnectionEventImpl(
                                            JccConnectionEvent.CONNECTION_DISCONNECTED,
                                            connection,
                                            JccConnectionEvent.CAUSE_BUSY);
                                    connection.queueEvent(evt);
                                    break;
                                case BCSMEvent.O_NO_ANSWER :
                                case BCSMEvent.T_NO_ANSWER :
                                    evt = new JccConnectionEventImpl(
                                            JccConnectionEvent.CONNECTION_DISCONNECTED,
                                            connection,
                                            JccConnectionEvent.CAUSE_NO_ANSWER);
                                    connection.queueEvent(evt);
                                    break;
                            }
                            break;
                        default:
                            if (logger.isDebugEnabled()) {
                                logger.debug("Ignoring");
                            }
                            break;
                    }
                }
                break;
            case TCMessage.ABORT :
                connection = provider.getConnection(txID);
                if (connection == null) {
                    logger.warn("Unknown connection " + txID);
                    return;
                }
                
                JccConnectionEvent evt = new JccConnectionEventImpl(
                        JccConnectionEvent.CONNECTION_FAILED,
                        connection,
                        JccEvent.CAUSE_CALL_CANCELLED);
                connection.queueEvent(evt);
        }
        
    }
    
}
