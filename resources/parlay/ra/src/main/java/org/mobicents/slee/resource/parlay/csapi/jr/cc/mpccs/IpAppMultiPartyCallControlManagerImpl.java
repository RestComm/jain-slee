package org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csapi.cc.TpCallMonitorMode;
import org.csapi.cc.TpCallNotificationInfo;
import org.csapi.cc.mpccs.IpAppCallLeg;
import org.csapi.cc.mpccs.IpAppMultiPartyCallControlManagerPOA;
import org.csapi.cc.mpccs.TpAppCallLegCallBack;
import org.csapi.cc.mpccs.TpAppMultiPartyCallBack;
import org.csapi.cc.mpccs.TpCallLegIdentifier;
import org.csapi.cc.mpccs.TpMultiPartyCallIdentifier;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.callleg.CallLeg;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.multipartycall.MultiPartyCall;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.multipartycallcontrolmanager.MultiPartyCallControlManager;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.eventHandlers.CallAbortedHandler;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.eventHandlers.CallOverloadCeasedHandler;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.eventHandlers.CallOverloadEncounteredHandler;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.eventHandlers.ManagerInterruptedHandler;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.eventHandlers.ManagerResumedHandler;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.eventHandlers.ReportNotificationHandler;
import org.omg.PortableServer.POA;

import EDU.oswego.cs.dl.util.concurrent.Executor;

/**
 * 
 * Class Description for IpAppMultiPartyCallControlManagerImpl
 */
