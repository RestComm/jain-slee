package org.csapi.policy;

/**
 *	Generated from IDL interface "IpPolicyEventDefinition"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpPolicyEventDefinitionOperations
	extends org.csapi.policy.IpPolicyOperations
{
	/* constants */
	/* operations  */
	void setRequiredAttributes(org.csapi.TpAttribute[] requiredAttributes) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS;
	void setOptionalAttributes(org.csapi.TpAttribute[] optionalAttributes) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS;
	org.csapi.TpAttribute[] getRequiredAttributes() throws org.csapi.TpCommonExceptions;
	org.csapi.TpAttribute[] getOptionalAttributes() throws org.csapi.TpCommonExceptions;
	org.csapi.policy.IpPolicyDomain getParentDomain() throws org.csapi.TpCommonExceptions;
}
