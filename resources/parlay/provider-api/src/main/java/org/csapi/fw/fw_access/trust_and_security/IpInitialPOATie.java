package org.csapi.fw.fw_access.trust_and_security;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpInitial"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpInitialPOATie
	extends IpInitialPOA
{
	private IpInitialOperations _delegate;

	private POA _poa;
	public IpInitialPOATie(IpInitialOperations delegate)
	{
		_delegate = delegate;
	}
	public IpInitialPOATie(IpInitialOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.fw.fw_access.trust_and_security.IpInitial _this()
	{
		return org.csapi.fw.fw_access.trust_and_security.IpInitialHelper.narrow(_this_object());
	}
	public org.csapi.fw.fw_access.trust_and_security.IpInitial _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.fw.fw_access.trust_and_security.IpInitialHelper.narrow(_this_object(orb));
	}
	public IpInitialOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpInitialOperations delegate)
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
	public org.csapi.fw.TpAuthDomain initiateAuthenticationWithVersion(org.csapi.fw.TpAuthDomain clientDomain, java.lang.String authType, java.lang.String frameworkVersion) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_VERSION,org.csapi.fw.P_INVALID_AUTH_TYPE,org.csapi.fw.P_INVALID_DOMAIN_ID
	{
		return _delegate.initiateAuthenticationWithVersion(clientDomain,authType,frameworkVersion);
	}

	public org.csapi.fw.TpAuthDomain initiateAuthentication(org.csapi.fw.TpAuthDomain clientDomain, java.lang.String authType) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions,org.csapi.fw.P_INVALID_AUTH_TYPE,org.csapi.fw.P_INVALID_DOMAIN_ID
	{
		return _delegate.initiateAuthentication(clientDomain,authType);
	}

}
