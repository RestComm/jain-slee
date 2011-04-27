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

import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.packet.Authentication;
import org.jivesoftware.smack.packet.IQ;

/**
 * Implementation of JEP-0078: Non-SASL Authentication. Follow the following
 * <a href=http://www.jabber.org/jeps/jep-0078.html>link</a> to obtain more
 * information about the JEP.
 *
 * @author Gaston Dombiak
 */
class NonSASLAuthentication implements UserAuthentication {

    private XMPPConnection connection;

    public NonSASLAuthentication(XMPPConnection connection) {
        super();
        this.connection = connection;
    }

    public String authenticate(String username, String password, String resource) throws
            XMPPException {
        // If we send an authentication packet in "get" mode with just the username,
        // the server will return the list of authentication protocols it supports.
        Authentication discoveryAuth = new Authentication();
        discoveryAuth.setType(IQ.Type.GET);
        discoveryAuth.setUsername(username);

        PacketCollector collector =
            connection.createPacketCollector(new PacketIDFilter(discoveryAuth.getPacketID()));
        // Send the packet
        connection.sendPacket(discoveryAuth);
        // Wait up to a certain number of seconds for a response from the server.
        IQ response = (IQ) collector.nextResult(SmackConfiguration.getPacketReplyTimeout());
        if (response == null) {
            throw new XMPPException("No response from the server.");
        }
        // If the server replied with an error, throw an exception.
        else if (response.getType() == IQ.Type.ERROR) {
            throw new XMPPException(response.getError());
        }
        // Otherwise, no error so continue processing.
        Authentication authTypes = (Authentication) response;
        collector.cancel();

        // Now, create the authentication packet we'll send to the server.
        Authentication auth = new Authentication();
        auth.setUsername(username);

        // Figure out if we should use digest or plain text authentication.
        if (authTypes.getDigest() != null) {
            auth.setDigest(connection.getConnectionID(), password);
        }
        else if (authTypes.getPassword() != null) {
            auth.setPassword(password);
        }
        else {
            throw new XMPPException("Server does not support compatible authentication mechanism.");
        }

        auth.setResource(resource);

        collector = connection.createPacketCollector(new PacketIDFilter(auth.getPacketID()));
        // Send the packet.
        connection.sendPacket(auth);
        // Wait up to a certain number of seconds for a response from the server.
        response = (IQ) collector.nextResult(SmackConfiguration.getPacketReplyTimeout());
        if (response == null) {
            throw new XMPPException("Authentication failed.");
        }
        else if (response.getType() == IQ.Type.ERROR) {
            throw new XMPPException(response.getError());
        }
        // We're done with the collector, so explicitly cancel it.
        collector.cancel();

        return response.getTo();
    }

    public String authenticateAnonymously() throws XMPPException {
        // Create the authentication packet we'll send to the server.
        Authentication auth = new Authentication();

        PacketCollector collector =
            connection.createPacketCollector(new PacketIDFilter(auth.getPacketID()));
        // Send the packet.
        connection.sendPacket(auth);
        // Wait up to a certain number of seconds for a response from the server.
        IQ response = (IQ) collector.nextResult(SmackConfiguration.getPacketReplyTimeout());
        if (response == null) {
            throw new XMPPException("Anonymous login failed.");
        }
        else if (response.getType() == IQ.Type.ERROR) {
            throw new XMPPException(response.getError());
        }
        // We're done with the collector, so explicitly cancel it.
        collector.cancel();

        if (response.getTo() != null) {
            return response.getTo();
        }
        else {
            return connection.serviceName + "/" + ((Authentication) response).getResource();
        }
    }
}
