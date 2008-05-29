package org.csapi.fw.fw_service.integrity;

/**
 *	Generated from IDL interface "IpSvcFaultManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpSvcFaultManagerOperations
	extends org.csapi.IpInterfaceOperations
{
	/* constants */
	/* operations  */
	void activityTestRes(int activityTestID, java.lang.String activityTestResult) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_INVALID_ACTIVITY_TEST_ID;
	void svcActivityTestReq(int activityTestID) throws org.csapi.TpCommonExceptions;
	void fwFaultReportInd(org.csapi.fw.TpInterfaceFault fault) throws org.csapi.TpCommonExceptions;
	void fwFaultRecoveryInd(org.csapi.fw.TpInterfaceFault fault) throws org.csapi.TpCommonExceptions;
	void fwUnavailableInd(org.csapi.fw.TpFwUnavailReason reason) throws org.csapi.TpCommonExceptions;
	void svcUnavailableInd() throws org.csapi.TpCommonExceptions;
	void appUnavailableInd() throws org.csapi.TpCommonExceptions;
	void genFaultStatsRecordRes(org.csapi.fw.TpFaultStatsRecord faultStatistics, org.csapi.fw.TpSubjectType recordSubject) throws org.csapi.TpCommonExceptions;
	void activityTestErr(int activityTestID) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_INVALID_ACTIVITY_TEST_ID;
	void genFaultStatsRecordErr(org.csapi.fw.TpFaultStatisticsError faultStatisticsError, org.csapi.fw.TpSubjectType recordSubject) throws org.csapi.TpCommonExceptions;
	void genFaultStatsRecordReq(org.csapi.TpTimeInterval timePeriod, java.lang.String[] serviceIDs) throws org.csapi.fw.P_INVALID_SERVICE_ID,org.csapi.TpCommonExceptions,org.csapi.P_UNAUTHORISED_PARAMETER_VALUE;
	void generateFaultStatsRecordReq(org.csapi.TpTimeInterval timePeriod) throws org.csapi.TpCommonExceptions;
	void appAvailStatusInd(org.csapi.fw.TpAppAvailStatusReason reason) throws org.csapi.TpCommonExceptions;
	void generateFaultStatisticsRecordRes(int faultStatsReqID, org.csapi.fw.TpFaultStatsRecord faultStatistics, org.csapi.fw.TpSubjectType recordSubject) throws org.csapi.TpCommonExceptions;
	void generateFaultStatisticsRecordErr(int faultStatsReqID, org.csapi.fw.TpFaultStatisticsError faultStatisticsError, org.csapi.fw.TpSubjectType recordSubject) throws org.csapi.TpCommonExceptions;
	void generateFaultStatisticsRecordReq(int faultStatsReqID, org.csapi.TpTimeInterval timePeriod) throws org.csapi.TpCommonExceptions;
}
