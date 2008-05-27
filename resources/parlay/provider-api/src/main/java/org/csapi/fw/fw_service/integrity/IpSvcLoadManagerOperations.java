package org.csapi.fw.fw_service.integrity;

/**
 *	Generated from IDL interface "IpSvcLoadManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpSvcLoadManagerOperations
	extends org.csapi.IpInterfaceOperations
{
	/* constants */
	/* operations  */
	void querySvcLoadReq(org.csapi.TpTimeInterval timeInterval) throws org.csapi.TpCommonExceptions;
	void queryLoadRes(org.csapi.fw.TpLoadStatistic[] loadStatistics) throws org.csapi.TpCommonExceptions;
	void queryLoadErr(org.csapi.fw.TpLoadStatisticError loadStatisticsError) throws org.csapi.TpCommonExceptions;
	void loadLevelNotification(org.csapi.fw.TpLoadStatistic[] loadStatistics) throws org.csapi.TpCommonExceptions;
	void suspendNotification() throws org.csapi.TpCommonExceptions;
	void resumeNotification() throws org.csapi.TpCommonExceptions;
	void createLoadLevelNotification() throws org.csapi.TpCommonExceptions;
	void destroyLoadLevelNotification() throws org.csapi.TpCommonExceptions;
	void querySvcLoadStatsReq(int loadStatsReqID, org.csapi.TpTimeInterval timeInterval) throws org.csapi.TpCommonExceptions;
	void queryLoadStatsRes(int loadStatsReqID, org.csapi.fw.TpLoadStatistic[] loadStatistics) throws org.csapi.TpCommonExceptions;
	void queryLoadStatsErr(int loadStatsReqID, org.csapi.fw.TpLoadStatisticError loadStatisticsError) throws org.csapi.TpCommonExceptions;
}
