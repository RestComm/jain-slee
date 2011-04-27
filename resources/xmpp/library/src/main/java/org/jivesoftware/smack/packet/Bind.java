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
 * IQ packet used by Smack to bind a resource and to obtain the jid assigned by the server.
 * There are two ways to bind a resource. One is simply sending an empty Bind packet where the
 * server will assign a new resource for this connection. The other option is to set a desired
 * resource but the server may return a modified version of the sent resource.<p>
 *
 * For more information refer to the following
 * <a href=http://www.xmpp.org/specs/rfc3920.html#bind>link</a>. 
 *
 * @author Gaston Dombiak
 */
public class Bind extends IQ {

    private String resource = null;
    private String jid = null;

    public Bind() {
        setType(IQ.Type.SET);
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getJid() {
        return jid;
    }

    public void setJid(String jid) {
        this.jid = jid;
    }

    public String getChildElementXML() {
        StringBuffer buf = new StringBuffer();
        buf.append("<bind xmlns=\"urn:ietf:params:xml:ns:xmpp-bind\">");
        if (resource != null) {
            buf.append("<resource>").append(resource).append("</resource>");
        }
        if (jid != null) {
            buf.append("<jid>").append(jid).append("</jid>");
        }
        buf.append("</bind>");
        return buf.toString();
    }
}
