package org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.eventHandlers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csapi.cc.gccs.TpCallEventInfo;
import org.mobicents.csapi.jr.slee.cc.gccs.TpCallIdentifier;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.activity.callcontrolmanager.CallControlManager;

/**
 *
 **/
public class CallEventNotifyHandler implements Runnable {
    /**
     * Log4J Logger for this class.
     */
    private static final Log logger = LogFactory
        .getLog(CallEventNotifyHandler.class);
    
    private final transient CallControlManager callControlManager;
    
    private final transient TpCallIdentifier callReference;
    
    private final transient TpCallEventInfo eventInfo;
    
    private final transient int assignmentID;

    public CallEventNotifyHandler(CallControlManager callControlManager, 
            TpCallIdentifier callReference, 
            TpCallEventInfo eventInfo, 
            int assignmentID) {
        super();
        this.callControlManager = callControlManager;
        this.callReference = callReference;
        this.eventInfo = eventInfo;
        this.assignmentID = assignmentID;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    public void run() {
        try {
	        if (callControlManager != null) {
	            callControlManager.callEventNotify(callReference, eventInfo, assignmentID);
	        }
        } catch(Exception e) {
//          Catch all
            logger.error("CallEventNotifyHandler failed", e);
        }
        
    }

}
