package org.mobicents.slee.resource.parlay.csapi.jr.ui.activity.ui;

import javax.slee.resource.ActivityHandle;
import javax.slee.resource.ResourceException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csapi.P_INVALID_ADDRESS;
import org.csapi.P_INVALID_NETWORK_STATE;
import org.csapi.P_INVALID_SESSION_ID;
import org.csapi.TpCommonExceptions;
import org.csapi.ui.IpUI;
import org.csapi.ui.P_ID_NOT_FOUND;
import org.csapi.ui.P_ILLEGAL_ID;
import org.csapi.ui.P_ILLEGAL_RANGE;
import org.csapi.ui.P_INVALID_COLLECTION_CRITERIA;
import org.csapi.ui.TpUICollectCriteria;
import org.csapi.ui.TpUIInfo;
import org.csapi.ui.TpUIVariableInfo;
import org.mobicents.slee.resource.parlay.csapi.jr.ui.UiListener;
import org.mobicents.slee.resource.parlay.csapi.jr.ui.activity.uimanager.UIManager;
import org.mobicents.slee.resource.parlay.util.ParlayExceptionUtil;
import org.mobicents.slee.resource.parlay.util.activity.ActivityManager;

public abstract class AbstractUIImpl implements AbstractUI {

    public static final String UI_NOT_VALID = "UI session is no longer valid.";

    private  int userInteractionSessionID;


    private  ActivityManager activityManager;

    private  UiListener eventListener;

    private  ActivityHandle activityHandle;  

    private  UIManager uiManager;

    /* 
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.csapi.jr.ui.activity.ui.AbstractUI#getIpUI()
     */
    public abstract IpUI getIpUI();

    private static final Log logger = LogFactory.getLog(AbstractUIImpl.class);

    public AbstractUIImpl(final UIManager uiManager, final int userInteractionSessionID,
            final ActivityManager activityManager, final UiListener eventListener, final ActivityHandle activityHandle) {
        super(); 
        setUiManager(uiManager);
        setUserInteractionSessionID(userInteractionSessionID);
        setActivityManager(activityManager);
        setEventListener(eventListener);
        setActivityHandle(activityHandle);
    }

    private void setUserInteractionSessionID(final int userInteractionSessionID) {
        this.userInteractionSessionID = userInteractionSessionID;        
    }

    public void dispose() {

        synchronized (this) {
            if (getUiManager() != null) {
                getUiManager().removeUI(getUserInteractionSessionID());
                setUiManager(null);
            }

            setEventListener(null);
        }
    }

 
    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.parlay.csapi.jr.ui.activity.ui.AbstractUI#getActivityHandle()
     */
    public final ActivityHandle getActivityHandle() {
        return activityHandle;
    }
    
    protected void setActivityHandle(final ActivityHandle activityHandle) {
        this.activityHandle = activityHandle;        
    }
    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.csapi.jr.slee.IpServiceConnection#closeConnection()
     */
    public void closeConnection() throws ResourceException {
        // ignore. should never be called
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.csapi.jr.slee.ui.IpUIConnection#sendInfoReq(org.csapi.ui.TpUIInfo,
     *      java.lang.String, org.csapi.ui.TpUIVariableInfo[], int, int)
     */
    public final int sendInfoReq(final TpUIInfo info, final String language,
            final TpUIVariableInfo[] variableInfo, final int repeatIndicator,
            final int responseRequested) throws TpCommonExceptions,
            P_INVALID_NETWORK_STATE, P_ILLEGAL_ID, P_ID_NOT_FOUND,
            ResourceException {
        final IpUI ui = getIpUI();

        int assignmentID = 0;

        try {
            assignmentID = ui.sendInfoReq(getUserInteractionSessionID(), info,
                    language, variableInfo, repeatIndicator, responseRequested);
        } catch (P_INVALID_SESSION_ID e) {
            // If this happens ui must have finished on gateway and we
            // haven't processed
            // the event yet.
            logger.warn(UI_NOT_VALID + ParlayExceptionUtil.stringify(e));
            throw new ResourceException(UI_NOT_VALID);
        }

        return assignmentID;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.csapi.jr.slee.ui.IpUIConnection#sendInfoAndCollectReq(org.csapi.ui.TpUIInfo,
     *      java.lang.String, org.csapi.ui.TpUIVariableInfo[],
     *      org.csapi.ui.TpUICollectCriteria, int)
     */
    public final int sendInfoAndCollectReq(final TpUIInfo info, final String language,
            final TpUIVariableInfo[] variableInfo, final TpUICollectCriteria criteria,
            final int responseRequested) throws TpCommonExceptions,
            P_INVALID_NETWORK_STATE, P_ILLEGAL_ID, P_ID_NOT_FOUND,
            P_ILLEGAL_RANGE, P_INVALID_COLLECTION_CRITERIA, ResourceException {
        final IpUI ui = getIpUI();

        int assignmentID = 0;

        try {
            assignmentID = ui.sendInfoAndCollectReq(getUserInteractionSessionID(),
                    info, language, variableInfo, criteria, responseRequested);
        } catch (P_INVALID_SESSION_ID e) {
            // If this happens ui must have finished on gateway and we
            // haven't processed
            // the event yet.
            logger.warn(UI_NOT_VALID + ParlayExceptionUtil.stringify(e));
            throw new ResourceException(UI_NOT_VALID);
        }
        return assignmentID;
    }

    

    /**
     * New in Parlay 4.
     * 
     * @see org.mobicents.csapi.jr.slee.ui.IpUIConnection#setOriginatingAddress(java.lang.String)
     */
    public final void setOriginatingAddress(final String origin)
            throws TpCommonExceptions, P_INVALID_NETWORK_STATE,
            P_INVALID_ADDRESS, ResourceException {
        final IpUI ui = getIpUI();
        try {
            ui.setOriginatingAddress(getUserInteractionSessionID(), origin);
        } catch (P_INVALID_SESSION_ID e) {
            // If this happens ui must have finished on gateway and we
            // haven't processed
            // the event yet.
            logger.warn(UI_NOT_VALID + ParlayExceptionUtil.stringify(e));
            throw new ResourceException(UI_NOT_VALID);
        }
    }

    /**
     * New in Parlay 4.
     * 
     * @see org.mobicents.csapi.jr.slee.ui.IpUIConnection#getOriginatingAddress()
     */
    public final String getOriginatingAddress() throws TpCommonExceptions,
            P_INVALID_NETWORK_STATE, ResourceException {
        String result;
        final IpUI ui = getIpUI();
        try {
            result = ui.getOriginatingAddress(getUserInteractionSessionID());
        } catch (P_INVALID_SESSION_ID e) {
            // If this happens ui must have finished on gateway and we
            // haven't processed
            // the event yet.
            logger.warn(UI_NOT_VALID + ParlayExceptionUtil.stringify(e));
            throw new ResourceException(UI_NOT_VALID);
        }
        return result;
    }

    protected void setUiManager(final UIManager uiManager) {
        this.uiManager = uiManager;
    }

    protected UIManager getUiManager() {
        return uiManager;
    }

    protected void setEventListener(final UiListener eventListener) {
        this.eventListener = eventListener;
    }
    // Public so can be acceses by Unit Test of UICallImpl which is in a different package
    public UiListener getEventListener() {
        return eventListener;
    }

    protected void setActivityManager(final ActivityManager activityManager) {
        this.activityManager = activityManager;
    }

    
    protected ActivityManager getActivityManager() {
        return activityManager;
    }

    protected int getUserInteractionSessionID() {
        return userInteractionSessionID;
    }


}
