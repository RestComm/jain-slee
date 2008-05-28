package org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csapi.cc.TpCallError;
import org.csapi.cc.TpCallEventInfo;
import org.csapi.cc.TpCallLegInfoReport;
import org.csapi.cc.TpReleaseCause;
import org.csapi.cc.mpccs.IpAppCallLegPOA;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.multipartycall.MultiPartyCall;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.eventHandlers.AttachMediaErrHandler;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.eventHandlers.AttachMediaResHandler;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.eventHandlers.CallLegEndedHandler;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.eventHandlers.DetachMediaErrHandler;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.eventHandlers.DetachMediaResHandler;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.eventHandlers.EventReportErrHandler;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.eventHandlers.EventReportResHandler;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.eventHandlers.GetInfoErrHandler;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.eventHandlers.GetInfoResHandler;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.eventHandlers.RouteErrHandler;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.eventHandlers.SuperviseErrHandler;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.eventHandlers.SuperviseResHandler;
import org.omg.PortableServer.POA;

import EDU.oswego.cs.dl.util.concurrent.Executor;

/**
 * 
 * Class Description for IpAppCallLegImpl
 */
public class IpAppCallLegImpl extends IpAppCallLegPOA {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(IpAppCallLegImpl.class);

    public IpAppCallLegImpl(MultiPartyCall multiPartyCall, POA defaultPOA, Executor[] ipAppCallLegExecutors) {
        super();
        this.multiPartyCall = multiPartyCall;
        this.defaultPOA = defaultPOA;
        setExecutors(ipAppCallLegExecutors);
    }

    private transient MultiPartyCall multiPartyCall;

    private transient POA defaultPOA;

    private transient Executor[] executors;

    public void dispose() {
        multiPartyCall = null;
        defaultPOA = null;
    }
    
