package org.csapi.fw.fw_application.integrity;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpAppHeartBeatMgmt"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpAppHeartBeatMgmtPOATie
	extends IpAppHeartBeatMgmtPOA
{
	private IpAppHeartBeatMgmtOperations _delegate;

	private POA _poa;
	public IpAppHeartBeatMgmtPOATie(IpAppHeartBeatMgmtOperations delegate)
	{
		_delegate = delegate;
	}
	public IpAppHeartBeatMgmtPOATie(IpAppHeartBeatMgmtOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.fw.fw_application.integrity.IpAppHeartBeatMgmt _this()
	{
		return org.csapi.fw.fw_application.integrity.IpAppHeartBeatMgmtHelper.narrow(_this_object());
	}
	public org.csapi.fw.fw_application.integrity.IpAppHeartBeatMgmt _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.fw.fw_application.integrity.IpAppHeartBeatMgmtHelper.narrow(_this_object(orb));
	}
	public IpAppHeartBeatMgmtOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpAppHeartBeatMgmtOperations delegate)
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
	public void disableAppHeartBeat()
	{
_delegate.disableAppHeartBeat();
	}

	public void enableAppHeartBeat(int interval, org.csapi.fw.fw_application.integrity.IpHeartBeat fwInterface)
	{
_delegate.enableAppHeartBeat(interval,fwInterface);
	}

	public void changeInterval(int interval)
	{
_delegate.changeInterval(interval);
	}

}
