package org.csapi.fw.fw_service.integrity;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpFwLoadManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpFwLoadManagerPOATie
	extends IpFwLoadManagerPOA
{
	private IpFwLoadManagerOperations _delegate;

	private POA _poa;
	public IpFwLoadManagerPOATie(IpFwLoadManagerOperations delegate)
	{
		_delegate = delegate;
	}
	public IpFwLoadManagerPOATie(IpFwLoadManagerOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.fw.fw_service.integrity.IpFwLoadManager _this()
	{
		return org.csapi.fw.fw_service.integrity.IpFwLoadManagerHelper.narrow(_this_object());
	}
	public org.csapi.fw.fw_service.integrity.IpFwLoadManager _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.fw.fw_service.integrity.IpFwLoadManagerHelper.narrow(_this_object(orb));
	}
	public IpFwLoadManagerOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpFwLoadManagerOperations delegate)
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
	public void createLoadLevelNotification(org.csapi.fw.TpSubjectType notificationSubject) throws org.csapi.TpCommonExceptions
	{
_delegate.createLoadLevelNotification(notificationSubject);
	}

	public void querySvcLoadRes(org.csapi.fw.TpLoadStatistic[] loadStatistics) throws org.csapi.TpCommonExceptions
	{
_delegate.querySvcLoadRes(loadStatistics);
	}

	public void querySvcLoadErr(org.csapi.fw.TpLoadStatisticError loadStatisticError) throws org.csapi.TpCommonExceptions
	{
_delegate.querySvcLoadErr(loadStatisticError);
	}

	public void querySvcLoadStatsRes(int loadStatsReqID, org.csapi.fw.TpLoadStatistic[] loadStatistics) throws org.csapi.TpCommonExceptions
	{
_delegate.querySvcLoadStatsRes(loadStatsReqID,loadStatistics);
	}

	public void queryLoadReq(org.csapi.fw.TpSubjectType querySubject, org.csapi.TpTimeInterval timeInterval) throws org.csapi.TpCommonExceptions
	{
_delegate.queryLoadReq(querySubject,timeInterval);
	}

	public void querySvcLoadStatsErr(int loadStatsReqID, org.csapi.fw.TpLoadStatisticError loadStatisticError) throws org.csapi.TpCommonExceptions
	{
_delegate.querySvcLoadStatsErr(loadStatsReqID,loadStatisticError);
	}

	public void suspendNotification(org.csapi.fw.TpSubjectType notificationSubject) throws org.csapi.TpCommonExceptions
	{
_delegate.suspendNotification(notificationSubject);
	}

	public void resumeNotification(org.csapi.fw.TpSubjectType notificationSubject) throws org.csapi.TpCommonExceptions
	{
_delegate.resumeNotification(notificationSubject);
	}

	public void reportLoad(org.csapi.fw.TpLoadLevel loadLevel) throws org.csapi.TpCommonExceptions
	{
_delegate.reportLoad(loadLevel);
	}

	public void queryLoadStatsReq(int loadStatsReqID, org.csapi.fw.TpSubjectType querySubject, org.csapi.TpTimeInterval timeInterval) throws org.csapi.TpCommonExceptions
	{
_delegate.queryLoadStatsReq(loadStatsReqID,querySubject,timeInterval);
	}

	public void destroyLoadLevelNotification(org.csapi.fw.TpSubjectType notificationSubject) throws org.csapi.TpCommonExceptions
	{
_delegate.destroyLoadLevelNotification(notificationSubject);
	}

}
