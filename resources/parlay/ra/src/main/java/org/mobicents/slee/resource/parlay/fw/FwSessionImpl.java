package org.mobicents.slee.resource.parlay.fw;

import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csapi.IpService;
import org.csapi.TpCommonExceptions;
import org.csapi.fw.P_ACCESS_DENIED;
import org.csapi.fw.P_ILLEGAL_SERVICE_TYPE;
import org.csapi.fw.P_INVALID_PROPERTY;
import org.csapi.fw.P_UNKNOWN_SERVICE_TYPE;
import org.csapi.fw.TpProperty;
import org.csapi.fw.TpService;
import org.csapi.fw.TpServiceProperty;
import org.csapi.fw.fw_application.discovery.IpServiceDiscovery;
import org.mobicents.csapi.jr.slee.fw.TerminateAccessEvent;
import org.mobicents.csapi.jr.slee.fw.TerminateServiceAgreementEvent;
import org.mobicents.slee.resource.parlay.fw.access.TSMBean;
import org.mobicents.slee.resource.parlay.fw.access.TSMBeanException;
import org.mobicents.slee.resource.parlay.fw.access.TSMBeanImpl;
import org.mobicents.slee.resource.parlay.fw.access.TSMBeanListener;
import org.mobicents.slee.resource.parlay.fw.application.SABean;
import org.mobicents.slee.resource.parlay.fw.application.SABeanException;
import org.mobicents.slee.resource.parlay.fw.application.SABeanListener;

/**
 * Implementation class for FwSession
 */
public class FwSessionImpl implements FwSession, TSMBeanListener, SABeanListener {
    /**
     * Log4J Logger for this class.
     */
    private static final Log logger = LogFactory
            .getLog(FwSessionImpl.class);

    private static final String TERMINATION_TEXT = "FwSession terminating service agreement";

    private static final String AGREEMENT_TEXT = "FwSession signing service agreement";

    private static final String EXCEPTION_INVOKING = "Exception invoking ";

    private static final String SERVICE_DISCOVERY = "org::csapi::fw::fw_applications::service_discovery::IpServiceDiscovery";

    private static final String DISCOVER_SERVICE = ".discoverService()";

    private transient Vector fwSessionListeners;

    // Maps tokens to service managers
    protected final transient TokenAndServiceMap tokenAndServiceMap;

    protected TSMBean tsmBean = null;
    
    private final FwSessionProperties fwSessionProperties;

    private IpServiceDiscovery serviceDiscovery = null;

