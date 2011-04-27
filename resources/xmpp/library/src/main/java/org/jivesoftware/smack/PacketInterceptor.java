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
 * Provides a mechanism to intercept and modify packets that are going to be
 * sent to the server. PacketInterceptors are added to the {@link XMPPConnection}
 * together with a {@link org.jivesoftware.smack.filter.PacketFilter} so that only
 * certain packets are intercepted and processed by the interceptor.<p>
 *
 * This allows event-style programming -- every time a new packet is found,
 * the {@link #interceptPacket(Packet)} method will be called.
 *
 * @see XMPPConnection#addPacketWriterInterceptor(PacketInterceptor, org.jivesoftware.smack.filter.PacketFilter)
 * @author Gaston Dombiak
 */
public interface PacketInterceptor {

    /**
     * Process the packet that is about to be sent to the server. The intercepted
     * packet can be modified by the interceptor.<p>
     *
     * Interceptors are invoked using the same thread that requested the packet
     * to be sent, so it's very important that implementations of this method
     * not block for any extended period of time.
     *
     * @param packet the packet to is going to be sent to the server.
     */
    public void interceptPacket(Packet packet);
}
