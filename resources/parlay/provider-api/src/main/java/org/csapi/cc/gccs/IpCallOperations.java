package org.csapi.cc.gccs;

/**
 *	Generated from IDL interface "IpCall"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpCallOperations
	extends org.csapi.IpServiceOperations
{
	/* constants */
	/* operations  */
	int routeReq(int callSessionID, org.csapi.cc.gccs.TpCallReportRequest[] responseRequested, org.csapi.TpAddress targetAddress, org.csapi.TpAddress originatingAddress, org.csapi.TpAddress originalDestinationAddress, org.csapi.TpAddress redirectingAddress, org.csapi.cc.gccs.TpCallAppInfo[] appInfo) throws org.csapi.P_INVALID_EVENT_TYPE,org.csapi.P_INVALID_NETWORK_STATE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_ADDRESS,org.csapi.P_INVALID_SESSION_ID,org.csapi.P_UNSUPPORTED_ADDRESS_PLAN,org.csapi.P_INVALID_CRITERIA;
	void release(int callSessionID, org.csapi.cc.gccs.TpCallReleaseCause cause) throws org.csapi.P_INVALID_NETWORK_STATE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID;
	void deassignCall(int callSessionID) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID;
	void getCallInfoReq(int callSessionID, int callInfoRequested) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID;
	void setCallChargePlan(int callSessionID, org.csapi.cc.TpCallChargePlan callChargePlan) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID;
	void setAdviceOfCharge(int callSessionID, org.csapi.TpAoCInfo aOCInfo, int tariffSwitch) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID;
	void getMoreDialledDigitsReq(int callSessionID, int length) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID;
	void superviseCallReq(int callSessionID, int time, int treatment) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID;
	void continueProcessing(int callSessionID) throws org.csapi.P_INVALID_NETWORK_STATE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID;
}
