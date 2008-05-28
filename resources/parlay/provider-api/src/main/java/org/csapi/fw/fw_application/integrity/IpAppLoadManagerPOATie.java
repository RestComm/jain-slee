package org.csapi.fw.fw_application.integrity;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpAppLoadManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpAppLoadManagerPOATie
	extends IpAppLoadManagerPOA
{
	private IpAppLoadManagerOperations _delegate;

	private POA _poa;
	public IpAppLoadManagerPOATie(IpAppLoadManagerOperations delegate)
	{
		_delegate = delegate;
	}
	public IpAppLoadManagerPOATie(IpAppLoadManagerOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.fw.fw_application.integrity.IpAppLoadManager _this()
	{
		return org.csapi.fw.fw_application.integrity.IpAppLoadManagerHelper.narrow(_this_object());
	}
	public org.csapi.fw.fw_application.integrity.IpAppLoadManager _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.fw.fw_application.integrity.IpAppLoadManagerHelper.narrow(_this_object(orb));
	}
	public IpAppLoadManagerOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpAppLoadManagerOperations delegate)
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
	public void loadLevelNotification(org.csapi.fw.TpLoadStatistic[] loadStatistics)
	{
_delegate.loadLevelNotification(loadStatistics);
	}

	public void queryLoadStatsRes(int loadStatsReqID, org.csapi.fw.TpLoadStatistic[] loadStatistics)
	{
_delegate.queryLoadStatsRes(loadStatsReqID,loadStatistics);
	}

	public void queryAppLoadStatsReq(int loadStatsReqID, org.csapi.TpTimeInterval timeInterval)
	{
_delegate.queryAppLoadStatsReq(loadStatsReqID,timeInterval);
	}

	public void createLoadLevelNotification()
	{
_delegate.createLoadLevelNotification();
	}

	public void queryLoadErr(org.csapi.fw.TpLoadStatisticError loadStatisticsError)
	{
_delegate.queryLoadErr(loadStatisticsError);
	}

	public void queryLoadStatsErr(int loadStatsReqID, org.csapi.fw.TpLoadStatisticError loadStatisticsError)
	{
_delegate.queryLoadStatsErr(loadStatsReqID,loadStatisticsError);
	}

	public void resumeNotification()
	{
_delegate.resumeNotification();
	}

	public void suspendNotification()
	{
_delegate.suspendNotification();
	}

	public void queryAppLoadReq(org.csapi.TpTimeInterval timeInterval)
	{
_delegate.queryAppLoadReq(timeInterval);
	}

	public void queryLoadRes(org.csapi.fw.TpLoadStatistic[] loadStatistics)
	{
_delegate.queryLoadRes(loadStatistics);
	}

	public void destroyLoadLevelNotification()
	{
_delegate.destroyLoadLevelNotification();
	}

}
