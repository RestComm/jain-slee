package org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.eventHandlers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csapi.cc.gccs.TpCallFault;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.activity.call.Call;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.activity.callcontrolmanager.CallControlManager;

/**
 *
 **/
public class CallFaultDetectedHandler implements Runnable {
    /**
     * Log4J Logger for this class.
     */
    private static final Log logger = LogFactory
        .getLog(CallFaultDetectedHandler.class);

    private final transient CallControlManager callControlManager; 
    private final transient int callSessionID; 
    private final transient TpCallFault fault; 
    
    /**
     * @param callControlManager
     * @param callSessionID
     * @param fault
     */
    public CallFaultDetectedHandler(CallControlManager callControlManager, int callSessionID, TpCallFault fault) {
        super();
        this.callControlManager = callControlManager;
        this.callSessionID = callSessionID;
        this.fault = fault;
    }

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    public void run() {
        try {
            if (callControlManager != null) {
	            final Call call = callControlManager.getCall(callSessionID);
	            if(call != null ) {
	                call.callFaultDetected(callSessionID, fault);
	            }
            }
        } catch (RuntimeException e) {
            // Catch all
            logger.error("CallFaultDetectedHandler failed", e);
        }
    }

}
