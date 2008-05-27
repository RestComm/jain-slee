package org.mobicents.slee.resource.parlay.csapi.jr.ui;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csapi.ui.IpAppUI;
import org.csapi.ui.IpAppUIManagerPOA;
import org.csapi.ui.TpUIEventInfo;
import org.csapi.ui.TpUIEventNotificationInfo;
import org.csapi.ui.TpUIIdentifier;
import org.mobicents.slee.resource.parlay.csapi.jr.ui.activity.ui.UIGeneric;
import org.mobicents.slee.resource.parlay.csapi.jr.ui.activity.uimanager.UIManager;
import org.mobicents.slee.resource.parlay.csapi.jr.ui.eventHandlers.ReportEventNotificationHandler;
import org.mobicents.slee.resource.parlay.csapi.jr.ui.eventHandlers.ReportNotificationHandler;
import org.mobicents.slee.resource.parlay.csapi.jr.ui.eventHandlers.UserInteractionAbortedHandler;
import org.mobicents.slee.resource.parlay.csapi.jr.ui.eventHandlers.UserInteractionNotificationContinuedHandler;
import org.mobicents.slee.resource.parlay.csapi.jr.ui.eventHandlers.UserInteractionNotificationInterruptedHandler;
import org.omg.PortableServer.POA;

import EDU.oswego.cs.dl.util.concurrent.Executor;

/**
 * 
 */
public class IpAppUIManagerImpl extends IpAppUIManagerPOA {
    /**
     * Log4J Logger for this class.
     */
    private static final Log logger = LogFactory
            .getLog(IpAppUIManagerImpl.class);

    private transient UIManager uiManager;

    private transient POA defaultPOA;

    private transient Executor executor;

    public IpAppUIManagerImpl(UIManager uiManager, POA defaultPOA, Executor ipAppUIManagerExecutor) {
        super();
        this.uiManager = uiManager;
        this.defaultPOA = defaultPOA;
        setExecutor(ipAppUIManagerExecutor);
    }

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

    /**
     * @param ipAppUIManagerExecutor
     */
    private void setExecutor(final Executor ipAppUIManagerExecutor) {
        executor = ipAppUIManagerExecutor;

    }

    public void userInteractionAborted(final TpUIIdentifier userInteraction) {
        final UserInteractionAbortedHandler handler = new UserInteractionAbortedHandler(
                uiManager, userInteraction);

        try {
            executor.execute(handler);
        } catch (InterruptedException e) {
            logger.error("Interrupted handling.");
        }

    }

    public IpAppUI reportNotification(final TpUIIdentifier userInteraction,
            final TpUIEventInfo eventInfo, final int assignmentID) {


        final UIGeneric uiGeneric = uiManager.createUIGeneric(userInteraction);
        
        final ReportNotificationHandler handler = new ReportNotificationHandler(
                uiManager, uiGeneric.getTpUIIdentifier(), eventInfo,
                assignmentID);
        try {        
            executor.execute(handler);
        } catch (InterruptedException e) {
            logger.error("Interrupted handling");
        }
        
        return uiManager.getIpAppUI();
    }
    
    public IpAppUI reportEventNotification(final TpUIIdentifier userInteraction,
            final TpUIEventNotificationInfo eventNotificationInfo, final int assignmentID) {

        final UIGeneric uiGeneric = uiManager.createUIGeneric(userInteraction);
        
        final ReportEventNotificationHandler handler = new ReportEventNotificationHandler(
                uiManager, uiGeneric.getTpUIIdentifier(),
                eventNotificationInfo, assignmentID);
        try {
            executor.execute(handler);
        } catch (InterruptedException e) {
            logger.error("Interrupted handling");
        }

        return uiManager.getIpAppUI();
    }
    

    public void userInteractionNotificationInterrupted() {
        final UserInteractionNotificationInterruptedHandler handler = new UserInteractionNotificationInterruptedHandler(
                uiManager);
        try {
            executor.execute(handler);
        } catch (InterruptedException e) {
            logger.error("Interrupted handling.");
        }

    }

    public void userInteractionNotificationContinued() {
        final UserInteractionNotificationContinuedHandler handler = new UserInteractionNotificationContinuedHandler(
                uiManager);
        try {
            executor.execute(handler);
        } catch (InterruptedException e) {
            logger.error("Interrupted handling.");
        }

    }



}
