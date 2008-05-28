package org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.eventHandlers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.multipartycallcontrolmanager.MultiPartyCallControlManager;

/**
 *  
 */
public final class CallAbortedHandler implements Runnable {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory
        .getLog(CallAbortedHandler.class);

    public CallAbortedHandler(MultiPartyCallControlManager multiPartyCallControlManager, int callID) {
        super();
        this.multiPartyCallControlManager = multiPartyCallControlManager;

        this.callID = callID;
    }
    
    private final transient MultiPartyCallControlManager multiPartyCallControlManager;

    private final transient int callID;

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Runnable#run()
     */
    public void run() {
        try {
	        if(multiPartyCallControlManager != null) {
	            multiPartyCallControlManager.callAborted(callID);
	        }
        }
        catch (RuntimeException e) {
            // Catch all
            logger.error("CallAbortedHandler failed", e);
        }
    }

}