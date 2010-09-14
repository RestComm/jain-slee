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
