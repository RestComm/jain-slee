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
	 * Retrieves the xmpp connection with the specified id.
	 * @param connectionId
	 * @return
	 */
	public XmppConnection getXmppConnection(String connectionId);
	
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
	 * @return
	 */
	public XmppConnection connectClient(String connectionID, String serverHost, int serverPort, String serviceName, String username, String password, String resource, Collection<Class<?>> packetFilters)  throws XMPPException;
	
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
	 * @return
	 */
	public XmppConnection connectComponent(String connectionID, String serverHost, int serverPort, String serviceName, String componentName, String componentSecret, Collection<Class<?>> packetFilters)  throws XMPPException;
	
	/**
	 * Disconnects the XMPP connection with the specified Id.
	 * 
	 * @param connectionID the Id of the XMPP connection to disconnect.
	 */
	public void disconnect(String connectionID);
		
}
