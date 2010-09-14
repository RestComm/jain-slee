/**
 * 
 */
package org.mobicents.xcap.client;

import org.mobicents.xcap.client.header.Header;

/**
 * @author martins
 *
 */
public interface XcapResponse {

	public int getCode();
	
	public XcapEntity getEntity();
	
	public String getETag();
	
	public Header[] getHeaders();
	
}
