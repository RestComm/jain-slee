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

package org.mobicents.slee.resource.xcapclient.handler;

import java.net.URI;

import org.mobicents.slee.resource.xcapclient.XCAPClientResourceAdaptor;
import org.mobicents.slee.resource.xcapclient.XCAPResourceAdaptorActivityHandle;
import org.mobicents.xcap.client.XcapResponse;
import org.mobicents.xcap.client.auth.Credentials;
import org.mobicents.xcap.client.header.Header;

/**
 * Handles an async put request, using String content.
 * 
 * @author emmartins
 * 
 */
public class AsyncPutStringContentHandler extends AbstractAsyncHandler {

	protected String mimetype;
	protected String content;

	public AsyncPutStringContentHandler(XCAPClientResourceAdaptor ra,
			XCAPResourceAdaptorActivityHandle handle, URI uri, String mimetype,
			String content, Header[] additionalRequestHeaders,
			Credentials credentials) {
		super(ra, handle, ra.getPutResponseEventType(), uri, additionalRequestHeaders, credentials);
		this.mimetype = mimetype;
		this.content = content;
	}

	@Override
	protected XcapResponse doRequest() throws Exception {
		return ra.getClient().put(uri, mimetype, content,
				additionalRequestHeaders, credentials);
	}

}