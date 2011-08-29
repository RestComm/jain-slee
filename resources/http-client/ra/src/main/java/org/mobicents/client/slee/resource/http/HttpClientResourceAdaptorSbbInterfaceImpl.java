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

import javax.slee.facilities.Tracer;
import javax.slee.resource.ActivityFlags;
import javax.slee.resource.StartActivityException;

import net.java.client.slee.resource.http.HttpClientActivity;
import net.java.client.slee.resource.http.HttpClientResourceAdaptorSbbInterface;

import org.apache.http.client.HttpClient;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

/**
 * 
 * @author amit bhayani
 * @author martins
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

	@Override
	public HttpClient getHttpClient() {
		
		if (tracer.isFinestEnabled()) {
			tracer.finest("getHttpClient()");
		}
		
		if (!this.ra.isActive) {
			throw new IllegalStateException("ra is not in active state");
		}
		return new HttpClientWrapper(this.ra.httpclient);
	}
	
	@Override
	public HttpClientActivity createHttpClientActivity(boolean endOnReceivingResponse, HttpContext context) throws StartActivityException {

		if (tracer.isFinestEnabled()) {
			tracer.finest("createHttpClientActivity(endOnReceivingResponse="+endOnReceivingResponse+",context="+context+")");
		}
		
		if (!this.ra.isActive) {
			throw new IllegalStateException("ra is not in active state");
		}
		
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
		this.ra.getResourceAdaptorContext().getSleeEndpoint().startActivitySuspended(handle, activity, ACTIVITY_FLAGS);

		this.ra.addActivity(handle, activity);

		if (tracer.isFineEnabled()) {
			tracer.fine("Started activity "+activity.getSessionId()+", context is "+context+", endOnReceivingResponse is "+endOnReceivingResponse);
		}
		
		return activity;
	}

}
