package org.csapi.fw.fw_access.trust_and_security;

/**
 *	Generated from IDL interface "IpInitial"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpInitialOperations
	extends org.csapi.IpInterfaceOperations
{
	/* constants */
	/* operations  */
	org.csapi.fw.TpAuthDomain initiateAuthentication(org.csapi.fw.TpAuthDomain clientDomain, java.lang.String authType) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions,org.csapi.fw.P_INVALID_AUTH_TYPE,org.csapi.fw.P_INVALID_DOMAIN_ID;
	org.csapi.fw.TpAuthDomain initiateAuthenticationWithVersion(org.csapi.fw.TpAuthDomain clientDomain, java.lang.String authType, java.lang.String frameworkVersion) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_VERSION,org.csapi.fw.P_INVALID_AUTH_TYPE,org.csapi.fw.P_INVALID_DOMAIN_ID;
}
