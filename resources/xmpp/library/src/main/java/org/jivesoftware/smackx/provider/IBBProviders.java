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
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.jivesoftware.smackx.packet.IBBExtensions;
import org.xmlpull.v1.XmlPullParser;

/**
 * 
 * Parses an IBB packet.
 * 
 * @author Alexander Wenckus
 */
public class IBBProviders {

	/**
	 * Parses an open IBB packet.
	 * 
	 * @author Alexander Wenckus
	 * 
	 */
	public static class Open implements IQProvider {
		public IQ parseIQ(XmlPullParser parser) throws Exception {
			final String sid = parser.getAttributeValue("", "sid");
			final int blockSize = Integer.parseInt(parser.getAttributeValue("",
					"block-size"));

			return new IBBExtensions.Open(sid, blockSize);
		}
	}

	/**
	 * Parses a data IBB packet.
	 * 
	 * @author Alexander Wenckus
	 * 
	 */
	public static class Data implements PacketExtensionProvider {
		public PacketExtension parseExtension(XmlPullParser parser)
				throws Exception {
			final String sid = parser.getAttributeValue("", "sid");
			final long seq = Long
					.parseLong(parser.getAttributeValue("", "seq"));
			final String data = parser.nextText();

			return new IBBExtensions.Data(sid, seq, data);
		}
	}

	/**
	 * Parses a close IBB packet.
	 * 
	 * @author Alexander Wenckus
	 * 
	 */
	public static class Close implements IQProvider {
		public IQ parseIQ(XmlPullParser parser) throws Exception {
			final String sid = parser.getAttributeValue("", "sid");

			return new IBBExtensions.Close(sid);
		}
	}

}
