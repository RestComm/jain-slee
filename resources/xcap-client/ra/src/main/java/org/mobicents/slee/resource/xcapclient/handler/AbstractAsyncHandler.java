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

import java.io.IOException;
import java.net.URI;

import javax.slee.resource.FireableEventType;

import org.mobicents.slee.resource.xcapclient.ResponseEvent;
import org.mobicents.slee.resource.xcapclient.XCAPClientResourceAdaptor;
import org.mobicents.slee.resource.xcapclient.XCAPResourceAdaptorActivityHandle;
import org.mobicents.xcap.client.XcapResponse;
import org.mobicents.xcap.client.auth.Credentials;
import org.mobicents.xcap.client.header.Header;

/**
 * Abstract class for a handler
 * 
 * @author emmartins
 * 
 */
public abstract class AbstractAsyncHandler implements Runnable {

	protected URI uri;
	protected XCAPClientResourceAdaptor ra;
	protected XCAPResourceAdaptorActivityHandle handle;
	protected Header[] additionalRequestHeaders;
	protected Credentials credentials;
	protected FireableEventType eventType;
	
	protected AbstractAsyncHandler(XCAPClientResourceAdaptor ra,
			XCAPResourceAdaptorActivityHandle handle, FireableEventType eventType, URI uri,
			Header[] additionalRequestHeaders, Credentials credentials) {
		this.uri = uri;
		this.ra = ra;
		this.handle = handle;
		this.eventType = eventType;
		this.additionalRequestHeaders = additionalRequestHeaders;
		this.credentials = credentials;
	}

	/**
	 * To be implemented by concrete handler, sends the request to the XCAP and
	 * processes response.
	 * 
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	protected abstract XcapResponse doRequest() throws Exception;

	public void run() {
		ResponseEvent event = null;
		try {
			// execute method and get response
			XcapResponse response = doRequest();
			// create event with response
			event = new ResponseEvent(response,uri);
		} catch (Exception e) {
			// create event with exception
			event = new ResponseEvent(e,uri);
		}
		// process event
		ra.processResponseEvent(eventType, event, handle);
	}
}
