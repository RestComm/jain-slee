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

package org.jivesoftware.smackx.packet;

import org.jivesoftware.smack.packet.IQ;

/**
 * A Version IQ packet, which is used by XMPP clients to discover version information
 * about the software running at another entity's JID.<p>
 *
 * An example to discover the version of the server:
 * <pre>
 * // Request the version from the server.
 * Version versionRequest = new Version();
 * timeRequest.setType(IQ.Type.GET);
 * timeRequest.setTo("example.com");
 *
 * // Create a packet collector to listen for a response.
 * PacketCollector collector = con.createPacketCollector(
 *                new PacketIDFilter(versionRequest.getPacketID()));
 *
 * con.sendPacket(versionRequest);
 *
 * // Wait up to 5 seconds for a result.
 * IQ result = (IQ)collector.nextResult(5000);
 * if (result != null && result.getType() == IQ.Type.RESULT) {
 *     Version versionResult = (Version)result;
 *     // Do something with result...
 * }</pre><p>
 *
 * @author Gaston Dombiak
 */
public class Version extends IQ {

    private String name;
    private String version;
    private String os;

    /**
     * Returns the natural-language name of the software. This property will always be
     * present in a result.
     *
     * @return the natural-language name of the software.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the natural-language name of the software. This message should only be
     * invoked when parsing the XML and setting the property to a Version instance.
     *
     * @param name the natural-language name of the software.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the specific version of the software. This property will always be
     * present in a result.
     *
     * @return the specific version of the software.
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the specific version of the software. This message should only be
     * invoked when parsing the XML and setting the property to a Version instance.
     *
     * @param version the specific version of the software.
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * Returns the operating system of the queried entity. This property will always be
     * present in a result.
     *
     * @return the operating system of the queried entity.
     */
    public String getOs() {
        return os;
    }

    /**
     * Sets the operating system of the queried entity. This message should only be
     * invoked when parsing the XML and setting the property to a Version instance.
     *
     * @param os operating system of the queried entity.
     */
    public void setOs(String os) {
        this.os = os;
    }

    public String getChildElementXML() {
        StringBuffer buf = new StringBuffer();
        buf.append("<query xmlns=\"jabber:iq:version\">");
        if (name != null) {
            buf.append("<name>").append(name).append("</name>");
        }
        if (version != null) {
            buf.append("<version>").append(version).append("</version>");
        }
        if (os != null) {
            buf.append("<os>").append(os).append("</os>");
        }
        buf.append("</query>");
        return buf.toString();
    }
}
