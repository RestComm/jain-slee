package org.mobicents.slee.resource.xcapclient;

import org.mobicents.slee.resource.xcapclient.AsyncActivity;
import org.mobicents.slee.resource.xcapclient.XCAPResourceAdaptorActivityHandle;
import org.mobicents.slee.resource.xcapclient.handler.AsyncDeleteHandler;
import org.mobicents.slee.resource.xcapclient.handler.AsyncDeleteIfMatchHandler;
import org.mobicents.slee.resource.xcapclient.handler.AsyncDeleteIfNoneMatchHandler;
import org.mobicents.slee.resource.xcapclient.handler.AsyncGetHandler;
import org.mobicents.slee.resource.xcapclient.handler.AsyncPutByteArrayContentHandler;
import org.mobicents.slee.resource.xcapclient.handler.AsyncPutIfMatchByteArrayContentHandler;
import org.mobicents.slee.resource.xcapclient.handler.AsyncPutIfMatchStringContentHandler;
import org.mobicents.slee.resource.xcapclient.handler.AsyncPutIfNoneMatchByteArrayContentHandler;
import org.mobicents.slee.resource.xcapclient.handler.AsyncPutIfNoneMatchStringContentHandler;
import org.mobicents.slee.resource.xcapclient.handler.AsyncPutStringContentHandler;
import org.openxdm.xcap.common.key.XcapUriKey;

public class AsyncActivityImpl implements AsyncActivity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private transient XCAPClientResourceAdaptor ra;
	private transient XCAPResourceAdaptorActivityHandle handle;
	
	public AsyncActivityImpl(XCAPClientResourceAdaptor ra, XCAPResourceAdaptorActivityHandle handle) {
		this.handle = handle;
		this.ra = ra;
	}
	
	public XCAPClientResourceAdaptor getRA() {
		return ra;
	}
	
	public XCAPResourceAdaptorActivityHandle getHandle() {
		return handle;
	}

	public void endActivity() {		
		ra.endActivity(handle);
	}
	
	public boolean equals(Object o) {
		if (o != null && o.getClass() == this.getClass()) {
			return ((AsyncActivityImpl)o).handle.equals(this.handle);
		}
		else {
			return false;
		}
	}
	
	public int hashCode() {
		return handle.hashCode();
	}

	public void delete(XcapUriKey key) {
		ra.getExecutorService().execute(new AsyncDeleteHandler(ra,handle,key));
	}

	public void deleteIfMatch(XcapUriKey key, String eTag) {
		ra.getExecutorService().execute(new AsyncDeleteIfMatchHandler(ra,handle,key,eTag));
	}

	public void deleteIfNoneMatch(XcapUriKey key, String eTag) {
		ra.getExecutorService().execute(new AsyncDeleteIfNoneMatchHandler(ra,handle,key,eTag));
	}

	public void get(XcapUriKey key) {
		ra.getExecutorService().execute(new AsyncGetHandler(ra,handle,key));
	}

	public void put(XcapUriKey key, String mimetype, String content) {
		ra.getExecutorService().execute(new AsyncPutStringContentHandler(ra,handle,key,mimetype,content));
	}

	public void put(XcapUriKey key, String mimetype, byte[] content) {
		ra.getExecutorService().execute(new AsyncPutByteArrayContentHandler(ra,handle,key,mimetype,content));
	}

	public void putIfMatch(XcapUriKey key, String eTag, String mimetype,
			String content) {
		ra.getExecutorService().execute(new AsyncPutIfMatchStringContentHandler(ra,handle,key,eTag,mimetype,content));
	}

	public void putIfMatch(XcapUriKey key, String eTag, String mimetype,
			byte[] content) {
		ra.getExecutorService().execute(new AsyncPutIfMatchByteArrayContentHandler(ra,handle,key,eTag,mimetype,content));
	}

	public void putIfNoneMatch(XcapUriKey key, String eTag, String mimetype,
			String content) {
		ra.getExecutorService().execute(new AsyncPutIfNoneMatchStringContentHandler(ra,handle,key,eTag,mimetype,content));
	}

	public void putIfNoneMatch(XcapUriKey key, String eTag, String mimetype,
			byte[] content) {
		ra.getExecutorService().execute(new AsyncPutIfNoneMatchByteArrayContentHandler(ra,handle,key,eTag,mimetype,content));
	}

	
}
