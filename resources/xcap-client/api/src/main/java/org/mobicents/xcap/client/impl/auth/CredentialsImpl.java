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

package org.mobicents.xcap.client.impl.auth;

import java.security.Principal;

import org.apache.http.auth.UsernamePasswordCredentials;
import org.mobicents.xcap.client.auth.Credentials;

/**
 * Implementation for the {@link Credentials} interface, extending the Apache
 * {@link UsernamePasswordCredentials}.
 * 
 * @author martins
 * 
 */
public class CredentialsImpl implements Credentials {

	/**
	 * 
	 */
	private final UsernamePasswordCredentials wrappedCredentials;

	/**
	 * 
	 * @param user
	 * @param password
	 */
	public CredentialsImpl(String user, String password) {
		wrappedCredentials = new UsernamePasswordCredentials(user, password);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.xcap.client.auth.Credentials#getPassword()
	 */
	public String getPassword() {
		return wrappedCredentials.getPassword();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.xcap.client.auth.Credentials#getUserPrincipal()
	 */
	public Principal getUserPrincipal() {
		return wrappedCredentials.getUserPrincipal();
	}

	/**
	 * 
	 * @return
	 */
	public UsernamePasswordCredentials getWrappedCredentials() {
		return wrappedCredentials;
	}
}
