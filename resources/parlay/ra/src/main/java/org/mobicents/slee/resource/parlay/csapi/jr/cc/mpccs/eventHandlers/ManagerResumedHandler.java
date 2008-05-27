package org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.eventHandlers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.multipartycallcontrolmanager.MultiPartyCallControlManager;



/**
 * 
 */
public final class ManagerResumedHandler implements Runnable {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory
        .getLog(ManagerResumedHandler.class);

    public ManagerResumedHandler(MultiPartyCallControlManager multiPartyCallControlManager) {
        super();
        this.multiPartyCallControlManager = multiPartyCallControlManager;
    }

    private final transient MultiPartyCallControlManager multiPartyCallControlManager;

    public void run() {
        try {
	        if(multiPartyCallControlManager != null) {
	            multiPartyCallControlManager.managerResumed();
	        }
        }
        catch (RuntimeException e) {
            // Catch all
            logger.error("ManagerResumedHandler failed", e);
        }
    }
}