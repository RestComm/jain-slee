package org.csapi.policy;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpPolicyGroup"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpPolicyGroupPOATie
	extends IpPolicyGroupPOA
{
	private IpPolicyGroupOperations _delegate;

	private POA _poa;
	public IpPolicyGroupPOATie(IpPolicyGroupOperations delegate)
	{
		_delegate = delegate;
	}
	public IpPolicyGroupPOATie(IpPolicyGroupOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.policy.IpPolicyGroup _this()
	{
		return org.csapi.policy.IpPolicyGroupHelper.narrow(_this_object());
	}
	public org.csapi.policy.IpPolicyGroup _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.policy.IpPolicyGroupHelper.narrow(_this_object(orb));
	}
	public IpPolicyGroupOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpPolicyGroupOperations delegate)
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
	public org.csapi.TpAttribute[] getAttributes(java.lang.String[] attributeNames) throws org.csapi.TpCommonExceptions
	{
		return _delegate.getAttributes(attributeNames);
	}

	public org.csapi.policy.IpPolicyDomain getParentDomain() throws org.csapi.TpCommonExceptions
	{
		return _delegate.getParentDomain();
	}

	public void setAttributes(org.csapi.TpAttribute[] targetAttributes) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS
	{
_delegate.setAttributes(targetAttributes);
	}

	public void removeRule(java.lang.String ruleName) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR
	{
_delegate.removeRule(ruleName);
	}

	public int getGroupCount() throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION
	{
		return _delegate.getGroupCount();
	}

	public org.csapi.policy.IpPolicyGroup createGroup(java.lang.String groupName) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR
	{
		return _delegate.createGroup(groupName);
	}

	public org.csapi.policy.IpPolicyGroup getGroup(java.lang.String groupName) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR
	{
		return _delegate.getGroup(groupName);
	}

	public void removeGroup(java.lang.String groupName) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR
	{
_delegate.removeGroup(groupName);
	}

	public org.csapi.policy.IpPolicyGroup getParentGroup() throws org.csapi.TpCommonExceptions
	{
		return _delegate.getParentGroup();
	}

	public org.csapi.policy.IpPolicyRule createRule(java.lang.String ruleName) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR
	{
		return _delegate.createRule(ruleName);
	}

	public void setAttribute(org.csapi.TpAttribute targetAttribute) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS
	{
_delegate.setAttribute(targetAttribute);
	}

	public org.csapi.policy.IpPolicyIterator getRuleIterator() throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION
	{
		return _delegate.getRuleIterator();
	}

	public int getRuleCount() throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION
	{
		return _delegate.getRuleCount();
	}

	public org.csapi.policy.IpPolicyRule getRule(java.lang.String ruleName) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR
	{
		return _delegate.getRule(ruleName);
	}

	public org.csapi.policy.IpPolicyIterator getGroupIterator() throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION
	{
		return _delegate.getGroupIterator();
	}

	public org.csapi.TpAttribute getAttribute(java.lang.String attributeName) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR
	{
		return _delegate.getAttribute(attributeName);
	}

}
