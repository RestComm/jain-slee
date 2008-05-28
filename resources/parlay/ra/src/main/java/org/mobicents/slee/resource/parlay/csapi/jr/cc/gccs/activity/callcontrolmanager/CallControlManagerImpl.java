
package org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.activity.callcontrolmanager;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.slee.resource.ActivityHandle;

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
import org.csapi.cc.gccs.IpAppCall;
import org.csapi.cc.gccs.IpAppCallControlManager;
import org.csapi.cc.gccs.IpAppCallControlManagerHelper;
import org.csapi.cc.gccs.IpAppCallHelper;
import org.csapi.cc.gccs.IpCallControlManager;
import org.csapi.cc.gccs.TpCallEventCriteria;
import org.csapi.cc.gccs.TpCallEventCriteriaResult;
import org.csapi.cc.gccs.TpCallEventInfo;
import org.csapi.cc.gccs.TpCallIdentifier;
import org.csapi.cc.gccs.TpCallTreatment;
import org.mobicents.csapi.jr.slee.TpServiceIdentifier;
import org.mobicents.csapi.jr.slee.cc.gccs.CallAbortedEvent;
import org.mobicents.csapi.jr.slee.cc.gccs.CallEventNotifyEvent;
import org.mobicents.csapi.jr.slee.cc.gccs.CallNotificationContinuedEvent;
import org.mobicents.csapi.jr.slee.cc.gccs.CallNotificationInterruptedEvent;
import org.mobicents.csapi.jr.slee.cc.gccs.CallOverloadCeasedEvent;
import org.mobicents.csapi.jr.slee.cc.gccs.CallOverloadEncounteredEvent;
import org.mobicents.csapi.jr.slee.cc.gccs.IpCallConnection;
import org.mobicents.slee.resource.parlay.csapi.jr.ParlayServiceActivityHandle;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.GccsListener;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.IpAppCallControlManagerImpl;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.IpAppCallImpl;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.IpCallConnectionImpl;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.activity.call.Call;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.activity.call.CallImpl;
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
 **/
public class CallControlManagerImpl implements CallControlManager {
    /**
     * Log4J Logger for this class.
     */
    private static final Log logger = LogFactory
        .getLog(CallControlManagerImpl.class);

    private static final String CALLBACK_FAILED = "Failed to set manager callback.";

    private final transient Map callMap;
    
    private final transient TpServiceIdentifier serviceIdentifier;
    
    private final transient Executor ipAppCallControlManagerExecutor;
    
    private final transient Executor[] ipAppCallExecutors;
    
    private final transient FwSession fwSession;
    
    private final transient ActivityManager activityManager;
    
    private final transient ActivityHandle activityHandle;
    
    private transient GccsListener eventListener;
    
    private transient POA ipAppCallControlManagerPOA;
    
    private transient POA ipAppCallPOA;
    
    private transient IpAppCallControlManagerImpl ipAppCallControlManagerImpl;
    
    private transient IpAppCallImpl ipAppCallImpl;
    
    private transient IpAppCallControlManager ipAppCallControlManager;
    
    private transient IpAppCall ipAppCall;
    
    private transient IpCallControlManager ipCallControlManager;

    private final transient ThreadFactory threadFactory;

    private static final int NUM_EXECUTORS = 20;
    
