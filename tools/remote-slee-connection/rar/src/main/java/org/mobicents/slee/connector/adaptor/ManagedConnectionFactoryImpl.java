/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.slee.connector.adaptor;

import org.apache.log4j.Logger;
import org.mobicents.slee.connector.remote.RemoteSleeConnectionService;
import java.io.PrintWriter;
import java.io.Serializable;
import java.rmi.Remote;
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
 * @author baranowb
 */
public class ManagedConnectionFactoryImpl implements ManagedConnectionFactory,
        Serializable {
    private static Logger log = Logger
            .getLogger(ManagedConnectionFactoryImpl.class);
    private String sleeJndiName;
    private boolean refreshOnDisconnect = true;
    private RemoteSleeConnectionService rmiStub; //NOTE: does it make sense to cache single stub?
    private PrintWriter printWriter;

    /* Getters and setters for properties */
    public String getSleeJndiName() {
    	 if(log.isDebugEnabled())
         {
    		 log.debug("mcf.getSleeJndiName() called.");
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

    public boolean isRefreshOnDisconnect() {
    	 if(log.isDebugEnabled())
         {
    		 log.debug("mcf.isRefreshOnDisconnect() called.");
         }
		return refreshOnDisconnect;
	}

	public void setRefreshOnDisconnect(boolean refreshOnDisconnect) {
		 if(log.isDebugEnabled())
         {
    		 log.debug("mcf.setRefreshOnDisconnect() called with " + refreshOnDisconnect);
         }
		this.refreshOnDisconnect = refreshOnDisconnect;
	}

	public ManagedConnectionFactoryImpl() {
    	 if(log.isDebugEnabled())
         {
    		 log.debug("Creating ManagedConnectionFactoryImpl instance");
         }
    }
    
    private synchronized boolean lookupRMIStub(boolean refresh) throws ResourceException{
        if (rmiStub == null || refresh) {
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
	            return true;
	        } catch (Exception e) {
	            log.error("Failed to lookup Slee service in JNDI ");
	            throw new ResourceException("Failed to lookup Slee service in JNDI ",e);
	        }
	       
        }
        return false;
    }
    RemoteSleeConnectionService getRemoteSleeConnectionService() throws ResourceException
    {
    	if(this.rmiStub == null)
    	{
    		this.lookupRMIStub(false);
    	}
    	return this.rmiStub;
    }
    
    boolean refreshRemoteSleeConnectionService() throws ResourceException
    {
    	if(this.refreshOnDisconnect)
    	{
    		return this.lookupRMIStub(true);
    	}else
    	{
    		return false;
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
                        
        lookupRMIStub(false);
        
        return new ManagedConnectionImpl(this);
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