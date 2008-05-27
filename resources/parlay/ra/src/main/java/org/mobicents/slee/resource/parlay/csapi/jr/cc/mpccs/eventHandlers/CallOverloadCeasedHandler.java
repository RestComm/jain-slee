package org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.eventHandlers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.multipartycallcontrolmanager.MultiPartyCallControlManager;


/**
 * 
 */
public final class CallOverloadCeasedHandler implements Runnable {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory
        .getLog(CallOverloadCeasedHandler.class);

    public CallOverloadCeasedHandler(MultiPartyCallControlManager multiPartyCallControlManager, int assignmentID) {
        super();
        this.multiPartyCallControlManager = multiPartyCallControlManager;

        this.assignmentID = assignmentID;
    }

    private final transient MultiPartyCallControlManager multiPartyCallControlManager;

    private final transient int assignmentID;

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Runnable#run()
     */
    public void run() {
        try {
	        if(multiPartyCallControlManager != null) {
	            multiPartyCallControlManager.callOverloadCeased(assignmentID);
	        }
        }
        catch (RuntimeException e) {
            // Catch all
            logger.error("CallOverloadCeasedHandler failed", e);
        }
    }

}