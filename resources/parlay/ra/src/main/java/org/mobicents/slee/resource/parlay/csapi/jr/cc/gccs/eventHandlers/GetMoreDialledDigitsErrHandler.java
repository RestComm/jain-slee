package org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.eventHandlers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csapi.cc.TpCallError;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.activity.call.Call;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.activity.callcontrolmanager.CallControlManager;

/**
 *
 **/
public class GetMoreDialledDigitsErrHandler implements Runnable {
    /**
     * Log4J Logger for this class.
     */
    private static final Log logger = LogFactory
        .getLog(GetMoreDialledDigitsErrHandler.class);

    private final transient CallControlManager callControlManager; 
    private final transient int callSessionID;
    private final transient TpCallError errorIndication;
    
    /**
     * @param callControlManager
     * @param callSessionID
     * @param errorIndication
     */
    public GetMoreDialledDigitsErrHandler(CallControlManager callControlManager, int callSessionID, TpCallError errorIndication) {
        super();
        this.callControlManager = callControlManager;
        this.callSessionID = callSessionID;
        this.errorIndication = errorIndication;
    }

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    public void run() {
        try {
            if (callControlManager != null) {
	            final Call call = callControlManager.getCall(callSessionID);
	            if(call != null ) {
	                call.getMoreDialledDigitsErr(callSessionID, errorIndication);
	            }
            }
        } catch (RuntimeException e) {
            // Catch all
            logger.error("GetMoreDialledDigitsErrHandler failed", e);
        }
    }

}
