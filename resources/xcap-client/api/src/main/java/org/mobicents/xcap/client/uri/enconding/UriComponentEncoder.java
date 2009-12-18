/**
 * 
 */
package org.mobicents.xcap.client.uri.enconding;

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
