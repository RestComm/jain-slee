package org.mobicents.slee.resource.xcapclient.handler;

import org.mobicents.slee.resource.xcapclient.XCAPClientResourceAdaptor;
import org.mobicents.slee.resource.xcapclient.XCAPResourceAdaptorActivityHandle;
import org.openxdm.xcap.client.Response;
import org.openxdm.xcap.common.key.XcapUriKey;

/**
 *  Handles an async delete request.
 * @author emmartins
 *
 */
public class AsyncDeleteHandler extends AbstractAsyncHandler {
	
	public AsyncDeleteHandler(XCAPClientResourceAdaptor ra,XCAPResourceAdaptorActivityHandle handle, XcapUriKey key) {
		super(ra, handle, key);
	}
	
	@Override
	protected Response doRequest() throws Exception {
		return ra.getClient().delete(key);
	}
	
}