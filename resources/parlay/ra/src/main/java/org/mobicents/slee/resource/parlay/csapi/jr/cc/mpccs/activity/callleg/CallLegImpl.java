package org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.callleg;

import javax.slee.resource.ActivityHandle;
import javax.slee.resource.ResourceException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csapi.P_INVALID_ADDRESS;
import org.csapi.P_INVALID_AMOUNT;
import org.csapi.P_INVALID_CRITERIA;
import org.csapi.P_INVALID_CURRENCY;
import org.csapi.P_INVALID_EVENT_TYPE;
import org.csapi.P_INVALID_NETWORK_STATE;
import org.csapi.P_INVALID_SESSION_ID;
import org.csapi.P_UNSUPPORTED_ADDRESS_PLAN;
import org.csapi.TpAddress;
import org.csapi.TpAoCInfo;
import org.csapi.TpCommonExceptions;
import org.csapi.cc.TpCallAppInfo;
import org.csapi.cc.TpCallChargePlan;
import org.csapi.cc.TpCallError;
import org.csapi.cc.TpCallEventInfo;
import org.csapi.cc.TpCallEventRequest;
import org.csapi.cc.TpCallLegConnectionProperties;
import org.csapi.cc.TpCallLegInfoReport;
import org.csapi.cc.TpReleaseCause;
import org.csapi.cc.mpccs.IpCallLeg;
import org.csapi.cc.mpccs.TpMultiPartyCallIdentifier;
import org.mobicents.csapi.jr.slee.cc.mpccs.AttachMediaErrEvent;
import org.mobicents.csapi.jr.slee.cc.mpccs.AttachMediaResEvent;
import org.mobicents.csapi.jr.slee.cc.mpccs.CallLegEndedEvent;
import org.mobicents.csapi.jr.slee.cc.mpccs.DetachMediaErrEvent;
import org.mobicents.csapi.jr.slee.cc.mpccs.DetachMediaResEvent;
import org.mobicents.csapi.jr.slee.cc.mpccs.EventReportErrEvent;
import org.mobicents.csapi.jr.slee.cc.mpccs.EventReportResEvent;
import org.mobicents.csapi.jr.slee.cc.mpccs.GetInfoErrEvent;
import org.mobicents.csapi.jr.slee.cc.mpccs.GetInfoResEvent;
import org.mobicents.csapi.jr.slee.cc.mpccs.RouteErrEvent;
import org.mobicents.csapi.jr.slee.cc.mpccs.SuperviseErrEvent;
import org.mobicents.csapi.jr.slee.cc.mpccs.SuperviseResEvent;
import org.mobicents.csapi.jr.slee.cc.mpccs.TpCallLegIdentifier;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.MpccsListener;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.TpCallLegActivityHandle;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.multipartycall.MultiPartyCall;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.multipartycallcontrolmanager.MultiPartyCallControlManager;
import org.mobicents.slee.resource.parlay.util.ParlayExceptionUtil;
import org.mobicents.slee.resource.parlay.util.activity.ActivityManager;

/**
 * 
 * Class Description for CallLegImpl
 */
public class CallLegImpl implements CallLeg {
    private static final String CALL_LEG_SESSION_IS_NO_LONGER_VALID = "Call leg session is no longer valid.";

    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(CallLegImpl.class);

    public CallLegImpl(MultiPartyCallControlManager multiPartyCallControlManager, MultiPartyCall multiPartyCall,
            TpCallLegIdentifier tpCallLegIdentifier, IpCallLeg ipCallLeg,
            int callLegSessionID, ActivityManager activityManager, MpccsListener eventListener) {
        super();
        this.multiPartyCallControlManager = multiPartyCallControlManager;
        this.multiPartyCall = multiPartyCall;
        this.tpCallLegIdentifier = tpCallLegIdentifier;
        this.ipCallLeg = ipCallLeg;
        this.callLegSessionID = callLegSessionID;
        this.activityManager = activityManager;
        this.eventListener = eventListener;
        this.activityHandle = new TpCallLegActivityHandle(tpCallLegIdentifier);
    }

    private transient MultiPartyCallControlManager multiPartyCallControlManager;

    private transient MultiPartyCall multiPartyCall;

    private final transient TpCallLegIdentifier tpCallLegIdentifier;

    private transient IpCallLeg ipCallLeg;

