package org.csapi.fw.fw_application.integrity;

/**
 *	Generated from IDL interface "IpFaultManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpFaultManagerOperations
	extends org.csapi.IpInterfaceOperations
{
	/* constants */
	/* operations  */
	void activityTestReq(int activityTestID, java.lang.String svcID) throws org.csapi.fw.P_INVALID_SERVICE_ID,org.csapi.TpCommonExceptions,org.csapi.P_UNAUTHORISED_PARAMETER_VALUE;
	void appActivityTestRes(int activityTestID, java.lang.String activityTestResult) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_INVALID_ACTIVITY_TEST_ID;
	void svcUnavailableInd(java.lang.String serviceID) throws org.csapi.fw.P_INVALID_SERVICE_ID,org.csapi.TpCommonExceptions,org.csapi.P_UNAUTHORISED_PARAMETER_VALUE;
	void genFaultStatsRecordReq(org.csapi.TpTimeInterval timePeriod, java.lang.String[] serviceIDs) throws org.csapi.fw.P_INVALID_SERVICE_ID,org.csapi.TpCommonExceptions,org.csapi.P_UNAUTHORISED_PARAMETER_VALUE;
	void appActivityTestErr(int activityTestID) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_INVALID_ACTIVITY_TEST_ID;
	void appUnavailableInd(java.lang.String serviceID) throws org.csapi.TpCommonExceptions;
	void genFaultStatsRecordRes(org.csapi.fw.TpFaultStatsRecord faultStatistics) throws org.csapi.TpCommonExceptions;
	void genFaultStatsRecordErr(org.csapi.fw.TpFaultStatisticsError faultStatisticsError) throws org.csapi.TpCommonExceptions;
	void appAvailStatusInd(org.csapi.fw.TpAppAvailStatusReason reason) throws org.csapi.TpCommonExceptions;
	void generateFaultStatisticsRecordReq(int faultStatsReqID, org.csapi.TpTimeInterval timePeriod, java.lang.String[] serviceIDs) throws org.csapi.fw.P_INVALID_SERVICE_ID,org.csapi.TpCommonExceptions,org.csapi.P_UNAUTHORISED_PARAMETER_VALUE;
	void generateFaultStatisticsRecordRes(int faultStatsReqID, org.csapi.fw.TpFaultStatsRecord faultStatistics) throws org.csapi.TpCommonExceptions;
	void generateFaultStatisticsRecordErr(int faultStatsReqID, org.csapi.fw.TpFaultStatisticsError faultStatisticsError) throws org.csapi.TpCommonExceptions;
}
