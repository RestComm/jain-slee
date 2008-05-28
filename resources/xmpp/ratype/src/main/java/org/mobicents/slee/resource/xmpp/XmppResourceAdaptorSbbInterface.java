package org.mobicents.slee.resource.xmpp;

import java.util.Collection;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Packet;

/**
 * This is the XMPP Resource Adaptor's Interface that Sbbs can use.
 * 
 * @author Eduardo Martins
 * @author Neutel
 * @version 2.1
 * 
 */

public interface XmppResourceAdaptorSbbInterface {
	
	/**
	 * Sends the specified XMPP packet using the specified connection Id.
	 * @param connectionID the Id of the connection that will send the XMPP packet.
	 * @param packet the XMPP packet to send.
	 */
	public void sendPacket(String connectionID, Packet packet);
	
	/**
	 * Creates a new XMPP client connection to the XMPP server using the given service name on the given host and port and authenticates the client using the given username, password and resource.
	 * This connection will be identified with the specified connection Id.
	 * 
	 * @param connectionID the Id that will identify the connection to create.
	 * @param serverHost the XMPP server host.
	 * @param serverPort the XMPP server port.
	 * @param serviceName the XMPP service name.
	 * @param username the client username.
	 * @param password the client password.
	 * @param resource the client resource to be binded on the connection to create.
	 * @param packetFilters the collection of XMPP packet's classes that this connection should listen.
	 * @throws XMPPException this exception is thrown if the connection can't be created.
	 */
	public void connectClient(String connectionID, String serverHost, int serverPort, String serviceName, String username, String password, String resource, Collection packetFilters)  throws XMPPException;
	
	/**
	 * Creates a new XMPP component connection to the XMPP server using the given service name on the given host and port and authenticates the component using the given component name and secret.
	 * 
	 * @param connectionID the Id that will identify the connection to create.
	 * @param serverHost the XMPP server host.
	 * @param serverPort the XMPP server port.
	 * @param serviceName the XMPP service name.
	 * @param componentName the component name.
	 * @param componentSecret the component secret.
	 * @param packetFilters the collection of XMPP packet's classes that this connection should listen.
	 * @throws XMPPException this exception is thrown if the connection can't be created.
	 */
	public void connectComponent(String connectionID, String serverHost, int serverPort, String serviceName, String componentName, String componentSecret, Collection packetFilters)  throws XMPPException;
	
	/**
	 * Disconnects the XMPP connection with the speficied Id.
	 * 
	 * @param connectionID the Id of the XMPP connection to disconnect.
	 */
	public void disconnect(String connectionID);
		
}
