package org.mobicents.client.slee.resource.http;

import java.io.IOException;

import javax.slee.resource.ActivityAlreadyExistsException;
import javax.slee.resource.CouldNotStartActivityException;

import net.java.client.slee.resource.http.HttpClientActivity;
import net.java.client.slee.resource.http.HttpClientResourceAdaptorSbbInterface;
import net.java.client.slee.resource.http.event.Response;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.HeadMethod;
import org.apache.commons.httpclient.methods.OptionsMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.TraceMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.log4j.Logger;

public class HttpClientResourceAdaptorSbbInterfaceImpl implements
		HttpClientResourceAdaptorSbbInterface {
	private static Logger logger = Logger
			.getLogger(HttpClientResourceAdaptorSbbInterfaceImpl.class);

	private HttpClient httpClient;

	private HttpClientResourceAdaptor ra;

	public HttpClientResourceAdaptorSbbInterfaceImpl(
			HttpClientResourceAdaptor ra) {
		super();
		this.ra = ra;
		httpClient = new HttpClient(new MultiThreadedHttpConnectionManager());
	}

	public HttpClientActivity createHttpClientActivity(
			boolean endOnReceivingResponse) {
		HttpClientActivity activity = new HttpClientActivityImpl(this.ra,
				this.httpClient, endOnReceivingResponse);

		HttpClientActivityHandle handle = new HttpClientActivityHandle(activity
				.getSessionId());
		this.ra.addActivity(handle, activity);

		try {

			this.ra.getSleeEndpoint().activityStarted(handle);
		} catch (ActivityAlreadyExistsException existException) {
			logger
					.error("Caught a ActivityAlreadyExistsException in createHttpClientActivity(boolean): ");
			existException.printStackTrace();
			throw new RuntimeException(
					"RAFrameResourceAdapter.onEvent(): FacilityException caught. ",
					existException);
		} catch (CouldNotStartActivityException couldNotStartException) {
			logger
					.error("Caught a CouldNotStartActivityException in createHttpClientActivity(): ");
			couldNotStartException.printStackTrace();
			throw new RuntimeException(
					"RAFrameResourceAdapter.onEvent(): FacilityException caught. ",
					couldNotStartException);
		}

		return activity;
	}

	public HttpClientActivity createHttpClientActivity() {
		return createHttpClientActivity(false);

	}

	public HttpMethod createHttpMethod(String method, String uri) {
		HttpMethod httpMethod = null;
		if (method == null) {
			throw new NullPointerException("method cannot be null");
		} else if (method.equals("GET")) {
			httpMethod = new GetMethod(uri);

		} else if (method.equals("POST")) {
			httpMethod = new PostMethod(uri);

		} else if (method.equals("PUT")) {
			httpMethod = new PutMethod(uri);

		} else if (method.equals("DELETE")) {
			httpMethod = new DeleteMethod(uri);

		} else if (method.equals("HEAD")) {
			httpMethod = new HeadMethod(uri);

		} else if (method.equals("OPTIONS")) {
			httpMethod = new OptionsMethod(uri);

		} else if (method.equals("TRACE")) {
			httpMethod = new TraceMethod(uri);

		} else {
			throw new UnsupportedOperationException(
					"method passed has to be one of the GET, POST, PUT, DELETE, HEAD, OPTIONS, TRACE. Passed method is = "
							+ method);

		}
		return httpMethod;
	}

	public Response executeMethod(HttpMethod method) {
		int statusCode = 0;
		byte[] responseBody = null;
		String responseBodyAsString = null;
		Header[] headers = null;
		Response response = null;
		try {
			statusCode = httpClient.executeMethod(method);
			responseBody = method.getResponseBody();
			responseBodyAsString = method.getResponseBodyAsString();
			headers = method.getResponseHeaders();
			response = new ResponseImpl(responseBody, responseBodyAsString,
					headers, statusCode);

		} catch (HttpException e) {
			logger.error("executeMethod failed ", e);

		} catch (IOException e) {
			logger.error("executeMethod failed ", e);

		} finally {
			// Release the connection.
			method.releaseConnection();
		}
		return response;
	}

	public HttpClientParams getParams() {
		return httpClient.getParams();
	}

	public HttpState getState() {
		return httpClient.getState();
	}

	public void setParams(HttpClientParams params) {
		httpClient.setParams(params);

	}

	public void setState(HttpState state) {
		httpClient.setState(state);

	}

}
