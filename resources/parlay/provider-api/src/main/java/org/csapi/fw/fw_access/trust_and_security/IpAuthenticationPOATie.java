package org.csapi.fw.fw_access.trust_and_security;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpAuthentication"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpAuthenticationPOATie
	extends IpAuthenticationPOA
{
	private IpAuthenticationOperations _delegate;

	private POA _poa;
	public IpAuthenticationPOATie(IpAuthenticationOperations delegate)
	{
		_delegate = delegate;
	}
	public IpAuthenticationPOATie(IpAuthenticationOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.fw.fw_access.trust_and_security.IpAuthentication _this()
	{
		return org.csapi.fw.fw_access.trust_and_security.IpAuthenticationHelper.narrow(_this_object());
	}
	public org.csapi.fw.fw_access.trust_and_security.IpAuthentication _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.fw.fw_access.trust_and_security.IpAuthenticationHelper.narrow(_this_object(orb));
	}
	public IpAuthenticationOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpAuthenticationOperations delegate)
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
	public org.csapi.IpInterface requestAccess(java.lang.String accessType, org.csapi.IpInterface clientAccessInterface) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions,org.csapi.fw.P_INVALID_ACCESS_TYPE,org.csapi.fw.P_ACCESS_DENIED
	{
		return _delegate.requestAccess(accessType,clientAccessInterface);
	}

}
