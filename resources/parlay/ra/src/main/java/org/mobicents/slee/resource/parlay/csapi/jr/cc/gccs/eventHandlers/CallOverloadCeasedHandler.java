
package org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.eventHandlers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.activity.callcontrolmanager.CallControlManager;

/**
 *
 **/
public class CallOverloadCeasedHandler implements Runnable {
    /**
     * Log4J Logger for this class.
     */
    private static final Log logger = LogFactory
        .getLog(CallOverloadCeasedHandler.class);
    
    private final transient CallControlManager callControlManager;
    private final transient int assignmentID;

    /**
     * @param callControlManager
     * @param assignmentID
     */
    public CallOverloadCeasedHandler(CallControlManager callControlManager, int assignmentID) {
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
                callControlManager.callOverloadCeased(assignmentID);
            }
        } catch (RuntimeException e) {
            // Catch all
            logger.error("CallOverloadCeasedHandler failed", e);
        } 
    }

}
