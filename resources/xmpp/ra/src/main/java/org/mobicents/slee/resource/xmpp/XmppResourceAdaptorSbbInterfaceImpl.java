/**
 * 
 */
package org.mobicents.slee.resource.xmpp;

import java.util.Collection;

import javax.slee.facilities.Tracer;

import org.jivesoftware.smack.ComponentXMPPConnection;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.ServiceDiscoveryManager;

/**
 * @author martins
 *
 */
public class XmppResourceAdaptorSbbInterfaceImpl implements XmppResourceAdaptorSbbInterface {

	private final XmppResourceAdaptor ra;
	private final Tracer tracer;
	
	/**
	 * @param ra
	 * @param tracer
	 */
	public XmppResourceAdaptorSbbInterfaceImpl(XmppResourceAdaptor ra) {
		this.ra = ra;
		this.tracer = ra.getTracer();
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.resource.xmpp.XmppResourceAdaptorSbbInterface#getXmppConnection(java.lang.String)
	 */
	public XmppConnection getXmppConnection(String connectionId) {
		return ra.getActivity(new XmppActivityHandle(connectionId));
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.resource.xmpp.XmppResourceAdaptorSbbInterface#connectClient(java.lang.String, java.lang.String, int, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.util.Collection)
	 */
	public XmppConnection connectClient(String connectionID, String serverHost,
			int serverPort, String serviceName, String username,
			String password, String resource, Collection<Class<?>> packetFilters)
			throws XMPPException {
		
		if(connectionID == null) {
			throw new NullPointerException("null connection id");
		}
		
		XmppActivityHandle handle = new XmppActivityHandle(connectionID);
    	if (ra.getActivities().get(handle) == null) {
    		
    		if(tracer.isInfoEnabled()) {
    			tracer.info("Connecting to service "+serviceName+" at "+serverHost+":"+serverPort);        		        		        		         		
    		}
    		        		
			XMPPConnection connection = null;
			try {
				//create connection
				connection = new XMPPConnection(serverHost, serverPort, serviceName);					
				ra.addListener(connectionID,connection,handle,packetFilters);
				// login
				connection.login(username,password,resource);
			} catch (Exception e) {
				if(tracer.isInfoEnabled()) {
					tracer.info("Can't connect to service.");					   		
				}
				throw new XMPPException(e);
			}
			
			// create activity
			return ra.createActivity(connectionID,connection,handle);
		
    	}
    	else {
    		String e = "Connection already exists!";
    		if(tracer.isInfoEnabled()) {
				tracer.info(e);
    		}
    		throw new XMPPException(e);
    	}
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.resource.xmpp.XmppResourceAdaptorSbbInterface#connectComponent(java.lang.String, java.lang.String, int, java.lang.String, java.lang.String, java.lang.String, java.util.Collection)
	 */
	public XmppConnection connectComponent(String connectionID, String serverHost,
			int serverPort, String serviceName, String componentName,
			String componentSecret, Collection<Class<?>> packetFilters)
			throws XMPPException {
		
		if(connectionID == null) {
			throw new NullPointerException("null connection id");
		}
		
		XmppActivityHandle handle = new XmppActivityHandle(connectionID);
    	if (ra.getActivities().get(handle) == null) {        		        		
    		
    		if (tracer.isInfoEnabled())
    			tracer.info("Opening Component XMPP connection to "+serverHost+" on port "+serverPort);         		
    		
    		//create connection
			XMPPConnection connection = null;
			try {
				connection = new ComponentXMPPConnection(componentName,componentSecret,serverHost,serverPort,serviceName);					
				//Obtain the ServiceDiscoveryManager associated with my XMPPConnection
				ServiceDiscoveryManager.setIdentityName("mobicents component");
				ServiceDiscoveryManager.setIdentityType("mobicents");
				ServiceDiscoveryManager.setIdentityCategory("component");
				ra.addListener(connectionID,connection,handle,packetFilters);					
				if (tracer.isInfoEnabled())
					tracer.info("XMPP Component connected.");
			} catch (Exception e) {
				if (tracer.isInfoEnabled())
					tracer.info("XMPP Component NOT connected.");					
				throw new XMPPException(e);					
			}
			
	        // create activity
			return ra.createActivity(connectionID,connection,handle);				
    	}
    	else {
    		String e = "Connection already exists!";
    		if (tracer.isInfoEnabled())
    			tracer.info(e);        		
    		throw new XMPPException(e);
    	}
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.resource.xmpp.XmppResourceAdaptorSbbInterface#disconnect(java.lang.String)
	 */
	public void disconnect(String connectionID) {
		try {	    	    
    		XmppActivityHandle handle = new XmppActivityHandle(connectionID);
    		XmppConnection connection = (XmppConnection) ra.getActivities().get(handle);
    		if (connection != null) {
    			((XMPPConnection)connection.getConnection()).close();        				    	    
    		}
    	}
    	catch (Exception e) {
    		tracer.severe("Failed to disconnect connection "+connectionID,e);
    	}
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.resource.xmpp.XmppResourceAdaptorSbbInterface#sendPacket(java.lang.String, org.jivesoftware.smack.packet.Packet)
	 */
	public void sendPacket(String connectionID, Packet packet) {
		try {
    		XmppActivityHandle handle = new XmppActivityHandle(connectionID);
    		XmppConnection connection = (XmppConnection) ra.getActivities().get(handle);
    		if (connection != null) {
    			((XMPPConnection)connection.getConnection()).sendPacket(packet);	    	   
    	    	if (tracer.isFineEnabled()) {
    	    		tracer.fine(connectionID+" sent packet: "+packet.toXML());
    	    	}
    	    }
    	}
    	catch (Exception e) {
    		tracer.severe("Failed to send packet on connection "+connectionID,e);
    	}
	}

}
