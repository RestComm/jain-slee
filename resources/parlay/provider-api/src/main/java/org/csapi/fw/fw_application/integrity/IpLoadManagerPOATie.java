package org.csapi.fw.fw_application.integrity;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpLoadManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpLoadManagerPOATie
	extends IpLoadManagerPOA
{
	private IpLoadManagerOperations _delegate;

	private POA _poa;
	public IpLoadManagerPOATie(IpLoadManagerOperations delegate)
	{
		_delegate = delegate;
	}
	public IpLoadManagerPOATie(IpLoadManagerOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.fw.fw_application.integrity.IpLoadManager _this()
	{
		return org.csapi.fw.fw_application.integrity.IpLoadManagerHelper.narrow(_this_object());
	}
	public org.csapi.fw.fw_application.integrity.IpLoadManager _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.fw.fw_application.integrity.IpLoadManagerHelper.narrow(_this_object(orb));
	}
	public IpLoadManagerOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpLoadManagerOperations delegate)
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
	public void queryLoadStatsReq(int loadStatsReqID, java.lang.String[] serviceIDs, org.csapi.TpTimeInterval timeInterval) throws org.csapi.fw.P_INVALID_SERVICE_ID,org.csapi.TpCommonExceptions,org.csapi.fw.P_SERVICE_NOT_ENABLED,org.csapi.P_UNAUTHORISED_PARAMETER_VALUE
	{
_delegate.queryLoadStatsReq(loadStatsReqID,serviceIDs,timeInterval);
	}

	public void queryAppLoadErr(org.csapi.fw.TpLoadStatisticError loadStatisticsError) throws org.csapi.TpCommonExceptions
	{
_delegate.queryAppLoadErr(loadStatisticsError);
	}

	public void queryAppLoadStatsErr(int loadStatsReqID, org.csapi.fw.TpLoadStatisticError loadStatisticsError) throws org.csapi.TpCommonExceptions
	{
_delegate.queryAppLoadStatsErr(loadStatsReqID,loadStatisticsError);
	}

	public void queryAppLoadRes(org.csapi.fw.TpLoadStatistic[] loadStatistics) throws org.csapi.TpCommonExceptions
	{
_delegate.queryAppLoadRes(loadStatistics);
	}

	public void resumeNotification(java.lang.String[] serviceIDs) throws org.csapi.fw.P_INVALID_SERVICE_ID,org.csapi.TpCommonExceptions,org.csapi.fw.P_SERVICE_NOT_ENABLED,org.csapi.P_UNAUTHORISED_PARAMETER_VALUE
	{
_delegate.resumeNotification(serviceIDs);
	}

	public void reportLoad(org.csapi.fw.TpLoadLevel loadLevel) throws org.csapi.TpCommonExceptions
	{
_delegate.reportLoad(loadLevel);
	}

	public void suspendNotification(java.lang.String[] serviceIDs) throws org.csapi.fw.P_INVALID_SERVICE_ID,org.csapi.TpCommonExceptions,org.csapi.fw.P_SERVICE_NOT_ENABLED,org.csapi.P_UNAUTHORISED_PARAMETER_VALUE
	{
_delegate.suspendNotification(serviceIDs);
	}

	public void createLoadLevelNotification(java.lang.String[] serviceIDs) throws org.csapi.fw.P_INVALID_SERVICE_ID,org.csapi.TpCommonExceptions,org.csapi.P_UNAUTHORISED_PARAMETER_VALUE
	{
_delegate.createLoadLevelNotification(serviceIDs);
	}

	public void queryLoadReq(java.lang.String[] serviceIDs, org.csapi.TpTimeInterval timeInterval) throws org.csapi.fw.P_INVALID_SERVICE_ID,org.csapi.TpCommonExceptions,org.csapi.fw.P_SERVICE_NOT_ENABLED,org.csapi.P_UNAUTHORISED_PARAMETER_VALUE
	{
_delegate.queryLoadReq(serviceIDs,timeInterval);
	}

	public void destroyLoadLevelNotification(java.lang.String[] serviceIDs) throws org.csapi.fw.P_INVALID_SERVICE_ID,org.csapi.TpCommonExceptions,org.csapi.P_UNAUTHORISED_PARAMETER_VALUE
	{
_delegate.destroyLoadLevelNotification(serviceIDs);
	}

	public void queryAppLoadStatsRes(int loadStatsReqID, org.csapi.fw.TpLoadStatistic[] loadStatistics) throws org.csapi.TpCommonExceptions
	{
_delegate.queryAppLoadStatsRes(loadStatsReqID,loadStatistics);
	}

}
