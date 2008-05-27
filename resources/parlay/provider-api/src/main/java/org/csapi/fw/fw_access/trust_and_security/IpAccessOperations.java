package org.csapi.fw.fw_access.trust_and_security;

/**
 *	Generated from IDL interface "IpAccess"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpAccessOperations
	extends org.csapi.IpInterfaceOperations
{
	/* constants */
	/* operations  */
	org.csapi.IpInterface obtainInterface(java.lang.String interfaceName) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED,org.csapi.P_INVALID_INTERFACE_NAME;
	org.csapi.IpInterface obtainInterfaceWithCallback(java.lang.String interfaceName, org.csapi.IpInterface clientInterface) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED,org.csapi.P_INVALID_INTERFACE_NAME;
	void endAccess(org.csapi.fw.TpProperty[] endAccessProperties) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED,org.csapi.fw.P_INVALID_PROPERTY;
	java.lang.String[] listInterfaces() throws org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED;
	void releaseInterface(java.lang.String interfaceName) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED,org.csapi.P_INVALID_INTERFACE_NAME;
	java.lang.String selectSigningAlgorithm(java.lang.String signingAlgorithmCaps) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED,org.csapi.fw.P_NO_ACCEPTABLE_SIGNING_ALGORITHM;
	void terminateAccess(java.lang.String terminationText, byte[] digitalSignature) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_INVALID_SIGNATURE;
	void relinquishInterface(java.lang.String interfaceName, java.lang.String terminationText, byte[] digitalSignature) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_INVALID_SIGNATURE,org.csapi.P_INVALID_INTERFACE_NAME;
}
