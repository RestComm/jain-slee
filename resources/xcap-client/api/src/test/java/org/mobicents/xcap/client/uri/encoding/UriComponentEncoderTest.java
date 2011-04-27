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

import org.junit.Assert;
import org.junit.Test;
import org.mobicents.xcap.client.uri.encoding.UriComponentEncoder;

/**
 * @author martins
 *
 */
public class UriComponentEncoderTest {

	@Test
	public void test() {
		
		String original_decoded_uri = "/rls-services/users/sip:bill@example.com/index/~~/rls-services/service[@uri=\"sip:good-friends@example.com;myparam=abc\"]";
		String original_encoded_uri = "/rls-services/users/sip:bill@example.com/index/~~/rls-services/service%5B@uri=%22sip:good-friends@example.com;myparam=abc%22%5D";
		
		String encoded_uri = UriComponentEncoder.encodePath(original_decoded_uri);
		System.out.println("Original Decoded: "+original_decoded_uri);
		System.out.println("Original Encoded: "+original_encoded_uri);
		System.out.println("Encoded         : "+encoded_uri);
		
		Assert.assertEquals(original_encoded_uri,encoded_uri);
								
	}
}
