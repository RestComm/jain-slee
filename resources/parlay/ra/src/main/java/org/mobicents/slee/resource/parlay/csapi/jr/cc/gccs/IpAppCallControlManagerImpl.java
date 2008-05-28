package org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csapi.cc.TpCallMonitorMode;
import org.csapi.cc.gccs.IpAppCall;
import org.csapi.cc.gccs.IpAppCallControlManagerPOA;
import org.csapi.cc.gccs.TpCallEventInfo;
import org.csapi.cc.gccs.TpCallIdentifier;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.activity.call.Call;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.activity.callcontrolmanager.CallControlManager;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.eventHandlers.CallAbortedHandler;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.eventHandlers.CallEventNotifyHandler;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.eventHandlers.CallNotificationContinuedHandler;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.eventHandlers.CallNotificationInterruptedHandler;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.eventHandlers.CallOverloadCeasedHandler;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.eventHandlers.CallOverloadEncounteredHandler;
import org.omg.PortableServer.POA;

import EDU.oswego.cs.dl.util.concurrent.Executor;

/**
 *
 **/
public class IpAppCallControlManagerImpl extends IpAppCallControlManagerPOA {
    /**
     * Log4J Logger for this class.
     */
    private static final Log logger = LogFactory
        .getLog(IpAppCallControlManagerImpl.class);
    

    private transient CallControlManager callControlManager;
    
    private transient POA defaultPOA;

    private transient Executor executor;
    
    public IpAppCallControlManagerImpl(CallControlManager callControlManager, POA defaultPOA, Executor ipAppCallControlManagerExecutor) {
        super();
        this.callControlManager = callControlManager;
        this.defaultPOA = defaultPOA;
        setExecutor(ipAppCallControlManagerExecutor);
    }
    
    public void dispose() {
        callControlManager = null;
        defaultPOA = null;
    }
    
    /* (non-Javadoc)
     * @see org.omg.PortableServer.Servant#_default_POA()
     */
    public POA _default_POA() {
        return defaultPOA;
    }
    

    /* (non-Javadoc)
     * @see org.csapi.cc.gccs.IpAppCallControlManagerOperations#callAborted(int)
     */
    public void callAborted(final int callReference) {
        final CallAbortedHandler handler = new CallAbortedHandler(
                callControlManager, callReference);

        try {
            executor.execute(handler);
        }
        catch (InterruptedException e) {
            logger.error("Interrupted handling callAborted");
        }
        
    }

    /* (non-Javadoc)
     * @see org.csapi.cc.gccs.IpAppCallControlManagerOperations#callEventNotify(org.csapi.cc.gccs.TpCallIdentifier, org.csapi.cc.gccs.TpCallEventInfo, int)
     */
    public IpAppCall callEventNotify(final TpCallIdentifier callReference, final TpCallEventInfo eventInfo, final int assignmentID) {
        Call call = null;
        //IpAppCall result = null;
        
        org.mobicents.csapi.jr.slee.cc.gccs.TpCallIdentifier callIdentifier = null;
        //Check if callIdentifier is unknown
        if (eventInfo.MonitorMode.value() == TpCallMonitorMode._P_CALL_MONITOR_MODE_INTERRUPT) {
            call = callControlManager.getCall(callReference.CallSessionID);
            if (call == null) {
                //create new internal call
                call = callControlManager.createCall(callReference);
            }
            //result = callControlManager.getIpAppCall();
            callIdentifier = call.getTpCallIdentifier();
        }
        
        final CallEventNotifyHandler handler = new CallEventNotifyHandler(
                callControlManager, callIdentifier, eventInfo, assignmentID);

        try {
            executor.execute(handler);
        }
        catch (InterruptedException e) {
            logger.error("Interrupted handling callEventNotify");
        }

        return callControlManager.getIpAppCall();
    }

    /* (non-Javadoc)
     * @see org.csapi.cc.gccs.IpAppCallControlManagerOperations#callNotificationInterrupted()
     */
    public void callNotificationInterrupted() {
        final CallNotificationInterruptedHandler handler = new CallNotificationInterruptedHandler(callControlManager);

        try {
            executor.execute(handler);
        }
        catch (InterruptedException e) {
            logger.error("Interrupted handling callNotificationInterrupted");
        }
        
    }

    /* (non-Javadoc)
     * @see org.csapi.cc.gccs.IpAppCallControlManagerOperations#callNotificationContinued()
     */
    public void callNotificationContinued() {
        final CallNotificationContinuedHandler handler = new CallNotificationContinuedHandler(callControlManager);

        try {
            executor.execute(handler);
        }
        catch (InterruptedException e) {
            logger.error("Interrupted handling callNotificationContinued");
        }
    }

    /* (non-Javadoc)
     * @see org.csapi.cc.gccs.IpAppCallControlManagerOperations#callOverloadEncountered(int)
     */
    public void callOverloadEncountered(final int assignmentID) {
        final CallOverloadEncounteredHandler handler = new CallOverloadEncounteredHandler(callControlManager, assignmentID);

        try {
            executor.execute(handler);
        }
        catch (InterruptedException e) {
            logger.error("Interrupted handling callOverloadEncountered");
        }
    }

    /* (non-Javadoc)
     * @see org.csapi.cc.gccs.IpAppCallControlManagerOperations#callOverloadCeased(int)
     */
    public void callOverloadCeased(final int assignmentID) {
        final CallOverloadCeasedHandler handler = new CallOverloadCeasedHandler(callControlManager, assignmentID);

        try {
            executor.execute(handler);
        }
        catch (InterruptedException e) {
            logger.error("Interrupted handling callOverloadCeased");
        }
    }

    /**
     * @param ipAppCallControlManagerExecutor
     */
    private void setExecutor(final Executor ipAppCallControlManagerExecutor) {
        executor = ipAppCallControlManagerExecutor;
        
    }

}
