package org.csapi.gms;

/**
 *	Generated from IDL definition of struct "TpMailboxFolderIdentifier"
 *	@author JacORB IDL compiler 
 */

public final class TpMailboxFolderIdentifier
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpMailboxFolderIdentifier(){}
	public org.csapi.gms.IpMailboxFolder MailboxFolder;
	public int SessionID;
	public TpMailboxFolderIdentifier(org.csapi.gms.IpMailboxFolder MailboxFolder, int SessionID)
	{
		this.MailboxFolder = MailboxFolder;
		this.SessionID = SessionID;
	}
}