    /**
     * @param fwSessionProperties
     */
    public FwSessionImpl(final FwSessionProperties fwSessionProperties) {
        if (logger.isDebugEnabled()) {
            logger.debug("FwSessionImpl created");
        }
        this.fwSessionProperties = fwSessionProperties;

        tokenAndServiceMap = new TokenAndServiceMap();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.fw.FwSession#initialize()
     */
    public void init() throws FwSessionException {
        try {
            tsmBean = new TSMBeanImpl(this, fwSessionProperties);
            tsmBean.initialize();
            tsmBean.addTSMBeanListener(this);
        } catch (TSMBeanException e) {
            throw new FwSessionException(e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.fw.FwSession#addFwSessionListener(org.mobicents.slee.resource.parlay.fw.FwSessionListener)
     */
    public void addFwSessionListener(final FwSessionListener listener) {
        Vector v = fwSessionListeners == null ? new Vector(2)
                : (Vector) fwSessionListeners.clone();
        if (!v.contains(listener)) {
            v.addElement(listener);
            fwSessionListeners = v;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.fw.FwSession#removeFwSessionListener(org.mobicents.slee.resource.parlay.fw.FwSessionListener)
     */
    public void removeFwSessionListener(final FwSessionListener listener) {
        if (fwSessionListeners != null && fwSessionListeners.contains(listener)) {
            Vector v = (Vector) fwSessionListeners.clone();
            v.removeElement(listener);
            fwSessionListeners = v;
        }

        if (tsmBean != null) {
	        tsmBean.removeTSMBeanListener(this);
	        if (tsmBean.getSABean() != null) {
	            tsmBean.getSABean().removeSABeanListener(this);
	        }
        }
        
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.fw.FwSession#authenticate()
     */
    public void authenticate() throws FwSessionException {
        try {
            // delegate to the TSMBean
            tsmBean.authenticate();
        } catch (TSMBeanException ex) {
            logger.error("TSMBeanException" + " " + ex.toString());
            
            if (tsmBean.getState() == TSMBeanImpl.INVALID_STATE) {
                shutdown();
            }
            throw new FwSessionException(ex);
        } catch (IllegalStateException ex) {
            logger.error("IllegalStateException" + " " + ex.toString());
            throw new FwSessionException(ex);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.fw.FwSession#endAccess(org.csapi.fw.TpProperty[])
     */
    public void endAccess(final TpProperty[] endAccessProperties)
            throws FwSessionException {
        try {
            // delegate to the TSMBean
            if (tsmBean != null) {
            tsmBean.endAccess(endAccessProperties);
            }
        } catch (TSMBeanException ex) {
            logger.error("TSMBeanException" + " " + ex.toString());
            throw new FwSessionException(ex);
        } catch (IllegalStateException ex) {
            logger.error("IllegalStateException" + " " + ex.toString());
            throw new FwSessionException(ex);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.fw.FwSession#getService(java.lang.String,
     *      org.csapi.fw.TpServiceProperty[])
     */
    public ServiceAndToken getService(String serviceTypeName,
            TpServiceProperty[] serviceProperties) throws FwSessionException {
        SABean saBean = null;

        if (logger.isDebugEnabled()) {
            logger.debug("Getting service from Gateway ...");
        }

        ServiceAndToken serviceAndToken = null;

        try {
            //          get a service ID
            String serviceID = discoverServiceID(serviceTypeName,
                    serviceProperties);

            saBean = tsmBean.getSABean();
            // create an SABean if necessary
            if (saBean == null) {

                saBean = tsmBean.createSABean();
                saBean.addSABeanListener(this);
                
                if (logger.isDebugEnabled()) {
                    logger.debug("Got SABean reference");
                }

                // select and sign service agreement
                serviceAndToken = saBean.selectAndSignServiceAgreement(
                        serviceID, AGREEMENT_TEXT);

                if (logger.isDebugEnabled()) {
                    logger.debug("Selected and signed service.");
                }

                // create a look-up entry for the token and service

                tokenAndServiceMap.put(serviceAndToken.getServiceToken(),
                        serviceAndToken.getIpService());

                if (logger.isDebugEnabled()) {
                    logger.debug("Storing service and token :" + serviceAndToken);
                }
            }
        } catch (SABeanException ex) {
            throw new FwSessionException("Failed to getService", ex);
        } catch (TSMBeanException ex) {
            throw new FwSessionException("Failed to getService", ex);
        } catch (IllegalStateException ex) {
            throw new FwSessionException("Failed to getService", ex);
        } catch (FwSessionException ex) {
            throw ex;
        }

        return serviceAndToken;
    }

    /**
     * Method discoverServiceID.
     * 
     * @param serviceTypeName
     * @param desiredPropertyList
     * @return String
     * @throws FwSessionException
     */
    private String discoverServiceID(String serviceTypeName,
            TpServiceProperty[] desiredPropertyList) throws FwSessionException {
        String result = null;

        // For storing the list returned by discoverService()
        TpService[] serviceList = null;
        // We only want to discover one service
        final int MAX_SERVICES = 1;

        try {
            // Get an interface to use for the parlay call
            if (serviceDiscovery == null) {
                serviceDiscovery = tsmBean.obtainDiscoveryInterface();
                if (logger.isDebugEnabled()) {
                    logger.debug("Got serviceDiscovery interface.");
                }
            }

            //   Make the parlay call
            serviceList = serviceDiscovery.discoverService(serviceTypeName,
                    desiredPropertyList, MAX_SERVICES);

        } catch (TpCommonExceptions ex) {
            logger.warn(EXCEPTION_INVOKING + SERVICE_DISCOVERY + DISCOVER_SERVICE
                    + " " + ex.toString());
            throw new FwSessionException(EXCEPTION_INVOKING + SERVICE_DISCOVERY
                    + DISCOVER_SERVICE, ex);
        } catch (P_ACCESS_DENIED ex) {
            logger.warn(EXCEPTION_INVOKING + SERVICE_DISCOVERY + DISCOVER_SERVICE
                    + " " + ex.toString());
            throw new FwSessionException(EXCEPTION_INVOKING + SERVICE_DISCOVERY
                    + DISCOVER_SERVICE, ex);
        } catch (P_ILLEGAL_SERVICE_TYPE ex) {
            logger.warn(EXCEPTION_INVOKING + SERVICE_DISCOVERY + DISCOVER_SERVICE
                    + " " + ex.toString());
            throw new FwSessionException(EXCEPTION_INVOKING + SERVICE_DISCOVERY
                    + DISCOVER_SERVICE, ex);
        } catch (P_UNKNOWN_SERVICE_TYPE ex) {
            logger.warn(EXCEPTION_INVOKING + SERVICE_DISCOVERY + DISCOVER_SERVICE
                    + " " + ex.toString());
            throw new FwSessionException(EXCEPTION_INVOKING + SERVICE_DISCOVERY
                    + DISCOVER_SERVICE, ex);
        } catch (P_INVALID_PROPERTY ex) {
            logger.warn(EXCEPTION_INVOKING + SERVICE_DISCOVERY + DISCOVER_SERVICE
                    + " " + ex.toString());
            throw new FwSessionException(EXCEPTION_INVOKING + SERVICE_DISCOVERY
                    + DISCOVER_SERVICE, ex);
        } catch (org.omg.CORBA.SystemException ex) {
            logger.warn(EXCEPTION_INVOKING + SERVICE_DISCOVERY + DISCOVER_SERVICE
                    + " " + ex.toString());
            throw new FwSessionException(EXCEPTION_INVOKING + SERVICE_DISCOVERY
                    + DISCOVER_SERVICE, ex);
        } catch (TSMBeanException ex) {
            throw new FwSessionException(ex);
        } catch (IllegalStateException ex) {
            throw new FwSessionException(ex);
        }

        // Get the ID from the list
        if (serviceList == null) {
            logger.warn("Null service list returned");
            throw new FwSessionException("Null service list returned");
        } else if (serviceList.length == 0) {
            logger.warn("No services discovered");
            throw new FwSessionException("No services discovered");
        } else if (serviceList.length != 1) {
            logger.warn("Framework returned " + serviceList.length
                    + " services, only 1 was requested");
            throw new FwSessionException("Framework returned "
                    + serviceList.length + " services, only 1 was requested");
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("Getting service ID from returned service list.");
            }
            result = serviceList[0].ServiceID;
        }

        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.fw.FwSession#shutdown()
     */
    public void shutdown() {

        if (logger.isDebugEnabled()) {
            logger.debug("Shutting down the FwSession...");
        }

        if (tsmBean != null) {
            synchronized (tsmBean) {
                SABean saBean = tsmBean.getSABean();
                if(saBean != null) {
	                saBean.cleanup();
	                saBean = null;
                }
            }
            tsmBean.shutdown();
            tsmBean = null;
        }

        tokenAndServiceMap.clear();

        serviceDiscovery = null;

        if (logger.isDebugEnabled()) {
            logger.debug("...FwSession shutdown OK");
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.fw.FwSession#getFwSessionProperties()
     */
    public FwSessionProperties getFwSessionProperties() {
        return fwSessionProperties;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.fw.FwSession#getORB()
     */
    public org.omg.CORBA.ORB getORB() {
        return tsmBean.getOrbHandler().getOrb();
    }

    public org.omg.PortableServer.POA getRootPOA() {
        return tsmBean.getOrbHandler().getRootPOA();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.fw.FwSessionInterface#releaseService(org.mobicents.slee.resource.parlay.fw.ServiceAndToken)
     */
    public void releaseService(ServiceAndToken service)
            throws FwSessionException {

        SABean saBean = tsmBean.getSABean();
        String serviceToken = service.getServiceToken();

        if (saBean != null) {
            try {
                //terminate the OSA Gateway
                saBean
                        .terminateServiceAgreement(serviceToken,
                                TERMINATION_TEXT);

                //remove reference from map
                removeServiceTokenService(serviceToken);
            } catch (SABeanException ex) {
                throw new FwSessionException(ex);
            }
        } else {
            logger.error("SABean is null cannot terminate service agreement.");
            throw new FwSessionException(
                    "SABean is null cannot terminate service agreement.");
        }
    }

    /**
     * Method removeServiceTokenService.
     * 
     * @param serviceToken
     * @return Service
     */
    private IpService removeServiceTokenService(String serviceToken) {
        return tokenAndServiceMap.remove(serviceToken);
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.parlay.fw.access.TSMBeanListener#terminateAccess(org.mobicents.slee.resource.parlay.fw.access.TerminateAccessEvent)
     */
    public void terminateAccess(TerminateAccessEvent event) {
        if (fwSessionListeners != null) {
            
            int count = fwSessionListeners.size();
            for (int i = 0; i < count; i++) {
                try {
                    ((FwSessionListener) fwSessionListeners.elementAt(i))
                            .terminateAccess(event);
                } catch (RuntimeException ex) {
                    logger
                            .error("Caught exception invoking application TSMBeanListener"
                                    + " " + ex.toString());
                }
            }
        }
        
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.parlay.fw.application.SABeanListener#terminateServiceAgreement(org.mobicents.slee.resource.parlay.fw.application.TerminateServiceAgreementEvent)
     */
    public void terminateServiceAgreement(TerminateServiceAgreementEvent event) {
        if (fwSessionListeners != null) {
            
            int count = fwSessionListeners.size();
            for (int i = 0; i < count; i++) {
                try {
                    ((FwSessionListener) fwSessionListeners.elementAt(i))
                            .terminateServiceAgreement(event);
                } catch (RuntimeException ex) {
                    logger
                            .error("Caught exception invoking application TSMBeanListener"
                                    + " " + ex.toString());
                }
            }
        }
    }

}