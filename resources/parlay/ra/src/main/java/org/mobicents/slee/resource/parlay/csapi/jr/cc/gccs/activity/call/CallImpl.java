
package org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.activity.call;

import javax.slee.resource.ActivityHandle;
import javax.slee.resource.ResourceException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csapi.P_INVALID_ADDRESS;
import org.csapi.P_INVALID_CRITERIA;
import org.csapi.P_INVALID_EVENT_TYPE;
import org.csapi.P_INVALID_NETWORK_STATE;
import org.csapi.P_INVALID_SESSION_ID;
import org.csapi.P_UNSUPPORTED_ADDRESS_PLAN;
import org.csapi.TpAddress;
import org.csapi.TpAoCInfo;
import org.csapi.TpCommonExceptions;
import org.csapi.cc.TpCallChargePlan;
import org.csapi.cc.TpCallError;
import org.csapi.cc.gccs.IpCall;
import org.csapi.cc.gccs.TpCallAppInfo;
import org.csapi.cc.gccs.TpCallEndedReport;
import org.csapi.cc.gccs.TpCallFault;
import org.csapi.cc.gccs.TpCallInfoReport;
import org.csapi.cc.gccs.TpCallReleaseCause;
import org.csapi.cc.gccs.TpCallReport;
import org.csapi.cc.gccs.TpCallReportRequest;
import org.mobicents.csapi.jr.slee.cc.gccs.CallEndedEvent;
import org.mobicents.csapi.jr.slee.cc.gccs.CallFaultDetectedEvent;
import org.mobicents.csapi.jr.slee.cc.gccs.GetCallInfoErrEvent;
import org.mobicents.csapi.jr.slee.cc.gccs.GetCallInfoResEvent;
import org.mobicents.csapi.jr.slee.cc.gccs.GetMoreDialledDigitsErrEvent;
import org.mobicents.csapi.jr.slee.cc.gccs.GetMoreDialledDigitsResEvent;
import org.mobicents.csapi.jr.slee.cc.gccs.RouteErrEvent;
import org.mobicents.csapi.jr.slee.cc.gccs.RouteResEvent;
import org.mobicents.csapi.jr.slee.cc.gccs.SuperviseCallErrEvent;
import org.mobicents.csapi.jr.slee.cc.gccs.SuperviseCallResEvent;
import org.mobicents.csapi.jr.slee.cc.gccs.TpCallIdentifier;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.GccsListener;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.TpCallActivityHandle;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.activity.callcontrolmanager.CallControlManager;
import org.mobicents.slee.resource.parlay.util.ParlayExceptionUtil;
import org.mobicents.slee.resource.parlay.util.activity.ActivityManager;


/**
 * 
 *
 */
public class CallImpl implements Call {
    /*
     * Log4J Logger for this class.
     */
    private static final Log logger = LogFactory.getLog(CallImpl.class);
    
    private static final String CALL_NOT_VALID = "Call session is no longer valid.";

    private transient CallControlManager callControlManager;
    
    private final transient IpCall ipCall;
    
    private final transient int callSessionID;   

    private final transient TpCallIdentifier tpCallIdentifier;

    private final transient ActivityManager activityManager;

    private transient GccsListener eventListener;

    private final transient ActivityHandle activityHandle;

    
    public CallImpl(CallControlManager manager, IpCall ipCall, int callSessionID,
            TpCallIdentifier tpCallIdenitifier, ActivityManager activityManager, GccsListener eventListener) {
        super();
        this.callControlManager = manager;
        this.ipCall = ipCall;
        this.callSessionID = callSessionID;
        this.tpCallIdentifier = tpCallIdenitifier;
        this.activityManager = activityManager;
        this.activityHandle = new TpCallActivityHandle(tpCallIdentifier);
        this.eventListener = eventListener;
    }
    
    public void init() {
    }
    
    public void dispose() {

        synchronized (this) {
            if (callControlManager != null) {
                callControlManager.removeCall(callSessionID);
                callControlManager = null;
            }

            eventListener = null;
        }
        
    }

    /* (non-Javadoc)
     * @see org.csapi.cc.gccs.IpAppCallOperations#routeRes(int, org.csapi.cc.gccs.TpCallReport, int)
     */
    public void routeRes(final int callSessionID, final TpCallReport eventReport, final int callLegSessionID) {
        final IpCall ipCall = getIpCall(); 

        if (ipCall != null) {
            final RouteResEvent event = new RouteResEvent(
                    callControlManager.getTpServiceIdentifier(),
                    tpCallIdentifier, eventReport, callLegSessionID);

            eventListener.onRouteResEvent(event);
        }
        
    }

