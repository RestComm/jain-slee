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

import javax.slee.resource.ResourceAdaptorTypeID;
import javax.slee.resource.StartActivityException;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.protocol.HttpContext;

/**
 * Provides SBB with the interface to interact with Http Client Resource
 * Adaptor. HttpClientResourceAdaptorSbbInterface is wrapper over
 * {@link org.apache.http.client.HttpClient} and exposes most commonly used
 * methods of HttpClient
 * 
 * @author amit bhayani
 * @author martins
 * 
 */
public interface HttpClientResourceAdaptorSbbInterface {

	public static final ResourceAdaptorTypeID RESOURCE_ADAPTOR_TYPE_ID = HttpClientActivityContextInterfaceFactory.RESOURCE_ADAPTOR_TYPE_ID;

	/**
	 * Retrieves the client managed by the RA, allowing execution of synchronous
	 * requests and access the client parameters. Note that the returned client
	 * throws {@link SecurityException} if the application tries to access its
	 * {@link ClientConnectionManager}.
	 * 
	 * @return
	 */
	public HttpClient getHttpClient();

	/**
	 * <p>
	 * Creates instance of {@link HttpClientActivity} for service that wants to
	 * send Requests asynchronously
	 * </p>
	 * 
	 * @param endOnReceivingResponse
	 *            if true Activity ends automatically as soon as the
	 *            ResponseEvent is sent by ResourceAdaptor. If false the service
	 *            has to explicitly end activity
	 * @param context
	 *            an optional http context may be provided. If not provided a
	 *            basic context will be set.
	 * @return
	 * @throws StartActivityException
	 */
	public HttpClientActivity createHttpClientActivity(
			boolean endOnReceivingResponse, HttpContext context)
			throws StartActivityException;

}
