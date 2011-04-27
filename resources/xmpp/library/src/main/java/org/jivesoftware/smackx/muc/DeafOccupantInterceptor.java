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

import org.jivesoftware.smack.PacketInterceptor;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.packet.Presence;

/**
 * Packet interceptor that will intercept presence packets sent to the MUC service to indicate
 * that the user wants to be a deaf occupant. A user can only indicate that he wants to be a
 * deaf occupant while joining the room. It is not possible to become deaf or stop being deaf
 * after the user joined the room.<p>
 *
 * Deaf occupants will not get messages broadcasted to all room occupants. However, they will
 * be able to get private messages, presences, IQ packets or room history. To use this
 * functionality you will need to send the message
 * {@link MultiUserChat#addPresenceInterceptor(org.jivesoftware.smack.PacketInterceptor)} and
 * pass this interceptor as the parameter.<p>
 *
 * Note that this is a custom extension to the MUC service so it may not work with other servers
 * than Wildfire.
 *
 * @author Gaston Dombiak
 */
public class DeafOccupantInterceptor implements PacketInterceptor {

    public void interceptPacket(Packet packet) {
        Presence presence = (Presence) packet;
        // Check if user is joining a room
        if (Presence.Type.AVAILABLE == presence.getType() &&
                presence.getExtension("x", "http://jabber.org/protocol/muc") != null) {
            // Add extension that indicates that user wants to be a deaf occupant
            packet.addExtension(new DeafExtension());
        }
    }

    private static class DeafExtension implements PacketExtension {

        public String getElementName() {
            return "x";
        }

        public String getNamespace() {
            return "http://jivesoftware.org/protocol/muc";
        }

        public String toXML() {
            StringBuffer buf = new StringBuffer();
            buf.append("<").append(getElementName()).append(" xmlns=\"").append(getNamespace())
                    .append("\">");
            buf.append("<deaf-occupant/>");
            buf.append("</").append(getElementName()).append(">");
            return buf.toString();
        }
    }
}
