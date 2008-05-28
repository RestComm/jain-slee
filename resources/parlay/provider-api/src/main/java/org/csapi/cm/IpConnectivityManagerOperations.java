package org.csapi.cm;

/**
 *	Generated from IDL interface "IpConnectivityManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpConnectivityManagerOperations
	extends org.csapi.IpServiceOperations
{
	/* constants */
	/* operations  */
	org.csapi.IpInterface getQoSMenu() throws org.csapi.TpCommonExceptions,org.csapi.cm.P_UNKNOWN_MENU;
	org.csapi.IpInterface getEnterpriseNetwork() throws org.csapi.TpCommonExceptions,org.csapi.cm.P_UNKNOWN_ENTERPRISE_NETWORK;
}
