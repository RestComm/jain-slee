package org.csapi.gms;

/**
 *	Generated from IDL interface "IpMessagingManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpMessagingManagerOperations
	extends org.csapi.IpServiceOperations
{
	/* constants */
	/* operations  */
	org.csapi.gms.TpMailboxIdentifier openMailbox(org.csapi.TpAddress mailboxID, java.lang.String authenticationInfo) throws org.csapi.gms.P_GMS_INVALID_AUTHENTICATION_INFORMATION,org.csapi.gms.P_GMS_INVALID_MAILBOX,org.csapi.TpCommonExceptions;
	int enableMessagingNotification(org.csapi.gms.IpAppMessagingManager appInterface, org.csapi.gms.TpMessagingEventCriteria eventCriteria) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_CRITERIA;
	void disableMessagingNotification(int assignmentID) throws org.csapi.P_INVALID_ASSIGNMENT_ID,org.csapi.TpCommonExceptions;
}
