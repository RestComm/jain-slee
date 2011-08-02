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

import java.util.UUID;

import net.java.client.slee.resource.http.HttpClientActivity;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.protocol.HttpContext;

/**
 * @author amit bhayani
 * @author martins
 * 
 */
public class HttpClientActivityImpl implements HttpClientActivity {

	private final String sessionId;
	private final HttpClientResourceAdaptor ra;
	private final boolean endOnReceivingResponse;
	private final HttpContext context;

	public HttpClientActivityImpl(HttpClientResourceAdaptor ra, boolean endOnReceivingResponse, HttpContext context) {
		this.ra = ra;
		this.sessionId = UUID.randomUUID().toString();
		this.endOnReceivingResponse = endOnReceivingResponse;
		this.context = context;

	}

	/**
	 * 
	 */
	public void endActivity() {
		if (this.endOnReceivingResponse) {
			throw new IllegalStateException("Activity will end automatically as soon as Response is received");
		}
		this.ra.endActivity(this);
	}

	/**
	 * 
	 */
	public void execute(HttpUriRequest httpMethod) {
		this.ra.getExecutorService().execute(this.ra.new AsyncExecuteMethodHandler(httpMethod, this, this.context));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.java.client.slee.resource.http.HttpClientActivity#execute(org.apache
	 * .http.client.methods.HttpUriRequest,
	 * org.apache.http.protocol.HttpContext)
	 */
	// @Override
	// public void execute(HttpUriRequest request, HttpContext context) {
	// this.ra.getExecutorService().execute(this.ra.new
	// AsyncExecuteMethodHandler(request, context, this));
	//
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.java.client.slee.resource.http.HttpClientActivity#execute(org.apache
	 * .http.HttpHost, org.apache.http.HttpRequest)
	 */
	@Override
	public void execute(HttpHost target, HttpRequest request) {
		this.ra.getExecutorService().execute(this.ra.new AsyncExecuteMethodHandler(target, request, this.context, this));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.java.client.slee.resource.http.HttpClientActivity#execute(org.apache
	 * .http.HttpHost, org.apache.http.HttpRequest,
	 * org.apache.http.protocol.HttpContext)
	 */
	// @Override
	// public void execute(HttpHost target, HttpRequest request, HttpContext
	// context) {
	// this.ra.getExecutorService().execute(this.ra.new
	// AsyncExecuteMethodHandler(target, request, context, this));
	// }

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return sessionId.hashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj.getClass() == this.getClass()) {
			return ((HttpClientActivityImpl) obj).sessionId.equals(this.sessionId);
		} else {
			return false;
		}
	}

}
