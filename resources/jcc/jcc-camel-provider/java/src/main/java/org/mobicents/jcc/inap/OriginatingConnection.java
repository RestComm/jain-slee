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
import javax.csapi.cc.jcc.JccConnectionEvent;
import javax.csapi.cc.jcc.JccEvent;
import javax.csapi.cc.jcc.MethodNotSupportedException;
import javax.csapi.cc.jcc.MidCallData;
import javax.csapi.cc.jcc.PrivilegeViolationException;
import javax.csapi.cc.jcc.ResourceUnavailableException;
import org.mobicents.jcc.inap.address.JccCalledPartyBCDNumber;
import org.mobicents.jcc.inap.address.JccCallingPartyNumber;
import org.mobicents.jcc.inap.protocol.ApplyCharging;
import org.mobicents.jcc.inap.protocol.CallInformationRequest;
import org.mobicents.jcc.inap.protocol.Connect;
import org.mobicents.jcc.inap.protocol.Continue;
import org.mobicents.jcc.inap.protocol.RequestBCSMState;
import org.mobicents.jcc.inap.protocol.parms.BCSMEvent;
import org.mobicents.jcc.inap.protocol.parms.CalledPartyNumber;
import org.mobicents.jcc.inap.protocol.parms.CallingPartyNumber;
import org.mobicents.jcc.inap.protocol.parms.LegID;
import org.mobicents.jcc.inap.protocol.parms.RequestedInformationType;
import org.mobicents.jcc.inap.protocol.parms.RequestedInformationTypeList;
import org.mobicents.jcc.inap.protocol.tcap.Components;
import org.mobicents.jcc.inap.protocol.tcap.DialoguePortion;
import org.mobicents.jcc.inap.protocol.tcap.Invoke;
import org.mobicents.jcc.inap.protocol.tcap.TCContinue;
import org.mobicents.ss7.sccp.SccpAddress;

import org.apache.log4j.Logger;

/**
 *
 * @author Oleg Kulikov
 */
public class OriginatingConnection extends AbstractConnection {
    
    private JccCalledPartyBCDNumber destinationAddress;
    private String lastAddress;
    private String originalAddress;
    private String redirectedAddress;
    
    
    /** Creates a new instance of OriginatingConnection */
    public OriginatingConnection(ConnectionID connectionID, JccCallImpl call, JccAddress address) {
        super(connectionID, call, address);
        logger = Logger.getLogger(OriginatingConnection.class);
    }
    
    /**
     * (Non Java-doc).
     * @see javax.csapi.cc.jcc.JccConnection#selectRoute().
     */
    public void selectRoute(String digits) throws MethodNotSupportedException, InvalidStateException, ResourceUnavailableException, PrivilegeViolationException, InvalidPartyException {
        destinationAddress.setName(digits);
        if (isBlocked()) {
            resume();
        }
    }
    
    /**
     * (Non Java-doc).
     * @see javax.csapi.cc.jcc.JccConnection#answer().
     */
    public void answer() throws PrivilegeViolationException, ResourceUnavailableException, InvalidStateException, MethodNotSupportedException {
        throw new MethodNotSupportedException();
    }
    
    /**
     * (Non Java-doc).
     * @see javax.csapi.cc.jcc.JccConnection#attachMedia().
     */
    public void attachMedia() throws PrivilegeViolationException, ResourceUnavailableException, InvalidStateException {
    }
    
    /**
     * (Non Java-doc).
     * @see javax.csapi.cc.jcc.JccConnection#detachMedia().
     */
    public void detachMedia() throws PrivilegeViolationException, ResourceUnavailableException, InvalidStateException {
    }
    
    /**
     * (Non Java-doc).
     * @see javax.csapi.cc.jcc.JccConnection#getLastAddress().
     */
    public String getLastAddress() {
        return null;
    }
    
    /**
     * (Non Java-doc).
     * @see javax.csapi.cc.jcc.JccConnection#getOriginalAddress().
     */
    public String getOriginalAddress() {
        return null;
    }
    
    /**
     * (Non Java-doc).
     * @see javax.csapi.cc.jcc.JccConnection#getDestinationAddress().
     */
    public String getDestinationAddress() {
        return destinationAddress == null ? null: destinationAddress.getName();
    }
    
    /**
     * (Non Java-doc).
     * @see javax.csapi.cc.jcc.JccConnection#getOriginatingAddress().
     */
    public JccAddress getOriginatingAddress() {
        return address;
    }
    
    /**
     * (Non Java-doc).
     * @see javax.csapi.cc.jcc.JccConnection#getRedirectingAddress().
     */
    public String getRedirectedAddress() {
        return null;
    }
    
    /**
     * (Non Java-doc).
     * @see javax.csapi.cc.jcc.JccConnection#routeConnection().
     */
    public void routeConnection(boolean b) throws InvalidStateException, ResourceUnavailableException, PrivilegeViolationException, MethodNotSupportedException, InvalidPartyException, InvalidArgumentException {
    }
    
    /**
     * (Non Java-doc).
     * @see javax.csapi.cc.jcc.JccConnection#getMidCallData().
     */
    public MidCallData getMidCallData() throws InvalidStateException, ResourceUnavailableException, MethodNotSupportedException {
        return null;
    }
    
    
    public void onAuthorizeCallAttempt() {
        //should never happen
    }
    
    public void onAddressCollect() {
    }
    
    public void onAddressAnalyze(JccConnectionEventImpl evt) {
        logger.info(this + "ADDRESS_ANALYZE, " + getCauseName(cause));
        
        if (logger.isDebugEnabled()) {
            logger.debug(this + "onAddressAnalyze(): reset timeout timer");
        }
        timer.reset(ADDRESS_ANALYZE_TIMEOUT);
        
        destinationAddress = evt.destAddress;
        JccEvent event = new JccConnectionEventImpl(
                JccConnectionEvent.CONNECTION_CALL_DELIVERY,
                this, 
                JccConnectionEvent.CAUSE_NORMAL);
        queueEvent(event);
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
        
        CalledPartyNumber routeNumber = destinationAddress.getRouteNumber();
        if (logger.isDebugEnabled()) {
            logger.debug(this + "connecting to " + routeNumber);
        }
        
        JccCallingPartyNumber cli = (JccCallingPartyNumber) getAddress();
        cli.setCLI("777");
        
        Connect connect = new Connect(routeNumber);
//        connect.setGenericNumber(cli.getGenericNumber());
        
        RequestBCSMState bcsm = new RequestBCSMState();
        bcsm.add(new BCSMEvent(BCSMEvent.O_ANSWER));
        bcsm.add(new BCSMEvent(BCSMEvent.O_NO_ANSWER));
        bcsm.add(new BCSMEvent(BCSMEvent.O_CALLED_PARTY_BUSY));
        bcsm.add(new BCSMEvent(BCSMEvent.O_ABANDON));
        
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
        //never happen
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
        bcsm.add(new BCSMEvent(BCSMEvent.O_DISCONNECT, new LegID(LegID.SENDING_SIDE_ID, LegID.FIRST_LEG)));
        bcsm.add(new BCSMEvent(BCSMEvent.O_DISCONNECT, new LegID(LegID.SENDING_SIDE_ID, LegID.SECOND_LEG)));
        
        Continue cont = new Continue();
        Components components = new Components();
        components.add(new Invoke(1, bcsm));
        components.add(new Invoke(2, cont));
        
        TCContinue message = new TCContinue(connectionID.getId());
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
}
