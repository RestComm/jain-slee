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

package org.jivesoftware.smack.filter;

import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

/**
 * Filters for packets of a specific type of Message (e.g. CHAT).
 * 
 * @see org.jivesoftware.smack.packet.Message.Type
 * @author Ward Harold
 */
public class MessageTypeFilter implements PacketFilter {

    private final Message.Type type;

    /**
     * Creates a new message type filter using the specified message type.
     * 
     * @param type the message type.
     */
    public MessageTypeFilter(Message.Type type) {
        this.type = type;
    }

    public boolean accept(Packet packet) {
        if (!(packet instanceof Message)) {
            return false;
        }
        else {
            return ((Message) packet).getType().equals(this.type);
        }
    }

}
