package org.mobicents.client.slee.resource.http;

import java.io.IOException;

import javax.slee.facilities.Tracer;
import javax.slee.resource.ActivityFlags;
import javax.slee.resource.StartActivityException;

import net.java.client.slee.resource.http.HttpClientActivity;
import net.java.client.slee.resource.http.HttpClientResourceAdaptorSbbInterface;
import net.java.client.slee.resource.http.HttpMethodName;
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

public class HttpClientResourceAdaptorSbbInterfaceImpl implements
		HttpClientResourceAdaptorSbbInterface {
	
	private static final int ACTIVITY_FLAGS = ActivityFlags.REQUEST_ENDED_CALLBACK;
	
	private final Tracer tracer;

	private final HttpClient httpClient;

	private final HttpClientResourceAdaptor ra;

	public HttpClientResourceAdaptorSbbInterfaceImpl(
			HttpClientResourceAdaptor ra) {
		this.ra = ra;
		this.tracer = ra.getResourceAdaptorContext().getTracer(HttpClientResourceAdaptorSbbInterfaceImpl.class.getName());
		this.httpClient = new HttpClient(new MultiThreadedHttpConnectionManager());
	}

	public HttpClientActivity createHttpClientActivity(
			boolean endOnReceivingResponse) throws StartActivityException {
		
		HttpClientActivity activity = new HttpClientActivityImpl(this.ra,
				this.httpClient, endOnReceivingResponse);

		HttpClientActivityHandle handle = new HttpClientActivityHandle(activity
				.getSessionId());
		
			// this happens with a tx context
		this.ra.getResourceAdaptorContext().getSleeEndpoint().startActivityTransacted(handle, activity,ACTIVITY_FLAGS);
		
		this.ra.addActivity(handle, activity);

		return activity;
	}

	public HttpClientActivity createHttpClientActivity() throws StartActivityException {
		return createHttpClientActivity(false);
	}
	
	public HttpMethod createHttpMethod(HttpMethodName methodName, String uri) {
		HttpMethod httpMethod = null;
		
		if (methodName == null) {
			throw new NullPointerException("method cannot be null");
		} else {
			switch (methodName) {
			case GET:
				httpMethod = new GetMethod(uri);
				break;
			case POST:
				httpMethod = new PostMethod(uri);
				break;

			case PUT:
				httpMethod = new PutMethod(uri);
				break;
			case DELETE:
				httpMethod = new DeleteMethod(uri);
				break;
			case HEAD:
				httpMethod = new HeadMethod(uri);
				break;
			case OPTIONS:
				httpMethod = new OptionsMethod(uri);
				break;
			case TRACE:
				httpMethod = new TraceMethod(uri);
				break;
			default:
				throw new UnsupportedOperationException(
						"method name passed has to be one of the GET, POST, PUT, DELETE, HEAD, OPTIONS, TRACE. Passed method is = "
								+ methodName);
			}
		}
			
		return httpMethod;
	}

	public Response executeMethod(HttpMethod method) throws IOException {
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
			tracer.severe("executeMethod failed ", e);
			throw e;
		} catch (IOException e) {
			tracer.severe("executeMethod failed ", e);
			throw e;
		} finally {
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
