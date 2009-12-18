/**
 * 
 */
package org.mobicents.xcap.client.uri;

/**
 * @author martins
 *
 */
public class NamespaceSelectorBuilder {
	
	private static final String NAMESPACE_SELECTOR = "/namespace::*";
	private static final String NAMESPACE_SELECTOR_ENCODED = "/namespace%3A%3A%2A";

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return NAMESPACE_SELECTOR;
	}
	
	/**
	 * 
	 * @return
	 */
	public String toPercentEncodedString() {
		return NAMESPACE_SELECTOR_ENCODED;
	}
}
