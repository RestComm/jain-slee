package org.csapi.fw.fw_enterprise_operator.service_subscription;

/**
 *	Generated from IDL interface "IpClientAppInfoQuery"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpClientAppInfoQueryOperations
	extends org.csapi.IpInterfaceOperations
{
	/* constants */
	/* operations  */
	org.csapi.fw.TpClientAppDescription describeClientApp(java.lang.String clientAppID) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED,org.csapi.fw.P_INVALID_CLIENT_APP_ID;
	java.lang.String[] listClientApps() throws org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED;
	java.lang.String describeSAG(java.lang.String sagID) throws org.csapi.fw.P_INVALID_SAG_ID,org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED;
	java.lang.String[] listSAGs() throws org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED;
	java.lang.String[] listSAGMembers(java.lang.String sagID) throws org.csapi.fw.P_INVALID_SAG_ID,org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED;
	java.lang.String[] listClientAppMembership(java.lang.String clientAppID) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED,org.csapi.fw.P_INVALID_CLIENT_APP_ID;
}
