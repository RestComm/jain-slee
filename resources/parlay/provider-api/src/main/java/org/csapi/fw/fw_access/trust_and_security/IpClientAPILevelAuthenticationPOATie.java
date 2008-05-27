package org.csapi.fw.fw_access.trust_and_security;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpClientAPILevelAuthentication"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpClientAPILevelAuthenticationPOATie
	extends IpClientAPILevelAuthenticationPOA
{
	private IpClientAPILevelAuthenticationOperations _delegate;

	private POA _poa;
	public IpClientAPILevelAuthenticationPOATie(IpClientAPILevelAuthenticationOperations delegate)
	{
		_delegate = delegate;
	}
	public IpClientAPILevelAuthenticationPOATie(IpClientAPILevelAuthenticationOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.fw.fw_access.trust_and_security.IpClientAPILevelAuthentication _this()
	{
		return org.csapi.fw.fw_access.trust_and_security.IpClientAPILevelAuthenticationHelper.narrow(_this_object());
	}
	public org.csapi.fw.fw_access.trust_and_security.IpClientAPILevelAuthentication _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.fw.fw_access.trust_and_security.IpClientAPILevelAuthenticationHelper.narrow(_this_object(orb));
	}
	public IpClientAPILevelAuthenticationOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpClientAPILevelAuthenticationOperations delegate)
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
	public byte[] challenge(byte[] challenge)
	{
		return _delegate.challenge(challenge);
	}

	public void authenticationSucceeded()
	{
_delegate.authenticationSucceeded();
	}

	public byte[] authenticate(byte[] challenge)
	{
		return _delegate.authenticate(challenge);
	}

	public void abortAuthentication()
	{
_delegate.abortAuthentication();
	}

}
