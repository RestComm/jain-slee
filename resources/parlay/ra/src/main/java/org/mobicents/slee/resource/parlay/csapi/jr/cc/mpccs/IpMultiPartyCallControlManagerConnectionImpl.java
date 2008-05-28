package org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs;

import javax.slee.resource.ResourceException;

import org.csapi.P_INVALID_ADDRESS;
import org.csapi.P_INVALID_ASSIGNMENT_ID;
import org.csapi.P_INVALID_CRITERIA;
import org.csapi.P_INVALID_EVENT_TYPE;
import org.csapi.P_UNSUPPORTED_ADDRESS_PLAN;
import org.csapi.TpAddressRange;
import org.csapi.TpCommonExceptions;
import org.csapi.cc.TpCallLoadControlMechanism;
import org.csapi.cc.TpCallNotificationRequest;
import org.csapi.cc.TpNotificationRequested;
import org.csapi.cc.TpNotificationRequestedSetEntry;
import org.mobicents.csapi.jr.slee.cc.mpccs.IpMultiPartyCallConnection;
import org.mobicents.csapi.jr.slee.cc.mpccs.IpMultiPartyCallControlManagerConnection;
import org.mobicents.csapi.jr.slee.cc.mpccs.TpMultiPartyCallIdentifier;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.multipartycallcontrolmanager.MultiPartyCallControlManager;

/**
 */
public class IpMultiPartyCallControlManagerConnectionImpl implements
        IpMultiPartyCallControlManagerConnection {

    public IpMultiPartyCallControlManagerConnectionImpl(
            MultiPartyCallControlManager multiPartyCallControlManager) {
        super();
        if (multiPartyCallControlManager == null) {
            throw new IllegalArgumentException(
                    "multiPartyCallControlManager should never be null");
        }

        this.multiPartyCallControlManager = multiPartyCallControlManager;
    }

    private final transient MultiPartyCallControlManager multiPartyCallControlManager;

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.csapi.jr.slee.cc.mpccs.IpMultiPartyCallControlManagerConnection#getMultiPartyCallConnection(org.mobicents.csapi.jr.slee.cc.mpccs.TpMultiPartyCallIdentifier)
     */
    public IpMultiPartyCallConnection getIpMultiPartyCallConnection(
            final TpMultiPartyCallIdentifier multiPartyCallIdentifier)
            throws javax.slee.resource.ResourceException {

        return multiPartyCallControlManager
                .getIpMultiPartyCallConnection(multiPartyCallIdentifier);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.csapi.jr.slee.cc.mpccs.IpMultiPartyCallControlManagerConnection#createCall()
     */
    public TpMultiPartyCallIdentifier createCall() throws TpCommonExceptions,
            javax.slee.resource.ResourceException {

        return multiPartyCallControlManager.createCall();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.csapi.jr.slee.cc.mpccs.IpMultiPartyCallControlManagerConnection#createNotification(org.csapi.cc.TpCallNotificationRequest)
     */
    public int createNotification(final TpCallNotificationRequest notificationRequest)
            throws TpCommonExceptions, P_INVALID_CRITERIA,
            P_INVALID_EVENT_TYPE, javax.slee.resource.ResourceException {

        return multiPartyCallControlManager
                .createNotification(notificationRequest);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.csapi.jr.slee.cc.mpccs.IpMultiPartyCallControlManagerConnection#destroyNotification(int)
     */
    public void destroyNotification(final int assignmentID)
            throws TpCommonExceptions, P_INVALID_ASSIGNMENT_ID,
            javax.slee.resource.ResourceException {

        multiPartyCallControlManager.destroyNotification(assignmentID);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.csapi.jr.slee.cc.mpccs.IpMultiPartyCallControlManagerConnection#changeNotification(int,
     *      org.csapi.cc.TpCallNotificationRequest)
     */
    public void changeNotification(final int assignmentID,
            final TpCallNotificationRequest notificationRequest)
            throws TpCommonExceptions, P_INVALID_ASSIGNMENT_ID,
            P_INVALID_CRITERIA, P_INVALID_EVENT_TYPE,
            javax.slee.resource.ResourceException {

        multiPartyCallControlManager.changeNotification(assignmentID,
                notificationRequest);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.csapi.jr.slee.cc.mpccs.IpMultiPartyCallControlManagerConnection#getNotification()
     */
    public TpNotificationRequested[] getNotification()
            throws TpCommonExceptions, javax.slee.resource.ResourceException {

        return multiPartyCallControlManager.getNotification();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.csapi.jr.slee.cc.mpccs.IpMultiPartyCallControlManagerConnection#setCallLoadControl(int,
     *      org.csapi.cc.TpCallLoadControlMechanism,
     *      org.csapi.cc.gccs.TpCallTreatment, org.csapi.TpAddressRange)
     */
    public int setCallLoadControl(final int duration,
            final TpCallLoadControlMechanism mechanism,
            final org.csapi.cc.TpCallTreatment treatment,
            final TpAddressRange addressRange) throws TpCommonExceptions,
            P_INVALID_ADDRESS, P_UNSUPPORTED_ADDRESS_PLAN, ResourceException {

        return multiPartyCallControlManager.setCallLoadControl(duration,
                mechanism, treatment, addressRange);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.csapi.jr.slee.cc.mpccs.IpMultiPartyCallControlManagerConnection#enableNotifications()
     */
    public int enableNotifications() throws TpCommonExceptions,
            ResourceException {
        return multiPartyCallControlManager.enableNotifications();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.csapi.jr.slee.cc.mpccs.IpMultiPartyCallControlManagerConnection#disableNotifications()
     */
    public void disableNotifications() throws TpCommonExceptions,
            ResourceException {
        multiPartyCallControlManager.disableNotifications();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.csapi.jr.slee.cc.mpccs.IpMultiPartyCallControlManagerConnection#getNextNotification(boolean)
     */
    public TpNotificationRequestedSetEntry getNextNotification(final boolean reset)
            throws TpCommonExceptions, ResourceException {
        return multiPartyCallControlManager.getNextNotification(reset);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.csapi.jr.slee.IpServiceConnection#close()
     */
    public void closeConnection() throws ResourceException {
        // Do nothing
    }
}