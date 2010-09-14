package org.mobicents.xcap.client.auth;

import java.security.Principal;

/**
 * This interface represents a set of credentials consisting of a security 
 * principal and a secret (password) that can be used to establish user
 * identity.
 * 
 * @author martins
 *
 */
public interface Credentials {

	/**
	 * 
	 * @return
	 */
	Principal getUserPrincipal();

	/**
	 * 
	 * @return
	 */
    String getPassword();
    
}
