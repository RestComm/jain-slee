/**
 * 
 */
package org.mobicents.xcap.client.uri;

import org.mobicents.xcap.client.uri.enconding.UriComponentEncoder;

/**
 * @author martins
 *
 */
public class AttributeSelectorBuilder {

	private static final String PREFIX = "/@";
	private final String name;
	
	/**
	 * 
	 */
	public AttributeSelectorBuilder(String name) {
		this.name = name;
	}	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return new StringBuilder(PREFIX).append(name).toString();
	}
	
	/**
	 * 
	 * @return
	 */
	public String toPercentEncodedString() {
		return new StringBuilder(PREFIX).append(UriComponentEncoder.encodePath(name)).toString();
	}
	
}
