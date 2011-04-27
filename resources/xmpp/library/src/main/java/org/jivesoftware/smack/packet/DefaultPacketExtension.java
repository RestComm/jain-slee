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

import java.util.*;

/**
 * Default implementation of the PacketExtension interface. Unless a PacketExtensionProvider
 * is registered with {@link org.jivesoftware.smack.provider.ProviderManager ProviderManager},
 * instances of this class will be returned when getting packet extensions.<p>
 *
 * This class provides a very simple representation of an XML sub-document. Each element
 * is a key in a Map with its CDATA being the value. For example, given the following
 * XML sub-document:
 *
 * <pre>
 * &lt;foo xmlns="http://bar.com"&gt;
 *     &lt;color&gt;blue&lt;/color&gt;
 *     &lt;food&gt;pizza&lt;/food&gt;
 * &lt;/foo&gt;</pre>
 *
 * In this case, getValue("color") would return "blue", and getValue("food") would
 * return "pizza". This parsing mechanism mechanism is very simplistic and will not work
 * as desired in all cases (for example, if some of the elements have attributes. In those
 * cases, a custom PacketExtensionProvider should be used.
 *
 * @author Matt Tucker
 */
public class DefaultPacketExtension implements PacketExtension {

    private String elementName;
    private String namespace;
    private Map map;

    /**
     * Creates a new generic packet extension.
     *
     * @param elementName the name of the element of the XML sub-document.
     * @param namespace the namespace of the element.
     */
    public DefaultPacketExtension(String elementName, String namespace) {
        this.elementName = elementName;
        this.namespace = namespace;
    }

     /**
     * Returns the XML element name of the extension sub-packet root element.
     *
     * @return the XML element name of the packet extension.
     */
    public String getElementName() {
        return elementName;
    }

    /**
     * Returns the XML namespace of the extension sub-packet root element.
     *
     * @return the XML namespace of the packet extension.
     */
    public String getNamespace() {
        return namespace;
    }

    public String toXML() {
        StringBuffer buf = new StringBuffer();
        buf.append("<").append(elementName).append(" xmlns=\"").append(namespace).append("\">");
        for (Iterator i=getNames(); i.hasNext(); ) {
            String name = (String)i.next();
            String value = getValue(name);
            buf.append("<").append(name).append(">");
            buf.append(value);
            buf.append("</").append(name).append(">");
        }
        buf.append("</").append(elementName).append(">");
        return buf.toString();
    }

    /**
     * Returns an Iterator for the names that can be used to get
     * values of the packet extension.
     *
     * @return an Iterator for the names.
     */
    public synchronized Iterator getNames() {
        if (map == null) {
            return Collections.EMPTY_LIST.iterator();
        }
        return Collections.unmodifiableMap(new HashMap(map)).keySet().iterator();
    }

    /**
     * Returns a packet extension value given a name.
     *
     * @param name the name.
     * @return the value.
     */
    public synchronized String getValue(String name) {
        if (map == null) {
            return null;
        }
        return (String)map.get(name);
    }

    /**
     * Sets a packet extension value using the given name.
     *
     * @param name the name.
     * @param value the value.
     */
    public synchronized void setValue(String name, String value) {
        if (map == null) {
            map = new HashMap();
        }
        map.put(name, value);
    }
}