/**
 * 
 */
package org.mobicents.xcap.client.uri;

import org.mobicents.xcap.client.uri.enconding.UriComponentEncoder;

/**
 * 
 * Builds an XCAP URI's Element Selector, i.e., the component of the URI that
 * selects a specific element in the target XML document.
 * 
 * @author martins
 * 
 */
public class ElementSelectorBuilder {

	private final StringBuilder elementSelectorSteps = new StringBuilder();

	private static final char SEPARATOR = '/';

	/**
	 * 
	 * @param name
	 * @return
	 */
	public ElementSelectorBuilder appendStepByName(String name) {
		elementSelectorSteps.append(SEPARATOR);
		elementSelectorSteps.append(name);
		return this;
	}

	/**
	 * 
	 * @param name
	 * @param attrName
	 * @param attrValue
	 * @return
	 */
	public ElementSelectorBuilder appendStepByAttr(String name,
			String attrName, String attrValue) {
		elementSelectorSteps.append(SEPARATOR);
		elementSelectorSteps.append(name).append("[@").append(attrName).append(
				"=\"").append(attrValue).append("\"]");
		return this;
	}

	/**
	 * 
	 * @param name
	 * @param pos
	 * @return
	 */
	public ElementSelectorBuilder appendStepByPos(String name, int pos) {
		elementSelectorSteps.append(SEPARATOR);
		elementSelectorSteps.append(name).append('[').append(pos).append(']');
		return this;
	}

	/**
	 * 
	 * @param name
	 * @param pos
	 * @param attrName
	 * @param attrValue
	 * @return
	 */
	public ElementSelectorBuilder appendStepByPosAttr(String name, int pos,
			String attrName, String attrValue) {
		elementSelectorSteps.append(SEPARATOR);
		elementSelectorSteps.append(name).append('[').append(pos).append("][@")
				.append(attrName).append("=\"").append(attrValue).append("\"]");
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return elementSelectorSteps.toString();
	}

	/**
	 * 
	 * @return
	 */
	public String toPercentEncodedString() {
		return UriComponentEncoder.encodePath(this.toString());
	}
}
