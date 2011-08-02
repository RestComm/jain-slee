/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.client.slee.resource.http;

import java.io.IOException;

import javax.slee.facilities.Tracer;
import javax.slee.resource.ActivityFlags;
import javax.slee.resource.StartActivityException;

import net.java.client.slee.resource.http.HttpClientActivity;
import net.java.client.slee.resource.http.HttpClientResourceAdaptorSbbInterface;
import net.java.client.slee.resource.http.HttpMethodName;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpTrace;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

/**
 * 
 * @author amit bhayani
 * 
 */
public class HttpClientResourceAdaptorSbbInterfaceImpl implements HttpClientResourceAdaptorSbbInterface {

	private static final int ACTIVITY_FLAGS = ActivityFlags.REQUEST_ENDED_CALLBACK;

	private final Tracer tracer;

	private final HttpClientResourceAdaptor ra;

	public HttpClientResourceAdaptorSbbInterfaceImpl(HttpClientResourceAdaptor ra) {
		this.ra = ra;
		this.tracer = ra.getResourceAdaptorContext().getTracer(HttpClientResourceAdaptorSbbInterfaceImpl.class.getName());
	}

	public HttpClientActivity createHttpClientActivity(boolean endOnReceivingResponse) throws StartActivityException {
		return createHttpClientActivity(endOnReceivingResponse, null);

	}

