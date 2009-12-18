/**
 * 
 */
package org.mobicents.xcap.client.uri;

import org.mobicents.xcap.client.uri.enconding.UriComponentEncoder;

/**
 * @author martins
 *
 */
public class NamespaceBindingsBuilder {

	private final StringBuilder bindings = new StringBuilder();
	
	private final static String BINDING_PREFIX = "xmlns("; 
	
	public NamespaceBindingsBuilder appendBinding(String prefix, String namespace) {
		bindings.append(BINDING_PREFIX).append(prefix).append('=').append(namespace).append(')');
		return this;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return bindings.toString();
	}
	
	/**
	 * 
	 * @return
	 */
	public String toPercentEncodedString() {
		return UriComponentEncoder.encodeQuery(this.toString());
	}
}
