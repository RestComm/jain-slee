package org.csapi.fw.fw_enterprise_operator.service_subscription;

/**
 *	Generated from IDL interface "IpServiceProfileInfoQuery"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpServiceProfileInfoQueryOperations
	extends org.csapi.IpInterfaceOperations
{
	/* constants */
	/* operations  */
	java.lang.String[] listServiceProfiles() throws org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED;
	org.csapi.fw.TpServiceProfileDescription describeServiceProfile(java.lang.String serviceProfileID) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED,org.csapi.fw.P_INVALID_SERVICE_PROFILE_ID;
	java.lang.String[] listAssignedMembers(java.lang.String serviceProfileID) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED,org.csapi.fw.P_INVALID_SERVICE_PROFILE_ID;
}
