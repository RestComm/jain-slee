package org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.eventHandlers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csapi.cc.TpCallError;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.activity.call.Call;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.activity.callcontrolmanager.CallControlManager;

/**
 *
 **/
public class RouteErrHandler implements Runnable {
    /**
     * Log4J Logger for this class.
     */
    private static final Log logger = LogFactory.getLog(RouteErrHandler.class);

    private final transient CallControlManager callControlManager; 
    private final transient int callSessionID; 
    private final transient TpCallError errorIndication; 
    private final transient int callLegSessionID;
    
    
    /**
     * @param callControlManager
     * @param callSessionID
     * @param errorIndication
     * @param callLegSessionID
     */
    public RouteErrHandler(CallControlManager callControlManager, int callSessionID, TpCallError errorIndication, int callLegSessionID) {
        super();
        this.callControlManager = callControlManager;
        this.callSessionID = callSessionID;
        this.errorIndication = errorIndication;
        this.callLegSessionID = callLegSessionID;
    }

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    public void run() {
        try {
            if (callControlManager != null) {
	            final Call call = callControlManager.getCall(callSessionID);
	            if(call != null ) {
	                call.routeErr(callSessionID, errorIndication, callLegSessionID);
	            }
            }
        } catch (RuntimeException e) {
            // Catch all
            logger.error("RouteErrHandler failed", e);
        }
        
    }

}
