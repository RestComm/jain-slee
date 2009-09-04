package org.mobicents.client.slee.resource.http;

import java.util.UUID;

import net.java.client.slee.resource.http.HttpClientActivity;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;

/**
 * @author amit
 * @author martins
 *
 */
public class HttpClientActivityImpl implements HttpClientActivity {

	private final String sessionId;
	private final HttpClient httpClient;
	private final HttpClientResourceAdaptor ra;
	private final boolean endOnReceivingResponse;

	/**
	 * 
	 * @param ra
	 * @param httpClient
	 */
	public HttpClientActivityImpl(HttpClientResourceAdaptor ra,
			HttpClient httpClient) {
		this(ra, httpClient, false);
	}

	/**
	 * 
	 * @param ra
	 * @param httpClient
	 * @param endOnReceivingResponse
	 */
	public HttpClientActivityImpl(HttpClientResourceAdaptor ra,
			HttpClient httpClient, boolean endOnReceivingResponse) {
		this.ra = ra;
		this.sessionId = UUID.randomUUID().toString();
		this.httpClient = httpClient;
		this.endOnReceivingResponse = endOnReceivingResponse;
	}

	/**
	 * 
	 */
	public void endActivity() {
		if (this.endOnReceivingResponse) {
			throw new IllegalStateException(
					"Activity will end automatically as soon as Response is received");
		}
		this.ra.endActivity(this);
	}

	/**
	 * 
	 */
	public void executeMethod(HttpMethod httpMethod) {
		this.ra.getExecutorService().execute(
				this.ra.new AsyncExecuteMethodHandler(httpMethod,
						this.httpClient, this));
	}

	/**
	 * 
	 */
	public boolean getEndOnReceivingResponse() {
		return endOnReceivingResponse;
	}

	/**
	 * 
	 */
	public String getSessionId() {
		return sessionId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return sessionId.hashCode();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj.getClass() == this.getClass()) {
			return ((HttpClientActivityImpl)obj).sessionId.equals(this.sessionId);
		}
		else {
			return false;
		}
	}
}
