package org.csapi.policy;

/**
 *	Generated from IDL interface "IpPolicyRule"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpPolicyRuleOperations
	extends org.csapi.policy.IpPolicyOperations
{
	/* constants */
	/* operations  */
	org.csapi.policy.IpPolicyGroup getParentGroup() throws org.csapi.TpCommonExceptions;
	org.csapi.policy.IpPolicyDomain getParentDomain() throws org.csapi.TpCommonExceptions;
	org.csapi.policy.IpPolicyCondition createCondition(java.lang.String conditionName, org.csapi.policy.TpPolicyConditionType conditionType, org.csapi.TpAttribute[] conditionAttributes) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR;
	org.csapi.policy.IpPolicyCondition getCondition(java.lang.String conditionName) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR;
	void removeCondition(java.lang.String conditionName) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR;
	int getConditionCount() throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION;
	org.csapi.policy.IpPolicyIterator getConditionIterator() throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION;
	org.csapi.policy.IpPolicyAction createAction(java.lang.String actionName, org.csapi.policy.TpPolicyActionType actionType, org.csapi.TpAttribute[] actionAttributes) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR;
	org.csapi.policy.IpPolicyAction getAction(java.lang.String actionName) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR;
	void removeAction(java.lang.String actionName) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR;
	int getActionCount() throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION;
	org.csapi.policy.IpPolicyIterator getActionIterator() throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION;
	void setValidityPeriodConditionByName(java.lang.String conditionName) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR;
	void setValidityPeriodCondition(org.csapi.policy.IpPolicyTimePeriodCondition conditionReference) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS;
	org.csapi.policy.IpPolicyTimePeriodCondition getValidityPeriodCondition() throws org.csapi.TpCommonExceptions;
	void unsetValidityPeriodCondition() throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS;
	void setConditionList(org.csapi.policy.TpPolicyConditionListElement[] conditionList) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS,org.csapi.policy.P_SYNTAX_ERROR;
	org.csapi.policy.TpPolicyConditionListElement[] getConditionList() throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION;
	void setActionList(org.csapi.policy.TpPolicyActionListElement[] actionList) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS,org.csapi.policy.P_SYNTAX_ERROR;
	org.csapi.policy.TpPolicyActionListElement[] getActionList() throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION;
}
