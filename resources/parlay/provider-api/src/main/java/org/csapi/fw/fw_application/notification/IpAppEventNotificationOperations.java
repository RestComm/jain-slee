package org.csapi.fw.fw_application.notification;

/**
 *	Generated from IDL interface "IpAppEventNotification"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpAppEventNotificationOperations
	extends org.csapi.IpInterfaceOperations
{
	/* constants */
	/* operations  */
	void reportNotification(org.csapi.fw.TpFwEventInfo eventInfo, int assignmentID);
	void notificationTerminated();
}
