package org.csapi.policy;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpPolicyEventAction"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpPolicyEventActionPOATie
	extends IpPolicyEventActionPOA
{
	private IpPolicyEventActionOperations _delegate;

	private POA _poa;
	public IpPolicyEventActionPOATie(IpPolicyEventActionOperations delegate)
	{
		_delegate = delegate;
	}
	public IpPolicyEventActionPOATie(IpPolicyEventActionOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.policy.IpPolicyEventAction _this()
	{
		return org.csapi.policy.IpPolicyEventActionHelper.narrow(_this_object());
	}
	public org.csapi.policy.IpPolicyEventAction _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.policy.IpPolicyEventActionHelper.narrow(_this_object(orb));
	}
	public IpPolicyEventActionOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpPolicyEventActionOperations delegate)
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
	public org.csapi.policy.IpPolicyRepository getParentRepository() throws org.csapi.TpCommonExceptions
	{
		return _delegate.getParentRepository();
	}

	public org.csapi.policy.IpPolicyRule getParentRule() throws org.csapi.TpCommonExceptions
	{
		return _delegate.getParentRule();
	}

	public void setAttributes(org.csapi.TpAttribute[] targetAttributes) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS
	{
_delegate.setAttributes(targetAttributes);
	}

	public org.csapi.TpAttribute[] getAttributes(java.lang.String[] attributeNames) throws org.csapi.TpCommonExceptions
	{
		return _delegate.getAttributes(attributeNames);
	}

	public void setAttribute(org.csapi.TpAttribute targetAttribute) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS
	{
_delegate.setAttribute(targetAttribute);
	}

	public org.csapi.TpAttribute getAttribute(java.lang.String attributeName) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR
	{
		return _delegate.getAttribute(attributeName);
	}

}
