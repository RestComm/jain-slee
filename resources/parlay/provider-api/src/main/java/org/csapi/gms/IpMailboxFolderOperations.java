package org.csapi.gms;

/**
 *	Generated from IDL interface "IpMailboxFolder"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpMailboxFolderOperations
	extends org.csapi.IpServiceOperations
{
	/* constants */
	/* operations  */
	int getInfoAmount(int folderSessionID) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID;
	org.csapi.gms.TpFolderInfoProperty[] getInfoProperties(int folderSessionID, int firstProperty, int numberOfProperties) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID,org.csapi.gms.P_GMS_NUMBER_NOT_POSITIVE;
	void setInfoProperties(int folderSessionID, int firstProperty, org.csapi.gms.TpFolderInfoProperty[] folderInfoProperties) throws org.csapi.TpCommonExceptions,org.csapi.gms.P_GMS_PROPERTY_NOT_SET,org.csapi.P_INVALID_SESSION_ID;
	void putMessage(int folderSessionID, java.lang.String message, org.csapi.gms.TpMessageInfoProperty[] messageInfoProperties) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID;
	org.csapi.gms.IpMessage getMessage(int folderSessionID, java.lang.String messageID) throws org.csapi.TpCommonExceptions,org.csapi.gms.P_GMS_INVALID_MESSAGE_ID,org.csapi.P_INVALID_SESSION_ID;
	void close(int mailboxSessionID, int folderSessionID) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID;
	void remove(int mailboxSessionID, java.lang.String folderID) throws org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID,org.csapi.gms.P_GMS_INVALID_FOLDER_ID,org.csapi.gms.P_GMS_FOLDER_IS_OPEN,org.csapi.gms.P_GMS_INSUFFICIENT_PRIVILEGE;
}
