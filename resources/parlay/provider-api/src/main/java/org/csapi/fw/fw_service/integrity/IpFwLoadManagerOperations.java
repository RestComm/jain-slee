package org.csapi.fw.fw_service.integrity;

/**
 *	Generated from IDL interface "IpFwLoadManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpFwLoadManagerOperations
	extends org.csapi.IpInterfaceOperations
{
	/* constants */
	/* operations  */
	void reportLoad(org.csapi.fw.TpLoadLevel loadLevel) throws org.csapi.TpCommonExceptions;
	void queryLoadReq(org.csapi.fw.TpSubjectType querySubject, org.csapi.TpTimeInterval timeInterval) throws org.csapi.TpCommonExceptions;
	void querySvcLoadRes(org.csapi.fw.TpLoadStatistic[] loadStatistics) throws org.csapi.TpCommonExceptions;
	void querySvcLoadErr(org.csapi.fw.TpLoadStatisticError loadStatisticError) throws org.csapi.TpCommonExceptions;
	void createLoadLevelNotification(org.csapi.fw.TpSubjectType notificationSubject) throws org.csapi.TpCommonExceptions;
	void destroyLoadLevelNotification(org.csapi.fw.TpSubjectType notificationSubject) throws org.csapi.TpCommonExceptions;
	void suspendNotification(org.csapi.fw.TpSubjectType notificationSubject) throws org.csapi.TpCommonExceptions;
	void resumeNotification(org.csapi.fw.TpSubjectType notificationSubject) throws org.csapi.TpCommonExceptions;
	void queryLoadStatsReq(int loadStatsReqID, org.csapi.fw.TpSubjectType querySubject, org.csapi.TpTimeInterval timeInterval) throws org.csapi.TpCommonExceptions;
	void querySvcLoadStatsRes(int loadStatsReqID, org.csapi.fw.TpLoadStatistic[] loadStatistics) throws org.csapi.TpCommonExceptions;
	void querySvcLoadStatsErr(int loadStatsReqID, org.csapi.fw.TpLoadStatisticError loadStatisticError) throws org.csapi.TpCommonExceptions;
}
