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

package org.jivesoftware.smack;

import org.jivesoftware.smack.packet.Packet;

/**
 * Provides a mechanism to listen for packets that pass a specified filter.
 * This allows event-style programming -- every time a new packet is found,
 * the {@link #processPacket(Packet)} method will be called. This is the
 * opposite approach to the functionality provided by a {@link PacketCollector}
 * which lets you block while waiting for results.
 *
 * @see XMPPConnection#addPacketListener(PacketListener, org.jivesoftware.smack.filter.PacketFilter)
 * @author Matt Tucker
 */
public interface PacketListener {

    /**
     * Process the next packet sent to this packet listener.<p>
     *
     * A single thread is responsible for invoking all listeners, so
     * it's very important that implementations of this method not block
     * for any extended period of time.
     *
     * @param packet the packet to process.
     */
    public void processPacket(Packet packet);

}
