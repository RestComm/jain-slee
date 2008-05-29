package org.csapi.fw.fw_application.integrity;

/**
 *	Generated from IDL interface "IpLoadManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpLoadManagerOperations
	extends org.csapi.IpInterfaceOperations
{
	/* constants */
	/* operations  */
	void reportLoad(org.csapi.fw.TpLoadLevel loadLevel) throws org.csapi.TpCommonExceptions;
	void queryLoadReq(java.lang.String[] serviceIDs, org.csapi.TpTimeInterval timeInterval) throws org.csapi.fw.P_INVALID_SERVICE_ID,org.csapi.TpCommonExceptions,org.csapi.fw.P_SERVICE_NOT_ENABLED,org.csapi.P_UNAUTHORISED_PARAMETER_VALUE;
	void queryAppLoadRes(org.csapi.fw.TpLoadStatistic[] loadStatistics) throws org.csapi.TpCommonExceptions;
	void queryAppLoadErr(org.csapi.fw.TpLoadStatisticError loadStatisticsError) throws org.csapi.TpCommonExceptions;
	void createLoadLevelNotification(java.lang.String[] serviceIDs) throws org.csapi.fw.P_INVALID_SERVICE_ID,org.csapi.TpCommonExceptions,org.csapi.P_UNAUTHORISED_PARAMETER_VALUE;
	void destroyLoadLevelNotification(java.lang.String[] serviceIDs) throws org.csapi.fw.P_INVALID_SERVICE_ID,org.csapi.TpCommonExceptions,org.csapi.P_UNAUTHORISED_PARAMETER_VALUE;
	void resumeNotification(java.lang.String[] serviceIDs) throws org.csapi.fw.P_INVALID_SERVICE_ID,org.csapi.TpCommonExceptions,org.csapi.fw.P_SERVICE_NOT_ENABLED,org.csapi.P_UNAUTHORISED_PARAMETER_VALUE;
	void suspendNotification(java.lang.String[] serviceIDs) throws org.csapi.fw.P_INVALID_SERVICE_ID,org.csapi.TpCommonExceptions,org.csapi.fw.P_SERVICE_NOT_ENABLED,org.csapi.P_UNAUTHORISED_PARAMETER_VALUE;
	void queryLoadStatsReq(int loadStatsReqID, java.lang.String[] serviceIDs, org.csapi.TpTimeInterval timeInterval) throws org.csapi.fw.P_INVALID_SERVICE_ID,org.csapi.TpCommonExceptions,org.csapi.fw.P_SERVICE_NOT_ENABLED,org.csapi.P_UNAUTHORISED_PARAMETER_VALUE;
	void queryAppLoadStatsRes(int loadStatsReqID, org.csapi.fw.TpLoadStatistic[] loadStatistics) throws org.csapi.TpCommonExceptions;
	void queryAppLoadStatsErr(int loadStatsReqID, org.csapi.fw.TpLoadStatisticError loadStatisticsError) throws org.csapi.TpCommonExceptions;
}
