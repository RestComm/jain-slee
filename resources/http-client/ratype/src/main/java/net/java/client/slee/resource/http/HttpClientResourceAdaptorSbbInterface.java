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

package net.java.client.slee.resource.http;

import java.io.IOException;

import javax.slee.resource.StartActivityException;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

/**
 * Provides SBB with the interface to interact with Http Client Resource
 * Adaptor. HttpClientResourceAdaptorSbbInterface is wrapper over
 * {@link org.apache.http.client.HttpClient} and exposes most commonly used
 * methods of HttpClient
 * 
 * @author amit bhayani
 * 
 */
public interface HttpClientResourceAdaptorSbbInterface {

	/**
	 * Method to create the HttpMethod that will used by service to send the
	 * Request
	 * 
	 * @param methodName
	 * 
	 * @param uri
	 *            The URI where the request has to be sent
	 * @return instance of HttpMethod
	 */
	public HttpRequest createHttpRequest(HttpMethodName methodName, String uri);

	/**
	 * Executes a request synchronously using the default context.
	 * 
	 * @param request
	 *            the request to execute
	 * 
	 * @return the response to the request. This is always a final response,
	 *         never an intermediate response with an 1xx status code. Whether
	 *         redirects or authentication challenges will be returned or
	 *         handled automatically depends on the implementation and
	 *         configuration of this client.
	 * @throws IOException
	 *             in case of a problem or the connection was aborted
	 * @throws ClientProtocolException
	 *             in case of an http protocol error
	 */
	HttpResponse execute(HttpUriRequest request) throws IOException, ClientProtocolException;

	/**
	 * Executes a request synchronously using the given context. The route to
	 * the target will be determined by the HTTP client.
	 * 
	 * @param request
	 *            the request to execute
	 * @param context
	 *            the context to use for the execution, or <code>null</code> to
	 *            use the default context
	 * 
	 * @return the response to the request. This is always a final response,
	 *         never an intermediate response with an 1xx status code. Whether
	 *         redirects or authentication challenges will be returned or
	 *         handled automatically depends on the implementation and
	 *         configuration of this client.
	 * @throws IOException
	 *             in case of a problem or the connection was aborted
	 * @throws ClientProtocolException
	 *             in case of an http protocol error
	 */
	HttpResponse execute(HttpUriRequest request, HttpContext context) throws IOException, ClientProtocolException;

	/**
	 * Executes a request synchronously to the target using the default context.
	 * 
	 * @param target
	 *            the target host for the request. Implementations may accept
	 *            <code>null</code> if they can still determine a route, for
	 *            example to a default target or by inspecting the request.
	 * @param request
	 *            the request to execute
	 * 
	 * @return the response to the request. This is always a final response,
	 *         never an intermediate response with an 1xx status code. Whether
	 *         redirects or authentication challenges will be returned or
	 *         handled automatically depends on the implementation and
	 *         configuration of this client.
	 * @throws IOException
	 *             in case of a problem or the connection was aborted
	 * @throws ClientProtocolException
	 *             in case of an http protocol error
	 */
	HttpResponse execute(HttpHost target, HttpRequest request) throws IOException, ClientProtocolException;

	/**
	 * Executes a request synchronously to the target using the given context.
	 * 
	 * @param target
	 *            the target host for the request. Implementations may accept
	 *            <code>null</code> if they can still determine a route, for
	 *            example to a default target or by inspecting the request.
	 * @param request
	 *            the request to execute
	 * @param context
	 *            the context to use for the execution, or <code>null</code> to
	 *            use the default context
	 * 
	 * @return the response to the request. This is always a final response,
	 *         never an intermediate response with an 1xx status code. Whether
	 *         redirects or authentication challenges will be returned or
	 *         handled automatically depends on the implementation and
	 *         configuration of this client.
	 * @throws IOException
	 *             in case of a problem or the connection was aborted
	 * @throws ClientProtocolException
	 *             in case of an http protocol error
	 */
	HttpResponse execute(HttpHost target, HttpRequest request, HttpContext context) throws IOException, ClientProtocolException;

	/**
	 * Executes a request synchronously using the default context and processes
	 * the response using the given response handler.
	 * 
	 * @param request
	 *            the request to execute
	 * @param responseHandler
	 *            the response handler
	 * 
	 * @return the response object as generated by the response handler.
	 * @throws IOException
	 *             in case of a problem or the connection was aborted
	 * @throws ClientProtocolException
	 *             in case of an http protocol error
	 */
	<T> T execute(HttpUriRequest request, ResponseHandler<? extends T> responseHandler) throws IOException, ClientProtocolException;

