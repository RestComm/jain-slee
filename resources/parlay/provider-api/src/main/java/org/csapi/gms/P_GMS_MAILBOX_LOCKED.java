package org.csapi.gms;

/**
 *	Generated from IDL definition of exception "P_GMS_MAILBOX_LOCKED"
 *	@author JacORB IDL compiler 
 */

public final class P_GMS_MAILBOX_LOCKED
	extends org.omg.CORBA.UserException
{
	public P_GMS_MAILBOX_LOCKED()
	{
		super(org.csapi.gms.P_GMS_MAILBOX_LOCKEDHelper.id());
	}

	public java.lang.String ExtraInformation;
	public P_GMS_MAILBOX_LOCKED(java.lang.String _reason,java.lang.String ExtraInformation)
	{
		super(org.csapi.gms.P_GMS_MAILBOX_LOCKEDHelper.id()+""+_reason);
		this.ExtraInformation = ExtraInformation;
	}
	public P_GMS_MAILBOX_LOCKED(java.lang.String ExtraInformation)
	{
		this.ExtraInformation = ExtraInformation;
	}
}