    private static final int THREAD_POOL_SIZE = 20;
    
    
    public CallControlManagerImpl(FwSession fwSession, IpCallControlManager ipCallControlManager, GccsListener eventListener, ActivityManager activityManager, TpServiceIdentifier serviceIdentifier) {
       super();
       
       this.fwSession = fwSession;
       this.ipCallControlManager = ipCallControlManager;
       this.eventListener = eventListener;
       this.activityManager = activityManager;
       this.serviceIdentifier = serviceIdentifier;
       
       callMap = new HashMap();

       if(logger.isDebugEnabled()) {
           // TODO configurable
           logger.debug("Initialising threadpool with size " + THREAD_POOL_SIZE);
       }
       ipAppCallControlManagerExecutor = new PooledExecutor(THREAD_POOL_SIZE);
       
       // Create own factory to name threads
       threadFactory = new ThreadFactory() {
           ThreadGroup threadGroup = new ThreadGroup("GCCS-ThreadGroup");
           final SynchronizedInt i = new SynchronizedInt(0);

           public Thread newThread(final Runnable runnable) {
               return new Thread(threadGroup, runnable, "GCCS-Thread-" + i.increment());
           }
       };
       ((PooledExecutor)ipAppCallControlManagerExecutor).setThreadFactory(threadFactory);
       ((PooledExecutor)ipAppCallControlManagerExecutor).setKeepAliveTime(-1);

       ipAppCallExecutors = new Executor[NUM_EXECUTORS];
       for (int i = 0; i < NUM_EXECUTORS; i++) {
           ipAppCallExecutors[i] = new QueuedExecutor();
           
           ((QueuedExecutor) ipAppCallExecutors[i])
           .setThreadFactory(threadFactory);
       }
       
       activityHandle = new ParlayServiceActivityHandle(this.serviceIdentifier);
    }
    
    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.parlay.session.ServiceSession#init()
     */
    public void init() throws javax.slee.resource.ResourceException {
        
        final Policy[] policies = PolicyFactory.createTransientPoaPolicies(fwSession.getRootPOA());
        
        synchronized (this) {
            
            createPOAs(policies);
            
            if (logger.isDebugEnabled()) {
                logger.debug("Activating servants.");
            }
            
            activateIpAppCall();
            
            activateIpAppCallControlManager();
            
            if (logger.isDebugEnabled()) {
                logger.debug("Setting callback.");
            }
            
            try {
                ipCallControlManager.setCallback(ipAppCallControlManager);
            } catch (P_INVALID_INTERFACE_TYPE e) {
                logger.error(CALLBACK_FAILED + ParlayExceptionUtil.stringify(e), e);                
                throw new javax.slee.resource.ResourceException(CALLBACK_FAILED,
                        e);
            } catch (TpCommonExceptions e) {
                logger.error(CALLBACK_FAILED, e);
                throw new javax.slee.resource.ResourceException(CALLBACK_FAILED,
                        e);
            }
            
            activityManager.add(activityHandle, serviceIdentifier);
            
            activityManager.activityStartedSuspended(activityHandle);
        }
    }
    

    /**
     * @throws javax.slee.resource.ResourceException
     */
    protected void activateIpAppCall() throws javax.slee.resource.ResourceException {

        ipAppCallImpl = new IpAppCallImpl(this,
                ipAppCallControlManagerPOA, ipAppCallExecutors);

        
        try {

            final org.omg.CORBA.Object tmpRef = ServantActivationHelper
                    .activateServant(ipAppCallPOA,
                            ipAppCallImpl);

            if (logger.isDebugEnabled()) {
                logger.debug("Activated ipAppCall.");
            }

            ipAppCall = IpAppCallHelper.narrow(tmpRef);
            
        }
        catch (UserException e) {
            logger.error("Failed to activate ipAppCall.", e);
            throw new javax.slee.resource.ResourceException(
                    "Failed to activate ipAppCall.", e);
        }
    }
    

    /**
     * @throws javax.slee.resource.ResourceException
     */
    protected void activateIpAppCallControlManager() throws javax.slee.resource.ResourceException {

        ipAppCallControlManagerImpl = new IpAppCallControlManagerImpl(this,
                ipAppCallControlManagerPOA, ipAppCallControlManagerExecutor);

        try {

            final org.omg.CORBA.Object tmpRef = ServantActivationHelper
                    .activateServant(ipAppCallControlManagerPOA,
                            ipAppCallControlManagerImpl);

            if (logger.isDebugEnabled()) {
                logger.debug("Activated ipAppCallControlManager.");
            }

            ipAppCallControlManager = IpAppCallControlManagerHelper.narrow(tmpRef);
        }
        catch (UserException e) {
            logger.error("Failed to activate ipAppCallControlManager.", e);
            throw new javax.slee.resource.ResourceException(
                    "Failed to activate ipAppCallControlManager.", e);
        }
    }
    

    /**
     * @param policies
     * @throws javax.slee.resource.ResourceException
     */
    protected void createPOAs(final Policy[] policies) throws javax.slee.resource.ResourceException {
        try {
            if (logger.isDebugEnabled()) {
                logger.debug("Creating GCCS POAs.");
            }

            ipAppCallControlManagerPOA = POAFactory.createPOA(
                    fwSession.getRootPOA(),
                    "ipAppCallControlManagerPOA_"
                            + serviceIdentifier.getServiceID(), fwSession
                            .getRootPOA().the_POAManager(), policies);

            if (logger.isDebugEnabled()) {
                logger.debug("Created ipAppCallControlManagerPOA_"
                        + serviceIdentifier.getServiceID());
            }

            ipAppCallPOA = POAFactory.createPOA(
                    fwSession.getRootPOA(), "ipAppCallPOA_"
                    + serviceIdentifier.getServiceID(), 
                    fwSession.getRootPOA().the_POAManager(), policies);

            if (logger.isDebugEnabled()) {
                logger.debug("Created ipAppCallPOA_"
                        + serviceIdentifier.getServiceID());
            }
        }
        catch (UserException e) {
            logger.error("Failed to initialise POAs.", e);
            throw new javax.slee.resource.ResourceException("Failed to initialise POAs.", e);
        }
    }
    
