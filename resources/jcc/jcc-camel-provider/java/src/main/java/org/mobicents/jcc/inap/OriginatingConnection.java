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
import org.mobicents.jcc.inap.protocol.Operation;
import org.mobicents.jcc.inap.protocol.RequestBCSMState;
import org.mobicents.jcc.inap.protocol.parms.BCSMEvent;
import org.mobicents.jcc.inap.protocol.parms.CalledPartyNumber;
import org.mobicents.jcc.inap.protocol.parms.LegID;
import org.mobicents.jcc.inap.protocol.parms.RequestedInformationType;
import org.mobicents.jcc.inap.protocol.parms.RequestedInformationTypeList;


import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.api.TCAPProvider;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCContinueRequest;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;

/**
 *
 * @author Oleg Kulikov
 */
public class OriginatingConnection extends AbstractConnection {

    private JccCalledPartyBCDNumber destinationAddress;
    private String lastAddress;
    private String originalAddress;
    private String redirectedAddress;
    private static final Logger logger = Logger.getLogger(OriginatingConnection.class);

    /** Creates a new instance of OriginatingConnection */
    public OriginatingConnection(ConnectionID connectionID, JccCallImpl call, JccAddress address,TCAPProvider tcapProvider,Dialog tcapDialog) {
        super(connectionID, call, address, tcapProvider, tcapDialog);
    }

