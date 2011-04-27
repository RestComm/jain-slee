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
 * Implements the logical AND operation over two or more packet filters.
 * In other words, packets pass this filter if they pass <b>all</b> of the filters.
 *
 * @author Matt Tucker
 */
public class AndFilter implements PacketFilter {

    /**
     * The current number of elements in the filter.
     */
    private int size;

    /**
     * The list of filters.
     */
    private PacketFilter [] filters;

    /**
     * Creates an empty AND filter. Filters should be added using the
     * {@link #addFilter(PacketFilter)} method.
     */
    public AndFilter() {
        size = 0;
        filters = new PacketFilter[3];
    }

    /**
     * Creates an AND filter using the two specified filters.
     *
     * @param filter1 the first packet filter.
     * @param filter2 the second packet filter.
     */
    public AndFilter(PacketFilter filter1, PacketFilter filter2) {
        if (filter1 == null || filter2 == null) {
            throw new IllegalArgumentException("Parameters cannot be null.");
        }
        size = 2;
        filters = new PacketFilter[2];
        filters[0] = filter1;
        filters[1] = filter2;
    }

    /**
     * Adds a filter to the filter list for the AND operation. A packet
     * will pass the filter if all of the filters in the list accept it.
     *
     * @param filter a filter to add to the filter list.
     */
    public void addFilter(PacketFilter filter) {
        if (filter == null) {
            throw new IllegalArgumentException("Parameter cannot be null.");
        }
        // If there is no more room left in the filters array, expand it.
        if (size == filters.length) {
            PacketFilter [] newFilters = new PacketFilter[filters.length+2];
            for (int i=0; i<filters.length; i++) {
                newFilters[i] = filters[i];
            }
            filters = newFilters;
        }
        // Add the new filter to the array.
        filters[size] = filter;
        size++;
    }

    public boolean accept(Packet packet) {
        for (int i=0; i<size; i++) {
            if (!filters[i].accept(packet)) {
                return false;
            }
        }
        return true;
    }

    public String toString() {
        return filters.toString();
    }
}
