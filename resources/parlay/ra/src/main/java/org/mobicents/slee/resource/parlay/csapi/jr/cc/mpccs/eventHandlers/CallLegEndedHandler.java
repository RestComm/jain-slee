package org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.eventHandlers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csapi.cc.TpReleaseCause;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.callleg.CallLeg;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.multipartycall.MultiPartyCall;


/**
 *  
 */
public final class CallLegEndedHandler implements Runnable {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory
        .getLog(CallLegEndedHandler.class);

    public CallLegEndedHandler(MultiPartyCall call,
            int callLegSessionID, TpReleaseCause releaseCause) {
        super();
        this.call = call;
        this.callLegSessionID = callLegSessionID;
        this.releaseCause = releaseCause;
    }

    private final transient MultiPartyCall call;

    private final transient int callLegSessionID;

    private final transient TpReleaseCause releaseCause;

    public void run() {
        try {
            if(call != null ) {
	            final CallLeg callLeg = call.getCallLeg(callLegSessionID);
	            
	            if(callLeg != null) {
	                callLeg.callLegEnded(callLegSessionID, releaseCause);
	            }
            }
        }
        catch (RuntimeException e) {
            // Catch all
            logger.error("CallLegEndedHandler failed", e);
        }
    }
}