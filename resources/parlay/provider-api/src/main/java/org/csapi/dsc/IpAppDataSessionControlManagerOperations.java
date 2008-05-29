package org.csapi.dsc;

/**
 *	Generated from IDL interface "IpAppDataSessionControlManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpAppDataSessionControlManagerOperations
	extends org.csapi.IpInterfaceOperations
{
	/* constants */
	/* operations  */
	void dataSessionAborted(int dataSession);
	org.csapi.dsc.IpAppDataSession reportNotification(org.csapi.dsc.TpDataSessionIdentifier dataSessionReference, org.csapi.dsc.TpDataSessionEventInfo eventInfo, int assignmentID);
	void dataSessionNotificationContinued();
	void dataSessionNotificationInterrupted();
}
