package org.csapi.dsc;

/**
 *	Generated from IDL interface "IpDataSession"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpDataSessionOperations
	extends org.csapi.IpServiceOperations
{
	/* constants */
	/* operations  */
	int connectReq(int dataSessionID, org.csapi.dsc.TpDataSessionReportRequest[] responseRequested, org.csapi.TpAddress targetAddress) throws org.csapi.P_INVALID_NETWORK_STATE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_ADDRESS,org.csapi.P_INVALID_SESSION_ID;
	void release(int dataSessionID, org.csapi.dsc.TpDataSessionReleaseCause cause) throws org.csapi.P_INVALID_NETWORK_STATE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID;
	void superviseDataSessionReq(int dataSessionID, int treatment, org.csapi.dsc.TpDataSessionSuperviseVolume bytes) throws org.csapi.P_INVALID_NETWORK_STATE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID;
	void setDataSessionChargePlan(int dataSessionID, org.csapi.dsc.TpDataSessionChargePlan dataSessionChargePlan) throws org.csapi.P_INVALID_NETWORK_STATE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID;
	void setAdviceOfCharge(int dataSessionID, org.csapi.TpAoCInfo aoCInfo, int tariffSwitch) throws org.csapi.P_INVALID_NETWORK_STATE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_TIME_AND_DATE_FORMAT;
	void deassignDataSession(int dataSessionID) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID;
	void continueProcessing(int dataSessionID) throws org.csapi.P_INVALID_NETWORK_STATE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID;
}