    protected void deactivateIpAppCall() {
        if (ipAppCallImpl != null) {
            try {
            ServantActivationHelper.deactivateServant(ipAppCallImpl);
        
            } catch (UserException e) {
                logger.error(
                        "Failed to deactivate IpAppCall servant.", e);
            }
            
            ipAppCallImpl.dispose();
            
            ipAppCall = null;
            
            ipAppCallImpl = null;
        }
    }
    
    protected void deactivateIpAppCallControlManager() {
        if (ipAppCallControlManagerImpl != null) {
            try {
                ServantActivationHelper.deactivateServant(ipAppCallControlManagerImpl);
            } catch (UserException e) {
                logger.error(
                        "Failed to deactivate IpAppCall servant.", e);
            }
            
            ipAppCallControlManagerImpl.dispose();
            
            ipAppCallControlManagerImpl = null;
            
            ipAppCallControlManager = null;
        }
    }
    
    /**
     * 
     */
    public void destroy() {
        
        ipCallControlManager = null;
        
        ipAppCall = null;
        
        final Collection calls = callMap.values();
        for (final Iterator iter = calls.iterator(); iter.hasNext();) {
            final Call call = (Call) iter.next();
            call.dispose();
        }
        callMap.clear();
        
        deactivateIpAppCall();
        deactivateIpAppCallControlManager();
        
        destroyPOAs();
        
        eventListener = null;
    }
    
