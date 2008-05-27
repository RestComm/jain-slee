package org.csapi.pam.provisioning;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpPAMIdentityTypeManagement"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpPAMIdentityTypeManagementPOATie
	extends IpPAMIdentityTypeManagementPOA
{
	private IpPAMIdentityTypeManagementOperations _delegate;

	private POA _poa;
	public IpPAMIdentityTypeManagementPOATie(IpPAMIdentityTypeManagementOperations delegate)
	{
		_delegate = delegate;
	}
	public IpPAMIdentityTypeManagementPOATie(IpPAMIdentityTypeManagementOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.pam.provisioning.IpPAMIdentityTypeManagement _this()
	{
		return org.csapi.pam.provisioning.IpPAMIdentityTypeManagementHelper.narrow(_this_object());
	}
	public org.csapi.pam.provisioning.IpPAMIdentityTypeManagement _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.pam.provisioning.IpPAMIdentityTypeManagementHelper.narrow(_this_object(orb));
	}
	public IpPAMIdentityTypeManagementOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpPAMIdentityTypeManagementOperations delegate)
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
	public void deleteIdentityAttribute(java.lang.String attributeName, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTE
	{
_delegate.deleteIdentityAttribute(attributeName,authToken);
	}

	public org.csapi.pam.TpPAMAttributeDef getIdentityAttributeDefinition(java.lang.String attributeName, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTE
	{
		return _delegate.getIdentityAttributeDefinition(attributeName,authToken);
	}

	public void removeIdentityTypeAttributes(java.lang.String typeName, java.lang.String[] attributeNames, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_TYPE,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTE
	{
_delegate.removeIdentityTypeAttributes(typeName,attributeNames,authToken);
	}

	public void addIdentityTypeAttributes(java.lang.String typeName, java.lang.String[] attributeNames, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_ATTRIBUTE_EXISTS,org.csapi.pam.P_PAM_UNKNOWN_TYPE,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTE
	{
_delegate.addIdentityTypeAttributes(typeName,attributeNames,authToken);
	}

	public void createIdentityType(java.lang.String typeName, java.lang.String[] attributeNames, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTE,org.csapi.pam.P_PAM_TYPE_EXISTS
	{
_delegate.createIdentityType(typeName,attributeNames,authToken);
	}

	public void createIdentityAttribute(org.csapi.pam.TpPAMAttributeDef pAttribute, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_ATTRIBUTE_EXISTS,org.csapi.pam.P_PAM_INVALID_CREDENTIAL
	{
_delegate.createIdentityAttribute(pAttribute,authToken);
	}

	public java.lang.String[] listIdentityTypes(byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_INVALID_CREDENTIAL
	{
		return _delegate.listIdentityTypes(authToken);
	}

	public void deleteIdentityType(java.lang.String typeName, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_TYPE,org.csapi.pam.P_PAM_INVALID_CREDENTIAL
	{
_delegate.deleteIdentityType(typeName,authToken);
	}

	public java.lang.String[] listIdentityTypeAttributes(java.lang.String typeName, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_TYPE,org.csapi.pam.P_PAM_INVALID_CREDENTIAL
	{
		return _delegate.listIdentityTypeAttributes(typeName,authToken);
	}

	public java.lang.String[] listAllIdentityAttributes(byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_INVALID_CREDENTIAL
	{
		return _delegate.listAllIdentityAttributes(authToken);
	}

}
