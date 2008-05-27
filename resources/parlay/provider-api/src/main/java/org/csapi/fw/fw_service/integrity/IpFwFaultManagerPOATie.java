package org.csapi.fw.fw_service.integrity;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpFwFaultManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpFwFaultManagerPOATie
	extends IpFwFaultManagerPOA
{
	private IpFwFaultManagerOperations _delegate;

	private POA _poa;
	public IpFwFaultManagerPOATie(IpFwFaultManagerOperations delegate)
	{
		_delegate = delegate;
	}
	public IpFwFaultManagerPOATie(IpFwFaultManagerOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.fw.fw_service.integrity.IpFwFaultManager _this()
	{
		return org.csapi.fw.fw_service.integrity.IpFwFaultManagerHelper.narrow(_this_object());
	}
	public org.csapi.fw.fw_service.integrity.IpFwFaultManager _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.fw.fw_service.integrity.IpFwFaultManagerHelper.narrow(_this_object(orb));
	}
	public IpFwFaultManagerOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpFwFaultManagerOperations delegate)
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
	public void generateFaultStatisticsRecordReq(int faultStatsReqID, org.csapi.TpTimeInterval timePeriod, org.csapi.fw.TpSubjectType recordSubject) throws org.csapi.TpCommonExceptions
	{
_delegate.generateFaultStatisticsRecordReq(faultStatsReqID,timePeriod,recordSubject);
	}

	public void generateFaultStatsRecordRes(org.csapi.fw.TpFaultStatsRecord faultStatistics) throws org.csapi.TpCommonExceptions
	{
_delegate.generateFaultStatsRecordRes(faultStatistics);
	}

	public void genFaultStatsRecordRes(org.csapi.fw.TpFaultStatsRecord faultStatistics, java.lang.String[] serviceIDs) throws org.csapi.TpCommonExceptions
	{
_delegate.genFaultStatsRecordRes(faultStatistics,serviceIDs);
	}

	public void generateFaultStatisticsRecordRes(int faultStatsReqID, org.csapi.fw.TpFaultStatsRecord faultStatistics) throws org.csapi.TpCommonExceptions
	{
_delegate.generateFaultStatisticsRecordRes(faultStatsReqID,faultStatistics);
	}

	public void genFaultStatsRecordErr(org.csapi.fw.TpFaultStatisticsError faultStatisticsError, java.lang.String[] serviceIDs) throws org.csapi.TpCommonExceptions
	{
_delegate.genFaultStatsRecordErr(faultStatisticsError,serviceIDs);
	}

	public void svcUnavailableInd(org.csapi.fw.TpSvcUnavailReason reason) throws org.csapi.TpCommonExceptions
	{
_delegate.svcUnavailableInd(reason);
	}

	public void svcActivityTestErr(int activityTestID) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_INVALID_ACTIVITY_TEST_ID
	{
_delegate.svcActivityTestErr(activityTestID);
	}

	public void svcAvailStatusInd(org.csapi.fw.TpSvcAvailStatusReason reason) throws org.csapi.TpCommonExceptions
	{
_delegate.svcAvailStatusInd(reason);
	}

	public void generateFaultStatisticsRecordErr(int faultStatsReqID, org.csapi.fw.TpFaultStatisticsError faultStatisticsError) throws org.csapi.TpCommonExceptions
	{
_delegate.generateFaultStatisticsRecordErr(faultStatsReqID,faultStatisticsError);
	}

	public void generateFaultStatsRecordErr(org.csapi.fw.TpFaultStatisticsError faultStatisticsError) throws org.csapi.TpCommonExceptions
	{
_delegate.generateFaultStatsRecordErr(faultStatisticsError);
	}

	public void svcActivityTestRes(int activityTestID, java.lang.String activityTestResult) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_INVALID_ACTIVITY_TEST_ID
	{
_delegate.svcActivityTestRes(activityTestID,activityTestResult);
	}

	public void activityTestReq(int activityTestID, org.csapi.fw.TpSubjectType testSubject) throws org.csapi.TpCommonExceptions
	{
_delegate.activityTestReq(activityTestID,testSubject);
	}

	public void genFaultStatsRecordReq(org.csapi.TpTimeInterval timePeriod, org.csapi.fw.TpSubjectType recordSubject) throws org.csapi.TpCommonExceptions
	{
_delegate.genFaultStatsRecordReq(timePeriod,recordSubject);
	}

	public void appUnavailableInd() throws org.csapi.TpCommonExceptions
	{
_delegate.appUnavailableInd();
	}

}
