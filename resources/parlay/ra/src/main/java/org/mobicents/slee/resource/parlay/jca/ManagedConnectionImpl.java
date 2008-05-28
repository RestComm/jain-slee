package org.mobicents.slee.resource.parlay.jca;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.resource.ResourceException;
import javax.resource.spi.ConnectionEvent;
import javax.resource.spi.ConnectionEventListener;
import javax.resource.spi.ConnectionRequestInfo;
import javax.resource.spi.LocalTransaction;
import javax.resource.spi.ManagedConnection;
import javax.resource.spi.ManagedConnectionMetaData;
import javax.security.auth.Subject;
import javax.transaction.xa.XAResource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mobicents.csapi.jr.slee.IpServiceConnection;
import org.mobicents.csapi.jr.slee.TpServiceIdentifier;
import org.mobicents.slee.resource.parlay.csapi.jr.ParlayConnectionImpl;
import org.mobicents.slee.resource.parlay.csapi.jr.ParlayConnectionProxy;
import org.mobicents.slee.resource.parlay.csapi.jr.ParlayConnectionProxyAssociation;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.IpCallControlManagerConnectionImpl;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.activity.callcontrolmanager.CallControlManager;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.IpMultiPartyCallControlManagerConnectionImpl;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.activity.multipartycallcontrolmanager.MultiPartyCallControlManager;
import org.mobicents.slee.resource.parlay.csapi.jr.ui.IpUIManagerConnectionImpl;
import org.mobicents.slee.resource.parlay.csapi.jr.ui.activity.uimanager.UIManager;
import org.mobicents.slee.resource.parlay.fw.FwSessionException;
import org.mobicents.slee.resource.parlay.session.ParlaySession;
import org.mobicents.slee.resource.parlay.session.ServiceSession;

/**
 * Stateful representation of a Parlay Connection. May be pooled for
 * optimisation. Must allow multiplexing of multiple instances to the same
 * gateway service session.
 * 
 * This managed connection is associated with a single ParlayConnection which
 * may change during the lifespan of this connection.
 *  
 */
