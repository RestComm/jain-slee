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

package org.jivesoftware.smackx.muc;

import org.jivesoftware.smackx.packet.MUCAdmin;
import org.jivesoftware.smackx.packet.MUCUser;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.util.StringUtils;

/**
 * Represents the information about an occupant in a given room. The information will always have
 * the affiliation and role of the occupant in the room. The full JID and nickname are optional.
 *
 * @author Gaston Dombiak
 */
public class Occupant {
    // Fields that must have a value
    private String affiliation;
    private String role;
    // Fields that may have a value
    private String jid;
    private String nick;

    Occupant(MUCAdmin.Item item) {
        super();
        this.jid = item.getJid();
        this.affiliation = item.getAffiliation();
        this.role = item.getRole();
        this.nick = item.getNick();
    }

    Occupant(Presence presence) {
        super();
        MUCUser mucUser = (MUCUser) presence.getExtension("x",
                "http://jabber.org/protocol/muc#user");
        MUCUser.Item item = mucUser.getItem();
        this.jid = item.getJid();
        this.affiliation = item.getAffiliation();
        this.role = item.getRole();
        // Get the nickname from the FROM attribute of the presence
        this.nick = StringUtils.parseResource(presence.getFrom());
    }

    /**
     * Returns the full JID of the occupant. If this information was extracted from a presence and
     * the room is semi or full-anonymous then the answer will be null. On the other hand, if this
     * information was obtained while maintaining the voice list or the moderator list then we will
     * always have a full JID.
     *
     * @return the full JID of the occupant.
     */
    public String getJid() {
        return jid;
    }

    /**
     * Returns the affiliation of the occupant. Possible affiliations are: "owner", "admin",
     * "member", "outcast". This information will always be available.
     *
     * @return the affiliation of the occupant.
     */
    public String getAffiliation() {
        return affiliation;
    }

    /**
     * Returns the current role of the occupant in the room. This information will always be
     * available.
     *
     * @return the current role of the occupant in the room.
     */
    public String getRole() {
        return role;
    }

    /**
     * Returns the current nickname of the occupant in the room. If this information was extracted
     * from a presence then the answer will be null.
     *
     * @return the current nickname of the occupant in the room or null if this information was
     *         obtained from a presence.
     */
    public String getNick() {
        return nick;
    }
}
