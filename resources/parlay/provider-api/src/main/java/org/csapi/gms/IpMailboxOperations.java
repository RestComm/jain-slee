package org.csapi.gms;

/**
 *	Generated from IDL interface "IpMailbox"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpMailboxOperations
	extends org.csapi.IpServiceOperations
{
	/* constants */
	/* operations  */
	void close(int mailboxSessionID) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID;
	void lock(int mailboxSessionID) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID,org.csapi.gms.P_GMS_LOCKING_LOCKED_MAILBOX;
	void unlock(int mailboxSessionID) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID,org.csapi.gms.P_GMS_UNLOCKING_UNLOCKED_MAILBOX,org.csapi.gms.P_GMS_CANNOT_UNLOCK_MAILBOX;
	int getInfoAmount(int mailboxSessionID) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID;
	org.csapi.gms.TpMailboxInfoProperty[] getInfoProperties(int mailboxSessionID, int firstProperty, int numberOfProperties) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID,org.csapi.gms.P_GMS_NUMBER_NOT_POSITIVE;
	void setInfoProperties(int mailboxSessionID, int firstProperty, org.csapi.gms.TpMailboxInfoProperty[] mailboxInfoProperties) throws org.csapi.TpCommonExceptions,org.csapi.gms.P_GMS_PROPERTY_NOT_SET,org.csapi.P_INVALID_SESSION_ID,org.csapi.gms.P_GMS_MAILBOX_LOCKED;
	org.csapi.gms.TpMailboxFolderIdentifier openFolder(int mailboxSessionID, java.lang.String folderID) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID,org.csapi.gms.P_GMS_INVALID_FOLDER_ID,org.csapi.gms.P_GMS_FOLDER_IS_OPEN,org.csapi.gms.P_GMS_MAILBOX_LOCKED;
	void createFolder(int mailboxSessionID, java.lang.String folderID) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID,org.csapi.gms.P_GMS_INVALID_FOLDER_ID,org.csapi.gms.P_GMS_MAILBOX_LOCKED;
	void remove(org.csapi.TpAddress mailboxID, java.lang.String authenticationInfo) throws org.csapi.gms.P_GMS_INVALID_AUTHENTICATION_INFORMATION,org.csapi.gms.P_GMS_INVALID_MAILBOX,org.csapi.TpCommonExceptions,org.csapi.gms.P_GMS_MAILBOX_OPEN,org.csapi.gms.P_GMS_MAILBOX_LOCKED,org.csapi.gms.P_GMS_INSUFFICIENT_PRIVILEGE;
}
