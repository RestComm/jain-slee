package org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.eventHandlers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.activity.callcontrolmanager.CallControlManager;

/**
 *
 **/
public class CallOverloadEncounteredHandler implements Runnable {
    /**
     * Log4J Logger for this class.
     */
    private static final Log logger = LogFactory
        .getLog(CallOverloadEncounteredHandler.class);

    private final transient CallControlManager callControlManager;
    private final transient int assignmentID;
    
    /**
     * @param callControlManager
     * @param assignmentID
     */
    public CallOverloadEncounteredHandler(CallControlManager callControlManager, int assignmentID) {
        super();
        this.callControlManager = callControlManager;
        this.assignmentID = assignmentID;
    }

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    public void run() {
        try {
            if (callControlManager != null) {               
                callControlManager.callOverloadEncountered(assignmentID);
            }
        } catch (RuntimeException e) {
            // Catch all
            logger.error("CallOverloadEncounteredHandler failed", e);
        } 
    }

}
