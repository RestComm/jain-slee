package org.csapi.fw.fw_service.integrity;

/**
 *	Generated from IDL interface "IpFwFaultManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpFwFaultManagerOperations
	extends org.csapi.IpInterfaceOperations
{
	/* constants */
	/* operations  */
	void activityTestReq(int activityTestID, org.csapi.fw.TpSubjectType testSubject) throws org.csapi.TpCommonExceptions;
	void svcActivityTestRes(int activityTestID, java.lang.String activityTestResult) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_INVALID_ACTIVITY_TEST_ID;
	void appUnavailableInd() throws org.csapi.TpCommonExceptions;
	void genFaultStatsRecordReq(org.csapi.TpTimeInterval timePeriod, org.csapi.fw.TpSubjectType recordSubject) throws org.csapi.TpCommonExceptions;
	void svcUnavailableInd(org.csapi.fw.TpSvcUnavailReason reason) throws org.csapi.TpCommonExceptions;
	void svcActivityTestErr(int activityTestID) throws org.csapi.TpCommonExceptions,org.csapi.fw.P_INVALID_ACTIVITY_TEST_ID;
	void genFaultStatsRecordRes(org.csapi.fw.TpFaultStatsRecord faultStatistics, java.lang.String[] serviceIDs) throws org.csapi.TpCommonExceptions;
	void genFaultStatsRecordErr(org.csapi.fw.TpFaultStatisticsError faultStatisticsError, java.lang.String[] serviceIDs) throws org.csapi.TpCommonExceptions;
	void generateFaultStatsRecordRes(org.csapi.fw.TpFaultStatsRecord faultStatistics) throws org.csapi.TpCommonExceptions;
	void generateFaultStatsRecordErr(org.csapi.fw.TpFaultStatisticsError faultStatisticsError) throws org.csapi.TpCommonExceptions;
	void svcAvailStatusInd(org.csapi.fw.TpSvcAvailStatusReason reason) throws org.csapi.TpCommonExceptions;
	void generateFaultStatisticsRecordReq(int faultStatsReqID, org.csapi.TpTimeInterval timePeriod, org.csapi.fw.TpSubjectType recordSubject) throws org.csapi.TpCommonExceptions;
	void generateFaultStatisticsRecordRes(int faultStatsReqID, org.csapi.fw.TpFaultStatsRecord faultStatistics) throws org.csapi.TpCommonExceptions;
	void generateFaultStatisticsRecordErr(int faultStatsReqID, org.csapi.fw.TpFaultStatisticsError faultStatisticsError) throws org.csapi.TpCommonExceptions;
}
