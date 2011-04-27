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

import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.jivesoftware.smackx.packet.DelayInformation;
import org.xmlpull.v1.XmlPullParser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * The DelayInformationProvider parses DelayInformation packets.
 *
 * @author Gaston Dombiak
 */
public class DelayInformationProvider implements PacketExtensionProvider {

    /**
     * Creates a new DeliveryInformationProvider.
     * ProviderManager requires that every PacketExtensionProvider has a public, no-argument
     * constructor
     */
    public DelayInformationProvider() {
    }

    public PacketExtension parseExtension(XmlPullParser parser) throws Exception {
        Date stamp = null;
        try {
            synchronized (DelayInformation.UTC_FORMAT) {
                stamp = DelayInformation.UTC_FORMAT.parse(parser.getAttributeValue("", "stamp"));
            }
        } catch (ParseException e) {
            // Try again but assuming that the date follows JEP-82 format
            // (Jabber Date and Time Profiles) 
            try {
                synchronized (DelayInformation.NEW_UTC_FORMAT) {
                    stamp = DelayInformation.NEW_UTC_FORMAT
                            .parse(parser.getAttributeValue("", "stamp"));
                }
            } catch (ParseException e1) {
                // Last attempt. Try parsing the date assuming that it does not include milliseconds
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
                stamp = formatter.parse(parser.getAttributeValue("", "stamp"));
            }
        }
        DelayInformation delayInformation = new DelayInformation(stamp);
        delayInformation.setFrom(parser.getAttributeValue("", "from"));
        delayInformation.setReason(parser.nextText());
        return delayInformation;
    }

}
