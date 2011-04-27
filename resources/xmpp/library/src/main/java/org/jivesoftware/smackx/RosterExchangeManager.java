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

import java.util.*;

import org.jivesoftware.smack.*;
import org.jivesoftware.smack.filter.*;
import org.jivesoftware.smack.packet.*;
import org.jivesoftware.smackx.packet.RosterExchange;

/**
 *
 * Manages Roster exchanges. A RosterExchangeManager provides a high level access to send 
 * rosters, roster groups and roster entries to XMPP clients. It also provides an easy way
 * to hook up custom logic when entries are received from another XMPP client through 
 * RosterExchangeListeners.
 *
 * @author Gaston Dombiak
 */
public class RosterExchangeManager {

    private List rosterExchangeListeners = new ArrayList();

    private XMPPConnection con;

    private PacketFilter packetFilter = new PacketExtensionFilter("x", "jabber:x:roster");
    private PacketListener packetListener;

    /**
     * Creates a new roster exchange manager.
     *
     * @param con an XMPPConnection.
     */
    public RosterExchangeManager(XMPPConnection con) {
        this.con = con;
        init();
    }

    /**
     * Adds a listener to roster exchanges. The listener will be fired anytime roster entries
     * are received from remote XMPP clients.
     *
     * @param rosterExchangeListener a roster exchange listener.
     */
    public void addRosterListener(RosterExchangeListener rosterExchangeListener) {
        synchronized (rosterExchangeListeners) {
            if (!rosterExchangeListeners.contains(rosterExchangeListener)) {
                rosterExchangeListeners.add(rosterExchangeListener);
            }
        }
    }

    /**
     * Removes a listener from roster exchanges. The listener will be fired anytime roster 
     * entries are received from remote XMPP clients.
     *
     * @param rosterExchangeListener a roster exchange listener..
     */
    public void removeRosterListener(RosterExchangeListener rosterExchangeListener) {
        synchronized (rosterExchangeListeners) {
            rosterExchangeListeners.remove(rosterExchangeListener);
        }
    }

    /**
     * Sends a roster to userID. All the entries of the roster will be sent to the
     * target user.
     * 
     * @param roster the roster to send
     * @param targetUserID the user that will receive the roster entries
     */
    public void send(Roster roster, String targetUserID) {
        // Create a new message to send the roster
        Message msg = new Message(targetUserID);
        // Create a RosterExchange Package and add it to the message
        RosterExchange rosterExchange = new RosterExchange(roster);
        msg.addExtension(rosterExchange);

        // Send the message that contains the roster
        con.sendPacket(msg);
    }

    /**
     * Sends a roster entry to userID.
     * 
     * @param rosterEntry the roster entry to send
     * @param targetUserID the user that will receive the roster entries
     */
    public void send(RosterEntry rosterEntry, String targetUserID) {
        // Create a new message to send the roster
        Message msg = new Message(targetUserID);
        // Create a RosterExchange Package and add it to the message
        RosterExchange rosterExchange = new RosterExchange();
        rosterExchange.addRosterEntry(rosterEntry);
        msg.addExtension(rosterExchange);

        // Send the message that contains the roster
        con.sendPacket(msg);
    }

    /**
     * Sends a roster group to userID. All the entries of the group will be sent to the 
     * target user.
     * 
     * @param rosterGroup the roster group to send
     * @param targetUserID the user that will receive the roster entries
     */
    public void send(RosterGroup rosterGroup, String targetUserID) {
        // Create a new message to send the roster
        Message msg = new Message(targetUserID);
        // Create a RosterExchange Package and add it to the message
        RosterExchange rosterExchange = new RosterExchange();
        for (Iterator it = rosterGroup.getEntries(); it.hasNext();)
            rosterExchange.addRosterEntry((RosterEntry) it.next());
        msg.addExtension(rosterExchange);

        // Send the message that contains the roster
        con.sendPacket(msg);
    }

    /**
     * Fires roster exchange listeners.
     */
    private void fireRosterExchangeListeners(String from, Iterator remoteRosterEntries) {
        RosterExchangeListener[] listeners = null;
        synchronized (rosterExchangeListeners) {
            listeners = new RosterExchangeListener[rosterExchangeListeners.size()];
            rosterExchangeListeners.toArray(listeners);
        }
        for (int i = 0; i < listeners.length; i++) {
            listeners[i].entriesReceived(from, remoteRosterEntries);
        }
    }

    private void init() {
        // Listens for all roster exchange packets and fire the roster exchange listeners.
        packetListener = new PacketListener() {
            public void processPacket(Packet packet) {
                Message message = (Message) packet;
                RosterExchange rosterExchange =
                    (RosterExchange) message.getExtension("x", "jabber:x:roster");
                // Fire event for roster exchange listeners
                fireRosterExchangeListeners(message.getFrom(), rosterExchange.getRosterEntries());
            };

        };
        con.addPacketListener(packetListener, packetFilter);
    }

    public void destroy() {
        if (con != null)
            con.removePacketListener(packetListener);

    }
    public void finalize() {
        destroy();
    }
}