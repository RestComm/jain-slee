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

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smackx.packet.*;
import org.xmlpull.v1.XmlPullParser;

/**
* The DiscoverInfoProvider parses Service Discovery items packets.
*
* @author Gaston Dombiak
*/
public class DiscoverItemsProvider implements IQProvider {

    public IQ parseIQ(XmlPullParser parser) throws Exception {
        DiscoverItems discoverItems = new DiscoverItems();
        boolean done = false;
        DiscoverItems.Item item = null;
        String jid = "";
        String name = "";
        String action = "";
        String node = "";
        discoverItems.setNode(parser.getAttributeValue("", "node"));
        while (!done) {
            int eventType = parser.next();
            if (eventType == XmlPullParser.START_TAG) {
                if (parser.getName().equals("item")) {
                    // Initialize the variables from the parsed XML
                    jid = parser.getAttributeValue("", "jid");
                    name = parser.getAttributeValue("", "name");
                    node = parser.getAttributeValue("", "node");
                    action = parser.getAttributeValue("", "action");
                }
            } else if (eventType == XmlPullParser.END_TAG) {
                if (parser.getName().equals("item")) {
                    // Create a new Item and add it to DiscoverItems.
                    item = new DiscoverItems.Item(jid);
                    item.setName(name);
                    item.setNode(node);
                    item.setAction(action);
                    discoverItems.addItem(item);
                }
                if (parser.getName().equals("query")) {
                    done = true;
                }
            }
        }

        return discoverItems;
    }
}