package org.csapi.fw.fw_enterprise_operator.service_subscription;

/**
 *	Generated from IDL interface "IpServiceProfileManagement"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpServiceProfileManagementOperations
	extends org.csapi.IpInterfaceOperations
{
	/* constants */
	/* operations  */
	java.lang.String createServiceProfile(org.csapi.fw.TpServiceProfileDescription serviceProfileDescription) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED;
	void modifyServiceProfile(org.csapi.fw.TpServiceProfile serviceProfile) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED,org.csapi.fw.P_INVALID_SERVICE_PROFILE_ID;
	void deleteServiceProfile(java.lang.String serviceProfileID) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED,org.csapi.fw.P_INVALID_SERVICE_PROFILE_ID;
	void assign(java.lang.String sagID, java.lang.String serviceProfileID) throws org.csapi.fw.P_INVALID_SAG_ID,org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED,org.csapi.fw.P_INVALID_SAG_TO_SERVICE_PROFILE_ASSIGNMENT,org.csapi.fw.P_INVALID_SERVICE_PROFILE_ID;
	void deassign(java.lang.String sagID, java.lang.String serviceProfileID) throws org.csapi.fw.P_INVALID_SAG_ID,org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED,org.csapi.fw.P_INVALID_SERVICE_PROFILE_ID;
	org.csapi.fw.TpAssignSagToServiceProfileConflict[] requestConflictInfo() throws org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED;
}
