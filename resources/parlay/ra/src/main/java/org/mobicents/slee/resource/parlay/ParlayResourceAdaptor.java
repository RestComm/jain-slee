package org.mobicents.slee.resource.parlay;

import java.io.Serializable;
import java.util.Properties;

import javax.naming.NamingException;
import javax.slee.Address;
import javax.slee.resource.ActivityHandle;
import javax.slee.resource.BootstrapContext;
import javax.slee.resource.FailureReason;
import javax.slee.resource.Marshaler;
import javax.slee.resource.ResourceAdaptor;
import javax.slee.resource.ResourceAdaptorTypeID;
import javax.slee.resource.ResourceException;
import javax.slee.resource.SleeEndpoint;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.resource.ResourceAdaptorActivityContextInterfaceFactory;
import org.mobicents.slee.resource.ResourceAdaptorEntity;
import org.mobicents.slee.resource.parlay.util.activity.ActivityManager;
import org.mobicents.slee.resource.parlay.util.activity.ActivityManagerImpl;

/**
 * This class will be created by the Slee container on deployment of the RA.
 */
public class ParlayResourceAdaptor implements ResourceAdaptor, Serializable {

    private static final String SERVICE_KEY = "serviceKey = ";

    private static final String FLAGS = "flags = ";

    private static final String ADDRESS = "address = ";

    private static final String PARAM = "param = ";

    private static final String OBJ = "obj = ";

    private static final String ACTIVITY_HANDLE = "activityHandle = ";

    public ParlayResourceAdaptor(Properties properties) {
        super();
        if (logger.isDebugEnabled()) {
            logger.debug("ParlayResourceAdaptor(Properties properties) called.");
        }
        
        adaptorProperties = new ParlayResourceAdaptorProperties();
        try {
            adaptorProperties.load(properties);
        }
        catch (ResourceException e) {
            logger
                    .error("Failed to load specified properties. Loading defaults ...");
            try {
                adaptorProperties.loadDefaults();
            }
            catch (ResourceException e1) {
                logger.fatal("Failed to load default properties.");
            }
        }
        logger.info(CREATED_PARLAY_RESOURCE_ADAPTER);
    }

    public ParlayResourceAdaptor(String ipInitialIOR, String namingServiceIOR,
            String ipInitialLocation, String ipInitialURL, String domainID,
            String authenticationSequence) {
        super();
        if (logger.isDebugEnabled()) {
            logger.debug("ParlayResourceAdaptor(String ipInitialIOR, " +
            		"String namingServiceIOR, String ipInitialLocation, " +
            		"String ipInitialURL, String domainID, String authenticationSequence) " +
            		"called.");
        }

        adaptorProperties = new ParlayResourceAdaptorProperties();
        adaptorProperties.setIpInitialIOR(ipInitialIOR);
        adaptorProperties.setNamingServiceIOR(namingServiceIOR);
        adaptorProperties.setIpInitialLocation(ipInitialLocation);
        adaptorProperties.setIpInitialURL(ipInitialURL);

        if (!adaptorProperties.isIpInitialConfigValid()) {
            logger.error("Invalid properties. Loading defaults ...");
            try {
                adaptorProperties.loadDefaults();
            }
            catch (ResourceException e1) {
                logger.fatal("Failed to load default properties.");
            }
        }

        adaptorProperties.setDomainID(domainID);
        adaptorProperties.setAuthenticationSequence(authenticationSequence);
        logger.info(CREATED_PARLAY_RESOURCE_ADAPTER);
    }

    public ParlayResourceAdaptor() {
        super();
        if (logger.isDebugEnabled()) {
            logger.debug("ParlayResourceAdaptor() called.");
        }
        
        adaptorProperties = new ParlayResourceAdaptorProperties();
        try {
            adaptorProperties.loadDefaults();
        }
        catch (ResourceException e) {
            logger.fatal("Failed to load default properties.");
        }
        logger.info(CREATED_PARLAY_RESOURCE_ADAPTER);
    }

    public static final String VENDOR = "org.mobicents";

    public static final String VERSION = "4.2";

