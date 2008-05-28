package org.mobicents.slee.resource.parlay.csapi.jr.ui.activity.ui;

import javax.slee.resource.ResourceException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csapi.P_INVALID_SESSION_ID;
import org.csapi.TpCommonExceptions;
import org.csapi.ui.IpUI;
import org.csapi.ui.TpUIError;
import org.csapi.ui.TpUIFault;
import org.csapi.ui.TpUIReport;
import org.mobicents.csapi.jr.slee.ui.SendInfoAndCollectErrEvent;
import org.mobicents.csapi.jr.slee.ui.SendInfoAndCollectResEvent;
import org.mobicents.csapi.jr.slee.ui.SendInfoErrEvent;
import org.mobicents.csapi.jr.slee.ui.SendInfoResEvent;
import org.mobicents.csapi.jr.slee.ui.TpUIIdentifier;
import org.mobicents.csapi.jr.slee.ui.UserInteractionFaultDetectedEvent;
import org.mobicents.slee.resource.parlay.csapi.jr.ui.TpUIActivityHandle;
import org.mobicents.slee.resource.parlay.csapi.jr.ui.UiListener;
import org.mobicents.slee.resource.parlay.csapi.jr.ui.activity.uimanager.UIManager;
import org.mobicents.slee.resource.parlay.util.ParlayExceptionUtil;
import org.mobicents.slee.resource.parlay.util.activity.ActivityManager;


/**
 * There is some duplication between the test classes for the UIGenericImpl and
 * UICallImpl activities. There is inheritance for the implementations but not
 * here in the testers, so need to be careful to keep them in line.
 */

public class UIGenericImpl extends AbstractUIImpl implements UIGeneric {
    /*
     * Log4J Logger for this class.
     */
    private static final Log logger = LogFactory.getLog(UIGenericImpl.class);

    private final transient IpUI ipUI; 

    private final transient TpUIIdentifier tpUIIdentifier;

    /**
     * @param manager
     * @param ipUI from Parlay 
     * @param userInteractionSessionID from Parlay 
     * @param tpUIIdentifier used by SLEE API ( part is same as Parlay userInteractionSessionID)
     * @param activityManager
     * @param eventListener
     */
    public UIGenericImpl(final UIManager manager, final IpUI ipUI,
            final  int userInteractionSessionID, final TpUIIdentifier sleeTpUIIdentifier,
            final ActivityManager activityManager, final UiListener eventListener) {
        super(manager, userInteractionSessionID, activityManager, eventListener, new TpUIActivityHandle(sleeTpUIIdentifier));
        this.ipUI = ipUI;
        this.tpUIIdentifier = sleeTpUIIdentifier;
        
        if (logger.isDebugEnabled()) {
            logger.debug("UIGenericImpl activity created for userInteractionSessionID =["
                            + userInteractionSessionID + "]");
        }

    }

    public void init() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.csapi.jr.ui.activity.ui.AbstractUIImpl#getIpUI()
     */
    public IpUI getIpUI() {
        synchronized (this) {
            return ipUI;
        }
    }

    
    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.parlay.csapi.jr.ui.activity.ui.UIGeneric#getTpUIIdentifier()
     */
    public TpUIIdentifier getTpUIIdentifier() {
        return tpUIIdentifier;
    }

 
    public final void sendInfoRes(final int userInteractionSessionID,
            final  int assignmentID, final TpUIReport response) {

        if (getIpUI() != null) {
            final SendInfoResEvent event = new SendInfoResEvent(getUiManager()
                    .getTpServiceIdentifier(), null, tpUIIdentifier,
                    assignmentID, response);
            getEventListener().onSendInfoResEvent(event);
        }
    }

    public final void sendInfoErr(final int userInteractionSessionID,
            final  int assignmentID, final TpUIError error) {

        if (getIpUI() != null) {
            final SendInfoErrEvent event = new SendInfoErrEvent(getUiManager()
                    .getTpServiceIdentifier(), null, tpUIIdentifier,
                    assignmentID, error);
            getEventListener().onSendInfoErrEvent(event);
        }

    }

    public final void sendInfoAndCollectRes(final int userInteractionSessionID,
            final int assignmentID, final TpUIReport response, final String collectedInfo) {

        if (getIpUI() != null) {
            final SendInfoAndCollectResEvent event = new SendInfoAndCollectResEvent(
                    getUiManager().getTpServiceIdentifier(), null, tpUIIdentifier,
                    assignmentID, response, collectedInfo);
            getEventListener().onSendInfoAndCollectResEvent(event);
        }
    }

    public final void sendInfoAndCollectErr(final int userInteractionSessionID,
            final int assignmentID, final TpUIError error) {
        if (getIpUI() != null) {
            final SendInfoAndCollectErrEvent event = new SendInfoAndCollectErrEvent(
                    getUiManager().getTpServiceIdentifier(), null, tpUIIdentifier,
                    assignmentID, error);
            getEventListener().onSendInfoAndCollectErrEvent(event);
        }

    }

    public final void userInteractionFaultDetected(
            final int userInteractionSessionID, final TpUIFault fault) {
        if (getIpUI() != null) {
            final UserInteractionFaultDetectedEvent event = new UserInteractionFaultDetectedEvent(
                    getUiManager().getTpServiceIdentifier(), null, tpUIIdentifier,
                    fault);
            getEventListener().onUserInteractionFaultDetectedEvent(event);
            
            // Only the next line is different than the equivalent method in the sibling
            getActivityManager().remove(getActivityHandle(), tpUIIdentifier);        
            getActivityManager().activityEnding(getActivityHandle());
            dispose();            
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.csapi.jr.slee.ui.IpUIConnection#release()
     */
    public final void release() throws TpCommonExceptions, ResourceException {

        final IpUI ui = getIpUI();

        try {
            ui.release(getUserInteractionSessionID());
        } catch (P_INVALID_SESSION_ID e) {
            // If this happens ui must have finished on gateway and we
            // haven't processed the event yet.
            logger.warn(UI_NOT_VALID + ParlayExceptionUtil.stringify(e));
            throw new ResourceException(UI_NOT_VALID);
        }
        
        // Only the next line is different than the equivalent method in the sibling
        getActivityManager().remove(getActivityHandle(), tpUIIdentifier);        
        getActivityManager().activityEnding(getActivityHandle());
        dispose();
    }
}
