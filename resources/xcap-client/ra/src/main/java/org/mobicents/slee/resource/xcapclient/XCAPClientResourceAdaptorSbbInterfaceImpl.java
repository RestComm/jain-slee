package org.mobicents.slee.resource.xcapclient;

import java.io.IOException;
import java.net.URI;
import java.util.UUID;

import javax.slee.resource.ActivityAlreadyExistsException;
import javax.slee.resource.ActivityFlags;
import javax.slee.resource.StartActivityException;

import org.apache.http.Header;
import org.apache.http.client.ClientProtocolException;
import org.mobicents.xcap.client.XcapResponse;

/**
 * 
 * @author emmartins
 * @author aayush.bhatnagar
 *
 */
public class XCAPClientResourceAdaptorSbbInterfaceImpl implements XCAPClientResourceAdaptorSbbInterface {
    
	private final XCAPClientResourceAdaptor ra;
	
	private static final int ACTIVITY_FLAGS = ActivityFlags.REQUEST_ENDED_CALLBACK;
	
	public XCAPClientResourceAdaptorSbbInterfaceImpl(XCAPClientResourceAdaptor ra) {
		this.ra = ra;
	}

	public AsyncActivity createActivity() throws ActivityAlreadyExistsException, StartActivityException {
			// generate id	
			final String id = UUID.randomUUID().toString();
			// create handle
			final XCAPResourceAdaptorActivityHandle handle = new XCAPResourceAdaptorActivityHandle(id);    		
			// create activity
			final AsyncActivityImpl activity = new AsyncActivityImpl(ra,handle);
			// start activity
			this.ra.getSleeEndpoint().startActivitySuspended(handle, activity,ACTIVITY_FLAGS);
			this.ra.addActivity(handle, activity);
			return activity;
	}

	public XcapResponse delete(URI uri, Header[] additionalRequestHeaders) throws ClientProtocolException, IOException {
		return ra.getClient().delete(uri, additionalRequestHeaders);
	}

	public XcapResponse deleteIfMatch(URI uri, String eTag, Header[] additionalRequestHeaders)
			throws ClientProtocolException, IOException {
		return ra.getClient().deleteIfMatch(uri, eTag, additionalRequestHeaders);
	}

	public XcapResponse deleteIfNoneMatch(URI uri, String eTag, Header[] additionalRequestHeaders)
			throws ClientProtocolException, IOException {
		return ra.getClient().deleteIfNoneMatch(uri, eTag, additionalRequestHeaders);
	}

	public XcapResponse get(URI uri, Header[] additionalRequestHeaders) throws ClientProtocolException, IOException {
		return ra.getClient().get(uri, additionalRequestHeaders);
	}

	public XcapResponse put(URI uri, String mimetype, String content, Header[] additionalRequestHeaders)
			throws ClientProtocolException, IOException {
		return ra.getClient().put(uri, mimetype, content, additionalRequestHeaders);
	}

	public XcapResponse put(URI uri, String mimetype, byte[] content, Header[] additionalRequestHeaders)
			throws ClientProtocolException, IOException {
		return ra.getClient().put(uri, mimetype, content, additionalRequestHeaders);
	}

	public XcapResponse putIfMatch(URI uri, String eTag, String mimetype,
			String content, Header[] additionalRequestHeaders) throws ClientProtocolException, IOException {
		return ra.getClient().putIfMatch(uri, eTag, mimetype, content, additionalRequestHeaders);
	}

	public XcapResponse putIfMatch(URI uri, String eTag, String mimetype,
			byte[] content, Header[] additionalRequestHeaders) throws ClientProtocolException, IOException {
		return ra.getClient().putIfMatch(uri, eTag, mimetype, content, additionalRequestHeaders);
	}

	public XcapResponse putIfNoneMatch(URI uri, String eTag, String mimetype,
			String content, Header[] additionalRequestHeaders) throws ClientProtocolException, IOException {
		return ra.getClient().putIfNoneMatch(uri, eTag, mimetype, content, additionalRequestHeaders);
	}

	public XcapResponse putIfNoneMatch(URI uri, String eTag, String mimetype,
			byte[] content, Header[] additionalRequestHeaders) throws ClientProtocolException, IOException {
		return ra.getClient().putIfNoneMatch(uri, eTag, mimetype, content, additionalRequestHeaders);
	}

	public void shutdown() {
		throw new UnsupportedOperationException("shutdown of ra interface not supported");
	}

	public void unsetAuthenticationCredentials() {
		ra.getClient().unsetAuthenticationCredentials();
	}
	
	public void setAuthenticationCredentials(String userName, String password) {
		ra.getClient().setAuthenticationCredentials(userName, password);
	}
	
}