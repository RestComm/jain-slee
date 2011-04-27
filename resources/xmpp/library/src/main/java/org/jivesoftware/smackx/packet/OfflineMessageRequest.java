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
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Represents a request to get some or all the offline messages of a user. This class can also
 * be used for deleting some or all the offline messages of a user.
 *
 * @author Gaston Dombiak
 */
public class OfflineMessageRequest extends IQ {

    private List items = new ArrayList();
    private boolean purge = false;
    private boolean fetch = false;

    /**
     * Returns an Iterator for item childs that holds information about offline messages to
     * view or delete.
     *
     * @return an Iterator for item childs that holds information about offline messages to
     *         view or delete.
     */
    public Iterator getItems() {
        synchronized (items) {
            return Collections.unmodifiableList(new ArrayList(items)).iterator();
        }
    }

    /**
     * Adds an item child that holds information about offline messages to view or delete.
     *
     * @param item the item child that holds information about offline messages to view or delete.
     */
    public void addItem(Item item) {
        synchronized (items) {
            items.add(item);
        }
    }

    /**
     * Returns true if all the offline messages of the user should be deleted.
     *
     * @return true if all the offline messages of the user should be deleted.
     */
    public boolean isPurge() {
        return purge;
    }

    /**
     * Sets if all the offline messages of the user should be deleted.
     *
     * @param purge true if all the offline messages of the user should be deleted.
     */
    public void setPurge(boolean purge) {
        this.purge = purge;
    }

    /**
     * Returns true if all the offline messages of the user should be retrieved.
     *
     * @return true if all the offline messages of the user should be retrieved.
     */
    public boolean isFetch() {
        return fetch;
    }

    /**
     * Sets if all the offline messages of the user should be retrieved.
     *
     * @param fetch true if all the offline messages of the user should be retrieved.
     */
    public void setFetch(boolean fetch) {
        this.fetch = fetch;
    }

    public String getChildElementXML() {
        StringBuffer buf = new StringBuffer();
        buf.append("<offline xmlns=\"http://jabber.org/protocol/offline\">");
        synchronized (items) {
            for (int i = 0; i < items.size(); i++) {
                Item item = (Item) items.get(i);
                buf.append(item.toXML());
            }
        }
        if (purge) {
            buf.append("<purge/>");
        }
        if (fetch) {
            buf.append("<fetch/>");
        }
        // Add packet extensions, if any are defined.
        buf.append(getExtensionsXML());
        buf.append("</offline>");
        return buf.toString();
    }

    /**
     * Item child that holds information about offline messages to view or delete.
     *
     * @author Gaston Dombiak
     */
    public static class Item {
        private String action;
        private String jid;
        private String node;

        /**
         * Creates a new item child.
         *
         * @param node the actor's affiliation to the room
         */
        public Item(String node) {
            this.node = node;
        }

        public String getNode() {
            return node;
        }

        /**
         * Returns "view" or "remove" that indicate if the server should return the specified
         * offline message or delete it.
         *
         * @return "view" or "remove" that indicate if the server should return the specified
         *         offline message or delete it.
         */
        public String getAction() {
            return action;
        }

        /**
         * Sets if the server should return the specified offline message or delete it. Possible
         * values are "view" or "remove".
         *
         * @param action if the server should return the specified offline message or delete it.
         */
        public void setAction(String action) {
            this.action = action;
        }

        public String getJid() {
            return jid;
        }

        public void setJid(String jid) {
            this.jid = jid;
        }

        public String toXML() {
            StringBuffer buf = new StringBuffer();
            buf.append("<item");
            if (getAction() != null) {
                buf.append(" action=\"").append(getAction()).append("\"");
            }
            if (getJid() != null) {
                buf.append(" jid=\"").append(getJid()).append("\"");
            }
            if (getNode() != null) {
                buf.append(" node=\"").append(getNode()).append("\"");
            }
            buf.append("/>");
            return buf.toString();
        }
    }

    public static class Provider implements IQProvider {

        public IQ parseIQ(XmlPullParser parser) throws Exception {
            OfflineMessageRequest request = new OfflineMessageRequest();
            boolean done = false;
            while (!done) {
                int eventType = parser.next();
                if (eventType == XmlPullParser.START_TAG) {
                    if (parser.getName().equals("item")) {
                        request.addItem(parseItem(parser));
                    }
                    else if (parser.getName().equals("purge")) {
                        request.setPurge(true);
                    }
                    else if (parser.getName().equals("fetch")) {
                        request.setFetch(true);
                    }
                } else if (eventType == XmlPullParser.END_TAG) {
                    if (parser.getName().equals("offline")) {
                        done = true;
                    }
                }
            }

            return request;
        }

        private Item parseItem(XmlPullParser parser) throws Exception {
            boolean done = false;
            Item item = new Item(parser.getAttributeValue("", "node"));
            item.setAction(parser.getAttributeValue("", "action"));
            item.setJid(parser.getAttributeValue("", "jid"));
            while (!done) {
                int eventType = parser.next();
                if (eventType == XmlPullParser.END_TAG) {
                    if (parser.getName().equals("item")) {
                        done = true;
                    }
                }
            }
            return item;
        }
    }
}
