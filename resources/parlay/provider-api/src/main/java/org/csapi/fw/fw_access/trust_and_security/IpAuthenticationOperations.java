package org.csapi.fw.fw_access.trust_and_security;

/**
 *	Generated from IDL interface "IpAuthentication"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpAuthenticationOperations
	extends org.csapi.IpInterfaceOperations
{
	/* constants */
	/* operations  */
	org.csapi.IpInterface requestAccess(java.lang.String accessType, org.csapi.IpInterface clientAccessInterface) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions,org.csapi.fw.P_INVALID_ACCESS_TYPE,org.csapi.fw.P_ACCESS_DENIED;
}
