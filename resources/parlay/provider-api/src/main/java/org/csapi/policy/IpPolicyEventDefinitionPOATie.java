package org.csapi.policy;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpPolicyEventDefinition"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpPolicyEventDefinitionPOATie
	extends IpPolicyEventDefinitionPOA
{
	private IpPolicyEventDefinitionOperations _delegate;

	private POA _poa;
	public IpPolicyEventDefinitionPOATie(IpPolicyEventDefinitionOperations delegate)
	{
		_delegate = delegate;
	}
	public IpPolicyEventDefinitionPOATie(IpPolicyEventDefinitionOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.policy.IpPolicyEventDefinition _this()
	{
		return org.csapi.policy.IpPolicyEventDefinitionHelper.narrow(_this_object());
	}
	public org.csapi.policy.IpPolicyEventDefinition _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.policy.IpPolicyEventDefinitionHelper.narrow(_this_object(orb));
	}
	public IpPolicyEventDefinitionOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpPolicyEventDefinitionOperations delegate)
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

	public void setOptionalAttributes(org.csapi.TpAttribute[] optionalAttributes) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS
	{
_delegate.setOptionalAttributes(optionalAttributes);
	}

	public org.csapi.TpAttribute[] getOptionalAttributes() throws org.csapi.TpCommonExceptions
	{
		return _delegate.getOptionalAttributes();
	}

	public void setAttribute(org.csapi.TpAttribute targetAttribute) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS
	{
_delegate.setAttribute(targetAttribute);
	}

	public org.csapi.TpAttribute[] getRequiredAttributes() throws org.csapi.TpCommonExceptions
	{
		return _delegate.getRequiredAttributes();
	}

	public void setRequiredAttributes(org.csapi.TpAttribute[] requiredAttributes) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_ACCESS_VIOLATION,org.csapi.policy.P_NO_TRANSACTION_IN_PROCESS
	{
_delegate.setRequiredAttributes(requiredAttributes);
	}

	public org.csapi.TpAttribute getAttribute(java.lang.String attributeName) throws org.csapi.TpCommonExceptions,org.csapi.policy.P_NAME_SPACE_ERROR,org.csapi.policy.P_SYNTAX_ERROR
	{
		return _delegate.getAttribute(attributeName);
	}

}
