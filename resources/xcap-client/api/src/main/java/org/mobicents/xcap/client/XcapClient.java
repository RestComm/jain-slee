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

package org.mobicents.xcap.client;

import java.io.IOException;
import java.net.URI;

import org.mobicents.xcap.client.auth.Credentials;
import org.mobicents.xcap.client.auth.CredentialsFactory;
import org.mobicents.xcap.client.header.Header;
import org.mobicents.xcap.client.header.HeaderFactory;

public interface XcapClient {

	/**
	 * Deletes the content related the specified XCAP URI uri.
	 * 
	 * @param uri
	 *            the request uri
	 * @param additionalRequestHeaders
	 *            additional headers to include in the XCAP request
	 * @param credentials
	 *            authentication credentials, can be null for requests that
	 *            don't need authentication.
	 * @return
	 * @throws IOException
	 */
	public XcapResponse delete(URI uri, Header[] additionalRequestHeaders,
			Credentials credentials) throws IOException;

	/**
	 * Deletes the content related the specified XCAP URI uri, if the specified
	 * ETag matches the current one on the server.
	 * 
	 * @param uri
	 *            the request uri
	 * @param eTag
	 * @param additionalRequestHeaders
	 *            additional headers to include in the XCAP request
	 * @param credentials
	 *            authentication credentials, can be null for requests that
	 *            don't need authentication.
	 * @return
	 * @throws IOException
	 */
	public XcapResponse deleteIfMatch(URI uri, String eTag,
			Header[] additionalRequestHeaders, Credentials credentials)
			throws IOException;

	/**
	 * Deletes the content related the specified XCAP URI uri, if the specified
	 * ETag does not matches the current one on the server.
	 * 
	 * @param uri
	 *            the request uri
	 * @param eTag
	 * @param additionalRequestHeaders
	 *            additional headers to include in the XCAP request
	 * @param credentials
	 *            authentication credentials, can be null for requests that
	 *            don't need authentication.
	 * @return
	 * @throws IOException
	 */
	public XcapResponse deleteIfNoneMatch(URI uri, String eTag,
			Header[] additionalRequestHeaders, Credentials credentials)
			throws IOException;

	/**
	 * Retrieves the XML resource from the XCAP server, for the specified uri.
	 * 
	 * @param uri
	 *            the request uri
	 * @param additionalRequestHeaders
	 *            additional headers to include in the XCAP request
	 * @param credentials
	 *            authentication credentials, can be null for requests that
	 *            don't need authentication.
	 * @return
	 * @throws IOException
	 */
	public XcapResponse get(URI uri, Header[] additionalRequestHeaders,
			Credentials credentials) throws IOException;

	/**
	 * Retrieves the request authentication credentials factory.
	 * 
	 * @return
	 */
	public CredentialsFactory getCredentialsFactory();

	/**
	 * Retrieves the request's header factory.
	 * 
	 * @return
	 */
	public HeaderFactory getHeaderFactory();

	/**
	 * Puts the specified content in the XCAP Server, in the XCAP URI pointed by
	 * the uri.
	 * 
	 * @param uri
	 *            the request uri
	 * @param mimetype
	 *            the mimetype of the content to put, for document each XCAP App
	 *            Usage defines their own mimetype, but for elements and
	 *            attributes you can use {@link ElementResource} and
	 *            {@link AttributeResource} static MIMETYPE fields.
	 * @param content
	 * @param additionalRequestHeaders
	 *            additional headers to include in the XCAP request
	 * @param credentials
	 *            authentication credentials, can be null for requests that
	 *            don't need authentication.
	 * @return
	 * @throws IOException
	 */
	public XcapResponse put(URI uri, String mimetype, byte[] content,
			Header[] additionalRequestHeaders, Credentials credentials)
			throws IOException;

	/**
	 * Puts the specified content in the XCAP Server, in the XCAP URI pointed by
	 * the uri.
	 * 
	 * @param uri
	 *            the request uri
	 * @param mimetype
	 *            the mimetype of the content to put, for document each XCAP App
	 *            Usage defines their own mimetype, but for elements and
	 *            attributes you can use {@link ElementResource} and
	 *            {@link AttributeResource} static MIMETYPE fields.
	 * @param content
	 * @param additionalRequestHeaders
	 *            additional headers to include in the XCAP request
	 * @param credentials
	 *            authentication credentials, can be null for requests that
	 *            don't need authentication.
	 * @return
	 * @throws IOException
	 */
	public XcapResponse put(URI uri, String mimetype, String content,
			Header[] additionalRequestHeaders, Credentials credentials)
			throws IOException;

