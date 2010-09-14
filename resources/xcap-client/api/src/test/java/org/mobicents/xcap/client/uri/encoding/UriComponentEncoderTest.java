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
