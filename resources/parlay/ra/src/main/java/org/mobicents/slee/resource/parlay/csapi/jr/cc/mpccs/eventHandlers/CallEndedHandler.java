package org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.eventHandlers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csapi.cc.TpCallEndedReport;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.multipartycall.MultiPartyCall;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.multipartycallcontrolmanager.MultiPartyCallControlManager;


/**
 *  
 */
public final class CallEndedHandler implements Runnable {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(CallEndedHandler.class);

    public CallEndedHandler(MultiPartyCallControlManager multiPartyCallControlManager, int callSessionID,
            TpCallEndedReport callEndedReport) {
        super();
        this.multiPartyCallControlManager = multiPartyCallControlManager;
        this.callSessionID = callSessionID;
        this.callEndedReport = callEndedReport;
    }

    private final transient MultiPartyCallControlManager multiPartyCallControlManager;

    private final transient int callSessionID;

    private final transient TpCallEndedReport callEndedReport;


    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    public void run() {
        try {
            if(multiPartyCallControlManager != null ) {
	            final MultiPartyCall multiPartyCall = multiPartyCallControlManager.getMultiPartyCall(callSessionID);
		        if(multiPartyCall != null) {
		            multiPartyCall.callEnded(callSessionID, callEndedReport);
		        }
            }
        }
        catch (RuntimeException e) {
            // Catch all
            logger.error("CallEndedHandler failed", e);
        }
    }

}