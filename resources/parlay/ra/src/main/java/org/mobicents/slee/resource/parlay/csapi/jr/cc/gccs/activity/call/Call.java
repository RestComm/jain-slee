
package org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.activity.call;


import javax.slee.resource.ActivityHandle;

import org.csapi.cc.gccs.IpAppCallOperations;
import org.mobicents.csapi.jr.slee.cc.gccs.IpCallConnection;
import org.mobicents.csapi.jr.slee.cc.gccs.TpCallIdentifier;

/**
 *
 **/
public interface Call extends IpCallConnection, IpAppCallOperations {
    
    /**
     * Initialises the internal resources for this call.
     */
    void init();
    
    /**
     * Destroys or deallocates all resources used by this call.
     */
    void dispose();
    
    /**
     * @return
     */
    org.csapi.cc.gccs.TpCallIdentifier getParlayTpCallIdentifier();
   
    /**
     * @return
     */
    TpCallIdentifier getTpCallIdentifier();

    /**
     * @return
     */
    ActivityHandle getActivityHandle();
}
