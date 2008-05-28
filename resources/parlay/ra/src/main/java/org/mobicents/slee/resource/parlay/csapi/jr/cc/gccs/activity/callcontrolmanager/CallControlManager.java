
package org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.activity.callcontrolmanager;

import org.csapi.cc.gccs.IpAppCall;
import org.csapi.cc.gccs.TpCallEventInfo;
import org.mobicents.csapi.jr.slee.TpServiceIdentifier;
import org.mobicents.csapi.jr.slee.cc.gccs.IpCallControlManagerConnection;
import org.mobicents.csapi.jr.slee.cc.gccs.TpCallIdentifier;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.activity.call.Call;
import org.mobicents.slee.resource.parlay.session.ServiceSession;

/**
 * This interface does not simply extend the org.csapi.cc.gccs.IpAppCallControlManagerOperations interface, because
 * methods which take CORBA TpXXXIdentifier in the IpAppCallControlManagerOperations (IDL generated) interface 
 * will in this interface have equivalents which take a SLEE TpXXXIdentifier.
 **/
public interface CallControlManager extends ServiceSession, IpCallControlManagerConnection {
    
    
    
    /**
     * @param callSessionID
     * @return
     */
    public Call getCall(int callSessionID);
    
    /**
     * @param callSessionID
     * @return
     */
    public Call removeCall(int callSessionID);
    
    /**
     * @param callSessionID
     * @param call
     */
    public void addCall(int callSessionID, Call call);
    
    /**
     * Return the underlying gateway interface.
     * 
     * @return
     */
    IpAppCall getIpAppCall();
    
    /**
     * @param callReference
     */
    void callAborted(int callReference);
    

    /**
     * @param callReference
     * @param eventInfo
     * @param assignmentID
     */
    void callEventNotify(TpCallIdentifier callReference, TpCallEventInfo eventInfo, int assignmentID);

    /**
     * 
     */
    void callNotificationInterrupted();
    
    /**
     * 
     */
    void callNotificationContinued();
    
    /**
     * @param assignmentID
     */
    void callOverloadEncountered(int assignmentID);
    
    /**
     * @param assignmentID
     */
    void callOverloadCeased(int assignmentID);

    /**
     * @return
     */
    TpServiceIdentifier getTpServiceIdentifier();

    /**
     * @param callReference
     * @return
     */
    public Call createCall(org.csapi.cc.gccs.TpCallIdentifier callReference);
}
