package org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.eventHandlers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.activity.call.Call;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.activity.callcontrolmanager.CallControlManager;

/**
 *
 **/
public class GetMoreDialledDigitsResHandler implements Runnable {
    /**
     * Log4J Logger for this class.
     */
    private static final Log logger = LogFactory
        .getLog(GetMoreDialledDigitsResHandler.class);

    private final transient CallControlManager callControlManager; 
    private final transient int callSessionID; 
    private final transient String digits;
    
    /**
     * @param callControlManager
     * @param callSessionID
     * @param digits
     */
    public GetMoreDialledDigitsResHandler(CallControlManager callControlManager, int callSessionID, String digits) {
        super();
        this.callControlManager = callControlManager;
        this.callSessionID = callSessionID;
        this.digits = digits;
    }

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    public void run() {
        try {
            if (callControlManager != null) {
	            final Call call = callControlManager.getCall(callSessionID);
	            if(call != null ) {
	                call.getMoreDialledDigitsRes(callSessionID, digits);
	            }
            }
        } catch (RuntimeException e) {
            // Catch all
            logger.error("GetMoreDialledDigitsResHandler failed", e);
        } 
    }

}
