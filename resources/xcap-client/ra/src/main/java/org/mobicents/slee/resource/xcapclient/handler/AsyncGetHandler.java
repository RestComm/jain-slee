package org.mobicents.slee.resource.xcapclient.handler;

import java.net.URI;

import org.mobicents.slee.resource.xcapclient.XCAPClientResourceAdaptor;
import org.mobicents.slee.resource.xcapclient.XCAPResourceAdaptorActivityHandle;
import org.mobicents.xcap.client.XcapResponse;
import org.mobicents.xcap.client.auth.Credentials;
import org.mobicents.xcap.client.header.Header;

/**
 * Handles an async get request.
 * 
 * @author emmartins
 * 
 */
public class AsyncGetHandler extends AbstractAsyncHandler {

	public AsyncGetHandler(XCAPClientResourceAdaptor ra,
			XCAPResourceAdaptorActivityHandle handle, URI uri,
			Header[] additionalRequestHeaders, Credentials credentials) {
		super(ra, handle, ra.getGetResponseEventType(), uri, additionalRequestHeaders, credentials);
	}

	@Override
	protected XcapResponse doRequest() throws Exception {
		return ra.getClient().get(uri, additionalRequestHeaders, credentials);
	}

}