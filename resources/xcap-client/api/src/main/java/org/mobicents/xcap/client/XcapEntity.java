/**
 * 
 */
package org.mobicents.xcap.client;

import org.apache.http.Header;

/**
 * @author martins
 *
 */
public interface XcapEntity {

	public boolean isChunked();

	public long getContentLength();

	public Header getContentType();

	public Header getContentEncoding();

	public byte[] getRawContent();
	
	public String getContentAsString();
	
}
