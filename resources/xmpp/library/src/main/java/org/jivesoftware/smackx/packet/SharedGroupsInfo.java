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

package org.jivesoftware.smackx.packet;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * IQ packet used for discovering the user's shared groups and for getting the answer back
 * from the server.<p>
 *
 * Important note: This functionality is not part of the XMPP spec and it will only work
 * with Wildfire.
 *
 * @author Gaston Dombiak
 */
public class SharedGroupsInfo extends IQ {

    private List groups = new ArrayList();

    /**
     * Returns a collection with the shared group names returned from the server.
     *
     * @return collection with the shared group names returned from the server.
     */
    public List getGroups() {
        return groups;
    }

    public String getChildElementXML() {
        StringBuffer buf = new StringBuffer();
        buf.append("<sharedgroup xmlns=\"http://www.jivesoftware.org/protocol/sharedgroup\">");
        for (Iterator it=groups.iterator(); it.hasNext();) {
            buf.append("<group>").append(it.next()).append("</group>");
        }
        buf.append("</sharedgroup>");
        return buf.toString();
    }

    /**
     * Internal Search service Provider.
     */
    public static class Provider implements IQProvider {

        /**
         * Provider Constructor.
         */
        public Provider() {
            super();
        }

        public IQ parseIQ(XmlPullParser parser) throws Exception {
            SharedGroupsInfo groupsInfo = new SharedGroupsInfo();

            boolean done = false;
            while (!done) {
                int eventType = parser.next();
                if (eventType == XmlPullParser.START_TAG && parser.getName().equals("group")) {
                    groupsInfo.getGroups().add(parser.nextText());
                }
                else if (eventType == XmlPullParser.END_TAG) {
                    if (parser.getName().equals("sharedgroup")) {
                        done = true;
                    }
                }
            }
            return groupsInfo;
        }
    }
}
