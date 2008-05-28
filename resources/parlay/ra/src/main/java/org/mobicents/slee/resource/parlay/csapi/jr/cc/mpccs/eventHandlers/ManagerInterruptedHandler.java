package org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.eventHandlers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.multipartycallcontrolmanager.MultiPartyCallControlManager;



/**
 * 
 */
public final class ManagerInterruptedHandler implements Runnable {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory
        .getLog(ManagerInterruptedHandler.class);

    public ManagerInterruptedHandler(MultiPartyCallControlManager multiPartyCallControlManager) {
        super();
        this.multiPartyCallControlManager = multiPartyCallControlManager;
    }

    private final transient MultiPartyCallControlManager multiPartyCallControlManager;

    public void run() {
        try {
	        if(multiPartyCallControlManager != null) {
	            multiPartyCallControlManager.managerInterrupted();
	        }
        }
        catch (RuntimeException e) {
            // Catch all
            logger.error("ManagerInterruptedHandler failed", e);
        }
    }

}