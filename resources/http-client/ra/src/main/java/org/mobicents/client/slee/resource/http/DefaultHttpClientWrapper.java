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

/**
 * 
 */
package org.mobicents.client.slee.resource.http;

import java.io.IOException;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

/**
 * <p>
 * A wrapper for {@link DefaultHttpClient} that sets the
 * {@link ThreadSafeClientConnManager} to make the {@link HttpClient} as
 * thread-safe and multi threaded client.
 * </p>
 * <p>
 * Any customization in HttpCLient can be done by custom wrapper class that
 * implements the HttpClient. Set the fully qualified class name in RA config
 * and RA will create a new instance of this class
 * </p>
 * 
 * 
 * @author amit bhayani
 * 
 */
public class DefaultHttpClientWrapper implements HttpClient {

	private final HttpClient httpclient;

	public DefaultHttpClientWrapper() {
		ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager();
		cm.setMaxTotal(100);

		httpclient = new DefaultHttpClient(cm);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.http.client.HttpClient#execute(org.apache.http.client.methods
	 * .HttpUriRequest)
	 */
	@Override
	public HttpResponse execute(HttpUriRequest arg0) throws IOException,
			ClientProtocolException {
		return httpclient.execute(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.http.client.HttpClient#execute(org.apache.http.client.methods
	 * .HttpUriRequest, org.apache.http.protocol.HttpContext)
	 */
	@Override
	public HttpResponse execute(HttpUriRequest arg0, HttpContext arg1)
			throws IOException, ClientProtocolException {
		return httpclient.execute(arg0, arg1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.http.client.HttpClient#execute(org.apache.http.HttpHost,
	 * org.apache.http.HttpRequest)
	 */
	@Override
	public HttpResponse execute(HttpHost arg0, HttpRequest arg1)
			throws IOException, ClientProtocolException {
		return httpclient.execute(arg0, arg1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.http.client.HttpClient#execute(org.apache.http.client.methods
	 * .HttpUriRequest, org.apache.http.client.ResponseHandler)
	 */
	@Override
	public <T> T execute(HttpUriRequest arg0, ResponseHandler<? extends T> arg1)
			throws IOException, ClientProtocolException {
		return httpclient.execute(arg0, arg1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.http.client.HttpClient#execute(org.apache.http.HttpHost,
	 * org.apache.http.HttpRequest, org.apache.http.protocol.HttpContext)
	 */
	@Override
	public HttpResponse execute(HttpHost arg0, HttpRequest arg1,
			HttpContext arg2) throws IOException, ClientProtocolException {
		return httpclient.execute(arg0, arg1, arg2);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.http.client.HttpClient#execute(org.apache.http.client.methods
	 * .HttpUriRequest, org.apache.http.client.ResponseHandler,
	 * org.apache.http.protocol.HttpContext)
	 */
	@Override
	public <T> T execute(HttpUriRequest arg0,
			ResponseHandler<? extends T> arg1, HttpContext arg2)
			throws IOException, ClientProtocolException {
		return httpclient.execute(arg0, arg1, arg2);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.http.client.HttpClient#execute(org.apache.http.HttpHost,
	 * org.apache.http.HttpRequest, org.apache.http.client.ResponseHandler)
	 */
	@Override
	public <T> T execute(HttpHost arg0, HttpRequest arg1,
			ResponseHandler<? extends T> arg2) throws IOException,
			ClientProtocolException {
		return httpclient.execute(arg0, arg1, arg2);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.http.client.HttpClient#execute(org.apache.http.HttpHost,
	 * org.apache.http.HttpRequest, org.apache.http.client.ResponseHandler,
	 * org.apache.http.protocol.HttpContext)
	 */
	@Override
	public <T> T execute(HttpHost arg0, HttpRequest arg1,
			ResponseHandler<? extends T> arg2, HttpContext arg3)
			throws IOException, ClientProtocolException {
		return httpclient.execute(arg0, arg1, arg2, arg3);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.http.client.HttpClient#getConnectionManager()
	 */
	@Override
	public ClientConnectionManager getConnectionManager() {
		return httpclient.getConnectionManager();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.http.client.HttpClient#getParams()
	 */
	@Override
	public HttpParams getParams() {
		return httpclient.getParams();
	}

}
