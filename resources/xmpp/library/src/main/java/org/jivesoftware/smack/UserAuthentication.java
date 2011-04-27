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

/**
 * There are two ways to authenticate a user with a server. Using SASL or Non-SASL
 * authentication. This interface makes {@link SASLAuthentication} and
 * {@link NonSASLAuthentication} polyphormic.
 *
 * @author Gaston Dombiak
 */
interface UserAuthentication {

    /**
     * Authenticates the user with the server. This method will return the full JID provided by
     * the server. The server may assign a full JID with a username and resource different than
     * the requested by this method.
     *
     * @param username the username that is authenticating with the server.
     * @param password the password to send to the server.
     * @param resource the desired resource.
     * @return the full JID provided by the server while binding a resource for the connection.
     * @throws XMPPException if an error occures while authenticating.
     */
    String authenticate(String username, String password, String resource) throws
            XMPPException;

    /**
     * Performs an anonymous authentication with the server. The server will created a new full JID
     * for this connection. An exception will be thrown if the server does not support anonymous
     * authentication.
     *
     * @return the full JID provided by the server while binding a resource for the connection.
     * @throws XMPPException if an error occures while authenticating.
     */
    String authenticateAnonymously() throws XMPPException;
}
