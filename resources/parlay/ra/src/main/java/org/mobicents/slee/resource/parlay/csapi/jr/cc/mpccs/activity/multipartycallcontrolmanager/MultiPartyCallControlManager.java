package org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.multipartycallcontrolmanager;

import org.csapi.cc.TpCallNotificationInfo;
import org.csapi.cc.mpccs.IpAppMultiPartyCall;
import org.csapi.cc.mpccs.IpAppMultiPartyCallControlManager;
import org.csapi.cc.mpccs.IpMultiPartyCallControlManager;
import org.mobicents.csapi.jr.slee.cc.mpccs.IpMultiPartyCallControlManagerConnection;
import org.mobicents.csapi.jr.slee.cc.mpccs.TpCallLegIdentifier;
import org.mobicents.csapi.jr.slee.cc.mpccs.TpMultiPartyCallIdentifier;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.IpAppMultiPartyCallControlManagerImpl;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.IpAppMultiPartyCallImpl;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.callleg.CallLeg;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.multipartycall.MultiPartyCall;
import org.mobicents.slee.resource.parlay.session.ServiceSession;
import org.omg.PortableServer.POA;

/**
 * This interface does not simply extend the org.csapi.cc.mpccs.IpAppMultiPartyCallControlManagerOperations interface, because
 * methods which take CORBA TpXXXIdentifier in the IpAppMultiPartyCallControlManagerOperations (IDL generated) interface 
 * will in this interface have equivalents which take a SLEE TpXXXIdentifier.
 **/
public interface MultiPartyCallControlManager extends ServiceSession, IpMultiPartyCallControlManagerConnection {

    /**
     * @return Returns the ipAppMultiPartyCall.
     */
    IpAppMultiPartyCall getIpAppMultiPartyCall();

    /**
     * @return Returns the ipAppMultiPartyCallControlManager.
     */
    IpAppMultiPartyCallControlManager getIpAppMultiPartyCallControlManager();

    /**
     * @return Returns the ipAppMultiPartyCallControlManagerImpl.
     */
    IpAppMultiPartyCallControlManagerImpl getIpAppMultiPartyCallControlManagerImpl();

    /**
     * @return Returns the ipAppMultiPartyCallImpl.
     */
    IpAppMultiPartyCallImpl getIpAppMultiPartyCallImpl();

    /**
     * @return Returns the ipMultiPartyCallControlManager.
     */
    IpMultiPartyCallControlManager getIpMultiPartyCallControlManager();

    /**
     * @return Returns the POA used to create ipAppCallLegs
     */
    POA getIpAppCallLegPOA();

    /**
     * @param tpMultiPartyCallIdentifier
     * @return
     */
    MultiPartyCall getMultiPartyCall(int callSessionID);

    /**
     * @param tpMultiPartyCallIdentifier
     * @return
     */
    MultiPartyCall removeMultiPartyCall(int callSessionID);

    /**
     * @param tpMultiPartyCallIdentifier
     * @param multiPartyCall
     */
    void addMultiPartyCall(int callSessionID, MultiPartyCall multiPartyCall);
    
    /**
     * @param callIdentifier
     * @return
     */
    MultiPartyCall createCall(org.csapi.cc.mpccs.TpMultiPartyCallIdentifier callIdentifier);
    
    /**
     * @param call
     * @param identifier
     * @return
     */
    CallLeg createCallLeg(MultiPartyCall call,
            org.csapi.cc.mpccs.TpCallLegIdentifier identifier);

    /**
     * @param callID
     */
    void callAborted(int callID);

    /**
     * @param assignmentID
     */
    void callOverloadCeased(int assignmentID);

    /**
     * @param assignmentID
     */
    void callOverloadEncountered(int assignmentID);

    /**
     *  
     */
    void managerInterrupted();

    /**
     *  
     */
    void managerResumed();

    /**
     * @param callIdentifier
     * @param callLegIdentifier
     * @param callNotificationInfo
     * @param assignmentID
     */
    void reportNotification(TpMultiPartyCallIdentifier callIdentifier,
            TpCallLegIdentifier[] callLegIdentifier,
            TpCallNotificationInfo callNotificationInfo, int assignmentID);
}