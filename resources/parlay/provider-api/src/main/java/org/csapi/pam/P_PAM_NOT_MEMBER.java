package org.csapi.pam;

/**
 *	Generated from IDL definition of exception "P_PAM_NOT_MEMBER"
 *	@author JacORB IDL compiler 
 */

public final class P_PAM_NOT_MEMBER
	extends org.omg.CORBA.UserException
{
	public P_PAM_NOT_MEMBER()
	{
		super(org.csapi.pam.P_PAM_NOT_MEMBERHelper.id());
	}

	public java.lang.String ExtraInformation;
	public P_PAM_NOT_MEMBER(java.lang.String _reason,java.lang.String ExtraInformation)
	{
		super(org.csapi.pam.P_PAM_NOT_MEMBERHelper.id()+""+_reason);
		this.ExtraInformation = ExtraInformation;
	}
	public P_PAM_NOT_MEMBER(java.lang.String ExtraInformation)
	{
		this.ExtraInformation = ExtraInformation;
	}
}
