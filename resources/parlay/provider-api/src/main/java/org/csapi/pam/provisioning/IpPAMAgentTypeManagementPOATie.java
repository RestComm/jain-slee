package org.csapi.pam.provisioning;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpPAMAgentTypeManagement"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpPAMAgentTypeManagementPOATie
	extends IpPAMAgentTypeManagementPOA
{
	private IpPAMAgentTypeManagementOperations _delegate;

	private POA _poa;
	public IpPAMAgentTypeManagementPOATie(IpPAMAgentTypeManagementOperations delegate)
	{
		_delegate = delegate;
	}
	public IpPAMAgentTypeManagementPOATie(IpPAMAgentTypeManagementOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.pam.provisioning.IpPAMAgentTypeManagement _this()
	{
		return org.csapi.pam.provisioning.IpPAMAgentTypeManagementHelper.narrow(_this_object());
	}
	public org.csapi.pam.provisioning.IpPAMAgentTypeManagement _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.pam.provisioning.IpPAMAgentTypeManagementHelper.narrow(_this_object(orb));
	}
	public IpPAMAgentTypeManagementOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpPAMAgentTypeManagementOperations delegate)
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
	public org.csapi.pam.TpPAMAttributeDef getAgentAttributeDefinition(java.lang.String attributeName, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTE
	{
		return _delegate.getAgentAttributeDefinition(attributeName,authToken);
	}

	public void deleteAgentAttribute(java.lang.String attributeName, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTE
	{
_delegate.deleteAgentAttribute(attributeName,authToken);
	}

	public void addAgentTypeAttributes(java.lang.String typeName, java.lang.String[] attributeNames, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_ATTRIBUTE_EXISTS,org.csapi.pam.P_PAM_UNKNOWN_TYPE,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTE
	{
_delegate.addAgentTypeAttributes(typeName,attributeNames,authToken);
	}

	public java.lang.String[] listAgentTypeAttributes(java.lang.String typeName, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_TYPE,org.csapi.pam.P_PAM_INVALID_CREDENTIAL
	{
		return _delegate.listAgentTypeAttributes(typeName,authToken);
	}

	public java.lang.String[] listAgentTypes(byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_INVALID_CREDENTIAL
	{
		return _delegate.listAgentTypes(authToken);
	}

	public void createAgentType(java.lang.String typeName, java.lang.String[] attributeNames, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTE,org.csapi.pam.P_PAM_TYPE_EXISTS
	{
_delegate.createAgentType(typeName,attributeNames,authToken);
	}

	public void deleteAgentType(java.lang.String typeName, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_TYPE,org.csapi.pam.P_PAM_INVALID_CREDENTIAL
	{
_delegate.deleteAgentType(typeName,authToken);
	}

	public void createAgentAttribute(org.csapi.pam.TpPAMAttributeDef pAttribute, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_ATTRIBUTE_EXISTS,org.csapi.pam.P_PAM_INVALID_CREDENTIAL
	{
_delegate.createAgentAttribute(pAttribute,authToken);
	}

	public java.lang.String[] listAllAgentAttributes(byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_INVALID_CREDENTIAL
	{
		return _delegate.listAllAgentAttributes(authToken);
	}

	public void removeAgentTypeAttributes(java.lang.String typeName, java.lang.String[] attributeNames, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_TYPE,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTE
	{
_delegate.removeAgentTypeAttributes(typeName,attributeNames,authToken);
	}

}
