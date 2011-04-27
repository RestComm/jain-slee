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
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.mobicents.xcap.client.XcapConstant;
import org.mobicents.xcap.client.header.Header;
import org.mobicents.xcap.client.impl.header.HeaderImpl;

/**
 * @author martins
 * 
 */
public class XcapResponseHandler implements ResponseHandler<XcapResponseImpl> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.http.client.ResponseHandler#handleResponse(org.apache.http
	 * .HttpResponse)
	 */
	public XcapResponseImpl handleResponse(HttpResponse httpResponse)
			throws ClientProtocolException, IOException {

		int statusCode = httpResponse.getStatusLine().getStatusCode();

		String eTag = null;
		String mimetype = null;
		if (statusCode == 200 || statusCode == 201) {
			// sucessful response
			org.apache.http.Header eTagHeader = httpResponse
					.getFirstHeader(XcapConstant.HEADER_ETAG);
			if (eTagHeader != null) {
				eTag = eTagHeader.getValue();
			}
			org.apache.http.Header mimetypeHeader = httpResponse
			.getFirstHeader(XcapConstant.HEADER_CONTENT_TYPE);
			if (mimetypeHeader != null) {
				mimetype = mimetypeHeader.getValue();
			}
		}

		final org.apache.http.Header[] apacheHeaders = httpResponse
				.getAllHeaders();
		final Header[] headers = new Header[apacheHeaders.length];
		for (int i = 0; i < headers.length; i++) {
			headers[i] = new HeaderImpl(apacheHeaders[i]);
		}

		final HttpEntity httpEntity = httpResponse.getEntity();

		final XcapEntityImpl xcapEntity = httpEntity != null ? new XcapEntityImpl(
				httpEntity)
				: null;

		return new XcapResponseImpl(statusCode, eTag, mimetype, headers, xcapEntity);
	}

}
