package org.csapi.fw.fw_application.integrity;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpAppHeartBeat"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpAppHeartBeatPOATie
	extends IpAppHeartBeatPOA
{
	private IpAppHeartBeatOperations _delegate;

	private POA _poa;
	public IpAppHeartBeatPOATie(IpAppHeartBeatOperations delegate)
	{
		_delegate = delegate;
	}
	public IpAppHeartBeatPOATie(IpAppHeartBeatOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.fw.fw_application.integrity.IpAppHeartBeat _this()
	{
		return org.csapi.fw.fw_application.integrity.IpAppHeartBeatHelper.narrow(_this_object());
	}
	public org.csapi.fw.fw_application.integrity.IpAppHeartBeat _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.fw.fw_application.integrity.IpAppHeartBeatHelper.narrow(_this_object(orb));
	}
	public IpAppHeartBeatOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpAppHeartBeatOperations delegate)
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
	public void pulse()
	{
_delegate.pulse();
	}

}
