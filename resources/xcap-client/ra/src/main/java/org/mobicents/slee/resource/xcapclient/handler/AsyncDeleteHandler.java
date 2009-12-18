package org.mobicents.slee.resource.xcapclient.handler;

import java.util.List;

import org.mobicents.slee.resource.xcapclient.XCAPClientResourceAdaptor;
import org.mobicents.slee.resource.xcapclient.XCAPResourceAdaptorActivityHandle;
import org.openxdm.xcap.client.RequestHeader;
import org.openxdm.xcap.client.Response;
import org.openxdm.xcap.common.key.XcapUriKey;

/**
 * Handles an async delete request.
 * 
 * @author emmartins
 * 
 */
public class AsyncDeleteHandler extends AbstractAsyncHandler {

	public AsyncDeleteHandler(XCAPClientResourceAdaptor ra,
			XCAPResourceAdaptorActivityHandle handle, XcapUriKey key,
			List<RequestHeader> additionalRequestHeaders) {
		super(ra, handle, key, additionalRequestHeaders);
	}

	@Override
	protected Response doRequest() throws Exception {
		return ra.getClient().delete(key, additionalRequestHeaders);
	}

}