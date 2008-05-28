package org.csapi.fw.fw_application.integrity;

/**
 *	Generated from IDL interface "IpAppFaultManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpAppFaultManagerOperations
	extends org.csapi.IpInterfaceOperations
{
	/* constants */
	/* operations  */
	void activityTestRes(int activityTestID, java.lang.String activityTestResult);
	void appActivityTestReq(int activityTestID);
	void fwFaultReportInd(org.csapi.fw.TpInterfaceFault fault);
	void fwFaultRecoveryInd(org.csapi.fw.TpInterfaceFault fault);
	void svcUnavailableInd(java.lang.String serviceID, org.csapi.fw.TpSvcUnavailReason reason);
	void genFaultStatsRecordRes(org.csapi.fw.TpFaultStatsRecord faultStatistics, java.lang.String[] serviceIDs);
	void fwUnavailableInd(org.csapi.fw.TpFwUnavailReason reason);
	void activityTestErr(int activityTestID);
	void genFaultStatsRecordErr(org.csapi.fw.TpFaultStatisticsError faultStatisticsError, java.lang.String[] serviceIDs);
	void appUnavailableInd(java.lang.String serviceID);
	void genFaultStatsRecordReq(org.csapi.TpTimeInterval timePeriod);
	void svcAvailStatusInd(java.lang.String serviceID, org.csapi.fw.TpSvcAvailStatusReason reason);
	void generateFaultStatisticsRecordRes(int faultStatsReqID, org.csapi.fw.TpFaultStatsRecord faultStatistics, java.lang.String[] serviceIDs);
	void generateFaultStatisticsRecordErr(int faultStatsReqID, org.csapi.fw.TpFaultStatisticsError[] faultStatistics, java.lang.String[] serviceIDs);
	void generateFaultStatisticsRecordReq(int faultStatsReqID, org.csapi.TpTimeInterval timePeriod);
}
