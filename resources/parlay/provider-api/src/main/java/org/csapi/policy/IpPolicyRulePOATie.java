package org.csapi.policy;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpPolicyRule"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpPolicyRulePOATie
	extends IpPolicyRulePOA
{
	private IpPolicyRuleOperations _delegate;

	private POA _poa;
	public IpPolicyRulePOATie(IpPolicyRuleOperations delegate)
	{
		_delegate = delegate;
	}
	public IpPolicyRulePOATie(IpPolicyRuleOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.policy.IpPolicyRule _this()
	{
		return org.csapi.policy.IpPolicyRuleHelper.narrow(_this_object());
	}
	public org.csapi.policy.IpPolicyRule _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.policy.IpPolicyRuleHelper.narrow(_this_object(orb));
	}
	public IpPolicyRuleOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpPolicyRuleOperations delegate)
	{
		_delegate = delegate;
	}
	public POA _default_POA()
	{
		if (_poa != null)
		{
			return _poa;
		}
		else
		{
			return super._default_POA();
		}
	}
	public org.csapi.policy.IpPolicyDomain getParentDomain() throws org.csapi.TpCommonExceptions
	{
		return _delegate.getParentDomain();
	}

	public void setActionList(org.csapi.policy.TpPolicyActionListElement[] actionList) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS,org.csapi.policy.P_SYNTAX_ERROR
	{
_delegate.setActionList(actionList);
	}

	public org.csapi.policy.IpPolicyTimePeriodCondition getValidityPeriodCondition() throws org.csapi.TpCommonExceptions
	{
		return _delegate.getValidityPeriodCondition();
	}

	public org.csapi.policy.IpPolicyGroup getParentGroup() throws org.csapi.TpCommonExceptions
	{
		return _delegate.getParentGroup();
	}

	public void setAttributes(org.csapi.TpAttribute[] targetAttributes) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS
	{
_delegate.setAttributes(targetAttributes);
	}

	public int getConditionCount() throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION
	{
		return _delegate.getConditionCount();
	}

	public org.csapi.policy.IpPolicyCondition createCondition(java.lang.String conditionName, org.csapi.policy.TpPolicyConditionType conditionType, org.csapi.TpAttribute[] conditionAttributes) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR
	{
		return _delegate.createCondition(conditionName,conditionType,conditionAttributes);
	}

	public void setConditionList(org.csapi.policy.TpPolicyConditionListElement[] conditionList) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS,org.csapi.policy.P_SYNTAX_ERROR
	{
_delegate.setConditionList(conditionList);
	}

	public org.csapi.policy.IpPolicyAction createAction(java.lang.String actionName, org.csapi.policy.TpPolicyActionType actionType, org.csapi.TpAttribute[] actionAttributes) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR
	{
		return _delegate.createAction(actionName,actionType,actionAttributes);
	}

	public org.csapi.TpAttribute[] getAttributes(java.lang.String[] attributeNames) throws org.csapi.TpCommonExceptions
	{
		return _delegate.getAttributes(attributeNames);
	}

	public org.csapi.policy.IpPolicyCondition getCondition(java.lang.String conditionName) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR
	{
		return _delegate.getCondition(conditionName);
	}

	public void setAttribute(org.csapi.TpAttribute targetAttribute) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS
	{
_delegate.setAttribute(targetAttribute);
	}

	public void removeAction(java.lang.String actionName) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR
	{
_delegate.removeAction(actionName);
	}

	public void unsetValidityPeriodCondition() throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS
	{
_delegate.unsetValidityPeriodCondition();
	}

	public org.csapi.policy.TpPolicyActionListElement[] getActionList() throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION
	{
		return _delegate.getActionList();
	}

	public org.csapi.policy.IpPolicyAction getAction(java.lang.String actionName) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR
	{
		return _delegate.getAction(actionName);
	}

	public org.csapi.policy.IpPolicyIterator getConditionIterator() throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION
	{
		return _delegate.getConditionIterator();
	}

	public org.csapi.policy.TpPolicyConditionListElement[] getConditionList() throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION
	{
		return _delegate.getConditionList();
	}

	public void setValidityPeriodCondition(org.csapi.policy.IpPolicyTimePeriodCondition conditionReference) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS
	{
_delegate.setValidityPeriodCondition(conditionReference);
	}

	public void setValidityPeriodConditionByName(java.lang.String conditionName) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR
	{
_delegate.setValidityPeriodConditionByName(conditionName);
	}

	public int getActionCount() throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION
	{
		return _delegate.getActionCount();
	}

	public org.csapi.TpAttribute getAttribute(java.lang.String attributeName) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR
	{
		return _delegate.getAttribute(attributeName);
	}

	public void removeCondition(java.lang.String conditionName) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR
	{
_delegate.removeCondition(conditionName);
	}

	public org.csapi.policy.IpPolicyIterator getActionIterator() throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION
	{
		return _delegate.getActionIterator();
	}

}