	/**
	 * Executes a request synchronously using the given context and processes
	 * the response using the given response handler.
	 * 
	 * @param request
	 *            the request to execute
	 * @param responseHandler
	 *            the response handler
	 * 
	 * @return the response object as generated by the response handler.
	 * @throws IOException
	 *             in case of a problem or the connection was aborted
	 * @throws ClientProtocolException
	 *             in case of an http protocol error
	 */
	<T> T execute(HttpUriRequest request, ResponseHandler<? extends T> responseHandler, HttpContext context) throws IOException, ClientProtocolException;

	/**
	 * Executes a request synchronously to the target using the default context
	 * and processes the response using the given response handler.
	 * 
	 * @param target
	 *            the target host for the request. Implementations may accept
	 *            <code>null</code> if they can still determine a route, for
	 *            example to a default target or by inspecting the request.
	 * @param request
	 *            the request to execute
	 * @param responseHandler
	 *            the response handler
	 * 
	 * @return the response object as generated by the response handler.
	 * @throws IOException
	 *             in case of a problem or the connection was aborted
	 * @throws ClientProtocolException
	 *             in case of an http protocol error
	 */
	<T> T execute(HttpHost target, HttpRequest request, ResponseHandler<? extends T> responseHandler) throws IOException, ClientProtocolException;

	/**
	 * Executes a request synchronously to the target using the given context
	 * and processes the response using the given response handler.
	 * 
	 * @param target
	 *            the target host for the request. Implementations may accept
	 *            <code>null</code> if they can still determine a route, for
	 *            example to a default target or by inspecting the request.
	 * @param request
	 *            the request to execute
	 * @param responseHandler
	 *            the response handler
	 * @param context
	 *            the context to use for the execution, or <code>null</code> to
	 *            use the default context
	 * 
	 * @return the response object as generated by the response handler.
	 * @throws IOException
	 *             in case of a problem or the connection was aborted
	 * @throws ClientProtocolException
	 *             in case of an http protocol error
	 */
	<T> T execute(HttpHost target, HttpRequest request, ResponseHandler<? extends T> responseHandler, HttpContext context) throws IOException,
			ClientProtocolException;

	/**
	 * @return Returns HTTP protocol parameters associated with this HttpClient.
	 */
	public HttpParams getParams();

	/**
	 * Creates instance of {@link HttpClientActivity} for service that wants to
	 * send the Request asynchronously. The endOnReceivingResponse value is set
	 * to false by default and service has to explicitly end this Activity
	 * 
	 * @return instance of HttpClientActivity
	 * @throws StartActivityException
	 */
	public HttpClientActivity createHttpClientActivity() throws StartActivityException;

	/**
	 * <p>
	 * Creates instance of {@link HttpClientActivity} for service that wants to
	 * send the Request asynchronously. The endOnReceivingResponse value is set
	 * to false by default and service has to explicitly end this Activity
	 * </p>
	 * <p>
	 * The {@link HttpContext} remains same for lifetime of this ACtivity
	 * </p>
	 * 
	 * @param context
	 * @return
	 * @throws StartActivityException
	 */
	public HttpClientActivity createHttpClientActivity(HttpContext context) throws StartActivityException;

	/**
	 * Creates instance of {@link HttpClientActivity} for service that wants to
	 * send the Request asynchronously
	 * 
	 * @param endOnReceivingResponse
	 *            if true Activity ends automatically as soon as the
	 *            ResponseEvent is sent by ResourceAdaptor. If false the service
	 *            has to explicitly end activity
	 * @return instance of HttpClientActivity
	 * @throws StartActivityException
	 */
	public HttpClientActivity createHttpClientActivity(boolean endOnReceivingResponse) throws StartActivityException;

	/**
	 * <p>
	 * Creates instance of {@link HttpClientActivity} for service that wants to
	 * send the Request asynchronously
	 * </p>
	 * 
	 * *
	 * <p>
	 * The {@link HttpContext} remains same for lifetime of this ACtivity
	 * </p>
	 * 
	 * @param endOnReceivingResponse
	 *            if true Activity ends automatically as soon as the
	 *            ResponseEvent is sent by ResourceAdaptor. If false the service
	 *            has to explicitly end activity
	 * @param context
	 * @return
	 * @throws StartActivityException
	 */
	public HttpClientActivity createHttpClientActivity(boolean endOnReceivingResponse, HttpContext context) throws StartActivityException;

}