    /* (non-Javadoc)
     * @see org.omg.PortableServer.Servant#_default_POA()
     */
    public POA _default_POA() {
        return defaultPOA;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.csapi.cc.mpccs.IpAppCallLegOperations#eventReportRes(int,
     *      org.csapi.cc.TpCallEventInfo)
     */
    public void eventReportRes(final int callLegSessionID, final TpCallEventInfo arg1) {
        if (logger.isDebugEnabled()) {
            logger.debug("Received eventReportRes()");
        }

        final EventReportResHandler handler = new EventReportResHandler(
                multiPartyCall, callLegSessionID, arg1);

        try {
            executors[callLegSessionID % executors.length].execute(handler);
        }
        catch (InterruptedException e) {
            logger.error("Interrupted handling eventReportRes");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.csapi.cc.mpccs.IpAppCallLegOperations#eventReportErr(int,
     *      org.csapi.cc.TpCallError)
     */
    public void eventReportErr(final int callLegSessionID, final TpCallError arg1) {
        if (logger.isDebugEnabled()) {
            logger.debug("Received eventReportErr()");
        }

        final EventReportErrHandler handler = new EventReportErrHandler(
                multiPartyCall, callLegSessionID, arg1);

        try {
            executors[callLegSessionID % executors.length].execute(handler);
        }
        catch (InterruptedException e) {
            logger.error("Interrupted handling eventReportErr");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.csapi.cc.mpccs.IpAppCallLegOperations#attachMediaRes(int)
     */
    public void attachMediaRes(final int callLegSessionID) {
        if (logger.isDebugEnabled()) {
            logger.debug("Received attachMediaRes()");
        }

        final AttachMediaResHandler handler = new AttachMediaResHandler(
                multiPartyCall, callLegSessionID);

        try {
            executors[callLegSessionID % executors.length].execute(handler);
        }
        catch (InterruptedException e) {
            logger.error("Interrupted handling attachMediaRes");
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.csapi.cc.mpccs.IpAppCallLegOperations#attachMediaErr(int,
     *      org.csapi.cc.TpCallError)
     */
    public void attachMediaErr(final int callLegSessionID, final TpCallError arg1) {
        if (logger.isDebugEnabled()) {
            logger.debug("Received attachMediaErr()");
        }

        final AttachMediaErrHandler handler = new AttachMediaErrHandler(
                multiPartyCall, callLegSessionID, arg1);

        try {
            executors[callLegSessionID % executors.length].execute(handler);
        }
        catch (InterruptedException e) {
            logger.error("Interrupted handling attachMediaErr");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.csapi.cc.mpccs.IpAppCallLegOperations#detachMediaRes(int)
     */
    public void detachMediaRes(final int callLegSessionID) {
        if (logger.isDebugEnabled()) {
            logger.debug("Received detachMediaRes()");
        }

        final DetachMediaResHandler handler = new DetachMediaResHandler(
                multiPartyCall, callLegSessionID);

        try {
            executors[callLegSessionID % executors.length].execute(handler);
        }
        catch (InterruptedException e) {
            logger.error("Interrupted handling detachMediaRes");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.csapi.cc.mpccs.IpAppCallLegOperations#detachMediaErr(int,
     *      org.csapi.cc.TpCallError)
     */
    public void detachMediaErr(final int callLegSessionID, final TpCallError arg1) {
        if (logger.isDebugEnabled()) {
            logger.debug("Received detachMediaErr()");
        }

        final DetachMediaErrHandler handler = new DetachMediaErrHandler(
                multiPartyCall, callLegSessionID, arg1);

        try {
            executors[callLegSessionID % executors.length].execute(handler);
        }
        catch (InterruptedException e) {
            logger.error("Interrupted handling detachMediaErr");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.csapi.cc.mpccs.IpAppCallLegOperations#getInfoRes(int,
     *      org.csapi.cc.TpCallLegInfoReport)
     */
    public void getInfoRes(final int callLegSessionID, final TpCallLegInfoReport arg1) {
        if (logger.isDebugEnabled()) {
            logger.debug("Received getInfoRes()");
        }

        final GetInfoResHandler handler = new GetInfoResHandler(
                multiPartyCall, callLegSessionID, arg1);

        try {
            executors[callLegSessionID % executors.length].execute(handler);
        }
        catch (InterruptedException e) {
            logger.error("Interrupted handling getInfoRes");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.csapi.cc.mpccs.IpAppCallLegOperations#getInfoErr(int,
     *      org.csapi.cc.TpCallError)
     */
    public void getInfoErr(final int callLegSessionID, final TpCallError arg1) {
        if (logger.isDebugEnabled()) {
            logger.debug("Received getInfoErr()");
        }

        final GetInfoErrHandler handler = new GetInfoErrHandler(
                multiPartyCall, callLegSessionID, arg1);

        try {
            executors[callLegSessionID % executors.length].execute(handler);
        }
        catch (InterruptedException e) {
            logger.error("Interrupted handling getInfoErr");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.csapi.cc.mpccs.IpAppCallLegOperations#routeErr(int,
     *      org.csapi.cc.TpCallError)
     */
    public void routeErr(final int callLegSessionID, final TpCallError arg1) {
        if (logger.isDebugEnabled()) {
            logger.debug("Received routeErr()");
        }

        final RouteErrHandler handler = new RouteErrHandler(
                multiPartyCall, callLegSessionID, arg1);

        try {
            executors[callLegSessionID % executors.length].execute(handler);
        }
        catch (InterruptedException e) {
            logger.error("Interrupted handling routeErr");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.csapi.cc.mpccs.IpAppCallLegOperations#superviseRes(int, int,
     *      int)
     */
    public void superviseRes(final int callLegSessionID, final int arg1, final int arg2) {
        if (logger.isDebugEnabled()) {
            logger.debug("Received superviseRes()");
        }

        final SuperviseResHandler handler = new SuperviseResHandler(
                multiPartyCall, callLegSessionID, arg1, arg2);

        try {
            executors[callLegSessionID % executors.length].execute(handler);
        }
        catch (InterruptedException e) {
            logger.error("Interrupted handling superviseRes");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.csapi.cc.mpccs.IpAppCallLegOperations#superviseErr(int,
     *      org.csapi.cc.TpCallError)
     */
    public void superviseErr(final int callLegSessionID, final TpCallError arg1) {
        if (logger.isDebugEnabled()) {
            logger.debug("Received superviseErr()");
        }

        final SuperviseErrHandler handler = new SuperviseErrHandler(
                multiPartyCall, callLegSessionID, arg1);

        try {
            executors[callLegSessionID % executors.length].execute(handler);
        }
        catch (InterruptedException e) {
            logger.error("Interrupted handling superviseErr");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.csapi.cc.mpccs.IpAppCallLegOperations#callLegEnded(int,
     *      org.csapi.cc.TpReleaseCause)
     */
    public void callLegEnded(final int callLegSessionID, final TpReleaseCause arg1) {
        if (logger.isDebugEnabled()) {
            logger.debug("Received callLegEnded()");
        }

        final CallLegEndedHandler handler = new CallLegEndedHandler(
                multiPartyCall, callLegSessionID, arg1);

        try {
            executors[callLegSessionID % executors.length].execute(handler);
        }
        catch (InterruptedException e) {
            logger.error("Interrupted handling callLegEnded");
        }
    }


    /**
     * @param executor
     *            The executor to set.
     */
    private void setExecutors(final Executor[] executors) {
        this.executors = executors;
    }

}