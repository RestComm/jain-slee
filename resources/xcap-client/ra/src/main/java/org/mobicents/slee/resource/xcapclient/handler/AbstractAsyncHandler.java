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
