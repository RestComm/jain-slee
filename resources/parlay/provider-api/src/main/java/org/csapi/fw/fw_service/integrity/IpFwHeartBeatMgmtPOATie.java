package org.csapi.fw.fw_service.integrity;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpFwHeartBeatMgmt"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpFwHeartBeatMgmtPOATie
	extends IpFwHeartBeatMgmtPOA
{
	private IpFwHeartBeatMgmtOperations _delegate;

	private POA _poa;
	public IpFwHeartBeatMgmtPOATie(IpFwHeartBeatMgmtOperations delegate)
	{
		_delegate = delegate;
	}
	public IpFwHeartBeatMgmtPOATie(IpFwHeartBeatMgmtOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.fw.fw_service.integrity.IpFwHeartBeatMgmt _this()
	{
		return org.csapi.fw.fw_service.integrity.IpFwHeartBeatMgmtHelper.narrow(_this_object());
	}
	public org.csapi.fw.fw_service.integrity.IpFwHeartBeatMgmt _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.fw.fw_service.integrity.IpFwHeartBeatMgmtHelper.narrow(_this_object(orb));
	}
	public IpFwHeartBeatMgmtOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpFwHeartBeatMgmtOperations delegate)
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
	public void enableHeartBeat(int interval, org.csapi.fw.fw_service.integrity.IpSvcHeartBeat svcInterface) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions
	{
_delegate.enableHeartBeat(interval,svcInterface);
	}

	public void disableHeartBeat() throws org.csapi.TpCommonExceptions
	{
_delegate.disableHeartBeat();
	}

	public void changeInterval(int interval) throws org.csapi.TpCommonExceptions
	{
_delegate.changeInterval(interval);
	}

}
