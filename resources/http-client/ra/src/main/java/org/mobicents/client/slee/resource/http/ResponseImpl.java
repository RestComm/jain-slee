package org.mobicents.client.slee.resource.http;

import net.java.client.slee.resource.http.event.Response;

import org.apache.commons.httpclient.Header;

public class ResponseImpl implements Response {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8062779765052329392L;

	/** Buffer for the response */
	private byte[] responseBody = null;

	private String responseBodyAsString = null;

	private Header[] headers = null;

	private int statusCode = 0;

	public ResponseImpl(byte[] responseBody, String responseBodyAsString,
			Header[] headers, int statusCode) {
		super();
		this.responseBody = responseBody;
		this.responseBodyAsString = responseBodyAsString;
		this.headers = headers;
		this.statusCode = statusCode;
	}

	public byte[] getResponseBody() {
		return responseBody;
	}

	public String getResponseBodyAsString() {
		return responseBodyAsString;
	}

	public Header[] getResponseHeaders() {
		return headers;
	}

	public int getStatusCode() {
		return statusCode;
	}

}
