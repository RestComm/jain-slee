package org.csapi.fw.fw_application.notification;

/**
 *	Generated from IDL interface "IpEventNotification"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpEventNotificationOperations
	extends org.csapi.IpInterfaceOperations
{
	/* constants */
	/* operations  */
	int createNotification(org.csapi.fw.TpFwEventCriteria eventCriteria) throws org.csapi.P_INVALID_EVENT_TYPE,org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED,org.csapi.P_INVALID_CRITERIA;
	void destroyNotification(int assignmentID) throws org.csapi.P_INVALID_ASSIGNMENT_ID,org.csapi.TpCommonExceptions,org.csapi.fw.P_ACCESS_DENIED;
}
