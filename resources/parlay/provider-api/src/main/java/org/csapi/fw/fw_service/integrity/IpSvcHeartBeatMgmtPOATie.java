package org.csapi.fw.fw_service.integrity;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpSvcHeartBeatMgmt"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpSvcHeartBeatMgmtPOATie
	extends IpSvcHeartBeatMgmtPOA
{
	private IpSvcHeartBeatMgmtOperations _delegate;

	private POA _poa;
	public IpSvcHeartBeatMgmtPOATie(IpSvcHeartBeatMgmtOperations delegate)
	{
		_delegate = delegate;
	}
	public IpSvcHeartBeatMgmtPOATie(IpSvcHeartBeatMgmtOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.fw.fw_service.integrity.IpSvcHeartBeatMgmt _this()
	{
		return org.csapi.fw.fw_service.integrity.IpSvcHeartBeatMgmtHelper.narrow(_this_object());
	}
	public org.csapi.fw.fw_service.integrity.IpSvcHeartBeatMgmt _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.fw.fw_service.integrity.IpSvcHeartBeatMgmtHelper.narrow(_this_object(orb));
	}
	public IpSvcHeartBeatMgmtOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpSvcHeartBeatMgmtOperations delegate)
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
	public void disableSvcHeartBeat() throws org.csapi.TpCommonExceptions
	{
_delegate.disableSvcHeartBeat();
	}

	public void enableSvcHeartBeat(int interval, org.csapi.fw.fw_service.integrity.IpFwHeartBeat fwInterface) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions
	{
_delegate.enableSvcHeartBeat(interval,fwInterface);
	}

	public void changeInterval(int interval) throws org.csapi.TpCommonExceptions
	{
_delegate.changeInterval(interval);
	}

}
