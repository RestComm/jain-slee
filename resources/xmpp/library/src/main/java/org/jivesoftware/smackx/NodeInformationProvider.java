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

import java.util.Iterator;


/**
 * The NodeInformationProvider is responsible for providing information (i.e. DiscoverItems.Item)
 * about a given node. This information will be requested each time this XMPPP client receives a
 * disco items requests on the given node.
 * 
 * @author Gaston Dombiak
 */
public interface NodeInformationProvider {
    
    /**
     * Returns an Iterator on the Items {@link org.jivesoftware.smackx.packet.DiscoverItems.Item} 
     * defined in the node. For example, the MUC protocol specifies that an XMPP client should 
     * answer an Item for each joined room when asked for the rooms where the use has joined.
     *  
     * @return an Iterator on the Items defined in the node.
     */
    public abstract Iterator getNodeItems();

}
