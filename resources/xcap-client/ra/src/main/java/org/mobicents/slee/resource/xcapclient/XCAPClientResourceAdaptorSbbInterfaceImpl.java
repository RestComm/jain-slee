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

package org.mobicents.slee.resource.xcapclient;

import java.io.IOException;
import java.net.URI;
import java.util.UUID;

import javax.slee.resource.ActivityAlreadyExistsException;
import javax.slee.resource.ActivityFlags;
import javax.slee.resource.StartActivityException;

import org.mobicents.xcap.client.XcapResponse;
import org.mobicents.xcap.client.auth.Credentials;
import org.mobicents.xcap.client.auth.CredentialsFactory;
import org.mobicents.xcap.client.header.Header;
import org.mobicents.xcap.client.header.HeaderFactory;

/**
 * 
 * @author emmartins
 * @author aayush.bhatnagar
 * 
 */
public class XCAPClientResourceAdaptorSbbInterfaceImpl implements
		XCAPClientResourceAdaptorSbbInterface {

	private final XCAPClientResourceAdaptor ra;

	private static final int ACTIVITY_FLAGS = ActivityFlags.REQUEST_ENDED_CALLBACK;

	private boolean active;
	
	public XCAPClientResourceAdaptorSbbInterfaceImpl(
			XCAPClientResourceAdaptor ra) {
		this.ra = ra;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	private void checkState() throws IllegalStateException {
		if (!active) {
			throw new IllegalStateException("RA not active");
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.xcap.client.XcapClient#getCredentialsFactory()
	 */
	public CredentialsFactory getCredentialsFactory() {
		checkState();
		return ra.getClient().getCredentialsFactory();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.xcap.client.XcapClient#getHeaderFactory()
	 */
	public HeaderFactory getHeaderFactory() {
		checkState();
		return ra.getClient().getHeaderFactory();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.resource.xcapclient.XCAPClientResourceAdaptorSbbInterface
	 * #createActivity()
	 */
	public AsyncActivity createActivity()
			throws ActivityAlreadyExistsException, StartActivityException {
		checkState();
		// generate id
		final String id = UUID.randomUUID().toString();
		// create handle
		final XCAPResourceAdaptorActivityHandle handle = new XCAPResourceAdaptorActivityHandle(
				id);
		// create activity
		final AsyncActivityImpl activity = new AsyncActivityImpl(ra, handle);
		// start activity
		this.ra.getSleeEndpoint().startActivitySuspended(handle, activity,
				ACTIVITY_FLAGS);
		this.ra.addActivity(handle, activity);
		return activity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.xcap.client.XcapClient#delete(java.net.URI,
	 * org.mobicents.xcap.client.header.Header[],
	 * org.mobicents.xcap.client.auth.Credentials)
	 */
	public XcapResponse delete(URI uri, Header[] additionalRequestHeaders,
			Credentials credentials) throws IOException {
		checkState();
		return ra.getClient()
				.delete(uri, additionalRequestHeaders, credentials);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.xcap.client.XcapClient#deleteIfMatch(java.net.URI,
	 * java.lang.String, org.mobicents.xcap.client.header.Header[],
	 * org.mobicents.xcap.client.auth.Credentials)
	 */
	public XcapResponse deleteIfMatch(URI uri, String eTag,
			Header[] additionalRequestHeaders, Credentials credentials)
			throws IOException {
		checkState();
		return ra.getClient().deleteIfMatch(uri, eTag,
				additionalRequestHeaders, credentials);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.xcap.client.XcapClient#deleteIfNoneMatch(java.net.URI,
	 * java.lang.String, org.mobicents.xcap.client.header.Header[],
	 * org.mobicents.xcap.client.auth.Credentials)
	 */
	public XcapResponse deleteIfNoneMatch(URI uri, String eTag,
			Header[] additionalRequestHeaders, Credentials credentials)
			throws IOException {
		checkState();
		return ra.getClient().deleteIfNoneMatch(uri, eTag,
				additionalRequestHeaders, credentials);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.xcap.client.XcapClient#get(java.net.URI,
	 * org.mobicents.xcap.client.header.Header[],
	 * org.mobicents.xcap.client.auth.Credentials)
	 */
	public XcapResponse get(URI uri, Header[] additionalRequestHeaders,
			Credentials credentials) throws IOException {
		checkState();
		return ra.getClient().get(uri, additionalRequestHeaders, credentials);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.xcap.client.XcapClient#put(java.net.URI,
	 * java.lang.String, java.lang.String,
	 * org.mobicents.xcap.client.header.Header[],
	 * org.mobicents.xcap.client.auth.Credentials)
	 */
	public XcapResponse put(URI uri, String mimetype, String content,
			Header[] additionalRequestHeaders, Credentials credentials)
			throws IOException {
		checkState();
		return ra.getClient().put(uri, mimetype, content,
				additionalRequestHeaders, credentials);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.xcap.client.XcapClient#put(java.net.URI,
	 * java.lang.String, byte[], org.mobicents.xcap.client.header.Header[],
	 * org.mobicents.xcap.client.auth.Credentials)
	 */
	public XcapResponse put(URI uri, String mimetype, byte[] content,
			Header[] additionalRequestHeaders, Credentials credentials)
			throws IOException {
		checkState();
		return ra.getClient().put(uri, mimetype, content,
				additionalRequestHeaders, credentials);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.xcap.client.XcapClient#putIfMatch(java.net.URI,
	 * java.lang.String, java.lang.String, java.lang.String,
	 * org.mobicents.xcap.client.header.Header[],
	 * org.mobicents.xcap.client.auth.Credentials)
	 */
	public XcapResponse putIfMatch(URI uri, String eTag, String mimetype,
			String content, Header[] additionalRequestHeaders,
			Credentials credentials) throws IOException {
		checkState();
		return ra.getClient().putIfMatch(uri, eTag, mimetype, content,
				additionalRequestHeaders, credentials);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.xcap.client.XcapClient#putIfMatch(java.net.URI,
	 * java.lang.String, java.lang.String, byte[],
	 * org.mobicents.xcap.client.header.Header[],
	 * org.mobicents.xcap.client.auth.Credentials)
	 */
	public XcapResponse putIfMatch(URI uri, String eTag, String mimetype,
			byte[] content, Header[] additionalRequestHeaders,
			Credentials credentials) throws IOException {
		checkState();
		return ra.getClient().putIfMatch(uri, eTag, mimetype, content,
				additionalRequestHeaders, credentials);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.xcap.client.XcapClient#putIfNoneMatch(java.net.URI,
	 * java.lang.String, java.lang.String, java.lang.String,
	 * org.mobicents.xcap.client.header.Header[],
	 * org.mobicents.xcap.client.auth.Credentials)
	 */
	public XcapResponse putIfNoneMatch(URI uri, String eTag, String mimetype,
			String content, Header[] additionalRequestHeaders,
			Credentials credentials) throws IOException {
		checkState();
		return ra.getClient().putIfNoneMatch(uri, eTag, mimetype, content,
				additionalRequestHeaders, credentials);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.xcap.client.XcapClient#putIfNoneMatch(java.net.URI,
	 * java.lang.String, java.lang.String, byte[],
	 * org.mobicents.xcap.client.header.Header[],
	 * org.mobicents.xcap.client.auth.Credentials)
	 */
	public XcapResponse putIfNoneMatch(URI uri, String eTag, String mimetype,
			byte[] content, Header[] additionalRequestHeaders,
			Credentials credentials) throws IOException {
		checkState();
		return ra.getClient().putIfNoneMatch(uri, eTag, mimetype, content,
				additionalRequestHeaders, credentials);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.xcap.client.XcapClient#shutdown()
	 */
	public void shutdown() {
		throw new UnsupportedOperationException(
				"shutdown of ra interface not supported");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.xcap.client.XcapClient#unsetAuthenticationCredentials()
	 */
	public void unsetAuthenticationCredentials() {
		checkState();
		ra.getClient().unsetAuthenticationCredentials();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.xcap.client.XcapClient#setAuthenticationCredentials(org
	 * .mobicents.xcap.client.auth.Credentials)
	 */
	public void setAuthenticationCredentials(Credentials credentials) {
		checkState();
		ra.getClient().setAuthenticationCredentials(credentials);
	}

}