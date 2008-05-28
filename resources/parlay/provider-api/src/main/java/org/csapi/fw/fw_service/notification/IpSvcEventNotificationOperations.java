package org.csapi.fw.fw_service.notification;

/**
 *	Generated from IDL interface "IpSvcEventNotification"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpSvcEventNotificationOperations
	extends org.csapi.IpInterfaceOperations
{
	/* constants */
	/* operations  */
	void reportNotification(org.csapi.fw.TpFwEventInfo eventInfo, int assignmentID) throws org.csapi.P_INVALID_ASSIGNMENT_ID,org.csapi.TpCommonExceptions;
	void notificationTerminated() throws org.csapi.TpCommonExceptions;
}
