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
import org.jivesoftware.smackx.packet.*;
import org.xmlpull.v1.XmlPullParser;

/**
 * The MUCUserProvider parses packets with extended presence information about 
 * roles and affiliations.
 *
 * @author Gaston Dombiak
 */
public class MUCUserProvider implements PacketExtensionProvider {

    /**
     * Creates a new MUCUserProvider.
     * ProviderManager requires that every PacketExtensionProvider has a public, no-argument 
     * constructor
     */
    public MUCUserProvider() {
    }

    /**
     * Parses a MUCUser packet (extension sub-packet).
     *
     * @param parser the XML parser, positioned at the starting element of the extension.
     * @return a PacketExtension.
     * @throws Exception if a parsing error occurs.
     */
    public PacketExtension parseExtension(XmlPullParser parser) throws Exception {
        MUCUser mucUser = new MUCUser();
        boolean done = false;
        while (!done) {
            int eventType = parser.next();
            if (eventType == XmlPullParser.START_TAG) {
                if (parser.getName().equals("invite")) {
                    mucUser.setInvite(parseInvite(parser));
                }
                if (parser.getName().equals("item")) {
                    mucUser.setItem(parseItem(parser));
                }
                if (parser.getName().equals("password")) {
                    mucUser.setPassword(parser.nextText());
                }
                if (parser.getName().equals("status")) {
                    mucUser.setStatus(new MUCUser.Status(parser.getAttributeValue("", "code")));
                }
                if (parser.getName().equals("decline")) {
                    mucUser.setDecline(parseDecline(parser));
                }
                if (parser.getName().equals("destroy")) {
                    mucUser.setDestroy(parseDestroy(parser));
                }
            }
            else if (eventType == XmlPullParser.END_TAG) {
                if (parser.getName().equals("x")) {
                    done = true;
                }
            }
        }

        return mucUser;
    }

    private MUCUser.Item parseItem(XmlPullParser parser) throws Exception {
        boolean done = false;
        MUCUser.Item item =
            new MUCUser.Item(
                parser.getAttributeValue("", "affiliation"),
                parser.getAttributeValue("", "role"));
        item.setNick(parser.getAttributeValue("", "nick"));
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

    private MUCUser.Invite parseInvite(XmlPullParser parser) throws Exception {
        boolean done = false;
        MUCUser.Invite invite = new MUCUser.Invite();
        invite.setFrom(parser.getAttributeValue("", "from"));
        invite.setTo(parser.getAttributeValue("", "to"));
        while (!done) {
            int eventType = parser.next();
            if (eventType == XmlPullParser.START_TAG) {
                if (parser.getName().equals("reason")) {
                    invite.setReason(parser.nextText());
                }
            }
            else if (eventType == XmlPullParser.END_TAG) {
                if (parser.getName().equals("invite")) {
                    done = true;
                }
            }
        }
        return invite;
    }

    private MUCUser.Decline parseDecline(XmlPullParser parser) throws Exception {
        boolean done = false;
        MUCUser.Decline decline = new MUCUser.Decline();
        decline.setFrom(parser.getAttributeValue("", "from"));
        decline.setTo(parser.getAttributeValue("", "to"));
        while (!done) {
            int eventType = parser.next();
            if (eventType == XmlPullParser.START_TAG) {
                if (parser.getName().equals("reason")) {
                    decline.setReason(parser.nextText());
                }
            }
            else if (eventType == XmlPullParser.END_TAG) {
                if (parser.getName().equals("decline")) {
                    done = true;
                }
            }
        }
        return decline;
    }

    private MUCUser.Destroy parseDestroy(XmlPullParser parser) throws Exception {
        boolean done = false;
        MUCUser.Destroy destroy = new MUCUser.Destroy();
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
