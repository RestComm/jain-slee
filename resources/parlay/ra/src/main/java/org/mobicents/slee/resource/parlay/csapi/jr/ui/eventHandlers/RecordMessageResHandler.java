package org.mobicents.slee.resource.parlay.csapi.jr.ui.eventHandlers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mobicents.slee.resource.parlay.csapi.jr.ui.activity.uicall.UICall;
import org.mobicents.slee.resource.parlay.csapi.jr.ui.activity.uimanager.UIManager;

/**
 * 
 */
public class RecordMessageResHandler implements Runnable {
    /**
     * Log4J Logger for this class.
     */
    private static final Log logger = LogFactory
            .getLog(RecordMessageResHandler.class);

    private final transient UIManager uiManager;

    private final transient int userInteractionSessionID;

    private final transient org.csapi.ui.TpUIReport response;
 
    private final transient int messageID;

    private final transient int assignmentID;

    public RecordMessageResHandler(UIManager uiManager,
            int userInteractionSessionID, int assignmentID,
            org.csapi.ui.TpUIReport response, int messageID) {
        super();
        this.uiManager = uiManager;
        this.userInteractionSessionID = userInteractionSessionID;
        this.response = response;
        this.messageID = messageID;
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
                    uiCall.recordMessageRes(userInteractionSessionID,
                            assignmentID, response, messageID);
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
            logger.error("RecordMessageResHandler failed", e);
        }

    }

}
