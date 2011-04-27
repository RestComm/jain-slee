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

package net.java.client.slee.resource.http.event;

import java.io.Serializable;

import org.apache.commons.httpclient.Header;

/**
 * This is the wrapper over response part of
 * org.apache.commons.httpclient.HttpMethod
 * 
 * @author amit.bhayani
 * 
 */
public interface Response extends Serializable {

	/**
	 * @return Returns the response body of the HTTP method, if any, as an array
	 *         of bytes.
	 */
	public byte[] getResponseBody();

	/**
	 * @return Returns the response body of the HTTP method, if any, as a
	 *         String.
	 */
	public String getResponseBodyAsString();

	/**
	 * @return Returns the status code associated with the latest response.
	 */
	public int getStatusCode();

	/**
	 * @return Returns the response headers from the most recent execution of
	 *         this request.
	 */
	public Header[] getResponseHeaders();

}