    /* (non-Javadoc)
     * @see org.csapi.cc.gccs.IpAppCallOperations#routeErr(int, org.csapi.cc.TpCallError, int)
     */
    public void routeErr(final int callSessionID, final TpCallError errorIndication, final int callLegSessionID) {
        final IpCall ipCall = getIpCall();

        if (ipCall != null) {
            final RouteErrEvent event = new RouteErrEvent(
                    callControlManager.getTpServiceIdentifier(),
                    tpCallIdentifier, errorIndication, callLegSessionID);

            eventListener.onRouteErrEvent(event);
        }
    }

    /* (non-Javadoc)
     * @see org.csapi.cc.gccs.IpAppCallOperations#getCallInfoRes(int, org.csapi.cc.gccs.TpCallInfoReport)
     */
    public void getCallInfoRes(final int callSessionID, final TpCallInfoReport callInfoReport) {
        final IpCall ipCall = getIpCall();

        if (ipCall != null) {
            final GetCallInfoResEvent event = new GetCallInfoResEvent(
                    callControlManager.getTpServiceIdentifier(),
                    tpCallIdentifier, callInfoReport);

            eventListener.onGetCallInfoResEvent(event);
        }
    }

    /* (non-Javadoc)
     * @see org.csapi.cc.gccs.IpAppCallOperations#getCallInfoErr(int, org.csapi.cc.TpCallError)
     */
    public void getCallInfoErr(final int callSessionID, final TpCallError errorIndication) {
        final IpCall ipCall = getIpCall();

        if (ipCall != null) {
            final GetCallInfoErrEvent event = new GetCallInfoErrEvent(
                    callControlManager.getTpServiceIdentifier(),
                    tpCallIdentifier, errorIndication);

            eventListener.onGetCallInfoErrEvent(event);
        }
    }

    /* (non-Javadoc)
     * @see org.csapi.cc.gccs.IpAppCallOperations#superviseCallRes(int, int, int)
     */
    public void superviseCallRes(final int callSessionID, final int report, final int usedTime) {
        final IpCall ipCall = getIpCall();

        if (ipCall != null) {
            final SuperviseCallResEvent event = new SuperviseCallResEvent(
                    callControlManager.getTpServiceIdentifier(),
                    tpCallIdentifier, report, usedTime);

            eventListener.onSuperviseCallResEvent(event);
        }
    }

    /* (non-Javadoc)
     * @see org.csapi.cc.gccs.IpAppCallOperations#superviseCallErr(int, org.csapi.cc.TpCallError)
     */
    public void superviseCallErr(final int callSessionID, final TpCallError errorIndication) {
        final IpCall ipCall = getIpCall();

        if (ipCall != null) {
            final SuperviseCallErrEvent event = new SuperviseCallErrEvent(
                    callControlManager.getTpServiceIdentifier(),
                    tpCallIdentifier, errorIndication);

            eventListener.onSuperviseCallErrEvent(event);
        }
    }

    /* (non-Javadoc)
     * @see org.csapi.cc.gccs.IpAppCallOperations#callFaultDetected(int, org.csapi.cc.gccs.TpCallFault)
     */
    public void callFaultDetected(final int callSessionID, final TpCallFault fault) {
        final IpCall ipCall = getIpCall();

        if (ipCall != null) {
            final CallFaultDetectedEvent event = new CallFaultDetectedEvent(
                    callControlManager.getTpServiceIdentifier(),
                    tpCallIdentifier, fault);

            eventListener.onCallFaultDetectedEvent(event);
        }
        activityManager.remove(activityHandle, tpCallIdentifier);
        
        activityManager.activityEnding(activityHandle);

        dispose();
    }

    /* (non-Javadoc)
     * @see org.csapi.cc.gccs.IpAppCallOperations#getMoreDialledDigitsRes(int, java.lang.String)
     */
    public void getMoreDialledDigitsRes(final int callSessionID, final String digits) {
        final IpCall ipCall = getIpCall();

        if (ipCall != null) {
            final GetMoreDialledDigitsResEvent event = new GetMoreDialledDigitsResEvent(
                    callControlManager.getTpServiceIdentifier(),
                    tpCallIdentifier, digits);

            eventListener.onGetMoreDialledDigitsResEvent(event);
        }
    }

    /* (non-Javadoc)
     * @see org.csapi.cc.gccs.IpAppCallOperations#getMoreDialledDigitsErr(int, org.csapi.cc.TpCallError)
     */
    public void getMoreDialledDigitsErr(final int callSessionID, final TpCallError errorIndication) {
        final IpCall ipCall = getIpCall();

        if (ipCall != null) {
            final GetMoreDialledDigitsErrEvent event = new GetMoreDialledDigitsErrEvent(
                    callControlManager.getTpServiceIdentifier(),
                    tpCallIdentifier, errorIndication);

            eventListener.onGetMoreDialledDigitsErrEvent(event);
        }
    }

