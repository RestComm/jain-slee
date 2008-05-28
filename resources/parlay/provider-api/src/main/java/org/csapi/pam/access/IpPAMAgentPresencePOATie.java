package org.csapi.pam.access;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpPAMAgentPresence"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpPAMAgentPresencePOATie
	extends IpPAMAgentPresencePOA
{
	private IpPAMAgentPresenceOperations _delegate;

	private POA _poa;
	public IpPAMAgentPresencePOATie(IpPAMAgentPresenceOperations delegate)
	{
		_delegate = delegate;
	}
	public IpPAMAgentPresencePOATie(IpPAMAgentPresenceOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.pam.access.IpPAMAgentPresence _this()
	{
		return org.csapi.pam.access.IpPAMAgentPresenceHelper.narrow(_this_object());
	}
	public org.csapi.pam.access.IpPAMAgentPresence _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.pam.access.IpPAMAgentPresenceHelper.narrow(_this_object(orb));
	}
	public IpPAMAgentPresenceOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpPAMAgentPresenceOperations delegate)
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
	public org.csapi.pam.TpPAMAttribute[] getAgentPresence(java.lang.String agent, java.lang.String agentType, java.lang.String[] attributeNames, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_TYPE,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTE,org.csapi.pam.P_PAM_UNKNOWN_AGENT
	{
		return _delegate.getAgentPresence(agent,agentType,attributeNames,authToken);
	}

	public void setAgentPresence(java.lang.String agent, java.lang.String agentType, org.csapi.pam.TpPAMAttribute[] attributes, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_TYPE,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTE,org.csapi.pam.P_PAM_UNKNOWN_AGENT
	{
_delegate.setAgentPresence(agent,agentType,attributes,authToken);
	}

	public void setAgentPresenceExpiration(java.lang.String agent, java.lang.String agentType, java.lang.String[] attributeNames, long expiresIn, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_TYPE,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTE,org.csapi.pam.P_PAM_UNKNOWN_AGENT
	{
_delegate.setAgentPresenceExpiration(agent,agentType,attributeNames,expiresIn,authToken);
	}

	public org.csapi.pam.TpPAMAttribute[] getCapabilityPresence(java.lang.String agent, java.lang.String capability, java.lang.String[] attributeNames, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_CAPABILITY,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTE,org.csapi.pam.P_PAM_UNKNOWN_AGENT
	{
		return _delegate.getCapabilityPresence(agent,capability,attributeNames,authToken);
	}

	public void setCapabilityPresence(java.lang.String agent, java.lang.String capability, org.csapi.pam.TpPAMAttribute[] attributes, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_CAPABILITY,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTE,org.csapi.pam.P_PAM_UNKNOWN_AGENT
	{
_delegate.setCapabilityPresence(agent,capability,attributes,authToken);
	}

	public void setCapabilityPresenceExpiration(java.lang.String agent, java.lang.String capability, java.lang.String[] attributeNames, long expiresIn, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_NO_CAPABILITY,org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTE,org.csapi.pam.P_PAM_UNKNOWN_AGENT
	{
_delegate.setCapabilityPresenceExpiration(agent,capability,attributeNames,expiresIn,authToken);
	}

}
