package org.mobicents.slee.resource.parlay.csapi.jr.ui.eventHandlers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csapi.ui.TpUIIdentifier;
import org.mobicents.slee.resource.parlay.csapi.jr.ui.activity.uimanager.UIManager;

/**
 * 
 */
public class UserInteractionAbortedHandler implements Runnable {
    /**
     * Log4J Logger for this class.
     */
    private static final Log logger = LogFactory
            .getLog(UserInteractionAbortedHandler.class);

    private final transient UIManager uiManager;

    private final transient TpUIIdentifier uiReference;

    public UserInteractionAbortedHandler(UIManager uiManager,
            org.csapi.ui.TpUIIdentifier userInteraction) {
        super();
        this.uiManager = uiManager;
        this.uiReference = userInteraction;

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Runnable#run()
     */
    public void run() {
        try {
            if (uiManager != null) {
                uiManager.userInteractionAborted(uiReference);
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("UIManager activity no longer exists.");
                }
            }
        } catch (RuntimeException e) {
            // Catch all
            logger.error("UserInteractionAbortedHandler failed", e);
        }

    }

}
