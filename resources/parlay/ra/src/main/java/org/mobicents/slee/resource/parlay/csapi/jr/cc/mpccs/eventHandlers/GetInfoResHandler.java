package org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.eventHandlers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csapi.cc.TpCallInfoReport;
import org.csapi.cc.TpCallLegInfoReport;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.callleg.CallLeg;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.multipartycall.MultiPartyCall;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.multipartycallcontrolmanager.MultiPartyCallControlManager;

/**
 *  
 */
public final class GetInfoResHandler implements Runnable {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory
        .getLog(GetInfoResHandler.class);

    public GetInfoResHandler(MultiPartyCall call, int callLegSessionID,
            TpCallLegInfoReport callLegInfoReport) {
        super();
        this.call = call;
        this.callLegSessionID = callLegSessionID;
        this.callLegInfoReport = callLegInfoReport;
        
        multiPartyCallControlManager = null;
        callSessionID = 0;
        callInfoReport = null;
    }

    public GetInfoResHandler(MultiPartyCallControlManager multiPartyCallControlManager, int callSessionID,
            TpCallInfoReport callInfoReport) {
        super();
        this.multiPartyCallControlManager = multiPartyCallControlManager;
        this.callSessionID = callSessionID;
        this.callInfoReport = callInfoReport;
        
        call = null;
        callLegSessionID = 0;
        callLegInfoReport = null;
    }

    private final transient MultiPartyCall call;

    private final transient MultiPartyCallControlManager multiPartyCallControlManager;

    private final transient int callLegSessionID;

    private final transient int callSessionID;

    private final transient TpCallLegInfoReport callLegInfoReport;

    private final transient TpCallInfoReport callInfoReport;

    public void run() {
        try {
            
            if(multiPartyCallControlManager == null) {
                final CallLeg callLeg = call.getCallLeg(callLegSessionID);
                
                if(callLeg != null) {
                    callLeg.getInfoRes(callLegSessionID, callLegInfoReport);
                }

            }
            else  {
                final MultiPartyCall multiPartyCall = multiPartyCallControlManager.getMultiPartyCall(callSessionID);
                
                if(multiPartyCall != null) {
                    multiPartyCall.getInfoRes(callSessionID, callInfoReport);
                } 
            }

        }
        catch (RuntimeException e) {
            // Catch all
            logger.error("GetInfoResHandler failed", e);
        }
    }

}