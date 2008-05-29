package org.csapi.pam.provisioning;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpPAMIdentityManagement"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpPAMIdentityManagementPOATie
	extends IpPAMIdentityManagementPOA
{
	private IpPAMIdentityManagementOperations _delegate;

	private POA _poa;
	public IpPAMIdentityManagementPOATie(IpPAMIdentityManagementOperations delegate)
	{
		_delegate = delegate;
	}
	public IpPAMIdentityManagementPOATie(IpPAMIdentityManagementOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.pam.provisioning.IpPAMIdentityManagement _this()
	{
		return org.csapi.pam.provisioning.IpPAMIdentityManagementHelper.narrow(_this_object());
	}
	public org.csapi.pam.provisioning.IpPAMIdentityManagement _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.pam.provisioning.IpPAMIdentityManagementHelper.narrow(_this_object(orb));
	}
	public IpPAMIdentityManagementOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpPAMIdentityManagementOperations delegate)
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
	public void addToGroup(java.lang.String group, java.lang.String member, byte[] authToken) throws org.csapi.pam.P_PAM_UNKNOWN_GROUP,org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_IS_CYCLIC,org.csapi.pam.P_PAM_UNKNOWN_MEMBER,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_MEMBER_EXISTS
	{
_delegate.addToGroup(group,member,authToken);
	}

	public void createIdentity(java.lang.String identity, java.lang.String[] identityTypes, byte[] authToken) throws org.csapi.pam.P_PAM_IDENTITY_EXISTS,org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_TYPE,org.csapi.pam.P_PAM_INVALID_CREDENTIAL
	{
_delegate.createIdentity(identity,identityTypes,authToken);
	}

	public java.lang.String[] listAliases(java.lang.String identity, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_IDENTITY,org.csapi.pam.P_PAM_INVALID_CREDENTIAL
	{
		return _delegate.listAliases(identity,authToken);
	}

	public void setIdentityAttributes(java.lang.String identity, java.lang.String identityType, org.csapi.pam.TpPAMAttribute[] attributes, byte[] authToken) throws org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTES,org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_TYPE,org.csapi.pam.P_PAM_UNKNOWN_IDENTITY,org.csapi.pam.P_PAM_INVALID_CREDENTIAL
	{
_delegate.setIdentityAttributes(identity,identityType,attributes,authToken);
	}

	public java.lang.String[] listMembers(java.lang.String identity, byte[] authToken) throws org.csapi.pam.P_PAM_UNKNOWN_GROUP,org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_INVALID_CREDENTIAL
	{
		return _delegate.listMembers(identity,authToken);
	}

	public void addAlias(java.lang.String identity, java.lang.String alias, byte[] authToken) throws org.csapi.pam.P_PAM_ALIAS_NOT_UNIQUE,org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_IDENTITY,org.csapi.pam.P_PAM_ALIAS_EXISTS,org.csapi.pam.P_PAM_INVALID_CREDENTIAL
	{
_delegate.addAlias(identity,alias,authToken);
	}

	public java.lang.String[] listGroupMembership(java.lang.String identity, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_IDENTITY,org.csapi.pam.P_PAM_INVALID_CREDENTIAL
	{
		return _delegate.listGroupMembership(identity,authToken);
	}

	public void removeAliases(java.lang.String identity, java.lang.String alias, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_IDENTITY,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNASSIGNED_ALIAS
	{
_delegate.removeAliases(identity,alias,authToken);
	}

	public boolean hasType(java.lang.String identity, java.lang.String typeName, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_IDENTITY,org.csapi.pam.P_PAM_INVALID_CREDENTIAL
	{
		return _delegate.hasType(identity,typeName,authToken);
	}

	public void createGroupIdentity(java.lang.String identity, java.lang.String[] identityTypes, byte[] authToken) throws org.csapi.pam.P_PAM_IDENTITY_EXISTS,org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_TYPE,org.csapi.pam.P_PAM_INVALID_CREDENTIAL
	{
_delegate.createGroupIdentity(identity,identityTypes,authToken);
	}

	public org.csapi.pam.TpPAMAttribute[] getIdentityAttributes(java.lang.String identity, java.lang.String identityType, java.lang.String[] attributeNames, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_TYPE,org.csapi.pam.P_PAM_UNKNOWN_IDENTITY,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTE
	{
		return _delegate.getIdentityAttributes(identity,identityType,attributeNames,authToken);
	}

	public java.lang.String lookupByAlias(java.lang.String alias, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_ALIAS
	{
		return _delegate.lookupByAlias(alias,authToken);
	}

	public void disassociateTypes(java.lang.String identity, java.lang.String[] identityTypes, byte[] authToken) throws org.csapi.pam.P_PAM_DISASSOCIATED_TYPE,org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_IDENTITY,org.csapi.pam.P_PAM_INVALID_CREDENTIAL
	{
_delegate.disassociateTypes(identity,identityTypes,authToken);
	}

	public java.lang.String[] listTypesOfIdentity(java.lang.String identity, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_IDENTITY,org.csapi.pam.P_PAM_INVALID_CREDENTIAL
	{
		return _delegate.listTypesOfIdentity(identity,authToken);
	}

	public boolean isIdentity(java.lang.String identity, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_INVALID_CREDENTIAL
	{
		return _delegate.isIdentity(identity,authToken);
	}

	public void deleteGroupIdentity(java.lang.String identity, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_IDENTITY,org.csapi.pam.P_PAM_INVALID_CREDENTIAL
	{
_delegate.deleteGroupIdentity(identity,authToken);
	}

	public void deleteIdentity(java.lang.String identity, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_IDENTITY,org.csapi.pam.P_PAM_INVALID_CREDENTIAL
	{
_delegate.deleteIdentity(identity,authToken);
	}

	public void removeFromGroup(java.lang.String group, java.lang.String identity, byte[] authToken) throws org.csapi.pam.P_PAM_UNKNOWN_GROUP,org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_MEMBER,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_NOT_MEMBER
	{
_delegate.removeFromGroup(group,identity,authToken);
	}

	public boolean isGroupIdentity(java.lang.String identity, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_INVALID_CREDENTIAL
	{
		return _delegate.isGroupIdentity(identity,authToken);
	}

	public void associateTypes(java.lang.String identity, java.lang.String[] identityTypes, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_TYPE_ASSOCIATED,org.csapi.pam.P_PAM_UNKNOWN_TYPE,org.csapi.pam.P_PAM_UNKNOWN_IDENTITY,org.csapi.pam.P_PAM_INVALID_CREDENTIAL
	{
_delegate.associateTypes(identity,identityTypes,authToken);
	}

}
