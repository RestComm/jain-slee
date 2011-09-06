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

import java.io.InputStream;
import java.util.Set;

import org.apache.http.Header;

import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;

/**
 * A REST client request.
 * 
 * @author martins
 * 
 */
public class RESTClientEnablerRequest {

	public enum Type {
		DELETE, GET, POST, PUT
	}

	private final Type type;
	private final String uri;
	private CommonsHttpOAuthConsumer oAuthConsumer;
	private String contentType;
	private String contentEncoding;
	private InputStream content;
	private Set<Header> headers;
	private Object applicationData;

	public RESTClientEnablerRequest(Type type, String uri) {
		this.type = type;
		this.uri = uri;
	}

	/**
	 * Retrieves the request type.
	 * 
	 * @return
	 */
	public Type getType() {
		return type;
	}

	/**
	 * Retrieves the request uri.
	 * 
	 * @return
	 */
	public String getUri() {
		return uri;
	}

	/**
	 * Retrieves the app data object.
	 * 
	 * @return
	 */
	public Object getApplicationData() {
		return applicationData;
	}

	/**
	 * Sets the optional app data object.
	 * 
	 * @param applicationData
	 * @return
	 */
	public RESTClientEnablerRequest setApplicationData(Object applicationData) {
		this.applicationData = applicationData;
		return this;
	}

	/**
	 * Retrieves the optional oAuth consumer.
	 * 
	 * @return
	 */
	public CommonsHttpOAuthConsumer getOAuthConsumer() {
		return oAuthConsumer;
	}

	/**
	 * Sets the optional oAuth consumer, if not null the enabler will be use it
	 * to sign the request.
	 * 
	 * @param oAuthConsumer
	 * @return
	 */
	public RESTClientEnablerRequest setOAuthConsumer(CommonsHttpOAuthConsumer oAuthConsumer) {
		this.oAuthConsumer = oAuthConsumer;
		return this;
	}

	/**
	 * Retrieves the content type.
	 * 
	 * @return
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * Sets the content type, mandatory if the request includes content.
	 * 
	 * @param contentType
	 * @return
	 */
	public RESTClientEnablerRequest setContentType(String contentType) {
		this.contentType = contentType;
		return this;
	}

	/**
	 * Retrieves the content encoding.
	 * 
	 * @return
	 */
	public String getContentEncoding() {
		return contentEncoding;
	}

	/**
	 * Sets the content encoding.
	 * 
	 * @param contentEncoding
	 * @return
	 */
	public RESTClientEnablerRequest setContentEncoding(String contentEncoding) {
		this.contentEncoding = contentEncoding;
		return this;
	}

	/**
	 * Retrieves the content stream.
	 * 
	 * @return
	 */
	public InputStream getContent() {
		return content;
	}

	/**
	 * Sets the content stream.
	 * 
	 * @param content
	 * @return
	 */
	public RESTClientEnablerRequest setContent(InputStream content) {
		this.content = content;
		return this;
	}

	/**
	 * Retrieves the optional headers to include in the http request.
	 * 
	 * @return
	 */
	public Set<Header> getHeaders() {
		return headers;
	}

	/**
	 * Sets the optional headers to include in the http request.
	 * 
	 * @param headers
	 * @return
	 */
	public RESTClientEnablerRequest setHeaders(Set<Header> headers) {
		this.headers = headers;
		return this;
	}

}
