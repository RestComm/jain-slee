package org.csapi.pam.access;

/**
 *	Generated from IDL interface "IpPAMIdentityPresence"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpPAMIdentityPresenceOperations
	extends org.csapi.IpInterfaceOperations
{
	/* constants */
	/* operations  */
	void setIdentityPresence(java.lang.String identity, java.lang.String identityType, org.csapi.pam.TpPAMAttribute[] attributes, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_TYPE,org.csapi.pam.P_PAM_UNKNOWN_IDENTITY,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTE;
	void setIdentityPresenceExpiration(java.lang.String identity, java.lang.String identityType, java.lang.String[] attributeNames, long expiresIn, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_TYPE,org.csapi.pam.P_PAM_UNKNOWN_IDENTITY,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTE;
	org.csapi.pam.TpPAMAttribute[] getIdentityPresence(java.lang.String identity, java.lang.String identityType, java.lang.String[] attributeNames, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_TYPE,org.csapi.pam.P_PAM_UNKNOWN_IDENTITY,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTE;
}
