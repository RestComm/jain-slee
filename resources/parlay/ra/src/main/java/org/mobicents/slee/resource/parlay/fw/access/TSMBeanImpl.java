package org.mobicents.slee.resource.parlay.fw.access;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.RSAPrivateKey;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.csapi.IpInterface;
import org.csapi.P_INVALID_INTERFACE_NAME;
import org.csapi.P_INVALID_INTERFACE_TYPE;
import org.csapi.TpCommonExceptions;
import org.csapi.fw.P_ACCESS_DENIED;
import org.csapi.fw.P_INVALID_PROPERTY;
import org.csapi.fw.P_INVALID_SIGNATURE;
import org.csapi.fw.P_NO_ACCEPTABLE_SIGNING_ALGORITHM;
import org.csapi.fw.TpAuthDomain;
import org.csapi.fw.TpDomainID;
import org.csapi.fw.TpDomainIDType;
import org.csapi.fw.TpProperty;
import org.csapi.fw.fw_access.trust_and_security.IpAccess;
import org.csapi.fw.fw_access.trust_and_security.IpClientAPILevelAuthentication;
import org.csapi.fw.fw_access.trust_and_security.IpClientAPILevelAuthenticationHelper;
import org.csapi.fw.fw_access.trust_and_security.IpClientAccess;
import org.csapi.fw.fw_access.trust_and_security.IpClientAccessHelper;
import org.csapi.fw.fw_access.trust_and_security.IpInitial;
import org.csapi.fw.fw_access.trust_and_security.IpInitialHelper;
import org.csapi.fw.fw_application.discovery.IpServiceDiscovery;
import org.csapi.fw.fw_application.discovery.IpServiceDiscoveryHelper;
import org.csapi.fw.fw_application.service_agreement.IpAppServiceAgreementManagement;
import org.csapi.fw.fw_application.service_agreement.IpServiceAgreementManagement;
import org.csapi.fw.fw_application.service_agreement.IpServiceAgreementManagementHelper;
import org.mobicents.csapi.jr.slee.fw.TerminateAccessEvent;
import org.mobicents.slee.resource.parlay.fw.AuthenticationSequence;
import org.mobicents.slee.resource.parlay.fw.FwSession;
import org.mobicents.slee.resource.parlay.fw.FwSessionProperties;
import org.mobicents.slee.resource.parlay.fw.application.SABean;
import org.mobicents.slee.resource.parlay.fw.application.SABeanException;
import org.mobicents.slee.resource.parlay.fw.application.SABeanImpl;
import org.mobicents.slee.resource.parlay.util.ParlayExceptionUtil;
import org.mobicents.slee.resource.parlay.util.corba.ORBHandler;
import org.mobicents.slee.resource.parlay.util.corba.POAFactory;
import org.mobicents.slee.resource.parlay.util.corba.PolicyFactory;
import org.mobicents.slee.resource.parlay.util.corba.ServantActivationHelper;
import org.mobicents.slee.resource.parlay.util.crypto.RSAUtil;
import org.mobicents.slee.resource.parlay.util.crypto.RSAUtilException;
import org.omg.CORBA.UserException;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAPackage.AdapterAlreadyExists;
import org.omg.PortableServer.POAPackage.InvalidPolicy;
import org.omg.PortableServer.POAPackage.ObjectAlreadyActive;
import org.omg.PortableServer.POAPackage.ObjectNotActive;
import org.omg.PortableServer.POAPackage.ServantAlreadyActive;
import org.omg.PortableServer.POAPackage.ServantNotActive;
import org.omg.PortableServer.POAPackage.WrongPolicy;

import EDU.oswego.cs.dl.util.concurrent.Executor;
import EDU.oswego.cs.dl.util.concurrent.QueuedExecutor;

/**
 * This class represents the trust and security management Framework services.
 * <BR>
 * It is used by clients to perform its part of the mutual authentication
 * process with the Framework necessary to be allowed to use any of the other
 * interfaces supported by the Framework. It can then be used to access the
 * other interfaces. This is a higher level bean which encompasses all of the
 * interfaces within the Parlay trust_and_security module. <BR>
 */
public class TSMBeanImpl implements Serializable, TSMBeanConstants, TSMBean {

    // VARIABLES
    // .......................................................

    //identifiers
    private String clientID = null;

    private TpDomainID clientDomainID = null;

    private transient Vector TSMBeanListeners = null;

    // this object's state
    private int state = IDLE_STATE;

    // API references
    private SABean saBean = null;

    private IpInitial initial = null;

    private IpAccess access = null;

    private IpServiceDiscovery serviceDiscovery = null;

    // API Callback implementations
    private IpClientAPILevelAuthenticationImpl ipClientAPILevelAuthenticationImpl = null;

    private IpClientAPILevelAuthentication ipClientAPILevelAuthentication = null;

    private IpClientAccessImpl ipClientAccessImpl = null;

    private IpClientAccess clientAccess = null;
    
    private POA ipClientAPILevelAuthenticationPOA = null;

    // parlay data storage
    private String encryptionMethod = null;

    // used in wait/notifys
    private Object authenticationMonitor = new java.lang.Object();

    // Flag for indicating framework has authenticated client successfully
    // Set this flag to false for 2-way support
    private boolean fwAuthenticationSucceeded = false;

    // Flag for when this object has been cleaned
    private boolean clean = false;

    // For processing high level FW events
    private Executor eventsQueue = null;

    private ORBHandler orbHandler = null;

    // TSM Session properties
    private FwSessionProperties fwProperties = null;

    private static final String TERMINATION_TEXT = "TSMBean terminating service agreement";

    private String signingAlgorithm = NULL;

    private AuthenticationHandler authHandler = null;

    // CONSTANTS
    // .......................................................
    private static final Log logger = LogFactory
            .getLog(TSMBeanImpl.class);

    private static final String lineSeparator = System
            .getProperty("line.separator");

    private static final String EXCEPTION_INVOKING = "Exception invoking ";

    private static final String ACCESS = "org::csapi::jr::fw::fwaccess::trustandsecurity::IpAccess";

    private static final String OBTAIN_INTERFACE = ".obtainInterface";

    private static final String OBTAIN_INTERFACE_WITH_CALLBACK = ".obtainInterfaceWithCallback";

    private static final String RELEASE_INTERFACE = ".releaseInterface";

    private static final String RELINQUISH_INTERFACE = ".relinquishInterface";

    private static final String SELECT_SIGNING_ALGORITHM = ".selectSigningAlgorithm";

    private static final String END_ACCESS = ".endAccess()";

    private static final String TERMINATE_ACCESS = ".terminateAccess()";

    private static final String INVOKING = "Invoking ";

    private static final String INVOKED = "Invoked ";
    
    private static final String NULL = "NULL";
    
    private static final String INTERFACE_NAME = "Interface Name = ";
    
    private static final String CLIENTAPI_EXCEPTION = "Exception creating IpClientAPILevelAuthentication";

    private static final String CLIENTACCESS_EXCEPTION = "Exception creating IpClientAccess";
    
    private static final String ACCESSNULL_EXCEPTION = "IpAccess ref is null.";
    
