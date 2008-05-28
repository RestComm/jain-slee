package org.csapi.fw.fw_access.trust_and_security;

/**
 *	Generated from IDL interface "IpAPILevelAuthentication"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpAPILevelAuthenticationOperations
	extends org.csapi.fw.fw_access.trust_and_security.IpAuthenticationOperations
{
	/* constants */
	/* operations  */
	java.lang.String selectEncryptionMethod(java.lang.String encryptionCaps) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_NO_ACCEPTABLE_ENCRYPTION_CAPABILITY,org.csapi.fw.P_ACCESS_DENIED;
	byte[] authenticate(byte[] challenge) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED;
	void abortAuthentication() throws org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED;
	void authenticationSucceeded() throws org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED;
	java.lang.String selectAuthenticationMechanism(java.lang.String authMechanismList) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_NO_ACCEPTABLE_AUTHENTICATION_MECHANISM,org.csapi.fw.P_ACCESS_DENIED;
	byte[] challenge(byte[] challenge) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED;
}
