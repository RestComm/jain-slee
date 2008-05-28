package org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.multipartycall;

import javax.slee.resource.ActivityHandle;

import org.csapi.cc.mpccs.IpAppCallLeg;
import org.csapi.cc.mpccs.IpAppMultiPartyCallOperations;
import org.mobicents.csapi.jr.slee.cc.mpccs.IpMultiPartyCallConnection;
import org.mobicents.csapi.jr.slee.cc.mpccs.TpMultiPartyCallIdentifier;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.callleg.CallLeg;

/**
 * Defines operation set for context object implementing an IpMultiPartyCall FSM.
 */
public interface MultiPartyCall extends
		IpMultiPartyCallConnection, IpAppMultiPartyCallOperations {
    
    // NOTE not using "Gang of Four" state pattern as transitions here are quite trivial
    
    /**
     * Return the SLEE identifier for this call.
     * 
     * @return
     */
    TpMultiPartyCallIdentifier getTpMultiPartyCallIdentifier();
    
    /**
     * Return the underlying gateway interface.
     * 
     * @return
     */
    org.csapi.cc.mpccs.TpMultiPartyCallIdentifier getParlayTpMultiPartyCallIdentifier();
    
 
    
    /**
     * Return the call leg callback.
     * 
     * @return
     */
    IpAppCallLeg getIpAppCallLeg();
    
    /**
     * Return the call leg identified by the Slee call leg identifier.
     * 
     * @param callLegIdentifier
     * @return
     */
    CallLeg getCallLeg(int callLegSessionID);
    
    /**
     * Stores a new call leg in this call.
     * 
     * @param callLegIdentifier
     * @param callLeg
     */
    void addCallLeg(int callLegSessionID, CallLeg callLeg);
    
    /**
     * Removes a new call leg in this call.
     * 
     * @param callLegIdentifier
     */
    CallLeg removeCallLeg(int callLegSessionID);
    
    /**
     * @return
     */
    ActivityHandle getActivityHandle();

    /**
     * Initialises the internal resources for this call.
     */
    void init();

    /**
     * Destroys or deallocates all resources used by this call.
     */
    void dispose();

     


}