package org.csapi.fw.fw_enterprise_operator.service_subscription;

/**
 *	Generated from IDL interface "IpServiceContractInfoQuery"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpServiceContractInfoQueryOperations
	extends org.csapi.IpInterfaceOperations
{
	/* constants */
	/* operations  */
	org.csapi.fw.TpServiceContractDescription describeServiceContract(java.lang.String serviceContractID) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_INVALID_SERVICE_CONTRACT_ID,org.csapi.fw.P_ACCESS_DENIED;
	java.lang.String[] listServiceContracts() throws org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED;
	java.lang.String[] listServiceProfiles(java.lang.String serviceContractID) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_INVALID_SERVICE_CONTRACT_ID,org.csapi.fw.P_ACCESS_DENIED;
}
