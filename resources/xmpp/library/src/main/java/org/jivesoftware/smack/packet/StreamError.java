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

/**
 * Represents a stream error packet. Stream errors are unrecoverable errors where the server
 * will close the unrelying TCP connection after the stream error was sent to the client.
 * These is the list of stream errors as defined in the XMPP spec:<p>
 *
 * <table border=1>
 *      <tr><td><b>Code</b></td><td><b>Description</b></td></tr>
 *      <tr><td> bad-format </td><td> the entity has sent XML that cannot be processed </td></tr>
 *      <tr><td> unsupported-encoding </td><td>  the entity has sent a namespace prefix that is
 *          unsupported </td></tr>
 *      <tr><td> bad-namespace-prefix </td><td> Remote Server Timeout </td></tr>
 *      <tr><td> conflict </td><td> the server is closing the active stream for this entity
 *          because a new stream has been initiated that conflicts with the existing
 *          stream. </td></tr>
 *      <tr><td> connection-timeout </td><td> the entity has not generated any traffic over
 *          the stream for some period of time. </td></tr>
 *      <tr><td> host-gone </td><td> the value of the 'to' attribute provided by the initiating
 *          entity in the stream header corresponds to a hostname that is no longer hosted by
 *          the server. </td></tr>
 *      <tr><td> host-unknown </td><td> the value of the 'to' attribute provided by the
 *          initiating entity in the stream header does not correspond to a hostname that is
 *          hosted by the server. </td></tr>
 *      <tr><td> improper-addressing </td><td> a stanza sent between two servers lacks a 'to'
 *          or 'from' attribute </td></tr>
 *      <tr><td> internal-server-error </td><td> the server has experienced a
 *          misconfiguration. </td></tr>
 *      <tr><td> invalid-from </td><td> the JID or hostname provided in a 'from' address does
 *          not match an authorized JID. </td></tr>
 *      <tr><td> invalid-id </td><td> the stream ID or dialback ID is invalid or does not match
 *          an ID previously provided. </td></tr>
 *      <tr><td> invalid-namespace </td><td> the streams namespace name is invalid. </td></tr>
 *      <tr><td> invalid-xml </td><td> the entity has sent invalid XML over the stream. </td></tr>
 *      <tr><td> not-authorized </td><td> the entity has attempted to send data before the
 *          stream has been authenticated </td></tr>
 *      <tr><td> policy-violation </td><td> the entity has violated some local service
 *          policy. </td></tr>
 *      <tr><td> remote-connection-failed </td><td> Rthe server is unable to properly connect
 *          to a remote entity. </td></tr>
 *      <tr><td> resource-constraint </td><td> Rthe server lacks the system resources necessary
 *          to service the stream. </td></tr>
 *      <tr><td> restricted-xml </td><td> the entity has attempted to send restricted XML
 *          features. </td></tr>
 *      <tr><td> see-other-host </td><td>  the server will not provide service to the initiating
 *          entity but is redirecting traffic to another host. </td></tr>
 *      <tr><td> system-shutdown </td><td> the server is being shut down and all active streams
 *          are being closed. </td></tr>
 *      <tr><td> undefined-condition </td><td> the error condition is not one of those defined
 *          by the other conditions in this list. </td></tr>
 *      <tr><td> unsupported-encoding </td><td> the initiating entity has encoded the stream in
 *          an encoding that is not supported. </td></tr>
 *      <tr><td> unsupported-stanza-type </td><td> the initiating entity has sent a first-level
 *          child of the stream that is not supported. </td></tr>
 *      <tr><td> unsupported-version </td><td> the value of the 'version' attribute provided by
 *          the initiating entity in the stream header specifies a version of XMPP that is not
 *          supported. </td></tr>
 *      <tr><td> xml-not-well-formed </td><td> the initiating entity has sent XML that is
 *          not well-formed. </td></tr>
 * </table>
 *
 * @author Gaston Dombiak
 */
public class StreamError {

    private String code;

    public StreamError(String code) {
        super();
        this.code = code;
    }

    /**
     * Returns the error code.
     *
     * @return the error code.
     */
    public String getCode() {
        return code;
    }

    public String toString() {
        StringBuffer txt = new StringBuffer();
        txt.append("stream:error (").append(code).append(")");
        return txt.toString();
    }
}
