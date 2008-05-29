package org.csapi.cc.mpccs;

/**
 *	Generated from IDL interface "IpCallLeg"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpCallLegOperations
	extends org.csapi.IpServiceOperations
{
	/* constants */
	/* operations  */
	void routeReq(int callLegSessionID, org.csapi.TpAddress targetAddress, org.csapi.TpAddress originatingAddress, org.csapi.cc.TpCallAppInfo[] appInfo, org.csapi.cc.TpCallLegConnectionProperties connectionProperties) throws org.csapi.P_INVALID_NETWORK_STATE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_ADDRESS,org.csapi.P_INVALID_SESSION_ID,org.csapi.P_UNSUPPORTED_ADDRESS_PLAN;
	void eventReportReq(int callLegSessionID, org.csapi.cc.TpCallEventRequest[] eventsRequested) throws org.csapi.P_INVALID_EVENT_TYPE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID,org.csapi.P_INVALID_CRITERIA;
	void release(int callLegSessionID, org.csapi.cc.TpReleaseCause cause) throws org.csapi.P_INVALID_NETWORK_STATE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID;
	void getInfoReq(int callLegSessionID, int callLegInfoRequested) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID;
	org.csapi.cc.mpccs.TpMultiPartyCallIdentifier getCall(int callLegSessionID) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID;
	void attachMediaReq(int callLegSessionID) throws org.csapi.P_INVALID_NETWORK_STATE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID;
	void detachMediaReq(int callLegSessionID) throws org.csapi.P_INVALID_NETWORK_STATE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID;
	org.csapi.TpAddress getCurrentDestinationAddress(int callLegSessionID) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID;
	void continueProcessing(int callLegSessionID) throws org.csapi.P_INVALID_NETWORK_STATE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID;
	void setChargePlan(int callLegSessionID, org.csapi.cc.TpCallChargePlan callChargePlan) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID;
	void setAdviceOfCharge(int callLegSessionID, org.csapi.TpAoCInfo aOCInfo, int tariffSwitch) throws org.csapi.P_INVALID_AMOUNT,org.csapi.P_INVALID_CURRENCY,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID;
	void superviseReq(int callLegSessionID, int time, int treatment) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID;
	void deassign(int callLegSessionID) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID;
}
