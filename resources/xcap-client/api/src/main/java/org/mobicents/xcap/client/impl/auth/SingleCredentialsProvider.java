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
