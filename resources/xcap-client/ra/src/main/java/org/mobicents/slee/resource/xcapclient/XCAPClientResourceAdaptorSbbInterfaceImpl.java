package org.mobicents.slee.resource.xcapclient;

import java.io.IOException;
import java.util.UUID;

import javax.slee.resource.ActivityAlreadyExistsException;
import javax.slee.resource.CouldNotStartActivityException;

import org.apache.commons.httpclient.HttpException;
import org.mobicents.slee.resource.xcapclient.AsyncActivity;
import org.mobicents.slee.resource.xcapclient.XCAPClientResourceAdaptorSbbInterface;
import org.mobicents.slee.resource.xcapclient.XCAPResourceAdaptorActivityHandle;
import org.openxdm.xcap.client.Response;
import org.openxdm.xcap.common.key.XcapUriKey;

public class XCAPClientResourceAdaptorSbbInterfaceImpl implements XCAPClientResourceAdaptorSbbInterface {
    
	private XCAPClientResourceAdaptor ra;
	
	public XCAPClientResourceAdaptorSbbInterfaceImpl(XCAPClientResourceAdaptor ra) {
		this.ra = ra;
	}

	public AsyncActivity createActivity() throws ActivityAlreadyExistsException, CouldNotStartActivityException {
			// generate id	
			String id = UUID.randomUUID().toString();
			// create handle
			XCAPResourceAdaptorActivityHandle handle = new XCAPResourceAdaptorActivityHandle(id);    		
			// create activity
			AsyncActivityImpl activity = new AsyncActivityImpl(ra,handle);
			// start activity
			ra.getSleeEndpoint().activityStarted(handle);
			// save handle in ra
			ra.getActivities().put(handle,activity);
			return activity;
	}

	public Response delete(XcapUriKey key) throws HttpException, IOException {
		return ra.getClient().delete(key);
	}

	public Response deleteIfMatch(XcapUriKey key, String eTag)
			throws HttpException, IOException {
		return ra.getClient().deleteIfMatch(key, eTag);
	}

	public Response deleteIfNoneMatch(XcapUriKey key, String eTag)
			throws HttpException, IOException {
		return ra.getClient().deleteIfNoneMatch(key, eTag);
	}

	public Response get(XcapUriKey key) throws HttpException, IOException {
		return ra.getClient().get(key);
	}

	public Response put(XcapUriKey key, String mimetype, String content)
			throws HttpException, IOException {
		return ra.getClient().put(key, mimetype, content);
	}

	public Response put(XcapUriKey key, String mimetype, byte[] content)
			throws HttpException, IOException {
		return ra.getClient().put(key, mimetype, content);
	}

	public Response putIfMatch(XcapUriKey key, String eTag, String mimetype,
			String content) throws HttpException, IOException {
		return ra.getClient().putIfMatch(key, eTag, mimetype, content);
	}

	public Response putIfMatch(XcapUriKey key, String eTag, String mimetype,
			byte[] content) throws HttpException, IOException {
		return ra.getClient().putIfMatch(key, eTag, mimetype, content);
	}

	public Response putIfNoneMatch(XcapUriKey key, String eTag, String mimetype,
			String content) throws HttpException, IOException {
		return ra.getClient().putIfNoneMatch(key, eTag, mimetype, content);
	}

	public Response putIfNoneMatch(XcapUriKey key, String eTag, String mimetype,
			byte[] content) throws HttpException, IOException {
		return ra.getClient().putIfNoneMatch(key, eTag, mimetype, content);
	}

	public void shutdown() {
		throw new UnsupportedOperationException("shutdown of ra interface not supported");
	}

}