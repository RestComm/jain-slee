package org.csapi.fw.fw_service.notification;

/**
 *	Generated from IDL interface "IpFwEventNotification"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpFwEventNotificationOperations
	extends org.csapi.IpInterfaceOperations
{
	/* constants */
	/* operations  */
	int createNotification(org.csapi.fw.TpFwEventCriteria eventCriteria) throws org.csapi.P_INVALID_EVENT_TYPE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_CRITERIA;
	void destroyNotification(int assignmentID) throws org.csapi.P_INVALID_ASSIGNMENT_ID,org.csapi.TpCommonExceptions;
}
