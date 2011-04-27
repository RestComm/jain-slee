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

import java.util.*;

import org.jivesoftware.smack.packet.PacketExtension;

/**
 * An XHTML sub-packet, which is used by XMPP clients to exchange formatted text. The XHTML 
 * extension is only a subset of XHTML 1.0.<p>
 * 
 * The following link summarizes the requirements of XHTML IM:
 * <a href="http://www.jabber.org/jeps/jep-0071.html#sect-id2598018">Valid tags</a>.<p>
 * 
 * Warning: this is an non-standard protocol documented by
 * <a href="http://www.jabber.org/jeps/jep-0071.html">JEP-71</a>. Because this is a
 * non-standard protocol, it is subject to change.
 *
 * @author Gaston Dombiak
 */
public class XHTMLExtension implements PacketExtension {

    private List bodies = new ArrayList();

    /**
    * Returns the XML element name of the extension sub-packet root element.
    * Always returns "html"
    *
    * @return the XML element name of the packet extension.
    */
    public String getElementName() {
        return "html";
    }

    /** 
     * Returns the XML namespace of the extension sub-packet root element.
     * According the specification the namespace is always "http://jabber.org/protocol/xhtml-im"
     *
     * @return the XML namespace of the packet extension.
     */
    public String getNamespace() {
        return "http://jabber.org/protocol/xhtml-im";
    }

    /**
     * Returns the XML representation of a XHTML extension according the specification.
     * 
     * Usually the XML representation will be inside of a Message XML representation like
     * in the following example:
     * <pre>
     * &lt;message id="MlIpV-4" to="gato1@gato.home" from="gato3@gato.home/Smack"&gt;
     *     &lt;subject&gt;Any subject you want&lt;/subject&gt;
     *     &lt;body&gt;This message contains something interesting.&lt;/body&gt;
     *     &lt;html xmlns="http://jabber.org/protocol/xhtml-im"&gt;
     *         &lt;body&gt;&lt;p style='font-size:large'&gt;This message contains something &lt;em&gt;interesting&lt;/em&gt;.&lt;/p&gt;&lt;/body&gt;
     *     &lt;/html&gt;
     * &lt;/message&gt;
     * </pre>
     * 
     */
    public String toXML() {
        StringBuffer buf = new StringBuffer();
        buf.append("<").append(getElementName()).append(" xmlns=\"").append(getNamespace()).append(
            "\">");
        // Loop through all the bodies and append them to the string buffer
        for (Iterator i = getBodies(); i.hasNext();) {
            buf.append((String) i.next());
        }
        buf.append("</").append(getElementName()).append(">");
        return buf.toString();
    }

    /**
     * Returns an Iterator for the bodies in the packet.
     *
     * @return an Iterator for the bodies in the packet.
     */
    public Iterator getBodies() {
        synchronized (bodies) {
            return Collections.unmodifiableList(new ArrayList(bodies)).iterator();
        }
    }

    /**
     * Adds a body to the packet.
     *
     * @param body the body to add.
     */
    public void addBody(String body) {
        synchronized (bodies) {
            bodies.add(body);
        }
    }

    /**
     * Returns a count of the bodies in the XHTML packet.
     *
     * @return the number of bodies in the XHTML packet.
     */
    public int getBodiesCount() {
        return bodies.size();
    }

}