public class ManagedConnectionImpl implements ManagedConnection,
        ParlayConnectionProxy {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory
            .getLog(ManagedConnectionImpl.class);

    public ManagedConnectionImpl(ParlaySession session) {
        parlaySession = session;
    }

    private transient ParlaySession parlaySession = null;

    private transient PrintWriter logWriter;

    private ParlayConnectionProxyAssociation association = null;

    private transient ArrayList connectionEventListeners = new ArrayList(2);

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.ParlayConnectionProxy#getService(java.lang.String,
     *      java.util.Properties)
     */
    public TpServiceIdentifier getService(String serviceTypeName,
            Properties serviceProperties) throws javax.slee.resource.ResourceException {

        try {
            return parlaySession.getService(serviceTypeName, serviceProperties);
        }
        catch (FwSessionException e) {
            logger.error("Failed to getService", e);
            throw new javax.slee.resource.ResourceException("Failed to getService", e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.ParlayConnectionProxy#getIpServiceConnection(org.mobicents.csapi.jr.slee.TpServiceIdentifier)
     */
    public IpServiceConnection getIpServiceConnection(
            TpServiceIdentifier serviceIdentifier) throws javax.slee.resource.ResourceException {

        IpServiceConnection result = null;

        ServiceSession serviceSession;
        serviceSession = parlaySession.getServiceSession(serviceIdentifier);
        
        if(serviceSession == null) {
            logger.error("Unrecognised serviceIdentifier:" + serviceIdentifier);
            throw new javax.slee.resource.ResourceException("Unrecognised serviceIdentifier:" + serviceIdentifier);
        }
        
        switch (serviceSession.getType()) {
            case (ServiceSession.MultiPartyCallControl):
                result = new IpMultiPartyCallControlManagerConnectionImpl(
                        (MultiPartyCallControlManager)serviceSession);
                break;
            case (ServiceSession.GenericCallControl):
                result = new IpCallControlManagerConnectionImpl((CallControlManager)serviceSession);
            	break;
            case (ServiceSession.UserInteraction):
                result = new IpUIManagerConnectionImpl((UIManager)serviceSession);
                break;    
            default:
                logger.error("Unsupported ServiceSession type");
                throw new javax.slee.resource.ResourceException("Unsupported ServiceSession type");
        }

        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.resource.spi.ManagedConnection#getConnection(javax.security.auth.Subject,
     *      javax.resource.spi.ConnectionRequestInfo)
     */
    public Object getConnection(Subject arg0, ConnectionRequestInfo arg1)
            throws ResourceException {
        association = new ParlayConnectionImpl();
        association.setParlayConnectionProxy(this);
        return association;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.resource.spi.ManagedConnection#destroy()
     */
    public void destroy() throws ResourceException {
        if (logger.isDebugEnabled()) {
            logger.debug("destroy()");
        }

        if (association != null) {
            association.setParlayConnectionProxy(null);
        }

        parlaySession = null;

        connectionEventListeners.clear();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.resource.spi.ManagedConnection#cleanup()
     */
    public void cleanup() throws ResourceException {
        if (logger.isDebugEnabled()) {
            logger.debug("cleanup()");
        }

        if (association != null) {
            association.setParlayConnectionProxy(null);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.resource.spi.ManagedConnection#associateConnection(java.lang.Object)
     */
    public void associateConnection(Object arg0) throws ResourceException {

        if (association != null) {
            association.setParlayConnectionProxy(null);
        }

        association = (ParlayConnectionProxyAssociation) arg0;
        association.setParlayConnectionProxy(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.resource.spi.ManagedConnection#addConnectionEventListener(javax.resource.spi.ConnectionEventListener)
     */
    public void addConnectionEventListener(ConnectionEventListener l) {
        if (!connectionEventListeners.contains(l)) {
            connectionEventListeners.add(l);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.resource.spi.ManagedConnection#removeConnectionEventListener(javax.resource.spi.ConnectionEventListener)
     */
    public void removeConnectionEventListener(ConnectionEventListener l) {
        connectionEventListeners.remove(l);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.resource.spi.ManagedConnection#getXAResource()
     */
    public XAResource getXAResource() throws ResourceException {
        throw new ResourceException("getXAResource() not supported");
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.resource.spi.ManagedConnection#getLocalTransaction()
     */
    public LocalTransaction getLocalTransaction() throws ResourceException {
        throw new ResourceException("getLocalTransaction() not supported");
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.resource.spi.ManagedConnection#getMetaData()
     */
    public ManagedConnectionMetaData getMetaData() throws ResourceException {
        throw new ResourceException("getMetaData() not supported");
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.resource.spi.ManagedConnection#setLogWriter(java.io.PrintWriter)
     */
    public void setLogWriter(PrintWriter arg0) throws ResourceException {
        logWriter = arg0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.resource.spi.ManagedConnection#getLogWriter()
     */
    public PrintWriter getLogWriter() throws ResourceException {
        return logWriter;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.ParlayConnectionProxy#associationClosed(org.mobicents.slee.resource.parlay.ParlayConnectionProxyAssociation)
     */
    public void associationClosed(ParlayConnectionProxyAssociation association) {
        if (this.association == association) {
            this.association = null;

            // Inform connection manager
            ConnectionEvent cev = new ConnectionEvent(this,
                    ConnectionEvent.CONNECTION_CLOSED);
            cev.setConnectionHandle(association);
            fireConnectionClosed(cev);
        }

    }

    /**
     * Fires connection closed event to all registered listeners
     * 
     * @param e
     *            the event
     */
    protected void fireConnectionClosed(ConnectionEvent e) {
        List listeners = null;
        synchronized (connectionEventListeners) {
            listeners = (List) ((ArrayList) connectionEventListeners).clone();
        }
        int count = listeners.size();

        for (int i = 0; i < count; i++) {
            if (logger.isDebugEnabled()) {
                logger.debug("ConnectionEventListener.connectionClosed()");
            }
            ((ConnectionEventListener) listeners.get(i)).connectionClosed(e);
        }
    }

    /**
     * Fired connection error event to all registered listeners
     * 
     * @param e
     *            the event
     */
    protected void fireConnectionErrorOccurred(ConnectionEvent e) {
        List listeners = null;
        synchronized (connectionEventListeners) {
            listeners = (List) ((ArrayList) connectionEventListeners).clone();
        }
        int count = listeners.size();

        for (int i = 0; i < count; i++) {
            if (logger.isDebugEnabled()) {
                logger
                        .debug("ConnectionEventListener.connectionErrorOccurred()");
            }
            ((ConnectionEventListener) listeners.get(i))
                    .connectionErrorOccurred(e);
        }
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj instanceof ManagedConnectionImpl) {
            return true;
        }
        else {
            return false;
        }
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return 1;
    }
}