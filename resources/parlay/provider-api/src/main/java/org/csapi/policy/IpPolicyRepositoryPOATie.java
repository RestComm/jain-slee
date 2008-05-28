package org.csapi.policy;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpPolicyRepository"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpPolicyRepositoryPOATie
	extends IpPolicyRepositoryPOA
{
	private IpPolicyRepositoryOperations _delegate;

	private POA _poa;
	public IpPolicyRepositoryPOATie(IpPolicyRepositoryOperations delegate)
	{
		_delegate = delegate;
	}
	public IpPolicyRepositoryPOATie(IpPolicyRepositoryOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.policy.IpPolicyRepository _this()
	{
		return org.csapi.policy.IpPolicyRepositoryHelper.narrow(_this_object());
	}
	public org.csapi.policy.IpPolicyRepository _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.policy.IpPolicyRepositoryHelper.narrow(_this_object(orb));
	}
	public IpPolicyRepositoryOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpPolicyRepositoryOperations delegate)
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
	public int getConditionCount() throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION
	{
		return _delegate.getConditionCount();
	}

	public void setAttributes(org.csapi.TpAttribute[] targetAttributes) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS
	{
_delegate.setAttributes(targetAttributes);
	}

	public org.csapi.policy.IpPolicyRepository createRepository(java.lang.String repositoryName) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR
	{
		return _delegate.createRepository(repositoryName);
	}

	public org.csapi.policy.IpPolicyCondition createCondition(java.lang.String conditionName, org.csapi.policy.TpPolicyConditionType conditionType, org.csapi.TpAttribute[] conditionAttributes) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR
	{
		return _delegate.createCondition(conditionName,conditionType,conditionAttributes);
	}

	public org.csapi.policy.IpPolicyAction createAction(java.lang.String actionName, org.csapi.policy.TpPolicyActionType actionType, org.csapi.TpAttribute[] actionAttributes) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR
	{
		return _delegate.createAction(actionName,actionType,actionAttributes);
	}

	public org.csapi.TpAttribute[] getAttributes(java.lang.String[] attributeNames) throws org.csapi.TpCommonExceptions
	{
		return _delegate.getAttributes(attributeNames);
	}

	public org.csapi.policy.IpPolicyIterator getRepositoryIterator() throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION
	{
		return _delegate.getRepositoryIterator();
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

	public org.csapi.policy.IpPolicyAction getAction(java.lang.String actionName) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR
	{
		return _delegate.getAction(actionName);
	}

	public void removeRepository(java.lang.String repositoryName) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR
	{
_delegate.removeRepository(repositoryName);
	}

	public org.csapi.policy.IpPolicyIterator getConditionIterator() throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION
	{
		return _delegate.getConditionIterator();
	}

	public int getRepositoryCount() throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION
	{
		return _delegate.getRepositoryCount();
	}

	public org.csapi.policy.IpPolicyRepository getParentRepository() throws org.csapi.TpCommonExceptions
	{
		return _delegate.getParentRepository();
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

	public org.csapi.policy.IpPolicyRepository getRepository(java.lang.String repositoryName) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR
	{
		return _delegate.getRepository(repositoryName);
	}

}