    private static final int MAX_WAIT_AUTHENTICATION_SUCCEEDED = 5;
    
    private static String version = NULL;

    private FwSession fwSession = null;
    
    private TpAuthDomain tpAuthDomain = null;

    /**
     * @param fwProperties
     */
    public TSMBeanImpl(FwSession fwSession, FwSessionProperties fwProperties) {

        this.fwSession = fwSession;

        this.fwProperties = fwProperties;

        setClientDomainID(fwProperties.getDomainID());

    }

    /**
     * This method must be called before attempting to make any parlay requests
     * via this object. It is called automatically on construction except by the
     * default constructor.
     */
    public void initialize() throws TSMBeanException {

        if (state != IDLE_STATE) {
            StringBuffer errorMessage = new StringBuffer(
                    "This object can only be initialized when in the IDLE_STATE.");
            errorMessage.append(getStateMessage());

            throw new IllegalStateException(errorMessage.toString());
        }

        if (getClientDomainID() == null) {
            StringBuffer errorMessage = new StringBuffer(
                    "DomainID must be specified before initialisation.");
            errorMessage.append(getStateMessage());

            throw new IllegalStateException(errorMessage.toString());
        }

        initialiseCorbaServer();

        // Set the flag to false for 2-way authentication support if it is
        // enabled.
        setAuthenticationSequence(fwProperties.getAuthenticationSequence());

        try {
           
            ipClientAPILevelAuthenticationImpl = new IpClientAPILevelAuthenticationImpl();
            ipClientAPILevelAuthenticationImpl.setTSMBean(this);
            
            ipClientAPILevelAuthenticationPOA = POAFactory.createPOA(orbHandler.getRootPOA(),
                    "IpClientAPILevelAuthentication", orbHandler.getRootPOA().the_POAManager(), 
                    PolicyFactory.createTransientPoaPolicies(orbHandler.getRootPOA()));
            
            //byte[] id = getClientID().getBytes();
            
            org.omg.CORBA.Object activatedCallback = null;

            activatedCallback =  ServantActivationHelper.activateServant(
                    ipClientAPILevelAuthenticationPOA, 
                    ipClientAPILevelAuthenticationImpl);
            
            ipClientAPILevelAuthentication = IpClientAPILevelAuthenticationHelper.narrow(activatedCallback);

            // Construct a reference to the client authentication domain

            tpAuthDomain = new TpAuthDomain(clientDomainID,
                    ipClientAPILevelAuthentication);
        } catch (WrongPolicy e) {
            if (logger.isDebugEnabled()) {
                logger.debug(
                        CLIENTAPI_EXCEPTION, e);
            }
            throw new TSMBeanException(
                    CLIENTAPI_EXCEPTION, e);
        } catch (ServantAlreadyActive e) {
            throw new TSMBeanException(
                    CLIENTAPI_EXCEPTION, e);
        } catch (ObjectAlreadyActive e) {
            throw new TSMBeanException(
                    CLIENTAPI_EXCEPTION, e);
        } catch (AdapterAlreadyExists e) {
            throw new TSMBeanException(
                    CLIENTAPI_EXCEPTION, e);
        } catch (InvalidPolicy e) {
            throw new TSMBeanException(
                    CLIENTAPI_EXCEPTION, e);
        }

        try {

            ipClientAccessImpl = new IpClientAccessImpl(fwSession.getRootPOA(), this);

            clientAccess = IpClientAccessHelper.narrow(fwSession.getRootPOA()
                    .servant_to_reference(ipClientAccessImpl));

        }
        catch (ServantNotActive e) {
            if (logger.isDebugEnabled()) {
                logger.debug(CLIENTACCESS_EXCEPTION, e);
            }
            throw new TSMBeanException(CLIENTACCESS_EXCEPTION, e);

        }
        catch (WrongPolicy e) {
            if (logger.isDebugEnabled()) {
                logger.debug(CLIENTACCESS_EXCEPTION, e);
            }
            throw new TSMBeanException(CLIENTACCESS_EXCEPTION, e);

        }

        // Create a queue and corresponding consumer
        eventsQueue = new QueuedExecutor();

        // This line allows the BouncyCastle package to provide key generation
        // facilities
        if (logger.isDebugEnabled()) {
            logger.debug("Registering the security provider ...");
        }

        Security.addProvider(new BouncyCastleProvider());
        traceSecurityProviders();

        //Framework Parlay Version
        version = fwProperties.getFwParlayVersion();
        
        try {
            RSAUtil.loadKeyStore(getFwProperties().getCertificateVault(), "mobicents-parlay-ra.jks");
        } catch (RSAUtilException e) {
            logger.error("RSAUtilException, keyStore not loaded properly", e);
        }

        // Create a handler object for performing the authentication
        authHandler = new AuthenticationHandlerImpl(this);

        state = INITIALIZED_STATE;

        if (logger.isDebugEnabled()) {
            logger.debug("INITIALIZED ".concat(this.toString()));
        }

    }

