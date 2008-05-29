package org.csapi.fw.fw_access.trust_and_security;

/**
 *	Generated from IDL interface "IpClientAPILevelAuthentication"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpClientAPILevelAuthenticationOperations
	extends org.csapi.IpInterfaceOperations
{
	/* constants */
	/* operations  */
	byte[] authenticate(byte[] challenge);
	void abortAuthentication();
	void authenticationSucceeded();
	byte[] challenge(byte[] challenge);
}
