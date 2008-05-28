package org.csapi.pam.provisioning;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpPAMCapabilityManagement"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpPAMCapabilityManagementPOATie
	extends IpPAMCapabilityManagementPOA
{
	private IpPAMCapabilityManagementOperations _delegate;

	private POA _poa;
	public IpPAMCapabilityManagementPOATie(IpPAMCapabilityManagementOperations delegate)
	{
		_delegate = delegate;
	}
	public IpPAMCapabilityManagementPOATie(IpPAMCapabilityManagementOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.pam.provisioning.IpPAMCapabilityManagement _this()
	{
		return org.csapi.pam.provisioning.IpPAMCapabilityManagementHelper.narrow(_this_object());
	}
	public org.csapi.pam.provisioning.IpPAMCapabilityManagement _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.pam.provisioning.IpPAMCapabilityManagementHelper.narrow(_this_object(orb));
	}
	public IpPAMCapabilityManagementOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpPAMCapabilityManagementOperations delegate)
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
	public java.lang.String[] listAllCapabilityAttributes(byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_INVALID_CREDENTIAL
	{
		return _delegate.listAllCapabilityAttributes(authToken);
	}

	public void addCapabilityAttributes(java.lang.String capabilityName, java.lang.String[] attributeNames, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_ATTRIBUTE_EXISTS,org.csapi.pam.P_PAM_UNKNOWN_TYPE,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTE
	{
_delegate.addCapabilityAttributes(capabilityName,attributeNames,authToken);
	}

	public void unassignCapabilitiesFromType(java.lang.String agentType, java.lang.String[] capabilities, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_CAPABILITY,org.csapi.pam.P_PAM_UNKNOWN_TYPE,org.csapi.pam.P_PAM_INVALID_CREDENTIAL
	{
_delegate.unassignCapabilitiesFromType(agentType,capabilities,authToken);
	}

	public void deleteCapabilityAttribute(java.lang.String attributeName, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTE
	{
_delegate.deleteCapabilityAttribute(attributeName,authToken);
	}

	public void createCapabilityAttribute(org.csapi.pam.TpPAMAttributeDef pAttribute, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_ATTRIBUTE_EXISTS,org.csapi.pam.P_PAM_INVALID_CREDENTIAL
	{
_delegate.createCapabilityAttribute(pAttribute,authToken);
	}

	public void deleteCapability(java.lang.String capabilityName, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_TYPE,org.csapi.pam.P_PAM_INVALID_CREDENTIAL
	{
_delegate.deleteCapability(capabilityName,authToken);
	}

	public java.lang.String[] listCapabilities(byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_INVALID_CREDENTIAL
	{
		return _delegate.listCapabilities(authToken);
	}

	public void assignCapabilitiesToType(java.lang.String agentType, java.lang.String[] capabilities, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_CAPABILITY,org.csapi.pam.P_PAM_UNKNOWN_TYPE,org.csapi.pam.P_PAM_INVALID_CREDENTIAL
	{
_delegate.assignCapabilitiesToType(agentType,capabilities,authToken);
	}

	public org.csapi.pam.TpPAMAttributeDef getCapabilityAttributeDefinition(java.lang.String attributeName, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTE
	{
		return _delegate.getCapabilityAttributeDefinition(attributeName,authToken);
	}

	public java.lang.String[] listCapabilityAttributes(java.lang.String capabilityName, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_TYPE,org.csapi.pam.P_PAM_INVALID_CREDENTIAL
	{
		return _delegate.listCapabilityAttributes(capabilityName,authToken);
	}

	public void removeCapabilityAttributes(java.lang.String capabilityName, java.lang.String[] attributeNames, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_TYPE,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTE
	{
_delegate.removeCapabilityAttributes(capabilityName,attributeNames,authToken);
	}

	public java.lang.String[] listCapabilitiesOfType(java.lang.String agentType, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_TYPE,org.csapi.pam.P_PAM_INVALID_CREDENTIAL
	{
		return _delegate.listCapabilitiesOfType(agentType,authToken);
	}

	public void createCapability(java.lang.String capabilityName, java.lang.String[] attributeNames, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTE,org.csapi.pam.P_PAM_TYPE_EXISTS
	{
_delegate.createCapability(capabilityName,attributeNames,authToken);
	}

}
