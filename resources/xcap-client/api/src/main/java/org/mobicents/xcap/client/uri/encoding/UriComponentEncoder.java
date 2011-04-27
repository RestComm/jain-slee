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

/**
 * 
 */
package org.mobicents.xcap.client.uri.encoding;

import java.nio.charset.Charset;
import java.util.BitSet;

import org.apache.commons.codec.net.URLCodec;

/**
 * @author martins
 *
 */
public class UriComponentEncoder {

	public static Charset UTF8_CHARSET = Charset.forName("UTF-8");
	
	private static byte[] encode(String s, BitSet allowed) throws NullPointerException {
        if (s == null) {
            throw new NullPointerException("string to encode is null");
        }
        if (allowed == null) {
            throw new NullPointerException("Allowed bitset may not be null");
        }
        return URLCodec.encodeUrl(allowed, s.getBytes(UTF8_CHARSET));
    }
	
	/**
	 * Encodes an HTTP URI Path.
	 * 
	 * @param path
	 * @return
	 * @throws NullPointerException
	 */
	public static String encodePath(String path) throws NullPointerException {
		return new String(encode(path, UriComponentEncoderBitSets.allowed_abs_path));
	}

	/**
	 * Encodes an HTTP URI Query.
	 * @param query
	 * @return
	 * @throws NullPointerException
	 */
	public static String encodeQuery(String query) throws NullPointerException {
		return new String(encode(query, UriComponentEncoderBitSets.allowed_query));
	}
	
}
