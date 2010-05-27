/*
 * ***************************************************
 *                                                 *
 *  Mobicents: The Open Source JSLEE Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************
 *
 * Created on Dec 5, 2004 ManagedConnectionFactoryImpl.java
 */
package org.mobicents.slee.connector.adaptor;

import org.apache.log4j.Logger;
import org.mobicents.slee.connector.remote.RemoteSleeConnectionService;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Set;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.resource.ResourceException;
import javax.resource.spi.ConnectionManager;
import javax.resource.spi.ConnectionRequestInfo;
import javax.resource.spi.ManagedConnection;
import javax.resource.spi.ManagedConnectionFactory;
import javax.security.auth.Subject;


/**
 * 
 * Implementation of ManagedConnectionFactory according to the JCA
 * connection contract.
 * 
 * @author Tim Fox
 */
public class ManagedConnectionFactoryImpl implements ManagedConnectionFactory,
        Serializable {
    private static Logger log = Logger
            .getLogger(ManagedConnectionFactoryImpl.class);
    private String sleeJndiName;
    private RemoteSleeConnectionService rmiStub;
    private PrintWriter printWriter;

    /* Getters and setters for properties */
    public String getSleeJndiName() {
    	 if(log.isDebugEnabled())
         {
    		 log.debug("mcf.getSleeJndiName() called");
         }
        return sleeJndiName;
    }

    public void setSleeJndiName(String name) {
    	 if(log.isDebugEnabled())
         {
    		 log.debug("mcf.setSleeJndiName() called with " + name);
         }
        sleeJndiName = name;
    }

    public ManagedConnectionFactoryImpl() {
    	 if(log.isDebugEnabled())
         {
    		 log.debug("Creating ManagedConnectionFactoryImpl instance");
         }
    }
    
    private synchronized void lookupRMIStub() {
        if (rmiStub == null ) {
	        try {
	            InitialContext ctx = new InitialContext();
	            if(log.isDebugEnabled())
	            {
	            	log.debug("Looking up slee service with name " + sleeJndiName);
	            }
	            rmiStub = (RemoteSleeConnectionService) ctx.lookup(sleeJndiName);
	            //fails on my machine.... if not printed it works...
	            //log.debug("RMI Stub is: " + rmiStub);
	            if(log.isDebugEnabled())
	            {
	            	log.debug("Success on RMI stub of RemoteSleeService lookup");
	            }
	        } catch (NamingException e) {
	            log.error("Failed to lookup Slee service in JNDI ", e);
	        }
        }
    }

    /*
     * Create a connection factory. This would normally be called in a 
     * non-managed environment i.e. not in a J2EE app server.
     * We don't support this
     * 
     * @see javax.resource.spi.ManagedConnectionFactory#createConnectionFactory()
     */
    public Object createConnectionFactory() throws ResourceException {
    	 if(log.isDebugEnabled())
         {
    		 log.debug("createConnectionFactory() called");
         }
        //We are only supporting use within the managed environment for now
        throw new ResourceException(
                "Mobicents SLEE resource adaptor only works in managed environment!");
    }

    /*
     * Create the connection factory
     * 
     * @see javax.resource.spi.ManagedConnectionFactory#createConnectionFactory(javax.resource.spi.ConnectionManager)
     */
    public Object createConnectionFactory(ConnectionManager connectionMgr)
            throws ResourceException {
    	 if(log.isDebugEnabled())
         {
    		 log.debug("createConnectionFactory(ConnectionManager connectionMgr) called");
         }
        /*
         * Get EIS-specific Connection Factory instance We're not using CCI for
         * this resource adaptor
         */                        
        return new SleeConnectionFactoryImpl(connectionMgr, this);
    }

    /*
     * Create a ManagedConnection instance
     * 
     * @see javax.resource.spi.ManagedConnectionFactory#createManagedConnection(javax.security.auth.Subject,
     *      javax.resource.spi.ConnectionRequestInfo)
     */
    public ManagedConnection createManagedConnection(Subject subject,
            ConnectionRequestInfo info) throws ResourceException {
    	 if(log.isDebugEnabled())
         {
    		 log.debug("createManagedConnection() called");
         }
                        
        lookupRMIStub();
        
        return new ManagedConnectionImpl(rmiStub);
    }

    /*
     * Match the connections. Called by the connection manager to ask us
     * whether we prefer a particular connection to be used for this
     * connection
     * 
     * @see javax.resource.spi.ManagedConnectionFactory#matchManagedConnections(java.util.Set,
     *      javax.security.auth.Subject,
     *      javax.resource.spi.ConnectionRequestInfo)
     */
    public ManagedConnection matchManagedConnections(Set connections,
            Subject subject, ConnectionRequestInfo info)
            throws ResourceException {
    	 if(log.isDebugEnabled())
         {
    		 log.debug("matchManagedConnections() called");
         }
        //Any of the connections will do
        if (connections.isEmpty())
            return null;
        return (ManagedConnection) (connections.iterator().next());
    }

    /*
     * Not used
     * 
     * @see javax.resource.spi.ManagedConnectionFactory#getLogWriter()
     */
    public PrintWriter getLogWriter() throws ResourceException {
        return printWriter;
    }

    /*
     * Not used
     * 
     * @see javax.resource.spi.ManagedConnectionFactory#setLogWriter(java.io.PrintWriter)
     */
    public void setLogWriter(PrintWriter writer) throws ResourceException {
        printWriter = writer;
    }
    
    //TODO: Override hashcode() and equals() ??? see SLEE spec 5.5.3
    // "Requirements"
}