package org.mobicents.slee.resource.parlay.csapi.jr.ui;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csapi.ui.IpAppUICallPOA;
import org.csapi.ui.TpUIError;
import org.csapi.ui.TpUIFault;
import org.csapi.ui.TpUIReport;
import org.mobicents.slee.resource.parlay.csapi.jr.ui.activity.uimanager.UIManager;
import org.mobicents.slee.resource.parlay.csapi.jr.ui.eventHandlers.AbortActionErrHandler;
import org.mobicents.slee.resource.parlay.csapi.jr.ui.eventHandlers.AbortActionResHandler;
import org.mobicents.slee.resource.parlay.csapi.jr.ui.eventHandlers.DeleteMessageErrHandler;
import org.mobicents.slee.resource.parlay.csapi.jr.ui.eventHandlers.DeleteMessageResHandler;
import org.mobicents.slee.resource.parlay.csapi.jr.ui.eventHandlers.RecordMessageErrHandler;
import org.mobicents.slee.resource.parlay.csapi.jr.ui.eventHandlers.RecordMessageResHandler;
import org.omg.PortableServer.POA;

import EDU.oswego.cs.dl.util.concurrent.Executor;

/**
 * 
 */
public class IpAppUICallImpl extends IpAppUICallPOA {
    /**
     * Log4J Logger for this class.
     */
    private static final Log logger = LogFactory.getLog(IpAppUICallImpl.class);

    private transient POA defaultPOA;

    private transient UIManager uiManager;
 
    private final transient Executor[] executors;

    private final transient IpAppUIOperationsDelegate ipAppUIOperationsDelegate;

    /**
     * @param uiManager
     * @param ipAppCallPOA
     */
    public IpAppUICallImpl(UIManager uiManager, POA ipAppCallPOA,
            Executor[] executors) {
        super();
        this.uiManager = uiManager;
        this.defaultPOA = ipAppCallPOA;
        this.ipAppUIOperationsDelegate = new IpAppUIOperationsDelegate(
                uiManager, executors);
        this.executors = executors;
        

    }

    /**
     * 
     */
    public void dispose() {
        uiManager = null;
        defaultPOA = null;

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.omg.PortableServer.Servant#_default_POA()
     */
    public POA _default_POA() {
        return defaultPOA;
    }

    public void sendInfoRes(final int userInteractionSessionID, final int assignmentID,
            final TpUIReport response) {
        ipAppUIOperationsDelegate.sendInfoRes(userInteractionSessionID,
                assignmentID, response);
    }

    public void sendInfoErr(final int userInteractionSessionID, final int assignmentID,
            final TpUIError error) {
        ipAppUIOperationsDelegate.sendInfoErr(userInteractionSessionID,
                assignmentID, error);
    }

    public void sendInfoAndCollectRes(final int userInteractionSessionID,
            final int assignmentID, final TpUIReport response, final String collectedInfo) {
        ipAppUIOperationsDelegate
                .sendInfoAndCollectRes(userInteractionSessionID, assignmentID,
                        response, collectedInfo);

    }

    public void sendInfoAndCollectErr(final int userInteractionSessionID,
            final int assignmentID, final TpUIError error) {
        ipAppUIOperationsDelegate.sendInfoAndCollectErr(
                userInteractionSessionID, assignmentID, error);

    }

    public void userInteractionFaultDetected(final int userInteractionSessionID,
            final TpUIFault fault) {
        ipAppUIOperationsDelegate.userInteractionFaultDetected(
                userInteractionSessionID, fault);

    }

    public void recordMessageRes(final int userInteractionSessionID,
            final int assignmentID, final TpUIReport response, final int messageID) {
        final RecordMessageResHandler handler = new RecordMessageResHandler(
                uiManager, userInteractionSessionID, assignmentID, response,
                messageID);
        try {
            executors[userInteractionSessionID % executors.length]
                    .execute(handler);
        } catch (InterruptedException e) {
            logger.error("Interrupted handling recordMessageRes");
        }

    }

    public void recordMessageErr(final int userInteractionSessionID,
            final int assignmentID, final TpUIError error) {
        final RecordMessageErrHandler handler = new RecordMessageErrHandler(
                uiManager, userInteractionSessionID, assignmentID, error);
        try {
            executors[userInteractionSessionID % executors.length]
                    .execute(handler);
        } catch (InterruptedException e) {
            logger.error("Interrupted handling recordMessageErr");
        }

    }

    public void deleteMessageRes(final int userInteractionSessionID,
            final TpUIReport response, final int assignmentID) {
        final DeleteMessageResHandler handler = new DeleteMessageResHandler(
                uiManager, userInteractionSessionID, response, assignmentID);
        try {
            executors[userInteractionSessionID % executors.length]
                    .execute(handler);
        } catch (InterruptedException e) {
            logger.error("Interrupted handling deleteMessageRes");
        }
    }

    public void deleteMessageErr(final int userInteractionSessionID, final TpUIError error,
            final int assignmentID) {
        final DeleteMessageErrHandler handler = new DeleteMessageErrHandler(
                uiManager, userInteractionSessionID, error, assignmentID);
        try {
            executors[userInteractionSessionID % executors.length]
                    .execute(handler);
        } catch (InterruptedException e) {
            logger.error("Interrupted handling deleteMessageErr");
        }

    }

    public void abortActionRes(final int userInteractionSessionID, final int assignmentID) {
        final AbortActionResHandler handler = new AbortActionResHandler(uiManager,
                userInteractionSessionID, assignmentID);
        try {
            executors[userInteractionSessionID % executors.length]
                    .execute(handler);
        } catch (InterruptedException e) {
            logger.error("Interrupted handling abortActionRes");
        }

    }

    public void abortActionErr(final int userInteractionSessionID, final int assignmentID,
            final TpUIError error) {
        final AbortActionErrHandler handler = new AbortActionErrHandler(uiManager,
                userInteractionSessionID, assignmentID, error);
        try {
            executors[userInteractionSessionID % executors.length]
                    .execute(handler);
        } catch (InterruptedException e) {
            logger.error("Interrupted handling abortActionErr");
        }

    }

}
