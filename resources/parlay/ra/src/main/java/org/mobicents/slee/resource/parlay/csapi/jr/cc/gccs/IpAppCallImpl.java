
package org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csapi.cc.TpCallError;
import org.csapi.cc.gccs.IpAppCallPOA;
import org.csapi.cc.gccs.TpCallEndedReport;
import org.csapi.cc.gccs.TpCallFault;
import org.csapi.cc.gccs.TpCallInfoReport;
import org.csapi.cc.gccs.TpCallReport;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.activity.callcontrolmanager.CallControlManager;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.eventHandlers.CallEndedHandler;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.eventHandlers.CallFaultDetectedHandler;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.eventHandlers.GetCallInfoErrHandler;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.eventHandlers.GetCallInfoResHandler;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.eventHandlers.GetMoreDialledDigitsErrHandler;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.eventHandlers.GetMoreDialledDigitsResHandler;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.eventHandlers.RouteErrHandler;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.eventHandlers.RouteResHandler;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.eventHandlers.SuperviseCallErrHandler;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.eventHandlers.SuperviseCallResHandler;
import org.omg.PortableServer.POA;

import EDU.oswego.cs.dl.util.concurrent.Executor;

/**
 *
 **/
public class IpAppCallImpl extends IpAppCallPOA {
    /**
     * Log4J Logger for this class.
     */
    private static final Log logger = LogFactory.getLog(IpAppCallImpl.class);

    private transient POA defaultPOA;
    
    private transient CallControlManager callControlManager;
    
    private transient Executor[] executors;
    
    /**
     * @param callControlManager
     * @param ipAppCallPOA
     * @param ipAppCallExecutors 
     */
    public IpAppCallImpl(CallControlManager callControlManager, POA ipAppCallPOA, Executor[] ipAppCallExecutors) {
        super();
        this.callControlManager = callControlManager;
        this.defaultPOA = ipAppCallPOA;
        setExecutors(ipAppCallExecutors);
    }

    /* (non-Javadoc)
     * @see org.csapi.cc.gccs.IpAppCallOperations#routeRes(int, org.csapi.cc.gccs.TpCallReport, int)
     */
    public void routeRes(final int callSessionID, final TpCallReport eventReport, final int callLegSessionID) {

        final RouteResHandler handler = new RouteResHandler(callControlManager, callSessionID, eventReport, callLegSessionID);

        try {
            executors[callSessionID % executors.length].execute(handler);
        }
        catch (InterruptedException e) {
            logger.error("Interrupted handling RouteRes");
        }
        
    }

    /* (non-Javadoc)
     * @see org.csapi.cc.gccs.IpAppCallOperations#routeErr(int, org.csapi.cc.TpCallError, int)
     */
    public void routeErr(final int callSessionID, final TpCallError errorIndication, final int callLegSessionID) {
        final RouteErrHandler handler = new RouteErrHandler(callControlManager, callSessionID, errorIndication, callLegSessionID);

        try {
            executors[callSessionID % executors.length].execute(handler);
        }
        catch (InterruptedException e) {
            logger.error("Interrupted handling routeErr");
        }
    }

    /* (non-Javadoc)
     * @see org.csapi.cc.gccs.IpAppCallOperations#getCallInfoRes(int, org.csapi.cc.gccs.TpCallInfoReport)
     */
    public void getCallInfoRes(final int callSessionID, final TpCallInfoReport callInfoReport) {
        final GetCallInfoResHandler handler = new GetCallInfoResHandler(callControlManager, callSessionID, callInfoReport);

        try {
            executors[callSessionID % executors.length].execute(handler);
        }
        catch (InterruptedException e) {
            logger.error("Interrupted handling getCallInfoRes");
        }
    }

    /* (non-Javadoc)
     * @see org.csapi.cc.gccs.IpAppCallOperations#getCallInfoErr(int, org.csapi.cc.TpCallError)
     */
    public void getCallInfoErr(final int callSessionID, final TpCallError errorIndication) {
        final GetCallInfoErrHandler handler = new GetCallInfoErrHandler(callControlManager, callSessionID, errorIndication);

        try {
            executors[callSessionID % executors.length].execute(handler);
        }
        catch (InterruptedException e) {
            logger.error("Interrupted handling getCallInfoErr");
        }
        
    }

