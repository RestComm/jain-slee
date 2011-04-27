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

import org.jivesoftware.smack.packet.*;
import org.jivesoftware.smack.provider.*;
import org.jivesoftware.smack.util.PacketParserUtils;
import org.jivesoftware.smackx.packet.MUCOwner;
import org.xmlpull.v1.XmlPullParser;

/**
 * The MUCOwnerProvider parses MUCOwner packets. (@see MUCOwner)
 * 
 * @author Gaston Dombiak
 */
public class MUCOwnerProvider implements IQProvider {

    public IQ parseIQ(XmlPullParser parser) throws Exception {
        MUCOwner mucOwner = new MUCOwner();
        boolean done = false;
        while (!done) {
            int eventType = parser.next();
            if (eventType == XmlPullParser.START_TAG) {
                if (parser.getName().equals("item")) {
                    mucOwner.addItem(parseItem(parser));
                }
                else if (parser.getName().equals("destroy")) {
                    mucOwner.setDestroy(parseDestroy(parser));
                }
                // Otherwise, it must be a packet extension.
                else {
                    mucOwner.addExtension(PacketParserUtils.parsePacketExtension(parser.getName(),
                            parser.getNamespace(), parser));
                }
            }
            else if (eventType == XmlPullParser.END_TAG) {
                if (parser.getName().equals("query")) {
                    done = true;
                }
            }
        }

        return mucOwner;
    }

    private MUCOwner.Item parseItem(XmlPullParser parser) throws Exception {
        boolean done = false;
        MUCOwner.Item item = new MUCOwner.Item(parser.getAttributeValue("", "affiliation"));
        item.setNick(parser.getAttributeValue("", "nick"));
        item.setRole(parser.getAttributeValue("", "role"));
        item.setJid(parser.getAttributeValue("", "jid"));
        while (!done) {
            int eventType = parser.next();
            if (eventType == XmlPullParser.START_TAG) {
                if (parser.getName().equals("actor")) {
                    item.setActor(parser.getAttributeValue("", "jid"));
                }
                if (parser.getName().equals("reason")) {
                    item.setReason(parser.nextText());
                }
            }
            else if (eventType == XmlPullParser.END_TAG) {
                if (parser.getName().equals("item")) {
                    done = true;
                }
            }
        }
        return item;
    }

    private MUCOwner.Destroy parseDestroy(XmlPullParser parser) throws Exception {
        boolean done = false;
        MUCOwner.Destroy destroy = new MUCOwner.Destroy();
        destroy.setJid(parser.getAttributeValue("", "jid"));
        while (!done) {
            int eventType = parser.next();
            if (eventType == XmlPullParser.START_TAG) {
                if (parser.getName().equals("reason")) {
                    destroy.setReason(parser.nextText());
                }
            }
            else if (eventType == XmlPullParser.END_TAG) {
                if (parser.getName().equals("destroy")) {
                    done = true;
                }
            }
        }
        return destroy;
    }
}
