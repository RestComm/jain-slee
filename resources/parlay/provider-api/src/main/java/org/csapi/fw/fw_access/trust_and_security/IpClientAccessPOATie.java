package org.csapi.fw.fw_access.trust_and_security;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpClientAccess"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpClientAccessPOATie
	extends IpClientAccessPOA
{
	private IpClientAccessOperations _delegate;

	private POA _poa;
	public IpClientAccessPOATie(IpClientAccessOperations delegate)
	{
		_delegate = delegate;
	}
	public IpClientAccessPOATie(IpClientAccessOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.fw.fw_access.trust_and_security.IpClientAccess _this()
	{
		return org.csapi.fw.fw_access.trust_and_security.IpClientAccessHelper.narrow(_this_object());
	}
	public org.csapi.fw.fw_access.trust_and_security.IpClientAccess _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.fw.fw_access.trust_and_security.IpClientAccessHelper.narrow(_this_object(orb));
	}
	public IpClientAccessOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpClientAccessOperations delegate)
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
	public void terminateAccess(java.lang.String terminationText, java.lang.String signingAlgorithm, byte[] digitalSignature) throws org.csapi.fw.P_INVALID_SIGNING_ALGORITHM,org.csapi.TpCommonExceptions,org.csapi.fw.P_INVALID_SIGNATURE
	{
_delegate.terminateAccess(terminationText,signingAlgorithm,digitalSignature);
	}

}
