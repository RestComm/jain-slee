package net.java.client.slee.resource.http.event;

import java.io.Serializable;

import org.apache.commons.httpclient.Header;

/**
 * This is the wrapper over response part of
 * org.apache.commons.httpclient.HttpMethod
 * 
 * @author amit.bhayani
 * 
 */
public interface Response extends Serializable {

	/**
	 * @return Returns the response body of the HTTP method, if any, as an array
	 *         of bytes.
	 */
	public byte[] getResponseBody();

	/**
	 * @return Returns the response body of the HTTP method, if any, as a
	 *         String.
	 */
	public String getResponseBodyAsString();

	/**
	 * @return Returns the status code associated with the latest response.
	 */
	public int getStatusCode();

	/**
	 * @return Returns the response headers from the most recent execution of
	 *         this request.
	 */
	public Header[] getResponseHeaders();

}
