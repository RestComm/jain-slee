package org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.eventHandlers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.callleg.CallLeg;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.multipartycall.MultiPartyCall;


/**
 * 
 */
public final class DetachMediaResHandler implements Runnable {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory
        .getLog(DetachMediaResHandler.class);

    public DetachMediaResHandler(MultiPartyCall call, int callLegSessionID) {
        super();
        this.call = call;
        this.callLegSessionID = callLegSessionID;
    }

    private final transient MultiPartyCall call;
    
    private final transient int callLegSessionID;

    public void run() {
        try {
            if(call != null ) {
	            final CallLeg callLeg = call.getCallLeg(callLegSessionID);
	            
	            if(callLeg != null) {
	                callLeg.detachMediaRes(callLegSessionID);
	            }
            }
        }
        catch (RuntimeException e) {
            // Catch all
            logger.error("DetachMediaResHandler failed", e);
        }
    }
}