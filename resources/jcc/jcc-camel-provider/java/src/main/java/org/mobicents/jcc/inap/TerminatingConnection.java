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
import javax.csapi.cc.jcc.InvalidArgumentException;
import javax.csapi.cc.jcc.InvalidPartyException;
import javax.csapi.cc.jcc.InvalidStateException;
import javax.csapi.cc.jcc.JccAddress;
import javax.csapi.cc.jcc.JccConnection;
import javax.csapi.cc.jcc.JccConnectionEvent;
import javax.csapi.cc.jcc.JccEvent;
import javax.csapi.cc.jcc.MethodNotSupportedException;
import javax.csapi.cc.jcc.MidCallData;
import javax.csapi.cc.jcc.PrivilegeViolationException;
import javax.csapi.cc.jcc.ResourceUnavailableException;
import org.apache.log4j.Logger;
import org.mobicents.jcc.inap.address.JccCalledPartyNumber;
import org.mobicents.jcc.inap.protocol.ApplyCharging;
import org.mobicents.jcc.inap.protocol.CallInformationRequest;
import org.mobicents.jcc.inap.protocol.Connect;
import org.mobicents.jcc.inap.protocol.Continue;
import org.mobicents.jcc.inap.protocol.RequestBCSMState;
import org.mobicents.jcc.inap.protocol.parms.BCSMEvent;
import org.mobicents.jcc.inap.protocol.parms.CalledPartyNumber;
import org.mobicents.jcc.inap.protocol.parms.LegID;
import org.mobicents.jcc.inap.protocol.parms.RequestedInformationType;
import org.mobicents.jcc.inap.protocol.parms.RequestedInformationTypeList;
import org.mobicents.jcc.inap.protocol.tcap.Components;
import org.mobicents.jcc.inap.protocol.tcap.DialoguePortion;
import org.mobicents.jcc.inap.protocol.tcap.Invoke;
import org.mobicents.jcc.inap.protocol.tcap.TCContinue;
import org.mobicents.ss7.sccp.SccpAddress;

/**
 *
 * @author Oleg Kulikov
 */
public class TerminatingConnection extends AbstractConnection {
    
    private JccCalledPartyNumber destinationAddress;
    private String lastAddress;
    private String originalAddress;
    private String redirectedAddress;
    private JccAddress originatingAddress;
    
    
    /** Creates a new instance of TerminatingConnection */
    public TerminatingConnection(ConnectionID connectionID, JccCallImpl call, JccAddress address, JccAddress originatingAddress) {
        super(connectionID, call, address);
        logger = Logger.getLogger(TerminatingConnection.class);
        this.originatingAddress = originatingAddress;
        this.destinationAddress = (JccCalledPartyNumber) address;
    }
    
    public void onAuthorizeCallAttempt() {
        logger.info(this + "AUTHORIZE_CALL_ATTEMPT, " + getCauseName(cause));
        timer.reset(AUTH_TIMEOUT);
        
        JccEvent evt = new JccConnectionEventImpl(JccConnectionEvent.CONNECTION_CALL_DELIVERY, this, cause);
        queueEvent(evt);
    }
    
    public void onAddressCollect() {
        //never happened for terminating connection
    }
    
    public void onAddressAnalyze(JccConnectionEventImpl evt) {
        //never happened for termminated connection
    }
    
