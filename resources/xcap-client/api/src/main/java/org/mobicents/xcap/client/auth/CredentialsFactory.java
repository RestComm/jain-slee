package org.mobicents.xcap.client.auth;

/**
 * The factory to build {@link Credentials} used in XCAP requests authentication.
 * @author martins
 *
 */
public interface CredentialsFactory {

	/**
	 * Retrieves the credentials to be used in HTTP Digest authentication.
	 * @param user
	 * @param password
	 * @return
	 */
	public Credentials getHttpDigestCredentials(String user, String password);
	
}
