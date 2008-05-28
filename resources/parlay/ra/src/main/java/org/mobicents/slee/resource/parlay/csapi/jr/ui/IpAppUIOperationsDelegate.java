package org.mobicents.slee.resource.parlay.csapi.jr.ui;

import org.apache.log4j.Logger;
import org.csapi.ui.IpAppUIOperations;
import org.csapi.ui.TpUIError;
import org.csapi.ui.TpUIFault;
import org.csapi.ui.TpUIReport;
import org.mobicents.slee.resource.parlay.csapi.jr.ui.activity.uimanager.UIManager;
import org.mobicents.slee.resource.parlay.csapi.jr.ui.eventHandlers.SendInfoAndCollectErrHandler;
import org.mobicents.slee.resource.parlay.csapi.jr.ui.eventHandlers.SendInfoAndCollectResHandler;
import org.mobicents.slee.resource.parlay.csapi.jr.ui.eventHandlers.SendInfoErrHandler;
import org.mobicents.slee.resource.parlay.csapi.jr.ui.eventHandlers.SendInfoResHandler;
import org.mobicents.slee.resource.parlay.csapi.jr.ui.eventHandlers.UserInteractionFaultDetectedHandler;

import EDU.oswego.cs.dl.util.concurrent.Executor;

/** Used where same callbacks used in several IpApps */ 
public class IpAppUIOperationsDelegate implements IpAppUIOperations {
    /**
     * Logger for this class.
     */
    private static final Logger logger = Logger
            .getLogger(IpAppUIOperationsDelegate.class);

    private final transient UIManager uiManager;

    private final transient Executor[] executors;

    public IpAppUIOperationsDelegate(UIManager uiManager, Executor[] executors) {
        super();
        this.uiManager = uiManager;
        this.executors = executors;
    }

    public void sendInfoRes(final int userInteractionSessionID, final int assignmentID,
            final TpUIReport response) {
        final SendInfoResHandler handler = new SendInfoResHandler(uiManager,
                userInteractionSessionID, assignmentID, response);
        try {             
            executors[userInteractionSessionID % executors.length]
                    .execute(handler);
        } catch (InterruptedException e) {
            logger.error("Interrupted handling sendInfoRes");
        }
    }

    public void sendInfoErr(final int userInteractionSessionID, final int assignmentID,
            final TpUIError error) {
        final SendInfoErrHandler handler = new SendInfoErrHandler(uiManager,
                userInteractionSessionID, assignmentID, error);
        try {
            executors[userInteractionSessionID % executors.length]
                    .execute(handler);
        } catch (InterruptedException e) {
            logger.error("Interrupted handling sendInfoErr");
        }

    }

    public void sendInfoAndCollectRes(final int userInteractionSessionID,
            final int assignmentID, final TpUIReport response, final String collectedInfo) {
        final SendInfoAndCollectResHandler handler = new SendInfoAndCollectResHandler(
                uiManager, userInteractionSessionID, assignmentID, response,
                collectedInfo);
        try {
            executors[userInteractionSessionID % executors.length]
                    .execute(handler);
        } catch (InterruptedException e) {
            logger.error("Interrupted handling sendInfoAndCollectRes");
        }

    }

    public void sendInfoAndCollectErr(final int userInteractionSessionID,
            final int assignmentID, final TpUIError error) {
        final SendInfoAndCollectErrHandler handler = new SendInfoAndCollectErrHandler(
                uiManager, userInteractionSessionID, assignmentID, error);
        try {
            executors[userInteractionSessionID % executors.length]
                    .execute(handler);
        } catch (InterruptedException e) {
            logger.error("Interrupted handling sendInfoAndCollectErr");
        }

    }

    public void userInteractionFaultDetected(final int userInteractionSessionID,
            final TpUIFault fault) {
        final UserInteractionFaultDetectedHandler handler = new UserInteractionFaultDetectedHandler(
                uiManager, userInteractionSessionID, fault);
        try {
            executors[userInteractionSessionID % executors.length]
                    .execute(handler);
        } catch (InterruptedException e) {
            logger.error("Interrupted handling userInteractionFaultDetected");
        }

    }

  

}
