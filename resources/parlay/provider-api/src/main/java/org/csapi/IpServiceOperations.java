package org.csapi;

/**
 *	Generated from IDL interface "IpService"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpServiceOperations
	extends org.csapi.IpInterfaceOperations
{
	/* constants */
	/* operations  */
	void setCallback(org.csapi.IpInterface appInterface) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions;
	void setCallbackWithSessionID(org.csapi.IpInterface appInterface, int sessionID) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID;
}
