package org.mobicents.slee.resource.xcapclient.handler;

import java.util.List;

import org.mobicents.slee.resource.xcapclient.XCAPClientResourceAdaptor;
import org.mobicents.slee.resource.xcapclient.XCAPResourceAdaptorActivityHandle;
import org.openxdm.xcap.client.RequestHeader;
import org.openxdm.xcap.client.Response;
import org.openxdm.xcap.common.key.XcapUriKey;

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
			XCAPResourceAdaptorActivityHandle handle, XcapUriKey key,
			String mimetype, byte[] content,
			List<RequestHeader> additionalRequestHeaders) {
		super(ra, handle, key, additionalRequestHeaders);
		this.mimetype = mimetype;
		this.content = content;
	}

	@Override
	protected Response doRequest() throws Exception {
		return ra.getClient().put(key, mimetype, content,
				additionalRequestHeaders);
	}

}