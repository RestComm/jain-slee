package org.mobicents.slee.resource.parlay.csapi.jr.ui.eventHandlers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mobicents.slee.resource.parlay.csapi.jr.ui.activity.uicall.UICall;
import org.mobicents.slee.resource.parlay.csapi.jr.ui.activity.uimanager.UIManager;

/**
 * 
 */
public class AbortActionResHandler implements Runnable {
    /**
     * Log4J Logger for this class.
     */
    private static final Log logger = LogFactory
            .getLog(AbortActionResHandler.class);

    private final transient UIManager uiManager;

    private final transient int userInteractionSessionID;

    private final transient int assignmentID;

    public AbortActionResHandler(UIManager uiManager,
            int userInteractionSessionID, int assignmentID) {
        super();
        this.uiManager = uiManager;
        this.userInteractionSessionID = userInteractionSessionID;
        this.assignmentID = assignmentID;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Runnable#run()
     */
    public void run() {
        try {
            if (uiManager != null) {
                final UICall uiCall = uiManager.getUICall(userInteractionSessionID);
                if (uiCall != null) {
                    uiCall.abortActionRes(userInteractionSessionID,
                            assignmentID);
                } else {
                    if (logger.isDebugEnabled()) {
                        logger.debug("UICall Activity no longer exists.");
                    }

                }
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("UIManager Activity no longer exists.");
                }
            }
        } catch (RuntimeException e) {
            // Catch all
            logger.error("AbortActionResHandler failed", e);
        }

    }

}
