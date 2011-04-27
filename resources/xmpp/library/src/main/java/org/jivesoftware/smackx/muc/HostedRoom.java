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

import org.jivesoftware.smackx.packet.DiscoverItems;

/**
 * Hosted rooms by a chat service may be discovered if they are configured to appear in the room
 * directory . The information that may be discovered is the XMPP address of the room and the room
 * name. The address of the room may be used for obtaining more detailed information
 * {@link org.jivesoftware.smackx.muc.MultiUserChat#getRoomInfo(org.jivesoftware.smack.XMPPConnection, String)}
 * or could be used for joining the room
 * {@link org.jivesoftware.smackx.muc.MultiUserChat#MultiUserChat(org.jivesoftware.smack.XMPPConnection, String)}
 * and {@link org.jivesoftware.smackx.muc.MultiUserChat#join(String)}.
 *
 * @author Gaston Dombiak
 */
public class HostedRoom {

    private String jid;

    private String name;

    public HostedRoom(DiscoverItems.Item item) {
        super();
        jid = item.getEntityID();
        name = item.getName();
    }

    /**
     * Returns the XMPP address of the hosted room by the chat service. This address may be used
     * when creating a <code>MultiUserChat</code> when joining a room.
     *
     * @return the XMPP address of the hosted room by the chat service.
     */
    public String getJid() {
        return jid;
    }

    /**
     * Returns the name of the room.
     *
     * @return the name of the room.
     */
    public String getName() {
        return name;
    }
}