    /**
     * @throws TSMBeanException
     */
    private void initialiseCorbaServer() throws TSMBeanException {
        try {
            orbHandler = ORBHandler.getInstance();
        }
        catch (IOException e) {
            logger.error("Failed to create ORBHandler.");
            throw new TSMBeanException("Failed to create ORBHandler.", e);
        }
        synchronized (orbHandler) {
            try {
                orbHandler.init();
            }
            catch (UserException e) {
                logger.error("Failed to initialise corba server.", e);
                throw new TSMBeanException("Failed to initialise corba server.", e);
            }
            // Test every 2 seconds
            int numberOfRetries = 20;
            while (!orbHandler.getIsServerReady() && numberOfRetries > 0) {
                try {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Waiting for orb to initialise ...");
                    }
                    orbHandler.wait(2000);
                }
                catch (InterruptedException e1) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Wait interrupted.");
                    }
                }
                numberOfRetries--;
            }
        }
        if (!orbHandler.getIsServerReady()) {
            throw new TSMBeanException("Failed to initialise ORB.");
        }
    }

    /**
     * Method getStateMessage. Creates a short string printing this objects
     * state in a human readable format.
     * 
     * @return String e.g. "Current state = IDLE_STATE"
     */
    private String getStateMessage() {
        StringBuffer errorMessage = new StringBuffer("Current state = ");
        switch (state) {
            case IDLE_STATE:
                errorMessage.append("IDLE_STATE");
                break;
            case INITIALIZED_STATE:
                errorMessage.append("INITIALIZED_STATE");
                break;
            case INVALID_STATE:
                errorMessage.append("INVALID_STATE");
                break;
            case ACTIVE_STATE:
                errorMessage.append("ACTIVE_STATE");
                break;
            default:
                errorMessage.append("UNKNOWN");
                break;
        }
        return errorMessage.toString();
    }

    /**
     * Defines a method used to provide the caller with a string representation
     * of the class.
     * 
     * @return This is an developer defined representation of the class object
     *         as a string
     */

    public String toString() {
        StringBuffer value = new StringBuffer("TSMBean");
        value.append(lineSeparator);

        if (clientDomainID != null) {

            value.append("ClientDomainID = ");
            if (clientDomainID != null) {
                value.append(getClientID());
            }
            else {
                value.append(NULL);
            }
            value.append(lineSeparator);

            value.append(getStateMessage()).append(lineSeparator);

            //value.append("Framework Location = ").append(
            //       orbProperties.getFwLocation()).append(lineSeparator);

            value.append("InstanceID = ").append(fwProperties.getInstanceID())
                    .append(lineSeparator);
        }

        return value.toString();
    }

    /**
     * Stores the client domain ID to be used for authentication.
     * 
     * @param newClientDomainID
     *            the domain ID to be used
     * @exception IllegalStateException
     *                if the bean is not in IDLE_STATE.
     */
    public synchronized void setClientDomainID(TpDomainID newClientDomainID) {
        if (state == ACTIVE_STATE || state == INVALID_STATE) {
            StringBuffer errorMessage = new StringBuffer(
                    "Client Domain can only be changed when this object's state is IDLE_STATE or INTITIALIZED_STATE. ");
            errorMessage.append(getStateMessage());
            throw new IllegalStateException(errorMessage.toString());
        }

        clientDomainID = newClientDomainID;

        clientID = this.getClientDomain(newClientDomainID);

    }

    /**
     * Retrieves the client domain ID to be used for checking the HA database.
     * 
     * @param clientDomainID
     *            the domain ID to be used
     *  
     */
    public String getClientDomain(TpDomainID clientDomainID) {
        String clientID = null;

        // Retrieve ID as a string
        if (clientDomainID.discriminator().equals(
                TpDomainIDType.P_CLIENT_APPLICATION)) {
            clientID = clientDomainID.ClientAppID();
        }
        else if (clientDomainID.discriminator().equals(
                TpDomainIDType.P_SERVICE_SUPPLIER)) {
            clientID = clientDomainID.ServiceSupplierID();
        }
        else if (clientDomainID.discriminator().equals(TpDomainIDType.P_FW)) {
            clientID = clientDomainID.FwID();
        }
        else if (clientDomainID.discriminator().equals(TpDomainIDType.P_ENT_OP)) {
            clientID = clientDomainID.EntOpID();
        }
        else if (clientDomainID.discriminator().equals(
                TpDomainIDType.P_SERVICE_INSTANCE)) {
            clientID = clientDomainID.ServiceID();
        }

        if (clientID == null) {
            StringBuffer errorMessage = new StringBuffer(
                    "Null client domain not allowed.");
            errorMessage.append(getStateMessage());
            throw new IllegalArgumentException(errorMessage.toString());
        }

        return clientID;

    }

    /**
     * Returns the client domain ID to be used for authentication.
     * 
     * @return the domain ID to be used
     */
    public TpDomainID getClientDomainID() {
        return clientDomainID;
    }

    /**
     * Returns the client ID (without type) to be used for authentication.
     * 
     * @return the client ID to be used
     */
    public String getClientID() {
        return clientID;
    }

    /**
     * Returns the current state of this bean.
     * 
     * @return One of IDLE_STATE}, ACTIVE_STATE_STATE}, INTITIALIZED_STATE or
     *         INVALID_STATE_STATE.
     */
    public int getState() {
        return state;
    }

    /**
     * Sets the state of this bean.
     * 
     * @param newState
     *            the new state.
     */
    public void setState(int newState) {
        state = newState;
    }

    /**
     * Returns the encryption method chosen by the framework during the
     * authentication sequence.
     * 
     * @return a String containing the chosen encryption method.
     */
    public String getEncryptionMethod() {
        return encryptionMethod;
    }

    /**
     * Returns the Initial object used by this bean.
     * 
     * @return the object reference.
     */
    public IpInitial getInitial() {
        return initial;
    }

    /**
     * Returns an object that can be used as a monitor for authentication.
     * 
     * @return the object reference.
     */
    public java.lang.Object getAuthenticationMonitor() {
        return authenticationMonitor;
    }

    /**
     * Called to indicate to this bean that the framework has indicated
     * successful authentication.
     * 
     * @param value
     *            true if it successful.
     */
    public void setFwAuthenticationSucceeded(boolean value) {
        fwAuthenticationSucceeded = value;
    }

    /**
     * This method must be called when an application has no more use of this
     * object to ensure a graceful shutdown.
     */
    public void shutdown() {
        cleanup();
        if (orbHandler != null) {
            orbHandler.shutdown();
        }
    }

    /**
     * This method will authenticate a client with the Parlay framework. The
     * clientDomainID must be set before calling this method.
     * 
     * @exception TSMBeanException
     * @exception IllegalStateException
     */
    public synchronized void authenticate() throws TSMBeanException {
        if (logger.isDebugEnabled()) {
            logger.debug("TSMBean.authenticate() called.");
        }

        if (getState() != TSMBeanImpl.INITIALIZED_STATE) {
            logger.warn("Can only authenticate when in the INTITIALIZED_STATE state.");
            throw new IllegalStateException(
                    "Can only authenticate when in the INTITIALIZED_STATE state.");
        }

        authenticateWithGateway();

        if (logger.isDebugEnabled()) {
            logger.debug("Changing TSMBean state to ACTIVE_STATE.");
        }
        setState(TSMBeanImpl.ACTIVE_STATE);
    }

    /**
     * Performs the desired Parlay authentication sequence against the remote
     * gateway framework; can interwork with either a Parlay 3 or a Parlay 4 Gateway.
     * 
     * @throws TSMBeanException
     *             if authentication fails for any reason.
     */
    private void authenticateWithGateway() throws TSMBeanException {
        if (logger.isDebugEnabled()) {
            logger.debug("Authenticating with Gateway ...");
        }

        try {
            if (this.initial == null) {
                createInitial(fwProperties);
            }

            if (version.startsWith("P_PARLAY_4")) {

                authenticateWithP4Gateway(tpAuthDomain, version);

            }
            else if ((version.equalsIgnoreCase(NULL))
                    || version.startsWith("P_PARLAY_3")) {

                authenticateWithP3Gateway(tpAuthDomain);

            }
            else {

                logger.error("INVALID PARLAY VERSION, AUTHENTICATION FAILED, ABORTING");

                // Re-throw exception for outer handler to also deal with
                throw new TSMBeanException("INVALID PARLAY VERSION: " + version);
                //invalid parlay version
            }

        }
        catch (TSMBeanException ex) {
            logger.error("INVALIDATING " + this.toString());
            cleanup();

            // Re-throw for calling app or FwSession to deal with
            throw ex;
        }
    }

    private void authenticateWithP3Gateway(TpAuthDomain tpAuthDomain)
            throws TSMBeanException {

        authHandler.initiateAuthentication(tpAuthDomain);

        try {

            if (!getFwProperties().getAuthenticationSequence()
                    .equals(AuthenticationSequence.TRUSTED)) {
                // Get the framework's chosen encryption method
                synchronized (this.getAuthenticationMonitor()) {
                    encryptionMethod = authHandler
                            .selectEncryptionMethod(fwProperties
                                    .getAuthenticationCapabilityList());
                }

                authHandler.authenticate(encryptionMethod);

                authHandler.authenticationSucceeded();
            }

            // Wait for gateway to finish its auth
            waitForAuthenticationSucceeded();

            // request an access interface
            access = authHandler.requestAccess(clientAccess);

        }
        catch (TSMBeanException ex) {

            // if exception occurs in one fo the calls against the
            // authentication interface
            // abort the authentication.
            logger.error("AUTHENTICATION FAILED, ABORTING", ex);

            abortAuthentication();

            cleanup();

            // Re-throw exception for outer handler to also deal with
            throw ex;
        }
    }

    private void authenticateWithP4Gateway(TpAuthDomain tpAuthDomain,
            String version) throws TSMBeanException {
        //      Get the framework authentication domain.
        authHandler.initiateAuthenticationWithVersion(tpAuthDomain, version);

        //      parlay auth Mechanism
        String authMechanism = null;
        
        // TRUSTED authentication not valid for Parlay 4
        if (getFwProperties().getAuthenticationSequence()
                .equals(AuthenticationSequence.TRUSTED)) {
            throw new TSMBeanException("'TRUSTED' Authentication Sequence not valid for Parlay version.");
        }

        try {

            // Get the framework's chosen encryption method
            synchronized (this.getAuthenticationMonitor()) {
                authMechanism = authHandler
                        .selectAuthenticationMechanism(fwProperties
                                .getAuthenticationHash());
            }

            authHandler.challenge(authMechanism);

            authHandler.authenticationSucceeded();

            // Wait for gateway to finish its auth
            waitForAuthenticationSucceeded();

            // request an access interface
            access = authHandler.requestAccess(clientAccess);
            
            //call selectSigningAlgorithm
            signingAlgorithm = selectSigningAlgorithm();

        }
        catch (TSMBeanException ex) {

            // if exception occurs in one fo the calls against the
            // authentication interface
            // abort the authentication.
            logger.error("AUTHENTICATION FAILED, ABORTING", ex);

            abortAuthentication();
            if (authHandler != null) {
                authHandler.cleanup();
            }

            // Re-throw exception for outer handler to also deal with
            throw ex;
        }
    }

    /**
     * Waits for a specific period for the gateway to indicate succesfull
     * authentication via an authenticationSucceeded() callback. The time period
     * is set in the Fw.properties configuration file.
     */
    private void waitForAuthenticationSucceeded() throws TSMBeanException {
        // The framework will not be allowed to authenticate the client until
        // client authentication has completed.
        int waitAttempts = 0;

        while (!fwAuthenticationSucceeded) {
            if (waitAttempts < MAX_WAIT_AUTHENTICATION_SUCCEEDED) {
                synchronized (this.getAuthenticationMonitor()) {
                    try {
                        if (logger.isDebugEnabled()) {
                            logger
                                    .debug("Waiting for authenticationSucceeded() message.");
                        }
                        waitAttempts++;
                        this.getAuthenticationMonitor().wait(
                                fwProperties
                                        .getAuthenticationSucceededTimeout());

                    } catch (InterruptedException ex) {
                        if (logger.isDebugEnabled()) {
                            logger
                                    .debug("Interrupted waiting for gateway authenticationSucceeded message");
                        }
                    }
                }

            } else {
                throw new TSMBeanException(
                        "Failed to receive authenticationSucceeded.");
            }
        }
    }

    /**
     * Initialises the ORBHandler and resloves the framework IpInitial object
     * from the appropriate location.
     * 
     * @param properties
     * @throws TSMBeanException
     */
    private void createInitial(FwSessionProperties properties)
            throws TSMBeanException {

        if (properties.getIpInitialIOR() != null
                && !properties.getIpInitialIOR().equals("")) {
            
            if(logger.isDebugEnabled()) {
                logger.debug("Resolving IpInitial from IOR.");
            }
            initial = IpInitialHelper.narrow(orbHandler.getOrb()
                    .string_to_object(properties.getIpInitialIOR()));
        }
        else if (properties.getNamingServiceIOR() != null
                && !properties.getNamingServiceIOR().equals("")) {
            
            if(logger.isDebugEnabled()) {
                logger.debug("Resolving IpInitial from NameService.");
            }
            try {
                initial = org.mobicents.slee.resource.parlay.util.corba.IpInitialHelper
                        .getIpInitialFromNamingService(orbHandler.getOrb(),
                                properties.getNamingServiceIOR(), properties
                                        .getIpInitialLocation());
            }
            catch (UserException e) {
                throw new TSMBeanException("Failed to get IpInitial from NameService.", e);
            }
        }
        else {
            
            if(logger.isDebugEnabled()) {
                logger.debug("Resolving IpInitial from URL.");
            }
            try {
                initial = org.mobicents.slee.resource.parlay.util.corba.IpInitialHelper
                        .getIpInitialfromURL(orbHandler.getOrb(), properties
                                .getIpInitialURL());
            }
            catch (UserException e) {
                throw new TSMBeanException("Failed to get IpInitial from URL.", e);
            }
            catch (IOException e) {
                throw new TSMBeanException("Failed to get IpInitial from URL.", e);
            }
        }

        if (initial == null) {
            logger.error("Factory returned a null IpInitial object.");
            throw new TSMBeanException(
                    "Factory returned a null IpInitial object.");
        }
    }

    /**
     * Invokes abortAuthentication() against the gateway.
     */
    private void abortAuthentication() {
        if (logger.isDebugEnabled()) {
            logger.debug("Aborting authentication");
        }
        if (authHandler != null) {
        // Make the parlay call
            authHandler.abortAuthentication();
        }
    }

    /**
     * This nethod will end a client's access session with the Parlay framework.
     * The client must be successfully authenticated before calling this method.
     * 
     * @param endAccessProperties
     *            can be used to tell the framework the actions to perform when
     *            ending the access session.
     * 
     * @exception TSMBeanException
     * @exception IllegalStateException
     */
    public synchronized void endAccess(TpProperty[] endAccessProperties)
            throws TSMBeanException {
        if (logger.isDebugEnabled()) {
            logger.debug("TSMBean.endAccess() called.");
        }

        if (getState() != TSMBeanImpl.ACTIVE_STATE) {
            logger.warn("Can only endAccess when in the ACTIVE_STATE state.");
            throw new IllegalStateException(
                    "Can only endAccess when in the ACTIVE_STATE state.");
        }
        else if (access == null) {
            logger.warn(ACCESSNULL_EXCEPTION);
            throw new TSMBeanException(ACCESSNULL_EXCEPTION);
        }

        // Make the parlay call
        try {
            if ((version.equalsIgnoreCase(NULL))
                    || version.startsWith("P_PARLAY_3")) {
                if (logger.isDebugEnabled()) {
                    logger.debug(INVOKING + ACCESS + END_ACCESS);
                }
                access.endAccess(endAccessProperties);
                if (logger.isDebugEnabled()) {
                    logger.debug(INVOKED + ACCESS + END_ACCESS);
                }
            }
            else if (version.startsWith("P_PARLAY_4")) {
                if (logger.isDebugEnabled()) {
                    logger.debug(INVOKING + ACCESS + TERMINATE_ACCESS);
                }
                terminateAccessWithGateway();
                if (logger.isDebugEnabled()) {
                    logger.debug(INVOKED + ACCESS + TERMINATE_ACCESS);
                }
            }
        }
        catch (TpCommonExceptions ex) {
            logger.warn(EXCEPTION_INVOKING + ACCESS + END_ACCESS + " "
                    + ex.toString());
            throw new TSMBeanException(
                    EXCEPTION_INVOKING + ACCESS + END_ACCESS, ex);
        }
        catch (P_ACCESS_DENIED ex) {
            logger.warn(EXCEPTION_INVOKING + ACCESS + END_ACCESS + " "
                    + ex.toString());
            throw new TSMBeanException(
                    EXCEPTION_INVOKING + ACCESS + END_ACCESS, ex);
        }
        catch (P_INVALID_PROPERTY ex) {
            logger.warn(EXCEPTION_INVOKING + ACCESS + END_ACCESS + " "
                    + ex.toString());
            throw new TSMBeanException(
                    EXCEPTION_INVOKING + ACCESS + END_ACCESS, ex);
        }
        catch (org.omg.CORBA.SystemException ex) {
            logger.warn(EXCEPTION_INVOKING + ACCESS + END_ACCESS + " "
                    + ex.toString());
            throw new TSMBeanException(
                    EXCEPTION_INVOKING + ACCESS + END_ACCESS, ex);
        }

        // Change object state
        setState(TSMBeanImpl.INVALID_STATE);
    }


    /**
     * Invokes terminateAccess() against the gateway.
     * 
     * @throws TSMBeanException
     */
    private synchronized void terminateAccessWithGateway()
            throws TSMBeanException {

        if (logger.isDebugEnabled()) {
            logger.debug("Terminating access with Gateway ...");
        }

        // Make the parlay call
        try {
            if (logger.isDebugEnabled()) {
                logger.debug(INVOKING + ACCESS + TERMINATE_ACCESS);
            }
            access.terminateAccess(TERMINATION_TEXT,
                    generateDigitalSignature(TERMINATION_TEXT));
            if (logger.isDebugEnabled()) {
                logger.debug(INVOKED + ACCESS + TERMINATE_ACCESS);
            }
        }
        catch (TpCommonExceptions ex) {
            logger.warn(EXCEPTION_INVOKING + ACCESS + TERMINATE_ACCESS + " "
                    + ex.toString());
            throw new TSMBeanException(EXCEPTION_INVOKING + ACCESS
                    + TERMINATE_ACCESS, ex);
        }
        catch (org.omg.CORBA.SystemException ex) {
            logger.warn(EXCEPTION_INVOKING + ACCESS + TERMINATE_ACCESS + " "
                    + ex.toString());
            throw new TSMBeanException(EXCEPTION_INVOKING + ACCESS
                    + TERMINATE_ACCESS, ex);
        }
        catch (P_INVALID_SIGNATURE ex) {
            logger.warn(EXCEPTION_INVOKING + ACCESS + TERMINATE_ACCESS + " "
                    + ex.toString());
            throw new TSMBeanException(EXCEPTION_INVOKING + ACCESS
                    + TERMINATE_ACCESS, ex);
        }
    }

    /**
     * This method will return an instance of the serviceDiscovery interface.
     * 
     * @return an instance of the serviceDiscovery interface.
     * 
     * @exception TSMBeanException
     * @exception IllegalStateException
     */
    public synchronized IpServiceDiscovery obtainDiscoveryInterface()
            throws TSMBeanException {
        if (logger.isDebugEnabled()) {
            logger.debug("TSMBean.obtainDiscoveryInterface() called.");
        }
        IpServiceDiscovery result = null;

        if (getState() == TSMBeanImpl.ACTIVE_STATE) {
            if (serviceDiscovery == null) {
                // Make the parlay call
                try {
                    if (logger.isDebugEnabled()) {
                        logger.debug(INVOKING + ACCESS + OBTAIN_INTERFACE);
                        logger.debug(INTERFACE_NAME
                                + TSMBeanImpl.P_DISCOVERY);
                    }
                    IpInterface ipInterface = access
                            .obtainInterface(TSMBeanImpl.P_DISCOVERY);
                    if (logger.isDebugEnabled()) {
                        logger.debug(INVOKED + ACCESS + OBTAIN_INTERFACE);
                    }

                    serviceDiscovery = IpServiceDiscoveryHelper
                            .narrow(ipInterface);
                    if (logger.isDebugEnabled()) {
                        logger.debug("Narrowed IpServiceDiscovery");
                    }
                }
                catch (TpCommonExceptions ex) {
                    logger.warn(EXCEPTION_INVOKING + ACCESS + OBTAIN_INTERFACE
                            + " " + ex.toString());
                    throw new TSMBeanException(EXCEPTION_INVOKING + ACCESS
                            + OBTAIN_INTERFACE, ex);
                }
                catch (P_ACCESS_DENIED ex) {
                    logger.warn(EXCEPTION_INVOKING + ACCESS + OBTAIN_INTERFACE
                            + " " + ex.toString());
                    throw new TSMBeanException(EXCEPTION_INVOKING + ACCESS
                            + OBTAIN_INTERFACE, ex);
                }
                catch (P_INVALID_INTERFACE_NAME ex) {
                    logger.warn(EXCEPTION_INVOKING + ACCESS + OBTAIN_INTERFACE
                            + " " + ex.toString());
                    throw new TSMBeanException(EXCEPTION_INVOKING + ACCESS
                            + OBTAIN_INTERFACE, ex);
                }
                catch (org.omg.CORBA.SystemException ex) {
                    logger.warn(EXCEPTION_INVOKING + ACCESS + OBTAIN_INTERFACE
                            + " " + ex.toString());
                    throw new TSMBeanException(EXCEPTION_INVOKING + ACCESS
                            + OBTAIN_INTERFACE, ex);
                }
            }
            result = serviceDiscovery;
        }
        else {
            logger
                    .warn("Can only obtainDiscoveryInterface() when in the ACTIVE_STATE state.");
            throw new IllegalStateException(
                    "Can only obtainDiscoveryInterface() when in the ACTIVE_STATE state.");
        }

        return result;
    }

    /**
     * This method will release an instance of the serviceDiscovery interface.
     * 
     * @exception TSMBeanException
     * @exception IllegalStateException
     */
    public synchronized void releaseDiscoveryInterface()
            throws TSMBeanException {
        if (logger.isDebugEnabled()) {
            logger.debug("TSMBean.releaseDiscoverInterface() called.");
        }

        if (getState() == TSMBeanImpl.ACTIVE_STATE) {
            if (serviceDiscovery != null) {
                if (version.equalsIgnoreCase(NULL)) {
                    // Make the parlay call
                    try {
                        if (logger.isDebugEnabled()) {
                            logger.debug(INVOKING + ACCESS + RELEASE_INTERFACE);
                            logger.debug(INTERFACE_NAME
                                    + TSMBeanImpl.P_DISCOVERY);
                        }
                        access.releaseInterface(TSMBeanImpl.P_DISCOVERY);
                        if (logger.isDebugEnabled()) {
                            logger.debug(INVOKED + ACCESS + RELEASE_INTERFACE);
                        }
                    }
                    catch (TpCommonExceptions ex) {
                        logger.warn(EXCEPTION_INVOKING + ACCESS
                                + RELEASE_INTERFACE + " " + ex.toString());
                        throw new TSMBeanException(EXCEPTION_INVOKING + ACCESS
                                + RELEASE_INTERFACE, ex);
                    }
                    catch (P_ACCESS_DENIED ex) {
                        logger.warn(EXCEPTION_INVOKING + ACCESS
                                + RELEASE_INTERFACE + " " + ex.toString());
                        throw new TSMBeanException(EXCEPTION_INVOKING + ACCESS
                                + RELEASE_INTERFACE, ex);
                    }
                    catch (P_INVALID_INTERFACE_NAME ex) {
                        logger.warn(EXCEPTION_INVOKING + ACCESS
                                + RELEASE_INTERFACE + " " + ex.toString());
                        throw new TSMBeanException(EXCEPTION_INVOKING + ACCESS
                                + RELEASE_INTERFACE, ex);
                    }
                    catch (org.omg.CORBA.SystemException ex) {
                        logger.warn(EXCEPTION_INVOKING + ACCESS
                                + RELEASE_INTERFACE + " " + ex.toString());
                        throw new TSMBeanException(EXCEPTION_INVOKING + ACCESS
                                + RELEASE_INTERFACE, ex);
                    }
                }
                else {
                    // Make the parlay call
                    try {
                        if (logger.isDebugEnabled()) {
                            logger.debug(INVOKING + ACCESS
                                    + RELINQUISH_INTERFACE);
                            logger.debug(INTERFACE_NAME
                                    + TSMBeanImpl.P_DISCOVERY);
                        }
                        access.relinquishInterface(TSMBeanImpl.P_DISCOVERY,
                                TERMINATION_TEXT,
                                generateDigitalSignature(TERMINATION_TEXT));
                        if (logger.isDebugEnabled()) {
                            logger.debug(INVOKED + ACCESS
                                    + RELINQUISH_INTERFACE);
                        }
                    }
                    catch (TpCommonExceptions ex) {
                        logger.warn(EXCEPTION_INVOKING + ACCESS
                                + RELINQUISH_INTERFACE + " " + ex.toString());
                        throw new TSMBeanException(EXCEPTION_INVOKING + ACCESS
                                + RELINQUISH_INTERFACE, ex);
                    }
                    catch (P_INVALID_INTERFACE_NAME ex) {
                        logger.warn(EXCEPTION_INVOKING + ACCESS
                                + RELINQUISH_INTERFACE + " " + ex.toString());
                        throw new TSMBeanException(EXCEPTION_INVOKING + ACCESS
                                + RELINQUISH_INTERFACE, ex);
                    }
                    catch (org.omg.CORBA.SystemException ex) {
                        logger.warn(EXCEPTION_INVOKING + ACCESS
                                + RELINQUISH_INTERFACE + " " + ex.toString());
                        throw new TSMBeanException(EXCEPTION_INVOKING + ACCESS
                                + RELINQUISH_INTERFACE, ex);
                    }
                    catch (P_INVALID_SIGNATURE ex) {
                        logger.warn(EXCEPTION_INVOKING + ACCESS
                                + RELINQUISH_INTERFACE + " " + ex.toString());
                        throw new TSMBeanException(EXCEPTION_INVOKING + ACCESS
                                + RELINQUISH_INTERFACE, ex);
                    }
                }
            }
            else {
                logger
                        .warn("There is no serviceDiscovery interface to release.");
                throw new TSMBeanException(
                        "There is no serviceDiscovery interface to release.");
            }
        }
        else {
            logger
                    .warn("Can only releaseDiscoveryInterface when in the ACTIVE_STATE state.");
            throw new IllegalStateException(
                    "Can only releaseDiscoveryInterface when in the ACTIVE_STATE state.");
        }
    }

    /**
     * used to generate a digital signature using the termination text
     * 
     * @param text
     * @return @throws
     *         TSMBeanException
     */
    public byte[] generateDigitalSignature(String text) throws TSMBeanException {

        if (logger.isDebugEnabled()) {
            logger.debug("Generating a digital signature.");
            logger.debug("Text = " + text);
            logger.debug("Algorithm = " + signingAlgorithm);
        }

        RSAPrivateKey privateKey = null;

        //int keySize = 0;
        byte[] result = new byte[0];

        Signature signature = null;

        //If signing algorithm is RSA then generate digital signature
        if ((signingAlgorithm.equals("P_MD5_RSA_512"))
                || (signingAlgorithm.equals("P_MD5_RSA_1024"))) {

            /*if (signingAlgorithm.equals("P_MD5_RSA_512")) {
                keySize = 512;
            }
            else if (signingAlgorithm.equals("P_MD5_RSA_1024")) {
                keySize = 1024;
            }*/

            /*
             * String filename = fileLocation + getClientID() + "RSAPRIVATE" +
             * keySize + ".pem";
             * 
             * try { privateKey = RSAUtil.readPrivateKeyFromPEMFile(filename); }
             * catch (RSAUtilException ex) { logger .error("Exception caught
             * reading private key from pem file"); }
             */


            if (logger.isDebugEnabled()) {
                logger
                        .debug("Using DigestInfo to generate Digital Signature");
            }
            try {
                signature = Signature.getInstance("MD5withRSA");
                signature.initSign(privateKey);

                signature.update(text.getBytes("ISO-8859-1"));
                result = signature.sign();
            }
            catch (NoSuchAlgorithmException ex) {
                logger.error("NoSuchAlgorithmException caught:", ex
                        .fillInStackTrace());
            }
            catch (InvalidKeyException ex) {
                logger.error("InvalidKeyException caught:", ex
                        .fillInStackTrace());
            }
            catch (UnsupportedEncodingException ex) {
                logger.error("UnsupportedEncodingException caught:", ex
                        .fillInStackTrace());
            }
            catch (SignatureException ex) {
                logger.error("SignatureException caught:", ex
                        .fillInStackTrace());
            }
            
        } else { // signing algorithm is not RSA, i.e. could be "" or NULL, etc
            try {
                result = signingAlgorithm.getBytes("ISO-8859-1");
            }
            catch (UnsupportedEncodingException e) {
                logger
                        .error(
                                "UnsupportedEncodingException caught generating digital signature.",
                                e);
            }
        }

        return result;
    }

    /**
     * Select the Signing Algorithm from the signing algorithm capability list
     * 
     * @return @throws
     *         TSMBeanException
     */
    public synchronized String selectSigningAlgorithm() throws TSMBeanException {
        if (logger.isDebugEnabled()) {
            logger.debug("selectSigningAlgorithm()");
        }

        //TSMBean tsmBean = new TSMBean();

        try {
            signingAlgorithm = access.selectSigningAlgorithm(this
                    .getFwProperties().getSigningAlgorithmCapabilityList());
            if (logger.isDebugEnabled()) {
                logger.debug("signing algorithm received: " + signingAlgorithm);
            }
        }
        catch (TpCommonExceptions ex) {
            logger.warn(EXCEPTION_INVOKING + ACCESS + SELECT_SIGNING_ALGORITHM
                    + " " + ex.toString());
            throw new TSMBeanException(EXCEPTION_INVOKING + ACCESS
                    + SELECT_SIGNING_ALGORITHM, ex);
        }
        catch (P_ACCESS_DENIED ex) {
            logger.warn(EXCEPTION_INVOKING + ACCESS + SELECT_SIGNING_ALGORITHM
                    + " " + ex.toString());
            throw new TSMBeanException(EXCEPTION_INVOKING + ACCESS
                    + SELECT_SIGNING_ALGORITHM, ex);
        }
        catch (P_NO_ACCEPTABLE_SIGNING_ALGORITHM ex) {
            logger.warn(EXCEPTION_INVOKING + ACCESS + SELECT_SIGNING_ALGORITHM
                    + " " + ex.toString());
            throw new TSMBeanException(EXCEPTION_INVOKING + ACCESS
                    + SELECT_SIGNING_ALGORITHM, ex);
        }

        return signingAlgorithm;
    }

    /**
     * This method will return a previously created instance of the Servcie
     * Agreement bean.
     * 
     * @return an instance of the SABean.
     */
    public synchronized SABean getSABean() {
        return saBean;
    }

    /**
     * This method will create an instance of the Servcie Agreement bean. If
     * this method has already been called the previously created instance will
     * be returned.
     * 
     * @return an instance of the SABean.
     * 
     * @exception TSMBeanException
     * @exception IllegalStateException
     */
    public synchronized SABean createSABean() throws TSMBeanException {

        if (logger.isDebugEnabled()) {
            logger.debug("TSMBean.createSABean() called.");
        }
        if (getState() == TSMBeanImpl.ACTIVE_STATE) {
            if (saBean == null) {
                // Create the bean
                try {
                    saBean = new SABeanImpl(this);
                    saBean.initialise();
                } catch (SABeanException e) {
                    logger.error("Exception creating SABean" + e);
                    throw new TSMBeanException("Exception creating SABean", e);
                }

                // Get the callback impl
                IpAppServiceAgreementManagement appSam = saBean
                        .getAppServiceAgreementManagement();

                // For the returned interface
                IpServiceAgreementManagement sam = null;

                try {
                    // Make the parlay call
                    if (logger.isDebugEnabled()) {
                        logger.debug(INVOKING + ACCESS
                                + OBTAIN_INTERFACE_WITH_CALLBACK);
                        logger.debug(INTERFACE_NAME
                                + TSMBeanImpl.P_SERVICE_AGREEMENT_MANAGEMENT);
                    }
                    IpInterface ipInterface = access
                            .obtainInterfaceWithCallback(
                                    TSMBeanImpl.P_SERVICE_AGREEMENT_MANAGEMENT,
                                    appSam);
                    if (logger.isDebugEnabled()) {
                        logger.debug(INVOKED + ACCESS
                                + OBTAIN_INTERFACE_WITH_CALLBACK);
                    }
                    sam = IpServiceAgreementManagementHelper
                            .narrow(ipInterface);

                    if (logger.isDebugEnabled()) {
                        logger.debug("Narrowed IpServiceAgreementManagement");
                    }

                }
                catch (TpCommonExceptions ex) {
                    saBean.cleanup();
                    saBean = null;
                    logger.warn(EXCEPTION_INVOKING + ACCESS
                            + OBTAIN_INTERFACE_WITH_CALLBACK + " "
                            + ex.toString());
                    throw new TSMBeanException(EXCEPTION_INVOKING + ACCESS
                            + OBTAIN_INTERFACE_WITH_CALLBACK, ex);
                }
                catch (P_ACCESS_DENIED ex) {
                    saBean.cleanup();
                    saBean = null;
                    logger.warn(EXCEPTION_INVOKING + ACCESS
                            + OBTAIN_INTERFACE_WITH_CALLBACK + " "
                            + ex.toString());
                    throw new TSMBeanException(EXCEPTION_INVOKING + ACCESS
                            + OBTAIN_INTERFACE_WITH_CALLBACK, ex);
                }
                catch (P_INVALID_INTERFACE_NAME ex) {
                    saBean.cleanup();
                    saBean = null;
                    logger.warn(EXCEPTION_INVOKING + ACCESS
                            + OBTAIN_INTERFACE_WITH_CALLBACK + " "
                            + ex.toString());
                    throw new TSMBeanException(EXCEPTION_INVOKING + ACCESS
                            + OBTAIN_INTERFACE_WITH_CALLBACK, ex);
                }
                catch (P_INVALID_INTERFACE_TYPE ex) {
                    saBean.cleanup();
                    saBean = null;
                    logger.warn(EXCEPTION_INVOKING + ACCESS
                            + OBTAIN_INTERFACE_WITH_CALLBACK + " "
                            + ParlayExceptionUtil.stringify(ex));
                    throw new TSMBeanException(EXCEPTION_INVOKING + ACCESS
                            + OBTAIN_INTERFACE_WITH_CALLBACK, ex);
                }
                catch (org.omg.CORBA.SystemException ex) {
                    saBean.cleanup();
                    saBean = null;
                    logger.warn(EXCEPTION_INVOKING + ACCESS
                            + OBTAIN_INTERFACE_WITH_CALLBACK + " "
                            + ex.toString());
                    throw new TSMBeanException(EXCEPTION_INVOKING + ACCESS
                            + OBTAIN_INTERFACE_WITH_CALLBACK, ex);
                }

                // Store the SAM interface in the bean
                saBean.setServiceAgreementManagement(sam);
            }
        }
        else {
            logger.warn("Can only getSABean when in the ACTIVE_STATE state.");
            throw new IllegalStateException(
                    "Can only getSABean when in the ACTIVE_STATE state.");
        }

        return saBean;
    }

    /**
     * This method will release an instance of the Service Agreement bean.
     * 
     * @exception TSMBeanException
     * @exception IllegalStateException
     */
    public synchronized void destroySABean() {
        if (logger.isDebugEnabled()) {
            logger.debug("TSMBean.destroySABean() called.");
        }

        if (saBean != null) {
            try {
                // Make the parlay call
                if (logger.isDebugEnabled()) {
                    logger.debug(INVOKING + ACCESS + RELEASE_INTERFACE);
                    logger.debug(INTERFACE_NAME
                            + TSMBeanImpl.P_SERVICE_AGREEMENT_MANAGEMENT);
                }
                access
                        .releaseInterface(TSMBeanImpl.P_SERVICE_AGREEMENT_MANAGEMENT);
                if (logger.isDebugEnabled()) {
                    logger.debug(INVOKED + ACCESS + RELEASE_INTERFACE);
                }
            }
            catch (TpCommonExceptions ex) {
                logger.warn(EXCEPTION_INVOKING + ACCESS + RELEASE_INTERFACE
                        + " " + ex.toString());
            }
            catch (P_ACCESS_DENIED ex) {
                logger.warn(EXCEPTION_INVOKING + ACCESS + RELEASE_INTERFACE
                        + " " + ex.toString());
            }
            catch (P_INVALID_INTERFACE_NAME ex) {
                logger.warn(EXCEPTION_INVOKING + ACCESS + RELEASE_INTERFACE
                        + " " + ex.toString());
            }
            catch (org.omg.CORBA.SystemException ex) {
                logger.warn(EXCEPTION_INVOKING + ACCESS + RELEASE_INTERFACE
                        + " " + ex.toString());
            }

            saBean.cleanup();
            saBean = null;
        }
        else {
            if (logger.isDebugEnabled()) {
                logger.warn("No SABean exists for this class.");
            }
        }
    }

    /**
     * Removes an application listener from the list of those registered on this
     * bean.
     * 
     * @param l
     *            The PCP User Application's implementation of the
     *            TSMBeanListener interface.
     */
    public synchronized void removeTSMBeanListener(TSMBeanListener l) {
        if (TSMBeanListeners != null && TSMBeanListeners.contains(l)) {
            Vector v = (Vector) TSMBeanListeners.clone();
            v.removeElement(l);
            TSMBeanListeners = v;
        }
    }

    /**
     * Adds an application listener to the list of those registered on this
     * bean.
     * 
     * @param l
     *            The PCP User Application's implementation of the
     *            TSMBeanListener interface.
     */
    public synchronized void addTSMBeanListener(TSMBeanListener l) {
        Vector v = TSMBeanListeners == null ? new Vector(2)
                : (Vector) TSMBeanListeners.clone();
        if (!v.contains(l)) {
            v.addElement(l);
            TSMBeanListeners = v;
        }
    }

