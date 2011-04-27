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

package org.jivesoftware.smack.packet;

import org.jivesoftware.smack.util.StringUtils;

/**
 * Authentication packet, which can be used to login to a XMPP server as well
 * as discover login information from the server.
 */
public class Authentication extends IQ {

    private String username = null;
    private String password = null;
    private String digest = null;
    private String resource = null;

    /**
     * Create a new authentication packet. By default, the packet will be in
     * "set" mode in order to perform an actual authentication with the server.
     * In order to send a "get" request to get the available authentication
     * modes back from the server, change the type of the IQ packet to "get":
     * <p/>
     * <p><tt>setType(IQ.Type.GET);</tt>
     */
    public Authentication() {
        setType(IQ.Type.SET);
    }

    /**
     * Returns the username, or <tt>null</tt> if the username hasn't been sent.
     *
     * @return the username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username.
     *
     * @param username the username.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns the plain text password or <tt>null</tt> if the password hasn't
     * been set.
     *
     * @return the password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the plain text password.
     *
     * @param password the password.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns the password digest or <tt>null</tt> if the digest hasn't
     * been set. Password digests offer a more secure alternative for
     * authentication compared to plain text. The digest is the hex-encoded
     * SHA-1 hash of the connection ID plus the user's password. If the
     * digest and password are set, digest authentication will be used. If
     * only one value is set, the respective authentication mode will be used.
     *
     * @return the digest of the user's password.
     */
    public String getDigest() {
        return digest;
    }

    /**
     * Sets the digest value using a connection ID and password. Password
     * digests offer a more secure alternative for authentication compared to
     * plain text. The digest is the hex-encoded SHA-1 hash of the connection ID
     * plus the user's password. If the digest and password are set, digest
     * authentication will be used. If only one value is set, the respective
     * authentication mode will be used.
     *
     * @param connectionID the connection ID.
     * @param password     the password.
     * @see org.jivesoftware.smack.XMPPConnection#getConnectionID()
     */
    public void setDigest(String connectionID, String password) {
        this.digest = StringUtils.hash(connectionID + password);
    }

    /**
     * Sets the digest value directly. Password digests offer a more secure
     * alternative for authentication compared to plain text. The digest is
     * the hex-encoded SHA-1 hash of the connection ID plus the user's password.
     * If the digest and password are set, digest authentication will be used.
     * If only one value is set, the respective authentication mode will be used.
     *
     * @param digest the digest, which is the SHA-1 hash of the connection ID
     *               the user's password, encoded as hex.
     * @see org.jivesoftware.smack.XMPPConnection#getConnectionID()
     */
    public void setDigest(String digest) {
        this.digest = digest;
    }

    /**
     * Returns the resource or <tt>null</tt> if the resource hasn't been set.
     *
     * @return the resource.
     */
    public String getResource() {
        return resource;
    }

    /**
     * Sets the resource.
     *
     * @param resource the resource.
     */
    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getChildElementXML() {
        StringBuffer buf = new StringBuffer();
        buf.append("<query xmlns=\"jabber:iq:auth\">");
        if (username != null) {
            if (username.equals("")) {
                buf.append("<username/>");
            }
            else {
                buf.append("<username>").append(username).append("</username>");
            }
        }
        if (digest != null) {
            if (digest.equals("")) {
                buf.append("<digest/>");
            }
            else {
                buf.append("<digest>").append(digest).append("</digest>");
            }
        }
        if (password != null && digest == null) {
            if (password.equals("")) {
                buf.append("<password/>");
            }
            else {
                buf.append("<password>").append(StringUtils.escapeForXML(password)).append("</password>");
            }
        }
        if (resource != null) {
            if (resource.equals("")) {
                buf.append("<resource/>");
            }
            else {
                buf.append("<resource>").append(resource).append("</resource>");
            }
        }
        buf.append("</query>");
        return buf.toString();
    }
}
