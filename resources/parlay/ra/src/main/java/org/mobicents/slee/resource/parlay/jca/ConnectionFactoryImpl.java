package org.mobicents.slee.resource.parlay.jca;

import javax.naming.NamingException;
import javax.naming.Reference;
import javax.resource.ResourceException;
import javax.resource.cci.Connection;
import javax.resource.cci.ConnectionFactory;
import javax.resource.cci.ConnectionSpec;
import javax.resource.cci.RecordFactory;
import javax.resource.cci.ResourceAdapterMetaData;
import javax.resource.spi.ConnectionManager;
import javax.resource.spi.ConnectionRequestInfo;
import javax.resource.spi.ManagedConnectionFactory;

/**
 * This class provides connections to services using this RA.
 */
public class ConnectionFactoryImpl implements ConnectionFactory {

    public ConnectionFactoryImpl(ManagedConnectionFactory m, ConnectionManager c) {
        mcf = m;
        cm = c;
    }

    private ManagedConnectionFactory mcf = null;

    private ConnectionManager cm = null;

    private Reference ref = null;

    /*
     * (non-Javadoc)
     * 
     * @see javax.resource.cci.ConnectionFactory#getConnection()
     */
    public Connection getConnection() throws ResourceException {
        return (Connection) cm.allocateConnection(mcf, null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.resource.cci.ConnectionFactory#getConnection(javax.resource.cci.ConnectionSpec)
     */
    public Connection getConnection(ConnectionSpec arg0)
            throws ResourceException {
        return (Connection) cm.allocateConnection(mcf,
                (ConnectionRequestInfo) arg0);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.resource.cci.ConnectionFactory#getRecordFactory()
     */
    public RecordFactory getRecordFactory() throws ResourceException {
        throw new UnsupportedOperationException(
                "getRecordFactory() not supported");
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.resource.cci.ConnectionFactory#getMetaData()
     */
    public ResourceAdapterMetaData getMetaData() throws ResourceException {
        throw new UnsupportedOperationException("getMetaData() not supported");
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.resource.Referenceable#setReference(javax.naming.Reference)
     */
    public void setReference(Reference arg0) {
        ref = arg0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.naming.Referenceable#getReference()
     */
    public Reference getReference() throws NamingException {
        return ref;
    }

}