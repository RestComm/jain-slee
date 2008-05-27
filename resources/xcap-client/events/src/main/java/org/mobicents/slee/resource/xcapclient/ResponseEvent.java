package org.mobicents.slee.resource.xcapclient;

import java.io.Serializable;

import org.openxdm.xcap.client.Response;

public final class ResponseEvent implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Response response = null;
	private Exception exception = null;
	private int id;
	
	public ResponseEvent(Response response) {
		this.response = response;
		id = response.hashCode()*31+"null".hashCode();
	}
	
	public ResponseEvent(Exception exception) {
		this.exception = exception;
		id = "null".hashCode()*31+exception.hashCode();
	}
	
	public Response getResponse() {
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
