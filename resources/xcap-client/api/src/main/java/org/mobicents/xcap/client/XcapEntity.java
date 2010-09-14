/**
 * 
 */
package org.mobicents.xcap.client;

import org.mobicents.xcap.client.header.Header;

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
