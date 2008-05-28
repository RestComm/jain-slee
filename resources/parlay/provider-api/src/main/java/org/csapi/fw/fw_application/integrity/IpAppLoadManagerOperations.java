package org.csapi.fw.fw_application.integrity;

/**
 *	Generated from IDL interface "IpAppLoadManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpAppLoadManagerOperations
	extends org.csapi.IpInterfaceOperations
{
	/* constants */
	/* operations  */
	void queryAppLoadReq(org.csapi.TpTimeInterval timeInterval);
	void queryLoadRes(org.csapi.fw.TpLoadStatistic[] loadStatistics);
	void queryLoadErr(org.csapi.fw.TpLoadStatisticError loadStatisticsError);
	void loadLevelNotification(org.csapi.fw.TpLoadStatistic[] loadStatistics);
	void resumeNotification();
	void suspendNotification();
	void createLoadLevelNotification();
	void destroyLoadLevelNotification();
	void queryAppLoadStatsReq(int loadStatsReqID, org.csapi.TpTimeInterval timeInterval);
	void queryLoadStatsRes(int loadStatsReqID, org.csapi.fw.TpLoadStatistic[] loadStatistics);
	void queryLoadStatsErr(int loadStatsReqID, org.csapi.fw.TpLoadStatisticError loadStatisticsError);
}
