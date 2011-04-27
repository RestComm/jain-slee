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

package org.jivesoftware.smack.filter;

import org.jivesoftware.smack.packet.Packet;

/**
 * Filters for packets with a particular type of packet extension.
 *
 * @author Matt Tucker
 */
public class PacketExtensionFilter implements PacketFilter {

    private String elementName;
    private String namespace;

    /**
     * Creates a new packet extension filter. Packets will pass the filter if
     * they have a packet extension that matches the specified element name
     * and namespace.
     *
     * @param elementName the XML element name of the packet extension.
     * @param namespace the XML namespace of the packet extension.
     */
    public PacketExtensionFilter(String elementName, String namespace) {
        this.elementName = elementName;
        this.namespace = namespace;
    }

    public boolean accept(Packet packet) {
        return packet.getExtension(elementName, namespace) != null;
    }
}