	/**
	 * Puts the specified content in the XCAP Server, in the XCAP URI pointed by
	 * the uri, if the specified ETag matches the current one on the server.
	 * 
	 * @param uri
	 *            the request uri
	 * @param eTag
	 * @param mimetype
	 *            the mimetype of the content to put, for document each XCAP App
	 *            Usage defines their own mimetype, but for elements and
	 *            attributes you can use {@link ElementResource} and
	 *            {@link AttributeResource} static MIMETYPE fields.
	 * @param content
	 * @param additionalRequestHeaders
	 *            additional headers to include in the XCAP request
	 * @param credentials
	 *            authentication credentials, can be null for requests that
	 *            don't need authentication.
	 * @return
	 * @throws IOException
	 */
	public XcapResponse putIfMatch(URI uri, String eTag, String mimetype,
			byte[] content, Header[] additionalRequestHeaders,
			Credentials credentials) throws IOException;

	/**
	 * Puts the specified content in the XCAP Server, in the XCAP URI pointed by
	 * the uri, if the specified ETag matches the current one on the server.
	 * 
	 * @param uri
	 *            the request uri
	 * @param eTag
	 * @param mimetype
	 *            the mimetype of the content to put, for document each XCAP App
	 *            Usage defines their own mimetype, but for elements and
	 *            attributes you can use {@link ElementResource} and
	 *            {@link AttributeResource} static MIMETYPE fields.
	 * @param content
	 * @param additionalRequestHeaders
	 *            additional headers to include in the XCAP request
	 * @param credentials
	 *            authentication credentials, can be null for requests that
	 *            don't need authentication.
	 * @return
	 * @throws IOException
	 */
	public XcapResponse putIfMatch(URI uri, String eTag, String mimetype,
			String content, Header[] additionalRequestHeaders,
			Credentials credentials) throws IOException;

	/**
	 * Puts the specified content in the XCAP Server, in the XCAP URI pointed by
	 * the uri, if the specified ETag does not matches the current one on the
	 * server.
	 * 
	 * @param uri
	 *            the request uri
	 * @param eTag
	 * @param mimetype
	 *            the mimetype of the content to put, for document each XCAP App
	 *            Usage defines their own mimetype, but for elements and
	 *            attributes you can use {@link ElementResource} and
	 *            {@link AttributeResource} static MIMETYPE fields.
	 * @param content
	 * @param additionalRequestHeaders
	 *            additional headers to include in the XCAP request
	 * @param credentials
	 *            authentication credentials, can be null for requests that
	 *            don't need authentication.
	 * @return
	 * @throws IOException
	 */
	public XcapResponse putIfNoneMatch(URI uri, String eTag, String mimetype,
			byte[] content, Header[] additionalRequestHeaders,
			Credentials credentials) throws IOException;

	/**
	 * Puts the specified content in the XCAP Server, in the XCAP URI pointed by
	 * the uri, if the specified ETag does not matches the current one on the
	 * server.
	 * 
	 * @param uri
	 *            the request uri
	 * @param eTag
	 * @param mimetype
	 *            the mimetype of the content to put, for document each XCAP App
	 *            Usage defines their own mimetype, but for elements and
	 *            attributes you can use {@link ElementResource} and
	 *            {@link AttributeResource} static MIMETYPE fields.
	 * @param content
	 * @param additionalRequestHeaders
	 *            additional headers to include in the XCAP request
	 * @param credentials
	 *            authentication credentials, can be null for requests that
	 *            don't need authentication.
	 * @return
	 * @throws IOException
	 */
	public XcapResponse putIfNoneMatch(URI uri, String eTag, String mimetype,
			String content, Header[] additionalRequestHeaders,
			Credentials credentials) throws IOException;

	/**
	 * 
	 * Sets the global authentication credentials, those will be used when there
	 * are no credentials for the request. Those credentials are applied to any
	 * host.
	 * 
	 * @param credentials
	 */
	public void setAuthenticationCredentials(Credentials credentials);

	/**
	 * Shutdown the client.
	 */
	public void shutdown();

	/**
	 * Unsets the authentication credentials.
	 */
	public void unsetAuthenticationCredentials();
}
