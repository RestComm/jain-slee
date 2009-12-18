package org.mobicents.slee.resource.xcapclient;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.slee.resource.ActivityAlreadyExistsException;
import javax.slee.resource.ActivityFlags;
import javax.slee.resource.StartActivityException;

import org.apache.commons.httpclient.HttpException;
import org.mobicents.slee.resource.xcapclient.AsyncActivity;
import org.mobicents.slee.resource.xcapclient.XCAPClientResourceAdaptorSbbInterface;
import org.mobicents.slee.resource.xcapclient.XCAPResourceAdaptorActivityHandle;
import org.openxdm.xcap.client.RequestHeader;
import org.openxdm.xcap.client.Response;
import org.openxdm.xcap.common.key.XcapUriKey;

/**
 * 
 * @author emmartins
 * @author aayush.bhatnagar
 *
 */
public class XCAPClientResourceAdaptorSbbInterfaceImpl implements XCAPClientResourceAdaptorSbbInterface {
    
	private XCAPClientResourceAdaptor ra;
	private static final int ACTIVITY_FLAGS = ActivityFlags.REQUEST_ENDED_CALLBACK;
	
	public XCAPClientResourceAdaptorSbbInterfaceImpl(XCAPClientResourceAdaptor ra) {
		this.ra = ra;
	}

	public AsyncActivity createActivity() throws ActivityAlreadyExistsException, StartActivityException {
			// generate id	
			String id = UUID.randomUUID().toString();
			// create handle
			XCAPResourceAdaptorActivityHandle handle = new XCAPResourceAdaptorActivityHandle(id);    		
			// create activity
			AsyncActivityImpl activity = new AsyncActivityImpl(ra,handle);
			// start activity
			//ra.getSleeEndpoint().activityStarted(handle);
			this.ra.getXCAPResourceAdaptorContext().getSleeEndpoint().startActivityTransacted(handle, activity,ACTIVITY_FLAGS);
			this.ra.addActivity(handle, activity);

			return activity;
	}

	public Response delete(XcapUriKey key, List<RequestHeader> additionalRequestHeaders) throws HttpException, IOException {
		return ra.getClient().delete(key, additionalRequestHeaders);
	}

	public Response deleteIfMatch(XcapUriKey key, String eTag, List<RequestHeader> additionalRequestHeaders)
			throws HttpException, IOException {
		return ra.getClient().deleteIfMatch(key, eTag, additionalRequestHeaders);
	}

	public Response deleteIfNoneMatch(XcapUriKey key, String eTag, List<RequestHeader> additionalRequestHeaders)
			throws HttpException, IOException {
		return ra.getClient().deleteIfNoneMatch(key, eTag, additionalRequestHeaders);
	}

	public Response get(XcapUriKey key, List<RequestHeader> additionalRequestHeaders) throws HttpException, IOException {
		return ra.getClient().get(key, additionalRequestHeaders);
	}

	public Response put(XcapUriKey key, String mimetype, String content, List<RequestHeader> additionalRequestHeaders)
			throws HttpException, IOException {
		return ra.getClient().put(key, mimetype, content, additionalRequestHeaders);
	}

	public Response put(XcapUriKey key, String mimetype, byte[] content, List<RequestHeader> additionalRequestHeaders)
			throws HttpException, IOException {
		return ra.getClient().put(key, mimetype, content, additionalRequestHeaders);
	}

	public Response putIfMatch(XcapUriKey key, String eTag, String mimetype,
			String content, List<RequestHeader> additionalRequestHeaders) throws HttpException, IOException {
		return ra.getClient().putIfMatch(key, eTag, mimetype, content, additionalRequestHeaders);
	}

	public Response putIfMatch(XcapUriKey key, String eTag, String mimetype,
			byte[] content, List<RequestHeader> additionalRequestHeaders) throws HttpException, IOException {
		return ra.getClient().putIfMatch(key, eTag, mimetype, content, additionalRequestHeaders);
	}

	public Response putIfNoneMatch(XcapUriKey key, String eTag, String mimetype,
			String content, List<RequestHeader> additionalRequestHeaders) throws HttpException, IOException {
		return ra.getClient().putIfNoneMatch(key, eTag, mimetype, content, additionalRequestHeaders);
	}

	public Response putIfNoneMatch(XcapUriKey key, String eTag, String mimetype,
			byte[] content, List<RequestHeader> additionalRequestHeaders) throws HttpException, IOException {
		return ra.getClient().putIfNoneMatch(key, eTag, mimetype, content, additionalRequestHeaders);
	}

	public void shutdown() {
		throw new UnsupportedOperationException("shutdown of ra interface not supported");
	}

	public boolean getDoAuthentication() {
		return ra.getClient().getDoAuthentication();
	}
	
	public void setAuthenticationCredentials(String userName, String password) {
		ra.getClient().setAuthenticationCredentials(userName, password);
	}
	
	public void setDoAuthentication(boolean value) {
		ra.getClient().setDoAuthentication(value);		
	}
}