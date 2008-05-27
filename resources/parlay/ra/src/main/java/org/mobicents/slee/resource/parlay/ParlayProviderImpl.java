package org.mobicents.slee.resource.parlay;

import javax.resource.ResourceException;
import javax.resource.cci.ConnectionFactory;
import javax.resource.spi.ResourceAdapterInternalException;
import javax.slee.Address;
import javax.slee.AddressPlan;
import javax.slee.resource.BootstrapContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mobicents.csapi.jr.slee.ParlayConnection;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.GccsListener;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.GccsListenerImpl;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.MpccsListener;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.MpccsListenerImpl;
import org.mobicents.slee.resource.parlay.jca.ManagedConnectionFactoryImpl;
import org.mobicents.slee.resource.parlay.jca.ResourceAdapter;
import org.mobicents.slee.resource.parlay.jca.ResourceAdapterImpl;
import org.mobicents.slee.resource.parlay.util.activity.ActivityManager;
import org.mobicents.slee.resource.parlay.util.event.EventSender;
import org.mobicents.slee.resource.parlay.util.event.EventSenderImpl;

/**
 * This class is responsible for initialising all resources for the Parlay
 * connection. It also proxies connection allocation requests to the RA
 * connection pooling internals.
 */
public class ParlayProviderImpl implements ParlayProvider {

    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory
            .getLog(ParlayProviderImpl.class);

    public ParlayProviderImpl(BootstrapContext bootstrapContext,
            ParlayResourceAdaptorProperties adaptorProperties,
            ActivityManager activityManager) {
        super();
        if (logger.isDebugEnabled()) {
            logger.debug("Creating ParlayProvider.");
            logger.debug("Properties = " + adaptorProperties);
        }

        this.bootstrapContext = bootstrapContext;
        this.activityManager = activityManager;

        resourceAdapterImpl = new ResourceAdapterImpl();
        resourceAdapterImpl.setAuthenticationSequence(adaptorProperties
                .getAuthenticationSequence());
        resourceAdapterImpl.setDomainID(adaptorProperties.getDomainID());
        resourceAdapterImpl
                .setIpInitialIOR(adaptorProperties.getIpInitialIOR());
        resourceAdapterImpl.setIpInitialLocation(adaptorProperties
                .getIpInitialLocation());
        resourceAdapterImpl
                .setIpInitialURL(adaptorProperties.getIpInitialURL());
        resourceAdapterImpl.setNamingServiceIOR(adaptorProperties
                .getNamingServiceIOR());
        resourceAdapterImpl.setParlayVersion(adaptorProperties.getParlayVersion());
        resourceAdapterImpl.setSharedSecret(adaptorProperties.getSharedSecret());

        managedConnectionFactory = new ManagedConnectionFactoryImpl();
        managedConnectionFactory.setAuthenticationSequence(adaptorProperties
                .getAuthenticationSequence());
        managedConnectionFactory.setDomainID(adaptorProperties.getDomainID());
        managedConnectionFactory.setIpInitialIOR(adaptorProperties
                .getIpInitialIOR());
        managedConnectionFactory.setIpInitialLocation(adaptorProperties
                .getIpInitialLocation());
        managedConnectionFactory.setIpInitialURL(adaptorProperties
                .getIpInitialURL());
        managedConnectionFactory.setNamingServiceIOR(adaptorProperties
                .getNamingServiceIOR());
    }

    private final transient ManagedConnectionFactoryImpl managedConnectionFactory;

    private transient ResourceAdapter resourceAdapterImpl = null;

    private transient ConnectionFactory connectionFactory = null;

    private final transient BootstrapContext bootstrapContext;

    private final transient ActivityManager activityManager;

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.ParlayProvider#start()
     */
    public void start() throws javax.slee.resource.ResourceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Starting ParlayProvider.");
        }

        // TODO sort out default address for events.
        final Address address = new Address(AddressPlan.SIP, "127.0.0.1");
        final EventSender eventSender = new EventSenderImpl(bootstrapContext
                .getEventLookupFacility(), bootstrapContext.getSleeEndpoint(),
                address);

        // Register listeners, will be one per service type in future
        final MpccsListener mpccsListener = new MpccsListenerImpl(eventSender);
        resourceAdapterImpl.setMpccsListener(mpccsListener);
        
        final GccsListener gccsListener = new GccsListenerImpl(eventSender);
        resourceAdapterImpl.setGccsListener(gccsListener);
        

        // Handles maps of activities, abstracts slee interface from service
        // logic.
        resourceAdapterImpl.setActivityManager(activityManager);

        try {
            managedConnectionFactory.setResourceAdapter(resourceAdapterImpl);
        }
        catch (ResourceException e) {
            logger
                    .error(
                            "Failed to setResourceAdapter() on managedConnectionFactory",
                            e);
            throw new javax.slee.resource.ResourceException(
                    "Failed to setResourceAdapter() on managedConnectionFactory",
                    e);
        }

        startResourceAdapter();

        try {
            connectionFactory = (ConnectionFactory) managedConnectionFactory
                    .createConnectionFactory();
        }
        catch (ResourceException e) {
            logger
                    .error(
                            "Failed to createConnectionFactory() on managedConnectionFactory",
                            e);
            throw new javax.slee.resource.ResourceException(
                    "Failed to createConnectionFactory() on managedConnectionFactory",
                    e);
        }
    }

    /**
     * @throws javax.slee.resource.ResourceException
     */
    protected void startResourceAdapter()
            throws javax.slee.resource.ResourceException {
        try {
            resourceAdapterImpl.start(null);
        }
        catch (ResourceAdapterInternalException e) {
            logger.error("Failed to start() on resourceAdapterImpl", e);
            throw new javax.slee.resource.ResourceException(
                    "Failed to start provider, internal resource error");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.ParlayResourceAdapterSbbInterface#getParlayConnection()
     */
    public ParlayConnection getParlayConnection()
            throws javax.slee.resource.ResourceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Attempting to getParlayConnection()");
        }

        if (connectionFactory == null) {
            throw new javax.slee.resource.ResourceException(
                    "Cannot allocate connection - no ConnectionFactory");
        }

        try {
            return (ParlayConnection) connectionFactory.getConnection();
        }
        catch (ResourceException e) {
            logger.error("Failed to getParlayConnection()", e);
            throw new javax.slee.resource.ResourceException(
                    "Failed to getParlayConnection()", e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.ParlayProvider#stop()
     */
    public void stop() throws javax.slee.resource.ResourceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Stopping ParlayProvider.");
        }

        connectionFactory = null;

        resourceAdapterImpl.stop();

        try {
            managedConnectionFactory.setResourceAdapter(null);
        }
        catch (ResourceException e) {
            if (logger.isDebugEnabled()) {
                logger.debug("Problem stopping Parlay Resources.", e);
            }
        }
    }

    /**
     * @param resourceAdapterImpl
     *            The resourceAdapterImpl to set.
     */
    public void setResourceAdapterImpl(final ResourceAdapter resourceAdapterImpl) {
        this.resourceAdapterImpl = resourceAdapterImpl;
    }

    /**
     * @param connectionFactory
     *            The connectionFactory to set.
     */
    public void setConnectionFactory(final ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }
}