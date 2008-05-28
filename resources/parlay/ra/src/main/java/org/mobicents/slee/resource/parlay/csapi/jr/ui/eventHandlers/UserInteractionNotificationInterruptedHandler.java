package org.mobicents.slee.resource.parlay.csapi.jr.ui.eventHandlers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mobicents.slee.resource.parlay.csapi.jr.ui.activity.uimanager.UIManager;

/**
 * 
 */
public class UserInteractionNotificationInterruptedHandler implements Runnable {
    /**
     * Log4J Logger for this class.
     */
    private static final Log logger = LogFactory
            .getLog(UserInteractionNotificationInterruptedHandler.class);

    private final transient UIManager uiManager;

    public UserInteractionNotificationInterruptedHandler(UIManager uiManager) {
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
                uiManager.userInteractionNotificationInterrupted();
            } else {

                if (logger.isDebugEnabled()) {
                    logger.debug("UIManager activity no longer exists.");
                }
            }
        } catch (RuntimeException e) {
            // Catch all
            logger.error(
                    "UserInteractionNotificationInterruptedHandler failed", e);
        }

    }

}
