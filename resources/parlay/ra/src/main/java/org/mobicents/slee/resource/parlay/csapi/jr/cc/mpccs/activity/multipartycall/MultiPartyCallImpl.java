package org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.multipartycall;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.slee.resource.ActivityHandle;
import javax.slee.resource.ResourceException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csapi.P_INVALID_ADDRESS;
import org.csapi.P_INVALID_AMOUNT;
import org.csapi.P_INVALID_CRITERIA;
import org.csapi.P_INVALID_CURRENCY;
import org.csapi.P_INVALID_EVENT_TYPE;
import org.csapi.P_INVALID_INTERFACE_TYPE;
import org.csapi.P_INVALID_NETWORK_STATE;
import org.csapi.P_INVALID_SESSION_ID;
import org.csapi.P_UNSUPPORTED_ADDRESS_PLAN;
import org.csapi.TpAddress;
import org.csapi.TpAoCInfo;
import org.csapi.TpCommonExceptions;
import org.csapi.cc.TpCallAppInfo;
import org.csapi.cc.TpCallChargePlan;
import org.csapi.cc.TpCallEndedReport;
import org.csapi.cc.TpCallError;
import org.csapi.cc.TpCallEventRequest;
import org.csapi.cc.TpCallInfoReport;
import org.csapi.cc.TpReleaseCause;
import org.csapi.cc.mpccs.IpAppCallLeg;
import org.csapi.cc.mpccs.IpAppCallLegHelper;
import org.csapi.cc.mpccs.IpMultiPartyCall;
import org.csapi.cc.mpccs.TpCallLegIdentifier;
import org.mobicents.csapi.jr.slee.cc.mpccs.CallEndedEvent;
import org.mobicents.csapi.jr.slee.cc.mpccs.CreateAndRouteCallLegErrEvent;
import org.mobicents.csapi.jr.slee.cc.mpccs.GetInfoErrEvent;
import org.mobicents.csapi.jr.slee.cc.mpccs.GetInfoResEvent;
import org.mobicents.csapi.jr.slee.cc.mpccs.IpCallLegConnection;
import org.mobicents.csapi.jr.slee.cc.mpccs.SuperviseErrEvent;
import org.mobicents.csapi.jr.slee.cc.mpccs.SuperviseResEvent;
import org.mobicents.csapi.jr.slee.cc.mpccs.TpMultiPartyCallIdentifier;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.IpAppCallLegImpl;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.IpCallLegConnectionImpl;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.MpccsListener;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.TpMultiPartyCallActivityHandle;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.callleg.CallLeg;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.callleg.CallLegImpl;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.multipartycallcontrolmanager.MultiPartyCallControlManager;
import org.mobicents.slee.resource.parlay.util.ParlayExceptionUtil;
import org.mobicents.slee.resource.parlay.util.ResourceIDFactory;
import org.mobicents.slee.resource.parlay.util.activity.ActivityManager;
import org.mobicents.slee.resource.parlay.util.corba.ServantActivationHelper;
import org.omg.CORBA.UserException;

import EDU.oswego.cs.dl.util.concurrent.Executor;

/**
 * Context object for a Multi Party Call.
 */
public class MultiPartyCallImpl implements MultiPartyCall {
    private static final String CALL_SESSION_IS_NO_LONGER_VALID = "Call session is no longer valid.";

    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory
            .getLog(MultiPartyCallImpl.class);

    public MultiPartyCallImpl(
            MultiPartyCallControlManager multiPartyCallControlManager,
            TpMultiPartyCallIdentifier tpMultiPartyCallIdentifier,
            IpMultiPartyCall ipMultiPartyCall, int callSessionID,
            ActivityManager activityManager, MpccsListener eventListener,
            Executor[] ipAppCallLegExecutors) {
        super();
        this.multiPartyCallControlManager = multiPartyCallControlManager;
        this.tpMultiPartyCallIdentifier = tpMultiPartyCallIdentifier;
        this.ipMultiPartyCall = ipMultiPartyCall;
        this.callSessionID = callSessionID;
        this.activityManager = activityManager;
        this.eventListener = eventListener;
        this.activityHandle = new TpMultiPartyCallActivityHandle(
                tpMultiPartyCallIdentifier);

        callLegMap = new HashMap();

        // Defaulting to 5 executors, maybe make this configurable at some point
        this.ipAppCallLegExecutors = ipAppCallLegExecutors;
    }

