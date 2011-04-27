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

/**
 * Implementation of the SASL PLAIN mechanisn as defined by the
 * <a href="http://www.ietf.org/internet-drafts/draft-ietf-sasl-plain-08.txt">IETF draft
 * document</a>.
 *
 * @author Gaston Dombiak
 */
public class SASLPlainMechanism extends SASLMechanism {

    public SASLPlainMechanism(SASLAuthentication saslAuthentication) {
        super(saslAuthentication);
    }

    protected String getName() {
        return "PLAIN";
    }

    protected String getAuthenticationText(String username, String host, String password) {
        // Build the text containing the "authorization identity" + NUL char +
        // "authentication identity" + NUL char + "clear-text password"
        StringBuffer text = new StringBuffer();
        text.append(username).append("@").append(host);
        text.append('\0');
        text.append(username);
        text.append('\0');
        text.append(password);
        return text.toString();
    }

    protected String getChallengeResponse(byte[] bytes) {
        // Return null since this mechanism will never get a challenge from the server
        return null;
    }
}
