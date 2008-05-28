package org.mobicents.slee.resource.parlay.session;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.slee.resource.ResourceException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csapi.cc.gccs.IpCallControlManager;
import org.csapi.cc.gccs.IpCallControlManagerHelper;
import org.csapi.cc.mpccs.IpMultiPartyCallControlManager;
import org.csapi.cc.mpccs.IpMultiPartyCallControlManagerHelper;
import org.csapi.fw.TpProperty;
import org.csapi.ui.IpUIManager;
import org.csapi.ui.IpUIManagerHelper;
import org.mobicents.csapi.jr.slee.TpServiceIdentifier;
import org.mobicents.csapi.jr.slee.fw.TerminateAccessEvent;
import org.mobicents.csapi.jr.slee.fw.TerminateServiceAgreementEvent;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.GccsListener;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.activity.callcontrolmanager.CallControlManagerImpl;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.MpccsListener;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.multipartycallcontrolmanager.MultiPartyCallControlManagerImpl;
import org.mobicents.slee.resource.parlay.csapi.jr.ui.UiListener;
import org.mobicents.slee.resource.parlay.csapi.jr.ui.activity.uimanager.UIManagerImpl;
import org.mobicents.slee.resource.parlay.fw.FwSession;
import org.mobicents.slee.resource.parlay.fw.FwSessionException;
import org.mobicents.slee.resource.parlay.fw.FwSessionFactory;
import org.mobicents.slee.resource.parlay.fw.FwSessionListener;
import org.mobicents.slee.resource.parlay.fw.FwSessionProperties;
import org.mobicents.slee.resource.parlay.fw.ServiceAndToken;
import org.mobicents.slee.resource.parlay.util.activity.ActivityManager;
import org.omg.CORBA.SystemException;

/**
 * Manages all Parlay Session state. Will contain maps to each of the selected
 * services and the related FwSession.
 *  
 */
public class ParlaySessionImpl implements ParlaySession, FwSessionListener {
    private static final String P_GENERIC_CALL_CONTROL = "P_GENERIC_CALL_CONTROL";

    private static final String P_MULTI_PARTY_CALL_CONTROL = "P_MULTI_PARTY_CALL_CONTROL";
    
    private static final String P_USER_INTERACTION = "P_USER_INTERACTION";

    private static final String FAILED_TO_CREATE_SERVICE_SESSION = "Failed to createServiceSession";

    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory
            .getLog(ParlaySessionImpl.class);

    public ParlaySessionImpl(FwSessionProperties fwProperties,
            ActivityManager activityManager) {

        this.fwSessionProperties = fwProperties;
        this.activityManager = activityManager;

        serviceIdentifierToServiceSessionMap = new HashMap();
        propertiesToServiceIdentifierMap = new HashMap();
    }

    protected transient FwSession fwSession = null;

    private transient FwSessionProperties fwSessionProperties = null;

    private transient Map serviceIdentifierToServiceSessionMap;

    private transient Map propertiesToServiceIdentifierMap;

    private transient ActivityManager activityManager;

    private transient MpccsListener mpccsListener;

    private transient GccsListener gccsListener;
    
