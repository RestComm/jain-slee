package org.csapi.policy;

/**
 *	Generated from IDL interface "IpPolicyGroup"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpPolicyGroupOperations
	extends org.csapi.policy.IpPolicyOperations
{
	/* constants */
	/* operations  */
	org.csapi.policy.IpPolicyDomain getParentDomain() throws org.csapi.TpCommonExceptions;
	org.csapi.policy.IpPolicyGroup getParentGroup() throws org.csapi.TpCommonExceptions;
	org.csapi.policy.IpPolicyGroup createGroup(java.lang.String groupName) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR;
	org.csapi.policy.IpPolicyGroup getGroup(java.lang.String groupName) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR;
	void removeGroup(java.lang.String groupName) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR;
	int getGroupCount() throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION;
	org.csapi.policy.IpPolicyIterator getGroupIterator() throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION;
	org.csapi.policy.IpPolicyRule createRule(java.lang.String ruleName) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR;
	org.csapi.policy.IpPolicyRule getRule(java.lang.String ruleName) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR;
	void removeRule(java.lang.String ruleName) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR;
	int getRuleCount() throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION;
	org.csapi.policy.IpPolicyIterator getRuleIterator() throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION;
}
