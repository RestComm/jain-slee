package org.csapi;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpService"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpServicePOATie
	extends IpServicePOA
{
	private IpServiceOperations _delegate;

	private POA _poa;
	public IpServicePOATie(IpServiceOperations delegate)
	{
		_delegate = delegate;
	}
	public IpServicePOATie(IpServiceOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.IpService _this()
	{
		return org.csapi.IpServiceHelper.narrow(_this_object());
	}
	public org.csapi.IpService _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.IpServiceHelper.narrow(_this_object(orb));
	}
	public IpServiceOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpServiceOperations delegate)
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
	public void setCallbackWithSessionID(org.csapi.IpInterface appInterface, int sessionID) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
_delegate.setCallbackWithSessionID(appInterface,sessionID);
	}

	public void setCallback(org.csapi.IpInterface appInterface) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions
	{
_delegate.setCallback(appInterface);
	}

}
