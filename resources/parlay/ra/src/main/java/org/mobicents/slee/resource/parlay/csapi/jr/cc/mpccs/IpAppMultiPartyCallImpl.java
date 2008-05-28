package org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csapi.cc.TpCallEndedReport;
import org.csapi.cc.TpCallError;
import org.csapi.cc.TpCallInfoReport;
import org.csapi.cc.mpccs.IpAppMultiPartyCallPOA;
import org.csapi.cc.mpccs.TpCallLegIdentifier;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.multipartycallcontrolmanager.MultiPartyCallControlManager;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.eventHandlers.CallEndedHandler;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.eventHandlers.CreateAndRouteCallLegErrHandler;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.eventHandlers.GetInfoErrHandler;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.eventHandlers.GetInfoResHandler;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.eventHandlers.SuperviseErrHandler;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.eventHandlers.SuperviseResHandler;
import org.omg.PortableServer.POA;

import EDU.oswego.cs.dl.util.concurrent.Executor;

/**
 * 
 * Class Description for IpAppMultiPartyCallImpl
 */
public class IpAppMultiPartyCallImpl extends IpAppMultiPartyCallPOA {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory
            .getLog(IpAppMultiPartyCallImpl.class);

    public IpAppMultiPartyCallImpl(MultiPartyCallControlManager multiPartyCallControlManager, POA defaultPOA, Executor[] ipAppMultiPartyCallExecutors) {
        super();
        this.multiPartyCallControlManager = multiPartyCallControlManager;
        this.defaultPOA = defaultPOA;
        setExecutors(ipAppMultiPartyCallExecutors);
	}

    private transient MultiPartyCallControlManager multiPartyCallControlManager;

    private transient POA defaultPOA;

    private transient Executor[] executors;

    public void dispose() {
        multiPartyCallControlManager = null;
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

    /*
     * (non-Javadoc)
     * 
     * @see org.csapi.cc.mpccs.IpAppMultiPartyCallOperations#getInfoRes(int,
     *      org.csapi.cc.TpCallInfoReport)
     */
    public void getInfoRes(final int callSessionID, final TpCallInfoReport arg1) {
        if (logger.isDebugEnabled()) {
            logger.debug("Received getInfoRes()");
        }

        final GetInfoResHandler handler = new GetInfoResHandler(multiPartyCallControlManager, callSessionID, arg1);

        try {
            executors[callSessionID % executors.length].execute(handler);
        }
        catch (InterruptedException e) {
            logger.error("Interrupted handling getInfoRes");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.csapi.cc.mpccs.IpAppMultiPartyCallOperations#getInfoErr(int,
     *      org.csapi.cc.TpCallError)
     */
    public void getInfoErr(final int callSessionID, final TpCallError arg1) {
        if (logger.isDebugEnabled()) {
            logger.debug("Received getInfoErr()");
        }

        final GetInfoErrHandler handler = new GetInfoErrHandler(multiPartyCallControlManager, callSessionID, arg1);

        try {
            executors[callSessionID % executors.length].execute(handler);
        }
        catch (InterruptedException e) {
            logger.error("Interrupted handling getInfoErr");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.csapi.cc.mpccs.IpAppMultiPartyCallOperations#superviseRes(int,
     *      int, int)
     */
    public void superviseRes(final int callSessionID, final int arg1, final int arg2) {
        if (logger.isDebugEnabled()) {
            logger.debug("Received superviseRes()");
        }

        final SuperviseResHandler handler = new SuperviseResHandler(multiPartyCallControlManager, callSessionID,
                arg1, arg2);

        try {
            executors[callSessionID % executors.length].execute(handler);
        }
        catch (InterruptedException e) {
            logger.error("Interrupted handling superviseRes");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.csapi.cc.mpccs.IpAppMultiPartyCallOperations#superviseErr(int,
     *      org.csapi.cc.TpCallError)
     */
    public void superviseErr(final int callSessionID, final TpCallError arg1) {
        if (logger.isDebugEnabled()) {
            logger.debug("Received superviseErr()");
        }

        final SuperviseErrHandler handler = new SuperviseErrHandler(multiPartyCallControlManager, callSessionID,
                arg1);

        try {
            executors[callSessionID % executors.length].execute(handler);
        }
        catch (InterruptedException e) {
            logger.error("Interrupted handling superviseErr");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.csapi.cc.mpccs.IpAppMultiPartyCallOperations#callEnded(int,
     *      org.csapi.cc.TpCallEndedReport)
     */
    public void callEnded(final int callSessionID, final TpCallEndedReport arg1) {
        if (logger.isDebugEnabled()) {
            logger.debug("Received callEnded()");
        }

        final CallEndedHandler handler = new CallEndedHandler(multiPartyCallControlManager, callSessionID, arg1);

        try {
            executors[callSessionID % executors.length].execute(handler);
        }
        catch (InterruptedException e) {
            logger.error("Interrupted handling callEnded");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.csapi.cc.mpccs.IpAppMultiPartyCallOperations#createAndRouteCallLegErr(int,
     *      org.csapi.cc.mpccs.TpCallLegIdentifier, org.csapi.cc.TpCallError)
     */
    public void createAndRouteCallLegErr(final int callSessionID, final TpCallLegIdentifier arg1,
            TpCallError arg2) {
        if (logger.isDebugEnabled()) {
            logger.debug("Received createAndRouteCallLegErr()");
        }

        final CreateAndRouteCallLegErrHandler handler = new CreateAndRouteCallLegErrHandler(
                multiPartyCallControlManager, callSessionID, arg1, arg2);

        try {
            executors[callSessionID % executors.length].execute(handler);
        }
        catch (InterruptedException e) {
            logger.error("Interrupted handling createAndRouteCallLegErr");
        }
    }

    /**
     * @return Returns the executor.
     */
    public Executor[] getExecutors() {
        return executors;
    }

    /**
     * @param executor
     *            The executor to set.
     */
    private void setExecutors(final Executor[] executors) {
        this.executors = executors;
    }
}