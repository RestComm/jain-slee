package org.csapi.fw.fw_service.integrity;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpSvcFaultManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpSvcFaultManagerPOATie
	extends IpSvcFaultManagerPOA
{
	private IpSvcFaultManagerOperations _delegate;

	private POA _poa;
	public IpSvcFaultManagerPOATie(IpSvcFaultManagerOperations delegate)
	{
		_delegate = delegate;
	}
	public IpSvcFaultManagerPOATie(IpSvcFaultManagerOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.fw.fw_service.integrity.IpSvcFaultManager _this()
	{
		return org.csapi.fw.fw_service.integrity.IpSvcFaultManagerHelper.narrow(_this_object());
	}
	public org.csapi.fw.fw_service.integrity.IpSvcFaultManager _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.fw.fw_service.integrity.IpSvcFaultManagerHelper.narrow(_this_object(orb));
	}
	public IpSvcFaultManagerOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpSvcFaultManagerOperations delegate)
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

	public void generateFaultStatisticsRecordReq(int faultStatsReqID, org.csapi.TpTimeInterval timePeriod) throws org.csapi.TpCommonExceptions
	{
_delegate.generateFaultStatisticsRecordReq(faultStatsReqID,timePeriod);
	}

	public void genFaultStatsRecordErr(org.csapi.fw.TpFaultStatisticsError faultStatisticsError, org.csapi.fw.TpSubjectType recordSubject) throws org.csapi.TpCommonExceptions
	{
_delegate.genFaultStatsRecordErr(faultStatisticsError,recordSubject);
	}

	public void generateFaultStatisticsRecordRes(int faultStatsReqID, org.csapi.fw.TpFaultStatsRecord faultStatistics, org.csapi.fw.TpSubjectType recordSubject) throws org.csapi.TpCommonExceptions
	{
_delegate.generateFaultStatisticsRecordRes(faultStatsReqID,faultStatistics,recordSubject);
	}

	public void fwUnavailableInd(org.csapi.fw.TpFwUnavailReason reason) throws org.csapi.TpCommonExceptions
	{
_delegate.fwUnavailableInd(reason);
	}

	public void svcUnavailableInd() throws org.csapi.TpCommonExceptions
	{
_delegate.svcUnavailableInd();
	}

	public void activityTestErr(int activityTestID) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_INVALID_ACTIVITY_TEST_ID
	{
_delegate.activityTestErr(activityTestID);
	}

	public void svcActivityTestReq(int activityTestID) throws org.csapi.TpCommonExceptions
	{
_delegate.svcActivityTestReq(activityTestID);
	}

	public void fwFaultRecoveryInd(org.csapi.fw.TpInterfaceFault fault) throws org.csapi.TpCommonExceptions
	{
_delegate.fwFaultRecoveryInd(fault);
	}

	public void genFaultStatsRecordRes(org.csapi.fw.TpFaultStatsRecord faultStatistics, org.csapi.fw.TpSubjectType recordSubject) throws org.csapi.TpCommonExceptions
	{
_delegate.genFaultStatsRecordRes(faultStatistics,recordSubject);
	}

	public void appAvailStatusInd(org.csapi.fw.TpAppAvailStatusReason reason) throws org.csapi.TpCommonExceptions
	{
_delegate.appAvailStatusInd(reason);
	}

	public void generateFaultStatisticsRecordErr(int faultStatsReqID, org.csapi.fw.TpFaultStatisticsError faultStatisticsError, org.csapi.fw.TpSubjectType recordSubject) throws org.csapi.TpCommonExceptions
	{
_delegate.generateFaultStatisticsRecordErr(faultStatsReqID,faultStatisticsError,recordSubject);
	}

	public void generateFaultStatsRecordReq(org.csapi.TpTimeInterval timePeriod) throws org.csapi.TpCommonExceptions
	{
_delegate.generateFaultStatsRecordReq(timePeriod);
	}

	public void fwFaultReportInd(org.csapi.fw.TpInterfaceFault fault) throws org.csapi.TpCommonExceptions
	{
_delegate.fwFaultReportInd(fault);
	}

	public void appUnavailableInd() throws org.csapi.TpCommonExceptions
	{
_delegate.appUnavailableInd();
	}

	public void activityTestRes(int activityTestID, java.lang.String activityTestResult) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_INVALID_ACTIVITY_TEST_ID
	{
_delegate.activityTestRes(activityTestID,activityTestResult);
	}

}
