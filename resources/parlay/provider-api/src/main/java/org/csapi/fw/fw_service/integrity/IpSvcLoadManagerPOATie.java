package org.csapi.fw.fw_service.integrity;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpSvcLoadManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpSvcLoadManagerPOATie
	extends IpSvcLoadManagerPOA
{
	private IpSvcLoadManagerOperations _delegate;

	private POA _poa;
	public IpSvcLoadManagerPOATie(IpSvcLoadManagerOperations delegate)
	{
		_delegate = delegate;
	}
	public IpSvcLoadManagerPOATie(IpSvcLoadManagerOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.fw.fw_service.integrity.IpSvcLoadManager _this()
	{
		return org.csapi.fw.fw_service.integrity.IpSvcLoadManagerHelper.narrow(_this_object());
	}
	public org.csapi.fw.fw_service.integrity.IpSvcLoadManager _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.fw.fw_service.integrity.IpSvcLoadManagerHelper.narrow(_this_object(orb));
	}
	public IpSvcLoadManagerOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpSvcLoadManagerOperations delegate)
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
	public void querySvcLoadReq(org.csapi.TpTimeInterval timeInterval) throws org.csapi.TpCommonExceptions
	{
_delegate.querySvcLoadReq(timeInterval);
	}

	public void querySvcLoadStatsReq(int loadStatsReqID, org.csapi.TpTimeInterval timeInterval) throws org.csapi.TpCommonExceptions
	{
_delegate.querySvcLoadStatsReq(loadStatsReqID,timeInterval);
	}

	public void loadLevelNotification(org.csapi.fw.TpLoadStatistic[] loadStatistics) throws org.csapi.TpCommonExceptions
	{
_delegate.loadLevelNotification(loadStatistics);
	}

	public void queryLoadStatsRes(int loadStatsReqID, org.csapi.fw.TpLoadStatistic[] loadStatistics) throws org.csapi.TpCommonExceptions
	{
_delegate.queryLoadStatsRes(loadStatsReqID,loadStatistics);
	}

	public void createLoadLevelNotification() throws org.csapi.TpCommonExceptions
	{
_delegate.createLoadLevelNotification();
	}

	public void queryLoadErr(org.csapi.fw.TpLoadStatisticError loadStatisticsError) throws org.csapi.TpCommonExceptions
	{
_delegate.queryLoadErr(loadStatisticsError);
	}

	public void queryLoadStatsErr(int loadStatsReqID, org.csapi.fw.TpLoadStatisticError loadStatisticsError) throws org.csapi.TpCommonExceptions
	{
_delegate.queryLoadStatsErr(loadStatsReqID,loadStatisticsError);
	}

	public void resumeNotification() throws org.csapi.TpCommonExceptions
	{
_delegate.resumeNotification();
	}

	public void suspendNotification() throws org.csapi.TpCommonExceptions
	{
_delegate.suspendNotification();
	}

	public void queryLoadRes(org.csapi.fw.TpLoadStatistic[] loadStatistics) throws org.csapi.TpCommonExceptions
	{
_delegate.queryLoadRes(loadStatistics);
	}

	public void destroyLoadLevelNotification() throws org.csapi.TpCommonExceptions
	{
_delegate.destroyLoadLevelNotification();
	}

}
