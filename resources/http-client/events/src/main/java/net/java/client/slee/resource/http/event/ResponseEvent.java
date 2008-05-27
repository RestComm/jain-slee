package net.java.client.slee.resource.http.event;

import java.io.Serializable;

/**
 * Service sending Request asynchronously will receive
 * net.java.client.slee.resource.http.event.ResponseEvent as soon as the
 * Resource Adaptor receives the Response and emitts ResponseEvent. </br>
 * ResponseEvent carries either the Response if everything went fine else
 * Exception if there was any problem
 * 
 * @author amit.bhayani
 * 
 */
public class ResponseEvent implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8346946507877889058L;

	private Response response;

	private Exception exception;

	private int id;

	public ResponseEvent(Response response) {
		this.response = response;
		id = response.hashCode() * 31 + "null".hashCode();
	}

	public ResponseEvent(Exception exception) {
		this.exception = exception;
		id = "null".hashCode() * 31 + exception.hashCode();
	}

	public Response getResponse() {
		return this.response;
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
