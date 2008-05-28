package org.csapi.gms;

/**
 *	Generated from IDL interface "IpMessage"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public interface IpMessageOperations
	extends org.csapi.IpServiceOperations
{
	/* constants */
	/* operations  */
	int getInfoAmount(int folderSessionID, java.lang.String messageID) throws org.csapi.TpCommonExceptions,org.csapi.gms.P_GMS_INVALID_MESSAGE_ID,org.csapi.P_INVALID_SESSION_ID;
	org.csapi.gms.TpMessageInfoProperty[] getInfoProperties(int folderSessionID, java.lang.String messageID, int firstProperty, int numberOfProperties) throws org.csapi.TpCommonExceptions,org.csapi.gms.P_GMS_INVALID_MESSAGE_ID,org.csapi.P_INVALID_SESSION_ID,org.csapi.gms.P_GMS_NUMBER_NOT_POSITIVE;
	void setInfoProperties(int folderSessionID, java.lang.String messageID, int firstProperty, org.csapi.gms.TpMessageInfoProperty[] messageInfoProperties) throws org.csapi.TpCommonExceptions,org.csapi.gms.P_GMS_PROPERTY_NOT_SET,org.csapi.gms.P_GMS_INVALID_MESSAGE_ID,org.csapi.P_INVALID_SESSION_ID;
	void remove(int folderSessionID, java.lang.String messageID) throws org.csapi.TpCommonExceptions,org.csapi.gms.P_GMS_INVALID_MESSAGE_ID,org.csapi.gms.P_GMS_MESSAGE_NOT_REMOVED,org.csapi.P_INVALID_SESSION_ID,org.csapi.gms.P_GMS_INSUFFICIENT_PRIVILEGE;
	java.lang.String getContent(int folderSessionID, java.lang.String messageID) throws org.csapi.TpCommonExceptions,org.csapi.gms.P_GMS_INVALID_MESSAGE_ID,org.csapi.P_INVALID_SESSION_ID;
}
