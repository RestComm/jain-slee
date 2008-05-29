package org.csapi.pam;

/**
 *	Generated from IDL definition of exception "P_PAM_MEMBER_EXISTS"
 *	@author JacORB IDL compiler 
 */

public final class P_PAM_MEMBER_EXISTS
	extends org.omg.CORBA.UserException
{
	public P_PAM_MEMBER_EXISTS()
	{
		super(org.csapi.pam.P_PAM_MEMBER_EXISTSHelper.id());
	}

	public java.lang.String ExtraInformation;
	public P_PAM_MEMBER_EXISTS(java.lang.String _reason,java.lang.String ExtraInformation)
	{
		super(org.csapi.pam.P_PAM_MEMBER_EXISTSHelper.id()+""+_reason);
		this.ExtraInformation = ExtraInformation;
	}
	public P_PAM_MEMBER_EXISTS(java.lang.String ExtraInformation)
	{
		this.ExtraInformation = ExtraInformation;
	}
}
