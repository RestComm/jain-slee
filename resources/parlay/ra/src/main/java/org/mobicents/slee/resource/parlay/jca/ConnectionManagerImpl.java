package org.mobicents.slee.resource.parlay.jca;

import java.util.HashSet;
import java.util.Set;

import javax.resource.ResourceException;
import javax.resource.spi.ConnectionEvent;
import javax.resource.spi.ConnectionEventListener;
import javax.resource.spi.ConnectionManager;
import javax.resource.spi.ConnectionRequestInfo;
import javax.resource.spi.ManagedConnection;
import javax.resource.spi.ManagedConnectionFactory;
import javax.security.auth.Subject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Basic JCA compliant connection manager implementation. Will be used in slee
 * container to manage client connections.
 * 
 * TODO lookup a standard implementation of this
 */
public class ConnectionManagerImpl implements ConnectionManager,
        ConnectionEventListener {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory
            .getLog(ConnectionManagerImpl.class);

    /**
     * Constructor for ConnectionManagerImpl.
     */
    public ConnectionManagerImpl() {
        if (logger.isDebugEnabled()) {
            logger.debug("Default ConnectionManager implementation created");
        }
        connectionPool = new HashSet();
    }
    
    private final transient Set connectionPool;

    /**
     * @see javax.resource.spi.ConnectionManager#allocateConnection(ManagedConnectionFactory,
     *      ConnectionRequestInfo)
     */
    public synchronized Object allocateConnection(
            ManagedConnectionFactory managedConnectionFactory,
            ConnectionRequestInfo connectionRequestInfo)
            throws ResourceException {

        if (logger.isDebugEnabled()) {
            logger.debug("Allocating connection.");
        }

        Object result = null;
        ManagedConnection managedConnection = null;

        final Subject subject = null;

        synchronized (connectionPool) {
            if (! connectionPool.isEmpty()) {
                managedConnection = managedConnectionFactory
                        .matchManagedConnections(connectionPool, subject,
                                connectionRequestInfo);
            }

            if (managedConnection != null) {
                connectionPool.remove(managedConnection);
            }
        }

        if (managedConnection == null) {
            managedConnection = managedConnectionFactory
                    .createManagedConnection(subject, connectionRequestInfo);

            managedConnection.addConnectionEventListener(this);
        }

        result = managedConnection
                .getConnection(subject, connectionRequestInfo);
        return result;
    }

    /**
     * @see javax.resource.spi.ConnectionEventListener#connectionClosed(ConnectionEvent)
     */
    public void connectionClosed(ConnectionEvent event) {
        ManagedConnection managedConnection = (ManagedConnection) event
                .getSource();
        try {
            managedConnection.cleanup();

            synchronized (connectionPool) {
                connectionPool.add(managedConnection);
            }
        }
        catch (ResourceException e) {
            try {
                if (logger.isDebugEnabled()) {
                    logger.debug("Exception cleaning managed connnection. ", e);
                }
                managedConnection.destroy();
            }
            catch (ResourceException e1) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Exception destroying managed connnection. ",
                            e1);
                }
            }
        }
    }

    /**
     * @see javax.resource.spi.ConnectionEventListener#connectionErrorOccurred(ConnectionEvent)
     */
    public void connectionErrorOccurred(ConnectionEvent event) {
        ManagedConnection managedConnection = (ManagedConnection) event
                .getSource();
        managedConnection.removeConnectionEventListener(this);
        try {
            managedConnection.cleanup();
            managedConnection.destroy();
        }
        catch (ResourceException e) {
            if (logger.isDebugEnabled()) {
                logger.debug("Exception destroying managed connection.", e);
            }
        }
    }

    /**
     * @see javax.resource.spi.ConnectionEventListener#localTransactionCommitted(ConnectionEvent)
     */
    public void localTransactionCommitted(ConnectionEvent arg0) {
    }

    /**
     * @see javax.resource.spi.ConnectionEventListener#localTransactionRolledback(ConnectionEvent)
     */
    public void localTransactionRolledback(ConnectionEvent arg0) {
    }

    /**
     * @see javax.resource.spi.ConnectionEventListener#localTransactionStarted(ConnectionEvent)
     */
    public void localTransactionStarted(ConnectionEvent arg0) {
    }

}