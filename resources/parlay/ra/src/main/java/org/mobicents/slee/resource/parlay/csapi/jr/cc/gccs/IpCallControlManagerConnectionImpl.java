
package org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs;

import javax.slee.resource.ResourceException;

import org.csapi.P_INVALID_ADDRESS;
import org.csapi.P_INVALID_ASSIGNMENT_ID;
import org.csapi.P_INVALID_CRITERIA;
import org.csapi.P_INVALID_EVENT_TYPE;
import org.csapi.P_UNSUPPORTED_ADDRESS_PLAN;
import org.csapi.TpAddressRange;
import org.csapi.TpCommonExceptions;
import org.csapi.cc.TpCallLoadControlMechanism;
import org.csapi.cc.gccs.TpCallEventCriteria;
import org.csapi.cc.gccs.TpCallEventCriteriaResult;
import org.csapi.cc.gccs.TpCallTreatment;
import org.mobicents.csapi.jr.slee.cc.gccs.IpCallConnection;
import org.mobicents.csapi.jr.slee.cc.gccs.IpCallControlManagerConnection;
import org.mobicents.csapi.jr.slee.cc.gccs.TpCallIdentifier;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.activity.callcontrolmanager.CallControlManager;

/**
 *
 **/
public class IpCallControlManagerConnectionImpl implements IpCallControlManagerConnection {

    
    private final transient CallControlManager callControlManager;
    
    public IpCallControlManagerConnectionImpl(CallControlManager callControlManager) {
        super();
        if (callControlManager == null) {
            throw new IllegalArgumentException(
                    "callControlManager should never be null");
        }
        this.callControlManager = callControlManager;
    }
    
    /* (non-Javadoc)
     * @see org.mobicents.csapi.jr.slee.cc.gccs.IpCallControlManagerConnection#getIpCallConnection(org.mobicents.csapi.jr.slee.cc.gccs.TpCallIdentifier)
     */
    public IpCallConnection getIpCallConnection(final TpCallIdentifier callIdentifier) throws ResourceException {
        return callControlManager.getIpCallConnection(callIdentifier);
    }

    /* (non-Javadoc)
     * @see org.mobicents.csapi.jr.slee.cc.gccs.IpCallControlManagerConnection#createCall()
     */
    public TpCallIdentifier createCall() throws TpCommonExceptions, ResourceException {
        return callControlManager.createCall();
    }

    /* (non-Javadoc)
     * @see org.mobicents.csapi.jr.slee.cc.gccs.IpCallControlManagerConnection#enableCallNotification(org.csapi.cc.gccs.TpCallEventCriteria)
     */
    public int enableCallNotification(final TpCallEventCriteria eventCriteria) throws TpCommonExceptions, P_INVALID_CRITERIA, P_INVALID_EVENT_TYPE, ResourceException {
        return callControlManager.enableCallNotification(eventCriteria);
    }

    /* (non-Javadoc)
     * @see org.mobicents.csapi.jr.slee.cc.gccs.IpCallControlManagerConnection#disableCallNotification(int)
     */
    public void disableCallNotification(final int assignmentID) throws TpCommonExceptions, P_INVALID_ASSIGNMENT_ID, ResourceException {
        callControlManager.disableCallNotification(assignmentID);
        
    }

    /* (non-Javadoc)
     * @see org.mobicents.csapi.jr.slee.cc.gccs.IpCallControlManagerConnection#setCallLoadControl(int, org.csapi.cc.TpCallLoadControlMechanism, org.csapi.cc.gccs.TpCallTreatment, org.csapi.TpAddressRange)
     */
    public int setCallLoadControl(final int duration, final TpCallLoadControlMechanism mechanism, final TpCallTreatment treatment, final TpAddressRange addressRange) throws TpCommonExceptions, P_INVALID_ADDRESS, P_UNSUPPORTED_ADDRESS_PLAN, ResourceException {
        return callControlManager.setCallLoadControl(duration, mechanism, treatment, addressRange);
    }

    /* (non-Javadoc)
     * @see org.mobicents.csapi.jr.slee.cc.gccs.IpCallControlManagerConnection#changeCallNotification(int, org.csapi.cc.gccs.TpCallEventCriteria)
     */
    public void changeCallNotification(final int assignmentID, final TpCallEventCriteria eventCriteria) throws TpCommonExceptions, P_INVALID_ASSIGNMENT_ID, P_INVALID_CRITERIA, P_INVALID_EVENT_TYPE, ResourceException {
        callControlManager.changeCallNotification(assignmentID, eventCriteria);
        
    }

    /* (non-Javadoc)
     * @see org.mobicents.csapi.jr.slee.cc.gccs.IpCallControlManagerConnection#getCriteria()
     */
    public TpCallEventCriteriaResult[] getCriteria() throws TpCommonExceptions, ResourceException {
        return callControlManager.getCriteria();
    }

    /* (non-Javadoc)
     * @see org.mobicents.csapi.jr.slee.IpServiceConnection#closeConnection()
     */
    public void closeConnection() throws ResourceException {
        //do nothing
        //callControlManager.closeConnection(); 
    }

}