    private transient MultiPartyCallControlManager multiPartyCallControlManager;

    private final transient TpMultiPartyCallIdentifier tpMultiPartyCallIdentifier;

    private transient IpAppCallLeg ipAppCallLeg;

    private transient IpAppCallLegImpl ipAppCallLegImpl;

    private final transient Map callLegMap;

    private transient IpMultiPartyCall ipMultiPartyCall;

    private final transient int callSessionID;

    private final transient ActivityManager activityManager;

    private transient MpccsListener eventListener;

    private final transient ActivityHandle activityHandle;

    private final transient Executor[] ipAppCallLegExecutors;

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.multipartycall.MultiPartyCall#init()
     */
    public void init() {

        synchronized (this) {
            activateIpAppCallLeg();
        }
    }

    /**
     *  
     */
    protected void activateIpAppCallLeg() {
        ipAppCallLegImpl = new IpAppCallLegImpl(this,
                multiPartyCallControlManager.getIpAppCallLegPOA(), ipAppCallLegExecutors);

        try {
            final org.omg.CORBA.Object object = ServantActivationHelper
                    .activateServant(multiPartyCallControlManager
                            .getIpAppCallLegPOA(), ipAppCallLegImpl);

            ipAppCallLeg = IpAppCallLegHelper.narrow(object);
        }
        catch (UserException e) {
            logger.error("Failed to activate IpAppCallLeg.", e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.multipartycall.MultiPartyCall#getTpMultiPartyCallIdentifier()
     */
    public TpMultiPartyCallIdentifier getTpMultiPartyCallIdentifier() {
        return tpMultiPartyCallIdentifier;
    }

 

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.multipartycall.MultiPartyCall#getIpAppCallLeg()
     */
    public IpAppCallLeg getIpAppCallLeg() {
        return ipAppCallLeg;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.multipartycall.MultiPartyCall#getCallLeg(org.mobicents.csapi.jr.slee.cc.mpccs.TpCallLegIdentifier)
     */
    public synchronized CallLeg getCallLeg(final int callLegSessionID) {
        return (CallLeg) callLegMap.get(new Integer(callLegSessionID));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.multipartycall.MultiPartyCall#addCallLeg(org.mobicents.csapi.jr.slee.cc.mpccs.TpCallLegIdentifier,
     *      org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.callleg.CallLeg)
     */
    public synchronized void addCallLeg(final int callLegSessionID, final CallLeg callLeg) {
        callLegMap.put(new Integer(callLegSessionID), callLeg);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.multipartycall.MultiPartyCall#removeCallLeg(org.mobicents.csapi.jr.slee.cc.mpccs.TpCallLegIdentifier)
     */
    public synchronized CallLeg removeCallLeg(final int callLegSessionID) {
        return (CallLeg) callLegMap.remove(new Integer(callLegSessionID));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.multipartycall.MultiPartyCall#getIpMultiPartyCall()
     */
    public IpMultiPartyCall getIpMultiPartyCall() {
        synchronized (this) {
            return ipMultiPartyCall;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.multipartycall.MultiPartyCall#getCallSessionID()
     */
    public int getCallSessionID() {
        return callSessionID;
    }
    
    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.multipartycall.MultiPartyCall#getActivityHandle()
     */
    public ActivityHandle getActivityHandle() {
        return activityHandle;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.csapi.cc.mpccs.IpMultiPartyCallOperations#getCallLegs(int)
     */
    public org.mobicents.csapi.jr.slee.cc.mpccs.TpCallLegIdentifier[] getCallLegs()
            throws TpCommonExceptions, ResourceException {

        org.mobicents.csapi.jr.slee.cc.mpccs.TpCallLegIdentifier[] result = null;

        final IpMultiPartyCall multiPartyCall = getIpMultiPartyCall();

        if (multiPartyCall != null) {
            TpCallLegIdentifier[] callLegIdentifiers;
            try {
                callLegIdentifiers = multiPartyCall.getCallLegs(callSessionID);

                result = new org.mobicents.csapi.jr.slee.cc.mpccs.TpCallLegIdentifier[callLegIdentifiers.length];

                for (int i = 0; i < callLegIdentifiers.length; i++) {
                    result[i] = getCallLeg(
                            callLegIdentifiers[i].CallLegSessionID)
                            .getTpCallLegIdentifier();
                }
            }
            catch (P_INVALID_SESSION_ID e) {
                // If this happens call must have finished on gateway and we
                // haven't processed
                // the event yet.
                logger.warn(CALL_SESSION_IS_NO_LONGER_VALID + ParlayExceptionUtil.stringify(e));
                throw new ResourceException(CALL_SESSION_IS_NO_LONGER_VALID);
            }
        }

        return result;
    }

    /* (non-Javadoc)
     * @see org.mobicents.csapi.jr.slee.cc.mpccs.IpMultiPartyCallConnection#createCallLeg()
     */
    public org.mobicents.csapi.jr.slee.cc.mpccs.TpCallLegIdentifier createCallLeg(
            ) throws TpCommonExceptions,
            ResourceException {

        org.mobicents.csapi.jr.slee.cc.mpccs.TpCallLegIdentifier result = null;

        final IpMultiPartyCall multiPartyCall = getIpMultiPartyCall();

        if (multiPartyCall != null) {

            synchronized (this) {
                TpCallLegIdentifier callLegIdentifier;
                try {
                    callLegIdentifier = multiPartyCall.createCallLeg(callSessionID, getIpAppCallLeg());

                    CallLeg callLeg = createCallLeg(callLegIdentifier);
                    
                    result = callLeg.getTpCallLegIdentifier();
                    
                    activityManager.add(callLeg.getActivityHandle(), callLeg.getTpCallLegIdentifier());
                    
                    activityManager.activityStartedSuspended(callLeg.getActivityHandle());
                }
                catch (P_INVALID_SESSION_ID e) {
                    // If this happens call must have finished on gateway and we
                    // haven't processed
                    // the event yet.                    
                    logger.warn(CALL_SESSION_IS_NO_LONGER_VALID + ParlayExceptionUtil.stringify(e));
                    throw new ResourceException(CALL_SESSION_IS_NO_LONGER_VALID);
                }
                catch (P_INVALID_INTERFACE_TYPE e) {
                    logger.error(ParlayExceptionUtil.stringify(e), e);   
                    throw new javax.slee.resource.ResourceException("Unexpected Parlay exception", e);
                }
            }
        }

        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.csapi.jr.slee.cc.mpccs.IpMultiPartyCallConnection#createAndRouteCallLegReq(org.csapi.cc.TpCallEventRequest[],
     *      org.csapi.TpAddress, org.csapi.TpAddress,
     *      org.csapi.cc.TpCallAppInfo[])
     */
    public org.mobicents.csapi.jr.slee.cc.mpccs.TpCallLegIdentifier createAndRouteCallLegReq(
            final TpCallEventRequest[] arg1, final TpAddress arg2, final TpAddress arg3,
            final TpCallAppInfo[] arg4) throws TpCommonExceptions, P_INVALID_ADDRESS,
            P_UNSUPPORTED_ADDRESS_PLAN, P_INVALID_NETWORK_STATE,
            P_INVALID_EVENT_TYPE, P_INVALID_CRITERIA, ResourceException {

        org.mobicents.csapi.jr.slee.cc.mpccs.TpCallLegIdentifier result = null;

        final IpMultiPartyCall multiPartyCall = getIpMultiPartyCall();

        if (multiPartyCall != null) {

            synchronized (this) {
                TpCallLegIdentifier callLegIdentifier;
                try {
                    callLegIdentifier = multiPartyCall
                            .createAndRouteCallLegReq(callSessionID, arg1,
                                    arg2, arg3, arg4, getIpAppCallLeg());


                    CallLeg callLeg = createCallLeg(callLegIdentifier);
                    
                    result = callLeg.getTpCallLegIdentifier();
                    
                    activityManager.add(callLeg.getActivityHandle(), callLeg.getTpCallLegIdentifier());
                    
                    activityManager.activityStartedSuspended(callLeg.getActivityHandle());
                }
                catch (P_INVALID_SESSION_ID e) {
                    // If this happens call must have finished on gateway and we
                    // haven't processed
                    // the event yet.
                    logger.warn(CALL_SESSION_IS_NO_LONGER_VALID + ParlayExceptionUtil.stringify(e));
                    throw new ResourceException(CALL_SESSION_IS_NO_LONGER_VALID);
                }
                catch (P_INVALID_INTERFACE_TYPE e) {
                    logger.error(ParlayExceptionUtil.stringify(e), e);   
                    throw new javax.slee.resource.ResourceException("Unexpected Parlay exception", e);
                }
            }
        }

        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.csapi.jr.slee.cc.mpccs.IpMultiPartyCallConnection#release(org.csapi.cc.TpReleaseCause)
     */
    public void release(final TpReleaseCause arg1) throws TpCommonExceptions,
            P_INVALID_NETWORK_STATE, ResourceException {

        final IpMultiPartyCall multiPartyCall = getIpMultiPartyCall();

        if (multiPartyCall != null) {
            try {
                multiPartyCall.release(callSessionID, arg1);
            }
            catch (P_INVALID_SESSION_ID e) {
                // If this happens call must have finished on gateway and we
                // haven't processed
                // the event yet.
                logger.warn(CALL_SESSION_IS_NO_LONGER_VALID + ParlayExceptionUtil.stringify(e));
                throw new ResourceException(CALL_SESSION_IS_NO_LONGER_VALID);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.csapi.jr.slee.cc.mpccs.IpMultiPartyCallConnection#deassignCall()
     */
    public void deassignCall() throws TpCommonExceptions, ResourceException {

        final IpMultiPartyCall multiPartyCall = getIpMultiPartyCall();

        if (multiPartyCall != null) {
            try {
                multiPartyCall.deassignCall(callSessionID);
            }
            catch (P_INVALID_SESSION_ID e) {
                // If this happens call must have finished on gateway and we
                // haven't processed
                // the event yet.
                logger.warn(CALL_SESSION_IS_NO_LONGER_VALID + ParlayExceptionUtil.stringify(e));
                throw new ResourceException(CALL_SESSION_IS_NO_LONGER_VALID);
            }

            activityManager.remove(activityHandle, tpMultiPartyCallIdentifier);
            
            activityManager.activityEnding(activityHandle);

            dispose();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.csapi.jr.slee.cc.mpccs.IpMultiPartyCallConnection#getInfoReq(int)
     */
    public void getInfoReq(final int arg1) throws TpCommonExceptions,
            ResourceException {

        final IpMultiPartyCall multiPartyCall = getIpMultiPartyCall();

        if (multiPartyCall != null) {
            try {
                multiPartyCall.getInfoReq(callSessionID, arg1);
            }
            catch (P_INVALID_SESSION_ID e) {
                // If this happens call must have finished on gateway and we
                // haven't processed
                // the event yet.
                logger.warn(CALL_SESSION_IS_NO_LONGER_VALID + ParlayExceptionUtil.stringify(e));
                throw new ResourceException(CALL_SESSION_IS_NO_LONGER_VALID);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.csapi.jr.slee.cc.mpccs.IpMultiPartyCallConnection#setChargePlan(org.csapi.cc.TpCallChargePlan)
     */
    public void setChargePlan(final TpCallChargePlan arg1) throws TpCommonExceptions,
            ResourceException {

        final IpMultiPartyCall multiPartyCall = getIpMultiPartyCall();

        if (multiPartyCall != null) {
            try {
                multiPartyCall.setChargePlan(callSessionID, arg1);
            }
            catch (P_INVALID_SESSION_ID e) {
                // If this happens call must have finished on gateway and we
                // haven't processed
                // the event yet.
                logger.warn(CALL_SESSION_IS_NO_LONGER_VALID + ParlayExceptionUtil.stringify(e));
                throw new ResourceException(CALL_SESSION_IS_NO_LONGER_VALID);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.csapi.jr.slee.cc.mpccs.IpMultiPartyCallConnection#setAdviceOfCharge(org.csapi.TpAoCInfo,
     *      int)
     */
    public void setAdviceOfCharge(final TpAoCInfo arg1, final int arg2)
            throws TpCommonExceptions, ResourceException, P_INVALID_CURRENCY,
            P_INVALID_AMOUNT {

        final IpMultiPartyCall multiPartyCall = getIpMultiPartyCall();

        if (multiPartyCall != null) {
            try {
                multiPartyCall.setAdviceOfCharge(callSessionID, arg1, arg2);
            }
            catch (P_INVALID_SESSION_ID e) {
                // If this happens call must have finished on gateway and we
                // haven't processed
                // the event yet.
                logger.warn(CALL_SESSION_IS_NO_LONGER_VALID + ParlayExceptionUtil.stringify(e));
                throw new ResourceException(CALL_SESSION_IS_NO_LONGER_VALID);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.csapi.jr.slee.cc.mpccs.IpMultiPartyCallConnection#superviseReq(int,
     *      int)
     */
    public void superviseReq(final int arg1, final int arg2) throws TpCommonExceptions,
            ResourceException {

        final IpMultiPartyCall multiPartyCall = getIpMultiPartyCall();

        if (multiPartyCall != null) {
            try {
                multiPartyCall.superviseReq(callSessionID, arg1, arg2);
            }
            catch (P_INVALID_SESSION_ID e) {
                // If this happens call must have finished on gateway and we
                // haven't processed
                // the event yet.
                logger.warn(CALL_SESSION_IS_NO_LONGER_VALID + ParlayExceptionUtil.stringify(e));
                throw new ResourceException(CALL_SESSION_IS_NO_LONGER_VALID);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.csapi.cc.mpccs.IpAppMultiPartyCallOperations#getInfoRes(int,
     *      org.csapi.cc.TpCallInfoReport)
     */
    public void getInfoRes(final int arg0, final TpCallInfoReport arg1) {

        final IpMultiPartyCall multiPartyCall = getIpMultiPartyCall();

        if (multiPartyCall != null) {
            final GetInfoResEvent event = new GetInfoResEvent(
                    multiPartyCallControlManager.getTpServiceIdentifier(),
                    tpMultiPartyCallIdentifier, arg1);

            eventListener.onGetInfoResEvent(event);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.csapi.cc.mpccs.IpAppMultiPartyCallOperations#getInfoErr(int,
     *      org.csapi.cc.TpCallError)
     */
    public void getInfoErr(final int arg0, final TpCallError arg1) {

        final IpMultiPartyCall multiPartyCall = getIpMultiPartyCall();

        if (multiPartyCall != null) {
            final GetInfoErrEvent event = new GetInfoErrEvent(
                    multiPartyCallControlManager.getTpServiceIdentifier(),
                    tpMultiPartyCallIdentifier, arg1);

            eventListener.onGetInfoErrEvent(event);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.csapi.cc.mpccs.IpAppMultiPartyCallOperations#superviseRes(int,
     *      int, int)
     */
    public void superviseRes(final int arg0, final int arg1,final  int arg2) {

        final IpMultiPartyCall multiPartyCall = getIpMultiPartyCall();

        if (multiPartyCall != null) {
            final SuperviseResEvent event = new SuperviseResEvent(
                    multiPartyCallControlManager.getTpServiceIdentifier(),
                    tpMultiPartyCallIdentifier, arg1, arg2);

            eventListener.onSuperviseResEvent(event);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.csapi.cc.mpccs.IpAppMultiPartyCallOperations#superviseErr(int,
     *      org.csapi.cc.TpCallError)
     */
    public void superviseErr(final int arg0, final TpCallError arg1) {

        final IpMultiPartyCall multiPartyCall = getIpMultiPartyCall();

        if (multiPartyCall != null) {
            final SuperviseErrEvent event = new SuperviseErrEvent(
                    multiPartyCallControlManager.getTpServiceIdentifier(),
                    tpMultiPartyCallIdentifier, arg1);

            eventListener.onSuperviseErrEvent(event);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.csapi.cc.mpccs.IpAppMultiPartyCallOperations#callEnded(int,
     *      org.csapi.cc.TpCallEndedReport)
     */
    public void callEnded(final int arg0, final TpCallEndedReport arg1) {

        final IpMultiPartyCall multiPartyCall = getIpMultiPartyCall();

        if (multiPartyCall != null) {
            final CallEndedEvent event = new CallEndedEvent(
                    multiPartyCallControlManager.getTpServiceIdentifier(),
                    tpMultiPartyCallIdentifier, arg1);

            eventListener.onCallEndedEvent(event);

            activityManager.remove(activityHandle, tpMultiPartyCallIdentifier);
            
            activityManager.activityEnding(activityHandle);

            dispose();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.csapi.cc.mpccs.IpAppMultiPartyCallOperations#createAndRouteCallLegErr(int,
     *      org.csapi.cc.mpccs.TpCallLegIdentifier, org.csapi.cc.TpCallError)
     */
    public void createAndRouteCallLegErr(final int arg0, final TpCallLegIdentifier arg1,
            TpCallError arg2) {

        CallLeg callLeg;
        synchronized (this) {
            callLeg = getCallLeg(arg1.CallLegSessionID);
        }

        if (callLeg != null) {

            final IpMultiPartyCall multiPartyCall = getIpMultiPartyCall();

            if (multiPartyCall != null) {
                final CreateAndRouteCallLegErrEvent event = new CreateAndRouteCallLegErrEvent(
                        multiPartyCallControlManager.getTpServiceIdentifier(),
                        tpMultiPartyCallIdentifier, callLeg
                                .getTpCallLegIdentifier(), arg2);

                eventListener.onCreateAndRouteCallLegErrEvent(event);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.multipartycall.MultiPartyCall#dispose()
     */
    public void dispose() {

        synchronized (this) {
            ipMultiPartyCall = null;

            final Collection callLegs = callLegMap.values();
            for (final Iterator iter = callLegs.iterator(); iter.hasNext();) {
                final CallLeg callLeg = (CallLeg) iter.next();
                callLeg.dispose();
            }
            callLegMap.clear();

            deactivateIpAppCallLeg();

            if (multiPartyCallControlManager != null) {
                multiPartyCallControlManager
                        .removeMultiPartyCall(callSessionID);

                multiPartyCallControlManager = null;
            }

            eventListener = null;
        }

    }

    /**
     *  
     */
    private void deactivateIpAppCallLeg() {
        if (ipAppCallLegImpl != null) {
            try {
                ServantActivationHelper.deactivateServant(ipAppCallLegImpl);
            }
            catch (UserException e) {
                logger.error("Failed to deactivate IpAppCallLeg servant.", e);
            }

            ipAppCallLegImpl.dispose();

            ipAppCallLeg = null;

            ipAppCallLegImpl = null;
        }
    }

    /**
     * Utility method for creating an new CallLeg.
     * 
     * @param corbaIdentifier
     */
    private CallLeg createCallLeg(final TpCallLegIdentifier corbaIdentifier) {

        final int callLegReferenceID = ResourceIDFactory.getNextID();

        final org.mobicents.csapi.jr.slee.cc.mpccs.TpCallLegIdentifier callLegIdentifier = new org.mobicents.csapi.jr.slee.cc.mpccs.TpCallLegIdentifier(
                callLegReferenceID, corbaIdentifier.CallLegSessionID);

        final CallLeg callLeg = new CallLegImpl(multiPartyCallControlManager, this,
                callLegIdentifier, corbaIdentifier.CallLegReference,
                corbaIdentifier.CallLegSessionID, activityManager, eventListener);

        callLeg.init();

        addCallLeg(corbaIdentifier.CallLegSessionID, callLeg);

        return callLeg;
    }

    /* (non-Javadoc)
     * @see org.mobicents.csapi.jr.slee.cc.mpccs.IpMultiPartyCallConnection#getIpCallLegConnection(org.mobicents.csapi.jr.slee.cc.mpccs.TpCallLegIdentifier)
     */
    public IpCallLegConnection getIpCallLegConnection(final org.mobicents.csapi.jr.slee.cc.mpccs.TpCallLegIdentifier callLegIdentifier) throws ResourceException {
 
        CallLeg callLeg;
        synchronized (this) {
            callLeg = getCallLeg(callLegIdentifier.getCallLegSessionID());
        }
        
        if(callLeg != null) {
            return new IpCallLegConnectionImpl(callLeg);
        }
        else {
            throw new ResourceException("Unrecognized TpCallLegIdentifier");
        }
    }

    /* (non-Javadoc)
     * @see org.mobicents.csapi.jr.slee.IpServiceConnection#close()
     */
    public void closeConnection() throws ResourceException {
        // should never be called
    }

    public org.csapi.cc.mpccs.TpMultiPartyCallIdentifier getParlayTpMultiPartyCallIdentifier() {
        // TODO   BETTER TO STORE FULL PARLAY TYPE
        return new org.csapi.cc.mpccs.TpMultiPartyCallIdentifier(ipMultiPartyCall,callSessionID);
    }

}