package org.csapi.gms;

/**
 *	Generated from IDL interface "IpAppMessagingManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpAppMessagingManagerOperations
	extends org.csapi.IpInterfaceOperations
{
	/* constants */
	/* operations  */
	void mailboxTerminated(org.csapi.gms.IpMailbox mailbox, int mailboxSessionID);
	void mailboxFaultDetected(org.csapi.gms.IpMailbox mailbox, int mailboxSessionID, org.csapi.gms.TpMessagingFault fault);
	void messagingEventNotify(org.csapi.gms.IpMessagingManager messagingManager, org.csapi.gms.TpMessagingEventInfo eventInfo, int assignmentID);
	void messagingNotificationTerminated();
}
