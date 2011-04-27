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

import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.util.StringUtils;

/**
 * Filter for packets where the "from" field exactly matches a specified JID. If the specified
 * address is a bare JID then the filter will match any address whose bare JID matches the
 * specified JID. But if the specified address is a full JID then the filter will only match
 * if the sender of the packet matches the specified resource.
 *
 * @author Gaston Dombiak
 */
public class FromMatchesFilter implements PacketFilter {

    private String address;
    /**
     * Flag that indicates if the checking will be done against bare JID addresses or full JIDs.
     */
    private boolean matchBareJID = false;

    /**
     * Creates a "from" filter using the "from" field part. If the specified address is a bare JID
     * then the filter will match any address whose bare JID matches the specified JID. But if the
     * specified address is a full JID then the filter will only match if the sender of the packet
     * matches the specified resource.
     *
     * @param address the from field value the packet must match. Could be a full or bare JID.
     */
    public FromMatchesFilter(String address) {
        if (address == null) {
            throw new IllegalArgumentException("Parameter cannot be null.");
        }
        this.address = address.toLowerCase();
        matchBareJID = "".equals(StringUtils.parseResource(address));
    }

    public boolean accept(Packet packet) {
        if (packet.getFrom() == null) {
            return false;
        }
        else if (matchBareJID) {
            // Check if the bare JID of the sender of the packet matches the specified JID
            return packet.getFrom().toLowerCase().startsWith(address);
        }
        else {
            // Check if the full JID of the sender of the packet matches the specified JID
            return address.equals(packet.getFrom().toLowerCase());
        }
    }
}
