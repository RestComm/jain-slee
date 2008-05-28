package org.mobicents.slee.resource.parlay.csapi.jr.ui.activity.uicall;

import javax.slee.resource.ResourceException;

import org.apache.log4j.Logger;
import org.csapi.P_INVALID_ASSIGNMENT_ID;
import org.csapi.P_INVALID_CRITERIA;
import org.csapi.P_INVALID_NETWORK_STATE;
import org.csapi.P_INVALID_SESSION_ID;
import org.csapi.TpCommonExceptions;
import org.csapi.ui.IpUI;
import org.csapi.ui.IpUICall;
import org.csapi.ui.P_ID_NOT_FOUND;
import org.csapi.ui.P_ILLEGAL_ID;
import org.csapi.ui.TpUIError;
import org.csapi.ui.TpUIFault;
import org.csapi.ui.TpUIInfo;
import org.csapi.ui.TpUIMessageCriteria;
import org.csapi.ui.TpUIReport;
import org.mobicents.csapi.jr.slee.ui.AbortActionErrEvent;
import org.mobicents.csapi.jr.slee.ui.AbortActionResEvent;
import org.mobicents.csapi.jr.slee.ui.DeleteMessageErrEvent;
import org.mobicents.csapi.jr.slee.ui.DeleteMessageResEvent;
import org.mobicents.csapi.jr.slee.ui.RecordMessageErrEvent;
import org.mobicents.csapi.jr.slee.ui.RecordMessageResEvent;
import org.mobicents.csapi.jr.slee.ui.SendInfoAndCollectErrEvent;
import org.mobicents.csapi.jr.slee.ui.SendInfoAndCollectResEvent;
import org.mobicents.csapi.jr.slee.ui.SendInfoErrEvent;
import org.mobicents.csapi.jr.slee.ui.SendInfoResEvent;
import org.mobicents.csapi.jr.slee.ui.TpUICallIdentifier;
import org.mobicents.csapi.jr.slee.ui.UserInteractionFaultDetectedEvent;
import org.mobicents.slee.resource.parlay.csapi.jr.ui.TpUICallActivityHandle;
import org.mobicents.slee.resource.parlay.csapi.jr.ui.UiListener;
import org.mobicents.slee.resource.parlay.csapi.jr.ui.activity.ui.AbstractUIImpl;
import org.mobicents.slee.resource.parlay.csapi.jr.ui.activity.uimanager.UIManager;
import org.mobicents.slee.resource.parlay.util.ParlayExceptionUtil;
import org.mobicents.slee.resource.parlay.util.activity.ActivityManager;

public class UICallImpl extends AbstractUIImpl implements UICall {
    /**
     * Logger for this class.
     */
    private static final Logger logger = Logger.getLogger(UICallImpl.class);

    private static final  String UICALL_NOT_VALID = "UICall session is no longer valid.";

    private final transient IpUICall ipUICall;

    private final transient TpUICallIdentifier tpUICallIdentifier;

    public UICallImpl(final UIManager manager, final IpUICall ipUICall,
            final int userInteractionSessionID,
           final TpUICallIdentifier sleeTpUICallIdentifier,
            final ActivityManager activityManager, final UiListener eventListener) {
        super(manager, userInteractionSessionID, activityManager, eventListener, new TpUICallActivityHandle(sleeTpUICallIdentifier));
        this.ipUICall = ipUICall;
        this.tpUICallIdentifier = sleeTpUICallIdentifier;
        if (logger.isDebugEnabled()) {
            logger.debug("UICallImpl activity created for userInteractionSessionID =["
                            + userInteractionSessionID + "]");
        }
    }

    public IpUICall getIpUICall() {
        synchronized (this) {
            return ipUICall;
        }
    }

    public TpUICallIdentifier getTpUICallIdentifier() {
        return tpUICallIdentifier;
    }

    public void init() {

    }

