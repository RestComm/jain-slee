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
 * Interface to represent packet extensions. A packet extension is an XML subdocument
 * with a root element name and namespace. Packet extensions are used to provide
 * extended functionality beyond what is in the base XMPP specification. Examples of
 * packet extensions include message events, message properties, and extra presence data.
 * IQ packets cannot contain packet extensions.
 *
 * @see DefaultPacketExtension
 * @see org.jivesoftware.smack.provider.PacketExtensionProvider
 * @author Matt Tucker
 */
public interface PacketExtension {

    /**
     * Returns the root element name.
     *
     * @return the element name.
     */
    public String getElementName();

    /**
     * Returns the root element XML namespace.
     *
     * @return the namespace.
     */
    public String getNamespace();

    /**
     * Returns the XML reppresentation of the PacketExtension.
     *
     * @return the packet extension as XML.
     */
    public String toXML();
}
