package org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.eventHandlers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csapi.cc.TpCallError;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.callleg.CallLeg;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.multipartycall.MultiPartyCall;


/**
 * 
 */
public final class EventReportErrHandler implements Runnable {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory
        .getLog(EventReportErrHandler.class);

    public EventReportErrHandler(MultiPartyCall call, int callLegSessionID,
            TpCallError errorIndication) {
        super();
        this.call = call;
        this.callLegSessionID = callLegSessionID;
        this.errorIndication = errorIndication;
    }

    private final transient MultiPartyCall call;

    private final transient int callLegSessionID;

    private final transient TpCallError errorIndication;

    public void run() {
        try {
            if(call != null ) {
	            CallLeg callLeg = call.getCallLeg(callLegSessionID);
	            
	            if(callLeg == null) {
                    logger.debug("leg destroyed");	                
	            } else {
                    callLeg.eventReportErr(callLegSessionID, errorIndication);
                }
            } else {
                logger.debug("Call destroyed");
            }
        }
        catch (RuntimeException e) {
            // Catch all
            logger.error("EventReportErrHandler failed", e);
        }
    }
}