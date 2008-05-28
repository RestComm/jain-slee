package org.mobicents.slee.resource.parlay.csapi.jr.ui.activity.uimanager;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.slee.resource.ActivityHandle;
import javax.slee.resource.ResourceException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csapi.P_INVALID_ASSIGNMENT_ID;
import org.csapi.P_INVALID_CRITERIA;
import org.csapi.P_INVALID_INTERFACE_TYPE;
import org.csapi.P_INVALID_NETWORK_STATE;
import org.csapi.TpAddress;
import org.csapi.TpCommonExceptions;
import org.csapi.ui.IpAppUI;
import org.csapi.ui.IpAppUICall;
import org.csapi.ui.IpAppUICallHelper;
import org.csapi.ui.IpAppUIHelper;
import org.csapi.ui.IpAppUIManager;
import org.csapi.ui.IpAppUIManagerHelper;
import org.csapi.ui.IpUIManager;
import org.csapi.ui.TpUICallIdentifier;
import org.csapi.ui.TpUIEventCriteria;
import org.csapi.ui.TpUIEventCriteriaResult;
import org.csapi.ui.TpUIEventInfo;
import org.csapi.ui.TpUIEventNotificationInfo;
import org.csapi.ui.TpUIIdentifier;
import org.mobicents.csapi.jr.slee.InvalidUnionAccessorException;
import org.mobicents.csapi.jr.slee.TpServiceIdentifier;
import org.mobicents.csapi.jr.slee.ui.CallLegUITarget;
import org.mobicents.csapi.jr.slee.ui.CallUITarget;
import org.mobicents.csapi.jr.slee.ui.IpUICallConnection;
import org.mobicents.csapi.jr.slee.ui.IpUIConnection;
import org.mobicents.csapi.jr.slee.ui.MultiPartyCallUITarget;
import org.mobicents.csapi.jr.slee.ui.ReportEventNotificationEvent;
import org.mobicents.csapi.jr.slee.ui.ReportNotificationEvent;
import org.mobicents.csapi.jr.slee.ui.TpUITargetObject;
import org.mobicents.csapi.jr.slee.ui.UserInteractionAbortedEvent;
import org.mobicents.csapi.jr.slee.ui.UserInteractionNotificationContinuedEvent;
import org.mobicents.csapi.jr.slee.ui.UserInteractionNotificationInterruptedEvent;
import org.mobicents.slee.resource.parlay.csapi.jr.ParlayServiceActivityHandle;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.activity.call.Call;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.activity.callcontrolmanager.CallControlManager;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.callleg.CallLeg;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.multipartycall.MultiPartyCall;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.multipartycallcontrolmanager.MultiPartyCallControlManager;
import org.mobicents.slee.resource.parlay.csapi.jr.ui.IpAppUICallImpl;
import org.mobicents.slee.resource.parlay.csapi.jr.ui.IpAppUIImpl;
import org.mobicents.slee.resource.parlay.csapi.jr.ui.IpAppUIManagerImpl;
import org.mobicents.slee.resource.parlay.csapi.jr.ui.IpUICallConnectionImpl;
import org.mobicents.slee.resource.parlay.csapi.jr.ui.IpUIConnectionImpl;
import org.mobicents.slee.resource.parlay.csapi.jr.ui.UiListener;
import org.mobicents.slee.resource.parlay.csapi.jr.ui.activity.ui.AbstractUI;
import org.mobicents.slee.resource.parlay.csapi.jr.ui.activity.ui.UIGeneric;
import org.mobicents.slee.resource.parlay.csapi.jr.ui.activity.ui.UIGenericImpl;
import org.mobicents.slee.resource.parlay.csapi.jr.ui.activity.uicall.UICall;
import org.mobicents.slee.resource.parlay.csapi.jr.ui.activity.uicall.UICallImpl;
import org.mobicents.slee.resource.parlay.fw.FwSession;
import org.mobicents.slee.resource.parlay.session.ParlaySession;
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
 */
public class UIManagerImpl implements UIManager {
    /**
     * Log4J Logger for this class.
     */
    private static final Log logger = LogFactory.getLog(UIManagerImpl.class);

    private static final String CALLBACK_FAILED = "Failed to set manager uiback.";