	public HttpClientActivity createHttpClientActivity() throws StartActivityException {
		return createHttpClientActivity(false, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.java.client.slee.resource.http.HttpClientResourceAdaptorSbbInterface
	 * #createHttpClientActivity(org.apache.http.protocol.HttpContext)
	 */
	@Override
	public HttpClientActivity createHttpClientActivity(HttpContext context) throws StartActivityException {
		return createHttpClientActivity(false, context);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.java.client.slee.resource.http.HttpClientResourceAdaptorSbbInterface
	 * #createHttpClientActivity(boolean, org.apache.http.protocol.HttpContext)
	 */
	@Override
	public HttpClientActivity createHttpClientActivity(boolean endOnReceivingResponse, HttpContext context) throws StartActivityException {

		// Maintain HttpSession on server side
		if (context == null) {
			context = new BasicHttpContext();

		}
		if (context.getAttribute(ClientContext.COOKIE_STORE) == null) {
			BasicCookieStore cookieStore = new BasicCookieStore();
			context.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
		}

		HttpClientActivity activity = new HttpClientActivityImpl(this.ra, endOnReceivingResponse, context);

		HttpClientActivityHandle handle = new HttpClientActivityHandle(activity.getSessionId());

		// this happens with a tx context
		this.ra.getResourceAdaptorContext().getSleeEndpoint().startActivityTransacted(handle, activity, ACTIVITY_FLAGS);

		this.ra.addActivity(handle, activity);

		return activity;
	}

	public HttpRequest createHttpRequest(HttpMethodName methodName, String uri) {
		HttpRequest httpMethod = null;

		if (methodName == null) {
			throw new NullPointerException("method cannot be null");
		} else {
			switch (methodName) {
			case GET:
				httpMethod = new HttpGet(uri);
				break;
			case POST:
				httpMethod = new HttpPost(uri);
				break;

			case PUT:
				httpMethod = new HttpPut(uri);
				break;
			case DELETE:
				httpMethod = new HttpDelete(uri);
				break;
			case HEAD:
				httpMethod = new HttpHead(uri);
				break;
			case OPTIONS:
				httpMethod = new HttpOptions(uri);
				break;
			case TRACE:
				httpMethod = new HttpTrace(uri);
				break;
			default:
				throw new UnsupportedOperationException(
						"method name passed has to be one of the GET, POST, PUT, DELETE, HEAD, OPTIONS, TRACE. Passed method is = " + methodName);
			}
		}

		return httpMethod;
	}

	public HttpResponse execute(HttpUriRequest request) throws IOException, ClientProtocolException {
		if (!this.ra.isActive) {
			throw new IOException(String.format("The HttpClientResourceAdaptor=%s is not Active", this.ra.resourceAdaptorContext.getEntityName()));
		}
		return this.ra.httpclient.execute(request);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.java.client.slee.resource.http.HttpClientResourceAdaptorSbbInterface
	 * #execute(org.apache.http.client.methods.HttpUriRequest,
	 * org.apache.http.protocol.HttpContext)
	 */
	@Override
	public HttpResponse execute(HttpUriRequest request, HttpContext context) throws IOException, ClientProtocolException {
		if (!this.ra.isActive) {
			throw new IOException(String.format("The HttpClientResourceAdaptor=%s is not Active", this.ra.resourceAdaptorContext.getEntityName()));
		}
		return this.ra.httpclient.execute(request, context);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.java.client.slee.resource.http.HttpClientResourceAdaptorSbbInterface
	 * #execute(org.apache.http.HttpHost, org.apache.http.HttpRequest)
	 */
	@Override
	public HttpResponse execute(HttpHost target, HttpRequest request) throws IOException, ClientProtocolException {
		if (!this.ra.isActive) {
			throw new IOException(String.format("The HttpClientResourceAdaptor=%s is not Active", this.ra.resourceAdaptorContext.getEntityName()));
		}
		return this.ra.httpclient.execute(target, request);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.java.client.slee.resource.http.HttpClientResourceAdaptorSbbInterface
	 * #execute(org.apache.http.HttpHost, org.apache.http.HttpRequest,
	 * org.apache.http.protocol.HttpContext)
	 */
	@Override
	public HttpResponse execute(HttpHost target, HttpRequest request, HttpContext context) throws IOException, ClientProtocolException {
		if (!this.ra.isActive) {
			throw new IOException(String.format("The HttpClientResourceAdaptor=%s is not Active", this.ra.resourceAdaptorContext.getEntityName()));
		}
		return this.ra.httpclient.execute(target, request, context);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.java.client.slee.resource.http.HttpClientResourceAdaptorSbbInterface
	 * #execute(org.apache.http.client.methods.HttpUriRequest,
	 * org.apache.http.client.ResponseHandler)
	 */
	@Override
	public <T> T execute(HttpUriRequest request, ResponseHandler<? extends T> responseHandler) throws IOException, ClientProtocolException {
		if (!this.ra.isActive) {
			throw new IOException(String.format("The HttpClientResourceAdaptor=%s is not Active", this.ra.resourceAdaptorContext.getEntityName()));
		}
		return this.ra.httpclient.execute(request, responseHandler);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.java.client.slee.resource.http.HttpClientResourceAdaptorSbbInterface
	 * #execute(org.apache.http.client.methods.HttpUriRequest,
	 * org.apache.http.client.ResponseHandler,
	 * org.apache.http.protocol.HttpContext)
	 */
	@Override
	public <T> T execute(HttpUriRequest request, ResponseHandler<? extends T> responseHandler, HttpContext context) throws IOException, ClientProtocolException {
		if (!this.ra.isActive) {
			throw new IOException(String.format("The HttpClientResourceAdaptor=%s is not Active", this.ra.resourceAdaptorContext.getEntityName()));
		}
		return this.ra.httpclient.execute(request, responseHandler, context);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.java.client.slee.resource.http.HttpClientResourceAdaptorSbbInterface
	 * #execute(org.apache.http.HttpHost, org.apache.http.HttpRequest,
	 * org.apache.http.client.ResponseHandler)
	 */
	@Override
	public <T> T execute(HttpHost target, HttpRequest request, ResponseHandler<? extends T> responseHandler) throws IOException, ClientProtocolException {
		if (!this.ra.isActive) {
			throw new IOException(String.format("The HttpClientResourceAdaptor=%s is not Active", this.ra.resourceAdaptorContext.getEntityName()));
		}
		return this.ra.httpclient.execute(target, request, responseHandler);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.java.client.slee.resource.http.HttpClientResourceAdaptorSbbInterface
	 * #execute(org.apache.http.HttpHost, org.apache.http.HttpRequest,
	 * org.apache.http.client.ResponseHandler,
	 * org.apache.http.protocol.HttpContext)
	 */
	@Override
	public <T> T execute(HttpHost target, HttpRequest request, ResponseHandler<? extends T> responseHandler, HttpContext context) throws IOException,
			ClientProtocolException {
		if (!this.ra.isActive) {
			throw new IOException(String.format("The HttpClientResourceAdaptor=%s is not Active", this.ra.resourceAdaptorContext.getEntityName()));
		}
		return this.ra.httpclient.execute(target, request, responseHandler, context);
	}

	public HttpParams getParams() {
		return this.ra.httpclient.getParams();
	}

}
