package org.mobicents.slee.resource.xcapclient.handler;

import java.net.URI;

import org.apache.http.Header;
import org.apache.http.auth.Credentials;
import org.mobicents.slee.resource.xcapclient.XCAPClientResourceAdaptor;
import org.mobicents.slee.resource.xcapclient.XCAPResourceAdaptorActivityHandle;
import org.mobicents.xcap.client.XcapResponse;

/**
 * Handles an async delete if ETag match request.
 * 
 * @author emmartins
 * 
 */
public class AsyncDeleteIfMatchHandler extends AbstractAsyncHandler {

	protected String eTag;

	public AsyncDeleteIfMatchHandler(XCAPClientResourceAdaptor ra,
			XCAPResourceAdaptorActivityHandle handle, URI uri, String eTag,
			Header[] additionalRequestHeaders, Credentials credentials) {
		super(ra, handle, uri, additionalRequestHeaders, credentials);
		this.eTag = eTag;
	}

	@Override
	protected XcapResponse doRequest() throws Exception {
		return ra.getClient().deleteIfMatch(uri, eTag,
				additionalRequestHeaders, credentials);
	}

}