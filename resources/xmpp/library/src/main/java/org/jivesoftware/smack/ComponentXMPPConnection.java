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

package org.jivesoftware.smack;

import javax.net.SocketFactory;

import org.jivesoftware.smackx.ServiceDiscoveryManager;

/**
 *
 * @author Eduardo Martins
 *
 */

public class ComponentXMPPConnection extends XMPPConnection {
	
	/** the server's domain for this component connection */
	protected String componentName = null;	
	
	/** the server's component secret */
	protected String componentSecret = null;	
		
        
    /**
     * Creates a new component connection to the specified XMPP server.
     * 
     * @param componentName the server's domain for this component connection.
     * @param componentSecret the server's password for component connections.
     * @param serviceName the name of the XMPP server to connect to; e.g. <tt>jivesoftware.com</tt>.
     * @throws XMPPException if an error occurs while trying to establish the connection.
     *      Two possible errors can occur which will be wrapped by an XMPPException --
     *      UnknownHostException (XMPP error code 504), and IOException (XMPP error code
     *      502). The error codes and wrapped exceptions can be used to present more
     *      appropiate error messages to end-users.
     */
    public ComponentXMPPConnection(String componentName, String componentSecret, String serviceName) throws XMPPException {
    	super(serviceName);
    	synchronized(packetReader) {
    		// Obtain the ServiceDiscoveryManager associated with my XMPPConnection
	        ServiceDiscoveryManager discoManager = ServiceDiscoveryManager.getInstanceFor(this);
	        // Set service discovery identity
	        discoManager.setIdentityName("smack component");
	        discoManager.setIdentityType("smack");	        
	        discoManager.setIdentityCategory("component");
	        // Remove client features
        	discoManager.removeFeature("http://jabber.org/protocol/xhtml-im");
		    discoManager.removeFeature("http://jabber.org/protocol/muc");
		    // Set fields for Packet Reader's handshake
        	this.componentSecret = componentSecret;
        	this.componentName = componentName;       
        	// Notify packet reader
        	packetReader.notifyAll();	
    	}
     }

    /**
     * Creates a new component connection to the specified XMPP server on the given host and port.
     *
     * @param componentName the server's domain for this component connection.
     * @param componentSecret the server's password for component connections.
     * @param host the host name, or null for the loopback address.
     * @param port the port on the server that should be used; e.g. <tt>5222</tt>.
     * @param serviceName the name of the XMPP server to connect to; e.g. <tt>jivesoftware.com</tt>.
     * @throws XMPPException if an error occurs while trying to establish the connection.
     *      Two possible errors can occur which will be wrapped by an XMPPException --
     *      UnknownHostException (XMPP error code 504), and IOException (XMPP error code
     *      502). The error codes and wrapped exceptions can be used to present more
     *      appropiate error messages to end-users.
     */
    public ComponentXMPPConnection(String componentName, String componentSecret, String host, int port, String serviceName) throws XMPPException {    	
        super(host,port,serviceName);
        synchronized(packetReader) {
    		// Obtain the ServiceDiscoveryManager associated with my XMPPConnection
	        ServiceDiscoveryManager discoManager = ServiceDiscoveryManager.getInstanceFor(this);
	        // Set service discovery identity
	        discoManager.setIdentityName("smack component");
	        discoManager.setIdentityType("smack");	        
	        discoManager.setIdentityCategory("component");
	        // Remove client features
        	discoManager.removeFeature("http://jabber.org/protocol/xhtml-im");
		    discoManager.removeFeature("http://jabber.org/protocol/muc");
		    // Set fields for Packet Reader's handshake
        	this.componentSecret = componentSecret;
        	this.componentName = componentName;       
        	// Notify packet reader
        	packetReader.notifyAll();	
    	}    	    	
    }

