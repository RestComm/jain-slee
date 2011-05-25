/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

/**
 * 
 */
package org.mobicents.xcap.client.uri;

import org.mobicents.xcap.client.uri.encoding.UriComponentEncoder;

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
		if (elementSelectorSteps.length() != 0) {
			elementSelectorSteps.append(SEPARATOR);
		}
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
		if (elementSelectorSteps.length() != 0) {
			elementSelectorSteps.append(SEPARATOR);
		}
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
		if (elementSelectorSteps.length() != 0) {
			elementSelectorSteps.append(SEPARATOR);
		}
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
		if (elementSelectorSteps.length() != 0) {
			elementSelectorSteps.append(SEPARATOR);
		}
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
