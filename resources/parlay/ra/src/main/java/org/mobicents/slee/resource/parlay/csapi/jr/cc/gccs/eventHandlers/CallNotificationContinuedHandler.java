package org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.eventHandlers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.activity.callcontrolmanager.CallControlManager;

/**
 *
 **/
public class CallNotificationContinuedHandler implements Runnable {
    /**
     * Log4J Logger for this class.
     */
    private static final Log logger = LogFactory
        .getLog(CallNotificationContinuedHandler.class);

    private final transient CallControlManager callControlManager;
    
    /**
     * @param callControlManager
     */
    public CallNotificationContinuedHandler(CallControlManager callControlManager) {
        super();
        this.callControlManager = callControlManager;
    }

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    public void run() {
        try {
            if (callControlManager != null) {               
                callControlManager.callNotificationContinued();
            }
        } catch (RuntimeException e) {
            // Catch all
            logger.error("CallNotificationContinuedHandler failed", e);
        }
    }

}
