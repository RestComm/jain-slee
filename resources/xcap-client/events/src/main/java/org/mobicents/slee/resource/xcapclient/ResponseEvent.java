package org.mobicents.slee.resource.xcapclient;

import java.io.Serializable;
import java.net.URI;

import org.mobicents.xcap.client.XcapResponse;

public final class ResponseEvent implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private XcapResponse response = null;
	private Exception exception = null;
	private int id;
	private URI uri;
	
	public ResponseEvent(XcapResponse response, URI uri) {
		this.response = response;
		this.uri = uri;
		id = response.hashCode()*31+"null".hashCode();
	}
	
	public ResponseEvent(Exception exception, URI uri) {
		this.exception = exception;
		this.uri = uri;
		id = "null".hashCode()*31+exception.hashCode();
	}
	
	public URI getURI() {
		return uri;
	}
	
	public XcapResponse getResponse() {
		return response;
	}
	
	public Exception getException() {
		return exception;
	}
	
	public boolean equals(Object o) {
		if (o != null && o.getClass() == this.getClass()) {
			return ((ResponseEvent)o).id == this.id;
		}
		else {
			return false;
		}
	}
	
	public int hashCode() {		
		return id;
	}
}
