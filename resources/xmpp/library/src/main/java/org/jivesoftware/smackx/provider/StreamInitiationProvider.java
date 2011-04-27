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
import org.jivesoftware.smackx.packet.DataForm;
import org.jivesoftware.smackx.packet.DelayInformation;
import org.jivesoftware.smackx.packet.StreamInitiation;
import org.jivesoftware.smackx.packet.StreamInitiation.File;
import org.xmlpull.v1.XmlPullParser;

/**
 * The StreamInitiationProvider parses StreamInitiation packets.
 * 
 * @author Alexander Wenckus
 * 
 */
public class StreamInitiationProvider implements IQProvider {

	public IQ parseIQ(final XmlPullParser parser) throws Exception {
		boolean done = false;

		// si
		String id = parser.getAttributeValue("", "id");
		String mimeType = parser.getAttributeValue("", "mime-type");

		StreamInitiation initiation = new StreamInitiation();

		// file
		String name = null;
		String size = null;
		String hash = null;
		String date = null;
		String desc = null;
		boolean isRanged = false;

		// feature
		DataForm form = null;
		DataFormProvider dataFormProvider = new DataFormProvider();

		int eventType;
		String elementName;
		String namespace;
		while (!done) {
			eventType = parser.next();
			elementName = parser.getName();
			namespace = parser.getNamespace();
			if (eventType == XmlPullParser.START_TAG) {
				if (elementName.equals("file")) {
					name = parser.getAttributeValue("", "name");
					size = parser.getAttributeValue("", "size");
					hash = parser.getAttributeValue("", "hash");
					date = parser.getAttributeValue("", "date");
				} else if (elementName.equals("desc")) {
					desc = parser.nextText();
				} else if (elementName.equals("range")) {
					isRanged = true;
				} else if (elementName.equals("x")
						&& namespace.equals("jabber:x:data")) {
					form = (DataForm) dataFormProvider.parseExtension(parser);
				}
			} else if (eventType == XmlPullParser.END_TAG) {
				if (elementName.equals("si")) {
					done = true;
				} else if (elementName.equals("file")) {
                    long fileSize = 0;
                    if(size != null && size.trim().length() !=0){
                        try {
                            fileSize = Long.parseLong(size);
                        }
                        catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    File file = new File(name, fileSize);
					file.setHash(hash);
					if (date != null)
						file.setDate(DelayInformation.UTC_FORMAT.parse(date));
					file.setDesc(desc);
					file.setRanged(isRanged);
					initiation.setFile(file);
				}
			}
		}

		initiation.setSesssionID(id);
		initiation.setMimeType(mimeType);

		initiation.setFeatureNegotiationForm(form);

		return initiation;
	}

}
