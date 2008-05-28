package org.csapi.fw.fw_access.trust_and_security;

/**
 *	Generated from IDL interface "IpClientAccess"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpClientAccessOperations
	extends org.csapi.IpInterfaceOperations
{
	/* constants */
	/* operations  */
	void terminateAccess(java.lang.String terminationText, java.lang.String signingAlgorithm, byte[] digitalSignature) throws org.csapi.fw.P_INVALID_SIGNING_ALGORITHM,org.csapi.TpCommonExceptions,org.csapi.fw.P_INVALID_SIGNATURE;
}
