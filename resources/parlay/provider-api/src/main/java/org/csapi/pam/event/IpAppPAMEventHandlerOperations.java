package org.csapi.pam.event;

/**
 *	Generated from IDL interface "IpAppPAMEventHandler"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpAppPAMEventHandlerOperations
	extends org.csapi.IpInterfaceOperations
{
	/* constants */
	/* operations  */
	void eventNotify(int eventID, org.csapi.pam.TpPAMNotificationInfo[] eventInfo);
	void eventNotifyErr(int eventID, org.csapi.pam.TpPAMErrorInfo errorInfo);
}
