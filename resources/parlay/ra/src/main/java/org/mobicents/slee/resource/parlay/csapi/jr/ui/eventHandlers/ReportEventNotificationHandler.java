package org.mobicents.slee.resource.parlay.csapi.jr.ui.eventHandlers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csapi.ui.TpUIEventNotificationInfo;
import org.mobicents.csapi.jr.slee.ui.TpUIIdentifier;
import org.mobicents.slee.resource.parlay.csapi.jr.ui.activity.uimanager.UIManager;

/**
 * 
 */
public class ReportEventNotificationHandler implements Runnable {
    /**
     * Log4J Logger for this class.
     */ 
    private static final Log logger = LogFactory
            .getLog(ReportEventNotificationHandler.class);

    private final transient UIManager uiManager;

    private final transient TpUIIdentifier uiReference;

    private final transient TpUIEventNotificationInfo eventNotificationInfo;

    private final transient int assignmentID;

    public ReportEventNotificationHandler(UIManager uiManager,
            TpUIIdentifier uiReference,
            org.csapi.ui.TpUIEventNotificationInfo eventNotificationInfo,
            int assignmentID) {
        super();
        this.uiManager = uiManager;
        this.uiReference = uiReference;
        this.eventNotificationInfo = eventNotificationInfo;
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
                uiManager.reportEventNotification(uiReference,
                        eventNotificationInfo, assignmentID);
            } else {
                if (logger.isDebugEnabled()) {
                    logger
                            .debug("UIManager activity no longer exists. AssignmentID = ["
                                    + assignmentID + "]");
                }
            }

        } catch (RuntimeException e) {
            // Catch all
            logger.error("ReportEventNotificationHandler failed", e);
        }

    }

}
