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

package org.jivesoftware.smackx;

import org.jivesoftware.smackx.packet.DiscoverItems;

/**
 * The OfflineMessageHeader holds header information of an offline message. The header
 * information was retrieved using the {@link OfflineMessageManager} class.<p>
 *
 * Each offline message is identified by the target user of the offline message and a unique stamp.
 * Use {@link OfflineMessageManager#getMessages(java.util.List)} to retrieve the whole message.
 *
 * @author Gaston Dombiak
 */
public class OfflineMessageHeader {
    /**
     * Bare JID of the user that was offline when the message was sent.
     */
    private String user;
    /**
     * Full JID of the user that sent the message.
     */
    private String jid;
    /**
     * Stamp that uniquely identifies the offline message. This stamp will be used for
     * getting the specific message or delete it. The stamp may be of the form UTC timestamps
     * but it is not required to have that format.
     */
    private String stamp;

    public OfflineMessageHeader(DiscoverItems.Item item) {
        super();
        user = item.getEntityID();
        jid = item.getName();
        stamp = item.getNode();
    }

    /**
     * Returns the bare JID of the user that was offline when the message was sent.
     *
     * @return the bare JID of the user that was offline when the message was sent.
     */
    public String getUser() {
        return user;
    }

    /**
     * Returns the full JID of the user that sent the message.
     *
     * @return the full JID of the user that sent the message.
     */
    public String getJid() {
        return jid;
    }

    /**
     * Returns the stamp that uniquely identifies the offline message. This stamp will
     * be used for getting the specific message or delete it. The stamp may be of the
     * form UTC timestamps but it is not required to have that format.
     *
     * @return the stamp that uniquely identifies the offline message.
     */
    public String getStamp() {
        return stamp;
    }
}
