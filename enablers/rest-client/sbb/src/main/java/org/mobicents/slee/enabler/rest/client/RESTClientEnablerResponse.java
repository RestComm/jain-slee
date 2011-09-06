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

package org.mobicents.slee.enabler.rest.client;

import org.apache.http.HttpResponse;

/**
 * The response related with the execution of a {@link RESTClientEnablerRequest}
 * .
 * 
 * @author martins
 * 
 */
public class RESTClientEnablerResponse {

	private final RESTClientEnablerRequest request;
	private final HttpResponse response;
	private final Exception exception;

	RESTClientEnablerResponse(RESTClientEnablerRequest request,
			HttpResponse response, Exception exception) {
		this.request = request;
		this.response = response;
		this.exception = exception;
	}

	/**
	 * Retrieves the executed request.
	 * 
	 * @return
	 */
	public RESTClientEnablerRequest getRequest() {
		return request;
	}

	/**
	 * Retrieves the response obtained from HTTP client, if any.
	 * 
	 * @return
	 */
	public HttpResponse getHttpResponse() {
		return response;
	}

	/**
	 * Retrieves the exception thrown when executing the request, if any.
	 * 
	 * @return
	 */
	public Exception getExecutionException() {
		return exception;
	}

}
