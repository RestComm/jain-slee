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

package org.mobicents.slee.enabler.xdmc;

import java.io.IOException;
import java.net.URI;

import org.mobicents.slee.enabler.sip.SubscriptionException;
import org.mobicents.xcap.client.auth.Credentials;
import org.mobicents.xcap.client.auth.CredentialsFactory;

/**
 * @author martins
 *
 */
public interface XDMClientChild {

	/**
	 * Retrieves the factory to create request authentication credentials.
	 * @return
	 */
	public CredentialsFactory getCredentialsFactory();
	
	// --- get/put/delete interface methods

	/**
	 * Retrieves the XML resource from the XCAP server, for the specified uri.
	 * Response is async.
	 * @param uri
	 *            the request uri
	 * @param credentials
	 *            authentication credentials, can be null for requests that
	 *            don't need authentication.
	 * @throws IOException 
	 */
	public void get(URI uri, Credentials credentials) throws IOException;

	/**
	 * Retrieves the XML resource from the XCAP server, for the specified uri.
	 * Response is async.
	 * @param uri
	 *            the request uri
	 * @param assertedUserId
	 *            user id already authenticated 
	 * @throws IOException 
	 */
	public void get(URI uri, String assertedUserId) throws IOException;
	
	/**
	 * Puts the specified content in the XCAP Server.
	 * 
	 * @param uri
	 *            the request uri
	 * @param mimetype
	 *            the mimetype of the content to put, for document each XCAP App
	 *            Usage defines their own mimetype, but for elements and
	 *            attributes you can use {@link ElementResource} and
	 *            {@link AttributeResource} static MIMETYPE fields.
	 * @param content
	 * @param credentials
	 *            authentication credentials, can be null for requests that
	 *            don't need authentication.
	 * @throws IOException 
	 */
	public void put(URI uri, String mimetype, byte[] content, Credentials credentials) throws IOException;

	/**
	 * Puts the specified content in the XCAP Server.
	 * 
	 * @param uri
	 *            the request uri
	 * @param mimetype
	 *            the mimetype of the content to put, for document each XCAP App
	 *            Usage defines their own mimetype, but for elements and
	 *            attributes you can use {@link ElementResource} and
	 *            {@link AttributeResource} static MIMETYPE fields.
	 * @param content
	 * @param assertedUserId
	 *            user id already authenticated 
	 * @throws IOException 
	 */
	public void put(URI uri, String mimetype, byte[] content, String assertedUserId) throws IOException;
	
	/**
	 * Puts the specified content in the XCAP Server, if the specified ETag matches the current one on the server.
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
	 * @param credentials
	 *            authentication credentials, can be null for requests that
	 *            don't need authentication.
	 * @throws IOException 
	 */
	public void putIfMatch(URI uri, String eTag, String mimetype,
			byte[] content, Credentials credentials) throws IOException;

	/**
	 * Puts the specified content in the XCAP Server, if the specified ETag matches the current one on the server.
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
	 * @param assertedUserId
	 *            user id already authenticated 
	 * @throws IOException 
	 */
	public void putIfMatch(URI uri, String eTag, String mimetype,
			byte[] content, String assertedUserId) throws IOException;
	
	/**
	 * Puts the specified content in the XCAP Server, if the specified ETag does not matches the current one on the
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
	 * @param credentials
	 *            authentication credentials, can be null for requests that
	 *            don't need authentication.
	 * @throws IOException 
	 */
	public void putIfNoneMatch(URI uri, String eTag, String mimetype,
			byte[] content, Credentials credentials) throws IOException;

	/**
	 * Puts the specified content in the XCAP Server, if the specified ETag does not matches the current one on the
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
	 * @param assertedUserId
	 *            user id already authenticated 
	 * @throws IOException 
	 */
	public void putIfNoneMatch(URI uri, String eTag, String mimetype,
			byte[] content, String assertedUserId) throws IOException;
	
	/**
	 * Deletes the content related the specified XCAP URI.
	 * 
	  * @param uri
	 *            the request uri
	 * @param credentials
	 *            authentication credentials, can be null for requests that
	 *            don't need authentication.            
	 * @throws IOException 
	 */
	public void delete(URI uri, Credentials credentials) throws IOException;

	/**
	 * Deletes the content related the specified XCAP URI.
	 * 
	  * @param uri
	 *            the request uri
	 * @param assertedUserId
	 *            user id already authenticated 
	 * @throws IOException 
	 */
	public void delete(URI uri, String assertedUserId) throws IOException;
	
	/**
	 * Deletes the content related the specified XCAP URI, if the specified
	 * ETag matches the current one on the server.
	 * 
	  * @param uri
	 *            the request uri
	 * @param eTag
	 * @param credentials
	 *            authentication credentials, can be null for requests that
	 *            don't need authentication.
	 * @throws IOException 
	 */
	public void deleteIfMatch(URI uri, String eTag, Credentials credentials) throws IOException;

	/**
	 * Deletes the content related the specified XCAP URI, if the specified
	 * ETag matches the current one on the server.
	 * 
	  * @param uri
	 *            the request uri
	 * @param eTag
	 * @param assertedUserId
	 *            user id already authenticated 
	 * @throws IOException 
	 */
	public void deleteIfMatch(URI uri, String eTag, String assertedUserId) throws IOException;
	
	/**
	 * Deletes the content related the specified XCAP URI, if the specified
	 * ETag does not matches the current one on the server.
	 * 
	  * @param uri
	 *            the request uri
	 * @param eTag
	 * @param credentials
	 *            authentication credentials, can be null for requests that
	 *            don't need authentication.
	 * @throws IOException 
	 */
	public void deleteIfNoneMatch(URI uri, String eTag, 
			Credentials credentials) throws IOException;

	/**
	 * Deletes the content related the specified XCAP URI, if the specified
	 * ETag does not matches the current one on the server.
	 * 
	  * @param uri
	 *            the request uri
	 * @param eTag
	 * @param assertedUserId
	 *            user id already authenticated 
	 * @throws IOException 
	 */
	public void deleteIfNoneMatch(URI uri, String eTag, 
			String assertedUserId) throws IOException;
	
	// --- subscribe/unsubscribe interface methods

	/**
	 * Method which triggers subscription to changes in XDMS resources. XCAP DIFF mode used is agreggated.
	 * @param subscriber - address that identifies local entity
	 * @param notifier - address that identifies the resource being subscribed (ie. XCAP Diff entity), ie. sip:tests@xcap.example.com
	 * @param expires - duration of subscription, in seconds.
	 * @param resourceURIs - array of resource uris to which enabler will subscribe, ie. String{}[resource-lists/users/sip:joe@example.com/index,rls-services/users/sip:joe@example.com/index/~~/*\/service%5b@uri='sip:marketing@example.com'%5d]
	 * @throws SubscriptionException 
	 */
	public void subscribe(String subscriber, String notifier, int expires, String[] resourceURIs) throws SubscriptionException;
	
	/**
	 * Terminates a subscription to changes in XDMS resources.
	 * @param subscriber
	 * @param notifier
	 */
	public void unsubscribe(String subscriber, String notifier);

	
}
