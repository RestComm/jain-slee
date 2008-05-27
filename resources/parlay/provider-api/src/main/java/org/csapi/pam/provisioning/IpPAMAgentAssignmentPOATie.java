package org.csapi.pam.provisioning;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpPAMAgentAssignment"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpPAMAgentAssignmentPOATie
	extends IpPAMAgentAssignmentPOA
{
	private IpPAMAgentAssignmentOperations _delegate;

	private POA _poa;
	public IpPAMAgentAssignmentPOATie(IpPAMAgentAssignmentOperations delegate)
	{
		_delegate = delegate;
	}
	public IpPAMAgentAssignmentPOATie(IpPAMAgentAssignmentOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.pam.provisioning.IpPAMAgentAssignment _this()
	{
		return org.csapi.pam.provisioning.IpPAMAgentAssignmentHelper.narrow(_this_object());
	}
	public org.csapi.pam.provisioning.IpPAMAgentAssignment _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.pam.provisioning.IpPAMAgentAssignmentHelper.narrow(_this_object(orb));
	}
	public IpPAMAgentAssignmentOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpPAMAgentAssignmentOperations delegate)
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
	public java.lang.String[] listAssignedAgentsByCapability(java.lang.String identity, java.lang.String capability, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_IDENTITY,org.csapi.pam.P_PAM_INVALID_CREDENTIAL
	{
		return _delegate.listAssignedAgentsByCapability(identity,capability,authToken);
	}

	public void unassignAgent(java.lang.String identity, java.lang.String agentName, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_IDENTITY,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_ASSIGNMENT,org.csapi.pam.P_PAM_UNKNOWN_AGENT
	{
_delegate.unassignAgent(identity,agentName,authToken);
	}

	public java.lang.String[] listCapabilitiesOfIdentity(java.lang.String identity, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_IDENTITY,org.csapi.pam.P_PAM_INVALID_CREDENTIAL
	{
		return _delegate.listCapabilitiesOfIdentity(identity,authToken);
	}

	public void assignAgent(java.lang.String identity, java.lang.String agentName, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_IDENTITY,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_AGENT
	{
_delegate.assignAgent(identity,agentName,authToken);
	}

	public java.lang.String[] listAssignedAgents(java.lang.String identity, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_IDENTITY,org.csapi.pam.P_PAM_INVALID_CREDENTIAL
	{
		return _delegate.listAssignedAgents(identity,authToken);
	}

	public boolean isIdentityCapableOf(java.lang.String identity, java.lang.String capability, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_IDENTITY,org.csapi.pam.P_PAM_INVALID_CREDENTIAL
	{
		return _delegate.isIdentityCapableOf(identity,capability,authToken);
	}

	public java.lang.String[] listAssociatedIdentitiesOfAgent(java.lang.String agentName, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_AGENT
	{
		return _delegate.listAssociatedIdentitiesOfAgent(agentName,authToken);
	}

}