    private static final String CREATED_PARLAY_RESOURCE_ADAPTER = "Created Parlay Resource Adapter";

    private transient BootstrapContext bootstrapContext = null;

    private transient SleeEndpoint sleeEndpoint = null;

    private transient ParlayActivityContextInterfaceFactoryImpl activityConextInterfaceFactory = null;

    private transient ParlayProvider parlayProvider = null;

    private final transient ParlayResourceAdaptorProperties adaptorProperties;

    private transient ActivityManager activityManager = null;
    
    private transient Marshaler marshaler = null;

    private static final Log logger = LogFactory.getLog(ParlayResourceAdaptor.class);

    /*
     * (non-Javadoc)
     * 
     * @see javax.slee.resource.ResourceAdaptor#entityCreated(javax.slee.resource.BootstrapContext)
     */
    public void entityCreated(final BootstrapContext bootstrapContext)
            throws ResourceException {
        
        if(logger.isDebugEnabled()) {
            logger.debug("ParlayResourceAdapter.entityCreated()");
        }
        
        this.bootstrapContext = bootstrapContext;
        this.sleeEndpoint = bootstrapContext.getSleeEndpoint();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.slee.resource.ResourceAdaptor#entityRemoved()
     */
    public void entityRemoved() {
        
        if(logger.isDebugEnabled()) {
            logger.debug("ParlayResourceAdapter.entityRemoved()");
        }
        
        this.bootstrapContext = null;
        this.sleeEndpoint = null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.slee.resource.ResourceAdaptor#entityActivated()
     */
    public void entityActivated() throws ResourceException {
        // initialisation goes here
        
        if(logger.isDebugEnabled()) {
            logger.debug("ParlayResourceAdapter.entityActivated()");
        }

        try {
            initializeRAActivityContextInterfaceFactory();
        }
        catch (Exception e) {
            logger
                    .error("Failed to init ResourceAdapterActivityContextInterfaceFactory");
            throw new ResourceException(
                    "Failed to init ResourceAdapterActivityContextInterfaceFactory",
                    e);
        }

        registerRAActivityContextInterfaceFactoryWithJndi((ResourceAdaptorActivityContextInterfaceFactory) activityConextInterfaceFactory);

        activityManager = new ActivityManagerImpl(sleeEndpoint);

        parlayProvider = new ParlayProviderImpl(bootstrapContext,
                adaptorProperties, activityManager);
        parlayProvider.start();
        
        marshaler = new ParlayMarshaler();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.slee.resource.ResourceAdaptor#entityDeactivating()
     */
    public void entityDeactivating() {
        
        if(logger.isDebugEnabled()) {
            logger.debug("ParlayResourceAdapter.entityDeactivating()");
        }

        try {
            // Resource deallocation goes here
            parlayProvider.stop();
        }
        catch (ResourceException e) {
            logger.error("Problem stopping Parlay Provider.", e);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.slee.resource.ResourceAdaptor#entityDeactivated()
     */
    public void entityDeactivated() {
        
        if(logger.isDebugEnabled()) {
            logger.debug("ParlayResourceAdapter.entityDeactivated()");
        }

        unRegisterRAActivityContextInterfaceFactoryWithJndi((ResourceAdaptorActivityContextInterfaceFactory) activityConextInterfaceFactory);

        
        if(logger.isDebugEnabled()) {
            logger.debug("ParlayResourceAdapter stopped.");
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.slee.resource.ResourceAdaptor#eventProcessingSuccessful(javax.slee.resource.ActivityHandle,
     *      java.lang.Object, int, javax.slee.Address, int)
     */
    public void eventProcessingSuccessful(final ActivityHandle activityHandle,
            final Object obj, final int param, final Address address, final int flags) {
        
        if(logger.isDebugEnabled()) {
            logger.debug("ParlayResourceAdapter.eventProcessingSuccessful()");
            logger.debug(ACTIVITY_HANDLE + activityHandle);
            logger.debug(OBJ + obj);
            logger.debug(PARAM + param);
            logger.debug(ADDRESS + address);
            logger.debug(FLAGS + flags);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.slee.resource.ResourceAdaptor#eventProcessingFailed(javax.slee.resource.ActivityHandle,
     *      java.lang.Object, int, javax.slee.Address, int,
     *      javax.slee.resource.FailureReason)
     */
    public void eventProcessingFailed(final ActivityHandle activityHandle,
            final Object obj, final int param, final Address address, final int flags,
            final FailureReason failureReason) {
        
        if(logger.isDebugEnabled()) {
            logger.debug("ParlayResourceAdapter.eventProcessingFailed()");
            logger.debug(ACTIVITY_HANDLE + activityHandle);
            logger.debug(OBJ + obj);
            logger.debug(PARAM + param);
            logger.debug(ADDRESS + address);
            logger.debug(FLAGS + flags);
            logger.debug("failureReason = " + failureReason);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.slee.resource.ResourceAdaptor#activityEnded(javax.slee.resource.ActivityHandle)
     */
    public void activityEnded(final ActivityHandle activityHandle) {
        if(logger.isDebugEnabled()) {
            logger.debug("ParlayResourceAdapter.activityEnded()");
            logger.debug(ACTIVITY_HANDLE + activityHandle);
        }
        
        activityManager.activityEnded(activityHandle);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.slee.resource.ResourceAdaptor#activityUnreferenced(javax.slee.resource.ActivityHandle)
     */
    public void activityUnreferenced(final ActivityHandle activityHandle) {
        if(logger.isDebugEnabled()) {
            logger.debug("ParlayResourceAdapter.activityUnreferenced()");
            logger.debug(ACTIVITY_HANDLE + activityHandle);
        }
        
        activityManager.activityUnreferenced(activityHandle);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.slee.resource.ResourceAdaptor#queryLiveness(javax.slee.resource.ActivityHandle)
     */
    public void queryLiveness(final ActivityHandle activityHandle) {
        if(logger.isDebugEnabled()) {
            logger.debug("ParlayResourceAdapter.queryLiveness()");
            logger.debug(ACTIVITY_HANDLE + activityHandle);
        }
        
        activityManager.queryLiveness(activityHandle);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.slee.resource.ResourceAdaptor#getActivity(javax.slee.resource.ActivityHandle)
     */
    public Object getActivity(final ActivityHandle handle) {
        return activityManager.getActivity(handle);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.slee.resource.ResourceAdaptor#getActivityHandle(java.lang.Object)
     */
    public ActivityHandle getActivityHandle(final Object arg0) {
        return activityManager.getActivityHandle(arg0);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.slee.resource.ResourceAdaptor#getSBBResourceAdaptorInterface(java.lang.String)
     */
    public Object getSBBResourceAdaptorInterface(final String arg0) {
        return parlayProvider;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.slee.resource.ResourceAdaptor#getMarshaler()
     */
    public Marshaler getMarshaler() {
        return marshaler;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.slee.resource.ResourceAdaptor#serviceInstalled(java.lang.String,
     *      int[], java.lang.String[])
     */
    public void serviceInstalled(final String serviceKey, final int[] eventIDs, final String[] serviceOptions) {

        if(logger.isDebugEnabled()) {
            logger.debug("ParlayResourceAdapter.serviceInstalled()");
            logger.debug(SERVICE_KEY + serviceKey);
            for (int i = 0; i < eventIDs.length; i++) {
                logger.debug("eventIDs[" + i + "] = " + eventIDs[i]);
            }
            for (int i = 0; i < serviceOptions.length; i++) {

                logger.debug("serviceOptions[" + i + "] = " + serviceOptions[i]);
            }
        }
        // Used by SLEE to tell RA a service is interested in the specified event set.
        
        // RA should only fire events in these sets.
        
        // From testing it appears mobicents slee doesn't fire in an event array
        // so can't support
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.slee.resource.ResourceAdaptor#serviceUninstalled(java.lang.String)
     */
    public void serviceUninstalled(final String serviceKey) {
        if(logger.isDebugEnabled()) {
            logger.debug("ParlayResourceAdapter.serviceUninstalled()");
            logger.debug(SERVICE_KEY + serviceKey);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.slee.resource.ResourceAdaptor#serviceActivated(java.lang.String)
     */
    public void serviceActivated(final String serviceKey) {

        // Service ready to receive events
        if(logger.isDebugEnabled()) {
            logger.debug("ParlayResourceAdapter.serviceActivated()");
            logger.debug(SERVICE_KEY + serviceKey);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.slee.resource.ResourceAdaptor#serviceDeactivated(java.lang.String)
     */
    public void serviceDeactivated(final String serviceKey) {
        if(logger.isDebugEnabled()) {
            logger.debug("ParlayResourceAdapter.serviceDeactivated()");
            logger.debug(SERVICE_KEY + serviceKey);
        }

    }

    // set up the JNDI naming context
    private void initializeRAActivityContextInterfaceFactory()
            throws Exception {

        if (logger.isDebugEnabled()) {
            logger.debug("Initializing RA ActivityContextInterfaceFactory");
        }

        // get the reference to the SLEE container from JNDI
        final SleeContainer container = SleeContainer.lookupFromJndi();
        // get the entities name
        final String entityName = bootstrapContext.getEntityName();

        // Get RA Entity
        final ResourceAdaptorEntity resourceAdaptorEntity = container.getResourceManagement().getResourceAdaptorEntity(entityName);
        // Get RA Type ID
        final ResourceAdaptorTypeID raTypeId = resourceAdaptorEntity
                .getInstalledResourceAdaptor().getRaType()
                .getResourceAdaptorTypeID();

        // create the ActivityContextInterfaceFactory
        activityConextInterfaceFactory = new ParlayActivityContextInterfaceFactoryImpl(
                resourceAdaptorEntity.getServiceContainer(), entityName);

        // store the mapping in the service container
        resourceAdaptorEntity.getServiceContainer().getResourceManagement()
                .getActivityContextInterfaceFactories().put(raTypeId,
                        activityConextInterfaceFactory);
    }

    /**
     * Registers the activityConextInterfaceFactory with the SleeContainer JNDI
     * tree.
     *  
     */
    private void registerRAActivityContextInterfaceFactoryWithJndi(
            final ResourceAdaptorActivityContextInterfaceFactory factory) {

        if (logger.isDebugEnabled()) {
            logger
                    .debug("Registering ResourceAdaptorActivityContextInterfaceFactory in JNDI");
        }

        try {
            if (factory != null) {

                // string format == java:<prefix>/<name>

                final String jndiName = factory.getJndiName();

                final int begind = jndiName.indexOf(':');
                final int toind = jndiName.lastIndexOf('/');
                final String prefix = jndiName.substring(begind + 1, toind);
                final String name = jndiName.substring(toind + 1);

                if (logger.isDebugEnabled()) {
                    logger.debug("jndiName prefix =" + prefix + "; jndiName = "
                            + name);
                }

                SleeContainer.registerWithJndi(prefix, name,
                        this.activityConextInterfaceFactory);
            }
        }
        catch (IndexOutOfBoundsException e) {
            logger
                    .error(
                            "Failed to register ResourceAdaptorActivityContextInterfaceFactory in JNDI",
                            e);
        }
    }

    /**
     * 
     * Unregisters the activityConextInterfaceFactory with the SleeContainer
     * JNDI tree.
     */
    private void unRegisterRAActivityContextInterfaceFactoryWithJndi(
            final ResourceAdaptorActivityContextInterfaceFactory factory) {
        if (factory != null) {

            // string format == java:<prefix>/<name>

            final String jndiName = factory.getJndiName();

            //remove "java:" prefix
            final int begind = jndiName.indexOf(':');
            final int toind = jndiName.lastIndexOf('/');
            final String prefix = jndiName.substring(begind + 1, toind);
            final String name = jndiName.substring(toind + 1);

            if (logger.isDebugEnabled()) {
                logger.debug("JNDI name to unregister: prefix =" + prefix
                        + "; jndiName = " + name);
            }

            SleeContainer.unregisterWithJndi(prefix, name);

            if (logger.isDebugEnabled()) {
                logger.debug("JNDI name unregistered.");
            }
        }
    }

}