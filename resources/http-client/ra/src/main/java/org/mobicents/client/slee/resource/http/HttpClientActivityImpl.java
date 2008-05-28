package org.mobicents.client.slee.resource.http;

import java.rmi.server.UID;

import net.java.client.slee.resource.http.HttpClientActivity;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.log4j.Logger;

public class HttpClientActivityImpl implements HttpClientActivity {

	private static Logger logger = Logger
			.getLogger(HttpClientActivityImpl.class);

	private String sessionId;

	private HttpClient httpClient;

	private HttpClientResourceAdaptor ra;

	private boolean endOnReceivingResponse;

	public HttpClientActivityImpl(HttpClientResourceAdaptor ra,
			HttpClient httpClient) {
		init(ra, httpClient, false);

	}

	public HttpClientActivityImpl(HttpClientResourceAdaptor ra,
			HttpClient httpClient, boolean endOnReceivingResponse) {
		init(ra, httpClient, endOnReceivingResponse);

	}

	private void init(HttpClientResourceAdaptor ra, HttpClient httpClient,
			boolean endOnReceivingResponse) {
		this.ra = ra;
		this.sessionId = (new UID()).toString();
		this.httpClient = httpClient;
		this.endOnReceivingResponse = endOnReceivingResponse;

	}

	public void endActivity() {
		if (this.endOnReceivingResponse) {
			throw new IllegalStateException(
					"Activity will end automatically as soon as Response is received");
		}
		this.ra.endHttpClientActivity(this);

	}

	public void executeMethod(HttpMethod httpMethod) {
		this.ra.getExecutorService().execute(
				this.ra.new AsyncExecuteMethodHandler(httpMethod,
						this.httpClient, this));

	}

	public boolean getEndOnReceivingResponse() {
		return endOnReceivingResponse;
	}

	public String getSessionId() {
		return sessionId;
	}

}
