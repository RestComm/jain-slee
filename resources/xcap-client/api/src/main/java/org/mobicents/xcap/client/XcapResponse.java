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

	/**
	 * 
	 * @return
	 */
	public int getCode();
	
	/**
	 * 
	 * @return
	 */
	public XcapEntity getEntity();
	
	/**
	 * 
	 * @return
	 */
	public String getETag();
	
	/**
	 * 
	 * @return
	 */
	public String getMimetype();
	
	/**
	 * 
	 * @return
	 */
	public Header[] getHeaders();
	
}
