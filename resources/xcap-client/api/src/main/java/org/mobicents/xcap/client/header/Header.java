package org.mobicents.xcap.client.header;

/**
 * A HTTP header. 
 * @author martins
 *
 */
public interface Header {

	/**
	 * Retrieves the header name. 
	 * @return
	 */
	public String getName();
	
	/**
	 * Retrieves the header value.
	 * @return
	 */
	public String getValue();
	
}
