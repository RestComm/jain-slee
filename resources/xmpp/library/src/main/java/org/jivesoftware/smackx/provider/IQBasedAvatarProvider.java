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



package org.jivesoftware.smackx.provider;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smack.util.PacketParserUtils;
import org.jivesoftware.smackx.packet.IQBasedAvatar;
import org.xmlpull.v1.XmlPullParser;

/**
* The DiscoverInfoProvider parses Service Discovery information packets.
*
* @author Gaston Dombiak
*/

public class IQBasedAvatarProvider implements IQProvider {

    public IQ parseIQ(XmlPullParser parser) throws Exception {
        IQBasedAvatar iQBasedAvatar = new IQBasedAvatar();
        boolean done = false;
        IQBasedAvatar.Data data = null;
        String dataText = "";
        String dataMimeType = "";       
        while (!done) {
            int eventType = parser.next();
            if (eventType == XmlPullParser.START_TAG) {
                if (parser.getName().equals("data")) {                   
                    dataMimeType = parser.getAttributeValue("", "mimetype");
                    dataText = parser.nextText();                    
                }
                // Otherwise, it must be a packet extension.
                else {
                    iQBasedAvatar.addExtension(PacketParserUtils.parsePacketExtension(parser
                            .getName(), parser.getNamespace(), parser));
                }
            } else if (eventType == XmlPullParser.END_TAG) {
                if (parser.getName().equals("data")) {
                   
                    data = new IQBasedAvatar.Data(dataText, dataMimeType);
                    iQBasedAvatar.setData(data);
                }               
                if (parser.getName().equals("query")) {
                    done = true;
                }
            }
        }

        return iQBasedAvatar;
    }
}