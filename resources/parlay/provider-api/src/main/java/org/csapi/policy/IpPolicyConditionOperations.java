package org.csapi.policy;

/**
 *	Generated from IDL interface "IpPolicyCondition"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpPolicyConditionOperations
	extends org.csapi.policy.IpPolicyOperations
{
	/* constants */
	/* operations  */
	org.csapi.policy.IpPolicyRepository getParentRepository() throws org.csapi.TpCommonExceptions;
	org.csapi.policy.IpPolicyRule getParentRule() throws org.csapi.TpCommonExceptions;
}
