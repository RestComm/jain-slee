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

package org.jivesoftware.smackx.provider;

import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.jivesoftware.smackx.packet.XHTMLExtension;
import org.xmlpull.v1.XmlPullParser;

/**
 * The XHTMLExtensionProvider parses XHTML packets.
 *
 * @author Gaston Dombiak
 */
public class XHTMLExtensionProvider implements PacketExtensionProvider {

    /**
     * Creates a new XHTMLExtensionProvider.
     * ProviderManager requires that every PacketExtensionProvider has a public, no-argument constructor
     */
    public XHTMLExtensionProvider() {
    }

    /**
     * Parses a XHTMLExtension packet (extension sub-packet).
     *
     * @param parser the XML parser, positioned at the starting element of the extension.
     * @return a PacketExtension.
     * @throws Exception if a parsing error occurs.
     */
    public PacketExtension parseExtension(XmlPullParser parser)
        throws Exception {
        XHTMLExtension xhtmlExtension = new XHTMLExtension();
        boolean done = false;
        StringBuffer buffer = new StringBuffer();;
        while (!done) {
            int eventType = parser.next();
            if (eventType == XmlPullParser.START_TAG) {
                if (parser.getName().equals("body")) 
                    buffer = new StringBuffer();
                buffer.append(parser.getText());
            } else if (eventType == XmlPullParser.TEXT) {
                if (buffer != null) buffer.append(parser.getText());
            } else if (eventType == XmlPullParser.END_TAG) {
                if (parser.getName().equals("body")) {
                    buffer.append(parser.getText());
                    xhtmlExtension.addBody(buffer.toString());
                }
                else if (parser.getName().equals(xhtmlExtension.getElementName())) {
                    done = true;
                }
                else 
                    buffer.append(parser.getText());
            }
        }

        return xhtmlExtension;
    }

}
