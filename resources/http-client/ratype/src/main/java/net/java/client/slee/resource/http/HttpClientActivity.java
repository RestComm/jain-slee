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

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.protocol.HttpContext;

/**
 * HttpClientActivity is created by service by calling
 * createHttpClientActivity() method of HttpClientResourceAdaptorSbbInterface.
 * Service that wants to send the Request asynchronously uses and attaches
 * itself to HttpClientActivity to receive the ResponseEvent latter. <br/>
 * 
 * @author amit bhayani
 * 
 */
public interface HttpClientActivity {

	/**
	 * @return Uniques Id for this activity
	 */
	public String getSessionId();

	/**
	 * HttpClientActivity can be created by calling createHttpClientActivity(
	 * boolean endOnReceivingResponse) method of
	 * HttpClientResourceAdaptorSbbInterface. <br/>
	 * If endOnReceivingResponse is set to true this Activity will end as soon
	 * as the ResponseEvent is sent by the ResourceAdaptor and calling this
	 * method explicitly will throw IllegalStateException <br/>
	 * If endOnReceivingResponse is set to false, service using this activity
	 * has to explicitly call endActivity() method to end the Activity.
	 */
	public void endActivity();

	/**
	 * @return Returns true if this Activity is set to end as soon as the
	 *         Response is received
	 */
	public boolean getEndOnReceivingResponse();

	/**
	 * Retrieves the http context used to send requests.
	 * 
	 * @return
	 */
	public HttpContext getHttpContext();

	/**
	 * Executes a request asynchronously using the default context.
	 * 
	 * @param request
	 *            the request to execute
	 * @param requestApplicationData
	 *            a data object optionally provided by the application, to be
	 *            returned in the response event.
	 * 
	 */
	public void execute(HttpUriRequest request, Object requestApplicationData);

	/**
	 * Executes a request asynchronously using the given context. The route to
	 * the target will be determined by the HTTP client.
	 * 
	 * @param request
	 *            the request to execute
	 * @param context
	 *            the context to use for the execution, or <code>null</code> to
	 *            use the default context
	 * 
	 */
	// void execute(HttpUriRequest request, HttpContext context);

	/**
	 * Executes a request asynchronously to the target using the default
	 * context.
	 * 
	 * @param target
	 *            the target host for the request. Implementations may accept
	 *            <code>null</code> if they can still determine a route, for
	 *            example to a default target or by inspecting the request.
	 * @param request
	 *            the request to execute
	 * @param requestApplicationData
	 *            a data object optionally provided by the application, to be
	 *            returned in the response event.
	 */
	public void execute(HttpHost target, HttpRequest request,
			Object requestApplicationData);

	/**
	 * Executes a request asynchronously to the target using the given context.
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
	 */
	// void execute(HttpHost target, HttpRequest request, HttpContext context);

}
