package org.csapi.fw.fw_application.integrity;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpAppFaultManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpAppFaultManagerPOATie
	extends IpAppFaultManagerPOA
{
	private IpAppFaultManagerOperations _delegate;

	private POA _poa;
	public IpAppFaultManagerPOATie(IpAppFaultManagerOperations delegate)
	{
		_delegate = delegate;
	}
	public IpAppFaultManagerPOATie(IpAppFaultManagerOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.fw.fw_application.integrity.IpAppFaultManager _this()
	{
		return org.csapi.fw.fw_application.integrity.IpAppFaultManagerHelper.narrow(_this_object());
	}
	public org.csapi.fw.fw_application.integrity.IpAppFaultManager _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.fw.fw_application.integrity.IpAppFaultManagerHelper.narrow(_this_object(orb));
	}
	public IpAppFaultManagerOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpAppFaultManagerOperations delegate)
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
	public void svcUnavailableInd(java.lang.String serviceID, org.csapi.fw.TpSvcUnavailReason reason)
	{
_delegate.svcUnavailableInd(serviceID,reason);
	}

	public void generateFaultStatisticsRecordReq(int faultStatsReqID, org.csapi.TpTimeInterval timePeriod)
	{
_delegate.generateFaultStatisticsRecordReq(faultStatsReqID,timePeriod);
	}

	public void generateFaultStatisticsRecordRes(int faultStatsReqID, org.csapi.fw.TpFaultStatsRecord faultStatistics, java.lang.String[] serviceIDs)
	{
_delegate.generateFaultStatisticsRecordRes(faultStatsReqID,faultStatistics,serviceIDs);
	}

	public void appUnavailableInd(java.lang.String serviceID)
	{
_delegate.appUnavailableInd(serviceID);
	}

	public void genFaultStatsRecordReq(org.csapi.TpTimeInterval timePeriod)
	{
_delegate.genFaultStatsRecordReq(timePeriod);
	}

	public void genFaultStatsRecordRes(org.csapi.fw.TpFaultStatsRecord faultStatistics, java.lang.String[] serviceIDs)
	{
_delegate.genFaultStatsRecordRes(faultStatistics,serviceIDs);
	}

	public void genFaultStatsRecordErr(org.csapi.fw.TpFaultStatisticsError faultStatisticsError, java.lang.String[] serviceIDs)
	{
_delegate.genFaultStatsRecordErr(faultStatisticsError,serviceIDs);
	}

	public void fwUnavailableInd(org.csapi.fw.TpFwUnavailReason reason)
	{
_delegate.fwUnavailableInd(reason);
	}

	public void svcAvailStatusInd(java.lang.String serviceID, org.csapi.fw.TpSvcAvailStatusReason reason)
	{
_delegate.svcAvailStatusInd(serviceID,reason);
	}

	public void activityTestErr(int activityTestID)
	{
_delegate.activityTestErr(activityTestID);
	}

	public void fwFaultRecoveryInd(org.csapi.fw.TpInterfaceFault fault)
	{
_delegate.fwFaultRecoveryInd(fault);
	}

	public void generateFaultStatisticsRecordErr(int faultStatsReqID, org.csapi.fw.TpFaultStatisticsError[] faultStatistics, java.lang.String[] serviceIDs)
	{
_delegate.generateFaultStatisticsRecordErr(faultStatsReqID,faultStatistics,serviceIDs);
	}

	public void appActivityTestReq(int activityTestID)
	{
_delegate.appActivityTestReq(activityTestID);
	}

	public void fwFaultReportInd(org.csapi.fw.TpInterfaceFault fault)
	{
_delegate.fwFaultReportInd(fault);
	}

	public void activityTestRes(int activityTestID, java.lang.String activityTestResult)
	{
_delegate.activityTestRes(activityTestID,activityTestResult);
	}

}
