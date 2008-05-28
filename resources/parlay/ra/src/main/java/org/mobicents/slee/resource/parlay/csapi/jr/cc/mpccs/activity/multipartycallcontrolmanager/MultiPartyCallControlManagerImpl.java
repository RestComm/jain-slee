package org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.multipartycallcontrolmanager;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.slee.resource.ActivityHandle;
import javax.slee.resource.ResourceException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csapi.P_INVALID_ADDRESS;
import org.csapi.P_INVALID_ASSIGNMENT_ID;
import org.csapi.P_INVALID_CRITERIA;
import org.csapi.P_INVALID_EVENT_TYPE;
import org.csapi.P_INVALID_INTERFACE_TYPE;
import org.csapi.P_UNSUPPORTED_ADDRESS_PLAN;
import org.csapi.TpAddressRange;
import org.csapi.TpCommonExceptions;
import org.csapi.cc.TpCallLoadControlMechanism;
import org.csapi.cc.TpCallNotificationInfo;
import org.csapi.cc.TpCallNotificationRequest;
import org.csapi.cc.TpCallTreatment;
import org.csapi.cc.TpNotificationRequested;
import org.csapi.cc.TpNotificationRequestedSetEntry;
import org.csapi.cc.mpccs.IpAppMultiPartyCall;
import org.csapi.cc.mpccs.IpAppMultiPartyCallControlManager;
import org.csapi.cc.mpccs.IpAppMultiPartyCallControlManagerHelper;
import org.csapi.cc.mpccs.IpAppMultiPartyCallHelper;
import org.csapi.cc.mpccs.IpMultiPartyCallControlManager;
import org.csapi.cc.mpccs.TpCallLegIdentifier;
import org.csapi.cc.mpccs.TpMultiPartyCallIdentifier;
import org.mobicents.csapi.jr.slee.TpServiceIdentifier;
import org.mobicents.csapi.jr.slee.cc.mpccs.CallAbortedEvent;
import org.mobicents.csapi.jr.slee.cc.mpccs.CallOverloadCeasedEvent;
import org.mobicents.csapi.jr.slee.cc.mpccs.CallOverloadEncounteredEvent;
import org.mobicents.csapi.jr.slee.cc.mpccs.IpMultiPartyCallConnection;
import org.mobicents.csapi.jr.slee.cc.mpccs.ManagerInterruptedEvent;
import org.mobicents.csapi.jr.slee.cc.mpccs.ManagerResumedEvent;
import org.mobicents.csapi.jr.slee.cc.mpccs.ReportNotificationEvent;
import org.mobicents.slee.resource.parlay.csapi.jr.ParlayServiceActivityHandle;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.IpAppMultiPartyCallControlManagerImpl;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.IpAppMultiPartyCallImpl;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.IpMultiPartyCallConnectionImpl;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.MpccsListener;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.callleg.CallLeg;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.callleg.CallLegImpl;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.multipartycall.MultiPartyCall;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.multipartycall.MultiPartyCallImpl;
import org.mobicents.slee.resource.parlay.fw.FwSession;
import org.mobicents.slee.resource.parlay.session.ServiceSession;
import org.mobicents.slee.resource.parlay.util.ParlayExceptionUtil;
import org.mobicents.slee.resource.parlay.util.ResourceIDFactory;
import org.mobicents.slee.resource.parlay.util.activity.ActivityManager;
import org.mobicents.slee.resource.parlay.util.corba.POAFactory;
import org.mobicents.slee.resource.parlay.util.corba.PolicyFactory;
import org.mobicents.slee.resource.parlay.util.corba.ServantActivationHelper;
import org.omg.CORBA.Policy;
import org.omg.CORBA.UserException;
import org.omg.PortableServer.POA;

import EDU.oswego.cs.dl.util.concurrent.Executor;
import EDU.oswego.cs.dl.util.concurrent.PooledExecutor;
import EDU.oswego.cs.dl.util.concurrent.QueuedExecutor;
import EDU.oswego.cs.dl.util.concurrent.SynchronizedInt;
import EDU.oswego.cs.dl.util.concurrent.ThreadFactory;

