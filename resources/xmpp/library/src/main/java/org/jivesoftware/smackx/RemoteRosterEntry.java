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

/**
 * Represents a roster item, which consists of a JID and , their name and
 * the groups the roster item belongs to. This roster item does not belong
 * to the local roster. Therefore, it does not persist in the server.<p> 
 * 
 * The idea of a RemoteRosterEntry is to be used as part of a roster exchange.
 *
 * @author Gaston Dombiak
 */
public class RemoteRosterEntry {

    private String user;
    private String name;
    private List groupNames = new ArrayList();

    /**
     * Creates a new remote roster entry.
     *
     * @param user the user.
     * @param name the user's name.
     * @param groups the list of group names the entry will belong to, or <tt>null</tt> if the
     *      the roster entry won't belong to a group.
     */
    public RemoteRosterEntry(String user, String name, String [] groups) {
        this.user = user;
        this.name = name;
		if (groups != null) {
			groupNames = new ArrayList(Arrays.asList(groups));
		}
    }

    /**
     * Returns the user.
     *
     * @return the user.
     */
    public String getUser() {
        return user;
    }

    /**
     * Returns the user's name.
     *
     * @return the user's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns an Iterator for the group names (as Strings) that the roster entry
     * belongs to.
     *
     * @return an Iterator for the group names.
     */
    public Iterator getGroupNames() {
        synchronized (groupNames) {
            return Collections.unmodifiableList(groupNames).iterator();
        }
    }

    /**
     * Returns a String array for the group names that the roster entry
     * belongs to.
     *
     * @return a String[] for the group names.
     */
    public String[] getGroupArrayNames() {
        synchronized (groupNames) {
            return (String[])
                (Collections
                    .unmodifiableList(groupNames)
                    .toArray(new String[groupNames.size()]));
        }
    }

    public String toXML() {
        StringBuffer buf = new StringBuffer();
        buf.append("<item jid=\"").append(user).append("\"");
        if (name != null) {
            buf.append(" name=\"").append(name).append("\"");
        }
        buf.append(">");
        synchronized (groupNames) {
            for (int i = 0; i < groupNames.size(); i++) {
                String groupName = (String) groupNames.get(i);
                buf.append("<group>").append(groupName).append("</group>");
            }
        }
        buf.append("</item>");
        return buf.toString();
    }

}
