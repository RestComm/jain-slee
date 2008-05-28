package org.mobicents.slee.resource.parlay.csapi.jr.ui.eventHandlers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csapi.ui.TpUIError;
import org.mobicents.slee.resource.parlay.csapi.jr.ui.activity.uicall.UICall;
import org.mobicents.slee.resource.parlay.csapi.jr.ui.activity.uimanager.UIManager;

/**
 * 
 */
public class DeleteMessageErrHandler implements Runnable {
    /** 
     * Log4J Logger for this class.
     */
    private static final Log logger = LogFactory
            .getLog(DeleteMessageErrHandler.class);

    private final transient UIManager uiManager;

    private final transient int userInteractionSessionID;

    private final transient TpUIError error;

    private final transient int messageID;

    public DeleteMessageErrHandler(UIManager uiManager,
            int userInteractionSessionID, org.csapi.ui.TpUIError error,
            int messageID) {
        super();
        this.uiManager = uiManager;
        this.userInteractionSessionID = userInteractionSessionID;
        this.error = error;
        this.messageID = messageID;

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
                    uiCall.deleteMessageErr(userInteractionSessionID, error,
                            messageID);
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
            logger.error("DeleteMessageResHandler failed", e);
        }

    }

}
