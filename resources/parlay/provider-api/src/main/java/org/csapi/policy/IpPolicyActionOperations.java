package org.csapi.policy;

/**
 *	Generated from IDL interface "IpPolicyAction"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpPolicyActionOperations
	extends org.csapi.policy.IpPolicyOperations
{
	/* constants */
	/* operations  */
	org.csapi.policy.IpPolicyRepository getParentRepository() throws org.csapi.TpCommonExceptions;
	org.csapi.policy.IpPolicyRule getParentRule() throws org.csapi.TpCommonExceptions;
}
