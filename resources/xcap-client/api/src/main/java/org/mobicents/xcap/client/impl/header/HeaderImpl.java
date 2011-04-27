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

package org.mobicents.xcap.client.impl.header;

import org.apache.http.message.BasicHeader;
import org.mobicents.xcap.client.header.Header;

/**
 * Implementation for the {@link Header} interface, extending the Apache
 * {@link BasicHeader}.
 * 
 * @author martins
 * 
 */
public class HeaderImpl implements Header {

	private org.apache.http.Header wrappedHeader;

	/**
	 * 
	 * @param name
	 * @param value
	 */
	public HeaderImpl(String name, String value) {
		wrappedHeader = new BasicHeader(name, value);
	}

	/**
	 * 
	 * @param wrappedHeader
	 */
	public HeaderImpl(org.apache.http.Header wrappedHeader) {
		this.wrappedHeader = wrappedHeader;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.http.message.BasicHeader#getName()
	 */
	public String getName() {
		return wrappedHeader.getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.http.message.BasicHeader#getValue()
	 */
	public String getValue() {
		return wrappedHeader.getValue();
	}

	/**
	 * 
	 * @return
	 */
	public org.apache.http.Header getWrappedHeader() {
		return wrappedHeader;
	}
	
	@Override
	public String toString() {
		return wrappedHeader.toString();
	}
}