public final class IpAppMultiPartyCallControlManagerImpl extends
        IpAppMultiPartyCallControlManagerPOA {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory
            .getLog(IpAppMultiPartyCallControlManagerImpl.class);

    public IpAppMultiPartyCallControlManagerImpl(
            MultiPartyCallControlManager multiPartyCallControlManager,
            POA defaultPOA, Executor ipAppMultiPartyCallControlManagerExecutor) {
        super();
        this.multiPartyCallControlManager = multiPartyCallControlManager;
        this.defaultPOA = defaultPOA;
        setExecutor(ipAppMultiPartyCallControlManagerExecutor);
    }

    private transient MultiPartyCallControlManager multiPartyCallControlManager;

    private transient POA defaultPOA;

    private transient Executor executor;

    public void dispose() {
        multiPartyCallControlManager = null;
        defaultPOA = null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.omg.PortableServer.Servant#_default_POA()
     */
    public POA _default_POA() {
        return defaultPOA;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.csapi.cc.mpccs.IpAppMultiPartyCallControlManagerOperations#reportNotification(org.csapi.cc.mpccs.TpMultiPartyCallIdentifier,
     *      org.csapi.cc.mpccs.TpCallLegIdentifier[],
     *      org.csapi.cc.TpCallNotificationInfo, int)
     */
    public TpAppMultiPartyCallBack reportNotification(
            final TpMultiPartyCallIdentifier callReference,
            final TpCallLegIdentifier[] callLegReferenceSet,
            final TpCallNotificationInfo callNotificationInfo, final int assignmentID) {

        final TpAppMultiPartyCallBack result = new TpAppMultiPartyCallBack();

        org.mobicents.csapi.jr.slee.cc.mpccs.TpMultiPartyCallIdentifier multiPartyCallIdentifier = null;
        org.mobicents.csapi.jr.slee.cc.mpccs.TpCallLegIdentifier[] callLegIdentifiers = null;

       if (callNotificationInfo.CallEventInfo.CallMonitorMode.value() == TpCallMonitorMode._P_CALL_MONITOR_MODE_INTERRUPT) {

            // In interrupt mode, need to create internal call FSM

            // Check if callIdentifier is known
            MultiPartyCall multiPartyCall = multiPartyCallControlManager
                    .getMultiPartyCall(callReference.CallSessionID);
            IpAppCallLeg[] appCallLegs = new IpAppCallLeg[callLegReferenceSet.length];
            if (multiPartyCall == null) {
                // New call
                multiPartyCall = multiPartyCallControlManager
                        .createCall(callReference);
                multiPartyCallIdentifier = multiPartyCall
                        .getTpMultiPartyCallIdentifier();

                // New call legs
                callLegIdentifiers = new org.mobicents.csapi.jr.slee.cc.mpccs.TpCallLegIdentifier[callLegReferenceSet.length];
                if (callLegReferenceSet.length > 0) {
                    for (int i = 0; i < callLegReferenceSet.length; i++) {
                        CallLeg callLeg = multiPartyCallControlManager
                                .createCallLeg(multiPartyCall,
                                        callLegReferenceSet[i]);

                        callLegIdentifiers[i] = callLeg
                                .getTpCallLegIdentifier();
                        appCallLegs[i] = multiPartyCall.getIpAppCallLeg();
                    }
                }
            }
            else {
                // Event on existing call
                multiPartyCallIdentifier = multiPartyCall
                        .getTpMultiPartyCallIdentifier();

                callLegIdentifiers = new org.mobicents.csapi.jr.slee.cc.mpccs.TpCallLegIdentifier[callLegReferenceSet.length];
                if (callLegReferenceSet.length > 0) {
                    for (int i = 0; i < callLegReferenceSet.length; i++) {
                        multiPartyCallControlManager
                                .createCallLeg(multiPartyCall,
                                        callLegReferenceSet[i]);

                        callLegIdentifiers[i] = multiPartyCall.getCallLeg(
                                callLegReferenceSet[i].CallLegSessionID)
                                .getTpCallLegIdentifier();
                        appCallLegs[i] = multiPartyCall.getIpAppCallLeg();
                    }
                }
            }
            result.AppMultiPartyCallAndCallLeg(new TpAppCallLegCallBack(
                    multiPartyCallControlManager.getIpAppMultiPartyCall(),
                    appCallLegs));
        }
        else {
            result.AppMultiPartyCallAndCallLeg(new TpAppCallLegCallBack(
                    null,
                    new IpAppCallLeg[callLegReferenceSet.length]));
        }

        ReportNotificationHandler handler = new ReportNotificationHandler(
                multiPartyCallControlManager, multiPartyCallIdentifier,
                callLegIdentifiers, callNotificationInfo, assignmentID);

        try {
            executor.execute(handler);
        }
        catch (InterruptedException e) {
            logger.error("Interrupted handling reportNotification");
        }

        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.csapi.cc.mpccs.IpAppMultiPartyCallControlManagerOperations#callAborted(int)
     */
    public void callAborted(final int callSessionID) {
        if (logger.isDebugEnabled()) {
            logger.debug("Received callAborted(" + callSessionID + ")");
        }

        final CallAbortedHandler handler = new CallAbortedHandler(
                multiPartyCallControlManager, callSessionID);

        try {
            executor.execute(handler);
        }
        catch (InterruptedException e) {
            logger.error("Interrupted handling callaborted");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.csapi.cc.mpccs.IpAppMultiPartyCallControlManagerOperations#managerInterrupted()
     */
    public void managerInterrupted() {
        if (logger.isDebugEnabled()) {
            logger.debug("Received managerInterrupted()");
        }

        final ManagerInterruptedHandler handler = new ManagerInterruptedHandler(
                multiPartyCallControlManager);

        try {
            executor.execute(handler);
        }
        catch (InterruptedException e) {
            logger.error("Interrupted handling managerInterrupted");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.csapi.cc.mpccs.IpAppMultiPartyCallControlManagerOperations#managerResumed()
     */
    public void managerResumed() {
        if (logger.isDebugEnabled()) {
            logger.debug("Received managerResumed()");
        }

        final ManagerResumedHandler handler = new ManagerResumedHandler(
                multiPartyCallControlManager);

        try {
            executor.execute(handler);
        }
        catch (InterruptedException e) {
            logger.error("Interrupted handling managerResumed");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.csapi.cc.mpccs.IpAppMultiPartyCallControlManagerOperations#callOverloadEncountered(int)
     */
    public void callOverloadEncountered(final int arg0) {
        if (logger.isDebugEnabled()) {
            logger.debug("Received callOverloadEncountered(" + arg0 + ")");
        }

        final CallOverloadEncounteredHandler handler = new CallOverloadEncounteredHandler(
                multiPartyCallControlManager, arg0);

        try {
            executor.execute(handler);
        }
        catch (InterruptedException e) {
            logger.error("Interrupted handling callOverloadEncountered");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.csapi.cc.mpccs.IpAppMultiPartyCallControlManagerOperations#callOverloadCeased(int)
     */
    public void callOverloadCeased(final int arg0) {
        if (logger.isDebugEnabled()) {
            logger.debug("Received callOverloadCeased(" + arg0 + ")");
        }

        final CallOverloadCeasedHandler handler = new CallOverloadCeasedHandler(
                multiPartyCallControlManager, arg0);

        try {
            executor.execute(handler);
        }
        catch (InterruptedException e) {
            logger.error("Interrupted handling callOverloadCeased");
        }
    }

     

    /**
     * @param executor
     *            The executor to set.
     */
    private void setExecutor(final Executor executor) {
        this.executor = executor;
    }
}