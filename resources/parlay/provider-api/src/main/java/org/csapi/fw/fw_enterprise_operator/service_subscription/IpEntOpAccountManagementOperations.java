package org.csapi.fw.fw_enterprise_operator.service_subscription;

/**
 *	Generated from IDL interface "IpEntOpAccountManagement"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpEntOpAccountManagementOperations
	extends org.csapi.IpInterfaceOperations
{
	/* constants */
	/* operations  */
	void modifyEntOpAccount(org.csapi.fw.TpProperty[] enterpriseOperatorProperties) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED,org.csapi.fw.P_INVALID_PROPERTY;
	void deleteEntOpAccount() throws org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED;
}
