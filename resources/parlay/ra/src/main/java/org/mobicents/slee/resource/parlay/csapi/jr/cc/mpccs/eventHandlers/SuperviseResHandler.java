package org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.eventHandlers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.callleg.CallLeg;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.multipartycall.MultiPartyCall;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.multipartycallcontrolmanager.MultiPartyCallControlManager;


/**
 *  
 */
public final class SuperviseResHandler implements Runnable {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory
        .getLog(SuperviseResHandler.class);

    public SuperviseResHandler(MultiPartyCall call, int callLegSessionID,
            int report, int duration) {
        super();
        this.multiPartyCallControlManager = null;
        this.call = call;
        this.callSessionID = 0;
        this.callLegSessionID = callLegSessionID;
        this.report = report;
        this.duration = duration;
    }

    public SuperviseResHandler(MultiPartyCallControlManager multiPartyCallControlManager, int callSessionID,
            int report, int duration) {
        super();
        this.multiPartyCallControlManager = multiPartyCallControlManager;
        this.call = null;
        this.callSessionID = callSessionID;
        this.callLegSessionID = 0;
        this.report = report;
        this.duration = duration;
    }

    private final transient MultiPartyCall call;

    private final transient MultiPartyCallControlManager multiPartyCallControlManager;

    private final transient int callLegSessionID;

    private final transient int callSessionID;

    private final transient int report;

    private final transient int duration;

    public void run() {
        try {
            if(multiPartyCallControlManager != null) {
	            MultiPartyCall multiPartyCall = multiPartyCallControlManager.getMultiPartyCall(callSessionID);
	            
		        if(multiPartyCall != null) {
		            multiPartyCall.superviseRes(callSessionID, report, duration);
		        }
            }
            else if(call != null ) {
	            CallLeg callLeg = call.getCallLeg(callLegSessionID);
	            
	            if(callLeg != null) {
	                callLeg.superviseRes(callLegSessionID, report, duration);
	            }
            }
        }
        catch (RuntimeException e) {
            // Catch all
            logger.error("SuperviseErrHandler failed", e);
        }
    }
}