    /* (non-Javadoc)
     * @see org.csapi.cc.gccs.IpAppCallOperations#callEnded(int, org.csapi.cc.gccs.TpCallEndedReport)
     */
    public void callEnded(final int callSessionID, final TpCallEndedReport report) {
        final IpCall ipCall = getIpCall();

        if (ipCall != null) {
            final CallEndedEvent event = new CallEndedEvent(
                    callControlManager.getTpServiceIdentifier(),
                    tpCallIdentifier, report);

            eventListener.onCallEndedEvent(event);
        }
    }

    /* (non-Javadoc)
     * @see org.mobicents.csapi.jr.slee.cc.gccs.IpCallConnection#routeReq(org.csapi.cc.gccs.TpCallReportRequest[], org.csapi.TpAddress, org.csapi.TpAddress, org.csapi.TpAddress, org.csapi.TpAddress, org.csapi.cc.gccs.TpCallAppInfo[])
     */
    public int routeReq(final TpCallReportRequest[] responseRequested, final TpAddress targetAddress, final TpAddress originatingAddress, final TpAddress originalDestinationAddress, final TpAddress redirectingAddress, final TpCallAppInfo[] appInfo) throws TpCommonExceptions, P_INVALID_ADDRESS, P_UNSUPPORTED_ADDRESS_PLAN, P_INVALID_NETWORK_STATE, P_INVALID_CRITERIA, P_INVALID_EVENT_TYPE, ResourceException {
        final IpCall call = getIpCall();

        int sessionID = 0;

        try {
            sessionID = call.routeReq(callSessionID, responseRequested, targetAddress, originatingAddress, originalDestinationAddress, redirectingAddress, appInfo);
        } catch (P_INVALID_SESSION_ID e) {
//          If this happens call must have finished on gateway and we
            // haven't processed
            // the event yet.
            logger.warn(CALL_NOT_VALID + ParlayExceptionUtil.stringify(e));
            throw new ResourceException(CALL_NOT_VALID);
        }  
        
        return sessionID;
    }

    /* (non-Javadoc)
     * @see org.mobicents.csapi.jr.slee.cc.gccs.IpCallConnection#release(org.csapi.cc.gccs.TpCallReleaseCause)
     */
    public void release(final TpCallReleaseCause cause) throws TpCommonExceptions, P_INVALID_NETWORK_STATE, ResourceException {
        final IpCall call = getIpCall();
        
        try {
            call.release(callSessionID, cause);
        } catch (P_INVALID_SESSION_ID e) {
            // If this happens call must have finished on gateway and we
            // haven't processed
            // the event yet.
            logger.warn(CALL_NOT_VALID + ParlayExceptionUtil.stringify(e));
            throw new ResourceException(CALL_NOT_VALID);
        }
        
        
        activityManager.remove(activityHandle, tpCallIdentifier);
        
        activityManager.activityEnding(activityHandle);

        dispose();
        
    }

    /* (non-Javadoc)
     * @see org.mobicents.csapi.jr.slee.cc.gccs.IpCallConnection#deassignCall()
     */
    public void deassignCall() throws TpCommonExceptions, ResourceException {
        final IpCall call = getIpCall();
        
        try {
            call.deassignCall(callSessionID);
        } catch (P_INVALID_SESSION_ID e) {
//          If this happens call must have finished on gateway and we
            // haven't processed
            // the event yet.
            logger.warn(CALL_NOT_VALID + ParlayExceptionUtil.stringify(e));
            throw new ResourceException(CALL_NOT_VALID);
        }
        
        activityManager.remove(activityHandle, tpCallIdentifier);
        
        activityManager.activityEnding(activityHandle);

        dispose();
        
    }

    /* (non-Javadoc)
     * @see org.mobicents.csapi.jr.slee.cc.gccs.IpCallConnection#getCallInfoReq(int)
     */
    public void getCallInfoReq(final int callInfoRequested) throws TpCommonExceptions, ResourceException {
        final IpCall call = getIpCall();
        
        try {
            call.getCallInfoReq(callSessionID, callInfoRequested);
        } catch (P_INVALID_SESSION_ID e) {
//          If this happens call must have finished on gateway and we
            // haven't processed
            // the event yet.
            logger.warn(CALL_NOT_VALID + ParlayExceptionUtil.stringify(e));
            throw new ResourceException(CALL_NOT_VALID);
        }
        
    }

