package org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.eventHandlers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csapi.cc.TpCallError;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.callleg.CallLeg;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.multipartycall.MultiPartyCall;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.multipartycallcontrolmanager.MultiPartyCallControlManager;


/**
 *  
 */
public final class SuperviseErrHandler implements Runnable {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory
        .getLog(SuperviseErrHandler.class);

    public SuperviseErrHandler(MultiPartyCall call, int callLegSessionID,
            TpCallError errorIndication) {
        super();
        this.call = call;
        this.callLegSessionID = callLegSessionID;
        this.errorIndication = errorIndication;
        
        multiPartyCallControlManager = null;
        callSessionID = 0;
    }

    public SuperviseErrHandler(MultiPartyCallControlManager multiPartyCallControlManager, int callSessionID,
            TpCallError errorIndication) {
        super();
        this.multiPartyCallControlManager = multiPartyCallControlManager;
        this.callSessionID = callSessionID;
        this.errorIndication = errorIndication;
        
        call = null;
        callLegSessionID = 0;
    }

    private final transient MultiPartyCall call;

    private final transient MultiPartyCallControlManager multiPartyCallControlManager;

    private final transient int callLegSessionID;

    private final transient int callSessionID;

    private final transient TpCallError errorIndication;

    public void run() {
        try {
            if(multiPartyCallControlManager != null) {
	            final MultiPartyCall multiPartyCall = multiPartyCallControlManager.getMultiPartyCall(callSessionID);
	            
		        if(multiPartyCall != null) {
		            multiPartyCall.superviseErr(callSessionID, errorIndication);
		        }
            }
            else if(call != null ) {
	            final CallLeg callLeg = call.getCallLeg(callLegSessionID);
	            
	            if(callLeg != null) {
	                callLeg.superviseErr(callLegSessionID, errorIndication);
	            }
            }
        }
        catch (RuntimeException e) {
            // Catch all
            logger.error("SuperviseErrHandler failed", e);
        }
    }
}