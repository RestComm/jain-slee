package org.csapi.gms;

/**
 *	Generated from IDL definition of struct "TpMailboxIdentifier"
 *	@author JacORB IDL compiler 
 */

public final class TpMailboxIdentifier
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpMailboxIdentifier(){}
	public org.csapi.gms.IpMailbox Mailbox;
	public int SessionID;
	public TpMailboxIdentifier(org.csapi.gms.IpMailbox Mailbox, int SessionID)
	{
		this.Mailbox = Mailbox;
		this.SessionID = SessionID;
	}
}