    /* (non-Javadoc)
     * @see org.csapi.cc.gccs.IpAppCallOperations#superviseCallRes(int, int, int)
     */
    public void superviseCallRes(final int callSessionID, final int report, final int usedTime) {
        final SuperviseCallResHandler handler = new SuperviseCallResHandler(callControlManager, callSessionID, report, usedTime);

        try {
            executors[callSessionID % executors.length].execute(handler);
        }
        catch (InterruptedException e) {
            logger.error("Interrupted handling superviseCallRes");
        }
    }

    /* (non-Javadoc)
     * @see org.csapi.cc.gccs.IpAppCallOperations#superviseCallErr(int, org.csapi.cc.TpCallError)
     */
    public void superviseCallErr(final int callSessionID, final TpCallError errorIndication) {
        final SuperviseCallErrHandler handler = new SuperviseCallErrHandler(callControlManager, callSessionID, errorIndication);

        try {
            executors[callSessionID % executors.length].execute(handler);
        }
        catch (InterruptedException e) {
            logger.error("Interrupted handling superviseCallErr");
        }
        
    }

    /* (non-Javadoc)
     * @see org.csapi.cc.gccs.IpAppCallOperations#callFaultDetected(int, org.csapi.cc.gccs.TpCallFault)
     */
    public void callFaultDetected(final int callSessionID, final TpCallFault fault) {
        final CallFaultDetectedHandler handler = new CallFaultDetectedHandler(callControlManager, callSessionID, fault);

        try {
            executors[callSessionID % executors.length].execute(handler);
        }
        catch (InterruptedException e) {
            logger.error("Interrupted handling callFaultDetected");
        }
    }

    /* (non-Javadoc)
     * @see org.csapi.cc.gccs.IpAppCallOperations#getMoreDialledDigitsRes(int, java.lang.String)
     */
    public void getMoreDialledDigitsRes(final int callSessionID, final String digits) {
        final GetMoreDialledDigitsResHandler handler = new GetMoreDialledDigitsResHandler(callControlManager, callSessionID, digits);

        try {
            executors[callSessionID % executors.length].execute(handler);
        }
        catch (InterruptedException e) {
            logger.error("Interrupted handling getMoreDialledDigitsRes");
        }
        
    }

    /* (non-Javadoc)
     * @see org.csapi.cc.gccs.IpAppCallOperations#getMoreDialledDigitsErr(int, org.csapi.cc.TpCallError)
     */
    public void getMoreDialledDigitsErr(final int callSessionID, final TpCallError errorIndication) {
        final GetMoreDialledDigitsErrHandler handler = new GetMoreDialledDigitsErrHandler(callControlManager, callSessionID, errorIndication);

        try {
            executors[callSessionID % executors.length].execute(handler);
        }
        catch (InterruptedException e) {
            logger.error("Interrupted handling getMoreDialledDigitsErr");
        }
    }

    /* (non-Javadoc)
     * @see org.csapi.cc.gccs.IpAppCallOperations#callEnded(int, org.csapi.cc.gccs.TpCallEndedReport)
     */
    public void callEnded(final int callSessionID, final TpCallEndedReport report) {
        final CallEndedHandler handler = new CallEndedHandler(callControlManager, callSessionID, report);

        try {
            executors[callSessionID % executors.length].execute(handler);
        }
        catch (InterruptedException e) {
            logger.error("Interrupted handling callEnded");
        }
    }

    /**
     * @param ipAppCallExecutors
     */
    private void setExecutors(final Executor[] ipAppCallExecutors) {
        executors = ipAppCallExecutors;
    }

    /**
     * 
     */
    public void dispose() {
        callControlManager = null;
        defaultPOA = null;
        
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.omg.PortableServer.Servant#_default_POA()
     */
    public POA _default_POA() {
        return defaultPOA;
    }

}
