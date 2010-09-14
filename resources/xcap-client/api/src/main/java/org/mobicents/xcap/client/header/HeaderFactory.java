package org.mobicents.xcap.client.header;

/**
 * Factory to build {@link Header} instances.
 * @author martins
 *
 */
public interface HeaderFactory {

	/**
	 * Creates a new header from name and value.
	 * @param name
	 * @param value
	 * @return
	 */
	public Header getBasicHeader(String name, String value);
	
}
