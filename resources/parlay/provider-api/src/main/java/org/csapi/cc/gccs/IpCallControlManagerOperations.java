package org.csapi.cc.gccs;

/**
 *	Generated from IDL interface "IpCallControlManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpCallControlManagerOperations
	extends org.csapi.IpServiceOperations
{
	/* constants */
	/* operations  */
	org.csapi.cc.gccs.TpCallIdentifier createCall(org.csapi.cc.gccs.IpAppCall appCall) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions;
	int enableCallNotification(org.csapi.cc.gccs.IpAppCallControlManager appCallControlManager, org.csapi.cc.gccs.TpCallEventCriteria eventCriteria) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.P_INVALID_EVENT_TYPE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_CRITERIA;
	void disableCallNotification(int assignmentID) throws org.csapi.P_INVALID_ASSIGNMENT_ID,org.csapi.TpCommonExceptions;
	int setCallLoadControl(int duration, org.csapi.cc.TpCallLoadControlMechanism mechanism, org.csapi.cc.gccs.TpCallTreatment treatment, org.csapi.TpAddressRange addressRange) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_ADDRESS,org.csapi.P_UNSUPPORTED_ADDRESS_PLAN;
	void changeCallNotification(int assignmentID, org.csapi.cc.gccs.TpCallEventCriteria eventCriteria) throws org.csapi.P_INVALID_ASSIGNMENT_ID,org.csapi.P_INVALID_EVENT_TYPE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_CRITERIA;
	org.csapi.cc.gccs.TpCallEventCriteriaResult[] getCriteria() throws org.csapi.TpCommonExceptions;
}
