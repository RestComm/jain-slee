package org.csapi.gms;

/**
 *	Generated from IDL definition of exception "P_GMS_CANNOT_UNLOCK_MAILBOX"
 *	@author JacORB IDL compiler 
 */

public final class P_GMS_CANNOT_UNLOCK_MAILBOX
	extends org.omg.CORBA.UserException
{
	public P_GMS_CANNOT_UNLOCK_MAILBOX()
	{
		super(org.csapi.gms.P_GMS_CANNOT_UNLOCK_MAILBOXHelper.id());
	}

	public java.lang.String ExtraInformation;
	public P_GMS_CANNOT_UNLOCK_MAILBOX(java.lang.String _reason,java.lang.String ExtraInformation)
	{
		super(org.csapi.gms.P_GMS_CANNOT_UNLOCK_MAILBOXHelper.id()+""+_reason);
		this.ExtraInformation = ExtraInformation;
	}
	public P_GMS_CANNOT_UNLOCK_MAILBOX(java.lang.String ExtraInformation)
	{
		this.ExtraInformation = ExtraInformation;
	}
}
