package org.mobicents.slee.resource.parlay.csapi.jr.ui;

import javax.slee.resource.ResourceException;

import org.csapi.P_INVALID_ASSIGNMENT_ID;
import org.csapi.P_INVALID_CRITERIA;
import org.csapi.P_INVALID_NETWORK_STATE;
import org.csapi.TpAddress;
import org.csapi.TpCommonExceptions;
import org.csapi.ui.TpUIEventCriteria;
import org.csapi.ui.TpUIEventCriteriaResult;
import org.mobicents.csapi.jr.slee.ui.IpUICallConnection;
import org.mobicents.csapi.jr.slee.ui.IpUIConnection;
import org.mobicents.csapi.jr.slee.ui.IpUIManagerConnection;
import org.mobicents.csapi.jr.slee.ui.TpUICallIdentifier;
import org.mobicents.csapi.jr.slee.ui.TpUIIdentifier;
import org.mobicents.csapi.jr.slee.ui.TpUITargetObject;
import org.mobicents.slee.resource.parlay.csapi.jr.ui.activity.uimanager.UIManager;

/**
 * 
 */
public class IpUIManagerConnectionImpl implements IpUIManagerConnection {

    private final transient UIManager uiManager;

    public IpUIManagerConnectionImpl(UIManager uiManager) {
        super();
        if (uiManager == null) {
            throw new IllegalArgumentException("uiManager should never be null");
        }
        this.uiManager = uiManager;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.csapi.jr.slee.ui.IpUIManagerConnection#getIpUIConnection(org.mobicents.csapi.jr.slee.ui.TpUIIdentifier)
     */
    public IpUIConnection getIpUIConnection(final TpUIIdentifier uiIdentifier)
            throws ResourceException {
        return uiManager.getIpUIConnection(uiIdentifier);
    }

    public IpUICallConnection getIpUICallConnection(
            final TpUICallIdentifier uICallIdentifier) throws ResourceException {
        return uiManager.getIpUICallConnection(uICallIdentifier);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.csapi.jr.slee.IpServiceConnection#closeConnection()
     */
    public void closeConnection() throws ResourceException {
        // do nothing TODO KMP check this
        // uiManager.closeConnection();
    }

    public TpUIIdentifier createUI(final TpAddress userAddress)
            throws TpCommonExceptions, P_INVALID_NETWORK_STATE,
            ResourceException {
        return uiManager.createUI(userAddress);
    }

    // TODO KMP check TpUITargetObject was generated okay
    public TpUICallIdentifier createUICall(final TpUITargetObject uiTargetObject)
            throws TpCommonExceptions, P_INVALID_NETWORK_STATE,
            ResourceException {
        return uiManager.createUICall(uiTargetObject);
    }

    public int createNotification(final TpUIEventCriteria eventCriteria)
            throws TpCommonExceptions, P_INVALID_CRITERIA, ResourceException {
        return uiManager.createNotification(eventCriteria);
    }

    public void destroyNotification(final int assignmentID)
            throws TpCommonExceptions, P_INVALID_ASSIGNMENT_ID,
            ResourceException {
        uiManager.destroyNotification(assignmentID);

    }

    public void changeNotification(final int assignmentID,
            TpUIEventCriteria eventCriteria) throws TpCommonExceptions,
            P_INVALID_ASSIGNMENT_ID, P_INVALID_CRITERIA, ResourceException {
        uiManager.changeNotification(assignmentID, eventCriteria);

    }

    public TpUIEventCriteriaResult[] getNotification()
            throws TpCommonExceptions, ResourceException {
        return uiManager.getNotification();
    }

    public int enableNotifications() throws TpCommonExceptions,
            ResourceException {
        return uiManager.enableNotifications();
    }

    public void disableNotifications() throws TpCommonExceptions,
            ResourceException {
        uiManager.disableNotifications();

    }

}
