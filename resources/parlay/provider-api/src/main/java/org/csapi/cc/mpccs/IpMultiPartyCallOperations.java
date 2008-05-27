package org.csapi.cc.mpccs;

/**
 *	Generated from IDL interface "IpMultiPartyCall"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpMultiPartyCallOperations
	extends org.csapi.IpServiceOperations
{
	/* constants */
	/* operations  */
	org.csapi.cc.mpccs.TpCallLegIdentifier[] getCallLegs(int callSessionID) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID;
	org.csapi.cc.mpccs.TpCallLegIdentifier createCallLeg(int callSessionID, org.csapi.cc.mpccs.IpAppCallLeg appCallLeg) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID;
	org.csapi.cc.mpccs.TpCallLegIdentifier createAndRouteCallLegReq(int callSessionID, org.csapi.cc.TpCallEventRequest[] eventsRequested, org.csapi.TpAddress targetAddress, org.csapi.TpAddress originatingAddress, org.csapi.cc.TpCallAppInfo[] appInfo, org.csapi.cc.mpccs.IpAppCallLeg appLegInterface) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.P_INVALID_EVENT_TYPE,org.csapi.P_INVALID_NETWORK_STATE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_ADDRESS,org.csapi.P_INVALID_SESSION_ID,org.csapi.P_UNSUPPORTED_ADDRESS_PLAN,org.csapi.P_INVALID_CRITERIA;
	void release(int callSessionID, org.csapi.cc.TpReleaseCause cause) throws org.csapi.P_INVALID_NETWORK_STATE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID;
	void deassignCall(int callSessionID) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID;
	void getInfoReq(int callSessionID, int callInfoRequested) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID;
	void setChargePlan(int callSessionID, org.csapi.cc.TpCallChargePlan callChargePlan) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID;
	void setAdviceOfCharge(int callSessionID, org.csapi.TpAoCInfo aOCInfo, int tariffSwitch) throws org.csapi.P_INVALID_AMOUNT,org.csapi.P_INVALID_CURRENCY,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID;
	void superviseReq(int callSessionID, int time, int treatment) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID;
}