/**
 * 
 * Class Description for MpccsSession
 */
public final class MultiPartyCallControlManagerImpl implements
        MultiPartyCallControlManager {

    public MultiPartyCallControlManagerImpl(
            TpServiceIdentifier serviceIdentifier,
            IpMultiPartyCallControlManager ipMultiPartyCallControlManager,
            FwSession fwSession, ActivityManager activityManager,
            MpccsListener eventListener) {
        super();
        this.ipMultiPartyCallControlManager = ipMultiPartyCallControlManager;
        this.fwSession = fwSession;
        this.activityManager = activityManager;
        this.eventListener = eventListener;
        this.serviceIdentifier = serviceIdentifier;

        activityHandle = new ParlayServiceActivityHandle(serviceIdentifier);

        multiPartyCallMap = new HashMap();

        // Create own factory to name threads
        threadFactory = new ThreadFactory() {
            ThreadGroup threadGroup = new ThreadGroup("MPCCS-ThreadGroup");
            final SynchronizedInt i = new SynchronizedInt(0);

            public Thread newThread(final Runnable runnable) {
                return new Thread(threadGroup, runnable, "MPCCS-Thread-" + i.increment());
            }
        };

        if(logger.isDebugEnabled()) {
            // TODO configurable
            logger.debug("Initialising threadpool with size " + THREAD_POOL_SIZE);
        }
        ipAppMultiPartyCallControlManagerExecutor = new PooledExecutor(THREAD_POOL_SIZE);
        ((PooledExecutor) ipAppMultiPartyCallControlManagerExecutor).setThreadFactory(threadFactory);
        ((PooledExecutor) ipAppMultiPartyCallControlManagerExecutor).setKeepAliveTime(-1);

        ipAppMultiPartyCallExecutors = new Executor[NUM_EXECUTORS];
        for (int i = 0; i < NUM_EXECUTORS; i++) {
            ipAppMultiPartyCallExecutors[i] = new QueuedExecutor();

            ((QueuedExecutor) ipAppMultiPartyCallExecutors[i])
                    .setThreadFactory(threadFactory);
        }
    }

    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory
            .getLog(MultiPartyCallControlManagerImpl.class);

    private transient FwSession fwSession = null;

    private transient IpMultiPartyCallControlManager ipMultiPartyCallControlManager;

    private transient IpAppMultiPartyCallControlManagerImpl ipAppMultiPartyCallControlManagerImpl;

    private transient IpAppMultiPartyCallControlManager ipAppMultiPartyCallControlManager;

    private transient IpAppMultiPartyCallImpl ipAppMultiPartyCallImpl;

    private transient IpAppMultiPartyCall ipAppMultiPartyCall = null;

    private final transient ActivityManager activityManager;

    private transient MpccsListener eventListener;

    private final transient TpServiceIdentifier serviceIdentifier;

    private final transient ActivityHandle activityHandle;

    private transient POA ipAppMultiPartyCallControlManagerPOA;

    private transient POA ipAppMultiPartyCallPOA;

    private transient POA ipAppCallLegPOA;

    private final transient Map multiPartyCallMap;

    private final transient Executor ipAppMultiPartyCallControlManagerExecutor;

    private final transient Executor[] ipAppMultiPartyCallExecutors;

    private final transient ThreadFactory threadFactory;

    private static final int NUM_EXECUTORS = 20;
    
    private static final int THREAD_POOL_SIZE = 20;

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.session.ServiceSession#init()
     */
    public void init() throws ResourceException {

        Policy[] policies = PolicyFactory.createTransientPoaPolicies(fwSession
                .getRootPOA());

        synchronized (this) {

            createPOAs(policies);

            if (logger.isDebugEnabled()) {
                logger.debug("Activating servants.");
            }

            activateIpAppMultiPartyCall();

            activateIpAppMultiPartyCallControlManager();

            try {
                if (logger.isDebugEnabled()) {
                    logger.debug("Setting callback.");
                }

                ipMultiPartyCallControlManager
                        .setCallback(ipAppMultiPartyCallControlManager);
            }
            catch (TpCommonExceptions e) {
                logger.error("Failed to set manager callback.", e);
                throw new ResourceException("Failed to set manager callback.",
                        e);
            }
            catch (P_INVALID_INTERFACE_TYPE e) {
                // NOTE: this should never occur so will throw a runtime ex
                logger.error("Invalid interface exception on setCallback." + ParlayExceptionUtil.stringify(e), e);   
                throw new IllegalArgumentException(
                        "Invalid interface exception on setCallback.");
            }

            activityManager.add(activityHandle, serviceIdentifier);

            activityManager.activityStartedSuspended(activityHandle);

        }
    }

    /**
     * @param policies
     * @throws ResourceException
     */
    protected void createPOAs(final Policy[] policies) throws ResourceException {
        try {
            if (logger.isDebugEnabled()) {
                logger.debug("Creating MPCCS POAs.");
            }

            ipAppMultiPartyCallControlManagerPOA = POAFactory.createPOA(
                    fwSession.getRootPOA(),
                    "ipAppMultiPartyCallControlManagerPOA_"
                            + serviceIdentifier.getServiceID(), fwSession
                            .getRootPOA().the_POAManager(), policies);

            if (logger.isDebugEnabled()) {
                logger.debug("Created ipAppMultiPartyCallControlManagerPOA_"
                        + serviceIdentifier.getServiceID());
            }

            ipAppMultiPartyCallPOA = POAFactory.createPOA(fwSession
                    .getRootPOA(), "ipAppMultiPartyCallPOA_"
                    + serviceIdentifier.getServiceID(), fwSession.getRootPOA()
                    .the_POAManager(), policies);

            if (logger.isDebugEnabled()) {
                logger.debug("Created ipAppMultiPartyCallPOA_"
                        + serviceIdentifier.getServiceID());
            }

            ipAppCallLegPOA = POAFactory.createPOA(fwSession.getRootPOA(),
                    "ipAppCallLegPOA_" + serviceIdentifier.getServiceID(),
                    fwSession.getRootPOA().the_POAManager(), policies);

            if (logger.isDebugEnabled()) {
                logger.debug("Created ipAppCallLegPOA_"
                        + serviceIdentifier.getServiceID());
            }
        }
        catch (UserException e) {
            logger.error("Failed to initialise POAs.", e);
            throw new ResourceException("Failed to initialise POAs.", e);
        }
    }

    /**
     * @throws ResourceException
     */
    protected void activateIpAppMultiPartyCallControlManager()
            throws ResourceException {

        ipAppMultiPartyCallControlManagerImpl = new IpAppMultiPartyCallControlManagerImpl(
                this, ipAppMultiPartyCallControlManagerPOA, ipAppMultiPartyCallControlManagerExecutor);

        try {
            if (logger.isDebugEnabled()) {
                logger.debug("Activating servants.");
            }

            final org.omg.CORBA.Object tmpRef = ServantActivationHelper
                    .activateServant(ipAppMultiPartyCallControlManagerPOA,
                            ipAppMultiPartyCallControlManagerImpl);

            if (logger.isDebugEnabled()) {
                logger.debug("Activated ipAppMultiPartyCallControlManager.");
            }

            ipAppMultiPartyCallControlManager = IpAppMultiPartyCallControlManagerHelper
                    .narrow(tmpRef);
        }
        catch (UserException e) {
            logger.error(
                    "Failed to activate ipAppMultiPartyCallControlManager.", e);
            throw new ResourceException(
                    "Failed to activate ipAppMultiPartyCallControlManager.", e);
        }
    }

    /**
     * @throws ResourceException
     */
    protected void activateIpAppMultiPartyCall() throws ResourceException {

        ipAppMultiPartyCallImpl = new IpAppMultiPartyCallImpl(this,
                ipAppMultiPartyCallControlManagerPOA, ipAppMultiPartyCallExecutors);

        try {

            final org.omg.CORBA.Object tmpRef = ServantActivationHelper
                    .activateServant(ipAppMultiPartyCallPOA,
                            ipAppMultiPartyCallImpl);

            if (logger.isDebugEnabled()) {
                logger.debug("Activated ipAppMultiPartyCall.");
            }

            ipAppMultiPartyCall = IpAppMultiPartyCallHelper.narrow(tmpRef);
        }
        catch (UserException e) {
            logger.error("Failed to activate ipAppMultiPartyCall.", e);
            throw new ResourceException(
                    "Failed to activate ipAppMultiPartyCall.", e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.session.ServiceSession#getType()
     */
    public int getType() {
        return ServiceSession.MultiPartyCallControl;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.session.ServiceSession#getTpServiceIdentifier()
     */
    public TpServiceIdentifier getTpServiceIdentifier() {
        return serviceIdentifier;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.session.ServiceSession#destroy()
     */
    public void destroy() {
        synchronized (this) {
            if (logger.isDebugEnabled()) {
                logger.debug("Disposing of MpccsSession");
            }

            ipMultiPartyCallControlManager = null;

            final Collection calls = multiPartyCallMap.values();
            for (final Iterator iter = calls.iterator(); iter.hasNext();) {
                final MultiPartyCall multiPartyCall = (MultiPartyCall) iter.next();
                multiPartyCall.dispose();
            }
            multiPartyCallMap.clear();

            deactivateIpAppMultiPartyCall();

            deactivateIpAppMultiPartyCallControlManager();

            destroyPOAs();

            eventListener = null;

            fwSession = null;

        }
    }

    /**
     *  
     */
    private void destroyPOAs() {
        if (ipAppCallLegPOA != null) {
            POAFactory.destroyPOA(ipAppCallLegPOA);
            ipAppCallLegPOA = null;
        }
        if (ipAppMultiPartyCallPOA != null) {
            POAFactory.destroyPOA(ipAppMultiPartyCallPOA);
            ipAppMultiPartyCallPOA = null;
        }
        if (ipAppMultiPartyCallControlManagerPOA != null) {
            POAFactory.destroyPOA(ipAppMultiPartyCallControlManagerPOA);
            ipAppMultiPartyCallControlManagerPOA = null;
        }
    }

    /**
     *  
     */
    protected void deactivateIpAppMultiPartyCallControlManager() {
        if (ipAppMultiPartyCallControlManagerImpl != null) {
            try {
                ServantActivationHelper
                        .deactivateServant(ipAppMultiPartyCallControlManagerImpl);
            }
            catch (UserException e) {
                logger
                        .error(
                                "Failed to deactivate IpAppMultiPartyCallControlManager servant.",
                                e);
            }

            ipAppMultiPartyCallControlManagerImpl.dispose();

            ipAppMultiPartyCallControlManager = null;

            ipAppMultiPartyCallControlManagerImpl = null;
        }
    }

    /**
     *  
     */
    protected void deactivateIpAppMultiPartyCall() {
        if (ipAppMultiPartyCallImpl != null) {
            try {
                ServantActivationHelper
                        .deactivateServant(ipAppMultiPartyCallImpl);
            }
            catch (UserException e) {
                logger.error(
                        "Failed to deactivate IpAppMultiPartyCall servant.", e);
            }

            ipAppMultiPartyCallImpl.dispose();

            ipAppMultiPartyCall = null;

            ipAppMultiPartyCallImpl = null;
        }
    }

    /**
     * @return Returns the ipAppMultiPartyCall.
     */
    public IpAppMultiPartyCall getIpAppMultiPartyCall() {
        return ipAppMultiPartyCall;
    }

    /**
     * @return Returns the ipAppMultiPartyCallControlManager.
     */
    public IpAppMultiPartyCallControlManager getIpAppMultiPartyCallControlManager() {
        return ipAppMultiPartyCallControlManager;
    }

    /**
     * @return Returns the ipAppMultiPartyCallControlManagerImpl.
     */
    public IpAppMultiPartyCallControlManagerImpl getIpAppMultiPartyCallControlManagerImpl() {
        return ipAppMultiPartyCallControlManagerImpl;
    }

    /**
     * @return Returns the ipAppMultiPartyCallImpl.
     */
    public IpAppMultiPartyCallImpl getIpAppMultiPartyCallImpl() {
        return ipAppMultiPartyCallImpl;
    }

    /**
     * @return Returns the ipMultiPartyCallControlManager.
     */
    public IpMultiPartyCallControlManager getIpMultiPartyCallControlManager() {
        synchronized (this) {
            return ipMultiPartyCallControlManager;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.MpccsSession#getIpAppCallLegPOA()
     */
    public POA getIpAppCallLegPOA() {
        return ipAppCallLegPOA;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.MpccsSession#getMultiPartyCall(org.csapi.cc.mpccs.TpMultiPartyCallIdentifier)
     */
    public MultiPartyCall getMultiPartyCall(final int callSessionID) {
        return (MultiPartyCall) multiPartyCallMap
                .get(new Integer(callSessionID));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.MpccsSession#removeMultiPartyCall(org.csapi.cc.mpccs.TpMultiPartyCallIdentifier)
     */
    public MultiPartyCall removeMultiPartyCall(final int callSessionID) {
        return (MultiPartyCall) multiPartyCallMap.remove(new Integer(
                callSessionID));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.MpccsSession#addMultiPartyCall(org.csapi.cc.mpccs.TpMultiPartyCallIdentifier,
     *      org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.multipartycall.MultiPartyCall)
     */
    public void addMultiPartyCall(final int callSessionID,
            final MultiPartyCall multiPartyCall) {
        multiPartyCallMap.put(new Integer(callSessionID), multiPartyCall);
    }

 
    /* (non-Javadoc)
     * @see org.mobicents.csapi.jr.slee.cc.mpccs.IpMultiPartyCallControlManagerConnection#createCall()
     */
    public org.mobicents.csapi.jr.slee.cc.mpccs.TpMultiPartyCallIdentifier createCall()
            throws TpCommonExceptions, javax.slee.resource.ResourceException {

        org.mobicents.csapi.jr.slee.cc.mpccs.TpMultiPartyCallIdentifier result = null;

        final IpMultiPartyCallControlManager multiPartyCallControlManager = getIpMultiPartyCallControlManager();

        if (multiPartyCallControlManager != null) {
            TpMultiPartyCallIdentifier multiPartyCallIdentifier;
            try {
                multiPartyCallIdentifier = multiPartyCallControlManager
                        .createCall(getIpAppMultiPartyCall());

                MultiPartyCall call = createCall(multiPartyCallIdentifier);
                result = call.getTpMultiPartyCallIdentifier();
                
                activityManager.add(call.getActivityHandle(), call.getTpMultiPartyCallIdentifier());
                
                activityManager.activityStartedSuspended(call.getActivityHandle());
            }
            catch (P_INVALID_INTERFACE_TYPE e) {
                logger.error(ParlayExceptionUtil.stringify(e), e);   
                throw new javax.slee.resource.ResourceException("Unexpected Parlay exception", e);
            }
        }

        return result;
    }


    /* (non-Javadoc)
     * @see org.mobicents.csapi.jr.slee.cc.mpccs.IpMultiPartyCallControlManagerConnection#createNotification(org.csapi.cc.TpCallNotificationRequest)
     */
    public int createNotification(final TpCallNotificationRequest arg1)
            throws TpCommonExceptions, P_INVALID_CRITERIA, P_INVALID_EVENT_TYPE, javax.slee.resource.ResourceException {

        int result = 0;

        final IpMultiPartyCallControlManager multiPartyCallControlManager = getIpMultiPartyCallControlManager();

        if (multiPartyCallControlManager != null) {
            try {
                result = multiPartyCallControlManager.createNotification(
                        getIpAppMultiPartyCallControlManager(), arg1);
            }
            catch (P_INVALID_INTERFACE_TYPE e) {
                logger.error(ParlayExceptionUtil.stringify(e), e);   
                throw new javax.slee.resource.ResourceException("Unexpected Parlay exception", e);
            }
        }

        return result;
    }


    /* (non-Javadoc)
     * @see org.mobicents.csapi.jr.slee.cc.mpccs.IpMultiPartyCallControlManagerConnection#destroyNotification(int)
     */
    public void destroyNotification(final int arg0) throws TpCommonExceptions,
            P_INVALID_ASSIGNMENT_ID {

        final IpMultiPartyCallControlManager multiPartyCallControlManager = getIpMultiPartyCallControlManager();

        if (multiPartyCallControlManager != null) {
            multiPartyCallControlManager.destroyNotification(arg0);
        }

    }


    /* (non-Javadoc)
     * @see org.mobicents.csapi.jr.slee.cc.mpccs.IpMultiPartyCallControlManagerConnection#changeNotification(int, org.csapi.cc.TpCallNotificationRequest)
     */
    public void changeNotification(final int arg0, final TpCallNotificationRequest arg1)
            throws TpCommonExceptions, P_INVALID_ASSIGNMENT_ID,
            P_INVALID_CRITERIA, P_INVALID_EVENT_TYPE {

        final IpMultiPartyCallControlManager multiPartyCallControlManager = getIpMultiPartyCallControlManager();

        if (multiPartyCallControlManager != null) {
            multiPartyCallControlManager.changeNotification(arg0, arg1);
        }

    }

  
    /* (non-Javadoc)
     * @see org.mobicents.csapi.jr.slee.cc.mpccs.IpMultiPartyCallControlManagerConnection#getNotification()
     */
    public TpNotificationRequested[] getNotification()
            throws TpCommonExceptions {

        TpNotificationRequested[] result = null;

        final IpMultiPartyCallControlManager multiPartyCallControlManager = getIpMultiPartyCallControlManager();

        if (multiPartyCallControlManager != null) {
            result = multiPartyCallControlManager.getNotification();
        }

        return result;
    }


    /* (non-Javadoc)
     * @see org.mobicents.csapi.jr.slee.cc.mpccs.IpMultiPartyCallControlManagerConnection#setCallLoadControl(int, org.csapi.cc.TpCallLoadControlMechanism, org.csapi.cc.TpCallTreatment, org.csapi.TpAddressRange)
     */
    public int setCallLoadControl(final int arg0, final TpCallLoadControlMechanism arg1,
            final TpCallTreatment arg2, final TpAddressRange arg3)
            throws TpCommonExceptions, P_INVALID_ADDRESS,
            P_UNSUPPORTED_ADDRESS_PLAN {

        int result = 0;

        final IpMultiPartyCallControlManager multiPartyCallControlManager = getIpMultiPartyCallControlManager();

        if (multiPartyCallControlManager != null) {
            result = multiPartyCallControlManager.setCallLoadControl(arg0,
                    arg1, arg2, arg3);
        }

        return result;
    }


    /* (non-Javadoc)
     * @see org.mobicents.csapi.jr.slee.cc.mpccs.IpMultiPartyCallControlManagerConnection#enableNotifications()
     */
    public int enableNotifications() throws TpCommonExceptions {

        int result = 0;

        final IpMultiPartyCallControlManager multiPartyCallControlManager = getIpMultiPartyCallControlManager();

        if (multiPartyCallControlManager != null) {
            result = multiPartyCallControlManager
                    .enableNotifications(getIpAppMultiPartyCallControlManager());
        }

        return result;
    }

    /* (non-Javadoc)
     * @see org.mobicents.csapi.jr.slee.cc.mpccs.IpMultiPartyCallControlManagerConnection#disableNotifications()
     */
    public void disableNotifications() throws TpCommonExceptions {

        final IpMultiPartyCallControlManager multiPartyCallControlManager = getIpMultiPartyCallControlManager();

        if (multiPartyCallControlManager != null) {
            multiPartyCallControlManager.disableNotifications();
        }
    }


    /* (non-Javadoc)
     * @see org.mobicents.csapi.jr.slee.cc.mpccs.IpMultiPartyCallControlManagerConnection#getNextNotification(boolean)
     */
    public TpNotificationRequestedSetEntry getNextNotification(final boolean arg0)
            throws TpCommonExceptions {

        TpNotificationRequestedSetEntry result = null;

        IpMultiPartyCallControlManager multiPartyCallControlManager = getIpMultiPartyCallControlManager();

        if (multiPartyCallControlManager != null) {
            result = multiPartyCallControlManager.getNextNotification(arg0);
        }

        return result;
    }


    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.multipartycallcontrolmanager.MultiPartyCallControlManager#reportNotification(org.mobicents.csapi.jr.slee.cc.mpccs.TpMultiPartyCallIdentifier, org.mobicents.csapi.jr.slee.cc.mpccs.TpCallLegIdentifier[], org.csapi.cc.TpCallNotificationInfo, int)
     */
    public void reportNotification(
            final org.mobicents.csapi.jr.slee.cc.mpccs.TpMultiPartyCallIdentifier arg0,
            final org.mobicents.csapi.jr.slee.cc.mpccs.TpCallLegIdentifier[] arg1,
            final TpCallNotificationInfo arg2, int arg3) {

        final IpMultiPartyCallControlManager multiPartyCallControlManager = getIpMultiPartyCallControlManager();

        if (multiPartyCallControlManager != null) {

            MultiPartyCall call = getMultiPartyCall(arg0.getCallSessionID());
            if (call != null) {
                activityManager.add(call.getActivityHandle(), call.getTpMultiPartyCallIdentifier());

                activityManager.activityStarted(call.getActivityHandle());

                for (int i = 0; i < arg1.length; i++) {
                    CallLeg callLeg = call.getCallLeg(arg1[i].getCallLegSessionID());
                    if (callLeg != null) {
                        activityManager.add(callLeg.getActivityHandle(), callLeg.getTpCallLegIdentifier());

                        activityManager.activityStarted(callLeg.getActivityHandle());
                    }
                }
            }

            final ReportNotificationEvent event = new ReportNotificationEvent(serviceIdentifier, arg0, arg1, arg2, arg3);
    
            // Notify app
            eventListener.onReportNotificationEvent(event);
        }
        
    }


    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.multipartycallcontrolmanager.MultiPartyCallControlManager#callAborted(int)
     */
    public void callAborted(final int arg0) {

        final IpMultiPartyCallControlManager multiPartyCallControlManager = getIpMultiPartyCallControlManager();

        if (multiPartyCallControlManager != null) {
            CallAbortedEvent event = null;

            final MultiPartyCall call = getMultiPartyCall(arg0);
            if (call != null) {
                event = new CallAbortedEvent(serviceIdentifier, call
                        .getTpMultiPartyCallIdentifier());

                // Sending this as an event on the manager
                // opionally we could also send it as a final event on the call
                eventListener.onCallAbortedEvent(event);

                activityManager.remove(call.getActivityHandle(), call
                        .getTpMultiPartyCallIdentifier());

                activityManager.activityEnding(call.getActivityHandle());

                call.dispose();
            }
        }
    }


    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.multipartycallcontrolmanager.MultiPartyCallControlManager#managerInterrupted()
     */
    public void managerInterrupted() {

        final IpMultiPartyCallControlManager multiPartyCallControlManager = getIpMultiPartyCallControlManager();

        if (multiPartyCallControlManager != null) {
            final ManagerInterruptedEvent event = new ManagerInterruptedEvent(
                    serviceIdentifier);

            eventListener.onManagerInterruptedEvent(event);
        }
    }


    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.multipartycallcontrolmanager.MultiPartyCallControlManager#managerResumed()
     */
    public void managerResumed() {

        final IpMultiPartyCallControlManager multiPartyCallControlManager = getIpMultiPartyCallControlManager();

        if (multiPartyCallControlManager != null) {
            final ManagerResumedEvent event = new ManagerResumedEvent(
                    serviceIdentifier);

            eventListener.onManagerResumedEvent(event);
        }
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.multipartycallcontrolmanager.MultiPartyCallControlManager#callOverloadEncountered(int)
     */
    public void callOverloadEncountered(final int arg0) {

        final IpMultiPartyCallControlManager multiPartyCallControlManager = getIpMultiPartyCallControlManager();

        if (multiPartyCallControlManager != null) {
            final CallOverloadEncounteredEvent event = new CallOverloadEncounteredEvent(
                    serviceIdentifier, arg0);

            eventListener.onCallOverloadEncounteredEvent(event);
        }
    }


    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.multipartycallcontrolmanager.MultiPartyCallControlManager#callOverloadCeased(int)
     */
    public void callOverloadCeased(final int arg0) {

        final IpMultiPartyCallControlManager multiPartyCallControlManager = getIpMultiPartyCallControlManager();

        if (multiPartyCallControlManager != null) {
            final CallOverloadCeasedEvent event = new CallOverloadCeasedEvent(
                    serviceIdentifier, arg0);

            eventListener.onCallOverloadCeasedEvent(event);
        }
    }

    /**
     * Utility method for creating a new Call.
     * @see org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.multipartycallcontrolmanager.MultiPartyCallControlManager#createCall(org.csapi.cc.mpccs.TpMultiPartyCallIdentifier)
     */
    public MultiPartyCall createCall(final TpMultiPartyCallIdentifier corbaCallIdentifier) {

        final int callID = ResourceIDFactory.getNextID();

        final org.mobicents.csapi.jr.slee.cc.mpccs.TpMultiPartyCallIdentifier multiPartyCallIdentifier = new org.mobicents.csapi.jr.slee.cc.mpccs.TpMultiPartyCallIdentifier(
                callID, corbaCallIdentifier.CallSessionID);

        final MultiPartyCall multiPartyCall = new MultiPartyCallImpl(this,
                multiPartyCallIdentifier, corbaCallIdentifier.CallReference,
                corbaCallIdentifier.CallSessionID, activityManager, eventListener,
                ipAppMultiPartyCallExecutors);

        multiPartyCall.init();

        addMultiPartyCall(corbaCallIdentifier.CallSessionID, multiPartyCall);

        return multiPartyCall;
    }

    /**
     * Utility method for creating an new CallLeg.
     * @see org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.multipartycallcontrolmanager.MultiPartyCallControlManager#createCallLeg(org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.multipartycall.MultiPartyCall, org.csapi.cc.mpccs.TpCallLegIdentifier)
     */
    public CallLeg createCallLeg(final MultiPartyCall call,
            final TpCallLegIdentifier identifier) {

        final int callLegReferenceID = ResourceIDFactory.getNextID();

        final org.mobicents.csapi.jr.slee.cc.mpccs.TpCallLegIdentifier callLegIdentifier = new org.mobicents.csapi.jr.slee.cc.mpccs.TpCallLegIdentifier(
                callLegReferenceID, identifier.CallLegSessionID);

        final CallLeg callLeg = new CallLegImpl(this, call, callLegIdentifier,
                identifier.CallLegReference, identifier.CallLegSessionID,
                activityManager, eventListener);

        callLeg.init();

        call.addCallLeg(identifier.CallLegSessionID, callLeg);

        return callLeg;
    }

 
    /* (non-Javadoc)
     * @see org.mobicents.csapi.jr.slee.cc.mpccs.IpMultiPartyCallControlManagerConnection#getIpMultiPartyCallConnection(org.mobicents.csapi.jr.slee.cc.mpccs.TpMultiPartyCallIdentifier)
     */
    public IpMultiPartyCallConnection getIpMultiPartyCallConnection(
            final org.mobicents.csapi.jr.slee.cc.mpccs.TpMultiPartyCallIdentifier multiPartyCallIdentifier)
            throws javax.slee.resource.ResourceException {
        final MultiPartyCall multiPartyCall = getMultiPartyCall(multiPartyCallIdentifier
                .getCallSessionID());

        if (multiPartyCall != null) {
            return new IpMultiPartyCallConnectionImpl(multiPartyCall);
        }
        else {
            throw new javax.slee.resource.ResourceException(
                    "Unrecognized TpMultiPartyCallIdentifier");
        }
    }
 
    /* (non-Javadoc)
     * @see org.mobicents.csapi.jr.slee.IpServiceConnection#closeConnection()
     */
    public void closeConnection() throws javax.slee.resource.ResourceException {
    }
}