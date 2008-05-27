package org.csapi.gms;

/**
 *	Generated from IDL definition of struct "TpGMSNewMessageArrivedInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpGMSNewMessageArrivedInfo
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpGMSNewMessageArrivedInfo(){}
	public org.csapi.TpAddress MailboxID;
	public java.lang.String FolderID;
	public java.lang.String MessageID;
	public int NumberOfProperties;
	public TpGMSNewMessageArrivedInfo(org.csapi.TpAddress MailboxID, java.lang.String FolderID, java.lang.String MessageID, int NumberOfProperties)
	{
		this.MailboxID = MailboxID;
		this.FolderID = FolderID;
		this.MessageID = MessageID;
		this.NumberOfProperties = NumberOfProperties;
	}
}
