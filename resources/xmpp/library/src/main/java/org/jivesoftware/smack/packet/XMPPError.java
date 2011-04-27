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
 * Represents a XMPP error sub-packet. Typically, a server responds to a request that has
 * problems by sending the packet back and including an error packet. Each error has a code
 * as well as as an optional text explanation. Typical error codes are as follows:<p>
 *
 * <table border=1>
 *      <tr><td><b>Code</b></td><td><b>Description</b></td></tr>
 *      <tr><td> 302 </td><td> Redirect </td></tr>
 *      <tr><td> 400 </td><td> Bad Request </td></tr>
 *      <tr><td> 401 </td><td> Unauthorized </td></tr>
 *      <tr><td> 402 </td><td> Payment Required </td></tr>
 *      <tr><td> 403 </td><td> Forbidden </td></tr>
 *      <tr><td> 404 </td><td> Not Found </td></tr>
 *      <tr><td> 405 </td><td> Not Allowed </td></tr>
 *      <tr><td> 406 </td><td> Not Acceptable </td></tr>
 *      <tr><td> 407 </td><td> Registration Required </td></tr>
 *      <tr><td> 408 </td><td> Request Timeout </td></tr>
 *      <tr><td> 409 </td><td> Conflict </td></tr>
 *      <tr><td> 500 </td><td> Internal Server XMPPError </td></tr>
 *      <tr><td> 501 </td><td> Not Implemented </td></tr>
 *      <tr><td> 502 </td><td> Remote Server Error </td></tr>
 *      <tr><td> 503 </td><td> Service Unavailable </td></tr>
 *      <tr><td> 504 </td><td> Remote Server Timeout </td></tr>
 * </table>
 *
 * @author Matt Tucker
 */
public class XMPPError {

    private int code;
    private String message;

    /**
     * Creates a new  error with the specified code and no message..
     *
     * @param code the error code.
     */
    public XMPPError(int code) {
        this.code = code;
        this.message = null;
    }

    /**
     * Creates a new error with the specified code and message.
     *
     * @param code the error code.
     * @param message a message describing the error.
     */
    public XMPPError(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * Returns the error code.
     *
     * @return the error code.
     */
    public int getCode() {
        return code;
    }

    /**
     * Returns the message describing the error, or null if there is no message.
     *
     * @return the message describing the error, or null if there is no message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Returns the error as XML.
     *
     * @return the error as XML.
     */
    public String toXML() {
        StringBuffer buf = new StringBuffer();
        buf.append("<error code=\"").append(code).append("\">");
        if (message != null) {
            buf.append(message);
        }
        buf.append("</error>");
        return buf.toString();
    }

    public String toString() {
        StringBuffer txt = new StringBuffer();
        txt.append("(").append(code).append(")");
        if (message != null) {
            txt.append(" ").append(message);
        }
        return txt.toString();
    }
}
