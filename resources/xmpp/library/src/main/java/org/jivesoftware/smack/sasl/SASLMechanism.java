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

package org.jivesoftware.smack.sasl;

import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.util.StringUtils;

import java.io.IOException;

/**
 * Base class for SASL mechanisms. Subclasses must implement three methods:
 * <ul>
 *  <li>{@link #getName()} -- returns the common name of the SASL mechanism.</li>
 *  <li>{@link #getAuthenticationText(String, String, String)} -- authentication text to include
 *      in the initial <tt>auth</tt> stanza.</li>
 *  <li>{@link #getChallengeResponse(byte[])} -- to respond challenges made by the server.</li>
 * </ul>
 *
 * @author Gaston Dombiak
 */
public abstract class SASLMechanism {

    private SASLAuthentication saslAuthentication;

    public SASLMechanism(SASLAuthentication saslAuthentication) {
        super();
        this.saslAuthentication = saslAuthentication;
    }

    /**
     * Builds and sends the <tt>auth</tt> stanza to the server.
     *
     * @param username the username of the user being authenticated.
     * @param host     the hostname where the user account resides.
     * @param password the password of the user.
     * @throws IOException If a network error occures while authenticating.
     */
    public void authenticate(String username, String host, String password) throws IOException {
        // Build the authentication stanza encoding the authentication text
        StringBuffer stanza = new StringBuffer();
        stanza.append("<auth mechanism=\"").append(getName());
        stanza.append("\" xmlns=\"urn:ietf:params:xml:ns:xmpp-sasl\">");
        String authenticationText = getAuthenticationText(username, host, password);
        if (authenticationText != null) {
            stanza.append(StringUtils.encodeBase64(authenticationText));
        }
        stanza.append("</auth>");

        // Send the authentication to the server
        getSASLAuthentication().send(stanza.toString());
    }

    /**
     * The server is challenging the SASL mechanism for the stanza he just sent. Send a
     * response to the server's challenge.
     *
     * @param challenge a base64 encoded string representing the challenge.
     */
    public void challengeReceived(String challenge) throws IOException {
        // Build the challenge response stanza encoding the response text
        StringBuffer stanza = new StringBuffer();
        stanza.append("<response xmlns=\"urn:ietf:params:xml:ns:xmpp-sasl\">");
        String authenticationText = getChallengeResponse(StringUtils.decodeBase64(challenge));
        if (authenticationText != null) {
            stanza.append(StringUtils.encodeBase64(authenticationText));
        }
        stanza.append("</response>");

        // Send the authentication to the server
        getSASLAuthentication().send(stanza.toString());
    }

    /**
     * Returns the response text to send answering the challenge sent by the server. Mechanisms
     * that will never receive a challenge may redefine this method returning <tt>null</tt>.
     *
     * @param bytes the challenge sent by the server.
     * @return the response text to send to answer the challenge sent by the server.
     */
    protected abstract String getChallengeResponse(byte[] bytes);

    /**
     * Returns the common name of the SASL mechanism. E.g.: PLAIN, DIGEST-MD5 or KERBEROS_V4.
     *
     * @return the common name of the SASL mechanism.
     */
    protected abstract String getName();

    /**
     * Returns the authentication text to include in the initial <tt>auth</tt> stanza
     * or <tt>null</tt> if nothing should be added.
     *
     * @param username the username of the user being authenticated.
     * @param host     the hostname where the user account resides.
     * @param password the password of the user.
     * @return the authentication text to include in the initial <tt>auth</tt> stanza
     *         or <tt>null</tt> if nothing should be added.
     */
    protected abstract String getAuthenticationText(String username, String host, String password);

    protected SASLAuthentication getSASLAuthentication() {
        return saslAuthentication;
    }
}
