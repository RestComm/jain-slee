package org.mobicents.slee.resource.parlay.csapi.jr.ui.eventHandlers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csapi.ui.TpUIFault;
import org.mobicents.slee.resource.parlay.csapi.jr.ui.activity.ui.AbstractUI;
import org.mobicents.slee.resource.parlay.csapi.jr.ui.activity.uimanager.UIManager;

/**
 * 
 */
public class UserInteractionFaultDetectedHandler implements Runnable {
    /**
     * Log4J Logger for this class.
     */
    private static final Log logger = LogFactory
            .getLog(UserInteractionFaultDetectedHandler.class);

    private final transient UIManager uiManager;

    private final transient int userInteractionSessionID;

    private final transient TpUIFault fault;

    public UserInteractionFaultDetectedHandler(UIManager uiManager,
            int userInteractionSessionID, TpUIFault fault) {
        super();
        this.uiManager = uiManager;
        this.userInteractionSessionID = userInteractionSessionID;
        this.fault = fault;

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Runnable#run()
     */
    public void run() {
        try {
            if (uiManager != null) {
                final AbstractUI ui = uiManager.getAbstractUI(userInteractionSessionID);
                if (ui != null) {
                    ui.userInteractionFaultDetected(userInteractionSessionID,
                            fault);
                } else {
                    if (logger.isDebugEnabled()) {
                        logger.debug("AbstractUI activity no longer exists.");
                    }
                }
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("UIManager activity no longer exists.");
                } 
            }
        } catch (RuntimeException e) {
            // Catch all
            logger.error("UserInteractionFaultDetectedHandler failed", e);
        }

    }

}