//    /**
//     * Returns a hash code value for the object.
//     * 
//     * @return a hash code value for this object.
//     */
//    public int hashCode() {
//        int result = Integer.MIN_VALUE;
//        String undefined = "UNDEFINED";
//
//        /*
//         * // Hash is the combined has of domain ID. if
//         * (orbProperties.getFwLocation() != null) { if (getClientID() != null &&
//         * clientDomainID != null && orbProperties.getFwLocation() != null &&
//         * fwProperties != null) { try { result = getClientID().hashCode() ^
//         * orbProperties.getFwLocation().hashCode() ^
//         * clientDomainID.getDiscriminator().getValue() ^
//         * fwProperties.getInstanceID(); } catch (InvalidUnionAccessorException
//         * ex) { result = 1; // will never happen if clientDomainID is
//         * implemented // correctly } } else { result =
//         * orbProperties.getFwLocation().hashCode() ^ undefined.hashCode(); } }
//         * else { result = 1; }
//         */
//
//        return result;
//    }

    /**
     * This method will clean up all internal object references with the
     * exception of registered listeners which are the responsibility of the
     * application.
     */
    public synchronized void cleanup() {
        if (!clean) {
            if (logger.isDebugEnabled()) {
                logger.debug("Cleaning up " + toString());
            }
            tpAuthDomain = null;
            clientDomainID = null;
            state = INVALID_STATE;

            // object references
            serviceDiscovery = null;
            initial = null;
            access = null;

            // callback impls
            try {

                if (ipClientAPILevelAuthenticationImpl != null) {
                    ServantActivationHelper
                            .deactivateServant(ipClientAPILevelAuthenticationImpl);
                    ipClientAPILevelAuthenticationImpl = null;
                    ipClientAPILevelAuthenticationPOA = null;
                    ipClientAPILevelAuthentication = null;
                }

                if (ipClientAccessImpl != null) {
                    ServantActivationHelper
                            .deactivateServant(ipClientAccessImpl);
                    ipClientAccessImpl = null;
                }
                
                if (saBean != null) {
                    saBean.cleanup();
                    saBean = null;
                }

            }
            catch (ObjectNotActive e) {
                logger.error("Exception deactivating callbacks", e);
            }
            catch (WrongPolicy e) {
                logger.error("Exception deactivating callbacks", e);
            }
            catch (ServantNotActive e) {
                logger.error("Exception deactivating callbacks", e);
            }

            if (authHandler != null) {
                authHandler.cleanup();
            }
            
            
            try {
                RSAUtil.saveKeyStore(fwProperties.getCertificateVault());
            } catch (RSAUtilException e) {
                logger.error("Unable to save keyStore", e);
            }

            // make sure this method is not called again
            clean = true;
        }
    }

    /**
     * Method fireTerminateAccess.
     * 
     * @param e
     */
    public void fireTerminateAccess(TerminateAccessEvent e) {
        if (TSMBeanListeners != null) {
            Vector listeners = TSMBeanListeners;
            int count = listeners.size();
            for (int i = 0; i < count; i++) {
                try {
                    ((TSMBeanListener) listeners.elementAt(i))
                            .terminateAccess(e);
                }
                catch (RuntimeException ex) {
                    logger
                            .error("Caught exception invoking application TSMBeanListener"
                                    + " " + ex.toString());
                }
            }
        }
    }

    public Executor getEventsQueue() {
        return eventsQueue;
    }

    /**
     * Method traceSecurityProviders.
     */
    private void traceSecurityProviders() {
        if (logger.isDebugEnabled()) {
            logger.debug("Installed security providers providers:");
            Provider aprovider[] = Security.getProviders();
            for (int i = 0; i < aprovider.length; i++) {
                Provider provider = aprovider[i];
                logger.debug("Provider " + (i + 1) + ": " + provider.getName()
                        + "  version: " + provider.getVersion());
            }
        }
    }

    /**
     * @param newAuthenticationMechanism
     */
    public void setAuthenticationSequence(
            AuthenticationSequence newAuthenticationMechanism)
            throws TSMBeanException {
        if (newAuthenticationMechanism.equals(
                AuthenticationSequence.TWO_WAY)) {
            fwAuthenticationSucceeded = false;
        }
        else if (newAuthenticationMechanism.equals(
                AuthenticationSequence.ONE_WAY)) {
            fwAuthenticationSucceeded = true;
        }
        else if (newAuthenticationMechanism.equals(
                AuthenticationSequence.TRUSTED)) {
            fwAuthenticationSucceeded = false;
        }
        else {
            throw new TSMBeanException(newAuthenticationMechanism
                    + " is not a valid authentication mechanism.");
        }

    }

    /**
     * @return Returns the fwProperties.
     */
    public FwSessionProperties getFwProperties() {
        return fwProperties;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.fw.access.TSMBeanInterface#setFwProperties(org.mobicents.slee.resource.parlay.fw.FwSessionProperties)
     */
    public void setFwProperties(FwSessionProperties properties) {
        fwProperties = properties;

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.fw.access.TSMBeanInterface#getOrbHandler()
     */
    public ORBHandler getOrbHandler() {
        return orbHandler;
    }

    /**
     * @param authHandler The authHandler to set.
     */
    public void setAuthHandler(AuthenticationHandler authHandler) {
        this.authHandler = authHandler;
    }

    /**
     * @param tpAuthDomain The tpAuthDomain to set.
     */
    public void setTpAuthDomain(TpAuthDomain tpAuthDomain) {
        this.tpAuthDomain = tpAuthDomain;
    }
}