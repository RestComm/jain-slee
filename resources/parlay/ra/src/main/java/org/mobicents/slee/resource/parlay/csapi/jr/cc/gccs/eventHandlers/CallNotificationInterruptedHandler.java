package org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.eventHandlers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.activity.callcontrolmanager.CallControlManager;

/**
 *
 **/
public class CallNotificationInterruptedHandler implements Runnable {
    /**
     * Log4J Logger for this class.
     */
    private static final Log logger = LogFactory
        .getLog(CallNotificationInterruptedHandler.class);

    private final transient CallControlManager callControlManager;
    
    /**
     * @param callControlManager
     */
    public CallNotificationInterruptedHandler(CallControlManager callControlManager) {
        super();
        this.callControlManager = callControlManager;
    }

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    public void run() {
        try {
            if (callControlManager != null) {               
                callControlManager.callNotificationInterrupted();
            }
        } catch (RuntimeException e) {
            // Catch all
            logger.error("CallNotificationInterruptedHandler failed", e);
        }
    }

}