    /* (non-Javadoc)
     * @see org.mobicents.csapi.jr.slee.cc.gccs.IpCallConnection#setCallChargePlan(org.csapi.cc.TpCallChargePlan)
     */
    public void setCallChargePlan(final TpCallChargePlan callChargePlan) throws TpCommonExceptions, ResourceException {
        final IpCall call = getIpCall();
        
        try {
            call.setCallChargePlan(callSessionID, callChargePlan);
        } catch (P_INVALID_SESSION_ID e) {
//          If this happens call must have finished on gateway and we
            // haven't processed
            // the event yet.
            logger.warn(CALL_NOT_VALID + ParlayExceptionUtil.stringify(e));
            throw new ResourceException(CALL_NOT_VALID);
        }
    }

    /* (non-Javadoc)
     * @see org.mobicents.csapi.jr.slee.cc.gccs.IpCallConnection#setAdviceOfCharge(org.csapi.TpAoCInfo, int)
     */
    public void setAdviceOfCharge(final TpAoCInfo aOCInfo, final int tariffSwitch) throws TpCommonExceptions, ResourceException {
        final IpCall call = getIpCall();
        
        try {
            call.setAdviceOfCharge(callSessionID, aOCInfo, tariffSwitch);
        } catch (P_INVALID_SESSION_ID e) {
//          If this happens call must have finished on gateway and we
            // haven't processed
            // the event yet.
            logger.warn(CALL_NOT_VALID + ParlayExceptionUtil.stringify(e));
            throw new ResourceException(CALL_NOT_VALID);
        }
        
    }

    /* (non-Javadoc)
     * @see org.mobicents.csapi.jr.slee.cc.gccs.IpCallConnection#getMoreDialledDigitsReq(int)
     */
    public void getMoreDialledDigitsReq(final int length) throws TpCommonExceptions, ResourceException {
        final IpCall call = getIpCall();
        
        try {
            call.getMoreDialledDigitsReq(callSessionID, length);
        } catch (P_INVALID_SESSION_ID e) {
//          If this happens call must have finished on gateway and we
            // haven't processed
            // the event yet.
            logger.warn(CALL_NOT_VALID + ParlayExceptionUtil.stringify(e));
            throw new ResourceException(CALL_NOT_VALID);
        }
        
    }

    /* (non-Javadoc)
     * @see org.mobicents.csapi.jr.slee.cc.gccs.IpCallConnection#superviseCallReq(int, int)
     */
    public void superviseCallReq(final int time, final int treatment) throws TpCommonExceptions, ResourceException {
        final IpCall call = getIpCall();
        
        try {
            call.superviseCallReq(callSessionID, time, treatment);
        } catch (P_INVALID_SESSION_ID e) {
//          If this happens call must have finished on gateway and we
            // haven't processed
            // the event yet.
            logger.warn(CALL_NOT_VALID + ParlayExceptionUtil.stringify(e));
            throw new ResourceException(CALL_NOT_VALID);
        }
    }

    /* (non-Javadoc)
     * @see org.mobicents.csapi.jr.slee.cc.gccs.IpCallConnection#continueProcessing()
     */
    public void continueProcessing() throws TpCommonExceptions, P_INVALID_NETWORK_STATE, ResourceException {
        final IpCall call = getIpCall();
        
        try {
            call.continueProcessing(callSessionID);
        } catch (P_INVALID_SESSION_ID e) {
//          If this happens call must have finished on gateway and we
            // haven't processed
            // the event yet.
            logger.warn(CALL_NOT_VALID + ParlayExceptionUtil.stringify(e));
            throw new ResourceException(CALL_NOT_VALID);
        }
    }

    /* (non-Javadoc)
     * @see org.mobicents.csapi.jr.slee.IpServiceConnection#closeConnection()
     */
    public void closeConnection() throws ResourceException {
        //should never be called
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.activity.call.Call#getIpAppCall()
     */
    public IpCall getIpCall() {
        synchronized (this) {
            return ipCall;
        }
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.activity.call.Call#getTpCallIdentifier()
     */
    public TpCallIdentifier getTpCallIdentifier() {
        return tpCallIdentifier;
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.activity.call.Call#getActivityHandle()
     */
    public ActivityHandle getActivityHandle() {
        return activityHandle;
    }

    public org.csapi.cc.gccs.TpCallIdentifier getParlayTpCallIdentifier() {
        // TODO  store  org.csapi.cc.gccs.TpCallIdentifier
        return new org.csapi.cc.gccs.TpCallIdentifier (ipCall, callSessionID);
    }

}
