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