    private transient UiListener uiListener;

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.session.ParlaySession#init()
     */
    public void init() throws FwSessionException {

        if (logger.isDebugEnabled()) {
            logger.debug("Init Parlay Session");
        }

        fwSession = FwSessionFactory.createFwSession(fwSessionProperties);
        fwSession.addFwSessionListener(this);
        try {
            fwSession.init();
        } catch (FwSessionException e) {
            fwSession.removeFwSessionListener(this);
            fwSession.shutdown();
            throw e;
        }

        try {
            fwSession.authenticate();
        } catch (FwSessionException e) {
            fwSession.removeFwSessionListener(this);
            fwSession.shutdown();
            throw e;
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.session.ParlaySession#destroy()
     */
    public synchronized void destroy() {
        if (logger.isDebugEnabled()) {
            logger.debug("Destroying Parlay Session");
        }

        if (fwSession != null) {
            fwSession.removeFwSessionListener(this);

            endAccess();

            fwSession.shutdown();
        }

        propertiesToServiceIdentifierMap.clear();

        serviceIdentifierToServiceSessionMap.clear();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.session.ParlaySession#getService(java.lang.String,
     *      java.util.Properties)
     */
    public TpServiceIdentifier getService(String serviceTypeName,
            Properties serviceProperties) throws FwSessionException,
            ResourceException {
        TpServiceIdentifier result = null;

        ServiceProperties properties = ServiceProperties
                .load(serviceProperties);
        if (logger.isDebugEnabled()) {
            logger.debug("handling getService()");
            logger.debug(properties);
        }

        // synch around map lookup/put
        synchronized (this) {

            if (propertiesToServiceIdentifierMap.get(properties) != null) {
                result = (TpServiceIdentifier) propertiesToServiceIdentifierMap
                        .get(properties);
                if (logger.isDebugEnabled()) {
                    logger.debug("Found a matching service identifier");
                }
            }
            else {
                if (logger.isDebugEnabled()) {
                    logger
                            .debug("No matching service found. Selecting new service instance.");
                }
                try {
                    ServiceAndToken serviceAndToken = fwSession.getService(
                            serviceTypeName, properties.getServiceProperties());

                    String ior = fwSession.getORB().object_to_string(
                            serviceAndToken.getIpService());
                    if (logger.isDebugEnabled()) {
                        logger.debug("IpService IOR = " + ior);
                    }

                    result = new TpServiceIdentifier(serviceAndToken
                            .getServiceToken().hashCode());

                    ServiceSession serviceSession = createServiceSession(serviceTypeName,
                            serviceAndToken, result, fwSession, activityManager);

                    serviceSession.init();

                    // Create result and update maps
                    propertiesToServiceIdentifierMap.put(properties, result);

                    serviceIdentifierToServiceSessionMap.put(result,
                            serviceSession);
                }
                catch (FwSessionException e) {
                    throw e;
                }
                catch (ResourceException e) {
                    throw e;
                }
            }
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Returning :" + result);
        }

        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.session.ParlaySession#getServiceSession(org.mobicents.csapi.jr.slee.TpServiceIdentifier)
     */
    public ServiceSession getServiceSession(
            TpServiceIdentifier serviceIdentifier) {
        return (ServiceSession) serviceIdentifierToServiceSessionMap
                .get(serviceIdentifier);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.fw.FwSessionListener#terminateAccess(org.mobicents.slee.resource.parlay.fw.TerminateAccessEvent)
     */
    public void terminateAccess(TerminateAccessEvent event) {

        fwSession.removeFwSessionListener(this);

        fwSession.shutdown();
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.parlay.fw.FwSessionListener#terminateServiceAgreement(org.mobicents.csapi.jr.slee.fw.TerminateServiceAgreementEvent)
     */
    public void terminateServiceAgreement(TerminateServiceAgreementEvent event) {
        fwSession.removeFwSessionListener(this);

        endAccess();

        fwSession.shutdown();
    }

    /**
     * Performs endAccess on FwSession
     */
    private void endAccess() {
        TpProperty[] properties = new TpProperty[0];
        try {
            fwSession.endAccess(properties);
        }
        catch (FwSessionException e) {
            logger.error("Exception performing endAccess", e);
        }
    }

    /**
     * @return Returns the fwSession.
     */
    public FwSession getFwSession() {
        return fwSession;
    }
 

    /**
     * @return Returns the mpccsListener.
     */
    public MpccsListener getMpccsListener() {
        return mpccsListener;
    }

    /**
     * @param mpccsListener
     *            The mpccsListener to set.
     */
    public void setMpccsListener(MpccsListener mpccsListener) {
        this.mpccsListener = mpccsListener;
    }

    /**
     * @param serviceAndToken
     * @param serviceIdentifier
     * @param fwSession
     * @param activityManager
     * @return
     * @throws ResourceException 
     */
    public ServiceSession createServiceSession(String serviceTypeName, ServiceAndToken serviceAndToken,
            TpServiceIdentifier serviceIdentifier, FwSession fwSession,
            ActivityManager activityManager) throws ResourceException {

        ServiceSession result = null;
        try {
            if (serviceTypeName.equals(P_MULTI_PARTY_CALL_CONTROL)) {
	            IpMultiPartyCallControlManager manager = IpMultiPartyCallControlManagerHelper
	                    .narrow(serviceAndToken.getIpService());
	            if (logger.isDebugEnabled()) {
	                logger.debug("Creating MpccsSession");
	            }
	
	            result = new MultiPartyCallControlManagerImpl(serviceIdentifier,
	                    manager, fwSession, activityManager, mpccsListener);
            } else if (serviceTypeName.equals(P_GENERIC_CALL_CONTROL)) {

                IpCallControlManager ipCallControlManager = IpCallControlManagerHelper
                		.narrow(serviceAndToken.getIpService());
                if (logger.isDebugEnabled()) {
                    logger.debug("Creating GccsSession");
                }
                result = new CallControlManagerImpl(fwSession, ipCallControlManager, gccsListener, activityManager, serviceIdentifier);
            } else if (serviceTypeName.equals(P_USER_INTERACTION)) {

                IpUIManager ipUIManager = IpUIManagerHelper
                        .narrow(serviceAndToken.getIpService());
                if (logger.isDebugEnabled()) {
                    logger.debug("Creating UiSession");
                }
                result = new UIManagerImpl(this, fwSession, ipUIManager,
                        uiListener, activityManager, serviceIdentifier);
            } else {
                //should never occur
                logger.error("Invalid service Type: "+serviceTypeName);
            }
            
        }
        catch (SystemException e) {
            logger.error(FAILED_TO_CREATE_SERVICE_SESSION);
            throw new ResourceException(
                    FAILED_TO_CREATE_SERVICE_SESSION);
        }

        return result;
    }
    
    /**
     * @return Returns the mpccsListener.
     */
    public GccsListener getGccsListener() {
        return gccsListener;
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.parlay.session.ParlaySession#setGccsListener(org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.GccsListener)
     */
    public void setGccsListener(GccsListener gccsListener) {
        this.gccsListener = gccsListener;
        
    }

    /**
     * @return Returns the  uiListener
     */
    public UiListener getUiListener() {         
        return uiListener;
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.parlay.session.ParlaySession#setUiListener(org.mobicents.slee.resource.parlay.csapi.jr.ui.UiListener)
     */
    public void setUiListener(UiListener uiListener) {
        this.uiListener = uiListener;
        
    }
}