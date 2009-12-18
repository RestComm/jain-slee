package org.mobicents.slee.resource.xcapclient;

import java.net.URI;
import java.util.List;

import org.apache.http.Header;
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

	public void delete(URI uri, Header[] additionalRequestHeaders) {
		ra.getExecutorService().execute(new AsyncDeleteHandler(ra,handle,uri,additionalRequestHeaders));
	}

	public void deleteIfMatch(URI uri, String eTag, Header[] additionalRequestHeaders) {
		ra.getExecutorService().execute(new AsyncDeleteIfMatchHandler(ra,handle,uri,eTag,additionalRequestHeaders));
	}

	public void deleteIfNoneMatch(URI uri, String eTag, Header[] additionalRequestHeaders) {
		ra.getExecutorService().execute(new AsyncDeleteIfNoneMatchHandler(ra,handle,uri,eTag,additionalRequestHeaders));
	}

	public void get(URI uri, Header[] additionalRequestHeaders) {
		ra.getExecutorService().execute(new AsyncGetHandler(ra,handle,uri,additionalRequestHeaders));
	}

	public void put(URI uri, String mimetype, String content, Header[] additionalRequestHeaders) {
		ra.getExecutorService().execute(new AsyncPutStringContentHandler(ra,handle,uri,mimetype,content,additionalRequestHeaders));
	}

	public void put(URI uri, String mimetype, byte[] content, Header[] additionalRequestHeaders) {
		ra.getExecutorService().execute(new AsyncPutByteArrayContentHandler(ra,handle,uri,mimetype,content,additionalRequestHeaders));
	}

	public void putIfMatch(URI uri, String eTag, String mimetype,
			String content, Header[] additionalRequestHeaders) {
		ra.getExecutorService().execute(new AsyncPutIfMatchStringContentHandler(ra,handle,uri,eTag,mimetype,content,additionalRequestHeaders));
	}

	public void putIfMatch(URI uri, String eTag, String mimetype,
			byte[] content, Header[] additionalRequestHeaders) {
		ra.getExecutorService().execute(new AsyncPutIfMatchByteArrayContentHandler(ra,handle,uri,eTag,mimetype,content,additionalRequestHeaders));
	}

	public void putIfNoneMatch(URI uri, String eTag, String mimetype,
			String content, Header[] additionalRequestHeaders) {
		ra.getExecutorService().execute(new AsyncPutIfNoneMatchStringContentHandler(ra,handle,uri,eTag,mimetype,content,additionalRequestHeaders));
	}

	public void putIfNoneMatch(URI uri, String eTag, String mimetype,
			byte[] content, Header[] additionalRequestHeaders) {
		ra.getExecutorService().execute(new AsyncPutIfNoneMatchByteArrayContentHandler(ra,handle,uri,eTag,mimetype,content,additionalRequestHeaders));
	}

	
}
