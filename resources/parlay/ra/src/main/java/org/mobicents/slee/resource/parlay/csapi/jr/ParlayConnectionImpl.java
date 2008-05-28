package org.mobicents.slee.resource.parlay.csapi.jr;

import java.util.Properties;

import javax.resource.ResourceException;
import javax.resource.cci.ConnectionMetaData;
import javax.resource.cci.Interaction;
import javax.resource.cci.LocalTransaction;
import javax.resource.cci.ResultSetInfo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mobicents.csapi.jr.slee.IpServiceConnection;
import org.mobicents.csapi.jr.slee.ParlayConnection;
import org.mobicents.csapi.jr.slee.TpServiceIdentifier;

/**
 * Implementation of a client connection handle. Transient and lightweight.
 * Delegates all functionality to a pooled proxy.
 */
public class ParlayConnectionImpl implements ParlayConnection, ParlayConnectionProxyAssociation {
    
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory
            .getLog(ParlayConnectionImpl.class);
    
    private static final String INVALID_STATE_ERROR = "This object is in an invalid state to complete the requested operation.";

    private transient ParlayConnectionProxy connectionProxy = null;

    public TpServiceIdentifier getService(String serviceTypeName,
            Properties serviceProperties) throws javax.slee.resource.ResourceException {
        if(connectionProxy == null) {
            throw new javax.slee.resource.ResourceException(INVALID_STATE_ERROR);
        }
            
        return connectionProxy.getService(serviceTypeName, serviceProperties);
    }

    public IpServiceConnection getIpServiceConnection(
            TpServiceIdentifier serviceIdentifier) throws javax.slee.resource.ResourceException {
        if(connectionProxy == null) {
            throw new javax.slee.resource.ResourceException(INVALID_STATE_ERROR);
        }
            
        return connectionProxy.getIpServiceConnection(serviceIdentifier);
    }

    public Interaction createInteraction() throws ResourceException {
        throw new ResourceException("createInteraction() not supported");
    }

    public LocalTransaction getLocalTransaction() throws ResourceException {
        throw new ResourceException("getLocalTransaction() not supported");
    }

    public ConnectionMetaData getMetaData() throws ResourceException {
        throw new ResourceException("getMetaData() not supported");
    }

    public ResultSetInfo getResultSetInfo() throws ResourceException {
        throw new ResourceException("getResultSetInfo() not supported");
    }

    public void close() throws ResourceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Closing ParlayConnectionImpl");
        }
        if(connectionProxy != null) {
            connectionProxy.associationClosed(this);
            connectionProxy = null;
        }
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.parlay.ParlayConnectionProxyAssociation#setParlayConnectionProxy(org.mobicents.slee.resource.parlay.ParlayConnectionProxy)
     */
    public void setParlayConnectionProxy(ParlayConnectionProxy connectionProxy) {
        this.connectionProxy = connectionProxy;
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.parlay.ParlayConnectionProxyAssociation#getParlayConnectionProxy()
     */
    public ParlayConnectionProxy getParlayConnectionProxy() {
        return connectionProxy;
    }
}