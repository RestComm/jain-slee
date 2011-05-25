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
package org.mobicents.xcap.client.impl;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.mobicents.xcap.client.XcapEntity;
import org.mobicents.xcap.client.header.Header;
import org.mobicents.xcap.client.impl.header.HeaderImpl;
import org.mobicents.xcap.client.uri.encoding.UriComponentEncoder;

/**
 * @author martins
 * 
 */
public class XcapEntityImpl implements XcapEntity {

	private final HttpEntity httpEntity;
	private final byte[] rawContent;

	private String contentAsString;

	/**
	 * @param httpEntity
	 * @throws IOException
	 */
	@SuppressWarnings("deprecation")
	public XcapEntityImpl(HttpEntity httpEntity) throws IOException {
		this.httpEntity = httpEntity;
		rawContent = EntityUtils.toByteArray(httpEntity);
		httpEntity.consumeContent();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.xcap.client.XcapEntity#getContentAsString()
	 */
	public String getContentAsString() {
		if (contentAsString == null) {
			contentAsString = new String(rawContent,
					UriComponentEncoder.UTF8_CHARSET);
		}
		return contentAsString;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.xcap.client.XcapEntity#getContentEncoding()
	 */
	public Header getContentEncoding() {
		return new HeaderImpl(httpEntity.getContentEncoding());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.xcap.client.XcapEntity#getContentLength()
	 */
	public long getContentLength() {
		return httpEntity.getContentLength();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.xcap.client.XcapEntity#getContentType()
	 */
	public Header getContentType() {
		return new HeaderImpl(httpEntity.getContentType());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.xcap.client.XcapEntity#getRawContent()
	 */
	public byte[] getRawContent() {
		return rawContent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.xcap.client.XcapEntity#isChunked()
	 */
	public boolean isChunked() {
		return httpEntity.isChunked();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getContentAsString();
	}
}
