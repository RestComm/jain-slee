package org.csapi.fw.fw_application.integrity;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpHeartBeat"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpHeartBeatPOATie
	extends IpHeartBeatPOA
{
	private IpHeartBeatOperations _delegate;

	private POA _poa;
	public IpHeartBeatPOATie(IpHeartBeatOperations delegate)
	{
		_delegate = delegate;
	}
	public IpHeartBeatPOATie(IpHeartBeatOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.fw.fw_application.integrity.IpHeartBeat _this()
	{
		return org.csapi.fw.fw_application.integrity.IpHeartBeatHelper.narrow(_this_object());
	}
	public org.csapi.fw.fw_application.integrity.IpHeartBeat _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.fw.fw_application.integrity.IpHeartBeatHelper.narrow(_this_object(orb));
	}
	public IpHeartBeatOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpHeartBeatOperations delegate)
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
	public void pulse() throws org.csapi.TpCommonExceptions
	{
_delegate.pulse();
	}

}
