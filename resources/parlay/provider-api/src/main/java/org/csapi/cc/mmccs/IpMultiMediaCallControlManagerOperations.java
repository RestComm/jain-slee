package org.csapi.cc.mmccs;

/**
 *	Generated from IDL interface "IpMultiMediaCallControlManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpMultiMediaCallControlManagerOperations
	extends org.csapi.cc.mpccs.IpMultiPartyCallControlManagerOperations
{
	/* constants */
	/* operations  */
	int createMediaNotification(org.csapi.cc.mmccs.IpAppMultiMediaCallControlManager appInterface, org.csapi.cc.mmccs.TpNotificationMediaRequest notificationMediaRequest) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.P_INVALID_EVENT_TYPE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_CRITERIA;
	void destroyMediaNotification(int assignmentID) throws org.csapi.TpCommonExceptions;
	void changeMediaNotification(int assignmentID, org.csapi.cc.mmccs.TpNotificationMediaRequest notificationMediaRequest) throws org.csapi.P_INVALID_ASSIGNMENT_ID,org.csapi.P_INVALID_EVENT_TYPE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_CRITERIA;
	org.csapi.cc.mmccs.TpMediaNotificationRequested[] getMediaNotification() throws org.csapi.TpCommonExceptions;
}
