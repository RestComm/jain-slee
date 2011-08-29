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

	public HttpClientActivityImpl(HttpClientResourceAdaptor ra,
			boolean endOnReceivingResponse, HttpContext context) {
		this.ra = ra;
		this.sessionId = UUID.randomUUID().toString();
		this.endOnReceivingResponse = endOnReceivingResponse;
		this.context = context;

	}

	@Override
	public void endActivity() {
		if (this.endOnReceivingResponse) {
			throw new IllegalStateException(
					"Activity will end automatically as soon as Response is received");
		}
		this.ra.endActivity(this);
	}

	@Override
	public void execute(HttpUriRequest httpMethod, Object requestApplicationData) {
		this.ra.getExecutorService().execute(
				this.ra.new AsyncExecuteMethodHandler(httpMethod,
						requestApplicationData, this, this.context));
	}

	@Override
	public void execute(HttpHost target, HttpRequest request,
			Object requestApplicationData) {
		this.ra.getExecutorService().execute(
				this.ra.new AsyncExecuteMethodHandler(target, request,
						requestApplicationData, this.context, this));
	}

	@Override
	public boolean getEndOnReceivingResponse() {
		return endOnReceivingResponse;
	}

	@Override
	public HttpContext getHttpContext() {
		return context;
	}

	@Override
	public String getSessionId() {
		return sessionId;
	}

	@Override
	public int hashCode() {
		return sessionId.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj.getClass() == this.getClass()) {
			return ((HttpClientActivityImpl) obj).sessionId
					.equals(this.sessionId);
		} else {
			return false;
		}
	}

}
