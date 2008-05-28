package org.mobicents.slee.resource.parlay.jca;

import java.io.PrintWriter;
import java.util.Set;

import javax.resource.ResourceException;
import javax.resource.spi.ConnectionManager;
import javax.resource.spi.ConnectionRequestInfo;
import javax.resource.spi.ManagedConnection;
import javax.resource.spi.ManagedConnectionFactory;
import javax.resource.spi.ResourceAdapter;
import javax.resource.spi.ResourceAdapterAssociation;
import javax.security.auth.Subject;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * Factory class for all managed connections
 */
public final class ManagedConnectionFactoryImpl implements
        ManagedConnectionFactory, ResourceAdapterAssociation {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory
            .getLog(ManagedConnectionFactoryImpl.class);

    /*
     * (non-Javadoc)
     * 
     * @see javax.resource.spi.ManagedConnectionFactory#createConnectionFactory(javax.resource.spi.ConnectionManager)
     */
    public Object createConnectionFactory(ConnectionManager connectionManager)
            throws ResourceException {

        return new ConnectionFactoryImpl(this, connectionManager);
    }

    private transient PrintWriter logWriter = null;

    private transient ResourceAdapter resourceAdapter = null;

    private transient String ipInitialIOR;

    private transient String domainID;

    private transient String authenticationSequence;

    private transient String namingServiceIOR;

    private transient String ipInitialLocation;

    private transient String ipInitialURL;

    /**
     * @return Returns the ipInitialLocation.
     */
    public String getIpInitialLocation() {
        return ipInitialLocation;
    }
    /**
     * @param ipInitialLocation The ipInitialLocation to set.
     */
    public void setIpInitialLocation(String ipInitialLocation) {
        this.ipInitialLocation = ipInitialLocation;
    }
    /**
     * @return Returns the ipInitialURL.
     */
    public String getIpInitialURL() {
        return ipInitialURL;
    }
    /**
     * @param ipInitialURL The ipInitialURL to set.
     */
    public void setIpInitialURL(String ipInitialURL) {
        this.ipInitialURL = ipInitialURL;
    }
    /**
     * @return Returns the namingServiceIOR.
     */
    public String getNamingServiceIOR() {
        return namingServiceIOR;
    }
    /**
     * @param namingServiceIOR The namingServiceIOR to set.
     */
    public void setNamingServiceIOR(String namingServiceIOR) {
        this.namingServiceIOR = namingServiceIOR;
    }
    /*
     * (non-Javadoc)
     * 
     * @see javax.resource.spi.ManagedConnectionFactory#createConnectionFactory()
     */
    public Object createConnectionFactory() throws ResourceException {

        ConnectionManager connectionManager = new ConnectionManagerImpl();

        return new ConnectionFactoryImpl(this, connectionManager);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.resource.spi.ManagedConnectionFactory#createManagedConnection(javax.security.auth.Subject,
     *      javax.resource.spi.ConnectionRequestInfo)
     */
    public ManagedConnection createManagedConnection(Subject arg0,
            ConnectionRequestInfo arg1) throws ResourceException {

        if (resourceAdapter == null) {
            logger
                    .error("Cannot createManagedConnection, resourceAdapter not set.");
            throw new ResourceException(
                    "Cannot createManagedConnection, resourceAdapter not set.");
        }

        return new ManagedConnectionImpl(
                ((ResourceAdapterImpl) resourceAdapter).getParlaySession());
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.resource.spi.ManagedConnectionFactory#matchManagedConnections(java.util.Set,
     *      javax.security.auth.Subject,
     *      javax.resource.spi.ConnectionRequestInfo)
     */
    public ManagedConnection matchManagedConnections(Set connectionSet,
            Subject arg1, ConnectionRequestInfo arg2) throws ResourceException {
        
        if (connectionSet.isEmpty()) {
            if (logger.isDebugEnabled()) {
                logger.debug("Found no matching connection.");
            }
            return null;
        }
        else {

            if (logger.isDebugEnabled()) {
                logger.debug("Found a matching connection.");
            }
            return (ManagedConnection) (connectionSet.iterator().next());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.resource.spi.ManagedConnectionFactory#setLogWriter(java.io.PrintWriter)
     */
    public void setLogWriter(PrintWriter arg0) throws ResourceException {
        logWriter = arg0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.resource.spi.ManagedConnectionFactory#getLogWriter()
     */
    public PrintWriter getLogWriter() throws ResourceException {
        return logWriter;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.resource.spi.ManagedConnectionFactory#equals(java.lang.Object)
     */
    public boolean equals(Object o) {
        
        if (!(o instanceof ManagedConnectionFactoryImpl)) {
            return false;
        }
        ManagedConnectionFactoryImpl rhs = (ManagedConnectionFactoryImpl) o;
        return new EqualsBuilder().append(authenticationSequence, rhs.authenticationSequence)
                .append(domainID, rhs.domainID)
                    .append(ipInitialIOR, rhs.ipInitialIOR)
                    .append(ipInitialLocation, rhs.ipInitialLocation)
                    .append(ipInitialURL, rhs.ipInitialURL)
                    .append(namingServiceIOR, rhs.namingServiceIOR)
                        .isEquals();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.resource.spi.ManagedConnectionFactory#hashCode()
     */
    public int hashCode() {
        return new HashCodeBuilder(13, 113).append(authenticationSequence)
		.append(domainID)
        .append(ipInitialIOR)
        .append(ipInitialLocation)
        .append(ipInitialURL)
        .append(namingServiceIOR)
        .toHashCode();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.resource.spi.ResourceAdapterAssociation#getResourceAdapter()
     */
    public ResourceAdapter getResourceAdapter() {
        return resourceAdapter;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.resource.spi.ResourceAdapterAssociation#setResourceAdapter(javax.resource.spi.ResourceAdapter)
     */
    public void setResourceAdapter(ResourceAdapter arg0)
            throws ResourceException {
        resourceAdapter = arg0;
    }

    /**
     * @return Returns the authenticationSequence.
     */
    public String getAuthenticationSequence() {
        return authenticationSequence;
    }

    /**
     * @param authenticationSequence
     *            The authenticationSequence to set.
     */
    public void setAuthenticationSequence(String authenticationSequence) {
        this.authenticationSequence = authenticationSequence;
    }

    /**
     * @return Returns the domainID.
     */
    public String getDomainID() {
        return domainID;
    }

    /**
     * @param domainID
     *            The domainID to set.
     */
    public void setDomainID(String domainID) {
        this.domainID = domainID;
    }

    /**
     * @return Returns the ipInitialIOR.
     */
    public String getIpInitialIOR() {
        return ipInitialIOR;
    }

    /**
     * @param ipInitialIOR
     *            The ipInitialIOR to set.
     */
    public void setIpInitialIOR(String ipInitialIOR) {
        this.ipInitialIOR = ipInitialIOR;
    }
}