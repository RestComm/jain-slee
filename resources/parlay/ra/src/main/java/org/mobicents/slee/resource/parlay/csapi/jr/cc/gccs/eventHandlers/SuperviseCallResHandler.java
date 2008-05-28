package org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.eventHandlers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.activity.call.Call;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.activity.callcontrolmanager.CallControlManager;

/**
 *
 **/
public class SuperviseCallResHandler implements Runnable {
    /**
     * Log4J Logger for this class.
     */
    private static final Log logger = LogFactory
        .getLog(SuperviseCallResHandler.class);

    private final transient CallControlManager callControlManager; 
    private final transient int callSessionID; 
    private final transient int report; 
    private final transient int usedTime;
    /**
     * @param callControlManager
     * @param callSessionID
     * @param report
     * @param usedTime
     */
    public SuperviseCallResHandler(CallControlManager callControlManager, int callSessionID, int report, int usedTime) {
        super();
        this.callControlManager = callControlManager;
        this.callSessionID = callSessionID;
        this.report = report;
        this.usedTime = usedTime;
    }

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    public void run() {
        try {
            if (callControlManager != null) {
	            final Call call = callControlManager.getCall(callSessionID);
	            if(call != null ) {
	                call.superviseCallRes(callSessionID, report, usedTime);
	            }
            }
        } catch (RuntimeException e) {
            // Catch all
            logger.error("SuperviseCallResHandler failed", e);
        }
    }

}
