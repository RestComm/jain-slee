package org.mobicents.slee.resource.xcapclient.handler;

import java.net.URI;

import org.apache.http.Header;
import org.apache.http.auth.Credentials;
import org.mobicents.slee.resource.xcapclient.XCAPClientResourceAdaptor;
import org.mobicents.slee.resource.xcapclient.XCAPResourceAdaptorActivityHandle;
import org.mobicents.xcap.client.XcapResponse;

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
		super(ra, handle, uri, additionalRequestHeaders, credentials);
	}

	@Override
	protected XcapResponse doRequest() throws Exception {
		return ra.getClient().get(uri, additionalRequestHeaders, credentials);
	}

}