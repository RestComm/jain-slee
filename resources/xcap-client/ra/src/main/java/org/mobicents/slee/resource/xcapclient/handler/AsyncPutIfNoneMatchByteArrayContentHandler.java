package org.mobicents.slee.resource.xcapclient.handler;

import java.util.List;

import org.mobicents.slee.resource.xcapclient.XCAPClientResourceAdaptor;
import org.mobicents.slee.resource.xcapclient.XCAPResourceAdaptorActivityHandle;
import org.openxdm.xcap.client.RequestHeader;
import org.openxdm.xcap.client.Response;
import org.openxdm.xcap.common.key.XcapUriKey;

/**
 * Handles an async put if ETag matches none request, using byte array content.
 * 
 * @author emmartins
 * 
 */
public class AsyncPutIfNoneMatchByteArrayContentHandler extends AbstractAsyncHandler {

	protected String mimetype;
	protected byte[] content;
	protected String eTag;

	public AsyncPutIfNoneMatchByteArrayContentHandler(XCAPClientResourceAdaptor ra,
			XCAPResourceAdaptorActivityHandle handle, XcapUriKey key,
			String eTag, String mimetype, byte[] content, List<RequestHeader> additionalRequestHeaders) {
		super(ra, handle, key, additionalRequestHeaders);
		this.mimetype = mimetype;
		this.content = content;
		this.eTag = eTag;
	}

	@Override
	protected Response doRequest() throws Exception {
		return ra.getClient().putIfNoneMatch(key, eTag, mimetype, content, additionalRequestHeaders);
	}

}