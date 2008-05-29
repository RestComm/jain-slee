package org.csapi.fw.fw_application.integrity;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpHeartBeatMgmt"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpHeartBeatMgmtPOATie
	extends IpHeartBeatMgmtPOA
{
	private IpHeartBeatMgmtOperations _delegate;

	private POA _poa;
	public IpHeartBeatMgmtPOATie(IpHeartBeatMgmtOperations delegate)
	{
		_delegate = delegate;
	}
	public IpHeartBeatMgmtPOATie(IpHeartBeatMgmtOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.fw.fw_application.integrity.IpHeartBeatMgmt _this()
	{
		return org.csapi.fw.fw_application.integrity.IpHeartBeatMgmtHelper.narrow(_this_object());
	}
	public org.csapi.fw.fw_application.integrity.IpHeartBeatMgmt _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.fw.fw_application.integrity.IpHeartBeatMgmtHelper.narrow(_this_object(orb));
	}
	public IpHeartBeatMgmtOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpHeartBeatMgmtOperations delegate)
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
	public void enableHeartBeat(int interval, org.csapi.fw.fw_application.integrity.IpAppHeartBeat appInterface) throws org.csapi.TpCommonExceptions
	{
_delegate.enableHeartBeat(interval,appInterface);
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
