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
import org.jivesoftware.smack.filter.PacketFilter;

import java.util.LinkedList;

/**
 * Provides a mechanism to collect packets into a result queue that pass a
 * specified filter. The collector lets you perform blocking and polling
 * operations on the result queue. So, a PacketCollector is more suitable to
 * use than a {@link PacketListener} when you need to wait for a specific
 * result.<p>
 *
 * Each packet collector will queue up to 2^16 packets for processing before
 * older packets are automatically dropped.
 *
 * @see XMPPConnection#createPacketCollector(PacketFilter)
 * @author Matt Tucker
 */
public class PacketCollector {

    /**
     * Max number of packets that any one collector can hold. After the max is
     * reached, older packets will be automatically dropped from the queue as
     * new packets are added.
     */
    private static final int MAX_PACKETS = 65536;

    private PacketFilter packetFilter;
    private LinkedList resultQueue;
    private PacketReader packetReader;
    private boolean cancelled = false;

    /**
     * Creates a new packet collector. If the packet filter is <tt>null</tt>, then
     * all packets will match this collector.
     *
     * @param packetReader the packetReader the collector is tied to.
     * @param packetFilter determines which packets will be returned by this collector.
     */
    protected PacketCollector(PacketReader packetReader, PacketFilter packetFilter) {
        this.packetReader = packetReader;
        this.packetFilter = packetFilter;
        this.resultQueue = new LinkedList();
         // Add the collector to the packet reader's list of active collector.
        synchronized (packetReader.collectors) {
            packetReader.collectors.add(this);
        }
    }

    /**
     * Explicitly cancels the packet collector so that no more results are
     * queued up. Once a packet collector has been cancelled, it cannot be
     * re-enabled. Instead, a new packet collector must be created.
     */
    public void cancel() {
        // If the packet collector has already been cancelled, do nothing.
        if (!cancelled) {
            cancelled = true;
            // Remove object from collectors list by setting the value in the
            // list at the correct index to null. The collector thread will
            // automatically remove the actual list entry when it can.
            synchronized (packetReader.collectors) {
                int index = packetReader.collectors.indexOf(this);
                packetReader.collectors.set(index, null);
            }
        }
    }

    /**
     * Returns the packet filter associated with this packet collector. The packet
     * filter is used to determine what packets are queued as results.
     *
     * @return the packet filter.
     */
    public PacketFilter getPacketFilter() {
        return packetFilter;
    }

    /**
     * Polls to see if a packet is currently available and returns it, or
     * immediately returns <tt>null</tt> if no packets are currently in the
     * result queue.
     *
     * @return the next packet result, or <tt>null</tt> if there are no more
     *      results.
     */
    public synchronized Packet pollResult() {
        if (resultQueue.isEmpty()) {
            return null;
        }
        else {
            return (Packet)resultQueue.removeLast();
        }
    }

    /**
     * Returns the next available packet. The method call will block (not return)
     * until a packet is available.
     *
     * @return the next available packet.
     */
    public synchronized Packet nextResult() {
        // Wait indefinitely until there is a result to return.
        while (resultQueue.isEmpty()) {
            try {
                wait();
            }
            catch (InterruptedException ie) {
                // Ignore.
            }
        }
        return (Packet)resultQueue.removeLast();
    }

    /**
     * Returns the next available packet. The method call will block (not return)
     * until a packet is available or the <tt>timeout</tt> has elapased. If the
     * timeout elapses without a result, <tt>null</tt> will be returned.
     *
     * @param timeout the amount of time to wait for the next packet (in milleseconds).
     * @return the next available packet.
     */
    public synchronized Packet nextResult(long timeout) {
        // Wait up to the specified amount of time for a result.
        if (resultQueue.isEmpty()) {
            try {
                wait(timeout);
            }
            catch (InterruptedException ie) {
                // Ignore.
            }
        }
        // If still no result, return null.
        if (resultQueue.isEmpty()) {
            return null;
        }
        else {
            return (Packet)resultQueue.removeLast();
        }
    }

    /**
     * Processes a packet to see if it meets the criteria for this packet collector.
     * If so, the packet is added to the result queue.
     *
     * @param packet the packet to process.
     */
    protected synchronized void processPacket(Packet packet) {
        if (packet == null) {
            return;
        }
        if (packetFilter == null || packetFilter.accept(packet)) {
            // If the max number of packets has been reached, remove the oldest one.
            if (resultQueue.size() == MAX_PACKETS) {
                resultQueue.removeLast();
            }
            // Add the new packet.
            resultQueue.addFirst(packet);
            // Notify waiting threads a result is available.
            notifyAll();
        }
    }
}
