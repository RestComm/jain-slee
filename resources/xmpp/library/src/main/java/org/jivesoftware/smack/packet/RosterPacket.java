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
 * Represents XMPP roster packets.
 *
 * @author Matt Tucker
 */
public class RosterPacket extends IQ {

    private List rosterItems = new ArrayList();

    /**
     * Adds a roster item to the packet.
     *
     * @param item a roster item.
     */
    public void addRosterItem(Item item) {
        synchronized (rosterItems) {
            rosterItems.add(item);
        }
    }

    /**
     * Returns the number of roster items in this roster packet.
     *
     * @return the number of roster items.
     */
    public int getRosterItemCount() {
        synchronized (rosterItems) {
            return rosterItems.size();
        }
    }

    /**
     * Returns an Iterator for the roster items in the packet.
     *
     * @return and Iterator for the roster items in the packet.
     */
    public Iterator getRosterItems() {
        synchronized (rosterItems) {
            List entries = Collections.unmodifiableList(new ArrayList(rosterItems));
            return entries.iterator();
        }
    }

    public String getChildElementXML() {
        StringBuffer buf = new StringBuffer();
        buf.append("<query xmlns=\"jabber:iq:roster\">");
        synchronized (rosterItems) {
            for (int i=0; i<rosterItems.size(); i++) {
                Item entry = (Item)rosterItems.get(i);
                buf.append(entry.toXML());
            }
        }
        buf.append("</query>");
        return buf.toString();
    }

    /**
     * A roster item, which consists of a JID, their name, the type of subscription, and
     * the groups the roster item belongs to.
     */
    public static class Item {

        private String user;
        private String name;
        private ItemType itemType;
        private ItemStatus itemStatus;
        private List groupNames;

        /**
         * Creates a new roster item.
         *
         * @param user the user.
         * @param name the user's name.
         */
        public Item(String user, String name) {
            this.user = user.toLowerCase();
            this.name = name;
            itemType = null;
            itemStatus = null;
            groupNames = new ArrayList();
        }

        /**
         * Returns the user.
         *
         * @return the user.
         */
        public String getUser() {
            return user;
        }

        /**
         * Returns the user's name.
         *
         * @return the user's name.
         */
        public String getName() {
            return name;
        }

        /**
         * Sets the user's name.
         *
         * @param name the user's name.
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * Returns the roster item type.
         *
         * @return the roster item type.
         */
        public ItemType getItemType() {
            return itemType;
        }

        /**
         * Sets the roster item type.
         *
         * @param itemType the roster item type.
         */
        public void setItemType(ItemType itemType) {
            this.itemType = itemType;
        }

        /**
         * Returns the roster item status.
         *
         * @return the roster item status.
         */
        public ItemStatus getItemStatus() {
            return itemStatus;
        }

        /**
         * Sets the roster item status.
         *
         * @param itemStatus the roster item status.
         */
        public void setItemStatus(ItemStatus itemStatus) {
            this.itemStatus = itemStatus;
        }

        /**
         * Returns an Iterator for the group names (as Strings) that the roster item
         * belongs to.
         *
         * @return an Iterator for the group names.
         */
        public Iterator getGroupNames() {
            synchronized (groupNames) {
                return Collections.unmodifiableList(groupNames).iterator();
            }
        }

        /**
         * Adds a group name.
         *
         * @param groupName the group name.
         */
        public void addGroupName(String groupName) {
            synchronized (groupNames) {
                if (!groupNames.contains(groupName)) {
                    groupNames.add(groupName);
                }
            }
        }

        /**
         * Removes a group name.
         *
         * @param groupName the group name.
         */
        public void removeGroupName(String groupName) {
            synchronized (groupNames) {
                groupNames.remove(groupName);
            }
        }

        public String toXML() {
            StringBuffer buf = new StringBuffer();
            buf.append("<item jid=\"").append(user).append("\"");
            if (name != null) {
                buf.append(" name=\"").append(name).append("\"");
            }
            if (itemType != null) {
                buf.append(" subscription=\"").append(itemType).append("\"");
            }
            if (itemStatus != null) {
                buf.append(" ask=\"").append(itemStatus).append("\"");
            }
            buf.append(">");
            synchronized (groupNames) {
                for (int i=0; i<groupNames.size(); i++) {
                    String groupName = (String)groupNames.get(i);
                    buf.append("<group>").append(groupName).append("</group>");
                }
            }
            buf.append("</item>");
            return buf.toString();
        }
    }

    /**
     * The subscription status of a roster item. An optional element that indicates
     * the subscription status if a change request is pending.
     */
    public static class ItemStatus {

        /**
         * Request to subcribe.
         */
        public static final ItemStatus SUBSCRIPTION_PENDING = new ItemStatus("subscribe");

        /**
         * Request to unsubscribe.
         */
        public static final ItemStatus UNSUBCRIPTION_PENDING = new ItemStatus("unsubscribe");

        public static ItemStatus fromString(String value) {
            if (value == null) {
                return null;
            }
            value = value.toLowerCase();
            if ("unsubscribe".equals(value)) {
                return SUBSCRIPTION_PENDING;
            }
            else if ("subscribe".equals(value)) {
                return SUBSCRIPTION_PENDING;
            }
            else {
                return null;
            }
        }

        private String value;

        /**
         * Returns the item status associated with the specified string.
         *
         * @param value the item status.
         */
        private ItemStatus(String value) {
            this.value = value;
        }

        public String toString() {
            return value;
        }
    }

    /**
     * The subscription type of a roster item.
     */
    public static class ItemType {

        /**
         * The user and subscriber have no interest in each other's presence.
         */
        public static final ItemType NONE = new ItemType("none");

        /**
         * The user is interested in receiving presence updates from the subscriber.
         */
        public static final ItemType TO = new ItemType("to");

        /**
         * The subscriber is interested in receiving presence updates from the user.
         */
        public static final ItemType FROM = new ItemType("from");

        /**
         * The user and subscriber have a mutual interest in each other's presence.
         */
        public static final ItemType BOTH = new ItemType("both");

        /**
         * The user wishes to stop receiving presence updates from the subscriber.
         */
        public static final ItemType REMOVE = new ItemType("remove");

        public static ItemType fromString(String value) {
            if (value == null) {
                return null;
            }
            value = value.toLowerCase();
            if ("none".equals(value)) {
                return NONE;
            }
            else if ("to".equals(value)) {
                return TO;
            }
            else if ("from".equals(value)) {
                return FROM;
            }
            else if ("both".equals(value)) {
                return BOTH;
            }
            else if ("remove".equals(value)) {
                return REMOVE;
            }
            else {
                return null;
            }
        }

        private String value;

        /**
         * Returns the item type associated with the specified string.
         *
         * @param value the item type.
         */
        public ItemType(String value) {
            this.value = value;
        }

        public String toString() {
            return value;
        }
    }
}