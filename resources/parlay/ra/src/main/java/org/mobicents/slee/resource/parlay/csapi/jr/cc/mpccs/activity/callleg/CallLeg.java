package org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.callleg;


import javax.slee.resource.ActivityHandle;

import org.csapi.cc.mpccs.IpAppCallLegOperations;
import org.mobicents.csapi.jr.slee.cc.mpccs.IpCallLegConnection;
import org.mobicents.csapi.jr.slee.cc.mpccs.TpCallLegIdentifier;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.multipartycallcontrolmanager.MultiPartyCallControlManager;

/**
 *
 * Defines operation set for context object implementing an IpCallLeg FSM.
 */
public interface CallLeg extends IpCallLegConnection, IpAppCallLegOperations {
    
    // NOTE not using "Gang of Four" state pattern as transitions here are quite trivial
    
    /**
     * Return the SLEE identifier for this call.
     * 
     * @return
     */
    TpCallLegIdentifier getTpCallLegIdentifier();
    
    /**
     * Return the Parlay identifier for this call.
     * 
     * @return
     */    
    org.csapi.cc.mpccs.TpCallLegIdentifier getParlayTpCallLegIdentifier();
    
 
    /**
     * Return the Parlay Session.
     * 
     * @return
     */
    MultiPartyCallControlManager getMpccsSession();

    /**
     * Initialises the internal resources for this call leg.
     */
    void init();

    /**
     * Destroys or deallocates all resources used by this call leg.
     */
    void dispose();
    
    /**
     * @return Returns the activityHandle.
     */
    ActivityHandle getActivityHandle();
   
}