    private final transient int callLegSessionID;

    private final transient ActivityManager activityManager;

    private transient MpccsListener eventListener;

    private final transient ActivityHandle activityHandle;

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.callleg.CallLeg#init()
     */
    public void init() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.callleg.CallLeg#getTpCallLegIdentifier()
     */
    public TpCallLegIdentifier getTpCallLegIdentifier() {
        return tpCallLegIdentifier;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.callleg.CallLeg#getIpCallLeg()
     */
    public IpCallLeg getIpCallLeg() {
        synchronized (this) {
            return ipCallLeg;
        }
    }
 
    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.callleg.CallLeg#getMpccsSession()
     */
    public MultiPartyCallControlManager getMpccsSession() {
        return multiPartyCallControlManager;
    }

    /* (non-Javadoc)
     * @see org.mobicents.csapi.jr.slee.cc.mpccs.IpCallLegConnection#routeReq(org.csapi.TpAddress, org.csapi.TpAddress, org.csapi.cc.TpCallAppInfo[], org.csapi.cc.TpCallLegConnectionProperties)
     */
    public void routeReq(final TpAddress arg1, final TpAddress arg2,
            final TpCallAppInfo[] arg3, final TpCallLegConnectionProperties arg4)
            throws TpCommonExceptions,
            P_INVALID_NETWORK_STATE, P_INVALID_ADDRESS,
            P_UNSUPPORTED_ADDRESS_PLAN, ResourceException {

        final IpCallLeg callLeg = getIpCallLeg();

        if (callLeg != null) {
            try {
                callLeg.routeReq(callLegSessionID, arg1, arg2, arg3, arg4);
            }
            catch (P_INVALID_SESSION_ID e) {
                // If this happens call must have finished on gateway and we
                // haven't processed
                // the event yet.
                logger.warn(CALL_LEG_SESSION_IS_NO_LONGER_VALID + ParlayExceptionUtil.stringify(e));
                throw new ResourceException(CALL_LEG_SESSION_IS_NO_LONGER_VALID);
            }
        }

    }

    /* (non-Javadoc)
     * @see org.mobicents.csapi.jr.slee.cc.mpccs.IpCallLegConnection#eventReportReq(org.csapi.cc.TpCallEventRequest[])
     */
    public void eventReportReq(final TpCallEventRequest[] arg1)
            throws TpCommonExceptions,
            P_INVALID_EVENT_TYPE, P_INVALID_CRITERIA, ResourceException {

        final IpCallLeg callLeg = getIpCallLeg();

        if (callLeg != null) {
            try {
                callLeg.eventReportReq(callLegSessionID, arg1);
            }
            catch (P_INVALID_SESSION_ID e) {
                // If this happens call must have finished on gateway and we
                // haven't processed
                // the event yet.
                logger.warn(CALL_LEG_SESSION_IS_NO_LONGER_VALID + ParlayExceptionUtil.stringify(e));
                throw new ResourceException(CALL_LEG_SESSION_IS_NO_LONGER_VALID);
            }
        }

    }

    /* (non-Javadoc)
     * @see org.mobicents.csapi.jr.slee.cc.mpccs.IpCallLegConnection#release(org.csapi.cc.TpReleaseCause)
     */
    public void release(final TpReleaseCause arg1)
            throws TpCommonExceptions,
            P_INVALID_NETWORK_STATE, ResourceException {

        final IpCallLeg callLeg = getIpCallLeg();

        if (callLeg != null) {
            try {
                callLeg.release(callLegSessionID, arg1);
            }
            catch (P_INVALID_SESSION_ID e) {
                // If this happens call must have finished on gateway and we
                // haven't processed
                // the event yet.
                logger.warn(CALL_LEG_SESSION_IS_NO_LONGER_VALID + ParlayExceptionUtil.stringify(e));
                throw new ResourceException(CALL_LEG_SESSION_IS_NO_LONGER_VALID);
            }
        }

    }

    /* (non-Javadoc)
     * @see org.mobicents.csapi.jr.slee.cc.mpccs.IpCallLegConnection#getInfoReq(int)
     */
    public void getInfoReq(final int arg1) throws TpCommonExceptions,
            ResourceException {

        final IpCallLeg callLeg = getIpCallLeg();

        if (callLeg != null) {
            try {
                callLeg.getInfoReq(callLegSessionID, arg1);
            }
            catch (P_INVALID_SESSION_ID e) {
                // If this happens call must have finished on gateway and we
                // haven't processed
                // the event yet.
                logger.warn(CALL_LEG_SESSION_IS_NO_LONGER_VALID + ParlayExceptionUtil.stringify(e));
                throw new ResourceException(CALL_LEG_SESSION_IS_NO_LONGER_VALID);
            }
        }

    }

   /* (non-Javadoc)
     * @see org.mobicents.csapi.jr.slee.cc.mpccs.IpCallLegConnection#getCall()
     */
    public org.mobicents.csapi.jr.slee.cc.mpccs.TpMultiPartyCallIdentifier getCall()
            throws TpCommonExceptions, ResourceException {
        org.mobicents.csapi.jr.slee.cc.mpccs.TpMultiPartyCallIdentifier result = null;

        final IpCallLeg callLeg = getIpCallLeg();

        if (callLeg != null) {
            try {
                
                // Note we don't have to call the gateway here but for now will do so to allow
                // the gateway to throw errors e.g. if call no longer exists.
                
                final TpMultiPartyCallIdentifier callIdentifier = callLeg.getCall(callLegSessionID);
                
                result = new org.mobicents.csapi.jr.slee.cc.mpccs.TpMultiPartyCallIdentifier(multiPartyCall.getTpMultiPartyCallIdentifier().getCallRefID(), callIdentifier.CallSessionID);
            }
            catch (P_INVALID_SESSION_ID e) {
                // If this happens call must have finished on gateway and we
                // haven't processed
                // the event yet.
                logger.warn(CALL_LEG_SESSION_IS_NO_LONGER_VALID + ParlayExceptionUtil.stringify(e));
                throw new ResourceException(CALL_LEG_SESSION_IS_NO_LONGER_VALID);
            }
        }

        return result;
    }

    
    /* (non-Javadoc)
     * @see org.mobicents.csapi.jr.slee.cc.mpccs.IpCallLegConnection#attachMediaReq()
     */
    public void attachMediaReq() throws TpCommonExceptions,
            P_INVALID_NETWORK_STATE, ResourceException {

        final IpCallLeg callLeg = getIpCallLeg();

        if (callLeg != null) {
            try {
                callLeg.attachMediaReq(callLegSessionID);
            }
            catch (P_INVALID_SESSION_ID e) {
                // If this happens call must have finished on gateway and we
                // haven't processed
                // the event yet.
                logger.warn(CALL_LEG_SESSION_IS_NO_LONGER_VALID + ParlayExceptionUtil.stringify(e));
                throw new ResourceException(CALL_LEG_SESSION_IS_NO_LONGER_VALID);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.csapi.cc.mpccs.IpCallLegOperations#detachMediaReq(int)
     */
    public void detachMediaReq() throws TpCommonExceptions,
            P_INVALID_NETWORK_STATE, ResourceException {

        final IpCallLeg callLeg = getIpCallLeg();

        if (callLeg != null) {
            try {
                callLeg.detachMediaReq(callLegSessionID);
            }
            catch (P_INVALID_SESSION_ID e) {
                // If this happens call must have finished on gateway and we
                // haven't processed
                // the event yet.
                logger.warn(CALL_LEG_SESSION_IS_NO_LONGER_VALID + ParlayExceptionUtil.stringify(e));
                throw new ResourceException(CALL_LEG_SESSION_IS_NO_LONGER_VALID);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.csapi.cc.mpccs.IpCallLegOperations#getCurrentDestinationAddress(int)
     */
    public TpAddress getCurrentDestinationAddress()
            throws TpCommonExceptions, ResourceException {

        TpAddress result = null;

        final IpCallLeg callLeg = getIpCallLeg();

        if (callLeg != null) {
            try {
                result = callLeg.getCurrentDestinationAddress(callLegSessionID);
            }
            catch (P_INVALID_SESSION_ID e) {
                // If this happens call must have finished on gateway and we
                // haven't processed
                // the event yet.
                logger.warn(CALL_LEG_SESSION_IS_NO_LONGER_VALID + ParlayExceptionUtil.stringify(e));
                throw new ResourceException(CALL_LEG_SESSION_IS_NO_LONGER_VALID);
            }
        }
        return result;
    }

    /* (non-Javadoc)
     * @see org.mobicents.csapi.jr.slee.cc.mpccs.IpCallLegConnection#continueProcessing()
     */
    public void continueProcessing() throws TpCommonExceptions,
            P_INVALID_NETWORK_STATE, ResourceException {

        final IpCallLeg callLeg = getIpCallLeg();

        if (callLeg != null) {
            try {
                callLeg.continueProcessing(callLegSessionID);
            }
            catch (P_INVALID_SESSION_ID e) {
                // If this happens call must have finished on gateway and we
                // haven't processed
                // the event yet.
                logger.warn(CALL_LEG_SESSION_IS_NO_LONGER_VALID + ParlayExceptionUtil.stringify(e));
                throw new ResourceException(CALL_LEG_SESSION_IS_NO_LONGER_VALID);
            }
        }
    }

    /* (non-Javadoc)
     * @see org.mobicents.csapi.jr.slee.cc.mpccs.IpCallLegConnection#setChargePlan(org.csapi.cc.TpCallChargePlan)
     */
    public void setChargePlan(final TpCallChargePlan arg1)
            throws TpCommonExceptions, ResourceException {

        final IpCallLeg callLeg = getIpCallLeg();

        if (callLeg != null) {
            try {
                callLeg.setChargePlan(callLegSessionID, arg1);
            }
            catch (P_INVALID_SESSION_ID e) {
                // If this happens call must have finished on gateway and we
                // haven't processed
                // the event yet.
                logger.warn(CALL_LEG_SESSION_IS_NO_LONGER_VALID + ParlayExceptionUtil.stringify(e));
                throw new ResourceException(CALL_LEG_SESSION_IS_NO_LONGER_VALID);
            }
        }
    }

    
    /* (non-Javadoc)
     * @see org.mobicents.csapi.jr.slee.cc.mpccs.IpCallLegConnection#setAdviceOfCharge(org.csapi.TpAoCInfo, int)
     */
    public void setAdviceOfCharge(final TpAoCInfo arg1, final int arg2)
            throws TpCommonExceptions,
            P_INVALID_CURRENCY, P_INVALID_AMOUNT,
            ResourceException{

        final IpCallLeg callLeg = getIpCallLeg();

        if (callLeg != null) {
            try {
                callLeg.setAdviceOfCharge(callLegSessionID, arg1, arg2);
            }
            catch (P_INVALID_SESSION_ID e) {
                // If this happens call must have finished on gateway and we
                // haven't processed
                // the event yet.
                logger.warn(CALL_LEG_SESSION_IS_NO_LONGER_VALID + ParlayExceptionUtil.stringify(e));
                throw new ResourceException(CALL_LEG_SESSION_IS_NO_LONGER_VALID);
            }
        }
    }

    /* (non-Javadoc)
     * @see org.mobicents.csapi.jr.slee.cc.mpccs.IpCallLegConnection#superviseReq(int, int)
     */
    public void superviseReq(final int arg1, final int arg2)
            throws TpCommonExceptions, ResourceException {

        final IpCallLeg callLeg = getIpCallLeg();

        if (callLeg != null) {
            try {
                callLeg.superviseReq(callLegSessionID, arg1, arg2);
            }
            catch (P_INVALID_SESSION_ID e) {
                // If this happens call must have finished on gateway and we
                // haven't processed
                // the event yet.
                logger.warn(CALL_LEG_SESSION_IS_NO_LONGER_VALID + ParlayExceptionUtil.stringify(e));
                throw new ResourceException(CALL_LEG_SESSION_IS_NO_LONGER_VALID);
            }
        }
    }

    /* (non-Javadoc)
     * @see org.mobicents.csapi.jr.slee.cc.mpccs.IpCallLegConnection#deassign()
     */
    public void deassign() throws TpCommonExceptions,
            ResourceException {

        final IpCallLeg callLeg = getIpCallLeg();

        if (callLeg != null) {
            try {
                callLeg.deassign(callLegSessionID);
            }
            catch (P_INVALID_SESSION_ID e) {
                // If this happens call must have finished on gateway and we
                // haven't processed
                // the event yet.
                logger.warn(CALL_LEG_SESSION_IS_NO_LONGER_VALID + ParlayExceptionUtil.stringify(e));
                throw new ResourceException(CALL_LEG_SESSION_IS_NO_LONGER_VALID);
            }

            dispose();
            
            activityManager.remove(activityHandle, tpCallLegIdentifier);
            
            activityManager.activityEnding(activityHandle);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.csapi.cc.mpccs.IpAppCallLegOperations#eventReportRes(int,
     *      org.csapi.cc.TpCallEventInfo)
     */
    public void eventReportRes(final int arg0, final TpCallEventInfo arg1) {

        final IpCallLeg callLeg = getIpCallLeg();

        if (callLeg != null) {
            final EventReportResEvent event = new EventReportResEvent(multiPartyCallControlManager
                    .getTpServiceIdentifier(), multiPartyCall
                    .getTpMultiPartyCallIdentifier(), tpCallLegIdentifier,
                    arg1);

            eventListener.onEventReportResEvent(event);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.csapi.cc.mpccs.IpAppCallLegOperations#eventReportErr(int,
     *      org.csapi.cc.TpCallError)
     */
    public void eventReportErr(final int arg0, final TpCallError arg1) {

        final IpCallLeg callLeg = getIpCallLeg();

        if (callLeg != null) {
            final EventReportErrEvent event = new EventReportErrEvent(multiPartyCallControlManager
                    .getTpServiceIdentifier(), multiPartyCall
                    .getTpMultiPartyCallIdentifier(), tpCallLegIdentifier,
                    arg1);

            eventListener.onEventReportErrEvent(event);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.csapi.cc.mpccs.IpAppCallLegOperations#attachMediaRes(int)
     */
    public void attachMediaRes(final int arg0) {

        final IpCallLeg callLeg = getIpCallLeg();

        if (callLeg != null) {
            final AttachMediaResEvent event = new AttachMediaResEvent(multiPartyCallControlManager
                    .getTpServiceIdentifier(), multiPartyCall
                    .getTpMultiPartyCallIdentifier(), tpCallLegIdentifier);

            eventListener.onAttachMediaResEvent(event);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.csapi.cc.mpccs.IpAppCallLegOperations#attachMediaErr(int,
     *      org.csapi.cc.TpCallError)
     */
    public void attachMediaErr(final int arg0, final TpCallError arg1) {

        final IpCallLeg callLeg = getIpCallLeg();

        if (callLeg != null) {
            final AttachMediaErrEvent event = new AttachMediaErrEvent(multiPartyCallControlManager
                    .getTpServiceIdentifier(), multiPartyCall
                    .getTpMultiPartyCallIdentifier(), tpCallLegIdentifier,
                    arg1);

            eventListener.onAttachMediaErrEvent(event);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.csapi.cc.mpccs.IpAppCallLegOperations#detachMediaRes(int)
     */
    public void detachMediaRes(final int arg0) {

        final IpCallLeg callLeg = getIpCallLeg();

        if (callLeg != null) {
            DetachMediaResEvent event = new DetachMediaResEvent(multiPartyCallControlManager
                    .getTpServiceIdentifier(), multiPartyCall
                    .getTpMultiPartyCallIdentifier(), tpCallLegIdentifier);

            eventListener.onDetachMediaResEvent(event);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.csapi.cc.mpccs.IpAppCallLegOperations#detachMediaErr(int,
     *      org.csapi.cc.TpCallError)
     */
    public void detachMediaErr(final int arg0, final TpCallError arg1) {

        final IpCallLeg callLeg = getIpCallLeg();

        if (callLeg != null) {
            final DetachMediaErrEvent event = new DetachMediaErrEvent(multiPartyCallControlManager
                    .getTpServiceIdentifier(), multiPartyCall
                    .getTpMultiPartyCallIdentifier(), tpCallLegIdentifier,
                    arg1);

            eventListener.onDetachMediaErrEvent(event);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.csapi.cc.mpccs.IpAppCallLegOperations#getInfoRes(int,
     *      org.csapi.cc.TpCallLegInfoReport)
     */
    public void getInfoRes(final int arg0, final TpCallLegInfoReport arg1) {

        final IpCallLeg callLeg = getIpCallLeg();

        if (callLeg != null) {
            final GetInfoResEvent event = new GetInfoResEvent(multiPartyCallControlManager
                    .getTpServiceIdentifier(), multiPartyCall
                    .getTpMultiPartyCallIdentifier(), tpCallLegIdentifier,
                    arg1);

            eventListener.onGetInfoResEvent(event);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.csapi.cc.mpccs.IpAppCallLegOperations#getInfoErr(int,
     *      org.csapi.cc.TpCallError)
     */
    public void getInfoErr(final int arg0, final TpCallError arg1) {

        final IpCallLeg callLeg = getIpCallLeg();

        if (callLeg != null) {
            final GetInfoErrEvent event = new GetInfoErrEvent(multiPartyCallControlManager
                    .getTpServiceIdentifier(), multiPartyCall
                    .getTpMultiPartyCallIdentifier(), tpCallLegIdentifier,
                    arg1);

            eventListener.onGetInfoErrEvent(event);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.csapi.cc.mpccs.IpAppCallLegOperations#routeErr(int,
     *      org.csapi.cc.TpCallError)
     */
    public void routeErr(final int arg0, final TpCallError arg1) {

        final IpCallLeg callLeg = getIpCallLeg();

        if (callLeg != null) {
            final RouteErrEvent event = new RouteErrEvent(multiPartyCallControlManager
                    .getTpServiceIdentifier(), multiPartyCall
                    .getTpMultiPartyCallIdentifier(), tpCallLegIdentifier,
                    arg1);

            eventListener.onRouteErrEvent(event);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.csapi.cc.mpccs.IpAppCallLegOperations#superviseRes(int, int,
     *      int)
     */
    public void superviseRes(final int arg0, final int arg1, final int arg2) {

        final IpCallLeg callLeg = getIpCallLeg();

        if (callLeg != null) {
            final SuperviseResEvent event = new SuperviseResEvent(multiPartyCallControlManager
                    .getTpServiceIdentifier(), multiPartyCall
                    .getTpMultiPartyCallIdentifier(), tpCallLegIdentifier,
                    arg1, arg2);

            eventListener.onSuperviseResEvent(event);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.csapi.cc.mpccs.IpAppCallLegOperations#superviseErr(int,
     *      org.csapi.cc.TpCallError)
     */
    public void superviseErr(final int arg0, final TpCallError arg1) {

        final IpCallLeg callLeg = getIpCallLeg();

        if (callLeg != null) {
            final SuperviseErrEvent event = new SuperviseErrEvent(multiPartyCallControlManager
                    .getTpServiceIdentifier(), multiPartyCall
                    .getTpMultiPartyCallIdentifier(), tpCallLegIdentifier,
                    arg1);

            eventListener.onSuperviseErrEvent(event);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.csapi.cc.mpccs.IpAppCallLegOperations#callLegEnded(int,
     *      org.csapi.cc.TpReleaseCause)
     */
    public void callLegEnded(final int arg0, final TpReleaseCause arg1) {

        final IpCallLeg callLeg = getIpCallLeg();

        if (callLeg != null) {
            final CallLegEndedEvent event = new CallLegEndedEvent(multiPartyCallControlManager
                    .getTpServiceIdentifier(), multiPartyCall
                    .getTpMultiPartyCallIdentifier(), tpCallLegIdentifier,
                    arg1);

            eventListener.onCallLegEndedEvent(event);
            
            activityManager.remove(activityHandle, tpCallLegIdentifier);
            
            activityManager.activityEnding(activityHandle);

            dispose();
        }
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.callleg.CallLeg#dispose()
     */
    public void dispose() {

        synchronized (this) {
            ipCallLeg = null;

            if(multiPartyCall!= null) {
	            multiPartyCall.removeCallLeg(tpCallLegIdentifier.getCallLegSessionID());
	            multiPartyCall = null;
            }

            multiPartyCallControlManager = null;

            eventListener = null;
        }

    }

    /* (non-Javadoc)
     * @see org.mobicents.csapi.jr.slee.IpServiceConnection#close()
     */
    public void closeConnection() throws ResourceException {
        // Do nothing
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.callleg.CallLeg#getParlayTpCallLegIdentifier()
     */
    public org.csapi.cc.mpccs.TpCallLegIdentifier getParlayTpCallLegIdentifier() {
        // TODO  store full org.csapi.cc.mpccs.TpCallLegIdentifier 
        return new org.csapi.cc.mpccs.TpCallLegIdentifier (ipCallLeg,callLegSessionID);
    }

    /**
     * @return Returns the activityHandle.
     */
    public ActivityHandle getActivityHandle() {
        return activityHandle;
    }

}