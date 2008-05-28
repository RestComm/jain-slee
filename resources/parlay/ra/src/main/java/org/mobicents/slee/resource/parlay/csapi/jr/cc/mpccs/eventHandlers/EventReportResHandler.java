package org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.eventHandlers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csapi.cc.TpCallEventInfo;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.callleg.CallLeg;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.multipartycall.MultiPartyCall;


/**
 *  
 */
public final class EventReportResHandler implements Runnable {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory
        .getLog(EventReportResHandler.class);

    public EventReportResHandler(MultiPartyCall call, int callLegSessionID,
            TpCallEventInfo callEventInfo) {
        super();
        this.call = call;
        this.callLegSessionID = callLegSessionID;
        this.callEventInfo = callEventInfo;
    }

    private final transient MultiPartyCall call;

    private final transient int callLegSessionID;

    private final transient TpCallEventInfo callEventInfo;

    public void run() {
        try {
            if(call != null ) {
	            final CallLeg callLeg = call.getCallLeg(callLegSessionID);
	            
	            if(callLeg != null) {
	                callLeg.eventReportRes(callLegSessionID, callEventInfo);
	            }
            }
        }
        catch (RuntimeException e) {
            // Catch all
            logger.error("EventReportResHandler failed", e);
        }
    }
}