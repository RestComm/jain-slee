package org.csapi.fw.fw_application.integrity;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpFaultManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpFaultManagerPOATie
	extends IpFaultManagerPOA
{
	private IpFaultManagerOperations _delegate;

	private POA _poa;
	public IpFaultManagerPOATie(IpFaultManagerOperations delegate)
	{
		_delegate = delegate;
	}
	public IpFaultManagerPOATie(IpFaultManagerOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.fw.fw_application.integrity.IpFaultManager _this()
	{
		return org.csapi.fw.fw_application.integrity.IpFaultManagerHelper.narrow(_this_object());
	}
	public org.csapi.fw.fw_application.integrity.IpFaultManager _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.fw.fw_application.integrity.IpFaultManagerHelper.narrow(_this_object(orb));
	}
	public IpFaultManagerOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpFaultManagerOperations delegate)
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
	public void genFaultStatsRecordReq(org.csapi.TpTimeInterval timePeriod, java.lang.String[] serviceIDs) throws org.csapi.fw.P_INVALID_SERVICE_ID,org.csapi.TpCommonExceptions,org.csapi.P_UNAUTHORISED_PARAMETER_VALUE
	{
_delegate.genFaultStatsRecordReq(timePeriod,serviceIDs);
	}

	public void generateFaultStatisticsRecordReq(int faultStatsReqID, org.csapi.TpTimeInterval timePeriod, java.lang.String[] serviceIDs) throws org.csapi.fw.P_INVALID_SERVICE_ID,org.csapi.TpCommonExceptions,org.csapi.P_UNAUTHORISED_PARAMETER_VALUE
	{
_delegate.generateFaultStatisticsRecordReq(faultStatsReqID,timePeriod,serviceIDs);
	}

	public void appUnavailableInd(java.lang.String serviceID) throws org.csapi.TpCommonExceptions
	{
_delegate.appUnavailableInd(serviceID);
	}

	public void generateFaultStatisticsRecordRes(int faultStatsReqID, org.csapi.fw.TpFaultStatsRecord faultStatistics) throws org.csapi.TpCommonExceptions
	{
_delegate.generateFaultStatisticsRecordRes(faultStatsReqID,faultStatistics);
	}

	public void appActivityTestRes(int activityTestID, java.lang.String activityTestResult) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_INVALID_ACTIVITY_TEST_ID
	{
_delegate.appActivityTestRes(activityTestID,activityTestResult);
	}

	public void activityTestReq(int activityTestID, java.lang.String svcID) throws org.csapi.fw.P_INVALID_SERVICE_ID,org.csapi.TpCommonExceptions,org.csapi.P_UNAUTHORISED_PARAMETER_VALUE
	{
_delegate.activityTestReq(activityTestID,svcID);
	}

	public void appActivityTestErr(int activityTestID) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_INVALID_ACTIVITY_TEST_ID
	{
_delegate.appActivityTestErr(activityTestID);
	}

	public void generateFaultStatisticsRecordErr(int faultStatsReqID, org.csapi.fw.TpFaultStatisticsError faultStatisticsError) throws org.csapi.TpCommonExceptions
	{
_delegate.generateFaultStatisticsRecordErr(faultStatsReqID,faultStatisticsError);
	}

	public void svcUnavailableInd(java.lang.String serviceID) throws org.csapi.fw.P_INVALID_SERVICE_ID,org.csapi.TpCommonExceptions,org.csapi.P_UNAUTHORISED_PARAMETER_VALUE
	{
_delegate.svcUnavailableInd(serviceID);
	}

	public void appAvailStatusInd(org.csapi.fw.TpAppAvailStatusReason reason) throws org.csapi.TpCommonExceptions
	{
_delegate.appAvailStatusInd(reason);
	}

	public void genFaultStatsRecordRes(org.csapi.fw.TpFaultStatsRecord faultStatistics) throws org.csapi.TpCommonExceptions
	{
_delegate.genFaultStatsRecordRes(faultStatistics);
	}

	public void genFaultStatsRecordErr(org.csapi.fw.TpFaultStatisticsError faultStatisticsError) throws org.csapi.TpCommonExceptions
	{
_delegate.genFaultStatsRecordErr(faultStatisticsError);
	}

}
