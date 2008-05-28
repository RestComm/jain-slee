package org.mobicents.slee.resource.xcapclient.handler;

import org.mobicents.slee.resource.xcapclient.XCAPClientResourceAdaptor;
import org.mobicents.slee.resource.xcapclient.XCAPResourceAdaptorActivityHandle;
import org.openxdm.xcap.client.Response;
import org.openxdm.xcap.common.key.XcapUriKey;

/**
 * Handles an async delete if ETag matches none request.
 * 
 * @author emmartins
 * 
 */
public class AsyncDeleteIfNoneMatchHandler extends AbstractAsyncHandler {

	protected String eTag;

	public AsyncDeleteIfNoneMatchHandler(XCAPClientResourceAdaptor ra,
			XCAPResourceAdaptorActivityHandle handle, XcapUriKey key,
			String eTag) {
		super(ra, handle, key);
		this.eTag = eTag;
	}

	@Override
	protected Response doRequest() throws Exception {
		return ra.getClient().deleteIfNoneMatch(key, eTag);
	}

}