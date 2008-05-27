package org.csapi.pam.provisioning;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpPAMAgentManagement"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpPAMAgentManagementPOATie
	extends IpPAMAgentManagementPOA
{
	private IpPAMAgentManagementOperations _delegate;

	private POA _poa;
	public IpPAMAgentManagementPOATie(IpPAMAgentManagementOperations delegate)
	{
		_delegate = delegate;
	}
	public IpPAMAgentManagementPOATie(IpPAMAgentManagementOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.pam.provisioning.IpPAMAgentManagement _this()
	{
		return org.csapi.pam.provisioning.IpPAMAgentManagementHelper.narrow(_this_object());
	}
	public org.csapi.pam.provisioning.IpPAMAgentManagement _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.pam.provisioning.IpPAMAgentManagementHelper.narrow(_this_object(orb));
	}
	public IpPAMAgentManagementOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpPAMAgentManagementOperations delegate)
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
	public void setAgentAttributes(java.lang.String agentName, java.lang.String agentType, org.csapi.pam.TpPAMAttribute[] attributes, byte[] authToken) throws org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTES,org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_TYPE,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_AGENT
	{
_delegate.setAgentAttributes(agentName,agentType,attributes,authToken);
	}

	public boolean hasType(java.lang.String agentName, java.lang.String typeName, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_AGENT
	{
		return _delegate.hasType(agentName,typeName,authToken);
	}

	public void disableCapabilities(java.lang.String agentName, java.lang.String[] capabilities, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_NO_CAPABILITY,org.csapi.pam.P_PAM_UNKNOWN_AGENT
	{
_delegate.disableCapabilities(agentName,capabilities,authToken);
	}

	public boolean isAgent(java.lang.String agentName, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_INVALID_CREDENTIAL
	{
		return _delegate.isAgent(agentName,authToken);
	}

	public void disassociateTypes(java.lang.String agentName, java.lang.String[] agentTypes, byte[] authToken) throws org.csapi.pam.P_PAM_DISASSOCIATED_TYPE,org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_AGENT
	{
_delegate.disassociateTypes(agentName,agentTypes,authToken);
	}

	public java.lang.String[] listTypesOfAgent(java.lang.String agentName, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_AGENT
	{
		return _delegate.listTypesOfAgent(agentName,authToken);
	}

	public void associateTypes(java.lang.String agentName, java.lang.String[] agentTypes, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_TYPE_ASSOCIATED,org.csapi.pam.P_PAM_UNKNOWN_TYPE,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_AGENT
	{
_delegate.associateTypes(agentName,agentTypes,authToken);
	}

	public org.csapi.pam.TpPAMAttribute[] getAgentAttributes(java.lang.String agentName, java.lang.String agentType, java.lang.String[] attributeNames, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_TYPE,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTE,org.csapi.pam.P_PAM_UNKNOWN_AGENT
	{
		return _delegate.getAgentAttributes(agentName,agentType,attributeNames,authToken);
	}

	public java.lang.String[] listAllCapabilities(java.lang.String agentName, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_AGENT
	{
		return _delegate.listAllCapabilities(agentName,authToken);
	}

	public void createAgent(java.lang.String agentName, java.lang.String[] agentTypes, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_TYPE,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_AGENT_EXISTS
	{
_delegate.createAgent(agentName,agentTypes,authToken);
	}

	public void enableCapabilities(java.lang.String agentName, java.lang.String[] capabilities, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_AGENT
	{
_delegate.enableCapabilities(agentName,capabilities,authToken);
	}

	public void deleteAgent(java.lang.String agentName, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_AGENT
	{
_delegate.deleteAgent(agentName,authToken);
	}

	public java.lang.String[] listEnabledCapabilities(java.lang.String agentName, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_AGENT
	{
		return _delegate.listEnabledCapabilities(agentName,authToken);
	}

	public boolean isCapableOf(java.lang.String agentName, java.lang.String capability, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_AGENT
	{
		return _delegate.isCapableOf(agentName,capability,authToken);
	}

}
