package org.mobicents.slee.resource.parlay.csapi.jr.ui.eventHandlers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mobicents.slee.resource.parlay.csapi.jr.ui.activity.uimanager.UIManager;

/**
 * 
 */
public class UserInteractionNotificationContinuedHandler implements Runnable {
    /**
     * Log4J Logger for this class.
     */
    private static final Log logger = LogFactory
            .getLog(UserInteractionNotificationContinuedHandler.class);

    private final transient UIManager uiManager;

    public UserInteractionNotificationContinuedHandler(UIManager uiManager) {
        super();
        this.uiManager = uiManager;

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Runnable#run()
     */
    public void run() {
        try {
            if (uiManager != null) {
                uiManager.userInteractionNotificationContinued();
            } else {

                if (logger.isDebugEnabled()) {
                    logger.debug("UIManager activity no longer exists.");
                }
            }
        } catch (RuntimeException e) {
            // Catch all
            logger.error("UserInteractionNotificationContinuedHandler failed",
                    e);
        }

    }

}
