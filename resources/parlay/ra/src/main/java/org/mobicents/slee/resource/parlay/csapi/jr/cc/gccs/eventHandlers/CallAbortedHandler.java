package org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.eventHandlers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.activity.callcontrolmanager.CallControlManager;

/**
 *
 **/
public class CallAbortedHandler implements Runnable {
    /**
     * Log4J Logger for this class.
     */
    private static final Log logger = LogFactory
        .getLog(CallAbortedHandler.class);
    
    private final transient CallControlManager callControlManager;
    private final transient int callReference;

    /**
     * @param callControlManager
     * @param callReference
     */
    public CallAbortedHandler(CallControlManager callControlManager, int callReference) {
        super();
        this.callControlManager = callControlManager;
        this.callReference = callReference;
    }

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    public void run() {
        try {
            if (callControlManager != null) {               
                callControlManager.callAborted(callReference);
            }
        } catch (RuntimeException e) {
            // Catch all
            logger.error("CallAbortedHandler failed", e);
        }
    }

}
