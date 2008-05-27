package org.csapi.pam.access;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpPAMIdentityPresence"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpPAMIdentityPresencePOATie
	extends IpPAMIdentityPresencePOA
{
	private IpPAMIdentityPresenceOperations _delegate;

	private POA _poa;
	public IpPAMIdentityPresencePOATie(IpPAMIdentityPresenceOperations delegate)
	{
		_delegate = delegate;
	}
	public IpPAMIdentityPresencePOATie(IpPAMIdentityPresenceOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.pam.access.IpPAMIdentityPresence _this()
	{
		return org.csapi.pam.access.IpPAMIdentityPresenceHelper.narrow(_this_object());
	}
	public org.csapi.pam.access.IpPAMIdentityPresence _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.pam.access.IpPAMIdentityPresenceHelper.narrow(_this_object(orb));
	}
	public IpPAMIdentityPresenceOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpPAMIdentityPresenceOperations delegate)
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
	public void setIdentityPresence(java.lang.String identity, java.lang.String identityType, org.csapi.pam.TpPAMAttribute[] attributes, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_TYPE,org.csapi.pam.P_PAM_UNKNOWN_IDENTITY,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTE
	{
_delegate.setIdentityPresence(identity,identityType,attributes,authToken);
	}

	public org.csapi.pam.TpPAMAttribute[] getIdentityPresence(java.lang.String identity, java.lang.String identityType, java.lang.String[] attributeNames, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_TYPE,org.csapi.pam.P_PAM_UNKNOWN_IDENTITY,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTE
	{
		return _delegate.getIdentityPresence(identity,identityType,attributeNames,authToken);
	}

	public void setIdentityPresenceExpiration(java.lang.String identity, java.lang.String identityType, java.lang.String[] attributeNames, long expiresIn, byte[] authToken) throws org.csapi.TpCommonExceptions,org.csapi.pam.P_PAM_UNKNOWN_TYPE,org.csapi.pam.P_PAM_UNKNOWN_IDENTITY,org.csapi.pam.P_PAM_INVALID_CREDENTIAL,org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTE
	{
_delegate.setIdentityPresenceExpiration(identity,identityType,attributeNames,expiresIn,authToken);
	}

}
