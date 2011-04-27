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

import java.io.Serializable;

import org.mobicents.xcap.client.XcapEntity;
import org.mobicents.xcap.client.XcapResponse;
import org.mobicents.xcap.client.header.Header;

/**
 * @author martins
 *
 */
public class XcapResponseImpl implements XcapResponse, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final int statusCode;
	private final String eTag;
	private final String mimetype;
	private final Header[] headers;
	private final XcapEntity xcapEntity;
	
	/**
	 * @param statusCode
	 * @param eTag
	 * @param headers
	 * @param xcapEntity
	 */
	public XcapResponseImpl(int statusCode, String eTag,  String mimetype, Header[] headers,
			XcapEntity xcapEntity) {
		this.statusCode = statusCode;
		this.eTag = eTag;
		this.mimetype = mimetype;
		this.headers = headers;
		this.xcapEntity = xcapEntity;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.xcap.client.XcapResponse#getETag()
	 */
	public String getETag() {
		return eTag;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.xcap.client.XcapResponse#getEntity()
	 */
	public XcapEntity getEntity() {
		return xcapEntity;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.xcap.client.XcapResponse#getHeaders()
	 */
	public Header[] getHeaders() {
		return headers;
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.xcap.client.XcapResponse#getMimetype()
	 */
	public String getMimetype() {
		return mimetype;
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.xcap.client.XcapResponse#getCode()
	 */
	public int getCode() {
		return statusCode;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Response {").append("\nStatusCode: ").append(statusCode).append("\nHeaders: ");
		boolean first=true;
		for(int i=0;i<headers.length;i++) {
			Header header = headers[i];
			if (!first) {
				sb.append(", ");
				
			} else {
				first=false;
			}
			sb.append(header.getName()).append("=").append(header.getValue());
		}
		
		sb.append("\nEntity:\n").append(xcapEntity).append("\n}");
		return sb.toString();
	}
	
}
