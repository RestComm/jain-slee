package org.mobicents.slee.resource.xcapclient.handler;

import java.net.URI;

import org.apache.http.Header;
import org.apache.http.auth.Credentials;
import org.mobicents.slee.resource.xcapclient.XCAPClientResourceAdaptor;
import org.mobicents.slee.resource.xcapclient.XCAPResourceAdaptorActivityHandle;
import org.mobicents.xcap.client.XcapResponse;

/**
 * Handles an async put request, using byte array content.
 * 
 * @author emmartins
 * 
 */
public class AsyncPutByteArrayContentHandler extends AbstractAsyncHandler {

	protected String mimetype;
	protected byte[] content;

	public AsyncPutByteArrayContentHandler(XCAPClientResourceAdaptor ra,
			XCAPResourceAdaptorActivityHandle handle, URI uri, String mimetype,
			byte[] content, Header[] additionalRequestHeaders,
			Credentials credentials) {
		super(ra, handle, uri, additionalRequestHeaders, credentials);
		this.mimetype = mimetype;
		this.content = content;
	}

	@Override
	protected XcapResponse doRequest() throws Exception {
		return ra.getClient().put(uri, mimetype, content,
				additionalRequestHeaders, credentials);
	}

}