    /**
     * Creates a new component connection to the specified XMPP server on the given port using the
     * specified SocketFactory.<p>
     *
     * A custom SocketFactory allows fine-grained control of the actual connection to the
     * XMPP server. A typical use for a custom SocketFactory is when connecting through a
     * SOCKS proxy.
     * 
     * @param componentName the server's domain for this component connection.
     * @param componentSecret the server's password for component connections.     *
     * @param host the host name, or null for the loopback address.
     * @param port the port on the server that should be used; e.g. <tt>5222</tt>.
     * @param serviceName the name of the XMPP server to connect to; e.g. <tt>jivesoftware.com</tt>.
     * @param socketFactory a SocketFactory that will be used to create the socket to the XMPP
     *        server.
     * @throws XMPPException if an error occurs while trying to establish the connection.
     *      Two possible errors can occur which will be wrapped by an XMPPException --
     *      UnknownHostException (XMPP error code 504), and IOException (XMPP error code
     *      502). The error codes and wrapped exceptions can be used to present more
     *      appropiate error messages to end-users.
     */
    public ComponentXMPPConnection(String componentName, String componentSecret, String host, int port, String serviceName, SocketFactory socketFactory)
            throws XMPPException
    {
    	super(host,port,serviceName,socketFactory);
    	synchronized(packetReader) {
    		// Obtain the ServiceDiscoveryManager associated with my XMPPConnection
	        ServiceDiscoveryManager discoManager = ServiceDiscoveryManager.getInstanceFor(this);
	        // Set service discovery identity
	        discoManager.setIdentityName("smack component");
	        discoManager.setIdentityType("smack");
	        discoManager.setIdentityCategory("component");
	        // Remove client features
        	discoManager.removeFeature("http://jabber.org/protocol/xhtml-im");
		    discoManager.removeFeature("http://jabber.org/protocol/muc");
		    // Set fields for Packet Reader's handshake
        	this.componentSecret = componentSecret;
        	this.componentName = componentName;       
        	// Notify packet reader
        	packetReader.notifyAll();	
    	}    	    
    }
    
    /**
     * This XMPPConnection method does not apply for a component connection, returns null.
     * 
     * @return null.
     */
    public String getUser() {
        return null;
    }

    /**
     * This XMPPComponent method does not qualify for the component connection, it throws a exception.
     *
     * @throws XMPPException always.
     * 
     */
    public void login(String username, String password) throws XMPPException {
    	throw new XMPPException("SASL Authentication does not exist in a component connection");
    }

    /**
     * This XMPPComponent method does not qualify for the component connection, it throws a exception.
     *
     * @throws XMPPException always.
     * 
     */
    public synchronized void login(String username, String password, String resource)
            throws XMPPException
    {
    	throw new XMPPException("SASL Authentication does not exist in a component connection");
    }

    /**
     * This XMPPComponent method does not qualify for the component connection, it throws a exception.
     *
     * @throws XMPPException always.
     * 
     */
    public synchronized void login(String username, String password, String resource,
            boolean sendPresence) throws XMPPException
    {
    	throw new XMPPException("SASL Authentication does not exist in a component connection");
    }

    /**
     * This XMPPComponent method does not qualify for the component connection, it throws a exception.
     *
     * @throws XMPPException always.
     * 
     */
    public synchronized void loginAnonymously() throws XMPPException {
    	throw new XMPPException("SASL Authentication does not exist in a component connection");
    }

    /**
     * This XMPPComponent method does not qualify for the component connection, returns null.
     *
     * @return null.
     */
    public Roster getRoster() {
        return null;
    }

    /**
     * This XMPPComponent method does not qualify for the component connection, returns null.
     * 
     * @return null.
     */
    public synchronized AccountManager getAccountManager() {
        return null;
    }

    /**
     * This XMPPComponent method does not qualify for the component connection, returns null.
     *
     * @param participant the person to start the conversation with.
     * @return null.
     */
    public Chat createChat(String participant) {
        return null;
    }

    /**
     * This XMPPComponent method does not qualify for the component connection, returns null.
     * 
     * @param room the fully qualifed name of the room.
     * @return null.
     */
    public GroupChat createGroupChat(String room) {
    	return null;
    }

    
    /**
     * Returns true since every component connection is authenticated.
     *
     * @return true if.
     */
    public boolean isAuthenticated() {
        return true;
    }

    /**
     * Returns false since every component connection is authenticated.
     *
     * @return false.
     */
    public boolean isAnonymous() {
        return false;
    }
    
    /**
     * This XMPPComponent method does not qualify for the component connection, returns null.
     *
     * @return null.
     */
    public SASLAuthentication getSASLAuthentication() {
        return null;
    }

    
    /*
    // simple test
     
    public static void main(String[] args) {
		try {
			String componentName = "";
			String componentSecret = "";
			String host = "";
			int port = 10015;
			String serviceName = componentName;
			ComponentXMPPConnection c = new ComponentXMPPConnection(componentName,componentSecret,host,port,serviceName);
			System.out.println("Component connected.");
			while (true);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	*/
	
}
