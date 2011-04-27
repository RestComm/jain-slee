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

/**
 * Defines a way to filter packets for particular attributes. Packet filters are
 * used when constructing packet listeners or collectors -- the filter defines
 * what packets match the criteria of the collector or listener for further
 * packet processing.<p>
 *
 * Several pre-defined filters are defined. These filters can be logically combined
 * for more complex packet filtering by using the
 * {@link org.jivesoftware.smack.filter.AndFilter AndFilter} and
 * {@link org.jivesoftware.smack.filter.OrFilter OrFilter} filters. It's also possible
 * to define your own filters by implementing this interface. The code example below
 * creates a trivial filter for packets with a specific ID.
 *
 * <pre>
 * // Use an anonymous inner class to define a packet filter that returns
 * // all packets that have a packet ID of "RS145".
 * PacketFilter myFilter = new PacketFilter() {
 *     public boolean accept(Packet packet) {
 *         return "RS145".equals(packet.getPacketID());
 *     }
 * };
 * // Create a new packet collector using the filter we created.
 * PacketCollector myCollector = packetReader.createPacketCollector(myFilter);
 * </pre>
 *
 * @see org.jivesoftware.smack.PacketCollector
 * @see org.jivesoftware.smack.PacketListener
 * @author Matt Tucker
 */
public interface PacketFilter {

    /**
     * Tests whether or not the specified packet should pass the filter.
     *
     * @param packet the packet to test.
     * @return true if and only if <tt>packet</tt> passes the filter.
     */
    public boolean accept(Packet packet);
}