    /**
     *  
     */
    private void destroyPOAs() {
        if (ipAppCallPOA != null) {
            POAFactory.destroyPOA(ipAppCallPOA);
            ipAppCallPOA = null;
        }

        if (ipAppCallControlManagerPOA != null) {
            POAFactory.destroyPOA(ipAppCallControlManagerPOA);
            ipAppCallControlManagerPOA = null;
        }
    }
    
    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.activity.callcontrolmanager.CallControlManager#getCall(int)
     */
    public Call getCall(final int callSessionID) {
        return (Call)callMap.get(new Integer(callSessionID));
    }
    
    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.activity.callcontrolmanager.CallControlManager#removeCall(int)
     */
    public Call removeCall(final int callSessionID) {
        return (Call)callMap.remove(new Integer(callSessionID));
    }
    
    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.activity.callcontrolmanager.CallControlManager#addCall(int, org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.activity.call.Call)
     */
    public void addCall(final int callSessionID, final Call call) {
        callMap.put(new Integer(callSessionID), call);
    }
    
 
    /** Utility method for creating a new Call.
     * @see org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.activity.callcontrolmanager.CallControlManager#createCall(org.csapi.cc.gccs.TpCallIdentifier)
     */
    public Call createCall(final TpCallIdentifier corbaTpCallIdentifier) {

        final int callID = ResourceIDFactory.getNextID();

        final org.mobicents.csapi.jr.slee.cc.gccs.TpCallIdentifier callIdentifier = new org.mobicents.csapi.jr.slee.cc.gccs.TpCallIdentifier(
                callID, corbaTpCallIdentifier.CallSessionID);

        final Call call = new CallImpl(this, corbaTpCallIdentifier.CallReference,
                corbaTpCallIdentifier.CallSessionID, 
                callIdentifier, activityManager, eventListener);

        call.init();

        addCall(corbaTpCallIdentifier.CallSessionID, call);
        
        activityManager.add(call.getActivityHandle(), call.getTpCallIdentifier());
        
        activityManager.activityStartedSuspended(call.getActivityHandle());

        return call;
    }
    
    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.activity.callcontrolmanager.CallControlManager#callAborted(int)
     */
    public void callAborted(final int callReference) {
        final IpCallControlManager callControlManager = getIpCallControlManager();

        if (callControlManager != null) {
            CallAbortedEvent event = null;

            final Call call = getCall(callReference);
            if (call != null) {
                event = new CallAbortedEvent(serviceIdentifier, call
                        .getTpCallIdentifier());

                // Sending this as an event on the manager
                // opionally we could also send it as a final event on the call
                eventListener.onCallAbortedEvent(event);

                activityManager.remove(call.getActivityHandle(), call
                        .getTpCallIdentifier());

                activityManager.activityEnding(call.getActivityHandle());

                call.dispose();
            }
        }
    }


    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.activity.callcontrolmanager.CallControlManager#callEventNotify(org.mobicents.csapi.jr.slee.cc.gccs.TpCallIdentifier, org.csapi.cc.gccs.TpCallEventInfo, int)
     */
    public void callEventNotify(final org.mobicents.csapi.jr.slee.cc.gccs.TpCallIdentifier callReference, final TpCallEventInfo eventInfo, final int assignmentID) {
       
        final IpCallControlManager callControlManager = getIpCallControlManager(); 
        
        if (callControlManager != null) {
            
            Call call = getCall(callReference.getCallSessionID());
            if(call != null) {
            
                activityManager.add(call.getActivityHandle(), call.getTpCallIdentifier());
                
                activityManager.activityStarted(activityHandle);
            }
            
            final CallEventNotifyEvent event = new CallEventNotifyEvent(
                    serviceIdentifier, callReference, eventInfo, assignmentID);
        
            eventListener.onCallEventNotifiyEvent(event);
        }
        
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.activity.callcontrolmanager.CallControlManager#callNotificationInterrupted()
     */
    public void callNotificationInterrupted() {
        final IpCallControlManager callControlManager = getIpCallControlManager();

        if (callControlManager != null) {
            final CallNotificationInterruptedEvent event = new CallNotificationInterruptedEvent(serviceIdentifier);

            eventListener.onCallNotificationInterruptedEvent(event);
        }
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.activity.callcontrolmanager.CallControlManager#callNotificationContinued()
     */
    public void callNotificationContinued() {
        final IpCallControlManager callControlManager = getIpCallControlManager();

        if (callControlManager != null) {
            final CallNotificationContinuedEvent event = new CallNotificationContinuedEvent(serviceIdentifier);

            eventListener.onCallNotificationContinuedEvent(event);
        }
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.activity.callcontrolmanager.CallControlManager#callOverloadEncountered(int)
     */
    public void callOverloadEncountered(final int assignmentID) {
        final IpCallControlManager callControlManager = getIpCallControlManager();

        if (callControlManager != null) {
            final CallOverloadEncounteredEvent event = new CallOverloadEncounteredEvent(
                    serviceIdentifier, assignmentID);

            eventListener.onCallOverloadEncounteredEvent(event);
        }
        
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.activity.callcontrolmanager.CallControlManager#callOverloadCeased(int)
     */
    public void callOverloadCeased(final int assignmentID) {
        final IpCallControlManager callControlManager = getIpCallControlManager();

        if (callControlManager != null) {
            final CallOverloadCeasedEvent event = new CallOverloadCeasedEvent(
                    serviceIdentifier, assignmentID);

            eventListener.onCallOverloadCeasedEvent(event);
        }
        
    }

    /* (non-Javadoc)
     * @see org.mobicents.csapi.jr.slee.cc.gccs.IpCallControlManagerConnection#getIpCallConnection(org.mobicents.csapi.jr.slee.cc.gccs.TpCallIdentifier)
     */
    public IpCallConnection getIpCallConnection(final org.mobicents.csapi.jr.slee.cc.gccs.TpCallIdentifier callIdentifier) throws javax.slee.resource.ResourceException {
        final Call call = getCall(callIdentifier.getCallSessionID());
        
        if (call != null) {
            return new IpCallConnectionImpl(call);
        }
        throw new javax.slee.resource.ResourceException(
            "Unrecognized TpCallIdentifier");
    }

    /* (non-Javadoc)
     * @see org.mobicents.csapi.jr.slee.cc.gccs.IpCallControlManagerConnection#createCall()
     */
    public org.mobicents.csapi.jr.slee.cc.gccs.TpCallIdentifier createCall() throws TpCommonExceptions, javax.slee.resource.ResourceException {
        
        org.mobicents.csapi.jr.slee.cc.gccs.TpCallIdentifier result = null;
        
        final IpCallControlManager callControlManager = getIpCallControlManager();
        
        if (callControlManager != null) {
            TpCallIdentifier callReference;
            try {
                callReference = callControlManager.createCall(getIpAppCall());
            
                result = createCall(callReference).getTpCallIdentifier();
            
            } catch (P_INVALID_INTERFACE_TYPE e) {
                logger.error(ParlayExceptionUtil.stringify(e), e);
                throw new javax.slee.resource.ResourceException("Unexpected Parlay exception", e);
            } 
        }
        return result;
    }

    /* (non-Javadoc)
     * @see org.mobicents.csapi.jr.slee.cc.gccs.IpCallControlManagerConnection#enableCallNotification(org.csapi.cc.gccs.TpCallEventCriteria)
     */
    public int enableCallNotification(final TpCallEventCriteria eventCriteria) throws javax.slee.resource.ResourceException, P_INVALID_EVENT_TYPE, TpCommonExceptions, P_INVALID_CRITERIA {
        final IpCallControlManager callControlManager = getIpCallControlManager();
        int result = 0;
        if (callControlManager != null) {
           
            try {
                result = callControlManager.enableCallNotification(getIpAppCallControlManager(), eventCriteria);
            } catch (P_INVALID_INTERFACE_TYPE e) {
                logger.error(ParlayExceptionUtil.stringify(e), e);
                throw new javax.slee.resource.ResourceException("Unexpected Parlay exception", e);
            }
        }
        return result;
    }

    /* (non-Javadoc)
     * @see org.mobicents.csapi.jr.slee.cc.gccs.IpCallControlManagerConnection#disableCallNotification(int)
     */
    public void disableCallNotification(final int assignmentID) throws TpCommonExceptions, P_INVALID_ASSIGNMENT_ID, javax.slee.resource.ResourceException {
        final IpCallControlManager callControlManager = getIpCallControlManager();
        if (callControlManager != null) {
            callControlManager.disableCallNotification(assignmentID);
        }
    }

    /* (non-Javadoc)
     * @see org.mobicents.csapi.jr.slee.cc.gccs.IpCallControlManagerConnection#setCallLoadControl(int, org.csapi.cc.TpCallLoadControlMechanism, org.csapi.cc.gccs.TpCallTreatment, org.csapi.TpAddressRange)
     */
    public int setCallLoadControl(final int duration, final TpCallLoadControlMechanism mechanism, final TpCallTreatment treatment, final TpAddressRange addressRange) throws TpCommonExceptions, P_INVALID_ADDRESS, P_UNSUPPORTED_ADDRESS_PLAN, javax.slee.resource.ResourceException {
        final IpCallControlManager callControlManager = getIpCallControlManager();
        int result = 0;
        if (callControlManager != null) {
            result = callControlManager.setCallLoadControl(duration, mechanism, treatment, addressRange);
        }
        return result;
    }

    /* (non-Javadoc)
     * @see org.mobicents.csapi.jr.slee.cc.gccs.IpCallControlManagerConnection#changeCallNotification(int, org.csapi.cc.gccs.TpCallEventCriteria)
     */
    public void changeCallNotification(final int assignmentID, final TpCallEventCriteria eventCriteria) throws TpCommonExceptions, P_INVALID_ASSIGNMENT_ID, P_INVALID_CRITERIA, P_INVALID_EVENT_TYPE, javax.slee.resource.ResourceException {
        final IpCallControlManager callControlManager = getIpCallControlManager();
        if (callControlManager != null) {
            callControlManager.changeCallNotification(assignmentID, eventCriteria);
        }
        
    }

    /* (non-Javadoc)
     * @see org.mobicents.csapi.jr.slee.cc.gccs.IpCallControlManagerConnection#getCriteria()
     */
    public TpCallEventCriteriaResult[] getCriteria() throws TpCommonExceptions, javax.slee.resource.ResourceException {
        final IpCallControlManager callControlManager = getIpCallControlManager();
        TpCallEventCriteriaResult[] result = null;
        if (callControlManager != null) {
            result = callControlManager.getCriteria();
        }
        return result;
    }

    /* (non-Javadoc)
     * @see org.mobicents.csapi.jr.slee.IpServiceConnection#closeConnection()
     */
    public void closeConnection() throws javax.slee.resource.ResourceException {
//      should never be called
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.activity.callcontrolmanager.CallControlManager#getIpAppCall()
     */
    public IpAppCall getIpAppCall() {
        return ipAppCall;
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.activity.callcontrolmanager.CallControlManager#getTpServiceIdentifier()
     */
    public TpServiceIdentifier getTpServiceIdentifier() {
        return serviceIdentifier;
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.parlay.session.ServiceSession#getType()
     */
    public int getType() {
        return ServiceSession.GenericCallControl;
    }

    /**
     * @return Returns the ipAppCallControlManagerImpl.
     */
    public IpAppCallControlManagerImpl getIpAppCallControlManagerImpl() {
        return ipAppCallControlManagerImpl;
    }
    /**
     * @return Returns the ipAppCallImpl.
     */
    public IpAppCallImpl getIpAppCallImpl() {
        return ipAppCallImpl;
    }
    /**
     * @return Returns the ipCallControlManager.
     */
    public IpCallControlManager getIpCallControlManager() {
        return ipCallControlManager;
    }
    /**
     * @return Returns the ipAppCallControlManager.
     */
    public IpAppCallControlManager getIpAppCallControlManager() {
        return ipAppCallControlManager;
    }

}
