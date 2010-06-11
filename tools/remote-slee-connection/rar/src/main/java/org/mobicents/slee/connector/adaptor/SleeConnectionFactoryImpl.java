package org.mobicents.slee.connector.adaptor;

import java.io.Serializable;
import javax.naming.Reference;
import javax.resource.Referenceable;
import javax.resource.ResourceException;
import javax.resource.spi.ConnectionManager;
import javax.slee.connection.SleeConnection;
import javax.slee.connection.SleeConnectionFactory;

import org.apache.log4j.Logger;

/**
 * Implementation of the SleeConnectionFactory as specified in
 * section F.2 of the JAIN SLEE 1.0 spec.
 * 
 * @author Tim
 */
public class SleeConnectionFactoryImpl implements SleeConnectionFactory,
        Serializable, Referenceable {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Reference reference;
    private ConnectionManager connectionMgr;
    private ManagedConnectionFactoryImpl mcf;
    private static Logger log = Logger
            .getLogger(SleeConnectionFactoryImpl.class);

    SleeConnectionFactoryImpl(ConnectionManager mgr,
            ManagedConnectionFactoryImpl mcf) {
        log.debug("Creating SleeConnectionFactoryImpl");
        this.connectionMgr = mgr;
        this.mcf = mcf;
    }

    /*
     * Get a SLEE connection
     * 
     * @see javax.slee.connection.SleeConnectionFactory#getConnection()
     */
    public SleeConnection getConnection() throws ResourceException {
        /*
         * Delegate request to the app server connection manager. Note that we
         * do not need to pass any connection request info.
         */
        log.debug("getConnection() called");
        return (SleeConnection) connectionMgr.allocateConnection(mcf, null);
    }

    public Reference getReference() {
        return this.reference;
    }

    public void setReference(Reference ref) {
        this.reference = ref;
    }
    
   
}