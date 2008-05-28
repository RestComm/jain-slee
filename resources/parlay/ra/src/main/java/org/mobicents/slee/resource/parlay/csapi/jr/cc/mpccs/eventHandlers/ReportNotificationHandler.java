package org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.eventHandlers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csapi.cc.TpCallNotificationInfo;
import org.mobicents.csapi.jr.slee.cc.mpccs.TpCallLegIdentifier;
import org.mobicents.csapi.jr.slee.cc.mpccs.TpMultiPartyCallIdentifier;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.multipartycallcontrolmanager.MultiPartyCallControlManager;

/**
 *  
 */
public final class ReportNotificationHandler implements Runnable {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory
        .getLog(ReportNotificationHandler.class);

    public ReportNotificationHandler(
            MultiPartyCallControlManager multiPartyCallControlManager,
            TpMultiPartyCallIdentifier callIdentifier,
            TpCallLegIdentifier[] callLegIdentifier,
            TpCallNotificationInfo callNotificationInfo, int assignmentID) {
        super();
        this.multiPartyCallControlManager = multiPartyCallControlManager;
        this.callIdentifier = callIdentifier;
        this.callLegIdentifier = callLegIdentifier;
        this.callNotificationInfo = callNotificationInfo;
        this.assignmentID = assignmentID;
    }

    private final transient MultiPartyCallControlManager multiPartyCallControlManager;

    private final transient TpMultiPartyCallIdentifier callIdentifier;

    private final transient TpCallLegIdentifier[] callLegIdentifier;

    private final transient TpCallNotificationInfo callNotificationInfo;

    private final transient int assignmentID;

    public void run() {
        try {
            if (multiPartyCallControlManager != null) {
                multiPartyCallControlManager.reportNotification(callIdentifier,
                        callLegIdentifier, callNotificationInfo, assignmentID);
            }
        }
        catch (RuntimeException e) {
            // Catch all
            logger.error("ReportNotificationHandler failed", e);
        }

    }

}