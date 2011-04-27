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

package org.mobicents.client.slee.resource.http;

import net.java.client.slee.resource.http.event.Response;

import org.apache.commons.httpclient.Header;

public class ResponseImpl implements Response {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8062779765052329392L;

	/** Buffer for the response */
	private byte[] responseBody = null;

	private String responseBodyAsString = null;

	private Header[] headers = null;

	private int statusCode = 0;

	public ResponseImpl(byte[] responseBody, String responseBodyAsString,
			Header[] headers, int statusCode) {
		super();
		this.responseBody = responseBody;
		this.responseBodyAsString = responseBodyAsString;
		this.headers = headers;
		this.statusCode = statusCode;
	}

	public byte[] getResponseBody() {
		return responseBody;
	}

	public String getResponseBodyAsString() {
		return responseBodyAsString;
	}

	public Header[] getResponseHeaders() {
		return headers;
	}

	public int getStatusCode() {
		return statusCode;
	}

}
