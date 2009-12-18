package org.mobicents.slee.resource.xcapclient.handler;

import java.util.List;

import org.mobicents.slee.resource.xcapclient.XCAPClientResourceAdaptor;
import org.mobicents.slee.resource.xcapclient.XCAPResourceAdaptorActivityHandle;
import org.openxdm.xcap.client.RequestHeader;
import org.openxdm.xcap.client.Response;
import org.openxdm.xcap.common.key.XcapUriKey;

/**
 * Handles an async delete if ETag match request.
 * 
 * @author emmartins
 * 
 */
public class AsyncDeleteIfMatchHandler extends AbstractAsyncHandler {

	protected String eTag;

	public AsyncDeleteIfMatchHandler(XCAPClientResourceAdaptor ra,
			XCAPResourceAdaptorActivityHandle handle, XcapUriKey key,
			String eTag, List<RequestHeader> additionalRequestHeaders) {
		super(ra, handle, key, additionalRequestHeaders);
		this.eTag = eTag;
	}

	@Override
	protected Response doRequest() throws Exception {
		return ra.getClient().deleteIfMatch(key, eTag, additionalRequestHeaders);
	}

}