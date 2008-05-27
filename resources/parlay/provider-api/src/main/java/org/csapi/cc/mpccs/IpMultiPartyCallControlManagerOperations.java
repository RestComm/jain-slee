package org.csapi.cc.mpccs;

/**
 *	Generated from IDL interface "IpMultiPartyCallControlManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpMultiPartyCallControlManagerOperations
	extends org.csapi.IpServiceOperations
{
	/* constants */
	/* operations  */
	org.csapi.cc.mpccs.TpMultiPartyCallIdentifier createCall(org.csapi.cc.mpccs.IpAppMultiPartyCall appCall) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions;
	int createNotification(org.csapi.cc.mpccs.IpAppMultiPartyCallControlManager appCallControlManager, org.csapi.cc.TpCallNotificationRequest notificationRequest) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.P_INVALID_EVENT_TYPE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_CRITERIA;
	void destroyNotification(int assignmentID) throws org.csapi.P_INVALID_ASSIGNMENT_ID,org.csapi.TpCommonExceptions;
	void changeNotification(int assignmentID, org.csapi.cc.TpCallNotificationRequest notificationRequest) throws org.csapi.P_INVALID_ASSIGNMENT_ID,org.csapi.P_INVALID_EVENT_TYPE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_CRITERIA;
	org.csapi.cc.TpNotificationRequested[] getNotification() throws org.csapi.TpCommonExceptions;
	int setCallLoadControl(int duration, org.csapi.cc.TpCallLoadControlMechanism mechanism, org.csapi.cc.TpCallTreatment treatment, org.csapi.TpAddressRange addressRange) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_ADDRESS,org.csapi.P_UNSUPPORTED_ADDRESS_PLAN;
	int enableNotifications(org.csapi.cc.mpccs.IpAppMultiPartyCallControlManager appCallControlManager) throws org.csapi.TpCommonExceptions;
	void disableNotifications() throws org.csapi.TpCommonExceptions;
	org.csapi.cc.TpNotificationRequestedSetEntry getNextNotification(boolean reset) throws org.csapi.TpCommonExceptions;
}
