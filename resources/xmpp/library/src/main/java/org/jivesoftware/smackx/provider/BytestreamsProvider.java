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
import org.jivesoftware.smackx.packet.Bytestream;
import org.xmlpull.v1.XmlPullParser;

/**
 * Parses a bytestream packet.
 * 
 * @author Alexander Wenckus
 */
public class BytestreamsProvider implements IQProvider {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jivesoftware.smack.provider.IQProvider#parseIQ(org.xmlpull.v1.XmlPullParser)
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jivesoftware.smack.provider.IQProvider#parseIQ(org.xmlpull.v1.XmlPullParser)
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jivesoftware.smack.provider.IQProvider#parseIQ(org.xmlpull.v1.XmlPullParser)
	 */
	public IQ parseIQ(XmlPullParser parser) throws Exception {
		// StringBuffer buf = new StringBuffer();
		boolean done = false;

		Bytestream toReturn = new Bytestream();

		String id = parser.getAttributeValue("", "sid");
		String mode = parser.getAttributeValue("", "mode");

		// streamhost
		String JID = null;
		String host = null;
		String port = null;

		int eventType;
		String elementName;
		// String namespace;
		while (!done) {
			eventType = parser.next();
			elementName = parser.getName();
			// namespace = parser.getNamespace();
			if (eventType == XmlPullParser.START_TAG) {
				if (elementName.equals(Bytestream.StreamHost.ELEMENTNAME)) {
					JID = parser.getAttributeValue("", "jid");
					host = parser.getAttributeValue("", "host");
					port = parser.getAttributeValue("", "port");
				} else if (elementName
						.equals(Bytestream.StreamHostUsed.ELEMENTNAME)) {
					toReturn.setUsedHost(parser.getAttributeValue("", "jid"));
				} else if (elementName.equals(Bytestream.Activate.ELEMENTNAME)) {
					toReturn.setToActivate(parser.getAttributeValue("", "jid"));
				}
			} else if (eventType == XmlPullParser.END_TAG) {
				if (elementName.equals("streamhost")) {
					if (port == null) {
						toReturn.addStreamHost(JID, host);
					} else {
						toReturn.addStreamHost(JID, host, Integer
								.parseInt(port));
					}
					JID = null;
					host = null;
					port = null;
				} else if (elementName.equals("query")) {
					done = true;
				}
			}
		}

		toReturn.setMode((mode == "udp" ? Bytestream.Mode.UDP
				: Bytestream.Mode.TCP));
		toReturn.setSessionID(id);
		return toReturn;
	}

}
