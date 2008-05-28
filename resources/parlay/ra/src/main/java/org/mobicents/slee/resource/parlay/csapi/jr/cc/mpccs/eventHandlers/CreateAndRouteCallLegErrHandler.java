package org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.eventHandlers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csapi.cc.TpCallError;
import org.csapi.cc.mpccs.TpCallLegIdentifier;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.multipartycall.MultiPartyCall;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.multipartycallcontrolmanager.MultiPartyCallControlManager;

/**
 *  
 */
public final class CreateAndRouteCallLegErrHandler implements Runnable {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory
        .getLog(CreateAndRouteCallLegErrHandler.class);

    public CreateAndRouteCallLegErrHandler(MultiPartyCallControlManager multiPartyCallControlManager,
            int callSessionID, TpCallLegIdentifier callLegIdentifier,
            TpCallError callError) {
        super();
        this.multiPartyCallControlManager = multiPartyCallControlManager;
        this.callSessionID = callSessionID;
        this.callLegIdentifier = callLegIdentifier;
        this.callError = callError;
    }

    private final transient MultiPartyCallControlManager multiPartyCallControlManager;

    private final transient int callSessionID;

    private final transient TpCallLegIdentifier callLegIdentifier;

    private final transient TpCallError callError;


    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    public void run() {
        try {
            if(multiPartyCallControlManager != null ) {
	            final MultiPartyCall multiPartyCall = multiPartyCallControlManager.getMultiPartyCall(callSessionID);
		        if(multiPartyCall != null) {
		            multiPartyCall.createAndRouteCallLegErr(callSessionID, callLegIdentifier, callError);
		        }
            }
        }
        catch (RuntimeException e) {
            // Catch all
            logger.error("CreateAndRouteCallLegErrHandler failed", e);
        }
    }

}