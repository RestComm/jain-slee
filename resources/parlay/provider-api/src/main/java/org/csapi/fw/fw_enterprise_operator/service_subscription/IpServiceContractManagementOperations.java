package org.csapi.fw.fw_enterprise_operator.service_subscription;

/**
 *	Generated from IDL interface "IpServiceContractManagement"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpServiceContractManagementOperations
	extends org.csapi.IpInterfaceOperations
{
	/* constants */
	/* operations  */
	java.lang.String createServiceContract(org.csapi.fw.TpServiceContractDescription serviceContractDescription) throws org.csapi.fw.P_INVALID_SERVICE_ID,org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED;
	void modifyServiceContract(org.csapi.fw.TpServiceContract serviceContract) throws org.csapi.fw.P_INVALID_SERVICE_ID,org.csapi.TpCommonExceptions,org.csapi.fw.P_INVALID_SERVICE_CONTRACT_ID,org.csapi.fw.P_ACCESS_DENIED;
	void deleteServiceContract(java.lang.String serviceContractID) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_INVALID_SERVICE_CONTRACT_ID,org.csapi.fw.P_ACCESS_DENIED;
}
