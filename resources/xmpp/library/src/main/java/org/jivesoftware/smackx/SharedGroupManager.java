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

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smackx.packet.SharedGroupsInfo;

import java.util.List;

/**
 * A SharedGroupManager provides services for discovering the shared groups where a user belongs.<p>
 *
 * Important note: This functionality is not part of the XMPP spec and it will only work
 * with Wildfire.
 *
 * @author Gaston Dombiak
 */
public class SharedGroupManager {

    /**
     * Returns the collection that will contain the name of the shared groups where the user
     * logged in with the specified session belongs.
     *
     * @param connection connection to use to get the user's shared groups.
     * @return collection with the shared groups' name of the logged user.
     */
    public static List getSharedGroups(XMPPConnection connection) throws XMPPException {
        // Discover the shared groups of the logged user
        SharedGroupsInfo info = new SharedGroupsInfo();
        info.setType(IQ.Type.GET);

        // Create a packet collector to listen for a response.
        PacketCollector collector =
            connection.createPacketCollector(new PacketIDFilter(info.getPacketID()));

        connection.sendPacket(info);

        // Wait up to 5 seconds for a result.
        IQ result = (IQ) collector.nextResult(SmackConfiguration.getPacketReplyTimeout());
        // Stop queuing results
        collector.cancel();
        if (result == null) {
            throw new XMPPException("No response from the server.");
        }
        if (result.getType() == IQ.Type.ERROR) {
            throw new XMPPException(result.getError());
        }
        return ((SharedGroupsInfo) result).getGroups();
    }
}
