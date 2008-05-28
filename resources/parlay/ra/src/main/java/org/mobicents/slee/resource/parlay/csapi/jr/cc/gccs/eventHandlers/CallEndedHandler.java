package org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.eventHandlers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csapi.cc.gccs.TpCallEndedReport;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.activity.call.Call;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.activity.callcontrolmanager.CallControlManager;

/**
 *
 **/
public class CallEndedHandler implements Runnable {
    /**
     * Log4J Logger for this class.
     */
    private static final Log logger = LogFactory.getLog(CallEndedHandler.class);

    private final transient CallControlManager callControlManager; 
    private final transient int callSessionID;
    private final transient TpCallEndedReport report;
    
    /**
     * @param callControlManager
     * @param callSessionID
     * @param report
     */
    public CallEndedHandler(CallControlManager callControlManager, int callSessionID, TpCallEndedReport report) {
        super();
        this.callControlManager = callControlManager;
        this.callSessionID = callSessionID;
        this.report = report;
    }

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    public void run() {
        try {
            if (callControlManager != null) {
	            final Call call = callControlManager.getCall(callSessionID);
	            if(call != null ) {
	                call.callEnded(callSessionID, report);
	            }
            }
        } catch (RuntimeException e) {
            // Catch all
            logger.error("CallEndedHandler failed", e);
        }
    }

}
