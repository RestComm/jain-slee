package org.mobicents.slee.resource.xcapclient.handler;

import java.io.IOException;
import java.net.URI;

import org.apache.http.Header;
import org.apache.http.auth.Credentials;
import org.mobicents.slee.resource.xcapclient.ResponseEvent;
import org.mobicents.slee.resource.xcapclient.XCAPClientResourceAdaptor;
import org.mobicents.slee.resource.xcapclient.XCAPResourceAdaptorActivityHandle;
import org.mobicents.xcap.client.XcapResponse;

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

	protected AbstractAsyncHandler(XCAPClientResourceAdaptor ra,
			XCAPResourceAdaptorActivityHandle handle, URI uri,
			Header[] additionalRequestHeaders, Credentials credentials) {
		this.uri = uri;
		this.ra = ra;
		this.handle = handle;
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
			event = new ResponseEvent(response);
		} catch (Exception e) {
			// create event with exception
			event = new ResponseEvent(e);
		}
		// process event
		ra.processResponseEvent(event, handle);
	}
}
