package org.csapi.fw.fw_service.integrity;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpSvcHeartBeat"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpSvcHeartBeatPOATie
	extends IpSvcHeartBeatPOA
{
	private IpSvcHeartBeatOperations _delegate;

	private POA _poa;
	public IpSvcHeartBeatPOATie(IpSvcHeartBeatOperations delegate)
	{
		_delegate = delegate;
	}
	public IpSvcHeartBeatPOATie(IpSvcHeartBeatOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.fw.fw_service.integrity.IpSvcHeartBeat _this()
	{
		return org.csapi.fw.fw_service.integrity.IpSvcHeartBeatHelper.narrow(_this_object());
	}
	public org.csapi.fw.fw_service.integrity.IpSvcHeartBeat _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.fw.fw_service.integrity.IpSvcHeartBeatHelper.narrow(_this_object(orb));
	}
	public IpSvcHeartBeatOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpSvcHeartBeatOperations delegate)
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
