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
import org.jivesoftware.smackx.packet.MUCOwner;

/**
 * Represents an affiliation of a user to a given room. The affiliate's information will always have
 * the bare jid of the real user and its affiliation. If the affiliate is an occupant of the room
 * then we will also have information about the role and nickname of the user in the room.
 *
 * @author Gaston Dombiak
 */
public class Affiliate {
    // Fields that must have a value
    private String jid;
    private String affiliation;

    // Fields that may have a value
    private String role;
    private String nick;

    Affiliate(MUCOwner.Item item) {
        super();
        this.jid = item.getJid();
        this.affiliation = item.getAffiliation();
        this.role = item.getRole();
        this.nick = item.getNick();
    }

    Affiliate(MUCAdmin.Item item) {
        super();
        this.jid = item.getJid();
        this.affiliation = item.getAffiliation();
        this.role = item.getRole();
        this.nick = item.getNick();
    }

    /**
     * Returns the bare JID of the affiliated user. This information will always be available.
     *
     * @return the bare JID of the affiliated user.
     */
    public String getJid() {
        return jid;
    }

    /**
     * Returns the affiliation of the afffiliated user. Possible affiliations are: "owner", "admin",
     * "member", "outcast". This information will always be available.
     *
     * @return the affiliation of the afffiliated user.
     */
    public String getAffiliation() {
        return affiliation;
    }

    /**
     * Returns the current role of the affiliated user if the user is currently in the room.
     * If the user is not present in the room then the answer will be null.
     *
     * @return the current role of the affiliated user in the room or null if the user is not in
     *         the room.
     */
    public String getRole() {
        return role;
    }

    /**
     * Returns the current nickname of the affiliated user if the user is currently in the room.
     * If the user is not present in the room then the answer will be null.
     *
     * @return the current nickname of the affiliated user in the room or null if the user is not in
     *         the room.
     */
    public String getNick() {
        return nick;
    }
}
