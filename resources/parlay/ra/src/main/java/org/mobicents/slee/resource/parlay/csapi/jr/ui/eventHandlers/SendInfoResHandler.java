package org.mobicents.slee.resource.parlay.csapi.jr.ui.eventHandlers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mobicents.slee.resource.parlay.csapi.jr.ui.activity.ui.AbstractUI;
import org.mobicents.slee.resource.parlay.csapi.jr.ui.activity.uimanager.UIManager;

/**
 * 
 */
public class SendInfoResHandler implements Runnable {
    /**
     * Log4J Logger for this class.
     */
    private static final Log logger = LogFactory
            .getLog(SendInfoResHandler.class);

    private final transient UIManager uiManager;

    private final transient int userInteractionSessionID;

    private final transient org.csapi.ui.TpUIReport response;

    private final transient int assignmentID;

    public SendInfoResHandler(UIManager uiManager,
            int userInteractionSessionID, int assignmentID,
            org.csapi.ui.TpUIReport response) {
        super();
        this.uiManager = uiManager;
        this.userInteractionSessionID = userInteractionSessionID;
        this.response = response;
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
                final AbstractUI ui = uiManager.getAbstractUI(userInteractionSessionID);
                if (ui != null) {
                    ui.sendInfoRes(userInteractionSessionID, assignmentID,
                            response);
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
            logger.error("SendInfoResHandler failed", e);
        }

    }

}
