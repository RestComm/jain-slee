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

import org.jivesoftware.smack.packet.IQ;

/**
 * IQ packet that serves for kicking users, granting and revoking voice, banning users, 
 * modifying the ban list, granting and revoking membership and granting and revoking 
 * moderator privileges. All these operations are scoped by the 
 * 'http://jabber.org/protocol/muc#admin' namespace.
 * 
 * @author Gaston Dombiak
 */
public class MUCAdmin extends IQ {

    private List items = new ArrayList();

    /**
     * Returns an Iterator for item childs that holds information about roles, affiliation, 
     * jids and nicks.
     * 
     * @return an Iterator for item childs that holds information about roles, affiliation,
     *          jids and nicks.
     */
    public Iterator getItems() {
        synchronized (items) {
            return Collections.unmodifiableList(new ArrayList(items)).iterator();
        }
    }

    /**
     * Adds an item child that holds information about roles, affiliation, jids and nicks.
     * 
     * @param item the item child that holds information about roles, affiliation, jids and nicks.
     */
    public void addItem(Item item) {
        synchronized (items) {
            items.add(item);
        }
    }

    public String getChildElementXML() {
        StringBuffer buf = new StringBuffer();
        buf.append("<query xmlns=\"http://jabber.org/protocol/muc#admin\">");
        synchronized (items) {
            for (int i = 0; i < items.size(); i++) {
                Item item = (Item) items.get(i);
                buf.append(item.toXML());
            }
        }
        // Add packet extensions, if any are defined.
        buf.append(getExtensionsXML());
        buf.append("</query>");
        return buf.toString();
    }

    /**
     * Item child that holds information about roles, affiliation, jids and nicks.
     *
     * @author Gaston Dombiak
     */
    public static class Item {
        private String actor;
        private String reason;
        private String affiliation;
        private String jid;
        private String nick;
        private String role;
        
        /**
         * Creates a new item child. 
         * 
         * @param affiliation the actor's affiliation to the room
         * @param role the privilege level of an occupant within a room.
         */
        public Item(String affiliation, String role) {
            this.affiliation = affiliation;
            this.role = role;
        }
        
        /**
         * Returns the actor (JID of an occupant in the room) that was kicked or banned.
         * 
         * @return the JID of an occupant in the room that was kicked or banned.
         */
        public String getActor() {
            return actor;
        }

        /**
         * Returns the reason for the item child. The reason is optional and could be used to
         * explain the reason why a user (occupant) was kicked or banned.
         *  
         * @return the reason for the item child.
         */
        public String getReason() {
            return reason;
        }

        /**
         * Returns the occupant's affiliation to the room. The affiliation is a semi-permanent 
         * association or connection with a room. The possible affiliations are "owner", "admin", 
         * "member", and "outcast" (naturally it is also possible to have no affiliation). An 
         * affiliation lasts across a user's visits to a room.
         * 
         * @return the actor's affiliation to the room
         */
        public String getAffiliation() {
            return affiliation;
        }

        /**
         * Returns the <room@service/nick> by which an occupant is identified within the context 
         * of a room. If the room is non-anonymous, the JID will be included in the item. 
         * 
         * @return the room JID by which an occupant is identified within the room.
         */
        public String getJid() {
            return jid;
        }

        /**
         * Returns the new nickname of an occupant that is changing his/her nickname. The new 
         * nickname is sent as part of the unavailable presence.  
         * 
         * @return the new nickname of an occupant that is changing his/her nickname.
         */
        public String getNick() {
            return nick;
        }

        /**
         * Returns the temporary position or privilege level of an occupant within a room. The 
         * possible roles are "moderator", "participant", and "visitor" (it is also possible to 
         * have no defined role). A role lasts only for the duration of an occupant's visit to 
         * a room. 
         * 
         * @return the privilege level of an occupant within a room.
         */
        public String getRole() {
            return role;
        }

        /**
         * Sets the actor (JID of an occupant in the room) that was kicked or banned.
         * 
         * @param actor the actor (JID of an occupant in the room) that was kicked or banned.
         */
        public void setActor(String actor) {
            this.actor = actor;
        }

        /**
         * Sets the reason for the item child. The reason is optional and could be used to
         * explain the reason why a user (occupant) was kicked or banned.
         * 
         * @param reason the reason why a user (occupant) was kicked or banned.
         */
        public void setReason(String reason) {
            this.reason = reason;
        }

        /**
         * Sets the <room@service/nick> by which an occupant is identified within the context 
         * of a room. If the room is non-anonymous, the JID will be included in the item.
         *  
         * @param jid the JID by which an occupant is identified within a room.
         */
        public void setJid(String jid) {
            this.jid = jid;
        }

        /**
         * Sets the new nickname of an occupant that is changing his/her nickname. The new 
         * nickname is sent as part of the unavailable presence.
         *   
         * @param nick the new nickname of an occupant that is changing his/her nickname.
         */
        public void setNick(String nick) {
            this.nick = nick;
        }

        public String toXML() {
            StringBuffer buf = new StringBuffer();
            buf.append("<item");
            if (getAffiliation() != null) {
                buf.append(" affiliation=\"").append(getAffiliation()).append("\"");
            }
            if (getJid() != null) {
                buf.append(" jid=\"").append(getJid()).append("\"");
            }
            if (getNick() != null) {
                buf.append(" nick=\"").append(getNick()).append("\"");
            }
            if (getRole() != null) {
                buf.append(" role=\"").append(getRole()).append("\"");
            }
            if (getReason() == null && getActor() == null) {
                buf.append("/>");
            }
            else {
                buf.append(">");
                if (getReason() != null) {
                    buf.append("<reason>").append(getReason()).append("</reason>");
                }
                if (getActor() != null) {
                    buf.append("<actor jid=\"").append(getActor()).append("\"/>");
                }
                buf.append("</item>");
            }
            return buf.toString();
        }
    };
}