    public void onCallDelivery() {
        logger.info(this + "CALL_DELIVERY, " + getCauseName(cause));

        if (logger.isDebugEnabled()) {
            logger.debug(this + "onCallDelivery(): reset timeout timer");
        }
        timer.reset(CALL_DELIVERY_TIMEOUT);
        
        if (logger.isDebugEnabled()) {
            logger.debug(this + "onCallDelivery(): sending signaling message to MSC");
        }       
        
        CalledPartyNumber routeNumber = destinationAddress.getRouteAddress();
        if (logger.isDebugEnabled()) {
            logger.debug(this + "connecting to " + routeNumber);
        }
        
        Connect connect = new Connect(routeNumber);
        
        RequestBCSMState bcsm = new RequestBCSMState();
        bcsm.add(new BCSMEvent(BCSMEvent.T_ANSWER));
        bcsm.add(new BCSMEvent(BCSMEvent.T_NO_ANSWER));
        bcsm.add(new BCSMEvent(BCSMEvent.T_BUSY));
        bcsm.add(new BCSMEvent(BCSMEvent.T_ABANDON));
        
        //Aply charging
        ApplyCharging applyCharging = new ApplyCharging(
                LegID.SENDING_SIDE_ID,
                LegID.FIRST_LEG);
        
        RequestedInformationTypeList list = new RequestedInformationTypeList();
        list.add(RequestedInformationType.CALL_STOP_TIME);
        list.add(RequestedInformationType.CALL_CONNECTED_ELAPSED_TIME);
        list.add(RequestedInformationType.RELEASE_CAUSE);
        
        LegID legID = new LegID(LegID.SENDING_SIDE_ID, LegID.SECOND_LEG);
        CallInformationRequest cir = new CallInformationRequest(list, legID);
        
        Continue cont = new Continue();
        
        Components components = new Components();
        components.add(new Invoke(1, applyCharging));
        components.add(new Invoke(2, bcsm));
        components.add(new Invoke(3, cir));
        components.add(new Invoke(4, connect));
        //components.add(new Invoke(5, cont));
        
        TCContinue message = new TCContinue(connectionID.getId());
        message.setDialogue(new DialoguePortion());
        message.setComponents(components);
        
        //switch called and calling party addresses
        SccpAddress calledPartyAddress = connectionID.getCallingPartyAddress();
        SccpAddress callingPartyAddress = connectionID.getCalledPartyAddress();
        try {
            call.provider.send(calledPartyAddress, callingPartyAddress, message);
            
            JccEvent evt = new JccConnectionEventImpl(
                    JccConnectionEvent.CONNECTION_ALERTING,
                    this,
                    JccConnectionEvent.CAUSE_NORMAL);
            this.queueEvent(evt);
        } catch (IOException e) {
            logger.error("I/O Error", e);
            JccEvent evt = new JccConnectionEventImpl(
                    JccConnectionEvent.CONNECTION_FAILED,
                    this,
                    JccEvent.CAUSE_GENERAL_FAILURE);
            queueEvent(evt);
        }
    }
    
    public void onAlerting() {
        logger.info(this + "ALERTING");
        timer.reset(ALERTING_TIMEOUT);
    }
    
    public void onConnected() {
        logger.info(this + "CONNECTED, " + getCauseName(cause));

        if (logger.isDebugEnabled()) {
            logger.debug(this + "onConnected(): reset timeout timer");
        }
        timer.reset(CONNECTED_TIMEOUT);
        
        if (logger.isDebugEnabled()) {
            logger.debug(this + "onConnected(): sending signaling message to MSC");
        }
        RequestBCSMState bcsm = new RequestBCSMState();
        bcsm.add(new BCSMEvent(BCSMEvent.T_DISCONNECT, new LegID(LegID.SENDING_SIDE_ID, LegID.FIRST_LEG)));
        bcsm.add(new BCSMEvent(BCSMEvent.T_DISCONNECT, new LegID(LegID.SENDING_SIDE_ID, LegID.SECOND_LEG)));
        
        Continue cont = new Continue();
        Components components = new Components();
        components.add(new Invoke(1, bcsm));
        components.add(new Invoke(2, cont));
        
        TCContinue message = new TCContinue(connectionID.getId());
        //message.setDialogue(new DialoguePortion());
        message.setComponents(components);
        
        //switch called and calling party addresses
        SccpAddress calledPartyAddress = connectionID.getCallingPartyAddress();
        SccpAddress callingPartyAddress = connectionID.getCalledPartyAddress();
        try {
            call.provider.send(calledPartyAddress, callingPartyAddress, message);
        } catch (IOException e) {
            logger.error("I/O Error", e);
            JccEvent evt = new JccConnectionEventImpl(
                    JccConnectionEvent.CONNECTION_FAILED,
                    this,
                    JccEvent.CAUSE_GENERAL_FAILURE);
            queueEvent(evt);
        }
    }
    
    public void selectRoute(String digits) throws MethodNotSupportedException, InvalidStateException, ResourceUnavailableException, PrivilegeViolationException, InvalidPartyException {
        destinationAddress.setName(digits);
        if (isBlocked()) resume();
    }
    
    public void answer() throws PrivilegeViolationException, ResourceUnavailableException, InvalidStateException, MethodNotSupportedException {
    }
    
    public void attachMedia() throws PrivilegeViolationException, ResourceUnavailableException, InvalidStateException {
    }
    
    public void detachMedia() throws PrivilegeViolationException, ResourceUnavailableException, InvalidStateException {
    }
    
    public String getLastAddress() {
        return address.getName();
    }
    
    public String getOriginalAddress() {
        return address.getName();
    }
    
    public String getDestinationAddress() {
        return null;
    }
    
    public JccAddress getOriginatingAddress() {
        return originatingAddress;
    }
    
    public String getRedirectedAddress() {
        return redirectedAddress;
    }
    
    public void routeConnection(boolean b) throws InvalidStateException, ResourceUnavailableException, PrivilegeViolationException, MethodNotSupportedException, InvalidPartyException, InvalidArgumentException {
    }
    
    public MidCallData getMidCallData() throws InvalidStateException, ResourceUnavailableException, MethodNotSupportedException {
        return null;
    }
    
}
