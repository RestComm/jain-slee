package org.csapi.fw.fw_access.trust_and_security;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpAPILevelAuthentication"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpAPILevelAuthenticationPOATie
	extends IpAPILevelAuthenticationPOA
{
	private IpAPILevelAuthenticationOperations _delegate;

	private POA _poa;
	public IpAPILevelAuthenticationPOATie(IpAPILevelAuthenticationOperations delegate)
	{
		_delegate = delegate;
	}
	public IpAPILevelAuthenticationPOATie(IpAPILevelAuthenticationOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.fw.fw_access.trust_and_security.IpAPILevelAuthentication _this()
	{
		return org.csapi.fw.fw_access.trust_and_security.IpAPILevelAuthenticationHelper.narrow(_this_object());
	}
	public org.csapi.fw.fw_access.trust_and_security.IpAPILevelAuthentication _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.fw.fw_access.trust_and_security.IpAPILevelAuthenticationHelper.narrow(_this_object(orb));
	}
	public IpAPILevelAuthenticationOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpAPILevelAuthenticationOperations delegate)
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
	public java.lang.String selectEncryptionMethod(java.lang.String encryptionCaps) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_NO_ACCEPTABLE_ENCRYPTION_CAPABILITY,org.csapi.fw.P_ACCESS_DENIED
	{
		return _delegate.selectEncryptionMethod(encryptionCaps);
	}

	public byte[] challenge(byte[] challenge) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED
	{
		return _delegate.challenge(challenge);
	}

	public org.csapi.IpInterface requestAccess(java.lang.String accessType, org.csapi.IpInterface clientAccessInterface) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions,org.csapi.fw.P_INVALID_ACCESS_TYPE,org.csapi.fw.P_ACCESS_DENIED
	{
		return _delegate.requestAccess(accessType,clientAccessInterface);
	}

	public void authenticationSucceeded() throws org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED
	{
_delegate.authenticationSucceeded();
	}

	public byte[] authenticate(byte[] challenge) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED
	{
		return _delegate.authenticate(challenge);
	}

	public void abortAuthentication() throws org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED
	{
_delegate.abortAuthentication();
	}

	public java.lang.String selectAuthenticationMechanism(java.lang.String authMechanismList) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_NO_ACCEPTABLE_AUTHENTICATION_MECHANISM,org.csapi.fw.P_ACCESS_DENIED
	{
		return _delegate.selectAuthenticationMechanism(authMechanismList);
	}

}