    private final transient  Map abstractUiMap;

    private final transient  TpServiceIdentifier serviceIdentifier;

    private final transient Executor ipAppUIManagerExecutor;

    private final transient Executor[] ipAppUIExecutors;

    private transient FwSession fwSession;

    private transient ActivityManager activityManager;

    private transient ActivityHandle activityHandle;

    private transient UiListener eventListener;

    private transient POA ipAppUIManagerPOA;

    private transient POA ipAppUIPOA;

    private transient POA ipAppUICallPOA;

    private transient IpAppUIManagerImpl ipAppUIManagerImpl;

    private transient IpAppUIImpl ipAppUIImpl;

    private transient IpAppUICallImpl ipAppUICallImpl;

    private transient IpAppUIManager ipAppUIManager;

    private transient IpAppUI ipAppUI;

    private transient IpAppUICall ipAppUICall;

    private transient IpUIManager ipUIManager;

    private final transient ThreadFactory threadFactory;

    private ParlaySession parlaySession;

    private static final int NUM_EXECUTORS = 20;
    
    private static final int THREAD_POOL_SIZE = 20;

    /**
     * @param parlaySession
     *            needed so that we can get access to GCCS or MPCC manager
     *            activities for translating UI target objects in Call Related U
     * @param fwSession
     * @param ipUIManager
     * @param eventListener
     * @param activityManager
     * @param serviceIdentifier
     */
    public UIManagerImpl(final ParlaySession parlaySession, final FwSession fwSession, final IpUIManager ipUIManager,
            final UiListener eventListener, final ActivityManager activityManager,
            final TpServiceIdentifier serviceIdentifier) {

        this.parlaySession = parlaySession;
        this.fwSession = fwSession;
        this.ipUIManager = ipUIManager;
        this.eventListener = eventListener;
        this.activityManager = activityManager;
        this.serviceIdentifier = serviceIdentifier;
        

        abstractUiMap = new HashMap();


        if (logger.isDebugEnabled()) {
            // TODO configurable
            logger.debug("Initialising threadpool with size "
                    + THREAD_POOL_SIZE);
        }
        ipAppUIManagerExecutor = new PooledExecutor(THREAD_POOL_SIZE);

        // Create own factory to name threads
        threadFactory = new ThreadFactory() {
            private ThreadGroup threadGroup = new ThreadGroup("UI-ThreadGroup");

            private final  SynchronizedInt i = new SynchronizedInt(0);

            public Thread newThread(final Runnable runnable) {
                return new Thread(threadGroup, runnable, "UI-Thread-"
                        + i.increment());
            }
        };
        ((PooledExecutor) ipAppUIManagerExecutor).setThreadFactory(threadFactory);
        ((PooledExecutor) ipAppUIManagerExecutor).setKeepAliveTime(-1);

        ipAppUIExecutors = new Executor[NUM_EXECUTORS];
        for (int i = 0; i < NUM_EXECUTORS; i++) {
            ipAppUIExecutors[i] = new QueuedExecutor();

            ((QueuedExecutor) ipAppUIExecutors[i])
                    .setThreadFactory(threadFactory);
        }

        activityHandle = new ParlayServiceActivityHandle(this.serviceIdentifier);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.session.ServiceSession#init()
     */
    public void init() throws javax.slee.resource.ResourceException {

        Policy[] policies = PolicyFactory.createTransientPoaPolicies(fwSession
                .getRootPOA());

        synchronized (this) {

            createPOAs(policies);

            if (logger.isDebugEnabled()) {
                logger.debug("Activating servants.");
            }

            activateIpAppUI();
            activateIpAppUICall();
            activateIpAppUIManager();

            if (logger.isDebugEnabled()) {
                logger.debug("Invoking setCallback on ipUIManager ...");
            }

            try {
                ipUIManager.setCallback(ipAppUIManager);
            } catch (P_INVALID_INTERFACE_TYPE e) {
                logger.error(
                        CALLBACK_FAILED + ParlayExceptionUtil.stringify(e), e);
                throw new javax.slee.resource.ResourceException(
                        CALLBACK_FAILED, e);
            } catch (TpCommonExceptions e) {
                logger.error(CALLBACK_FAILED, e);
                throw new javax.slee.resource.ResourceException(
                        CALLBACK_FAILED, e);
            }

            activityManager.add(activityHandle, serviceIdentifier);

            activityManager.activityStartedSuspended(activityHandle);
        }
    }

    /**
     * @throws javax.slee.resource.ResourceException
     */
    protected void activateIpAppUI()
            throws javax.slee.resource.ResourceException {

        ipAppUIImpl = new IpAppUIImpl(this, ipAppUIManagerPOA, ipAppUIExecutors);        
        try {

            org.omg.CORBA.Object tmpRef = ServantActivationHelper
                    .activateServant(ipAppUIPOA, ipAppUIImpl);

            if (logger.isDebugEnabled()) {
                logger.debug("Activated ipAppUI.");
            }

            ipAppUI = IpAppUIHelper.narrow(tmpRef);            

        } catch (UserException e) {
            logger.error("Failed to activate ipAppUI.", e);
            throw new javax.slee.resource.ResourceException(
                    "Failed to activate ipAppUI.", e);
        }
    }

    /**
     * @throws javax.slee.resource.ResourceException
     */
    protected void activateIpAppUICall()
            throws javax.slee.resource.ResourceException {

        ipAppUICallImpl = new IpAppUICallImpl(this, ipAppUIManagerPOA,
                ipAppUIExecutors);

        try {

            org.omg.CORBA.Object tmpRef = ServantActivationHelper
                    .activateServant(ipAppUICallPOA, ipAppUICallImpl);

            if (logger.isDebugEnabled()) {
                logger.debug("Activated ipAppUICall.");
            }

            ipAppUICall = IpAppUICallHelper.narrow(tmpRef);

        } catch (UserException e) {
            logger.error("Failed to activate ipAppUICall.", e);
            throw new javax.slee.resource.ResourceException(
                    "Failed to activate ipAppUICall.", e);
        }
    }

    /**
     * @throws javax.slee.resource.ResourceException
     */
    protected void activateIpAppUIManager()
            throws javax.slee.resource.ResourceException {

        ipAppUIManagerImpl = new IpAppUIManagerImpl(this, ipAppUIManagerPOA, ipAppUIManagerExecutor);



        try {

            org.omg.CORBA.Object tmpRef = ServantActivationHelper
                    .activateServant(ipAppUIManagerPOA, ipAppUIManagerImpl);

            if (logger.isDebugEnabled()) {
                logger.debug("Activated ipAppUIManager.");
            }

            ipAppUIManager = IpAppUIManagerHelper.narrow(tmpRef);
        } catch (UserException e) {
            logger.error("Failed to activate ipAppUIManager.", e);
            throw new javax.slee.resource.ResourceException(
                    "Failed to activate ipAppUIManager.", e);
        }
    }

    /**
     * @param policies
     * @throws javax.slee.resource.ResourceException
     */
    protected void createPOAs(final  Policy[] policies)
            throws javax.slee.resource.ResourceException {
        try {
            if (logger.isDebugEnabled()) {
                logger.debug("Creating UI POAs.");
            }

            ipAppUIManagerPOA = POAFactory.createPOA(fwSession.getRootPOA(),
                    "ipAppUIManagerPOA_" + serviceIdentifier.getServiceID(),
                    fwSession.getRootPOA().the_POAManager(), policies);

            if (logger.isDebugEnabled()) {
                logger.debug("Created ipAppUIManagerPOA_"
                        + serviceIdentifier.getServiceID());
            }

            ipAppUICallPOA = POAFactory.createPOA(fwSession.getRootPOA(),
                    "ipAppUIPOA_" + serviceIdentifier.getServiceID(), fwSession
                            .getRootPOA().the_POAManager(), policies);

            if (logger.isDebugEnabled()) {
                logger.debug("Created ipAppUIPOA_"
                        + serviceIdentifier.getServiceID());
            }

            ipAppUIPOA = POAFactory.createPOA(fwSession.getRootPOA(),
                    "ipAppUICallPOA_" + serviceIdentifier.getServiceID(),
                    fwSession.getRootPOA().the_POAManager(), policies);

            if (logger.isDebugEnabled()) {
                logger.debug("Created ipAppUICallPOA_"
                        + serviceIdentifier.getServiceID());
            }

        } catch (UserException e) {
            logger.error("Failed to initialise POAs.", e);
            throw new javax.slee.resource.ResourceException(
                    "Failed to initialise POAs.", e);
        }
    }

    protected void deactivateIpAppUI() {
        if (ipAppUIImpl != null) {
            try {
                ServantActivationHelper.deactivateServant(ipAppUIImpl);

            } catch (UserException e) {
                logger.error("Failed to deactivate IpAppUI servant.", e);
            }

            ipAppUIImpl.dispose();

            ipAppUI = null;

            ipAppUIImpl = null;
        }
    }

    protected void deactivateIpAppUICall() {
        if (ipAppUICallImpl != null) {
            try {
                ServantActivationHelper.deactivateServant(ipAppUICallImpl);

            } catch (UserException e) {
                logger
                        .error("Failed to deactivate ipAppUICallImpl servant.",
                                e);
            }

            ipAppUICallImpl.dispose();

            ipAppUICall = null;
            ipAppUICallImpl = null;
        }

    }

    protected void deactivateIpAppUIManager() {
        if (ipAppUIManagerImpl != null) {
            try {
                ServantActivationHelper.deactivateServant(ipAppUIManagerImpl);
            } catch (UserException e) {
                logger.error("Failed to deactivate IpAppUI servant.", e);
            }

            ipAppUIManagerImpl.dispose();

            ipAppUIManagerImpl = null;

            ipAppUIManager = null;
        }
    }

    /**
     * 
     */
    public void destroy() {

        ipUIManager = null;

        ipAppUI = null;

        ipAppUICall = null;

        Collection uis = abstractUiMap.values();
        for (Iterator iter = uis.iterator(); iter.hasNext();) {
            AbstractUI ui = (AbstractUI) iter.next();
            ui.dispose();
        }
        abstractUiMap.clear();
        deactivateIpAppUI();
        deactivateIpAppUICall();
        deactivateIpAppUIManager();
        destroyPOAs();

        eventListener = null;
    }

    /**
     * 
     */
    private void destroyPOAs() {
        if (ipAppUICallPOA != null) {
            POAFactory.destroyPOA(ipAppUICallPOA);
            ipAppUICallPOA = null;
        }
        if (ipAppUIPOA != null) {
            POAFactory.destroyPOA(ipAppUIPOA);
            ipAppUIPOA = null;
        }

        if (ipAppUIManagerPOA != null) {
            POAFactory.destroyPOA(ipAppUIManagerPOA);
            ipAppUIManagerPOA = null;
        }
    }

    public AbstractUI getAbstractUI(final int uiSessionID) {
        return   (AbstractUI) abstractUiMap.get(new Integer(uiSessionID));
    }
 
    public UIGeneric getUIGeneric(final int uiSessionID) {
        return  (UIGeneric) getAbstractUI(uiSessionID);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.csapi.jr.ui.activity.uimanager.UIManager#getUICall(int)
     */
    public UICall getUICall(final int uiSessionID) {
        return  (UICall) getAbstractUI(uiSessionID);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.csapi.jr.ui.activity.uimanager.UIManager#removeUI(int)
     */
    public AbstractUI removeUI(final int uiSessionID) {        
        return (AbstractUI) abstractUiMap.remove(new Integer(uiSessionID));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.csapi.jr.ui.activity.uimanager.UIManager#addUI(int,
     *      org.mobicents.slee.resource.parlay.csapi.jr.ui.activity.ui.AbstractUI)
     */
    public void addUI(final int uiSessionID, final AbstractUI ui) {
        abstractUiMap.put(new Integer(uiSessionID), ui);
    }

    /*
     * Utility method for creating an new UIGeneric
     * @see org.mobicents.slee.resource.parlay.csapi.jr.ui.activity.uimanager.UIManager#createUIGeneric(org.csapi.ui.TpUIIdentifier)
     */
    public UIGeneric createUIGeneric(final TpUIIdentifier corbaTpUIIdentifier) {
         
        int sleeUiID = ResourceIDFactory.getNextID();

        org.mobicents.csapi.jr.slee.ui.TpUIIdentifier sleeTpUIIdentifier = new org.mobicents.csapi.jr.slee.ui.TpUIIdentifier(
                sleeUiID, corbaTpUIIdentifier.UserInteractionSessionID);

        UIGeneric ui = new UIGenericImpl(this, corbaTpUIIdentifier.UIRef,
                corbaTpUIIdentifier.UserInteractionSessionID,
                sleeTpUIIdentifier, activityManager, eventListener);

        ui.init();

        addUI(corbaTpUIIdentifier.UserInteractionSessionID, ui);

        return ui;
    }

    /*
     * Utility method for creating an new UICall
     * 
     * @see org.mobicents.slee.resource.parlay.csapi.jr.ui.activity.uimanager.UIManager#createUI(org.csapi.ui.TpUIIdentifier)
     */

    public UICall createUICall(final TpUICallIdentifier corbaTpUIIdentifier) {

        int sleeUiID = ResourceIDFactory.getNextID();

        org.mobicents.csapi.jr.slee.ui.TpUICallIdentifier sleeTpUICallIdentifier = new org.mobicents.csapi.jr.slee.ui.TpUICallIdentifier(
                sleeUiID, corbaTpUIIdentifier.UserInteractionSessionID);

        UICall ui = new UICallImpl(this, corbaTpUIIdentifier.UICallRef,
                corbaTpUIIdentifier.UserInteractionSessionID,
                sleeTpUICallIdentifier, activityManager, eventListener);

        ui.init();

        addUI(corbaTpUIIdentifier.UserInteractionSessionID, ui);

        return ui;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.csapi.jr.slee.ui.IpUIManagerConnection#getIpUIConnection(org.mobicents.csapi.jr.slee.ui.TpUIIdentifier)
     */
    public IpUIConnection getIpUIConnection(
            final org.mobicents.csapi.jr.slee.ui.TpUIIdentifier uiIdentifier)
            throws javax.slee.resource.ResourceException {
        UIGeneric ui =  getUIGeneric(uiIdentifier
                .getUserInteractionSessionID());  

        if (ui != null) {
            return new IpUIConnectionImpl(ui);
        }
        throw new javax.slee.resource.ResourceException(
                "Unrecognized TpUIIdentifier");
    }

    public IpUICallConnection getIpUICallConnection(
            final org.mobicents.csapi.jr.slee.ui.TpUICallIdentifier uICallIdentifier)
            throws ResourceException {
        UICall ui = getUICall(uICallIdentifier.getUserInteractionSessionID());  

        if (ui != null) {
            return new IpUICallConnectionImpl(ui);
        }
        throw new javax.slee.resource.ResourceException(
                "Unrecognized TpUIIdentifier");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.csapi.jr.slee.IpServiceConnection#closeConnection()
     */
    public void closeConnection() throws javax.slee.resource.ResourceException {
        // TODO this method will be removed when  we delete closeConnection() method from the org.mobicents.csapi.jr.slee.IpServiceConnection in teh JR SLEE API.
        // will be ignored
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.csapi.jr.ui.activity.uimanager.UIManager#getIpAppUI()
     */
    public IpAppUI getIpAppUI() {
        return ipAppUI;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.csapi.jr.ui.activity.uimanager.UIManager#getIpAppUICall()
     */
    public IpAppUICall getIpAppUICall() {
        return ipAppUICall;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.csapi.jr.ui.activity.uicontrolmanager.UIManager#getTpServiceIdentifier()
     */
    public TpServiceIdentifier getTpServiceIdentifier() {
        return serviceIdentifier;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.session.ServiceSession#getType()
     */
    public int getType() {
        return ServiceSession.UserInteraction;
    }

    /**
     * @return Returns the ipAppUIImpl.
     */
    public IpAppUIImpl getIpAppUIImpl() {
        return ipAppUIImpl;
    }

    /**
     * @return Returns the ipUIManager.
     */
    public IpUIManager getIpUIManager() {
        return ipUIManager;
    }

    /**
     * @return Returns the ipAppUIManager.
     */
    IpAppUIManager getIpAppUIManager() {
        return ipAppUIManager;
    }

    public void userInteractionAborted(
            final org.csapi.ui.TpUIIdentifier userInteraction) {
        IpUIManager uiManager = getIpUIManager();
        
        if (uiManager != null) {
            UserInteractionAbortedEvent event = null;
                        
            AbstractUI uiActivity = getAbstractUI(userInteraction.UserInteractionSessionID);

            if (uiActivity != null) {
                // The Parlay callback method has a TpUIIdentifier parameter which 
                // in the CallRelated UI scenario must be interpreted as a TpUICallIdentifier (even though
                // TpUICallIdentifier is not a subclass of TpUIIdentifier, the CORBA references therein 
                // use inheritance.)
                if (uiActivity instanceof UIGeneric) {
                    UIGeneric uiGeneric = (UIGeneric) uiActivity;

                    event = new UserInteractionAbortedEvent(serviceIdentifier,
                            uiGeneric.getTpUIIdentifier());

                    eventListener.onUserInteractionAbortedEvent(event);

                    activityManager.remove(uiActivity.getActivityHandle(),
                            uiGeneric.getTpUIIdentifier());

                } else if (uiActivity instanceof UICall) {
                    UICall uiCall = (UICall) uiActivity;

                    event = new UserInteractionAbortedEvent(serviceIdentifier,
                            uiCall.getTpUICallIdentifier());

                    eventListener.onUserInteractionAbortedEvent(event);

                    activityManager.remove(uiActivity.getActivityHandle(),
                            uiCall.getTpUICallIdentifier());
                }

                activityManager.activityEnding(uiActivity.getActivityHandle());
                uiActivity.dispose();
            }
            
        }

    }

    public void reportNotification(
            final org.mobicents.csapi.jr.slee.ui.TpUIIdentifier userInteraction,
            final TpUIEventInfo eventInfo, final int assignmentID) {

        if (getIpUIManager() != null) {
            
            UIGeneric ui = getUIGeneric(userInteraction.getUserInteractionSessionID());
            if(ui != null) {

                activityManager.add(ui.getActivityHandle(), ui.getTpUIIdentifier());

                activityManager.activityStarted(ui.getActivityHandle());
            }
            else {
                UICall uiCall = getUICall(userInteraction.getUserInteractionSessionID());
                if(uiCall != null) {

                    activityManager.add(uiCall.getActivityHandle(), uiCall.getTpUICallIdentifier());

                    activityManager.activityStarted(uiCall.getActivityHandle());
                }
            }
            ReportNotificationEvent event = new ReportNotificationEvent(
                    serviceIdentifier, userInteraction, eventInfo, assignmentID);
            // Notify app
            eventListener.onReportNotificationEvent(event);
        }

    }

    public void userInteractionNotificationInterrupted() {
        IpUIManager uiManager = getIpUIManager();
        if (uiManager != null) {
            UserInteractionNotificationInterruptedEvent event = new UserInteractionNotificationInterruptedEvent(
                    serviceIdentifier);
            // Notify app
            eventListener.onUserInteractionNotificationInterruptedEvent(event);
        }

    }

    public void userInteractionNotificationContinued() {

        if (getIpUIManager() != null) {
            UserInteractionNotificationContinuedEvent event = new UserInteractionNotificationContinuedEvent(
                    serviceIdentifier);
            // Notify app
            eventListener.onUserInteractionNotificationContinuedEvent(event);
        }

    }

    public void reportEventNotification(
            final org.mobicents.csapi.jr.slee.ui.TpUIIdentifier userInteraction,
            final TpUIEventNotificationInfo eventNotificationInfo, final int assignmentID) {
        if (getIpUIManager() != null) {
            
            UIGeneric ui = getUIGeneric(userInteraction.getUserInteractionSessionID());
            if(ui != null) {

                activityManager.add(ui.getActivityHandle(), ui.getTpUIIdentifier());

                activityManager.activityStarted(ui.getActivityHandle());
            }
            else {
                UICall uiCall = getUICall(userInteraction.getUserInteractionSessionID());
                if(uiCall != null) {

                    activityManager.add(uiCall.getActivityHandle(), uiCall.getTpUICallIdentifier());

                    activityManager.activityStarted(uiCall.getActivityHandle());
                }
            }
            
            ReportEventNotificationEvent event = new ReportEventNotificationEvent(
                    serviceIdentifier, userInteraction, eventNotificationInfo,
                    assignmentID);
            // Notify app
            eventListener.onReportEventNotificationEvent(event);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.csapi.jr.slee.ui.IpUIManagerConnection#createUI(org.csapi.TpAddress)
     */

    public org.mobicents.csapi.jr.slee.ui.TpUIIdentifier createUI(
            final TpAddress userAddress) throws TpCommonExceptions,
            P_INVALID_NETWORK_STATE, ResourceException {

        org.mobicents.csapi.jr.slee.ui.TpUIIdentifier result;

        IpUIManager uiManager = getIpUIManager();

        if (uiManager != null) {
            TpUIIdentifier corbaUiReference;
            try {
                corbaUiReference = uiManager.createUI(getIpAppUI(), userAddress);

                UIGeneric generic = createUIGeneric(corbaUiReference);
                
                result = generic.getTpUIIdentifier();

                activityManager.add(generic.getActivityHandle(), generic.getTpUIIdentifier());

                activityManager.activityStartedSuspended(generic.getActivityHandle());

            } catch (P_INVALID_INTERFACE_TYPE e) {
                logger.error(ParlayExceptionUtil.stringify(e), e);
                throw new ResourceException("Unexpected Parlay Exception", e);
            }
        } else {
            result = null;
        }
        return result;
    }

    /* (non-Javadoc)
     * @see org.mobicents.csapi.jr.slee.ui.IpUIManagerConnection#createUICall(org.mobicents.csapi.jr.slee.ui.TpUITargetObject)
     */
    public org.mobicents.csapi.jr.slee.ui.TpUICallIdentifier createUICall(
            final TpUITargetObject sleeUiTargetObject) throws TpCommonExceptions,
            P_INVALID_NETWORK_STATE, ResourceException {
        org.mobicents.csapi.jr.slee.ui.TpUICallIdentifier result = null;

        if (sleeUiTargetObject != null) {  
            
            IpUIManager uiManager = getIpUIManager();
            
            if (uiManager != null) {
                TpUICallIdentifier corbaUiReference;
                try {
                    corbaUiReference = uiManager.createUICall(getIpAppUICall(),
                            getCorbaUITargetObject(sleeUiTargetObject));
                    
                    UICall ui = createUICall(corbaUiReference);
                    
                    result = ui.getTpUICallIdentifier();

                    activityManager.add(ui.getActivityHandle(), ui.getTpUICallIdentifier());

                    activityManager.activityStartedSuspended(ui.getActivityHandle());
                    
                } catch (P_INVALID_INTERFACE_TYPE e) {
                    logger.error(ParlayExceptionUtil.stringify(e), e);
                    throw new ResourceException("Unexpected Parlay Exception", e);
                }
            }
        } else {
            logger.error("Null TpUITargetObject not allowed.");
            throw new ResourceException("Null TpUITargetObject not allowed.");
        }
        return result;
    }

    public int createNotification(final TpUIEventCriteria eventCriteria)
            throws TpCommonExceptions, P_INVALID_CRITERIA, ResourceException {
        IpUIManager uiManager = getIpUIManager();
        int result = 0;
        if (uiManager != null) {

            try {
                result = uiManager.createNotification(getIpAppUIManager(),
                        eventCriteria);
            } catch (P_INVALID_INTERFACE_TYPE e) {
                logger.error(ParlayExceptionUtil.stringify(e), e);
                throw new javax.slee.resource.ResourceException(
                        "Unexpected Parlay exception", e);
            }
        }
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.csapi.jr.slee.ui.IpUIManagerConnection#destroyNotification(int)
     */
    public void destroyNotification(final int assignmentID)
            throws TpCommonExceptions, P_INVALID_ASSIGNMENT_ID,
            ResourceException {
        IpUIManager uiManager = getIpUIManager();

        if (uiManager != null) {
            uiManager.destroyNotification(assignmentID);

        }

    }

    public void changeNotification(final int assignmentID,
            final TpUIEventCriteria eventCriteria) throws TpCommonExceptions,
            P_INVALID_ASSIGNMENT_ID, P_INVALID_CRITERIA, ResourceException {
        IpUIManager uiManager = getIpUIManager();

        if (uiManager != null) {
            uiManager.changeNotification(assignmentID, eventCriteria);

        }

    }

    public TpUIEventCriteriaResult[] getNotification()
            throws TpCommonExceptions, ResourceException {
        IpUIManager uiManager = getIpUIManager();
        TpUIEventCriteriaResult[] result = null;
        if (uiManager != null) {

            result = uiManager.getNotification();

        }
        return result;
    }

    public int enableNotifications() throws TpCommonExceptions,
            ResourceException {

        IpUIManager uiManager = getIpUIManager();
        int result = 0;
        if (uiManager != null) {

            result = uiManager.enableNotifications(getIpAppUIManager());

        }
        return result;
    }

    public void disableNotifications() throws TpCommonExceptions,
            ResourceException {
        IpUIManager uiManager = getIpUIManager();

        if (uiManager != null) {

            uiManager.disableNotifications();

        }

    }
    
     
     org.csapi.ui.TpUITargetObject getCorbaUITargetObject(final TpUITargetObject sleeTpUITargetObject) throws ResourceException 
     {

        org.csapi.ui.TpUITargetObject result = new org.csapi.ui.TpUITargetObject();
        try {
            

            if (sleeTpUITargetObject.getDiscriminator() == org.mobicents.csapi.jr.slee.ui.TpUITargetObjectType.P_UI_TARGET_OBJECT_CALL) {

                CallUITarget target = sleeTpUITargetObject.getCallUITarget();
                ServiceSession sleeServiceSession = parlaySession.getServiceSession(target.getTpServiceIdentifier());
                CallControlManager managerActivity = (CallControlManager) sleeServiceSession;

                Call callActivity = managerActivity.getCall(target.getTpCallIdentifier().getCallSessionID());

                result.Call(callActivity.getParlayTpCallIdentifier());

            } else if (sleeTpUITargetObject.getDiscriminator() == org.mobicents.csapi.jr.slee.ui.TpUITargetObjectType.P_UI_TARGET_OBJECT_CALL_LEG) {
                CallLegUITarget target = sleeTpUITargetObject.getCallLegUITarget();
                ServiceSession sleeServiceSession = parlaySession.getServiceSession(target.getTpServiceIdentifier());
                MultiPartyCallControlManager managerActivity = (MultiPartyCallControlManager) sleeServiceSession;
                
                MultiPartyCall multiPartyCallActivity = managerActivity
                        .getMultiPartyCall(target.getTpMultiPartyCallIdentifier().getCallSessionID());

                CallLeg callLegActivity =  multiPartyCallActivity.getCallLeg(target.getTpCallLegIdentifier().getCallLegSessionID());
                result.CallLeg(callLegActivity.getParlayTpCallLegIdentifier());
                

            } else if (sleeTpUITargetObject.getDiscriminator() == org.mobicents.csapi.jr.slee.ui.TpUITargetObjectType.P_UI_TARGET_OBJECT_MULTI_PARTY_CALL) {

                MultiPartyCallUITarget target = sleeTpUITargetObject.getMultiPartyCallUITarget();
                ServiceSession sleeServiceSession = parlaySession.getServiceSession(target.getTpServiceIdentifier());
                MultiPartyCallControlManager managerActivity = (MultiPartyCallControlManager) sleeServiceSession;

                MultiPartyCall multiPartyCallActivity = managerActivity
                        .getMultiPartyCall(target.getTpMultiPartyCallIdentifier().getCallSessionID());

                result.MultiPartyCall(multiPartyCallActivity
                        .getParlayTpMultiPartyCallIdentifier());

            }
        } catch (InvalidUnionAccessorException e) {

            throw new javax.slee.resource.ResourceException(
                    "Could not translate to a CORBA org.csapi.ui.TpUITargetObject", e);
        }  catch (RuntimeException e) {
            
            throw new javax.slee.resource.ResourceException(
                    "Could not translate to a CORBA org.csapi.ui.TpUITargetObject", e);
        }

        return result;
    }
     
    

}