    /**
     * (Non Java-doc).
     * @see javax.csapi.cc.jcc.JccConnection#selectRoute().
     */
    public void selectRoute(String digits) throws MethodNotSupportedException, InvalidStateException, ResourceUnavailableException, PrivilegeViolationException, InvalidPartyException {
        //we assume that format of the digits may be as follows:
        //destination#genericNumber which can be used to represent callerID
        String tokens[] = digits.split("#");
        destinationAddress.setName(tokens[0]);

        if (tokens.length > 1) {
            ((JccCallingPartyNumber) address).setCallerID(tokens[1]);
        }

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
        return destinationAddress == null ? null : destinationAddress.getName();
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


        Connect connect = new Connect(routeNumber);

        //assign callerID
        JccCallingPartyNumber cpn = (JccCallingPartyNumber) getAddress();
        if (cpn.getGenericNumber() != null) {
            connect.setGenericNumber(cpn.getGenericNumber());
        }

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
        try {
        Invoke invoke = super.tcapProvider.getComponentPrimitiveFactory().createTCInvokeRequest();
        invoke.setInvokeId(super.tcapDialog.getNewInvokeId());
        
        //components.add(new Invoke(1, applyCharging));
        OperationCode oc = super.tcapProvider.getComponentPrimitiveFactory().createOperationCode(false, new Long(Operation.APPLY_CHARGING));
        invoke.setOperationCode(oc);
        //now set apply charging.
        Parameter parameter = super.tcapProvider.getComponentPrimitiveFactory().createParameter();
        parameter.setPrimitive(ApplyCharging._IS_PRIMITIVE);
        parameter.setTag(ApplyCharging._TAG);
        parameter.setTagClass(ApplyCharging._TAG_CLASS);
        parameter.setData(applyCharging.toByteArray());
        invoke.setParameter(parameter);
        tcapDialog.sendComponent(invoke);
        
        //components.add(new Invoke(2, bcsm));
        invoke = super.tcapProvider.getComponentPrimitiveFactory().createTCInvokeRequest();
        invoke.setInvokeId(super.tcapDialog.getNewInvokeId());
        
        oc = super.tcapProvider.getComponentPrimitiveFactory().createOperationCode(false, new Long(Operation.REQUEST_REPORT_BCSM_EVENT));
        invoke.setOperationCode(oc);
        parameter = super.tcapProvider.getComponentPrimitiveFactory().createParameter();
        parameter.setPrimitive(RequestBCSMState._IS_PRIMITIVE);
        parameter.setTag(RequestBCSMState._TAG);
        parameter.setTagClass(RequestBCSMState._TAG_CLASS);
        parameter.setData(bcsm.toByteArray());
        invoke.setParameter(parameter);
        tcapDialog.sendComponent(invoke);
        
        
        //components.add(new Invoke(3, cir));
        invoke = super.tcapProvider.getComponentPrimitiveFactory().createTCInvokeRequest();
        invoke.setInvokeId(super.tcapDialog.getNewInvokeId());
        
        oc = super.tcapProvider.getComponentPrimitiveFactory().createOperationCode(false, new Long(Operation.CALL_INFORMATION_REQUEST));
        invoke.setOperationCode(oc);
        parameter = super.tcapProvider.getComponentPrimitiveFactory().createParameter();
        parameter.setPrimitive(CallInformationRequest._IS_PRIMITIVE);
        parameter.setTag(CallInformationRequest._TAG);
        parameter.setTagClass(CallInformationRequest._TAG_CLASS);
        parameter.setData(cir.toByteArray());
        invoke.setParameter(parameter);
        tcapDialog.sendComponent(invoke);
        
        
        //components.add(new Invoke(4, connect));
        invoke = super.tcapProvider.getComponentPrimitiveFactory().createTCInvokeRequest();
        invoke.setInvokeId(super.tcapDialog.getNewInvokeId());
        oc = super.tcapProvider.getComponentPrimitiveFactory().createOperationCode(false, new Long(Operation.CONNECT));
        invoke.setOperationCode(oc);
        parameter = super.tcapProvider.getComponentPrimitiveFactory().createParameter();
        parameter.setPrimitive(Connect._IS_PRIMITIVE);
        parameter.setTag(Connect._TAG);
        parameter.setTagClass( Connect._TAG_CLASS);
        parameter.setData(connect.toByteArray());
        invoke.setParameter(parameter);
        tcapDialog.sendComponent(invoke);
        //components.add(new Invoke(5, cont));

        TCContinueRequest continueRequest = this.tcapProvider.getDialogPrimitiveFactory().createContinue(this.tcapDialog);
        
        	this.tcapDialog.send(continueRequest);

        //switch called and calling party addresses
       // SccpAddress calledPartyAddress = connectionID.getCallingPartyAddress();
       // SccpAddress callingPartyAddress = connectionID.getCalledPartyAddress();

        
           // call.provider.send(calledPartyAddress, callingPartyAddress, message);
        } catch (Exception e) {
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
        try {
        timer.reset(CONNECTED_TIMEOUT);

        if (logger.isDebugEnabled()) {
            logger.debug(this + "onConnected(): sending signaling message to MSC");
        }
        RequestBCSMState bcsm = new RequestBCSMState();
        bcsm.add(new BCSMEvent(BCSMEvent.O_DISCONNECT, new LegID(LegID.SENDING_SIDE_ID, LegID.FIRST_LEG)));
        bcsm.add(new BCSMEvent(BCSMEvent.O_DISCONNECT, new LegID(LegID.SENDING_SIDE_ID, LegID.SECOND_LEG)));

        Continue cont = new Continue();
        //Components components = new Components();
        //components.add(new Invoke(1, bcsm));
        Invoke invoke = super.tcapProvider.getComponentPrimitiveFactory().createTCInvokeRequest();
        invoke.setInvokeId(super.tcapDialog.getNewInvokeId());
        
        OperationCode oc = super.tcapProvider.getComponentPrimitiveFactory().createOperationCode(false, new Long(Operation.REQUEST_REPORT_BCSM_EVENT));
        invoke.setOperationCode(oc);
        Parameter parameter = super.tcapProvider.getComponentPrimitiveFactory().createParameter();
        parameter.setPrimitive(RequestBCSMState._IS_PRIMITIVE);
        parameter.setTag(RequestBCSMState._TAG);
        parameter.setTagClass(RequestBCSMState._TAG_CLASS);
        parameter.setData(bcsm.toByteArray());
        tcapDialog.sendComponent(invoke);
        //components.add(new Invoke(2, cont));
        //continue does not have parameter
        
        invoke = super.tcapProvider.getComponentPrimitiveFactory().createTCInvokeRequest();
        invoke.setInvokeId(super.tcapDialog.getNewInvokeId());
        
        oc = super.tcapProvider.getComponentPrimitiveFactory().createOperationCode(false, new Long(Operation.CONTINUE));
        invoke.setOperationCode(oc);
        tcapDialog.sendComponent(invoke);
        TCContinueRequest message = this.tcapProvider.getDialogPrimitiveFactory().createContinue(this.tcapDialog);
        this.tcapDialog.send(message);

        //switch called and calling party addresses
       // SccpAddress calledPartyAddress = connectionID.getCallingPartyAddress();
        //SccpAddress callingPartyAddress = connectionID.getCalledPartyAddress();
        
            //call.provider.send(calledPartyAddress, callingPartyAddress, message);
        } catch (Exception e) {
            logger.error("I/O Error", e);
            JccEvent evt = new JccConnectionEventImpl(
                    JccConnectionEvent.CONNECTION_FAILED,
                    this,
                    JccEvent.CAUSE_GENERAL_FAILURE);
            queueEvent(evt);
        }
    }
}
