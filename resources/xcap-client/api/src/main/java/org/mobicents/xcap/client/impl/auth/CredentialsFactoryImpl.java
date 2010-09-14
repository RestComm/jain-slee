package org.mobicents.xcap.client.impl.auth;

import org.mobicents.xcap.client.auth.Credentials;
import org.mobicents.xcap.client.auth.CredentialsFactory;

/**
 * Implementation of {@link CredentialsFactory}.
 * 
 * @author martins
 * 
 */
public class CredentialsFactoryImpl implements CredentialsFactory {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.xcap.client.auth.CredentialsFactory#getHttpDigestCredentials
	 * (java.lang.String, java.lang.String)
	 */
	public Credentials getHttpDigestCredentials(String user, String password) {
		return new CredentialsImpl(user, password);
	}

}
