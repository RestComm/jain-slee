package org.csapi.gms;

/**
 *	Generated from IDL definition of exception "P_GMS_UNLOCKING_UNLOCKED_MAILBOX"
 *	@author JacORB IDL compiler 
 */

public final class P_GMS_UNLOCKING_UNLOCKED_MAILBOX
	extends org.omg.CORBA.UserException
{
	public P_GMS_UNLOCKING_UNLOCKED_MAILBOX()
	{
		super(org.csapi.gms.P_GMS_UNLOCKING_UNLOCKED_MAILBOXHelper.id());
	}

	public java.lang.String ExtraInformation;
	public P_GMS_UNLOCKING_UNLOCKED_MAILBOX(java.lang.String _reason,java.lang.String ExtraInformation)
	{
		super(org.csapi.gms.P_GMS_UNLOCKING_UNLOCKED_MAILBOXHelper.id()+""+_reason);
		this.ExtraInformation = ExtraInformation;
	}
	public P_GMS_UNLOCKING_UNLOCKED_MAILBOX(java.lang.String ExtraInformation)
	{
		this.ExtraInformation = ExtraInformation;
	}
}
