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
package org.mobicents.xcap.client.impl.auth;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.client.CredentialsProvider;

/**
 * 
 * {@link CredentialsProvider} that has just one credentials applied to any host.
 * 
 * @author martins
 *
 */
public class SingleCredentialsProvider implements CredentialsProvider {

	/**
	 * 
	 */
	private Credentials credentials;
	
	/**
	 * 
	 */
	public SingleCredentialsProvider() {
		this.credentials = null;
	}
	
	/**
	 * @param credentials
	 */
	public SingleCredentialsProvider(Credentials credentials) {
		this.credentials = credentials;
	}
	
	/**
	 * @return the credentials
	 */
	public Credentials getCredentials() {
		return credentials;
	}

	/**
	 * @param credentials the credentials to set
	 */
	public void setCredentials(Credentials credentials) {
		this.credentials = credentials;
	}

	/* (non-Javadoc)
	 * @see org.apache.http.client.CredentialsProvider#clear()
	 */
	public void clear() {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.apache.http.client.CredentialsProvider#getCredentials(org.apache.http.auth.AuthScope)
	 */
	public Credentials getCredentials(AuthScope arg0) {
		return credentials;
	}

	/* (non-Javadoc)
	 * @see org.apache.http.client.CredentialsProvider#setCredentials(org.apache.http.auth.AuthScope, org.apache.http.auth.Credentials)
	 */
	public void setCredentials(AuthScope arg0, Credentials arg1) {
		throw new UnsupportedOperationException();
	}

}