    // IpUICallOperations ...
    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.csapi.jr.slee.ui.IpUICallConnection#recordMessageReq(org.csapi.ui.TpUIInfo,
     *      org.csapi.ui.TpUIMessageCriteria)
     */
    public final int recordMessageReq(final TpUIInfo info, final TpUIMessageCriteria criteria)
            throws TpCommonExceptions, P_INVALID_NETWORK_STATE, P_ILLEGAL_ID,
            P_ID_NOT_FOUND, P_INVALID_CRITERIA, ResourceException {

         int assignmentID = 0;

        try {
            assignmentID = getIpUICall().recordMessageReq(
                    getUserInteractionSessionID(), info, criteria);
        } catch (P_INVALID_SESSION_ID e) {
            // If this happens ui must have finished on gateway and we
            // haven't processed
            // the event yet.
            logger.warn(UICALL_NOT_VALID + ParlayExceptionUtil.stringify(e));
            throw new ResourceException(UICALL_NOT_VALID);
        }
        return assignmentID;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.csapi.jr.slee.ui.IpUICallConnection#deleteMessageReq(int)
     */
    public final int deleteMessageReq(final int messageID) throws TpCommonExceptions,
            P_ILLEGAL_ID, P_ID_NOT_FOUND, ResourceException {
        int assignmentID = 0;

        try {
            assignmentID = getIpUICall().deleteMessageReq(
                    getUserInteractionSessionID(), messageID);
        } catch (P_INVALID_SESSION_ID e) {
            // If this happens ui must have finished on gateway and we
            // haven't processed
            // the event yet.
            logger.warn(UICALL_NOT_VALID + ParlayExceptionUtil.stringify(e));
            throw new ResourceException(UICALL_NOT_VALID);
        }
        return assignmentID;
    }

    public void abortActionReq(final int assignmentID) throws TpCommonExceptions,
            P_INVALID_ASSIGNMENT_ID, ResourceException {

        try {
            getIpUICall()
                    .abortActionReq(getUserInteractionSessionID(), assignmentID);
        } catch (P_INVALID_SESSION_ID e) {
            // If this happens ui must have finished on gateway and we
            // haven't processed
            // the event yet.
            logger.warn(UICALL_NOT_VALID + ParlayExceptionUtil.stringify(e));
            throw new ResourceException(UICALL_NOT_VALID);
        }

    }

    public void recordMessageRes(final int userInteractionSessionID,
            final int assignmentID, final TpUIReport response, final int messageID) {
        if (getIpUICall() != null) {
            final RecordMessageResEvent event = new RecordMessageResEvent(getUiManager()
                    .getTpServiceIdentifier(), tpUICallIdentifier,
                    assignmentID, response, messageID);
            getEventListener().onRecordMessageResEvent(event);
        }

    }

    public void recordMessageErr(final int userInteractionSessionID,
            final int assignmentID, final TpUIError error) {
        if (getIpUICall() != null) {
            final RecordMessageErrEvent event = new RecordMessageErrEvent(getUiManager()
                    .getTpServiceIdentifier(), tpUICallIdentifier,
                    assignmentID, error);
            getEventListener().onRecordMessageErrEvent(event);
        }

    }

    public void deleteMessageRes(final int usrInteractionSessionID,
           final TpUIReport response, final int assignmentID) {
        if (getIpUICall() != null) {
            final DeleteMessageResEvent event = new DeleteMessageResEvent(getUiManager()
                    .getTpServiceIdentifier(), tpUICallIdentifier, response,
                    assignmentID);
            getEventListener().onDeleteMessageResEvent(event);
        }

    }

    public void deleteMessageErr(final int usrInteractionSessionID, final TpUIError error,
            final int assignmentID) {
        if (getIpUICall() != null) {
            final DeleteMessageErrEvent event = new DeleteMessageErrEvent(getUiManager()
                    .getTpServiceIdentifier(), tpUICallIdentifier, error,
                    assignmentID);
            getEventListener().onDeleteMessageErrEvent(event);
        }

    }

    public void abortActionRes(final int userInteractionSessionID, final int assignmentID) {
        if (getIpUICall() != null) {
            final AbortActionResEvent event = new AbortActionResEvent(getUiManager()
                    .getTpServiceIdentifier(), tpUICallIdentifier, assignmentID);
            getEventListener().onAbortActionResEvent(event);
        }

    }

    public void abortActionErr(final int userInteractionSessionID, final int assignmentID,
            final TpUIError error) {
        if (getIpUICall() != null) {
            final AbortActionErrEvent event = new AbortActionErrEvent(getUiManager()
                    .getTpServiceIdentifier(), tpUICallIdentifier,
                    assignmentID, error);
            getEventListener().onAbortActionErrEvent(event);
        }

    }

    
    /*
     * (non-Javadoc)
     * 
     * @see org.csapi.ui.IpAppUIOperations#sendInfoRes(int, int,
     *      org.csapi.ui.TpUIReport)
     */
    public final void sendInfoRes(final int userInteractionSessionID,
            final int assignmentID, final TpUIReport response) {

        if (getIpUI() != null) {
            final SendInfoResEvent event = new SendInfoResEvent(getUiManager()
                    .getTpServiceIdentifier(), tpUICallIdentifier, null,
                    assignmentID, response);
            getEventListener().onSendInfoResEvent(event);
        }
    }

    public final void sendInfoErr(final int userInteractionSessionID,
            final int assignmentID, final TpUIError error) {

        if (getIpUI() != null) {
            final SendInfoErrEvent event = new SendInfoErrEvent(getUiManager()
                    .getTpServiceIdentifier(), tpUICallIdentifier, null,
                    assignmentID, error);
            getEventListener().onSendInfoErrEvent(event);
        }

    }

    public final void sendInfoAndCollectRes(final int userInteractionSessionID,
            final int assignmentID, final TpUIReport response, final String collectedInfo) {

        if (getIpUI() != null) {
            final SendInfoAndCollectResEvent event = new SendInfoAndCollectResEvent(
                    getUiManager().getTpServiceIdentifier(), tpUICallIdentifier, null,
                    assignmentID, response, collectedInfo);
            getEventListener().onSendInfoAndCollectResEvent(event);
        }
    }

    public final void sendInfoAndCollectErr(final int userInteractionSessionID,
            final int assignmentID, final TpUIError error) {
        if (getIpUI() != null) {
            final SendInfoAndCollectErrEvent event = new SendInfoAndCollectErrEvent(
                    getUiManager().getTpServiceIdentifier(), tpUICallIdentifier, null,
                    assignmentID, error);
            getEventListener().onSendInfoAndCollectErrEvent(event);
        }

    }

    public final void userInteractionFaultDetected(
            final int userInteractionSessionID, final TpUIFault fault) {
        if (getIpUI() != null) {
            final UserInteractionFaultDetectedEvent event = new UserInteractionFaultDetectedEvent(
                    getUiManager().getTpServiceIdentifier(), tpUICallIdentifier, null,
                    fault);
            getEventListener().onUserInteractionFaultDetectedEvent(event);
            
            // Only the next line is different than the equivalent method in the sibling
            getActivityManager().remove(getActivityHandle(), tpUICallIdentifier);        
            getActivityManager().activityEnding(getActivityHandle());
            dispose();

        }

    }
    
    public IpUI getIpUI() {
        return getIpUICall();
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
            // haven't processed
            // the event yet.
            logger.warn(UICALL_NOT_VALID + ParlayExceptionUtil.stringify(e));
            throw new ResourceException(UICALL_NOT_VALID);
        }

        // Only this line is different than the equivalent method in the sibling
        getActivityManager().remove(getActivityHandle(), tpUICallIdentifier);

        getActivityManager().activityEnding(getActivityHandle());

        dispose();

    }
}
